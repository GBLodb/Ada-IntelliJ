package com.adacore.adaintellij.project;

import com.intellij.openapi.project.Project;

/**
 * Project component somewhat representing the Ada-IntelliJ side of a project.
 * When a project is being loaded, this component determines whether
 * that project is considered to be an Ada project, and provides the
 * `isAdaProject` method to the rest of the Ada-IntelliJ plugin to determine
 * whether features/operations should be enabled/performed in the context of
 * that project.
 * Therefore, despite the class name `AdaProject` and even though the doc
 * sometimes refers to "this Ada project", an instance of this class could
 * be a component of a project that is in fact not an Ada project.
 */
public final class AdaProjectService {

	/**
	 * The project to which this component belongs.
	 */
	Project project;

	/**
	 * Whether this project is considered to be an
	 * Ada project by the Ada-IntelliJ plugin.
	 */
	boolean isAdaProject = false;

	/**
	 * Constructs a new AdaProject given a project.
	 *
	 * @param project The project to attach to the constructed Ada project.
	 */
	public AdaProjectService(Project project) { this.project = project; }

	/**
	 * Returns whether this project is an Ada project.
	 *
	 * @return Whether this project is an Ada project.
	 */
	public boolean isAdaProject() { return isAdaProject; }

}
