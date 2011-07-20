/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.DiagramSettings;
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

    protected ArgoDiagram createDiagram(Object namespace, 
            DiagramSettings settings) {
        Object machine = buildMachine(namespace, getTarget(namespace));
        
        return DiagramFactory.getInstance().create(
                DiagramFactory.DiagramType.State,
                machine, settings);
    }
    

    private Object getTarget(Object namespace) {
        Object target = TargetManager.getInstance().getModelTarget();
        if (Model.getFacade().isAUMLElement(target) 
                && Model.getModelManagementHelper().isReadOnly(target)) {
            target = namespace;
        }
        return target;
    }

    private Object buildMachine(Object namespace, Object target) {
        Object machine = null;
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
                /* Follow well-formedness rule for a Class [2].
                 * Determine the owning namespace for the statemachine: */
                while (Model.getFacade().isAClass(namespace)) {
                    Object parent = Model.getFacade().getNamespace(namespace);
                    if (parent == null) {
                        break;
                    }
                    namespace = parent;
                }
            }
            Model.getCoreHelper().setNamespace(machine, namespace);
            
            if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
                Model.getStateMachinesFactory()
                    .buildCompositeStateOnStateMachine(machine);
            }
        }
        return machine;
    }
    
    private boolean hasNoDiagramYet(Object machine) {
        Project p = ProjectManager.getManager().getCurrentProject();
        for (ArgoDiagram d : p.getDiagramList()) {
            if (d instanceof UMLStateDiagram) {
                if (((UMLStateDiagram) d).getStateMachine() == machine) {
                    return false;
                }
            }
        }
        return true;
    }

}
