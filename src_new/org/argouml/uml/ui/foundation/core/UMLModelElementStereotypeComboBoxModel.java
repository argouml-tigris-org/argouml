// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.uml.ui.UMLComboBoxModel2;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/**
 * @since Oct 10, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLModelElementStereotypeComboBoxModel extends UMLComboBoxModel2 {

    /**
     * Constructor for UMLModelElementStereotypeComboBoxModel.
     * @param container
     */
    public UMLModelElementStereotypeComboBoxModel() {
        super("stereotype", true);
        UmlModelEventPump.getPump().addClassModelEventListener(this, MNamespace.class, "ownedElement");
    }

    
    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(ru.novosoft.uml.MBase)
     */
    protected boolean isValidElement(Object o) {
        return org.argouml.model.ModelFacade.isAStereotype(o) 
            && ExtensionMechanismsHelper.getHelper().isValidStereoType((MModelElement) getTarget(), (MStereotype) o);
    }

    /**
     * Helper method for buildModelList
     *
     * <p>Adds those elements from source that do not have the same path as
     * any path in paths to elements, and its path to paths. Thus elements
     * will never contain two objects with the same path, unless they are
     * added by other means.
     */
    private static void addAllUniqueModelElementsFrom(Set elements, Set paths,
							Collection source) {
        Iterator it2 = source.iterator();

	while (it2.hasNext()) {
	    Object obj = it2.next();
	    Object path = ModelManagementHelper.getHelper().getPath(obj);
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
        MModelElement elem = (MModelElement) getTarget();
        Set paths = new HashSet();
        Set elements = new TreeSet(new Comparator() {
            public int compare(Object o1, Object o2) {
                try {
                    String name1 = o1 instanceof String ? (String) o1 : ModelFacade.getName(o1);
                    String name2 = o2 instanceof String ? (String) o2 : ModelFacade.getName(o2);
                    name1 = (name1 != null ? name1 : "");
                    name2 = (name2 != null ? name2 : "");

                    return name1.compareTo(name2);
                } catch (Exception e) {
                    throw new ClassCastException(e.getMessage());
                }
            }});
	addAllUniqueModelElementsFrom(
	    elements,
	    paths,
	    ExtensionMechanismsHelper.getHelper().getAllPossibleStereotypes(elem)
	    );
        setElements(elements);
    }   

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#getSelectedModelElement()
     */
    protected Object getSelectedModelElement() {
        if (getTarget() != null) {
            Object stereo = null;
            if (ModelFacade.getStereotypes(getTarget()).size() > 0) {
                stereo = ModelFacade.getStereotypes(getTarget()).iterator().next();
            }
            return stereo;
        }
        return null;
    }

}