// $Id: PropPanelAction.java 12244 2007-03-09 17:00:24Z mvw $
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

package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLExpressionBodyField;
import org.argouml.uml.ui.UMLExpressionLanguageField;
import org.argouml.uml.ui.UMLExpressionModel2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLRecurrenceExpressionModel;
import org.argouml.uml.ui.UMLScriptExpressionModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * An abstract representatation of the properties panel of an Action.
 */
public abstract class PropPanelAction extends PropPanelModelElement {

    /**
     * The scroll pane for arguments.
     */
    protected JScrollPane argumentsScroll;

    /**
     * Construct a default property panel for an Action.
     */
    public PropPanelAction() {
        this("Action", null);
    }

    /**
     * Construct an Action property panel with the given name and icon.
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

        add(new UMLActionAsynchronousCheckBox());

        UMLExpressionModel2 scriptModel =
            new UMLScriptExpressionModel(
                this, "script");

        JPanel scriptPanel = createBorderPanel(Translator
                .localize("label.script"));

        scriptPanel.add(new JScrollPane(new UMLExpressionBodyField(
                scriptModel, true)));
        scriptPanel.add(new UMLExpressionLanguageField(scriptModel,
                false));

        add(scriptPanel);

        UMLExpressionModel2 recurrenceModel =
            new UMLRecurrenceExpressionModel(
                this, "recurrence");

        JPanel recurrencePanel = createBorderPanel(Translator
                .localize("label.recurrence"));
        recurrencePanel.add(new JScrollPane(new UMLExpressionBodyField(
                recurrenceModel, true)));
        recurrencePanel.add(new UMLExpressionLanguageField(
                recurrenceModel, false));

        add(recurrencePanel);

        addSeparator();

        JList argumentsList = new UMLLinkedList(
                    new UMLActionArgumentListModel());
        argumentsList.setVisibleRowCount(5);
        argumentsScroll = new JScrollPane(argumentsList);
        addField(Translator.localize("label.arguments"),
                argumentsScroll);

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionCreateArgument());
        addAction(new ActionNewStereotype());
        addExtraActions();
        addAction(getDeleteAction());

    }

    /**
     * Overrule this to add extra action buttons.
     */
    protected void addExtraActions() {
        // do nothing by default
    }
}

class ActionCreateArgument extends AbstractActionNewModelElement {

    /**
     * Constructor for ActionNewArgument.
     */
    public ActionCreateArgument() {
        super("button.new-argument");
        putValue(Action.NAME,
                Translator.localize("button.new-argument"));
        putValue(Action.SMALL_ICON,
                ResourceLoaderWrapper.lookupIcon("NewParameter"));
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object t = TargetManager.getInstance().getTarget();
        if (Model.getFacade().isAAction(t)) {
            Object argument = Model.getCommonBehaviorFactory().createArgument();
            Model.getCommonBehaviorHelper().addActualArgument(t, argument);
            TargetManager.getInstance().setTarget(argument);
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -3455108052199995234L;
}
