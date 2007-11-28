// $Id$
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

package org.argouml.uml.ui;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.ui.foundation.extension_mechanisms.UMLTagDefinitionComboBoxModel;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;
import org.argouml.profile.InitProfileSubsystem;

/**
 * Test cases for the UMLTagDefinitionComboBoxModel class.
 * 
 * @author euluis
 * @since 0.20
 */
public class TestUMLTagDefinitionComboBoxModel extends TestCase {

    private Object model;

    private Object theClass;

    private Object theStereotype;
    
    private Project proj;

    /**
     * Default constructor.
     */
    public TestUMLTagDefinitionComboBoxModel() {
        super("TestUMLTagDefinitionComboBoxModel");
    }
    /**
     * Constructor.
     * @param arg0 test name
     */
    public TestUMLTagDefinitionComboBoxModel(String arg0) {
        super(arg0);
    }
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        proj = ProjectManager.getManager().getCurrentProject();
        model = proj.getModel();

        theClass = Model.getCoreFactory().buildClass("TheClass", model);
        theStereotype = Model.getExtensionMechanismsFactory().buildStereotype(
                theClass, "containedStereotype", model);
    }

    /**
     * Test if a tag definition owned by a stereotype is available to be 
     * applied in a class to which the stereotype is applied.
     */
    public void testTagDefinitionFromStereotypeApplicableToStereotypedClass() {
        Object theTagDefinition = Model.getExtensionMechanismsFactory().
            buildTagDefinition("TagDefinition", theStereotype, null);
        // Testing with more than one, since it failed with a manual test I 
        // made.
        Object theTagDefinition2 = Model.getExtensionMechanismsFactory().
            buildTagDefinition("TagDefinition2", theStereotype, null);
        Model.getCoreHelper().addStereotype(theClass, theStereotype);
        UMLTagDefinitionComboBoxModel tagDefComboBoxModel = 
            new UMLTagDefinitionComboBoxModel();
        Object[] added = {theClass};
        tagDefComboBoxModel.targetAdded(new TargetEvent(this, "TARGET_ADDED", 
            new Object[0], added));
        assertTrue("The TagDefinition should be contained!", 
            tagDefComboBoxModel.contains(theTagDefinition));
        assertTrue("The TagDefinition2 should be contained!", 
            tagDefComboBoxModel.contains(theTagDefinition2));
    }

}
