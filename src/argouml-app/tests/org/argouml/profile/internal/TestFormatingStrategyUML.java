/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2009 The Regents of the University of California. All
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

package org.argouml.profile.internal;

import static org.argouml.model.Model.getCoreFactory;
import static org.argouml.model.Model.getFacade;
import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;

/**
 * Tests for the FormatingStrategyUML class.
 *
 * @author Luis Sergio Oliveira (euluis)
 * @since 0.29.1
 */
public class TestFormatingStrategyUML extends TestCase {

    private Object model;
    private Object classA;
    private Object classB;
    private Object association;
    private Object classAAssociationEnd;
    private Object classBAssociationEnd;
    private FormatingStrategyUML formatingStrategyUML;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        model = Model.getModelManagementFactory().createModel();
        Model.getCoreHelper().setName(model, "Model");
        classA = getCoreFactory().buildClass("ClassA", model);
        classB = getCoreFactory().buildClass("ClassB", model);
        association = getCoreFactory().buildAssociation(classA, true,
            classB, true, null);
        classAAssociationEnd = getFacade().getAssociationEnd(classA,
            association);
        classBAssociationEnd = getFacade().getAssociationEnd(classB,
            association);
        formatingStrategyUML = new FormatingStrategyUML();
    }

    /**
     * Test formatting of an unnamed association.
     *
     * @see FormatingStrategyUML#defaultAssocName(Object, Object)
     */
    public void testFormatUnnamedAssociation() {
        assertEquals("A_classA_classB",
            formatingStrategyUML.formatElement(association, model));
    }

    /**
     * Various tests for
     * {@link FormatingStrategyUML#ensureFirstCharLowerCase(String)}.
     */
    public void testEnsureFirstCharLowercase() {
        assertEquals("", formatingStrategyUML.ensureFirstCharLowerCase(""));
        assertEquals("a", formatingStrategyUML.ensureFirstCharLowerCase("a"));
        assertEquals("a", formatingStrategyUML.ensureFirstCharLowerCase("A"));
        assertEquals("aA", formatingStrategyUML.ensureFirstCharLowerCase("AA"));
        assertEquals("3a", formatingStrategyUML.ensureFirstCharLowerCase("3a"));
        assertEquals("theClass",
            formatingStrategyUML.ensureFirstCharLowerCase("TheClass"));
    }

    /**
     * Test formatting of an unnamed generalization.
     *
     * @see FormatingStrategyUML#defaultGeneralizationName(Object, Object)
     */
    public void testFormatUnnamedGeneralization() {
        Object generalization = getCoreFactory().buildGeneralization(classA,
            classB);
        assertEquals("ClassA specializes ClassB",
            formatingStrategyUML.formatElement(generalization, model));
    }

}
