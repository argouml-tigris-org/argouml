// $Id$
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

package org.argouml.uml.cognitive.critics;

import java.util.*;
import java.beans.*;
import javax.swing.*;

import org.apache.log4j.Category;
import org.argouml.cognitive.ui.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.util.*;

import org.argouml.kernel.*;

/** A non-modal wizard to help the user change navigability
 *  of an association. */

public class WizNavigable extends Wizard {
    protected static Category cat = Category.getInstance(WizNavigable.class);
					      
    protected String _instructions =
	"Please select one of the following navigability options.";
    protected String _option0 = "Navigable Toward Start";
    protected String _option1 = "Navigable Toward End";
    protected String _option2 = "Navigable Both Ways";
							      
    protected WizStepChoice _step1 = null;
								  
    public WizNavigable() { }
								      
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
	MAssociationEnd ae0 = (MAssociationEnd) asc.getConnections().get(0);
	MAssociationEnd ae1 = (MAssociationEnd) asc.getConnections().get(1);
	MClassifier cls0 = ae0.getType();
	MClassifier cls1 = ae1.getType();
			
	if (cls0 != null && !"".equals(cls0.getName()))
	    _option0 = "Navigable Toward " + cls0.getName();
									   
	if (cls1 != null && !"".equals(cls1.getName()))
	    _option1 = "Navigable Toward " + cls1.getName();
 
	// TODO: put in class names
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
	cat.debug("doAction " + oldStep);
	switch (oldStep) {
	case 1:
	    int choice = -1;
	    if (_step1 != null) choice = _step1.getSelectedIndex();
	    if (choice == -1) {
		throw new Error("nothing selected, should not get here");
	    }
	    try {
		MAssociation asc = (MAssociation) getModelElement();
		MAssociationEnd ae0 = (MAssociationEnd) asc.getConnections().get(0);
		MAssociationEnd ae1 = (MAssociationEnd) asc.getConnections().get(1);
		ae0.setNavigable(choice == 0 || choice == 2);
		ae1.setNavigable(choice == 1 || choice == 2);
	    }
	    catch (Exception pve) {
		cat.error("could not set navigablity", pve);
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
