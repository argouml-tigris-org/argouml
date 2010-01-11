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

// Copyright (c) 2002-2007 The Regents of the University of California. All
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

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;



/**
 * Test the UseCasesFactory class.
 *
 */
public class TestUseCasesFactory extends TestCase {
    /**
     * List of elements to test.
     */
    private static String[] allModelElements = {
        "Actor",
        "Extend",
        "ExtensionPoint",
        "Include",
        "UseCase",
//        "UseCaseInstance",  // Gone in UML 2.x & unused by ArgoUML
    };

    /**
     * The constructor.
     *
     * @param n the name of the test
     */
    public TestUseCasesFactory(String n) {
        super(n);
    }

    /**
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }

    /**
     * @return all testable model elements
     */
    static List<String> getTestableModelElements() {
        return Arrays.asList(allModelElements);
    } 
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        InitializeModel.initializeDefault();
    }

    /**
     * Test if the UseCasesFactory is really a singleton.
     */
    public void testSingleton() {
	Object o1 = Model.getUseCasesFactory();
	Object o2 = Model.getUseCasesFactory();
	assertTrue("Different singletons", o1 == o2);
    }

    /**
     * Test creation.
     */
    public void testCreates() {
        CheckUMLModelHelper.createAndRelease(
                Model.getUseCasesFactory(), getTestableModelElements());
    }

    /**
     * Test building extensions.
     */
    public void testBuildExtend1() {
        Object base = Model.getUseCasesFactory().createUseCase();
        Object extension = Model.getUseCasesFactory().createUseCase();
        Object point = Model.getUseCasesFactory()
            	.buildExtensionPoint(base);
        assertTrue("extensionpoint not added to base",
                   Model.getFacade().getExtensionPoints(base).contains(point));
        Object extend = Model.getUseCasesFactory()
            	.buildExtend(base, extension, point);
        assertTrue("extend not added to extension",
                Model.getFacade().getExtends(extension).contains(extend));
        assertTrue("extend not added to correct extensionpoint",
		   (Model.getFacade().getExtensionPoints(extend).contains(point)
		 && Model.getFacade().getExtensionPoints(extend).size() == 1));
        Model.getUmlFactory().delete(base);
        assertTrue("ExtensionPoint not deleted with base UseCase", 
                Model.getUmlFactory().isRemoved(point));
        assertTrue("Extend not deleted with its UseCase", 
                Model.getUmlFactory().isRemoved(extend));
    }
    
    public void testGetAll() {
        Object model = Model.getModelManagementFactory().createModel();
        Object pkg = Model.getModelManagementFactory().createPackage();
        Model.getCoreHelper().addOwnedElement(model, pkg);
        
        Object uc = Model.getUseCasesFactory().createUseCase();
        Model.getCoreHelper().setName(uc, "UseCase1");
        Model.getCoreHelper().addOwnedElement(model, uc);

        uc = Model.getUseCasesFactory().createUseCase();
        Model.getCoreHelper().setName(uc, "UseCase2");
        Model.getCoreHelper().addOwnedElement(pkg, uc);

        assertEquals("Failed to get two UseCases", 2, 
                Model.getUseCasesHelper().getAllUseCases(model).size());

        Object actor = Model.getUseCasesFactory().createActor();
        Model.getCoreHelper().setName(actor, "Actor1");
        Model.getCoreHelper().addOwnedElement(model, actor);

        actor = Model.getUseCasesFactory().createActor();
        Model.getCoreHelper().setName(actor, "Actor2");
        Model.getCoreHelper().addOwnedElement(pkg, actor);


        actor = Model.getUseCasesFactory().createActor();
        Model.getCoreHelper().setName(actor, "Actor3");
        Model.getCoreHelper().addOwnedElement(pkg, actor);
        
        assertEquals("Failed to get three Actors", 3, 
                Model.getUseCasesHelper().getAllActors(model).size());
        
    }


}

