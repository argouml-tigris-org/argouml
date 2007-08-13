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

package org.argouml.cognitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.Icon;

/**
 * A CompoundCritic acts like a regular critic in that it checks the
 * design and produces design feedback.  However, a CompoundCritic is
 * composed of several regular critics that are applied in order.
 * The first one that produces feedback ends the application.  This is
 * useful when criticism can be ordered from specific to general:
 * general feedback should not be produced if specific feedback is
 * available.  For example, one critic might check for the legality
 * of the name of a design element, and another might check for the
 * presence of any name.  If a given design element has no name, both
 * critics could produce feedback, but it would be more useful if
 * only the first one did.
 *
 * @author Jason Robbins
 */

// TODO: maybe should stop at first, or find highest priority.

public class CompoundCritic extends Critic {


    ////////////////////////////////////////////////////////////////
    // instance variables

    /**
     * The sub-critics that make up this CompoundCritic.
     */
    private List<Critic> critics = new ArrayList<Critic>();

    ////////////////////////////////////////////////////////////////
    // constructor

    /**
     * The constructor of a compound critic.
     *
     */
    public CompoundCritic() {
    }

    /**
     * The constructor.
     *
     * @param c1 the first critic that makes up the compound critic
     * @param c2 the 2nd critic that makes up the compound critic
     */
    public CompoundCritic(Critic c1, Critic c2) {
	this();
	critics.add(c1);
	critics.add(c2);
    }

    /**
     * The constructor.
     *
     * @param c1 the first critic that makes up the compound critic
     * @param c2 the 2nd critic that makes up the compound critic
     * @param c3 the 3rd critic that makes up the compound critic
     */
    public CompoundCritic(Critic c1, Critic c2, Critic c3) {
	this(c1, c2);
	critics.add(c3);
    }

    /**
     * The constructor.
     *
     * @param c1 the first critic that makes up the compound critic
     * @param c2 the 2nd critic that makes up the compound critic
     * @param c3 the 3rd critic that makes up the compound critic
     * @param c4 the 4th critic that makes up the compound critic
     */
    public CompoundCritic(Critic c1, Critic c2, Critic c3, Critic c4) {
	this(c1, c2, c3);
	critics.add(c4);
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @param c
     *            the new list of critics that completely replaces the old list
     * @deprecated for 0.25.4 by tfmorris. Use {@link #setCritics(List)}.
     *             Unused in ArgoUML. Can be scheduled for speedy removal.
     */
    @Deprecated
    public void setCritics(Vector<Critic> c) {
        critics = c;
    }

    /**
     * @param c the new list of critics that completely
     *                replaces the old list
     */
    public void setCritics(List<Critic> c) {
        critics = c;
    }

    /**
     * @return the complete list of critics
     * @deprecated for 0.25.4 by tfmorris. Use {@link #getCriticList()}. Unused
     *             in ArgoUML. Can be scheduled for speedy removal.
     */
    @Deprecated
    public Vector<Critic> getCritics() {
        return new Vector<Critic>(critics);
    }

    /**
     * @return the complete list of critics
     */
    public List<Critic> getCriticList() {
        return critics;
    }

    
    /**
     * @param c the critic to be added at the end of the current list
     */
    public void addCritic(Critic c) {
        critics.add(c);
    }

    /**
     * @param c the critic to be removed
     */
    public void removeCritic(Critic c) {
        critics.remove(c);
    }

    ////////////////////////////////////////////////////////////////
    // critiquing

    /*
     * @see org.argouml.cognitive.critics.Critic#critique(java.lang.Object,
     * org.argouml.cognitive.Designer)
     */
    @Override
    public void critique(Object dm, Designer dsgr) {
	for (Critic c : critics) {
	    if (c.isActive() && c.predicate(dm, dsgr)) {
		ToDoItem item = c.toDoItem(dm, dsgr);
		postItem(item, dm, dsgr);
		return; // once one criticism is found, exit
	    }
	}
    }

    /*
     * @see org.argouml.cognitive.Poster#supports(org.argouml.cognitive.Decision)
     */
    @Override
    public boolean supports(Decision d) {
        for (Critic c : critics) {
	    if (c.supports(d)) {
		return true;
	    }
	}
	return false;
    }

    /*
     * @see org.argouml.cognitive.Poster#getSupportedDecisions()
     */
    @Override
    public List<Decision> getSupportedDecisions() {
	throw new UnsupportedOperationException();
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#addSupportedDecision(org.argouml.cognitive.Decision)
     */
    @Override
    public void addSupportedDecision(Decision d) {
	throw new UnsupportedOperationException();
    }

    /*
     * @see org.argouml.cognitive.Poster#supports(org.argouml.cognitive.Goal)
     */
    @Override
    public boolean supports(Goal g) {
        for (Critic c : critics) {
	    if (c.supports(g)) {
		return true;
	    }
	}
	return false;
    }

    /*
     * @see org.argouml.cognitive.Poster#getSupportedGoals()
     */
    @Override
    public List<Goal> getSupportedGoals() {
	throw new UnsupportedOperationException();
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#addSupportedGoal(org.argouml.cognitive.Goal)
     */
    @Override
    public void addSupportedGoal(Goal g) {
	throw new UnsupportedOperationException();
    }

    /*
     * @see org.argouml.cognitive.Poster#containsKnowledgeType(java.lang.String)
     */
    @Override
    public boolean containsKnowledgeType(String type) {
        for (Critic c : critics) {
	    if (c.containsKnowledgeType(type)) {
		return true;
	    }
	}
	return false;
    }

    /*
     * @see org.argouml.cognitive.critics.Critic#addKnowledgeType(java.lang.String)
     */
    @Override
    public void addKnowledgeType(String type) {
	throw new UnsupportedOperationException();
    }

    /*
     * @see org.argouml.cognitive.Poster#expand(java.lang.String, ListSet)
     */
    @Override
    public String expand(String desc, ListSet offs) {
	throw new UnsupportedOperationException();
    }

    /*
     * @see org.argouml.cognitive.Poster#getClarifier()
     */
    @Override
    public Icon getClarifier() {
	throw new UnsupportedOperationException();
    }


    /*
     * @see org.argouml.cognitive.critics.Critic#isActive()
     */
    @Override
    public boolean isActive() {
        for (Critic c : critics) {
	    if (c.isActive()) {
		return true;
	    }
	}
	return false;
    }

    ////////////////////////////////////////////////////////////////
    // criticism control

    /*
     * @see org.argouml.cognitive.critics.Critic#isEnabled()
     */
    @Override
    public boolean isEnabled() {
	return true;
    }

    ////////////////////////////////////////////////////////////////
    // design feedback

    /*
     * @see org.argouml.cognitive.critics.Critic#toDoItem(java.lang.Object,
     * org.argouml.cognitive.Designer)
     */
    @Override
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	throw new UnsupportedOperationException();
    }

}
