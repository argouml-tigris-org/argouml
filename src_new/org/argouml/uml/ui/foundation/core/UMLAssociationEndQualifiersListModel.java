package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.List;

import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;

/**
 * @author pepargouml@yahoo.es
 */
public class UMLAssociationEndQualifiersListModel
    extends UMLModelElementOrderedListModel2 {

    /**
     * Constructor for UMLAssociationEndQualifiersListModel.
     */
    public UMLAssociationEndQualifiersListModel() {
        super("qualifier");
    }

     /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null)
            setAllElements(ModelFacade.getQualifiers(getTarget()));
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object o) {
        return org.argouml.model.ModelFacade.isAAttribute(o)
            && ModelFacade.getQualifiers(getTarget()).contains(o);
    }


    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#swap(int, int)
     */
    public void swap(int index1, int index2) {
        Object assocEnd = getTarget();
        List c = new ArrayList(ModelFacade.getQualifiers(assocEnd));
        Object mem1 = c.get(index1);
        Object mem2 = c.get(index2);
        c.set(index1, mem2);
        c.set(index2, mem1);
        ModelFacade.setQualifiers(assocEnd, c);
        buildModelList();
    }
}
