/*
 * Toolbox.java
 *
 * Created on 23 February 2003, 09:59
 */

package org.argouml.swingext;

import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.Action;
import javax.swing.JButton;

/**
 *
 * @author  Bob Tarling
 */
public class Toolbox extends Toolbar {

    int _rows;
    int _cols;
    ArrayList _actions = new ArrayList();
    MouseListener _mouseListener;

    /** Creates a new instance of ToolBox */
    public Toolbox(int rows, int cols) {
        super();
        _rows = rows;
        _cols = cols;
        setLayout(new GridLayout(_rows,_cols));
    }

    public JButton add(Action action) {
        _actions.add(action);
        return super.add(action);
    }

    public void setButtonMouseListener(MouseListener mouseListener) {
        _mouseListener = mouseListener;
    }

    /**
     * Occasionally the ToolBox gets in a state where a button
     * shows rollover status at the wrong time.
     * The only way to get around this is to rebuild the ToolBox.
     */
    public void rebuild() {
        super.removeAll();
        Iterator it = _actions.iterator();
        while(it.hasNext()) {
            final Action a = (Action)it.next();
            JButton button = super.add(a);
            if (_mouseListener != null) {
                button.addMouseListener(_mouseListener);
            }
        }
    }    
}
