/**
 * Created on Sep 22, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.Vector;

import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MParameter;

/**
 * @author Jaap
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class UMLTypeComboBox extends UMLComboBox2 {

    /**
     * Constructor for UMLTypeComboBox.
     * @param container
     * @param arg0
     */
    public UMLTypeComboBox(
        UMLUserInterfaceContainer container) {
        super(container, new UMLTypeModel(container));
    }
    
    public UMLTypeComboBox(
        UMLUserInterfaceContainer container, String propertySetName) {
        super(container, new UMLTypeModel(container, propertySetName));
    } 

    /**
     * @see org.argouml.uml.ui.UMLComboBox2#doIt(ActionEvent)
     */
    protected void doIt(ActionEvent event) {
        Object o = getModel().getElementAt(getSelectedIndex());
        if (o != null) {
            MClassifier type = (MClassifier)o;
            Object target = getContainer().getTarget();
            if (target instanceof MAttribute) {
                ((MAttribute)target).setType(type);
            } else
            if (target instanceof MParameter) {
                ((MParameter)target).setType(type);
            } else
            if (target instanceof MAssociationEnd) {
                ((MAssociationEnd)target).setType(type);
            }    
        }
    }

}
