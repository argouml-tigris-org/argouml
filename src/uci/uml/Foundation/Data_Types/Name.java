// Source file: f:/jr/projects/uml/Foundation/Data_Types/Name.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

//import uci.uml.Foundation.Core.Element;


public class Name {
  public static final Name UNSPEC = new Name("");
  
  public String _body;
  //- public Element name;
    
  public Name() { }
  public Name(String str) { _body = str; }

  public String getBody() { return _body; }
  public void setBody(String b) { _body = b; }

  ////////////////////////////////////////////////////////////////
  // debugging

  public String toString() {
    if (getBody() != null)
      return "Name(\"" + getBody() + "\")";
    else
      return "Name(null)";
  }
  
  
}
