// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;

/**
 * Class to register all Actions that we want to manage as singletons.
 * 
 * @author Tom Morris
 *
 */
public class Actions implements TargetListener {

    /**
     * The action to remove the current selected Figs from the diagram.
     */
    private final ActionRemoveFromDiagram removeFromDiagram =
        new ActionRemoveFromDiagram(
                Translator.localize("action.remove-from-diagram"));
    
    private final Action addAttributeAction = new ActionAddAttribute();

    private final Action addOperationAction = new ActionAddOperation();

    private final Action addEnumerationLiteralAction = 
        new ActionAddEnumerationLiteral();
    
    private final Action addExistingNodeAction = new ActionAddExistingNode();
    
    private AbstractAction deleteAction = new ActionDeleteModelElements();


    private static Actions instance;

    private Actions() {
        TargetManager.getInstance().addTargetListener(this);
    }

    /**
     * @return the Actions singleton
     */
    public static Actions getInstance() {
        if (instance == null) {
            instance = new Actions();
        }
        return instance;
    }
    

    /**
     * Get the Action class for creating and adding a new attribute
     * to the single selected target (or its owner).
     * @return the action
     */
    public Action getAddAttributeAction() {
        return addAttributeAction;
    }

    /**
     * Get the Action class for creating and adding a new operation
     * to the single selected target (or its owner).
     * @return the action
     */
    public Action getAddOperationAction() {
        return addOperationAction;
    }

    /**
     * Get the Action for creating and adding a new EnumerationLiteral for
     * the single selected target (or its owner).
     * 
     * @return the action
     */
    public Action getAddEnumerationLiteralAction() {
        return addEnumerationLiteralAction;
    }

    /**
     * Get the Action for adding the current target to the current diagram.
     * 
     * @return the action
     */
    public Action getAddExistingNodeAction() {
        return addExistingNodeAction;
    }

    /**
     * Get the action that removes selected figs from the diagram.
     * @return the remove from diagram action.
     */
    public static AbstractAction getRemoveFromDiagramAction() {
        return getInstance().removeFromDiagram;
    }
    
    /**
     * Get the Action for deleting the target list.
     * @return the action
     */
    public static AbstractAction getDeleteAction() {
        return getInstance().deleteAction;
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        targetChanged(e);
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        targetChanged(e);   
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        targetChanged(e);
    }
    
    private void targetChanged(TargetEvent e) {
        Object[] targets = e.getNewTargets();
        boolean addAttributeEnabled;
        if (targets.length == 1) {
            Object target = determineModelTarget(targets[0]);
            if ((Model.getFacade().isAClass(target)
                    || Model.getFacade().isAUseCase(target)
                    || (Model.getFacade().isAFeature(target)
                        && Model.getFacade().isAClass(
                            Model.getFacade().getOwner(target)))
                    || (Model.getFacade().isAFeature(target)
                        && Model.getFacade().isAUseCase(
                            Model.getFacade().getOwner(target)))
                    || Model.getFacade().isAAssociationEnd(target))) {
                addAttributeEnabled = true;
            } else {
                addAttributeEnabled = false;
            }
        } else {
            addAttributeEnabled = false;
        }

        boolean addOperationEnabled;
        if (targets.length == 1) {
            Object target = determineModelTarget(targets[0]);
            if ((Model.getFacade().isAClassifier(target)
                    || Model.getFacade().isAFeature(target))
                    && !Model.getFacade().isASignal(target)) {
                addOperationEnabled = true;
            } else {
                addOperationEnabled = false;
            }
        } else {
            addOperationEnabled = false;
        }

        addAttributeAction.setEnabled(addAttributeEnabled);
        addOperationAction.setEnabled(addOperationEnabled);
        addExistingNodeAction.setEnabled(addExistingNodeAction.isEnabled());
    }
    
    private void targetChanged2(TargetEvent e) {
        Object[] targets = e.getNewTargets();
        boolean addAttributeEnabled;
        if (targets.length == 1) {
            Object target = determineModelTarget(targets[0]);
            if ((Model.getFacade().isAClass(target)
                    || Model.getFacade().isAUseCase(target)
                    || (Model.getFacade().isAFeature(target)
                        && Model.getFacade().isAClass(
                            Model.getFacade().getOwner(target)))
                    || (Model.getFacade().isAFeature(target)
                        && Model.getFacade().isAUseCase(
                            Model.getFacade().getOwner(target)))
                    || Model.getFacade().isAAssociationEnd(target))) {
                addAttributeEnabled = true;
            } else {
                addAttributeEnabled = false;
            }
        } else {
            addAttributeEnabled = false;
        }

        boolean addOperationEnabled;
        if (targets.length == 1) {
            Object target = determineModelTarget(targets[0]);
            if ((Model.getFacade().isAClassifier(target)
                    || Model.getFacade().isAFeature(target))
                    && !Model.getFacade().isASignal(target)) {
                addOperationEnabled = true;
            } else {
                addOperationEnabled = false;
            }
        } else {
            addOperationEnabled = false;
        }
        
        addAttributeAction.setEnabled(addAttributeEnabled);
        addOperationAction.setEnabled(addOperationEnabled);
        deleteAction.setEnabled(isDeleteAllowed());

    }
    
    /**
     * Calculates the modeltarget.
     * @param target The target to calculate the modeltarget for
     * @return The modeltarget
     */
    private Object determineModelTarget(Object target) {
        if (target instanceof Fig) {
            Object owner = ((Fig) target).getOwner();
            if (Model.getFacade().isAModelElement(owner)) {
                target = owner;
            }
        }
        return target instanceof UMLDiagram
            || Model.getFacade().isAModelElement(target) ? target : null;

    }

    
    /**
     * Determine if the current selected targets should allow enablement of
     * the delete action.
     * 
     * TODO: Are all new dependencies needed?: GEF, ProjectManager, Project
     * @return true to enable delete
     */
    private boolean isDeleteAllowed() {
        int size = 0;
        try {
            Editor ce = Globals.curEditor();
            Vector figs = ce.getSelectionManager().getFigs();
            size = figs.size();
        } catch (Exception e) {
            // Ignore
        }
        if (size > 0) {
            return true;
        }
        Object target = TargetManager.getInstance().getTarget();
        if (target instanceof Diagram) { // we cannot delete the last diagram
            return (ProjectManager.getManager().getCurrentProject()
                    .getDiagrams().size() > 1);
        }
        if (Model.getFacade().isAModel(target)
                // we cannot delete the model itself
                && target.equals(ProjectManager.getManager().getCurrentProject()
                        .getModel())) {
            return false;
        }
        if (Model.getFacade().isAAssociationEnd(target)) {
            return Model.getFacade().getOtherAssociationEnds(target).size() > 1;
        }
        if (Model.getStateMachinesHelper().isTopState(target)) {
            /* we can not delete a "top" state,
             * it comes and goes with the statemachine. Issue 2655.
             */
            return false;
        }
        return target != null;
    }

}
