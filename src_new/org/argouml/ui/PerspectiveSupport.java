// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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

import org.argouml.application.api.Argo;


/**
 * Helper class for tree models that provides help building perspectives
 * out of gorules.
 *
 * <p>$Id$
 *
 * @author  alexb
 * @since 0.13.5, Created on 15 April 2003
 */
public class PerspectiveSupport {
    
    /**
     * The go rules that this Tree model uses to build child nodes.
     */
    protected Vector _goRules;
    
    /** name */
    protected String _name;
    
    /** list of all possible rules in the collection Todolist specific */
    protected static Vector _rules = new Vector();
        
    protected PerspectiveSupport() { }
    
    /** Creates a new instance of PerspectiveSupport */
    public PerspectiveSupport(String name) {
        
        setName(Argo.localize("Tree", name));
        _goRules = new Vector();
    }
    
    /** needs documenting */
    public PerspectiveSupport(String name, Vector subs) {
        this(name);
        _goRules = subs;
    }
    
    // ------------- Rule management --------------
    
    /** adds a rule to the perspective that will generate child
     * nodes for any given parent node.
     */
    public void addSubTreeModel(TreeModel tm) {
        if (_goRules.contains(tm)) return;
        _goRules.addElement(tm);
    }
    
    /** remove a rule from the perspective that will generate child
     * nodes for any given parent node.
     */
    public void removeSubTreeModel(TreeModel tm) {
        _goRules.removeElement(tm);
    }
    
    /** get the rules that together form the perspective */
    public Vector getSubTreeModels() {
        return _goRules;
    }
    
    // ----------- name -------------------------
    
    /** needs documenting */
    public String getName() { return _name; }
    
    /** needs documenting */
    public void setName(String s) { _name = s; }
    
    /** needs documenting */
    public String toString() {
        if (getName() != null) return getName();
        else return super.toString();
    }
        
    // ------ all rules ----------
    
    /** TODO: factor out */
    public static void registerRule(TreeModel rule) {
        _rules.addElement(rule);
    }
    
    /** TODO: factor out */
    public static Vector getRegisteredRules() { return _rules; }
    
}
