package org.argouml.uml.ui.behavior.collaborations;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;

import ru.novosoft.uml.MBase;
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
        super.roleAdded(e);
        if (e.getName().equals("base") && e.getSource() == getTarget()) {
            MClassifier clazz = (MClassifier)getChangedElement(e);
            addAll(clazz.getOwnedElements());
        }
    }
    
    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#setTarget(java.lang.Object)
     */
    protected void setTarget(Object target) {
        if (_target != null) {
            Collection bases = ((MClassifierRole)getTarget()).getBases();
            Iterator it = bases.iterator();
            while (it.hasNext()) {
                MBase base = (MBase)it.next();
                UmlModelEventPump.getPump().removeModelEventListener(this, base, "ownedElement");
            }
            UmlModelEventPump.getPump().removeModelEventListener(this, (MBase)getTarget(), "base");
        }
        _target = target;
        if (_target != null) {
            Collection bases = ((MClassifierRole)_target).getBases();
            Iterator it = bases.iterator();
            while (it.hasNext()) {
                MBase base = (MBase)it.next();
                UmlModelEventPump.getPump().addModelEventListener(this, base, "ownedElement");
            }
            // make sure we know it when a classifier is added as a base
            UmlModelEventPump.getPump().addModelEventListener(this, (MBase)_target, "base");
        }            
        super.setTarget(target);
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidRoleAdded(ru.novosoft.uml.MElementEvent)
     */
    protected boolean isValidRoleAdded(MElementEvent e) {
        return ((MClassifierRole)getTarget()).getBases().contains(e.getSource()) && e.getName().equals("ownedElement") && !contains(getChangedElement(e));
    }

}
