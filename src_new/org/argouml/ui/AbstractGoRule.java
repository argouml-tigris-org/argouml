// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

package org.argouml.ui;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.tree.TreeModel;

import org.apache.log4j.Logger;

/**
 * Abstract class to be used as a convenience class for implementing
 * 'go rules'.  Go rules are rules to which the navperspectives comply
 * if they are registred with the navperspectives. This usually
 * happens in the static block of NavPerspective.  If some
 * modelelement is not showing up in a navperspective, in most cases
 * this means that there is no go rule defined or an incorrect
 * one. Via the abstract method getChildren the children of some tree
 * element in a perspective are returned.  Only the returned children
 * are shown for some tree element.
 *
 * <p>Since the new Explorer implemnentation,
 *    this class is no longer used by the explorer, replaced by PerspectiveRule.
 *
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class AbstractGoRule implements TreeModel {

    private static Logger cat =
        Logger.getLogger(org.argouml.ui.AbstractGoRule.class);
 

    // ----------- TreeModel helpers -----------

    /**
     * @see javax.swing.tree.TreeModel#getChild(Object, int)
     */
    public Object getChild(Object parent, int index) {

        int count = 0;
        Collection col = getChildren(parent);
        if (col != null) {
            Iterator it = col.iterator();
            while (it.hasNext()) {

                Object candidate = it.next();
                if (count == index)
                    return candidate;
                count++;
            }
        }
        return null;
    }

    /**
     * @see javax.swing.tree.TreeModel#getChildCount(Object)
     */
    public int getChildCount(Object parent) {
        Collection c = getChildren(parent);

        if (c == null)
            return 0;

        return c.size();
    }

    /**
     * @see javax.swing.tree.TreeModel#isLeaf(Object)
     */
    public final boolean isLeaf(Object node) {

        //return (getChildCount(node) < 1);
        return false;
    }

    /**
     * @see javax.swing.tree.TreeModel#getIndexOfChild(Object, Object)
     */
    public int getIndexOfChild(Object parent, Object child) {

        int index = 0;
        Collection col = getChildren(parent);
        if (col != null) {
            Iterator it = col.iterator();
            while (it.hasNext()) {

                Object candidate = it.next();
                if (candidate == child)
                    return index;
                index++;
            }
        }
        return -1;
    }

    // -------------- other helper methods --------------------

    /**
     * this is the method that should be overridden by GoRules
     */
    public abstract Collection getChildren(Object parent);

    /** return the name of the rule as it is displayed in
     *  the nav perspective edit pane. Returns for example
     *  "State->Substates".
     *  @see #toString()
     */
    public abstract String getRuleName();

    /** wrapper around getRuleName()
     */
    public String toString() {
        return getRuleName();
    }

   

    // ------------- not used TreeModel methods -------------

    public void addTreeModelListener(
	    javax.swing.event.TreeModelListener treeModelListener) 
    {
    }

    public Object getRoot() {
        return null;
    }

    public void removeTreeModelListener(
	   javax.swing.event.TreeModelListener treeModelListener)
    {
    }

    public void valueForPathChanged(
				    javax.swing.tree.TreePath treePath,
				    Object obj) {
    }

}
