// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: UMLModelElementComboBoxEntry.java
// Classes: UMLModelElementComboBoxEntry
// Original Author: mail@jeremybennett.com
// $Id$

// 26 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created to support
// UMLModelElementComboBoxModel.


package org.argouml.uml.ui;

import org.argouml.uml.*;

import java.lang.*;

import ru.novosoft.uml.foundation.core.*;


/**
 * <p>A general combo box entry for use with drop down combo boxes that list
 *   NSUML modele elements.</p>
 *
 * @author Jeremy Bennett (mail@jeremybennett.com), 26 Apr 2002.
 */


public class UMLModelElementComboBoxEntry implements Comparable {


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>The model element held in this entry.</p>
     */

    private MModelElement _modelElement;


    /**
     * <p>The profile used to format the model element for display.</p>
     */

    private Profile _profile;
    

    /**
     * <p>The display name of this entry.</p>
     */

    private String _displayName;
    

    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * <p>Build a new entry, for the given model element, to be formatted with
     *   the given profile.</p>
     *
     * <p>We save the arguments in instance variables and generate the display
     *   name using the profile.</p>
     *
     * @param modelElement  The model element for this entry.
     *
     * @param profile       The profile to use for formatting
     */

    public UMLModelElementComboBoxEntry(MModelElement modelElement,
                                        Profile       profile) {
        _modelElement = modelElement;
        _profile      = profile;

        if ((modelElement != null) && (profile != null)) {
            _displayName = profile.formatElement(modelElement,
                                                 modelElement.getNamespace());
        }
        else {
            _displayName = "";
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Accessors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Return the name by which the element will be displayed.</p>
     *
     * @return  The display name of the element.
     */

    public String toString() {
        return _displayName;
    }


    /**
     * <p>Get the model element associated with this entry.</p>
     *
     * @return  The model element
     */

    public MModelElement getModelElement() {
        return _modelElement;
    }


    /**
     * <p>Set the model element associated with this entry. This implicitly
     *   requires an update of the display name.</p>
     *
     * @param modelElement  The model element
     */

    public void setModelElement(MModelElement modelElement) {
        _modelElement = modelElement;

        if ((modelElement != null) && (_profile != null)) {
            _displayName = _profile.formatElement(modelElement,
                                                  modelElement.getNamespace());
        }
        else {
            _displayName = "";
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Main methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Compare this entry to another entry.</p>
     *
     * <p>We compare first on display name, then if those are equal we compare
     *   on hashcode.</p>
     *
     * <p>Implemented using the {@link #compare(Object,Object)} method.</p>
     *
     * <p>Provided to comply with the {@link Comparable} interface.</p>
     *
     * @param other  The other entry against which we are comparing.
     *
     * @return       an integer < 0 if the first is "before" the second entry,
     *               0 if they compare equal and an intger > 0 if the first is
     *               "after" the other entry.
     */

    public int compareTo(Object other) throws ClassCastException {
        return compare(this, other);
    }


    /**
     * <p>Compare two entries.</p>
     *
     * <p>We compare first on display name, then if those are equal we compare
     *   on hashcode of the model element.</p>
     *
     * <p>There are some special cases. If other is the same as us, we return
     *   0. The void entry always goes first. We throw an exception if we are
     *   not both of the same class as <code>this</code>./p>
     *
     * @param obj1  The first entry being compared.
     *
     * @param obj2  The second entry being compared.
     *
     * @return       an integer < 0 if the first is "before" the second entry,
     *               0 if they compare equal and an intger > 0 if the first is
     *               "after" the other entry.
     */

    public int compare(Object obj1, Object obj2) throws ClassCastException {

        // Throw an exception if we do not match class.

        if ((obj1.getClass() != getClass()) ||
            (obj2.getClass() != getClass())) {
            throw new ClassCastException(getClass() + ": cannot compare " +
                                         obj1 + " and " + obj2);
        }

        // Are we the same entry, or do we have the same model element

        UMLModelElementComboBoxEntry obj1Entry        = 
            (UMLModelElementComboBoxEntry) obj1;
        MModelElement                obj1ModelElement =
            obj1Entry.getModelElement();

        UMLModelElementComboBoxEntry obj2Entry        = 
            (UMLModelElementComboBoxEntry) obj2;
        MModelElement                obj2ModelElement =
            obj2Entry.getModelElement();

        if ((obj1 == obj2) ||
            (obj1ModelElement == obj2ModelElement)) {
            return 0;
        }

        // The void element always goes first

        if (obj1ModelElement == null) {
            return -1;
        }

        if (obj2ModelElement == null) {
            return 1;
        }

        // Compare display strings and return result if they are not equal

        int nameCompare = obj1Entry.toString().compareTo(obj2Entry.toString());

        if (nameCompare != 0 ) {
            return nameCompare;
        }

        // Display strings are identical, so compare hash codes.

        int obj1Hash = obj1ModelElement.hashCode();
        int obj2Hash = obj2ModelElement.hashCode();

        return (obj1Hash == obj2Hash) ? 0 : ((obj1Hash < obj2Hash) ? -1 : 1);
    }

} /* UMLModelElementComboBoxEntry */



