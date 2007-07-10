// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;

import org.argouml.kernel.ProjectManager;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.undo.UndoableAction;

/**
* ActionAddExistingNode enables pasting of an existing node into a Diagram.
*
* @author Eugenio Alvarez
* Data Access Technologies.
*/
public class ActionAddExistingNode extends UndoableAction {

    /**
     * The UML object to be added to the diagram.
     */
    private Object object;

    /**
     * The Constructor.
     *
     * @param name the localized name of the action
     * @param o the node UML object to be added
     */
    public ActionAddExistingNode(String name, Object o) {
        super(name);
        object = o;
    }

    /*
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        Object target = TargetManager.getInstance().getTarget();
        ArgoDiagram dia = ProjectManager.getManager().
            getCurrentProject().getActiveDiagram();
        if (dia == null) return false;
        MutableGraphModel gm = (MutableGraphModel) dia.getGraphModel();
        return gm.canAddNode(target);
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        AddExistingNodeCommand cmd = new AddExistingNodeCommand(object);
        cmd.execute();
    }
} /* end class ActionAddExistingNode */
