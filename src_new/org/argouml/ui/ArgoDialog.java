/*
 * ArgoDialog.java
 *
 * Created on 12 June 2003, 00:01
 */

package org.argouml.ui;

import java.awt.Frame;
import org.argouml.application.api.Argo;

/**
 * A dialog with localized buttons
 *
 * @author Bob Tarling
 */
public abstract class ArgoDialog extends org.argouml.swingext.Dialog {
    
    protected static final String BUNDLE = "Cognitive";
    
    /**
     * Create a new ArgoDialog
     */
    public ArgoDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }
    
    protected void nameButtons() {
        _okButton.setText(Argo.localize(BUNDLE, "button.ok"));
        _cancelButton.setText(Argo.localize(BUNDLE, "button.cancel"));
        _closeButton.setText(Argo.localize(BUNDLE, "button.close"));
        _yesButton.setText(Argo.localize(BUNDLE, "button.yes"));
        _noButton.setText(Argo.localize(BUNDLE, "button.no"));
        _helpButton.setText(Argo.localize(BUNDLE, "button.help"));
    }
}
