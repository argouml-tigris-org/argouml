package org.argouml.uml.diagram.collaboration.ui;

import java.util.Collection;
import java.util.Vector;
import java.util.ArrayList;

import org.argouml.ui.AbstractGoRule;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;

/**
 * Go rule to navigate from some message to it's corresponding action
 * @author jaap.branderhorst
 */
public class GoMessageAction extends AbstractGoRule {

    public String getRuleName() { return "Message->Action";}

	/**
	 * @see org.argouml.ui.AbstractGoRule#getChildren(Object)
	 */
	public Collection getChildren(Object parent) {
		if (parent instanceof MMessage) {
			MAction action = ((MMessage)parent).getAction();
			if (action != null) {
                                ArrayList children = new ArrayList();
                                children.add(action);
				return children;
			}
		}
		return null;
	}

}
