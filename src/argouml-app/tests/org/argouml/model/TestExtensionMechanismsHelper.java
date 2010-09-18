/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
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
package org.argouml.model;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

/**
 * TODO: This is currently just a mechanical merge of the tests in
 * from the generic Model test and the NSUML tests.  They need to be
 * reviewed & merged.
 *
 * @author euluis
 * @since 0.19.2
 * @version 0.00
 */
public class TestExtensionMechanismsHelper extends TestCase {

    private Object model;

    private Object pack;

    private Object theClass;

    private Object theStereotype;

    private Collection models;

    /**
     * The constructor.
     *
     * @param n the name
     */
    public TestExtensionMechanismsHelper(String n) {
        super(n);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();

        InitializeModel.initializeDefault();

        model = Model.getModelManagementFactory().createModel();
        pack =
            Model.getModelManagementFactory().buildPackage("pack");
        Model.getCoreHelper().setNamespace(pack, model);

        theClass = Model.getCoreFactory().buildClass("TheClass", pack);
        theStereotype =
            Model.getExtensionMechanismsFactory().buildStereotype(
                theClass, "containedStereotype", pack);
        models = new ArrayList();
        models.add(model);
    }

    /**
     * This method tests the working of getAllPossibleStereotypes when the
     * stereotype is contained within the same package as the model element.
     */
    public void
    testGetAllPossibleStereotypesStereotypeAndMEContainedInSubPackage() {
        Collection stereotypes =
            Model.getExtensionMechanismsHelper()
                .getAllPossibleStereotypes(models, theClass);
        assertTrue("The \"" + theStereotype
                + "\" isn't returned, but is possible.", stereotypes
                .contains(theStereotype));
    }

    /**
     * Test that a stereotype contained in a containing package of the package
     * where the model element is, is applicable to the model element.
     */
    public void testGetAllPossibleStereotypesStereotypeInContainingPackage() {
        Object subpack = 
            Model.getModelManagementFactory().buildPackage(
                "subpack");
        Model.getCoreHelper().setNamespace(subpack, pack);
        theClass =
            Model.getCoreFactory().buildClass("TheClassInSubpack", subpack);
        Collection stereotypes =
            Model.getExtensionMechanismsHelper()
                .getAllPossibleStereotypes(models, theClass);
        assertTrue("The \"" + theStereotype
                + "\" isn't returned, but is possible.", stereotypes
                .contains(theStereotype));
    }


    /**
     * Test if we can create modelelements with the names given.
     */
    public void testGetMetaModelName() {
        CheckUMLModelHelper.metaModelNameCorrect(
                Model.getExtensionMechanismsFactory(),
                TestExtensionMechanismsFactory.getAllModelElements());
    }

    /**
     * Test if we can create a valid stereotype for all the modelelements.
     */
    public void testIsValidStereoType() {
        CheckUMLModelHelper.isValidStereoType(
                Model.getExtensionMechanismsFactory(),
                TestExtensionMechanismsFactory.getAllModelElements());
    }

    /**
     * Test multiple base class support.
     */
    public void testMultipleBaseClasses() {
        String classType = Model.getMetaTypes().getName(theClass);
        Collection baseClasses = 
            Model.getFacade().getBaseClasses(theStereotype);
        assertNotNull("There are no base classes", baseClasses);
        assertEquals("Wrong number of base classes", 1, baseClasses.size());
        assertEquals("Base class doesn't match type of model element",
                classType,
                baseClasses.iterator().next());
        Model.getExtensionMechanismsHelper().removeBaseClass(theStereotype,
                theClass);
        assertEquals("Wrong number of base classes",
                0,
                Model.getFacade().getBaseClasses(theStereotype).size());
        // Test both forms of addBaseClass
        Model.getExtensionMechanismsHelper().addBaseClass(theStereotype,
                theClass);
        Model.getExtensionMechanismsHelper().addBaseClass(theStereotype,
                "myOtherClass");
        assertEquals("Wrong number of base classes",
                2,
                Model.getFacade().getBaseClasses(theStereotype).size());
        assertTrue("Base class not found", Model.getFacade().getBaseClasses(
                theStereotype).contains(classType));
        assertTrue("Base class not found", Model.getFacade().getBaseClasses(
                theStereotype).contains("myOtherClass"));
        // Test remaining form of removeBaseClass
        Model.getExtensionMechanismsHelper().removeBaseClass(theStereotype,
                Model.getMetaTypes().getName(theClass));
        assertEquals("Wrong number of base classes",
                1,
                Model.getFacade().getBaseClasses(theStereotype).size());
    }
    
    public void testTagDefinitions() {
        Facade facade = Model.getFacade();
        Object stereotype = Model.getExtensionMechanismsFactory()
                .createStereotype();
        Object td = Model.getExtensionMechanismsFactory().buildTagDefinition(
                "testTD", stereotype, null, "String");
        Model.getExtensionMechanismsHelper().setTagType(td, "Boolean");
        Model.getCoreHelper().setMultiplicity(td, "1..3");
        assertEquals("1..3", facade.toString(facade.getMultiplicity(td)));
    }
}
