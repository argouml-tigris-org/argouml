// Source file: f:/jr/projects/uml/Foundation/Data_Types/BooleanExpression.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class BooleanExpression extends Expression {
    
  public BooleanExpression() { }
  public BooleanExpression(Uninterpreted body) { super(body); }
  public BooleanExpression(String bodyStr) { super(bodyStr); }
  public BooleanExpression(String langStr, String bodyStr) {
    super(langStr, bodyStr);
  }
  
}
