package uci.uml.test.omg;

import java.util.*;
import java.beans.PropertyVetoException;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Core.Class;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of three classes and two associations.  This example is
 *  taken from page 56 of the UML 1.1 notation guide (OMG document
 *  ad/97-08-05). */


public class GraphicsExample {
  public Model model;
  public Class polygonClass, pointClass, graphicsBundleClass;
  public Vector gbAttrs = new Vector();
  public Vector gbOps = new Vector();
  public Attribute at1, at2, at3;
  public Association assoc1, assoc2;
  public DataType realType, angleType;
  
  public GraphicsExample() {
    try {
      model = new Model("default");
      
      polygonClass = new Class("Polygon");
      pointClass = new Class("Point");
      graphicsBundleClass = new Class("GraphicsBundle");

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
      
      System.out.println(polygonClass.dbgString());
      System.out.println(pointClass.dbgString());
      System.out.println(graphicsBundleClass.dbgString());
      System.out.println(assoc1.dbgString());
      System.out.println(assoc2.dbgString());

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

} /* end class GraphicsExample */
