package com.adacore.adaintellij.build;

import com.adacore.adaintellij.notifications.AdaIJNotification;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;

import java.util.List;

public class GPRbuildConfigurationManagerListener implements ProjectManagerListener {

    /**
     * @see com.intellij.openapi.project.ProjectManagerListener#projectClosed(Project)
     *
     * Checks the run manager for GPRbuild run configurations, and if no
     * configurations are found, creates a default one.
     */
    @Override
    public void projectOpened(Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }

        GPRbuildConfigurationManagerService gpRbuildConfigurationManagerService = ApplicationManager.getApplication()
                .getService(GPRbuildConfigurationManagerService.class);

        if (!gpRbuildConfigurationManagerService.adaProjectService.isAdaProject()) { return; }

        // Get the list of GPRbuild run configurations

        List<RunConfiguration> configurations =
                gpRbuildConfigurationManagerService.runManager.getConfigurationsList(GPRbuildConfigurationType.INSTANCE);

        // If no configurations were found, create a default GPRbuild run
        // configuration and select it

        if (configurations.size() == 0) {

            RunnerAndConfigurationSettings settings =
                    gpRbuildConfigurationManagerService.runManager.createConfiguration("Default GPRbuild Configuration",
                            GPRbuildConfigurationType.INSTANCE.getConfigurationFactories()[0]);

            gpRbuildConfigurationManagerService.runManager.addConfiguration(settings);
            gpRbuildConfigurationManagerService.runManager.setSelectedConfiguration(settings);

            // Notify the user that a default gprbuild configuration was created
            Notifications.Bus.notify(new AdaIJNotification(
                    "No gprbuild configurations detected",
                    "A default gprbuild configuration was created and" +
                            " can be edited in `Run | Edit Configurations...`.",
                    NotificationType.INFORMATION
            ));

        }

    }
}
