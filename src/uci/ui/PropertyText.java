// From Sun's Beanbox
// Support for a PropertyEditor that uses text.

//package sun.beanbox;
package uci.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;

class PropertyText extends TextField implements KeyListener {

    PropertyText(PropertyEditor pe) {
	super(pe.getAsText());
	editor = pe;
	addKeyListener(this);
    }

    public void repaint() {
	setText(editor.getAsText());
    }

    //----------------------------------------------------------------------
    // Keyboard listener methods.

    public void keyReleased(KeyEvent e) {
        try {
	    editor.setAsText(getText());
	} catch (IllegalArgumentException ex) {
	    // Quietly ignore.
	}
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    //----------------------------------------------------------------------
    private PropertyEditor editor;
}
