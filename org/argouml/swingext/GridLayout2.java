package org.argouml.swingext;

import java.awt.*;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author Bob Tarling
 * @version 1.0
 */

public class GridLayout2 implements LayoutManager, java.io.Serializable {

     /**
       * Do not resize the component.
       */
    public static final int NONE = 0;

     /**
       * Resize the component both horizontally and vertically.
       */
    public static final int BOTH = 1;

     /**
       * Resize the component horizontally but not vertically.
       */
    public static final int HORIZONTAL = 2;

     /**
       * Resize the component vertically but not horizontally.
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
     * @see getHgap()
     * @see setHgap()
     */
    private int hgap;
    /**
     * This is the vertical gap (in pixels) which specifies the space
     * between rows.  They can be changed at any time.
     * This should be a non negative integer.
     *
     * @serial
     * @see getVgap()
     * @see setVgap()
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
     * @see getRows()
     * @see setRows()
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
     * @see getColumns()
     * @see setColumns()
     */
    private int cols;

    public static final int MAXPREFERRED = 20;
    public static final int ROWCOLPREFERRED = 21;
    public static final int FITPARENT = 22;

    int cellSizing = FITPARENT;

    private int fill = BOTH;
    private int anchor = WEST;

    protected int largestHeight;
    protected int largestWidth;
    protected int[] colWidth;
    protected int[] rowHeight;

    public GridLayout2() {
	this(1, 0, 0, 0);
    }

    public GridLayout2(int rows, int cols) {
	this(rows, cols, 0, 0);
    }

    public GridLayout2(int rows, int cols, int hgap, int vgap) {
	if ((rows == 0) && (cols == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot both be zero");
	}
	this.rows = rows;
	this.cols = cols;
	this.hgap = hgap;
	this.vgap = vgap;
    }

    public GridLayout2(int cellSizing) {
	this(1, 0, 0, 0, cellSizing);
    }

    public GridLayout2(int rows, int cols, int cellSizing) {
	this(rows, cols, 0, 0, cellSizing);
    }

    public GridLayout2(int rows, int cols, int hgap, int vgap, int cellSizing) {
        this(rows, cols, hgap, vgap);
        this.cellSizing = cellSizing;
    }

    public GridLayout2(int rows, int cols, int hgap, int vgap, int cellSizing, int fill) {
        this(rows, cols, hgap, vgap, cellSizing);
        this.fill = fill;
    }

    public GridLayout2(int rows, int cols, int hgap, int vgap, int cellSizing, int fill, int anchor) {
        this(rows, cols, hgap, vgap, cellSizing, fill);
        this.anchor = anchor;
    }


    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }


    /* Required by LayoutManager. */
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

            for (int c = 0; c < actualCols ; ++c) {
                for (int r = 0; r < actualRows; ++r) {
                    int i = r * actualCols + c;
                    if (parent.getComponent(i).getPreferredSize().getWidth() > colWidth[c]) {
                        colWidth[c] = (int) parent.getComponent(i).getPreferredSize().getWidth();
                        if (colWidth[c] > largestPreferredWidth) largestPreferredWidth = colWidth[c];
                    }
                    if (parent.getComponent(i).getPreferredSize().getHeight() > rowHeight[r]) {
                        rowHeight[r] = (int) parent.getComponent(i).getPreferredSize().getHeight();
                        if (rowHeight[r] > largestPreferredHeight) largestPreferredHeight = rowHeight[r];
                    }
                }
            }

