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

import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JPanel;
import org.apache.log4j.Logger;

import org.argouml.cognitive.ui.WizStepChoice;
import org.argouml.kernel.Wizard;
import org.argouml.model.ModelFacade;
import org.tigris.gef.util.VectorSet;
/** A non-modal wizard to help the user change navigability
 *  of an association. */

public class WizNavigable extends Wizard {
    private static final Logger LOG = Logger.getLogger(WizNavigable.class);
					      
    private String instructions =
	"Please select one of the following navigability options.";
    private String option0 = "Navigable Toward Start";
    private String option1 = "Navigable Toward End";
    private String option2 = "Navigable Both Ways";
							      
    private WizStepChoice step1 = null;
								  
    /**
     * The constructor.
     * 
     */
    public WizNavigable() { }
								      
    /**
     * @see org.argouml.kernel.Wizard#getNumSteps()
     */
    public int getNumSteps() { return 1; }
									  
    /**
     * @return the offending modelelement
     */
    public Object getModelElement() {
	if (_item != null) {
	    VectorSet offs = _item.getOffenders();
	    if (offs.size() >= 1) {
		Object me = /*(MModelElement)*/ offs.elementAt(0);
		return me;
	    }
	}
	return null;
    }
									      
    /**
     * @return the options
     */
    public Vector getOptions() {
	Vector res = new Vector();
	Object asc = /*(MAssociation)*/ getModelElement();
	Object ae0 = /*(MAssociationEnd)*/
	    new ArrayList(ModelFacade.getConnections(asc)).get(0);
	Object ae1 = /*(MAssociationEnd)*/
	    new ArrayList(ModelFacade.getConnections(asc)).get(1);
	Object cls0 = ModelFacade.getType(ae0);
	Object cls1 = ModelFacade.getType(ae1);
			
	if (cls0 != null && !"".equals(ModelFacade.getName(cls0))) {
	    option0 = "Navigable Toward " + ModelFacade.getName(cls0);
        }
									   
	if (cls1 != null && !"".equals(ModelFacade.getName(cls1))) {
	    option1 = "Navigable Toward " + ModelFacade.getName(cls1);
        }
 
	// TODO: put in class names
	res.addElement(option0);
	res.addElement(option1);
	res.addElement(option2);
	return res;
    }
 
    /**
     * @param s the instructions
     */
    public void setInstructions(String s) { instructions = s; }
 
    /** 
     * Create a new panel for the given step.
     * 
     * @see org.argouml.kernel.Wizard#makePanel(int)
     */
    public JPanel makePanel(int newStep) {
	switch (newStep) {
	case 1:
	    if (step1 == null) {
		step1 = new WizStepChoice(this, instructions, getOptions());
		step1.setTarget(_item);
	    }
	    return step1;
	}
	return null;
    }
 
    /** 
     * Take action at the completion of a step. For example, when the
     * given step is 0, do nothing; and when the given step is 1, do
     * the first action.  Argo non-modal wizards should take action as
     * they do along, as soon as possible, they should not wait until
     * the final step. 
     * 
     * @see org.argouml.kernel.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
	LOG.debug("doAction " + oldStep);
	switch (oldStep) {
	case 1:
	    int choice = -1;
	    if (step1 != null) choice = step1.getSelectedIndex();
	    if (choice == -1) {
		throw new Error("nothing selected, should not get here");
	    }
	    try {
		Object asc = /*(MAssociation)*/ getModelElement();
		Object ae0 = /*(MAssociationEnd)*/ 
		    new ArrayList(ModelFacade.getConnections(asc)).get(0);
		Object ae1 = /*(MAssociationEnd)*/ 
		    new ArrayList(ModelFacade.getConnections(asc)).get(1);
		ModelFacade.setNavigable(ae0, choice == 0 || choice == 2);
		ModelFacade.setNavigable(ae1, choice == 1 || choice == 2);
	    }
	    catch (Exception pve) {
		LOG.error("could not set navigablity", pve);
	    }
	}
    }
 
    /**
     * @see org.argouml.kernel.Wizard#canFinish()
     */
    public boolean canFinish() {
	if (!super.canFinish()) return false;
	if (_step == 0) return true;
	if (_step == 1 && step1 != null && step1.getSelectedIndex() != -1)
	    return true;
	return false;
    }
 
} /* end class WizNavigable */