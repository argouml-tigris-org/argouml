package org.argouml.uml.profile.cognitive.critics;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.CrUML;

/**
 * Sample Java Critics for the JavaBeans Profile
 * 
 * @author maurelio1234
 */
@SuppressWarnings("serial")
public class CrJavaBeanPropertyApplication extends CrUML {
	public CrJavaBeanPropertyApplication() {
		super("beansprofile");

		addSupportedDecision(UMLDecision.PLANNED_EXTENSIONS);
		setPriority(ToDoItem.HIGH_PRIORITY);
		addTrigger("association");
	}

	public boolean predicate2(Object dm, Designer dsgr) {
		if (Model.getFacade().isAAttribute(dm)) {
			if (Model.getExtensionMechanismsHelper().hasStereotype(dm,
					"Property")) {
				Object owner = Model.getFacade().getOwner(dm);
				if (!Model.getExtensionMechanismsHelper().hasStereotype(owner,
						"Bean")) {
					return PROBLEM_FOUND;
				}
			}
		}
		return NO_PROBLEM;
	}
}
