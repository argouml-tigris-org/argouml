// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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
import org.argouml.kernel.ProjectManager;
import org.argouml.uml.diagram.deployment.ui.UMLDeploymentDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;

import ru.novosoft.uml.foundation.core.MNamespace;

/** Action to trigger creation of a deployment diagram.
 *  @stereotype singleton
 */
public class ActionDeploymentDiagram extends ActionAddDiagram {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionDeploymentDiagram SINGLETON = new ActionDeploymentDiagram();


    ////////////////////////////////////////////////////////////////
    // constructors

    private ActionDeploymentDiagram() {
        super("DeploymentDiagram");
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see org.argouml.uml.ui.ActionAddDiagram#createDiagram(MNamespace,Object)
     */
    public UMLDiagram createDiagram(MNamespace ns) {
        return new UMLDeploymentDiagram(ns);
    }

    /**
     * @see org.argouml.uml.ui.ActionAddDiagram#isValidNamespace(MNamespace)
     */
    public boolean isValidNamespace(MNamespace ns) {
        return ns == ProjectManager.getManager().getCurrentProject().getModel(); // may only occur as child of the model
    }

} /* end class ActionDeploymentDiagram */
