// $Id$
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

package org.argouml.application.notation;
import org.argouml.application.api.*;
import org.argouml.application.events.*;
import java.util.*;
import javax.swing.*;

/**
 *   This class provides definition and manipulation of notation names.
 *   All notation names will be accessed using the
 *   {@link NotationName} wrapper.
 *
 *   Not mutable!
 *
 *   @author Thierry Lach
 *   @since 0.9.4
 */
public class NotationNameImpl
    implements NotationName, ArgoModuleEventListener
{

    String _name = null;
    String _version = null;
    Icon _icon = null;

    private static ArrayList _notations = new ArrayList();

    /** A notation without a version or icon.
     */
    protected NotationNameImpl(String name) {
        this(name, null, null);
    }

    /** A notation without a version and with an icon.
     */
    protected NotationNameImpl(String name, Icon icon) {
        this(name, null, icon);
    }

    /** A notation with a version and no icon.
     */
    protected NotationNameImpl(String name, String version) {
        this(name, version, null);
    }

    /** A notation with a version and an icon.
     */
    protected NotationNameImpl(String name, String version, Icon icon) {
        _name = name;
        _version = version;
        _icon = icon;
    }

    /** Accessor for the language name
     */
    public String getName() {
        return _name;
    }

    /** Accessor for the language version
     */
    public String getVersion() {
        return _version;
    }

    /** Gets a textual title for the notation suitable for use
     *  in a combo box or other such visual location.
     */
    public String getTitle() {
        // TODO:  Currently this does not
	//                   differentiate from the configuration
	//                   value.
        return getNotationNameString(_name, _version);
    }

    /** Returns an icon for the notation, or null if no icon is available.
     */
    public Icon getIcon() {
        return _icon;
    }

    public String getConfigurationValue() {
        return getNotationNameString(_name, _version);
    }

    public String toString() {
        return getNotationNameString(_name, _version);
    }

    /*public String toString() {
        if (_version == null) return "{NotationNameImpl:" + _name + "}";
        return "{NotationNameImpl:" + _name + " version " + _version + "}";
    }*/

    public static String getNotationNameString(String k1, String k2) {
        if (k2 == null) return k1;
        if (k2.equals("")) return k1;
	return k1 + "." + k2;
    }

    private static void fireEvent(int eventType, NotationName nn) {
	ArgoEventPump.fireEvent(new ArgoNotationEvent(eventType, nn));
    }

    /** Create a NotationName with or without a version.
     * The NotationName is only created if there is no such notation before.
     * @returns the newly created or the old NotationName
     */
    public static NotationName makeNotation(String k1, String k2, Icon icon) {
	NotationName nn = null;
	nn = findNotation(getNotationNameString(k1, k2));
	if (nn == null) {
	    nn = (NotationName) new NotationNameImpl(k1, k2, icon);
	    _notations.add(nn);
	    fireEvent(ArgoEventTypes.NOTATION_ADDED, nn);
	}
        return nn;
    }

    /** Get all of the registered notations.
     */
    public static ArrayList getAvailableNotations() {
        return _notations;
    }

    /** Finds a NotationName matching the configuration string.
     *  Returns null if no match.
     */
    public static NotationName findNotation(String s) {
        ListIterator iterator = _notations.listIterator();
        while (iterator.hasNext()) {
	    try {
                NotationName nn = (NotationName) iterator.next();
		if (s.equals(nn.getConfigurationValue())) {
		    return nn;
		}
	    }
	    catch (Exception e) {
	        Argo.log.error ("Unexpected exception", e);
	    }
	}
	return null;
    }

    public boolean equals(NotationName nn) {
        return this.getConfigurationValue().equals(nn.getConfigurationValue());
    }

    /** Finds a NotationName matching the language with no version.
     *  Returns null if no match.
     */
    public static NotationName getNotation(String k1) {
        return findNotation(getNotationNameString(k1, null));
    }

    /** Finds a NotationName matching the language and version.
     *  Returns null if no match.
     */
    public static NotationName getNotation(String k1, String k2) {
        return findNotation(getNotationNameString(k1, k2));
    }

    public void moduleLoaded(ArgoModuleEvent event) {
        Argo.log.info ("notation.moduleLoaded(" + event + ")");
    }

    public void moduleUnloaded(ArgoModuleEvent event) {
    }

    public void moduleEnabled(ArgoModuleEvent event) {
    }

    public void moduleDisabled(ArgoModuleEvent event) {
    }
}
