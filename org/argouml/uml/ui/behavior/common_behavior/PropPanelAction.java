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

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.UMLRecurrenceExpressionModel;
import org.argouml.uml.ui.UMLScriptExpressionModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;
import org.tigris.swidgets.GridLayout2;

/**
 * An abstract representatation of the properties panel of an Action. TODO: this
 * property panel needs refactoring to remove dependency on old gui components.
 */
public abstract class PropPanelAction extends PropPanelModelElement {

        private JScrollPane expressionScroll;

        private UMLExpressionLanguageField languageField;

        private JScrollPane recurrenceScroll;

        private JScrollPane argumentsScroll;

        private UMLExpressionModel2 scriptModel;

        private UMLExpressionModel2 recurrenceModel;

        private JList argumentsList;

        private JPanel recurrencePanel;

        private JPanel scriptPanel;

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
         *                the name of the properties panel
         * @param icon
         *                the icon to be shown next to the name
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

                add(new UMLActionAsynchronousCheckBox());

                UMLExpressionModel2 scriptModel = new UMLScriptExpressionModel(
                                this, "script");

                scriptPanel = new JPanel(new GridLayout2());
                scriptPanel.setBorder(new TitledBorder(Translator
                                .localize("label.script")));
                // recurrencePanel.add(addField(Translator.localize("label.expression"),
                // new JScrollPane(
                // new UMLExpressionBodyField(expressionModel, true))));
                // recurrencePanel.add(addField(Translator.localize("label.language"),
                // new UMLExpressionLanguageField(expressionModel, false)));
                scriptPanel.add(new JScrollPane(new UMLExpressionBodyField(
                                scriptModel, true)));
                scriptPanel.add(new UMLExpressionLanguageField(scriptModel,
                                false));

                add(scriptPanel);

                UMLExpressionModel2 recurrenceModel = new UMLRecurrenceExpressionModel(
                                this, "recurrence");

                recurrencePanel = new JPanel(new GridLayout2());
                recurrencePanel.setBorder(new TitledBorder(Translator
                                .localize("label.recurrence")));
                recurrencePanel.add(new JScrollPane(new UMLExpressionBodyField(
                                recurrenceModel, true)));
                recurrencePanel.add(new UMLExpressionLanguageField(
                                recurrenceModel, false));

                add(recurrencePanel);

                addSeperator();

                argumentsList = new UMLMutableLinkedList(
                                new UMLActionArgumentListModel(), null,
                                new ActionNewArgument(),
                                new ActionRemoveArgument(), true);
                argumentsList.setVisibleRowCount(5);
                JScrollPane argumentsScroll = new JScrollPane(argumentsList);
                addField(Translator.localize("label.arguments"),
                                argumentsScroll);

                addAction(new ActionNavigateContainerElement());
                addAction(new ActionNewStereotype());
                addAction(new ActionDeleteSingleModelElement());

        }

        public JScrollPane getExpressionScroll() {
                if (expressionScroll == null) {
                        UMLExpressionBodyField field = new UMLExpressionBodyField(
                                        scriptModel, true);
                        field.setRows(3);
                        expressionScroll = new JScrollPane(field);
                }
                return expressionScroll;
        }

        public UMLExpressionLanguageField getLanguageField() {
                if (languageField == null) {
                        languageField = new UMLExpressionLanguageField(
                                        scriptModel, true);
                }
                return languageField;
        }

        public JScrollPane getRecurrenceScroll() {
                if (recurrenceScroll == null) {
                        recurrenceScroll = new JScrollPane(
                                        new UMLExpressionBodyField(
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
         * UMLActionActualArgumentListModel(), null,
         * ActionNewArgument.SINGLETON, ActionRemoveArgument.SINGLETON, false);
         * _argumentsList.setForeground(Color.blue);
         * _argumentsList.setVisibleRowCount(5);
         * _argumentsList.setFont(smallFont); }
         */
}
