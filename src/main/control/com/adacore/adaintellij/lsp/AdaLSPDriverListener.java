package com.adacore.adaintellij.lsp;

import com.adacore.adaintellij.notifications.AdaIJNotification;
import com.intellij.execution.RunManagerListener;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManagerListener;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageServer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static com.adacore.adaintellij.Utils.getPathFromSystemPath;

/**
 * Driver handling the LSP session between Ada-IntelliJ's
 * integrated client and the Ada Language Server (ALS).
 * <p>
 * The Ada-IntelliJ LSP integration is up-to-date with
 * protocol version 3.13.0.
 */
public final class AdaLSPDriverListener implements ProjectManagerListener {
	/*
	    Project Component Open/Close Handlers
	*/
    @Override
    public void projectOpened(@NotNull Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }

        AdaLSPDriverService adaLSPDriverService = ApplicationManager.getApplication().getService(AdaLSPDriverService.class);

        if (!adaLSPDriverService.adaProjectService.isAdaProject()) { return; }

        // Get ALS path

        String alsPath = getPathFromSystemPath(LSPUtils.ALS_NAME, false);

        // If the ALS is not found on the PATH, then notify the
        // user to install the ALS and add it to their PATH

        if (alsPath == null) {

            Notifications.Bus.notify(new AdaIJNotification(
                    "Ada Language Server not found on PATH",
                    "In order to provide semantic features and smart assistance for Ada, the " +
                            "Ada-IntelliJ plugin relies heavily on the Ada Language Server (ALS). " +
                            "To enable these features, you need download and install the ALS, add " +
                            "it to your PATH, then reload open projects for the effect to take place.\n" +
                            "ALS binaries can be found here:\n" +
                            "https://bintray.com/beta/#/reznikmm/ada-language-server/ada-language-server?tab=files",
                    NotificationType.WARNING
            ));

            return;

        }

        Process process;

        try {

            // Try to start the server process

            process = new ProcessBuilder(alsPath).start();

        } catch (IOException exception) {

            // Notify the user that the server could not be started

            Notifications.Bus.notify(new AdaIJNotification(
                    "Failed to start Ada Language Server",
                    "Reload the current project to try again.",
                    NotificationType.ERROR
            ));

            return;

        }

        // Connect to the server process' input/output

        adaLSPDriverService.client = new AdaLSPClient(adaLSPDriverService, project);

        Launcher<LanguageServer> serverLauncher = LSPLauncher.createClientLauncher(
                adaLSPDriverService.client, process.getInputStream(), process.getOutputStream());

        adaLSPDriverService.server = new AdaLSPServer(adaLSPDriverService, serverLauncher.getRemoteProxy());

        serverLauncher.startListening();

        // Send the `initialize` request to initialize the server

        InitializeResult result = adaLSPDriverService.server.initialize(adaLSPDriverService.getInitParams());

        if (result == null) {

            // Notify the user that the initialization failed

            Notifications.Bus.notify(new AdaIJNotification(
                    "Failed to initialize Ada Language Server",
                    "Reload the current project to try again.",
                    NotificationType.ERROR
            ));

            return;

        }

        adaLSPDriverService.server.setCapabilities(result.getCapabilities());

        adaLSPDriverService.server.initialized(new InitializedParams());

        // Try to set up the LSP server with the project's GPR file path
        // This may not complete in case no GPR files exist in the project
        // or in case multiple ones exist and the user has not chosen one,
        // in which case the server will not be marked as initialized and
        // no requests will be made to it until a GPR file path is set and
        // successfully communicated to the server

        adaLSPDriverService.setConfiguration();

        // Add a GPR file change listener and a GPRbuild configuration
        // selection/change listener in order to send workspace
        // configuration changes to the server
        adaLSPDriverService.gprFileManagerService.addGprFileChangeListener(
                AdaLSPDriverService.GPR_FILE_CHANGE_LISTENER_KEY, adaLSPDriverService::setConfiguration);

        adaLSPDriverService.gprbuildConfigurationManagerService.addRunManagerListener(new RunManagerListener() {

            /**
             * Called when a configuration is changed.
             *
             * @param settings The changed configuration's settings.
             */
            @Override
            public void runConfigurationChanged(@NotNull RunnerAndConfigurationSettings settings) {
                adaLSPDriverService.setConfiguration();
            }

            /**
             * Called when a different configuration is selected.
             */
            @Override
            public void runConfigurationSelected() {
                adaLSPDriverService.setConfiguration();
            }

        });

    }

    /**
     * @see com.intellij.openapi.project.ProjectManagerListener#projectClosed(Project)
     */
    @Override
    public void projectClosed(@NotNull Project project) {
        if (ApplicationManager.getApplication().isUnitTestMode()) {
            return;
        }

        AdaLSPDriverService adaLSPDriverService = ApplicationManager.getApplication().getService(AdaLSPDriverService.class);

        if (!adaLSPDriverService.adaProjectService.isAdaProject()) { return; }

        // Shut down the server
        adaLSPDriverService.shutDownServer();

    }

}
