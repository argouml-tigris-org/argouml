package uci.uml.test.omg;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Core.Class;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.generate.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of three classes that inherit from a fourth.  This
 *  example is taken from page 69 of the UML 1.1 notation guide (OMG
 *  document ad/97-08-05). */


public class ShapesExample {
  public Model model;
  public Class shapeClass, polygonClass, ellipseClass, splineClass;
  public Generalization g1, g2, g3;
  
  public ShapesExample() {
    try {
      model = new Model("default");
      shapeClass = new Class("Shape");
      polygonClass = new Class("Polygon");
      ellipseClass = new Class("Ellipse");
      splineClass = new Class("Spline");
      shapeClass.setIsAbstract(Boolean.TRUE);
      splineClass.setIsAbstract(Boolean.TRUE); // bug that a critic can find

      // using this Generalization constructor also modifies the classes
      g1 = new Generalization(polygonClass, shapeClass);
      g2 = new Generalization(ellipseClass, shapeClass);
      g3 = new Generalization(splineClass, shapeClass);
    
      model.addPublicOwnedElement(shapeClass);
      model.addPublicOwnedElement(polygonClass);
      model.addPublicOwnedElement(ellipseClass);
      model.addPublicOwnedElement(splineClass);
      model.addPublicOwnedElement(g1); //?
      model.addPublicOwnedElement(g2); //?
      model.addPublicOwnedElement(g3); //?

      System.out.println(GeneratorDisplay.Generate(model));
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in ShapesExample");
    }
  }

} /* end class ShapesExample */


