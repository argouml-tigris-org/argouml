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

package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper;
import org.argouml.uml.ui.UMLComboBoxModel2;
import ru.novosoft.uml.MBase;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;

/**
 * The model behind the UMLMessageActivatorComboBox. I don't use the UMLComboBoxModel
 * since this mixes the GUI and the model too much and is much more maintainance 
 * intensive then this implementation.
 */
public class UMLMessageActivatorComboBoxModel extends UMLComboBoxModel2 {
		


    /**
     * Constructor for UMLMessageActivatorComboBoxModel.
     * @param container
     */
    public UMLMessageActivatorComboBoxModel() {
        super("activator", false);
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Object target = getTarget();
        if (target instanceof MMessage) {
            MMessage mes = (MMessage)target;
            removeAllElements();
            // fill the list with items
            setElements(CollaborationsHelper.getHelper().getAllPossibleActivators(mes));
        }
    }

    
    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(ru.novosoft.uml.MBase)
     */
    protected boolean isValidElement(MBase m) {
        return ((m instanceof MMessage)  && 
            m != getTarget() && 
            !((MMessage)(getTarget())).getPredecessors().contains(m) &&
            ((MMessage)m).getInteraction() == ((MMessage)(getTarget())).getInteraction());
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            return ((MMessage)getTarget()).getActivator();
        }
        return null;
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#setTarget(java.lang.Object)
     */
    protected void setTarget(Object target) {
        if (getTarget() instanceof MMessage) {
            MInteraction inter = ((MMessage)getTarget()).getInteraction();
            if (inter != null)
                UmlModelEventPump.getPump().removeModelEventListener(this, inter, "message");
        }   
        super.setTarget(target);
        if (target instanceof MMessage) {
            MInteraction inter = ((MMessage)target).getInteraction();
            if (inter != null)
                UmlModelEventPump.getPump().addModelEventListener(this, inter, "message");
        }
    }

}
