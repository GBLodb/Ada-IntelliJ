package com.adacore.adaintellij.file;

import com.adacore.adaintellij.AdaLanguage;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Any file type specific to the Ada language.
 */
public abstract class AdaFileType extends LanguageFileType {

	/**
	 * Constructs a new Ada-specific file type.
	 */
	AdaFileType() { super(AdaLanguage.INSTANCE); }

	/**
	 * Returns whether the given file is an Ada source file.
	 *
	 * @param file The file to test.
	 * @return Whether the given file is an Ada source file.
	 */
	public static boolean isAdaFile(VirtualFile file) {
		return file.getFileType() instanceof AdaFileType;
	}

}
