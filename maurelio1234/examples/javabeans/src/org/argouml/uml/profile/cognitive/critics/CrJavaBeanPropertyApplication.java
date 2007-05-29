package org.argouml.uml.profile.cognitive.critics;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;
import org.argouml.uml.cognitive.critics.CrUML;
import org.argouml.uml.profile.javabeans.ProfileJavaBeans;

public class CrJavaBeanPropertyApplication extends CrUML {
    public CrJavaBeanPropertyApplication() {
	setHeadline("");
	addSupportedDecision(UMLDecision.PLANNED_EXTENSIONS);
	setPriority(ToDoItem.HIGH_PRIORITY);
	addTrigger("association");
    }
    
    public boolean predicate2(Object dm, Designer dsgr) {
	if (ProjectManager.getManager().getCurrentProject()
		.getProfileConfiguration().getProfiles().contains(
			ProfileJavaBeans.getInstance())) {
	    if (Model.getFacade().isAAttribute(dm)) {
		if (Model.getExtensionMechanismsHelper().hasStereoType(dm,
			"Property")) {
		    Object owner = Model.getFacade().getOwner(dm);
		    if (!Model.getExtensionMechanismsHelper().hasStereoType(
			    owner, "Bean")) {
			return PROBLEM_FOUND;
		    }
		}
	    }
	}
	return NO_PROBLEM;
    }
}
