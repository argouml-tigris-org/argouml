// Source file: f:/jr/projects/uml/Foundation/Core/Constraint.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
//import uci.uml.Behavioral_Elements.Collaborations.*;



public class Constraint extends ModelElementImpl {
  public BooleanExpression _body;
  //- public ModelElement _constrainedElement[];
  //- public Stereotype _constrainedStereotype;

  public Constraint() { }
  public Constraint(Name name) { super(name); }
  public Constraint(String nameStr) { super(new Name(nameStr)); }
  public Constraint(Name name, String body) {
    super(name);
    setBody(new BooleanExpression(body));
  }
  public Constraint(String nameStr, String body) {
    super(new Name(nameStr));
    setBody(new BooleanExpression(body));
  }
  public Constraint(String nameStr, String lang, String body) {
    super(new Name(nameStr));
    setBody(new BooleanExpression(lang, body));
  }
  public Constraint(Name name, String lang, String body) {
    super(name);
    setBody(new BooleanExpression(lang, body));
  }

  public BooleanExpression getBody() { return _body; }
  public void setBody(BooleanExpression x) {
    _body = x;
  }

  //- public ModelElement[] getConstrainedElement() {
  //-   return _constrainedElement;
  //- }
  //- public void setConstrainedElement(ModelElement[] x) {
  //-   _constrainedElement = x;
  //- }

  //- public Stereotype getConstrainedStereotype() {
  //-   return _constrainedStereotype;
  //- }
  //- public void setConstrainedStereotype(Stereotype x) {
  //-   _constrainedStereotype = x;
  //- }

}
