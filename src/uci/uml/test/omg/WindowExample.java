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
 *  consists of a single class.  This example is taken from page 25 of
 *  the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class WindowExample {

  public Model model;
  public MMClass windowClass;
  public Vector tvs = new Vector();
  public TaggedValue tv1, tv2;
  public Vector ops = new Vector();
  public Operation op1, op2, op3, op4;
  public Vector attrs = new Vector();
  public Attribute at1, at2, at3, at4, at5;
  public MMClass areaType, rectType, xWindowPtrType;
  public DataType booleanType, voidType;
  
  public WindowExample() {
    try {
      model = new Model("WindowExample");
      
      windowClass = new MMClass("Window");
      windowClass.setIsAbstract(true);
      tvs.addElement(tv1 = new TaggedValue("author", "Joe"));
      tvs.addElement(tv2 = new TaggedValue("status", "tested"));
    
      booleanType = new DataType("Boolean");
      voidType = new DataType("void");
      areaType = new MMClass("Area");
      rectType = new MMClass("Rectangle");
      xWindowPtrType = new MMClass("Xwindow*");

      at1 = new Attribute("size", areaType, "(100, 100)");
      at1.setVisibility(VisibilityKind.PUBLIC);
      at2 = new Attribute("visibility", booleanType, "false");
      at2.setVisibility(VisibilityKind.PROTECTED);
      at3 = new Attribute("default-size", rectType);
      at3.setOwnerScope(ScopeKind.CLASSIFIER);
      at3.setVisibility(VisibilityKind.PUBLIC);
      at4 = new Attribute("maximum-size", rectType);
      at4.setVisibility(VisibilityKind.PROTECTED);
      at4.setOwnerScope(ScopeKind.CLASSIFIER);
      at5 = new Attribute("xprt", xWindowPtrType);
      at5.setVisibility(VisibilityKind.PRIVATE);
      attrs.addElement(at1);
      attrs.addElement(at2); 
      attrs.addElement(at3);
      attrs.addElement(at4);
      attrs.addElement(at5);
    
      op1 = new Operation(voidType, "display");
      op1.setVisibility(VisibilityKind.PUBLIC);
      op2 = new Operation(voidType, "hide");
      op2.setVisibility(VisibilityKind.PUBLIC);
      op3 = new Operation(voidType, "create");
      op3.setVisibility(VisibilityKind.PUBLIC);
      op3.setOwnerScope(ScopeKind.CLASSIFIER);
      op4 = new Operation(voidType, "attachXWindow", xWindowPtrType, "xwin");
      op4.setVisibility(VisibilityKind.PRIVATE);
      ops.addElement(op1);
      ops.addElement(op2);
      ops.addElement(op3);
      ops.addElement(op4);
    
      windowClass.setTaggedValue(tvs);
      windowClass.setStructuralFeature(attrs);
      windowClass.setBehavioralFeature(ops);

      model.addPublicOwnedElement(windowClass);
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in WindowExample");
    }
  }

  public void print() {
    System.out.println(windowClass.dbgString());
  }

} /* end class WindowExample */
