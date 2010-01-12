/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    linus
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.beans.PropertyChangeEvent;


/**
 *   This class provides definition and manipulation of configuration keys.
 *   All keys in the configuration system will be accessed using the
 *   ConfigurationKey wrapper.
 *
 *   @author Thierry Lach
 *   @since ARGO0.9.4
 */
public class ConfigurationKeyImpl
    implements ConfigurationKey {

    /**
     * The string value for the key.
     */
    private String key = null;

    /**
     * Create a single component configuration key.
     *
     * @param k1 key component.
     */
    public ConfigurationKeyImpl(String k1) {
	key = "argo." + k1;
    }

    /**
     * Create a sub-component of an existing configuration key.
     *
     * @param ck configuration key
     * @param k1 additional key component.
     */
    public ConfigurationKeyImpl(ConfigurationKey ck, String k1) {
	key = ck.getKey() + "." + k1;
    }

    /**
     * Create a two-component configuration key.
     *
     * @param k1 key component 1.
     * @param k2 key component 2.
     */
    public ConfigurationKeyImpl(String k1, String k2) {
	key = "argo." + k1 + "." + k2;
    }

    /**
     * Create a three-component configuration key.
     *
     * @param k1 key component 1.
     * @param k2 key component 2.
     * @param k3 key component 3.
     */
    public ConfigurationKeyImpl(String k1, String k2, String k3) {
	key = "argo." + k1 + "." + k2 + "." + k3;
    }

    /**
     * Create a four-component configuration key.
     *
     * @param k1 key component 1.
     * @param k2 key component 2.
     * @param k3 key component 3.
     * @param k4 key component 4.
     */
    public ConfigurationKeyImpl(String k1, String k2, String k3, String k4) {
	key = "argo." + k1 + "." + k2 + "." + k3 + "." + k4;
    }

    /**
     * Create a five-component configuration key.
     *
     * @param k1 key component 1.
     * @param k2 key component 2.
     * @param k3 key component 3.
     * @param k4 key component 4.
     * @param k5 key component 5.
     */
    public ConfigurationKeyImpl(String k1, String k2,
				String k3, String k4, String k5) {
	key = "argo." + k1 + "." + k2 + "." + k3 + "." + k4 + "." + k5;
    }

    /**
     * Return the actual key used to access the configuration.
     *
     * @return the key
     */
    public final String getKey() {
	return key;
    }

    /**
     * Compare the configuration key to a string.
     *
     * @param pce PropertyChangeEvent to check
     * @return true if the changed property is for the key.
     */
    public boolean isChangedProperty(PropertyChangeEvent pce) {
	if (pce == null) {
	    return false;
	}
	return pce.getPropertyName().equals(key);
    }

    /**
     * Returns a formatted key.
     * @return a formatted key string.
     */
    public String toString() {
	return "{ConfigurationKeyImpl:" + key + "}";
    }
}

