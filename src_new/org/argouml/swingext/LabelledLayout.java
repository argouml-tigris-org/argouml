// Copyright (c) 1996-2003 The Regents of the University of California. All
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

package org.argouml.swingext;

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * This layout manager lines up components in 2 columns. All JLabels are the first column and
 * any component the JLabel is registered with is in a second column next to the label. <br />
 * The height of each row is the largest minimum height of the 2 components. <br />
 * The width of the first column is the largest preferred width of the 2 components. <br />
 * The width of the 2nd column is any left over space or the maximum width of the component,
 * whichever is the least. <br />
 * LabelledLayout can show multiple panels of label/component pairs. The seperation of these
 * panels is indicated by adding a Seperator component to the parent component. Labelled layout
 * starts a new panel when detecting this Seperator.
 *
 * @author Bob Tarling
 */
public class LabelledLayout implements LayoutManager, java.io.Serializable {

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

    /**
     * The height of the child component with the largest height
     */
    protected int largestHeight;
    /**
     * The width of the child component with the largest width
     */
    protected int largestWidth;
    /**
     * The required cell width of the labels column
     */
    protected int labelWidth;

    private Orientation orientation;

    /**
     * Construct a new LabelledLayout with a default of one column per component, in a single row.
     */
    public LabelledLayout() {
    }

