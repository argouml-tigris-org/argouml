// $Id: UMLModelElementOrderedListModel2.java 12803 2007-06-09 20:08:48Z tfmorris $
// Copyright (c) 2004-2007 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.AbstractActionRemoveElement;

/**
 * This class resembles UMLModelElementListModel2, but is for those associations
 * in the metamodel (see UML standard) that have a {ordered} constraint.
 * <p>
 *
 * This adds the functionality of a popup menu with the items "Move Up",
 * "Move Down", "Move to Top", and "Move to Bottom".
 *
 * @author Michiel
 */
public abstract class UMLModelElementOrderedListModel extends
        UMLModelElementListModel {

    /**
     * The constructor.
     *
     * @param name
     *            the name
     */
    public UMLModelElementOrderedListModel(
            final String name, 
            final Object theMetaType, 
            final AbstractActionAddModelElement2 addAction, 
            final AbstractActionNewModelElement newAction, 
            final AbstractActionRemoveElement removeAction) {
        super(name, theMetaType, addAction, newAction, removeAction);
    }

    /**
     * The constructor.
     *
     * @param name
     *            the name
     */
    public UMLModelElementOrderedListModel(String name) {
        super(name);
    }

    /**
     * The constructor.
     *
     * @param name
     *            the name
     */
    public UMLModelElementOrderedListModel(
            final String name,
            final boolean showIcon,
            final boolean showPath) {
        super(name, showIcon, showPath);
    }

    /**
     * The constructor.
     *
     * @param name
     *            the name
     */
    public UMLModelElementOrderedListModel(
            final String name,
            final boolean showIcon,
            final boolean showPath,
            final Object metaType) {
        super(name, showIcon, showPath, metaType);
    }
}
