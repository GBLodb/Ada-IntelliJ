package com.adacore.adaintellij.analysis.syntactic;

import com.adacore.adaintellij.AdaLanguage;
import com.intellij.psi.tree.IFileElementType;

/**
 * Element type representing an Ada file.
 */
public final class AdaFileElementType extends IFileElementType {

	/**
	 * Unique instance representing the Ada file element type.
	 */
	public static final AdaFileElementType INSTANCE = new AdaFileElementType();

	/**
	 * Constructs a new instance of the Ada file element type.
	 */
	private AdaFileElementType() {
		super("Ada.FILE", AdaLanguage.INSTANCE);
	}

}
