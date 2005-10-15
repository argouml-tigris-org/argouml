package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.argouml.model.Model;

public class GoStereotypeToTagDefinition extends AbstractPerspectiveRule {

    public GoStereotypeToTagDefinition() {
        super();
    }

    /**
     * @see org.argouml.ui.explorer.rules.AbstractPerspectiveRule#getChildren(java.lang.Object)
     */
    public Collection getChildren(Object parent) {
        if (Model.getFacade().isAStereotype(parent)) {
            ArrayList list = new ArrayList();

            if (Model.getFacade().getTagDefinitions(parent)!=null
                    && Model.getFacade().getTagDefinitions(parent).size() > 0)
                    list.addAll(Model.getFacade().getTagDefinitions(parent));            
            return list;
        }
        return null;
    }

    /**
     * @see org.argouml.ui.explorer.rules.AbstractPerspectiveRule#getRuleName()
     */
    public String getRuleName() {
        return "Stereotype->TagDefinition";
    }

    /**
     * @see org.argouml.ui.explorer.rules.AbstractPerspectiveRule#toString()
     */
    public String toString() {
        return super.toString();
    }

    /**
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getDependencies(java.lang.Object)
     */
    public Set getDependencies(Object parent) {
        if (Model.getFacade().isAStereotype(parent)) {
            Set set = new HashSet();
            set.add(parent);
            set.addAll(Model.getFacade().getTagDefinitions(parent));
            return set;
        }
        return null;
    }

}