    /**
     * Construct a new LabelledLayout with the given orientation.
     * The orientation determines how panels are layed out. A horizontal orientation
     * has panels to the right of the previous. Vertical orientation has panels layed
     * out below the previous
     */
    public LabelledLayout(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Construct a new horizontal LabelledLayout with the specified cell spacing.
     */
    public LabelledLayout(int hgap, int vgap) {
        this.orientation = Horizontal.getInstance();
        this.hgap = hgap;
        this.vgap = vgap;
    }

    /** 
     * Adds the specified component with the specified name to the layout. This is included
     * to satisfy the LayoutManager interface but is not actually used in this layout
     * implementation.
     *
     * @param name the name of the component
     * @param comp the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /** 
     * Removes the specified component with the specified name from the layout. This is included
     * to satisfy the LayoutManager interface but is not actually used in this layout
     * implementation.
     *
     * @param name the name of the component
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Determines the preferred size of the container argument using this labelled layout. 
     */
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int preferredWidth = 0;
            int preferredHeight = 0;
            int widestLabel = 0;
            ArrayList rowHeights = new ArrayList();

            int componentCount = parent.getComponentCount();
            for (int i = 0; i < componentCount; ++i) {
                Component childComp = parent.getComponent(i);
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

                    int childHeight = (int) childComp.getPreferredSize().getHeight();
                    if (childHeight < jlabel.getPreferredSize().getHeight()) {
                        childHeight = (int) jlabel.getPreferredSize().getHeight();
                    }
                    preferredHeight += childHeight;
                }
            }
            preferredWidth += insets.left + widestLabel + insets.right;
            preferredHeight += insets.top + insets.bottom;
            return new Dimension(insets.left + widestLabel + preferredWidth + insets.right, preferredHeight);
        }
    }

    /* Required by LayoutManager. */
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int minimumHeight = insets.top + insets.bottom;

            ArrayList rowHeights = new ArrayList();

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
                    minimumHeight += childHeight;
                }
            }
            return new Dimension(0, minimumHeight);
        }
    }

    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int sectionCount = 1;
            int componentCount = parent.getComponentCount();

            int sectionIndex = 0;
            ArrayList al = new ArrayList();
            if (orientation.equals(Horizontal.getInstance())) {
                for (int i = 0; i < componentCount; ++i) {
                    if (parent.getComponent(i) instanceof Seperator)
                        ++sectionCount;
                }
                for (int i = 0; i < componentCount; ++i) {
                    al.add(parent.getComponent(i));
                    if (parent.getComponent(i) instanceof Seperator) {
                        layoutSection(parent, sectionIndex++, sectionCount, al);
                        al.clear();
                    }
                }
            } else {
                componentCount = parent.getComponentCount();
                for (int i = 0; i < componentCount; ++i) {
                    al.add(parent.getComponent(i));
                }
            }
            layoutSection(parent, sectionIndex, sectionCount, al);
        }
    }

    public int getHgap() {
        return hgap;
    }

    public void setHgap(int hgap) {
        this.hgap = hgap;
    }

    public int getVgap() {
        return vgap;
    }

    public void setVgap(int vgap) {
        this.vgap = vgap;
    }

    private void layoutSection(Container parent, int sectionIndex, int sectionCount, ArrayList components) {
        int sectionWidth = parent.getWidth() / sectionCount;
        int sectionX = sectionWidth * sectionIndex;

        ArrayList rowHeights = new ArrayList();

        int componentCount = components.size();
        if (componentCount == 0) {
            return;
        }

        int childWidth, childHeight;
        int labelWidth = 0;
        int unknownHeights = 0;
        int totalHeight = 0;

        // Build up an array list of the heights of each label/component pair.
        // Heights of zero indicate a proportional height.
        for (int i = 0; i < componentCount; ++i) {
            Component childComp = (Component) components.get(i);
            if (childComp instanceof JLabel) {
                JLabel jlabel = (JLabel) childComp;
                childWidth = (int) childComp.getPreferredSize().getWidth();
                if (childWidth > labelWidth) {
                    labelWidth = childWidth;
                }
                childHeight = (int) childComp.getPreferredSize().getHeight();

                childComp = jlabel.getLabelFor();
                if (childComp != null)
                    i++;
                if (childComp != null && childComp.getMinimumSize() != null && childHeight < childComp.getMinimumSize().getHeight()) {
                    if (childComp != null && childComp.getPreferredSize() != null && childComp.getMinimumSize().getHeight() < childComp.getPreferredSize().getHeight()) {
                        // If the preferred size is greater then the minimum size then this child components
                        // actual current size is currently unknown. It will be calculated later is a
                        // propertion of the available left over space.
                        // For now this is flagged as zero.
                        childHeight = 0;
                        ++unknownHeights;
                    } else {
                        // If a preferred height is not given or is the same as the minimum height then
                        // fix the height of this row.
                        childHeight = (int) childComp.getMinimumSize().getHeight();
                    }
                }
                totalHeight += childHeight;
                rowHeights.add(new Integer(childHeight));
            } else {
                // to manage the case there are no label/component pairs but just one component
                childHeight = (int) childComp.getPreferredSize().getHeight();
                if (childComp != null && childComp.getPreferredSize() != null && childComp.getMinimumSize().getHeight() < childComp.getPreferredSize().getHeight()) {
                    // If the preferred size is greater then the minimum size then this child components
                    // actual current size is currently unknown. It will be calculated later is a
                    // propertion of the available left over space.
                    // For now this is flagged as zero.
                    childHeight = 0;
                    ++unknownHeights;
                } else {
                    // If a preferred height is not given or is the same as the minimum height then
                    // fix the height of this row.
                    childHeight = (int) childComp.getMinimumSize().getHeight();
                }
                totalHeight += childHeight;
                rowHeights.add(new Integer(childHeight));
            }

        }

        Insets insets = parent.getInsets();

        // Set the child components to the heights in the array list calculating the
        // height of any proportional component on the fly.
        int x = insets.left;
        int y = insets.right;
        int row = 0;
        int rowHeight;
        for (int i = 0; i < componentCount; ++i) {
            Component childComp = (Component) components.get(i);
            if (childComp instanceof JLabel) {
                JLabel jlabel = (JLabel) childComp;
                if (jlabel.getLabelFor() != null) 
                    i++;
                jlabel.getLabelFor().setVisible(jlabel.isVisible());
                if (jlabel.isVisible()) {
                    childComp = jlabel.getLabelFor();
                    rowHeight = ((Integer) rowHeights.get(row)).intValue();
                    if (rowHeight == 0) {
                        rowHeight = (parent.getHeight() - totalHeight) / unknownHeights;
                        if (rowHeight < childComp.getMinimumSize().getHeight()) {
                            rowHeight = (int) childComp.getMinimumSize().getHeight();
                        }
                        totalHeight += rowHeight;
                        --unknownHeights;
                    }
                    jlabel.setBounds(sectionX + x, y, labelWidth, (int) jlabel.getPreferredSize().getHeight());
                    if (childComp != null) {
                        childComp.setBounds(sectionX + x + labelWidth, y, sectionWidth - labelWidth, rowHeight);
                    }
                    y += rowHeight;
                    ++row;
                }
            } else {
                // handle the case where there is no label.
                if (childComp.isVisible()) {
                    rowHeight = ((Integer) rowHeights.get(row)).intValue();
                    if (rowHeight == 0) {
                        rowHeight = (parent.getHeight() - totalHeight) / unknownHeights;
                        if (rowHeight < childComp.getMinimumSize().getHeight()) {
                            rowHeight = (int) childComp.getMinimumSize().getHeight();
                        }
                        totalHeight += rowHeight;
                        --unknownHeights;
                    }
                    childComp.setBounds(sectionX + x, y, sectionWidth, rowHeight);
                    y += rowHeight;
                    ++row;
                }
            }
        }
    }

    static public Seperator getSeperator() {
        return new Seperator();
    }

}
class Seperator extends JPanel {
    Seperator() {
        super.setVisible(false);
    }
}
