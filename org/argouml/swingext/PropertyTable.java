package org.argouml.swingext;

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
     * @param nameColumnTitle   title to be displayed in the first column header
     * @param valueColumnTitle  title to be displayed in the second column header
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
    
        JComboBox heightBox = new JComboBox(new String[] { DETERMINE_HEIGHT_TEXT });
        setRowHeight(heightBox.getPreferredSize().height);
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
