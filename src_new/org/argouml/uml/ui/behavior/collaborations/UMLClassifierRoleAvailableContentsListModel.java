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
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (isValidRoleAdded(e)) {
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
        if (isValidRoleRemoved(e)) {
            MClassifier clazz = (MClassifier)e.getRemovedValue();
            Iterator it = clazz.getOwnedElements().iterator();
            while (it.hasNext()) {
                removeElement(it.next());
            }
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidRoleAdded(ru.novosoft.uml.MElementEvent)
     */
    protected boolean isValidRoleAdded(MElementEvent e) {
        Object elem = getChangedElement(e);
        if (elem instanceof MClassifier) {
            Collection availableContents = CollaborationsHelper.getHelper().allAvailableContents((MClassifierRole)getTarget());
            Iterator it = ((MClassifier)elem).getOwnedElements().iterator();
            while (it.hasNext()) {
                MModelElement me = (MModelElement)it.next();
                if (availableContents.contains(me)) return true;
            }
        }
        return false;
    }

}
