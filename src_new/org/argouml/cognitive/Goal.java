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

package org.argouml.cognitive;

import org.argouml.application.api.*;

public class Goal {

  // TODO: values

  ////////////////////////////////////////////////////////////////
  // constants
  public static final Goal UNSPEC = new Goal("goal.unspecified", 1);
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  protected String _name;
  protected int _priority;
  
  ////////////////////////////////////////////////////////////////
  // constructor
  public Goal(String n, int p) {
    setName(Argo.localize("Cognitive", n));
    setPriority(p);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public boolean equals(Object d2) {
    if (!(d2 instanceof Goal)) return false;
    return ((Goal)d2).getName().equals(getName());
  }
  
  public String getName() { return _name; }
  public void setName(String n) { _name = n; }
  public int getPriority() { return _priority; }
  public void setPriority(int p) { _priority = p; }

  public String toString() { return getName(); }
  
} /* end class Goal */
