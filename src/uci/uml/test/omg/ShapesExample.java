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
import uci.uml.generate.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of three classes that inherit from a fourth.  This
 *  example is taken from page 69 of the UML 1.1 notation guide (OMG
 *  document ad/97-08-05). */


public class ShapesExample {
  public Model model;
  public MMClass shapeClass, polygonClass, ellipseClass, splineClass;
  public Generalization g1, g2, g3;
  
  public ShapesExample() {
    try {
      model = new Model("ShapesExample");
      shapeClass = new MMClass("Shape");
      polygonClass = new MMClass("Polygon");
      ellipseClass = new MMClass("Ellipse");
      splineClass = new MMClass("Spline");
      shapeClass.setIsAbstract(true);
      splineClass.setIsAbstract(true); // bug that a critic can find

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
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in ShapesExample");
    }
  }

  public void print() {
    System.out.println(GeneratorDisplay.Generate(model));
  }
  
} /* end class ShapesExample */


