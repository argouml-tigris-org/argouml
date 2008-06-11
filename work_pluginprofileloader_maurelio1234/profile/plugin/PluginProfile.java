package org.argouml.profile.plugin;

import org.argouml.profile.Profile;

/**
 * This class represents a User Defined Profile defined as a Plug-in
 * It allows the loading code to abstract from the internal format used
 * to store these informations into the JAR archive 
 * 
 * @author maas
 */
public interface PluginProfile {

	/**
	 * @return the description for this profile
	 */
	String getDescription();

	/**
	 * @return the author of this profile
	 */
	String getAuthor();

	/**
	 * @return the version of this profile
	 */
	String getVersion();

	/**
	 * @return the download site for this profile
	 */
	String getDownloadSite();

	/**
	 * @return the name for this profile
	 */
	String getName();
	
	/**
	 * @return the profile object
	 */
	Profile getProfile();

}
