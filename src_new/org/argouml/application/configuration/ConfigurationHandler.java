// Copyright (c) 1996-2001 The Regents of the University of California. All
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


package org.argouml.application.configuration;
import org.argouml.application.api.*;
import java.beans.*;
import java.io.*;
import java.util.*;
import java.net.*;
import org.apache.commons.logging.*;

/**
 *   This class provides a user configuration based upon properties.
 *   This provides an implementation-free interface to a configuration
 *   repository.  Any classes which need to implement a configuration
 *   datastore must extend this class.
 *
 *   All of the required behavioral logic for the configuration is
 *   contained within this class, as public final methods.  Any
 *   storage-related logic must be handled by the extending class.
 *   These methods are abstract.
 *
 *   This class is intimately related to
 *   {@link org.argouml.application.api.Configuration}.
 *   
 *   @author Thierry Lach
 *
 *   @since 0.9.4
 */
public abstract class ConfigurationHandler {

  /** Internal storage for the <code>File</code> the configuration was
   *  loaded from, otherwise null.
   */
  private File _loadedFromFile = null;

  /** Internal storage for the <code>URL</code> the configuration was
   *  loaded from, otherwise null.
   */ 
  private URL _loadedFromURL = null;

  /** Internal flag indicating whether the configuration can be updated.
   */
  private boolean _changeable = false;

  /** Internal flag indicating whether the configuration has been loaded.
   *  Configuration rules allow a single load, whether manual or automatic.
   */
  private boolean _loaded = false;

  /** Internal flag indicating whether the configuration has been modified
   *  after it was loaded.
   */
  private boolean _changed = false;

  /** Internal worker for property change.
   */
  private static PropertyChangeSupport _pcl = null; 

  /** Anonymous constructor allows configuration changes.
   */
  public ConfigurationHandler() {
      this(true);
  }

  /** Constructor which optionally allows configuration changes.
   */
  public ConfigurationHandler(boolean changeable) {
      super();
      _changeable = changeable;
  }

  /** Returns a default configuration path.  This could be a filename
   *  or a URL.  It is not guaranteed to be usable across different
   *  implementations of ConfigurationHandler.
   *
   *  @return the default configuration path or url.
   */
  public abstract String getDefaultPath();

  /** Internal worker which is called prior to any getValue
   *  or setValue to ensure that the default load is done if it was not
   *  loaded previously.
   */
  private void loadIfNecessary() {
      if (!_loaded) loadDefault();
  }

  /** Load the configuration from the default location.
   *
   *  @return true if this call loaded the configuration,
   *  otherwise false, not distinguishing between a load
   *  error or a previously loaded configuration.
   *
   *  @see #isLoaded
   */
  public final boolean loadDefault() {
      // Only allow one load
      if (_loaded) return false;

      boolean status = load(new File(getDefaultPath()));
      if (!status) status = loadUnspecified();
      _loaded = true;
      return status;
  }

  /** Save the configuration to the location it was loaded from.
   *  Do not force it if the configuration was not loaded already.
   *
   *  @return true if the save was successful, false if it was not
   *  attempted or encountered an error.
   */
  public final boolean saveDefault() {
      return saveDefault(false);
  }

  /** Save the configuration to the location it was loaded from.
   *
   *  @return true if the save was successful, false if it was not
   *  attempted or encountered an error.
   */
  public final boolean saveDefault(boolean force) {
      if (force) {
          File toFile = new File(getDefaultPath());
          boolean saved = saveFile(toFile);
	  if (saved) {
	      _loadedFromFile = toFile;
	  }
	  return saved;
      }
      if (! _loaded) return false;

      if (_loadedFromFile != null) {
          return saveFile(_loadedFromFile);
      }
      if (_loadedFromURL != null) {
          return saveURL(_loadedFromURL);
      }
      return false;
  }

  /** Indicates whether the configuration can be saved.
   *
   *  @return true if the configuration can be saved.
   */
  public final boolean isChangeable() { return _changeable; }

  /** Indicates whether the configuration can be saved.
   *
   *  @return true if the configuration has been changed after load.
   */
  public final boolean isChanged() { return _changed; }

  /** Indicates whether the configuration has been loaded.
   *
   *  @return true if the configuration has been loaded.
   */
  public final boolean isLoaded() { return _loaded; }

  /** Load the configuration from a <code>File</code>.
   *
   *  @return true if this call loaded the configuration,
   *  otherwise false, not distinguishing between a load
   *  error or a previously loaded configuration.
   */

