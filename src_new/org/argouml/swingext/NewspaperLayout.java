// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
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

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

/**
 * Similar to {@link GridLayout2} but once the components fill
 * the height of the container they flow into another grid on the
 * right until the full width of the container is filled. Once the
 * containers width is full it flows to the right no more, the grid
 * depths increase instead so that the user scrolls up/down instead of
 * left/right.
 *
 * @author Bob Tarling
 */
public class NewspaperLayout extends GridLayout2 {
    private static final Logger LOG = Logger.getLogger(NewspaperLayout.class);

    private int gridGap = 0;
    private int preferredX;
    private int preferredY;

    private int gridWidth;

    public NewspaperLayout() {
        this(1, 0, 0, 0, 0);
    }

    public NewspaperLayout(int rows, int cols) {
        this(rows, cols, 0, 0, 0);
    }

    public NewspaperLayout(int rows, int cols, int hgap, int vgap, int gridGap)
    {
        super(rows, cols, hgap, vgap, ROWCOLPREFERRED, NONE, NORTHWEST);
        this.gridGap = gridGap;
    }

    /**
     * @see java.awt.LayoutManager#addLayoutComponent(java.lang.String, java.awt.Component)
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * @see java.awt.LayoutManager#removeLayoutComponent(java.awt.Component)
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * @see java.awt.LayoutManager#preferredLayoutSize(java.awt.Container)
     */
    public Dimension preferredLayoutSize(Container parent) {
        JComponent comp = (JComponent) parent;
        Rectangle rect = comp.getVisibleRect();
        //preferredX = (int) rect.getWidth();
        LOG.debug("Visible width = " + preferredX);
        LOG.debug("Visible X = " + rect.getX() + " Width = " + preferredX);
        Insets insets = parent.getInsets();
        layoutContainer(parent);
        if (preferredX < insets.right + gridWidth + insets.left)
	    preferredX = insets.right + gridWidth + insets.left;
        LOG.debug("Preferred width = " + preferredX);
        return new Dimension(preferredX, preferredY);
    }

    /**
     * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
     */
    public Dimension minimumLayoutSize(Container parent) {
        Insets insets = parent.getInsets();
        return new Dimension(insets.right + gridWidth + insets.left, 0);
    }

    public Dimension maximumLayoutSize(Container parent) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * @see java.awt.LayoutManager#layoutContainer(java.awt.Container)
     */
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int ncomponents = parent.getComponentCount();
            if (ncomponents == 0) {
                return;
            }
            Insets insets = parent.getInsets();
            int nrows = this.getRows();
            int ncols = this.getColumns();

            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }

            // Determine the width for each column and the height for each row.
            colWidth = new int[ncols];
            rowHeight = new int[nrows];
            largestWidth = 0;
            largestHeight = 0;

            if (cellSizing == FITPARENT) {
                int availableWidth =
		    parent.getWidth()
		    - (insets.left + insets.right + (ncols - 1) * getHgap());
                int availableHeight =
		    parent.getHeight()
		    - (insets.top + insets.bottom + (nrows - 1) * getVgap());
                largestWidth = availableWidth / ncols;
                largestHeight = availableHeight / nrows;
            }
            else {

                for (int c = 0; c < ncols; ++c) {
                    for (int r = 0; r < nrows; ++r) {
                        int i = r * ncols + c;
                        if (parent.getComponent(i).getPreferredSize().getWidth()
			    > colWidth[c]) {
                            colWidth[c] =
				(int) parent.getComponent(i)
				    .getPreferredSize().getWidth();
                            if (colWidth[c] > largestWidth)
				largestWidth = colWidth[c];
                        }
                        if ((parent.getComponent(i)
			     .getPreferredSize().getHeight())
			    > rowHeight[r]) {
                            rowHeight[r] =
				(int) parent.getComponent(i)
				    .getPreferredSize().getHeight();
                            if (rowHeight[r] > largestHeight) {
				largestHeight = rowHeight[r];
			    }
                        }
                    }
                }
            }

            // Calculate width
            gridWidth = (ncols - 1) * getHgap();
            for (int c = 0; c < ncols; ++c) {
                gridWidth += colWidth[c];
            }

            // Calculate Height
            int gridHeight = (nrows - 1) * getVgap();
            for (int r = 0; r < nrows; ++r) {
                gridHeight += rowHeight[r];
            }

            int numberOfGrids =
		positionComponentsInternal(parent, colWidth, rowHeight,
					   gridHeight, nrows, ncols);
            if (numberOfGrids > 0) {
                positionComponentsExternal(parent, colWidth, rowHeight,
					   gridHeight, nrows, ncols,
					   numberOfGrids);
            }
        }
    }

    private int positionComponentsInternal(Container parent,
					   int colWidth[], int rowHeight[],
					   int gridHeight, int nrows, int ncols)
    {
        JComponent parentComp = (JComponent) parent;
        int visibleHeight = (int) parentComp.getVisibleRect().getHeight();
        int visibleWidth = (int) parentComp.getVisibleRect().getWidth();
        int ncomponents = parent.getComponentCount();
        Insets insets = parent.getInsets();
        int newsColumn = 0;
        int highestY = 0;
        int y = insets.top;
        int cellHeight;
        int cellWidth;
        for (int r = 0; r < nrows; ++r) {

            cellHeight = getComponentCellHeight(r);

            if (y + cellHeight + insets.bottom > visibleHeight ) {
                y = insets.top;
                newsColumn++;
                if ((insets.left
		     + insets.right
		     + newsColumn * (gridWidth + gridGap)
		     + gridWidth)
		    > visibleWidth)
		    return newsColumn;
            }

            int x = insets.left + newsColumn * (gridWidth + gridGap);
            for (int c = 0; c < ncols; ++c) {
                cellWidth = getComponentCellWidth(c);

                int i = r * ncols + c;
                if (i < ncomponents) {
                    positionComponentInCell(parent.getComponent(i), x, y,
					    cellWidth, cellHeight);
                    if (y + cellHeight > highestY) highestY = y + cellHeight;
                }
                x += cellWidth + getHgap();
            }
            y += cellHeight + getVgap();
        }
        preferredY = highestY + insets.bottom;
        return -1;
    }


    private boolean positionComponentsExternal(Container parent,
					       int colWidth[], int rowHeight[],
					       int gridHeight,
					       int nrows, int ncols,
					       int maxGrids) {
        int ncomponents = parent.getComponentCount();
        Insets insets = parent.getInsets();
        int newsColumn = 0;
        int targetHeight = gridHeight / maxGrids;
        int highestY = 0;
        int y = insets.top;
        int componentCellHeight;
        int componentCellWidth;
        for (int r = 0; r < nrows; ++r) {
            if (cellSizing != ROWCOLPREFERRED)
		componentCellHeight = largestHeight;
            else componentCellHeight = rowHeight[r];

            int x = insets.left + newsColumn * (gridWidth + gridGap);
            for (int c = 0; c < ncols; ++c) {
                if (cellSizing != ROWCOLPREFERRED)
		    componentCellWidth = largestWidth;
                else componentCellWidth = colWidth[c];

                int i = r * ncols + c;
                if (i < ncomponents) {
                    positionComponentInCell(parent.getComponent(i),
					    x, y,
					    componentCellWidth,
					    componentCellHeight);
                    if (y + componentCellHeight > highestY)
			highestY = y + componentCellHeight;
                }
                x += componentCellWidth + getHgap();
            }
            y += componentCellHeight + getVgap();
            if (y >= targetHeight + insets.top) {
                y = insets.top;
                newsColumn++;
            }
        }
        preferredY = highestY + insets.bottom;

        return true;
    }
}

