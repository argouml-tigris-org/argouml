/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefObject;
import javax.jmi.reflect.RefPackage;

import org.argouml.model.InvalidElementException;
import org.argouml.model.ModelManagementHelper;
import org.omg.uml.behavioralelements.collaborations.Collaboration;
import org.omg.uml.behavioralelements.commonbehavior.Instance;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.Dependency;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Permission;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.ElementImport;
import org.omg.uml.modelmanagement.Subsystem;
import org.omg.uml.modelmanagement.UmlPackage;

/**
 * Helper class for UML ModelManagement Package.
 * <p>
 *
 * @since ARGO0.19.5
 * @author Ludovic Ma&icirc;tre
 * <p>
 * derived from NSUML implementation by:
 * @author Thierry Lach
 */
class ModelManagementHelperMDRImpl implements ModelManagementHelper {

    private static final Logger LOG =
        Logger.getLogger(ModelManagementHelperMDRImpl.class.getName());

    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Construct a ModelManagementHelper.  Not for use outside of the
     * Model subsystem implementation.
     *
     * @param implementation
     *            To get other helpers and factories.
     */
    ModelManagementHelperMDRImpl(MDRModelImplementation implementation) {
        modelImpl = implementation;
    }


    public Collection getAllSubSystems(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((Namespace) ns).getOwnedElement().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof Namespace) {
                list.addAll(getAllSubSystems(o));
            }
            if (o instanceof Subsystem) {
                list.add(o);
            }

        }
        return list;
    }

    /*
     * This method is CPU intensive and therefore needs to be as efficient as
     * possible.
     */
    public Collection getAllNamespaces(Object ns) {

        if (ns == null || !(ns instanceof Namespace)) {
            return Collections.EMPTY_LIST;
        }

        Collection namespaces = ((Namespace) ns).getOwnedElement();
        // the list of namespaces to return
        List list = Collections.EMPTY_LIST;

        // if there are no owned elements then return empty list
        if (namespaces == Collections.EMPTY_LIST || namespaces.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        for (Iterator it = namespaces.iterator(); it.hasNext();) {
            Object o = it.next();
            if (o instanceof Namespace) {

                // only build a namepace if needed, with
                if (list == Collections.EMPTY_LIST) {
                    list = new ArrayList(namespaces.size());
                }

                list.add(o);

                Collection namespaces1 = getAllNamespaces(o);
                // only add all if there are some to add.
                if (namespaces1 != Collections.EMPTY_LIST
                        && namespaces1.size() > 0) {
                    list.addAll(namespaces1);
                }
            }
        }
        return list;
    }


    public Collection getAllModelElementsOfKindWithModel(Object model,
            Object type) {
        if (model == null) {
            throw new IllegalArgumentException("A model must be supplied");
        }
        Class kind = (Class) type;
        Collection ret = getAllModelElementsOfKind(model, kind);
        if (kind.isAssignableFrom(model.getClass()) && !ret.contains(model)) {
            // TODO: It doesn't really make sense that a namespace would be
            // returned as part of its own contents, but that's the historical
            // behavior.
            ret = new ArrayList(ret);
            ret.add(model);
        }
        return ret;
    }


    public Collection getAllModelElementsOfKind(Object nsa, Object type) {
        // TODO: Performance critical method
        long startTime = System.currentTimeMillis();
        if (nsa == null || type == null) {
            return Collections.EMPTY_LIST;
        }
        if (type instanceof String) {
            return getAllModelElementsOfKind(nsa, (String) type);
        }
        if (!(nsa instanceof Namespace) || !(type instanceof Class)) {
            throw new IllegalArgumentException("illegal argument - namespace: "
                    + nsa + " type: " + type);
        }

        /*
         * Because we get the metatype class stripped of its reflective
         * proxies, we need to jump through a hoop or two to find it
         * in the metamodel, then work from there to get its proxy.
         */
        String name = ((Class) type).getName();
        name = name.substring(name.lastIndexOf(".") + 1);
        if (name.startsWith("Uml")) {
            name = name.substring(3);
        }

        Collection allOfType = Collections.emptySet();
        try {
            RefPackage extent = ((RefObject) nsa).refOutermostPackage();
            RefClass classProxy = ((FacadeMDRImpl) modelImpl.getFacade())
                    .getProxy(name, extent);
            allOfType = classProxy.refAllOfType();
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }

        // Remove any elements not in requested namespace
        Collection returnElements = new ArrayList();
        // TODO: Perhaps use a HashSet or other collection with faster lookup
        // performance in case our callers are doing naive .contains() lookups
        for (Iterator i = allOfType.iterator(); i.hasNext();) {
            Object me = i.next();
            // TODO: Optimize for root model case? - tfm
            if (contained(nsa, me)) {
                returnElements.add(me);
            }
        }
        if ( LOG.isLoggable( Level.FINE ) ) {
            long duration = System.currentTimeMillis() - startTime;
            LOG.log(Level.FINE, "Get allOfKind took {0} msec.", duration);
        }
        return returnElements;
    }

    /*
     * Check whether model element is contained in given namespace/container.
     */
    private boolean contained(Object container, Object candidate) {
        Object current = ((RefObject) candidate).refImmediateComposite();
        while (current != null) {
            if (container.equals(current)) {
                return true;
            }
            current = ((RefObject) current).refImmediateComposite();
        }
        return false;
    }


    public Collection getAllModelElementsOfKind(Object nsa, String kind) {

        if (nsa == null || kind == null) {
            return Collections.EMPTY_LIST;
        }
        if (!(nsa instanceof Namespace)) {
            throw new IllegalArgumentException("given argument " + nsa
                    + " is not a namespace");
        }
        Collection col = null;
        try {
            col = getAllModelElementsOfKind(nsa, Class.forName(kind));
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException(
                    "Can't derive a class name from " + kind);
        }
        return col;
    }


    public Collection getAllSurroundingNamespaces(Object ns) {
        if (!(ns instanceof Namespace)) {
            throw new IllegalArgumentException();
        }

        Set set = new HashSet();
        set.add(ns);
        Namespace namespace = ((Namespace) ns);
        if (namespace.getNamespace() != null) {
            set.addAll(getAllSurroundingNamespaces(namespace.getNamespace()));
        }
        return set;
    }

    /*
     * TODO: As currently coded, this actually returns all BehavioralFeatures
     * which are owned by Classifiers contained in the given namespace, which
     * is slightly different then what's documented.  It will not include any
     * BehavioralFeatures which are part of the Namespace, but which don't have
     * an owner.
     */
    public Collection getAllBehavioralFeatures(Object ns) {
        // Get Classifiers in Namespace
        ArrayList features = new ArrayList();
        try {
            Collection classifiers = getAllModelElementsOfKind(ns,
                    modelImpl.getMetaTypes().getClassifier());
            Iterator i = classifiers.iterator();
            // Get Features owned by those Classifiers
            while (i.hasNext()) {
                features.addAll(modelImpl.getFacade().getFeatures(i.next()));
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        // Select those Features which are BehavioralFeatures
        ArrayList behavioralfeatures = new ArrayList();
        Iterator ii = features.iterator();
        while (ii.hasNext()) {
            Object f = ii.next();
            if (f instanceof BehavioralFeature) {
                behavioralfeatures.add(f);
            }
        }
        return behavioralfeatures;
    }


    public Collection getAllPossibleImports(Object pack) {
        // TODO: Fully implement this!

        Object container = pack;
        Object cc = modelImpl.getFacade().getModelElementContainer(pack);
        while (cc != null) {
            container = cc;
            cc = modelImpl.getFacade().getModelElementContainer(cc);
        }

        Collection mes = getAllModelElementsOfKind(container,
                modelImpl.getMetaTypes().getModelElement());

        Collection vmes = new ArrayList();
        Iterator i = mes.iterator();
        while (i.hasNext()) {
            Object me = i.next();
            if (modelImpl.getCoreHelper().isValidNamespace(me, pack)) {
                vmes.add(me);
            }
        }

        return vmes;
    }

    public Object getElement(List<String> path, Object theRootNamespace) {
        ModelElement root = (ModelElement) theRootNamespace;

        if (root == null) {
            return getElement(path);
        } else {

            for (int i = 0; i < path.size(); i++) {
                if (root == null || !(root instanceof Namespace)) {
                    return null;
                }

                String name = path.get(i);
                boolean found = false;
                for (ModelElement me : ((Namespace) root).getOwnedElement()) {
                    if (i < path.size() - 1 && !(me instanceof Namespace)) {
                        continue;
                    }
                    if (name.equals(me.getName())) {
                        root = me;
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return null;
                }
            }
            return root;
        }
    }


    public Object getElement (List<String> fullPath) {
        if (fullPath == null || fullPath.isEmpty()) {
            return null;
        }
        Object element = null;
        for (Object root : modelImpl.getFacade().getRootElements()) {
            /*
             * modelImpl.getFacade().getRootElements()  gets all root elements
             * in the UML repository, including available profiles that are not
             * part of the current project (degrades performance).
             *
             * ProjectManager.getManager().getCurrentProject().getRoots() only
             * returns user model roots, and no profiles.
             *
             * ProjectManager.getManager().getCurrentProject().getModels() gets
             * all root models, but no root namespaces.
             *
             * TODO: Which is best? Is there any other way?
             */
            if (((ModelElement) root).getName().equals(fullPath.get(0))) {
                element = root;
                if (root instanceof Namespace && fullPath.size() > 1) {
                    element =
                            modelImpl.getModelManagementHelper().getElement(
                                    fullPath.subList(1, fullPath.size()), root);
                }
                if (element != null) {
                    break;
                }
            }
        }
        return element;
    }


    public List<String> getPathList(Object element) {

        if (element == null) {
            return new ArrayList<String>();
        }
        if (!(element instanceof RefObject)) {
            throw new IllegalArgumentException();
        }

        List<String> path = getPathList(((RefObject) element)
                .refImmediateComposite());
        path.add(modelImpl.getFacade().getName(element));

        return path;
    }


    public List<Object> getRootElements(Object model) {
        List<Object> contents = new ArrayList<Object>();
        contents.add(model);
        return contents;
    }


    public boolean isCyclicOwnership(Object parent, Object child) {
        return (getOwnerShipPath(parent).contains(child) || parent == child);
    }

    /**
     * Return a list of all ModelElements which contain this one, starting with
     * the immediate parent and ending with the top level ModelElement.
     *
     * @param elem
     *            the model element to search for
     * @return a list of ModelElements
     */
    private List getOwnerShipPath(Object elem) {
        if (elem instanceof ModelElement) {
            List ownershipPath = new ArrayList();
            Object parent = modelImpl.getFacade()
                    .getModelElementContainer(elem);
            while (parent != null) {
                ownershipPath.add(parent);
                parent = modelImpl.getFacade().getModelElementContainer(parent);
            }
            return ownershipPath;
        }
        throw new IllegalArgumentException("Not a base");
    }


    public void removeImportedElement(Object pack, Object me) {
        try {
            if (pack instanceof UmlPackage && me instanceof ModelElement) {
                Collection c = ((UmlPackage) pack).getElementImport();
                ElementImport match = null;
                Iterator it = c.iterator();
                while (it.hasNext()) {
                    ElementImport ei = (ElementImport) it.next();
                    if (ei.getImportedElement() == me) {
                        match = ei;
                        break;
                    }
                }
                if (match != null) c.remove(match);
                return;
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        throw new IllegalArgumentException(
                "There must be a Package and a ModelElement we got " + pack + " and " + me);
    }


    public void setImportedElements(Object pack, Collection imports) {
        if (pack instanceof UmlPackage) {
            Collection<ElementImport> currentImports =
                ((UmlPackage) pack).getElementImport();
            Collection<ElementImport> toRemove =
                new ArrayList<ElementImport>();
            Collection toAdd = new ArrayList(imports);
            for (final ElementImport ei : currentImports) {
                if (imports.contains(ei.getImportedElement())) {
                    toAdd.remove(ei.getImportedElement());
                } else {
                    toRemove.add(ei);
                }
            }
            currentImports.removeAll(toRemove); // Should these also be deleted?

            Collection toAddEIs = new ArrayList();
            Iterator i = toAdd.iterator();
            while (i.hasNext()) {
                ModelElement me = (ModelElement) i.next();
                toAddEIs.add(modelImpl.getModelManagementFactory()
                        .buildElementImport(pack, me));
            }
            currentImports.addAll(toAddEIs);
            return;
        }
        throw new IllegalArgumentException(
                "There must be a Package and a ModelElement");
    }


    public void setAlias(Object handle, String alias) {
        if ((handle instanceof ElementImport) && (alias != null)) {
            ((ElementImport) handle).setAlias(alias);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle + " or alias: "
                + alias);
    }

    public void setSpecification(Object handle, boolean specification) {
        if (handle instanceof ElementImport) {
            ((ElementImport) handle).setSpecification(specification);
            return;
        }
        throw new IllegalArgumentException("handle: " + handle);
    }

    public Collection<ModelElement> getContents(Object modelelement) {
        if (modelelement instanceof UmlPackage) {
            return getContents((UmlPackage) modelelement);
        } else if (modelelement instanceof Namespace) {
            return getContents((Namespace) modelelement);
        } else if (modelelement instanceof Instance) {
            return getContents((Instance) modelelement);
        } else if (modelelement == null) {
            // This is silly, but for backward compatibility
            return Collections.emptySet();
        }
        throw new IllegalArgumentException("Unsupported element type "
                + modelelement);
    }

    /**
     * <p>A helper method to make {@link #getContents(Object)} as
     * efficient as possible.</p>
     * <p>This is called passing in a collection to place the result
     * rather than creating a new instance of a collection to return</p>
     * @param results a collection of model elements to which the contents
     * are to be added
     * @param modelelement the model element to get the contents from
     */
    private void getContents(
            final Collection<ModelElement> results,
            final Object modelelement) {
        if (modelelement instanceof UmlPackage) {
            getContents(results, (UmlPackage) modelelement);
            return;
        } else if (modelelement instanceof Namespace) {
            getContents(results, (Namespace) modelelement);
            return;
        } else if (modelelement instanceof Instance) {
            getContents(results, (Instance) modelelement);
            return;
        } else if (modelelement == null) {
            return;
        }
        throw new IllegalArgumentException("Unsupported element type "
                + modelelement);
    }

    /**
     * Get the contents of a Package.
     * <p>
     * For a Package: <pre>
     * [1] The operation contents results in a Set containing
     * the ModelElements owned by or imported by the Package.
     * contents : Set(ModelElement)
     * contents = self.ownedElement->union(self.importedElement)
     * </pre>
     * For a Subsystem (subtype of Package): <pre>
     * [2] The operation contents results in a Set containing
     * the ModelElements owned by or imported by the Subsystem.
     *   contents : Set(ModelElement)
     *   contents = self.ownedElement->union(self.importedElement)
     * </pre>
     * @param pkg package to get contents of
     * @return all owned plus imported elements
     */
    static Collection<ModelElement> getContents(UmlPackage pkg) {
        Collection<ModelElement> results = new ArrayList<ModelElement>();
        Collection<ElementImport> c = pkg.getElementImport();
        for (ElementImport ei : c) {
            results.add(ei.getImportedElement());
        }
        results.addAll(getContents((Namespace) pkg));
        return results;
    }

    /**
     * Adds the contents of a package to the given collection.
     * Same as {@link #getContents(UmlPackage)} but adding to results to an
     * existing collection instead of returning a new collection.
     */
    private static void getContents(
            final Collection<ModelElement> results,
            final UmlPackage pkg) {
        Collection<ElementImport> c = pkg.getElementImport();
        for (ElementImport ei : c) {
            results.add(ei.getImportedElement());
        }
        getContents(results, (Namespace) pkg);
    }

    /**
     * Get the contents of a Namespace (which includes the contents of
     * all owning namespaces).
     * <p>
     * For a Namespace: <pre>
     * [1] The operation contents results in a Set containing
     * all ModelElements contained by the Namespace.
     * contents : Set(ModelElement)
     * contents = self.ownedElement -> union(self.namespace, contents)
     * </pre>
     * @param namespace Namespace to get contents of
     * @return contents of namespace and all containing namespaces
     */
    static Collection<ModelElement> getContents(Namespace namespace) {
        Collection<ModelElement> results = new ArrayList<ModelElement>();
        results.addAll(namespace.getOwnedElement());
        Namespace owner = namespace.getNamespace();
        if (owner != null) {
            results.addAll(getContents(owner));
        }
        // TODO: Should we handle <<access>> and <<import>> here?
        return results;
    }

    /**
     * Adds the contents of a namespace to the given collection.
     * Same as {@link #getContents(Namespace)} but adding to results to an
     * existing collection instead of returning a new collection.
     */
    private static void getContents(
            final Collection<ModelElement> results,
            final Namespace namespace) {
        results.addAll(namespace.getOwnedElement());
        Namespace owner = namespace.getNamespace();
        if (owner != null) {
            getContents(results, owner);
        }
        // TODO: Should we handle <<access>> and <<import>>?
    }

    /**
     * Return the contents of an Instance.
     * For a Instance: <pre>
     * [5] The operation contents results in a Set containing all
     * ModelElements contained by the Instance.
     *   contents: Set(ModelElement);
     *   contents = self.ownedInstance->union(self.ownedLink)
     * </pre>
     * @param instance the instance
     * @return a collection containing all owned instances and links
     */
    static Collection<ModelElement> getContents(Instance instance) {
        Collection<ModelElement> results = new ArrayList<ModelElement>();
        results.addAll(instance.getOwnedInstance());
        results.addAll(instance.getOwnedLink());
        return results;
    }

    /**
     * Adds the contents of an instance to the given collection.
     * Same as {@link #getContents(Instance)} but adding to results to an
     * existing collection instead of returning a new collection.
     */
    private static void getContents(
            final Collection<ModelElement> results,
            final Instance instance) {
        results.addAll(instance.getOwnedInstance());
        results.addAll(instance.getOwnedLink());
    }

    public Collection<ModelElement> getAllImportedElements(Object pack) {
        if (!(pack instanceof Namespace)) {
            return Collections.emptyList();
        }
        Collection<ModelElement> ret = new ArrayList<ModelElement>();
        getAllImportedElements(ret, pack);
        return ret;
    }

    /**
     * <p>A helper method to make {@link #getAllImportedElements(Object)} as
     * efficient as possible.</p>
     * <p>This is called passing in a collection to place the result
     * rather than creating a new instance of a collection to return</p>
     * @param results a collection of modelelements to which the imported
     * elements are to be added
     * @param pack the package to get the imported elements from
     */
    private void getAllImportedElements(
            final Collection<ModelElement> results,
            final Object pack) {
        if (!(pack instanceof Namespace)) {
            return;
        }
        Namespace ns = ((Namespace) pack);
        try {
            /* TODO: This is not according the contract for this function, but
             * it is used in several places, and I (MVW) presume that
             * we need this generally.
             * This part (1) is about drawing an <<import>> permission
             * between packages.
             * The part (2) below is about ModelManagement.ElementImport. */
            Collection<Dependency> deps = ns.getClientDependency();
            for (Dependency dep : deps) {
                if (dep instanceof Permission) {
                    if (modelImpl.getExtensionMechanismsHelper()
                            .hasStereotype(dep, FRIEND_STEREOTYPE)) {
                        for (ModelElement o : dep.getSupplier()) {
                            if (o instanceof Namespace) {
                                results.addAll(((Namespace) o).getOwnedElement());
                            }
                        }
                    } else if (modelImpl.getExtensionMechanismsHelper()
                            .hasStereotype(dep, IMPORT_STEREOTYPE)
                            || modelImpl.getExtensionMechanismsHelper()
                                    .hasStereotype(dep, ACCESS_STEREOTYPE)) {
                        for (ModelElement o : dep.getSupplier()) {
                            if (o instanceof Namespace) {
                                results.addAll(CoreHelperMDRImpl
                                        .getAllVisibleElements((Namespace) o));
                            }
                        }
                    }
                }
            }
            /* TODO: This is the 2nd part of this method: */
            if (ns instanceof UmlPackage) {
                results.addAll(modelImpl.getFacade().getImportedElements(ns));
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }


    public Collection<ModelElement> getAllContents(Object pack) {
        // TODO: Is there anyway we can determine this size at runtime?
        Set<ModelElement> results = new HashSet<ModelElement>(2000);
        Set<ModelElement> dupCheck = new HashSet<ModelElement>(2000);
        getAllContents(results, (ModelElement) pack, dupCheck);
        return results;
    }

    /**
     * <p>A helper method to make {@link #getAllContents(Object)} as efficient
     * as possible. This is called recursively.</p>
     * <p>
     * @param results a collection that will be populated with model elements
     * @param pack the namespace to get the contents from
     * @param dupCheck a collection of model elements that have already been
     * checked on a previous recursive call. Checking this saves duplicating
     * effort on already processed elements.
     */
    void getAllContents(
            final Collection<ModelElement> results,
            final ModelElement pack,
            final Collection<ModelElement> dupCheck) {
        if (pack == null || dupCheck.contains(pack)) {
            return;
        }

        dupCheck.add(pack);

        try {
            /*
             * For a Namespace:
             * <pre>
             * [2] The operation allContents results in a Set containing
             * all ModelElements contained by the Namespace.
             *   allContents : Set(ModelElement);
             *   allContents = self.contents
             * where
             *   contents = self.ownedElement -> union(self.namespace, contents)
             * </pre><p>
             */
            if (pack instanceof Namespace) {
                getContents(results, pack);
            }

            /*
             * For a Classifier:
             * <pre>
             * [10] The operation allContents returns a Set containing
             * all ModelElements contained in the Classifier together
             * with the contents inherited from its parents.
             *   allContents : Set(ModelElement);
             *   allContents = self.contents->union(
             *       self.parent.allContents->select(e |
             *            e.elementOwnership.visibility = #public or
             *            e.elementOwnership.visibility = #protected))
             * where parent is defined for GeneralizableElement as:
             * [1] The operation parent returns a Set containing all direct
             * parents
             *   parent : Set(GeneralizableElement);
             *   parent = self.generalization.parent
             * </pre><p>
             */

            /*
             * For a Package:
             * <pre>
             * [3]  The operation allContents results in a Set containing
             * the ModelElements owned by or imported
             * by the Package or one of its ancestors.
             *   allContents : Set(ModelElement);
             *   allContents = self.contents->union(
             *     self.parent.allContents->select(e |
             *          e.elementOwnership.visibility = #public or
             *          e.elementOwnership.visibility = #protected))
             *
             * where the required operations are defined as :
             *
             * [1] The operation contents results in a Set containing the
             * ModelElements owned by or imported by the Package.
             *   contents : Set(ModelElement)
             *   contents = self.ownedElement->union(self.importedElement)
             * [2] The operation allImportedElements results in a Set containing
             * the ModelElements imported by the Package or one of its parents.
             *   allImportedElements : Set(ModelElement)
             *   allImportedElements = self.importedElement->union(
             *     self.parent.oclAsType(Package).allImportedElements->select(
             *                   re | re.elementImport.visibility = #public or
             *                        re.elementImport.visibility = #protected))
             * </pre>
             */

            if (pack instanceof Classifier || pack instanceof UmlPackage) {
                Collection<GeneralizableElement> parents =
                    CoreHelperMDRImpl.getParents((GeneralizableElement) pack);
                // TODO: Try reusing the same set on every recursion
                Set<ModelElement> allContents = new HashSet<ModelElement>(2000);
                for (GeneralizableElement parent : parents) {
                    getAllContents(allContents, parent, dupCheck);
                }

                if (pack instanceof UmlPackage) {
                    getAllImportedElements(allContents, pack);
                    for (GeneralizableElement parent : parents) {
                        getAllImportedElements(allContents, parent);
                    }
                }

                for (ModelElement element : allContents) {
                    if (VisibilityKindEnum.VK_PUBLIC.equals(element
                            .getVisibility())
                            || VisibilityKindEnum.VK_PROTECTED.equals(element
                                    .getVisibility())) {
                        results.add(element);
                    }
                }

            }



            /*
             * For a Collaboration:
             * <pre>
             * [1 ] The operation allContents results in the set of
             * all ModelElements contained in the Collaboration
             * together with those contained in the parents
             * except those that have been specialized.
             *   allContents : Set(ModelElement);
             *   allContents = self.contents->union (
             *                       self.parent.allContents->reject ( e |
             *                       self.contents.name->include (e.name) ))
             *
             *  parent here is the GeneralizableElement definition
             * </pre>
             */
            if (pack instanceof Collaboration) {
                // TODO: Not implemented
                LOG.log(Level.FINE,
                        "Not implemented - getAllContents for: {0}", pack);
            }
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }

    public boolean isReadOnly(Object element) {
        try {
            RefPackage extent = ((RefObject) element).refOutermostPackage();
            return modelImpl.isReadOnly(extent);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
    }

}
