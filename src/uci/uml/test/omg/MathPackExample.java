package uci.uml.test.omg;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Core.Class;
import uci.uml.Foundation.Extension_Mechanisms.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of a single class.  This example is taken from page 42 of
 *  the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class MathPackExample {

  public Class mathPackClass;
  public Stereotype utilityStereotype;
  public Operation op1, op2, op3, op4;
  public DataType realType, angleType;
  
  public MathPackExample() {
    mathPackClass = new Class("MathPack");
    utilityStereotype = new Stereotype("utility", "Class");
    realType = new DataType("Real");
    angleType = new DataType("Angle");
    op1 = new Operation(realType, "sin", angleType, "x");
    op2 = new Operation(realType, "cos", angleType, "x");
    op3 = new Operation(realType, "sqrt", angleType, "x");
    op4 = new Operation(realType, "random");

    mathPackClass.addStereotype(utilityStereotype);
    mathPackClass.addFeature(op1);
    mathPackClass.addFeature(op2);
    mathPackClass.addFeature(op3);
    mathPackClass.addFeature(op4);

    System.out.println(mathPackClass.dbgString());
  }

}
