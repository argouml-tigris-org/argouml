package org.argouml.model.uml.behavioralelements.usecases;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MExtend;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;

public class UseCasesFactory extends AbstractModelFactory {
    private static UseCasesFactory SINGLETON =
                   new UseCasesFactory();

    public static UseCasesFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private UseCasesFactory() {
    }

    public MExtend createExtend() {
        // Line split to hide from ant replace
        MExtend modelElement = MFactory.getDefaultFactory().
	       createExtend();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MExtensionPoint createExtensionPoint() {
        // Line split to hide from ant replace
        MExtensionPoint modelElement = MFactory.getDefaultFactory().
	       createExtensionPoint();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MActor createActor() {
        // Line split to hide from ant replace
        MActor modelElement = MFactory.getDefaultFactory().
	       createActor();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MInclude createInclude() {
        // Line split to hide from ant replace
        MInclude modelElement = MFactory.getDefaultFactory().
	       createInclude();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MUseCase createUseCase() {
        // Line split to hide from ant replace
        MUseCase modelElement = MFactory.getDefaultFactory().
	       createUseCase();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

}

