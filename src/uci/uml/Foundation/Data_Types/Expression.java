// Source file: f:/jr/projects/uml/Foundation/Data_Types/Expression.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class Expression {
  public static final Name UNSPECIFIED_LANGUAGE = new Name("Unspecified");
  public static final Name JAVA_LANGUAGE = new Name("Java");
  public static final Name C_LANGUAGE = new Name("C/C++");
  public static final Name OCL_LANGUAGE = new Name("OCL");
  public static final Name[] POSSIBLE_LANGUAGES = {
    UNSPECIFIED_LANGUAGE, JAVA_LANGUAGE, C_LANGUAGE, OCL_LANGUAGE };
  
  public Name _language = UNSPECIFIED_LANGUAGE;
  public Uninterpreted _body;
  
  public Expression() { }
  public Expression(String body) {
    setBody(body);
  }

  public Name getLanguage() { return _language; }
  public void setLanguage(Name x) {
    _language = x;
  }
  
  public Uninterpreted getBody() { return _body; }
  public void setBody(Uninterpreted x) {
    _body = x;
  }

  public void setBody(String x) {
    setBody(new Uninterpreted(x));
  }

}
