// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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
import java.util.Map;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;

/**
 * This panel contains radio buttons that represent the Changeability
 * of an sssociation-end.
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 4, 2003
 */
public class UMLAssociationEndChangeabilityRadioButtonPanel
    extends UMLRadioButtonPanel {

    private static Map labelTextsAndActionCommands = new HashMap();

    static {
        labelTextsAndActionCommands.put(Translator.localize(
                "label.changeability-addonly"),
                ActionSetChangeability.ADDONLY_COMMAND);
        labelTextsAndActionCommands.put(Translator.localize(
                "label.changeability-changeable"),
                ActionSetChangeability.CHANGEABLE_COMMAND);
        labelTextsAndActionCommands.put(Translator.localize(
                "label.changeability-frozen"),
                ActionSetChangeability.FROZEN_COMMAND);
    }

    /**
     * Constructor for UMLAssociationEndChangeabilityRadioButtonPanel.
     * @param title the title for the panel
     * @param horizontal determines the orientation
     */
    public UMLAssociationEndChangeabilityRadioButtonPanel(
            String title, boolean horizontal) {
        super(title, labelTextsAndActionCommands, "changeability",
                ActionSetChangeability.getInstance(), horizontal);
    }

    /*
     * @see org.argouml.uml.ui.UMLRadioButtonPanel#buildModel()
     */
    public void buildModel() {
        if (getTarget() != null) {
            Object target = getTarget();
            Object kind = Model.getFacade().getChangeability(target);
            if (kind == null
                || kind.equals(Model.getChangeableKind().getChangeable())) {
                setSelected(ActionSetChangeability.CHANGEABLE_COMMAND);
            } else
		if (kind.equals(Model.getChangeableKind().getAddOnly())) {
		    setSelected(ActionSetChangeability.ADDONLY_COMMAND);
		} else
		    if (kind.equals(Model.getChangeableKind().getFrozen())) {
			setSelected(ActionSetChangeability.FROZEN_COMMAND);
		    } else {
		        setSelected(ActionSetChangeability.CHANGEABLE_COMMAND);
		    }
        }
    }
}