  public final boolean load(File file) {
      boolean status = loadFile(file);
      if (status) {
          if (_pcl != null) {
              _pcl.firePropertyChange(Configuration.FILE_LOADED, null, file);
          }
          _loadedFromFile = file;
      }
      return status;
  }

  /** Load the configuration from a <code>URL</code>.
   *
   *  @return true if this call loaded the configuration,
   *  otherwise false, not distinguishing between a load
   *  error or a previously loaded configuration.
   */ 
  public final boolean load(URL url) {
      boolean status = loadURL(url);
      if (status) {
          if (_pcl != null) {
              _pcl.firePropertyChange(Configuration.URL_LOADED, null, url);
          }
          _loadedFromURL = url;
      }
      return status;
  }

  /** Save the configuration to a <code>File</code>.
   *
   *  @return true if this call saved the configuration,
   *  otherwise false.
   */ 
  public final boolean save(File file) {
      if (! _loaded) {
          return false;
      }
      boolean status = saveFile(file);
      if (status) {
          if (_pcl != null) {
              _pcl.firePropertyChange(Configuration.FILE_SAVED, null, file);
          }
      }
      return status;
  }

  /** Save the configuration to a <code>URL</code>.
   *
   *  @return true if this call saved the configuration,
   *  otherwise false.
   */ 
  public final boolean save(URL url) {
      if (! _loaded) {
          return false;
      }
      boolean status = saveURL(url);
      if (status) {
          if (_pcl != null) {
              _pcl.firePropertyChange(Configuration.URL_SAVED, null, url);
          }
      }
      return status;
  }

  /** Returns the string value of a configuration property.
   *
   *  @param key the configuration key to return.
   *  @param defaultValue the default value key to return
   *  if the key is not found.
   *
   *  @return the value of the key or the default value
   *  if the key does not exist.
   */
  public final String getString(ConfigurationKey key, String defaultValue) {
      loadIfNecessary();
      return getValue(key.getKey(), defaultValue);
  }

  /** Returns the numeric value of a configuration property.
   *
   *  @param key the configuration key to return.
   *  @param defaultValue the default value key to return
   *  if the key is not found.
   *
   *  @return the value of the key or the default value
   *  if the key does not exist.
   */
  public final int getInteger(ConfigurationKey key, int defaultValue) {
      loadIfNecessary();
      try {
          String s = getValue(key.getKey(), Integer.toString(defaultValue));
          return Integer.parseInt(s);
      }
      catch (NumberFormatException nfe) {
          return defaultValue;
      }
  }

  /** Returns the numeric value of a configuration property.
   *
   *  @param key the configuration key to return.
   *  @param defaultValue the default value key to return
   *  if the key is not found.
   *
   *  @return the value of the key or the default value
   *  if the key does not exist.
   */
  public final double getDouble(ConfigurationKey key, double defaultValue) {
      loadIfNecessary();
      try {
          String s = getValue(key.getKey(), Double.toString(defaultValue));
          return Double.parseDouble(s);
      }
      catch (NumberFormatException nfe) {
          return defaultValue;
      }
  }

  /** Returns the boolean value of a configuration property.
   *
   *  @param key the configuration key to return.
   *  @param defaultValue the default value key to return
   *  if the key is not found.
   *
   *  @return the value of the key or the default value
   *  if the key does not exist.
   */
  public final boolean getBoolean(ConfigurationKey key, boolean defaultValue) {
      loadIfNecessary();
      Boolean dflt = new Boolean(defaultValue);
      Boolean b = new Boolean(getValue(key.getKey(), dflt.toString()));
      return b.booleanValue();
  }

  /** Internal routine which calls the abstract setValue and handles
   *  all necessary functionality including firing property change
   *  notifications and tracing.  
   *
   *  @param key the configuration key to modify.
   *  @param newValue the new value of the key.
   */
  private synchronized final void workerSetValue(ConfigurationKey key, String newValue) {
      loadIfNecessary();

      String oldValue = getValue(key.getKey(), "");
      setValue(key.getKey(), newValue);
      if (_pcl != null) {
          _pcl.firePropertyChange(key.getKey(), oldValue, newValue);
      }
  }

  /** Sets the string value of a configuration property.
   *
   *  @param key the configuration key to modify.
   *  @param newValue the value to set the key to.
   */
  public final void setString(ConfigurationKey key, String newValue) {
      workerSetValue(key, newValue);
  }

