// Copyright (c) 2003 The Regents of the University of California. All
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

// $Id$

package org.argouml.model.uml;

import java.util.Iterator;

import junit.framework.TestCase;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MElement;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org
	.argouml
	.model
	.uml
	.foundation
	.extensionmechanisms
	.ExtensionMechanismsFactory;
import org.argouml.model.uml.modelmanagement.ModelManagementFactory;

public class TestCopyHelper extends TestCase {
	MModel m1 = ModelManagementFactory.getFactory().createModel();
	MModel m2 = ModelManagementFactory.getFactory().createModel();
	CopyHelper helper = CopyHelper.getHelper();

	public TestCopyHelper(String name) {
		super(name);
	}

	public void testCopyClass() {
		Object o;
		MClass c;
		MClass c2;
		MClass k;
		MStereotype st;

		k = CoreFactory.getFactory().createClass();
		m1.addOwnedElement(k);

		st = ExtensionMechanismsFactory.getFactory().createStereotype();
		st.setName("clsStT");
		st.setBaseClass("Class");
		m1.addOwnedElement(st);

		// See if we can copy a class right off
		o = helper.copy(k, m2);
		assertTrue(o != null);
		assertTrue(o.getClass() == k.getClass());
		c = (MClass) o;
		assertTrue(c.getNamespace() == m2);
		checkClassCopy(k, c);

		// Change things
		k.setName("TestClass");
		k.setVisibility(MVisibilityKind.PUBLIC);
		k.setSpecification(true);
		k.setTaggedValue("TVKey", "TVValue");
		k.setActive(false);
		c = (MClass) helper.copy(k, m2);
		checkClassCopy(k, c);

		// Assert that the copy isn't modified when the source changes
		k.setName("TestClass2");
		k.setVisibility(MVisibilityKind.PROTECTED);
		k.setSpecification(false);
		k.setTaggedValue("TVKey", "TVNewValue");
		k.setActive(true);
		k.setStereotype(st);
		assertTrue("TestClass".equals(c.getName()));
		assertTrue(c.getVisibility() == MVisibilityKind.PUBLIC);
		assertTrue(c.isSpecification() == true);
		assertTrue(c.isActive() == false);
		assertTrue("TVValue".equals(c.getTaggedValue("TVKey")));

		// See if the other copy was just a lucky shot
		c = (MClass) helper.copy(k, m2);
		checkClassCopy(k, c);

		// See if two copies look like copies of eachother
		c2 = (MClass) helper.copy(k, m2);
		checkClassCopy(c, c2);
	}

	public void testCopyDataType() {
		Object o;
		MDataType c;
		MDataType c2;
		MDataType d;
		MStereotype st;

		d = CoreFactory.getFactory().createDataType();
		m1.addOwnedElement(d);

		st = ExtensionMechanismsFactory.getFactory().createStereotype();
		st.setName("dttStT");
		st.setBaseClass("DataType");
		m1.addOwnedElement(st);

		// See if we can copy a datatype right off
		o = helper.copy(d, m2);
		assertTrue(o != null);
		assertTrue(o.getClass() == d.getClass());
		c = (MDataType) o;
		assertTrue(c.getNamespace() == m2);
		checkDataTypeCopy(d, c);

		// Change things
		d.setName("TestDataType");
		d.setVisibility(MVisibilityKind.PUBLIC);
		d.setSpecification(true);
		d.setTaggedValue("TVKey", "TVValue");
		c = (MDataType) helper.copy(d, m2);
		checkDataTypeCopy(d, c);

		// Assert that the copy isn't modified when the source changes
		d.setName("TestDataType2");
		d.setVisibility(MVisibilityKind.PROTECTED);
		d.setSpecification(false);
		d.setTaggedValue("TVKey", "TVNewValue");
		d.setStereotype(st);
		assertTrue("TestDataType".equals(c.getName()));
		assertTrue(c.getVisibility() == MVisibilityKind.PUBLIC);
		assertTrue(c.isSpecification() == true);
		assertTrue("TVValue".equals(c.getTaggedValue("TVKey")));

		// See if the other copy was just a lucky shot
		c = (MDataType) helper.copy(d, m2);
		checkDataTypeCopy(d, c);

		// See if two copies look like copies of eachother
		c2 = (MDataType) helper.copy(d, m2);
		checkDataTypeCopy(c, c2);
	}

