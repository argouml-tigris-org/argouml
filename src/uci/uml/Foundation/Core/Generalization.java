// Source file: Foundation/Core/Generalization.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;
import uci.uml.Foundation.Data_Types.Name;

public class Generalization extends ModelElementImpl {
  private Name _discriminator;
  public GeneralizableElement _subtype;
  public GeneralizableElement _supertype;
    
  public Generalization() { }
  public Generalization(Name name) { super(name); }
  public Generalization(String nameStr) { super(new Name(nameStr)); }
  public Generalization(GeneralizableElement sub, GeneralizableElement sup)
  throws PropertyVetoException {
    super();
    setSubtype(sub);
    setSupertype(sup);
    sub.addGeneralization(this);
    sup.addSpecialization(this);
  }

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
