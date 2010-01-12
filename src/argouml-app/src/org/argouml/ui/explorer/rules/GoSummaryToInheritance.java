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

package org.argouml.ui.explorer.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * Rule for Summary->Inheritance.
 * This class is a Go Rule for the "Class - centric" Navigation perspective.
 *
 * @author  alexb, d00mst
 * @since argo 0.13.4, Created on 21 March 2003, 23:18
 */
public class GoSummaryToInheritance extends AbstractPerspectiveRule {

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getRuleName()
     */
    public String getRuleName() {
        return Translator.localize("misc.summary.inheritance");
    }

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getChildren(java.lang.Object)
     */
    public Collection getChildren(Object parent) {
	if (parent instanceof InheritanceNode) {
	    List list = new ArrayList();

	    Iterator it =
		Model.getFacade().getSupplierDependencies(
			((InheritanceNode) parent).getParent()).iterator();

	    while (it.hasNext()) {
		Object next = it.next();
		if (Model.getFacade().isAAbstraction(next)) {
		    list.add(next);
		}
	    }

	    it =
		Model.getFacade().getClientDependencies(
		        ((InheritanceNode) parent).getParent()).iterator();

	    while (it.hasNext()) {
		Object next = it.next();
		if (Model.getFacade().isAAbstraction(next)) {
		    list.add(next);
		}
	    }

	    Iterator generalizationsIt =
		Model.getFacade().getGeneralizations(
			((InheritanceNode) parent).getParent()).iterator();
	    Iterator specializationsIt =
		Model.getFacade().getSpecializations(
			((InheritanceNode) parent).getParent()).iterator();

	    while (generalizationsIt.hasNext()) {
		list.add(generalizationsIt.next());
	    }

	    while (specializationsIt.hasNext()) {
		list.add(specializationsIt.next());
	    }

	    return list;
	}

	return Collections.EMPTY_SET;
    }

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getDependencies(java.lang.Object)
     */
    public Set getDependencies(Object parent) {
        if (parent instanceof InheritanceNode) {
	    Set set = new HashSet();
	    set.add(((InheritanceNode) parent).getParent());
	    return set;
	}
	return Collections.EMPTY_SET;
    }

}
