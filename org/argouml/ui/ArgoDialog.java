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
public class ArgoDialog extends org.argouml.swingext.Dialog {
    
    protected static final String BUNDLE = "Cognitive";
    
    /**
     * Create a new ArgoDialog
     */
    public ArgoDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
    }
    
    protected void nameButtons() {
        getOkButton().setText(Argo.localize(BUNDLE, "button.ok"));
        getCancelButton().setText(Argo.localize(BUNDLE, "button.cancel"));
        getCloseButton().setText(Argo.localize(BUNDLE, "button.close"));
        getYesButton().setText(Argo.localize(BUNDLE, "button.yes"));
        getNoButton().setText(Argo.localize(BUNDLE, "button.no"));
        getHelpButton().setText(Argo.localize(BUNDLE, "button.help"));
    }
}
