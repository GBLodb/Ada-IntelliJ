package com.adacore.adaintellij.build;

import com.adacore.adaintellij.AdaIntelliJUI;
import com.intellij.execution.RunManagerListener;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ItemEvent;

/**
 * Simple UI view with a combo-box containing GPRbuild configurations.
 */
public final class GPRbuildConfigurationComboBoxView extends AdaIntelliJUI {

	/**
	 * Root UI component.
	 */
	private JPanel rootPanel;

	/**
	 * Child UI components.
	 */
	private JComboBox<RunConfiguration> comboBox;

	/**
	 * The GPRbuild configuration manager project component of this
	 * view's project.
	 */
	private final GPRbuildConfigurationManagerService gprbuildConfigurationManagerService;

	/**
	 * Constructs a new GPRbuildConfigurationComboBoxView given a project.
	 *
	 * @param project The project to which this view belongs.
	 */
	GPRbuildConfigurationComboBoxView(@NotNull Project project) {
		this(project, null);
	}

	/**
	 * Constructs a new GPRbuildConfigurationComboBoxView given a project
	 * and an optional parent UI.
	 *
	 * @param project The project to which this view belongs.
	 * @param parentUI The UI in which to embed the constructed UI, or null
	 *                 to construct a standalone UI view.
	 */
	GPRbuildConfigurationComboBoxView(@NotNull Project project, @Nullable AdaIntelliJUI parentUI) {

		super(parentUI);

		// Get the project's GPRbuild configuration manager and
		// add a listener to update combo-box selected configuration
		// on global configuration selection change

		gprbuildConfigurationManagerService = GPRbuildConfigurationManagerService.getInstance(project);

		gprbuildConfigurationManagerService.addRunManagerListener(new RunManagerListener() {

			/**
			 * Called when a different configuration is selected.
			 */
			@Override
			public void runConfigurationSelected(RunnerAndConfigurationSettings settings) {

				GPRbuildConfiguration configuration = gprbuildConfigurationManagerService.getSelectedConfiguration();

				if (configuration == null) return;

				comboBox.setSelectedItem(configuration);

			}

			/**
			 * Called when a new configuration is added.
			 *
			 * @param settings The new configuration's settings.
			 */
			@Override
			public void runConfigurationAdded(@NotNull RunnerAndConfigurationSettings settings) {
				reloadComboBox();
			}

			/**
			 * Called when a configuration is removed.
			 *
			 * @param settings The removed configuration's settings.
			 */
			@Override
			public void runConfigurationRemoved(@NotNull RunnerAndConfigurationSettings settings) {
				reloadComboBox();
			}

			/**
			 * Called when a configuration is changed.
			 *
			 * @param settings The changed configuration's settings.
			 */
			@Override
			public void runConfigurationChanged(@NotNull RunnerAndConfigurationSettings settings) {
				reloadComboBox();
			}

		});

		// Add a listener to the combo-box to update globally selected
		// configuration on combo-box selection change

		comboBox.addItemListener(itemEvent -> {

			if (itemEvent.getStateChange() != ItemEvent.SELECTED) { return; }

			gprbuildConfigurationManagerService.setSelectedConfiguration(
				(GPRbuildConfiguration)itemEvent.getItem());

		});

		// Reload the combo-box for the first time

		reloadComboBox();

	}

	/**
	 * @see com.adacore.adaintellij.AdaIntelliJUI#getUIRoot()
	 */
	@NotNull
	@Override
	public JComponent getUIRoot() { return rootPanel; }

	/**
	 * Fetches all GPRbuild configurations and reloads the combo-box.
	 */
	private void reloadComboBox() {

		// Set up the combo-box data

		DefaultComboBoxModel<RunConfiguration> model = new DefaultComboBoxModel<>();

		gprbuildConfigurationManagerService.getAllConfigurations().forEach(model::addElement);

		comboBox.setModel(model);

		// Set selected configuration

		GPRbuildConfiguration configuration =
			gprbuildConfigurationManagerService.getSelectedConfiguration();

		if (configuration == null) { return; }

		comboBox.setSelectedItem(configuration);

	}

}
