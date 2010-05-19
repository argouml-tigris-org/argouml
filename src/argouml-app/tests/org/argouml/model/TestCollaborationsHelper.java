/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2010 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Bob Tarling
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

// $header$
package org.argouml.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import junit.framework.TestCase;

/**
 * @since Oct 10, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestCollaborationsHelper extends TestCase {

    /**
     * Constructor for TestCollaborationsHelper.
     *
     * @param arg0 is the name of the test case.
     */
    public TestCollaborationsHelper(String arg0) {
	super(arg0);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
//        new InitProfileSubsystem().init();   
    }

    /**
     * Test for the metamodel name.
     */
    public void testGetMetaModelName() {
	CheckUMLModelHelper.metaModelNameCorrect(
			 Model.getCollaborationsFactory(),
			 TestCollaborationsFactory.getAllModelElements());
    }

    /**
     * Test if the stereotype is valid.
     */
    public void testIsValidStereoType() {
	CheckUMLModelHelper.isValidStereoType(
		      Model.getCollaborationsFactory(),
		      TestCollaborationsFactory.getAllModelElements());
    }
    
    /**
     * Test allAvailableFeatures and allAvailableContents including
     * inheritance from generalizations.
     */
    public void testAllAvailable() {
        Object model = Model.getModelManagementFactory().createModel();
        Object collab = 
            Model.getCollaborationsFactory().buildCollaboration(model);
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object intType = Model.getCoreFactory().buildDataType("int", model);
        Object attr = Model.getCoreFactory().buildAttribute2(class2, intType);
        Object class3 = Model.getCoreFactory().buildClass(model);
        Object op = Model.getCoreFactory().buildOperation(class3, intType);
        Object class4 = Model.getCoreFactory().buildClass(class3);
        
        Object role1 =
            Model.getCollaborationsFactory().buildClassifierRole(collab);
        Model.getCollaborationsHelper().addBase(role1, class1);
        Object role2 =
            Model.getCollaborationsFactory().buildClassifierRole(collab);
        Model.getCollaborationsHelper().addBase(role2, class2);
        Object role3 =
            Model.getCollaborationsFactory().buildClassifierRole(collab);
        Model.getCollaborationsHelper().addBase(role3, class3);
        Object gen = Model.getCoreFactory().buildGeneralization(role3, role2);
        
        assertEquals("Didn't find all classifier roles", 3, 
                Model.getCollaborationsHelper()
                .getAllClassifierRoles(collab).size());
   
        Collection features = 
            Model.getCollaborationsHelper().allAvailableFeatures(role1);
        assertEquals("Wrong number of features", 0, features.size());
        features = 
            Model.getCollaborationsHelper().allAvailableFeatures(role2);
        assertEquals("Wrong number of features", 1, features.size());
        features = 
            Model.getCollaborationsHelper().allAvailableFeatures(role3);
        assertEquals("Wrong number of features", 2, features.size());
        assertTrue("Didn't find expected attribute", features.contains(attr));
        assertTrue("Didn't find expected operation", features.contains(op));
        assertFalse("Found unexpected nested class in features", 
                features.contains(class4));
        
        Collection contents = 
            Model.getCollaborationsHelper().allAvailableContents(role3);
        assertEquals("Wrong number of elements in contents", 1, 
                contents.size());
        assertTrue("Didn't find expected nested class", 
                contents.contains(class4));

        Model.getUmlFactory().delete(model);
    }

    /**
     * Test various forms of addBase/setBase for ClassifierRole.
     */
    public void testAddBases() {
        Object model = Model.getModelManagementFactory().createModel();
        Object collab = 
            Model.getCollaborationsFactory().buildCollaboration(model);
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);

        Object assoc = Model.getCoreFactory().buildAssociation(class1, class2);

        Object role1 =
            Model.getCollaborationsFactory().buildClassifierRole(collab);
        Object role2 =
            Model.getCollaborationsFactory().buildClassifierRole(collab);
        Model.getCollaborationsHelper().addBase(role1, class1);
        Model.getCoreHelper().setName(role1, "role1");
        Model.getCoreHelper().setName(role2, "role2");
        // This should work now that they both have names
        Model.getCollaborationsHelper().addBase(role2, class1);

        Model.getCollaborationsHelper().setBases(role2, Collections.emptySet());
        assertEquals("Wrong number of bases", 0, Model.getFacade().getBases(
                role2).size());
        Collection bases = new ArrayList();
        bases.add(class1);
        bases.add(class2);
        Model.getCollaborationsHelper().setBases(role2, bases);
        Collection fetchedBases = Model.getFacade().getBases(role2);
        assertEquals("Wrong number of bases", 2, fetchedBases.size());
        assertTrue("Didn't find expected base", fetchedBases.contains(class1));
        assertTrue("Didn't find expected base", fetchedBases.contains(class2));
        Model.getCollaborationsHelper().removeBase(role2, class1);
        fetchedBases = Model.getFacade().getBases(role2);
        assertEquals("Wrong number of bases", 1, fetchedBases.size());
        assertFalse("Base wasn't removed", fetchedBases.contains(class1));
        Model.getUmlFactory().delete(model);
    }
    
    /**
     * Test AssociationRoles.
     */
    public void testAssociationRole() {

        Object model = Model.getModelManagementFactory().createModel();
        Object collab = 
            Model.getCollaborationsFactory().buildCollaboration(model);
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object assoc1 = Model.getCoreFactory().buildAssociation(class1, class2);
        Object assoc2 = Model.getCoreFactory().buildAssociation(class1, class2);


        Object role1 =
            Model.getCollaborationsFactory().buildClassifierRole(collab);
        assertNotNull(role1);
        assertEquals("found wrong number of possible bases", 2, Model
                .getCollaborationsHelper().getAllPossibleBases(role1).size());
        Model.getCollaborationsHelper().addBase(role1, class1);

        Object role2 =
            Model.getCollaborationsFactory().buildClassifierRole(collab);
        Model.getCollaborationsHelper().addBase(role2, class2);
      
        assertEquals("Found wrong number of possible association roles", 2,
                Model.getCollaborationsHelper().getAllPossibleAssociationRoles(
                        role1).size());
        Object assocRole =
            Model.getCollaborationsFactory().buildAssociationRole(role1, role2);
        assertNotNull(assocRole);
        
        assertEquals("getAssocationRole returned wrong result", assocRole,
                Model.getCollaborationsHelper()
                        .getAssociationRole(role1, role2));

        assertEquals("Didn't find connected classifier role", 1, Model
                .getCollaborationsHelper().getClassifierRoles(role1).size());

        
        Model.getUmlFactory().delete(model);
    }
    
    /**
     * Test methods for Interactions.
     */
    public void testInteractions() {
        Object model = Model.getModelManagementFactory().createModel();
        Object collab = 
            Model.getCollaborationsFactory().buildCollaboration(model);
        Object interaction = Model.getCollaborationsFactory()
                .createInteraction();
        Model.getCollaborationsHelper().setContext(interaction, collab);

        Model.getCollaborationsFactory().buildInteraction(collab);
        assertEquals(2, Model.getFacade().getInteractions(collab).size());
        assertTrue(Model.getFacade().getInteractions(collab).contains(
                interaction));
        
        Object msg1 = Model.getCollaborationsFactory().createMessage();
        assertNotNull(msg1);
        Model.getCollaborationsHelper().addMessage(interaction, msg1);
        assertTrue(Model.getFacade().getMessages(interaction).contains(msg1));
        
        Model.getCollaborationsHelper().removeInteraction(collab,
                interaction);
        assertEquals(1, Model.getFacade().getInteractions(collab).size());

    }
}
