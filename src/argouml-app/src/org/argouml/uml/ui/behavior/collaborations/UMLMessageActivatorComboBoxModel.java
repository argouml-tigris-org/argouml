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

package org.argouml.uml.ui.behavior.collaborations;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;

/**
 * The model behind the UMLMessageActivatorComboBox.
 */
public class UMLMessageActivatorComboBoxModel extends UMLComboBoxModel2 {

    private Object interaction = null;

    /**
     * Constructor for UMLMessageActivatorComboBoxModel.
     */
    public UMLMessageActivatorComboBoxModel() {
        super("activator", false);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Object target = getTarget();
        if (Model.getFacade().isAMessage(target)) {
            Object mes = target;
            removeAllElements();
            // fill the list with items
            setElements(Model.getCollaborationsHelper()
                    .getAllPossibleActivators(mes));
        }
    }


    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object m) {
        return ((Model.getFacade().isAMessage(m))
                && m != getTarget()
                && !Model.getFacade().getPredecessors((getTarget())).contains(m)
                && Model.getFacade().getInteraction(m)
                    == Model.getFacade().getInteraction((getTarget())));
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            return Model.getFacade().getActivator(getTarget());
        }
        return null;
    }
    
    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#setTarget(java.lang.Object)
     */
    protected void setTarget(Object target) {
        if (Model.getFacade().isAMessage(getTarget())) {
            if (interaction != null) {
                Model.getPump().removeModelEventListener(
                    this,
                    interaction,
                    "message");
            }
        }
        super.setTarget(target);
        if (Model.getFacade().isAMessage(target)) {
            interaction = Model.getFacade().getInteraction(target);
            if (interaction != null) {
                Model.getPump().addModelEventListener(
                    this,
                    interaction,
                    "message");
            }
        }
    }
}

