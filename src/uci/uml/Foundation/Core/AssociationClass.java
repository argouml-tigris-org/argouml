// Source file: Foundation/Core/Association.java

package uci.uml.Foundation.Core;

//nmw: import uci.uml.Behavioral_Elements.Collaborations.AssociationRole;
import java.util.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.Common_Behavior.Link;


public class AssociationClass extends Class
implements IAssociation {


  public AssociationClass() { }
  public AssociationClass(Name name) { super(name); }
  public AssociationClass(String nameStr) { super(new Name(nameStr)); }

  ////////////////////////////////////////////////////////////////
  // IAssociation implementation
  public Vector _connection;
  public Vector _link;
    
  public Vector getConnection() { return _connection; }
  public void setConnection(Vector x) {
    _connection = x;
  }
  public void addConnection(AssociationEnd x) {
    if (_connection == null) _connection = new Vector();
    _connection.addElement(x);
    x.setAssociation(this);
  }
  public void removeConnection(AssociationEnd x) {
    _connection.removeElement(x);
  }
 

  public Vector getLink() { return _link; }
  public void setLink(Vector x) {
    _link = x;
  }
  public void addLink(Link x) {
    if (_link == null) _link = new Vector();
    _link.addElement(x);
  }
  public void removeLink(Link x) {
    _link.removeElement(x);
  }

  ////////////////////////////////////////////////////////////////
  // debugging

  
  public String dbgString() {
    String s = "";
    Vector v;
    java.util.Enumeration enum;

    String slash = "";
    if (containsStereotype(Stereotype.DERIVED)) slash = "/";
    
    s += getOCLTypeStr() + "(" + slash + getName().getBody().toString() + ")[";

    String stereos = dbgStereotypes();
    if (!stereos.equals("")) s += "\n" + stereos;

    String tags = dbgTaggedValues();
    if (tags != "") s += "\n" + tags;

    if ((v = getConnection()) != null) {
      s += "\n  connections:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((AssociationEnd)enum.nextElement()).dbgString();
    }
    if ((v = getStructuralFeature()) != null) {
      s += "\n  attributes:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((Attribute)enum.nextElement()).dbgString();
    }
    if ((v = getBehavioralFeature()) != null) {
      s += "\n  operations:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((Operation) enum.nextElement()).dbgString();
    }
    s += "\n]";
    return s;
  }
  
}

