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
import java.util.*;

/**
 *   This class provides definition and manipulation of notation names.
 *   All notation names will be accessed using the
 *   {@link NotationName} wrapper.
 *
 *   @author Thierry Lach
 *   @since 0.9.4
 */
public class NotationNameImpl 
implements NotationName {

    String _name = null;
    String _version = null;
    
    private static Hashtable _notations = null;

    public NotationNameImpl(String name) {
        this(name, null);
    }

    public NotationNameImpl(String name, String version) {
        _name = name;
        _version = version;
    }

    public String getName() {
        return _name;
    }

    public String getVersion() {
        return _version;
    }

    public String getTitle() {
        return getNotationNameString(_name, _version);
    }

    public String getConfigurationValue() {
        return getNotationNameString(_name, _version);
    }

    public String toString() {
        if (_version == null) return "{NotationNameImpl:" + _name + "}";
        return "{NotationNameImpl:" + _name + " version " + _version + "}";
    }

    public static String getNotationNameString(String k1, String k2) {
        if (k2 == null) return k1;
        if (k2.equals("")) return k1;
	return k1 + "." + k2;
    }

    /** Create a notation name with or without a version.
     */
    public static NotationName makeNotation(String k1, String k2) {
        if (_notations == null) _notations = new Hashtable();
	NotationName nn = null;
	nn = (NotationName)_notations.get(getNotationNameString(k1, k2));
	System.out.println ("k1:" + k1 + " nn:" + nn);
	// new NotationNameImpl(k1);
        return nn;
    }
}