            return calculateSizes(parent, colWidth, rowHeight, actualRows, actualCols, largestPreferredWidth, largestPreferredHeight);
        }
    }

    /* Required by LayoutManager. */
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

            for (int c = 0; c < actualCols ; ++c) {
                for (int r = 0; r < actualRows; ++r) {
                    int i = r * actualCols + c;
                    if (parent.getComponent(i).getMinimumSize().getWidth() > colWidth[c]) {
                        colWidth[c] = (int) parent.getComponent(i).getMinimumSize().getWidth();
                        if (colWidth[c] > largestMinimumWidth) largestMinimumWidth = colWidth[c];
                    }
                    if (parent.getComponent(i).getMinimumSize().getHeight() > rowHeight[r]) {
                        rowHeight[r] = (int) parent.getComponent(i).getMinimumSize().getHeight();
                        if (rowHeight[r] > largestMinimumHeight) largestMinimumHeight = rowHeight[r];
                    }
                }
            }

            return calculateSizes(parent, colWidth, rowHeight, actualRows, actualCols, largestMinimumWidth, largestMinimumHeight);
        }
    }

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
                int availableWidth = parent.getWidth() - (insets.left + insets.right + (ncols-1)*hgap);
                int availableHeight = parent.getHeight() - (insets.top + insets.bottom + (nrows-1)*vgap);
                largestWidth = availableWidth / ncols;
                largestHeight = availableHeight / nrows;
            }
            else {
                for (int c = 0; c < ncols ; ++c) {
                    for (int r = 0; r < nrows; ++r) {
                        int i = r * ncols + c;
                        if (parent.getComponent(i).getPreferredSize().getWidth() > colWidth[c]) {
                            colWidth[c] = (int) parent.getComponent(i).getPreferredSize().getWidth();
                            if (colWidth[c] > largestWidth) largestWidth = colWidth[c];
                        }
                        if (parent.getComponent(i).getPreferredSize().getHeight() > rowHeight[r]) {
                            rowHeight[r] = (int) parent.getComponent(i).getPreferredSize().getHeight();
                            if (rowHeight[r] > largestHeight) largestHeight = rowHeight[r];
                        }
                    }
                }
            }

            int cellWidth;
            int cellHeight;
            for (int c = 0, x = insets.left ; c < ncols ; x += cellWidth + hgap, ++c) {
                cellWidth = getComponentCellWidth(c);
                for (int r = 0, y = insets.top ; r < nrows ; y += cellHeight + vgap, ++r) {
                    cellHeight = getComponentCellHeight(r);

                    int i = r * ncols + c;

                    if (i < ncomponents) {
                        positionComponentInCell(parent.getComponent(i), x, y, cellWidth, cellHeight);
                    }
                }
            }
        }
    }


    protected Dimension calculateSizes(Container parent, int colWidth[], int rowHeight[], int actualRows, int actualCols, int largestWidth, int largestHeight) {
        int w = 0;
        int h = 0;
        if (cellSizing == this.ROWCOLPREFERRED) {
            for (int c = 0; c < actualCols ; ++c) w += colWidth[c];
            for (int r = 0; r < actualRows ; ++r) h += rowHeight[r];
        }
        else {
            w = largestWidth * actualCols;
            h = largestHeight * actualRows;
        }

        Insets insets = parent.getInsets();
        return new Dimension(insets.left + insets.right + w + (actualCols-1) * hgap,
                             insets.top + insets.bottom + h + (actualRows-1) * vgap);
    }

    protected int getComponentCellHeight(int row) {
        if (cellSizing == ROWCOLPREFERRED) return rowHeight[row];
        return largestHeight;
    }

    protected int getComponentCellWidth(int col) {
        if (cellSizing == ROWCOLPREFERRED) return colWidth[col];
        return largestWidth;
    }

    protected void positionComponentInCell(Component comp, int x, int y, int cellWidth, int cellHeight) {
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
        if (fill == BOTH || fill == HORIZONTAL || anchor == NORTHWEST || anchor == WEST || anchor == SOUTHWEST) {
            xAnchor = x;
        }
        else if (anchor == NORTHEAST || anchor == EAST || anchor == SOUTHEAST) {
            xAnchor = x + cellWidth - componentWidth;
        }
        else {
            xAnchor = x + (cellWidth - componentWidth)/2;
        }

        if (fill == BOTH || fill == VERTICAL || anchor == NORTH || anchor == NORTHWEST || anchor == NORTHEAST) {
            yAnchor = y;
        }
        else if (anchor == SOUTHEAST || anchor == SOUTH || anchor == SOUTHWEST) {
            yAnchor = y + cellHeight - componentHeight;
        }
        else {
            yAnchor = y + (cellHeight - componentHeight)/2;
        }

        comp.setBounds(xAnchor, yAnchor, componentWidth, componentHeight);
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
	if ((rows == 0) && (this.cols == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot both be zero");
	}
	this.rows = rows;
    }

    public int getColumns() {
        return cols;
    }

    public void setCols(int cols) {
	if ((cols == 0) && (this.rows == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot both be zero");
	}
	this.cols = cols;
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


}
