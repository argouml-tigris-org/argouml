package org.argouml.uml.diagram.state.ui;

import java.util.Collection;

import org.argouml.ui.AbstractGoRule;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;

/**
 * @author Jaap
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GoStateMachineToTransition extends AbstractGoRule {

    public String getRuleName() { return "State Machine->Transition"; }

	/**
	 * @see org.argouml.ui.AbstractGoRule#getChildren(Object)
	 */
	public Collection getChildren(Object parent) {
		if (parent instanceof MStateMachine) {
			return ((MStateMachine)parent).getTransitions();
		}
		return null;
	}

}
