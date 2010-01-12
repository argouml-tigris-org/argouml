/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2008-2009 The Regents of the University of California. All
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * This layout manager lines up components in 2 columns. All JLabels
 * are the first column and any component the JLabel is registered
 * with is in a second column next to the label. <p>
 *
 * Components are sized automatically to fill available space in the container
 * when it is resized. <p>
 * 
 * All JLabel widths will be the largest of the JLabel preferred widths (unless
 * the container is too narrow). <p>
 * 
 * The components will take up any left over width unless they are
 * restricted themselves by a maximum width. <p>
 * 
 * The height of each component is either fixed or will resize to use up any
 * available space in the container. Whether a components height is resizable
 * is determined by checking whether the preferred height of that component is
 * greater then its minimum height. This is the case for components such as
 * JList which would require to expand to show the maximum number or items. <p>
 * 
 * If a component is not to have its height resized then its preferred
 * height and minimum height should be the same. This is the case for
 * components such as JTextField and JComboBox* which should always stay the
 * same height. <p>
 * 
 * [There is known bug in JRE5 where the prefered height and minimum height of
 * a JComboBox can differ. LabelledLayout has coded a workaround for this bug.
 * See - http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6255154 ] <p>
 * 
 * LabelledLayout can show multiple panels of label/component
 * pairs. The seperation of these panels is indicated by adding a
 * Seperator component to the container. Labelled layout starts
 * a new panel when detecting this Seperator. <p>
 * 
 * When there are multiple panels, each panel is given equal width.
 * The width restriction of JLabels and components described above are then
 * dependent on panel width rather than container width.
 *
 * @author Bob Tarling
 */
class LabelledLayout implements LayoutManager, java.io.Serializable {

    /**
     * The class uid
     */
    private static final long serialVersionUID = -5596655602155151443L;

    /**
     * This is the horizontal gap (in pixels) which specifies the space
     * between sections.  They can be changed at any time.
     * This should be a non negative integer.
     *
     * @serial
     * @see #getHgap()
     * @see #setHgap(int)
     */
    private int hgap;
    
    /**
     * This is the vertical gap (in pixels) which specifies the space
     * between rows.  They can be changed at any time.
     * This should be a non negative integer.
     *
     * @serial
     * @see #getVgap()
     * @see #setVgap(int)
     */
    private int vgap;

    private boolean ignoreSplitters;

    /**
     * Construct a new LabelledLayout.
     */
    public LabelledLayout() {
        ignoreSplitters = false;
        hgap = 0;
        vgap = 0;
    }

    /**
     * Construct a new LabelledLayout.
     */
    public LabelledLayout(boolean ignoreSplitters) {
        this.ignoreSplitters = ignoreSplitters;
        this.hgap = 0;
        this.vgap = 0;
    }

    /**
     * Construct a new horizontal LabelledLayout with the specified
     * cell spacing.
     * @param hgap The horizontal gap between components
     * @param vgap The vertical gap between components
     */
    public LabelledLayout(int hgap, int vgap) {
        this.ignoreSplitters = false;
        this.hgap = hgap;
        this.vgap = vgap;
    }

    /** 
     * Adds the specified component with the specified name to the
     * layout. This is included to satisfy the LayoutManager interface
     * but is not actually used in this layout implementation.
     *
     * @param name the name of the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /** 
     * Removes the specified component from
     * the layout. This is included to satisfy the LayoutManager
     * interface but is not actually used in this layout
     * implementation.
     *
     * @param comp the component
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Determines the preferred size of the container argument using
     * this labelled layout.  The preferred size is that all child
     * components are in one section at their own preferred size with
     * gaps and border indents.
     */
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            final Insets insets = parent.getInsets();
            int preferredWidth = 0;
            int preferredHeight = 0;
            int widestLabel = 0;

