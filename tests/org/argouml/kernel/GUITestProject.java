// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.util.Collection;

import junit.framework.TestCase;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.ActivityGraphsFactory;
import org.argouml.model.uml.CoreFactory;
import org.argouml.model.uml.ModelManagementFactory;
import org.argouml.model.uml.StateMachinesFactory;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.explorer.ExplorerTree;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;

/**
 * Test to check whether diagrams are deleted when their owning elements are
 * deleted.
 * @author mkl
 */
public class GUITestProject extends TestCase {

    private ExplorerTree exTree;
    /**
     * Constructor for TestProject.
     *
     * @param arg0 is the name of the test case.
     */
    public GUITestProject(String arg0) {
        super(arg0);
    }
    
    
    
    /**
     * Test deleting a package that contains a Class diagram.
     * The diagram should be deleted, too.
     */
    public void testDeletePackageWithClassDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        assertEquals("untitledModel", ModelFacade.getName(p.getModel()));
        assertEquals(p.getRoot(), p.getModel());
        
        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagrams().size();
        
        // test with a class and class diagram
        Object package1 = ModelManagementFactory.getFactory().buildPackage(
                "test1", null);
        Object package2 = ModelManagementFactory.getFactory().buildPackage(
                "test2", null);
        
        UMLClassDiagram cDiag = new UMLClassDiagram(package2);
        p.addMember(cDiag);
        assertEquals(sizeDiagrams + 1, p.getDiagrams().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        p.moveToTrash(package2);
        assertEquals(sizeDiagrams, p.getDiagrams().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }
    
    /**
     * Test deleting a class that contains a Statechart diagram.
     * The diagram should be deleted, too.
     */
    public void testDeleteClassWithStateDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        
        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagrams().size();
        
        // test with a class and class diagram
        Object package1 = ModelManagementFactory.getFactory().buildPackage(
                "test1", null);
        Object aClass = CoreFactory.getFactory().buildClass(package1);
        
        
        // try with Statediagram
        Object machine = StateMachinesFactory.getFactory().buildStateMachine(
                aClass);
        UMLStateDiagram d = new UMLStateDiagram(ModelFacade
                .getNamespace(machine), machine);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagrams().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        p.moveToTrash(aClass);
        assertTrue("Statemachine not in trash", p.isInTrash(machine));
        assertTrue("Class not in trash", p.isInTrash(aClass));
        assertEquals(sizeDiagrams, p.getDiagrams().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }
    
    /**
     * Test deleting a statechart diagram directly.
     */
    public void testDeleteStateDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        
        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagrams().size();
        
        // test with a class and class diagram
        Object package1 = ModelManagementFactory.getFactory().buildPackage(
                "test1", null);
        Object aClass = CoreFactory.getFactory().buildClass(package1);
        
        
        // try with Statediagram
        Object machine = StateMachinesFactory.getFactory().buildStateMachine(
                aClass);
        UMLStateDiagram d = new UMLStateDiagram(ModelFacade
                .getNamespace(machine), machine);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagrams().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        p.moveToTrash(d);
        assertTrue("Statediagram not in trash", p.isInTrash(d));
        assertEquals(sizeDiagrams, p.getDiagrams().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }

    /**
     * Test deleting a package that contains a Class.
     * The class should be deleted, too.
     */ 
    public void testDeleteClassWithInnerClass() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        
        // test with a class and an inner class
        Object aClass = CoreFactory.getFactory().buildClass("Test");
        Object bClass = CoreFactory.getFactory().buildClass(aClass);

        p.moveToTrash(aClass);
        
        assertTrue("Class not in trash", p.isInTrash(aClass));
        assertTrue("Inner Class not in trash", 
                UmlFactory.getFactory().isRemoved(bClass));
    }
   
    /**
     * Test deleting a package that contains a Class.
     * The class should be deleted, too.
     */ 
    public void testDeletePackageWithClass() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        
        // test with a class and class diagram
        Object package1 = ModelManagementFactory.getFactory().buildPackage(
                "test1", null);
        Object aClass = CoreFactory.getFactory().buildClass(package1);

        p.moveToTrash(package1);
        
