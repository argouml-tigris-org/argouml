// Copyright (c) 1996-2002 The Regents of the University of California. All
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.uml.ui.UMLComboBoxModel2;

import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.model_management.MModel;

/**
 * The combobox model for the type belonging to some attribute.
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLStructuralFeatureTypeComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLStructuralFeatureTypeComboBoxModel.
     * @param container
     * @param propertySetName
     * @param clearable
     */
    public UMLStructuralFeatureTypeComboBoxModel() {
        super("type", false);
        UmlModelEventPump.getPump().addClassModelEventListener(
            this,
            MNamespace.class,
            "ownedElement");
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(ru.novosoft.uml.MBase)
     */
    protected boolean isValidElement(Object element) {
        return element instanceof MClassifier;
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Set elements = new HashSet();
        Project p = ProjectManager.getManager().getCurrentProject();
        Iterator it = p.getUserDefinedModels().iterator();
        while (it.hasNext()) {
            MModel model = (MModel)it.next();
            elements.addAll(
                ModelManagementHelper.getHelper().getAllModelElementsOfKind(
                    model,
                    MClassifier.class));
        }
        elements.addAll(
            ModelManagementHelper.getHelper().getAllModelElementsOfKind(
                p.getDefaultModel(),
                MClassifier.class));
        setElements(elements);
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        Object o = null;
        if (getTarget() != null) {
            o = ((MStructuralFeature)getTarget()).getType();
        }
        if (o == null) {
            o = " ";
        }
        return o;
    }

}
