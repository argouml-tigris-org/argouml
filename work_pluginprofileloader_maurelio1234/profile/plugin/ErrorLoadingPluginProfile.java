package org.argouml.profile.plugin;

/**
 * Excepton raised when loading profile from plugin
 *
 * @author maas
 */
@SuppressWarnings("serial")
public class ErrorLoadingPluginProfile extends Exception {

	/**
	 * Default constructor
	 */
	public ErrorLoadingPluginProfile() {
		super();
	}

	/**
	 * @param message message
	 * @param cause cause
	 */
	public ErrorLoadingPluginProfile(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message message
	 */
	public ErrorLoadingPluginProfile(String message) {
		super(message);
	}

	/**
	 * @param cause cause
	 */
	public ErrorLoadingPluginProfile(Throwable cause) {
		super(cause);
	}

}