  /** Sets the numeric value of a configuration property.
   *
   *  @param key the configuration key to modify.
   *  @param value the value to set the key to.
   */
  public final void setInteger(ConfigurationKey key, int value) {
      workerSetValue(key, Integer.toString(value));
  }

  /** Sets the numeric value of a configuration property.
   *
   *  @param key the configuration key to modify.
   *  @param value the value to set the key to.
   */
  public final void setDouble(ConfigurationKey key, double value) {
      workerSetValue(key, Double.toString(value));
  }

  /** Sets the boolean value of a configuration property.
   *
   *  @param key the configuration key to modify.
   *  @param value the value to set the key to.
   */
  public final void setBoolean(ConfigurationKey key, boolean value) {
      Boolean bool = new Boolean(value);
      workerSetValue(key, bool.toString());
  }

  /** Adds a property change listener.
   *
   *  @param pcl The class which will listen for property changes.
   */
  public final void addListener(PropertyChangeListener pcl) {
      if (_pcl == null) {
          _pcl = new PropertyChangeSupport(this);
      }
      Configuration.logger.debug("addPropertyChangeListener(" + pcl + ")");
      _pcl.addPropertyChangeListener(pcl);
  }

  /** Removes a property change listener.
   *
   *  @param pcl The class to remove as a property change listener.
   */
  public final void removeListener(PropertyChangeListener pcl) {
      if (_pcl != null) {
          Configuration.logger.debug("removePropertyChangeListener()");
          _pcl.removePropertyChangeListener(pcl);
      }
  }

  /** Adds a property change listener.Static for simplicity of use.
   *
   *  @param key The specific key to listen for.
   *  @param pcl The class which will listen for property changes.
   */
  public final void addListener(ConfigurationKey key, PropertyChangeListener pcl) {
      if (_pcl == null) {
          _pcl = new PropertyChangeSupport(this);
      }
      Configuration.logger.debug("addPropertyChangeListener(" + key.getKey() + ")");
      _pcl.addPropertyChangeListener(key.getKey(), pcl);
  }

  /** Removes a property change listener.
   *
   *  @param key The specific key being listened for.
   *  @param pcl The class to remove as a property change listener.
   */
  public final void removeListener(ConfigurationKey key, PropertyChangeListener pcl) {
      if (_pcl != null) {
          Configuration.logger.debug("removePropertyChangeListener(" + key.getKey() + ")");
          _pcl.removePropertyChangeListener(key.getKey(), pcl);
      }
  }

  /** Internal processing to load from an unspecified source.
   *
   *  @return true if the load was successful, otherwise false.
   */
  boolean loadUnspecified() {
      return false;
  }

  /** Internal processing to save to an unspecified source.
   *
   *  @return true if the save was successful, otherwise false.
   */
  boolean saveUnspecified() {
      return false;
  }

  /** Internal processing to load a <code>File</code>.
   *
   *  @param file the file to load.
   *
   *  @return true if the load was successful, otherwise false.
   */
  abstract boolean loadFile(File file);

  /** Internal processing to load a <code>URL</code>.
   *
   *  @param url the url to load.
   *
   *  @return true if the load was successful, otherwise false.
   */
  abstract boolean loadURL(URL url);

  /** Internal processing to save a <code>File</code>.
   *
   *  @param file the file to save.
   *
   *  @return true if the save was successful, otherwise false.
   */
  abstract boolean saveFile(File file);

  /** Internal processing to save a <code>URL</code>.
   *
   *  @param url the url to save.
   *
   *  @return true if the save was successful, otherwise false.
   */
  abstract boolean saveURL(URL url);

  /** Allows query for the existence of a configuration property.
   *  This may be overridden if the implementation has a
   *  more efficient method.
   * 
   *  @param key  the property being checked.
   *
   *  @return true if the key exists, otherwise false.
   */
  public boolean hasKey(ConfigurationKey key) {
      return getValue(key.getKey(), "true").equals(getValue(key.getKey(), "false"));
  }

  /** Returns the string value of a configuration property.
   *
   *  @param key the configuration key to return.
   *  @param defaultValue the configuration key to return.
   *
   *  @return the value of the key or the default value
   *  if the key does not exist.
   */
  public abstract String getValue(String key, String defaultValue);

  /** Sets the string value of a configuration property.
   *
   *  @param key the configuration key to modify.
   *  @param value the value to set the key to.
   */
  abstract void setValue(String key, String value);
}


