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

package org.argouml.uml.diagram.state.ui;

import java.util.Collection;
import java.util.Vector;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.AbstractGoRule;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;

import ru.novosoft.uml.behavior.state_machines.MStateMachine;

public class GoMachineDiagram extends AbstractGoRule {

    public String getRuleName() {
        return "Machine->Diagram";
    }

    public Collection getChildren(Object parent) {
        
        if (!(parent instanceof MStateMachine))
            return null;
        Project p = ProjectManager.getManager().getCurrentProject();
        if (p == null)
            return null;

        Vector res = new Vector();
        Vector diagrams = p.getDiagrams();
        if (diagrams == null)
            return null;
        java.util.Enumeration enum = diagrams.elements();
        while (enum.hasMoreElements()) {
            Object d = enum.nextElement();
            if (d instanceof UMLStateDiagram && ((UMLStateDiagram) d).getStateMachine() == parent)
                res.addElement(d);
            else if (d instanceof UMLActivityDiagram && ((UMLActivityDiagram) d).getStateMachine() == parent)
                res.addElement(d);
        }
        return res;
    }

}
