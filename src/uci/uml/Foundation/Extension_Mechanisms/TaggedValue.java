// Source file: f:/jr/projects/uml/Foundation/Extension_Mechanisms/TaggedValue.java

package uci.uml.Foundation.Extension_Mechanisms;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.generate.*;


public class TaggedValue extends ElementImpl {
  public Name _tag;
  public Uninterpreted _value;
  //- public Stereotype stereotype;
  //- public ModelElement modelElement;
  //- public Element _element;
  //- public Element m_Element;
    
  public TaggedValue() { }
  public TaggedValue(Name tag, Uninterpreted value) {
    super(tag);  // a bit redundant
    setTag(tag);
    setValue(value);
  }
  public TaggedValue(String tagStr, String valueStr) {
    super(new Name(tagStr));  // a bit redundant
    setTag(getName());
    setValue(new Uninterpreted(valueStr));
  }

  public Name getTag() { return _tag; }
  public void setTag(Name x) {
    _tag = x;
  }
  public Uninterpreted getValue() { return _value; }
  public void setValue(Uninterpreted x) {
    _value = x;
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