	public void testCopyInterface() {
		Object o;
		MInterface c;
		MInterface c2;
		MInterface i;
		MStereotype st;

		i = CoreFactory.getFactory().createInterface();
		m1.addOwnedElement(i);

		st = ExtensionMechanismsFactory.getFactory().createStereotype();
		st.setName("intStT");
		st.setBaseClass("Interface");
		m1.addOwnedElement(st);

		// See if we can copy an interface right off
		o = helper.copy(i, m2);
		assertTrue(o != null);
		assertTrue(o.getClass() == i.getClass());
		c = (MInterface) o;
		assertTrue(c.getNamespace() == m2);
		checkInterfaceCopy(i, c);

		// Change things
		i.setName("TestInterface");
		i.setVisibility(MVisibilityKind.PUBLIC);
		i.setSpecification(true);
		i.setTaggedValue("TVKey", "TVValue");
		c = (MInterface) helper.copy(i, m2);
		checkInterfaceCopy(i, c);

		// Assert that the copy isn't modified when the source changes
		i.setName("TestInterface2");
		i.setVisibility(MVisibilityKind.PROTECTED);
		i.setSpecification(false);
		i.setTaggedValue("TVKey", "TVNewValue");
		i.setStereotype(st);
		assertTrue("TestInterface".equals(c.getName()));
		assertTrue(c.getVisibility() == MVisibilityKind.PUBLIC);
		assertTrue(c.isSpecification() == true);
		assertTrue("TVValue".equals(c.getTaggedValue("TVKey")));

		// See if the other copy was just a lucky shot
		c = (MInterface) helper.copy(i, m2);
		checkInterfaceCopy(i, c);

		// See if two copies look like copies of eachother
		c2 = (MInterface) helper.copy(i, m2);
		checkInterfaceCopy(c, c2);
	}

	public void testCopyPackage() {
		Object o;
		MPackage c;
		MPackage c2;
		MPackage p;
		MStereotype st;

		p = ModelManagementFactory.getFactory().createPackage();
		m1.addOwnedElement(p);

		st = ExtensionMechanismsFactory.getFactory().createStereotype();
		st.setName("pkgStT");
		st.setBaseClass("Package");
		m1.addOwnedElement(st);

		// See if we can copy a package right off
		o = helper.copy(p, m2);
		assertTrue(o != null);
		assertTrue(o.getClass() == p.getClass());
		c = (MPackage) o;
		assertTrue(c.getNamespace() == m2);
		checkPackageCopy(p, c);

		// Change things
		p.setName("TestPackage");
		p.setVisibility(MVisibilityKind.PUBLIC);
		p.setSpecification(true);
		p.setTaggedValue("TVKey", "TVValue");
		c = (MPackage) helper.copy(p, m2);
		checkPackageCopy(p, c);

		// Assert that the copy isn't modified when the source changes
		p.setName("TestPackage2");
		p.setVisibility(MVisibilityKind.PROTECTED);
		p.setSpecification(false);
		p.setTaggedValue("TVKey", "TVNewValue");
		p.setStereotype(st);
		assertTrue("TestPackage".equals(c.getName()));
		assertTrue(c.getVisibility() == MVisibilityKind.PUBLIC);
		assertTrue(c.isSpecification() == true);
		assertTrue("TVValue".equals(c.getTaggedValue("TVKey")));

		// See if the other copy was just a lucky shot
		c = (MPackage) helper.copy(p, m2);
		checkPackageCopy(p, c);

		// See if two copies look like copies of eachother
		c2 = (MPackage) helper.copy(p, m2);
		checkPackageCopy(c, c2);
	}

