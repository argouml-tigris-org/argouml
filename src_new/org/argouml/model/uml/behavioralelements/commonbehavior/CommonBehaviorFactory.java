package org.argouml.model.uml.behavioralelements.commonbehavior;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.behavior.common_behavior.MComponentInstance;
import ru.novosoft.uml.behavior.common_behavior.MException;
import ru.novosoft.uml.behavior.common_behavior.MLink;
import ru.novosoft.uml.behavior.common_behavior.MLinkEnd;
import ru.novosoft.uml.behavior.common_behavior.MNodeInstance;
import ru.novosoft.uml.behavior.common_behavior.MObject;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.behavior.common_behavior.MStimulus;

public class CommonBehaviorFactory extends AbstractModelFactory {
    private static CommonBehaviorFactory SINGLETON =
                   new CommonBehaviorFactory();

    public static CommonBehaviorFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private CommonBehaviorFactory() {
    }

    public MCallAction createCallAction() {
        // Line split to hide from ant replace
        MCallAction modelElement = MFactory.getDefaultFactory().
	       createCallAction();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MComponentInstance createComponentInstance() {
        // Line split to hide from ant replace
        MComponentInstance modelElement = MFactory.getDefaultFactory().
	       createComponentInstance();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MException createException() {
        // Line split to hide from ant replace
        MException modelElement = MFactory.getDefaultFactory().
	       createException();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MLink createLink() {
        // Line split to hide from ant replace
        MLink modelElement = MFactory.getDefaultFactory().
	       createLink();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MLinkEnd createLinkEnd() {
        // Line split to hide from ant replace
        MLinkEnd modelElement = MFactory.getDefaultFactory().
	       createLinkEnd();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MNodeInstance createNodeInstance() {
        // Line split to hide from ant replace
        MNodeInstance modelElement = MFactory.getDefaultFactory().
	       createNodeInstance();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MObject createObject() {
        // Line split to hide from ant replace
        MObject modelElement = MFactory.getDefaultFactory().
	       createObject();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MSignal createSignal() {
        // Line split to hide from ant replace
        MSignal modelElement = MFactory.getDefaultFactory().
	       createSignal();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MStimulus createStimulus() {
        // Line split to hide from ant replace
        MStimulus modelElement = MFactory.getDefaultFactory().
	       createStimulus();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

}

