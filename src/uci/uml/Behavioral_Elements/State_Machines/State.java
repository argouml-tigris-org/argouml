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


// Source file: Behavioral_Elements/State_Machines/State.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;

import uci.uml.Behavioral_Elements.Common_Behavior.ActionSequence;
import uci.uml.Foundation.Core.Attribute;


public class State extends StateVertex {
  public ActionSequence _entry;
  public ActionSequence _exit;
  //% public Event _deferredEvent[];
  public Vector _deferredEvent;
  public StateMachine _stateMachine;
  //% public Attribute _stateVariable[];
  public Vector _stateVariable;
  //% public ClassifierInState _classifierInState[];
  public Vector _classifierInState;
  //% public Transition _internalTransition[];
  public Vector _internalTransition;
    
  public State() { }

  public ActionSequence getEntry() { return _entry; }
  public void setEntry(ActionSequence x) {
    _entry = x;
  }
  
  public ActionSequence getExit() { return _exit; }
  public void setExit(ActionSequence x) {
    _exit = x;
  }

  public Vector getDeferredEvent() { return _deferredEvent; }
  public void setDeferredEvent(Vector x) {
    _deferredEvent = x;
  }
  public void addDeferredEvent(Event x) {
    if (_deferredEvent == null) _deferredEvent = new Vector();
    _deferredEvent.addElement(x);
  }
  public void removeDeferredEvent(Event x) {
    _deferredEvent.removeElement(x);
  }

  public StateMachine getStateMachine() { return _stateMachine; }
  public void setStateMachine(StateMachine x) {
    _stateMachine = x;
  }

  public Vector getStateVariable() { return _stateVariable; }
  public void setStateVariable(Vector x) {
    _stateVariable = x;
  }
  public void addStateVariable(Attribute x) {
    if (_stateVariable == null) _stateVariable = new Vector();
    _stateVariable.addElement(x);
  }
  public void removeStateVariable(Attribute x) {
    _stateVariable.removeElement(x);
  }

  public Vector getClassifierInState() {
    return _classifierInState; }
  public void setClassifierInState(Vector x) {
    _classifierInState = x;
  }
  public void addClassifierInState(ClassifierInState x) {
    if (_classifierInState == null) _classifierInState = new Vector();
    _classifierInState.addElement(x);
  }
  public void removeClassifierInState(ClassifierInState x) {
    _classifierInState.removeElement(x);
  }

  public Vector getInternalTransition() {
    return _internalTransition;
  }
  public void setInternalTransition(Vector x) {
    _internalTransition = x;
  }
  public void addInternalTransition(Transition x) {
    if (_internalTransition == null) _internalTransition = new
				       Vector();
    _internalTransition.addElement(x);
  }
  public void removeInternalTransition(Transition x) {
    _internalTransition.removeElement(x);
  }
  
}
