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

import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.generate.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of a single class.  This example is taken from page 42 of
 *  the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class MathPackExample {
  public Model model;
  public MMClass mathPackClass;
  public Stereotype utilityStereotype;
  public Operation op1, op2, op3, op4;
  public DataType realType, angleType;
  
  public MathPackExample() {
    try {
      model = new Model("MathPackExample");
      mathPackClass = new MMClass("MathPack");
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

      model.addPublicOwnedElement(mathPackClass);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in MathPackExample");
    }
  }

  public void print() {
    System.out.println(GeneratorDisplay.Generate(model));
  }

} /* end class MathPackExample */
