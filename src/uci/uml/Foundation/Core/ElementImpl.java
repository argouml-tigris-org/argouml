// Source file: Foundation/Core/Element.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Extension_Mechanisms.TaggedValue;
import uci.uml.Foundation.Extension_Mechanisms.Stereotype;
import uci.uml.Foundation.Data_Types.Name;


public class ElementImpl implements Element {
  public Name _name;
  //% public TaggedValue _characteristic[];
  public Vector _characteristic;
  public Stereotype _classification;
  //% public TaggedValue _taggedValue[];
  public Vector _taggedValue;

  public ElementImpl() { }
  public ElementImpl(Name name) { setName(name); }
  public ElementImpl(String nameStr) { setName(new Name(nameStr)); }

  public Vector getCharacteristic() { return _characteristic; }
  public void setCharacteristic(Vector x) {
    _characteristic = x;
  }
  public void addCharacteristic(TaggedValue x) {
    if (_characteristic == null) _characteristic = new Vector();
    _characteristic.addElement(x);
  }
  public void removeCharacteristic(TaggedValue x) {
    _characteristic.removeElement(x);
  }

  public Stereotype getClassification() { return _classification; }
  public void setClassification(Stereotype x) {
    _classification = x;
  }

  public Name getName() { return _name; }
  public void setName(Name x) {
    _name = x;
  }

  public Vector getTaggedValue() { return _taggedValue; }
  public void setTaggedValue(Vector x) {
    _taggedValue = x;
  }
  public void addTaggedValue(TaggedValue x) {
    if (_taggedValue == null) _taggedValue = new Vector();
    _taggedValue.addElement(x);
  }
  public void removeTaggedValue(TaggedValue x) {
    _taggedValue.removeElement(x);
  }

  ////////////////////////////////////////////////////////////////
  // OCL support
  
  public String getOCLTypeStr() {
    String javaClassName = getClass().getName();
    int dotIndex = javaClassName.lastIndexOf(".");
    String OCLTypeString = javaClassName.substring(dotIndex+1);
    return OCLTypeString;
  }

}
