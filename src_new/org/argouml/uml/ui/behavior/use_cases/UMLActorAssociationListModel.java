package org.argouml.uml.ui.behavior.use_cases;

import java.util.Collection;
import java.util.Vector;

import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.uml.ui.UMLBinaryRelationListModel;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import org.tigris.gef.graph.MutableGraphModel;

import ru.novosoft.uml.behavior.use_cases.MActor;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.model_management.MSubsystem;

/**
 * @author Jaap
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class UMLActorAssociationListModel extends UMLBinaryRelationListModel {

	/**
	 * Constructor for UMLActorAssociationListModel.
	 * @param container
	 * @param property
	 * @param showNone
	 */
	public UMLActorAssociationListModel(
		UMLUserInterfaceContainer container,
		String property,
		boolean showNone) {
		super(container, property, showNone);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getChoices()
	 */
	protected Collection getChoices() {
		Vector choices = new Vector();
		choices.addAll(UseCasesHelper.getHelper().getAllUseCases());
		choices.addAll(CoreHelper.getHelper().getAllClasses());
		choices.addAll(ModelManagementHelper.getHelper().getAllSubSystems());
		return choices;
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getSelected()
	 */
	protected Collection getSelected() {
		if (getTarget() instanceof MClassifier) {
			return CoreHelper.getHelper().getAssociatedClassifiers((MClassifier)getTarget());
		} else
			throw new IllegalStateException("Target is no instanceof MClassifier");
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getAddDialogTitle()
	 */
	protected String getAddDialogTitle() {
		return "";
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#connect(MutableGraphModel, MModelElement, MModelElement)
	 */
	protected void connect(
		MutableGraphModel gm,
		MModelElement from,
		MModelElement to) {
			gm.connect(from, to, MAssociation.class);
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#build(MModelElement, MModelElement)
	 */
	protected void build(MModelElement from, MModelElement to) {
		if (from != null && to != null && from instanceof MActor && 
			(to instanceof MUseCase || to instanceof MSubsystem || to instanceof MClass)) {
			CoreFactory.getFactory().buildAssociation((MClassifier)from, (MClassifier)to);
		}
	}

	/**
	 * @see org.argouml.uml.ui.UMLBinaryRelationListModel#getRelation(MModelElement, MModelElement)
	 */
	protected MModelElement getRelation(MModelElement from, MModelElement to) {
		if (from instanceof MClassifier && to instanceof MClassifier) {
			return CoreHelper.getHelper().getAssociation((MClassifier)from, (MClassifier)to);
		} else 
			throw new IllegalArgumentException("Tried to get relation between some objects of which one was not a classifier");
	}

}
