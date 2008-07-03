// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import java.io.File;
import java.net.URL;
import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.i18n.Translator;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.notation.providers.uml.InitNotationUml;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.OpenException;
import org.argouml.persistence.PersistenceManager;
import org.argouml.profile.init.InitProfileSubsystem;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.activity.ui.InitActivityDiagram;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.collaboration.ui.InitCollaborationDiagram;
import org.argouml.uml.diagram.deployment.ui.InitDeploymentDiagram;
import org.argouml.uml.diagram.sequence.ui.InitSequenceDiagram;
import org.argouml.uml.diagram.state.ui.InitStateDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.InitClassDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.ui.InitDiagramAppearanceUI;
import org.argouml.uml.diagram.use_case.ui.InitUseCaseDiagram;


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
        assertEquals(2, p.getDiagramList().size());
        assertEquals(Translator.localize("misc.untitled-model"), 
                Model.getFacade().getName(p.getModel()));
        // maybe next test is going to change in future
        assertEquals(p.getRoot(), p.getModel());
    }

    /**
     * Test remove() function. This is called when a new project is created to
     * remove the old project. We confirm here that the users model has been
     * emptied and that no none 'Model' model elements are at root.
     * 
     * @throws InterruptedException if there the project load was interrupted
     * @throws OpenException if there was an error during project load
     */
    public void testRemove() throws OpenException, InterruptedException {
        String name = "/testmodels/uml14/Alittlebitofeverything.zargo";
        URL url = TestProject.class.getResource(name);
        AbstractFilePersister persister =
            PersistenceManager.getInstance().getPersisterFromFileName(name);
        String filename = url.getFile();
        Project p = persister.doLoad(new File(filename));
        
        p.remove();
        
        for (Object root : Model.getFacade().getRootElements()) {
            assertTrue(
                    "All roots should be a Model",
                    Model.getFacade().isAModel(root));
            System.out.println(Model.getFacade().getName(root) + " "
                    + Model.getFacade().getOwnedElements(root).size());
            if ("untitledModel".equals(Model.getFacade().getName(root))) {
                assertEquals(
                        "All root models should be empty", 0, 
                        Model.getFacade().getOwnedElements(root).size());
            }
        }
    }

    /**
     * Test the moveToTrash function for package and content.
     */
    public void testTrashcanPackageContent() {
        Project p = ProjectManager.getManager().getCurrentProject();
        // test with a class in a package
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1");
        Model.getCoreHelper().setNamespace(package1, p.getRoot());
        Object cls1 = Model.getCoreFactory().buildClass(package1);
        Object cls2 = Model.getCoreFactory().buildClass(package1);
        Object cls3 = Model.getCoreFactory().buildClass(package1);
        Object cls4 = Model.getCoreFactory().buildClass(p.getRoot());
        Object c1 = Model.getFacade().getOwnedElements(p.getRoot());
        assertTrue(c1 instanceof Collection);
        // Let's make it a bit more difficult by setting the target:
        TargetManager.getInstance().setTarget(cls2);
        
        p.moveToTrash(package1);
        Model.getPump().flushModelEvents();
        
        //TODO: We should also test that the object
        //have been removed from their namespace.
        //Collection c = Model.getFacade().getOwnedElements(p.getRoot());
        assertTrue("Package not in trash", p.isInTrash(package1));
        assertTrue("Package not deleted",
                Model.getUmlFactory().isRemoved(package1));
        assertTrue("Class 1 not deleted",
                Model.getUmlFactory().isRemoved(cls1));
        assertTrue("Class 2 not deleted",
                Model.getUmlFactory().isRemoved(cls2));
        assertTrue("Class 3 not deleted",
                Model.getUmlFactory().isRemoved(cls3));
        assertTrue("Class 4 has been deleted",
                !Model.getUmlFactory().isRemoved(cls4));
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
        Object oper2a = Model.getCoreFactory().buildOperation(cls2, cls3);
        assertNotNull(oper2a);
        Object oper2b = Model.getCoreFactory().buildOperation(cls2, typ);
        assertNotNull(oper2b);
        
        p.moveToTrash(aClass);
        Model.getPump().flushModelEvents();
        
        assertTrue("Package not in trash", p.isInTrash(aClass));
        assertTrue("Package not deleted",
                Model.getUmlFactory().isRemoved(aClass));
        assertTrue("Class 1 not deleted",
                Model.getUmlFactory().isRemoved(cls1));
        assertTrue("Class 2 not deleted",
                Model.getUmlFactory().isRemoved(cls2));
        assertTrue("Class 3 not deleted",
                Model.getUmlFactory().isRemoved(cls3));
    }



    /**
     * Test deleting a package that contains a Class diagram.
     * The diagram should be deleted, too.
     */
    public void testDeletePackageWithClassDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagramList().size());
        assertEquals(Translator.localize("misc.untitled-model"), 
                Model.getFacade().getName(p.getModel()));
        assertEquals(p.getRoot(), p.getModel());

        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagramList().size();

        // test with a class and class diagram
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1");
        assertNotNull(package1);
        Object package2 =
            Model.getModelManagementFactory().buildPackage("test2");

        UMLClassDiagram cDiag = new UMLClassDiagram(package2);
        p.addMember(cDiag);
        assertEquals(sizeDiagrams + 1, p.getDiagramList().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        p.moveToTrash(package2);
        Model.getPump().flushModelEvents();
        
        assertEquals(sizeDiagrams, p.getDiagramList().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }

    /**
     * Test deleting a class that contains a Statechart diagram.
     * The diagram should be deleted, too.
     */
    public void testDeleteClassWithStateDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagramList().size());

        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagramList().size();

        // test with a class and class diagram
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1");
        Object aClass = Model.getCoreFactory().buildClass(package1);


        // try with Statediagram
        Object machine =
            Model.getStateMachinesFactory().buildStateMachine(aClass);
        UMLStateDiagram d =
            new UMLStateDiagram(
                    Model.getFacade().getNamespace(machine),
                    machine);
        p.addMember(d);
        assertEquals("Diagram count incorrect", sizeDiagrams + 1, 
                p.getDiagramList().size());
        assertEquals("Member count incorrect", sizeMembers + 1, 
                p.getMembers().size());
        p.moveToTrash(aClass);
        Model.getPump().flushModelEvents();
        
        assertTrue("Statemachine not in trash", p.isInTrash(machine));
        assertTrue("Class not in trash", p.isInTrash(aClass));
        assertEquals("Diagram count incorrect after trash", sizeDiagrams, 
                p.getDiagramList().size());
        assertEquals("Member count incorrect after trash", sizeMembers, 
                p.getMembers().size());
    }

    /**
     * Test deleting a statechart diagram directly.
     */
    public void testDeleteStateDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagramList().size());

        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagramList().size();

        // test with a class and class diagram
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1");
        Object aClass = Model.getCoreFactory().buildClass(package1);


        // try with Statediagram
        Object machine =
            Model.getStateMachinesFactory().buildStateMachine(aClass);
        UMLStateDiagram d =
            new UMLStateDiagram(
                    Model.getFacade().getNamespace(machine),
                    machine);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagramList().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        p.moveToTrash(d);
        Model.getPump().flushModelEvents();
        
        assertTrue("Statediagram not in trash", p.isInTrash(d));
        assertEquals(sizeDiagrams, p.getDiagramList().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }

    /**
     * Test deleting a class that contains a Class.
     * The inner class should be deleted, too.
     */
    public void testDeleteClassWithInnerClass() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagramList().size());

        // test with a class and an inner class
        Object aClass = Model.getCoreFactory().buildClass("Test");
        Object bClass = Model.getCoreFactory().buildClass(aClass);

        p.moveToTrash(aClass);
        Model.getPump().flushModelEvents();
        
        assertTrue("Class not in trash", p.isInTrash(aClass));
        assertTrue("Inner Class not in trash",
                Model.getUmlFactory().isRemoved(bClass));
    }

    /**
     * Test deleting a package that contains a Class.
     * The class should be deleted, too.
     */
    public void testDeletePackageWithClass() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagramList().size());

        // test with a class and class diagram
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1");
        Object aClass = Model.getCoreFactory().buildClass(package1);

        p.moveToTrash(package1);
        Model.getPump().flushModelEvents();
        
        assertTrue("Class not in trash",
                Model.getUmlFactory().isRemoved(aClass));
    }


    /**
     * Test deleting a package that contains a Class with Statechart diagram.
     * The diagram should be deleted, too.
     */
    public void testDeletePackageWithStateDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagramList().size());

        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagramList().size();

        // test with a class and class diagram
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1");
        Object aClass = Model.getCoreFactory().buildClass(package1);


        // try with Statediagram
        Object machine =
            Model.getStateMachinesFactory().buildStateMachine(aClass);
        UMLStateDiagram d =
            new UMLStateDiagram(
                    Model.getFacade().getNamespace(machine),
                    machine);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagramList().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        
        p.moveToTrash(package1);
        Model.getPump().flushModelEvents();
        
        assertTrue("Class not in trash",
                Model.getUmlFactory().isRemoved(aClass));
        assertTrue("Statemachine not in trash",
                Model.getUmlFactory().isRemoved(machine));
        assertEquals(sizeDiagrams, p.getDiagramList().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }

    /**
     * Test deleting an operation that contains a statechart diagram.
     * The diagram should be deleted, too.
     */
    public void testDeleteOperationWithStateDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagramList().size());

        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagramList().size();

        // test with a class and class diagram
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1");
        Object aClass = Model.getCoreFactory().buildClass(package1);

        Object voidType = p.getDefaultReturnType();
        Object oper =
            Model.getCoreFactory().buildOperation(
                    aClass,
                    voidType);

        // try with Statediagram
        Object machine =
            Model.getStateMachinesFactory().buildStateMachine(oper);
        UMLStateDiagram d =
            new UMLStateDiagram(
                    Model.getFacade().getNamespace(machine),
                    machine);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagramList().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());

        p.moveToTrash(oper);
        Model.getPump().flushModelEvents();

        assertTrue("Operation not in trash", p.isInTrash(oper));
        /* Changed by issue 4281: */
        assertTrue("Statemachine in trash",
                !Model.getUmlFactory().isRemoved(machine));
        assertEquals(sizeDiagrams + 1, p.getDiagramList().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        /* After issue 4284 will be solved, we
         * may even delete the class, and the diagram
         * should still exist. */
    }


    /**
     * Test deleting a package with a Class with a Activity diagram.
     * The diagram should be deleted, too.
     */
    public void testDeletePackageWithClassWithActivityDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagramList().size());

        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagramList().size();

        // test with a package and a class and activity diagram
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1");
        Object aClass = Model.getCoreFactory().buildClass(package1);

        // build the Activity Diagram
        Object actgrph =
            Model.getActivityGraphsFactory().buildActivityGraph(aClass);
        UMLActivityDiagram d =
            new UMLActivityDiagram(
                    Model.getFacade().getNamespace(actgrph),
                    actgrph);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagramList().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());

        p.moveToTrash(package1);
        Model.getPump().flushModelEvents();

        assertTrue("Class not in trash",
                Model.getUmlFactory().isRemoved(aClass));
        assertTrue("ActivityGraph not in trash",
                Model.getUmlFactory().isRemoved(actgrph));
        assertEquals(sizeDiagrams, p.getDiagramList().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }


    /**
     * Test deleting a package with a package with a Activity diagram.
     * The diagram should be deleted, too.
     */
    public void testDeletePackageWithPackageWithActivityDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagramList().size());

        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagramList().size();

        // test with a package and a class and activity diagram
        Object package1 =
            Model.getModelManagementFactory().buildPackage("test1");
        Model.getCoreHelper().setNamespace(package1, p.getModel());
        Object package2 =
            Model.getModelManagementFactory().buildPackage("test2");
        Model.getCoreHelper().setNamespace(package2, package1);

        // build the Activity Diagram
        Object actgrph =
            Model.getActivityGraphsFactory().buildActivityGraph(package2);
        UMLActivityDiagram d =
            new UMLActivityDiagram(
                    Model.getFacade().getNamespace(actgrph), actgrph);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagramList().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());

        p.moveToTrash(package1);
        Model.getPump().flushModelEvents();
        
        assertTrue("Package 2 not in trash",
                Model.getUmlFactory().isRemoved(package2));
        assertTrue("ActivityGraph not in trash",
                Model.getUmlFactory().isRemoved(actgrph));
        assertEquals(sizeDiagrams, p.getDiagramList().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }

    /**
     * Check that there is only one searchPath. 
     * See issue 1671.
     */
    public void testAddSearchPath() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertNotNull(p.getSearchPathList());
        assertTrue(p.getSearchPathList().size() == 1);
        
        p.addSearchPath("PROJECT_DIR");
        assertTrue(p.getSearchPathList().size() == 1);
        
        p.addSearchPath("foo");
        p.addSearchPath("foo");
        assertTrue(p.getSearchPathList().size() == 2);
        
    }


    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        ProjectManager.getManager().setCurrentProject(null);
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();
        (new InitDiagramAppearanceUI()).init();
        (new InitActivityDiagram()).init();
        (new InitCollaborationDiagram()).init();
        (new InitDeploymentDiagram()).init();
        (new InitSequenceDiagram()).init();
        (new InitStateDiagram()).init();
        (new InitClassDiagram()).init();
        (new InitUseCaseDiagram()).init();
        (new InitProfileSubsystem()).init();
    }
}
