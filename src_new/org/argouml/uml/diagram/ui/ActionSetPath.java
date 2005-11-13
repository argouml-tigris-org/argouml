// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import java.util.Iterator;

import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;

/**
 * Action to set or unset the display of the path (namespace) for elements
 * supporting such an action.
 *
 * @see org.argouml.uml.diagram.ui.PathContainer
 * @author MarkusK
 */
public class ActionSetPath extends UMLAction {

    private boolean display;

    public static final UMLAction SHOW = new ActionSetPath("action.show-path",
            true);

    public static final UMLAction HIDE = new ActionSetPath("action.hide-path",
            false);

    /**
     * @param key
     *            key for i18n text to display
     * @param display
     *            whether to show the path or not.
     */
    protected ActionSetPath(String key, boolean display) {
        super(key, true, NO_ICON);
        this.display = display;
    }

    public void actionPerformed(ActionEvent ae) {
        Iterator i = Globals.curEditor().getSelectionManager().selections()
                .iterator();
        while (i.hasNext()) {
            Selection sel = (Selection) i.next();
            Fig f = sel.getContent();

            if (f instanceof PathContainer)
                ((PathContainer) f).setPathVisible(display);
        }
    }
}
