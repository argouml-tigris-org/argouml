// $Id:ActionActivityDiagram.java 13166 2007-07-27 21:26:00Z tfmorris $
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

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;

/**
 * Action to trigger creation of a new activity diagram.<p>
 * 
 * An ActivityGraph specifies the dynamics of<ul>
 * <li> a Package, or
 * <li> a Classifier (including UseCase), or
 * <li> a BehavioralFeature.
 * </ul>
 * 
 * @author michiel
 */
public class ActionActivityDiagram extends ActionNewDiagram {

    /**
     * Constructor.
     */
    public ActionActivityDiagram() {
        super("action.activity-diagram");
    }

    /**
     * Create the diagram.
     * @param namespace the namespace in which to create the diagram
     * @return the newly created and initialized diagram
     */
    protected ArgoDiagram createDiagram(Object namespace) {
        Object target = TargetManager.getInstance().getModelTarget();
        Object graph = null;

        if (Model.getActivityGraphsHelper().isAddingActivityGraphAllowed(
                target)) {
            /* The target is a valid context */
            graph = Model.getActivityGraphsFactory().buildActivityGraph(target);
        } else {
            graph = Model.getActivityGraphsFactory().createActivityGraph();
            if (Model.getFacade().isANamespace(target)) {
                namespace = target;
            }
            Model.getCoreHelper().setNamespace(graph, namespace);
            Model.getStateMachinesFactory()
                .buildCompositeStateOnStateMachine(graph);
        }

        return DiagramFactory.getInstance().createDiagram(
                DiagramFactory.DiagramType.Activity,
                Model.getFacade().getNamespace(graph),
                graph);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -28844322376391273L;

} 
