// Source file: Foundation/Core/IAssociation.java

package uci.uml.Foundation.Core;

//nmw: import uci.uml.Behavioral_Elements.Collaborations.AssociationRole;
import java.util.*;
import uci.uml.Behavioral_Elements.Common_Behavior.Link;


public interface IAssociation extends ModelElement {

  public Vector getConnection();
  public void setConnection(Vector x);
  public void addConnection(AssociationEnd x);
  public void removeConnection(AssociationEnd x);
  public Vector getLink();
  public void setLink(Vector x);
  public void addLink(Link x);
  public void removeLink(Link x);
  
}

