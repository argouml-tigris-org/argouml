/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    mvw
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007-2009 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.notation.providers.java;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.ModelElementNameNotation;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Tests the ModelElementNameNotationJava class.
 * 
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestModelElementNameNotationJava extends TestCase {
    private Object theClass;
    private Object model;

    private Object getModel() {
        return model;
    }

    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        model = Model.getModelManagementFactory().createModel();
        theClass = Model.getCoreFactory().buildClass("TheClass", getModel());
    }
    
    /**
     * Test if we can parse a class path and set it abstract.
     */
    public void testParsingPath() {
        Object pack1 = Model.getModelManagementFactory().buildPackage("p1");
        Model.getCoreHelper().setNamespace(pack1, getModel());
        Object pack2 = Model.getModelManagementFactory().buildPackage("p2");
        Model.getCoreHelper().setNamespace(pack2, pack1);
        Model.getCoreHelper().setNamespace(theClass, pack2);
        Model.getCoreHelper().setAbstract(theClass, false);
        assertFalse("Could not build a non-abstract class", 
                Model.getFacade().isAbstract(theClass));
        ModelElementNameNotation notation = 
            new ModelElementNameNotationJava(theClass);
        notation.parse(theClass, "abstract p1.p2.TheClass");
        assertTrue("Could not parse abstract class with path", 
                Model.getFacade().isAbstract(theClass));
        notation.parse(theClass, "TheClass");
        assertTrue("Abstract class misbehavior",
                Model.getFacade().isAbstract(theClass)); 
    }

    /**
     * Test that we parse a class path and modify its location.
     */
    public void testParsingPathAndModify() {
        Model.getCoreHelper().setName(getModel(), "root-model");
        Object mod1 = Model.getModelManagementFactory().createModel();
        Model.getCoreHelper().setName(mod1, "mod1");
        Model.getCoreHelper().setNamespace(mod1, getModel());
        Object pack1 = Model.getModelManagementFactory().buildPackage("p1");
        Model.getCoreHelper().setNamespace(pack1, mod1);
        Object pack2 = Model.getModelManagementFactory().buildPackage("p2");
        Model.getCoreHelper().setNamespace(pack2, pack1);
        Model.getCoreHelper().setNamespace(theClass, pack2);
        Model.getCoreHelper().setAbstract(theClass, false);
        ModelElementNameNotation notation = 
            new ModelElementNameNotationJava(theClass);
        notation.parse(theClass, "abstract mod1.p1.p2.TheClass");
        assertTrue("Could not parse abstract class with path", 
                Model.getFacade().isAbstract(theClass));
        Object pack3 = Model.getModelManagementFactory().buildPackage("p3");
        Model.getCoreHelper().setNamespace(pack3, pack1);
        notation.parse(theClass, " mod1.p1.p3.TheClass  ");
        assertTrue("Could not move a class into another package",
                Model.getFacade().getOwnedElements(pack3).contains(theClass));
    }

    public void testToStringForRealization() {
        Object theInterface = Model.getCoreFactory().buildInterface(
                "TheInterface", getModel());
        Object realization = Model.getCoreFactory().buildRealization(theClass,
                theInterface, getModel());
        assertToStringForUnnamedRelationshipIsEmpty(realization);
        assertToStringForNamedRelationshipEqualsItsName(realization);
    }

    private void assertToStringForUnnamedRelationshipIsEmpty(
            Object relationship) {
        ModelElementNameNotation notation = new ModelElementNameNotationJava(
                relationship);
        assertEquals("", notation.toString(relationship, 
                NotationSettings.getDefaultSettings()));
    }

    private void assertToStringForNamedRelationshipEqualsItsName(
            Object relationship) {
        ModelElementNameNotation notation = new ModelElementNameNotationJava(
                relationship);
        Model.getCoreHelper().setName(relationship, "relationshipName");
        assertEquals(Model.getFacade().getName(relationship), notation.toString(
                relationship, NotationSettings.getDefaultSettings()));
    }

    public void testToStringForGeneralization() {
        Object theBaseClass = Model.getCoreFactory().buildClass("TheBaseClass",
                getModel());
        Object generalization = Model.getCoreFactory().buildGeneralization(
                theClass, theBaseClass);
        assertToStringForUnnamedRelationshipIsEmpty(generalization);
        assertToStringForNamedRelationshipEqualsItsName(generalization);
    }

    /**
     * Test if help is correctly provided.
     */
    public void testGetHelpOperation() {
        ModelElementNameNotation notation = 
            new ModelElementNameNotationJava(theClass); 
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }
    
    /**
     * Test if the notationProvider refuses to instantiate 
     * without showing it the right UML element.
     */
    public void testValidObjectCheck() {
        try {
            new ModelElementNameNotationJava(new Object());
            fail("The NotationProvider did not throw for a wrong UML element.");
        } catch (IllegalArgumentException e) {
            /* Everything fine... */
        } 
    }
}
