// Copyright (c) 1996-2001 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.argouml.kernel.Project;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.MModel;

/**
 * Broken out into its own file due to a need to reference it both in
 * PropPanelAttribute and PropPanelParameter.
 *
 * d00mst / 2002-09-10
 */
public class UMLTypeModel extends UMLComboBoxModel2 {
	

    /**
     * Constructor for UMLTypeModel.
     * @param container
     * @param propertySetName
     */
    public UMLTypeModel(
        UMLUserInterfaceContainer container) {
        super(container, false);
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Set elements = new HashSet();
        Project p = ProjectBrowser.TheInstance.getProject();
        Iterator it = p.getUserDefinedModels().iterator();
        while (it.hasNext()) {
           MModel model = (MModel)it.next();
           elements.addAll(ModelManagementHelper.getHelper().getAllModelElementsOfKind(model, MClassifier.class));
        }
        elements.addAll(ModelManagementHelper.getHelper().getAllModelElementsOfKind(p.getDefaultModel(), MClassifier.class));
        setElements(elements);
        Object target = getContainer().getTarget();
        if (target instanceof MAttribute) {
            setSelectedItem(((MAttribute)target).getType());
        } else
        if (target instanceof MParameter) {
            setSelectedItem(((MParameter)target).getType());
        } else
        if (target instanceof MAssociationEnd) {
            setSelectedItem(((MAssociationEnd)target).getType());
        }
    }

    /**
     * @see javax.swing.ComboBoxModel#setSelectedItem(Object)
     */
    public void setSelectedItem(Object arg0) {
        super.setSelectedItem(arg0);
        if (arg0 != null) {
            if (arg0 instanceof Collection) {
                Iterator it = ((Collection)arg0).iterator();
                if (it.hasNext()) {
                    arg0 = it.next();
                } else
                    return;
            }
                
            MClassifier type = (MClassifier)arg0;
            if (type.getModel().equals(ProjectBrowser.TheInstance.getProject().getDefaultModel())) {
                move(type);
            }
        }
    }
    
    private void move(MModelElement elem) {
        Project p = ProjectBrowser.TheInstance.getProject();
        if (p.getUserDefinedModels().contains(elem.getModel())) throw new IllegalStateException("in move: modelelement allready in models");
        if (elem.getNamespace() != elem.getModel()) {
            move(elem.getNamespace());
        } else
            elem.setNamespace(p.getModel());
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidPropertySet(ru.novosoft.uml.MElementEvent)
     */
    protected boolean isValidPropertySet(MElementEvent e) {
        MClassifier type = null;
        if (getTarget() instanceof MParameter)
            type = ((MParameter)getTarget()).getType();
        else
        if (getTarget() instanceof MAttribute)
            type = ((MAttribute)getTarget()).getType();
        else
        if (getTarget() instanceof MAssociationEnd)
            type = ((MAssociationEnd)getTarget()).getType();
        return ((e.getSource() instanceof MAssociationEnd && e.getName().equals("type")) ||
            (e.getSource() instanceof MParameter && e.getName().equals("type")) ||
            (e.getSource() instanceof MAttribute && e.getName().equals("type"))) ||
            (type == e.getSource());
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidRoleAdded(ru.novosoft.uml.MElementEvent)
     */
    protected boolean isValidRoleAdded(MElementEvent e) {
        MModelElement m = (MModelElement)getChangedElement(e);
        return m instanceof MClassifier;
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() == null) return null;
        if (getTarget() instanceof MParameter) {
            return ((MParameter)getTarget()).getType();
        } else
        if (getTarget() instanceof MAttribute) {
            return ((MAttribute)getTarget()).getType();
        } else
        if (getTarget() instanceof MAssociationEnd) {
            return ((MAssociationEnd)getTarget()).getType();
        } else
        return null;
    }

}

