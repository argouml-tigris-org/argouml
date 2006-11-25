// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.util.HashMap;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;

/**
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 4, 2003
 */
public class UMLModelElementVisibilityRadioButtonPanel
    extends UMLRadioButtonPanel {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -1705561978481456281L;
    
    private static HashMap labelTextsAndActionCommands = new HashMap();

    // TODO: The buttons should be order of reducing visibility, but
    // they get ordered by natural order of the keys (hash order?) - tfm
    static {
        labelTextsAndActionCommands.put(Translator.localize(
                "label.visibility-public"),
                ActionSetModelElementVisibility.PUBLIC_COMMAND);
        labelTextsAndActionCommands.put(Translator.localize(
                "label.visibility-package"),
                ActionSetModelElementVisibility.PACKAGE_COMMAND);
        labelTextsAndActionCommands.put(Translator.localize(
                "label.visibility-protected"),
                ActionSetModelElementVisibility.PROTECTED_COMMAND);
        labelTextsAndActionCommands.put(Translator.localize(
                "label.visibility-private"),
                ActionSetModelElementVisibility.PRIVATE_COMMAND);
    }

    /**
     * Constructor for UMLAssociationEndChangeabilityRadioButtonPanel.
     * @param title the title for the panel
     * @param horizontal determines the orientation
     */
    public UMLModelElementVisibilityRadioButtonPanel(
            String title, boolean horizontal) {
        super(title, labelTextsAndActionCommands, "visibility",
                ActionSetModelElementVisibility.getInstance(), horizontal);
    }

    /*
     * @see org.argouml.uml.ui.UMLRadioButtonPanel#buildModel()
     */
    public void buildModel() {
        if (getTarget() != null) {
            Object target = getTarget();
            Object kind = Model.getFacade().getVisibility(target);
            if (kind == null
                    || kind.equals(
                            Model.getVisibilityKind().getPublic())) {
                setSelected(ActionSetModelElementVisibility.PUBLIC_COMMAND);
            } else if (kind.equals(
                    Model.getVisibilityKind().getPackage())) {
                setSelected(ActionSetModelElementVisibility.PACKAGE_COMMAND);
            } else if (kind.equals(
                    Model.getVisibilityKind().getProtected())) {
                setSelected(ActionSetModelElementVisibility.PROTECTED_COMMAND);
            } else if (kind.equals(
                    Model.getVisibilityKind().getPrivate())) {
                setSelected(ActionSetModelElementVisibility.PRIVATE_COMMAND);
            } else {
                setSelected(ActionSetModelElementVisibility.PUBLIC_COMMAND);
            }
        }
    }

}
