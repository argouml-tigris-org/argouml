// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: PropPanelExtensionPoint.java
// Classes: PropPanelExtensionPoint
// Original Author: mail@jeremybennett.com
// $Id$

// 27 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Created to support a
// proper Extend implementation with Use Cases


package org.argouml.uml.ui.behavior.use_cases;

import org.argouml.application.api.*;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;
import org.argouml.util.ConfigLoader;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;


/**
 * <p>Builds the property panel for an extension point.</p>
 *
 * <p>This is a child of PropPanelModelElement.</p>
 */

public class PropPanelExtensionPoint extends PropPanelModelElement {


    /**
     * Constructor. Builds up the various fields required.
     */

    public PropPanelExtensionPoint() {

        // Invoke the ModelElement constructor, but passing in our name and
        // representation (we use the same as dependency) and requesting 2
        // columns 

        super("ExtensionPoint", _extensionPointIcon, ConfigLoader.getTabPropsOrientation());

        // This will cause the components on this property panel to be notified
        // anytime a stereotype, namespace or use case has its name changed
        // or is removed anywhere in the model.

        Class[] namesToWatch = { MStereotype.class,
                                 MNamespace.class,
                                 MUseCase.class };
        setNameEventListening(namesToWatch);

        // First column

        // nameField, stereotypeBox and namespaceScroll are all set up by
        // PropPanelModelElement.

        addField(Argo.localize("UMLMenu", "label.name"), nameField);
        addField(Argo.localize("UMLMenu", "label.stereotype"), 
            new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
        addField(Argo.localize("UMLMenu", "label.namespace"), namespaceComboBox);
        
        // Our location (a String). We can pass in the get and set methods from
        // NSUML associated with the NSUML type. Allow the location label to
        // expand vertically so we all float to the top.

        JTextField locationField = new UMLTextField2(this, new UMLExtensionPointLocationDocument(this));
        addField(Argo.localize("UMLMenu", "label.location"), locationField);
        
        add(LabelledLayout.getSeperator());
        
        JList usecaseList = new UMLLinkedList(this, new UMLExtensionPointUseCaseListModel(this));
        usecaseList.setVisibleRowCount(1);
        addField(Argo.localize("UMLMenu", "label.usecase"), 
            new JScrollPane(usecaseList));
        
        JList extendList = new UMLLinkedList(this, new UMLExtensionPointExtendListModel(this));
        addField(Argo.localize("UMLMenu", "label.extend"), 
            new JScrollPane(extendList));


        // Add the toolbar. Just the four basic buttons for now. Note that
        // navigate up is not to the namespace, but to our local routine that
        // selects the owning use case.

        new PropPanelButton(this, buttonPanel, _navUpIcon,
                            Argo.localize("UMLMenu", "button.go-up"), "navigateUp", null);
        new PropPanelButton(this, buttonPanel, _navBackIcon,
                            Argo.localize("UMLMenu", "button.go-back"), "navigateBackAction",
                            "isNavigateBackEnabled");
        new PropPanelButton(this, buttonPanel, _navForwardIcon,
                            Argo.localize("UMLMenu", "button.go-forward"), "navigateForwardAction",
                            "isNavigateForwardEnabled");
        new PropPanelButton(this, buttonPanel, _deleteIcon,
                            localize("Delete"), "removeElement", null); 
    }


    /**
     * <p>The method for the navigate up button, which takes us to the owning
     *   use case.</p>
     *
     * <p>This is a change from the norm, which is to navigate to the parent
     *   namespace.</p>
     */

    public void navigateUp() {
        Object target = getTarget();

        // Only works for extension points

        if (!(target instanceof MExtensionPoint)) {
            return;
        }

        // Get the owning use case and navigate to it if it exists.

        MUseCase owner = ((MExtensionPoint) target).getUseCase();

        if(owner != null) {
            navigateTo(owner);
        }
    }


    /**
     * <p>Check if a given name is our metaclass name, or that of one of our
     *   parents. Used to determine which stereotypes to show. Only handles
     *   metaclasses below ModelElement.</p>
     *
     * <p>Since we are a child of ModelElement, we effectively have no
     *   parents.</p>
     *
     * @param baseClass  the string representation of the base class to test.
     *
     * @return           <code>true</code> if baseClass is our metaclass name
     *                   of that of one of our parents.
     */

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {

        return baseClass.equals("ExtensionPoint");
    }


   

} /* end class PropPanelExtend */
