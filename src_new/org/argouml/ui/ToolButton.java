/*
 * ToolButton.java
 *
 * Created on 29 September 2002, 21:02
 */

package org.argouml.ui;

import javax.swing.*;

/**
 *
 * @author  administrator
 */
public class ToolButton extends javax.swing.JButton {
    
    /** Creates a new instance of ToolButton */
    public ToolButton(Action a) {
        super(a);
        this.setText("");
        //this.setBorderPainted(false);
        this.setRolloverEnabled(true);
    }
}
