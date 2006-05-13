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

package org.argouml.notation;

import java.util.ArrayList;
import java.util.ListIterator;

import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.events.ArgoEventPump;
import org.argouml.application.events.ArgoEventTypes;
import org.argouml.application.events.ArgoModuleEvent;
import org.argouml.application.events.ArgoModuleEventListener;
import org.argouml.application.events.ArgoNotationEvent;

/**
 * This class provides definition and manipulation of notation names.
 * All notation names will be accessed using the
 * {@link NotationName} wrapper.
 *
 * Not mutable!
 *
 * @author Thierry Lach
 * @since 0.9.4
 */
class NotationNameImpl
    implements NotationName, ArgoModuleEventListener {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(NotationNameImpl.class);

    private String name;
    private String version;
    private Icon icon;

    private static ArrayList notations = new ArrayList();

    /**
     * A notation without a version or icon.
     *
     * @param theName the name of the notation
     */
    protected NotationNameImpl(String theName) {
        this(theName, null, null);
    }

    /**
     * A notation without a version and with an icon.
     *
     * @param theName the name of the notation
     * @param theIcon the icon for of the notation
     */
    protected NotationNameImpl(String theName, Icon theIcon) {
        this(theName, null, theIcon);
    }

    /**
     * A notation with a version and no icon.
     *
     * @param theName the name of the notation
     * @param theVersion the version of the notation
     */
    protected NotationNameImpl(String theName, String theVersion) {
        this(theName, theVersion, null);
    }

    /**
     * A notation with a version and an icon.
     *
     * @param myName    the name of the notation
     * @param myVersion the version of the notation
     * @param myIcon    the icon of the notation
     */
    protected NotationNameImpl(String myName, String myVersion, Icon myIcon) {
        name = myName;
        version = myVersion;
        icon = myIcon;
    }

    /**
     * Accessor for the language name.
     *
     * @see org.argouml.notation.NotationName#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * Accessor for the language version.
     *
     * @see org.argouml.notation.NotationName#getVersion()
     */
    public String getVersion() {
        return version;
    }

    /**
     * Gets a textual title for the notation suitable for use
     * in a combo box or other such visual location.
     *
     * @see org.argouml.notation.NotationName#getTitle()
     */
    public String getTitle() {
        String myName = name;
        if (myName.equalsIgnoreCase("uml")) {
            myName = myName.toUpperCase();
        }

        if (version == null || version.equals("")) {
            return myName;
        }
        return myName + " " + version;
    }

    /**
     * Returns an icon for the notation, or null if no icon is available.
     *
     * @see org.argouml.notation.NotationName#getIcon()
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * @see org.argouml.notation.NotationName#getConfigurationValue()
     */
    public String getConfigurationValue() {
        return getNotationNameString(name, version);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getTitle();
    }

    /**
     * @param k1 first part of the given name
     * @param k2 2nd part of the given name
     * @return the notation name string
     */
    static String getNotationNameString(String k1, String k2) {
        if (k2 == null) {
            return k1;
        }
        if (k2.equals("")) {
            return k1;
        }
	return k1 + " " + k2;
    }

    private static void fireEvent(int eventType, NotationName nn) {
	ArgoEventPump.fireEvent(new ArgoNotationEvent(eventType, nn));
    }

    /**
     * Create a NotationName with or without a version.
     * The NotationName is only created if there is no such notation before.
     *
     * @param k1 the 1st part of the notation name
     * @param k2 the 2nd part of the notation name
     * @param icon the icon for the notation
     * @return the newly created or the old NotationName
     */
    static NotationName makeNotation(String k1, String k2, Icon icon) {
	NotationName nn = null;
	nn = findNotation(getNotationNameString(k1, k2));
	if (nn == null) {
	    nn = new NotationNameImpl(k1, k2, icon);
	    notations.add(nn);
	    fireEvent(ArgoEventTypes.NOTATION_ADDED, nn);
	}
        return nn;
    }

    /**
     * Get all of the registered notations.
     *
     * @return an ArrayList with all notations
     */
    static ArrayList getAvailableNotations() {
        return notations;
    }

    /**
     * Finds a NotationName matching the string matching
     * the name of the notation. Returns null if no match.
     *
     * @param s the name string
     * @return the notationName or null
     */
    static NotationName findNotation(String s) {
        ListIterator iterator = notations.listIterator();
        while (iterator.hasNext()) {
	    try {
                NotationName nn = (NotationName) iterator.next();
		if (s.equals(nn.getConfigurationValue())) {
		    return nn;
		}
	    } catch (Exception e) {
	        // TODO: Document why we catch this.
	        LOG.error("Unexpected exception", e);
	    }
	}
	return null;
    }

    /**
     * @see org.argouml.notation.NotationName#sameNotationAs(org.argouml.notation.NotationName)
     */
    public boolean sameNotationAs(NotationName nn) {
        return this.getConfigurationValue().equals(nn.getConfigurationValue());
    }

    /**
     * Finds a NotationName matching the language with no version.
     * Returns null if no match.
     *
     * @param k1 the notation name string
     * @return the notation name
     */
    static NotationName getNotation(String k1) {
        return findNotation(getNotationNameString(k1, null));
    }

    /**
     * Finds a NotationName matching the language and version.
     * Returns null if no match.
     *
     * @param k1 the 1st part of the notation name
     * @param k2 the 2nd part of the notation name
     * @return the notation name
     */
    static NotationName getNotation(String k1, String k2) {
        return findNotation(getNotationNameString(k1, k2));
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleLoaded(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleLoaded(ArgoModuleEvent event) {
        LOG.info ("notation.moduleLoaded(" + event + ")");
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleUnloaded(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleUnloaded(ArgoModuleEvent event) {
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleEnabled(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleEnabled(ArgoModuleEvent event) {
    }

    /**
     * @see org.argouml.application.events.ArgoModuleEventListener#moduleDisabled(org.argouml.application.events.ArgoModuleEvent)
     */
    public void moduleDisabled(ArgoModuleEvent event) {
    }
}
