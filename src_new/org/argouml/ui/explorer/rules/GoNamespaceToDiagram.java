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

package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
/**
 * Shows the diagrams as children of their namespace. 
 * 
 * @author jaap.branderhorst@xs4all.nl	
 * @since Dec 30, 2002
 */
public class GoNamespaceToDiagram extends AbstractPerspectiveRule{

    public String getRuleName() {
        return Translator.localize("Tree", "misc.package.diagram");
    }

    public Collection getChildren(Object parent) {
        if (ModelFacade.isANamespace(parent)) {
            List returnList = new ArrayList();
            Object namespace = parent;//MNamespace
            Project proj = ProjectManager.getManager().getCurrentProject();
            Iterator it = proj.getDiagrams().iterator();
            while (it.hasNext()) {
                UMLDiagram diagram = (UMLDiagram) it.next();
                if (diagram instanceof UMLStateDiagram) {
                    UMLStateDiagram stateDiagram = (UMLStateDiagram)diagram;
                    Object stateMachine = stateDiagram.getStateMachine();
                    Object context = ModelFacade.getContext(stateMachine);
                    if (ModelFacade.isABehavioralFeature(context)) {
                    	continue;
                    }
                }       
                // sequence diagrams are not shown as children of the collaboration that
                // they show but as children of the classifier/operation the
                // collaboration represents
                if (diagram instanceof UMLSequenceDiagram) {
                	continue;
                }         
                if (diagram.getNamespace() == namespace) {
                    returnList.add(diagram);
                }
            }
            return returnList;
        }
        return null;
    }

    public Set getDependencies(Object parent) {
        // TODO: What?
	return null;
    }
}
