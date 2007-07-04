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

package org.argouml.util;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.swing.KeyStroke;

/**
 * Utility class for KeyEvents
 * 
 * @author andrea.nironi@gmail.com
 */
public class KeyEventUtils {

    /** 
     * The expression between modifier/modifier and between modifier/text 
     */
    public static final String MODIFIER_JOINER = " + ";
    /** 
     * The text for the shift modifier 
     */
    public static final String SHIFT_MODIFIER = "SHIFT";
    /** 
     * The text for the ctrl modifier 
     */
    public static final String CTRL_MODIFIER = "CTRL";
    /** 
     * The text for the meta modifier 
     */
    public static final String META_MODIFIER = "META";
    /** 
     * The text for the alt modifier 
     */
    public static final String ALT_MODIFIER = "ALT";
    /** 
     * The text for the alt-gr modifier 
     */
    public static final String ALT_GRAPH_MODIFIER = "altGraph";


    /**
     * Returns whether the key in this event is an "action" key. This is a
     * customization of KeyEvent#isActionKey()
     * 
     * @param evt
     *            the event to be verified
     * @return true if the event is an
     * @see java.awt.event.KeyEvent#isActionKey()
     */
    public static final boolean isActionEvent(KeyEvent evt) {
        int keyCode = evt.getKeyCode();

        switch (keyCode) {

        // Argo customization
        case KeyEvent.VK_BACK_SPACE:
        case KeyEvent.VK_DELETE:
        case KeyEvent.VK_CANCEL:

            // KeyEvent.isActionKey() method
        case KeyEvent.VK_HOME:
        case KeyEvent.VK_END:
        case KeyEvent.VK_PAGE_UP:
        case KeyEvent.VK_PAGE_DOWN:
        case KeyEvent.VK_UP:
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:

        case KeyEvent.VK_KP_LEFT:
        case KeyEvent.VK_KP_UP:
        case KeyEvent.VK_KP_RIGHT:
        case KeyEvent.VK_KP_DOWN:

        case KeyEvent.VK_F1:
        case KeyEvent.VK_F2:
        case KeyEvent.VK_F3:
        case KeyEvent.VK_F4:
        case KeyEvent.VK_F5:
        case KeyEvent.VK_F6:
        case KeyEvent.VK_F7:
        case KeyEvent.VK_F8:
        case KeyEvent.VK_F9:
        case KeyEvent.VK_F10:
        case KeyEvent.VK_F11:
        case KeyEvent.VK_F12:
        case KeyEvent.VK_F13:
        case KeyEvent.VK_F14:
        case KeyEvent.VK_F15:
        case KeyEvent.VK_F16:
        case KeyEvent.VK_F17:
        case KeyEvent.VK_F18:
        case KeyEvent.VK_F19:
        case KeyEvent.VK_F20:
        case KeyEvent.VK_F21:
        case KeyEvent.VK_F22:
        case KeyEvent.VK_F23:
        case KeyEvent.VK_F24:
        case KeyEvent.VK_PRINTSCREEN:
        case KeyEvent.VK_SCROLL_LOCK:
        case KeyEvent.VK_CAPS_LOCK:
        case KeyEvent.VK_NUM_LOCK:
        case KeyEvent.VK_PAUSE:
        case KeyEvent.VK_INSERT:

        case KeyEvent.VK_FINAL:
        case KeyEvent.VK_CONVERT:
        case KeyEvent.VK_NONCONVERT:
        case KeyEvent.VK_ACCEPT:
        case KeyEvent.VK_MODECHANGE:
        case KeyEvent.VK_KANA:
        case KeyEvent.VK_KANJI:
        case KeyEvent.VK_ALPHANUMERIC:
        case KeyEvent.VK_KATAKANA:
        case KeyEvent.VK_HIRAGANA:
        case KeyEvent.VK_FULL_WIDTH:
        case KeyEvent.VK_HALF_WIDTH:
        case KeyEvent.VK_ROMAN_CHARACTERS:
        case KeyEvent.VK_ALL_CANDIDATES:
        case KeyEvent.VK_PREVIOUS_CANDIDATE:
        case KeyEvent.VK_CODE_INPUT:
        case KeyEvent.VK_JAPANESE_KATAKANA:
        case KeyEvent.VK_JAPANESE_HIRAGANA:
        case KeyEvent.VK_JAPANESE_ROMAN:
        case KeyEvent.VK_KANA_LOCK:
        case KeyEvent.VK_INPUT_METHOD_ON_OFF:

        case KeyEvent.VK_AGAIN:
        case KeyEvent.VK_UNDO:
        case KeyEvent.VK_COPY:
        case KeyEvent.VK_PASTE:
        case KeyEvent.VK_CUT:
        case KeyEvent.VK_FIND:
        case KeyEvent.VK_PROPS:
        case KeyEvent.VK_STOP:

        case KeyEvent.VK_HELP:
            return true;
        }
        return false;
    }
    
    /**
     * Returns a unique text for a KeyEvent code 
     * 
     * @param keyCode   the keyCode to be "translated"
     * @return          the corrisponding text for the keyCode 
     */
    public static String getKeyText(int keyCode) {
        int expectedModifiers = 
            (Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL);
        
        Field[] fields = KeyEvent.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].getModifiers() == expectedModifiers
                        && fields[i].getType() == Integer.TYPE
                        && fields[i].getName().startsWith("VK_")
                        && fields[i].getInt(KeyEvent.class) == keyCode) {

                    return fields[i].getName().substring(3);
                }
            } catch (IllegalAccessException e) {

            }
        }
        return "UNKNOWN";
    }
    
    /**
     * Returns a unique text for the given key modifiers 
     * 
     * @param modifiers   the modifiers to be "translated"
     * @return          the corrisponding text for the keyCode 
     */
    public static String getModifiersText(int modifiers) {
        StringBuffer buf = new StringBuffer();

        if ((modifiers & InputEvent.SHIFT_MASK) != 0) {
            buf.append(SHIFT_MODIFIER).append(MODIFIER_JOINER);
        }
        if ((modifiers & InputEvent.CTRL_MASK) != 0) {
            buf.append(CTRL_MODIFIER).append(MODIFIER_JOINER);
        }
        if ((modifiers & InputEvent.META_MASK) != 0) {
            buf.append(META_MODIFIER).append(MODIFIER_JOINER);
        }
        if ((modifiers & InputEvent.ALT_MASK) != 0) {
            buf.append(ALT_MODIFIER).append(MODIFIER_JOINER);
        }
        if ((modifiers & InputEvent.ALT_GRAPH_MASK) != 0) {
            buf.append(ALT_GRAPH_MODIFIER).append(MODIFIER_JOINER);
        }
        return buf.toString();
    }
    
    /**
     * Formats a given KeyStroke into a string
     * 
     * @param keyStroke         the KeyStroke to be formatted
     * @return                  the formatted String
     */
    public static String formatKeyStroke(KeyStroke keyStroke) {
        if (keyStroke != null) {
            return getModifiersText(keyStroke.getModifiers())
                    + KeyEventUtils.getKeyText(keyStroke.getKeyCode());
        } else {
            return "";
        }        
    }
}
