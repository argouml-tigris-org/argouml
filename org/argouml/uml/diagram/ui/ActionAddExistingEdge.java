/**
 * Created on Sep 25, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;

import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.graph.MutableGraphModel;

import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * @author Jaap
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ActionAddExistingEdge extends UMLAction {
    
    Object _edge = null;

    /**
     * Constructor for ActionAddExistingEdge.
     * @param name
     */
    public ActionAddExistingEdge(String tabName, Object edge) {
        super(tabName, NO_ICON);
        _edge = edge;
        
    }

    

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent arg0) {
        super.actionPerformed(arg0);
        // we have an edge
        if (_edge == null) return;
        // lets test which situation we have. 3 Possibilities:
        // 1. The nodes are allready on the diagram, we can use canAddEdge for this
        // 2. One of the nodes is allready on the diagram. The other has to be added
        // 3. Both of the nodes are not yet on the diagram.
        // For the time being we will only implement situation 1.
        // TODO implement situation 2 and 3.
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        MutableGraphModel gm = (MutableGraphModel)pb.getActiveDiagram().getGraphModel();
        if (gm.canAddEdge(_edge)) { // situation 1
            gm.addNodeRelatedEdges(gm.getSourcePort(_edge));
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
        ProjectBrowser pb = ProjectBrowser.TheInstance;
        Object target = pb.getTarget();
        MutableGraphModel gm = (MutableGraphModel)pb.getActiveDiagram().getGraphModel();
        return gm.canAddEdge(target);
    }

}
