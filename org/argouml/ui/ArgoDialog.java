/*
 * ArgoDialog.java
 *
 * Created on 12 June 2003, 00:01
 */

package org.argouml.ui;

import java.awt.Frame;

import javax.swing.JButton;

import org.argouml.application.api.Argo;

/**
 * A dialog with localized buttons.
 *
 * @author Bob Tarling
 */
public class ArgoDialog extends org.argouml.swingext.Dialog {
    
    protected static final String BUNDLE = "Cognitive";
    protected static final String MNEMONIC_KEY_SUFFIX = ".mnemonic";
    
    /**
     * Creates a new ArgoDialog with the default optionType.
     */
    public ArgoDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }

    /**
     * Creates a new ArgoDialog with the specified optionType.
     */
    public ArgoDialog(Frame owner, String title, int optionType, boolean modal) {
        super(owner, title, optionType, modal);
    }
        
    protected void nameButtons() {  
        nameButton(getOkButton(), "button.ok");
        nameButton(getCancelButton(), "button.cancel");
        nameButton(getCloseButton(), "button.close");
        nameButton(getYesButton(), "button.yes");
        nameButton(getNoButton(), "button.no");
        nameButton(getHelpButton(), "button.help");
    }
    
    protected void nameButton(JButton button, String key) {
        if (button != null) {
            button.setText(Argo.localize(BUNDLE, key));
            String mnemonic = Argo.localize(BUNDLE, key + MNEMONIC_KEY_SUFFIX);
            if (mnemonic != null && mnemonic.length() > 0) {
                button.setMnemonic(mnemonic.charAt(0));
            }
        }
    }
}
