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

import java.util.Collection;
import java.util.Iterator;

import org.argouml.api.FacadeManager;

/**
 * Fake implementatino of UmlModelFacade.
 * 
 * All methods are stubs that throw RuntimeException.
 */

public final class FakeUmlModelFacade
implements UmlModelFacade
{
    private String NOT_SET_MESSAGE = "System property '" +
                FacadeManager.UML_MODEL_FACADE_PROPERTY + "' was not set";

	public boolean isAAbstraction(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAActor(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAAssociation(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAAssociationEnd(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAAssociationRole(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAbstract(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isABase(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAClass(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAClassifier(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAComment(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAComponent(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAComponentInstance(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isADataType(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isADependency(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isACompositeState(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAElement(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAExpression(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAExtensionPoint(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAFeature(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAGeneralizableElement(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAGeneralization(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAInstance(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAInteraction(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAInterface(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isALink(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAMethod(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAModel(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAModelElement(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isANamespace(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isANode(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isANodeInstance(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAOperation(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAObject(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAPermission(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAPackage(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAReception(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isARelationship(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAStateMachine(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAStateVertex(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAStereotype(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAStructuralFeature(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isATaggedValue(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isATransition(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isAUseCase(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isChangeable(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isClassifierScope(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isConstructor(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isComposite(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isInitialized(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isInstanceScope(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isLeaf(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isNavigable(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isPrimaryObject(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isPrivate(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isRealize(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isReturn(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isSingleton(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isTop(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isType(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public boolean isUtility(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getAssociationEnd(Object type, Object assoc)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getAssociationEnds(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getAttributes(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getBehaviors(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getBody(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getChild(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getChildren(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Iterator getClientDependencies(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public short getConcurrency(Object o)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Iterator getConnections(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getFeatures(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getGeneralization(Object child, Object parent)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Iterator getGeneralizations(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getIncomings(Object stateVertex)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getMessages(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getContainer(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getContext(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getNamespace(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getOperations(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Iterator getOperationsInh(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getOppositeEnd(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getOutgoings(Object stateVertex)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getOtherAssociationEnds(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getOwnedElements(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getParameter(Object op, int n)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Iterator getParameters(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getParent(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getResidents(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getSource(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Iterator getSpecializations(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getStereoType(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getSubvertices(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Iterator getSupplierDependencies(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getType(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getTarget(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public int getUpper(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getTransitions(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getStructuralFeatures(Object classifier)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getSpecifications(Object cls)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getSuppliers(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Collection getAssociatedClasses(Object o)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public String getName(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getOwner(Object f)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Iterator getTaggedValues(Object modelElement)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object getTaggedValue(Object modelElement, String name)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public String getTagOfTag(Object tv)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public String getValueOfTag(Object tv)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public String getUUID(Object base)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public Object lookupIn(Object o, String name)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void addFeature(Object cls, Object f)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void addMethod(Object o, Object m)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void addOwnedElement(Object ns, Object me)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void addSupplier(Object a, Object cls)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void addClient(Object a, Object cls)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void removeClientDependency(Object o, Object dep)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void removeFeature(Object cls, Object feature)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void removeParameter(Object o, Object p)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setBody(Object m, Object expr)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setInitialValue(Object at, Object expr)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setMultiplicity(Object o, String mult)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setName(Object o, String name)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setNamespace(Object o, Object ns)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setNavigable(Object o, boolean flag)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setVisibility(Object o, short v)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setOwnerScope(Object f, short os)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setTargetScope(Object ae, short ts)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setConcurrency(Object o, short c)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setChangeable(Object o, boolean flag)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setAbstract(Object o, boolean flag)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setLeaf(Object o, boolean flag)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setRoot(Object o, boolean flag)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setKindToIn(Object p)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setKindToReturn(Object p)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setType(Object p, Object cls)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setTaggedValue(Object o, String tag, String value)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setValueOfTag(Object tv, String value)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void setStereotype(Object m, Object stereo)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public void addConstraint(Object me, Object mc)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}

	public String getUMLClassName(Object handle)
	{
		throw new RuntimeException(NOT_SET_MESSAGE);
	}
    
}
