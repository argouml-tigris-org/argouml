// $Id$
// Copyright (c) 2007, The ArgoUML Project
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the ArgoUML Project nor the
//       names of its contributors may be used to endorse or promote products
//       derived from this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE ArgoUML PROJECT ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE ArgoUML PROJECT BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import org.argouml.model.ModelManagementHelper;
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
    public ModelManagementHelperEUMLImpl(EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    @SuppressWarnings("deprecation")
    public boolean corresponds(Object obj1, Object obj2) {
        // TODO Auto-generated method stub
        return false;
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
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllModelElementsOfKind(Object nsa, Object type) {
        if (!(nsa instanceof Element)) {
            throw new IllegalArgumentException(
                    "nsa must be instance of Element"); //$NON-NLS-1$
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
            throw new IllegalArgumentException("type must represent an Element"); //$NON-NLS-1$
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
        return getAllModelElementsOfKind(nsa, kind);
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
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllSubSystems(Object ns) {
        // TODO Auto-generated method stub
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

    public Object getCorrespondingElement(Object elem, Object model) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getCorrespondingElement(Object elem, Object model,
            boolean canCreate) {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getElement(List<String> path) {
        return getElement(path, null);
    }

    public Object getElement(List<String> path, Object theRootNamespace) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Vector<String> getPath(Object element) {
        // TODO: Needs completing - stub implementation only! - tfm
        Vector<String> path = new Vector<String>();
        path.add(modelImpl.getFacade().getName(element));
        return path;
    }

    public List<String> getPathList(Object element) {
        // TODO: Needs completing - stub implementation only! - tfm
        List<String> path = new ArrayList<String>();
        path.add(modelImpl.getFacade().getName(element));
        return path;
    }
    
    public boolean isCyclicOwnership(Object parent, Object child) {
        // TODO Auto-generated method stub
        return false;
    }

    public void removeImportedElement(Object handle, Object me) {
        // TODO Auto-generated method stub
    }

    public void setAlias(Object handle, String alias) {
        // TODO Auto-generated method stub
    }

    public void setImportedElements(Object pack, Collection imports) {
        // TODO Auto-generated method stub
    }

    public void setSpecification(Object handle, boolean isSpecification) {
        // TODO Auto-generated method stub
    }

}
