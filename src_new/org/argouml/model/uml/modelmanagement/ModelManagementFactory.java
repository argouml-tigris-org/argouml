package org.argouml.model.uml.modelmanagement;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.model_management.*;

public class ModelManagementFactory extends AbstractModelFactory {
    private static ModelManagementFactory SINGLETON =
                   new ModelManagementFactory();

    public static ModelManagementFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private ModelManagementFactory() {
    }

    public MModel createModel() {
        // Line split to hide from ant replace
        MModel modelElement = MFactory.getDefaultFactory().
	       createModel();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MElementImport createElementImport() {
        // Line split to hide from ant replace
        MElementImport modelElement = MFactory.getDefaultFactory().
	       createElementImport();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MPackage createPackage() {
        // Line split to hide from ant replace
        MPackage modelElement = MFactory.getDefaultFactory().
	       createPackage();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MSubsystem createUseCase() {
        // Line split to hide from ant replace
        MSubsystem modelElement = MFactory.getDefaultFactory().
	       createSubsystem();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }
}
