/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Thomas Neustupny
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Action;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.util.PathComparator;
import org.argouml.util.MyTokenizer;

/**
 * Utility classes for use in diagram popup menus for stereotypes.
 */
public class StereotypeUtility {

    /**
     * Private default constructor.
     */
    private StereotypeUtility() {
        super();
    }

    /**
     * Returns an array of all applicable actions for adding stereotypes for a
     * given model element.
     * 
     * @param modelElement the given model element
     * @return the array with actions for adding stereotype UML objects
     */
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
        return new Action[0];
    }

    /**
     * Returns an array of all applicable actions for adding stereotypes for a
     * given collection of model elements.
     * 
     * @param elements the given collection of model elements
     * @return the array with actions for adding stereotype UML objects
     */
    public static Action[] getApplyStereotypeActions(Collection elements) {
        Set availableStereotypes = getAvailableStereotypes(elements);

        if (!availableStereotypes.isEmpty()) {
            Action[] menuActions = new Action[availableStereotypes.size()];

            Iterator it = availableStereotypes.iterator();
            for (int i = 0; it.hasNext(); ++i) {
                menuActions[i] = new ActionAddStereotype(elements, it.next());
            }
            return menuActions;
        }
        return new Action[0];
    }

    /**
     * Returns a set of all unique applicable stereotypes for a given
     * modelelement.
     * 
     * @param modelElement the given modelelement
     * @return the set with stereotype UML objects
     */
    public static Set<Object> getAvailableStereotypes(Object modelElement) {
        Set<List> paths = new HashSet<List>();
        Set<Object> availableStereotypes = new TreeSet<Object>(
                new PathComparator());

        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {

            Collection models = ProjectManager.getManager().getCurrentProject()
                    .getModels();

            Collection topLevelModels = ProjectManager.getManager()
                    .getCurrentProject().getModels();

            // adds all stereotypes defined at the top level namespaces
            Collection topLevelStereotypes = getTopLevelStereotypes(topLevelModels);

            Collection validTopLevelStereotypes = new ArrayList();

            addAllUniqueModelElementsFrom(availableStereotypes, paths,
                    Model.getExtensionMechanismsHelper()
                            .getAllPossibleStereotypes(models, modelElement));
            for (Object stereotype : topLevelStereotypes) {
                if (Model.getExtensionMechanismsHelper().isValidStereotype(
                        modelElement, stereotype)) {
                    validTopLevelStereotypes.add(stereotype);
                }
            }

            addAllUniqueModelElementsFrom(availableStereotypes, paths,
                    validTopLevelStereotypes);

            // adds all stereotypes defined at the profiles applied to the
            // current namespace
            Object namespace = Model.getFacade().getNamespace(modelElement);
            if (namespace != null) {
                while (true) {
                    getApplicableStereotypesInNamespace(modelElement, paths,
                            availableStereotypes, namespace);
                    Object newNamespace = Model.getFacade().getNamespace(
                            namespace);

                    if (newNamespace == null) {
                        break;
                    }

                    namespace = newNamespace;
                }
            }
        }

        // adds all stereotypes defined at the profiles applied
        // to the current project
        addAllUniqueModelElementsFrom(availableStereotypes, paths,
                ProjectManager.getManager().getCurrentProject()
                        .getProfileConfiguration()
                        .findAllStereotypesForModelElement(modelElement));

        return availableStereotypes;
    }

    /**
     * Returns a set (union) of all unique applicable stereotypes for a given
     * collection of model elements. TODO: This is not optimized for
     * performance.
     * 
     * @param elements the given collection of model elements
     * @return the set with stereotype UML objects
     */
    public static Set<Object> getAvailableStereotypes(Collection elements) {
        Set<Object> availableStereotypes = new TreeSet<Object>(
                new PathComparator());
        if (elements != null) {
            for (Object element : elements) {
                availableStereotypes.addAll(getAvailableStereotypes(element));
            }
        }
        return availableStereotypes;
    }

    private static Collection<Object> getTopLevelStereotypes(
            Collection<Object> topLevelModels) {
        Collection<Object> ret = new ArrayList<Object>();
        for (Object model : topLevelModels) {
            for (Object stereotype : Model.getExtensionMechanismsHelper()
                    .getStereotypes(model)) {
                Object namespace = Model.getFacade().getNamespace(stereotype);
                if (Model.getFacade().getNamespace(namespace) == null) {
                    ret.add(stereotype);
                }
            }
        }
        return ret;
    }

    private static void getApplicableStereotypesInNamespace(
            Object modelElement, Set<List> paths,
            Set<Object> availableStereotypes, Object namespace) {

        Collection allProfiles = getAllProfilePackages(Model.getFacade()
                .getRoot(modelElement));
        Collection<Object> allAppliedProfiles = new ArrayList<Object>();

        for (Object profilePackage : allProfiles) {
            Collection allDependencies = Model.getCoreHelper().getDependencies(
                    profilePackage, namespace);

            for (Object dependency : allDependencies) {
                if (Model.getExtensionMechanismsHelper().hasStereotype(
                        dependency, "appliedProfile")) {
                    allAppliedProfiles.add(profilePackage);
                    break;
                }
            }
        }

        addAllUniqueModelElementsFrom(availableStereotypes, paths,
                getApplicableStereotypes(modelElement, allAppliedProfiles));
    }

    private static Collection<Object> getApplicableStereotypes(
            Object modelElement, Collection<Object> allAppliedProfiles) {
        Collection<Object> ret = new ArrayList<Object>();
        for (Object profile : allAppliedProfiles) {
            for (Object stereotype : Model.getExtensionMechanismsHelper()
                    .getStereotypes(profile)) {
                if (Model.getExtensionMechanismsHelper().isValidStereotype(
                        modelElement, stereotype)) {
                    ret.add(stereotype);
                }
            }
        }

        return ret;
    }

    private static Collection<Object> getAllProfilePackages(Object model) {
        Collection col = Model.getModelManagementHelper()
                .getAllModelElementsOfKind(model,
                        Model.getMetaTypes().getPackage());
        Collection<Object> ret = new ArrayList<Object>();

        for (Object element : col) {
            if (Model.getFacade().isAPackage(element)
                    && Model.getExtensionMechanismsHelper().hasStereotype(
                            element, "profile")) {
                ret.add(element);
            }
        }
        return ret;
    }

    /**
     * Helper method for buildModelList.
     * <p>
     * Adds those elements from source that do not have the same path as any
     * path in paths to elements, and its path to paths. Thus elements will
     * never contain two objects with the same path, unless they are added by
     * other means.
     */
    private static void addAllUniqueModelElementsFrom(Set<Object> elements,
            Set<List> paths, Collection<Object> source) {
        for (Object obj : source) {
            List path = Model.getModelManagementHelper().getPathList(obj);
            if (!paths.contains(path)) {
                paths.add(path);
                elements.add(obj);
            }
        }
    }

    /**
     * Replace the previous set of stereotypes applied to the given modelelement
     * with a new set, given in the form of a "," separated string of stereotype
     * names.
     * 
     * @param element the UML element to modify
     * @param stereotype Comma separated list of stereotype names. Empty string
     *            or <code>null</code> represents no stereotypes.
     * @param removeCurrent true if all current stereotypes should be removed
     *            before adding the new stereotypes, false if new stereotypes
     *            should be added to existing ones.
     */
    public static void dealWithStereotypes(Object element,
            StringBuilder stereotype, boolean removeCurrent) {
        if (stereotype == null) {
            dealWithStereotypes(element, (String) null, removeCurrent);
        } else {
            dealWithStereotypes(element, stereotype.toString(), removeCurrent);
        }
    }

    /**
     * This function shall replace the previous set of stereotypes of the given
     * modelelement with a new set, given in the form of a "," separated string
     * of stereotype names.
     * 
     * @param umlobject the UML element to adapt
     * @param stereotype Comma separated list stereotype names. Empty string or
     *            <code>null</code> represents no stereotypes.
     * @param full false if stereotypes are only added, true if removal should
     *            be done, too.
     */
    public static void dealWithStereotypes(Object umlobject, String stereotype,
            boolean full) {
        String token;
        MyTokenizer mst;
        Collection<String> stereotypes = new ArrayList<String>();

        /*
         * Convert the string (e.g. "aaa,bbb,ccc") into separate
         * stereotype-names (e.g. "aaa", "bbb", "ccc").
         */
        if (stereotype != null) {
            mst = new MyTokenizer(stereotype, " ,\\,");
            while (mst.hasMoreTokens()) {
                token = mst.nextToken();
                if (!",".equals(token) && !" ".equals(token)) {
                    stereotypes.add(token);
                }
            }
        }

        if (full) {
            // collect the to be removed stereotypes
            Collection<Object> toBeRemoved = new ArrayList<Object>();
            for (Object stereo : Model.getFacade().getStereotypes(umlobject)) {
                String stereotypename = Model.getFacade().getName(stereo);
                if (stereotypename != null
                        && !stereotypes.contains(stereotypename)) {
                    toBeRemoved.add(getStereotype(umlobject, stereotypename));
                }
            }

            // and now remove them
            for (Object o : toBeRemoved) {
                Model.getCoreHelper().removeStereotype(umlobject, o);
            }
        }

        // add stereotypes
        for (String stereotypename : stereotypes) {
            if (!Model.getExtensionMechanismsHelper().hasStereotype(umlobject,
                    stereotypename)) {
                Object umlstereo = getStereotype(umlobject, stereotypename);
                if (umlstereo != null) {
                    Model.getCoreHelper().addStereotype(umlobject, umlstereo);
                }
            }
        }
        ProjectManager.getManager().updateRoots();
    }

    /**
     * Finds a stereotype with the given name either in the user model, or in
     * one of the profiles' models. If it's not found, a new stereotype will be
     * created in the root model.
     * 
     * @param obj A ModelElement to find a suitable stereotype for.
     * @param name The name of the stereotype to search for.
     * @return A stereotype named name, or possibly null.
     */
    private static Object getStereotype(Object obj, String name) {
        Object root = Model.getFacade().getRoot(obj);
        Object stereo;

        stereo = findStereotypeContained(obj, root, name);
        // TODO: The following rather than the above is probably the correct
        // way to search
        // stereo = findStereotype(obj, null, name);
        if (stereo != null) {
            return stereo;
        }

        Project project = ProjectManager.getManager().getCurrentProject();
        stereo = project.getProfileConfiguration().findStereotypeForObject(
                name, obj);

        if (stereo != null) {
            return stereo;
        }

        if (root != null && name.length() > 0) {
            stereo = Model.getExtensionMechanismsFactory().buildStereotype(obj,
                    name, root);
        }

        return stereo;
    }

    /**
     * Search for a stereotype with the name given in a namespace and its
     * containing namespaces.
     * 
     * @param obj The model element to be suitable for.
     * @param namespace The namespace to start search at. If null, the namespace
     *            of the given model element will be used as the starting point.
     * @param name The name of the stereotype to search for.
     * @return An stereotype named name, or null if none is found.
     */
    private static Object findStereotype(final Object obj,
            final Object namespace, final String name) {
        Object ns = namespace;
        if (ns == null) {
            ns = Model.getFacade().getNamespace(obj);
            if (ns == null) {
                return null;
            }
        }

        Collection ownedElements = Model.getFacade().getOwnedElements(ns);
        for (Object element : ownedElements) {
            if (Model.getFacade().isAStereotype(element)
                    && name.equals(Model.getFacade().getName(element))) {
                return element;
            }
        }

        // If not found, try the parent namespace
        ns = Model.getFacade().getNamespace(ns);
        if (namespace != null) {
            return findStereotype(obj, ns, name);
        }

        return null;
    }

    /**
     * Search descending recursively for a stereotype with the name given in
     * name. NOTE: You probably don't want to use this because it's searching
     * the wrong direction!
     * 
     * @param obj The model element to be suitable for.
     * @param root The model element to search from.
     * @param name The name of the stereotype to search for.
     * @return An stereotype named name, or null if none is found.
     */
    private static Object findStereotypeContained(Object obj, Object root,
            String name) {
        Object stereo;

        if (root == null) {
            return null;
        }

        if (Model.getFacade().isAStereotype(root)
                && name.equals(Model.getFacade().getName(root))) {
            if (Model.getExtensionMechanismsHelper().isValidStereotype(obj,
                    root)) {
                return root;
            }
        }

        if (!Model.getFacade().isANamespace(root)) {
            return null;
        }

        Collection ownedElements = Model.getFacade().getOwnedElements(root);

        // Loop through each element in the namespace, recursing.
        for (Object ownedElement : ownedElements) {
            stereo = findStereotypeContained(obj, ownedElement, name);
            if (stereo != null) {
                return stereo;
            }
        }
        return null;
    }

}
