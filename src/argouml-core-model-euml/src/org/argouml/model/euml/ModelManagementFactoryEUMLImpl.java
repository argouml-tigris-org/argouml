// $Id$
/*******************************************************************************
 * Copyright (c) 2007,2010 Tom Morris and other contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris - initial implementation
 *    Bogdan Pistol - Undo support
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
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.Profile;
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
    
    private org.eclipse.uml2.uml.Package theRootModel;

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
        UMLUtil.checkArgs(new Object[] {pack, me}, 
                new Class[] {Namespace.class, PackageableElement.class});
        
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


    public org.eclipse.uml2.uml.Package buildPackage(final String name) {
        RunnableClass run = new RunnableClass() {
            public void run() {
                org.eclipse.uml2.uml.Package pkg =
                    (org.eclipse.uml2.uml.Package) createPackage();
        if (name != null) {
            pkg.setName(name);
        }
            getParams().add(pkg);
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));
        return (org.eclipse.uml2.uml.Package) run.getParams().get(0);
    }
    
    public Object copyPackage(Object source, Object ns) {
        // TODO: Auto-generated method stub
        return null;
    }

    public ElementImport createElementImport() {
       RunnableClass run = new RunnableClass() {
            public void run() {
                getParams().add(UMLFactory.eINSTANCE.createElementImport());
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));
        return (ElementImport) run.getParams().get(0);
    }

    public Model createModel() {
        // TODO: Check for Resource to hold this and create if necessary?
        // This is a discrepancy between MDR which does it here and eUML which
        // does it as part of setRootModel
        RunnableClass run = new RunnableClass() {
            public void run() {
                getParams().add(UMLFactory.eINSTANCE.createModel());
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));
        return (Model) run.getParams().get(0);
    }

    public org.eclipse.uml2.uml.Package createPackage() {
        RunnableClass run = new RunnableClass() {
            public void run() {
                getParams().add(UMLFactory.eINSTANCE.createPackage());
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));
        return (org.eclipse.uml2.uml.Package) run.getParams().get(0);
    }

    public Profile createProfile() {
        RunnableClass run = new RunnableClass() {
            public void run() {
                getParams().add(UMLFactory.eINSTANCE.createProfile());
            }
        };
        editingDomain.getCommandStack().execute(
                new ChangeCommand(editingDomain, run));
        return (Profile) run.getParams().get(0);
    }

    @Deprecated
    public Object createSubsystem() {
        // Removed from UML 2
        throw new NotImplementedException();
    }

    // TODO: get/setRootModel aren't specific to the Model implementation
    // they could probably be moved elsewhere - tfm - 20070530
    @Deprecated
    public void setRootModel(Object rootModel) {
        // TODO: Hook this creating of a new resource in to someplace more
        // more appropriate (perhaps createModel() ?)
        // Better yet add a new method to Model API to create a new top level 
        // project/model/xmi file so we don't depend on side effects
        if (rootModel != null 
                && !(rootModel instanceof org.eclipse.uml2.uml.Package)) {
            throw new IllegalArgumentException(
                    "The rootModel supplied must be a Package. Got a " //$NON-NLS-1$
                    + rootModel.getClass().getName());
        }
	if (theRootModel != null && theRootModel.eResource() != null) {
	    EcoreUtil.remove(theRootModel);
	}
        theRootModel = (org.eclipse.uml2.uml.Package) rootModel;
	if (rootModel != null) {
            Resource r = UMLUtil.getResource(modelImpl, UMLUtil.DEFAULT_URI, 
                    Boolean.FALSE);
            r.getContents().add(theRootModel);
	}
        modelImpl.getModelEventPump().setRootContainer(theRootModel);
    }

    @Deprecated
    public org.eclipse.uml2.uml.Package getRootModel() {
        return theRootModel;
    }

}
