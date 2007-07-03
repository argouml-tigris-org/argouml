// $Id: ActionAddEventAsTrigger.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.uml.ui.behavior.activity_graphs;

import java.util.Vector;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement;

/**
 * Provide a dialog which helps the user to select one event out of an existing
 * list, which will be used as the trigger of the transition.
 * 
 * @author MarkusK
 * 
 */
public class ActionAddEventAsTrigger extends AbstractActionAddModelElement {

    /**
     * The one and only instance of this class.
     */
    public static final ActionAddEventAsTrigger SINGLETON =
        new ActionAddEventAsTrigger();

    /**
     * Constructor for ActionAddClassifierRoleBase.
     */
    protected ActionAddEventAsTrigger() {
        super();
        setMultiSelect(false);
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
     */
    protected Vector getChoices() {
        Vector vec = new Vector();
        // TODO: the namespace of created events is currently the model. 
        // I think this is wrong, they should be
        // in the namespace of the activitygraph!
//        vec.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(
//                Model.getFacade().getNamespace(getTarget()),
//                Model.getMetaTypes().getEvent()));
        vec.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(
                Model.getFacade().getModel(getTarget()),
                Model.getMetaTypes().getEvent()));

        return vec;
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getSelected()
     */
    protected Vector getSelected() {
        Vector vec = new Vector();
        Object trigger = Model.getFacade().getTrigger(getTarget());
        if (trigger != null)
            vec.add(trigger);
        return vec;
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getDialogTitle()
     */
    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-events");
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(java.util.Vector)
     */
    protected void doIt(Vector selected) {
        Object trans = getTarget();
        if (selected == null || selected.size() == 0) {
            Model.getStateMachinesHelper().setEventAsTrigger(trans, null);
        } else {
            Model.getStateMachinesHelper().setEventAsTrigger(trans,
                    selected.get(0));
        }
    }

}
