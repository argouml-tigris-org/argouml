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


// Source file: uci/uml/Behavioral_Elements/Common_Behavior/MMAction.java

package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Model_Management.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Collaborations.*;


public class MMAction extends ModelElementImpl {
  public Expression _recurrence;
  public ObjectSetExpression _target;
  public Boolean _isAsynchronous;
  public String _script;
  //public Argument _actualArgument[];
  public Request _request;
  //public ActionSequence m_ActionSequence;
  public ActionSequence _actionSequence;
  //% public Message _message[];
  public Vector _message;
    
  public MMAction() { }
  
  public Expression getRecurrence() { return _recurrence; }
  public void setRecurrence(Expression x) {
    _recurrence = x;
  }

  public Request getRequest() { return _request; }
  public void setRequest(Request x) {
    _request = x;
  }

  public ActionSequence getActionSequence() { return _actionSequence;
  }
  public void setActionSequence(ActionSequence x) {
    _actionSequence = x;
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

  public ObjectSetExpression getTarget() { return _target; }
  public void setTarget(ObjectSetExpression x) {
    _target = x;
  }

  public Boolean getIsAsynchronous() { return _isAsynchronous; }
  public void setIsAsynchronous(Boolean x) {
    _isAsynchronous = x;
  }

  public String getScript() { return _script; }
  public void setScript(String x) {
    _script = x;
  }
  
}
  
