package uci.uml.test.omg;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Core.Class;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;

/** This is a very simple demo of how to represent a UML design that
 *  consists of a single class.  This example is taken from page 25 of
 *  the UML 1.1 notation guide (OMG document ad/97-08-05). */


public class WindowExample {

  public Model model;
  public Class windowClass;
  public Vector tvs = new Vector();
  public TaggedValue tv1, tv2;
  public Vector ops = new Vector();
  public Operation op1, op2, op3, op4;
  public Vector attrs = new Vector();
  public Attribute at1, at2, at3, at4, at5;
  public Class areaType, rectType, xWindowPtrType;
  public DataType booleanType, voidType;
  
  public WindowExample() {
    try {
      model = new Model("default");
      
      windowClass = new Class("Window");
      windowClass.setIsAbstract(Boolean.TRUE);
      tvs.addElement(tv1 = new TaggedValue("author", "Joe"));
      tvs.addElement(tv2 = new TaggedValue("status", "tested"));
    
      booleanType = new DataType("Boolean");
      voidType = new DataType("void");
      areaType = new Class("Area");
      rectType = new Class("Rectangle");
      xWindowPtrType = new Class("Xwindow*");

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

      System.out.println(windowClass.dbgString());
    }
    catch (PropertyVetoException ex) {
      System.out.println("an veto execption occured in WindowExample");
    }


  }

} /* end Class WindowExample */
