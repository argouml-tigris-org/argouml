// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.application.api.Argo;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.AbstractGoRule;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram;

/**
 * Shows the diagrams as children of their namespace. 
 * 
 * @author jaap.branderhorst@xs4all.nl	
 * @since Dec 30, 2002
 */
public class GoNamespaceToDiagram extends AbstractGoRule {

    public String getRuleName() {
        return Argo.localize("Tree", "misc.package.diagram");
    }

    public Collection getChildren(Object parent) {
        if (org.argouml.model.ModelFacade.isANamespace(parent)) {
            List returnList = new ArrayList();
            Object namespace = parent;//MNamespace
            Project proj = ProjectManager.getManager().getCurrentProject();
            Iterator it = proj.getDiagrams().iterator();
            while (it.hasNext()) {
                UMLDiagram d = (UMLDiagram) it.next();
                if (d instanceof UMLStateDiagram) {
                    UMLStateDiagram sd = (UMLStateDiagram) d;
                    if (ModelFacade.isABehavioralFeature(ModelFacade.getContext(ModelFacade.getStateMachine(sd))))
                    	continue;
                }
                // patch for 0.14 stability to disable SD's
                if (d instanceof UMLSequenceDiagram) {
                    continue;
                }
                if (d.getNamespace() == namespace)
                	returnList.add(d);                 
            }
            return returnList;
        }
        return null;
    }

}