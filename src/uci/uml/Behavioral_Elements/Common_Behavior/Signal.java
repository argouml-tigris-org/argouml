// Source file: f:/jr/projects/uml/Behavioral_Elements/Common_Behavior/Signal.java

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;


public class Signal extends GeneralizableElementImpl implements Request {
  //% public Reception _reception[];
  public Vector _reception;
  //% public Parameter _parameter[];
  public Vector _parameter;
  //% public SignalEvent _occurrence[];
  public Vector _occurrence;
  //% public SendAction _raises[];
  public Vector _raises;
  //public SendAction Raises[];
    
  public Signal() { }

  public Vector getReception() { return _reception; }
  public void setReception(Vector x) {
    _reception = x;
  }
  public void addReception(Reception x) {
    if (_reception == null) _reception = new Vector();
    _reception.addElement(x);
  }
  public void removeReception(Reception x) {
    _reception.removeElement(x);
  }

  public Vector getParameter() { return _parameter; }
  public void setParameter(Vector x) {
    _parameter = x;
  }
  public void addParameter(Parameter x) {
    if (_parameter == null) _parameter = new Vector();
    _parameter.addElement(x);
  }
  public void removeParameter(Parameter x) {
    _parameter.removeElement(x);
  }

  public Vector getOccurrence() { return _occurrence; }
  public void setOccurrence(Vector x) {
    _occurrence = x;
  }
  public void addOccurrence(SignalEvent x) {
    if (_occurrence == null) _occurrence = new Vector();
    _occurrence.addElement(x);
  }
  public void removeOccurrence(SignalEvent x) {
    _occurrence.removeElement(x);
  }

  public Vector getRaises() { return _raises; }
  public void setRaises(Vector x) {
    _raises = x;
  }
  public void addRaises(SendAction x) {
    if (_raises == null) _raises = new Vector();
    _raises.addElement(x);
  }
  public void removeRaises(SendAction x) {
    _raises.removeElement(x);
  }


  ////////////////////////////////////////////////////////////////
  // Request implementation 

  //public MessageInstance _messageInstance[];
  //% public Action _action[];
  public Vector _action;
  
  //public Vector getMessageInstance() {
  //  return _messageInstance;
  //}
  //public void setMessageInstance(Vector x) {
  //  _messageInstance = x;
  //}

  public Vector getAction() { return _action; }
  public void setAction(Vector x) {
    _action = x;
  }
  public void addAction(Action x) {
    if (_action == null) _action = new Vector();
    _action.addElement(x);
  }
  public void removeAction(Action x) {
    _action.removeElement(x);
  }
  
}
