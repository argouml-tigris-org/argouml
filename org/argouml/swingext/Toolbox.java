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

    private int _rows;
    private int _cols;

    /** Creates a new instance of ToolBox */
    public Toolbox(int rows, int cols) {
        super();
        _rows = rows;
        _cols = cols;
        setLayout(new GridLayout(_rows,_cols));
    }

}
