// $Id:TestExtensionMechanismsFactory.java 12576 2007-05-09 14:19:16Z tfmorris $
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

/**
 * Test the ExtensionMechanismsFactory class.
 *
 */
public class TestExtensionMechanismsFactory extends TestCase {
    /**
     * List of model elements to test.
     */
    private static String[] allModelElements = {
	"Stereotype",
        "TagDefinition",
	"TaggedValue",
    };

    /**
     * The constructor.
     *
     * @param n the name of the test
     */
    public TestExtensionMechanismsFactory(String n) {
	super(n);
    }

    /**
     * @return Returns the allModelElements.
     */
    static String[] getAllModelElements() {
        return allModelElements;
    }
    
    /**
     * @return the concrete ModelElements which are testable
     */
    static List<String> getTestableModelElements() {
        List<String> c = new ArrayList<String>(Arrays.asList(allModelElements));
        // TODO: Stereotype was untested before, but I'm not sure if it was
        // intentional.  Seems more like an oversight. - tfm
//        c.remove("Stereotype");
        return c;
    }
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        InitializeModel.initializeDefault();
    }

    /**
     * Test if the ExtensionMechanismsFactory is really a singleton.
     */
    public void testSingleton() {
	Object o1 = Model.getExtensionMechanismsFactory();
	Object o2 = Model.getExtensionMechanismsFactory();
	assertTrue("Different singletons", o1 == o2);
    }

    /**
     * Test creation.
     */
    public void testCreates() {
	CheckUMLModelHelper.createAndRelease(
		     Model.getExtensionMechanismsFactory(),
		     getTestableModelElements());

        ExtensionMechanismsFactory emFactory =
            Model.getExtensionMechanismsFactory();
        Object model = Model.getModelManagementFactory().createModel();
        Object stereo = emFactory.buildStereotype("mystereo", model);
        try {
            emFactory.buildTagDefinition("myTD", stereo, model);
            fail("Illegal buildTagDefinition with both sterotype"
                    + " and model didn't throw exception.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    /**
     * Test cascading delete to make sure dependent
     * elements disappear.
     */
    public void testDelete() {
        ExtensionMechanismsFactory emFactory =
            Model.getExtensionMechanismsFactory();
        ExtensionMechanismsHelper emHelper =
            Model.getExtensionMechanismsHelper();
        Object model = Model.getModelManagementFactory().createModel();
        Model.getModelManagementFactory().setRootModel(model);
        Object stereo = emFactory.buildStereotype("mystereo", model);
        emFactory.buildTagDefinition("myTD", stereo, null);
        Object tv = emFactory.buildTaggedValue("myTD", "the tag value");
        Object clazz = Model.getCoreFactory().buildClass("MyClass", model);
        emHelper.addTaggedValue(clazz, tv);

        Collection tvs = Model.getFacade().getTaggedValuesCollection(clazz);
        assertEquals("Wrong number of TaggedValues returned", 1, tvs.size());
        assertTrue("TaggedValue not found", tvs.contains(tv));
        Collection tds =
            Model.getModelManagementHelper()
                .getAllModelElementsOfKind(model,
                        Model.getMetaTypes().getTagDefinition());
        assertEquals("TagDefinition not found", 1, tds.size());

        // Deleting the stereotype should cascade to the TagDefinition,
        // then the TaggedValue
        Model.getUmlFactory().delete(stereo);
        Model.getPump().flushModelEvents();

        tvs = Model.getFacade().getTaggedValuesCollection(clazz);
        assertEquals("TaggedValue not deleted", 0, tvs.size());
        tds =
            Model.getModelManagementHelper()
                .getAllModelElementsOfKind(model,
                        Model.getMetaTypes().getTagDefinition());
        assertEquals("TagDefinition not deleted", 0, tds.size());
    }

}
