// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.ui;

import org.apache.log4j.Logger;
import org.argouml.model.Model;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.tigris.gef.base.Diagram;

/**
 * Provide factory methods to create different UML diagrams.
 * TODO: Can we make this a single method accepting a diagram type id
 * rather than many seperate methods?
 * @author Bob Tarling
 */
public class DiagramFactory {
    
    private static Logger LOG = Logger.getLogger(DiagramFactory.class);
    
    private static DiagramFactory diagramFactory = new DiagramFactory();
    
    private DiagramFactory() {
    }

    public static DiagramFactory getInstance() {
        return diagramFactory;
    }
    
    /**
     * Factory method to create a new instance of a Class Diagram
     * @param model The model that this class diagram represents
     * @return the newly instantiated class diagram
     */
    public ArgoDiagram createClassDiagram(Object model) {
        ArgoDiagram diagram = new UMLClassDiagram(model); 
        if (Model.getDiagramInterchangeModel() != null) {
            diagram.getGraphModel().addGraphEventListener(
                    GraphEventMediator.getInstance());
        }
        return diagram;
    }
    
    /**
     * Factory method to create a new instance of a Use Case Diagram
     * @param model The model that this use case diagram represents
     * @return the newly instantiated use case diagram
     */
    public ArgoDiagram createUseCaseDiagram(Object model) {
        ArgoDiagram diagram = new UMLUseCaseDiagram(model);
        if (Model.getDiagramInterchangeModel() != null) {
            diagram.getGraphModel().addGraphEventListener(
                    GraphEventMediator.getInstance());
        }
        return diagram;
    }
}
