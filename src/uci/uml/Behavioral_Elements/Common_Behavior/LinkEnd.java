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




package uci.uml.Behavioral_Elements.Common_Behavior;

import java.util.*;
import java.beans.*;
import uci.uml.Foundation.Core.*; 
import uci.uml.Foundation.Data_Types.Name;

// needs-more-work

public class LinkEnd extends ModelElementImpl {

  public Link _link;
  public AssociationEnd _associationEnd = null;
  public Instance _instance;
  public Classifier _type;
  
  public LinkEnd() { }
  public LinkEnd(Name name) { super(name); }
  public LinkEnd(String nameStr) { super(new Name(nameStr)); }
  public LinkEnd(Instance i) {
    super();
    try { setInstance(i); }
    catch (PropertyVetoException pce) { }
  }
  
  
  public Link getLink() { return _link; }
  
  public Instance getInstance() { return _instance; }
  
  public AssociationEnd getAssociationEnd() { return _associationEnd; }
  
  public void setLink(Link x) throws PropertyVetoException {
    fireVetoableChange("link", _link, x);
    _link = x;
  }
  
  public void setInstance(Instance x) throws PropertyVetoException {
    fireVetoableChange("instance", _instance, x);
    _instance = x;
  }
  
  public void setAssociationEnd(AssociationEnd x) throws PropertyVetoException {
    fireVetoableChange("associationEnd", _associationEnd, x);
    _associationEnd = x;
  }

  static final long serialVersionUID = 7327255478564468057L;

}
