// Source file: f:/jr/projects/uml/Behavioral_Elements/Collaborations/Message.java

package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;

import uci.uml.Behavioral_Elements.Common_Behavior.Action;


public class Message {
  public Action _action;
  public Message _activator;
  //public Message m_Message[];
  //% public Message _message[];
  public Vector _message;
  //% public Message _predecessor[];
  public Vector _predecessor;
  public ClassifierRole _sender;
  public ClassifierRole _receiver;
  //% public Interaction _interaction[];
  public Vector _interaction;
  
  public Message() { }

  public Action getAction() { return _action; }
  public void setAction(Action x) {
     _action = x;
  }

  public Message getActivator() { return _activator; }
  public void setActivator(Message x) {
     _activator = x;
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

  public Vector getPredecessor() { return _predecessor; }
  public void setPredecessor(Vector x) {
     _predecessor = x;
  }
  public void addPredecessor(Message x) {
    if (_predecessor == null) _predecessor = new Vector();
    _predecessor.addElement(x);
  }
  public void removePredecessor(Message x) {
     _predecessor.removeElement(x);
  }

  public ClassifierRole getSender() { return _sender; }
  public void setSender(ClassifierRole x) {
     _sender = x;
  }

  public ClassifierRole getReceiver() { return _receiver; }
  public void setReceiver(ClassifierRole x) {
     _receiver = x;
  }

  public Vector getInteraction() { return _interaction; }
  public void setInteraction(Vector x) {
     _interaction = x;
  }
  public void addInteraction(Interaction x) {
    if (_interaction == null) _interaction = new Vector();
    _interaction.addElement(x);
  }
  public void removeInteraction(Interaction x) {
     _interaction.removeElement(x);
  }
  
}
