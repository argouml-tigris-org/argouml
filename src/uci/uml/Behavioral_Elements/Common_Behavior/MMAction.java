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


public class MMAction extends ModelElementImpl {
  public Expression _recurrence;
  public ObjectSetExpression _target;
  public boolean _isAsynchronous;
  public String _script;
  //public Argument _actualArgument[];
  public Request _request;
  public ActionSequence _actionSequence;
  public Vector _message;

  public MMAction() { }
  public MMAction(Name name) { super(name); }
  public MMAction(String nameStr) { super(new Name(nameStr)); }

  public Expression getRecurrence() { return _recurrence; }
  public void setRecurrence(Expression x) throws PropertyVetoException {
    fireVetoableChange("recurrence", _recurrence, x);
    _recurrence = x;
  }

  public Request getRequest() { return _request; }
  public void setRequest(Request x) throws PropertyVetoException {
    fireVetoableChange("request", _request, x);
    _request = x;
  }

  public ActionSequence getActionSequence() { return _actionSequence; }
  public void setActionSequence(ActionSequence x) throws PropertyVetoException {
    if (_actionSequence == x) return;
    fireVetoableChange("actionSequence", _actionSequence, x);
    if (_actionSequence != null) _actionSequence.removeAction(this);
    _actionSequence = x;
    if (_actionSequence != null) _actionSequence.addAction(this);
    setNamespace(x.getNamespace());    
  }

  public Vector getMessage() { return _message; }
  public void setMessage(Vector x) throws PropertyVetoException {
    if (_message == null) _message = new Vector();
    fireVetoableChange("message", _message, x);
    _message = x;
  }
  public void addMessage(Message x) throws PropertyVetoException {
    if (_message == null) _message = new Vector();
    fireVetoableChange("message", _message, x);
    _message.addElement(x);
  }
  public void removeMessage(Message x) throws PropertyVetoException {
    if (_message == null) return;
    fireVetoableChange("message", _message, x);
    _message.removeElement(x);
  }

  public ObjectSetExpression getTarget() { return _target; }
  public void setTarget(ObjectSetExpression x) throws PropertyVetoException {
    fireVetoableChange("target", _target, x);
    _target = x;
  }

  public boolean getIsAsynchronous() { return _isAsynchronous; }
  public void setIsAsynchronous(boolean x) throws PropertyVetoException {
    fireVetoableChange("isAsynchronous",
		       _isAsynchronous ? Boolean.TRUE : Boolean.FALSE,
		       x ? Boolean.TRUE : Boolean.FALSE);
    _isAsynchronous = x;
  }

  public String getScript() { return _script; }
  public void setScript(String x) throws PropertyVetoException {
    fireVetoableChange("script", _script, x);
    _script = x;
  }

  public String getOCLTypeStr() {
    return "Action";
    // drop the MM prefix, it is just used
    // to prevent confusion with java.lang.Class
  }

  static final long serialVersionUID = -6867331064459335863L;
}

