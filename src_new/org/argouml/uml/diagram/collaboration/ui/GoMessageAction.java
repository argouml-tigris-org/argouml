package org.argouml.uml.diagram.collaboration.ui;

import java.util.ArrayList;
import java.util.Collection;

import org.argouml.model.ModelFacade;
import org.argouml.ui.AbstractGoRule;

import ru.novosoft.uml.behavior.collaborations.MMessage;

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
            
			Object action = ModelFacade.getAction(parent);
			if (action != null) {
                                ArrayList children = new ArrayList();
                                children.add(action);
				return children;
			}
		}
		return null;
	}

}
