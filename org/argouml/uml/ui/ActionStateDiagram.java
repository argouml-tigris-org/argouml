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

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.tigris.gef.undo.UndoableAction;

/**
 * Action to create a new statechart diagram.
 */
public class ActionStateDiagram extends UndoableAction {

    /**
     * Constructor.
     */
    public ActionStateDiagram() {
        super(Translator.localize("action.state-diagram"),
                ResourceLoaderWrapper.lookupIcon("action.state-diagram"));
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("action.state-diagram"));
    }

    public void actionPerformed(ActionEvent e) {
        Project p = ProjectManager.getManager().getCurrentProject();

        super.actionPerformed(e);
        UMLDiagram diagram = createDiagram();
        p.addMember(diagram);
        //TODO: make the explorer listen to project member property
        //changes...  to eliminate coupling on gui.
        ExplorerEventAdaptor.getInstance().modelElementAdded(
                diagram.getNamespace());
        TargetManager.getInstance().setTarget(diagram);
    }

    private UMLDiagram createDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        Object target = TargetManager.getInstance().getModelTarget();
        Object machine = null;
        Object namespace = p.getRoot(); // the root model
        if (Model.getStateMachinesHelper().isAddingStatemachineAllowed(
              target)) {
            /* The target is a valid context. */
            machine = Model.getStateMachinesFactory().buildStateMachine(target);
        } else {
            machine = Model.getStateMachinesFactory().createStateMachine();
            if (Model.getFacade().isANamespace(target)) {
                namespace = target;
            }
            Model.getCoreHelper().setNamespace(machine, namespace);
            Model.getStateMachinesFactory()
                    .buildCompositeStateOnStateMachine(machine);
        }
        
        return (UMLDiagram) DiagramFactory.getInstance().createDiagram(
                UMLStateDiagram.class,
                Model.getFacade().getNamespace(machine),
                machine);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5197718695001757808L;

} /* end class ActionStateDiagram */
