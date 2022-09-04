package com.adacore.adaintellij.project;

import com.adacore.adaintellij.file.GPRFileType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VfsUtil;

import java.util.Objects;

public class AdaProjectListener implements ProjectManagerListener {
    /**
     * @see com.intellij.openapi.project.ProjectManagerListener#projectOpened(Project)
     *
     * Checks if the project is an Ada project. Currently, a project is
     * considered to be an Ada project if it contains at least one GPR file
     * in its file hierarchy.
     */
    @Override
    public void projectOpened(Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }

        AdaProjectService adaProjectService = ApplicationManager.getApplication().getService(AdaProjectService.class);

        final String gprFileExtension = GPRFileType.INSTANCE.getDefaultExtension();

        VfsUtil.iterateChildrenRecursively(
                Objects.requireNonNull(ProjectUtil.guessProjectDir(project)),
                null,
                fileOrDir -> {

                    if (
                            fileOrDir.isValid() &&
                                    !fileOrDir.isDirectory() &&
                                    gprFileExtension.equals(fileOrDir.getExtension())
                    ) {
                        adaProjectService.isAdaProject = true;
                        return false;
                    }

                    return true;

                }
        );

        if (adaProjectService.isAdaProject) {

            // Refresh the project

            Objects.requireNonNull(ProjectUtil.guessProjectDir(project)).refresh(false, true);

        }

    }
}
