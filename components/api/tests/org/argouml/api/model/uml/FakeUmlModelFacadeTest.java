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

package org.argouml.api.model.uml;

import org.argouml.api.FacadeManager;

import junit.framework.TestCase;

/**
 * @author Thierry
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FakeUmlModelFacadeTest extends TestCase
{

	private UmlModelFacade facade;

	/**
	 * Constructor for FakeUmlModelFacadeTest.
	 * @param arg0
	 */
	public FakeUmlModelFacadeTest(String arg0)
	{
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();

		Object o = FacadeManager.getUmlFacade();
		assertNotNull("Unable to get UML Model facade", o);
		assertEquals("Didn't get the correct UML Model facade",
					 o.getClass(), FakeUmlModelFacade.class);
		facade = (UmlModelFacade)o;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testIsAAbstraction()
	{
		try {
			facade.isAAbstraction(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) { }
	}

	public void testIsAAssociation()
	{
		try {
			facade.isAAssociation(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAAssociationEnd()
	{
		try {
			facade.isAAssociationEnd(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAAssociationRole()
	{
		try {
			facade.isAAssociationRole(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAbstract()
	{
		try {
			facade.isAbstract(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsABase()
	{
		try {
			facade.isABase(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAClass()
	{
		try {
			facade.isAClass(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAClassifier()
	{
		try {
			facade.isAClassifier(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAComment()
	{
		try {
			facade.isAComment(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAComponent()
	{
		try {
			facade.isAComponent(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAComponentInstance()
	{
		try {
			facade.isAComponentInstance(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsADataType()
	{
		try {
			facade.isADataType(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsADependency()
	{
		try {
			facade.isADependency(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsACompositeState()
	{
		try {
			facade.isACompositeState(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAElement()
	{
		try {
			facade.isAElement(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAExpression()
	{
		try {
			facade.isAExpression(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAExtensionPoint()
	{
		try {
			facade.isAExtensionPoint(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAFeature()
	{
		try {
			facade.isAFeature(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAGeneralizableElement()
	{
		try {
			facade.isAGeneralizableElement(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAGeneralization()
	{
		try {
			facade.isAGeneralization(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAInstance()
	{
		try {
			facade.isAInstance(new Object());
			fail ("Didn't throw RuntimeException");
		}
		catch (RuntimeException re) {
		}
	}

	public void testIsAInteraction()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAInterface()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsALink()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAMethod()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAModel()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAModelElement()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsANamespace()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsANode()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsANodeInstance()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAOperation()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAObject()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAPermission()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAPackage()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAReception()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsARelationship()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAStateMachine()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAStateVertex()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAStereotype()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAStructuralFeature()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsATaggedValue()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsATransition()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsAUseCase()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsChangeable()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsClassifierScope()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsConstructor()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsComposite()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsInitialized()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsInstanceScope()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsLeaf()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsNavigable()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsPrimaryObject()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsPrivate()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsRealize()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsReturn()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsSingleton()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsTop()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsType()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testIsUtility()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetAssociationEnd()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetAssociationEnds()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetAttributes()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetBehaviors()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetBody()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetChild()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetChildren()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetClientDependencies()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetConcurrency()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetConnections()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetFeatures()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetGeneralization()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetGeneralizations()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetIncomings()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetMessages()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetContainer()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetContext()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetNamespace()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetOperations()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetOperationsInh()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetOppositeEnd()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetOutgoings()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetOtherAssociationEnds()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetOwnedElements()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetParameter()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetParameters()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetParent()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetResidents()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetSource()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetSpecializations()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetStereoType()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetSubvertices()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetSupplierDependencies()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetType()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetTarget()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetUpper()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetTransitions()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetStructuralFeatures()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetSpecifications()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetSuppliers()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetAssociatedClasses()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetName()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetOwner()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetTaggedValues()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetTaggedValue()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetTagOfTag()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetValueOfTag()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetUUID()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testLookupIn()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testAddFeature()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testAddMethod()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testAddOwnedElement()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testAddSupplier()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testAddClient()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testRemoveClientDependency()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testRemoveFeature()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testRemoveParameter()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetBody()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetInitialValue()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetMultiplicity()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetName()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetNamespace()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetNavigable()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetVisibility()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetOwnerScope()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetTargetScope()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetConcurrency()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetChangeable()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetAbstract()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetLeaf()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetRoot()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetKindToIn()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetKindToReturn()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetType()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetTaggedValue()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetValueOfTag()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testSetStereotype()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testAddConstraint()
	{
		// TODO Make sure it throws RuntimeException
	}

	public void testGetUMLClassName()
	{
		// TODO Make sure it throws RuntimeException
	}

}
