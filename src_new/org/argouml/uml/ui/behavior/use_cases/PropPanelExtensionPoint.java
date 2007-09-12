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

package org.argouml.uml.ui.behavior.use_cases;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * Builds the property panel for an extension point.<p>
 *
 * This is a child of PropPanelModelElement.<p>
 *
 * @author Jeremy Bennett
 */
public class PropPanelExtensionPoint extends PropPanelModelElement {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 1835785842490972735L;

    /**
     * Construct a new property panel for an ExtensionPoint.
     */
    public PropPanelExtensionPoint() {
        super("label.extension-point", ConfigLoader.getTabPropsOrientation());

        // First column

        // nameField, stereotypeBox and namespaceScroll are all set up by
        // PropPanelModelElement.

        addField("label.name",
                getNameTextField());

        // Our location (a String).  Allow the location label to
        // expand vertically so we all float to the top.

        JTextField locationField = new UMLTextField2(
                new UMLExtensionPointLocationDocument());
        addField("label.location",
                locationField);

        addSeparator();

        addField("label.usecase-base",
                getSingleRowScroll(new UMLExtensionPointUseCaseListModel()));

        JList extendList = new UMLLinkedList(
                new UMLExtensionPointExtendListModel());
        addField("label.extend",
                new JScrollPane(extendList));


        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewExtensionPoint());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }

    /**
     * The method for the navigate up button, which takes us to the owning use
     * case.<p>
     *
     * This is a change from the norm, which is to navigate to the parent
     * namespace.<p>
     */
    public void navigateUp() {
        Object target = getTarget();

        // Only works for extension points

        if (!(Model.getFacade().isAExtensionPoint(target))) {
            return;
        }

        // Get the owning use case and navigate to it if it exists.

        Object owner = Model.getFacade().getUseCase(target);

        if (owner != null) {
            TargetManager.getInstance().setTarget(owner);
        }
    }

    private static class ActionNewExtensionPoint
        extends AbstractActionNewModelElement {

        /**
         * The serial version.
         */
        private static final long serialVersionUID = -4149133466093969498L;

        /**
         * Construct an action to create a new extension point.
         */
        public ActionNewExtensionPoint() {
            super("button.new-extension-point");
            putValue(Action.NAME,
                    Translator.localize("button.new-extension-point"));
        }

        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (Model.getFacade().isAExtensionPoint(target)) {
                TargetManager.getInstance().setTarget(
                    Model.getUseCasesFactory().buildExtensionPoint(
                            Model.getFacade().getUseCase(target)));
                super.actionPerformed(e);
            }
        }
    }

} /* end class PropPanelExtend */
