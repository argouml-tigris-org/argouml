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

package org.argouml.uml.ui.behavior.collaborations;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * Properties panel for a Message.
 */
public class PropPanelMessage extends PropPanelModelElement {

    /**
     * Construct a new property panel for a Message.
     */
    public PropPanelMessage() {
        super("Message", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        JList interactionList =
            new UMLLinkedList(new UMLMessageInteractionListModel());
        interactionList.setVisibleRowCount(1);
        addField(Translator.localize("label.interaction"),
        	 new JScrollPane(interactionList));

        JList senderList = new UMLLinkedList(new UMLMessageSenderListModel());
        senderList.setVisibleRowCount(1);
        JScrollPane senderScroll = new JScrollPane(senderList);
        addField(Translator.localize("label.sender"), senderScroll);

        JList receiverList =
            new UMLLinkedList(new UMLMessageReceiverListModel());
        receiverList.setVisibleRowCount(1);
        JScrollPane receiverScroll = new JScrollPane(receiverList);
        addField(Translator.localize("label.receiver"),
                receiverScroll);

        addSeparator();

        addField(Translator.localize("label.activator"),
        	 new UMLMessageActivatorComboBox(this,
        		 new UMLMessageActivatorComboBoxModel()));

        JList actionList =
        	 new UMLMutableLinkedList(new UMLMessageActionListModel(),
        	         null, ActionNewActionForMessage.getInstance());
        actionList.setVisibleRowCount(1);
        JScrollPane actionScroll = new JScrollPane(actionList);
        addField(Translator.localize("label.action"), actionScroll);

        JScrollPane predecessorScroll =
                new JScrollPane(
                new UMLMutableLinkedList(new UMLMessagePredecessorListModel(),
        	ActionAddMessagePredecessor.getInstance(),
        	null));
        addField(Translator.localize("label.predecessor"),
        	 predecessorScroll);

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionToolNewAction());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }

    private static class ActionToolNewAction
        extends AbstractActionNewModelElement {

        /**
         * Construct an action to add a new UML Action to the Message.
         */
        public ActionToolNewAction() {
            super("button.new-action");
            putValue(Action.NAME, Translator.localize("button.new-action"));
            Icon icon = ResourceLoaderWrapper.lookupIcon("CallAction");
            putValue(Action.SMALL_ICON, icon);
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isAMessage(target)) {
                Model.getCommonBehaviorFactory().buildAction(target);
                super.actionPerformed(e);
            }
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -6588197204256288453L;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8433911715875762175L;
} /* end class PropPanelMessage */
