/*
 * GridLayout2.java
 */
package org.argouml.swingext;

import java.awt.*;
import javax.swing.*;
import java.util.*;

/**
 * Same as the standard java class GridLayout but allows more flexability for sizing of columns
 * and rows.
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
     * Construct a new GridLayout2 with a default of one column per component, in a single row.
     */
    public LabelledLayout() {
    }

    /**
     * Construct a new GridLayout2 with a default of one column per component, in a single row.
     */
    public LabelledLayout(Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows and columns, cell
     * spacing and cell sizing scheme.
     *
     * @param rows the number of rows in the layout
     * @param cols the number of columns in the layout
     */
    public LabelledLayout(int hgap, int vgap) {
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
            for (int i=0; i < componentCount; ++i) {
                Component childComp = parent.getComponent(i);
                if (childComp instanceof JLabel) {
                    JLabel jlabel = (JLabel)childComp;
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
                        childHeight = (int)jlabel.getPreferredSize().getHeight();
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
            for (int i=0; i < componentCount; ++i) {
                Component childComp = parent.getComponent(i);
                if (childComp instanceof JLabel) {
                    JLabel jlabel = (JLabel)childComp;
                    childComp = jlabel.getLabelFor();
                    int childHeight = (int)childComp.getMinimumSize().getHeight();
                    if (childHeight < jlabel.getMinimumSize().getHeight()) {
                        childHeight = (int)jlabel.getMinimumSize().getHeight();
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
                for (int i=0; i < componentCount; ++i) {
                    if (parent.getComponent(i) instanceof Seperator) ++sectionCount;
                }
                for (int i=0; i < componentCount; ++i) {
                    al.add(parent.getComponent(i));
                    if (parent.getComponent(i) instanceof Seperator) {
                        layoutSection(parent, sectionIndex++, sectionCount, al);
                        al.clear();
                    }
                }
            }
            else {
                componentCount = parent.getComponentCount();
                for (int i=0; i < componentCount; ++i) {
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
        for (int i=0; i < componentCount; ++i) {
            Component childComp = (Component)components.get(i);
            if (childComp instanceof JLabel) {
                JLabel jlabel = (JLabel)childComp;
                childWidth = (int) childComp.getPreferredSize().getWidth();
                if (childWidth > labelWidth) {
                    labelWidth = childWidth;
                }
                childHeight = (int) childComp.getPreferredSize().getHeight();

                childComp = jlabel.getLabelFor();
                if (childComp != null && childComp.getMinimumSize() != null && childHeight < childComp.getMinimumSize().getHeight()) {
                    if (childComp != null && childComp.getPreferredSize() != null && childComp.getMinimumSize().getHeight() < childComp.getPreferredSize().getHeight()) {
                        // If the preferred size is greater then the minimum size then this child components
                        // actual current size is currently unknown. It will be calculated later is a
                        // propertion of the available left over space.
                        // For now this is flagged as zero.
                        childHeight = 0;
                        ++unknownHeights;
                    }
                    else {
                        // If a preferred height is not given or is the same as the minimum height then
                        // fix the height of this row.
                        childHeight = (int) childComp.getMinimumSize().getHeight();
                    }
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
        for (int i=0; i < componentCount; ++i) {
            Component childComp = (Component)components.get(i);
            if (childComp instanceof JLabel) {
                JLabel jlabel = (JLabel)childComp;
                jlabel.getLabelFor().setVisible(jlabel.isVisible());
                if (jlabel.isVisible()) {
                    childComp = jlabel.getLabelFor();
                    rowHeight = ((Integer)rowHeights.get(row)).intValue();
                    if (rowHeight == 0) {
                        rowHeight = (parent.getHeight() - totalHeight) / unknownHeights;
                        if (rowHeight < childComp.getMinimumSize().getHeight()) {
                            rowHeight = (int)childComp.getMinimumSize().getHeight();
                        }
                        totalHeight += rowHeight;
                        --unknownHeights;
                    }
                    jlabel.setBounds(sectionX + x, y, labelWidth, (int)jlabel.getPreferredSize().getHeight());
                    if (childComp != null) {
                        childComp.setBounds(sectionX + x + labelWidth, y, sectionWidth - labelWidth, rowHeight);
                    }
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
    Seperator () {
        super.setVisible(false);
    }
}
