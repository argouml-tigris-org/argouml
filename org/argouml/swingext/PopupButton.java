// $Id$
// Copyright (c) 2003-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
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

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import org.tigris.toolbar.toolbutton.ModalButton;

/**
 * A button that displays a popup when pressed. The popup can contain
 * any Component.
 * 
 * @author Jeremy Jones
 */
public class PopupButton extends ModalButton {

    /**
     * The popup component.
     */
    private Component   _component = null;
    
    /**
     * Constructs a new PopupButton with no popup assigned.
     */
    public PopupButton() {
        super(null);
        
        setAction(new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                showPopup();
            }
        });
        
        String javaVersion = System.getProperties().getProperty(
            "java.specification.version");
        if (javaVersion.equals("1.3")) {
            setBorderPainted(false);
            // This is needed specifically for JDK1.3 on Windows & Motif
            setMargin(new Insets(0, 0, 0, 0));
        }
    }
    
    /**
     * Constructs a new PopupButton with the specified popup component
     * and button icon
     * 
     * @param c     the component to popup
     * @param icon  the button icon
     */
    public PopupButton(Component c, Icon icon) {
        this();
        _component = c;
        setIcon(icon);
    }
    
    /**
     * Returns the component that is displayed within the popup.
     * 
     * @return  the popup component
     */
    public Component getPopupComponent() {
        return _component;
    }
    
    /**
     * Sets the component to be displayed within the popup.
     * 
     * @param   c   the new popup component
     */
    public void setPopupComponent(Component c) {
        _component = c;
    }
    
    /**
     * Sets the button icon. Overridden to automatically add a drop down icon
     * to the right of the icon image.
     * 
     * @param   icon    the new button icon
     */
    public final void setIcon(Icon icon) {
        if (icon instanceof ImageIcon) {
            super.setIcon(new DropDownIcon((ImageIcon) icon));
        }
        else {
            super.setIcon(icon);
        }
    }
    
    /**
     * Called when the user clicks the button. This method will show the
     * popup component in a popup menu. Subclasses can override this
     * method to perform actions before the popup is shown.
     */
    protected void showPopup() {
        if (_component != null) {
            JPopupMenu popupMenu = new JPopupMenu();
            popupMenu.add(_component);
            popupMenu.pack();            
            popupMenu.show(this, 0, getHeight());
        }
    }
}
