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

package org.argouml.model.uml;

import java.util.Collection;

import org.argouml.api.FacadeManager;
import org.argouml.api.InvalidObjectRequestException;
import org.argouml.api.model.uml.UmlModelFacade;

import junit.framework.TestCase;

/**
 * @author Thierry
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NsumlModelFacadeTest extends TestCase
{

	private UmlModelFacade facade;

	/**
	 * Constructor for FakeUmlModelFacadeTest.
	 * @param arg0
	 */
	public NsumlModelFacadeTest(String arg0)
	{
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
 	    System.setProperty(FacadeManager.UML_MODEL_FACADE_PROPERTY,
						   NsumlModelFacade.class.getName());

		Object o = FacadeManager.getUmlFacade();
		assertNotNull("Unable to get UML Model facade", o);
		assertEquals("Didn't get the correct UML Model facade",
					 o.getClass(), NsumlModelFacade.class);
		facade = (UmlModelFacade)o;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testIsAAbstraction() {

			boolean b = facade.isAAbstraction(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAAssociation() {

			boolean b = facade.isAAssociation(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAAssociationEnd() {
			boolean b = facade.isAAssociationEnd(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAAssociationRole() {
			boolean b = facade.isAAssociationRole(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAbstract() {
		try {
			boolean b = facade.isAbstract(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsABase() {
			boolean b = facade.isABase(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAClass() {

			boolean b = facade.isAClass(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAClassifier() {

			boolean b = facade.isAClassifier(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAComment() {

			boolean b = facade.isAComment(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAComponent() {

			boolean b = facade.isAComponent(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAComponentInstance() {

			boolean b = facade.isAComponentInstance(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsADataType() {
			boolean b = facade.isADataType(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsADependency() {
			boolean b = facade.isADependency(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsACompositeState() {
			boolean b = facade.isACompositeState(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAElement() {
			boolean b = facade.isAElement(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAExpression() {
			boolean b = facade.isAExpression(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAExtensionPoint() {
			boolean b = facade.isAExtensionPoint(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAFeature() {
			boolean b = facade.isAFeature(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAGeneralizableElement() {
			boolean b = facade.isAGeneralizableElement(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAGeneralization() {
			boolean b = facade.isAGeneralization(new Object());
			assertEquals("passed an object", b, false);

	}

	public void testIsAInstance() {
			boolean b = facade.isAInstance(new Object());
			assertEquals("passed an object", b, false);

	}

	public void testIsAInteraction() {
			boolean b = facade.isAInteraction(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAInterface() {
			boolean b = facade.isAInterface(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsALink() {
			boolean b = facade.isALink(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAMethod() {
			boolean b = facade.isAMethod(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAModel() {
			boolean b = facade.isAModel(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAModelElement() {
			boolean b = facade.isAModelElement(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsANamespace() {
			boolean b = facade.isANamespace(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsANode() {
			boolean b = facade.isANode(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsANodeInstance() {
			boolean b = facade.isANodeInstance(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAOperation() {
			boolean b = facade.isAOperation(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAObject() {
			boolean b = facade.isAObject(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAPermission() {
			boolean b = facade.isAPermission(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAPackage() {
			boolean b = facade.isAPackage(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAReception() {
			boolean b = facade.isAReception(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsARelationship() {
			boolean b = facade.isARelationship(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAStateMachine() {
			boolean b = facade.isAStateMachine(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAStateVertex() {
			boolean b = facade.isAStateVertex(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAStereotype() {
			boolean b = facade.isAStereotype(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAStructuralFeature() {
			boolean b = facade.isAStructuralFeature(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsATaggedValue() {
			boolean b = facade.isATaggedValue(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsATransition() {
			boolean b = facade.isATransition(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsAUseCase() {
			boolean b = facade.isAUseCase(new Object());
			assertEquals("passed an object", b, false);
	}

	public void testIsChangeable() {
		try {

			boolean b = facade.isChangeable(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsClassifierScope() {
		try {

			boolean b = facade.isClassifierScope(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsConstructor() {
		try {

			boolean b = facade.isConstructor(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsComposite() {
		try {

			boolean b = facade.isComposite(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsInitialized() {
		try {

			boolean b = facade.isInitialized(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsInstanceScope() {
		try {

			boolean b = facade.isInstanceScope(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsLeaf() {
		try {

			boolean b = facade.isLeaf(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsNavigable() {
		try {

			boolean b = facade.isNavigable(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsPrimaryObject() {
		try {

			boolean b = facade.isPrimaryObject(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsPrivate() {
		try {

			boolean b = facade.isPrivate(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsRealize() {
		try {

			boolean b = facade.isRealize(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsReturn() {
		try {

			boolean b = facade.isReturn(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsSingleton() {
		try {

			boolean b = facade.isSingleton(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsTop() {
		try {

			boolean b = facade.isTop(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsType() {
		try {

			boolean b = facade.isType(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testIsUtility() {
		try {

			boolean b = facade.isUtility(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetAssociationEnd() {
		try {

			Object o = facade.getAssociationEnd(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetAssociationEnds() {
		try {

			Object o = facade.getAssociationEnds(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetAttributes() {
		try {

			Object o = facade.getAttributes(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetBehaviors() {
		try {

			Object o = facade.getBehaviors(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetBody() {
		try {

			Object o = facade.getBody(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetChild() {
		try {

			Object o = facade.getChild(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetChildren() {
		try {

			Object o = facade.getChildren(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetClientDependencies() {
		try {

			Object o = facade.getClientDependencies(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetConcurrency() {
		try {

			int o = facade.getConcurrency(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetConnections() {
		try {

			Object o = facade.getConnections(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetFeatures() {
		try {

			Collection c = facade.getFeatures(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetGeneralization() {
		try {

			Object o = facade.getGeneralization(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetGeneralizations() {
		try {

			Object o = facade.getGeneralizations(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetIncomings() {
		try {

			Object o = facade.getIncomings(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetMessages() {
		try {

			Object o = facade.getMessages(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetContainer() {
		try {

			Object o = facade.getContainer(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetContext() {
		try {

			Object o = facade.getContext(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetNamespace() {
		try {

			Object o = facade.getNamespace(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetOperations() {
		try {

			Object o = facade.getOperations(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetOperationsInh() {
		try {

			Object o = facade.getOperationsInh(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetOppositeEnd() {
		try {

			Object o = facade.getOppositeEnd(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetOutgoings() {
		try {

			Object o = facade.getOutgoings(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetOtherAssociationEnds() {
		try {

			Object o = facade.getOtherAssociationEnds(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetOwnedElements() {
		try {

			Object o = facade.getOwnedElements(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetParameter() {
		try {

			Object o = facade.getParameter(new Object(), 0);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetParameters() {
		try {

			Object o = facade.getParameters(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetParent() {
		try {

			Object o = facade.getParent(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetResidents() {
		try {

			Object o = facade.getResidents(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetSource() {
		try {

			Object o = facade.getSource(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetSpecializations() {
		try {

			Object o = facade.getSpecializations(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetStereoType() {
		try {

			Object o = facade.getStereoType(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetSubvertices() {
		try {

			Object o = facade.getSubvertices(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetSupplierDependencies() {
		try {

			Object o = facade.getSupplierDependencies(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetType() {
		try {

			Object o = facade.getType(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetTarget() {
		try {

			Object o = facade.getTarget(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetUpper() {
		try {

			int o = facade.getUpper(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetTransitions() {
		try {

			Object o = facade.getTransitions(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetStructuralFeatures() {
		try {

			Collection c = facade.getStructuralFeatures(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetSpecifications() {
		try {

			Collection c = facade.getSpecifications(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetSuppliers() {
		try {

			Collection c = facade.getSuppliers(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetAssociatedClasses() {
		try {

			Collection c = facade.getAssociatedClasses(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetName() {
		try {

			Object o = facade.getName(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetOwner() {
		try {

			Object o = facade.getOwner(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetTaggedValues() {
		try {

			Object o = facade.getTaggedValues(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetTaggedValue() {
		try {

			Object o = facade.getTaggedValue(new Object(), "");
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetTagOfTag() {
		try {

			Object o = facade.getTagOfTag(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetValueOfTag() {
		try {

			Object o = facade.getValueOfTag(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetUUID() {
		try {

			Object o = facade.getUUID(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testLookupIn() {
		try {

			Object o = facade.lookupIn(new Object(), "");
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testAddFeature() {
		try {

			facade.addFeature(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testAddMethod() {
		try {

			facade.addMethod(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testAddOwnedElement() {
		try {

			facade.addOwnedElement(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testAddSupplier() {
		try {

			facade.addSupplier(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testAddClient() {
		try {

			facade.addClient(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testRemoveClientDependency() {
		try {

			facade.removeClientDependency(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testRemoveFeature() {
		try {

			facade.removeFeature(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testRemoveParameter() {
		try {

			facade.removeParameter(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetBody() {
		try {

			facade.setBody(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetInitialValue() {
		try {

			facade.setInitialValue(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetMultiplicity() {
		try {

			facade.setMultiplicity(new Object(), "");
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetName() {
		try {

			facade.setName(new Object(), "");
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetNamespace() {
		try {

			facade.setNamespace(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetNavigable() {
		try {

			facade.setNavigable(new Object(), true);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetVisibility() {
		try {

			short concur = 0;
			facade.setVisibility(new Object(), concur);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetOwnerScope() {
		try {

			short concur = 0;
			facade.setOwnerScope(new Object(), concur);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetTargetScope() {
		try {

			short concur = 0;
			facade.setTargetScope(new Object(), concur);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetConcurrency() {
		try {

			short concur = 0;
			facade.setConcurrency(new Object(), concur);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetChangeable() {
		try {

			facade.setChangeable(new Object(), true);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetAbstract() {

		try {
			facade.setAbstract(new Object(), true);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetLeaf() {
		try {

			facade.setLeaf(new Object(), true);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetRoot() {
		try {

			facade.setRoot(new Object(), true);
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetKindToIn() {
		try {

			facade.setKindToIn(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetKindToReturn() {
		try {

			facade.setKindToReturn(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetType() {
		try {
			facade.setType(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetTaggedValue() {
		try {

			facade.setTaggedValue(new Object(), "", "");
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetValueOfTag() {
		try {

			facade.setValueOfTag(new Object(), "");
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testSetStereotype() {
		try {

			facade.setStereotype(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testAddConstraint() {
		try {

			facade.addConstraint(new Object(), new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}

	public void testGetUMLClassName() {
		try {

			Object o = facade.getUMLClassName(new Object());
			fail("should have thrown InvalidObjectRequestException");
		}
		catch (InvalidObjectRequestException e) {
		}
	}
}
