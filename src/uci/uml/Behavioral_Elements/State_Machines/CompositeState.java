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

import uci.uml.Foundation.Data_Types.*;

public class CompositeState extends State {
  public boolean _isConcurrent;
  //% public StateVertex _substate[];
  public Vector _substate = new Vector();

  public CompositeState() { }
  public CompositeState(Name name) { super(name); }
  public CompositeState(String nameStr) { super(new Name(nameStr)); }

  public boolean getIsConcurrent() { return _isConcurrent; }
  public void setIsConcurrent(boolean x) throws PropertyVetoException {
    fireVetoableChange("isConcurrent",
		       _isConcurrent ? Boolean.TRUE : Boolean.FALSE,
		       x ? Boolean.TRUE : Boolean.FALSE);
    _isConcurrent = x;
  }

  public Vector getSubstate() { return _substate; }
  public void setSubstate(Vector x) throws PropertyVetoException {
    if (_substate == null) _substate = new Vector();
    fireVetoableChangeNoCompare("substate", _substate, x);
    _substate = x;
    java.util.Enumeration enum = _substate.elements();
    while (enum.hasMoreElements()) {
      StateVertex sv = (StateVertex) enum.nextElement();
      sv.setNamespace(getNamespace());
    }
  }
  public void addSubstate(StateVertex x) throws PropertyVetoException {
    if (_substate == null) _substate = new Vector();
    else if (_substate.contains(x)) return;
    fireVetoableChange("substate", _substate, x);
    _substate.addElement(x);
    x.setNamespace(getNamespace());
    //     CompositeState oldParent = x.getParent();
    //     if (oldParent != this) {
    //       if (oldParent != null) oldParent.removeSubstate(x);
    x.setParent(this);
    //     }
    if (x instanceof State)
      ((State)x).setStateMachine(getStateMachine());
  }
  public void removeSubstate(StateVertex x) throws PropertyVetoException {
    if (_substate == null) return;
    else if (!_substate.contains(x)) return;
    fireVetoableChange("substate", _substate, x);
    _substate.removeElement(x);
    x.setParent(null);
  }

  public Vector alsoTrash() {
    if (getParent() == null) return getSubstate();
    else return new Vector();
  }

  public Object prepareForTrash() throws PropertyVetoException {
    CompositeState enclosing = getParent();
    if (enclosing != null) {
      int size = _substate.size();
      for (int i = 0; i < size; i++) {
	StateVertex sv = (StateVertex) _substate.elementAt(i);
	sv.setParent(enclosing);
      }
    }
    super.prepareForTrash();
    return null;
    //needs-more-work: remember old substates
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work: restore old substates
  }

  ////////////////////////////////////////////////////////////////
  // debugging

  public String dbgString() {
    String s = "  CompositeState " +
      (getName() == null?"(anon)":getName().getBody());
    s += "   {\n";
    java.util.Enumeration subs = _substate.elements();
    while (subs.hasMoreElements()) {
      StateVertex sv = (StateVertex) subs.nextElement();
      s += sv.dbgString() + "\n";
    }
    s += "  }\n";
    return s;
  }
  static final long serialVersionUID = 4852498284812300009L;
}
