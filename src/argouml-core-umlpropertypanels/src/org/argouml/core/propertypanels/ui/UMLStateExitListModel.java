/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.core.propertypanels.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * @since Dec 14, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
class UMLStateExitListModel extends UMLModelElementListModel {
	
	private static final Logger LOG = Logger.getLogger(UMLStateExitListModel.class);

    private final Object[] metaTypes = new Object[] {
        Model.getMetaTypes().getCallAction(),
        Model.getMetaTypes().getCreateAction(),
        Model.getMetaTypes().getDestroyAction(),
        Model.getMetaTypes().getReturnAction(),
        Model.getMetaTypes().getSendAction(),
        Model.getMetaTypes().getTerminateAction(),
        Model.getMetaTypes().getUninterpretedAction(),
        Model.getMetaTypes().getActionSequence()
    };
	
	
    /**
     * Constructor for UMLStateExitListModel.
     */
    public UMLStateExitListModel(final Object modelElement) {
        super("exit");
        setTarget(modelElement);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    public void buildModelList() {
        removeAllElements();
        addElement(Model.getFacade().getExit(getTarget()));
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    public boolean isValidElement(Object element) {
        return element == Model.getFacade().getExit(getTarget());
    }
    
    public List<Object> getMetaTypes() {
    	return null;
    }

    public List<Action> getNewActions() {
    	ArrayList<Action> newActions = new ArrayList<Action>();
    	for (Object meta : metaTypes) {
            final String label =
                "button.new-" + Model.getMetaTypes().getName(meta).toLowerCase();
            final Action createAction = new ActionCreateContainedExitAction(
                    meta,
                    getTarget(),
                    label);
            newActions.add(createAction);
    	}
    	return newActions;
    }
    
    
    /**
     * An action to create a model element to be contained by the 
     * target model element.
     *
     * @author Scott Roberts
     */
    public class ActionCreateContainedExitAction
                extends AbstractActionNewModelElement {

        private Object metaType; 

        /**
         * Construct the action.
         * 
         * @param theMetaType the element to be created
         * @param target the container that will own the new element
         */
        public ActionCreateContainedExitAction(
                Object theMetaType, 
                Object target) {
            this(theMetaType, target,
                    "button.new-"
                    + Model.getMetaTypes().getName(theMetaType).toLowerCase());
        }
        
        
        /**
         * Construct the action.
         * 
         * @param theMetaType the element to be created
         * @param target the container that will own the new element
         * @param menuDescr the description for the menu item label.
         */
        public ActionCreateContainedExitAction(
                Object theMetaType, 
                Object target,
                String menuDescr) {
            super(menuDescr);
            
            metaType = theMetaType;
            
            setTarget(target);
        }

        public void actionPerformed(ActionEvent e) {            
            Object t = getTarget();
            Object action = null;
            if (Model.getMetaTypes().getCallAction() == metaType) {
                action = Model.getCommonBehaviorFactory().createCallAction();
            } else if (Model.getMetaTypes().getCreateAction() == metaType) {
                action = Model.getCommonBehaviorFactory().createCreateAction();
            } else if (Model.getMetaTypes().getReturnAction() == metaType) {
                action = Model.getCommonBehaviorFactory().createReturnAction();
            } else if (Model.getMetaTypes().getDestroyAction() == metaType) {
                action = Model.getCommonBehaviorFactory().createDestroyAction();
            } else if (Model.getMetaTypes().getSendAction() == metaType) {
                action = Model.getCommonBehaviorFactory().createSendAction();
            } else if (Model.getMetaTypes().getTerminateAction() == metaType) {
                action = Model.getCommonBehaviorFactory().createTerminateAction();
            } else if (Model.getMetaTypes().getUninterpretedAction() == metaType) {
                action = Model.getCommonBehaviorFactory().createUninterpretedAction();
            } else if (Model.getMetaTypes().getActionSequence() == metaType) {
                action = Model.getCommonBehaviorFactory().createActionSequence();
            } else {
            	throw new IllegalStateException(metaType + " not recognised as an Action");
            }
            Model.getStateMachinesHelper().setExit(t, action);
            TargetManager.getInstance().setTarget(action);
        }
    }
}
