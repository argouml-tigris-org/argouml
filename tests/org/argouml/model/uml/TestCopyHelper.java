// $Id$
// Copyright (c) 2003-2005 The Regents of the University of California. All
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

package org.argouml.model.uml;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import junit.framework.TestCase;

import org.argouml.model.CopyHelper;
import org.argouml.model.Model;

/**
 * Non-gui tests of the CopyHelper class.
 */
public class TestCopyHelper extends TestCase {

    /**
     * @see junit.framework.TestCase#TestCase(String)
     */
    public TestCopyHelper(String name) {
	super(name);
	
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
			Model.getCoreFactory().createNamespace());
    }

    /**
     * Testing the copying of a class.
     */
    public void testCopyClass() {
	Model m1 = (Model) Model.getModelManagementFactory().createModel();
	Model m2 = (Model) Model.getModelManagementFactory().createModel();
	CopyHelper helper = Model.getCopyHelper();

	Object o;
	Object c;
	Object c2;
	Object k;
	Object st;

	k = Model.getCoreFactory().createClass();
	Model.getCoreHelper().addOwnedElement(m1,k);

	st = Model.getExtensionMechanismsFactory()
	        .buildStereotype("clsStT", k);
	Model.getExtensionMechanismsHelper().setBaseClass(st,"Class");
	Model.getCoreHelper().addOwnedElement(m1,st);

	// See if we can copy a class right off
	o = helper.copy(k, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == k.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkClassCopy(k, c);

	// Change things
	Model.getCoreHelper().setName(k,"TestClass");
	Model.getCoreHelper().setVisibility(k,Model.getVisibilityKind().getPublic());
	Model.getCoreHelper().setSpecification(k,true);
	Object taggedValue = Model.
		getExtensionMechanismsFactory().buildTaggedValue("TVKey", "TVValue");
	Model.getExtensionMechanismsHelper().addTaggedValue(k,taggedValue);
	Model.getCoreHelper().setActive(k,false);
	c = helper.copy(k, m2);
	checkClassCopy(k, c);

	// Assert that the copy isn't modified when the source changes
	Model.getCoreHelper().setName(k,"TestClass2");
	Model.getCoreHelper().setVisibility(k,Model.getVisibilityKind().getProtected());
	Model.getCoreHelper().setSpecification(k,false);
	taggedValue = Model.
	getExtensionMechanismsFactory().buildTaggedValue("TVKey", "TVValue");	
	Collection taggedValues = new Vector();
	taggedValues.add(taggedValue);
	Model.getExtensionMechanismsHelper().setTaggedValue(k,taggedValues);	
	Model.getCoreHelper().setActive(k,true);
	Model.getExtensionMechanismsHelper().setStereoType(k,st);		
	assertEquals("TestClass", Model.getFacade().getName(c));
	assertTrue(Model.getFacade().getVisibility(c) == Model.getVisibilityKind().getPublic());
	assertTrue(Model.getFacade().isSpecification(c));
	assertTrue(!Model.getFacade().isActive(c));
	assertEquals("TVValue", Model.getFacade().getTaggedValue(c,"TVKey"));

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
	Model m1 = (Model) Model.getModelManagementFactory().createModel();
	Model m2 = (Model) Model.getModelManagementFactory().createModel();
	CopyHelper helper = Model.getCopyHelper();

	Object o;
	Object c;
	Object c2;
	Object d;
	Object st;

	d = Model.getCoreFactory().createDataType();
	Model.getCoreHelper().addOwnedElement(m1,d);

	st = Model.getExtensionMechanismsFactory()
	        .buildStereotype("dttStT", d);
	Model.getExtensionMechanismsHelper().setBaseClass(st,"DataType");
	Model.getCoreHelper().addOwnedElement(m1,st);

	// See if we can copy a datatype right off
	o = helper.copy(d, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == d.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkDataTypeCopy(d, c);

	// Change things
	Model.getCoreHelper().setName(d,"TestDataType");
	Model.getCoreHelper().setVisibility(d,Model.getVisibilityKind().getPublic());
	Model.getCoreHelper().setSpecification(d,true);
	Model.getExtensionMechanismsHelper().setValueOfTag
	(Model.getFacade().getTaggedValue(d,"TVKey"),"TVNewValue");	
	c = helper.copy(d, m2);
	checkDataTypeCopy(d, c);

	// Assert that the copy isn't modified when the source changes
	Model.getCoreHelper().setName(d,"TestDataType2");
	Model.getCoreHelper().setVisibility(d,Model.getVisibilityKind().getProtected());
	Model.getCoreHelper().setSpecification(d,false);
	Model.getExtensionMechanismsHelper().setValueOfTag
		(Model.getFacade().getTaggedValue(d,"TVKey"),"TVNewValue");
	Model.getExtensionMechanismsHelper().setStereoType(d,st);	
	assertEquals("TestDataType", Model.getFacade().getName(c));
	assertTrue(Model.getFacade().getVisibility(c) == Model.getVisibilityKind().getPublic());
	assertTrue(Model.getFacade().isSpecification(c));
	assertEquals("TVValue", Model.getFacade().getTaggedValue(c,"TVKey"));

	// See if the other copy was just a lucky shot
	c = helper.copy(d, m2);
	checkDataTypeCopy(d, c);

	// See if two copies look like copies of eachother
	c2 = helper.copy(d, m2);
	checkDataTypeCopy(c, c2);
    }

    /**
     * Testing the copying of an interface.
     */
    public void testCopyInterface() {
	Model m1 = (Model) Model.getModelManagementFactory().createModel();
	Model m2 = (Model) Model.getModelManagementFactory().createModel();
	CopyHelper helper = Model.getCopyHelper();

	Object o;
	Object c;
	Object c2;
	Object i;
	Object st;

	i = Model.getCoreFactory().createInterface();
	Model.getCoreHelper().addOwnedElement(m1,i);

	st = Model.getExtensionMechanismsFactory()
	        .buildStereotype("intStT", i);
	Model.getExtensionMechanismsHelper().setBaseClass(st,"Interface");	
	Model.getCoreHelper().addOwnedElement(m1,st);

	// See if we can copy an interface right off
	o = helper.copy(i, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == i.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkInterfaceCopy(i, c);

	// Change things
	Model.getCoreHelper().setName(i,"TestInterface");
	Model.getCoreHelper().setVisibility(i,Model.getVisibilityKind().getPublic());
	Model.getCoreHelper().setSpecification(i,true);
	Model.getExtensionMechanismsHelper().setValueOfTag
	(Model.getFacade().getTaggedValue(i,"TVKey"),"TVNewValue");	
	c = helper.copy(i, m2);
	checkInterfaceCopy(i, c);

	// Assert that the copy isn't modified when the source changes
	Model.getCoreHelper().setName(i,"TestInterface2");
	Model.getCoreHelper().setVisibility(i,Model.getVisibilityKind().getProtected());
	Model.getCoreHelper().setSpecification(i,false);
	Model.getExtensionMechanismsHelper().setValueOfTag
	(Model.getFacade().getTaggedValue(i,"TVKey"),"TVNewValue");	
	Model.getExtensionMechanismsHelper().setStereoType(i,st);	
	assertEquals("TestInterface", Model.getFacade().getName(c));
	assertTrue(Model.getFacade().getVisibility(c) == Model.getVisibilityKind().getPublic());
	assertTrue(Model.getFacade().isSpecification(c));
	assertEquals("TVValue", Model.getFacade().getTaggedValue(c,"TVKey"));

	// See if the other copy was just a lucky shot
	c = helper.copy(i, m2);
	checkInterfaceCopy(i, c);

	// See if two copies look like copies of eachother
	c2 = helper.copy(i, m2);
	checkInterfaceCopy(c, c2);
    }

    /**
     * Testing the copying of a package.
     */
    public void testCopyPackage() {
	Model m1 = (Model) Model.getModelManagementFactory().createModel();
	Model m2 = (Model) Model.getModelManagementFactory().createModel();
	CopyHelper helper = Model.getCopyHelper();

	Object o;
	Object c;
	Object c2;
	Object p;
	Object st;

	p = Model.getModelManagementFactory().createPackage();
	Model.getCoreHelper().addOwnedElement(m1,p);

	st = Model.getExtensionMechanismsFactory()
	        .buildStereotype("pkgStT", p);
	Model.getExtensionMechanismsHelper().setBaseClass(st,"Package");
	Model.getCoreHelper().addOwnedElement(m1,st);

	// See if we can copy a package right off
	o = helper.copy(p, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == p.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkPackageCopy(p, c);

	// Change things
	Model.getCoreHelper().setName(p,"TestPackage");
	Model.getCoreHelper().setVisibility(p,Model.getVisibilityKind().getPublic());
	Model.getCoreHelper().setSpecification(p,true);
	Model.getExtensionMechanismsHelper().setValueOfTag
	(Model.getFacade().getTaggedValue(p,"TVKey"),"TVNewValue");	
	c = helper.copy(p, m2);
	checkPackageCopy(p, c);

	// Assert that the copy isn't modified when the source changes
	Model.getCoreHelper().setName(p,"TestPackage2");
	Model.getCoreHelper().setVisibility(p,Model.getVisibilityKind().getProtected());
	Model.getCoreHelper().setSpecification(p,false);
	Model.getExtensionMechanismsHelper().setValueOfTag
	(Model.getFacade().getTaggedValue(p,"TVKey"),"TVNewValue");	
	Model.getExtensionMechanismsHelper().setStereoType(p,st);	
	assertEquals("TestPackage", Model.getFacade().getName(c));
	assertTrue(Model.getFacade().getVisibility(c)  == Model.getVisibilityKind().getPublic());
	assertTrue(Model.getFacade().isSpecification(c));
	assertEquals("TVValue", Model.getFacade().getTaggedValue(c,"TVKey"));

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

	s = Model.getExtensionMechanismsFactory()
	        .buildStereotype(null, m1);
	Model.getCoreHelper().addOwnedElement(m1,s);

	st = Model.getExtensionMechanismsFactory()
	        .buildStereotype("sttStT", m1);
	Model.getExtensionMechanismsHelper().setBaseClass(st,"Stereotype");
	Model.getCoreHelper().addOwnedElement(m1,st);

	// See if we can copy a stereotype right off
	o = helper.copy(s, m2);
	assertNotNull(o);
	assertTrue(o.getClass() == s.getClass());
	c = o;
	assertTrue(Model.getFacade().getNamespace(c) == m2);
	checkStereotypeCopy(s, c);

	// Change things
	Model.getCoreHelper().setName(s,"TestStereotype");
	Model.getCoreHelper().setVisibility(s,Model.getVisibilityKind().getPublic());
	Model.getCoreHelper().setSpecification(s,true);
	Model.getExtensionMechanismsHelper().setValueOfTag
	(Model.getFacade().getTaggedValue(s,"TVKey"),"TVNewValue");	
	Model.getExtensionMechanismsHelper().setBaseClass(st,"ModelElement");	
	Model.getExtensionMechanismsHelper().setIcon(s,"Icon1");
	c = helper.copy(s, m2);
	checkStereotypeCopy(s, c);

	// Assert that the copy isn't modified when the source changes
	Model.getCoreHelper().setName(s,"TestStereotype2");
	Model.getCoreHelper().setVisibility(s,Model.getVisibilityKind().getProtected());
	Model.getCoreHelper().setSpecification(s,false);
	Model.getExtensionMechanismsHelper().setValueOfTag
	(Model.getFacade().getTaggedValue(s,"TVKey"),"TVNewValue");	
	Model.getExtensionMechanismsHelper().setStereoType(s,st);		
	Model.getExtensionMechanismsHelper().setBaseClass(st,"ClassifierRole");	
	Model.getExtensionMechanismsHelper().setIcon(s,"Icon2");
	assertEquals("TestStereotype", Model.getFacade().getName(c));
	assertTrue(Model.getFacade().getVisibility(c)  == Model.getVisibilityKind().getPublic());
	assertTrue(Model.getFacade().isSpecification(c));
	assertEquals("ModelElement", Model.getFacade().getBaseClass(c));
	assertEquals("Icon1", Model.getFacade().getIcon(c));
	assertEquals("TVValue", Model.getFacade().getTaggedValue(c,"TVKey"));

	// See if the other copy was just a lucky shot
	c = helper.copy(s, m2);
	checkStereotypeCopy(s, c);

	// See if two copies look like copies of eachother
	c2 = helper.copy(s, m2);
	checkStereotypeCopy(c, c2);
    }

    private void checkBaseCopy(Object b1,Object b2) {
    }

    private void checkClassCopy(Object c1, Object c2) {
	checkClassifierCopy(c1, c2);

	assertTrue(Model.getFacade().isActive(c1) == Model.getFacade().isActive(c2));
    }

    private void checkClassifierCopy(Object c1, Object c2) {
	checkNamespaceCopy(c1, c2);
	checkGeneralizableElementCopy(c1, c2);
    }

    private void checkDataTypeCopy(Object d1, Object d2) {
	checkClassifierCopy(d1, d2);
    }

    private void checkElementCopy(Object e1, Object e2) {
	checkBaseCopy(e1, e2);
    }

    private void checkGeneralizableElementCopy(
    		Object e1,
    		Object e2) {
	checkModelElementCopy(e1, e2);

	assertTrue(Model.getFacade().isAbstract(e1) == Model.getFacade().isAbstract(e2));
	assertTrue(Model.getFacade().isLeaf(e1) == Model.getFacade().isLeaf(e2));
	assertTrue(Model.getFacade().isRoot(e1) == Model.getFacade().isRoot(e2));
    }

    private void checkInterfaceCopy(Object i1, Object i2) {
	checkClassifierCopy(i1, i2);
    }

    private void checkModelElementCopy(Object e1, Object e2) {
	checkElementCopy(e1, e2);
	if (Model.getFacade().getName(e1) == null) {
	    assertNull(Model.getFacade().getName(e2));
	} else {
	    assertEquals(Model.getFacade().getName(e1), Model.getFacade().getName(e2));
	}
	assertTrue(Model.getFacade().getVisibility(e1) == Model.getFacade().getVisibility(e2));
	assertTrue(Model.getFacade().isSpecification(e1) == Model.getFacade().isSpecification(e2));

	assertTrue(Model.getFacade().getTaggedValuesCollection(e1).size() == Model.getFacade().getTaggedValuesCollection(e2).size());
	Iterator it = Model.getFacade().getTaggedValues(e1);
	while (it.hasNext()) {
	    Object tv = it.next();
	    //TODO: not sure if we must use getTag here
	    Object e2Tv = Model.getFacade().getTaggedValue(e2,Model.getFacade().getTag(tv));
	    if (Model.getFacade().getValueOfTag(tv)==null) {
	        assertNull(Model.getFacade().getValueOfTag(e2Tv));
	    } else {
	        assertEquals(Model.getFacade().getValueOfTag(tv), 
	        		Model.getFacade().getValueOfTag(e2Tv));
	    }
	}

	if (Model.getFacade().getStereotypes(e1)==null) {
	    assertNull(Model.getFacade().getStereotypes(e2));
	} else if (Model.getFacade().getStereotypes(e1).isEmpty()) {
	    assertTrue(Model.getFacade().getStereotypes(e2).isEmpty());
	} else {
	    assertTrue(!Model.getFacade().getStereotypes(e2).isEmpty());
	    Object firstStereoOfe1 = Model.getFacade().getStereotypes(e1).iterator().next();
	    Object firstStereoOfe2 = Model.getFacade().getStereotypes(e2).iterator().next();
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
	if (Model.getFacade().getBaseClass(s1) == null) {
	    assertNull(Model.getFacade().getBaseClass(s2));
	} else {
	    assertEquals(Model.getFacade().getBaseClass(s1), Model.getFacade().getBaseClass(s2));
	}

	if (Model.getFacade().getIcon(s1) == null) {
	    assertNull(Model.getFacade().getIcon(s2));
	} else {
	    assertEquals(Model.getFacade().getIcon(s1), Model.getFacade().getIcon(s2));
	}

	// TODO: constraints
	// TODO: required tags
    }
}
