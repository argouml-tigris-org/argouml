/*
 * Created on 25.08.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tudresden.ocl20.integration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import javax.jmi.reflect.RefObject;

import tudresden.ocl20.core.RepositoryManager;
import javax.jmi.reflect.RefBaseObject;
import tudresden.ocl20.core.jmi.uml15.core.Association;
import tudresden.ocl20.core.jmi.uml15.core.AssociationEnd;
import tudresden.ocl20.core.jmi.uml15.core.DataType;
import tudresden.ocl20.core.jmi.uml15.core.Enumeration;
import tudresden.ocl20.core.jmi.uml15.core.Operation;
import tudresden.ocl20.core.jmi.uml15.core.UmlClass;
import tudresden.ocl20.core.jmi.uml15.core.Classifier;
import tudresden.ocl20.core.jmi.uml15.core.CorePackage;
import tudresden.ocl20.core.jmi.uml15.core.Generalization;
import tudresden.ocl20.core.jmi.uml15.core.ModelElement;
import tudresden.ocl20.core.jmi.uml15.core.Namespace;
import tudresden.ocl20.core.jmi.uml15.datatypes.Multiplicity;
import tudresden.ocl20.core.jmi.uml15.datatypes.MultiplicityRange;
import tudresden.ocl20.core.jmi.uml15.datatypes.OrderingKind;
import tudresden.ocl20.core.jmi.uml15.datatypes.OrderingKindEnum;
import tudresden.ocl20.core.jmi.uml15.datatypes.ParameterDirectionKind;
import tudresden.ocl20.core.jmi.uml15.datatypes.ParameterDirectionKindEnum;
import tudresden.ocl20.core.jmi.uml15.datatypes.ScopeKind;
import tudresden.ocl20.core.jmi.uml15.datatypes.ScopeKindEnum;
import tudresden.ocl20.core.jmi.uml15.impl.uml15ocl.types.OclLibraryHelper;
import tudresden.ocl20.core.jmi.uml15.uml15.Uml15Package;

/**
 * Implementations of this class are used to access the repository of the CASE-Tools.
 * @author Mirko
 */
public abstract class ModelFacade 
{
	/**
	 * All instances of the class ModelFacade which are associated to an UML-Model in the MDR.
	 */
	private static HashMap<String, ModelFacade> instances = new HashMap<String, ModelFacade>();
	
	/**
	 * All associations between the elements in the MDR and the objects in the repository of a CASE-Tool.
	 */
	private static HashMap<String, Object> refObjects = new HashMap<String, Object>();
	
	/**
     * Returns the UML-Model in the MDR which is associated to the given ModelFacade.
     */
	private Uml15Package getModel(ModelFacade facade)
	{
		Iterator<String> it = instances.keySet().iterator();
		Uml15Package model = null;
		while (it.hasNext())
		{
			String mofID = it.next().toString();
			if (instances.get(mofID) != null)
				if (instances.get(mofID).equals(facade))
					model = (Uml15Package) RepositoryManager.getRepository().getElementByMofID(mofID);			
		}
		return model;
	}
	
	/**
     * Generates the association between an element in the MDR and object in the repository of a CASE-Tool.
     */
	public void addRefObject(String mofID, Object refObject)
	{
		refObjects.put(mofID, refObject);
	}
	
	/**
     * Returns all element in the MDR which are associated to the given object. 
     */
	private static List<RefBaseObject> getAllElement(Object refObject)
	{
		ArrayList<RefBaseObject> result = new ArrayList<RefBaseObject>();
		Iterator<String> it = refObjects.keySet().iterator();
		while (it.hasNext())
		{
			String mofID = it.next().toString();
			if (refObjects.get(mofID) != null)
				if (refObjects.get(mofID).equals(refObject))
					result.add(RepositoryManager.getRepository().getElementByMofID(mofID));			
		}
		return result;
	}
	
	/**
     * Deletes all collection types of an classifier in the OCL Standard Library.
     */
	private static void deleteCollectionTypes(Classifier c)
	{
		Object[] types = OclLibraryHelper.getInstance(c.refOutermostPackage()).findCollectionTypes(c).toArray();
		for (int i = 0; i < types.length; i++)
		{
			RefObject object = (RefObject) types[i];
			object.refDelete();
		}			
	}
	
