package org.argouml.model.uml.foundation.core;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.core.MAbstraction;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBinding;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MComment;
import ru.novosoft.uml.foundation.core.MComponent;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MElementResidence;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MMethod;
import ru.novosoft.uml.foundation.core.MNode;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MUsage;

public class CoreFactory extends AbstractModelFactory {

    private static CoreFactory SINGLETON =
                   new CoreFactory();

    public static CoreFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private CoreFactory() {
    }

    public MAbstraction createAbstraction() {
        // Line split to hide from ant replace
        MAbstraction modelElement = MFactory.getDefaultFactory().
	       createAbstraction();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MAssociation createAssociation() {
        // Line split to hide from ant replace
        MAssociation modelElement = MFactory.getDefaultFactory().
	       createAssociation();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MAssociationEnd createAssociationEnd() {
        // Line split to hide from ant replace
        MAssociationEnd modelElement = MFactory.getDefaultFactory().
	       createAssociationEnd();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MAttribute createAttribute() {
        // Line split to hide from ant replace
        MAttribute modelElement = MFactory.getDefaultFactory().
	       createAttribute();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MBinding createBinding() {
        // Line split to hide from ant replace
        MBinding modelElement = MFactory.getDefaultFactory().
	       createBinding();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MClass createClass() {
        // Line split to hide from ant replace
        MClass modelElement = MFactory.getDefaultFactory().
	       createClass();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MComment createComment() {
        // Line split to hide from ant replace
        MComment modelElement = MFactory.getDefaultFactory().
	       createComment();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MComponent createComponent() {
        // Line split to hide from ant replace
        MComponent modelElement = MFactory.getDefaultFactory().
	       createComponent();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MConstraint createConstraint() {
        // Line split to hide from ant replace
        MConstraint modelElement = MFactory.getDefaultFactory().
	       createConstraint();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MDataType createDataType() {
        // Line split to hide from ant replace
        MDataType modelElement = MFactory.getDefaultFactory().
	       createDataType();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MDependency createDependency() {
        // Line split to hide from ant replace
        MDependency modelElement = MFactory.getDefaultFactory().
	       createDependency();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MElementResidence createElementResidence() {
        // Line split to hide from ant replace
        MElementResidence modelElement = MFactory.getDefaultFactory().
	       createElementResidence();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MGeneralization createGeneralization() {
        // Line split to hide from ant replace
        MGeneralization modelElement = MFactory.getDefaultFactory().
	       createGeneralization();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MInterface createInterface() {
        // Line split to hide from ant replace
        MInterface modelElement = MFactory.getDefaultFactory().
	       createInterface();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MMethod createMethod() {
        // Line split to hide from ant replace
        MMethod modelElement = MFactory.getDefaultFactory().
	       createMethod();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MNode createNode() {
        // Line split to hide from ant replace
        MNode modelElement = MFactory.getDefaultFactory().
	       createNode();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MOperation createOperation() {
        // Line split to hide from ant replace
        MOperation modelElement = MFactory.getDefaultFactory().
	       createOperation();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MParameter createParameter() {
        // Line split to hide from ant replace
        MParameter modelElement = MFactory.getDefaultFactory().
	       createParameter();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MUsage createUsage() {
        // Line split to hide from ant replace
        MUsage modelElement = MFactory.getDefaultFactory().
	       createUsage();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

}

