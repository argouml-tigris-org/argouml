// $Id:PerspectiveSupport.java 11510 2006-11-24 07:37:59Z tfmorris $
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

package org.argouml.ui;

import javax.swing.tree.TreeModel;
import java.util.Vector;

import org.argouml.i18n.Translator;


/**
 * Helper class for tree models that provides help building perspectives
 * out of gorules.<p>
 *
 * @author  alexb
 * @since 0.13.5, Created on 15 April 2003
 */
public class PerspectiveSupport {

    /**
     * The go rules that this Tree model uses to build child nodes.
     */
    private Vector goRules;

    /** name */
    private String name;

    /** list of all possible rules in the collection Todolist specific */
    private static Vector rules = new Vector();

    private PerspectiveSupport() { }

    /**
     * Creates a new instance of PerspectiveSupport
     *
     * @param n the name to be localized
     */
    public PerspectiveSupport(String n) {
        name = Translator.localize(n);
        goRules = new Vector();
    }

    /**
     * The constructor.<p>
     *
     * TODO: Is this constructor used? What is the purpose with it?
     *
     * @param n the name to be localized
     * @param subs the go rules
     */
    public PerspectiveSupport(String n, Vector subs) {
        this(n);
        goRules = subs;
    }

    // ------------- Rule management --------------

    /**
     * Adds a rule to the perspective that will generate child
     * nodes for any given parent node.
     *
     * @param tm the tree model to be added
     */
    public void addSubTreeModel(TreeModel tm) {
        if (goRules.contains(tm)) return;
        goRules.addElement(tm);
    }

    /**
     * Remove a rule from the perspective that will generate child
     * nodes for any given parent node.
     *
     * @param tm the treemodel to be removed
     */
    public void removeSubTreeModel(TreeModel tm) {
        goRules.removeElement(tm);
    }

    /**
     * Get the rules that together form the perspective.
     *
     * @return the rules that form the perspecive
     */
    public Vector getSubTreeModels() {
        return goRules;
    }

    // ----------- name -------------------------

    /**
     * @return the name
     */
    public String getName() { return name; }


    /**
     * @param s the name
     */
    public void setName(String s) { name = s; }


    /*
     * @see java.lang.Object#toString()
     */
    public String toString() {
        if (getName() != null) return getName();
        else return super.toString();
    }

    // ------ all rules ----------

    /** TODO: factor out
     *
     * @param rule the rule to be added
     */
    public static void registerRule(TreeModel rule) {
        rules.addElement(rule);
    }

    /**
     * @return Returns the _goRules.
     */
    protected Vector getGoRules() {
        return goRules;
    }

}
