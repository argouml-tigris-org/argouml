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

package org.argouml.cognitive.critics;

import java.util.Enumeration;
import java.util.Vector;
import org.argouml.cognitive.Designer;


public class StandardCM extends AndCM {
    public StandardCM() {
	addMech(new EnabledCM());
	addMech(new NotSnoozedCM());
	addMech(new DesignGoalsCM());
	addMech(new CurDecisionCM());
    }
} /* end class StandardCM */



class NotSnoozedCM extends ControlMech {
    public boolean isRelevant(Critic c, Designer d) {
	return !c.snoozeOrder().getSnoozed();
    }
} // end class NotSnoozedCM



class DesignGoalsCM extends ControlMech {
    public boolean isRelevant(Critic c, Designer d) {
	return c.isRelevantToGoals(d);
    }
} // end class DesignGoalsCM

// How much control should critics have over when they are relavant?
// Does doing that in code instead of declaratively limit reasoning?
// How does using more semantically rich method calls impact
// componentization?

class CurDecisionCM extends ControlMech {
    public boolean isRelevant(Critic c, Designer d) {
	return c.isRelevantToDecisions(d);
    }
} // end class CurDecisionCM



class CompositeCM extends ControlMech {
    protected Vector _mechs = new Vector();
    public void addMech(ControlMech cm) {
	_mechs.addElement(cm);
    }
} // end class CompositeCM



class AndCM extends CompositeCM {
    public boolean isRelevant(Critic c, Designer d) {
	Enumeration cur = _mechs.elements();
	while (cur.hasMoreElements()) {
	    ControlMech cm = (ControlMech) cur.nextElement();
	    if (!cm.isRelevant(c, d)) return false;
	}
	return true;
    }
} // end class AndCM



class OrCM extends CompositeCM {
    public boolean isRelevant(Critic c, Designer d) {
	Enumeration cur = _mechs.elements();
	while (cur.hasMoreElements()) {
	    ControlMech cm = (ControlMech) cur.nextElement();
	    if (cm.isRelevant(c, d)) return true;
	}
	return false;
    }
} // end class OrCM
