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
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;
import org.argouml.util.ConfigLoader;

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
     * TODO: improve the conditionfield so it can be checked and the 
     * OCL editor can be used.
     */

    public PropPanelExtend() {
        super("Extend", _extendIcon, ConfigLoader.getTabPropsOrientation());

        addField(Argo.localize("UMLMenu", "label.name"), nameField);
        addField(Argo.localize("UMLMenu", "label.stereotype"), 
            new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
        addField(Argo.localize("UMLMenu", "label.namespace"), namespaceScroll);

        add(LabelledLayout.getSeperator());
            

        // Link to the two ends. This is done as a drop down. First for the
        // base use case.
        
        addField(Argo.localize("UMLMenu", "label.usecase-base"), 
            new UMLComboBox2(this, new UMLExtendBaseComboBoxModel(this), ActionSetExtendBase.SINGLETON));
            
        addField(Argo.localize("UMLMenu", "label.extension"), 
            new UMLComboBox2(this, new UMLExtendExtensionComboBoxModel(this), ActionSetExtendExtension.SINGLETON));
            
        JList extensionPointList = new UMLMutableLinkedList(this, new UMLExtendExtensionPointListModel(this), ActionAddExtendExtensionPoint.SINGLETON, ActionNewExtendExtensionPoint.SINGLETON);
        addField(Argo.localize("UMLMenu", "label.extensionpoints"), 
            new JScrollPane(extensionPointList));
            
        add(LabelledLayout.getSeperator());
            
        UMLExpressionModel conditionModel =
            new UMLExpressionModel(this,MExtend.class,"condition",
            MBooleanExpression.class,"getCondition","setCondition");

        JTextArea conditionArea = new UMLExpressionBodyField(conditionModel,
                                                            true);
        JScrollPane conditionScroll =
            new JScrollPane(conditionArea);

        addField("Condition:", conditionScroll);

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
                if (extend.getBase() != null) {
                    
                MExtensionPoint extensionPoint =
                    UseCasesFactory.getFactory().buildExtensionPoint(extend.getBase());

                extend.addExtensionPoint(extensionPoint);
                }
 
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







} /* end class PropPanelExtend */
