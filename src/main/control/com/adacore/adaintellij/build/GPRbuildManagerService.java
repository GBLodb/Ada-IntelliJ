package com.adacore.adaintellij.build;

import com.adacore.adaintellij.notifications.AdaIJNotification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.adacore.adaintellij.Utils.getPathFromSystemPath;

/**
 * Application component handling GPRbuild-related tasks.
 */
@Service
public final class GPRbuildManagerService implements PersistentStateComponent<GPRbuildManagerService.State> {

	/**
	 * The extension-less name of the gprbuild executable.
	 */
	private static final String GPRBUILD_NAME = "gprbuild";
	/**
	 * The gprbuild path in the system.
	 */
	private static String gprbuildPath = "";

	private State serviceState = new State();

	/**
	 * Returns the gprbuild path in the system.
	 *
	 * @return The gprbuild path.
	 */
	@Contract(pure = true)
	@NotNull
	public static String getGprbuildPath() { return gprbuildPath; }

	/**
	 * Sets the gprbuild path in the system.
	 *
	 * @param path The new gprbuild path.
	 */
	public static void setGprBuildPath(@NotNull String path) { gprbuildPath = path; }

	@Override
	public @Nullable GPRbuildManagerService.State getState() {
		return serviceState;
	}

	@Override
	public void loadState(@NotNull State state) {
		serviceState = state;
	}

	/**
	 * @see com.intellij.openapi.components.PersistentStateComponent#initializeComponent()
	 */
	@Override
	public void initializeComponent() {

		String path = getPathFromSystemPath(GPRBUILD_NAME, false);

		if (path == null) {

			// Notify the user that no compiler was found on the path
			Notifications.Bus.notify(new AdaIJNotification(
				"No Compiler Found on the PATH",
				"Please set the gprbuild path in `Run | Edit Configurations`.",
				NotificationType.WARNING
			));

			return;

		}

		gprbuildPath = path;

		// Notify the user that a compiler was found on the path
		Notifications.Bus.notify(new AdaIJNotification(
			"Compiler Found on the PATH",
			"Using the following gprbuild for compilation:\n" + gprbuildPath,
			NotificationType.INFORMATION
		));

	}

	static class State {
		public String value;
	}

}
