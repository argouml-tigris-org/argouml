package org.argouml.uml.diagram.state.ui;

import java.util.Collection;
import java.util.Vector;

import org.argouml.ui.AbstractGoRule;

import ru.novosoft.uml.behavior.state_machines.MState;


/**
 * 
 * @author jaap.branderhorst@xs4all.nl	
 * @since Dec 25, 2002
 */
public class GoStateToDoActivity extends AbstractGoRule {

    /**
     * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
     */
    public boolean isLeaf(Object node) {
        return !(node instanceof MState && getChildCount(node) > 0);
    }

    /**
     * @see org.argouml.ui.AbstractGoRule#getChildren(java.lang.Object)
     */
    public Collection getChildren(Object parent) {
        if (parent instanceof MState && ((MState)parent).getDoActivity() != null) {
            Vector children = new Vector();
            
            children.add(((MState)parent).getDoActivity());
            return children;
        }
        return null;
    }

    /**
     * @see org.argouml.ui.AbstractGoRule#getRuleName()
     */
    public String getRuleName() {
        return "State->Do Activity"; 
    }

}
