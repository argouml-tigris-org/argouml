/*
 * Toolbar.java
 *
 * Created on 29 September 2002, 21:01
 */

package org.argouml.ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.Insets;

/**
 *
 * @author  administrator
 */
public class Toolbar extends JToolBar {
    
    /** Creates a new instance of Toolbar */
    public Toolbar(String title) {
        //setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        this.setFloatable(true);
        this.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        this.setMargin(new Insets(0,0,0,0));
    }
}
