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

package org.argouml.uml.ui;

import javax.swing.Action;

import org.argouml.model.Model;

/**
 * An editable and searchable combobox to edit the multiplicity attribute of
 * some modelelement.
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 5, 2003
 */
public class UMLMultiplicityComboBox2 extends UMLSearchableComboBox {

    /**
     * Constructor for UMLMultiplicityComboBox2.
     * @param arg0 the combobox model
     * @param selectAction the action
     */
    public UMLMultiplicityComboBox2(UMLComboBoxModel2 arg0,
            Action selectAction) {
        super(arg0, selectAction);
    }

    /**
     * On enter, the text the user has filled in the textfield is first checked
     * to see if it's a valid multiplicity. If so then that is the multiplicity
     * to be set. If not, the combobox searches for a multiplicity starting with
     * the given text. If there is no multiplicity starting with the given text,
     * the old value is reset in the comboboxeditor.
     * @see org.argouml.uml.ui.UMLEditableComboBox#doOnEdit(java.lang.Object)
     */
    protected void doOnEdit(Object item) {
        String text = (String) item;
        Object/*MMultiplicity*/ multi = null;
        try {
            multi =
                Model.getDataTypesFactory()
                	.createMultiplicity(text); //new MMultiplicity(text);
        } catch (IllegalArgumentException e) {
            Object o = search(text);
            if (o != null ) {
                multi = o;
            }
        }
        if (multi != null) {
            setSelectedItem(multi);
        } else {
            getEditor().setItem(getSelectedItem());
        }
    }

}
