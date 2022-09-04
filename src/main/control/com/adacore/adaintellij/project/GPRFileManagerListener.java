package com.adacore.adaintellij.project;

import com.adacore.adaintellij.notifications.AdaIJNotification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;

public class GPRFileManagerListener implements ProjectManagerListener {

    /**
     * @see com.intellij.openapi.project.ProjectManagerListener#projectOpened(Project)
     */
    @Override
    public void projectOpened(Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }

        GPRFileManagerService gprFileManagerService = ApplicationManager.getApplication().getService(GPRFileManagerService.class);

        if (!gprFileManagerService.adaProjectService.isAdaProject()) { return; }

        gprFileManagerService.setGprFilePath(gprFileManagerService.getGprFilePathOrChoose());

        if (!"".equals(gprFileManagerService.gprFilePath) && !gprFileManagerService.notifiedAboutGprFile) {

            Notifications.Bus.notify(new AdaIJNotification(
                    "Project File",
                    "Using the following project file:\n" + gprFileManagerService.gprFilePath,
                    NotificationType.INFORMATION
            ));

            gprFileManagerService.notifiedAboutGprFile = true;

        }

    }
}