        assertTrue("Class not in trash", 
                UmlFactory.getFactory().isRemoved(aClass));
    }

    
    /**
     * Test deleting a package that contains a Class with Statechart diagram.
     * The diagram should be deleted, too.
     */ 
    public void testDeletePackageWithStateDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        
        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagrams().size();
        
        // test with a class and class diagram
        Object package1 = ModelManagementFactory.getFactory().buildPackage(
                "test1", null);
        Object aClass = CoreFactory.getFactory().buildClass(package1);
        
        
        // try with Statediagram
        Object machine = StateMachinesFactory.getFactory().buildStateMachine(
                aClass);
        UMLStateDiagram d = new UMLStateDiagram(ModelFacade
                .getNamespace(machine), machine);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagrams().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        p.moveToTrash(package1);
        assertTrue("Class not in trash", 
                UmlFactory.getFactory().isRemoved(aClass));
        assertTrue("Statemachine not in trash", 
                UmlFactory.getFactory().isRemoved(machine));
        assertEquals(sizeDiagrams, p.getDiagrams().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }

    /**
     * Test deleting an operation that contains a statechart diagram.
     * The diagram should be deleted, too.
     */
    public void testDeleteOperationWithStateDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        
        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagrams().size();
        
        // test with a class and class diagram
        Object package1 = ModelManagementFactory.getFactory().buildPackage(
                "test1", null);
        Object aClass = CoreFactory.getFactory().buildClass(package1);
        
        Collection propertyChangeListeners = p.findFigsForMember(aClass);
        Object model = p.getModel();
        Object voidType = p.findType("void");
        Object oper = UmlFactory.getFactory().getCore()
            .buildOperation(aClass, model, voidType, propertyChangeListeners);
        
        // try with Statediagram
        Object machine = StateMachinesFactory.getFactory().buildStateMachine(
                oper);
        UMLStateDiagram d = new UMLStateDiagram(ModelFacade
                .getNamespace(machine), machine);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagrams().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        
        p.moveToTrash(oper);
        
        assertTrue("Operation not in trash", p.isInTrash(oper));
        assertTrue("Statemachine not in trash", 
                UmlFactory.getFactory().isRemoved(machine));
        assertEquals(sizeDiagrams, p.getDiagrams().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }

    
    /**
     * Test deleting a package with a Class with a Activity diagram.
     * The diagram should be deleted, too.
     */ 
    public void testDeletePackageWithClassWithActivityDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        
        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagrams().size();
        
        // test with a package and a class and activity diagram
        Object package1 = ModelManagementFactory.getFactory().buildPackage(
                "test1", null);
        Object aClass = CoreFactory.getFactory().buildClass(package1);
      
        // build the Activity Diagram
        Object actgrph = ActivityGraphsFactory.getFactory().buildActivityGraph(
                aClass);
        UMLActivityDiagram d = new UMLActivityDiagram(ModelFacade
                .getNamespace(actgrph), actgrph);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagrams().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        
        p.moveToTrash(package1);
        
        assertTrue("Class not in trash", 
                UmlFactory.getFactory().isRemoved(aClass));
        assertTrue("ActivityGraph not in trash", 
                UmlFactory.getFactory().isRemoved(actgrph));
        assertEquals(sizeDiagrams, p.getDiagrams().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }


    /**
     * Test deleting a package with a package with a Activity diagram.
     * The diagram should be deleted, too.
     */ 
    public void testDeletePackageWithPackageWithActivityDiagram() {
        Project p = ProjectManager.getManager().getCurrentProject();
        assertEquals(2, p.getDiagrams().size());
        
        int sizeMembers = p.getMembers().size();
        int sizeDiagrams = p.getDiagrams().size();
        
        // test with a package and a class and activity diagram
        Object package1 = ModelManagementFactory.getFactory().buildPackage(
                "test1", null);
        ModelFacade.setNamespace(package1, p.getModel());
        Object package2 = ModelManagementFactory.getFactory().buildPackage(
                "test2", null);
        ModelFacade.setNamespace(package2, package1);
      
        // build the Activity Diagram
        Object actgrph = ActivityGraphsFactory.getFactory().buildActivityGraph(
                package2);
        UMLActivityDiagram d = new UMLActivityDiagram(ModelFacade
                .getNamespace(actgrph), actgrph);
        p.addMember(d);
        assertEquals(sizeDiagrams + 1, p.getDiagrams().size());
        assertEquals(sizeMembers + 1, p.getMembers().size());
        
        p.moveToTrash(package1);
        
        assertTrue("Package 2 not in trash", 
                UmlFactory.getFactory().isRemoved(package2));
        assertTrue("ActivityGraph not in trash", 
                UmlFactory.getFactory().isRemoved(actgrph));
        assertEquals(sizeDiagrams, p.getDiagrams().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }

    
    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ProjectManager.getManager().setCurrentProject(null);
//        exTree = new ExplorerTree();
    }
}
