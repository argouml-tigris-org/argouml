// Source file: f:/jr/projects/uml/Foundation/Data_Types/Enumeration.java

package uci.uml.Foundation.Data_Types;

import java.util.*;

import uci.uml.Foundation.Core.DataType;


public class Enumeration extends DataType {
  //% public EnumerationLiteral _literal[];
  public Vector _literal;
    
  public Enumeration() { }
  public Enumeration(Name name) { super(name); }
  public Enumeration(String nameStr) { super(new Name(nameStr)); }
  public Enumeration(String nameStr, EnumerationLiteral[] lits) {
    super(new Name(nameStr));
    setLiteral(lits);
  }
  public Enumeration(String nameStr, Vector lits) {
    super(new Name(nameStr));
    setLiteral(lits);
  }

  public Vector getLiteral() { return _literal; }
  public void setLiteral(Vector x) {
    _literal = x;
  }
  public void setLiteral(EnumerationLiteral[] x) {
    Vector vec = new Vector(x.length);
    for (int i=0; i < x.length; i++)
      vec.addElement(x[i]);
    _literal = vec;
  }
  public void addLiteral(EnumerationLiteral x) {
    if (_literal == null) _literal = new Vector();
    _literal.addElement(x);
  }
  public void removeLiteral(EnumerationLiteral x) {
    _literal.removeElement(x);
  }
  
}
