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
import java.beans.PropertyVetoException;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of three classes and two associations.  This example is
 *  taken from page 56 of the UML 1.1 notation guide (OMG document
 *  ad/97-08-05). */


public class GraphicsExample {
  public Model model;
  public MMClass polygonClass, pointClass, graphicsBundleClass;
  public Vector gbAttrs = new Vector();
  public Vector gbOps = new Vector();
  public Attribute at1, at2, at3;
  public Association assoc1, assoc2;
  public DataType realType, angleType;
  
  public GraphicsExample() {
    try {
      model = new Model("GraphicsExample");
      
      polygonClass = new MMClass("Polygon");
      pointClass = new MMClass("Point");
      graphicsBundleClass = new MMClass("GraphicsBundle");

      realType = new DataType("Real");
      angleType = new DataType("Angle");

      at1 = new Attribute("color");
      at2 = new Attribute("texture");
      at3 = new Attribute("density");
      Operation op1 = new Operation(realType, "sin", angleType, "x");
      Operation op2 = new Operation(realType, "cos", angleType, "x");
      Operation op3 = new Operation(realType, "sqrt", angleType, "x");
      Operation op4 = new Operation(realType, "random");
      gbAttrs.addElement(at1);
      gbAttrs.addElement(at2); 
      gbAttrs.addElement(at3);
      gbOps.addElement(op1);
      gbOps.addElement(op2);
      gbOps.addElement(op3);
      gbOps.addElement(op4);

      graphicsBundleClass.setStructuralFeature(gbAttrs);
      graphicsBundleClass.setBehavioralFeature(gbOps);

      AssociationEnd ae11 =
	new AssociationEnd(Name.UNSPEC, polygonClass,
			   Multiplicity.ONE, AggregationKind.AGG);
      AssociationEnd ae12 =
	new AssociationEnd(new Name("points"), pointClass,
			   new Multiplicity(new Integer(3), null),
			   AggregationKind.NONE);
      ae12.setIsNavigable(true);
      ae12.setIsOrdered(true);

      AssociationEnd ae21 =
	new AssociationEnd(Name.UNSPEC, polygonClass,
			   Multiplicity.ONE, AggregationKind.AGG);
      AssociationEnd ae22 =
	new AssociationEnd(Name.UNSPEC, graphicsBundleClass,
			   Multiplicity.ONE, AggregationKind.NONE);
      ae22.setIsNavigable(true);

      assoc1 = new Association("Contains");
      assoc1.addConnection(ae11);
      assoc1.addConnection(ae12);
      assoc2 = new Association(Name.UNSPEC);
      assoc2.addConnection(ae21);
      assoc2.addConnection(ae22);

      polygonClass.addAssociationEnd(ae11);
      pointClass.addAssociationEnd(ae12);
      polygonClass.addAssociationEnd(ae21);
      graphicsBundleClass.addAssociationEnd(ae22);
      
      model.addPublicOwnedElement(polygonClass);
      model.addPublicOwnedElement(pointClass);
      model.addPublicOwnedElement(graphicsBundleClass);
      model.addPublicOwnedElement(assoc1);
      model.addPublicOwnedElement(assoc2);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in GraphicsExample");
    }
  }

  public void print() {
    System.out.println(polygonClass.dbgString());
    System.out.println(pointClass.dbgString());
    System.out.println(graphicsBundleClass.dbgString());
    System.out.println(assoc1.dbgString());
    System.out.println(assoc2.dbgString());
  }
  
} /* end class GraphicsExample */
