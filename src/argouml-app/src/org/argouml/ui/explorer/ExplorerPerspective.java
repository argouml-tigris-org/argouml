/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
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

package org.argouml.ui.explorer;

import java.util.ArrayList;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.ui.explorer.rules.PerspectiveRule;

/**
 * Represents a perspective (or view) of the uml model for display in the
 * explorer.<p>
 *
 * This class replaces the old NavPerspective class. This is much simpler.<p>
 *
 * The rules in the perspective generate child nodes for any given parent
 * node in the explorer tree view. Those nodes are then stored as user objects
 * in the ExplorerTreeModel for efficient rendering.
 *
 * @author  alexb
 * @since 0.15.2, Created on 27 September 2003, 09:32
 */
public class ExplorerPerspective {

    private List<PerspectiveRule> rules;
    private String name;

    /**
     * Creates a new instance of ExplorerPerspective.
     *
     * @param newName the to be localized name for the perspective
     */
    public ExplorerPerspective(String newName) {
        name = Translator.localize(newName);
        rules = new ArrayList<PerspectiveRule>();
    }

    /**
     * @param rule the rule to add
     */
    public void addRule(PerspectiveRule rule) {
        rules.add(rule);
    }

    /**
     * @param rule the rule to remove
     */
    public void removeRule(PerspectiveRule rule) {
        rules.remove(rule);
    }

    /**
     * @return the array with all the rules
     */
    public Object[] getRulesArray() {
        return rules.toArray();
    }

    /**
     * @return the List with all the rules
     */
    public List<PerspectiveRule> getList() {
        return rules;
    }


    @Override
    public String toString() {
        return name;
    }

    /**
     * Make a clone of this ExplorerPerspective with a different given name.
     * @param newName the given name
     * @return the new ExplorerPerspective
     */
    public ExplorerPerspective makeNamedClone(String newName) {
        ExplorerPerspective ep = new ExplorerPerspective(newName);
        ep.rules.addAll(rules);
        return ep;
    }
    
    /**
     * @param theNewName the new name for the ExplorerPerspective
     */
    protected void setName(String theNewName) {
        this.name = theNewName;
    }
}
