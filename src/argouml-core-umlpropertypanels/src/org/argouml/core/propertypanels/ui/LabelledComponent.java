/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.plaf.metal.MetalTreeUI;

import org.tigris.swidgets.FlexiGridLayout;

/**
 * A wrapped label and component pair
 * 
 * @author Bob Tarling
 */
class LabelledComponent extends JPanel implements MouseListener {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -6805892904843209588L;

    private static final Set<String> EXPANDED_CONTROLS = new TreeSet<String>();
    
    /**
     * The icon to use when the control is expanded
     */
    private static Icon expandedIcon;

    private JComponent expansion;
    /**
     * The icon to use when the control is collapsed
     */
    private static Icon collapsedIcon;
    
    /**
     * The label for the component that might also be wrapped inside this
     * container
     */
    private final JLabel label;
    
    private final JComponent component;
    /**
     * The label that contains the +/- symbol to indicate
     * expansion feature to user.
     */
    private final JLabel expander;

    private final JPanel leftPanel;
    
    
    static {
        // Extract the icon that is used by the tree control
        // for the current look and feel
        final JTree dummyTree = new JTree();

        final BasicTreeUI btu;
        if (dummyTree.getUI() instanceof BasicTreeUI) {
            btu = (BasicTreeUI) dummyTree.getUI();
        } else {
            btu = new MetalTreeUI();
        }

        expandedIcon = btu.getExpandedIcon();
        collapsedIcon = btu.getCollapsedIcon();
    }
    
    /**
     * Construct a new LabelledComponent
     * @param name the name of the label to create
     * @param component the component to wrap
     */
    public LabelledComponent(final String name, final JComponent component) {
        super(new BorderLayout());
        this.component = component;
        setName(name);
        add(component, BorderLayout.CENTER);
        
        if (name != null) {
            label = new JLabel(name);
            label.setLabelFor(component);
            JPanel labelPanel = new JPanel();
            labelPanel.setLayout(new FlexiGridLayout(1, 2, FlexiGridLayout.ROWCOLPREFERRED));
            labelPanel.add(label);
            leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(labelPanel, BorderLayout.NORTH);
            add(leftPanel, BorderLayout.WEST);
            if (component instanceof Expandable && ((Expandable) component).isExpandable()) {
                expander = new JLabel();
        	labelPanel.add(expander);
        	addMouseListener(this);
                setIcon();
                if (EXPANDED_CONTROLS.contains(getId())) {
                    toggleExpansion((Expandable) component);
                }
            } else {
                expander = null;
            }
        } else {
            label = null;
            expander = null;
            leftPanel = null;
        }
    }
    
    private String getId() {
	return label.getText();
    }
    
    /**
     * Set the icon according to the current expansion setting
     */
    private void setIcon() {
	if (expander != null) {
            if (((Expandable) component).isExpanded()) {
                expander.setIcon(expandedIcon);
            } else {
                expander.setIcon(collapsedIcon);
            }
        }
    }
    
    
    
    /**
     * Get the label created from the component name
     * @return the label
     */
    JLabel getLabel() {
        return label;
    }
    
    public Dimension getPreferredSize() {
        if (component instanceof JComboBox) {
            return null;
        } else {
            return super.getPreferredSize();
        }
    }
    
    public void mouseClicked(MouseEvent e) {
	if (e.getSource() == this) {
            toggleExpansion((Expandable) component);
	}
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }
    
    /**
     * Toggle between expansion and contraction of the control
     */
    private void toggleExpansion(Expandable expandable) {
	
	boolean expanded = !expandable.isExpanded();
	
	expandable.setExpanded(expanded);
        
        if (expanded) {
            EXPANDED_CONTROLS.add(getId());
        } else {
            EXPANDED_CONTROLS.remove(getId());
        }

        setIcon();
        
        if (expansion == null) {
            expansion = expandable.getExpansion();
            leftPanel.add(expansion, BorderLayout.CENTER);
        }
        expansion.setVisible(expanded);

        // Force the parent to redraw
        Component c = component.getParent().getParent();
        if (c != null) {
            c.invalidate();
            c.validate();
        }
    }
}
