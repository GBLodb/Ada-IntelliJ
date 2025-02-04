package com.adacore.adaintellij.build;

import com.adacore.adaintellij.Icons;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;

import javax.swing.*;

/**
 * Run configuration type for GPRbuild run configurations.
 */
public final class GPRbuildConfigurationType implements ConfigurationType {

	/**
	 * Unique instance representing the GPRbuild run configuration type.
	 */
	public static final GPRbuildConfigurationType INSTANCE = new GPRbuildConfigurationType();

	/**
	 * Constructs a new instance of the GPRbuild run configuration type.
	 */
	private GPRbuildConfigurationType() {}

	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getDisplayName()
	 */
	@Override
	public String getDisplayName() { return "GPRbuild"; }

	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getConfigurationTypeDescription()
	 */
	@Override
	public String getConfigurationTypeDescription() { return "GPRbuild Run Configuration Type"; }

	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getIcon()
	 */
	@Override
	public Icon getIcon() { return Icons.GPRBUILD_RUN_CONFIGURATION; }

	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getId()
	 */
	@Override
	public String getId() { return "GPR_BUILD_CONFIGURATION"; }

	/**
	 * @see com.intellij.execution.configurations.ConfigurationType#getConfigurationFactories()
	 */
	@Override
	public ConfigurationFactory[] getConfigurationFactories() {
		return new ConfigurationFactory[] { new GPRbuildConfigurationFactory(this) };
	}

}
