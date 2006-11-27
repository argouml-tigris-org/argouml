// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.diagram.ui;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Action;

import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;

/**
 * @author BTarling
 *
 */
public class StereotypeUtility {

    /**
     * Utility classes for 
     */
    private StereotypeUtility() {
        super();
        // TODO: Auto-generated constructor stub
    }

    public static Action[] getApplyStereotypeActions(Object modelElement) {
        Set availableStereotypes = getAvailableStereotypes(modelElement);
        
        if (!availableStereotypes.isEmpty()) {
            Action[] menuActions = new Action[availableStereotypes.size()];

            Iterator it = availableStereotypes.iterator();
            for (int i = 0; it.hasNext(); ++i) {
                menuActions[i] = new ActionAddStereotype(modelElement, 
                        it.next());
            }
            return menuActions;
        }
        return null;
    }

    /**
     * Returns a set of all unique available stereotypes 
     * for a given modelelement.
     * 
     * @param modelElement the given modelelement
     * @return the set with stereotype UML objects
     */
    public static Set getAvailableStereotypes(Object modelElement) {
        Set paths = new HashSet();
        Set availableStereotypes = new TreeSet(new Comparator() {
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
        Collection models =
            ProjectManager.getManager().getCurrentProject().getModels();
            
        addAllUniqueModelElementsFrom(availableStereotypes, paths, Model
                .getExtensionMechanismsHelper().getAllPossibleStereotypes(
                        models, modelElement));
        return availableStereotypes;
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
    
}
