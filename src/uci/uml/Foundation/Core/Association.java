// Source file: Foundation/Core/Association.java

package uci.uml.Foundation.Core;

//nmw: import uci.uml.Behavioral_Elements.Collaborations.AssociationRole;
import java.util.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Data_Types.Name;
import uci.uml.Behavioral_Elements.Common_Behavior.Link;


public class Association extends GeneralizableElementImpl {
  //% public AssociationEnd _connection[];
  public Vector _connection;
  //nmw: public AssociationRole associationRole[];
  //% public Link _link[];
  public Vector _link;
    
  public Association() { }
  public Association(Name name) { super(name); }
  public Association(String nameStr) { super(new Name(nameStr)); }
  public Association(String nameStr,AssociationEnd from,AssociationEnd to){
    super(new Name(nameStr));
    addConnection(from);
    addConnection(to);
  }
  public Association(Name name, Name srcN, Classifier srcC,
		     Multiplicity srcM, AggregationKind srcA,
		     Name dstN, Classifier dstC, Multiplicity dstM,
		     AggregationKind dstA) { 
    super(name);
    AssociationEnd src = new AssociationEnd(srcN, srcC, srcM, srcA);
    AssociationEnd dst = new AssociationEnd(dstN, dstC, dstM, dstA);
    addConnection(src);
    addConnection(dst);
  }

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
 
  //- public AssociationRole[] getAssociationRole() {
  //-   return associationRole;
  //- }
  //- public void setAssociationRole(AssociationRole[] x) {
  //-   associationRole = x;
  //- }

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

    s += getOCLTypeStr() + "(" + getName().toString() + ")[";

        String stereos = dbgStereotypes();
    if (stereos != "") s += "\n" + stereos;

    String tags = dbgTaggedValues();
    if (tags != "") s += "\n" + tags;

    if ((v = getConnection()) != null) {
      s += "\n  connections:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((AssociationEnd)enum.nextElement()).dbgString();
    }
    s += "\n]";
    return s;
  }
  
}