	/**
     * Returns true if an object in the repository of the CASE-Tool is associated to the element in the MDR with the given mofID.
     */
	public static boolean isRepresentative(String mofID)
	{
		return refObjects.containsKey(mofID);
	}
	
	/**
     * Returns the object which is association to the element in the MDR with the given mofID.
     */
	public Object getRefObject(String mofID)
	{
		return refObjects.get(mofID);
	}
	
	/**
     * Returns the existing element in the MDR which is associated to the given object. If no instance
     * of the given class exists a new element will be created.
     */
	protected Object getElement(Classes elementClass, Object refObject)
	{
		Collection elements = getAllElement(refObject);
		Iterator it = elements.iterator();
		Object existingElement = null;
		while (it.hasNext())
		{
			existingElement = it.next();
			if (elementClass.getValue().isAssignableFrom(existingElement.getClass()))
			{
				return existingElement;
			}
		}			
		
		Uml15Package model = this.getModel(this);
		RefBaseObject newElement = null;
		
		if (elementClass.equals(Classes.umlClass))
			newElement = model.getCore().getUmlClass().createUmlClass();
		
		if (elementClass.equals(Classes.multiplicity))
			newElement = model.getDataTypes().getMultiplicity().createMultiplicity();
			
		if (elementClass.equals(Classes.method))
			newElement = model.getCore().getOperation().createOperation();
	       
		if (elementClass.equals(Classes.association))
			newElement = model.getCore().getAssociation().createAssociation();
	       	
		if (elementClass.equals(Classes.associationend))
			newElement = model.getCore().getAssociationEnd().createAssociationEnd();
	       
		if (elementClass.equals(Classes.attribute))
			newElement = model.getCore().getAttribute().createAttribute();
		
		if (elementClass.equals(Classes.parameter))
			newElement = model.getCore().getParameter().createParameter();
	     
		if (elementClass.equals(Classes.range))
			newElement = model.getDataTypes().getMultiplicityRange().createMultiplicityRange();
		
		if (elementClass.equals(Classes.umlPackage))
			newElement = model.getModelManagement().getPackage().createPackage();
		
		if (elementClass.equals(Classes.dataType))
			newElement = model.getCore().getDataType().createDataType();
		
		if (elementClass.equals(Classes.scopeKind_classifier))
			return ScopeKindEnum.SK_CLASSIFIER;
		
		if (elementClass.equals(Classes.scopeKind_instance))
			return ScopeKindEnum.SK_INSTANCE;
		
		if (elementClass.equals(Classes.orderingKind_ordered))
			return OrderingKindEnum.OK_ORDERED;
		
		if (elementClass.equals(Classes.orderingKind_unordered))
			return OrderingKindEnum.OK_UNORDERED;
	
		if (elementClass.equals(Classes.parameterDirectionKind_Return))
			return ParameterDirectionKindEnum.PDK_RETURN;
		
		if (elementClass.equals(Classes.parameterDirectionKind_In))
			return ParameterDirectionKindEnum.PDK_IN;
		
		if (elementClass.equals(Classes.parameterDirectionKind_Out))
			return ParameterDirectionKindEnum.PDK_OUT;
		
		if (elementClass.equals(Classes.parameterDirectionKind_InOut))
			return ParameterDirectionKindEnum.PDK_INOUT;
	
		if (elementClass.equals(Classes.generalization))
			newElement = model.getCore().getGeneralization().createGeneralization();
		
		if (elementClass.equals(Classes.enumeration))
			newElement = model.getCore().getEnumeration().createEnumeration();
		
		if (elementClass.equals(Classes.enumerationLiteral))
			newElement = model.getCore().getEnumerationLiteral().createEnumerationLiteral();
		
		if (elementClass.equals(Classes.umlinterface))
			newElement = model.getCore().getInterface().createInterface();
		
		this.addRefObject(newElement.refMofId(), refObject);
		return newElement;
	}
	
