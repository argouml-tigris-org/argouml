// Source file: Foundation/Core/Namespace.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;

public interface Namespace extends ModelElement {
  //public ModelElement _ownedElement[];
  
  public Vector getOwnedElement();
  public void setOwnedElement(Vector x) throws PropertyVetoException;
}
