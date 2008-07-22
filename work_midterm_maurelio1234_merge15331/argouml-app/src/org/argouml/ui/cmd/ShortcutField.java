// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.ui.cmd;

import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;

import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.EventListenerList;

import org.argouml.util.KeyEventUtils;

/**
 * A special JTextField that records key strokes
 *
 * @author nirux
 */
public class ShortcutField extends JTextField {

    /**
     * The UID
     */
    private static final long serialVersionUID = -62483698420802557L;

    private EventListenerList listenerList = new EventListenerList();

    /**
     * Main constructor for ShortcutField
     * 
     * @param text      the initial text of the field
     * @param columns   the number of columns
     */
    public ShortcutField(String text, int columns) {
        super(null, text, columns);
        
        // trap focus traversal keys also 
        this.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, 
                Collections.EMPTY_SET); 
        this.setFocusTraversalKeys(
                KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, 
                Collections.EMPTY_SET); 
 
        // let's add the key printing logic
        this.addKeyListener(new KeyListener() { 
            private int currentKeyCode = 0; 
            public void keyPressed(KeyEvent ke) { 
                ke.consume(); 
                JTextField tf = (JTextField) ke.getSource(); 
                tf.setText(toString(ke)); 
            } 
 
            public void keyReleased(KeyEvent ke) { 
                ke.consume(); 
                JTextField tf = (JTextField) ke.getSource(); 
                switch(currentKeyCode) { 
                case KeyEvent.VK_ALT: 
                case KeyEvent.VK_ALT_GRAPH: 
                case KeyEvent.VK_CONTROL: 
                case KeyEvent.VK_SHIFT: 
                    tf.setText(""); 
                    return; 
                } 
            } 
 
            public void keyTyped(KeyEvent ke) { 
                ke.consume(); 
            } 
            
            private String toString(KeyEvent ke) { 
                currentKeyCode = ke.getKeyCode(); 
                int keyCode = currentKeyCode; 
                String modifText = 
                    KeyEventUtils.getModifiersText(ke.getModifiers());
                
                if ("".equals(modifText)) {
                    // no modifiers - let's check if the key is valid
                    if (KeyEventUtils.isActionEvent(ke)) {
                        return KeyEventUtils.getKeyText(keyCode);
                    } else {
                        return "";
                    }
                } else {
                    switch(keyCode) { 
                    case KeyEvent.VK_ALT: 
                    case KeyEvent.VK_ALT_GRAPH: 
                    case KeyEvent.VK_CONTROL: 
                    case KeyEvent.VK_SHIFT: 
                        return modifText; // middle of shortcut 
                    default: 
                        modifText += KeyEventUtils.getKeyText(ke.getKeyCode());
                        fireShortcutChangedEvent(modifText);
                        return modifText;
                    }
                } 
            } 
        });
    }

    /**
     * Adds a ShortcutChangedListener to the listener list
     *  
     * @param listener          the ShortcutChangedListener to be added
     */
    public void addShortcutChangedListener(ShortcutChangedListener listener) {
        listenerList.add(ShortcutChangedListener.class, listener);
    }

    /**
     * Inform listeners of any shortcut notifications.
     * 
     * @param text      the text of the field
     */
    protected void fireShortcutChangedEvent(String text) {
        ShortcutChangedEvent event = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        KeyStroke keyStroke = ShortcutMgr.decodeKeyStroke(text);

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ShortcutChangedListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event = new ShortcutChangedEvent(this, keyStroke);
                }
                ((ShortcutChangedListener) listeners[i + 1])
                        .shortcutChange(event);
            }
        }
    }
}
