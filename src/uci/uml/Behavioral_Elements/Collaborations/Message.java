// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;
import java.beans.*;

import uci.uml.Behavioral_Elements.Common_Behavior.MMAction;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;


public class Message extends ModelElementImpl {
  public MMAction _action;
  public Message _activator;
  public Vector _message;
  public Vector _predecessor;
  public ClassifierRole _sender;
  public ClassifierRole _receiver;
  public Vector _interaction;
  public AssociationRole _associationRole;

  public Message() { }
  public Message(String nameStr) { this(new Name(nameStr)); }
  public Message(Name name) { super(name); }

  public MMAction getAction() { return _action; }
  public void setAction(MMAction x) throws PropertyVetoException {
    fireVetoableChange("action", _action, x);
    _action = x;
  }

  public Message getActivator() { return _activator; }
  public void setActivator(Message x) throws PropertyVetoException {
    fireVetoableChange("activator", _activator, x);
     _activator = x;
  }



  public Vector getMessage() { return _message; }
  public void setMessage(Vector x) throws PropertyVetoException {
    if (_message == null) _message = new Vector();
    fireVetoableChangeNoCompare("message", _message, x);
     _message = x;
  }
  public void addMessage(Message x) throws PropertyVetoException {
    if (_message == null) _message = new Vector();
    fireVetoableChange("message", _message, x);
    _message.addElement(x);
  }
  public void removeMessage(Message x) throws PropertyVetoException {
    if (_message == null) return;
    fireVetoableChange("message", _message, x);
     _message.removeElement(x);
  }

  public Vector getPredecessor() { return _predecessor; }
  public void setPredecessor(Vector x) throws PropertyVetoException {
    if (_predecessor == null) _predecessor = new Vector();
    fireVetoableChangeNoCompare("predecessor", _predecessor, x);
     _predecessor = x;
  }
  public void addPredecessor(Message x) throws PropertyVetoException {
    if (_predecessor == null) _predecessor = new Vector();
    fireVetoableChange("predecessor", _predecessor, x);
    _predecessor.addElement(x);
  }
  public void removePredecessor(Message x) throws PropertyVetoException {
    if (_predecessor == null) return;
    fireVetoableChange("predecessor", _predecessor, x);
     _predecessor.removeElement(x);
  }

  public ClassifierRole getSender() { return _sender; }
  public void setSender(ClassifierRole x) throws PropertyVetoException {
    fireVetoableChange("sender", _sender, x);
     _sender = x;
  }

  public ClassifierRole getReceiver() { return _receiver; }
  public void setReceiver(ClassifierRole x) throws PropertyVetoException {
    fireVetoableChange("receiver", _receiver, x);
     _receiver = x;
  }

  public AssociationRole getAssociationRole() { return _associationRole; }
  public void setAssociationRole(AssociationRole x) throws PropertyVetoException {
    fireVetoableChange("associationRole", _associationRole, x);
     _associationRole = x;
  }

  public Vector getInteraction() { return _interaction; }
  public void setInteraction(Vector x) throws PropertyVetoException {
    if (_interaction == null) _interaction = new Vector();
    fireVetoableChangeNoCompare("interaction", _interaction, x);
     _interaction = x;
  }
  public void addInteraction(Interaction x) throws PropertyVetoException {
    if (_interaction == null) _interaction = new Vector();
    if (_interaction.contains(x)) return;
    fireVetoableChange("interaction", _interaction, x);
    _interaction.addElement(x);
    x.addMessage(this);
  }
  public void removeInteraction(Interaction x) throws PropertyVetoException {
    if (_interaction == null) return;
    if (!_interaction.contains(x)) return;
    fireVetoableChange("interaction", _interaction, x);
     _interaction.removeElement(x);
     x.removeMessage(this);
  }

  public Object prepareForTrash() throws PropertyVetoException {
    //setInteraction(null);
    setSender(null);
    setReceiver(null);
    setPredecessor(null);
    setActivator(null);
    setAssociationRole(null);
    //needs-more-work
    return super.prepareForTrash();
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work
    super.recoverFromTrash(momento);
  }


  static final long serialVersionUID = 6766833621483911685L;
}
