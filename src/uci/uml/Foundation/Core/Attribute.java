// Source file: f:/jr/projects/uml/Foundation/Core/Attribute.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;
import uci.uml.generate.*;

public class Attribute extends StructuralFeature {
  public Expression _initialValue;

  public Attribute() { }
  public Attribute(Name name) { super(name); }
  public Attribute(String nameStr) { super(new Name(nameStr)); }
  public Attribute(Name name, Classifier type) { super(name, type); }
  public Attribute(String nameStr, Classifier type) {
    super(new Name(nameStr), type);
  }
  public Attribute(Name name, Classifier type, Expression init) {
    super(name, type);
    setInitialValue(init);
  }
  public Attribute(String nameStr, Classifier type, Expression init) {
    super(new Name(nameStr), type);
    setInitialValue(init);
  }
  public Attribute(String nameStr, Classifier type, String initStr) {
    super(new Name(nameStr), type);
    setInitialValue(new Expression(initStr));
  }

  
  public Expression getInitialValue() { return _initialValue; }
  public void setInitialValue(Expression x) {
    _initialValue = x;
  }
  
  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = getOCLTypeStr() + "{" +
      GeneratorDisplay.Generate(this) +
      "}";
    return s;
  }
  
}
