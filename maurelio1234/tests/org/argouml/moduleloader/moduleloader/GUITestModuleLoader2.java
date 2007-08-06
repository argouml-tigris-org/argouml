// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.moduleloader;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

/**
 * Tests for the new module loader.
 */
public class GUITestModuleLoader2 extends TestCase {

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
    }

    public void testGetInstance() {
	assertNotNull(ModuleLoader2.getInstance());
    }

    public void testDoLoad() {
	ModuleLoader2.doLoad(false);
	ModuleLoader2.doLoad(true);
    }

    public void testIsEnabled() {
	assertFalse(ModuleLoader2.isEnabled("some-nonexisting-module"));
    }

    public void testAllModules() {
	Collection<String> all = ModuleLoader2.allModules();
	assertNotNull(all);

	Iterator<String> iter = all.iterator();
	while (iter.hasNext()) {
	    assertNotNull(iter.next());
	}
    }

    public void testIsSelected() {
	assertFalse(ModuleLoader2.isSelected("some-module-that-do-not-exist"));
    }

    public void testSetSelected() {
	ModuleLoader2.setSelected("nonexisting-module", true);
	ModuleLoader2.setSelected("nonexisting-module", false);
    }

    public void testGetDescription() {
	try {
	    ModuleLoader2.getDescription("nonexisting-module");
	    fail("No exception was thrown.");
	} catch (IllegalArgumentException e) {
	    // Fine!
	}
    }

    public void testGetExtensionLocations() {
	List<String> extensionLocations =
	    ModuleLoader2.getInstance().getExtensionLocations();
	assertNotNull(extensionLocations);
	for (Iterator<String> iter = extensionLocations.iterator();
		iter.hasNext();) {
	    assertNotNull(iter.next());
	}
    }

    public void testAddClassNegative() {
	try {
	    ModuleLoader2.addClass("a.class.that.do.not.exist");
	    fail("No exception was thrown!");
	} catch (ClassNotFoundException e) {
	    // Fine!
	}

	try {
	    ModuleLoader2.addClass(this.getClass().getName());
	} catch (ClassNotFoundException e) {
	    fail("Exception thrown unexpectedly.");
	}
    }


    private static Object created;
    public static void interfaceCreatedForTesting(Object testing1) {
	created = testing1;	
    }

    public void testAddClass() {
	created = null;
	
	try {
	    ModuleLoader2.addClass(ModuleInterfaceForTesting1.class.getName());
	} catch (ClassNotFoundException e) {
	    fail("Exception thrown unexpectedly.");
	}
	
	assertNotNull(created);

	ModuleInterfaceForTesting1.setReadyToBeEnabled(false);

	ModuleLoader2.setSelected(
		ModuleInterfaceForTesting1.TEST_MODULE_NAME, false);

	assertNotNull(
		ModuleLoader2.getDescription(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));
	assertFalse(
		ModuleLoader2.isEnabled(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));
	assertFalse(
		ModuleLoader2.isSelected(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));

	ModuleLoader2.doLoad(false);

	assertFalse(
		ModuleLoader2.isEnabled(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));
	assertFalse(
		ModuleLoader2.isSelected(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));

	ModuleLoader2.setSelected(
		ModuleInterfaceForTesting1.TEST_MODULE_NAME, true);

	assertFalse(
		ModuleLoader2.isEnabled(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));
	assertTrue(
		ModuleLoader2.isSelected(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));

	ModuleLoader2.setSelected(
		ModuleInterfaceForTesting1.TEST_MODULE_NAME, false);

	assertFalse(
		ModuleLoader2.isEnabled(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));
	assertFalse(
		ModuleLoader2.isSelected(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));

	ModuleLoader2.setSelected(
		ModuleInterfaceForTesting1.TEST_MODULE_NAME, true);

	assertFalse(
		ModuleLoader2.isEnabled(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));
	assertTrue(
		ModuleLoader2.isSelected(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));

	ModuleLoader2.doLoad(false);

	assertFalse(
		ModuleLoader2.isEnabled(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));
	assertTrue(
		ModuleLoader2.isSelected(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));

	ModuleInterfaceForTesting1.setReadyToBeEnabled(true);

	ModuleLoader2.doLoad(false);

	assertTrue(
		ModuleLoader2.isEnabled(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));
	assertTrue(
		ModuleLoader2.isSelected(
			ModuleInterfaceForTesting1.TEST_MODULE_NAME));
    }
}
