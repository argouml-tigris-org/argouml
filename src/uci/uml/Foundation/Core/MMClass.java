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




// Source file: Foundation/Core/MMClass.java

package uci.uml.Foundation.Core;

import java.util.*;
import java.beans.*;

import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.Stereotype;

public class MMClass extends Classifier {
  public boolean _isActive;

  public MMClass() { }
  public MMClass(Name name) { super(name); }
  public MMClass(String nameStr) { super(new Name(nameStr)); }


  /** Change the name of this class as well as all constructor names. */
  public void setName(Name newName) throws PropertyVetoException {
    Name oldName = getName();
    super.setName(newName);
    renameConstructors(oldName);
  }

  /** Rename all constructors from oldName to the current class name. */
  protected void renameConstructors (Name oldName) {
    Operation aConstructor = null;
    do {
      aConstructor = (Operation) findBehavioralFeature(oldName);
      // Make sure we're only renaming constructors (this test is needed
      // when the class name is "" and there's an unnamed
      // non-constructor operation in the class.)
      if (aConstructor != null
	  && aConstructor.containsStereotype(Stereotype.CONSTRUCTOR)) {
	try {
	  aConstructor.setName(getName());
	}
	catch (PropertyVetoException e) {
	  // Ignore, but make sure we get out of the loop
	  aConstructor = null;
	}
      }
    } while (aConstructor != null);
  }


  public boolean getIsActive(){ return _isActive; }
  public void setIsActive(boolean x) throws PropertyVetoException {
    fireVetoableChange("isActive", _isActive, x);
    _isActive = x;
  }

  public String getOCLTypeStr() {
    return "Class";
    // drop the MM prefix, it is just used
    // to prevent confusion with java.lang.Class
  }

  static final long serialVersionUID = 8412807590870729471L;
}
