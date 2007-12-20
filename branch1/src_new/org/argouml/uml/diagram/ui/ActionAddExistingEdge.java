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
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.undo.UndoableAction;

/**
* ActionAddExistingEdge enables pasting of an existing edge into a Diagram.
*/
public class ActionAddExistingEdge extends UndoableAction {

    private static final long serialVersionUID = 736094733140639882L;
    
    private Object edge = null;

    /**
     * Constructor for ActionAddExistingEdge.
     *
     * @param name       the name of the action
     * @param edgeObject    the edge (the UML ModelElement!)
     */
    public ActionAddExistingEdge(String name, Object edgeObject) {
        super(name);
        edge = edgeObject;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        super.actionPerformed(arg0);
        // we have an edge (the UML modelelement!)
        if (edge == null) {
            return;
        }
        // let's test which situation we have. 3 Possibilities:
        // 1. The nodes are already on the diagram, we can use
        //    canAddEdge for this.
        // 2. One of the nodes is already on the diagram. The other
        //    has to be added.
        // 3. Both of the nodes are not yet on the diagram.
        // For the time being we will only implement situation 1.
        // TODO: implement situation 2 and 3.
        MutableGraphModel gm = (MutableGraphModel) ProjectManager.getManager().
            getCurrentProject().getActiveDiagram().getGraphModel();
        if (gm.canAddEdge(edge)) { // situation 1
            gm.addEdge(edge);
            if (Model.getFacade().isAAssociationClass(edge)) {
                ModeCreateAssociationClass.buildInActiveLayer(Globals
                        .curEditor(), edge);
            }
        }
    }

    /*
     * @see javax.swing.Action#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        Object target = TargetManager.getInstance().getModelTarget();
        ArgoDiagram dia = ProjectManager.getManager().getCurrentProject().
            getActiveDiagram();
        if (dia == null) {
            return false;
        }
        MutableGraphModel gm = (MutableGraphModel) dia.getGraphModel();
        return gm.canAddEdge(target);
    }

}
