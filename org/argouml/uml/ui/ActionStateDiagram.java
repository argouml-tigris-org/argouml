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

import java.util.Collection;
import java.util.Iterator;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;

/**
 * Action to create a new statechart diagram.
 */
public class ActionStateDiagram extends ActionNewDiagram {

    /**
     * Constructor.
     */
    public ActionStateDiagram() {
        super("action.state-diagram");
    }

    /*
     * @see org.argouml.uml.ui.ActionNewDiagram#createDiagram()
     */
    protected ArgoDiagram createDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        Object target = TargetManager.getInstance().getModelTarget();
        Object machine = null;
        Object namespace = p.getRoot(); // the root model
        if (Model.getStateMachinesHelper().isAddingStatemachineAllowed(
              target)) {
            /* The target is a valid context. */
            machine = Model.getStateMachinesFactory().buildStateMachine(target);
        } else if (Model.getFacade().isAStateMachine(target)
                && hasNoDiagramYet(target)) {
            /* This target is a statemachine, 
             * for which no diagram exists yet, 
             * so, let's use it. */
            machine = target;
        } else {
            /* Let's just build a Statemachine, 
             * and put it in a suitable namespace. */
            machine = Model.getStateMachinesFactory().createStateMachine();
            if (Model.getFacade().isANamespace(target)) {
                namespace = target;
            }
            Model.getCoreHelper().setNamespace(machine, namespace);
            Model.getStateMachinesFactory()
                    .buildCompositeStateOnStateMachine(machine);
        }
        
        return DiagramFactory.getInstance().createDiagram(
                UMLStateDiagram.class,
                Model.getFacade().getNamespace(machine),
                machine);
    }
    
    private boolean hasNoDiagramYet(Object machine) {
        Project p = ProjectManager.getManager().getCurrentProject();
        Collection c = p.getDiagrams();
        Iterator i = c.iterator();
        while (i.hasNext()) {
            ArgoDiagram d = (ArgoDiagram) i.next();
            if (d instanceof UMLStateDiagram) {
                if (((UMLStateDiagram) d).getStateMachine() == machine) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -5197718695001757808L;

} /* end class ActionStateDiagram */
