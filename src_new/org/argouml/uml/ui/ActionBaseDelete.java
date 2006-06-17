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

package org.argouml.uml.ui;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Collection;

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
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.static_structure.ui.CommentEdge;
import org.argouml.uml.diagram.ui.ActionDeleteConcurrentRegion;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigTextEditor;
import org.tigris.gef.undo.UndoableAction;

/**
 * Action for removing (moving to trash) objects from the model. 
 * Objects can be Modelelements, Diagrams (argodiagram and it's children),
 * Figs without owner,... <p>
 * 
 * The root model and the last diagram in the project can not be removed. The
 * reason for this is to prevent problems updating the detailspane and the
 * explorer. Besides that, it is not possible to make a new root model.
 * All this is taken care of in the class Project.
 *
 * @author original author not known.
 * @author jaap.branderhorst@xs4all.nl extensions
 */
public abstract class ActionBaseDelete extends UndoableAction {
    
    private static final Logger LOG = Logger.getLogger(ActionBaseDelete.class);

    /**
     * Constructor.
     */
    public ActionBaseDelete() {
        super(Translator.localize("action.delete-from-model"),
                ResourceLoaderWrapper.lookupIcon("action.delete-from-model"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.delete-from-model"));
        putValue(Action.SMALL_ICON,
                ResourceLoaderWrapper.lookupIcon("Delete"));
    }

    /**
     * Deletes the selected target.

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
        Object[] targets = getTargets();
        Object target = null;
        for (int i = targets.length - 1; i >= 0; i--) {
            target = targets[i];
            try {
                if (sureRemove(target)) {
                    // remove from the model
                    if (target instanceof Fig) {
                        target = ((Fig) target).getOwner();
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
        } else if (target instanceof UMLDiagram) {
            // lets see if this diagram has some figs on it
            UMLDiagram diagram = (UMLDiagram) target;
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
                    JOptionPane.showConfirmDialog(ProjectBrowser.getInstance(),
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
     * @see ActionBaseDelete#sureRemove(Object)
     * @param me the modelelement that may be removed
     * @return boolean
     */
    protected static boolean sureRemoveModelElement(Object me) {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();

        int count = p.getPresentationCountFor(me);

        boolean doAsk = false;
        String confirmStr = "";
        if (count > 1) {
            confirmStr +=
		Translator.localize(
		    "optionpane.remove-from-model-will-remove-from-diagrams");
            doAsk = true;
        }

        Collection beh = Model.getFacade().getBehaviors(me);
        if (beh != null && beh.size() > 0) {
            confirmStr +=
		Translator.localize(
			"optionpane.remove-from-model-will-remove-subdiagram");
            doAsk = true;
        }

        if (!doAsk) {
            return true;
        }

        String name = Model.getFacade().getName(me);
        if (name == null || name.equals("")) {
            name =
		Translator.localize(
			"optionpane.remove-from-model-anon-element-name");
        }

        confirmStr =
            MessageFormat.format(
                    Translator.localize(
			    "optionpane.remove-from-model-confirm-delete"),
		    new Object[] {
			name, confirmStr,
		    });
        int response =
            JOptionPane.showConfirmDialog(
		    pb,
		    confirmStr,
		    Translator.localize(
			"optionpane.remove-from-model-confirm-delete-title"),
		    JOptionPane.YES_NO_OPTION);

        return (response == JOptionPane.YES_OPTION);
    }

    /**
     * @return the complete array of targets
     */
    protected abstract Object[] getTargets();

} /* end class ActionRemoveFromModel */
