// $Id$
// Copyright (c) 2002-2003 The Regents of the University of California. All
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

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;

import ru.novosoft.uml.MFactoryImpl;

import junit.framework.TestCase;

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
     * Constructor
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
	assertNotNull(pb.getNamedTab("tab.properties"));
	assertNotNull(pb.getNamedTab("tab.source"));
	assertNotNull(pb.getTodoPane());
	assertNotNull(pb.getNavigatorPane());
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
     * Tests the existance of public methods and public static methods for
     * accessing the splashscreen.
     *
     * Since ProjectBrowser is a singleton without possibilities to remove it
     * and the existance of the SplashScreen is determined when initially 
     * creating it, it is not possible to test it.
     */
    public void compileTestSplashScreen() {
	ProjectBrowser.setSplash(true);
	ProjectBrowser inst = ProjectBrowser.getInstance();
	inst.setSplashScreen(inst.getSplashScreen());
    }

    /** Test the existance of public static members.
     */
    public void compileTestPublicStaticMembers() {
	int r = ProjectBrowser.DEFAULT_COMPONENTWIDTH 
	        + ProjectBrowser.DEFAULT_COMPONENTHEIGHT;
    }

    /**
     * Test the existance of public members.
     */
    public void compileTestPublicMembers() {
	assertNotNull(ProjectBrowser.getInstance().defaultFont);
    }
    

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ArgoSecurityManager.getInstance().setAllowExit(true);
    }
    
    /**
     * Testing the setTarget method in ProjectBrowser.
     * The method has been deprecated.
     */
    public void testSetTarget() {
	MFactoryImpl.setEventPolicy(MFactoryImpl.EVENT_POLICY_IMMEDIATE);
	Object package1 = 
	    ModelManagementFactory.getFactory().buildPackage("test1", null);
	Object package2 = 
	    ModelManagementFactory.getFactory().buildPackage("test2", null);
	UMLClassDiagram diagram1 = new UMLClassDiagram(package1);
	UMLClassDiagram diagram2 = new UMLClassDiagram(package2);

        ProjectBrowser pb = ProjectBrowser.getInstance();

	pb.setTarget(diagram1);
	assertEquals(diagram1, pb.getTarget());

	pb.setTarget(diagram2);
	assertEquals(diagram2, pb.getTarget());

	UmlFactory.getFactory().delete(package2);
	ProjectManager pm = ProjectManager.getManager();
	assertEquals(pm.getCurrentProject().getDiagrams().get(0), 
		     pb.getTarget());
    }

    /**
     * Test the existance of deprecated methods.
     */
    public void compileExistDeprecated()
    {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	pb.getTarget();
	pb.getActiveDiagram();
	pb.getDetailsTarget();
    }
}
