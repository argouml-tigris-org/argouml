// Source file: Foundation/Core/Namespace.java

package uci.uml.Foundation.Core;

import java.util.*;

public interface Namespace extends ModelElement {
  //public ModelElement _ownedElement[];
  
  public Vector getOwnedElement();
  public void setOwnedElement(Vector x);
}
