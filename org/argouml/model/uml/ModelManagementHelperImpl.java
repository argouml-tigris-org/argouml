// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.ModelManagementHelper;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MSubsystem;

import org.apache.log4j.Logger;

/**
 * Helper class for UML ModelManagement Package.<p>
 *
 * Current implementation is a placeholder.<p>
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
class ModelManagementHelperImpl implements ModelManagementHelper {
    /**
     * Logger.
     */
    private static final Logger LOG =
	Logger.getLogger(ModelManagementHelperImpl.class);

    /**
     * The model implementation.
     */
    private NSUMLModelImplementation nsmodel;

    /**
     * Don't allow instantiation.
     *
     * @param implementation To get other helpers and factories.
     */
    ModelManagementHelperImpl(NSUMLModelImplementation implementation) {
        nsmodel = implementation;
    }

    /**
     * Returns all subsystems found in this namespace and in its children.
     *
     * @param ns is the namespace
     * @return Collection
     */
    public Collection getAllSubSystems(Object ns) {
        if (ns == null) {
            return new ArrayList();
        }
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        Iterator it = ((MNamespace) ns).getOwnedElements().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.addAll(getAllSubSystems(o));
            }
            if (o instanceof MSubsystem) {
                list.add(o);
            }

        }
        return list;
    }

    /**
     * Returns all namespaces found in this namespace and in its children
     *
     * This method is CPU intensive and therefore needs to be as efficient as
     * possible.
     *
     * @param ns namespace to process
     * @return Collection of all namespaces found
     */
    public Collection getAllNamespaces(Object ns) {
        if (ns == null || !(ns instanceof MNamespace)) {
            return new ArrayList();
        }

        Collection namespaces = ((MNamespace) ns).getOwnedElements();
        // the list of namespaces to return
        List list = Collections.EMPTY_LIST;

        // if there are no owned elements then return empty list
        if (namespaces == Collections.EMPTY_LIST
	    || namespaces.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        // work with an array instead of iterator.
        Object[] nsArray = namespaces.toArray();

        for (int i = 0; i < nsArray.length; i++) {

            Object o = nsArray[i];
            if (o instanceof MNamespace) {

                // only build a namepace if needed, with
                if (list == Collections.EMPTY_LIST) {
                    list = new ArrayList(nsArray.length);
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

    /**
     * Returns all modelelements of the given kind.
     *
     * @param type is the class kind
     * @return Collection
     */
    public Collection getAllModelElementsOfKind(Object type) {
        Class kind = (Class) type;
        if (kind == null) {
            return Collections.EMPTY_LIST;
        }
        Project p = ProjectManager.getManager().getCurrentProject();
        MNamespace model = (MModel) p.getRoot();
        Collection ret = getAllModelElementsOfKind(model, kind);
        if (kind.isAssignableFrom(model.getClass())) {
            ret = new ArrayList(ret);
            ret.add(model);
        }
        return ret;
    }

    /**
     * Returns all modelelements found in this namespace and its children
     * that are of some class kind.<p>
     *
     * This method is CPU intensive and therefore needs to be as efficient as
     * possible.<p>
     *
     * @param nsa is the namespace
     * @param type is the class kind
     * @return Collection
     */
    public Collection getAllModelElementsOfKind(Object nsa, Object type) {
        if (type instanceof String) {
            return getAllModelElementsOfKind(nsa, (String) type);
        }
        Class kind = (Class) type;
        if (nsa == null || kind == null) {
            return Collections.EMPTY_LIST;
        }

        if (!ModelFacade.isANamespace(nsa)) {
            throw new IllegalArgumentException(
                "given argument " + nsa + " is not a namespace");
        }

        Collection elementsCol = ModelFacade.getOwnedElements(nsa);

        // only continue if there are owned elements
        if (elementsCol == Collections.EMPTY_LIST
	    || elementsCol.size() == 0) {
            return Collections.EMPTY_LIST;
        }

        // use array for speed over iterator
        Object[] elements = elementsCol.toArray();
        Object element = null;
        // only instantiate arraylist when needed
        List list = Collections.EMPTY_LIST;

        for (int i = 0; i < elements.length; i++) {

            element = elements[i];

            if (element instanceof MNamespace) {

                if (list == Collections.EMPTY_LIST) {
                    list = new ArrayList(elements.length);
                }

                // get child model elements recursively
                Collection elementsCol1 =
		    getAllModelElementsOfKind(element, kind);

                // only add model elements if there are some
                if (!(elementsCol1 == Collections.EMPTY_LIST)
		    && !(elementsCol1.size() == 0)) {

		    list.addAll(elementsCol1);

                }

            }
            if (kind.isAssignableFrom(element.getClass())) {

                if (list == Collections.EMPTY_LIST) {
                    list = new ArrayList(elements.length);
                }

                list.add(element);
            }
        }
        return list;

    }

    /**
     * Helper method for {@link #getAllModelElementsOfKind(Object, Class)}.
     *
     * @param nsa namespace.
     * @param kind name of class to find, this implementation will add the "M"
     *             for NSUML.
     * @return a Collection.
     */
    public Collection getAllModelElementsOfKind(Object nsa, String kind) {

        if (nsa == null || kind == null) {
            return Collections.EMPTY_LIST;
        }
        if (!ModelFacade.isANamespace(nsa)) {
            throw new IllegalArgumentException(
                "given argument " + nsa + " is not a namespace");
        }
        Collection col = null;
        try {
            // TODO: This assumes we are working with MThings
            col = getAllModelElementsOfKind(nsa, Class.forName("M" + kind));
        } catch (ClassNotFoundException cnfe) {
            LOG.error(cnfe);
            return Collections.EMPTY_LIST;
        }
        return col;
    }

    /**
     * Returns all surrounding namespaces of some namespace ns. See
     * section 2.5.3.24 of the UML 1.3 spec for a definition.
     * @param ns to process
     * @return Collection of surrounding namespaces.
     */
    public Collection getAllSurroundingNamespaces(Object ns) {
        if (!(ns instanceof MNamespace)) {
            throw new IllegalArgumentException();
        }

        Set set = new HashSet();
        set.add(ns);
        MNamespace namespace = ((MNamespace) ns);
        if (namespace.getNamespace() != null) {
            set.addAll(getAllSurroundingNamespaces(namespace.getNamespace()));
        }
        return set;
    }

    /**
     * @return a collection of all behavioralfeatures in the current project
     */
    public Collection getAllBehavioralFeatures() {
        Object model =
            ProjectManager.getManager().getCurrentProject().getModel();
        return getAllBehavioralFeatures(model);
    }

    /**
     * @param ns the given namespace
     * @return a collection of all behavioralfeatures in the given namespace
     */
    public Collection getAllBehavioralFeatures(Object ns) {
        Collection classifiers =
            getAllModelElementsOfKind(ns, ModelFacade.CLASSIFIER);
        ArrayList features = new ArrayList();
        Iterator i = classifiers.iterator();
        while (i.hasNext()) {
            features.addAll(ModelFacade.getFeatures(i.next()));
        }
        ArrayList behavioralfeatures = new ArrayList();
        Iterator ii = features.iterator();
        while (ii.hasNext()) {
            Object f = ii.next();
            if (ModelFacade.isABehavioralFeature(f)) {
                behavioralfeatures.add(f);
            }
        }
        return behavioralfeatures;
    }

    /**
     * Get the modelelement a given path below a given root-namespace.
     *
     * @param path the given path
     * @param theRootNamespace the given namespace to start from
     * @return the modelelement looked for, or null if not found
     */
    public Object getElement(Vector path, Object theRootNamespace) {
        MModelElement root = (MModelElement) theRootNamespace;
        Object name;
        int i;

        for (i = 0; i < path.size(); i++) {
            if (root == null || !(root instanceof MNamespace)) {
                return null;
            }

            name = path.get(i);
            Iterator it = ((MNamespace) root).getOwnedElements().iterator();
            root = null;
            while (it.hasNext()) {
                MModelElement me = (MModelElement) it.next();
                if (i < path.size() - 1
		    && !(me instanceof MNamespace)) {

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

    /**
     * Finds the absolute path of a ModelElement. Ie the name of each
     * namespace starting at the root (the Model) and ending with the
     * name of the element.<p>
     *
     * The returned Vector implicitly starts at the root (the model)
     * and follows element's chain of owning namespaces back down to
     * element. The first element will thus be the name of the top level
     * namespace below the model, and the last element will be the name
     * of element itself. Note thus that for the model the path will be
     * empty.
     *
     * @param  element is the object to resolve the path for.
     * @return A Vector as described above.
     * @throws IllegalArgumentException if element isn't a ModelElement
     *         properly owned by namespaces and a model.
     */
    public Vector getPath(Object element) {
        Vector path;

        if (ModelFacade.isAModel(element)) {
            return new Vector();
        }

        path = getPath(ModelFacade.getNamespace(element));
        path.add(ModelFacade.getName(element));

        return path;
    }

    /**
     * Utility function for managing several overlayed models, eg a user
     * model to which elements from some profile models is imported when
     * needed. This version of the function assumes it is permissible to
     * create missing elements.
     *
     * This function may fail and return null eg if some required object
     * doesn't exist in the target model and cannot be copied.
     *
     * @param elem is some element.
     * @param model is the model the returned object shall belong to.
     * @return An element of the same type and at the same position in the
     *  model as elem, or if that would turn out impossible then null.
     */
    public Object getCorrespondingElement(Object elem, Object model) {
    return getCorrespondingElement(elem, model, true);
    }

    /**
     * Utility function for managing several overlayed models, eg a user
     * model to which elements from some profile models is imported when
     * needed. This version of the function will only copy objects if
     * canCreate is true, but may then also copy other missing elements.
     *
     * This function may fail and return null eg if the required object
     * doesn't exist in the target model and canCreate is false or some
     * required object doesn't exist in the target model and cannot be
     * copied.
     *
     * @param elem is some element.
     * @param model is the model the returned object shall belong to.
     * @param canCreate determines if objects can be copied into model.
     * @return An element of the same type and at the same position in the
     *  model as elem, or if that would turn out impossible then null.
     */
    public Object getCorrespondingElement(Object elem,
                     Object model, boolean canCreate) {
        if (elem == null || model == null || !(elem instanceof MModelElement)) {
            throw new NullPointerException();
        }

        // Trivial case
        if (((MModelElement) elem).getModel() == model) {
            return elem;
        }

        // Base case
        if (elem instanceof MModel) {
            return model;
        }

        // The cast is actually safe
        MNamespace ns =
            (MNamespace) getCorrespondingElement(
                    ((MModelElement) elem).getNamespace(), model, canCreate);
        if (ns == null) {
            return null;
        }

        Iterator it = ns.getOwnedElements().iterator();
        while (it.hasNext()) {
            MModelElement e = (MModelElement) it.next();
            if (e.getClass() == ((MModelElement) elem).getClass()
                && ((((MModelElement) elem).getName() == null
                     && e.getName() == null)
                    || (((MModelElement) elem).getName() != null
                        && ((MModelElement) elem).getName().equals(
                                e.getName())))) {
                return e;
            }
        }

        if (!canCreate) {
            return null;
        }

        return nsmodel.getCopyHelper().copy(elem, ns);
    }

    /**
     * Tests if two objects are of the same type, have the same name and the
     * same relative position in the model.
     *
     * Same relative position implies either:
     * * their owners correspond to eachother.
     * * they are both owned by model objects.
     *
     * @param obj1 is an object.
     * @param obj2 is another object.
     * @return true if obj1 corresponds to obj2, false otherwise.
     */
    public boolean corresponds(Object obj1, Object obj2) {
        if (!(obj1 instanceof MModelElement)) {
            throw new IllegalArgumentException("obj1");
        }
        if (!(obj2 instanceof MModelElement)) {
            throw new IllegalArgumentException("obj2");
        }

        if (obj1 instanceof MModel && obj2 instanceof MModel) {
            return true;
        }
        if (obj1.getClass() != obj2.getClass()) {
            return false;
        }

        MModelElement modelElement1 = (MModelElement) obj1;
        MModelElement modelElement2 = (MModelElement) obj2;
        if ((modelElement1.getName() == null
             && modelElement2.getName() != null)
	    || (modelElement1.getName() != null
	        && !modelElement1.getName().equals(modelElement2.getName()))) {

            return false;

	}
        return corresponds(modelElement1.getNamespace(),
                	   modelElement2.getNamespace());
    }

    /**
     * Checks if a child for some ownershiprelationship (as in a
     * namespace A is owned by a namespace B) is allready in the
     * ownerhship relation.
     *
     * @param parent The current leaf for the ownership relation
     * @param child The child that should be owned by the parent
     * @return true if the child is allready in the ownership relationship
     */
    public boolean isCyclicOwnership(Object parent, Object child) {
        return (getOwnerShipPath(parent).contains(child) || parent == child);
    }


    private List getOwnerShipPath(Object elem) {
        if (ModelFacade.isABase(elem)) {
            List ownershipPath = new ArrayList();
            Object parent = ModelFacade.getModelElementContainer(elem);
            while (parent != null) {
                ownershipPath.add(parent);
                parent = ModelFacade.getModelElementContainer(parent);
            }
            return ownershipPath;
        }
        throw new IllegalArgumentException("Not a base");
    }
}

