// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLScriptExpressionModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

import tudresden.ocl.check.types.ModelFacade;

/**
 * An abstract representatation of the properties panel of an Action. TODO: this
 * property panel needs refactoring to remove dependency on old gui components.
 */
public abstract class PropPanelAction extends PropPanelModelElement {

    private JScrollPane expressionScroll;

    private UMLExpressionLanguageField languageField;

    private JScrollPane recurrenceScroll;

    private JScrollPane argumentsScroll;

    private UMLExpressionModel2 expressionModel;

    private UMLExpressionModel2 recurrenceModel;

    private JList argumentsList;

    /**
     * The constructor.
     */
    public PropPanelAction() {
        this("Action", null);
    }

    /**
     * The constructor.
     * 
     * @param name
     *            the name of the properties panel
     * @param icon
     *            the icon to be shown next to the name
     */
    public PropPanelAction(String name, ImageIcon icon) {
        super(name, icon, ConfigLoader.getTabPropsOrientation());
        initialize();
    }

    /**
     * The initialization of the panel with its fields and stuff.
     */
    public void initialize() {

        addField(Translator.localize("label.name"), getNameTextField());

        UMLExpressionModel2 expressionModel = new UMLScriptExpressionModel(
                this, "script");
        addField(Translator.localize("label.expression"), new JScrollPane(
                new UMLExpressionBodyField(expressionModel, true)));
        addField(Translator.localize("label.language"),
                new UMLExpressionLanguageField(expressionModel, true));

        addSeperator();

        argumentsList = new UMLMutableLinkedList(
                new UMLActionArgumentListModel(), null,
                new ActionNewArgument(), new ActionRemoveArgument(), true);
        argumentsList.setVisibleRowCount(5);
        JScrollPane argumentsScroll = new JScrollPane(argumentsList);
        addField(Translator.localize("label.arguments"), argumentsScroll);

        addButton(new PropPanelButton2(new ActionNavigateContainerElement()));
        addButton(new PropPanelButton2(new ActionNewStereotype(),
                lookupIcon("Stereotype")));
        addButton(new PropPanelButton2(new ActionDeleteSingleModelElement(),
                lookupIcon("Delete")));
    }

    public JScrollPane getExpressionScroll() {
        if (expressionScroll == null) {
            UMLExpressionBodyField field = new UMLExpressionBodyField(
                    expressionModel, true);
            field.setRows(3);
            expressionScroll = new JScrollPane(field);
        }
        return expressionScroll;
    }

    public UMLExpressionLanguageField getLanguageField() {
        if (languageField == null) {
            languageField = new UMLExpressionLanguageField(expressionModel,
                    true);
        }
        return languageField;
    }

    public JScrollPane getRecurrenceScroll() {
        if (recurrenceScroll == null) {
            recurrenceScroll = new JScrollPane(new UMLExpressionBodyField(
                    recurrenceModel, true),
                    JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }
        return recurrenceScroll;
    }

    public JScrollPane getArgumentsScroll() {
        if (argumentsScroll == null) {
            argumentsScroll = new JScrollPane(argumentsList);
        }
        return argumentsScroll;
    }

    /*
     * public void initialize() { _expressionModel = new UMLExpressionModel(
     * this, (Class) ModelFacade.ACTION, "script", (Class)
     * ModelFacade.ACTION_EXPRESSION, "getScript", "setScript" );
     * _recurrenceModel = new UMLExpressionModel( this, (Class)
     * ModelFacade.ACTION, "recurrence", (Class)
     * ModelFacade.ITERATION_EXPRESSION, "getRecurrence", "setRecurrence" );
     * _argumentsList = new UMLMutableLinkedList(new
     * UMLActionActualArgumentListModel(), null, ActionNewArgument.SINGLETON,
     * ActionRemoveArgument.SINGLETON, false);
     * _argumentsList.setForeground(Color.blue);
     * _argumentsList.setVisibleRowCount(5); _argumentsList.setFont(smallFont); }
     */
}