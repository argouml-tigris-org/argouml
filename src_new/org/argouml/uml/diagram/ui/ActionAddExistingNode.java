// Copyright (c) 1996-2003 The Regents of the University of California. All
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

import org.argouml.application.api.Argo;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ArgoDiagram;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.graph.GraphFactory;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;

/**
* ActionAddExistingNode.java
*
* This class enables pasting of an existing ModelElement into a Diagram.
*
* @author Eugenio Alvarez
* Data Access Technologies.
*/
public class ActionAddExistingNode extends UMLAction 
    implements GraphFactory
{

    ////////////////////////////////////////////////////////////////
    // instance variables
    protected String _tabName;
    protected Object _object;

    ////////////////////////////////////////////////////////////////
    // constructor
    public ActionAddExistingNode(String tabName) {
        super(tabName, NO_ICON);
        _tabName = tabName;
    }

    public ActionAddExistingNode(String tabName, Object o) {
        super(tabName, NO_ICON);
        _tabName = tabName;
        _object = o;
    }

    public boolean shouldBeEnabled() {	
        Object target = TargetManager.getInstance().getTarget();
        ArgoDiagram dia = ProjectManager.getManager().
            getCurrentProject().getActiveDiagram();
        if (dia == null) return false;
        MutableGraphModel gm = (MutableGraphModel)dia.getGraphModel();
        return gm.canAddNode(target);
    }

    public void actionPerformed(ActionEvent ae) {
        Editor ce = Globals.curEditor();
        GraphModel gm = ce.getGraphModel();
        if (!(gm instanceof MutableGraphModel)) return;

        String instructions = null;
        if(_object != null) {
            instructions =  Argo.localize ("Tree", "misc.message.click-on-diagram-to-add") + _object.toString();
            Globals.showStatus(instructions);
        }
        ModePlace placeMode = new ModePlace(this,instructions);
        placeMode.setAddRelatedEdges(true);
	
        //
        //   This only occurs when an diagram is entered
        //
        //

        Globals.mode(placeMode, false );
    }

    ////////////////////////////////////////////////////////////////
    // GraphFactory implementation

    public GraphModel makeGraphModel() { return null; }
    public Object makeEdge() { return null; }

    public Object makeNode() {
        return _object;
    }

} /* end class ActionAddExistingNode */