	public void testCopyStereotype() {
		Object o;
		MStereotype c;
		MStereotype c2;
		MStereotype s;
		MStereotype st;

		s = ExtensionMechanismsFactory.getFactory().createStereotype();
		m1.addOwnedElement(s);

		st = ExtensionMechanismsFactory.getFactory().createStereotype();
		st.setName("sttStT");
		st.setBaseClass("Stereotype");
		m1.addOwnedElement(st);

		// See if we can copy a stereotype right off
		o = helper.copy(s, m2);
		assertTrue(o != null);
		assertTrue(o.getClass() == s.getClass());
		c = (MStereotype) o;
		assertTrue(c.getNamespace() == m2);
		checkStereotypeCopy(s, c);

		// Change things
		s.setName("TestStereotype");
		s.setVisibility(MVisibilityKind.PUBLIC);
		s.setSpecification(true);
		s.setTaggedValue("TVKey", "TVValue");
		s.setBaseClass("ModelElement");
		s.setIcon("Icon1");
		c = (MStereotype) helper.copy(s, m2);
		checkStereotypeCopy(s, c);

		// Assert that the copy isn't modified when the source changes
		s.setName("TestStereotype2");
		s.setVisibility(MVisibilityKind.PROTECTED);
		s.setSpecification(false);
		s.setTaggedValue("TVKey", "TVNewValue");
		s.setStereotype(st);
		s.setBaseClass("ClassifierRole");
		s.setIcon("Icon2");
		assertTrue("TestStereotype".equals(c.getName()));
		assertTrue(c.getVisibility() == MVisibilityKind.PUBLIC);
		assertTrue(c.isSpecification() == true);
		assertTrue("ModelElement".equals(c.getBaseClass()));
		assertTrue("Icon1".equals(c.getIcon()));
		assertTrue("TVValue".equals(c.getTaggedValue("TVKey")));

		// See if the other copy was just a lucky shot
		c = (MStereotype) helper.copy(s, m2);
		checkStereotypeCopy(s, c);

		// See if two copies look like copies of eachother
		c2 = (MStereotype) helper.copy(s, m2);
		checkStereotypeCopy(c, c2);
	}

	private void checkBaseCopy(MBase b1, MBase b2) {
	}

	private void checkClassCopy(MClass c1, MClass c2) {
		checkClassifierCopy(c1, c2);

		assertTrue(c1.isActive() == c2.isActive());
	}

	private void checkClassifierCopy(MClassifier c1, MClassifier c2) {
		checkNamespaceCopy(c1, c2);
		checkGeneralizableElementCopy(c1, c2);
	}

	private void checkDataTypeCopy(MDataType d1, MDataType d2) {
		checkClassifierCopy(d1, d2);
	}

	private void checkElementCopy(MElement e1, MElement e2) {
		checkBaseCopy(e1, e2);
	}

	private void checkGeneralizableElementCopy(
		MGeneralizableElement e1,
		MGeneralizableElement e2) {
		checkModelElementCopy(e1, e2);

		assertTrue(e1.isAbstract() == e2.isAbstract());
		assertTrue(e1.isLeaf() == e2.isLeaf());
		assertTrue(e1.isRoot() == e2.isRoot());
	}

	private void checkInterfaceCopy(MInterface i1, MInterface i2) {
		checkClassifierCopy(i1, i2);
	}

	private void checkModelElementCopy(MModelElement e1, MModelElement e2) {
		checkElementCopy(e1, e2);
		assertTrue(
			e1.getName() == null
				&& e2.getName() == null
				|| e1.getName() != null
				&& e1.getName().equals(e2.getName()));
		assertTrue(e1.getVisibility() == e2.getVisibility());
		assertTrue(e1.isSpecification() == e2.isSpecification());

		assertTrue(e1.getTaggedValues().size() == e2.getTaggedValues().size());
		Iterator it = e1.getTaggedValues().iterator();
		while (it.hasNext()) {
			MTaggedValue tv = (MTaggedValue) it.next();
			assertTrue(
				tv.getValue() == null
					&& e2.getTaggedValue(tv.getTag()) == null
					|| tv.getValue() != null
					&& tv.getValue().equals(e2.getTaggedValue(tv.getTag())));
		}

		if (e1.getStereotype() != null) {
			assertTrue(e2.getStereotype() != null);
			assertTrue(e2.getStereotype().getName() != null);
			assertTrue(
				e1.getStereotype().getName().equals(
					e2.getStereotype().getName()));
		} else {
			assertTrue(e2.getStereotype() == null);
		}
	}

	private void checkNamespaceCopy(MNamespace n1, MNamespace n2) {
		checkModelElementCopy(n1, n2);
	}

	private void checkPackageCopy(MPackage p1, MPackage p2) {
		checkNamespaceCopy(p1, p2);
	}

	private void checkStereotypeCopy(MStereotype s1, MStereotype s2) {
		checkGeneralizableElementCopy(s1, s2);
		assertTrue(
			s1.getBaseClass() == null
				&& s2.getBaseClass() == null
				|| s1.getBaseClass() != null
				&& s1.getBaseClass().equals(s2.getBaseClass()));
		assertTrue(
			s1.getIcon() == null
				&& s2.getIcon() == null
				|| s1.getIcon() != null
				&& s1.getIcon().equals(s2.getIcon()));
		// TODO: constraints
		// TODO: reuired tags
	}

	/* (non-Javadoc)
	     * @see junit.framework.TestCase#setUp()
	     */
	protected void setUp() throws Exception {
		super.setUp();
		ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false);
	}
}
