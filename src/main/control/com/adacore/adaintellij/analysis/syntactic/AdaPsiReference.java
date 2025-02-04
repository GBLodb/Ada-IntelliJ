package com.adacore.adaintellij.analysis.syntactic;

import com.adacore.adaintellij.lsp.AdaLSPDriverService;
import com.adacore.adaintellij.lsp.AdaLSPServer;
import com.adacore.adaintellij.lsp.LSPUtils;
import com.adacore.adaintellij.misc.cache.CacheKey;
import com.adacore.adaintellij.misc.cache.CacheResult;
import com.adacore.adaintellij.misc.cache.Cacher;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.eclipse.lsp4j.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.adacore.adaintellij.Utils.*;

/**
 * Ada AST node representing an element that can reference other elements.
 * Typically, such an element is an Ada identifier. By nature, an element
 * of this class always has a name and a corresponding element identifying
 * it, that element being itself, which is why this class implements
 * `PsiNameIdentifierOwner` (which in turn extends `PsiNamedElement`).
 * <p>
 * For detailed information about the structure of ASTs built by the
 * Ada-IntelliJ Ada parser:
 * @see AdaParser
 */
public final class AdaPsiReference extends AdaPsiElement
	implements PsiReference, PsiNameIdentifierOwner, Cacher
{

	/**
	 * Cash key for cashed resolved elements of Ada PSI references.
	 */
	private static final CacheKey<AdaPsiElement>
		RESOLVED_ELEMENT_CACHE_KEY = CacheKey.getNewKey();

	/**
	 * The underlying tree node.
	 */
	private final ASTNode node;

	/**
	 * Constructs a new AdaPsiReference given a tree node.
	 *
	 * @param node The tree node to back the constructed
	 *             PSI reference.
	 */
	AdaPsiReference(@NotNull ASTNode node) {
		super(node);
		this.node = node;
	}

	/**
	 * @see com.intellij.psi.PsiNamedElement#getName()
	 */
	@NotNull
	@Override
	public String getName() { return node.getText(); }

	/**
	 * @see com.intellij.psi.PsiNamedElement#setName(String)
	 */
	@Override
	public PsiElement setName(@NotNull String name) throws IncorrectOperationException {

		PsiFile     psiFile     = getContainingFile();
		VirtualFile virtualFile = psiFile.getVirtualFile();
		Document    document    = getVirtualFileDocument(virtualFile);

		if (document == null) { return this; }

		// Get this reference's start offset

		int startOffset = getStartOffset();

		// Use this element's start/end offsets to replace its
		// corresponding text in the document with that of the
		// given name

		document.replaceString(startOffset, startOffset + getTextLength(), name);

		// Commit the document change for the file to be reparsed

		PsiDocumentManager.getInstance(getProject()).commitDocument(document);

		// Find the new element at the same start offset in the
		// modified file and return it

		return psiFile.findElementAt(startOffset);

	}

	/**
	 * @see com.intellij.psi.PsiNameIdentifierOwner#getNameIdentifier()
	 */
	@NotNull
	@Override
	public PsiElement getNameIdentifier() { return this; }

	/**
	 * @see com.intellij.psi.PsiElement#getReferences()
	 */
	@Override
	@NotNull
	public PsiReference[] getReferences() { return new PsiReference[] { this }; }

	/**
	 * @see com.intellij.psi.PsiReference#getElement()
	 */
	@NotNull
	@Override
	public PsiElement getElement() { return this; }

	/**
	 * @see com.intellij.psi.PsiReference#getRangeInElement()
	 */
	@NotNull
	@Override
	public TextRange getRangeInElement() { return new TextRange(0, node.getTextLength()); }

	/**
	 * @see com.intellij.psi.PsiReference#resolve()
	 *
	 * The IntelliJ platform expects that this method return null if
	 * this reference represents a declaration and does not resolve to
	 * a different element than itself, hence the separation between
	 * this method and `resolveAdaReference`.
	 */
	@Nullable
	@Override
	public AdaPsiElement resolve() {

		AdaPsiElement definition = resolveAdaReference();

		return AdaPsiElement.areEqual(definition, this) ? null : definition;

	}

	/**
	 * Returns the element to which this reference resolves.
	 * Instead of performing any sort of name resolution, this method
	 * makes a `textDocument/definition` request to the ALS to get the
	 * element referenced by this element and returns it, or null if no
	 * such element was found or if something went wrong.
	 *
	 * @return The element to which this reference resolves, or null
	 *         if no such element is found.
	 */
	@Nullable
	public AdaPsiElement resolveAdaReference() {

		// Check if the resolved element is cached
		// and if it is, then return it

		CacheResult<AdaPsiElement> cacheResult =
			getCachedData(RESOLVED_ELEMENT_CACHE_KEY);

		if (cacheResult.hit) { return cacheResult.data; }

		// Get the document of the containing file

		PsiFile  containingFile = getContainingFile();
		Document document       = getPsiFileDocument(containingFile);

		if (document == null) { return null; }

		String documentUri = containingFile.getVirtualFile().getUrl();

		// If this reference belongs to a mock file, such as the one
		// generated by the PSI structure viewer, then return null

		if (documentUri.startsWith("mock://")) { return null; }

		// Make the request and wait for the result

		AdaLSPServer lspServer = AdaLSPDriverService.getServer(getProject());

		if (lspServer == null) { return null; }

		Location definitionLocation = lspServer.definition(
			documentUri, LSPUtils.offsetToPosition(document, getStartOffset()));

		// If no valid result was returned, cash the result
		// (no resolved element) and return null

		if (definitionLocation == null) {
			cacheData(RESOLVED_ELEMENT_CACHE_KEY, null);
			return null;
		}

		// Get the definition's file

		VirtualFile definitionVirtualFile =
			findFileByUrlString(definitionLocation.getUri());

		if (definitionVirtualFile == null) { return null; }

		PsiFile  definitionPsiFile  = getVirtualFilePsiFile(getProject(), definitionVirtualFile);
		Document definitionDocument = getVirtualFileDocument(definitionVirtualFile);

		if (definitionPsiFile == null || definitionDocument == null) { return null; }

		// Find the element at the given position in the file

		PsiElement definition = definitionPsiFile.findElementAt(
			LSPUtils.positionToOffset(
				definitionDocument,
				definitionLocation.getRange().getStart()
			)
		);

		AdaPsiElement adaDefinition = definition == null ?
			null : AdaPsiElement.getFrom(definition);

		// If the element was found, then cash it

		if (adaDefinition != null) {
			cacheData(RESOLVED_ELEMENT_CACHE_KEY, adaDefinition);
		}

		// Return the element (or null if it was not found)

		return adaDefinition;

	}

	/**
	 * @see com.intellij.psi.PsiReference#getCanonicalText()
	 */
	@NotNull
	@Override
	public String getCanonicalText() {
		// TODO: Return proper canonical text here
		return node.getText();
	}

	/**
	 * @see com.intellij.psi.PsiReference#handleElementRename(String)
	 */
	@Override
	public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
		return setName(newElementName);
	}

	/**
	 * @see com.intellij.psi.PsiReference#bindToElement(PsiElement)
	 */
	@Override
	public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
		throw new IncorrectOperationException("Bind not yet supported");
	}

	/**
	 * @see com.intellij.psi.PsiReference#isReferenceTo(PsiElement)
	 */
	@Override
	public boolean isReferenceTo(@NotNull PsiElement element) {
		return getText().equalsIgnoreCase(element.getText()) &&
			AdaPsiElement.areEqual(resolveAdaReference(), element);
	}

	/**
	 * @see com.intellij.psi.PsiReference#getVariants()
	 */
	@NotNull
	@Override
	public Object[] getVariants() { return new Object[0]; }

	/**
	 * @see com.intellij.psi.PsiReference#isSoft()
	 */
	@Override
	public boolean isSoft() { return false; }

	/**
	 * @see com.intellij.pom.Navigatable#canNavigate()
	 */
	@Override
	public boolean canNavigate() { return true; }

	/**
	 * Returns whether this element references itself,
	 * i.e. if it is a declaration of any kind.
	 *
	 * @return Whether this element is a declaration.
	 */
	public boolean isDeclaration() { return isReferenceTo(this); }

	/**
	 * Returns a string representation of this PSI reference.
	 *
	 * @return A string representation of this PSI reference.
	 */
	@Override
	public String toString() {
		return "AdaPsiReference(" + getElementType() + ")";
	}

}
