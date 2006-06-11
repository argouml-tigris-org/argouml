// $Id$
// Copyright (c) 2002-2006 The Regents of the University of California. All
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

package org.argouml.ui;

import junit.framework.TestCase;

import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;

/**
 * Testing the ProjectBrowser.
 * The reason for this being a GUITest is that all the tests rely on
 * that the ProjectBrowser can be created and it can only be created
 * if there is a real gui available.
 *
 * @since Nov 23, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class GUITestProjectBrowser extends TestCase {

    /**
     * Constructor.
     *
     * @param arg0 name of the test case
     */
    public GUITestProjectBrowser(String arg0) {
        super(arg0);
    }

    /**
     * Tests that the projectbrowser is created or can be created.
     * Also test that everything is set up properly within the object.
     */
    public void testConstruction() {
	assertNotNull(ProjectBrowser.getInstance());
	ProjectBrowser pb = ProjectBrowser.getInstance();
	assertNotNull(pb.getLocale());
	assertNotNull(pb.getAppName());
	assertNotNull(pb.getTabProps());
	assertNotNull(pb.getStatusBar());
	assertNotNull(pb.getJMenuBar());
	assertNotNull(pb.getEditorPane());
	assertNotNull(pb.getNamedTab(Translator.localize("tab.properties")));
	assertNotNull(pb.getNamedTab(Translator.localize("tab.source")));
	assertNotNull(pb.getTodoPane());
    }

    /**
     * Test the AppName.
     */
    public void testAppName() {
	ProjectBrowser pb = ProjectBrowser.getInstance();

	pb.setAppName("Gurkburk");
	assertEquals("Gurkburk", pb.getAppName());
    }

    /**
     * Test the existance of public static members.
     */
    public void compileTestPublicStaticMembers() {
	new Integer(ProjectBrowser.DEFAULT_COMPONENTWIDTH
	        + ProjectBrowser.DEFAULT_COMPONENTHEIGHT);
    }

    /**
     * Test the existance of public members.
     */
    public void compileTestPublicMembers() {
	assertNotNull(ProjectBrowser.getInstance().getDefaultFont());
    }


    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * Testing the setTarget method in ProjectBrowser.
     * The method has been deprecated.
     */
    public void testSetTarget() {
        Project p = ProjectManager.getManager().getCurrentProject();
	Object package1 =
	    Model.getModelManagementFactory().buildPackage("test1", null);
	Object package2 =
	    Model.getModelManagementFactory().buildPackage("test2", null);
	UMLClassDiagram diagram1 = new UMLClassDiagram(package1);
	UMLClassDiagram diagram2 = new UMLClassDiagram(package2);
        p.addMember(diagram1);
        p.addMember(diagram2);

        TargetManager tm = TargetManager.getInstance();

        tm.setTarget(diagram1);
	assertEquals("Diagram1 should be the target", diagram1, tm.getTarget());

        TargetManager.getInstance().setTarget(diagram2);
	assertEquals("Diagram2 should be the target", diagram2, tm.getTarget());

	p.moveToTrash(package2);
	assertEquals("The target is not reset to the first diagram",
            p.getDiagrams().get(0), tm.getTarget());
    }
}
