// $Id$
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

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

/**
 * A control for displaying the association (or association role) of and
 * association end (or association end role) property.
 * @author Bob Tarling
 * @since 0.23 alpha3
 */
public class UMLSingleRowSelector extends JPanel {
    
    /**
     * The scrollpane showing the association that owns this associationend
     */
    private JScrollPane associationScroll;

    private Dimension preferredSize = null;
    
    /**
     * Constructor
     * @param model The single item list model
     */
    public UMLSingleRowSelector(ListModel model) {
        super(new BorderLayout());
        
        JList associationList = new UMLLinkedList(model);
        associationList.setVisibleRowCount(1);
        associationScroll = new JScrollPane(associationList);
        add(associationScroll);
        
        preferredSize = associationScroll.getPreferredSize();

        associationScroll.setVerticalScrollBarPolicy(
        	JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        associationScroll.setHorizontalScrollBarPolicy(
        	JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    /**
     * Make sure the control is always a fixed size
     * @return the minimum size as the height of one row in a JList
     */
    public Dimension getMinimumSize() {
        return preferredSize;
    }
    
    /**
     * Make sure the control is always a fixed size
     * @return the maximum size as the height of one row in a JList
     */
    public Dimension getMaximumSize() {
        return preferredSize;
    }
    
    
    /**
     * Make sure the control is always a fixed size
     * @return the preferred size as the height of one row in a JList
     */
    public Dimension getPreferredSize() {
        return preferredSize;
    }
}
