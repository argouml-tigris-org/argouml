// $Id: ActionModifierLeaf.java 11516 2006-11-25 04:30:15Z tfmorris $
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

class ActionModifierLeaf extends AbstractActionCheckBoxMenuItem {
    /**
     * Serial version generated for rev. 1.5
     */
    private static final long serialVersionUID = 1087245945242698348L;

    /**
     * The constructor.
     *
     * @param o the target
     */
    public ActionModifierLeaf(Object o) {
        super("checkbox.final-uc");
        putValue("SELECTED", Boolean.valueOf(valueOfTarget(o)));
    }

    /*
     * @see org.argouml.uml.diagram.ui.AbstractActionCheckBoxMenuItem#toggleValueOfTarget(java.lang.Object)
     */
    void toggleValueOfTarget(Object t) {
        Model.getCoreHelper().setLeaf(t, !Model.getFacade().isLeaf(t));
    }

    /*
     * @see org.argouml.uml.diagram.ui.AbstractActionCheckBoxMenuItem#valueOfTarget(java.lang.Object)
     */
    boolean valueOfTarget(Object t) {
        return Model.getFacade().isLeaf(t);
    }
}
