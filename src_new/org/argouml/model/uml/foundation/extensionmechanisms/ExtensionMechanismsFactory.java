package org.argouml.model.uml.foundation.extensionmechanisms;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;

public class ExtensionMechanismsFactory extends AbstractModelFactory {
    private static ExtensionMechanismsFactory SINGLETON =
                   new ExtensionMechanismsFactory();

    public static ExtensionMechanismsFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private ExtensionMechanismsFactory() {
    }

    public MStereotype createStereotype() {
        // Line split to hide from ant replace
        MStereotype modelElement = MFactory.getDefaultFactory().
	       createStereotype();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MTaggedValue createTaggedValue() {
        // Line split to hide from ant replace
        MTaggedValue modelElement = MFactory.getDefaultFactory().
	       createTaggedValue();
	super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

}

