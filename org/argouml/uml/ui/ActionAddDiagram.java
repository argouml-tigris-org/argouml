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

// $Id$

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.diagram.ui.UMLDiagram;

/**
 * Abstract class that is the parent of all actions adding diagrams to ArgoUML.
 * The children of this class should implement createDiagram to do any specific
 * actions for creating a diagram and isValidNamespace that checks if some 
 * namespace is valid to add the diagram to.
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class ActionAddDiagram extends UMLChangeAction {
    private Logger log = Logger.getLogger(this.getClass());

    /**
     * Constructor for ActionAddDiagram.
     * @param s
     */
    public ActionAddDiagram(String s) {
        super(s);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Project p = ProjectManager.getManager().getCurrentProject();
        // find the right namespace for the diagram
        Object target = pb.getTarget();
        Object ns = null;
        if (target == null || !ModelFacade.isABase(target)) {
            target = p.getRoot();
            ns = target;
        }
        if (ModelFacade.isANamespace(target)) {
            ns = target;
        } else {

            Object owner = null;
            if (ModelFacade.isABase(target)) {
                owner = ModelFacade.getContainer(target);
                if (owner != null && ModelFacade.isANamespace(owner)) {
                    ns = owner;
                }
            }
        }
        if (ns!= null && isValidNamespace(ns)) {
            UMLDiagram diagram = createDiagram(ns);
            p.addMember(diagram);
            ProjectBrowser.TheInstance.getNavigatorPane().addToHistory(diagram);
            ProjectBrowser.TheInstance.setTarget(diagram);
            ProjectBrowser.TheInstance.getNavigatorPane().forceUpdate();
            super.actionPerformed(e);
        } else {
            log.error("No valid namespace found");
            throw new IllegalStateException("No valid namespace found");
        }
        // Issue 1722
        // Removed following code so we allways get the correct namespace of the 
        // diagram (via the getContainer method). 
        /*    
        if (ModelFacade.isABase(target)) {
            MBase base = (MBase)target;
            base.getModelElementContainer();
        }
        }
        }
        MNamespace ns = null;
        if (target instanceof MNamespace) {
            ns = (MNamespace) target;
        }
        if (ns == null || !isValidNamespace(ns))
            ns = ProjectManager.getManager().getCurrentProject().getModel();
            
        if (isValidNamespace(ns)) {
            ArgoDiagram diagram = createDiagram(ns);
            p.addMember(diagram);
            ProjectBrowser.TheInstance.getNavigatorPane().addToHistory(diagram);
            ProjectBrowser.TheInstance.setTarget(diagram);
            ProjectBrowser.TheInstance.getNavigatorPane().forceUpdate();
            super.actionPerformed(e);
        }
        */
    }

    /**
     * Returns true as the given namespace a valid namespace is to add the 
     * diagram to.
     * @param ns
     * @return boolean
     */
    public abstract boolean isValidNamespace(Object ns);

    /**
     * Creates the diagram. Classes derived from this class should implement any
     * specific behaviour to create the diagram.
     * @param ns The namespace the UMLDiagram should get.
     * @return UMLDiagram
     */
    public abstract UMLDiagram createDiagram(Object ns);

}
