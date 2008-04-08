// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.sequence2.module;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.argouml.model.DiDiagram;
import org.argouml.model.Model;
import org.argouml.model.SequenceDiagram;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.GraphChangeAdapter;
import org.argouml.uml.diagram.sequence2.ui.UMLSequenceDiagram;

/**
 * This factory creates a sequence2 Sequence Diagram if needed, otherwise 
 * it delegates on the ArgoUML core DiagramFactory.
 * @see org.argouml.uml.diagram.DiagramFactory
 * @author penyaskito
 */
public class SequenceDiagramFactory  {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger
            .getLogger(SequenceDiagramFactory.class);

    
    private static SequenceDiagramFactory diagramFactory = 
        new SequenceDiagramFactory();
    
    private List<ArgoDiagram> diagrams = new LinkedList<ArgoDiagram>();
        
    private SequenceDiagramFactory() {
    }

    /**
     * @return the singleton
     */
    public static SequenceDiagramFactory getInstance() {
        return diagramFactory;
    }
    
    /**
     * Factory method to create a new instance of an ArgoDiagram,
     * including the sequence2 Sequence Diagram
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
        
        if (type == UMLSequenceDiagram.class) {
            diagram = new UMLSequenceDiagram(namespace);
            diType = SequenceDiagram.class;
            LOG.debug("New Sequence Diagram created");         
        }
        else {
            // if is not the UML Sequence Diagram, we delegate 
            // on the core DiagramFactory
            // This method is deprecated, but we'll keep using it 
            // because this code will be deleted with the integration
            // of sequence diagrams in the trunk of argouml.
            return DiagramFactory.getInstance().createDiagram(type, 
                    namespace, machine);
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
            // ((UMLMutableGraphSupport) diagram.getGraphModel()).
            // setDiDiagram(dd);
        }
        
        //keep a reference on it in the case where we must add all the diagrams
        //as project members (loading)
        diagrams.add(diagram);
        return diagram;            
    }
}
