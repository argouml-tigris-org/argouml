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


package org.argouml.application.configuration;

import org.apache.log4j.Logger;

/**
 * A factory object that provides configuration information.
 *
 * @author Thierry Lach
 * @since 0.9.4
 */
public class ConfigurationFactory {
    /**
     * The only occurance of the configuration factory.
     */
    private static final ConfigurationFactory SINGLETON;

    /**
     * The active configuration handler.
     */
    private static ConfigurationHandler handler =
	new ConfigurationProperties();

    /**
     * Initialize the factory singleton based on system
     * property argo.ConfigurationFactory, or use the default
     * if not set.
     */
    static {
        String name = System.getProperty("argo.ConfigurationFactory");
	ConfigurationFactory newFactory = null;
	if (name != null) {
            try {
                newFactory =
		    (ConfigurationFactory) Class.forName(name).newInstance();
            } catch (Exception e) {
		Logger.getLogger(ConfigurationFactory.class).
		    warn("Can't create configuration factory "
                        + name + ", using default factory");
            }
	}
	if (newFactory == null) {
	    newFactory = new ConfigurationFactory();
	}
	SINGLETON = newFactory;
    }

    /**
     * Private constructor to not allow instantiation.
     */
    private ConfigurationFactory() {
    }

    /**
     * Returns the instance of the singleton.
     *
     * @return the only instance of the configuration factory.
     */
    public static final ConfigurationFactory getInstance() {
	return SINGLETON;
    }

    /**
     * Returns the customized configuration for the user.
     *
     * @return a concrete class which extends ConfigurationHandler and
     *         can be used to access and manipulate the configuration.
     */
    public ConfigurationHandler getConfigurationHandler() {
	// TODO:  Allow other configuration handlers.
	return handler;
    }

} /* end class ConfigurationFactory */
