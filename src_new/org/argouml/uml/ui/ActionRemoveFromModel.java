// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import javax.swing.JOptionPane;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesHelper;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.presentation.Fig;

/**
 * Action for removing (moving to trash) objects from the model. Objects can be:
 * - Modelelements (NSUML)
 * - Diagrams (argodiagram and it's children)
 * The root model and the last diagram in the project can not be removed. The 
 * reason for this is to prevent problems updating the detailspane and the 
 * navpane. Besides that, it is not possible to make a new root model.
 * 
 * @author original author not known.
 * @author jaap.branderhorst@xs4all.nl extensions
 * @stereotype singleton
 */

public class ActionRemoveFromModel extends UMLChangeAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    /**
     * @deprecated by Linus Tolke as of 0.15.4. Create your own action every
     * time. This will be removed.
     */
    public static ActionRemoveFromModel SINGLETON =
	new ActionRemoveFromModel();

    /**
     * @deprecated by Linus Tolke as of 0.15.4. Use your own logger in your
     * class. This will be removed.
     */
    protected static Logger cat =
        Logger.getLogger(ActionRemoveFromModel.class);
    
    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionRemoveFromModel() {
        super(Translator.localize("action.delete-from-model"));
    }

    protected ActionRemoveFromModel(boolean global) {
        super(Translator.localize("action.delete-from-model"), global);
    }

    /**
     * Only disabled when nothing is selected. Necessary to use since this 
     * option works via the menu too. A user cannot delete the last diagram. 
     * A user cannot delete the root model.
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
        super.shouldBeEnabled();
        int size = 0;
        try {
            Editor ce = Globals.curEditor();
            Vector figs = ce.getSelectionManager().getFigs();
            size = figs.size();
        } catch (Exception e) {
	    // Ignore
        }
        if (size > 0)
            return true;
        Object target = TargetManager.getInstance().getTarget();
        if (target instanceof Diagram) { // we cannot delete the last diagram
            return (ProjectManager.getManager().getCurrentProject()
		    .getDiagrams().size()
		    > 1);
        }
        if (org.argouml.model.ModelFacade.isAModel(target)
	    // we cannot delete the model itself
            && target.equals(ProjectManager.getManager().getCurrentProject()
			     .getModel())) {
            return false;
        }
        if (StateMachinesHelper.getHelper().isTopState(target))
            /* we can not delete a "top" state, 
             * it comes and goes with the statemachine. Issue 2655.
             */
            return false;
        return target != null;
    }

    /**
     * Moves the selected target to the trash bin. Moves the selected target 
     * after the remove to the parent of the selected target (that is: the next
     * level up in the navpane). In case of a diagram the selected target will 
     * be the next diagram in the list with diagrams.
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        Project p = ProjectManager.getManager().getCurrentProject();
        Object[] targets = null;
        if (ae.getSource() instanceof PropPanel) {
            targets =
                new Object[] {
		    TargetManager.getInstance().getModelTarget()
		};
        } else
            targets = getTargets();
        Object target = null;
        for (int i = 0; i < targets.length; i++) {
            target = targets[i];
            if (sureRemove(target)) {
                // remove from the model
                if (target instanceof Fig) {
                    target = ((Fig) target).getOwner();
                }
                p.moveToTrash(target);
                if (target instanceof Diagram) {
                    Diagram firstDiagram = (Diagram) p.getDiagrams().get(0);
                    if (target != firstDiagram)
                        TargetManager.getInstance().setTarget(firstDiagram);
                }

            }
        }
        //      move the pointer to the target in the NavPane to some
        //      other target
        Object newTarget = null;
        target = target instanceof Fig ? ((Fig) target).getOwner() : target;
        if (ModelFacade.isABase(target)) {
            newTarget = ModelFacade.getModelElementContainer(target);
        } else if (ModelFacade.isADiagram(target)) {
            Diagram firstDiagram = (Diagram) p.getDiagrams().get(0);
            if (target != firstDiagram)
                newTarget = firstDiagram;
            else {
                if (p.getDiagrams().size() > 1) {
                    newTarget = p.getDiagrams().get(1);
                } else
                    newTarget = p.getRoot();
            }
        } else {
            newTarget = p.getRoot();
        }       
        if (newTarget != null)
            TargetManager.getInstance().setTarget(newTarget);
        super.actionPerformed(ae);
    }

    /**
     * A utility method that asks the user if he is sure to remove the selected
     * target.<p>
     *
     * @param target
     * @return boolean
     */
    public static boolean sureRemove(Object target) {
        // usage of other sureRemove method is legacy. They should be
        // integrated.
        boolean sure = false;
        if (org.argouml.model.ModelFacade.isAModelElement(target)) {
            sure = sureRemoveModelElement(target);
        } else if (target instanceof UMLDiagram) {
            // lets see if this diagram has some figs on it
            UMLDiagram diagram = (UMLDiagram) target;
            Collection nodes = diagram.getNodes(new Vector());
            Collection edges = diagram.getNodes(new Vector());
            if ((nodes.size() + edges.size()) > 0) {
                // the diagram contains figs so lets ask the user if
                // he/she is sure
                String confirmStr =
                    MessageFormat.format(Translator.localize(
			    "Actions",
			    "optionpane.remove-from-model-confirm-delete"),
					 new Object[] {
					     diagram.getName(), "" 
					 });
		String text =
		    Translator.localize(
			"Actions",
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
            if (ModelFacade.isAModelElement(((Fig) target).getOwner())) {
                sure = sureRemoveModelElement(((Fig) target).getOwner());
            } else
                sure = true;
        }
        return sure;
    }

    /**
     * An utility method that asks the user if he is sure to remove a selected 
     * modelement.<p>
     *
     * @see ActionRemoveFromModel#sureRemove(Object)
     * @param me
     * @return boolean
     */
    public static boolean sureRemoveModelElement(Object/*MModelElement*/ me) {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Project p = ProjectManager.getManager().getCurrentProject();

        int count = p.getPresentationCountFor(me);

        boolean doAsk = false;
        String confirmStr = "";
        if (count > 1) {
            confirmStr +=
		Translator.localize(
		    "Actions",
		    "optionpane.remove-from-model-will-remove-from-diagrams");
            doAsk = true;
        }

        Collection beh = ModelFacade.getBehaviors(me);
        if (beh != null && beh.size() > 0) {
            confirmStr +=
		Translator.localize(
			"Actions",
			"optionpane.remove-from-model-will-remove-subdiagram");
            doAsk = true;
        }

        if (!doAsk) {
            return true;
        }

        String name = ModelFacade.getName(me);
        if (name == null || name.equals("")) {
            name =
		Translator.localize(
			"Actions",
			"optionpane.remove-from-model-anon-element-name");
        }

        confirmStr =
            MessageFormat.format(
                    Translator.localize(
			    "Actions",
			    "optionpane.remove-from-model-confirm-delete"),
		    new Object[] {
			name, confirmStr 
		    });
        int response =
            JOptionPane.showConfirmDialog(
		    pb,
		    confirmStr,
		    Translator.localize(
			"Actions",
			"optionpane.remove-from-model-confirm-delete-title"),
		    JOptionPane.YES_NO_OPTION);

        return (response == JOptionPane.YES_OPTION);
    }

    protected Object[] getTargets() {
        /*
	  Vector figs = null; try { Editor ce = Globals.curEditor();
	  figs = ce.getSelectionManager().getFigs(); } catch
	  (Exception e) { } return figs.size() > 0 ? figs.toArray() :
	  new Object[] {TargetManager.getInstance().getTarget()};
        */
        return TargetManager.getInstance().getTargets().toArray();
    }
} /* end class ActionRemoveFromModel */
