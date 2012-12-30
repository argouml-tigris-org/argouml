/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Icon;

import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoNotationEvent;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.configuration.Configuration;
import org.argouml.configuration.ConfigurationKey;

/**
 * Provides centralized methods dealing with notation.
 *
 * @stereotype singleton
 * @author Thierry Lach
 * @since 0.9.4
 */
public final class Notation implements PropertyChangeListener {

    private static final Logger LOG =
        Logger.getLogger(Notation.class.getName());

    // TODO: Do we have any potential consumers of the unversioned name outside
    // of the notation subsystem?
    private static final String DEFAULT_NOTATION_NAME = "UML";

    private static final String DEFAULT_NOTATION_VERSION = "1.4";

    /**
     * Default notation with both base name and version (e.g. "UML 1.4")
     */
    public static final String DEFAULT_NOTATION = DEFAULT_NOTATION_NAME + " "
            + DEFAULT_NOTATION_VERSION;

    /**
     * The name of the default ArgoUML notation.  This notation is
     * part of ArgoUML core distribution.
     */
    private static NotationName notationArgo =
        makeNotation(
            DEFAULT_NOTATION_NAME,
            DEFAULT_NOTATION_VERSION,
            ResourceLoaderWrapper.lookupIconResource("UmlNotation"));

    /*
     * Remark:
     * There is also a java-like notation, which is also
     * part of ArgoUML core distribution.
     */

    /**
     * The configuration key for the preferred notation.
     */
    public static final ConfigurationKey KEY_DEFAULT_NOTATION =
        Configuration.makeKey("notation", "default");

    /**
     * The configuration key that indicates whether to show stereotypes
     * in the navigation panel.
     */
    public static final ConfigurationKey KEY_SHOW_STEREOTYPES =
        Configuration.makeKey("notation", "navigation", "show", "stereotypes");

    /**
     * The configuration key that indicates whether to show stereotypes
     * in the navigation panel.
     */
    public static final ConfigurationKey KEY_SHOW_SINGULAR_MULTIPLICITIES =
        Configuration.makeKey("notation", "show", "singularmultiplicities");


    /**
     * The configuration key that indicates whether to use guillemots
     * or greater/lessthan characters in stereotypes.
     */
    public static final ConfigurationKey KEY_USE_GUILLEMOTS =
        Configuration.makeKey("notation", "guillemots");

    /**
     * Indicates if the user wants to see association names
     */
    public static final ConfigurationKey KEY_SHOW_ASSOCIATION_NAMES =
        Configuration.makeKey("notation", "show", "associationnames");

    /**
     * Indicates if the user wants to see visibility signs (public,
     * private, protected or # + -).
     */
    public static final ConfigurationKey KEY_SHOW_VISIBILITY =
        Configuration.makeKey("notation", "show", "visibility");

    /**
     * Indicates if the user wants to see multiplicity in attributes
     * and classes.
     */
    public static final ConfigurationKey KEY_SHOW_MULTIPLICITY =
        Configuration.makeKey("notation", "show", "multiplicity");

    /**
     * Indicates if the user wants to see the initial value.
     */
    public static final ConfigurationKey KEY_SHOW_INITIAL_VALUE =
        Configuration.makeKey("notation", "show", "initialvalue");

    /**
     * Indicates if the user wants to see the properties (everything
     * between braces), that is for example the concurrency.
     */
    public static final ConfigurationKey KEY_SHOW_PROPERTIES =
        Configuration.makeKey("notation", "show", "properties");

    /**
     * Indicates if the user wants to see the types and parameters
     * of attributes and operations.
     */
    public static final ConfigurationKey KEY_SHOW_TYPES =
        Configuration.makeKey("notation", "show", "types");


    /**
     * The instance.
     */
    private static final Notation SINGLETON = new Notation();


    /**
     * The constructor.
     * TODO: Why does this method not handle all settings?
     */
    private Notation() {
        Configuration.addListener(KEY_USE_GUILLEMOTS, this);
        Configuration.addListener(KEY_DEFAULT_NOTATION, this);
        Configuration.addListener(KEY_SHOW_TYPES, this);
        Configuration.addListener(KEY_SHOW_MULTIPLICITY, this);
        Configuration.addListener(KEY_SHOW_PROPERTIES, this);
        Configuration.addListener(KEY_SHOW_ASSOCIATION_NAMES, this);
        Configuration.addListener(KEY_SHOW_VISIBILITY, this);
        Configuration.addListener(KEY_SHOW_INITIAL_VALUE, this);
    }

    /**
     * @param n the NotationName that will become default
     */
    public static void setDefaultNotation(NotationName n) {
        LOG.log(Level.INFO,
                "default notation set to {0}", n.getConfigurationValue());

        Configuration.setString(
            KEY_DEFAULT_NOTATION,
            n.getConfigurationValue());
    }

    /**
     * Convert a String into a NotationName.
     * @param s the String
     * @return the matching NotationName
     */
    public static NotationName findNotation(String s) {
        return NotationNameImpl.findNotation(s);
    }

    /**
     * Returns the Notation as set in the menu.
     *
     * @return the default NotationName
     */
    public static NotationName getConfiguredNotation() {
        NotationName n =
            NotationNameImpl.findNotation(
                Configuration.getString(
                    KEY_DEFAULT_NOTATION,
                    notationArgo.getConfigurationValue()));
        // This is needed for the case when the default notation is
        // not loaded at this point.
        if (n == null) {
            n = NotationNameImpl.findNotation(DEFAULT_NOTATION);
	}
        LOG.log(Level.FINE, "default notation is {0}", n.getConfigurationValue());
        return n;
    }

    ////////////////////////////////////////////////////////////////
    // class accessors

    ////////////////////////////////////////////////////////////////
    // static accessors

    /**
     * @return the singleton
     */
    public static Notation getInstance() {
        return SINGLETON;
    }

    /*
     * Called after the notation default property gets changed.
     *
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent pce) {
        LOG.log(Level.INFO, "Notation change: {0} to {1}",
                new Object[]{pce.getOldValue(), pce.getNewValue()});

        ArgoEventPump.fireEvent(
            new ArgoNotationEvent(ArgoEventTypes.NOTATION_CHANGED, pce));
    }

    ////////////////////////////////////////////////////////////////
    // Static workers for dealing with notation names.

    /**
     * Get list of available notations, of type NotationName.
     * This returns an immutable list so that
     * the implementation type isn't exposed in the API.
     *
     * @return list of available notations
     */
    public static List<NotationName> getAvailableNotations() {
        return NotationNameImpl.getAvailableNotations();
    }

    /**
     * Remove a complete Notation language.
     * This is to be used by plug-ins that implement their own notation,
     * and that are removed. <p>
     * This function fails if the given notation does not exist.
     *
     * @param theNotation the given NotationName
     * @return true if the Notation indeed is removed
     */
    public static boolean removeNotation(NotationName theNotation)  {
        return NotationNameImpl.removeNotation(theNotation);
    }

    /**
     * Create a versioned notation name with an icon.
     *
     * @param k1 the name (e.g. UML)
     * @param k2 the version (e.g. 1.3)
     * @param icon the icon
     * @return the notation name
     */
    public static NotationName makeNotation(String k1, String k2, Icon icon) {
        NotationName nn = NotationNameImpl.makeNotation(k1, k2, icon);
        return nn;
    }
}
