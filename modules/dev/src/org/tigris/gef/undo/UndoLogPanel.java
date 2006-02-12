package org.tigris.gef.undo;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.tigris.gef.undo.Memento;

public class UndoLogPanel extends JScrollPane {

    private static final long serialVersionUID = -3483889053389473380L;
    
    private JPanel list;
    
    private static UndoLogPanel INSTANCE = new UndoLogPanel();
    
    public static UndoLogPanel getInstance() {
        return INSTANCE;
    }

    private UndoLogPanel() {
        list = new JPanel(new GridLayout(0, 1));
        JPanel listContainer = new JPanel(new BorderLayout());
        listContainer.add(BorderLayout.NORTH, list);
        this.setViewportView(listContainer);
    }
    
    public void addMemento(Memento memento) {
        list.add(new JLabel(memento.toString()));
        doLayout();
        validate();
        if (getVerticalScrollBar() != null) {
            int maxScroll = getVerticalScrollBar().getMaximum();
            getVerticalScrollBar().setValue(maxScroll + 1);
        }
    }

    public void removeMemento(Memento memento) {
        list.remove(list.getComponentCount()-1);
        doLayout();
        validate();
    }
}
