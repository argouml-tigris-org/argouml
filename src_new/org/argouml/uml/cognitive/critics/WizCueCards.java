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



// File: WizCueCards.java
// Classes: WizCueCards
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;
import java.beans.*;
import javax.swing.*;

import org.argouml.cognitive.ui.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;

import org.argouml.kernel.*;
import org.tigris.gef.util.*;

/** A non-modal wizard to help the user change navigability
 *  of an association. */

public class WizCueCards extends Wizard {

    protected Vector _cues = new Vector();
    protected WizStepCue _steps[] = null;

    public WizCueCards() { }

    public int getNumSteps() { return _cues.size(); }

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

    public void addCue(String s) { _cues.addElement(s); }

    /** Create a new panel for the given step.
     *
     * @return a newly created panel or null if there isn't that many steps.
     */
    public JPanel makePanel(int newStep) {
	if (newStep <= getNumSteps()) {
	    String c = (String) _cues.elementAt(newStep - 1);
	    return new WizStepCue(this, c);
	}
	return null;
    }

    /** This wizard never takes action, it just displays step by step
     *  instructions. */
    public void doAction(int oldStep) {  }

    /** This wizard cannot automatically finish the task. It can only be
     *  finished when the user is on the last step. */
    public boolean canFinish() {
	return _step == getNumSteps();
    }


} /* end class WizCueCards */
