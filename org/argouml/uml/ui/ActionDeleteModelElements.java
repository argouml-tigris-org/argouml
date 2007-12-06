// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.List;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.ui.ArgoFrame;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.CommentEdge;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.ui.ActionDeleteConcurrentRegion;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigTextEditor;
import org.tigris.gef.undo.UndoableAction;

/**
 * Action for removing objects from the model. 
 * Objects can be Modelelements, Diagrams (argodiagram and it's children),
 * Figs without owner,... 
 */
public class ActionDeleteModelElements extends UndoableAction {

    /**
     * Generated serial version for rev 1.4
     */
    private static final long serialVersionUID = -5728400220151823726L;

    private static ActionDeleteModelElements targetFollower;

    public static ActionDeleteModelElements getTargetFollower() {
        if (targetFollower == null) {
            targetFollower  = new ActionDeleteModelElements();
            TargetManager.getInstance().addTargetListener(new TargetListener() {
                public void targetAdded(TargetEvent e) {
                    setTarget();
                }
                public void targetRemoved(TargetEvent e) {
                    setTarget();
                }

                public void targetSet(TargetEvent e) {
                    setTarget();
                }
                private void setTarget() {
                    targetFollower.setEnabled(targetFollower.shouldBeEnabled());
                }
            });
            targetFollower.setEnabled(targetFollower.shouldBeEnabled());
        }
        return targetFollower;
    }
    
    private static final Logger LOG =
        Logger.getLogger(ActionDeleteModelElements.class);

    /**
     * Constructor.
     */
    public ActionDeleteModelElements() {
        super(Translator.localize("action.delete-from-model"),
                ResourceLoaderWrapper.lookupIcon("action.delete-from-model"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.delete-from-model"));
        putValue(Action.SMALL_ICON,
                ResourceLoaderWrapper.lookupIcon("Delete"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        KeyboardFocusManager focusManager =
            KeyboardFocusManager.getCurrentKeyboardFocusManager();
        Component focusOwner = focusManager.getFocusOwner();
        if (focusOwner instanceof FigTextEditor) {
            // TODO: Probably really want to cancel editing
            //((FigTextEditor) focusOwner).cancelEditing();
            ((FigTextEditor) focusOwner).endEditing();
        } else if (focusOwner instanceof JTable) {
            JTable table = (JTable) focusOwner;
            if (table.isEditing()) {
                TableCellEditor ce = table.getCellEditor();
                if (ce != null) {
                    ce.cancelCellEditing();
                }
            }
        }

        Project p = ProjectManager.getManager().getCurrentProject();
        Object[] targets = TargetManager.getInstance().getTargets().toArray();
        /* This next line fixes issue 4276: */
        TargetManager.getInstance().setTarget(null);
        Object target = null;
        for (int i = targets.length - 1; i >= 0; i--) {
            target = targets[i];
            try {
                if (sureRemove(target)) {
                    // remove from the model
                    if (target instanceof Fig) {
                        Object owner = ((Fig) target).getOwner();
                        if (owner != null) {
                            target = owner;
                        }
                    }
                    if (Model.getFacade().isAConcurrentRegion(target)) {
                        new ActionDeleteConcurrentRegion()
                            .actionPerformed(ae);
                    } else {
                        p.moveToTrash(target);
                    }
                }
            } catch (InvalidElementException e) {
                LOG.debug("Model element deleted twice - ignoring 2nd delete");
            }
        }
    }

    /**
     * A utility method that asks the user if he is sure to remove the selected
     * target.<p>
     *
     * @param target the object that will be removed
     * @return boolean
     */
    public static boolean sureRemove(Object target) {
        // usage of other sureRemove method is legacy. They should be
        // integrated.
        boolean sure = false;
        if (Model.getFacade().isAModelElement(target)) {
            sure = sureRemoveModelElement(target);
        } else if (Model.getFacade().isAUMLElement(target)) {
            // It is a UML element that is not a ModelElement
            sure = true;
        } else if (target instanceof ArgoDiagram) {
            // lets see if this diagram has some figs on it
            ArgoDiagram diagram = (ArgoDiagram) target;
            if (diagram.getNodes().size() + diagram.getEdges().size() != 0) {
                // the diagram contains figs so lets ask the user if
                // he/she is sure
                String confirmStr =
                    MessageFormat.format(Translator.localize(
                        "optionpane.remove-from-model-confirm-delete"),
                        new Object[] {
                            diagram.getName(), "",
                        });
                String text =
                    Translator.localize(
                        "optionpane.remove-from-model-confirm-delete-title");
                int response =
                    JOptionPane.showConfirmDialog(ArgoFrame.getInstance(),
                          confirmStr,
                          text,
                          JOptionPane.YES_NO_OPTION);
                sure = (response == JOptionPane.YES_OPTION);
            } else { // no content of diagram
                sure = true;
            }
        } else if (target instanceof Fig) {
            // we can delete figs like figrects now too
            if (Model.getFacade().isAModelElement(((Fig) target).getOwner())) {
                sure = sureRemoveModelElement(((Fig) target).getOwner());
            } else {
                sure = true;
            }
        } else if (target instanceof CommentEdge) {
            // we can delete CommentEdge now too thanks to issue 3643.
            sure = true;
        }
        return sure;
    }

    /**
     * An utility method that asks the user if he is sure to remove a selected
     * model element.
     *
     * @param me the modelelement that may be removed
     * @return boolean
     */
    protected static boolean sureRemoveModelElement(Object me) {
        Project p = ProjectManager.getManager().getCurrentProject();

        int count = p.getPresentationCountFor(me);

        boolean doAsk = false;
        String confirmStr = "";
        if (count > 1) {
            confirmStr += Translator.localize(
                "optionpane.remove-from-model-will-remove-from-diagrams");
            doAsk = true;
        }

        /* TODO: If a namespace with sub-classdiagrams is deleted, then { 
            confirmStr +=
                Translator.localize(
                    "optionpane.remove-from-model-will-remove-subdiagram");
            doAsk = true;
        }*/

        if (!doAsk) {
            return true;
        }

        String name = Model.getFacade().getName(me);
        if (name == null || name.equals("")) {
            name = Translator.localize(
                "optionpane.remove-from-model-anon-element-name");
        }

        confirmStr =
            MessageFormat.format(Translator.localize(
                "optionpane.remove-from-model-confirm-delete"),
                new Object[] {
                    name, confirmStr,
                });
        int response =
            JOptionPane.showConfirmDialog(
                    ArgoFrame.getInstance(),
                    confirmStr,
                    Translator.localize(
                    "optionpane.remove-from-model-confirm-delete-title"),
                    JOptionPane.YES_NO_OPTION);

        return (response == JOptionPane.YES_OPTION);
    }
    
    /**
     * @return true if the tool should be enabled
     */
    public boolean shouldBeEnabled() {
        int size = 0;
        try {
            Editor ce = Globals.curEditor();
            List<Fig> figs = ce.getSelectionManager().getFigs();
            size = figs.size();
        } catch (Exception e) {
            // TODO: This catch block needs to be narrower and do something
            // with the caught exception - tfm 20071120
            // Ignore
        }
        if (size > 0) {
            return true;
        }
        Object target = TargetManager.getInstance().getTarget();
        if (target instanceof ArgoDiagram) { 
            // we cannot delete the last diagram
            return (ProjectManager.getManager().getCurrentProject()
                .getDiagramList().size() > 1);
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
