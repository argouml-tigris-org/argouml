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
// INCLUDING LOST PROFITS,s ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.




package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Data_Types.Name;

/** By default a Realization is in the Namespace of the sub class. */
public class Realization extends ModelElementImpl {
  public Classifier _subtype;
  public Classifier _supertype;

  public Realization() { }
  public Realization(Name name) { super(name); }
  public Realization(String nameStr) { super(new Name(nameStr)); }
  public Realization(Classifier sub, Classifier sup)
  throws PropertyVetoException {
    super();
    setSubtype(sub);
    setSupertype(sup);
    sub.addSpecification(this);
    sup.addRealization(this);
  }

  public Classifier getSubtype() { return _subtype; }
  public void setSubtype(Classifier x) throws PropertyVetoException {
    fireVetoableChange("subtype", _subtype, x);
    _subtype = x;
    //if (x != null) setNamespace(x.getNamespace());
  }
  public Classifier getSupertype() { return _supertype; }
  public void setSupertype(Classifier x) throws PropertyVetoException {
    fireVetoableChange("supertype", _supertype, x);
    _supertype = x;
  }


}
