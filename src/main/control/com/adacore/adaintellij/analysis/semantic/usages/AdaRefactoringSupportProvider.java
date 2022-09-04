package com.adacore.adaintellij.analysis.semantic.usages;

import com.intellij.lang.refactoring.RefactoringSupportProvider;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Refactoring support provider for Ada elements.
 */
public class AdaRefactoringSupportProvider extends RefactoringSupportProvider {

	/**
	 * Returns whether the given element, in the context of
	 * the second given element, supports being renamed in-place by
	 * a `MemberInplaceRenameHandler`.
	 *
	 * @param element The element to check for support.
	 * @param context The context element.
	 * @return Whether the element supports renaming by a
	 *         `MemberInplaceRenameHandler`.
	 */
	@Override
	public boolean isMemberInplaceRenameAvailable(
		@NotNull  PsiElement element,
		@Nullable PsiElement context
	) { return AdaRenamePsiElementProcessor.canRenameElement(element); }

}
