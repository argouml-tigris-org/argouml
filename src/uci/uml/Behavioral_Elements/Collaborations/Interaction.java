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

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

public class Interaction extends ModelElementImpl {
  public Vector _message;
  public Collaboration _context;
  public Vector _link;
  public Vector _instances;

  public Interaction() { }
  public Interaction(Name name) { super(name); }
  public Interaction(String nameStr) { super(new Name(nameStr)); }

  public Vector getMessage() { return _message; }
  public void setMessage(Vector x) throws PropertyVetoException {
    if (_message == null) _message = new Vector();
    fireVetoableChangeNoCompare("message", _message, x);
    _message = x;
  }
  public void addMessage(Message x) throws PropertyVetoException {
    if (_message == null) _message = new Vector();
    if (_message.contains(x)) return;
    fireVetoableChange("message", _message, x);
    _message.addElement(x);
    x.addInteraction(this);
  }
  public void removeMessage(Message x) throws PropertyVetoException {
    if (_message == null) return;
    if (!_message.contains(x)) return;
    fireVetoableChange("message", _message, x);
    _message.removeElement(x);
    x.removeInteraction(this);
  }

  public Collaboration getContext() { return _context; }
  public void setContext(Collaboration x) throws PropertyVetoException {
    if (_context == x) return;
    fireVetoableChange("context", _context, x);
    _context = x;
    setNamespace(_context);
  }

  public Vector getLink() { return _link; }
  public void setLink(Vector x) throws PropertyVetoException {
    if (_link == null) _link = new Vector();
    fireVetoableChangeNoCompare("link", _link, x);
    _link = x;
  }
  public void addLink(Link x) throws PropertyVetoException {
    if (_link == null) _link = new Vector();
    fireVetoableChange("link", _link, x);
    _link.addElement(x);
  }
  public void removeLink(Link x) throws PropertyVetoException {
    if (_link == null) return;
    fireVetoableChange("link", _link, x);
    _link.removeElement(x);
  }

  public Vector getInstances() { return _instances; }
  public void setInstances(Vector x) throws PropertyVetoException {
    if (_instances == null) _instances = new Vector();
    fireVetoableChangeNoCompare("instances", _instances, x);
    _instances = x;
  }
  public void addInstance(Instance x) throws PropertyVetoException {
    if (_instances == null) _instances = new Vector();
    fireVetoableChange("instance", _instances, x);
    _instances.addElement(x);
  }
  public void removeInstance(Instance x) throws PropertyVetoException {
    if (_instances == null) return;
    fireVetoableChange("instance", _instances, x);
    _instances.removeElement(x);
  }

  static final long serialVersionUID = -3378148253359414278L;
}
