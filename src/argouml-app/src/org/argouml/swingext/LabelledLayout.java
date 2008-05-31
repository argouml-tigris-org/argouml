// $Id: JLinkButton.java 14825 2008-05-28 23:21:50Z euluis $
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

package org.argouml.swingext;

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
public class LabelledLayout implements LayoutManager, java.io.Serializable {

    private static final long serialVersionUID = -5596655602155151443L;

    /**
     * This is the horizontal gap (in pixels) which specifies the space
     * between columns.  They can be changed at any time.
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
            Insets insets = parent.getInsets();
            int preferredWidth = 0;
            int preferredHeight = 0;
            int widestLabel = 0;

            int componentCount = parent.getComponentCount();
            for (int i = 0; i < componentCount; ++i) {
                Component childComp = parent.getComponent(i);
                if (childComp.isVisible() && !(childComp instanceof Seperator)) {
                    int childHeight = (int) childComp.getPreferredSize().getHeight();
                    if (childComp instanceof JLabel) {
                        JLabel jlabel = (JLabel) childComp;
                        if (widestLabel < jlabel.getPreferredSize().getWidth()) {
                            widestLabel = (int) jlabel.getPreferredSize().getWidth();
                        }
                        childComp = jlabel.getLabelFor();
                        int childWidth = (int) childComp.getPreferredSize().getWidth();
                        if (childWidth > preferredWidth) {
                            preferredWidth = childWidth;
                        }

                        if (childHeight < jlabel.getPreferredSize().getHeight())
			{
                            childHeight = (int) jlabel.getPreferredSize().getHeight();
                        }
                    }
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
            Insets insets = parent.getInsets();
            int minimumHeight = insets.top + insets.bottom;

            int componentCount = parent.getComponentCount();
            for (int i = 0; i < componentCount; ++i) {
                Component childComp = parent.getComponent(i);
                if (childComp instanceof JLabel) {
                    JLabel jlabel = (JLabel) childComp;
                    childComp = jlabel.getLabelFor();
                    int childHeight = (int) childComp.getMinimumSize().getHeight();
                    if (childHeight < jlabel.getMinimumSize().getHeight()) {
                        childHeight = (int) jlabel.getMinimumSize().getHeight();
                    }
                    minimumHeight += childHeight + this.vgap;
                }
            }
            return new Dimension(0, minimumHeight);
        }
    }

    /**
     * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
     */
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int sectionX = parent.getInsets().left;

