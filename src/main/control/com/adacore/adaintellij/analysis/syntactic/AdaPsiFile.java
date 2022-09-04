package com.adacore.adaintellij.analysis.syntactic;

import com.adacore.adaintellij.AdaLanguage;
import com.adacore.adaintellij.analysis.lexical.AdaTokenTypes;
import com.adacore.adaintellij.misc.cache.Markable;
import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.util.FileContentUtilCore;
import org.jetbrains.annotations.NotNull;

/**
 * Representation of an Ada file, acting as the root node in the AST
 * structure used by the IntelliJ platform.
 * <p>
 * For detailed information about the structure of ASTs built by the
 * Ada-IntelliJ Ada parser:
 * @see AdaParser
 */
public final class AdaPsiFile extends PsiFileBase implements Markable {

	/**
	 * The file view provider corresponding to this Ada file.
	 */
	private final FileViewProvider viewProvider;

	/**
	 * Constructs a new AdaPsiFile given a file view provider.
	 *
	 * @param viewProvider The file view provider of the
	 *                     constructed AdaPsiFile.
	 */
	AdaPsiFile(@NotNull FileViewProvider viewProvider) {
		super(viewProvider, AdaLanguage.INSTANCE);
		this.viewProvider = viewProvider;
	}

	/**
	 * @see com.intellij.psi.PsiFile#getFileType()
	 */
	@NotNull
	@Override
	public FileType getFileType() { return viewProvider.getFileType(); }

	/**
	 * @see com.intellij.psi.PsiFile#subtreeChanged()
	 */
	@Override
	public void subtreeChanged() {

		super.subtreeChanged();

		// Reparse the file

		VirtualFile file = getVirtualFile();

		if (file != null) {
			FileContentUtilCore.reparseFiles(file);
		}

	}

	/**
	 * @see com.intellij.psi.PsiElement#getChildren()
	 */
	@NotNull
	@Override
	public PsiElement[] getChildren() {
		return calcTreeElement().getChildrenAsPsiElements(
			AdaTokenTypes.ALL_VALID_TOKENS, PsiElement.ARRAY_FACTORY);
	}

}
