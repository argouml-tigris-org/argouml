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

import uci.uml.Foundation.Core.BehavioralFeature;


public class MMException extends Signal {
  public Vector _context;

  public MMException() { }

  public Vector getContext() { return _context; }
  public void setContext(Vector x) throws PropertyVetoException {
    if (_context == null) _context = new Vector();
    fireVetoableChange("context", _context, x);
    _context = x;
  }
  public void addContext(BehavioralFeature x) throws PropertyVetoException {
    if (_context == null) _context = new Vector();
    fireVetoableChange("context", _context, x);
    _context.addElement(x);
  }
  public void removeContext(BehavioralFeature x) throws PropertyVetoException {
    if (_context == null) return;
    fireVetoableChange("context", _context, x);
    _context.removeElement(x);
  }

  public String getOCLTypeStr() {
    return "Exception";
    // drop the MM prefix, it is just used
    // to prevent confusion with java.lang.Class
  }

  static final long serialVersionUID = 5599355010204479490L;
}

