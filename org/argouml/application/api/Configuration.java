// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.application.api;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

import org.argouml.application.configuration.ConfigurationFactory;
import org.argouml.application.configuration.ConfigurationHandler;
import org.argouml.application.configuration.ConfigurationKeyImpl;
import org.argouml.application.configuration.IConfigurationFactory;

/**
 * This class provides the core user configuration implementation
 * logic.  All fancy handling and registry access occurs
 * behind the scenes.
 *
 * @stereotype utility
 * @author Thierry Lach
 * @since  0.9.4
 */
public final class Configuration {

    ////////////////////////////////////////////////////////////////
    // Instance variables

    /**
     * Property to indicate configuration load from file.
     */
    public static final String FILE_LOADED = "configuration.load.file";

    /**
     * Property to indicate configuration load from url.
     */
    public static final String URL_LOADED = "configuration.load.url";

    /**
     * Property to indicate configuration save to file.
     */
    public static final String FILE_SAVED = "configuration.save.file";

    /**
     * Property to indicate configuration save to url.
     */
    public static final String URL_SAVED = "configuration.save.url";

    /**
     * The only occurance of the configuration handler.
     */
    private static ConfigurationHandler config =
	getFactory().getConfigurationHandler();

    /**
     * Private constructor so it cannot be instantiated.
     */
    private Configuration() {
        // Don't allow instantiation
    }

    /**
     * Returns the instance of the configuration singleton.
     *
     * @return the configuration handler
     */
    public static ConfigurationHandler getConfigurationHandler() {
	return config;
    }

    /**
     * Returns the configuration factory instance.<p>
     *
     * This is equivalent to ConfigurationFactory.getInstance() but
     * using Configuration.getFactory() is shorter to type and
     * allows us not to have to deal with ConfigurationFactory at
     * all if we don't need to modify or configure it.
     *
     * @return the configuration factory
     */
    public static IConfigurationFactory getFactory() {
	return ConfigurationFactory.getInstance();
    }

    /**
     * Load the configuration from the default location.
     *
     * The configuration will be automatically loaded from the default
     * location the first time a value is queried or modified, if it
     * had not been previously loaded.  Only the first load request
     * will be honored, so if the configuration is to be loaded from
     * a non-default location, load(name) must be used prior to any
     * other call.  The configuration can be loaded only one time.
     *
     * Implementations must ignore load requests once a load is
     * already successful, and must return false for each of those
     * ignored requests.
     *
     * @return true if the load is successful, otherwise false
     */
    public static boolean load() {
	return config.loadDefault();
    }

    /**
     * Load the configuration from a specified file.
     *
     * @param file the File to load
     *
     * @return true if the load is successful, otherwise false
     */
    public static boolean load(File file) {
	return config.load(file);
    }

    /**
     * Load the configuration from a specified url.
     *
     * @param url the URL to load
     *
     * @return true if the load is successful, otherwise false
     */
    public static boolean load(URL url) {
	return config.load(url);
    }

    /**
     * Save the configuration to the default location.
     *
     * Implementations do not have to handle this method.
     * If the method is not allowed or it fails, the implementation
     * must return false.
     *
     * @return true if the save is successful, otherwise false
     */
    public static boolean save() {
	return Configuration.save(false);
    }

    /**
     * Save the configuration to the default location.
     *
     * Implementations do not have to handle this method.
     * If the method is not allowed or it fails, the implementation
     * must return false.
     *
     * @param force the file to save even if it would not normally
     * be saved.
     *
     * @return true if the save is successful, otherwise false
     */
    public static boolean save(boolean force) {
	return config.saveDefault(force);
    }

    /**
     * Returns the string value of a configuration property.
     *
     * @param key the key to retrieve the value of
     *
     * @return the string value of the parameter if it exists, otherwise
     * a zero length string
     */
    public static String getString(ConfigurationKey key) {
	return getString(key, "");
    }

    /**
     * Returns the string value of a configuration property.
     *
     * @param key the key to retrieve the value of
     * @param defaultValue the value to return if the key does not exist
     *
     * @return the string value of the parameter if it exists, otherwise the
     *   default value
     */
    public static String getString(ConfigurationKey key,
                                   String defaultValue) {
	return config.getString(key, defaultValue);
    }

    /**
     * Returns the numeric value of a configuration property.
     *
     * @param key the key to retrieve the value of
     *
     * @return the string value of the parameter if it exists, otherwise zero
     */
    public static int getInteger(ConfigurationKey key) {
	return getInteger(key, 0);
    }

    /**
     * Returns the numeric value of a configuration property.
     *
     * @param key the key to retrieve the value of
     * @param defaultValue if the key is not found
     *
     * @return the string value of the parameter if it exists,
     *         otherwise the default value
     */
    public static double getDouble(ConfigurationKey key,
                                   double defaultValue) {
	return config.getDouble(key, defaultValue);
    }

    /**
     * Returns the numeric value of a configuration property.
     *
     * @param key the key to retrieve the value of
     *
     * @return the string value of the parameter if it exists, otherwise zero
     */
    public static double getDouble(ConfigurationKey key) {
	return getDouble(key, 0);
    }

