// Source file: Foundation/Core/IAssociation.java

package uci.uml.Foundation.Core;

//nmw: import uci.uml.Behavioral_Elements.Collaborations.AssociationRole;
import java.util.*;
import java.beans.*;

import uci.uml.Behavioral_Elements.Common_Behavior.Link;


public interface IAssociation extends ModelElement {

  public Vector getConnection();
  public void setConnection(Vector x) throws PropertyVetoException;
  public void addConnection(AssociationEnd x) throws PropertyVetoException;
  public void removeConnection(AssociationEnd x) throws PropertyVetoException;
  public Vector getLink() throws PropertyVetoException;
  public void setLink(Vector x) throws PropertyVetoException;
  public void addLink(Link x) throws PropertyVetoException;
  public void removeLink(Link x) throws PropertyVetoException;
  
}

