// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
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

package org.argouml.swingext;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.EventListenerList;

import org.argouml.ui.cmd.ShortcutChangedEvent;
import org.argouml.ui.cmd.ShortcutChangedListener;
import org.argouml.ui.cmd.ShortcutMgr;
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

    private boolean shortcutCreated = false;

    private String lastValidKey = "";

    private int modifiers;

    private int pressedKeys;

    /**
     * Instantiate a ShortcutField object
     * 
     * @param text      the initial text of the field
     * @param columns   the number of columns
     */
    public ShortcutField(String text, int columns) {
        super(text, columns);
    }

    /**
     * Makes the tab key work.
     * 
     * @return  always false
     */
    public boolean getFocusTraversalKeysEnabled() {
        return false;
    }

    /**
     * Reset the lastValidKey and modifiers fields
     *
     */
    public void resetLastValidKey() {
        this.lastValidKey = "";
        modifiers = 0;
    }

    private void displayText() {
        int modifiersCopy = modifiers;
        StringBuffer textBuffer = new StringBuffer();
        if (modifiersCopy >= InputEvent.ALT_GRAPH_DOWN_MASK) {
            modifiersCopy &= ~InputEvent.ALT_GRAPH_DOWN_MASK;
            textBuffer.append(ShortcutMgr.ALT_GRAPH_MODIFIER);
            textBuffer.append(ShortcutMgr.MODIFIER_JOINER);
        }
        if (modifiersCopy >= InputEvent.ALT_DOWN_MASK) {
            modifiersCopy &= ~InputEvent.ALT_DOWN_MASK;
            textBuffer.append(ShortcutMgr.ALT_MODIFIER);
            textBuffer.append(ShortcutMgr.MODIFIER_JOINER);
        }
        if (modifiersCopy >= InputEvent.CTRL_DOWN_MASK) {
            modifiersCopy &= ~InputEvent.CTRL_DOWN_MASK;
            textBuffer.append(ShortcutMgr.CTRL_MODIFIER);
            textBuffer.append(ShortcutMgr.MODIFIER_JOINER);
        }
        if (modifiersCopy >= InputEvent.SHIFT_DOWN_MASK) {
            modifiersCopy &= ~InputEvent.SHIFT_DOWN_MASK;
            textBuffer.append(ShortcutMgr.SHIFT_MODIFIER);
            textBuffer.append(ShortcutMgr.MODIFIER_JOINER);
        }
        if (modifiersCopy >= InputEvent.META_DOWN_MASK) {
            modifiersCopy &= ~InputEvent.META_DOWN_MASK;
            textBuffer.append(ShortcutMgr.META_MODIFIER);
            textBuffer.append(ShortcutMgr.MODIFIER_JOINER);
        }
        textBuffer.append(lastValidKey != null ? lastValidKey : "");
        this.setText(textBuffer.toString());
    }

    /**lastValidKey
     * Overrides processKeyEvent
     * 
     * @see javax.swing.JComponent#processKeyEvent(java.awt.event.KeyEvent)
     */
    protected void processKeyEvent(KeyEvent evt) {
        int keyCode = evt.getKeyCode();

        if (pressedKeys == 0) {
            lastValidKey = "";
            modifiers = 0;
            shortcutCreated = false;
        }

        switch (evt.getID()) {
        case KeyEvent.KEY_PRESSED:
            // get rid of keys we never need to handle
            switch (keyCode) {
            // case '\0':
            // return;
            case KeyEvent.VK_ALT:
                if ("".equals(lastValidKey)) {
                    shortcutCreated = false;
                }
                modifiers |= InputEvent.ALT_DOWN_MASK;
                break;
            case KeyEvent.VK_ALT_GRAPH:
                if ("".equals(lastValidKey)) {
                    shortcutCreated = false;
                }
                modifiers |= InputEvent.ALT_GRAPH_DOWN_MASK;
                break;
            case KeyEvent.VK_CONTROL:
                if ("".equals(lastValidKey)) {
                    shortcutCreated = false;
                }
                modifiers |= InputEvent.CTRL_DOWN_MASK;
                break;
            case KeyEvent.VK_SHIFT:
                if ("".equals(lastValidKey)) {
                    shortcutCreated = false;
                }
                modifiers |= InputEvent.SHIFT_DOWN_MASK;
                break;
            case KeyEvent.VK_META:
                if ("".equals(lastValidKey)) {
                    shortcutCreated = false;
                }
                modifiers |= InputEvent.META_DOWN_MASK;
                break;
            default:
                // if the event is an action or a modifier was pressed
                // then we can assign the shortcut
                if (KeyEventUtils.isActionEvent(evt) || modifiers != 0) {
                    lastValidKey = KeyEventUtils.getKeyText(evt.getKeyCode());
                    shortcutCreated = true;
                    displayText();
                    fireShortcutChangedEvent();
                }
                break;
            }
            pressedKeys++;
            break;
        case KeyEvent.KEY_RELEASED:
            // get rid of keys we never need to handle
            switch (keyCode) {
            // case '\0':
            // return;
            case KeyEvent.VK_ALT:
                if (!shortcutCreated) modifiers &= ~InputEvent.ALT_DOWN_MASK;
                break;
            case KeyEvent.VK_ALT_GRAPH:
                if (!shortcutCreated) {
                    modifiers &= ~InputEvent.ALT_GRAPH_DOWN_MASK;
                }
                break;
            case KeyEvent.VK_CONTROL:
                if (!shortcutCreated) { 
                    modifiers &= ~InputEvent.CTRL_DOWN_MASK;
                }
                break;
            case KeyEvent.VK_SHIFT:
                if (!shortcutCreated) { 
                    modifiers &= ~InputEvent.SHIFT_DOWN_MASK;
                }
                break;
            case KeyEvent.VK_META:
                if (!shortcutCreated) { 
                    modifiers &= ~InputEvent.META_DOWN_MASK;
                }
                break;
            default:
                break;
            }
            pressedKeys--;
            break;
        default:
            break;
        }
        displayText();
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
     */
    protected void fireShortcutChangedEvent() {
        ShortcutChangedEvent event = null;
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        KeyStroke keyStroke = ShortcutMgr.decodeKeyStroke(this.getText());

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
