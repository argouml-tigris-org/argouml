// $Id: SequenceDiagramFactory.java 16381 2008-12-19 15:37:46Z bobtarling $
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

package org.argouml.diagram.uml2;

import java.beans.PropertyVetoException;

import org.apache.log4j.Logger;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactoryInterface2;
import org.argouml.uml.diagram.DiagramSettings;

/**
 * This factory creates a Use Case Diagram.
 * @see org.argouml.uml.diagram.DiagramFactory
 */
class UseCaseDiagram2Factory implements DiagramFactoryInterface2 {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(UseCaseDiagram2Factory.class);
    
    /**
     * Factory method to create a new instance of an ArgoDiagram.
     * 
     * @param owner the owning element. This can be the owning namespace for a
     *            Class diagram or an owning Statemachine for a State Diagram or
     *            any other interpretation that the diagram type wants to apply.
     * @param name the name of the diagram. This may be null if the caller would
     *            like the factory to provide a default name.
     * @param settings default rendering settings for the diagram
     * @return the newly instantiated diagram
     */
    public ArgoDiagram createDiagram(Object owner, String name,
            DiagramSettings settings) {
        final ArgoDiagram diagram = new UMLUseCaseDiagram2(owner);
        if (name != null) {
            try {
                diagram.setName(name);
            } catch (PropertyVetoException e) {            
                LOG.error("Cannot set the name " + name + 
                        " to the diagram just created: "+ diagram.getName(), e);
            }
        }
        return diagram;  
    }
}
