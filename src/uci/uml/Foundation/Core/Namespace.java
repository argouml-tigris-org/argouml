// Source file: Foundation/Core/Namespace.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.PropertyVetoException;

import uci.uml.Model_Management.*;


public interface Namespace extends ModelElement {
  //public ModelElement _ownedElement[];
  
  public Vector getOwnedElement();
  public void setOwnedElement(Vector x)
       throws PropertyVetoException;
  public void addOwnedElement(ElementOwnership x)
       throws PropertyVetoException;
  public void removeOwnedElement(ElementOwnership x)
       throws PropertyVetoException;
}
