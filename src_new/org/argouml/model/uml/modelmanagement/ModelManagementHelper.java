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

package org.argouml.model.uml.modelmanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Category;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.CopyHelper;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MSubsystem;

/**
 * Helper class for UML ModelManagement Package.
 *
 * Current implementation is a placeholder.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class ModelManagementHelper {
    protected static Category cat =
	Category.getInstance(ModelManagementHelper.class);

    /** Don't allow instantiation.
     */
    private ModelManagementHelper() {
    }

    /** Singleton instance.
    */
    private static ModelManagementHelper SINGLETON =
        new ModelManagementHelper();

    /** Singleton instance access method.
     */
    public static ModelManagementHelper getHelper() {
        return SINGLETON;
    }

    /**
     * Returns all subsystems found in the projectbrowser model
     * @return Collection
     */
    public Collection getAllSubSystems() {
        MNamespace model =
            ProjectManager.getManager().getCurrentProject().getModel();
        return getAllSubSystems(model);
    }

    /**
     * Returns all subsystems found in this namespace and in its children
     * @return Collection
     */
    public Collection getAllSubSystems(MNamespace ns) {
        if (ns == null)
            return new ArrayList();
        Iterator it = ns.getOwnedElements().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.addAll(getAllSubSystems((MNamespace) o));
            }
            if (o instanceof MSubsystem) {
                list.add(o);
            }

        }
        return list;
    }

    /**
     * Returns all namespaces found in the projectbrowser model
     * @return Collection
     */
    public Collection getAllNamespaces() {
        MNamespace model =
            ProjectManager.getManager().getCurrentProject().getModel();
        return getAllNamespaces(model);
    }

    /**
     * Returns all namespaces found in this namespace and in its children
     * @return Collection
     */
    public Collection getAllNamespaces(MNamespace ns) {
        if (ns == null)
            return new ArrayList();
        Iterator it = ns.getOwnedElements().iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.add(o);
                list.addAll(getAllNamespaces((MNamespace) o));
            }
        }
        return list;
    }

    /**
     * Returns all modelelements found in this namespace and its children
     * that are of some class kind n the projectbrowser model
     * @return Collection
     */
    public Collection getAllModelElementsOfKind(Class kind) {
        if (kind == null)
            return new ArrayList();
        Project p = ProjectManager.getManager().getCurrentProject();
        MNamespace model = p.getRoot();
        Collection col = getAllModelElementsOfKind(model, kind);
        return col;
    }

    /**
     * Returns all modelelements found in this namespace and its children
     * that are of some class kind.
     * @param ns
     * @param kind
     * @return Collection
     */
    public Collection getAllModelElementsOfKind(Object nsa, Class kind) {
        if (nsa == null || kind == null)
            return new ArrayList();
        if (!ModelFacade.isANamespace(nsa))
            throw new IllegalArgumentException(
                "given argument " + nsa + " is not a namespace");
        Iterator it = ModelFacade.getOwnedElements(nsa).iterator();
        List list = new ArrayList();
        while (it.hasNext()) {
            Object o = it.next();
            if (o instanceof MNamespace) {
                list.addAll(getAllModelElementsOfKind((MNamespace) o, kind));
            }
            if (kind.isAssignableFrom(o.getClass())) {
                list.add(o);
            }
        }
        return list;

    }

    /**
     * Returns all surrounding namespaces of some namespace ns. See
     * section 2.5.3.24 of the UML 1.3 spec for a definition.
     * @param ns
     * @return Collection
     */
    public Collection getAllSurroundingNamespaces(MNamespace ns) {
        Set set = new HashSet();
        set.add(ns);
        if (ns.getNamespace() != null) {
            set.addAll(getAllSurroundingNamespaces(ns.getNamespace()));
        }
        return set;
    }

    public MModelElement getElement(Vector path, MModelElement root) {
	Object name;
	int i;

	for (i = 0; i < path.size(); i++) {
	    if (root == null || !(root instanceof MNamespace))
		return null;

	    name = path.get(i);
	    Iterator it = ((MNamespace) root).getOwnedElements().iterator();
	    root = null;
	    while (it.hasNext()) {
		MModelElement me = (MModelElement) it.next();
		if (i < path.size() - 1 &&
		    !(me instanceof MNamespace))
		    continue;
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
     * name of the element.
     *
     * <p>The returned Vector implicitly starts at the root (the model)
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

        if (ModelFacade.isAModel(element))
            return new Vector();

        path = getPath(ModelFacade.getNamespace(element));
	path.add(ModelFacade.getName(element));

	return path;
    }

    /**
     * Move a modelelement to a new namespace. The way this is currently
     * implemented this means that ALL modelelements that share the same
     * namespace as the element to be moved are moved.
     * TODO: make this into a copy function
     * TODO: make this only move/copy the asked element
     * @param element
     * @param to
     * @deprecated As of ArgoUml version 0.13.5, 
     *             You should use 
     *          {@link #getCorrespondingElement(MModelElement,MModel,boolean)} 
     *             instead.
     */
    public void moveElement(MModelElement element, MModel to) {
        MModel currentModel = element.getModel();
        if (currentModel != to) {
            if (element.getNamespace() != currentModel) { // handle packages
                moveElement(element.getNamespace(), to);
            } else {
                element.setNamespace(to);
            }
        }
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
    public MModelElement getCorrespondingElement(MModelElement elem,
						 MModel model) {
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
    public MModelElement getCorrespondingElement(MModelElement elem,
					 MModel model, boolean canCreate) {
	if (elem == null || model == null)
	    throw new NullPointerException();

	// Trivial case
	if (elem.getModel() == model)
	    return elem;

	// Base case
	if (elem instanceof MModel)
	    return model;

	// The cast is actually safe
	MNamespace ns = (MNamespace) getCorrespondingElement(
					elem.getNamespace(),
					model,
					canCreate);
	if (ns == null)
	    return null;

	Iterator it = ns.getOwnedElements().iterator();
	while (it.hasNext()) {
	    MModelElement e = (MModelElement) it.next();
	    if (e.getClass() == elem.getClass()
		&& ((elem.getName() == null && e.getName() == null)
		    || (elem.getName() != null
			&& elem.getName().equals(e.getName()))))
	    {
		return (MModelElement) e;
	    }
	}

	if (!canCreate)
	    return null;

	return CopyHelper.getHelper().copy(elem, ns);
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
    public boolean corresponds(MModelElement obj1, MModelElement obj2) {
	if (obj1 instanceof MModel && obj2 instanceof MModel)
	    return true;
	if (obj1.getClass() != obj2.getClass())
	    return false;
	if (obj1.getName() == null && obj2.getName() != null ||
	    obj1.getName() != null && !obj1.getName().equals(obj2.getName()))
		return false;
	return corresponds(obj1.getNamespace(), obj2.getNamespace());
    }

    /**
     * Checks if a child for some ownershiprelationship (as in a
     * namespace A is owned by a namespace B) is allready in the
     * ownerhship relation.
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
            Object parent = ModelFacade.getContainer(elem);
            while (parent != null) {
                ownershipPath.add(parent);
                parent = ModelFacade.getContainer(parent);
            }
            return ownershipPath;
        } else {
            throw new IllegalArgumentException("Not a base");
        }

    }
}

