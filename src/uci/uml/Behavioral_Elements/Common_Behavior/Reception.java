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


public class Reception extends BehavioralFeature {
  public boolean _isPolymorphic;
  public Uninterpreted _specification;
  public Signal _signal;

  public Reception() { }

  public boolean getIsPolymorphic() { return _isPolymorphic; }
  public void setIsPolymorphic(boolean x) throws PropertyVetoException {
    fireVetoableChange("isPolymorphic",
		       _isPolymorphic ? Boolean.TRUE : Boolean.FALSE,
		       x ? Boolean.TRUE : Boolean.FALSE);
    _isPolymorphic = x;
  }

  public Uninterpreted getSpecification() { return _specification; }
  public void setSpecification(Uninterpreted x) throws PropertyVetoException {
    fireVetoableChange("specification", _specification, x);
    _specification = x;
  }

  public Signal getSignal() { return _signal; }
  public void setSignal(Signal x) throws PropertyVetoException {
    fireVetoableChange("signal", _signal, x);
    _signal = x;
  }

  static final long serialVersionUID = -7875946899393035281L;
}
