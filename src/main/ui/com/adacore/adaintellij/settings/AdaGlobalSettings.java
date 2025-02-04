package com.adacore.adaintellij.settings;

import com.adacore.adaintellij.UIUtils;
import com.adacore.adaintellij.build.GPRbuildManagerService;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Global IDE settings UI for Ada development.
 */
public final class AdaGlobalSettings implements ValidatableConfigurable {

	/**
	 * Root UI component.
	 */
	private JPanel rootPanel;

	/**
	 * Child UI components.
	 */
	private TextFieldWithBrowseButton gprbuildPathField;

	/**
	 * Last set values.
	 */
	private String lastSetGprbuildPath;

	/**
	 * @see com.intellij.openapi.options.Configurable#getDisplayName()
	 */
	@Nls(capitalization = Nls.Capitalization.Title)
	@Override
	public String getDisplayName() { return "Ada"; }

	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#createComponent()
	 */
	@Nullable
	@Override
	public JComponent createComponent() {

		// Set up the UI

		gprbuildPathField.addBrowseFolderListener(
			new TextBrowseFolderListener(UIUtils.SINGLE_FILE_CHOOSER_DESCRIPTOR));

		// Return the root panel

		return rootPanel;

	}

	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#isModified()
	 */
	@Override
	public boolean isModified() {
		return !gprbuildPathField.getText().equals(lastSetGprbuildPath);
	}

	/**
	 * @see com.adacore.adaintellij.settings.ValidatableConfigurable#applyAfterValidation()
	 */
	@Override
	public void applyAfterValidation() {

		String path = gprbuildPathField.getText();

		GPRbuildManagerService.setGprBuildPath(path);
		lastSetGprbuildPath = path;

	}

	/**
	 * @see com.intellij.openapi.options.UnnamedConfigurable#reset()
	 */
	@Override
	public void reset() {

		String gprbuildPath = GPRbuildManagerService.getGprbuildPath();

		gprbuildPathField.setText(gprbuildPath);
		lastSetGprbuildPath = gprbuildPath;

	}

}
