// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ui.WizStepChoice;
import org.argouml.cognitive.ui.WizStepConfirm;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * A non-modal wizard to help the user change select an association
 * to make non-aggregate.
 */
public class WizBreakCircularComp extends UMLWizard {
    private static final Logger LOG =
	Logger.getLogger(WizBreakCircularComp.class);

    private String instructions1 = 
        Translator.localize("critics.WizBreakCircularComp-ins1");

    private String instructions2 = 
        Translator.localize("critics.WizBreakCircularComp-ins2");

    private String instructions3 = 
        Translator.localize("critics.WizBreakCircularComp-ins3");

    private WizStepChoice step1 = null;
    private WizStepChoice step2 = null;
    private WizStepConfirm step3 = null;

    private Object selectedCls = null;
    private Object selectedAsc = null;

    /**
     * The constructor.
     */
    public WizBreakCircularComp() { }

    /**
     * @see org.argouml.cognitive.ui.Wizard#getNumSteps()
     */
    public int getNumSteps() { return 3; }

    /**
     * @return The choices for this step.
     */
    protected Vector getOptions1() {
	Vector res = new Vector();
	if (getToDoItem() != null) {
	    ToDoItem item = (ToDoItem) getToDoItem();
	    ListSet offs = item.getOffenders();
	    int size = offs.size();
	    for (int i = 0; i < size; i++) {
		Object me = /*(MModelElement)*/ offs.elementAt(i);
		String s = Model.getFacade().getName(me);
		res.addElement(s);
	    }
	}
	return res;
    }

    /**
     * @return The choices for this step.
     */
    protected Vector getOptions2() {
	Vector res = new Vector();
	if (selectedCls != null) {
	    Collection aes = Model.getFacade().getAssociationEnds(selectedCls);
	    Object fromType = selectedCls;
	    String fromName = Model.getFacade().getName(fromType);
	    for (Iterator iter = aes.iterator(); iter.hasNext();) {
		Object fromEnd = /*(MAssociationEnd)*/ iter.next();
		Object asc = Model.getFacade().getAssociation(fromEnd);
		Object toEnd =
		    new ArrayList(Model.getFacade().getConnections(asc)).get(0);
		if (toEnd == fromEnd) {
		    toEnd = new ArrayList(
		            Model.getFacade().getConnections(asc)).get(1);
		}
		Object toType = Model.getFacade().getType(toEnd);
		String ascName = Model.getFacade().getName(asc);
		String toName = Model.getFacade().getName(toType);
		String s = ascName 
                    + " " 
                    + Translator.localize("critics.WizBreakCircularComp-from") 
                    + fromName
                    + " "
                    + Translator.localize("critics.WizBreakCircularComp-to")
                    + " "
                    + toName;
		res.addElement(s);
	    }
	}
	return res;
    }

    /**
     * Create a new panel for the given step.
     *
     * @see org.argouml.cognitive.ui.Wizard#makePanel(int)
     */
    public JPanel makePanel(int newStep) {
	switch (newStep) {
	case 1:
	    if (step1 == null) {
		step1 = new WizStepChoice(this, instructions1, getOptions1());
		step1.setTarget(getToDoItem());
	    }
	    return step1;
	case 2:
	    if (step2 == null) {
		step2 = new WizStepChoice(this, instructions2, getOptions2());
		step2.setTarget(getToDoItem());
	    }
	    return step2;
	case 3:
	    if (step3 == null) {
		step3 = new WizStepConfirm(this, instructions3);
	    }
	    return step3;
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
     * @see org.argouml.cognitive.ui.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
	LOG.debug("doAction " + oldStep);
	int choice = -1;
	ToDoItem item = (ToDoItem) getToDoItem();
	ListSet offs = item.getOffenders();
	switch (oldStep) {
	case 1:
	    if (step1 != null) {
	        choice = step1.getSelectedIndex();
	    }
	    if (choice == -1) {
		throw new Error("nothing selected, should not get here");
	    }
	    selectedCls = offs.elementAt(choice);
	    break;
	    ////////////////
	case 2:
	    if (step2 != null) {
	        choice = step2.getSelectedIndex();
	    }
	    if (choice == -1) {
		throw new Error("nothing selected, should not get here");
	    }
	    Object ae = null;
	    Iterator iter =
	        Model.getFacade().getAssociationEnds(selectedCls).iterator();
	    for (int n = 0; n <= choice; n++) {
	        ae = /*(MAssociationEnd)*/ iter.next();
	    }
	    selectedAsc = Model.getFacade().getAssociation(ae);
	    break;
	    ////////////////
	case 3:
	    if (selectedAsc != null) {
		List conns = new ArrayList(
		        Model.getFacade().getConnections(selectedAsc));
		Object ae0 = /*(MAssociationEnd)*/ conns.get(0);
		Object ae1 = /*(MAssociationEnd)*/ conns.get(1);
		try {
		    Model.getCoreHelper().setAggregation(
		            ae0,
		            Model.getAggregationKind().getNone());
		    Model.getCoreHelper().setAggregation(
		            ae1,
		            Model.getAggregationKind().getNone());
		} catch (Exception pve) {
		    LOG.error("could not set aggregation", pve);
		}
	    }
	    break;
	}
    }

    /**
     * @see org.argouml.cognitive.ui.Wizard#canGoNext()
     */
    public boolean canGoNext() { return canFinish(); }

    /**
     * @see org.argouml.cognitive.ui.Wizard#canFinish()
     */
    public boolean canFinish() {
	if (!super.canFinish()) {
	    return false;
	}
	if (getStep() == 0) {
	    return true;
	}
	if (getStep() == 1 && step1 != null && step1.getSelectedIndex() != -1) {
	    return true;
	}
	if (getStep() == 2 && step2 != null && step2.getSelectedIndex() != -1) {
	    return true;
	}
	return false;
    }
} /* end class WizBreakCircularComp */
