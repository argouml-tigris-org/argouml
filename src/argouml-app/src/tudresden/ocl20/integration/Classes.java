/*
 * Created on 23.10.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package tudresden.ocl20.integration;

import tudresden.ocl20.core.jmi.uml15.core.Association;
import tudresden.ocl20.core.jmi.uml15.core.AssociationEnd;
import tudresden.ocl20.core.jmi.uml15.core.Attribute;
import tudresden.ocl20.core.jmi.uml15.core.Classifier;
import tudresden.ocl20.core.jmi.uml15.core.DataType;
import tudresden.ocl20.core.jmi.uml15.core.Enumeration;
import tudresden.ocl20.core.jmi.uml15.core.EnumerationLiteral;
import tudresden.ocl20.core.jmi.uml15.core.Generalization;
import tudresden.ocl20.core.jmi.uml15.core.Interface;
import tudresden.ocl20.core.jmi.uml15.core.Operation;
import tudresden.ocl20.core.jmi.uml15.core.Parameter;
import tudresden.ocl20.core.jmi.uml15.core.UmlClass;
import tudresden.ocl20.core.jmi.uml15.datatypes.Multiplicity;
import tudresden.ocl20.core.jmi.uml15.datatypes.MultiplicityRange;
import tudresden.ocl20.core.jmi.uml15.datatypes.OrderingKindEnum;
import tudresden.ocl20.core.jmi.uml15.datatypes.ParameterDirectionKindEnum;
import tudresden.ocl20.core.jmi.uml15.datatypes.ScopeKindEnum;

/**
 * An enumeration of all classes of elements in the MDR created by the class ModelFacade.
 * @author Mirko
 */
public enum Classes 
{
	multiplicity(0),
	attribute(1),
	parameter(2),
	association(3),
	associationend(4),
	range(5),
	method(6),
	umlClass(7),
	umlPackage(8),
	dataType(9),
	scopeKind_classifier(10),
	scopeKind_instance(11),
	orderingKind_ordered(12),
	orderingKind_unordered(13),
	parameterDirectionKind_Return(14),
	parameterDirectionKind_InOut(15),
	parameterDirectionKind_In(16),
	parameterDirectionKind_Out(17),
	generalization(18),
	enumeration(19),
	enumerationLiteral(20),
	umlinterface(21);
	
	private Class value = null;
	Classes (int i)
	{
		switch (i)
		{
			case 0: 
			{
				this.value = Multiplicity.class;
				break;
			}
			case 1: 
			{
				this.value = Attribute.class;
				break;
			}
			case 2: 
			{
				this.value = Parameter.class;
				break;
			}
			case 3: 
			{
				this.value = Association.class;
				break;
			}
			case 4: 
			{
				this.value = AssociationEnd.class;
				break;
			}
			case 5: 
			{
				this.value = MultiplicityRange.class;
				break;
			}
			case 6: 
			{
				this.value = Operation.class;
				break;
			}
			case 7: 
			{
				this.value = UmlClass.class;
				break;
			}
			case 8: 
			{
				this.value = tudresden.ocl20.core.jmi.uml15.modelmanagement.Package.class;
				break;
			}
			case 9: 
			{
				this.value = DataType.class;
				break;
			}
			case 10: 
			{
				this.value = ScopeKindEnum.SK_CLASSIFIER.getClass();
				break;
			}
			case 11: 
			{
				this.value = ScopeKindEnum.SK_INSTANCE.getClass();
				break;
			}
			case 12: 
			{				
				this.value = OrderingKindEnum.OK_ORDERED.getClass();
				break;
			}
			case 13: 
			{
				this.value = OrderingKindEnum.OK_UNORDERED.getClass();
				break;
			}
			case 14: 
			{
				this.value = ParameterDirectionKindEnum.PDK_RETURN.getClass();
				break;
			}
			case 15: 
			{
				this.value = ParameterDirectionKindEnum.PDK_INOUT.getClass();
				break;
			}
			case 16: 
			{
				this.value = ParameterDirectionKindEnum.PDK_IN.getClass();
				break;
			}
			case 17: 
			{
				this.value = ParameterDirectionKindEnum.PDK_OUT.getClass();
				break;
			}
			case 18: 
			{
				this.value = Generalization.class;
				break;
			}
			case 19: 
			{
				this.value = Enumeration.class;
				break;
			}
			case 20: 
			{
				this.value = EnumerationLiteral.class;
				break;
			}
			case 21: 
			{
				this.value = Interface.class;
				break;
			}
		}
	}
	
	public Class getValue()
	{
		return this.value;
	}	
}
