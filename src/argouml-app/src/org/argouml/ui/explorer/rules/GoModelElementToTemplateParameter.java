package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;

public class GoModelElementToTemplateParameter extends AbstractPerspectiveRule {

    @Override
    public Collection getChildren(Object parent) {
        if (Model.getFacade().isAClassifier(parent)) {
            return Model.getFacade().getTemplateParameters(parent);
        }
        return Collections.emptyList();
    }

    @Override
    public String getRuleName() {
        return Translator.localize("misc.model-element.template-parameter");
    }

    public Set getDependencies(Object parent) {
//        if (Model.getFacade().isAClassifier(parent)) {
//            Set set = new HashSet();
//            set.add(parent);
//            return set;
//        }
        return Collections.emptySet();
    }


}
