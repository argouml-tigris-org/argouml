// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

import java.awt.Dimension;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * A table of editable properties. Each row contains a single property,
 * where the first column is the property name and the second column is
 * the editable value. PropertyTable will attempt to use an editor that
 * is appropriate for the value type of each Property. This includes support
 * for using checkboxes for Booleans and Combo Boxes for arrays of choices.
 * 
 * @author Jeremy Jones
**/
public class PropertyTable extends JTable {

    private static final String DEFAULT_NAME_TITLE = "Property";
    private static final String DEFAULT_VALUE_TITLE = "Value";
    private static final String DETERMINE_HEIGHT_TEXT = "ABC";
    
    private Property[]          _properties;
    private String              _nameColumnTitle;
    private String              _valueColumnTitle;

    /**
     * Creates a new PropertyTable for the specified set of Properties.
     * 
     * @param properties set of properties to display in the table
    **/
    public PropertyTable(Property[] properties) {
        this(properties, DEFAULT_NAME_TITLE, DEFAULT_VALUE_TITLE);
    }

    /**
     * Creates a new PropertyTable for the specified set of Properties.
     * 
     * @param properties        set of properties to display in the table
     * @param nameColumnTitle title to be displayed in the first
     * column header
     * @param valueColumnTitle title to be displayed in the second
     * column header
    **/
    public PropertyTable(
            Property[] properties,
            String nameColumnTitle,
            String valueColumnTitle) {
        super();

        _properties = properties;
        _nameColumnTitle = nameColumnTitle;
        _valueColumnTitle = valueColumnTitle;
        setModel(new PropertyTableModel());

        setRowSelectionAllowed(false);
        setColumnSelectionAllowed(false);
    
        JComboBox heightBox = new JComboBox(new String[] {
	    DETERMINE_HEIGHT_TEXT 
	});
        setRowHeight(heightBox.getPreferredSize().height);
        
        int width = Math.min(getPreferredSize().width,
			     getPreferredScrollableViewportSize().width);
        int height = Math.min(getPreferredSize().height,
			      getPreferredScrollableViewportSize().height);
        setPreferredScrollableViewportSize(new Dimension(width, height));
    }

    /**
     * Overridden to return the appropriate cell editor for the property
     * at the specified row. Returns null if column is not the value column.
     * 
     * @param row       the row of the cell to edit
     * @param column    the column of the cell to edit
     * @return          the editor for this cell
    **/
    public TableCellEditor getCellEditor(int row, int column) {
        TableCellEditor editor = null;
        if (column == 1) {
            Object[] choices = _properties[row].getAvailableValues();
            if (choices != null) {
                JComboBox comboBox = new JComboBox(choices);
                comboBox.setEditable(false);
                editor = new DefaultCellEditor(comboBox);
            }
            else {
                editor = getDefaultEditor(_properties[row].getValueType());
            }
        }

        return editor;
    }

    /**
     * Overridden to return the appropriate cell renderer for the property
     * value if the cell is in the value column, otherwise returns the default 
     * renderer.
     * 
     * @param row       the row of the cell to render
     * @param column    the column of the cell to render
     * @return          the renderer for this cell
    **/
    public TableCellRenderer getCellRenderer(int row, int column) {
        TableCellRenderer renderer;
        if (column == 1) {
            renderer = getDefaultRenderer(_properties[row].getValueType());
        }
        else {
            renderer = getDefaultRenderer(Object.class);
        }

        return renderer;
    }

    /**
     * The TableModel for the PropertyTable. Wraps the array of Properties.
    **/
    private class PropertyTableModel extends AbstractTableModel {
        public PropertyTableModel() {
            super();
        }

        public int getColumnCount() {
            return 2;
        }

        public int getRowCount() {
            return _properties.length;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 1;
        }

        public String getColumnName(int column) {
            if (column == 0) {
                return _nameColumnTitle;
            }
            else if (column == 1) {
                return _valueColumnTitle;
            }
            else {
                return null;
            }
        }

        public Object getValueAt(int row, int col) {
            if (col == 0) {
                return _properties[row].getName();
            }
            else if (col == 1) {
                return _properties[row].getCurrentValue();
            }
            else {
                return null;
            }
        }

        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 1) {
                _properties[rowIndex].setCurrentValue(aValue);
            }
        }
    };
}
