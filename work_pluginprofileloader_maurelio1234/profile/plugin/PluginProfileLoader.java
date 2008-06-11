package org.argouml.profile.plugin;

/**
 * This interface abstracts the process used to load the
 * plugin descriptor. 
 * 
 * @author maas
 */
public interface PluginProfileLoader {
	/**
	 * Loads the profile
	 * 
	 * @param cl the reference class (used in order to find the needed resources) 
	 * @return the profile descriptor
	 * @throws ErrorLoadingPluginProfile
	 */
	PluginProfile loadProfile(Class cl) throws ErrorLoadingPluginProfile;
}
