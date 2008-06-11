package org.argouml.profile.plugin;

import org.argouml.profile.Profile;

/**
 * Default implementation for the {@link PluginProfile} interface
 * 
 * @author maas
 */
public class PluginProfileImpl implements PluginProfile {
	private String author;
	private String description;
	private String name;
	private String version;
	private String downloadsite;
	private Profile profile;
	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the downloadsite
	 */
	public String getDownloadSite() {
		return downloadsite;
	}
	/**
	 * @param downloadsite the downloadsite to set
	 */
	public void setDownloadSite(String downloadsite) {
		this.downloadsite = downloadsite;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}
	/**
	 * @param profile the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
}
