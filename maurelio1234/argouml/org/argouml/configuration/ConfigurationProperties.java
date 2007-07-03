// $Id: ConfigurationProperties.java 12546 2007-05-05 16:54:40Z linus $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * This class provides a user configuration based upon 
 * the properties file "argo.user.properties" in the user's home directory.
 * 
 * @author Thierry Lach
 */
class ConfigurationProperties extends ConfigurationHandler {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ConfigurationProperties.class);

    /**
     * The location of Argo's default properties resource.
     */
    private static String propertyLocation =
        "/org/argouml/resource/default.properties";

    /**
     * The primary property bundle.
     */
    private Properties propertyBundle;

    /**
     * Flag to ensure that only the first load failure is reported
     * even though we keep trying because the file or URL may only
     * be temporarily unavailable.
     */
    private boolean canComplain = true;

    /**
     * Anonymous constructor.
     */
    ConfigurationProperties() {
        super(true);
        Properties defaults = new Properties();
        try {
            defaults.load(getClass().getResourceAsStream(propertyLocation));
            LOG.debug("Configuration loaded from " + propertyLocation);
        } catch (Exception ioe) {
            // TODO:  What should we do here?
            LOG.warn("Configuration not loaded from " + propertyLocation, ioe);
        }
        propertyBundle = new Properties(defaults);
    }

    /**
     * Returns the default path for user properties.
     *
     * @return a generic path string.
     */
    public String getDefaultPath() {
        return System.getProperty("user.home") + "/argo.user.properties";
    }


    /**
     * Load the configuration from a specified location.
     *
     * @param file  the path to load the configuration from.
     *
     * @return true if the load was successful, false if not.
     */
    public boolean loadFile(File file) {
        try {
            propertyBundle.load(new FileInputStream(file));
            LOG.info("Configuration loaded from " + file);
            return true;
        } catch (Exception e) {
            if (canComplain) {
                LOG.warn("Unable to load configuration " + file);
            }
            // Try to create an empty file.
            try {
                file.createNewFile();
                if (file.exists() && file.isFile()) {
                    LOG.info("New configuration created as " + file);
                    // Pretend we loaded the file correctly
                    return true;
                }
            } catch (IOException e1) {
                // Ignore an error here
                LOG.warn("Unable to create configuration " + file, e1);
            }
            canComplain = false;
        }

        return false;
    }

    /**
     * Save the configuration to a specified location.
     *
     * @param file  the path to save the configuration at.
     *
     * @return true if the save was successful, false if not.
     */
    public boolean saveFile(File file) {
        try {
            propertyBundle.store(new FileOutputStream(file),
                    "ArgoUML properties");
            LOG.info("Configuration saved to " + file);
            return true;
        } catch (Exception e) {
            if (canComplain) {
                LOG.warn("Unable to save configuration " + file + "\n");
            }
            canComplain = false;
        }

        return false;
    }

    /**
     * Load the configuration from a specified location.
     *
     * @param url  the path to load the configuration from.
     *
     * @return true if the load was successful, false if not.
     */
    public boolean loadURL(URL url) {
        try {
            propertyBundle.load(url.openStream());
            LOG.info("Configuration loaded from " + url + "\n");
            return true;
        } catch (Exception e) {
            if (canComplain) {
                LOG.warn("Unable to load configuration " + url + "\n");
            }
            canComplain = false;
            return false;
        }
    }

    /**
     * Save the configuration to a specified location.
     *
     * @param url  the path to save the configuration at.
     *
     * @return true if the save was successful, false if not.
     */
    public boolean saveURL(URL url) {
        // LOG.info("Configuration saved to " + url + "\n");
        return false;
    }

    /**
     * Returns the string value of a configuration property.
     *
     * @param key the key to return the value of.
     * @param defaultValue the value to return if the key was not found.
     *
     * @return the string value of the key if found, otherwise null;
     */
    public String getValue(String key, String defaultValue) {
        String result = "";
        try {
            result = propertyBundle.getProperty(key, defaultValue);
        } catch (Exception e) {
            result = defaultValue;
        }
        return result;
    }

    /**
     * Sets the string value of a configuration property.
     *
     * @param key the key to set.
     * @param value the value to set the key to.
     */
    public void setValue(String key, String value) {
        LOG.debug("key '" + key + "' set to '" + value + "'");
        propertyBundle.setProperty(key, value);
    }

    /**
     * Remove a property.
     *
     * @param key The property to remove.
     */
    public void remove(String key) {
        propertyBundle.remove(key);
    }
}

