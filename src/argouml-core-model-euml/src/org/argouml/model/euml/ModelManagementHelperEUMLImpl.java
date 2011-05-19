// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial framework 
 *    Bogdan Pistol - initial implementation
 *******************************************************************************/
package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.argouml.model.ModelManagementHelper;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Collaboration;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.VisibilityKind;

/**
 * The implementation of the ModelManagementHelper for EUML2.
 */
class ModelManagementHelperEUMLImpl implements ModelManagementHelper {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;

    /**
     * Constructor.
     * 
     * @param implementation
     *                The ModelImplementation.
     */
    public ModelManagementHelperEUMLImpl(
            EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public Collection getAllBehavioralFeatures(Object ns) {
        return modelImpl.getCoreHelper().getAllBehavioralFeatures(ns);
    }

    @SuppressWarnings("unchecked")
    public Collection getAllContents(Object element) {
        if (!(element instanceof Element)) {
            throw new IllegalArgumentException(
                    "The argument must be instance of Element"); //$NON-NLS-1$
        }
        Collection result = new HashSet();
        if (element instanceof Collaboration) {
            // TODO: implement
        }
        if (element instanceof Classifier) {
            result.addAll(((Classifier) element).allFeatures());
        }
        if (element instanceof Namespace) {
            result.addAll(((Namespace) element).getMembers());
        }
        if (element instanceof org.eclipse.uml2.uml.Package) {
            result.addAll(((org.eclipse.uml2.uml.Package) element).getPackagedElements());
            org.eclipse.uml2.uml.Package p = ((org.eclipse.uml2.uml.Package) element).getNestingPackage();
            while (p != null) {
                for (PackageableElement e : p.getPackagedElements()) {
                    if (e.getVisibility() == VisibilityKind.PUBLIC_LITERAL) {
                        result.add(e);
                    }
                }
                p = p.getNestingPackage();
            }
        }
        return result;
    }

    public Collection getAllImportedElements(Object pack) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection getAllModelElementsOfKind(Object nsa, Object type) {
        if (!(nsa instanceof Element)) {
            return new ArrayList<Element>();
        }
        Class theType = null;
        if (type instanceof String) {
            try {
                theType = Class.forName((String) type);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        } else if (type instanceof Class) {
            theType = (Class) type;
        } else {
            throw new IllegalArgumentException(
                    "type must be instance of Class or String"); //$NON-NLS-1$
        }
        if (!Element.class.isAssignableFrom(theType)) {
            throw new IllegalArgumentException(
                    "type must represent an Element"); //$NON-NLS-1$
        }

        Collection<Element> result = new ArrayList<Element>();

        for (Element element : ((Namespace) nsa).allOwnedElements()) {
            if (theType.isAssignableFrom(element.getClass())) {
                result.add(element);
            }
        }
        
        return result;
    }

    public Collection getAllModelElementsOfKind(Object nsa, String kind) {
        try {
            return getAllModelElementsOfKind(nsa, Class.forName(kind));
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException(cnfe);
        }
    }

    public Collection getAllModelElementsOfKindWithModel(Object model,
            Object type) {
        if (!(model instanceof Model)) {
            throw new IllegalArgumentException(
                    "model must be instance of Model"); //$NON-NLS-1$
        }
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException(
                    "type must be instance of java.lang.Class"); //$NON-NLS-1$
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

    public Collection getAllNamespaces(Object ns) {
        return getAllModelElementsOfKind(ns, Namespace.class);
    }

    public Collection getAllPossibleImports(Object pack) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection getAllSubSystems(Object ns) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Collection getAllSurroundingNamespaces(Object element) {
        if (!(element instanceof NamedElement)) {
            throw new IllegalArgumentException(
                    "element must be instance of NamedElement"); //$NON-NLS-1$
        }
        return ((NamedElement) element).allNamespaces();
    }

    @SuppressWarnings("unchecked")
    public Collection getContents(Object element) {
        if (!(element instanceof Element)) {
            throw new IllegalArgumentException(
                    "element must be instance of Element"); //$NON-NLS-1$
        }
        Collection result = new HashSet();
        if (element instanceof Namespace) {
            result.addAll(((Namespace) element).getOwnedMembers());
            result.addAll(((Namespace) element).getImportedMembers());
        }
        if (element instanceof org.eclipse.uml2.uml.Package) {
            result.addAll(((org.eclipse.uml2.uml.Package) element).getPackagedElements());
        }
        return result;
    }


    public Object getElement(List<String> path) {
        return getElement(path, null);
    }

    public Object getElement(List<String> path, Object theRootNamespace) {
        // TODO: Auto-generated method stub
        return null;
    }

    public List<String> getPathList(Object element) {
        // TODO: Needs completing - stub implementation only! - tfm
        List<String> path = new ArrayList<String>();
        String name;
        if (modelImpl.getFacade().isANamedElement(element)) {
            name = modelImpl.getFacade().getName(element);
        } else {
            // TODO: Some elements such as Generalization are
            // no longer named.  For a transitional period we'll
            // return a String so debug can continue, but the
            // calling code should probably be fixed. - tfm 20070607
            // Bob says - these are the comments that were in
            // FacadeEUMLImpl.getName. Same trick is needed here
            // for now.
            name = modelImpl.getFacade().getUMLClassName(element)
                + " <not nameable>"; //$NON-NLS-1$
        }

        path.add(name);
        return path;
    }

    public List<Object> getRootElements(Object model) {
        if (model instanceof EObject) {
            List<Object> contents = new ArrayList<Object>();
            contents.addAll(((EObject) model).eResource().getContents());
            if (!contents.contains(model)) {
                contents.add(model);
            }
            return contents;
        }
        throw new IllegalArgumentException(
                "model must be instance of EObject"); //$NON-NLS-1$
    }

    public boolean isCyclicOwnership(Object parent, Object child) {
        // TODO: Auto-generated method stub
        return false;
    }

    public void removeImportedElement(Object handle, Object me) {
        // TODO: Auto-generated method stub
    }

    public void setAlias(Object handle, String alias) {
        // TODO: Auto-generated method stub
    }

    public void setImportedElements(Object pack, Collection imports) {
        // TODO: Auto-generated method stub
    }

    public void setSpecification(Object handle, boolean isSpecification) {
        // TODO: Auto-generated method stub
    }

    public boolean isReadOnly(Object element) {
        // TODO: Auto-generated method stub
        return false;
    }

}
