// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLScriptExpressionModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

/**
 * An abstract representatation of the properties panel of an Action.
 * 
 * TODO: this property panel needs refactoring to remove dependency on
 *       old gui components.
 */
public abstract class PropPanelAction extends PropPanelModelElement {

    /**
     * The constructor.
     * 
     */
    public PropPanelAction() {
        this("Action", null);
    }

    /**
     * The constructor.
     * 
     * @param name the name of the properties panel
     * @param icon the icon to be shown next to the name
     */
    public PropPanelAction(String name, ImageIcon icon) {
        super(name, icon, 
              ConfigLoader.getTabPropsOrientation());
        initialize();
    }

    /**
     * The initialization of the panel with its fields and stuff.
     */
    public void initialize() {
        
        addField(Translator.localize("label.name"), 
                getNameTextField());

//        UMLExpressionModel expressionModel =
//            new UMLExpressionModel(
//                this,
//                (Class) ModelFacade.ACTION,
//                "script",
//                (Class) ModelFacade.ACTION_EXPRESSION,
//                "getScript",
//                "setScript");
        UMLExpressionModel2 expressionModel = 
            new UMLScriptExpressionModel(this, "script");
        addField(Translator.localize("label.expression"), 
                new JScrollPane(
                        new UMLExpressionBodyField(expressionModel, true)));

        addField(Translator.localize("label.language"), 
                new UMLExpressionLanguageField(expressionModel, true));

        addButton(new PropPanelButton2(this,
                new ActionNavigateContainerElement()));
        addButton(new PropPanelButton2(this, new ActionRemoveFromModel()));
    }

} /* end class PropPanelCallAction */