    /**
     * Returns the numeric value of a configuration property.
     *
     * @param key the key to retrieve the value of
     * @param defaultValue the value to return if the key does not exist
     *
     * @return the numeric value of the parameter if it exists, otherwise
     *  the default value
     */
    public static int getInteger(ConfigurationKey key, int defaultValue) {
	return config.getInteger(key, defaultValue);
    }

    /**
     * Returns the boolean value of a configuration property.
     *
     * @param key the key to retrieve the value of
     *
     * @return the boolean value of the parameter if it exists, otherwise false
     */
    public static boolean getBoolean(ConfigurationKey key) {
	return getBoolean(key, false);
    }

    /**
     * Returns the boolean value of a configuration property.
     *
     * @param key the key to retrieve the value of
     * @param defaultValue the value to return if the key does not exist
     *
     * @return the boolean value of the parameter if it exists, otherwise
     *  the default value
     */
    public static boolean getBoolean(ConfigurationKey key,
                                     boolean defaultValue) {
	return config.getBoolean(key, defaultValue);
    }

    /**
     * Sets the string value of a configuration property.
     *
     * @param key the key to set
     * @param newValue the value to set the key to.
     */
    public static void setString(ConfigurationKey key, String newValue) {
	config.setString(key, newValue);
    }

    /**
     * Sets the numeric value of a configuration property.
     *
     * @param key the key to set
     * @param newValue the value to set the key to.
     */
    public static void setInteger(ConfigurationKey key, int newValue) {
	config.setInteger(key, newValue);
    }

    /**
     * Sets the numeric value of a configuration property.
     *
     * @param key the key to set
     * @param newValue the value to set the key to.
     */
    public static void setDouble(ConfigurationKey key, double newValue) {
	config.setDouble(key, newValue);
    }

    /**
     * Sets the boolean value of a configuration property.
     *
     * @param key the key to set
     * @param newValue the value to set the key to.
     */
    public static void setBoolean(ConfigurationKey key,
                                  boolean newValue) {
	config.setBoolean(key, newValue);
    }

    /**
     * Adds a property change listener.
     *
     * @param pcl The property change listener to add
     */
    public static void addListener(PropertyChangeListener pcl) {
	config.addListener(pcl);
    }

    /**
     * Removes a property change listener.
     *
     * @param pcl The property change listener to remove
     */
    public static void removeListener(PropertyChangeListener pcl) {
	config.removeListener(pcl);
    }

    /**
     * Adds a property change listener.Static for simplicity of use.
     *
     * @param key The key to listen for changes of
     * @param pcl The property change listener to add
     */
    public static void addListener(ConfigurationKey key,
                                   PropertyChangeListener pcl) {
	config.addListener(key, pcl);
    }

    /**
     * Removes a property change listener.
     *
     * @param key The key to listen for changes of
     * @param pcl The property change listener to remove
     */
    public static void removeListener(ConfigurationKey key,
                                      PropertyChangeListener pcl) {
	config.removeListener(key, pcl);
    }

    /**
     * @param key The key to remove.
     */
    public static void removeKey(ConfigurationKey key) {
        config.remove(key.getKey());
    }

    /**
     * Create a single component configuration key.
     *
     * @param k1 key component 1.
     * @return the new {@link ConfigurationKey}.
     */
    public static ConfigurationKey makeKey(String k1) {
	return new ConfigurationKeyImpl(k1);
    }

    /**
     * Create a sub-component of an existing configuration key.
     *
     * @param ck existing key to extend.
     * @param k1 key component 1.
     * @return the new {@link ConfigurationKey}.
     */
    public static ConfigurationKey makeKey(ConfigurationKey ck, String k1) {
	return new ConfigurationKeyImpl(ck, k1);
    }

    /**
     * Create a two-component configuration key.
     *
     * @param k1 key component 1.
     * @param k2 key component 2.
     * @return the new {@link ConfigurationKey}.
     */
    public static ConfigurationKey makeKey(String k1, String k2) {
	return new ConfigurationKeyImpl(k1, k2);
    }

    /**
     * Create a three-component configuration key.
     *
     * @param k1 key component 1.
     * @param k2 key component 2.
     * @param k3 key component 3.
     * @return the new {@link ConfigurationKey}.
     */
    public static ConfigurationKey makeKey(String k1, String k2, String k3) {
	return new ConfigurationKeyImpl(k1, k2, k3);
    }

    /**
     * Create a four-component configuration key.
     *
     * @param k1 key component 1.
     * @param k2 key component 2.
     * @param k3 key component 3.
     * @param k4 key component 4.
     * @return the new {@link ConfigurationKey}.
     */
    public static ConfigurationKey makeKey(String k1, String k2,
					   String k3, String k4) {
	return new ConfigurationKeyImpl(k1, k2, k3, k4);
    }

    /**
     * Create a five-component configuration key.
     *
     * @param k1 key component 1.
     * @param k2 key component 2.
     * @param k3 key component 3.
     * @param k4 key component 4.
     * @param k5 key component 5.
     * @return the new {@link ConfigurationKey}.
     */
    public static ConfigurationKey makeKey(String k1, String k2,
					   String k3, String k4,
					   String k5) {
	return new ConfigurationKeyImpl(k1, k2, k3, k4, k5);
    }
}

