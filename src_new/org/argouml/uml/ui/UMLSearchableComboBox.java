// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// $Id$
package org.argouml.uml.ui;

import javax.swing.ComboBoxModel;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * 
 * @author jaap.branderhorst@xs4all.nl	
 * @since Jan 5, 2003
 */
public class UMLSearchableComboBox extends UMLEditableComboBox {

    /**
     * Constructor for UMLSearchableComboBox.
     * @param model
     * @param selectAction
     * @param showIcon
     */
    public UMLSearchableComboBox(UMLComboBoxModel2 model, UMLAction selectAction, boolean showIcon) {
        super(model, selectAction, showIcon);
    }

    /**
     * Constructor for UMLSearchableComboBox.
     * @param arg0
     * @param selectAction
     */
    public UMLSearchableComboBox(UMLComboBoxModel2 arg0, UMLAction selectAction) {
        this(arg0, selectAction, false);
    }

    /**
     * @see org.argouml.uml.ui.UMLEditableComboBox#doIt(java.lang.Object)
     */
    protected void doIt(Object item) {
        String text = (String) item;
        ComboBoxModel model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Object element = model.getElementAt(i);
            if (element instanceof MBase) {
                if (getRenderer() instanceof UMLListCellRenderer2) {
                    String labelText = ((UMLListCellRenderer2) getRenderer()).makeText((MBase) element);
                    if (labelText != null && labelText.startsWith(text)) {
                        model.setSelectedItem(element);
                        break;
                    }
                }
                if (element instanceof MModelElement) {
                    MModelElement elem = (MModelElement) element;
                    String name = elem.getName();
                    if (name != null && name.startsWith(text)) {
                        model.setSelectedItem(element);
                        break;
                    }
                }
            }

        }

    }

}
