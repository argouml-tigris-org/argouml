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
package org.argouml.uml.ui.behavior.use_cases;

import java.util.ArrayList;
import java.util.List;

import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.use_cases.MInclude;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * @since Oct 7, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLIncludeBaseComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLIncludeBaseComboBoxModel.
     * @param container
     */
    public UMLIncludeBaseComboBoxModel(UMLUserInterfaceContainer container) {
        super(container);
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidRoleAdded(ru.novosoft.uml.MElementEvent)
     */
    protected boolean isValidRoleAdded(MElementEvent e) {
         MModelElement m = (MModelElement)getChangedElement(e);
        MInclude inc = (MInclude)getTarget();
        return (m instanceof MUseCase && m != inc.getAddition());
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidPropertySet(ru.novosoft.uml.MElementEvent)
     */
    protected boolean isValidPropertySet(MElementEvent e) {
        return e.getSource() == getTarget() && e.getName().equals("base");
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        MInclude inc = (MInclude)getTarget();
        if (inc == null) return;
        List list = new ArrayList();
        MNamespace ns = inc.getNamespace();
        list.addAll(ModelManagementHelper.getHelper().getAllModelElementsOfKind(ns, MUseCase.class));
        list.remove(inc.getAddition());
        addAll(list);
        if (inc.getBase() != null) {
            setSelectedItem(inc.getBase());
        } else
            setSelectedItem("");
    }

}
