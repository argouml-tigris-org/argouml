// Source file: f:/jr/projects/uml/Foundation/Data_Types/Expression.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class Expression {
  public static final Name UNSPEC = new Name("Unspecified");
  public static final Name JAVA = new Name("Java");
  public static final Name C = new Name("C/C++");
  public static final Name OCL = new Name("OCL");
  public static final Name[] POSSIBLE_LANGUAGES = {
    UNSPEC, JAVA, C, OCL };
  
  public Name _language = UNSPEC;
  public Uninterpreted _body;
  
  public Expression() { }
  public Expression(Uninterpreted body) { setBody(body); }
  public Expression(Name lang, Uninterpreted body) {
    setLanguage(lang);
    setBody(body);
  }
  public Expression(String bodyStr) { setBody(bodyStr); }
  public Expression(String langStr, String bodyStr) {
    setLanguage(langStr);
    setBody(bodyStr);
  }

  public Name getLanguage() { return _language; }
  public void setLanguage(Name x) { _language = x; }
  public void setLanguage(String langStr) { _language = new Name(langStr); }
  
  public Uninterpreted getBody() { return _body; }
  public void setBody(Uninterpreted x) {
    _body = x;
  }

  public void setBody(String x) {
    setBody(new Uninterpreted(x));
  }

}
