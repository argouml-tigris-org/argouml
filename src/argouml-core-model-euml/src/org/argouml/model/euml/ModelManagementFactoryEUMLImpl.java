/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//     * Redistributions of source code must retain the above copyright
//       notice, this list of conditions and the following disclaimer.
//     * Redistributions in binary form must reproduce the above copyright
//       notice, this list of conditions and the following disclaimer in the
//       documentation and/or other materials provided with the distribution.
//     * Neither the name of the project or its contributors may be used 
//       to endorse or promote products derived from this software without
//       specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE CONTRIBUTORS ``AS IS'' AND ANY
// EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE CONTRIBUTORS BE LIABLE FOR ANY
// DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

package org.argouml.model.euml;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.ModelManagementFactory;
import org.argouml.model.NotImplementedException;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.common.edit.command.ChangeCommand;
import org.eclipse.uml2.uml.ElementImport;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.UMLFactory;


/**
 * The implementation of the ModelManagementFactory for EUML2.
 */
class ModelManagementFactoryEUMLImpl implements ModelManagementFactory,
        AbstractModelFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;
    
    private EditingDomain editingDomain;
    
    private Package theRootModel;

    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public ModelManagementFactoryEUMLImpl(
            EUMLModelImplementation implementation) {
        modelImpl = implementation;
        editingDomain = implementation.getEditingDomain();
    }

    public ElementImport buildElementImport(final Object pack, 
            final Object me) {
        
        if (!(pack instanceof Namespace)) {
            throw new IllegalArgumentException(
                    "pack must be instance of Namespace"); //$NON-NLS-1$
        }
        if (!(me instanceof PackageableElement)) {
            throw new IllegalArgumentException(
                    "me must be instance of PackageableElement"); //$NON-NLS-1$
        }
        RunnableClass run = new RunnableClass() {
            public void run() {
                ElementImport elementImport = createElementImport();
                elementImport.setImportingNamespace((Namespace) pack);
                elementImport.setImportedElement((PackageableElement) me);
                getParams().add(elementImport);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));

        return (ElementImport) run.getParams().get(0);
    }


    public Package buildPackage(String name) {
        Package pkg = (Package) createPackage();
        if (name != null) {
            pkg.setName(name);
        }
        return pkg;
    }
    
    public Object copyPackage(Object source, Object ns) {
        // TODO: Auto-generated method stub
        return null;
    }

    public ElementImport createElementImport() {
        return UMLFactory.eINSTANCE.createElementImport();
    }

    public Model createModel() {
        // TODO: Check for Resource to hold this and create if necessary?
        // This is a discrepancy between MDR which does it here and eUML which
        // does it as part of setRootModel
        return UMLFactory.eINSTANCE.createModel();
    }

    public Package createPackage() {
        return UMLFactory.eINSTANCE.createPackage();
    }

    public Package createProfile() {
        return UMLFactory.eINSTANCE.createProfile();
    }

    @Deprecated
    public Object createSubsystem() {
        // Removed from UML 2
        throw new NotImplementedException();
    }

    // TODO: get/setRootModel aren't specific to the Model implementation
    // they could probably be moved elsewhere - tfm - 20070530
    public void setRootModel(Object rootModel) {
        // TODO: Hook this creating of a new resource in to someplace more
        // more appropriate (perhaps createModel() ?)
        // Better yet add a new method to Model API to create a new top level 
        // project/model/xmi file so we don't depend on side effects
        if (rootModel != null 
                && !(rootModel instanceof Package)) {
            throw new IllegalArgumentException(
                    "The rootModel supplied must be a Package. Got a " //$NON-NLS-1$
                    + rootModel.getClass().getName());
        }
	if (theRootModel != null && theRootModel.eResource() != null) {
	    EcoreUtil.remove(theRootModel);
	}
        theRootModel = (Package) rootModel;
	if (rootModel != null) {
            Resource r = UMLUtil.getResource(modelImpl, UMLUtil.DEFAULT_URI, 
                    Boolean.FALSE);
            r.getContents().add(theRootModel);
	}
        modelImpl.getModelEventPump().setRootContainer(theRootModel);
    }

    public Package getRootModel() {
        return theRootModel;
    }

}
