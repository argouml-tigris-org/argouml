/**
* ActionAddExistingNode.java
*
* This class enables pasting of an existing ModelElement into a Diagram.
*
* @author Eugenio Alvarez
* Data Access Technologies.
*/

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;

import org.argouml.application.api.Argo;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModePlace;
import org.tigris.gef.graph.GraphFactory;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.MutableGraphModel;

public class ActionAddExistingNode extends UMLAction implements GraphFactory
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
        MutableGraphModel gm = (MutableGraphModel)ProjectManager.getManager().getCurrentProject().getActiveDiagram().getGraphModel();
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
