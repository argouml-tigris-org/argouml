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

package org.argouml.kernel;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;


/**
 * @since Nov 17, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestProject extends TestCase {

    /**
     * Constructor for TestProject.
     *
     * @param arg0 is the name of the test case.
     */
    public TestProject(String arg0) {
        super(arg0);
    }

    /**
     * Test the makeUntitledProject() function.
     */
    public void testMakeUntitledProject() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        assertEquals("untitledModel", Model.getFacade().getName(p.getModel()));
        // maybe next test is going to change in future
        assertEquals(p.getRoot(), p.getModel());
    }

    /**
     * Test the moveToTrash function for package and content.
     */
    public void testTrashcanPackageContent() {
        Project p = ProjectManager.getManager().getCurrentProject();
        // test with a class in a package
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1", null);
        Model.getCoreHelper().setNamespace(package1, p.getRoot());
        Object cls1 = Model.getCoreFactory().buildClass(package1);
        Object cls2 = Model.getCoreFactory().buildClass(package1);
        Object cls3 = Model.getCoreFactory().buildClass(package1);
        Object cls4 = Model.getCoreFactory().buildClass(p.getRoot());
        Collection c1 = Model.getFacade().getOwnedElements(p.getRoot());
        // Let's make it a bit more difficult by setting the target:
        TargetManager.getInstance().setTarget(cls2);
        p.moveToTrash(package1);
        Collection c = Model.getFacade().getOwnedElements(p.getRoot());
        assertTrue("Package not in trash", p.isInTrash(package1));
        assertTrue("Package not deleted", !c.contains(package1));
        assertTrue("Class 1 not deleted", !c.contains(cls1));
        assertTrue("Class 2 not deleted", !c.contains(cls2));
        assertTrue("Class 3 not deleted", !c.contains(cls3));
        assertTrue("Class 4 has been deleted", c.contains(cls4));
    }

    /**
     * Test the moveToTrash function for class and content.
     */
    public void testTrashcanClassContent() {
        Project p = ProjectManager.getManager().getCurrentProject();
        // test with a class and an inner class
        Object aClass = Model.getCoreFactory().buildClass("Test", p.getRoot());
        Object cls1 = Model.getCoreFactory().buildClass(aClass);
        Object cls2 = Model.getCoreFactory().buildClass(aClass);
        Object cls3 = Model.getCoreFactory().buildClass(aClass);
        Object typ = Model.getCoreFactory().buildClass(p.getRoot());
        Object oper2a =
            Model.getCoreFactory().buildOperation(
                    cls2, p.getRoot(), cls3, new ArrayList());
        Object oper2b =
            Model.getCoreFactory().buildOperation(
                    cls2, p.getRoot(), typ, new ArrayList());
        p.moveToTrash(aClass);
        Collection c = Model.getFacade().getOwnedElements(p.getRoot());
        assertTrue("Package not in trash", p.isInTrash(aClass));
        assertTrue("Package not deleted", !c.contains(aClass));
        assertTrue("Class 1 not deleted", !c.contains(cls1));
        assertTrue("Class 2 not deleted", !c.contains(cls2));
        assertTrue("Class 3 not deleted", !c.contains(cls3));
    }


    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ProjectManager.getManager().setCurrentProject(null);
    }
}
