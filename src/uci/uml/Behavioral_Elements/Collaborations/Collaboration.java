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




// Source file: uci/uml/Behavioral_Elements/Collaborations/Collaboration.java

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
  //public AssociationRole /ownedElement[];
  public Operation _representedOperation;
  //public ClassifierRole /ownedElement[];
  public Vector _interaction;
  public Vector _constrainingElement;
    
  public Collaboration() { }
  public Collaboration(Name name) { super(name); }
  public Collaboration(String nameStr) { super(new Name(nameStr)); }


  public Operation getRepresentedOperation() {
    return _representedOperation;
  }
  public void setRepresentedOperation(Operation x) throws PropertyVetoException {
    fireVetoableChange("representedOperation", _representedOperation, x);
    _representedOperation = x;
  }

  public Vector getInteraction() { return _interaction; }
  public void setInteraction(Vector x) throws PropertyVetoException {
    fireVetoableChange("interaction", _interaction, x);
    _interaction = x;
  }
  public void addInteraction(Interaction x) throws PropertyVetoException {
    if (_interaction == null) _interaction = new Vector();
    fireVetoableChange("interaction", _interaction, x);
    _interaction.addElement(x);
  }
  public void removeInteraction(Interaction x) throws PropertyVetoException {
    if (_interaction == null) return;
    fireVetoableChange("interaction", _interaction, x);
    _interaction.removeElement(x);
  }

  public Vector getConstrainingElement() {
    return _constrainingElement;
  }
  public void setConstrainingElement(Vector x)
       throws PropertyVetoException {
    fireVetoableChange("constrainingElement", _constrainingElement, x);
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
  

}
