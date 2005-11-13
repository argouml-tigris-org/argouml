package org.argouml.uml.ui.behavior.common_behavior;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

public class UMLActionArgumentListModel extends UMLModelElementListModel2 {

    public UMLActionArgumentListModel() {
        super("arguments");
    }

    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(Model.getFacade().getActualArguments(getTarget()));
        }
    }

    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAArgument(element);
    }

}
