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



// File: WizAssocComposite.java
// Classes: WizAssocComposite
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.uml.critics;

import com.sun.java.util.collections.*;
import java.beans.*;
import javax.swing.*;

import uci.argo.kernel.*;
import uci.util.*;
import uci.uml.ui.todo.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;


/** A non-modal wizard to help the user change navigability
 *  of an association. */

public class WizAssocComposite extends Wizard {

  protected String _instructions =
  "Please select one of the following containment options.";
  protected String _option0 = "Start Contains End";
  protected String _option1 = "End Contains Start";
  protected String _option2 = "No Containment";

  protected WizStepChoice _step1 = null;

  protected MAggregationKind _orig0 = MAggregationKind.NONE;
  protected MAggregationKind _orig1 = MAggregationKind.NONE;

  public WizAssocComposite() { }

  public int getNumSteps() { return 1; }

  public MModelElement getModelElement() {
    if (_item != null) {
      VectorSet offs = _item.getOffenders();
      if (offs.size() >= 1) {
	MModelElement me = (MModelElement) offs.elementAt(0);
	return me;
      }
    }
    return null;
  }

  public Vector getOptions() {
    Vector res = new Vector();
    MAssociation asc = (MAssociation) getModelElement();
    Iterator iter = asc.getConnections().iterator();
    MAssociationEnd ae0 = (MAssociationEnd) iter.next();
    MAssociationEnd ae1 = (MAssociationEnd) iter.next();
    MClassifier cls0 = ae0.getType();
    MClassifier cls1 = ae1.getType();

    String start = "Start";
    String end = "End";

    if (cls0 != null && !"".equals(cls0.getName()))
      start = cls0.getName();

    if (cls1 != null && !"".equals(cls1.getName()))
      end =cls1.getName();

    _option0 = start + " Contains " + end;
    _option1 = end + " Contains " + start;

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
	MAssociation asc = (MAssociation) getModelElement();
        Iterator iter = asc.getConnections().iterator();
        MAssociationEnd ae0 = (MAssociationEnd) iter.next();
        MAssociationEnd ae1 = (MAssociationEnd) iter.next();
	ae0.setAggregation(choice == 0 ? MAggregationKind.COMPOSITE : _orig0);
	ae1.setAggregation(choice == 1 ? MAggregationKind.COMPOSITE : _orig1);
      }
      catch (Exception pve) {
	System.out.println("could not set Containment");
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


} /* end class WizAssocComposite */
