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

/*
 * GridLayout2.java
 */
package org.argouml.swingext;

import java.awt.*;

/**
 * Same as the standard java class GridLayout but allows more
 * flexability for sizing of columns and rows.
 *
 * @author Bob Tarling
 */
public class GridLayout2 implements LayoutManager, java.io.Serializable {

     /**
       * Do not resize the child components.
       */
    public static final int NONE = 0;

     /**
       * Resize all child components to fit their cell both
       * horizontally and vertically.
       */
    public static final int BOTH = 1;

     /**
       * Resize all child components to fit their cell horizontally
       * but not vertically.
       */
    public static final int HORIZONTAL = 2;

     /**
       * Resize all child components to fit their cell vertically but
       * not horizontally.
       */
    public static final int VERTICAL = 3;

     /**
      * Put the component in the center of its display area.
      */
    public static final int CENTER = 10;

     /**
       * Put the component at the top of its display area,
       * centered horizontally.
       */
    public static final int NORTH = 11;

      /**
       * Put the component at the top-right corner of its display area.
       */
    public static final int NORTHEAST = 12;

      /**
       * Put the component on the right side of its display area,
       * centered vertically.
       */
    public static final int EAST = 13;

      /**
       * Put the component at the bottom-right corner of its display area.
       */
    public static final int SOUTHEAST = 14;

      /**
       * Put the component at the bottom of its display area, centered
       * horizontally.
       */
    public static final int SOUTH = 15;

     /**
       * Put the component at the bottom-left corner of its display area.
       */
    public static final int SOUTHWEST = 16;

      /**
       * Put the component on the left side of its display area,
       * centered vertically.
       */
    public static final int WEST = 17;

     /**
       * Put each component in the top-left corner of its display area.
       */
    public static final int NORTHWEST = 18;

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
     * This is the number of rows specified for the grid.  The number
     * of rows can be changed at any time.
     * This should be a non negative integer, where '0' means
     * 'any number' meaning that the number of Rows in that
     * dimension depends on the other dimension.
     *
     * @serial
     * @see #getRows()
     * @see #setRows(int)
     */
    private int rows;
    /**
     * This is the number of columns specified for the grid.  The number
     * of columns can be changed at any time.
     * This should be a non negative integer, where '0' means
     * 'any number' meaning that the number of Columns in that
     * dimension depends on the other dimension.
     *
     * @serial
     * @see #getColumns()
     * @see #setCols(int)
     */
    private int cols;

    /**
     * Size all cells as the largest prefered width and height
     * component. <p>
     *
     * A possible value for the cellSizing parameter of the
     * constructor in order to size cells so that each have the same
     * width and height. The width is the largest prefered width and
     * the height is the largest prefered height of all these child
     * components.
     */
    public static final int MAXPREFERRED = 20;

    /**
     * Size all cells so that all in the same row are the same height
     * and all in the same column are the same width.<p>
     *
     * A possible value for the cellSizing parameter of the
     * constructor. The width is the largest prefered width of all
     * components in the same column and the height is the largest
     * prefered height of all components in the same row.
     */
    public static final int ROWCOLPREFERRED = 21;

    /**
     * Size all cells as the same width and height to fit the parent
     * component. <p>
     *
     * A possible value for the cellSizing parameter of the
     * constructor in order to size cells so that each has the same
     * height and width and are sized to fit their parent.  This
     * emulates the sizing done by a standard GridLayout.
     */
    public static final int FITPARENT = 22;

    private int cellSizing = FITPARENT;

    private int fill = BOTH;
    private int anchor = WEST;

    /**
     * The height of the child component with the largest height
     */
    private int largestHeight; 
    /**
     * The width of the child component with the largest width
     */
    private int largestWidth;
    /**
     * The required cell width of each column
     */
    private int[] colWidth;
    /**
     * The required cell height of each row
     */
    private int[] rowHeight;

