// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

// File: PropPanelStateVertex.java
// Classes: PropPanelStateVertex
// Original Author: oliver.heyden@gentleware.de
// $Id:

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.tigris.swidgets.Orientation;
import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;

/**
 * The properties tab panel for StateVertex and family.
 *
 */
public abstract class PropPanelStateVertex extends PropPanelModelElement {

    private JScrollPane incomingScroll;

    private JScrollPane outgoingScroll;

    private JScrollPane containerScroll;

    /**
     * Constructor for PropPanelStateVertex.
     * 
     * @param name the name of the tabpanel shown at the top
     * @param icon the icon of the tabpanel shown at the top
     * @param orientation the orientation
     */
    public PropPanelStateVertex(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);
        JList incomingList = new UMLLinkedList(
                new UMLStateVertexIncomingListModel());
        incomingScroll = new JScrollPane(incomingList);
        JList outgoingList = new UMLLinkedList(
                new UMLStateVertexOutgoingListModel());
        outgoingScroll = new JScrollPane(outgoingList);

        JList compositeList = new UMLLinkedList(
                new UMLStateVertexContainerListModel());
        compositeList.setVisibleRowCount(1);
        containerScroll = new JScrollPane(compositeList);

        addButton(new PropPanelButton2(this,
                new ActionNavigateNamespace()));
        new PropPanelButton(this, lookupIcon("Delete"), Translator.localize(
            "action.delete-from-model"), new ActionRemoveFromModel());
    }

    /**
     * @return Returns the incomingScroll.
     */
    protected JScrollPane getIncomingScroll() {
        return incomingScroll;
    }

    /**
     * @return Returns the outgoingScroll.
     */
    protected JScrollPane getOutgoingScroll() {
        return outgoingScroll;
    }

    /**
     * @return Returns the containerScroll.
     */
    protected JScrollPane getContainerScroll() {
        return containerScroll;
    }

} /* end class PropPanelStateVertex */

