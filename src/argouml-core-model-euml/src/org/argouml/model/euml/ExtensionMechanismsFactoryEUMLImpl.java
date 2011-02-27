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
 *    thn
 *****************************************************************************/

package org.argouml.model.euml;

import java.util.Collection;

import org.argouml.model.AbstractModelFactory;
import org.argouml.model.ExtensionMechanismsFactory;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.uml.Namespace;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLFactory;

/**
 * The implementation of the ExtensionMechanismsFactory for EUML2.
 */
class ExtensionMechanismsFactoryEUMLImpl implements
        ExtensionMechanismsFactory, AbstractModelFactory {

    /**
     * The model implementation.
     */
    private EUMLModelImplementation modelImpl;
    private EditingDomain editingDomain;


    /**
     * Constructor.
     * 
     * @param implementation
     *            The ModelImplementation.
     */
    public ExtensionMechanismsFactoryEUMLImpl(
            EUMLModelImplementation implementation) {
        modelImpl = implementation;
        editingDomain = implementation.getEditingDomain();
    }

    public Stereotype buildStereotype(Object element, Object name,
            Object namespace) {
        Stereotype stereo = buildStereotype((String) name, 
                (Namespace) namespace);
        // TODO: Add base classes - the following might not even be close!
//        for (Class i : ((Element) element).getClass().getInterfaces()) {
//            if (i instanceof umlmetaclass) {
//                stereo.getExtendedMetaclasses().add(i);
//            }
//        }
        return stereo;
    }

    public Object buildStereotype(Object element, String name,
            Object model, Collection models) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Stereotype buildStereotype(final String name, final Object namespace) {
        RunnableClass run = new RunnableClass() {
            public void run() {
                Stereotype stereo = createStereotype();
                stereo.setName(name);
                if (namespace instanceof Package) {
                    stereo.setPackage((Package) namespace);
                }
                getParams().add(stereo);
        }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Build a stereotype");
        editingDomain.getCommandStack().execute(cmd);
//        cmd.setObjects(run.getParams().get(0));
        return (Stereotype) run.getParams().get(0);
    }

    public Object buildTagDefinition(String name, Object stereotype, Object ns) {
        // TODO: Auto-generated method stub
        return null;
    }
    
    public Object buildTagDefinition(String name, Object stereotype, 
            Object namespace, String tagType) {
        // TODO: Auto-generated method stub
        return null;
    }

    @Deprecated
    public Object buildTaggedValue(String tag, String value) {
        // TODO: Auto-generated method stub
        return null;
    }
    
    public Object buildTaggedValue(Object type, String[] value) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Stereotype copyStereotype(Object source, Object ns) {
        // TODO: Auto-generated method stub
        return null;
    }

    public Object copyTagDefinition(Object aTd, Object aNs) {
        // TODO: Auto-generated method stub
        return null;
    }

    public void copyTaggedValues(Object source, Object target) {
        // TODO: Auto-generated method stub

    }

    public Stereotype createStereotype() {
        RunnableClass run = new RunnableClass() {
            public void run() {
                getParams().add(UMLFactory.eINSTANCE.createStereotype());
            }
        };
        ChangeCommand cmd = new ChangeCommand(
                modelImpl, run,
                "Create a stereotype");
        editingDomain.getCommandStack().execute(cmd);
//        cmd.setObjects(run.getParams().get(0));
        return (Stereotype) run.getParams().get(0);
    }

    public Object createTagDefinition() {
        return UMLFactory.eINSTANCE.createProperty();
    }

    public Object createTaggedValue() {
        // TODO: Auto-generated method stub
        return null;
    }
}
