// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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



// File: PropPanelState.java
// Classes: PropPanelState
// Original Author: your email address here
// $Id$

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.swingext.Orientation;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;

public abstract class PropPanelState extends PropPanelStateVertex {

    protected JScrollPane entryScroll;
    protected JScrollPane exitScroll;
    protected JScrollPane doScroll;
    protected JScrollPane internalTransitionsScroll;
    protected JScrollPane deferrableEventsScroll;
    protected JList entryList;
    protected JList exitList;
    protected JList doList;
    protected JList internalTransitionsList;


    /**
     * Constructor for PropPanelState.
     * @param name
     * @param icon
     * @param orientation
     */
    public PropPanelState(
        String name,
        ImageIcon icon,
        Orientation orientation) {
        super(name, icon, orientation);
        
        JList deferrableList = new UMLLinkedList(new UMLStateDeferrableEventListModel());
        deferrableEventsScroll = new JScrollPane(deferrableList);
        JList entryList = new UMLStateEntryList(new UMLStateEntryListModel());
        entryList.setVisibleRowCount(1);
        entryScroll = new JScrollPane(entryList);
        JList exitList = new UMLStateExitList(new UMLStateExitListModel());
        exitList.setVisibleRowCount(1);
        exitScroll = new JScrollPane(exitList);
        JList internalTransitionList = new UMLMutableLinkedList(new UMLStateInternalTransition(), null, ActionNewTransition.SINGLETON);
        internalTransitionsScroll = new JScrollPane(internalTransitionList);
        JList doList = new UMLStateDoActivityList(new UMLStateDoActivityListModel());
        doList.setVisibleRowCount(1);
        doScroll = new JScrollPane(doList);
    }

    


} /* end class PropPanelState */



