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

import junit.framework.TestCase;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.CoreFactory;
import org.argouml.model.uml.ModelManagementFactory;
import org.argouml.model.uml.StateMachinesFactory;
import org.argouml.ui.explorer.ExplorerTree;
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
        assertTrue("Class not int trash", p.isInTrash(aClass));
        assertEquals(sizeDiagrams, p.getDiagrams().size());
        assertEquals(sizeMembers, p.getMembers().size());
    }
    

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ProjectManager.getManager().setCurrentProject(null);
        exTree = new ExplorerTree();
    }
}
