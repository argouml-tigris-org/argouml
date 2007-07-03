// $Id: CheckItem.java 11462 2006-11-12 12:24:41Z tfmorris $
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

package org.argouml.cognitive.checklist;

import java.io.Serializable;
import org.tigris.gef.util.Predicate;
import org.tigris.gef.util.PredicateTrue;


/**
 * This class defines an item that can be placed on a Checklist.
 * This is a short piece of text to prompt the designer to think of a
 * specific design issue.  CheckItems are similiar to critics in that
 * they are categorized to be relevant to issues the designer is
 * interested in, they have a guarding condition that returns true if
 * the CheckItem should be presented, and they have a piece of text
 * as design feedback. They are different in that their predicate is
 * almost always the constant 'true', and the feedback they provide
 * is much simpler.
 *
 * CheckItems are part of Checklists.  And Checklists are registered
 * with the CheckManager.
 *
 * If you have a piece of advice you would like to give a designer,
 * you can implement it as a CheckItem _very_ easily.  If you can
 * formalize the advice more, you can implement it as a Critic.
 *
 * @see Checklist
 * @see CheckManager
 * @author jrobbins
 */
public class CheckItem implements Serializable {
    ////////////////////////////////////////////////////////////////
    // instance variables

    private String category;

    /**
     * One sentence description of the issue. usually in the form of a
     * question.
     */
    private String description;

    /**
     * URL for background (textbook?) knowledge about the domain.
     */
    private String moreInfoURL = "http://argouml.tigris.org/";

    /**
     * The predicate is the condition under which
     * the checkitem should be listed.
     */
    private Predicate pred = PredicateTrue.theInstance();

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     *
     * @param c the category
     * @param d the description
     */
    public CheckItem(String c, String d) {
	setCategory(c);
	setDescription(d);
    }

    /**
     * The constructor.
     *
     * @param c the category
     * @param d the description
     * @param m the more-info-url
     * @param p the predicate
     */
    public CheckItem(String c, String d, String m, Predicate p) {
	this(c, d);
	setMoreInfoURL(m);
	pred = p;
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    /**
     * @return the category
     */
    public String getCategory() { return category; }

    /**
     * @param c the category
     */
    public void setCategory(String c) { category = c; }

    /**
     * @return the description
     */
    public String getDescription() { return description; }

    /**
     * @param dm the design material
     * @return the description
     */
    public String getDescription(Object dm) {
	return expand(description, dm);
    }
    /**
     * @param d the description
     */
    public void setDescription(String d) { description = d; }

    /**
     * @return the more-info-url
     */
    public String getMoreInfoURL() { return moreInfoURL; }

    /**
     * @param m  the more-info-url
     */
    public void setMoreInfoURL(String m) { moreInfoURL = m; }

    /**
     * @return the predicate
     */
    public Predicate getPredicate() { return pred; }

    /**
     * @param p the predicate
     */
    public void setPredicate(Predicate p) { pred = p; }

    /*
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getDescription().hashCode();
    }

    /*
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
	if (!(o instanceof CheckItem)) {
	    return false;
	}
	CheckItem i = (CheckItem) o;
	return getDescription().equals(i.getDescription());
    }

    /*
     * @see java.lang.Object#toString()
     */
    public String toString() { return getDescription(); }

    /**
     * Customize/expand the description string just before it is displayed.
     * I.e. add offender specific information to the description string
     * (e.g. its name).
     *
     * @param desc       the description
     * @param dm         the design material
     * @return           the description
     */
    public String expand(String desc, Object dm) { return desc; }

} /* end class CheckItem */
