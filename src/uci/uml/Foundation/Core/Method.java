// Source file: Foundation/Core/Method.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Data_Types.ProcedureExpression;

public class Method extends BehavioralFeature {
  public ProcedureExpression _body;
  public Operation _specification;
  
  public Method() { }
  
  public ProcedureExpression getBody() { return _body; }
  public void setBody(ProcedureExpression x) {
    _body = x;
  }
  public Operation getSpecification() { return _specification; }
  public void setSpecification(Operation x) {
    _specification = x;
  }
  
}