    /**
     * Construct a new GridLayout2 with a default of one column per
     * component, in a single row.
     */
    public GridLayout2() {
	this(1, 0, 0, 0);
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows
     * and columns.
     *
     * @param r the number of rows in the layout
     * @param c the number of columns in the layout
     */
    public GridLayout2(int r, int c) {
	this(r, c, 0, 0);
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows
     * and columns and cell spacing.
     *
     * @param r the number of rows in the layout
     * @param c the number of columns in the layout
     * @param h the horizontal gap between cells
     * @param v the vertical gap between cells
     */
    public GridLayout2(int r, int c, int h, int v) {
	if ((r == 0) && (c == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot "
					       + "both be zero");
	}
	this.rows = r;
	this.cols = c;
	this.hgap = h;
	this.vgap = v;
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows
     * and columns and cell sizing scheme.
     *
     * @param r the number of rows in the layout
     * @param c the number of columns in the layout
     * @param cs the required cell sizing scheme
     */
    public GridLayout2(int r, int c, int cs) {
	this(r, c, 0, 0, cs);
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows
     * and columns, cell spacing and cell sizing scheme.
     *
     * @param r the number of rows in the layout
     * @param c the number of columns in the layout
     * @param h the horizontal gap between cells
     * @param v the vertical gap between cells
     * @param cs the required cell sizing scheme
     */
    public GridLayout2(int r, int c, int h, int v, int cs) {
        this(r, c, h, v);
        this.cellSizing = cs;
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows
     * and columns, cell spacing, cell sizing scheme and filling
     * scheme.
     *
     * @param r the number of rows in the layout
     * @param c the number of columns in the layout
     * @param h the horizontal gap between cells
     * @param v the vertical gap between cells
     * @param cs the required cell sizing scheme
     * @param f the required cell filling scheme
     */
    public GridLayout2(int r, int c, int h, int v,
		       int cs, int f)
    {
        this(r, c, h, v, cs);
        this.fill = f;
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows
     * and columns, cell spacing, cell sizing scheme and component
     * sizing and anchoring scheme.
     *
     * @param r the number of rows in the layout
     * @param c the number of columns in the layout
     * @param h the horizontal gap between cells
     * @param v the vertical gap between cells
     * @param cs the required cell sizing scheme
     * @param f the required cell filling scheme
     * @param a the required anchoring of a child component within its cell
     */
    public GridLayout2(int r, int c, int h, int v,
		       int cs, int f, int a) {
        this(r, c, h, v, cs, f);
        this.anchor = a;
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
     * Removes the specified component with the specified name from
     * the layout. This is included to satisfy the LayoutManager
     * interface but is not actually used in this layout
     * implementation.<p>
     *
     * @param comp the name of the component
     */    
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Determines the preferred size of the container argument using
     * this grid layout.  The preferred size of a grid layout is
     * dependant on the cellSizing scheme.<p>
     *
     * MAXPREFERRED and FITPARENT use the same formula to calculate
     * prefered size.<p>
     *
     * The prefered width using MAXPREFERRED or FITPARENT is the
     * largest preferred width of any of the widths in the container
     * times the number of columns, plus the horizontal padding times
     * the number of columns plus one, plus the left and right insets
     * of the target container.<p>
     *
     * The preferred height using MAXPREFERRED or FITPARENT is the
     * largest preferred height of any of the heights in the container
     * times the number of rows, plus the vertical padding times the
     * number of rows plus one, plus the top and bottom insets of the
     * target container.<p>
     *
     * The prefered width using ROWCOLPREFERRED is the largest
     * preferred is the sum of the widths of of all columns, plus the
     * horizontal padding times the number of columns plus one, plus
     * the left and right insets of the target container.<p>
     *
     * The prefered height using ROWCOLPREFERRED is the largest
     * preferred is the sum of the heights of of all columns, plus the
     * horizontal padding times the number of columns plus one, plus
     * the left and right insets of the target container.
     *
     * @param parent the container to be laid out 
     * @return the preferred dimensions to lay out the subcomponents
     * of the specified container
     */
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            int componentCount = parent.getComponentCount();
            int actualRows = rows;
            int actualCols = cols;

            if (actualRows > 0) {
                actualCols = (componentCount + actualRows - 1) / actualRows;
            } else {
                actualRows = (componentCount + actualCols - 1) / actualCols;
            }

            colWidth = new int[actualCols];
            rowHeight = new int[actualRows];
            int largestPreferredWidth = 0;
            int largestPreferredHeight = 0;

            for (int c = 0; c < actualCols; ++c) {
                for (int r = 0; r < actualRows; ++r) {
                    int i = r * actualCols + c;
                    if (i < componentCount) {
                        if (parent.getComponent(i).getPreferredSize().getWidth()
			    > colWidth[c]) {
                            colWidth[c] =
				(int) parent.getComponent(i)
				    .getPreferredSize().getWidth();
                            if (colWidth[c] > largestPreferredWidth)
				largestPreferredWidth = colWidth[c];
                        }
                        if (parent.getComponent(i)
			    .getPreferredSize().getHeight()
			    > rowHeight[r])
			{
                            rowHeight[r] =
				(int) parent.getComponent(i)
				    .getPreferredSize().getHeight();
                            if (rowHeight[r] > largestPreferredHeight)
				largestPreferredHeight = rowHeight[r];
                        }
                    }
                }
            }

            return calculateSizes(parent, colWidth, rowHeight,
				  actualRows, actualCols,
				  largestPreferredWidth,
				  largestPreferredHeight);
        }
    }

    /** 
     * Required by LayoutManager.
     * 
     * @see java.awt.LayoutManager#minimumLayoutSize(java.awt.Container)
     */
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            int componentCount = parent.getComponentCount();
            int actualRows = rows;
            int actualCols = cols;

            if (actualRows > 0) {
                actualCols = (componentCount + actualRows - 1) / actualRows;
            } else {
                actualRows = (componentCount + actualCols - 1) / actualCols;
            }

            colWidth = new int[actualCols];
            rowHeight = new int[actualRows];
            int largestMinimumWidth = 0;
            int largestMinimumHeight = 0;

            for (int c = 0; c < actualCols; ++c) {
                for (int r = 0; r < actualRows; ++r) {
                    int i = r * actualCols + c;
                    if (i < componentCount) {
                        if (parent.getComponent(i).getMinimumSize().getWidth()
			    > colWidth[c]) 
			{
                            colWidth[c] =
				(int) parent.getComponent(i)
				    .getMinimumSize().getWidth();
                            if (colWidth[c] > largestMinimumWidth)
				largestMinimumWidth = colWidth[c];
                        }
                        if (parent.getComponent(i).getMinimumSize().getHeight()
			    > rowHeight[r]) {
                            rowHeight[r] =
				(int) parent.getComponent(i)
				    .getMinimumSize().getHeight();
                            if (rowHeight[r] > largestMinimumHeight)
				largestMinimumHeight = rowHeight[r];
                        }
                    }
                }
            }

            return calculateSizes(parent, colWidth, rowHeight,
				  actualRows, actualCols,
				  largestMinimumWidth, largestMinimumHeight);
        }
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
            int nrows = this.rows;
            int ncols = this.cols;

            if (nrows > 0) {
                ncols = (ncomponents + nrows - 1) / nrows;
            } else {
                nrows = (ncomponents + ncols - 1) / ncols;
            }

            colWidth = new int[ncols];
            rowHeight = new int[nrows];
            largestWidth = 0;
            largestHeight = 0;

            if (cellSizing == FITPARENT) {
                int availableWidth =
		    parent.getWidth() 
		    - (insets.left + insets.right + (ncols - 1) * hgap);
                int availableHeight =
		    parent.getHeight()
		    - (insets.top + insets.bottom + (nrows - 1) * vgap);
                largestWidth = availableWidth / ncols;
                largestHeight = availableHeight / nrows;
            }
            else {
                for (int c = 0; c < ncols; ++c) {
                    for (int r = 0; r < nrows; ++r) {
                        int i = r * ncols + c;
                        if (i < ncomponents) {
                            if (parent.getComponent(i)
				.getPreferredSize().getWidth()
				> colWidth[c])
			    {
                                colWidth[c] =
				    (int) parent.getComponent(i)
				        .getPreferredSize().getWidth();
                                if (colWidth[c] > largestWidth)
				    largestWidth = colWidth[c];
                            }
                            if (parent.getComponent(i)
				.getPreferredSize().getHeight()
				> rowHeight[r])
			    {
                                rowHeight[r] =
				    (int) parent.getComponent(i)
				        .getPreferredSize().getHeight();
                                if (rowHeight[r] > largestHeight)
				    largestHeight = rowHeight[r];
                            }
                        }
                    }
                }
            }

            int cellWidth;
            int cellHeight;
            for (int c = 0, x = insets.left;
		 c < ncols;
		 x += cellWidth + hgap, ++c)
	    {
                cellWidth = getComponentCellWidth(c);
                for (int r = 0, y = insets.top;
		     r < nrows;
		     y += cellHeight + vgap, ++r)
		{
                    cellHeight = getComponentCellHeight(r);

                    int i = r * ncols + c;

                    if (i < ncomponents) {
                        positionComponentInCell(parent.getComponent(i), x, y,
						cellWidth, cellHeight);
                    }
                }
            }
        }
    }


    protected Dimension calculateSizes(Container parent, 
				       int theColWidth[], int theRowHeight[],
				       int actualRows, int actualCols,
				       int theLargestWidth, 
                                       int theLargestHeight)
    {
        int w = 0;
        int h = 0;
        if (cellSizing == GridLayout2.ROWCOLPREFERRED) {
            for (int c = 0; c < actualCols; ++c) w += theColWidth[c];
            for (int r = 0; r < actualRows; ++r) h += theRowHeight[r];
        }
        else {
            w = theLargestWidth * actualCols;
            h = theLargestHeight * actualRows;
        }

        Insets insets = parent.getInsets();
        return new Dimension(insets.left + insets.right + w 
			     + (actualCols - 1) * hgap,
                             insets.top + insets.bottom + h
			     + (actualRows - 1) * vgap);
    }

    /**
     * Calculate the cell height.
     * 
     * @param row the row for this cell
     * @return the height
     */
    protected int getComponentCellHeight(int row) {
        if (cellSizing == ROWCOLPREFERRED) return rowHeight[row];
        return largestHeight;
    }

    /**
     * Calculate the cell width.
     * 
     * @param col the column of this cell
     * @return the width
     */
    protected int getComponentCellWidth(int col) {
        if (cellSizing == ROWCOLPREFERRED) return colWidth[col];
        return largestWidth;
    }

    protected void positionComponentInCell(Component comp,
					   int x, int y,
					   int cellWidth, int cellHeight)
    {
        int componentWidth;
        int componentHeight;
        if (fill == VERTICAL || fill == NONE) {
            componentWidth = (int) comp.getPreferredSize().getWidth();
        }
        else {
            componentWidth = cellWidth;
        }

        if (fill == HORIZONTAL || fill == NONE) {
            componentHeight = (int) comp.getPreferredSize().getHeight();
        }
        else {
            componentHeight = cellHeight;
        }

        int xAnchor, yAnchor;
        if (fill == BOTH || fill == HORIZONTAL
	    || anchor == NORTHWEST || anchor == WEST || anchor == SOUTHWEST)
	{
            xAnchor = x;
        }
        else if (anchor == NORTHEAST || anchor == EAST || anchor == SOUTHEAST)
	{
            xAnchor = x + cellWidth - componentWidth;
        }
        else {
            xAnchor = x + (cellWidth - componentWidth) / 2;
        }

        if (fill == BOTH || fill == VERTICAL
	    || anchor == NORTH || anchor == NORTHWEST || anchor == NORTHEAST)
	{
            yAnchor = y;
        }
        else if (anchor == SOUTHEAST || anchor == SOUTH || anchor == SOUTHWEST)
	{
            yAnchor = y + cellHeight - componentHeight;
        }
        else {
            yAnchor = y + (cellHeight - componentHeight) / 2;
        }

        comp.setBounds(xAnchor, yAnchor, componentWidth, componentHeight);
    }

    /**
     * @return the number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @param r the number of rows. It can not be zero.
     */
    public void setRows(int r) {
	if ((r == 0) && (this.cols == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot "
					       + "both be zero");
	}
	this.rows = r;
    }

    /**
     * @return the number of columns
     */
    public int getColumns() {
        return cols;
    }

    /**
     * @param c the number of columns. It can not be zero.
     */
    public void setCols(int c) {
	if ((c == 0) && (this.rows == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot "
					       + "both be zero");
	}
	this.cols = c;
    }

    /**
     * @return the horizontal gap
     */
    public int getHgap() {
        return hgap;
    }

    /**
     * @param h the horizontal gap
     */
    public void setHgap(int h) {
        this.hgap = h;
    }

    /**
     * @return the vertical gap
     */
    public int getVgap() {
        return vgap;
    }

    /**
     * @param v the vertical gap
     */
    public void setVgap(int v) {
        this.vgap = v;
    }

    /**
     * @param lh The largestHeight to set.
     */
    protected void setLargestHeight(int lh) {
        this.largestHeight = lh;
    }

    /**
     * @return Returns the largestHeight.
     */
    protected int getLargestHeight() {
        return largestHeight;
    }

    /**
     * @param lw The largestWidth to set.
     */
    protected void setLargestWidth(int lw) {
        this.largestWidth = lw;
    }

    /**
     * @return Returns the largestWidth.
     */
    protected int getLargestWidth() {
        return largestWidth;
    }

    /**
     * @param cw The colWidth to set.
     */
    protected void setColWidth(int[] cw) {
        this.colWidth = cw;
    }

    /**
     * @return Returns the colWidth.
     */
    protected int[] getColWidth() {
        return colWidth;
    }

    /**
     * @param rh The rowHeight to set.
     */
    protected void setRowHeight(int[] rh) {
        this.rowHeight = rh;
    }

    /**
     * @return Returns the rowHeight.
     */
    protected int[] getRowHeight() {
        return rowHeight;
    }

    /**
     * @return Returns the cellSizing.
     */
    int getCellSizing() {
        return cellSizing;
    }
}
