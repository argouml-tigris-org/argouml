// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.ui;

import org.argouml.model.Model;

class ActionModifierAbstract extends AbstractActionCheckBoxMenuItem {
    /**
     * The constructor.
     *
     * @param o the target
     */
    public ActionModifierAbstract(Object o) {
        super("checkbox.abstract-uc", NO_ICON);
        putValue("SELECTED", new Boolean(valueOfTarget(o)));
    }

    /**
     * This action is performed on ALL targets.
     *
     * @see org.argouml.uml.diagram.ui.AbstractActionCheckBoxMenuItem#toggleValueOfTarget(java.lang.Object)
     */
    void toggleValueOfTarget(Object t) {
        Model.getCoreHelper().setAbstract(t,
                            !Model.getFacade().isAbstract(t));
    }

    /**
     * This action should be enabled when:
     * <ul>
     * <li>all targets are modelelements that support
     *     the "abstract" checkmark and
     * <li>all targets are either abstract or not
     *     (mixed is not yet supported, but could be if a tri-state
     *     checkmark is implemented)
     * </ul>
     *
     * @see org.argouml.uml.diagram.ui.AbstractActionCheckBoxMenuItem#valueOfTarget(java.lang.Object)
     */
    boolean valueOfTarget(Object t) {
        return Model.getFacade().isAbstract(t);
    }
}
