// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


// Source file: f:/jr/projects/uml/Behavioral_Elements/Collaborations/Message.java

package uci.uml.Behavioral_Elements.Collaborations;

import java.util.*;

import uci.uml.Behavioral_Elements.Common_Behavior.MMAction;


public class Message {
  public MMAction _action;
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

  public MMAction getAction() { return _action; }
  public void setAction(MMAction x) {
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
