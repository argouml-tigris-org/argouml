package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;

public class ActionRemoveTaggedValue extends AbstractAction {

    private TabTaggedValues tab;

    /**
     * The constructor.
     */
    public ActionRemoveTaggedValue(TabTaggedValues tabtv) {
        super("", ResourceLoaderWrapper.lookupIconResource("Delete"));
        tab = tabtv;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        TabTaggedValuesModel model = tab.getTableModel();
        JTable table = tab.getTable();
        int row = table.getSelectedRow();
        List c = new ArrayList(
                Model.getFacade().getTaggedValuesCollection(tab.getTarget()));
        if ((row != -1) && (c.size() > row)) {
            c.remove(row);
            Model.getCoreHelper().setTaggedValues(tab.getTarget(), c);
            model.fireTableChanged(new TableModelEvent(model));
        }
    }
}