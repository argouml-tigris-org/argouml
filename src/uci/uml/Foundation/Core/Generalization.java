// Source file: Foundation/Core/Generalization.java

package uci.uml.Foundation.Core;

import java.util.*;
import uci.uml.Foundation.Data_Types.Name;

public class Generalization extends ModelElementImpl {
  private Name _discriminator;
  public GeneralizableElement _subtype;
  public GeneralizableElement _supertype;
    
  public Generalization() { }

  public Name getDiscriminator() { return _discriminator; }
  public void setDiscriminator(Name x) {
    _discriminator = x;
  }
  public GeneralizableElement getSubtype() { return _subtype; }
  public void setSubtype(GeneralizableElement x) {
    _subtype = x;
  }
  public GeneralizableElement getSupertype() { return _supertype; }
  public void setSupertype(GeneralizableElement x) {
    _supertype = x;
  }

  
}
