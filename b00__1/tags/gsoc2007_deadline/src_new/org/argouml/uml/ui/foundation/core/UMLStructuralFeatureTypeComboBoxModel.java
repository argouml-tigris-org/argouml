// $Id:UMLStructuralFeatureTypeComboBoxModel.java 13215 2007-08-03 14:47:11Z mvw $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.beans.PropertyChangeEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLComboBoxModel2;

/**
 * The combobox model for the type belonging to some attribute.
 *
 * @since Nov 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLStructuralFeatureTypeComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLStructuralFeatureTypeComboBoxModel.
     */
    public UMLStructuralFeatureTypeComboBoxModel() {
        super("type", false);
        /* TODO: Investigate if the following is needed, and if so, adapt the
         * propertyChange() below.  */
//        Model.getPump().addClassModelEventListener(this,
//                Model.getMetaTypes().getNamespace(), "ownedElement");
    }

    /*
     * This is explained by WFR 2 of a StructuralFeature: 
     * The type of a StructuralFeature must be a Class, DataType, or Interface.
     * 
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().isAClass(element)
                || Model.getFacade().isAInterface(element)
                || Model.getFacade().isADataType(element);
    }

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
     */
    protected void buildModelList() {
        Set elements = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                List path1 = Model.getModelManagementHelper().getPath(o1);
                Collections.reverse(path1);
                List path2 = Model.getModelManagementHelper().getPath(o2);
                Collections.reverse(path2);
                return compareStringLists(path1, path2);
            }
        });

        Project p = ProjectManager.getManager().getCurrentProject();
        if (p == null) {
            return;
        }
        
        for (Object model : p.getUserDefinedModelList()) {
            elements.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getUMLClass()));
            elements.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getInterface()));
            elements.addAll(Model.getModelManagementHelper()
                    .getAllModelElementsOfKind(
                            model, Model.getMetaTypes().getDataType()));
        }

        elements.addAll(Model.getModelManagementHelper()
                .getAllModelElementsOfKind(
                        p.getDefaultModel(),
                        Model.getMetaTypes().getClassifier()));

	// Our comparator will throw an InvalidElementException if the old
	// list contains deleted elements (eg after a new project is loaded)
	// so remove all the old contents first
        removeAllElements();
        addAll(elements);
    }
    
    /**
     * Compare two lists of strings using case-insensitive comparison.
     * @return equivalent of list1.compareTo(list2)
     */
    private static int compareStringLists(List list1, List list2) {
        Iterator i2 = list2.iterator();
        Iterator i1 = list1.iterator();
        while (i2.hasNext()) {
            String name2 = (String) i2.next();
            if (!i1.hasNext()) {
                return -1;
            }
            String name1 = (String) i1.next();
            if (name1 == null) {
                return -1;
            }
            int comparison = name1.compareToIgnoreCase(name2);
            if (comparison != 0) {
                return comparison;
            }
        }
        if (i2.hasNext()) {
            return 1;
        }
        return 0;
    }

    /*
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

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        /*
         * The default behavior for super implementation is
         * to add/remove elements from the list, but it isn't
         * that complex here, because there is no need to
         * change the list on a simple type change.
         */
    }

}
