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



// File: CheckItem.java
// Classes: CheckItem
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.cognitive.checklist;

import java.util.*;

import org.tigris.gef.util.*;

/** This class defines an item that can be placed on a Checklist.
 *  This is a short piece of text to prompt the designer to think of a
 *  specific design issue.  CheckItems are similiar to critics in that
 *  they are categorized to be releavant to issues the designer is
 *  interested in, they have a guarding condition that returns true if
 *  the CheckItem should be presented, and they have a piece of text
 *  as design feedback. They are different in that their predicate is
 *  almost always the constant 'true', and the feedback they provide
 *  is much simpler.
 *
 * CheckItems are part of Checklists.  And Checklists are registered
 * with the CheckManager.
 *
 *  If you have a piece of advice you would like to give a designer,
 *  you can implement it as a CheckItem _very_ easily.  If you can
 *  formalize the advice more, you can implement it as a Critic.
 *
 * @see Checklist
 * @see CheckManager
 */

public class CheckItem implements java.io.Serializable {
    ////////////////////////////////////////////////////////////////
    // instance variables

    protected String _category;

    /** One sentence description of the issue. usually in the form of a
     *  question. */
    protected String _description;

    /** URL for background (textbook?) knowledge about the domain. */
    protected String _moreInfoURL = "http://www.ics.uci.edu/pub/arch/argo/";

    protected Predicate _pred = PredicateTrue.theInstance();

    ////////////////////////////////////////////////////////////////
    // constructors

    public CheckItem(String c, String d) {
	setCategory(c);
	setDescription(d);
    }

    public CheckItem(String c, String d, String m, Predicate p) {
	this(c, d);
	setMoreInfoURL(m);
	_pred = p;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public String getCategory() { return _category; }
    public void setCategory(String c) { _category = c; }

    public String getDescription() { return _description; }
    public String getDescription(Object dm) {
	return expand(_description, dm);
    }
    public void setDescription(String d) { _description = d; }

    public String getMoreInfoURL() { return _moreInfoURL; }
    public void setMoreInfoURL(String m) { _moreInfoURL = m; }

    public Predicate getPredicate() { return _pred; }
    public void setPredicate(Predicate p) { _pred = p; }

    /** Is this item already on the list? */
    public boolean equals(Object o) {
	if (!(o instanceof CheckItem)) return false;
	CheckItem i = (CheckItem) o;
	return getDescription().equals(i.getDescription());
    }

    public String toString() { return getDescription(); }

    public String expand(String desc, Object dm) { return desc; }
  
} /* end class CheckItem */
