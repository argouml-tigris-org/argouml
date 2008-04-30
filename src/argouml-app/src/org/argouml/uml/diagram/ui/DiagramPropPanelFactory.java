// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.PropPanelFactory;

/**
 * This factory creates the right PropPanelDiagram for a given UMLDiagram.
 * 
 * TODO: This class should be split up, to have a different
 * factory for each package. 
 * That would solve the current wrong package dependencies.
 * However, the statement added by Bob to return PropPanelDiagram
 * needs a different solution then - or can it be removed?
 *
 * @author Michiel
 */
class DiagramPropPanelFactory implements PropPanelFactory {

    public PropPanel createPropPanel(Object object) {
        if (object instanceof UMLDiagram) {
            if (object instanceof UMLActivityDiagram) {
                return new PropPanelUMLActivityDiagram();
            } else if (object instanceof UMLClassDiagram) {
                return new PropPanelUMLClassDiagram();
            } else if (object instanceof UMLCollaborationDiagram) {
                return new PropPanelUMLCollaborationDiagram();
            } else if (object instanceof UMLDeploymentDiagram) {
                return new PropPanelUMLDeploymentDiagram();
            } else if (object instanceof UMLSequenceDiagram) {
                return new PropPanelUMLSequenceDiagram();
            } else if (object instanceof UMLStateDiagram) {
                return new PropPanelUMLStateDiagram();
            } else if (object instanceof UMLUseCaseDiagram) {
                return new PropPanelUMLUseCaseDiagram();
            } else {
                // If we get here then presumably a plugin has provided a
                // different diagram type. For now lets show something.
                return new PropPanelDiagram();
            }
        }
        return null;
    }
    
}
