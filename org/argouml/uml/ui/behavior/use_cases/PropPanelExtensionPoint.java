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

package org.argouml.uml.ui.behavior.use_cases;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UseCasesFactory;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton2;
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
     * Constructor. Builds up the various fields required.
     */
    public PropPanelExtensionPoint() {

        // Invoke the ModelElement constructor, but passing in our name and
        // representation (we use the same as dependency) and requesting 2
        // columns

        super("ExtensionPoint", ConfigLoader.getTabPropsOrientation());

        // First column

        // nameField, stereotypeBox and namespaceScroll are all set up by
        // PropPanelModelElement.

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeBox());

        // Our location (a String). We can pass in the get and set methods from
        // NSUML associated with the NSUML type. Allow the location label to
        // expand vertically so we all float to the top.

        JTextField locationField = new UMLTextField2(
                new UMLExtensionPointLocationDocument());
        addField(Translator.localize("label.location"),
                locationField);

        addSeperator();

        JList usecaseList = new UMLLinkedList(
                new UMLExtensionPointUseCaseListModel());
        usecaseList.setVisibleRowCount(1);
        addField(Translator.localize("label.usecase-base"),
                new JScrollPane(usecaseList));

        JList extendList = new UMLLinkedList(
                new UMLExtensionPointExtendListModel());
        addField(Translator.localize("label.extend"),
                new JScrollPane(extendList));

        
        addButton(new PropPanelButton2(new ActionNavigateContainerElement()));
        addButton(new PropPanelButton2(new ActionNewExtensionPoint(), 
                lookupIcon("ExtensionPoint")));
        addButton(new PropPanelButton2(new ActionNewStereotype(), 
                lookupIcon("Stereotype")));
        addButton(new PropPanelButton2(new ActionRemoveFromModel(), 
                lookupIcon("Delete")));
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

        if (!(org.argouml.model.ModelFacade.isAExtensionPoint(target))) { 
            return; 
        }

        // Get the owning use case and navigate to it if it exists.

        Object owner = ModelFacade.getUseCase(target);

        if (owner != null) {
            TargetManager.getInstance().setTarget(owner);
        }
    }
    
    private class ActionNewExtensionPoint 
        extends AbstractActionNewModelElement {
    
        /**
         * The constructor.
         */
        public ActionNewExtensionPoint() {
            super("button.new-extension-point");
            putValue(Action.NAME, 
                    Translator.localize("button.new-extension-point"));
        }
        
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            Object target = TargetManager.getInstance().getModelTarget();
            if (ModelFacade.isAExtensionPoint(target)) {
                TargetManager.getInstance().setTarget(
                    UseCasesFactory.getFactory().buildExtensionPoint(
                            ModelFacade.getUseCase(target)));
                super.actionPerformed(e);
            }
        }
    }

} /* end class PropPanelExtend */