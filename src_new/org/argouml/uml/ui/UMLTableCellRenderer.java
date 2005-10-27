package org.argouml.uml.ui;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.apache.log4j.Logger;
import org.argouml.model.Model;

public class UMLTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {

    private Logger LOG = Logger.getLogger(UMLTableCellRenderer.class);
    
    public UMLTableCellRenderer() {
        super();
    }

    public void setValue(Object value) {
        LOG.info("Set value to "+value);
        if (Model.getFacade().isAModelElement(value)) {
            String name = Model.getFacade().getName(value);
            LOG.info("Set text to "+name);
            setText(name);
        } else {
            if (value instanceof String)
                setText((String)value);
            else
                setText("");            
        }
    }
}
