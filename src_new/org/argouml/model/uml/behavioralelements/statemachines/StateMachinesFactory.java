package org.argouml.model.uml.behavioralelements.statemachines;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.state_machines.MCallEvent;
import ru.novosoft.uml.behavior.state_machines.MCompositeState;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MTransition;

public class StateMachinesFactory extends AbstractModelFactory {
    private static StateMachinesFactory SINGLETON =
                   new StateMachinesFactory();

    public static StateMachinesFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private StateMachinesFactory() {
    }

    public MCallEvent createCallEvent() {
        // Line split to hide from ant replace
        MCallEvent modelElement = MFactory.getDefaultFactory().
	       createCallEvent();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MCompositeState createCompositeState() {
        // Line split to hide from ant replace
        MCompositeState modelElement = MFactory.getDefaultFactory().
	       createCompositeState();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MGuard createGuard() {
        // Line split to hide from ant replace
        MGuard modelElement = MFactory.getDefaultFactory().
	       createGuard();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MState createState() {
        // Line split to hide from ant replace
        MState modelElement = MFactory.getDefaultFactory().
	       createState();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MStateMachine createStateMachine() {
        // Line split to hide from ant replace
        MStateMachine modelElement = MFactory.getDefaultFactory().
	       createStateMachine();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MTransition createTransition() {
        // Line split to hide from ant replace
        MTransition modelElement = MFactory.getDefaultFactory().
	       createTransition();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

}

