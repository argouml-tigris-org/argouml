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



// File: WizNavigable.java
// Classes: WizNavigable
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import java.util.*;
import java.beans.*;
import javax.swing.*;

import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.ui.todo.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Model_Management.*;


/** A non-modal wizard to help the user change navigability
 *  of an association. */

public class WizNavigable extends Wizard {

  protected String _instructions =
  "Please select one of the following navigability options.";
  protected String _option0 = "Navigable Toward Start";
  protected String _option1 = "Navigable Toward End";
  protected String _option2 = "Navigable Both Ways";

  protected WizStepChoice _step1 = null;

  public WizNavigable() { }

  public int getNumSteps() { return 1; }

  public ModelElement getModelElement() {
    if (_item != null) {
      VectorSet offs = _item.getOffenders();
      if (offs.size() >= 1) {
	ModelElement me = (ModelElement) offs.elementAt(0);
	return me;
      }
    }
    return null;
  }

  public Vector getOptions() {
    Vector res = new Vector();
    Association asc = (Association) getModelElement();
    AssociationEnd ae0 = (AssociationEnd) asc.getConnection().elementAt(0);
    AssociationEnd ae1 = (AssociationEnd) asc.getConnection().elementAt(1);
    Classifier cls0 = ae0.getType();
    Classifier cls1 = ae1.getType();

    if (cls0 != null && !"".equals(cls0.getName().getBody()))
      _option0 = "Navigable Toward " + cls0.getName().getBody();

    if (cls1 != null && !"".equals(cls1.getName().getBody()))
      _option1 = "Navigable Toward " + cls1.getName().getBody();

    // needs-more-work: put in class names
    res.addElement(_option0);
    res.addElement(_option1);
    res.addElement(_option2);
    return res;
  }

  public void setInstructions(String s) { _instructions = s; }

  /** Create a new panel for the given step.  */
  public JPanel makePanel(int newStep) {
    switch (newStep) {
    case 1:
      if (_step1 == null) {
	_step1 = new WizStepChoice(this, _instructions, getOptions());
	_step1.setTarget(_item);
      }
      return _step1;
    }
    return null;
  }

  /** Take action at the completion of a step. For example, when the
   *  given step is 0, do nothing; and when the given step is 1, do
   *  the first action.  Argo non-modal wizards should take action as
   *  they do along, as soon as possible, they should not wait until
   *  the final step. */
  public void doAction(int oldStep) {
    //System.out.println("doAction " + oldStep);
    switch (oldStep) {
    case 1:
      int choice = -1;
      if (_step1 != null) choice = _step1.getSelectedIndex();
      if (choice == -1) {
	System.out.println("nothing selected, should not get here");
	return;
      }
      try {
	Association asc = (Association) getModelElement();
	AssociationEnd ae0 = (AssociationEnd) asc.getConnection().elementAt(0);
	AssociationEnd ae1 = (AssociationEnd) asc.getConnection().elementAt(1);
	ae0.setIsNavigable(choice == 0 || choice == 2);
	ae1.setIsNavigable(choice == 1 || choice == 2);
      }
      catch (PropertyVetoException pve) {
	System.out.println("could not set navigablity");
      }
    }
  }

  public boolean canFinish() {
    if (!super.canFinish()) return false;
    if (_step == 0) return true;
    if (_step == 1 && _step1 != null && _step1.getSelectedIndex() != -1)
      return true;
    return false;
  }


} /* end class WizNavigable */
