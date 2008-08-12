// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Collection;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.undo.UndoableAction;

/**
* ActionAddExistingNodes enables pasting of existing nodes into a Diagram.
*
* @author Thomas Neustupny
*/
public class ActionAddExistingNodes extends UndoableAction {

    /**
     * The UML objects to be added to the diagram.
     */
    private Collection objects;

    /**
     * The Constructor.
     *
     * @param name the localized name of the action
     * @param coll the UML objects to be added
     */
    public ActionAddExistingNodes(String name, Collection coll) {
        super(name);
        objects = coll;
    }

    /*
     * @see javax.swing.Action#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        ArgoDiagram dia = ProjectManager.getManager().getCurrentProject()
                .getActiveDiagram();
        if (dia == null) {
            return false;
        }
        MutableGraphModel gm = (MutableGraphModel) dia.getGraphModel();
        for (Object o : objects) {
            if (gm.canAddNode(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
        if (!(gm instanceof MutableGraphModel)) {
            return;
        }

        String instructions =
            Translator.localize(
                "misc.message.click-on-diagram-to-add");
        Globals.showStatus(instructions);
        
        final ModeAddToDiagram placeMode = new ModeAddToDiagram(
                objects,
                instructions);

        Globals.mode(placeMode, false);
    }
    
    /**
     * @param modelElements the modelelements to add Nodes for
     * @param location the point where to drop the node.
     *               Also <code>null</code> is acceptable.
     * @param diagram the diagram to add the nodes to
     */
    public static void addNodes(Collection modelElements, 
            Point location, ArgoDiagram diagram) {
        MutableGraphModel gm = (MutableGraphModel) diagram.getGraphModel();
        Collection oldTargets = TargetManager.getInstance().getTargets();
        int count = 0;
        for (Object me : modelElements) {
            if (Model.getFacade().isANaryAssociation(me)) {
                AddExistingNodeCommand cmd =
                    new AddExistingNodeCommand(me, location,
                            count++);
                cmd.execute();
            } else if (Model.getFacade().isAUMLElement(me)) {
                if (gm.canAddEdge(me)) {
                    gm.addEdge(me);
                    // TODO: An AssociationClass should be possible to add
                    // as a side effect of adding a node and its related
                    // edges, but that doesn't work as things are currently
                    // structured. - tfm 20061208
                    if (Model.getFacade().isAAssociationClass(me)) {
                        ModeCreateAssociationClass.buildInActiveLayer(
                                Globals.curEditor(), 
                                me);
                    }
                } else if (gm.canAddNode(me)) {
                    AddExistingNodeCommand cmd =
                        new AddExistingNodeCommand(me, location,
                                count++);
                    cmd.execute();
                }
            }
        }
        TargetManager.getInstance().setTargets(oldTargets);
    }
}
