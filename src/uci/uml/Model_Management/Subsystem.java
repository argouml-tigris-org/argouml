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
import java.beans.*;

import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;


public class Subsystem extends Classifier implements MMPackage {
  public boolean _isInstantiable;

  public Subsystem() { }
  public Subsystem(Name name) { super(name); }
  public Subsystem(Name name, boolean isInstantiable) {
    super(name);
    try { setIsInstantiable(isInstantiable); }
    catch (PropertyVetoException pve) { }
  }
  public Subsystem(String nameStr) { super(new Name(nameStr)); }

  public boolean getIsInstantiable() { return _isInstantiable; }
  public void setIsInstantiable(boolean x) throws PropertyVetoException {
    fireVetoableChange("isInstantiable",
		       _isInstantiable ? Boolean.TRUE : Boolean.FALSE,
		       x ? Boolean.TRUE : Boolean.FALSE);
    _isInstantiable = x;
  }

  ////////////////////////////////////////////////////////////////
  // MMPackage implementation

  // needs-more-work: these should use ElementReferences

  public Vector _referencedElement;

  public Vector getReferencedElement() { return _referencedElement; }
  public void setReferencedElement(Vector x) throws PropertyVetoException {
    if (_referencedElement == null) _referencedElement = new Vector();
    fireVetoableChangeNoCompare("referencedElement", _referencedElement, x);
    _referencedElement = x;
  }
  public void addReferencedElement(ModelElement x) throws PropertyVetoException {
    if (_referencedElement == null) _referencedElement = new Vector();
    fireVetoableChange("referencedElement", _referencedElement, x);
    _referencedElement.addElement(x);
  }
  public void removeReferencedElement(ModelElement x)
       throws PropertyVetoException {
    if (_referencedElement == null) return;
    fireVetoableChange("referencedElement", _referencedElement, x);
    _referencedElement.removeElement(x);
  }

  static final long serialVersionUID = 7804585279406663688L;
}
