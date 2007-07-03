// $Id: GoStateMachineToState.java 11516 2006-11-25 04:30:15Z tfmorris $
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

// $header$

package org.argouml.ui.explorer.rules;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * PerspectiveRule to navigate from statemachine to the subvertices of
 * its top state (1 level deep only).
 *
 * @author jaap.branderhorst@xs4all.nl
 */
public class GoStateMachineToState extends AbstractPerspectiveRule {

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getRuleName()
     */
    public String getRuleName() {
        return Translator.localize ("misc.state-machine.state");
    }

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getChildren(
     *         java.lang.Object)
     */
    public Collection getChildren(Object parent) {

        if (Model.getFacade().isAStateMachine(parent)) {
            if (Model.getFacade().getTop(parent) != null) {
                return Model.getFacade().getSubvertices(
                        Model.getFacade().getTop(parent));
            }
        }
        return null;
    }

    /*
     * @see org.argouml.ui.explorer.rules.PerspectiveRule#getDependencies(
     *         java.lang.Object)
     */
    public Set getDependencies(Object parent) {
        if (Model.getFacade().isAStateMachine(parent)) {
	    Set set = new HashSet();
	    set.add(parent);
	    if (Model.getFacade().getTop(parent) != null) {
                set.add(Model.getFacade().getTop(parent));
            }
	    return set;
	}
	return null;
    }
}
