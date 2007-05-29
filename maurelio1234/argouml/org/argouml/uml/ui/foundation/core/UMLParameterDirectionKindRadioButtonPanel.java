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
import java.util.Map;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLRadioButtonPanel;

/**
 * A panel for the parameterdiretion (in, inout...) of a parameter.
 *
 * @author mkl
 */
public class UMLParameterDirectionKindRadioButtonPanel extends
        UMLRadioButtonPanel {

    private static Map labelTextsAndActionCommands = new HashMap();

    static {
        // TODO: i18n, use Translator
        labelTextsAndActionCommands.put("in",
                ActionSetParameterDirectionKind.IN_COMMAND);
        labelTextsAndActionCommands.put("out",
                ActionSetParameterDirectionKind.OUT_COMMAND);
        labelTextsAndActionCommands.put("inout",
                ActionSetParameterDirectionKind.INOUT_COMMAND);
        labelTextsAndActionCommands.put("return",
                ActionSetParameterDirectionKind.RETURN_COMMAND);
    }

    /**
     * Constructor.
     *
     * @param title the title of the panel
     * @param horizontal determines the orientation
     */
    public UMLParameterDirectionKindRadioButtonPanel(String title,
            boolean horizontal) {
        // TODO: i18n
        super(title, labelTextsAndActionCommands, "ParameterKind:",
                ActionSetParameterDirectionKind.getInstance(), horizontal);
    }

    /*
     * @see org.argouml.uml.ui.UMLRadioButtonPanel#buildModel()
     */
    public void buildModel() {
        if (getTarget() != null) {
            Object target = /* (MModelElement) */getTarget();
            Object kind = Model.getFacade().getKind(target);
            if (kind == null
                    || kind.equals(
                            Model.getDirectionKind().getInParameter())) {
                setSelected(ActionSetParameterDirectionKind.IN_COMMAND);
            } else if (kind.equals(
                    Model.getDirectionKind().getInOutParameter())) {
                setSelected(ActionSetParameterDirectionKind.INOUT_COMMAND);
            } else if (kind.equals(
                    Model.getDirectionKind().getOutParameter())) {
                setSelected(ActionSetParameterDirectionKind.OUT_COMMAND);
            } else {
                setSelected(ActionSetParameterDirectionKind.RETURN_COMMAND);
            }
        }
    }
}
