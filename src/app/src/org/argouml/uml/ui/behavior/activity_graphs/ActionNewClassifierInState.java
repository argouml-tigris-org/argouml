// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.tigris.gef.undo.UndoableAction;

/**
 * This Action creates a new ClassifierInState for a ObjectFlowState. <p>
 * 
 * It is activated by a button on the properties panel, 
 * and hence has an icon, tooltip and needs to enable/disable itself 
 * depending on the model: A ClassifierInState needs at least one state,
 * so the "type" of the ObjectFlowState needs to be a Classifier 
 * with a Statemachine with at least one state (top-states do not count).
 * 
 * @author Michiel
 */
class ActionNewClassifierInState extends UndoableAction {
    
    private Object choiceClass = Model.getMetaTypes().getState();
    
    /**
     * Constructor.
     */
    public ActionNewClassifierInState() {
        super();
    }
    
    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object ofs = TargetManager.getInstance().getModelTarget();
        if (Model.getFacade().isAObjectFlowState(ofs)) {
            Object type = Model.getFacade().getType(ofs);
            if (Model.getFacade().isAClassifierInState(type)) {
                type = Model.getFacade().getType(type);
            }
            if (Model.getFacade().isAClassifier(type)) {
                Collection c = Model.getModelManagementHelper()
                    .getAllModelElementsOfKindWithModel(type, choiceClass);
                Collection states = new ArrayList(c);
                PropPanelObjectFlowState.removeTopStateFrom(states);

                if (states.size() < 1) return;
                Object state = pickNicestStateFrom(states);
                if (state != null) {
                    states.clear();
                    states.add(state);
                }
                super.actionPerformed(e);
                Object cis = Model.getActivityGraphsFactory()
                    .buildClassifierInState(type, states);
                Model.getCoreHelper().setType(ofs, cis);
                TargetManager.getInstance().setTarget(cis);
            }
        }
    }
    
    /*
     * @see javax.swing.AbstractAction#isEnabled()
     */
    public boolean isEnabled() {
        boolean isEnabled = false;
        Object t = TargetManager.getInstance().getModelTarget();
        if (Model.getFacade().isAObjectFlowState(t)) {
            Object type = Model.getFacade().getType(t);
            if (Model.getFacade().isAClassifier(type)) {
                if (!Model.getFacade().isAClassifierInState(type)) {
                    Collection states = Model.getModelManagementHelper()
                        .getAllModelElementsOfKindWithModel(type, 
                                choiceClass);
                    if (states.size() > 0) {
                        isEnabled = true;
                    }
                }
            }
        }
        return isEnabled;
    }
    
    /**
     * Pick the "nicest" state from the collection. <p>
     * 
     * We distinct between the following levels of "nice": <ul>
     * <li> named simple state (excluding objectflowstate)
     * <li> named composite state (excluding submachinestate) 
     * <li> unnamed simple state (excluding objectflowstate)
     * <li> unnamed composite state (excluding submachinestate)
     * <li> any other
     * 
     * @param states the collection with states
     * @return the nicest state
     */
    private Object pickNicestStateFrom(Collection states) {
        if (states.size() < 2) return states.iterator().next();
        Collection simples = new ArrayList();
        Collection composites = new ArrayList();
        Iterator i;

        i = states.iterator();
        while (i.hasNext()) {
            Object st = i.next();
            String name = Model.getFacade().getName(st);
            if (Model.getFacade().isASimpleState(st)
                    && !Model.getFacade().isAObjectFlowState(st)) {
                simples.add(st);
                if (name != null
                        && (name.length() > 0)) {
                    return st;
                }
            }
        }

        i = states.iterator();
        while (i.hasNext()) {
            Object st = i.next();
            String name = Model.getFacade().getName(st);
            if (Model.getFacade().isACompositeState(st)
                    && !Model.getFacade().isASubmachineState(st)) {
                composites.add(st);
                if (name != null
                        && (name.length() > 0)) {
                    return st;
                }
            }
        }

        if (simples.size() > 0) {
            return simples.iterator().next();
        }
        if (composites.size() > 0) {
            return composites.iterator().next();
        }
        return states.iterator().next();
    }
}