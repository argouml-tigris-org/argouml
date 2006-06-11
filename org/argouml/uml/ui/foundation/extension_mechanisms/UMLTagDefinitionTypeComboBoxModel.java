// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.ui.UMLComboBoxModel2;

/**
 * The combobox model for the type belonging to some attribute.
 *
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLTagDefinitionTypeComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLTagDefinitionTypeComboBoxModel.
     */
    public UMLTagDefinitionTypeComboBoxModel() {
        super("type", false);
        Model.getPump().addClassModelEventListener(this,
                Model.getMetaTypes().getNamespace(), "ownedElement");
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAClass(element)
                || Model.getFacade().isAInterface(element)
                || Model.getFacade().isADataType(element);
    }

    /**
     * Helper method for buildModelList.
     * <p>
     * Adds those elements from source that do not have the same path as any
     * path in paths to elements, and its path to paths. Thus elements will
     * never contain two objects with the same path, unless they are added by
     * other means.
     */
    private static void addAllUniqueModelElementsFrom(Set elements, Set paths,
            Collection source) {
        Iterator it2 = source.iterator();

        while (it2.hasNext()) {
            Object obj = it2.next();
            Object path = Model.getModelManagementHelper().getPath(obj);
            if (!paths.contains(path)) {
                paths.add(path);
                elements.add(obj);
            }
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Set paths = new HashSet();
        Set elements = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                try {
                    String name1 = Model.getFacade().getName(o1);
                    String name2 = Model.getFacade().getName(o2);
                    name1 = (name1 != null ? name1 : "");
                    name2 = (name2 != null ? name2 : "");

                    return name1.compareTo(name2);
                } catch (Exception e) {
                    throw new ClassCastException(e.getMessage());
                }
            }
        });
        //Object model =
        //    ProjectManager.getManager().getCurrentProject().getRoot();
        Project p = ProjectManager.getManager().getCurrentProject();
        if (p == null) {
            return;
        }
        Iterator it = (new ArrayList(p.getUserDefinedModels())).iterator();

        while (it.hasNext()) {
            Object model = /* (MModel) */it.next();

            addAllUniqueModelElementsFrom(elements, paths, Model
                    .getModelManagementHelper().getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getUMLClass()));
            addAllUniqueModelElementsFrom(elements, paths, Model
                    .getModelManagementHelper().getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getInterface()));
            addAllUniqueModelElementsFrom(elements, paths, Model
                    .getModelManagementHelper().getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getDataType()));
        }

        addAllUniqueModelElementsFrom(elements, paths, Model
                .getModelManagementHelper().getAllModelElementsOfKind(
                        p.getDefaultModel(),
                        Model.getMetaTypes().getClassifier()));

        setElements(elements);
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        Object o = null;
        if (getTarget() != null) {
            o = Model.getFacade().getType(getTarget());
        }
        if (o == null) {
            o = " ";
        }
        return o;
    }

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        /*
         * The default behavior for super implementation is
         * to add/remove elements from the list, but it isn't
         * that simple here, because we'll receive these events
         * on a simple type change.
         */
        if (evt instanceof AddAssociationEvent
                || evt instanceof RemoveAssociationEvent) {
            buildingModel = true;
            if ("ownedElement".equals(evt.getPropertyName())) {
                buildModelList();
            }
            buildingModel = false;
        } else {
            super.propertyChange(evt);
        }
    }

}