	/**
     * Deletes all elements in the MDR which are associated to the given object. 
     */
	public void deleteElements(Object refObject)
	{
		ArrayList elements = (ArrayList) getAllElement(refObject);
		for (int i = 0; i < elements.size(); i++)
		{
			RefObject object = (RefObject) elements.get(i);
			if (object instanceof Classifier)
			{
				this.deleteCollectionTypes((Classifier) object);
			}
			object.refDelete();
		}
		
	}
	
	/**
     * Sets the ModelFacade instance for the UML-Model with the given mofID. 
     */
	public static void addModelFacade(String mofID, ModelFacade facade) throws Exception 
	{
		Object obj = RepositoryManager.getRepository().getElementByMofID(mofID);
		if (obj != null)
			if (obj instanceof Uml15Package) //TODO test for MOF
				if (facade != null)
				{
					instances.put(mofID, facade);
				}
				else
					throw new NullPointerException();
		    else
		    	throw new Exception("Uml15Package excepted");
		else	
			throw new NullPointerException("Model with MOFID: " + mofID + " doesn't exist!");
	}
	
	public static ModelFacade getInstance(String mofID) 
	{
		if (instances != null && instances.get(mofID) != null)
			return instances.get(mofID);
		return null;
	}

	/**
     * Returns true if an element in the mdr has a valid element in the repository of the CASE-Tool 
     */
	public abstract boolean hasRefObject(String mofID);
	/**
     * Returns all elements within a namespace.
     */
	public abstract Collection getOwnedElements(String mofID);
	/**
     * Returns all features of a classifier.
     */
	public abstract java.util.List getFeature(String mofID);
	/**
     * Returns the name of a modelelement.
     */
	public abstract String getName(String mofID);
	/**
     * Returns the multiplicity of an association end or of an attribute.
     */
	public abstract Multiplicity getMultiplicity(String mofID);
	/**
     * Returns the ordering kind of an association end or of an attribute.
     */
	public abstract OrderingKind getOrdering(String mofID);
	/**
     * Returns the classifier which is connected to an association end.
     */
	public abstract Classifier getParticipant(String mofID);
	/**
     * Returns the association of an association end.
     */
	public abstract Association getAssociation(String mofID);
	/**
     * Returns all association ends and the association class of an association.
     */
	public abstract java.util.Collection getAssociationEnds(String mofID);
	/**
     * Returns the qualifiers of an association end.
     */
	public abstract List getQualifier(String mofID);
	/**
     * Returns the parameter direction kind of a parameter.
     */
	public abstract ParameterDirectionKind getKind(String mofID);
	/**
     * Returns the type of parameters and attributes.
     */
	public abstract Classifier getType(String mofID);
	/**
     * Returns the owner of features.
     */
	public abstract Classifier getOwner(String mofID);
	/**
     * Returns the namespace of modelelements.
     */
	public abstract Namespace getNamespace(String mofID);
	/**
     * Returns the generalization where a classifier is the superclass.
     */
	public abstract Collection getGeneralization(String mofID);
	/**
     * Returns the generalization where a classifier is the subclass.
     */
	public abstract Collection getSpecialization(String mofID);
	/**
     * Returns the association ends of an association.
     */
	public abstract List getConnection(String mofID);
	/**
     * Returns the parameters of an operation.
     */
	public abstract List getParameter(String mofID);	
	/**
     * Returns the owner scope of features.
     */
	public abstract ScopeKind getOwnerScope(String mofID);
	/**
     * Returns the multiplicity range of attributes and association ends.
     */
	public abstract java.util.Collection getRange(String mofID);
	/**
     * Returns the upper bound of the multiplicity range of attributes and association ends.
     */
	public abstract int getUpper(String mofID);
	/**
     * Returns the superclass of a generalization.
     */
	public abstract tudresden.ocl20.core.jmi.uml15.core.GeneralizableElement getParent(String mofID);
	/**
     * Returns subclass of a generalization.
     */
	public abstract tudresden.ocl20.core.jmi.uml15.core.GeneralizableElement getChild(String mofID);
	/**
     * Returns all enumeration literals of an enumeration.
     */
	public abstract java.util.List getLiteral(String mofID);
	/**
     * Returns the enumeration of an enumeration literal.
     */
	public abstract Enumeration getEnumeration(String mofID);
}
