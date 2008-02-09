// $Id$
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

/**
 * Non-gui tests of the CopyHelper class.
 */
public class TestCopyHelper extends TestCase {
    
    private static final String TV_TYPE = "TVType";
    private static final String TV_VALUE1 = "TVValue";
    private static final String TV_VALUE2 = "TVNewValue";

    /*
     * @see junit.framework.TestCase#TestCase(String)
     */
    public TestCopyHelper(String name) {
	super(name);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() {
        InitializeModel.initializeDefault();

        // Make sure we have a root model for TagDefinitions to get put in.
        ModelManagementFactory mmf = Model.getModelManagementFactory();
        mmf.setRootModel(mmf.createModel());
    }

    /**
     * Testing the existance of public static members.
     */
    public void compileTestPublicStaticMembers() {
        Model.getCopyHelper();
    }

    /**
     * Testing the existance of public members.
     */
    public void compileTestPublicMembers() {
	CopyHelper h = Model.getCopyHelper();

	h.copy(Model.getCoreFactory().createComment(),
			Model.getModelManagementFactory().createPackage());
    }
    
    /**
     * Tests CopyHelper without using stereotypes.
     * <p>
     * The test is using the tree of objects:
     * 
     * <pre>
     *              model
     *              /   \
     *            p1     p2
     *           /  \
     *      class_  nestedPackage
     *                \
     *                interface_
     *                  \
     *                  attribute
     * </pre>
     * 
     * TODO: This test doesn't match the MDR implementation because it expects
     * the contents of a namespace to be copied.  It is disabled until it can
     * be fixed to run with both MDR and eUML. - tfm 20070820
     * 
     * @author Bogdan
     */
    public void xtestCopyHelperBasic() {
	// create the tree of objects
	Object model = Model.getModelManagementFactory().createModel();
	Object p1 = Model.getModelManagementFactory().createPackage();
	Object p2 = Model.getModelManagementFactory().createPackage();
	Model.getCoreHelper().addOwnedElement(model, p1);
	Model.getCoreHelper().addOwnedElement(model, p2);
	Object nestedPackage = Model.getModelManagementFactory()
		.createPackage();
	Model.getCoreHelper().addOwnedElement(p1, nestedPackage);
	Object class_ = Model.getCoreFactory().createClass();
	Model.getCoreHelper().addOwnedElement(p1, class_);
	Object interface_ = Model.getCoreFactory().createInterface();
	Model.getCoreHelper().addOwnedElement(nestedPackage, interface_);
	Object attribute = Model.getCoreFactory().createAttribute();
	Model.getCoreHelper().addOwnedElement(interface_, attribute);

	// copy package p1 into p2 package
	Object copiedPackage = Model.getCopyHelper().copy(p1, p2);

	// change the structure in p1 package
	Model.getCoreHelper().addOwnedElement(p1, interface_);
	Model.getCoreHelper().addOwnedElement(nestedPackage, class_);

	// verify the copied package
	assertNotNull(copiedPackage);
	assertTrue(p1.getClass() == copiedPackage.getClass());
	assertTrue(p1 != copiedPackage);
	assertTrue(Model.getFacade().getNamespace(copiedPackage) == p2);

	// verify the copied class
	Collection collection = Model.getCoreHelper().getAllClasses(
		copiedPackage);
	assertNotNull(collection);
	assertTrue(collection.size() == 1);
	Object copiedClass = collection.iterator().next();
	assertTrue(Model.getFacade().getNamespace(copiedClass) == copiedPackage);
	assertTrue(class_ != copiedClass);
	assertTrue(class_.getClass() == copiedClass.getClass());

	// verify the copied nested package and the copied interface
	collection = Model.getCoreHelper().getAllInterfaces(copiedPackage);
	assertNotNull(collection);
	assertTrue(collection.size() == 1);
	Object copiedInterface = collection.iterator().next();
	Object copiedNestedPackage = Model.getFacade().getNamespace(
		copiedInterface);
	assertNotNull(copiedNestedPackage);
	assertTrue(Model.getFacade().getNamespace(copiedNestedPackage) == copiedPackage);
	assertTrue(nestedPackage != copiedNestedPackage);
	assertTrue(nestedPackage.getClass() == copiedNestedPackage.getClass());
	assertTrue(interface_ != copiedInterface);
	assertTrue(interface_.getClass() == copiedInterface.getClass());

	// verify the copied attribute
	collection = Model.getFacade().getAttributes(copiedInterface);
	assertNotNull(collection);
	assertTrue(collection.size() == 1);
	Object copiedAttribute = collection.iterator().next();
	assertTrue(attribute != copiedAttribute);
	assertTrue(attribute.getClass() == copiedAttribute.getClass());
    }

    /**
     * Testing the copying of a class.
     */
    public void testCopyClass() {
	Object m1 = Model.getModelManagementFactory().createModel();
	Object m2 = Model.getModelManagementFactory().createModel();
	CopyHelper helper = Model.getCopyHelper();

	Object o;
	Object c;
	Object c2;
	Object k;
	Object st;

	k = Model.getCoreFactory().createClass();
	Model.getCoreHelper().addOwnedElement(m1, k);

        st = Model.getExtensionMechanismsFactory().buildStereotype("clsStT", k);
        Model.getCoreHelper().addOwnedElement(m1, st);

	// See if we can copy a class right off
	o = helper.copy(k, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == k.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkClassCopy(k, c);

	// Change things
	Model.getCoreHelper().setName(k, "TestClass");
        Model.getCoreHelper().setVisibility(k,
                Model.getVisibilityKind().getPublic());
        Model.getCoreHelper().setSpecification(k, true);
        addNewTaggedValue(k, TV_TYPE, TV_VALUE1);
        Model.getCoreHelper().setActive(k, false);
	c = helper.copy(k, m2);
	checkClassCopy(k, c);

	// Assert that the copy isn't modified when the source changes
	Model.getCoreHelper().setName(k, "TestClass2");
        Model.getCoreHelper().setVisibility(k,
                Model.getVisibilityKind().getProtected());
        Model.getCoreHelper().setSpecification(k, false);
        updateTaggedValue(k, TV_TYPE, TV_VALUE2);

        Model.getCoreHelper().setActive(k, true);
        Model.getExtensionMechanismsHelper().addCopyStereotype(k, st);
	assertEquals("TestClass", Model.getFacade().getName(c));
	assertTrue(Model.getFacade().getVisibility(c)
                == Model.getVisibilityKind().getPublic());
	assertTrue(Model.getFacade().isSpecification(c));
	assertTrue(!Model.getFacade().isActive(c));
	assertEquals(TV_VALUE1, Model.getFacade().getValueOfTag(
                Model.getFacade().getTaggedValue(c, TV_TYPE)));

	// See if the other copy was just a lucky shot
	c = helper.copy(k, m2);
	checkClassCopy(k, c);

	// See if two copies look like copies of eachother
	c2 = helper.copy(k, m2);
	checkClassCopy(c, c2);
    }

    /**
     * Testing the copying of a data type.
     */
    public void testCopyDataType() {
	Object m1 = Model.getModelManagementFactory().createModel();
	Object m2 = Model.getModelManagementFactory().createModel();
	CopyHelper helper = Model.getCopyHelper();

	Object o;
	Object c;
	Object c2;
	Object d;
	Object st;

	d = Model.getCoreFactory().createDataType();
        Model.getCoreHelper().addOwnedElement(m1, d);

        st = Model.getExtensionMechanismsFactory().buildStereotype("dttStT", d);
        Model.getCoreHelper().addOwnedElement(m1, st);

	// See if we can copy a datatype right off
	o = helper.copy(d, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == d.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkDataTypeCopy(d, c);

	// Change things
	Model.getCoreHelper().setName(d, "TestDataType");
        Model.getCoreHelper().setVisibility(d,
                Model.getVisibilityKind().getPublic());
        Model.getCoreHelper().setSpecification(d, true);
        addNewTaggedValue(d, TV_TYPE, TV_VALUE1);
	c = helper.copy(d, m2);
	checkDataTypeCopy(d, c);

	// Assert that the copy isn't modified when the source changes
	Model.getCoreHelper().setName(d, "TestDataType2");
        Model.getCoreHelper().setVisibility(d,
                Model.getVisibilityKind().getProtected());
        Model.getCoreHelper().setSpecification(d, false);
        updateTaggedValue(d, TV_TYPE, TV_VALUE2);
        Model.getExtensionMechanismsHelper().addCopyStereotype(d, st);
	assertEquals("TestDataType", Model.getFacade().getName(c));
	assertTrue(Model.getFacade().getVisibility(c)
                == Model.getVisibilityKind().getPublic());
	assertTrue(Model.getFacade().isSpecification(c));
	assertEquals(TV_VALUE1, Model.getFacade().getValueOfTag(
                Model.getFacade().getTaggedValue(c, TV_TYPE)));

	// See if the other copy was just a lucky shot
	c = helper.copy(d, m2);
	checkDataTypeCopy(d, c);

	// See if two copies look like copies of each other
	c2 = helper.copy(d, m2);
	checkDataTypeCopy(c, c2);
    }

    /**
     * Testing the copying of an interface.
     */
    public void testCopyInterface() {
	Object m1 =  Model.getModelManagementFactory().createModel();
	Object m2 =  Model.getModelManagementFactory().createModel();
	CopyHelper helper = Model.getCopyHelper();

	Object o;
	Object c;
	Object c2;
	Object i;
	Object st;

	i = Model.getCoreFactory().createInterface();
        Model.getCoreHelper().addOwnedElement(m1, i);

        st = Model.getExtensionMechanismsFactory().buildStereotype("intStT", i);
        Model.getExtensionMechanismsHelper().addBaseClass(st, "Interface");
        Model.getCoreHelper().addOwnedElement(m1, st);

	// See if we can copy an interface right off
	o = helper.copy(i, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == i.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkInterfaceCopy(i, c);

	// Change things
        Model.getCoreHelper().setName(i, "TestInterface");
        Model.getCoreHelper().setVisibility(i,
                Model.getVisibilityKind().getPublic());
        Model.getCoreHelper().setSpecification(i, true);
        addNewTaggedValue(i, TV_TYPE, TV_VALUE1);
        c = helper.copy(i, m2);
        checkInterfaceCopy(i, c);

	// Assert that the copy isn't modified when the source changes
        Model.getCoreHelper().setName(i, "TestInterface2");
        Model.getCoreHelper().setVisibility(i,
                Model.getVisibilityKind().getProtected());
        Model.getCoreHelper().setSpecification(i, false);
        updateTaggedValue(i, TV_TYPE, TV_VALUE2);

        Model.getExtensionMechanismsHelper().addCopyStereotype(i, st);
	assertEquals("TestInterface", Model.getFacade().getName(c));
	assertTrue(Model.getFacade().getVisibility(c)
                == Model.getVisibilityKind().getPublic());
	assertTrue(Model.getFacade().isSpecification(c));
	assertEquals(TV_VALUE1, Model.getFacade().getValueOfTag(
                Model.getFacade().getTaggedValue(c, TV_TYPE)));

	// See if the other copy was just a lucky shot
	c = helper.copy(i, m2);
	checkInterfaceCopy(i, c);

	// See if two copies look like copies of eachother
	c2 = helper.copy(i, m2);
	checkInterfaceCopy(c, c2);
    }

    /*
     * Create a new tagged value with the given type and value
     * and add it to a ModelElement.
     */
    private void addNewTaggedValue(Object element, String type, 
            String dataValue) {
        Model.getExtensionMechanismsHelper().addTaggedValue(
                element,
                Model.getExtensionMechanismsFactory().buildTaggedValue(
                        type, dataValue));
    }


    /*
     * Update an existing tagged value on a model element.
     */
    private void updateTaggedValue(Object element, String type, 
            String dataValue) {
        Object tv = Model.getFacade().getTaggedValue(element, type);
        Model.getExtensionMechanismsHelper().setValueOfTag(tv, dataValue);
    }

    /**
     * Testing the copying of a package.
     */
    public void testCopyPackage() {
	Object m1 =  Model.getModelManagementFactory().createModel();
	Object m2 =  Model.getModelManagementFactory().createModel();
	CopyHelper helper = Model.getCopyHelper();

	Object o;
	Object c;
	Object c2;
	Object p;
	Object st;

	p = Model.getModelManagementFactory().createPackage();
        Model.getCoreHelper().addOwnedElement(m1, p);

        st = Model.getExtensionMechanismsFactory().buildStereotype("pkgStT", p);
        Model.getCoreHelper().addOwnedElement(m1, st);

	// See if we can copy a package right off
	o = helper.copy(p, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == p.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkPackageCopy(p, c);

	// Change things
	Model.getCoreHelper().setName(p, "TestPackage");
        Model.getCoreHelper().setVisibility(p,
                Model.getVisibilityKind().getPublic());
        Model.getCoreHelper().setSpecification(p, true);
        addNewTaggedValue(p, TV_TYPE, TV_VALUE1);
        c = helper.copy(p, m2);
	checkPackageCopy(p, c);

	// Assert that the copy isn't modified when the source changes
        Model.getCoreHelper().setName(p, "TestPackage2");
        Model.getCoreHelper().setVisibility(p,
                Model.getVisibilityKind().getProtected());
        Model.getCoreHelper().setSpecification(p, false);
        updateTaggedValue(p, TV_TYPE, TV_VALUE2);
        Model.getExtensionMechanismsHelper().addCopyStereotype(p, st);
	assertEquals("TestPackage", Model.getFacade().getName(c));
	assertTrue(Model.getFacade().getVisibility(c)
                == Model.getVisibilityKind().getPublic());
	assertTrue(Model.getFacade().isSpecification(c));
	assertEquals(TV_VALUE1, Model.getFacade().getValueOfTag(
                Model.getFacade().getTaggedValue(c, TV_TYPE)));

	// See if the other copy was just a lucky shot
	c = helper.copy(p, m2);
	checkPackageCopy(p, c);

	// See if two copies look like copies of eachother
	c2 = helper.copy(p, m2);
	checkPackageCopy(c, c2);
    }

    /**
     * Testing the copying of a stereotype.
     */
    public void testCopyStereotype() {
	Object m1 = Model.getModelManagementFactory().createModel();
	Object m2 = Model.getModelManagementFactory().createModel();
	CopyHelper helper = Model.getCopyHelper();

	Object o;
	Object c;
	Object c2;
	Object s;
	Object st;

	s = Model.getExtensionMechanismsFactory().buildStereotype(null, m1);
        Model.getCoreHelper().addOwnedElement(m1, s);

        st =
            Model.getExtensionMechanismsFactory()
                .buildStereotype("sttStT", m1);
        Model.getCoreHelper().addOwnedElement(m1, st);

	// See if we can copy a stereotype right off
	o = helper.copy(s, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == s.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkStereotypeCopy(s, c);

	// Change things
        Model.getCoreHelper().setName(s, "TestStereotype");
        Model.getCoreHelper().setVisibility(s,
                Model.getVisibilityKind().getPublic());
        Model.getCoreHelper().setSpecification(s, true);
        addNewTaggedValue(s, TV_TYPE, TV_VALUE1);
        Model.getExtensionMechanismsHelper().removeBaseClass(s, "Model");
        Model.getExtensionMechanismsHelper().addBaseClass(s, "ModelElement");
        Model.getExtensionMechanismsHelper().setIcon(s, "Icon1");
	c = helper.copy(s, m2);
	checkStereotypeCopy(s, c);

	// Assert that the copy isn't modified when the source changes
        Model.getCoreHelper().setName(s, "TestStereotype2");
        Model.getCoreHelper().setVisibility(s,
                Model.getVisibilityKind().getProtected());
        Model.getCoreHelper().setSpecification(s, false);
        updateTaggedValue(s, TV_TYPE, TV_VALUE2);
        Model.getExtensionMechanismsHelper().addCopyStereotype(s, st);
        Model.getExtensionMechanismsHelper().addBaseClass(st, "ClassifierRole");
        Model.getExtensionMechanismsHelper().setIcon(s, "Icon2");
        assertEquals("TestStereotype", Model.getFacade().getName(c));
        assertTrue(Model.getFacade().getVisibility(c) == Model
                .getVisibilityKind().getPublic());
        assertTrue(Model.getFacade().isSpecification(c));
        Collection bases = Model.getFacade().getBaseClasses(c);
        assertEquals(1, bases.size());
        assertEquals("ModelElement", bases.iterator().next());
        assertEquals("Icon1", Model.getFacade().getIcon(c));
        assertEquals(TV_VALUE1, Model.getFacade().getValueOfTag(
                Model.getFacade().getTaggedValue(c, TV_TYPE)));

	// See if the other copy was just a lucky shot
	c = helper.copy(s, m2);
	checkStereotypeCopy(s, c);

	// See if two copies look like copies of each other
	c2 = helper.copy(s, m2);
	checkStereotypeCopy(c, c2);
    }

    private void checkClassCopy(Object c1, Object c2) {
        checkClassifierCopy(c1, c2);
        assertTrue(Model.getFacade().isActive(c1) == Model.getFacade()
                .isActive(c2));
    }

    private void checkClassifierCopy(Object c1, Object c2) {
	checkNamespaceCopy(c1, c2);
	checkGeneralizableElementCopy(c1, c2);
    }

    private void checkDataTypeCopy(Object d1, Object d2) {
	checkClassifierCopy(d1, d2);
    }

    private void checkGeneralizableElementCopy(
    		Object e1,
    		Object e2) {
	checkModelElementCopy(e1, e2);

	assertTrue(Model.getFacade().isAbstract(e1) == Model.getFacade()
                .isAbstract(e2));
        assertTrue(Model.getFacade().isLeaf(e1)
                == Model.getFacade().isLeaf(e2));
        assertTrue(Model.getFacade().isRoot(e1)
                == Model.getFacade().isRoot(e2));
    }

    private void checkInterfaceCopy(Object i1, Object i2) {
	checkClassifierCopy(i1, i2);
    }

    private void checkModelElementCopy(Object e1, Object e2) {
        // Parent is Element, but it has no attributes or associations to check
	// checkElementCopy(e1, e2);
	if (Model.getFacade().getName(e1) == null) {
            assertNull(Model.getFacade().getName(e2));
        } else {
            assertEquals(Model.getFacade().getName(e1), Model.getFacade()
                    .getName(e2));
        }
        assertEquals(Model.getFacade().getVisibility(e1),
                     Model.getFacade().getVisibility(e2));
        assertEquals(Model.getFacade().isSpecification(e1),
                     Model.getFacade().isSpecification(e2));
        assertEquals(Model.getFacade().getTaggedValuesCollection(e1).size(),
                     Model.getFacade().getTaggedValuesCollection(e2).size());

	Iterator it = Model.getFacade().getTaggedValues(e1);
	while (it.hasNext()) {
	    Object tv = it.next();
	    Object e2Tv =
                Model.getFacade().getTaggedValue(e2,
                    Model.getFacade().getTag(tv));
            if (Model.getFacade().getValueOfTag(tv) == null) {
	        assertNull(Model.getFacade().getValueOfTag(e2Tv));
	    } else {
	        assertEquals(Model.getFacade().getValueOfTag(tv),
	        		Model.getFacade().getValueOfTag(e2Tv));
	    }
	}

	if (Model.getFacade().getStereotypes(e1) == null) {
	    assertNull(Model.getFacade().getStereotypes(e2));
	} else if (Model.getFacade().getStereotypes(e1).isEmpty()) {
	    assertTrue(Model.getFacade().getStereotypes(e2).isEmpty());
	} else {
	    assertTrue(!Model.getFacade().getStereotypes(e2).isEmpty());
	    Object firstStereoOfe1 =
                Model.getFacade().getStereotypes(e1).iterator().next();
            Object firstStereoOfe2 =
                Model.getFacade().getStereotypes(e2).iterator().next();
	    assertNotNull(Model.getFacade().getName(firstStereoOfe2));
	    assertEquals(Model.getFacade().getName(firstStereoOfe1),
	    		Model.getFacade().getName(firstStereoOfe2));
	}
    }

    private void checkNamespaceCopy(Object n1, Object n2) {
	checkModelElementCopy(n1, n2);
    }

    private void checkPackageCopy(Object p1, Object p2) {
	checkNamespaceCopy(p1, p2);
    }

    private void checkStereotypeCopy(Object s1, Object s2) {
	checkGeneralizableElementCopy(s1, s2);
        assertEquals(Model.getFacade().getBaseClasses(s1), Model.getFacade()
                .getBaseClasses(s2));

	if (Model.getFacade().getIcon(s1) == null) {
	    assertNull(Model.getFacade().getIcon(s2));
	} else {
            assertEquals(Model.getFacade().getIcon(s1), Model.getFacade()
                    .getIcon(s2));
        }

	// TODO: constraints
	// TODO: required tags
    }
}
