// Source file: f:/jr/projects/uml/Foundation/Data_Types/EnumerationLiteral.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

public class EnumerationLiteral {
  public Name _name;
  //- public Enumeration enumeration;
    
  public EnumerationLiteral() { }
  public EnumerationLiteral(Name name) { setName(name); }
  public EnumerationLiteral(String nameStr) { setName(nameStr); }
  
  public Name getName() { return _name; }
  public void setName(Name x) { _name = x; }
  public void setName(String x) { _name = new Name(x); }
  
  //- public Enumeration getEnumeration() { return enumeration; }
  //- public void setEnumeration(Enumeration x) {
  //-   enumeration = x;
  //- }
  
}
