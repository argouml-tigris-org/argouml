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
import java.util.Collections;
import java.util.Vector;

import org.argouml.model.ModelManagementHelper;
import org.eclipse.uml2.uml.Namespace;


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
     *            The ModelImplementation.
     */
    public ModelManagementHelperEUMLImpl(
            EUMLModelImplementation implementation) {
        modelImpl = implementation;
    }

    public boolean corresponds(Object obj1, Object obj2) {
        // TODO Auto-generated method stub
        return false;
    }

    public Collection getAllBehavioralFeatures(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllContents(Object namespace) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllImportedElements(Object pack) {
        // TODO Auto-generated method stub
        return null;
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

        return Collections.EMPTY_SET;
        
        // TODO: MDR impelementation below - needs review/conversion
        
        /*
         * Because we get the metatype class stripped of its reflective
         * proxies, we need to jump through a hoop or two to find it
         * in the metamodel, then work from there to get its proxy.
         */
//        String name = ((Class) type).getName();
//        name = name.substring(name.lastIndexOf(".") + 1);
//        if (name.startsWith("Uml")) name = name.substring(3);
//
//        Collection allOfType = Collections.EMPTY_LIST;
//        // Get all (UML) metaclasses and search for the requested one
//        Collection metaTypes = nsmodel.getModelPackage().getMofClass()
//                .refAllOfClass();
//        for (Iterator it = metaTypes.iterator(); it.hasNext();) {
//            MofClass elem = (MofClass) it.next();
//            // TODO: Generalize - assumes UML type names are unique
//            // without the qualifying package names - true for UML 1.4
//            if (name.equals(elem.getName())) {
//                List names = elem.getQualifiedName();
//                // TODO: Generalize to handle more than one level of package
//                // OK for UML 1.4 because of clustering
//                RefPackage pkg = nsmodel.getUmlPackage().refPackage(
//                        (String) names.get(0));
//                // Get the metatype proxy and use it to find all instances
//                RefClass classProxy = pkg.refClass((String) names.get(1));
//                allOfType = classProxy.refAllOfType();
//                break;
//            }
//        }
//
//        // Remove any elements not in requested namespace
//        Collection returnElements = new ArrayList();
//        for (Iterator i = allOfType.iterator(); i.hasNext();) {
//            Object me = i.next();
//            // TODO: Optimize for root model case? - tfm
//            if (contained(nsa, me)) {
//                returnElements.add(me);
//            } 
//        }
//        return returnElements;

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
        try {
            return getAllModelElementsOfKind(nsa, Class.forName(kind));
        } catch (ClassNotFoundException cnfe) {
            throw new IllegalArgumentException(
                    "Can't derive a class name from " + kind);
        }
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

    public Collection getAllNamespaces(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllPossibleImports(Object pack) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllSubSystems(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getAllSurroundingNamespaces(Object ns) {
        // TODO Auto-generated method stub
        return null;
    }

    public Collection getContents(Object namespace) {
        // TODO Auto-generated method stub
        return null;
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

    public Object getElement(Vector path, Object theRootNamespace) {
        // TODO Auto-generated method stub
        return null;
    }

    public Vector<String> getPath(Object element) {
        // TODO: Needs completing - stub implementation only! - tfm
        Vector<String> path = new Vector<String>();
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
