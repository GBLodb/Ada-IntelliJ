package com.adacore.adaintellij.build;

import com.adacore.adaintellij.project.AdaProjectService;
import com.intellij.execution.RunManagerListener;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.impl.RunManagerImpl;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Project component handling GPRbuild configuration management.
 */
public final class GPRbuildConfigurationManagerService {

	/**
	 * The empty list of GPRbuild configurations.
	 */
	private static final List<GPRbuildConfiguration>
		EMPTY_CONFIGURATION_LIST = Collections.emptyList();

	/**
	 * The corresponding Ada project component.
	 */
	AdaProjectService adaProjectService;

	/**
	 * Project run manager used to manage GPRbuild run configurations.
	 */
	RunManagerImpl runManager;

	/**
	 * Constructs a new GPRbuildConfigurationManager.
	 *
	 * @param project The project to attach to the constructed manager.
	 * @param adaProjectService The Ada project component to attach to the
	 *                   constructed manager.
	 */
	public GPRbuildConfigurationManagerService(Project project, AdaProjectService adaProjectService) {
		this.adaProjectService = adaProjectService;
		this.runManager = RunManagerImpl.getInstanceImpl(project);
	}

	/**
	 * Returns the GPRbuildConfigurationManager project component of the given project.
	 *
	 * @param project The project for which to get the component.
	 * @return The project component.
	 */
	@NotNull
	public static GPRbuildConfigurationManagerService getInstance(@NotNull Project project) {
		return project.getComponent(GPRbuildConfigurationManagerService.class);
	}

	/**
	 * Adds the given listener to the run manager.
	 *
	 * @param listener The listener to add.
	 */
	public void addRunManagerListener(@NotNull RunManagerListener listener) {

		if (!adaProjectService.isAdaProject()) { return; }

		runManager.addRunManagerListener(listener);

	}

	/**
	 * Returns the list of all registered GPRbuild configurations.
	 *
	 * @return The list of GPRbuild configurations.
	 */
	@NotNull
	public List<GPRbuildConfiguration> getAllConfigurations() {
		return !adaProjectService.isAdaProject() ? EMPTY_CONFIGURATION_LIST :
			runManager.getConfigurationsList(GPRbuildConfigurationType.INSTANCE)
				.stream()
				.map(runConfiguration -> (GPRbuildConfiguration)runConfiguration)
				.collect(Collectors.toList());
	}

	/**
	 * Returns the currently selected GPRbuild configuration, or null
	 * if no configuration is selected or if the selected configuration
	 * is not a GPRbuild configuration.
	 *
	 * @return The currently selected GPRbuild configuration, or null if
	 *         no such configuration is selected.
	 */
	@Nullable
	public GPRbuildConfiguration getSelectedConfiguration() {

		if (!adaProjectService.isAdaProject()) { return null; }

		RunnerAndConfigurationSettings settings =
			runManager.getSelectedConfiguration();

		if (settings == null) { return null; }

		RunConfiguration configuration = settings.getConfiguration();

		return configuration instanceof GPRbuildConfiguration ?
			(GPRbuildConfiguration)configuration : null;

	}

	/**
	 * Sets the selected configuration to the given GPRbuild configuration.
	 *
	 * @param configuration The GPRbuild configuration to select.
	 */
	void setSelectedConfiguration(@NotNull GPRbuildConfiguration configuration) {

		if (!adaProjectService.isAdaProject()) { return; }

		runManager.getAllSettings()
			.stream()
			.filter(settings -> settings.getConfiguration().equals(configuration))
			.findFirst()
			.ifPresent(runManager::setSelectedConfiguration);

	}

}
