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

package uci.uml.Behavioral_Elements.Use_Cases;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Core.Classifier;
import uci.uml.Foundation.Data_Types.*;


public class UseCase extends Classifier {
  public Vector _extensionPoint = new Vector();

  public UseCase() { }
  public UseCase(Name name) { super(name); }
  public UseCase(String nameStr) { super(new Name(nameStr)); }

  public Vector getExtensionPoint() { return _extensionPoint; }
  public void setExtensionPoint(Vector x) throws PropertyVetoException {
    if (_extensionPoint == null) _extensionPoint = new Vector();
    fireVetoableChangeNoCompare("extensionPoint", _extensionPoint, x);
    _extensionPoint = x;
  }
  public void addExtensionPoint(String x) throws PropertyVetoException {
    if (_extensionPoint == null) _extensionPoint = new Vector();
    fireVetoableChange("extensionPoint", _extensionPoint, x);
    _extensionPoint.addElement(x);
  }
  public void removeExtensionPoint(String x) throws PropertyVetoException {
    if (_extensionPoint == null) return;
    fireVetoableChange("extensionPoint", _extensionPoint, x);
    _extensionPoint.removeElement(x);
  }

  public String dbgString() {
    String s = "";
    Vector v;
    java.util.Enumeration enum;

    s += getOCLTypeStr() + "(" + getName().getBody().toString() + ")[";

    String stereos = dbgStereotypes();
    if (stereos != "") s += "\n" + stereos;

    String tags = dbgTaggedValues();
    if (tags != "") s += "\n" + tags;

    if ((v = getExtensionPoint()) != null) {
      s += "\n  extension points:";
      enum = v.elements();
      while (enum.hasMoreElements())
	s += "\n  | " + ((String)enum.nextElement()).toString();
    }
    s += "\n]";
    return s;
  }

  static final long serialVersionUID = -3250339412834176145L;
}

