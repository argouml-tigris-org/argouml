// $Id$
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

package org.argouml.model.mdr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.jmi.model.MofClass;
import javax.jmi.reflect.InvalidObjectException;
import javax.jmi.reflect.RefClass;
import javax.jmi.reflect.RefPackage;

import org.argouml.model.InvalidElementException;
import org.argouml.model.ModelManagementHelper;
import org.omg.uml.behavioralelements.collaborations.Collaboration;
import org.omg.uml.behavioralelements.commonbehavior.Instance;
import org.omg.uml.foundation.core.BehavioralFeature;
import org.omg.uml.foundation.core.Classifier;
import org.omg.uml.foundation.core.GeneralizableElement;
import org.omg.uml.foundation.core.ModelElement;
import org.omg.uml.foundation.core.Namespace;
import org.omg.uml.foundation.core.Permission;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import org.omg.uml.modelmanagement.ElementImport;
import org.omg.uml.modelmanagement.Model;
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
    
    /**
     * The model implementation.
     */
    private MDRModelImplementation modelImpl;

    /**
     * Don't allow instantiation.
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
        if (kind.isAssignableFrom(model.getClass())) {
            ret = new ArrayList(ret);
            if (!ret.contains(model)) {
                ret.add(model);
            }
        }
        return ret;
    }
    

    public Collection getAllModelElementsOfKind(Object nsa, Object type) {
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
        if (name.startsWith("Uml")) name = name.substring(3);

        Collection allOfType = Collections.EMPTY_LIST;
        // Get all (UML) metaclasses and search for the requested one
        Collection metaTypes = modelImpl.getModelPackage().getMofClass()
                .refAllOfClass();
        for (Iterator it = metaTypes.iterator(); it.hasNext();) {
            MofClass elem = (MofClass) it.next();
            // TODO: Generalize - assumes UML type names are unique
            // without the qualifying package names - true for UML 1.4
            if (name.equals(elem.getName())) {
                List names = elem.getQualifiedName();
                // TODO: Generalize to handle more than one level of package
                // OK for UML 1.4 because of clustering
                RefPackage pkg = modelImpl.getUmlPackage().refPackage(
                        (String) names.get(0));
                // Get the metatype proxy and use it to find all instances
                RefClass classProxy = pkg.refClass((String) names.get(1));
                allOfType = classProxy.refAllOfType();
                break;
            }
        }

        // Remove any elements not in requested namespace
        Collection returnElements = new ArrayList();
        for (Iterator i = allOfType.iterator(); i.hasNext();) {
            Object me = i.next();
            // TODO: Optimize for root model case? - tfm
            if (contained(nsa, me)) {
                returnElements.add(me);
            } 
        }
        return returnElements;
    }

    /*
     * Check whether model element is contained in given namespace/container.
     * TODO: Investigate a faster way to do this
     */
    private boolean contained(Object container, Object candidate) {
        Object current = candidate;
        while (current != null) {
            if (container.equals(current))
                return true;
            current = modelImpl.getFacade().getModelElementContainer(current);
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

    
    public Object getElement(Vector path, Object theRootNamespace) {
        ModelElement root = (ModelElement) theRootNamespace;
        Object name;
        int i;

        // TODO: This is very inefficient.  Investigate a direct method - tfm
        
        for (i = 0; i < path.size(); i++) {
            if (root == null || !(root instanceof Namespace)) {
                return null;
            }

            name = path.get(i);
            Iterator it = ((Namespace) root).getOwnedElement().iterator();
            root = null;
            while (it.hasNext()) {
                ModelElement me = (ModelElement) it.next();
                if (i < path.size() - 1 && !(me instanceof Namespace)) {
                    continue;
                }
                if (name.equals(me.getName())) {
                    root = me;
                    break;
                }
            }
        }
        return root;
    }

    
    public Vector getPath(Object element) {
        Vector path;

        // TODO: This only returns the path to the innermost nested Model.
        // We should have a version that returns the full path. - tfm
        if (element == null || element instanceof Model) {
            return new Vector();
        }

        path = getPath(modelImpl.getFacade().getModelElementContainer(element));
        path.add(modelImpl.getFacade().getName(element));

        return path;
    }

    
    public Object getCorrespondingElement(Object elem, Object model) {
        return getCorrespondingElement(elem, model, true);
    }

    /*
     * TODO: This should be supplement/replaced with methods to manage
     * references to external profiles using HREFs rather than using 
     * copy-on-reference semantics
     */
    public Object getCorrespondingElement(Object elem, Object model,
            boolean canCreate) {
        if (elem == null || model == null || !(elem instanceof ModelElement)) {
            throw new NullPointerException("elem: " + elem 
                    + ",model: " + model);
        }

        // Trivial case
        if (modelImpl.getFacade().getModel(elem) == model) {
            return elem;
        }

        // Base case
        if (elem instanceof Model) {
            return model;
        }

        Namespace ns = ((ModelElement) elem).getNamespace();
        if (ns == null) {
            return null;
        }
        ns = (Namespace) getCorrespondingElement(ns, model, canCreate);
        if (ns == null) {
            return null;
        }

        Iterator it = ns.getOwnedElement().iterator();
        while (it.hasNext()) {
            ModelElement e = (ModelElement) it.next();
            if (correspondsInternal(e, (ModelElement) elem)) {
                return e;
            }
        }

        if (!canCreate) {
            return null;
        }

        return modelImpl.getCopyHelper().copy(elem, ns);
    }

    /**
     * Check whether two ModelElements match in type and name.
     * 
     * @param me1 first element
     * @param me2 second element
     * @return true if they match
     */
    private boolean correspondsInternal(ModelElement me1, ModelElement me2) {
        if (me1.getClass() != me2.getClass()) {
            return false;
        }
        if ((me1.getName() == null && me2.getName() != null)
                || (me1.getName() != null 
                        && !me1.getName().equals(me2.getName()))) {

            return false;

        }
        return true;
    }
    

    public boolean corresponds(Object obj1, Object obj2) {
        if (!(obj1 instanceof ModelElement)) {
            throw new IllegalArgumentException("obj1");
        }
        if (!(obj2 instanceof ModelElement)) {
            throw new IllegalArgumentException("obj2");
        }

        if (obj1 instanceof Model && obj2 instanceof Model) {
            return true;
        }

        if (!correspondsInternal((ModelElement) obj1, (ModelElement) obj2)) {
            return false;
        }
        return corresponds(((ModelElement) obj1).getNamespace(), 
                ((ModelElement) obj1).getNamespace());
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
                "There must be a Package and a ModelElement"); 
    }


    public void setImportedElements(Object pack, Collection imports) {
        if (pack instanceof UmlPackage) {
            Collection eis = ((UmlPackage) pack).getElementImport();
            Collection toRemove = new ArrayList();
            Collection toAdd = new ArrayList(imports);
            Iterator i = eis.iterator();
            while (i.hasNext()) {
                ElementImport ei = (ElementImport) i.next();
                if (imports.contains(ei.getImportedElement())) {
                    toAdd.remove(ei);
                } else {
                    toRemove.add(ei);
                }
            }
            eis.removeAll(toRemove); // Should these also be deleted?

            Collection toAddEIs = new ArrayList();
            i = toAdd.iterator();
            while (i.hasNext()) {
                ModelElement me = (ModelElement) i.next();
                toAddEIs.add(modelImpl.getModelManagementFactory()
                        .buildElementImport(pack, me));
            }
            eis.addAll(toAddEIs);
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
        Set<ModelElement> results = new HashSet<ModelElement>();
        if (modelelement == null) {
            return results;
        }

        /*
         * For a Package: <pre>
         * [1] The operation contents results in a Set containing 
         * the ModelElements owned by or imported by the Package.
         * contents : Set(ModelElement)
         * contents = self.ownedElement->union(self.importedElement)
         * </pre>
         * For a Subsystem: <pre>
         * [2] The operation contents results in a Set containing 
         * the ModelElements owned by or imported by the Subsystem.
         *   contents : Set(ModelElement)
         *   contents = self.ownedElement->union(self.importedElement)
         * </pre>
         */
        if (modelelement instanceof UmlPackage) {
            Collection<ElementImport> c = 
                ((UmlPackage) modelelement).getElementImport();
            for (ElementImport ei : c) {
                results.add(ei.getImportedElement());
            }
        }

        /*
         * For a Namespace: <pre>
         * [1] The operation contents results in a Set containing 
         * all ModelElements contained by the Namespace.
         * contents : Set(ModelElement)
         * contents = self.ownedElement -> union(self.namespace, contents)
         * </pre> 
         */
        if (modelelement instanceof Namespace) {
            results.addAll(((Namespace) modelelement).getOwnedElement());
            Namespace ns = ((Namespace) modelelement).getNamespace();
            if (ns != null) {
                results.addAll(getContents(ns));
            }
        }

        /*
         * For a Instance: <pre>
         * [5] The operation contents results in a Set containing all 
         * ModelElements contained by the Instance.
         *   contents: Set(ModelElement);
         *   contents = self.ownedInstance->union(self.ownedLink)
         * </pre>
         */
        if (modelelement instanceof Instance) {
            results.addAll(((Instance) modelelement).getOwnedInstance());
            results.addAll(((Instance) modelelement).getOwnedLink());
        }

        return results;
    }


    public Collection getAllImportedElements(Object pack) {
        Collection c = new ArrayList();
        try {
            /* TODO: This is not according the contract for this function, but
             * it is used in several places, and I (MVW) presume that 
             * we need this generally.
             * This part (1) is about drawing an <<import>> permission
             * between packages.
             * The part (2) below is about ModelManagement.ElementImport. */
            Collection deps = modelImpl.getFacade().getClientDependencies(pack);
            Iterator i = deps.iterator();
            while (i.hasNext()) {
                Object dep = i.next();
                if (dep instanceof Permission) {
                    if (modelImpl.getExtensionMechanismsHelper()
                            .hasStereoType(dep, FRIEND_STEREOTYPE)) {
                        Collection mes = modelImpl.getFacade()
                                .getSuppliers(dep);
                        Iterator mei = mes.iterator();
                        while (mei.hasNext()) {
                            Object o = mei.next();
                            if (modelImpl.getFacade().isANamespace(o)) {
                                Collection v = 
                                    modelImpl.getFacade().getOwnedElements(o);
                                c.addAll(v);
                            }
                        }
                    } else if (modelImpl.getExtensionMechanismsHelper()
                            .hasStereoType(dep, IMPORT_STEREOTYPE)
                            || modelImpl.getExtensionMechanismsHelper()
                                    .hasStereoType(dep, ACCESS_STEREOTYPE)) {
                        Collection mes = modelImpl.getFacade()
                                .getSuppliers(dep);
                        Iterator mei = mes.iterator();
                        while (mei.hasNext()) {
                            Object o = mei.next();
                            if (modelImpl.getFacade().isANamespace(o)) {
                                Collection v = modelImpl.getCoreHelper()
                                        .getAllVisibleElements(o);
                                c.addAll(v);
                            }
                        }
                    }
                }
            }
            /* TODO: This is the 2nd part of this method: */
            Collection imports = modelImpl.getFacade()
                    .getImportedElements(pack);
            c.addAll(imports);
        } catch (InvalidObjectException e) {
            throw new InvalidElementException(e);
        }
        return c;
    }


    public Collection<ModelElement> getAllContents(Object pack) {
        Set<ModelElement> results = new HashSet<ModelElement>();
        if (pack == null) {
            return results;
        }
        
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
            results.addAll(getContents(pack));
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
         * [1] The operation parent returns a Set containing all direct parents
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
         *     self.parent.oclAsType(Package).allImportedElements->select( re |
         *                         re.elementImport.visibility = #public or
         *                         re.elementImport.visibility = #protected))
         * </pre>
         */

        if (pack instanceof Classifier || pack instanceof Package) {
            Collection<GeneralizableElement> ges = 
                modelImpl.getCoreHelper().getParents(pack);
            Collection<ModelElement> allContents = new HashSet<ModelElement>();
            for (GeneralizableElement ge : ges) {
                allContents.addAll(getAllContents(ge));
            }
            for (ModelElement element : allContents) {
                if (element.getVisibility()
                        .equals(VisibilityKindEnum.VK_PUBLIC)
                        || element.getVisibility().equals(
                                VisibilityKindEnum.VK_PROTECTED)) {
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
            throw new RuntimeException("Not implement - getAllContents for: "
                    + pack);
        }

        return results;

    }

}
