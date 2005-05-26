// $Id$
// Copyright (c) 2002-2005 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * @since Oct 3, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelButton2 extends JButton
        implements UMLUserInterfaceComponent, MElementListener {

    /**
     * Constructor for PropPanelButton2.
     * @param a the action
     */
    public PropPanelButton2(Action a) {
        super(a);
        setText(""); // just the icon and possibly a tooltip
    }

    /**
     * The constructor.
     *
     * @param a the action for this button
     * @param i the icon to be shown
     */
    public PropPanelButton2(Action a, Icon i) {
        this(a);
        a.putValue(Action.SMALL_ICON, i);
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        setEnabled(isEnabled());
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
        setEnabled(isEnabled());
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
    }

}