            final int componentCount = parent.getComponentCount();
            for (int i = 0; i < componentCount; ++i) {
                Component childComp = parent.getComponent(i);
                if (childComp.isVisible()
                        && !(childComp instanceof Seperator)) {
                    int childHeight = getPreferredHeight(childComp);
                    preferredHeight += childHeight + this.vgap;
                }
            }
            preferredWidth += insets.left + widestLabel + insets.right;
            preferredHeight += insets.top + insets.bottom;
            return new Dimension(
                    insets.left + widestLabel + preferredWidth + insets.right,
                    preferredHeight);
        }
    }
    
    /** 
     * Required by LayoutManager.
     * 
     * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
     */
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            final Insets insets = parent.getInsets();
            int minimumHeight = insets.top + insets.bottom;
            return new Dimension(0, minimumHeight);
        }
    }

    /**
     * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
     */
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int sectionX = parent.getInsets().left;

            final ArrayList<Component> components = new ArrayList<Component>();
            final int sectionCount = getSectionCount(parent);
            final int sectionWidth = getSectionWidth(parent, sectionCount);
            int sectionNo = 0;
            for (int i = 0; i < parent.getComponentCount(); ++i) {
                final Component childComp = parent.getComponent(i);
                if (childComp instanceof Seperator) {
                    if (!this.ignoreSplitters) {
                        layoutSection(
                                parent, 
                                sectionX, 
                                sectionWidth, 
                                components, 
                                sectionNo++);
                        sectionX += sectionWidth + this.hgap;
                        components.clear();
                    }
                } else {
                    components.add(parent.getComponent(i));
                }
            }
            layoutSection(
                    parent, 
                    sectionX, 
                    sectionWidth, 
                    components, 
                    sectionNo);
        }
    }

    /** Determine the number of sections.  There is only ever one
     * section if oriented vertically.  If oriented horizontally the
     * number of sections is deduced from the number of Splitters in
     * the parent container.
     */
    private int getSectionCount(Container parent) {
        int sectionCount = 1;
        final int componentCount = parent.getComponentCount();
        if (!ignoreSplitters) {
            for (int i = 0; i < componentCount; ++i) {
                if (parent.getComponent(i) instanceof Seperator) {
                    ++sectionCount;
                }
            }
        }
        return sectionCount;
    }
    
    /**
     * Determine the width of each section from the section count.
     * This is the working width minus the gaps between sections. This
     * result is then divided equally by the section count.
     */
    private int getSectionWidth(Container parent, int sectionCount) {
        return (getUsableWidth(parent) - (sectionCount - 1) * this.hgap)
                / sectionCount;
    }

    /**
     * Determine the usable width of the parent.
     * This is the full width minus any borders.
     */
    private int getUsableWidth(Container parent) {
        final Insets insets = parent.getInsets();
        return parent.getWidth() - (insets.left + insets.right);
    }
    
    /**
     * Layout a single section
     */
    private void layoutSection(
            final Container parent,
            final int sectionX,
            final int sectionWidth,
            final ArrayList components,
            final int sectionNo) {
        final ArrayList<Integer> rowHeights = new ArrayList<Integer>();

        final int componentCount = components.size();
        if (componentCount == 0) {
            return;
        }

        int labelWidth = 0;
        int unknownHeightCount = 0;
        int totalHeight = 0;

        // Build up an array list of the heights of each label/component pair.
        // Heights of zero indicate a proportional height.
        for (int i = 0; i < componentCount; ++i) {
            final Component childComp = (Component) components.get(i);
            if (childComp instanceof LabelledComponent) {
                LabelledComponent lc = (LabelledComponent) childComp;
                final JLabel label = lc.getLabel();
                if (label != null) {
                    if (label.getMinimumSize().width > labelWidth) {
                        labelWidth = label.getMinimumSize().width;
                    }
                }
            }
            final int childHeight = getChildHeight(childComp);
            if (childHeight == 0) {
                ++unknownHeightCount;
            }
            
            totalHeight += childHeight + this.vgap;
            rowHeights.add(new Integer(childHeight));
        }
        totalHeight -= this.vgap;
        
        final Insets insets = parent.getInsets();
        final int parentHeight = 
            parent.getHeight() - (insets.top + insets.bottom);
        // Set the child components to the heights in the array list
        // calculating the height of any proportional component on the
        // fly.
        int y = insets.top;
        int row = 0;
        for (int i = 0; i < componentCount; ++i) {
            Component childComp = (Component) components.get(i);
            if (childComp.isVisible()) {
                if (childComp instanceof LabelledComponent) {
                    final LabelledComponent lc = (LabelledComponent) childComp;
                    final JLabel label = lc.getLabel();
                    if (label != null) {
                        label.setPreferredSize(
                                new Dimension(
                                        labelWidth,
                                        label.getPreferredSize().height));
                    }
                }
                int rowHeight;
                int componentWidth = sectionWidth;
                int componentX = sectionX;
                // If the component is a JLabel which has another
                // component assigned then position/size the label and
                // calculate the size of the registered component
                rowHeight = rowHeights.get(row).intValue();
                if (rowHeight == 0) {
                    try {
                        rowHeight = calculateHeight(
                                parentHeight, 
                                totalHeight, 
                                unknownHeightCount--, 
                                childComp);
                    } catch (ArithmeticException e) {
                        final String lookAndFeel = 
                            UIManager.getLookAndFeel().getClass().getName();
                        throw new IllegalStateException(
                                "Division by zero laying out "
                                + childComp.getClass().getName()
                                + " on " + parent.getClass().getName()
                                + " in section " + sectionNo
                                + " using "
                                + lookAndFeel,
                                e);
                    }
                    totalHeight += rowHeight;
                }
                // Make sure the component width isn't any greater
                // than its maximum allowed width
                if (childComp.getMaximumSize() != null
                        && getMaximumWidth(childComp) < componentWidth) {
                    componentWidth = getMaximumWidth(childComp);
                }
                childComp.setBounds(componentX, y, componentWidth, rowHeight);
                y += rowHeight + this.vgap;
                ++row;
            }
        }
    }
    
    /**
     * @param childComp a component
     * @return 0 for a resizable component or a positive value for its fixed
     * height
     */
    private int getChildHeight(Component childComp) {
        if (isResizable(childComp)) {
            // If the child component is resizable then
            // we don't know it's actual size yet.
            // It will be calculated later as a
            // proportion of the available left over
            // space.  For now this is flagged as zero.
            return 0;
        } else {
            // If a preferred height is not given or is
            // the same as the minimum height then fix the
            // height of this row.
            return getMinimumHeight(childComp);
        }
    }

    /**
     * A component is resizable if its minimum size is less than
     * its preferred size.
     * There is a workaround here for a bug introduced in JRE5
     * where JComboBox minimum and preferred size now differ.
     * JComboBox is not resizable.
     * Anything in a JScrollPane is considered resizable
     * @param comp the component to check for resizability.
     * @return true if the given component should be resized to take u[p empty
     * space.
     */
    private boolean isResizable(Component comp) {
        if (comp == null) {
            return false;
        }
        if (comp instanceof JComboBox) {
            return false;
        }
        if (comp.getPreferredSize() == null) {
            return false;
        }
        if (comp.getMinimumSize() == null) {
            return false;
        }
        return (getMinimumHeight(comp) < getPreferredHeight(comp));
    }

    private final int calculateHeight(
            final int parentHeight, 
            final int totalHeight,
            final int unknownHeightsLeft, 
            final Component childComp) {
        return Math.max(
                (parentHeight - totalHeight) / unknownHeightsLeft,
                getMinimumHeight(childComp));
    }
    
    private int getPreferredHeight(final Component comp) {
        return (int) comp.getPreferredSize().getHeight();
    }
    
    private int getPreferredWidth(final Component comp) {
        return (int) comp.getPreferredSize().getWidth();
    }

    private int getMinimumHeight(final Component comp) {
        return (int) comp.getMinimumSize().getHeight();
    }
    
    private int getMaximumWidth(final Component comp) {
        return (int) comp.getMaximumSize().getWidth();
    }
    
    /**
     * Create a new instance of the Separator that splits the layout in columns
     * @return the separator
     */
    public static Seperator getSeparator() {
        return new Seperator();
    }

    /**
     * @return the horizontal gaps between components
     */
    public int getHgap() {
        return this.hgap;
    }

    /**
     * Set the horizontal gaps between components
     * @param hgap the horizontal gap
     */
    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    /**
     * @return the vertical gaps between components
     */
    public int getVgap() {
        return this.vgap;
    }

    /**
     * Set the vertical gaps between components
     * @param vgap the horizontal gap
     */
    public void setVgap(int vgap) {
        this.vgap = vgap;
    }
}

class Seperator extends JPanel {
    
    private static final long serialVersionUID = -4143634500959911688L;

    Seperator() {
        super.setVisible(false);
    }
}
