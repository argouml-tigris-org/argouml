// Copyright (c) 1996-99 The Regents of the University of California. All
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




package uci.uml.test.omg;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of three classes, three associations, and a note.  This
 *  example is taken from page 74 of the UML 1.1 notation guide (OMG
 *  document ad/97-08-05). */


public class HumanResourcesExample {
  public Model model;
  public MMClass personClass, companyClass, deptClass;
  public Vector personAttrs = new Vector();
  public Attribute at1, at2;
  public Association assoc1, assoc2, assoc3;
  public Constraint ageCalc, worksForCompanyConstraint;
  
  public HumanResourcesExample() {
    try {
      model = new Model("HumanResourcesExample");
      personClass = new MMClass("Person");
      companyClass = new MMClass("Company");
      deptClass = new MMClass("Deptartment");

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

  public void print() {
      System.out.println(personClass.dbgString());
      System.out.println(deptClass.dbgString());
      System.out.println(companyClass.dbgString());
      System.out.println(assoc1.dbgString());
      System.out.println(assoc2.dbgString());
      System.out.println(assoc3.dbgString());
  }
  
} /* end class HumanResourcesExample */


