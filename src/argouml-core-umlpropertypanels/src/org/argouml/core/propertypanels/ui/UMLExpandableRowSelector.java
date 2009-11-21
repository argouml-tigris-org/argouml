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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.basic.BasicTreeUI;

/**
 * A control for displaying the contents of a list model elements in a panel
 * that can be expanded to take maximum possible screen space or shrunk to
 * minimum at the user discretion.
 * 
 * @author Bob Tarling
 * @since 0.29.2
 */
public class UMLExpandableRowSelector extends JPanel
        implements MouseListener {
    
    /**
     * class uid
     */
    private static final long serialVersionUID = 3937183621483536749L;
    
    /**
     * The icon to use when the control is expanded
     */
    private static Icon expandedIcon;
    
    /**
     * The icon to use when the control is collapsed
     */
    private static Icon collapsedIcon;
    
    static {
        final JTree dummyTree = new JTree();
        
        final TreeUI tu = dummyTree.getUI();
        
        if (tu instanceof BasicTreeUI) {
            final BasicTreeUI btu = (BasicTreeUI) tu;
            expandedIcon = btu.getExpandedIcon();
            collapsedIcon = btu.getCollapsedIcon();
        } else {
            // TODO: We want some default icons of our own here
            expandedIcon = null;
            collapsedIcon = null;
        }
    }
    
    /**
     * The scrollpane that will contain the list
     */
    private JScrollPane scroll;

    /**
     * The preferred size of the component when shrunk
     */
    private Dimension shrunkPreferredSize = null;

    /**
     * The preferred size of the component when expanded
     */
    private Dimension expandedPreferredSize = null;

    /**
     * The maximum size of the component when expanded
     */
    private Dimension expandedMaximumSize = null;
    
    /**
     * The current expanded state
     */
    private boolean expanded = false;

    private JLabel expander;
    
    /**
     * Constructor
     * @param model The single item list model
     */
    public UMLExpandableRowSelector(UMLModelElementListModel model) {
        super(new BorderLayout());
        
        JPanel buttonPanel = new JPanel();
        expander = new JLabel();
        expander.addMouseListener(this);
        setIcon();
        buttonPanel.add(expander, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.WEST);
        scroll = new ScrollList(model, 1);
        add(scroll);
        
        shrunkPreferredSize = scroll.getPreferredSize();
        
        remove(scroll);
        scroll = new ScrollList(model);
        
        add(scroll);
        expandedPreferredSize = scroll.getPreferredSize();
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
    
    @Override
    public void mouseClicked(MouseEvent e) {
        expanded = !expanded;
        
        setIcon();
        
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
    
    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }
    
    private void toggleExpansion() {
        expanded = !expanded;
        
        setIcon();
        
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

    private void setIcon() {
        if (expanded) {
            expander.setIcon(expandedIcon);
        } else {
            expander.setIcon(collapsedIcon);
        }
    }
}
