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


package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Collaborations.*;

/** By default ActionSequences are in the same Namespace as the
 *  Transition or State that they are attached to.  And Actions in an
 *  ActionSequence are in the same Namespace as the ActionSequence. */

public class ActionSequence extends ModelElementImpl {
  //public MMAction _action[];
  public Vector _action;

  public ActionSequence() { }
  public ActionSequence(Name name) { super(name); }
  public ActionSequence(String actionNameStr) {
    super();
    try { addAction(new MMAction(actionNameStr)); }
    catch (PropertyVetoException pve) { }
  }

  public Vector getAction() { return _action; }
  public void setAction(Vector x) throws PropertyVetoException {
    if (_action == null) _action = new Vector();
    fireVetoableChange("action", _action, x);
    _action = x;
    java.util.Enumeration enum = _action.elements();
    while (enum.hasMoreElements()) {
      MMAction a = (MMAction) enum.nextElement();
      a.setActionSequence(this);
      a.setNamespace(getNamespace());
    }
  }
  public void addAction(MMAction x) throws PropertyVetoException {
    if (_action == null) _action = new Vector();
    fireVetoableChange("action", _action, x);
    _action.addElement(x);
    x.setActionSequence(this);
    x.setNamespace(getNamespace());
  }
  public void removeAction(MMAction x) throws PropertyVetoException {
    if (_action == null) return;
    fireVetoableChange("action", _action, x);
    _action.removeElement(x);
    x.setActionSequence(null);
  }

  static final long serialVersionUID = -1266775602257025737L;
}
