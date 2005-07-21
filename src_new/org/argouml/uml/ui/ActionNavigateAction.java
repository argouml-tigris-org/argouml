package org.argouml.uml.ui;

import org.argouml.model.Model;

public class ActionNavigateAction extends AbstractActionNavigate {

    protected Object navigateTo(Object source) {
        return Model.getFacade().getAction(source);
    }
}
