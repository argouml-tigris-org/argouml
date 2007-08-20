// $Id:Wizard.java 12950 2007-07-01 08:10:04Z tfmorris $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.cognitive.critics;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;


/**
 * "Abstract" base class for non-modal wizards.  Each subclass should
 * define its own makeNextPanel methods. Because most
 * wizards will not be run to completion, the panels are constructed
 * only as needed. This implies that Wizards should not initialize
 * many instance variables in their constructors.<p>
 *
 * By convention step 0 is the problem description of the ToDoItem,
 * step 1 is the first panel displayed after the user presses
 * "Next>", and so on.  The problem description panel is not stored in
 * this wizard, only the panels that are specific to the wizard are
 * stored. If the user presses "Back>" enough times to get back to the
 * problem description, backPanel should return null.  A null panel
 * indicates that the problem description should be shown. <p>
 *
 * Several of the comments in this class refer to "context".  Context
 * is the data about this execution of this wizard, for example, values
 * that the user enters in step 1 is part of the context of later steps,
 * and the ToDoItem with its offenders Set is always context.  Most
 * context should be stored in instance variables of Wizard subclasses.
 *
 * @author jrobbins
 */
public abstract class Wizard implements java.io.Serializable {

    /** User interface panels displayed so far. */
    private List<JPanel> panels = new ArrayList<JPanel>();

    /** The current step that the Wizard is on.  Zero indicates that the
     *  wizard has not yet begun. */
    private int step = 0;

    /** True when the wizard has done everything it can. */
    private boolean finished = false;
    private boolean started = false;

    private WizardItem item = null;

    /** Construct a new wizard to help the user repair a design flaw. */
    public Wizard() {
    }

    /**
     * @param s the step number of the panel to be removed
     */
    protected void removePanel(int s) {
        panels.remove(s);
    }

    /**
     * Setter for the todoitem.
     *
     * @param i the todoitem
     */
    public void setToDoItem(WizardItem i) {
        item = i;
    }

    /**
     * @return the todoitem
     */
    public WizardItem getToDoItem() {
        return item;
    }

    /** An integer between 0 and 100, shows percent done. The current
     *  ArgoUML user interface shows different PostIt note icons for
     *  0, 1-25, 26-50. 51-75, and 76-100.
     *  @return the percentage done.
     */
    public int getProgress() {
        return step * 100 / getNumSteps();
    }

    /** Get the number of steps in this wizard.  Subclasses should
     *  override to return a constant, or compute based on context.
     *  @return the number of steps in this wizard.
     */
    public abstract int getNumSteps();

    /** Get the panel that should be displayed now.  Usually called
     *  after the user pressed "Next&gt;" and next() has returned, or after
     *  the user pressed "&lt;Back" and back() has returned.  Also called
     *  when the user turns away from the wizard to do something else and
     *  then returns his or her attention to the wizard.
     *  @return the panel that should be displayed now.
     */
    public JPanel getCurrentPanel() {
        return getPanel(step);
    }


    /** Get the exising panel at step s. Step 1 is the first wizard
     *  panel.
     * @param s the step
     * @return the panel for step s or null if none.
     */
    public JPanel getPanel(int s) {
	if (s > 0 && s <= panels.size()) {
	    return panels.get(s - 1);
        }
	return null;
    }

    ////////////////////////////////////////////////////////////////
    // wizard actions

    /** Return true iff the "Next&gt;" button should be enabled.
     *  Subclasses should override to first check super.nextEnabled()
     *  and then check for legal context values.
     *  @return <code>true</code> iff the "Next&gt;" button should be enabled.
     */
    public boolean canGoNext() {
        return step < getNumSteps();
    }

    /**
     * The next step of the wizard.
     */
    public void next() {
	doAction(step);
	step++;
	JPanel p = makePanel(step);
	if (p != null) {
            panels.add(p);
        }
	started = true;
	if (item != null) {
            item.changed();
        }
    }

    /**
     * @return true if we can step back
     */
    public boolean canGoBack() {
        return step > 0;
    }

    /**
     * Step back.
     */
    public void back() {
	step--;
	if (step < 0) step = 0;
	undoAction(step);
	if (item != null) item.changed();
    }

    /**
     * @return true if we can finish (i.e. the finish button is not downlighted)
     */
    public boolean canFinish() {
        return true;
    }

    /**
     * @return true if the wizard is started
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * @return true if the wizard is finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Finish the wizard.
     */
    public void finish() {
	started = true;
	int numSteps = getNumSteps();
	for (int i = step; i <= numSteps; i++) {
	    doAction(i);
	    if (item != null) item.changed();
	}
	// TODO: do all following steps
	// TODO: resolve item from ToDoList
	finished = true;
    }

    /** Create a new panel for the given step. For example, When the
     *  given step is 1, create the first step of the wizard. <p>
     *
     *  TODO: It might be convient to make a reusable
     *  subclass of Wizard that shows all textual steps to guide the
     *  user without any automation.  Such a Wizard could be easily
     *  authored, stored in an XML file, and efficiently presented by
     *  reusing a single panel with a single JTextArea.
     *
     *  @param newStep the number of the step to make a panel for.
     *  @return a new panel for the given step
     */
    public abstract JPanel makePanel(int newStep);

    /** Take action at the completion of a step. For example, when the
     *  given step is 0, do nothing; and when the given step is 1, do
     *  the first action.  Argo non-modal wizards should take action as
     *  they do along, as soon as possible, they should not wait until
     *  the final step. Also, if the user pressed "Finish" doAction may
     *  be called for steps that never constructored or displayed their
     *  panels.
     *
     * @param oldStep the given step
     */
    public abstract void doAction(int oldStep);

    /**
     * Do the action of this wizard.
     */
    public void doAction() { doAction(step); }

    /** Undo the action done after the given step. For example, when the
     *  given step is 0, nothing was done, so nothing can be undone; and
     *  when the given step is 1, undo the first action.  Undo allows
     *  users to work part way through fixing a problem, see the partial
     *  result, and explore a different alternative.
     *
     * @param oldStep the given step
     */
    public void undoAction(int oldStep) {
    }

    /**
     * Undo the action.
     */
    public void undoAction() { undoAction(step); }

    /**
     * @return Returns the step.
     */
    protected int getStep() {
        return step;
    }
}
