// Source file: Foundation/Core/Element.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;
import uci.uml.Foundation.Extension_Mechanisms.TaggedValue;
//import uci.uml.Foundation.Extension_Mechanisms.Stereotype;
import uci.uml.Foundation.Data_Types.Name;


public interface Element {
  //    public TaggedValue _characteristic[];
  //    public Stereotype _classification;
  //    public Name _name;
  //    public TaggedValue _taggedValue[];
    
  public Vector getCharacteristic();
  public void setCharacteristic(Vector x) throws PropertyVetoException;
  
  //    public Stereotype getClassification();
  //  public void setClassification(Stereotype s) throws PropertyVetoException;
  
  public Name getName();
  public void setName(Name n) throws PropertyVetoException;
  
  public Vector getTaggedValue();
  public void setTaggedValue(Vector x) throws PropertyVetoException;

}
