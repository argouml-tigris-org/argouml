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




package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Data_Types.Name;

/** By default a Generalization is in the Namespace of the sub class. */
public class Generalization extends ModelElementImpl {
  private Name _discriminator;
  public GeneralizableElement _subtype;
  public GeneralizableElement _supertype;

  public Generalization() { }
  public Generalization(Name name) { super(name); }
  public Generalization(String nameStr) { super(new Name(nameStr)); }
  public Generalization(GeneralizableElement sub, GeneralizableElement sup)
  throws PropertyVetoException {
    super();
    setSubtype(sub);
    setSupertype(sup);
    sub.addGeneralization(this);
    sup.addSpecialization(this);
  }

  public Name getDiscriminator() { return _discriminator; }
  public void setDiscriminator(Name x) throws PropertyVetoException {
    fireVetoableChange("discriminator", _discriminator, x);
    _discriminator = x;
  }
  public GeneralizableElement getSubtype() { return _subtype; }
  public void setSubtype(GeneralizableElement x) throws PropertyVetoException {
    if (_subtype == x) return;
    fireVetoableChange("subtype", _subtype, x);
    if (_subtype != null) _subtype.removeGeneralization(this);
    _subtype = x;
    if (_subtype != null) _subtype.addGeneralization(this);
    //if (x != null) setNamespace(x.getNamespace());
  }
  public GeneralizableElement getSupertype() { return _supertype; }
  public void setSupertype(GeneralizableElement x) throws PropertyVetoException {
    if (_supertype == x) return;
    fireVetoableChange("supertype", _supertype, x);
    if (_supertype != null) _supertype.removeSpecialization(this);
    _supertype = x;
    if (_supertype != null) _supertype.addSpecialization(this);
  }

  public Object prepareForTrash() throws PropertyVetoException {
    setSupertype(null);
    setSubtype(null);
    return super.prepareForTrash();
    //needs-more-work
  }

  public void recoverFromTrash(Object momento) throws PropertyVetoException {
    // needs-more-work
    super.recoverFromTrash(momento);
  }

  static final long serialVersionUID = -9116891000303758417L;
}

