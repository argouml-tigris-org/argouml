// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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




// Source file: f:/jr/projects/uml/Behavioral_Elements/State_Machines/StateVertex.java

package uci.uml.Behavioral_Elements.State_Machines;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

/** By default, a StateVertex is in the same Namespace as it's
 *  CompositeState. */

public abstract class StateVertex extends ModelElementImpl {
  public CompositeState _parent;
  //% public Transition _outgoing[];
  public Vector _outgoing;
  //% public Transition _incoming[];
  public Vector _incoming;
    
  public StateVertex() { }
  public StateVertex(Name name) { super(name); }
  public StateVertex(String nameStr) { super(new Name(nameStr)); }

  public CompositeState getParent() { return _parent; }
  public void setParent(CompositeState x) throws PropertyVetoException {
    if (_parent == x) return;
    fireVetoableChange("parent", _parent, x);
    CompositeState oldParent = _parent;
    _parent = x;
    if (oldParent != null) oldParent.removeSubstate(this);
    if (_parent != null) _parent.addSubstate(this);
  }

  public Vector getOutgoing() { return _outgoing; }
  public void setOutgoing(Vector x) throws PropertyVetoException {
    if (_outgoing == null) _outgoing = new Vector();
    fireVetoableChangeNoCompare("outgoing", _outgoing, x);
    _outgoing = x;
  }
  public void addOutgoing(Transition x) throws PropertyVetoException {
    if (_outgoing == null) _outgoing = new Vector();
    else if (_outgoing.contains(x)) return;
    fireVetoableChange("outgoing", _outgoing, x);
    _outgoing.addElement(x);
  }
  public void removeOutgoing(Transition x) throws PropertyVetoException {
    if (_outgoing == null) return;
    fireVetoableChange("outgoing", _outgoing, x);
    _outgoing.removeElement(x);
  }

  
  public Vector getIncoming() { return _incoming; }
  public void setIncoming(Vector x) throws PropertyVetoException {
    if (_incoming == null) _incoming = new Vector();
    fireVetoableChangeNoCompare("incoming", _incoming, x);
    _incoming = x;
  }
  public void addIncoming(Transition x) throws PropertyVetoException {
    if (_incoming == null) _incoming = new Vector();
    else if (_incoming.contains(x)) return;
    fireVetoableChange("incoming", _incoming, x);
    _incoming.addElement(x);
  }
  public void removeIncoming(Transition x) throws PropertyVetoException {
    if (_incoming == null) return;
    fireVetoableChange("incoming", _incoming, x);
    _incoming.removeElement(x);
  }

  public String dbgString() {
    String s = getClass().getName() + (getName() == null?"(anon)":getName().getBody());
    return s;
  }

}
