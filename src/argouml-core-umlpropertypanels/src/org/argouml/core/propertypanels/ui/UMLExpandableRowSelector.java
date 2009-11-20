// $Id: UMLSingleRowSelector.java 15914 2008-10-13 17:10:00Z bobtarling $
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.plaf.metal.MetalTreeUI;

import org.tigris.toolbar.toolbutton.ToolButton;

/**
 * A control for displaying the contents of a list model elements in a panel
 * that can be expanded to take maximum possible screen space or shrunk to
 * minimum at the user discretion.
 * 
 * @author Bob Tarling
 * @since 0.29.2
 */
public class UMLExpandableRowSelector extends JPanel {
    
    /**
     * The scrollpane that will contain the list
     */
    private JScrollPane scroll;

    private Dimension shrunkPreferredSize = null;
    private Dimension shrunkMinimumSize = null;
    private Dimension expandedPreferredSize = null;
    private Dimension expandedMinimumSize = null;
    private Dimension expandedMaximumSize = null;
    
    private boolean expanded = false;
    
    /**
     * Constructor
     * @param model The single item list model
     */
    public UMLExpandableRowSelector(UMLModelElementListModel model) {
        super(new BorderLayout());
        
        BasicTreeUI btu = new MetalTreeUI();
        Icon expandedIcon = btu.getExpandedIcon();
        Icon collapsedIcon = btu.getCollapsedIcon();
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(new ToolButton(new ExpandAction(expandedIcon)), BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.WEST);
        scroll = new ScrollList(model, 1);
        add(scroll);
        
        shrunkPreferredSize = scroll.getPreferredSize();
        shrunkMinimumSize = scroll.getMinimumSize();
        
        remove(scroll);
        scroll = new ScrollList(model);
        
        add(scroll);
        expandedPreferredSize = scroll.getPreferredSize();
        expandedMinimumSize = scroll.getMinimumSize();
        expandedMaximumSize = scroll.getMaximumSize();
        
        scroll.setHorizontalScrollBarPolicy(
        	JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
    
    /**
     * Make sure the control is always a fixed height
     * @return the minimum size as the height of one row in a JList
     */
    public Dimension getMinimumSize() {
        return shrunkPreferredSize;
    }
    
    /**
     * Make sure the control is always a fixed height
     * @return the maximum size as the height of one row in a JList
     */
    public Dimension getMaximumSize() {
        Dimension size = super.getMaximumSize();
        if (expanded) {
            size.height = expandedMaximumSize.height;
        } else {
            size.height = shrunkPreferredSize.height;
        }
        return size;
    }
    
    
    /**
     * @return the preferred size as the height of one row in a JList
     */
    public Dimension getPreferredSize() {
        if (expanded) {
            return expandedPreferredSize;
        } else {
            return shrunkPreferredSize;
        }
    }
    
    private class ExpandAction extends AbstractAction {
        ExpandAction(Icon icon) {
            super(null, icon);
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            expanded = !expanded;
            // TODO: This forces a redraw but what is the minimum we really
            // need here?
            getParent().validate();
            getParent().invalidate();
            getParent().repaint();
            getParent().doLayout();
            getParent().validate();
            getParent().invalidate();
            getParent().repaint();
            getParent().doLayout();
        }
    }
}