            ArrayList components = new ArrayList();
            int sectionCount = getSectionCount(parent);
            int sectionWidth = getSectionWidth(parent, sectionCount);
            int sectionNo = 0;
            for (int i = 0; i < parent.getComponentCount(); ++i) {
                Component childComp = parent.getComponent(i);
                if (childComp instanceof Seperator) {
                    if (!this.ignoreSplitters) {
                        layoutSection(parent, sectionX, sectionWidth, components, sectionNo++);
                        sectionX += sectionWidth + this.hgap;
                        components.clear();
                    }
                } else {
                    components.add(parent.getComponent(i));
                }
            }
            layoutSection(parent, sectionX, sectionWidth, components, sectionNo);
        }
    }

    /** Determine the number of sections.  There is only ever one
     * section if oriented vertically.  If oriented horizontally the
     * number of sections is deduced from the number of Splitters in
     * the parent container.
     */
    private int getSectionCount(Container parent) {
        int sectionCount = 1;
        int componentCount = parent.getComponentCount();
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
        Insets insets = parent.getInsets();
        return parent.getWidth() - (insets.left + insets.right);
    }
    
    /**
     * Layout a single section
     */
    private void layoutSection(
            Container parent,
            int sectionX,
            int sectionWidth,
            ArrayList components,
            int sectionNo) {
        ArrayList rowHeights = new ArrayList();

        int componentCount = components.size();
        if (componentCount == 0) {
            return;
        }

        int childWidth, childHeight;
        int labelWidth = 0;
        int unknownHeightCount = 0;
        int totalHeight = 0;

        // Build up an array list of the heights of each label/component pair.
        // Heights of zero indicate a proportional height.
        for (int i = 0; i < componentCount; ++i) {
            Component childComp = (Component) components.get(i);
            if (childComp instanceof JLabel) {
                JLabel jlabel = (JLabel) childComp;
                childComp = jlabel.getLabelFor();
                childWidth = (int) jlabel.getPreferredSize().getWidth();
                if (childWidth > labelWidth) {
                    labelWidth = childWidth;
                }
                // Start off with the child height being the preferred
                // height of the label.
                childHeight = (int) jlabel.getPreferredSize().getHeight();

                if (childComp != null) {
                    i++;
                    childHeight = getChildHeight(childComp);
                    if (childHeight == 0) {
                        ++unknownHeightCount;
                    }
                }
            } else {
                // to manage the case there are no label/component
                // pairs but just one component
                childHeight = getChildHeight(childComp);
                if (childHeight == 0) {
                    ++unknownHeightCount;
                }
            }
            
            totalHeight += childHeight + this.vgap;
            rowHeights.add(new Integer(childHeight));
        }
        totalHeight -= this.vgap;
        
        Insets insets = parent.getInsets();
        int parentHeight = parent.getHeight() - (insets.top + insets.bottom);
        // Set the child components to the heights in the array list
        // calculating the height of any proportional component on the
        // fly.  FIXME - This assumes that the JLabel and the
        // component it labels have been added to the parent component
        // consecutively.
        int y = insets.top;
        int row = 0;
        for (int i = 0; i < componentCount; ++i) {
            Component childComp = (Component) components.get(i);
            if (childComp.isVisible()) {
                int rowHeight;
                int componentWidth = sectionWidth;
                int componentX = sectionX;
                // If the component is a JLabel which has another
                // component assigned then position/size the label and
                // calculate the size of the registered component
                if (childComp instanceof JLabel && ((JLabel) childComp).getLabelFor() != null) {
                    i++; // Assumes the next child is the labelled component (could be improved)
                    JLabel jlabel = (JLabel) childComp;
                    childComp = jlabel.getLabelFor();
                    jlabel.setBounds(sectionX, y, labelWidth,
				     (int)
				     jlabel.getPreferredSize().getHeight());
                    componentWidth = sectionWidth - (labelWidth + this.hgap);
                    componentX = sectionX + labelWidth + this.hgap;
                }
                rowHeight = ((Integer) rowHeights.get(row)).intValue();
                if (rowHeight == 0) {
                    try {
                        rowHeight = calculateHeight(parentHeight, totalHeight, unknownHeightCount--, childComp);
                    } catch(ArithmeticException e) {
                        // We should put the exception as second parameter when we use JDK 1.5 exclusively
                        throw new IllegalStateException(
                                "Division by zero laying out " + childComp.getClass().getName() +
                                " on " + parent.getClass().getName() +
                                " in section " + sectionNo +
                                " using " + UIManager.getLookAndFeel().getClass().getName() + ":" + e.getMessage());                        
                    }
                    totalHeight += rowHeight;
                }
                // Make sure the component width isn't any greater
                // than its maximum allowed width
                if (childComp.getMaximumSize() != null
		    && childComp.getMaximumSize().getWidth() < componentWidth)
		{
		    componentWidth =
			(int) childComp.getMaximumSize().getWidth();
                }
                childComp.setBounds(componentX, y, componentWidth, rowHeight);
                y += rowHeight + this.vgap;
                ++row;
            }
        }
    }
    
    private int getChildHeight(Component childComp) {
        if (isResizable(childComp)) {
            // If the child component is resizable then
            // we don't know it's actual size yet.
            // It will be calculated later as a
            // propertion of the available left over
            // space.  For now this is flagged as zero.
            return 0;
        } else {
            // If a preferred height is not given or is
            // the same as the minimum height then fix the
            // height of this row.
            return childComp.getMinimumSize().height;
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
        if (comp == null) return false;
        if (comp instanceof JComboBox) return false;
        if (comp.getPreferredSize() == null) return false;
        if (comp.getMinimumSize() == null) return false;
        return (comp.getMinimumSize().getHeight()
                    < comp.getPreferredSize().getHeight());
    }

    private int calculateHeight(int parentHeight, int totalHeight,
				int unknownHeightsLeft, Component childComp) {
        int rowHeight = (parentHeight - totalHeight) / unknownHeightsLeft;
        if (rowHeight < childComp.getMinimumSize().getHeight()) {
            rowHeight = (int) childComp.getMinimumSize().getHeight();
        }
        return rowHeight;
    }
    
    public static Seperator getSeparator() {
        return new Seperator();
    }

    public int getHgap() {
        return this.hgap;
    }

    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    public int getVgap() {
        return this.vgap;
    }

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
