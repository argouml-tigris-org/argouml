// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.api.model.FacadeManager;
import org.argouml.model.uml.NsumlModelFacade;

import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

/**
 * @todo this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public class PropPanelCallAction extends PropPanelModelElement {

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelCallAction() {
        super("Action", _callActionIcon, ConfigLoader.getTabPropsOrientation());

        Class mclass = (Class)NsumlModelFacade.CALL_ACTION;

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());

        UMLExpressionModel expressionModel =
            new UMLExpressionModel(
                this,
                (Class)NsumlModelFacade.ACTION,
                "script",
                (Class)NsumlModelFacade.ACTION_EXPRESSION,
                "getScript",
                "setScript");
        addField(Argo.localize("UMLMenu", "label.expression"), new JScrollPane(new UMLExpressionBodyField(expressionModel, true)));

        addField(Argo.localize("UMLMenu", "label.language"), new UMLExpressionLanguageField(expressionModel, true));

        new PropPanelButton(
            this,
            buttonPanel,
            _navUpIcon,
            Argo.localize("UMLMenu", "button.go-up"),
            "navigateUp",
            null);
        new PropPanelButton(
            this,
            buttonPanel,
            _deleteIcon,
            localize("Delete"),
            "removeElement",
            null);
    }

} /* end class PropPanelCallAction */
