/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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

package org.argouml.cognitive;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * The DecisionModel is part of the state of the Designer.  It
 * describes what types of decisions, or design issues, the Designer
 * is thinking about at the current time.  Critics that are relevant to
 * those decisions are made active, Critics that are not relevant are
 * made inactive.
 *
 * TODO: There is some notion that each decision has a
 * certain importanance at a certain time, but I have not followed
 * through on that because I don't have good examples of how to
 * quantify the importance of a decision.
 *
 * TODO: Right now the individual decisions are just
 * Strings, maybe they should have some non-atomic structure?
 *
 * @author Jason Robbins
 */
public class DecisionModel extends Observable implements Serializable {
    private List<Decision> decisions = new ArrayList<Decision>();

    /**
     * The constructor.
     *
     */
    public DecisionModel() {
	decisions.add(Decision.UNSPEC);
    }

    /**
     * @return the list of decisions
     */
    public List<Decision> getDecisionList() {
        return decisions;
    }

    /**
     * This function sets the priority of an existing decision, or
     * if the decision does not exist yet, it creates a new one.
     *
     * @param decision the given decision
     * @param priority the new priority
     */
    public synchronized void setDecisionPriority(String decision,
						 int priority)
    {
	Decision d = findDecision(decision);
	if (null == d) {
	    d = new Decision(decision, priority);
	    decisions.add(d);
	    return;
	}
	d.setPriority(priority);
	setChanged();
	notifyObservers(decision);
	//decision model listener
    }

    /**
     * If the given decision is already defined, do nothing. If it is
     * not already defined, set it to the given initial priority.
     *
     * @param decision the existing decision
     * @param priority the priority
     */
    public void defineDecision(String decision, int priority) {
	Decision d = findDecision(decision);
	if (d == null) {
	    setDecisionPriority(decision, priority);
	}
    }


    /**
     * The Designer has indicated that he is now interested in the
     * given decision.
     *
     * @param d the interesting decision
     */
    public void startConsidering(Decision d) {
	decisions.remove(d);
	decisions.add(d);
    }


    /**
     * The Designer has indicated that he is not interested in the
     * given decision right now.
     *
     * @param d the uninteresting decision
     */
    public void stopConsidering(Decision d) {
	decisions.remove(d);
    }

    /**
     * Finds a decision with a specific name.
     *
     * @param decName the decision name
     * @return a decision or null if not found.
     */
    protected Decision findDecision(String decName) {
        for (Decision d : decisions) {
	    if (decName.equals(d.getName())) {
		return d;
	    }
	}
	return null;
    }

}
