package com.adacore.adaintellij.project;

import com.adacore.adaintellij.dialogs.ListChooserDialog;
import com.adacore.adaintellij.file.GPRFileType;
import com.adacore.adaintellij.notifications.AdaIJNotification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.openapi.vfs.VfsUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;
import java.util.function.Consumer;

/**
 * Project component handling everything related to GPR files.
 */
public class GPRFileManagerService {

	/**
	 * The project to which this component belongs.
	 */
	Project project;

	/**
	 * The corresponding Ada project component.
	 */
	AdaProjectService adaProjectService;

	/**
	 * The path to the GPR file configured for the project
	 * to which this manager component belongs.
	 */
	String gprFilePath = "";

	/**
	 * The set of registered listeners for changes of the GPR file path.
	 * Every listener is identified by a unique string key that can be used
	 * to register/unregister the listener (see `addGprFileChangeListener`
	 * and `removeGprFileChangeListener`).
	 * To avoid collisions, all listeners registered from within a class
	 * should use a key prefixed by the fully qualified class name of that
	 * class, for example:
	 * `com.adacore.adaintellij.project.GPRFileManager@myGprFileChangeListener`
	 * Moreover, every class should assign different names to its own keys
	 * to avoid collisions.
	 */
	Map<String, Consumer<String>> gprFileChangeListeners = new HashMap<>();

	/**
	 * Whether the user has already been notified about the selected
	 * GPR file, or about the fact that no such file was found in the project
	 * structure.
	 */
	boolean notifiedAboutGprFile = false;

	/**
	 * Constructs a new GPRFileManager given a project.
	 *
	 * @param project The project to attach to the constructed manager.
	 * @param adaProjectService The Ada project component to attach to the
	 *                   constructed manager.
	 */
	public GPRFileManagerService(Project project, AdaProjectService adaProjectService) {
		this.project    = project;
		this.adaProjectService = adaProjectService;
	}

	/**
	 * Returns the GPRFileManager project component of the given project.
	 *
	 * @param project The project for which to get the component.
	 * @return The project component.
	 */
	@NotNull
	public static GPRFileManagerService getInstance(@NotNull Project project) {
		return project.getComponent(GPRFileManagerService.class);
	}

	/**
	 * Returns the path to the GPR file of this manager's project.
	 * The returned path may be the empty string in case the path
	 * is not set.
	 *
	 * @return The configured GPR file path.
	 */
	@NotNull
	public String getGprFilePath() { return gprFilePath; }

	/**
	 * Sets the GPR file path to the given path and notifies all
	 * GPR file path change listeners.
	 *
	 * @param path The new GPR file path.
	 */
	public void setGprFilePath(@NotNull final String path) {

		if (!adaProjectService.isAdaProject()) { return; }

		if (!path.equals(gprFilePath)) {

			gprFilePath = path;

			gprFileChangeListeners.forEach((key, listener) -> listener.accept(path));

		}

	}

	/**
	 * Returns the path to the GPR file of this manager's project.
	 * In case the path is not set, this method searches the project
	 * file hierarchy for files with the `.gpr` extension and asks
	 * the user to choose one to be used for the project. If the user
	 * chooses a file, the GPR file path will be set and returned.
	 * Note that the user may still discard the choose-file dialog,
	 * in which case this method returns the empty string.
	 *
	 * @return The configured GPR file path.
	 */
	@NotNull
	public String getGprFilePathOrChoose() {

		if (!adaProjectService.isAdaProject()) { return gprFilePath; }

		// If the GPR file path is set, then return it

		if (!"".equals(gprFilePath)) { return gprFilePath; }

		// Iterate over the project files and add the paths of files
		// with the GPR file extension to the list of GPR file paths

		final String gprFileExtension = GPRFileType.INSTANCE.getDefaultExtension();

		final List<String> gprFilePaths = new ArrayList<>();

		VfsUtil.iterateChildrenRecursively(
			Objects.requireNonNull(ProjectUtil.guessProjectDir(project)),
			null,
			fileOrDir -> {

				// Check that entry is an existing file
				// with the GPR file extension

				if (
					fileOrDir.isValid() &&
						!fileOrDir.isDirectory() &&
						gprFileExtension.equals(fileOrDir.getExtension())
				) {
					gprFilePaths.add(fileOrDir.getPath());
				}

				return true;

			}
		);

		// If no GPR files were found, set the path to the empty string,
		// otherwise if one file was found, set the path to that of that
		// file, otherwise ask the user to choose one of the found files

		String newGprFilePath = "";

		switch (gprFilePaths.size()) {

			case 0: {

				if (!notifiedAboutGprFile) {

					Notifications.Bus.notify(new AdaIJNotification(
						"No Project File Found",
						"Add a GPR file to the project structure, or" +
							" specify one in `Run | Edit Configurations...`",
						NotificationType.WARNING
					));

					notifiedAboutGprFile = true;

				}

				break;

			}

			case 1:
				newGprFilePath = gprFilePaths.get(0);
				break;

			default: {

				// Show the dialog and get the user selection

				ListChooserDialog<String> dialog = new ListChooserDialog<>(
					project,
					"Choose Project File",
					"Multiple GPR files were found in this project. " +
						"Please choose one to be used as project file:",
					gprFilePaths,
					"You can always set the project file later (under `Ada | Project " +
						"Settings`), but this is required for semantic features to work, " +
						"such as reference highlighting and code completion.",
					ListSelectionModel.SINGLE_SELECTION
				);

				String selectedPath = dialog.showAndGetSelection();

				// If the user made a selection, then set the GPR file path
				// to the selected path

				if (selectedPath != null) {
					newGprFilePath = selectedPath;
				}

			}

		}

		// Set the GPR file path

		setGprFilePath(newGprFilePath);

		// Return the GPR file path

		return gprFilePath;

	}

	/**
	 * Registers the given listener with the given key to GPR file change events.
	 * @see GPRFileManagerService#gprFileChangeListeners
	 *
	 * @param key The key of the listener to register.
	 * @param listener The listener to register.
	 */
	public void addGprFileChangeListener(String key, Consumer<String> listener) {

		if (!adaProjectService.isAdaProject()) { return; }

		gprFileChangeListeners.put(key, listener);

	}

	/**
	 * Unregisters the listener with the given key from GPR file change events.
	 * @see GPRFileManagerService#gprFileChangeListeners
	 *
	 * @param key The key of the listener to unregister.
	 */
	public void removeGprFileChangeListener(String key) {

		if (!adaProjectService.isAdaProject()) { return; }

		gprFileChangeListeners.remove(key);

	}

}
