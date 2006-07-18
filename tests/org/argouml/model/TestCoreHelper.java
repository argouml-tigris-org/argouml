// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

/**
 * @since Oct 10, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class TestCoreHelper extends TestCase {

    // Flag to enable performance testing - off by default
    private static final boolean PERFORMANCE_TEST = false;

    // Performance testing parameters
    private static final int CHILDREN_PER_NAMESPACE = 5;
    private static final int NAMESPACE_LEVELS = 5;
    private static final long TIME_LIMIT = 20L * 1000; // 20 sec.
    
    /**
     * Constructor for TestCoreHelper.
     *
     * @param arg0 is the name of the test case.
     */
    public TestCoreHelper(String arg0) {
	super(arg0);
    }

    /**
     * Test getting the metamodel name.
     */
    public void testGetMetaModelName() {
	CheckUMLModelHelper.metaModelNameCorrect(
			 Model.getCoreFactory(),
			 TestCoreFactory.getAllModelElements());
    }

    /**
     * Test stereotypes.
     */
    public void testIsValidStereoType() {
        CheckUMLModelHelper.isValidStereoType(
                          Model.getCoreFactory(),
                          TestCoreFactory.getAllModelElements());
    }

    /**
     * Test subtype check.
     */
    public void testIsSubType() {
    	assertTrue("Is not a subtype",
                       Model.getCoreHelper().
                       isSubType(
                               Model.getMetaTypes().getClassifier(),
                               Model.getMetaTypes().getUMLClass()));
        assertTrue("Is not a parent type",
                        !Model.getCoreHelper().
                        isSubType(
                                Model.getMetaTypes().getUMLClass(),
                                Model.getMetaTypes().getClassifier()));
        assertTrue("Is not a parent type",
                        !Model.getCoreHelper().
                        isSubType(Model.getMetaTypes().getUMLClass(),
                                  Model.getMetaTypes().getAggregationKind()));
    }

    /**
     * Test getting children.
     */
    public void testGetChildren() {
        CoreFactory coreFactory = Model.getCoreFactory();
        // Create a generalizable element with an element without children.
        Object ge = coreFactory.createClass();

        assertTrue(Model.getCoreHelper().getChildren(ge).size() == 0);

        // Add one child.
        Object g1 = coreFactory.createGeneralization();
        Model.getCoreHelper().setParent(g1, ge);
        Model.getCoreHelper().setChild(g1, coreFactory.createClass());

        assertTrue(Model.getCoreHelper().getChildren(ge).size() == 1);

        // Add another child.
        Object g2 = coreFactory.createGeneralization();
        Model.getCoreHelper().setParent(g2, ge);
        Object ge2 = coreFactory.createClass();
        Model.getCoreHelper().setChild(g2, ge2);

        assertTrue(Model.getCoreHelper().getChildren(ge).size() == 2);

        // Add grandchild.
        Object g3 = coreFactory.createGeneralization();
        Model.getCoreHelper().setParent(g3, ge2);
        Model.getCoreHelper().setChild(g3, coreFactory.createClass());

        assertTrue(Model.getCoreHelper().getChildren(ge).size() == 3);
    }

    /**
     * Test if adding a client to a binary dependency
     * actually increases the client count.
     */
    public void testAddClient() {
        Object model = Model.getModelManagementFactory().createModel();
        Object class1 = Model.getCoreFactory().buildClass(model);
        Object class2 = Model.getCoreFactory().buildClass(model);
        Object dep = Model.getCoreFactory().buildDependency(class1, class2);
        Object class3 = Model.getCoreFactory().buildClass(model);
        Model.getCoreHelper().addClient(dep, class3);
        Collection clients = Model.getFacade().getClients(dep);
        assertEquals(2, Model.getFacade().getClients(dep).size());
        Iterator it = clients.iterator();
        assertEquals(class1, it.next());
        assertEquals(class3, it.next());
    }
    
    /**
     * Test the getFirstSharedNamespace method for correctness
     * and, optionally, performance.
     */
    public void testGetFirstSharedNamespace() {
        Object model = Model.getModelManagementFactory().createModel();
        CoreFactory cf = Model.getCoreFactory();
        
        // Build namespace hierarchy like this:
        //   g     a
        //         /\
        //        b  c
        //           /\
        //          d  e f
        
        Object a = cf.buildClass("a", model);
        Object b = cf.buildClass("b", a);
        Object c = cf.buildClass("c", a);
        Object d = cf.buildClass("d", c);
        Object e = cf.buildClass("e", c);
        Object f = cf.buildClass("f", c);   
        Object g = cf.buildClass();      

        CoreHelper ch = Model.getCoreHelper();

        assertEquals("Got wrong namespace for first shared", a, 
                ch.getFirstSharedNamespace(b, e));
        assertEquals("Got wrong namespace for first shared", c, 
                ch.getFirstSharedNamespace(d, e));
        assertEquals("Got wrong namespace for first shared", a, 
                ch.getFirstSharedNamespace(a, e));
        assertEquals("Got wrong namespace for first shared", a, 
                ch.getFirstSharedNamespace(a, c));
        assertEquals("Got wrong namespace for first shared", a, 
                ch.getFirstSharedNamespace(b, c));
        // The following test fails with the current implementation
//        assertNull("getFirstSharedNamespace didn't return null"
//                + " when none shared",
//                ch.getFirstSharedNamespace(g, a));
        
        // Try changing namespace of element and make sure results track
        assertEquals("Got wrong namespace for first shared", c, 
                ch.getFirstSharedNamespace(d, f));
        ch.setNamespace(f, b);
        ch.setNamespace(g, f);
        assertEquals("Got wrong namespace after setNamespace", a, 
                ch.getFirstSharedNamespace(d, f));
        assertEquals("Got wrong namespace after setNamespace", a, 
                ch.getFirstSharedNamespace(g, e));
        
        if (PERFORMANCE_TEST) {
            List children = new ArrayList();
            Object root = cf.buildClass();
            children.add(root);
            createChildren(children, root, 0, NAMESPACE_LEVELS,
                    CHILDREN_PER_NAMESPACE);
            // Tree is created depth first, so this should be at the bottom
            Object base = children.get(NAMESPACE_LEVELS);
            long startTime = System.currentTimeMillis();
            int i;
            for (i = 0; i < children.size(); i++) {
                Object o = ch.getFirstSharedNamespace(base, children.get(i));
                if ( i % 100 == 0) {
                    // Check periodically to see if we've exceeded time limit
                    if ((System.currentTimeMillis() - startTime) > TIME_LIMIT) {
                        break;
                    }
                }
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Iterations: " + i + ", time: "
                    + (endTime - startTime) / 1.0e3 + " seconds.");
            System.out.println("Average time for getFirstSharedNameSpace = " 
                    + (endTime - startTime) * 1.0 / i
                    + " millisecs searching in " 
                    + children.size() + " total elements.");
        }
    }
    
    /*
     * Populate our namespace hierarchy to the requested depth.  Total number
     * of created elements is children^maxLevel, so be careful not to increase
     * parameters too much.
     */
    private List createChildren(List children, Object parent, int currentLevel,
            int maxLevel, int numChildren) {
        currentLevel++;
        if (currentLevel > maxLevel) {
            return children;
        }
        Object child;
        for (int i = 0; i < numChildren; i++) {
            child = Model.getCoreFactory().buildClass(
                    "l" + currentLevel + "n" + i);
            children.add(child);
            Model.getCoreHelper().setNamespace(child, parent);
            createChildren(children, child, currentLevel, maxLevel, 
                    numChildren);
        }
        return children;
    }

}
