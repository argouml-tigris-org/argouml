/*
 * GridLayout2.java
 */
package org.argouml.swingext;

import java.awt.*;

/**
 * Same as the standard java class GridLayout but allows more flexability for sizing of columns
 * and rows.
 *
 * @author Bob Tarling
 */
public class GridLayout2 implements LayoutManager, java.io.Serializable {

     /**
       * Do not resize the child components.
       */
    public static final int NONE = 0;

     /**
       * Resize all child components to fit their cell both horizontally and vertically.
       */
    public static final int BOTH = 1;

     /**
       * Resize all child components to fit their cell horizontally but not vertically.
       */
    public static final int HORIZONTAL = 2;

     /**
       * Resize all child components to fit their cell vertically but not horizontally.
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
     * Size all cells as the largest prefered width and height component. <br />
     * A possible value for the cellSizing parameter of the constructor in order to size
     * cells so that each have the same width and height. The width is the largest
     * prefered width and the height is the largest prefered height of all these child components.
     */
    public static final int MAXPREFERRED = 20;
    /**
     * Size all cells so that all in the same row are the same height and all in the 
     * same column are the same width. <br />
     * A possible value for the cellSizing parameter of the constructor. The width is the largest
     * prefered width of all components in the same column and the height is the largest prefered
     * height of all components in the same row.
     */
    public static final int ROWCOLPREFERRED = 21;
    /**
     * Size all cells as the same width and height to fit the parent component. <br />
     * A possible value for the cellSizing parameter of the constructor in order to size
     * cells so that each has the same height and width and are sized to fit their parent.
     * This emulates the sizing done by a standard GridLayout.
     */
    public static final int FITPARENT = 22;

    int cellSizing = FITPARENT;

    private int fill = BOTH;
    private int anchor = WEST;

    /**
     * The height of the child component with the largest height
     */
    protected int largestHeight;
    /**
     * The width of the child component with the largest width
     */
    protected int largestWidth;
    /**
     * The required cell width of each column
     */
    protected int[] colWidth;
    /**
     * The required cell height of each row
     */
    protected int[] rowHeight;

    /**
     * Construct a new GridLayout2 with a default of one column per component, in a single row.
     */
    public GridLayout2() {
	this(1, 0, 0, 0);
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows and columns.
     *
     * @param rows the number of rows in the layout
     * @param cols the number of columns in the layout
     */
    public GridLayout2(int rows, int cols) {
	this(rows, cols, 0, 0);
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows and columns and cell
     * spacing.
     *
     * @param rows the number of rows in the layout
     * @param cols the number of columns in the layout
     * @param hgap the horizontal gap between cells
     * @param vgap the vertical gap between cells
     */
    public GridLayout2(int rows, int cols, int hgap, int vgap) {
	if ((rows == 0) && (cols == 0)) {
	    throw new IllegalArgumentException("rows and cols cannot both be zero");
	}
	this.rows = rows;
	this.cols = cols;
	this.hgap = hgap;
	this.vgap = vgap;
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows and columns and cell sizing
     * scheme.
     *
     * @param rows the number of rows in the layout
     * @param cols the number of columns in the layout
     * @param cellSizing the required cell sizing scheme
     */
    public GridLayout2(int rows, int cols, int cellSizing) {
	this(rows, cols, 0, 0, cellSizing);
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows and columns, cell
     * spacing and cell sizing scheme.
     *
     * @param rows the number of rows in the layout
     * @param cols the number of columns in the layout
     * @param hgap the horizontal gap between cells
     * @param vgap the vertical gap between cells
     * @param cellSizing the required cell sizing scheme
     */
    public GridLayout2(int rows, int cols, int hgap, int vgap, int cellSizing) {
        this(rows, cols, hgap, vgap);
        this.cellSizing = cellSizing;
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows and columns, cell
     * spacing, cell sizing scheme and filling scheme.
     *
     * @param rows the number of rows in the layout
     * @param cols the number of columns in the layout
     * @param hgap the horizontal gap between cells
     * @param vgap the vertical gap between cells
     * @param cellSizing the required cell sizing scheme
     * @param fill the required cell filling scheme
     */
    public GridLayout2(int rows, int cols, int hgap, int vgap, int cellSizing, int fill) {
        this(rows, cols, hgap, vgap, cellSizing);
        this.fill = fill;
    }

    /**
     * Construct a new GridLayout2 with the specified number of rows and columns, cell
     * spacing, cell sizing scheme and component sizing and anchoring scheme.
     *
     * @param rows the number of rows in the layout
     * @param cols the number of columns in the layout
     * @param hgap the horizontal gap between cells
     * @param vgap the vertical gap between cells
     * @param cellSizing the required cell sizing scheme
     * @param fill the required cell filling scheme
     * @param anchor the required anchoring of a child component within its cell
     */
    public GridLayout2(int rows, int cols, int hgap, int vgap, int cellSizing, int fill, int anchor) {
        this(rows, cols, hgap, vgap, cellSizing, fill);
        this.anchor = anchor;
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
     * Determines the preferred size of the container argument using this grid layout. 
     * The preferred size of a grid layout is dependant on the cellSizing scheme.<br />
     *<br />
     * MAXPREFERRED and FITPARENT use the same formula to calculate prefered size.<br />
     * The prefered width using MAXPREFERRED or FITPARENT is the largest preferred width of any 
     * of the widths in the container times the number of columns, plus the horizontal padding 
     * times the number of columns plus one, plus the left and right insets of the target
     * container. <br />
     *<br />
     * The preferred height using MAXPREFERRED or FITPARENT is the largest preferred height of
     * any of the heights in the container times the number of rows, plus the vertical padding 
     * times the number of rows plus one, plus the top and bottom insets of the target 
     * container.<br />
     *<br />
     * The prefered width using ROWCOLPREFERRED is the largest preferred is the sum of the widths 
     * of of all columns, plus the horizontal padding times the number of columns plus one, plus
     * the left and right insets of the target container. <br />
     *<br />
     * The prefered height using ROWCOLPREFERRED is the largest preferred is the sum of the
     * heights of of all columns, plus the horizontal padding times the number of columns plus 
     * one, plus the left and right insets of the target container.
     *
     * @param parent the container to be laid out 
     * @return the preferred dimensions to lay out the subcomponents of the specified container
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

            for (int c = 0; c < actualCols ; ++c) {
                for (int r = 0; r < actualRows; ++r) {
                    int i = r * actualCols + c;
                    if (i < componentCount) {
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
                    if (i < componentCount) {
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
                        if (i < ncomponents) {
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
