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

//needs-more-work: switch to Vector 

public class Collaboration extends NamespaceImpl {
  public Operation _representedOperation;
  //needs-more-work: should it be multiple Interactions?
  public Interaction _interaction = new Interaction("Interaction");
  public Vector _constrainingElement;

  public Collaboration() { }
  public Collaboration(Name name) {
    super(name);
  }
  public Collaboration(String nameStr) { this(new Name(nameStr)); }


  public Operation getRepresentedOperation() {
    return _representedOperation;
  }
  public void setRepresentedOperation(Operation x) throws PropertyVetoException {
    fireVetoableChange("representedOperation", _representedOperation, x);
    _representedOperation = x;
  }

  public Interaction getInteraction() { return _interaction; }
  public void setInteraction(Interaction x) throws PropertyVetoException {
    fireVetoableChange("interaction", _interaction, x);
    _interaction = x;
  }

  public Vector getConstrainingElement() {
    return _constrainingElement;
  }
  public void setConstrainingElement(Vector x)
       throws PropertyVetoException {
    fireVetoableChangeNoCompare("constrainingElement", _constrainingElement, x);
    _constrainingElement = x;
  }
  public void addConstrainingElement(ModelElement x) 
  throws PropertyVetoException {
    if (_constrainingElement == null) _constrainingElement = new Vector();
    fireVetoableChange("constrainingElement", _constrainingElement, x);
    _constrainingElement.addElement(x);
  }
  public void removeConstrainingElement(ModelElement x) throws PropertyVetoException {
    if (_constrainingElement == null) return;
    fireVetoableChange("constrainingElement", _constrainingElement, x);
    _constrainingElement.removeElement(x);
  }

  static final long serialVersionUID = -9038991908839832965L;
}
