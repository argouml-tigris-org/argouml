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

package org.argouml.uml.diagram;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.argouml.model.ActivityDiagram;
import org.argouml.model.ClassDiagram;
import org.argouml.model.CollaborationDiagram;
import org.argouml.model.DeploymentDiagram;
import org.argouml.model.DiDiagram;
import org.argouml.model.Model;
import org.argouml.model.SequenceDiagram;
import org.argouml.model.StateDiagram;
import org.argouml.model.UseCaseDiagram;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.Fig;

/**
* Provide a factory method to create different UML diagrams.
* @author Bob Tarling
*/
public final class DiagramFactory {
    private static DiagramFactory diagramFactory = new DiagramFactory();

    private List diagrams = new Vector();

    private DiagramFactory() {
    }

    /**
     * @return the singleton
     */
    public static DiagramFactory getInstance() {
        return diagramFactory;
    }

    /**
     * @return the list of diagrams
     */
    public List getDiagram() {
        return diagrams;
    }


//    public ArgoDiagram createDiagram(Class type, Object namespace,
//            Object machine) {
//	
//    }
    
    
    /**
     * Factory method to create a new instance of an ArgoDiagram.
     *
     * @param type The class of rendering diagram to create
     * @param namespace The namespace that (in)directly 
     *                        owns the elements on the diagram
     * @param machine The StateMachine for the diagram
     *                         (only: statemachine - activitygraph)
     * @return the newly instantiated class diagram
     */
    public ArgoDiagram createDiagram(Class type, Object namespace,
            Object machine) {

        ArgoDiagram diagram = null;
        Class diType = null;

        if (type == UMLClassDiagram.class) {
            diagram = new UMLClassDiagram(namespace);
            diType = ClassDiagram.class;
        } else if (type == UMLUseCaseDiagram.class) {
            diagram = new UMLUseCaseDiagram(namespace);
            diType = UseCaseDiagram.class;
        } else if (type == UMLStateDiagram.class) {
            diagram = new UMLStateDiagram(namespace, machine);
            diType = StateDiagram.class;
        } else if (type == UMLDeploymentDiagram.class) {
            diagram = new UMLDeploymentDiagram(namespace);
            diType = DeploymentDiagram.class;
        } else if (type == UMLCollaborationDiagram.class) {
            diagram = new UMLCollaborationDiagram(namespace);
            diType = CollaborationDiagram.class;
        } else if (type == UMLActivityDiagram.class) {
            diagram = new UMLActivityDiagram(namespace, machine);
            diType = ActivityDiagram.class;
        } else if (type == UMLSequenceDiagram.class) {
            diagram = new UMLSequenceDiagram(namespace);
            diType = SequenceDiagram.class;
        }

        if (diagram == null) {
            throw new IllegalArgumentException ("Unknown diagram type");
        }
        
        if (Model.getDiagramInterchangeModel() != null) {
            diagram.getGraphModel().addGraphEventListener(
                 GraphChangeAdapter.getInstance());
            /*
             * The diagram are always owned by the model
             * in this first implementation.
             */
            DiDiagram dd = GraphChangeAdapter.getInstance()
                .createDiagram(diType, namespace);
            ((UMLMutableGraphSupport) diagram.getGraphModel()).setDiDiagram(dd);
        }

        //keep a reference on it in the case where we must add all the diagrams
        //as project members (loading)
        diagrams.add(diagram);
        return diagram;
    }

    /**
     * Factory method to create a new instance of a Class Diagram.
     *
     * @param diagram the diagram
     * @return the newly instantiated class diagram
     */
    public ArgoDiagram removeDiagram(ArgoDiagram diagram) {

        DiDiagram dd =
            ((UMLMutableGraphSupport) diagram.getGraphModel()).getDiDiagram();
        if (dd != null) {
            GraphChangeAdapter.getInstance().removeDiagram(dd);
        }
        return diagram;
    }

    public DiDiagram getDiDiagram(Object graphModel) {
        if (graphModel instanceof UMLMutableGraphSupport) {
            return ((UMLMutableGraphSupport) graphModel).getDiDiagram();
        }
        throw new IllegalArgumentException("graphModel: " + graphModel);
    }

    public void addElement(Object diagram, Object element) {
        if (!(diagram instanceof ArgoDiagram)) {
            throw new IllegalArgumentException("diagram: " + diagram);
        }
        if (!(element instanceof Fig)) {
            throw new IllegalArgumentException("fig: " + element);
        }
        ((ArgoDiagram) diagram).add((Fig) element);
    }


    private final Map noStyleProperties = new HashMap();

    public Object createRenderingElement(Object diagram, Object model) {
        GraphNodeRenderer rend =
            ((UMLDiagram) diagram).getLayer().getGraphNodeRenderer();
        Object renderingElement =
                rend.getFigNodeFor(model, 0, 0, noStyleProperties);
        return renderingElement;
    }
}
