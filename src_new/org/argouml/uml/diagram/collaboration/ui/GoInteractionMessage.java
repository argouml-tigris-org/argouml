package org.argouml.uml.diagram.collaboration.ui;

import java.util.Collection;

import org.argouml.application.api.Argo;
import org.argouml.ui.AbstractGoRule;

import ru.novosoft.uml.behavior.collaborations.MInteraction;

/**
 * 
 * Navrule to navigate from an interaction to a message
 * @author jaap.branderhorst@xs4all.nl
 */
public class GoInteractionMessage extends AbstractGoRule {

	public String getRuleName() {
    return Argo.localize ("Tree", "misc.interaction.messages");
  }

	/**
	 * @see org.argouml.ui.AbstractGoRule#getChildren(Object)
	 */
	public Collection getChildren(Object parent) {
		if (parent instanceof MInteraction) return ((MInteraction)parent).getMessages();
		return null;
	}

}
