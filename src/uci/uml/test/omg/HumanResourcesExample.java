package uci.uml.test.omg;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Core.Class;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of three classes, three associations, and a note.  This
 *  example is taken from page 74 of the UML 1.1 notation guide (OMG
 *  document ad/97-08-05). */


public class HumanResourcesExample {
  public Model model;
  public Class personClass, companyClass, deptClass;
  public Vector personAttrs = new Vector();
  public Attribute at1, at2;
  public Association assoc1, assoc2, assoc3;
  public Constraint ageCalc, worksForCompanyConstraint;
  
  public HumanResourcesExample() {
    try {
      model = new Model("default");
      personClass = new Class("Person");
      companyClass = new Class("Company");
      deptClass = new Class("Deptartment");

      at1 = new Attribute("birthdate");
      at2 = new Attribute("age");
      at2.addStereotype(Stereotype.DERIVED);
    
      ageCalc = new Constraint(Name.UNSPEC, "age = currentDate - birthdate");
      at2.addConstraint(ageCalc);
    
      personClass.addStructuralFeature(at1);
      personClass.addStructuralFeature(at2);

      AssociationEnd ae11 =
	new AssociationEnd(new Name("employer"), companyClass,
			   Multiplicity.ONE, AggregationKind.AGG);
      AssociationEnd ae12 =
	new AssociationEnd(Name.UNSPEC, deptClass,
			   Multiplicity.ONE_OR_MORE, AggregationKind.NONE);

      AssociationEnd ae21 =
	new AssociationEnd(new Name("employer"), companyClass,
			   Multiplicity.ONE, AggregationKind.NONE);
      AssociationEnd ae22 =
	new AssociationEnd(Name.UNSPEC, personClass,
			   Multiplicity.ZERO_OR_MORE, AggregationKind.NONE);

      AssociationEnd ae31 =
	new AssociationEnd(new Name("deptartment"), deptClass,
			   Multiplicity.ONE, AggregationKind.NONE);
      AssociationEnd ae32 =
	new AssociationEnd(Name.UNSPEC, personClass,
			   Multiplicity.ZERO_OR_MORE, AggregationKind.NONE);


      assoc1 = new Association();
      assoc1.addConnection(ae11);
      assoc1.addConnection(ae12);
    
      assoc2 = new Association("WorksForCompany");
      assoc2.addConnection(ae21);
      assoc2.addConnection(ae22);
      assoc2.addStereotype(Stereotype.DERIVED);

      assoc3 = new Association("WorksForDepartment");
      assoc3.addConnection(ae31);
      assoc3.addConnection(ae32);

      worksForCompanyConstraint =
	new Constraint(Name.UNSPEC, "OCL",
		       "Person.employer=Person.department.employer");
      assoc3.addConstraint(worksForCompanyConstraint);
    
      System.out.println(personClass.dbgString());
      System.out.println(deptClass.dbgString());
      System.out.println(companyClass.dbgString());
      System.out.println(assoc1.dbgString());
      System.out.println(assoc2.dbgString());
      System.out.println(assoc3.dbgString());

      model.addPublicOwnedElement(companyClass);
      model.addPublicOwnedElement(personClass);
      model.addPublicOwnedElement(deptClass);
      model.addPublicOwnedElement(assoc1);
      model.addPublicOwnedElement(assoc2);
      model.addPublicOwnedElement(assoc3);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in HumanResourcesExample");
    }
  }

} /* end class HumanResourcesExample */


