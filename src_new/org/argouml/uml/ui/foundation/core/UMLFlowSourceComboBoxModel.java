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
package org.argouml.uml.ui.foundation.core;

import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLThirdPartyEventListener;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.MFlow;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLFlowSourceComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLFlowSourceComboBoxModel.
     * @param container
     */
    public UMLFlowSourceComboBoxModel(UMLUserInterfaceContainer container) {
        super(container, false);
        UmlModelEventPump.getPump().addClassModelEventListener(this, MNamespace.class, "ownedElement");
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidRoleAdded(ru.novosoft.uml.MElementEvent)
     */
    protected boolean isValidRoleAdded(MElementEvent e) {
        Object o = getChangedElement(e);
        return o instanceof MModelElement
            && !((MFlow)getTarget()).getTargets().contains(o);
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidPropertySet(ru.novosoft.uml.MElementEvent)
     */
    protected boolean isValidPropertySet(MElementEvent e) {
        return (e.getSource() == getTarget() && e.getName().equals("source")) ||
            (e.getSource() instanceof MNamespace && e.getName().equals("ownedElement"));
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        setElements(ModelManagementHelper.getHelper().getAllModelElementsOfKind(((MModelElement)getTarget()).getNamespace(), MModelElement.class));
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null && !((MFlow)getTarget()).getSources().isEmpty()) {
            return ((MFlow)getTarget()).getSources().toArray()[0];
        }
        return null;
    }

}
