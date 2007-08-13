// $Id$
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

package org.argouml.uml.cognitive.critics;

import java.util.Vector;

import javax.swing.JPanel;

import org.argouml.cognitive.ui.WizStepCue;

/**
 *  A cue card wizard presents the user with a deck of instructions.
 *
 * @see org.argouml.cognitive.ui.WizStepCue
 *
 * @author jrobbins
 */
public class WizCueCards extends UMLWizard {

    private Vector cues = new Vector();

    /**
     * The constructor.
     */
    public WizCueCards() { }

    /*
     * @see org.argouml.cognitive.ui.Wizard#getNumSteps()
     */
    public int getNumSteps() { return cues.size(); }

    /**
     * @param s the text for the wizard step
     */
    public void addCue(String s) { cues.addElement(s); }

    /**
     * Create a new panel for the given step.
     * Returns a newly created panel or null if there isn't that many steps.
     *
     * @see org.argouml.cognitive.critics.Wizard#makePanel(int)
     */
    public JPanel makePanel(int newStep) {
	if (newStep <= getNumSteps()) {
	    String c = (String) cues.elementAt(newStep - 1);
	    return new WizStepCue(this, c);
	}
	return null;
    }

    /**
     * This wizard never takes action, it just displays step by step
     * instructions.
     *
     * @see org.argouml.cognitive.critics.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {  }

    /**
     * This wizard cannot automatically finish the task. It can only be
     * finished when the user is on the last step.
     *
     * @see org.argouml.cognitive.critics.Wizard#canFinish()
     */
    public boolean canFinish() {
	return getStep() == getNumSteps();
    }


} /* end class WizCueCards */
