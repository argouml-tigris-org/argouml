// Source file: f:/jr/projects/uml/Behavioral_Elements/Collaborations/ClassifierRole.java

package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;


public class ClassifierRole extends Classifier {
  public Multiplicity _multiplicity;
  //% public AssociationEndRole _associationEndRole[];
  public Vector _associationEndRole;
  //public Message m_Message[];
  //% public Message _message[];
  public Vector _message;
  //% public Feature _availableFeature[];
  public Vector _availableFeature;
  public Collaboration _collaboration;
  
  public ClassifierRole() { }

  public Multiplicity getMultiplicity() { return _multiplicity; }
  public void setMultiplicity(Multiplicity x) {
     _multiplicity = x;
  }

  public Vector getAssociationEndRole() {
    return _associationEndRole;
  }
  public void setAssociationEndRole(Vector x) {
     _associationEndRole = x;
  }
  public void addAssociationEndRole(AssociationEndRole x) {
    if (_associationEndRole == null) _associationEndRole = new
				       Vector();
    _associationEndRole.addElement(x);
  }
  public void removeAssociationEndRole(AssociationEndRole x) {
     _associationEndRole.removeElement(x);
  }


  public Vector getMessage() { return _message; }
  public void setMessage(Vector x) {
     _message = x;
  }
  public void addMessage(Message x) {
    if (_message == null) _message = new Vector();
    _message.addElement(x);
  }
  public void removeMessage(Message x) {
     _message.removeElement(x);
  }

  public Vector getAvailableFeature() { return _availableFeature; }
  public void setAvailableFeature(Vector x) {
     _availableFeature = x;
  }
  public void addAvailableFeature(Feature x) {
    if (_availableFeature == null) _availableFeature = new Vector();
    _availableFeature.addElement(x);
  }
  public void removeAvailableFeature(Feature x) {
     _availableFeature.removeElement(x);
  }

  public Collaboration getCollaboration() { return _collaboration; }
  public void setCollaboration(Collaboration x) {
     _collaboration = x;
  }
  
}
