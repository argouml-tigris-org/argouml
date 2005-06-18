// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        model = Model.getModelManagementFactory().createModel();
        pack = Model.getModelManagementFactory().buildPackage("pack",
                UUIDManager.getInstance().getNewUUID());
        Model.getCoreHelper().setNamespace(pack, model);

        theClass = Model.getCoreFactory().buildClass("TheClass", pack);
        theStereotype = Model.getExtensionMechanismsFactory().buildStereotype(
                theClass, "containedStereotype", pack);
        models = new ArrayList();
        models.add(model);
    }

    /**
     * This method tests the working of getAllPossibleStereotypes when the
     * stereotype is contained within the same package as the model element.
     */
    public void testGetAllPossibleStereotypesStereotypeAndMEContainedInSubPackage() {
        Collection stereotypes = Model.getExtensionMechanismsHelper()
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
        Object subpack = Model.getModelManagementFactory().buildPackage(
                "subpack", UUIDManager.getInstance().getNewUUID());
        Model.getCoreHelper().setNamespace(subpack, pack);
        theClass = Model.getCoreFactory().buildClass("TheClassInSubpack",
                subpack);
        Collection stereotypes = Model.getExtensionMechanismsHelper()
                .getAllPossibleStereotypes(models, theClass);
        assertTrue("The \"" + theStereotype
                + "\" isn't returned, but is possible.", stereotypes
                .contains(theStereotype));
    }
}