// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

// $header$
package org.argouml.uml.ui.behavior.collaborations;

import java.util.Iterator;

import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.uml.ui.PropPanel;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * @since Oct 4, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLAssociationRoleBaseComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLAssociationRoleBaseComboBoxModel.
     * @param container
     * @param propertySetName
     */
    public UMLAssociationRoleBaseComboBoxModel(
        UMLUserInterfaceContainer container) {
        super(container, "base");
        if (container instanceof PropPanel) {
            Object [] eventsToWatch = { MClassifierRole.class, "base"};
            ((PropPanel) container).addThirdPartyEventListening(eventsToWatch);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValid(ru.novosoft.uml.foundation.core.MModelElement)
     */
    protected boolean isValid(MElementEvent e) {
        MAssociationRole role = (MAssociationRole)getTarget();
        MModelElement m = (MModelElement)getChangedElement(e);
        return 
            ((e.getSource() instanceof MClassifierRole && e.getName().equals("base") &&
                (CoreHelper.getHelper().getSource(role) == e.getSource() || CoreHelper.getHelper().getDestination(role) == e.getSource()))  ||
             (e.getSource() instanceof MNamespace && e.getName().equals("ownedElement") &&
                CollaborationsHelper.getHelper().getAllPossibleBases(role).contains(m)) ||
             getIndexOf(m) >= 0);
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        MAssociationRole role = (MAssociationRole)getTarget();
        setElements(CollaborationsHelper.getHelper().getAllPossibleBases(role));
        if (role != null && role.getBase() != null) {
            setSelectedItem(role.getBase());
        } else
            setSelectedItem("");
        
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (isValid(e)) {
            buildModelList();
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        if (isValid(e)) {
            buildModelList();
        }
    }

}
