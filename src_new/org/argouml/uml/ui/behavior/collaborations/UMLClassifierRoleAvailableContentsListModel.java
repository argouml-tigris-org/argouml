package org.argouml.uml.ui.behavior.collaborations;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * Binary relation list model for available con between classifierroles
 * 
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLClassifierRoleAvailableContentsListModel
	extends UMLModelElementListModel2 {


    /**
     * Constructor for UMLClassifierRoleAvailableContentsListModel.
     * @param container
     */
    public UMLClassifierRoleAvailableContentsListModel(UMLUserInterfaceContainer container) {
        super(container);
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        setAllElements(CollaborationsHelper.getHelper().allAvailableContents((MClassifierRole)getTarget()));
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValid(ru.novosoft.uml.foundation.core.MModelElement)
     */
    protected boolean isValid(MModelElement elem) {
        if (elem instanceof MClassifier) {
            Collection availableContents = CollaborationsHelper.getHelper().allAvailableContents((MClassifierRole)getTarget());
            Iterator it = ((MClassifier)elem).getOwnedElements().iterator();
            while (it.hasNext()) {
                MModelElement me = (MModelElement)it.next();
                if (availableContents.contains(me) || contains(me)) return true;
            }
        }
        return false;
    }
    
    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (isValid((MModelElement)e.getAddedValue())) {
            MClassifier clazz = (MClassifier)e.getAddedValue();
            Iterator it = clazz.getOwnedElements().iterator();
            while (it.hasNext()) {
                addElement(it.next());
            }
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        if (isValid((MModelElement)e.getRemovedValue())) {
            MClassifier clazz = (MClassifier)e.getRemovedValue();
            Iterator it = clazz.getOwnedElements().iterator();
            while (it.hasNext()) {
                removeElement(it.next());
            }
        }
    }




}
