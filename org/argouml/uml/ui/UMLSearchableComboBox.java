// $Id$
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
import org.argouml.model.ModelFacade;

//import ru.novosoft.uml.MBase;
//import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * A searchable combobox. Searchable means that the user has only to type the
 * starting of a modelelement name to select that modelelement. The first
 * modelelement that conforms to the typed text is selected.
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
     * Searches amongst the modelelements (the elements in the listmodel) for an
     * item that conforms to the parameter item. If such an element is a
     * MModelelement, the name should start with the item (which is a String).
     * Otherwise the text that is shown in the combobox should start with the
     * text. As the element is found, this is made to the selected item.
     * @see org.argouml.uml.ui.UMLEditableComboBox#doIt(java.lang.Object)
     */
    protected void doOnEdit(Object item) {
        Object element = search(item);
        if (element != null) {
            setSelectedItem(element);
        }
    }
    
    /**
     * Does the actual searching. Returns the item found or null if there is no
     * item found.
     * @param item
     * @return Object
     */
    protected Object search(Object item) {
        String text = (String) item;
        ComboBoxModel model = getModel();
        for (int i = 0; i < model.getSize(); i++) {
            Object element = model.getElementAt(i);
            if (org.argouml.model.ModelFacade.isABase(element)) {
                if (getRenderer() instanceof UMLListCellRenderer2) {
                    String labelText = ((UMLListCellRenderer2) getRenderer()).makeText(element);
                    if (labelText != null && labelText.startsWith(text)) {                        
                        return element;
                    }
                }
                if (org.argouml.model.ModelFacade.isAModelElement(element)) {
                    Object/*MModelElement*/ elem = element;
                    String name = ModelFacade.getName(elem);
                    if (name != null && name.startsWith(text)) {
                        return element;
                    }
                }
            }

        }
        return null;
    }

}