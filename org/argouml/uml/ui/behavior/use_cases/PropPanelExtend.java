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

// File: PropPanelExtend.java
// Classes: PropPanelExtend
// Original Author: mail@jeremybennett.com
// $Id$

// 22 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Created to support a
// proper Extend implementation with Use Cases

// 11 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Condition field added.


package org.argouml.uml.ui.behavior.use_cases;

import org.argouml.application.api.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;


/**
 * <p>Builds the property panel for an Extend relationship.</p>
 *
 * <p>This is a type of Relationship, but, since Relationship has no semantic
 *   meaning of its own, we derive directly from PropPanelModelElement (as
 *   other children of Relationship do).</p>
 */

public class PropPanelExtend extends PropPanelModelElement {


    /**
     * Constructor. Builds up the various fields required.
     */

    public PropPanelExtend() {

        // Invoke the ModelElement constructor, but passing in our name and
        // representation and requesting 2 columns

        super("Extend", _extendIcon, 2);

        // nameField, stereotypeBox and namespaceScroll are all set up by
        // PropPanelModelElement.

        addCaption(Argo.localize("UMLMenu", "label.name"), 1, 0, 0);
        addField(nameField, 1, 0, 0);

        addCaption(Argo.localize("UMLMenu", "label.stereotype"), 2, 0, 0);
        addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),
                 2, 0, 0);

        addCaption(Argo.localize("UMLMenu", "label.namespace"), 3, 0, 0);
        addField(namespaceScroll, 3, 0, 0);

        // Our condition (ultimately a String). NSUML actually returns a
        // MBooleanExpression, so we must provide our own get and set methods.
        // Allow the condition label to expand vertically so we all float to
        // the top.

        UMLExpressionModel conditionModel =
            new UMLExpressionModel(this,MExtend.class,"condition",
		    MBooleanExpression.class,"getCondition","setCondition");

        JTextArea conditionArea = new UMLExpressionBodyField(conditionModel,
                                                            true);
        JScrollPane conditionScroll =
            new JScrollPane(conditionArea,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addCaption("Condition:", 4, 0, 1);
        addField(conditionScroll, 4, 0, 0);

        // Link to the two ends. This is done as a drop down. First for the
        // base use case.

        UMLModelElementComboBoxModel     model = 
            new UMLModelElementComboBoxModel(this, "getBase", "setBase",
                                             true, MUseCase.class);
        UMLModelElementComboBox          box   =
            new UMLModelElementComboBox(model);
        UMLModelElementComboBoxNavigator nav   =
            new UMLModelElementComboBoxNavigator(this, "NavUseCase", box);

        addCaption("Base:", 0, 1, 0);
        addField(nav, 0, 1, 0);

        // The extension use case (reuse earlier variables)

        model = new UMLModelElementComboBoxModel(this, "getExtension",
                                                 "setExtension", true,
                                                 MUseCase.class);
        box   = new UMLModelElementComboBox(model);
        nav   = new UMLModelElementComboBoxNavigator(this, "NavUseCase", box);

        addCaption("Extension:", 1, 1, 0);
        addField(nav, 1, 1, 0);

        // Build up a panel for extension points. This one will expand
        // vertically

        JList extensionPointList =
            new UMLList(new UMLExtensionPointListModel(this, true, true),
                        true);

        extensionPointList.setBackground(getBackground());
        extensionPointList.setForeground(Color.blue);

        JScrollPane extensionPointScroll =
            new JScrollPane(extensionPointList,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addCaption("Extension Points:", 2, 1, 1);
        addField(extensionPointScroll, 2, 1, 1);


        // Add the toolbar.

        new PropPanelButton(this, buttonPanel, _navUpIcon,
                            Argo.localize("UMLMenu", "button.go-up"), "navigateNamespace", null);
        new PropPanelButton(this, buttonPanel, _navBackIcon,
                            Argo.localize("UMLMenu", "button.go-back"), "navigateBackAction",
                            "isNavigateBackEnabled");
        new PropPanelButton(this, buttonPanel, _navForwardIcon,
                            Argo.localize("UMLMenu", "button.go-forward"), "navigateForwardAction",
                            "isNavigateForwardEnabled");
        new PropPanelButton(this, buttonPanel, _extensionPointIcon,
                            localize("Add extension point"),
                            "newExtensionPoint",
                            null);
        new PropPanelButton(this, buttonPanel, _deleteIcon,
                            localize("Delete"), "removeElement", null); 
    }


    /**
     * <p>Get the condition associated with the extend relationship.</p>
     *
     * <p>The condition is actually of type {@link MBooleanExpression}, which
     *   defines both a language and a body. We are only interested in the
     *   body, which is just a string.</p>
     *
     * @return  The body of the {@link MBooleanExpression} which is the
     *          condition associated with this extend relationship, or
     *          <code>null</code> if there is none.
     */ 

    public String getCondition() {
        String condBody = null;
        Object target   = getTarget();

        if (target instanceof MExtend) {
            MBooleanExpression condition = ((MExtend) target).getCondition();

            if (condition != null) {
                condBody = condition.getBody();
            }
        }

        return condBody;
    }


    /**
     * <p>Set the condition associated with the extend relationship.</p>
     *
     * <p>The condition is actually of type {@link MBooleanExpression}, which
     *   defines both a language and a body. We are only interested in setting
     *   the body, which is just a string.</p>
     *
     * @param condBody  The body of the condition to associate with this
     *                  extend relationship.
     */

    public void setCondition(String condBody) {

        // Give up if we are not an extend relationship

        Object target = getTarget();

        if (!(target instanceof MExtend)) {
            return;
        }

        // Set the condition body.

        ((MExtend) target).setCondition(UmlFactory.getFactory().getDataTypes().createBooleanExpression(null,condBody));
    }


    /**
     * <p>Invoked by the "Add extension point" toolbar button to create a new
     *   extension point for this extend relationship in the same namespace as
     *   the current extend relationship.</p>
     *
     * <p>This code uses getFactory and adds the extension point to
     *   the current extend relationship.</p> */

    public void newExtensionPoint() {
        Object target = getTarget();

        if(target instanceof MExtend) {
            MExtend    extend    = (MExtend) target;
            MNamespace ns        = extend.getNamespace();

            if(ns != null) {
                MExtensionPoint extensionPoint =
                    ns.getFactory().createExtensionPoint();

                // Add to the current extend relationship (NSUML will set the
                // reverse link) and place in the namespace, before navigating
                // to the extension point.

                extend.addExtensionPoint(extensionPoint);
                ns.addOwnedElement(extensionPoint);

                navigateTo(extensionPoint);
                // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
            }
        }
    }


    /**
     * <p>Check if a given name is our metaclass name, or that of one of our
     *   parents. Used to determine which stereotypes to show.</p>
     *
     * <p>Since we ignore Relationship, we effectively have no parents.</p>
     *
     * @param baseClass  the string representation of the base class to test.
     *
     * @return           <code>true</code> if baseClass is our metaclass name
     *                   of that of one of our parents.
     */

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {

        return baseClass.equals("Extend");
    }


    /**
     * <p>Get the current base use case of the extend relationship.</p>
     *
     * @return  The {@link MUseCase} that is the base of this extend
     *          relationship or <code>null</code> if there is none. Returned as
     *          type {@link MUseCase} to fit in with the type specified for
     *          the {@link UMLComboBoxModel}.
     */

    public MUseCase getBase() {
        MUseCase base   = null;
        Object      target = getTarget();

        if (target instanceof MExtend) {
            base = ((MExtend) target).getBase();
        }

        return base;
    }

    /**
     * <p>Set the base use case of the extend relationship.</p>
     *
     * @param base  The {@link MUseCase} to set as the base of this extend
     *              relationship. Supplied as type {@link MUseCase} to fit in
     *              with the type specified for the {@link UMLComboBoxModel}.
     */

    public void setBase(MUseCase base) {
        Object target = getTarget();

        if(target instanceof MExtend) {
            ((MExtend) target).setBase((MUseCase) base);
        }
    }


    /**
     * <p>Get the current extension use case of the extend relationship.</p>
     *
     * @return  The {@link MUseCase} that is the extension of this extend
     *          relationship or <code>null</code> if there is none. Returned as
     *          type {@link MUseCase} to fit in with the type specified for the
     *          {@link UMLComboBoxModel}.
     */

    public MUseCase getExtension() {
        MUseCase extension   = null;
        Object      target      = getTarget();

        if (target instanceof MExtend) {
            extension = ((MExtend) target).getExtension();
        }

        return extension;
    }

    /**
     * <p>Set the extension use case of the extend relationship.</p>
     *
     * @param extension  The {@link MUseCase} to set as the extension of this
     *                   extend relationship. Supplied as type {@link
     *                   MUseCase} to fit in with the type specified for the
     *                   {@link UMLComboBoxModel}.
     */

    public void setExtension(MUseCase extension) {
        Object target = getTarget();

        if(target instanceof MExtend) {
            ((MExtend) target).setExtension((MUseCase) extension);
        }
    }


    /**
     * <p>Predicate to test if a model element may appear in the list of
     *   potential use cases.</p>
     *
     * <p><em>Note</em>. We don't try to prevent the user setting up circular
     *   extend relationships. This may be necessary temporarily, for example
     *   while reversing a relationship. It is up to a critic to track
     *   this.</p>
     *
     * @param modElem  the {@link MModelElement} to test.
     *
     * @return         <code>true</code> if modElem is a use case,
     *                 <code>false</code> otherwise.
     */

    public boolean isAcceptableUseCase(MModelElement modElem) {

        return modElem instanceof MUseCase;
    }


} /* end class PropPanelExtend */
