package org.argouml.model.uml.behavioralelements.collaborations;

import org.argouml.model.uml.AbstractModelFactory;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.MFactory;
import ru.novosoft.uml.behavior.collaborations.MAssociationEndRole;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MMessage;

public class CollaborationsFactory extends AbstractModelFactory {
    private static CollaborationsFactory SINGLETON =
                   new CollaborationsFactory();

    public static CollaborationsFactory getFactory() {
        return SINGLETON;
    }

    /** Don't allow instantiation
     */
    private CollaborationsFactory() {
    }

    public MAssociationEndRole createAssociationEndRole() {
        // Line split to hide from ant replace
        MAssociationEndRole modelElement = MFactory.getDefaultFactory().
	       createAssociationEndRole();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MAssociationRole createAssociationRole() {
        // Line split to hide from ant replace
        MAssociationRole modelElement = MFactory.getDefaultFactory().
	       createAssociationRole();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MCollaboration createCollaboration() {
        // Line split to hide from ant replace
        MCollaboration modelElement = MFactory.getDefaultFactory().
	       createCollaboration();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MInteraction createInteraction() {
        // Line split to hide from ant replace
        MInteraction modelElement = MFactory.getDefaultFactory().
	       createInteraction();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }

    public MMessage createMessage() {
        // Line split to hide from ant replace
        MMessage modelElement = MFactory.getDefaultFactory().
	       createMessage();
        super.addListener(modelElement);
	super.postprocess(modelElement);
	return modelElement;
    }


}

