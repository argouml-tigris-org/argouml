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


package uci.uml.Model_Management;

import java.util.*;
import java.beans.PropertyVetoException;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;

public class ElementOwnership implements java.io.Serializable {
  public VisibilityKind _visibility;
  public ModelElement _modelElement;
  public Namespace _namespace;

  public ElementOwnership() { }
  public ElementOwnership(Namespace n, VisibilityKind vk, ModelElement me) {
    this();
    //try {
      setNamespace(n);
      setVisibility(vk);
      setModelElement(me);
    //}
    //catch (PropertyVetoException pve) { }
  }

  public VisibilityKind getVisibility() { return _visibility; }
  public void setVisibility(VisibilityKind x) { //throws PropertyVetoException {
    //fireVetoableChange("visibility", _visibility, x);
    _visibility = x;
  }

  public ModelElement getModelElement() { return _modelElement; }
  public void setModelElement(ModelElement x) { //throws PropertyVetoException {
    //fireVetoableChange("modelElement", _modelElement, x);
    _modelElement = x;
  }

  public Namespace getNamespace() { return _namespace; }
  public void setNamespace(Namespace x) {// throws PropertyVetoException {
    //fireVetoableChange("namespace", _namespace, x);
    _namespace = x;
  }

  static final long serialVersionUID = -5143188352697231294L;
}
