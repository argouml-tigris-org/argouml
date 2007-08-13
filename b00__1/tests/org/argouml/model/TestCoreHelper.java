// $Id:TestCoreHelper.java 12576 2007-05-09 14:19:16Z tfmorris $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        InitializeModel.initializeDefault();
    }

    /**
     * Test getting the metamodel name.
     */
    public void testGetMetaModelName() {
	CheckUMLModelHelper.metaModelNameCorrect(
			 Model.getCoreFactory(),
			 TestCoreFactory.getTestableModelElements());
    }

    /**
     * Test stereotypes.
     */
    public void testIsValidStereoType() {
        CheckUMLModelHelper.isValidStereoType(
                          Model.getCoreFactory(),
                          TestCoreFactory.getTestableModelElements());
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
    
    public void testAddAnnotatedElement() {
	Object comment = Model.getCoreFactory().createComment();
	Object element = Model.getCoreFactory().createClass();
	Model.getCoreHelper().addAnnotatedElement(comment, element);
	Collection collection = Model.getFacade().getAnnotatedElements(comment);
	assertTrue(collection.size() == 1);
	assertTrue(element == collection.iterator().next());
    }
    
    public void testAddFeature() {
	Object class_ = Model.getCoreFactory().createClass();
	Object attribute = Model.getCoreFactory().createAttribute();
	Model.getCoreHelper().addFeature(class_, attribute);
	Collection collection = Model.getFacade().getFeatures(class_);
	assertTrue(collection.size() == 1);
	assertTrue(attribute == collection.iterator().next());
    }
    
    public void testAddLiteral() {
	Object enumeration = Model.getCoreFactory().createEnumeration();
	Object enumerationLiteral = Model.getCoreFactory()
		.createEnumerationLiteral();
	Model.getCoreHelper().addLiteral(enumeration, 0, enumerationLiteral);
	Collection collection = Model.getFacade().getEnumerationLiterals(
		enumeration);
	assertTrue(collection.size() == 1);
	assertTrue(enumerationLiteral == collection.iterator().next());
    }
    
    public void testAddParameter() {
	Object operation = Model.getCoreFactory().createOperation();
	Object parameter = Model.getCoreFactory().createParameter();
	Model.getCoreHelper().addParameter(operation, parameter);
	Collection collection = Model.getFacade().getParameters(operation);
	assertTrue(collection.size() == 1);
	assertTrue(parameter == collection.iterator().next());
    }
    
    public void testGetAllAttributes() {
	Object c1 = Model.getCoreFactory().createClass();
	Object c2 = Model.getCoreFactory().createClass();
	Model.getCoreFactory().buildGeneralization(c2, c1);
	Object attribute1 = Model.getCoreFactory().createAttribute();
	Object attribute2 = Model.getCoreFactory().createAttribute();
	Model.getCoreHelper().addOwnedElement(c1, attribute1);
	Model.getCoreHelper().addOwnedElement(c2, attribute2);
	Collection collection = Model.getCoreHelper().getAllAttributes(c2);
	assertTrue(collection.size() == 2);
	Iterator it = collection.iterator();
	Object attr1 = it.next();
	Object attr2 = it.next();
	assertTrue(attribute1 == attr1 || attribute1 == attr2);
	assertTrue(attribute2 == attr1 || attribute2 == attr2);
    }
    
    public void testGetAllBehavioralFeatures() {
	Object c1 = Model.getCoreFactory().createClass();
	Object c2 = Model.getCoreFactory().createClass();
	Object attribute1 = Model.getCoreFactory().createAttribute();
	Object attribute2 = Model.getCoreFactory().createAttribute();
	Model.getCoreHelper().addOwnedElement(c1, attribute1);
	Model.getCoreHelper().addOwnedElement(c2, attribute2);
	Model.getCoreHelper().addOwnedElement(c1, c2);
	Object operation = Model.getCoreFactory().buildOperation2(c2, null,
		null);
	Collection collection = Model.getCoreHelper().getAllBehavioralFeatures(
		c1);
	assertTrue(collection.size() == 1);
	assertTrue(operation == collection.iterator().next());
    }
    
    public void testGetAllClasses() {
	Object c1 = Model.getCoreFactory().createClass();
	Object c2 = Model.getCoreFactory().createClass();
	Object c3 = Model.getCoreFactory().createClass();
	Object attribute1 = Model.getCoreFactory().createAttribute();
	Object attribute2 = Model.getCoreFactory().createAttribute();
	Model.getCoreHelper().addOwnedElement(c1, attribute1);
	Model.getCoreHelper().addOwnedElement(c2, attribute2);
	Model.getCoreHelper().addOwnedElement(c1, c2);
	Model.getCoreHelper().addOwnedElement(c2, c3);
	Object operation = Model.getCoreFactory().buildOperation2(c2, null,
		null);
	Collection collection = Model.getCoreHelper().getAllClasses(c1);
	assertTrue(collection.size() == 2);
	Iterator it = collection.iterator();
	Object cls1 = it.next();
	Object cls2 = it.next();
	assertTrue(c2 == cls1 || c2 == cls2);
	assertTrue(c3 == cls1 || c3 == cls2);
    }
    
    public void testGetAllPossibleNamespaces() {
	Object m = Model.getModelManagementFactory().createModel();
	Object p = Model.getModelManagementFactory().createPackage();
	Object c = Model.getCoreFactory().createClass();
	Object attr = Model.getCoreFactory().createAttribute();
	Model.getCoreHelper().addOwnedElement(c, attr);
	Model.getCoreHelper().addOwnedElement(p, c);
	Model.getCoreHelper().addOwnedElement(m, p);
	Collection collection = Model.getCoreHelper().getAllPossibleNamespaces(
		Model.getCoreFactory().createClass(), m);
	assertTrue(collection.size() == 3);
	Iterator it = collection.iterator();
	Object n1 = it.next();
	Object n2 = it.next();
	Object n3 = it.next();
	assertTrue(m == n1 || m == n2 || m == n3);
	assertTrue(p == n1 || p == n2 || p == n3);
	assertTrue(c == n1 || c == n2 || c == n3);
    }
    
    public void testGetAllRealizedInterfaces() {
	Object c1 = Model.getCoreFactory().createClass();
	Object c2 = Model.getCoreFactory().createClass();
	Model.getCoreFactory().buildGeneralization(c2, c1);
	Object i1 = Model.getCoreFactory().createInterface();
	Object i2 = Model.getCoreFactory().createInterface();
	Object i3 = Model.getCoreFactory().createInterface();
	Model.getCoreFactory().buildGeneralization(i2, i1);
	Model.getCoreFactory().buildRealization(c1, i2, null);
	Model.getCoreFactory().buildRealization(c2, i3, null);
	Collection collection = Model.getCoreHelper().getAllRealizedInterfaces(
		c2);
	assertTrue(collection.size() == 2);
	Iterator it = collection.iterator();
	Object interface1 = it.next();
	Object interface2 = it.next();
	assertTrue(i2 == interface1 || i2 == interface2);
	assertTrue(i3 == interface1 || i3 == interface2);
    }
    
    public void testGetAllSupertypes() {
	Object c1 = Model.getCoreFactory().createClass();
	Object c2 = Model.getCoreFactory().createClass();
	Model.getCoreFactory().buildGeneralization(c2, c1);
	Object i1 = Model.getCoreFactory().createInterface();
	Model.getCoreFactory().buildRealization(c1, i1, null);
	Collection collection = Model.getCoreHelper().getAllSupertypes(c2);
	assertTrue(collection.size() == 1);
	assertTrue(c1 == collection.iterator().next());
    }
    
    public void testGetAssociateEndsInh() {
	Object p = Model.getModelManagementFactory().createPackage();
	Object c1 = Model.getCoreFactory().createClass();
	Object c2 = Model.getCoreFactory().createClass();
	Object c3 = Model.getCoreFactory().createClass();
	Model.getCoreFactory().buildGeneralization(c3, c1);
	Model.getCoreHelper().addOwnedElement(p, c1);
	Object association = Model.getCoreFactory().buildAssociation(c1, c2);
	Object end1 = Model.getFacade().getAssociationEnd(c1, association);
	Object end2 = Model.getFacade().getAssociationEnd(c2, association);
	Collection collection = Model.getCoreHelper().getAssociateEndsInh(c3);
	assertTrue(collection.size() == 1);
	assertTrue(end2 == collection.iterator().next());
    }

}
