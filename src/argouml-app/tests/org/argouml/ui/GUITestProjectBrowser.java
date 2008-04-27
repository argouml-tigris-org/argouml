// $Id$
// Copyright (c) 2002-2008 The Regents of the University of California. All
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

import java.beans.PropertyVetoException;

import javax.swing.Action;

import junit.framework.TestCase;

import org.argouml.cognitive.ui.ToDoPane;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.cmd.ActionNew;
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

    private static final String GURKBURK = "Gurkburk";

    /**
     * Constructor.
     *
     * @param arg0 name of the test case
     */
    public GUITestProjectBrowser(String arg0) {
        super(arg0);
    }

    @Override
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        SplashScreen splashScreen = new SplashScreen();
        assertNotNull(ProjectBrowser.makeInstance(splashScreen, 
                true, new ToDoPane(splashScreen)));
    }

    /**
     * Tests that the projectbrowser is created or can be created.
     * Also test that everything is set up properly within the object.
     */
    public void testConstruction() {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	assertNotNull(pb.getLocale());
	assertNotNull(pb.getAppName());
	// ProjectBrowser exposes functionality, not components.
//	assertNotNull(pb.getTabProps());
//	assertNotNull(pb.getStatusBar());
	assertNotNull(pb.getJMenuBar());
	assertNotNull(pb.getEditorPane());
	assertNotNull(pb.getTodoPane());
    }

    /**
     * Test the AppName.
     */
    public void testAppName() {
	ProjectBrowser pb = ProjectBrowser.getInstance();

	pb.setAppName(GURKBURK);
	assertEquals(GURKBURK, pb.getAppName());
    }

    /**
     * Test the existence of public static members.
     */
    public void compileTestPublicStaticMembers() {
	Integer.valueOf(ProjectBrowser.DEFAULT_COMPONENTWIDTH
	        + ProjectBrowser.DEFAULT_COMPONENTHEIGHT);
    }

    /**
     * Test the existence of public members.
     */
    public void compileTestPublicMembers() {
	assertNotNull(ProjectBrowser.getInstance().getDefaultFont());
    }


    /**
     * Testing the setTarget method in ProjectBrowser.
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

        tm.setTarget(diagram2);
	assertEquals("Diagram2 should be the target", diagram2, tm.getTarget());

	p.moveToTrash(package2);
        Model.getPump().flushModelEvents();
	assertEquals("The target is not reset to the first diagram",
            p.getDiagramList().get(0), tm.getTarget());
    }

    /**
     * Testing the Window Title of the ProjectBrowser.
     */
    public void testSetTitle() {
        Project p = ProjectManager.getManager().getCurrentProject();
        ProjectBrowser pb = ProjectBrowser.getInstance();
        TargetManager tm = TargetManager.getInstance();

        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1", null);
        Object package2 =
            Model.getModelManagementFactory().buildPackage("test2", null);
        UMLClassDiagram diagram1 = new UMLClassDiagram(package1);
        UMLClassDiagram diagram2 = new UMLClassDiagram(package2);
        try {
            diagram1.setName("diagram1");
            diagram2.setName("diagram2");
        } catch (PropertyVetoException e) {
            assertNotNull("PropertyVetoException " + e, e);
        }

        p.addMember(diagram1);
        p.addMember(diagram2);

        Model.getPump().flushModelEvents();

        pb.setAppName(GURKBURK);

        tm.setTarget(diagram1);
        assertTrue("Title should contain diagram1 name", 
                pb.getTitle().indexOf(diagram1.getName()) != -1);

        tm.setTarget(diagram2);
        assertTrue("Title should contain diagram2 name", 
                pb.getTitle().indexOf(diagram2.getName()) != -1);
        
        assertTrue("Title should contain application name", 
                pb.getTitle().indexOf(GURKBURK) != -1);

        assertTrue("Title should contain *", 
                pb.getTitle().indexOf("*") != -1);

        Action a = new ActionNew();
        a.putValue("non-interactive", Boolean.TRUE);
        a.actionPerformed(null);
        
        assertTrue("Title should contain *", 
                pb.getTitle().indexOf("*") != -1);
    }
}
