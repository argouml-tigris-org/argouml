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
import uci.uml.Foundation.Data_Types.*;

/** By default, a StateVertex is in the same Namespace as it's
 *  CompositeState. */

public abstract class StateVertex extends ModelElementImpl {
  public CompositeState _parent;
  public Vector _outgoing;
  public Vector _incoming;

  public StateVertex() { }
  public StateVertex(Name name) { super(name); }
  public StateVertex(String nameStr) { super(new Name(nameStr)); }

  public CompositeState getParent() { return _parent; }
  public void setParent(CompositeState x) throws PropertyVetoException {
    if (_parent == x) return;
    fireVetoableChange("parent", _parent, x);
    CompositeState oldParent = _parent;
    if (oldParent != null) {
      _parent = null;
      oldParent.removeSubstate(this);
    }
    _parent = x;
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

  public Object prepareForTrash() throws PropertyVetoException {
    setParent(null);
    return super.prepareForTrash();
    //needs-more-work
  }

  public Vector alsoTrash() {
    Vector res = super.alsoTrash();
    if (_incoming != null) {
      for (int i = 0; i < _incoming.size(); i++)
	res.addElement(_incoming.elementAt(i));
    }
    if (_outgoing != null) {
      for (int i = 0; i < _outgoing.size(); i++)
	res.addElement(_outgoing.elementAt(i));
    }
    return res;
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work
    super.recoverFromTrash(momento);
  }

  static final long serialVersionUID = -8081077916116617988L;
}
