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


package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Foundation.Data_Types.*;

/** By default a Transition is in the same Namespace as its
 *  StateMachine */

public class Transition extends ModelElementImpl {
  public Guard _guard;
  public ActionSequence _effect;
  public State _state;
  public Event _trigger;
  public StateMachine _stateMachine;
  public StateVertex _source;
  public StateVertex _target;

  public Transition() { }
  public Transition(Name name) { super(name); }
  public Transition(Name name, StateVertex src, StateVertex tar) {
    super(name);
    try {
      setSource(src);
      setTarget(tar);
    }
    catch (PropertyVetoException pve) { }
  }
  public Transition(StateVertex src, StateVertex tar) {
    super();
    try {
      setSource(src);
      setTarget(tar);
    }
    catch (PropertyVetoException pve) { }
  }
  public Transition(String nameStr) { super(new Name(nameStr)); }

  public Guard getGuard() { return _guard; }
  public void setGuard(Guard x) throws PropertyVetoException {
    if (_guard == x) return;
    fireVetoableChange("guard", _guard, x);
    //if (_guard != null) _guard.setGuard(null);
    _guard = x;
    //if (_guard != null) _guard.setGuard(this);
    if (x != null) x.setNamespace(getNamespace());    
  }

  public ActionSequence getEffect() { return _effect; }
  public void setEffect(ActionSequence x) throws PropertyVetoException {
    if (_effect == x) return;
    fireVetoableChange("effect", _effect, x);
    //if (_effect != null) _effect.setTransition(null);
    _effect = x;
    //if (_effect != null) _effect.setTransition(this);
    if (x != null) x.setNamespace(getNamespace());    
  }

  public State getState() { return _state; }
  public void setState(State x) throws PropertyVetoException {
    if (_state == x) return;
    fireVetoableChange("state", _state, x);
    State oldState = _state;
    _state = x;
    if (oldState != null) oldState.removeInternalTransition(this);
    if (_state != null) _state.addInternalTransition(this);
  }

  public Event getTrigger() { return _trigger; }
  public void setTrigger(Event x) throws PropertyVetoException {
    fireVetoableChange("trigger", _trigger, x);
    _trigger = x;
  }

  public StateMachine getStateMachine() { return _stateMachine; }
  public void setStateMachine(StateMachine x) throws PropertyVetoException {
    if (_stateMachine == x) return;
    fireVetoableChange("stateMachine", _stateMachine, x);
    StateMachine oldStateMachine = _stateMachine;
    _stateMachine = x;
    if (oldStateMachine != null) oldStateMachine.removeTransition(this);
    if (_stateMachine != null) _stateMachine.addTransition(this);
  }

  public StateVertex getSource() { return _source; }
  public void setSource(StateVertex x) throws PropertyVetoException {
    if (_source == x) return;
    fireVetoableChange("source", _source, x);
    StateVertex oldSource = _source;
    _source = x;
    if (oldSource != null) oldSource.removeOutgoing(this);
    if (_source != null) _source.addOutgoing(this);
  }

  public StateVertex getTarget() { return _target; }
  public void setTarget(StateVertex x) throws PropertyVetoException {
    if (_target == x) return;
    fireVetoableChange("target", _target, x);
    StateVertex oldTarget = _target;
    _target = x;
    if (oldTarget != null) oldTarget.removeIncoming(this);
    if (_target != null) _target.addIncoming(this);
  }

  public Object prepareForTrash() throws PropertyVetoException {
    setStateMachine(null);
    setSource(null);
    setTarget(null);
    //needs-more-work
    return super.prepareForTrash();
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work
    super.recoverFromTrash(momento);
  }

  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = "Transition " + (getName() == null ? "(anon)" : getName().getBody());
    if (_source != null) s += " from " + _source.getName().getBody();
    if (_target != null) s += " to " + _target.getName().getBody();

    if (_trigger != null) s += " " + _trigger.dbgString();
    if (_guard != null) s += " [" + _guard.dbgString() + "]";
    if (_effect != null) s += " /" + _effect.dbgString();
    return s;
  }

  static final long serialVersionUID = 5131320930672761568L;
}
