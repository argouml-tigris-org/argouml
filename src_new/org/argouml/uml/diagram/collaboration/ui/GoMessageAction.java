package org.argouml.uml.diagram.collaboration.ui;

import java.util.Collection;
import java.util.Vector;

import org.argouml.ui.AbstractGoRule;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;

/**
 * Go rule to navigate from some message to it's corresponding action
 * @author jaap.branderhorst
 */
public class GoMessageAction extends AbstractGoRule {

	/**
	 * @see javax.swing.tree.TreeModel#isLeaf(Object)
	 */
	public boolean isLeaf(Object node) {
		return !(node instanceof MMessage && getChildCount(node)>0);
	}

	/**
	 * @see org.argouml.ui.AbstractGoRule#getChildren(Object)
	 */
	public Collection getChildren(Object parent) {
		if (parent instanceof MMessage) {
			MMessage mes = (MMessage)parent;
			MAction action = mes.getAction();
			if (action != null) {
				Vector vec = new Vector();	
				vec.add(((MMessage)parent).getAction());
				return vec;
			}
		}
		return null;
	}

}
