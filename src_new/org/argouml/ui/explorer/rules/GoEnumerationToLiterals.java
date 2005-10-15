package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.argouml.model.Model;

public class GoEnumerationToLiterals extends AbstractPerspectiveRule {

    public GoEnumerationToLiterals() {
        super();
    }

    /**
     * @see org.argouml.ui.explorer.rules.AbstractPerspectiveRule#getChildren(java.lang.Object)
     */
    public Collection getChildren(Object parent) {
        if (Model.getFacade().isAEnumeration(parent)) {
            ArrayList list = new ArrayList();

            if (Model.getFacade().getEnumerationLiterals(parent)!=null
                    && Model.getFacade().getEnumerationLiterals(parent).size() > 0)
                    list.addAll(Model.getFacade().getEnumerationLiterals(parent));            
            return list;
        }
        return null;
    }

    /**
     * @see org.argouml.ui.explorer.rules.AbstractPerspectiveRule#getRuleName()
     */
    public String getRuleName() {
        return "Enumeration->Literals";
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
        if (Model.getFacade().isAEnumeration(parent)) {
            Set set = new HashSet();
            set.add(parent);
            set.addAll(Model.getFacade().getEnumerationLiterals(parent));
            return set;
        }
        return null;
    }

}
