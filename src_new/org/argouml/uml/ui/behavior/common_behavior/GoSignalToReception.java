package org.argouml.uml.ui.behavior.common_behavior;

import java.util.Collection;

import org.argouml.application.api.Argo;
import org.argouml.ui.AbstractGoRule;
import ru.novosoft.uml.behavior.common_behavior.MSignal;

/**
 * @author Jaap
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GoSignalToReception extends AbstractGoRule {
	
	public String toString() {
    	return Argo.localize ("Tree", "misc.interaction.messages");
  	}

	/**
	 * @see javax.swing.tree.TreeModel#isLeaf(Object)
	 */
	public boolean isLeaf(Object node) {
		return !((node instanceof MSignal) && (getChildCount(node) > 0));
	}

	/**
	 * @see org.argouml.ui.AbstractGoRule#getChildren(Object)
	 */
	public Collection getChildren(Object parent) {
		if (parent instanceof MSignal) {
			return ((MSignal)parent).getReceptions();
		}
		return null;
	}

}
