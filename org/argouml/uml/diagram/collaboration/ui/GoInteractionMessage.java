package org.argouml.uml.diagram.collaboration.ui;

import java.util.Collection;
import java.util.Vector;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.argouml.application.api.Argo;
import org.argouml.ui.AbstractGoRule;
import ru.novosoft.uml.behavior.collaborations.MInteraction;

/**
 * 
 * Navrule to navigate from an interaction to a message
 * @author jaap.branderhorst@xs4all.nl
 */
public class GoInteractionMessage extends AbstractGoRule {

	public String toString() {
    return Argo.localize ("Tree", "misc.interaction.messages");
  }

	
	/**
	 * @see javax.swing.tree.TreeModel#isLeaf(Object)
	 */
	public boolean isLeaf(Object node) {
		return !(node instanceof MInteraction && getChildCount(node)>0);
	}

	

	/**
	 * @see org.argouml.ui.AbstractGoRule#getChildren(Object)
	 */
	public Collection getChildren(Object parent) {
		if (parent instanceof MInteraction) return ((MInteraction)parent).getMessages();
		return null;
	}

}
