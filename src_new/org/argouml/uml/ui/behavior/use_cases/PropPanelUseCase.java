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

// File: PropPanelUseCase.java
// Classes: PropPanelUseCase
// Original Author: your email address here
// $Id$

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" for inheritance (needs Specializes some time).

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Specializes field
// added. Factoring to use PropPanelModifiers and tidying up of layout.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Tool tip changed to
// "Add use case".

package org.argouml.uml.ui.behavior.use_cases;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.PropPanelModifiers;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.behavior.use_cases.MUseCase;
import ru.novosoft.uml.foundation.core.MNamespace;


/**
 * <p>Builds the property panel for a use case.</p>
 *
 * <p>This is a type of Classifier, and like other Classifiers can have
 *   attributes and operations (some processes use these to define
 *   requirements). <em>Note</em>. ArgoUML does not currently support separate
 *   compartments on the display for this.</p>
 */

public class PropPanelUseCase extends PropPanelClassifier {


    /**
     * <p>Constructor. Builds up the various fields required.</p>
     */

    public PropPanelUseCase() {

        // Invoke the Classifier constructor, but passing in our name and
        // representation and requesting 3 columns

        super("UseCase", ConfigLoader.getTabPropsOrientation());
        
        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
    	addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),getStereotypeBox()));
    	addField(Argo.localize("UMLMenu", "label.namespace"),getNamespaceComboBox());
		
	PropPanelModifiers mPanel = new PropPanelModifiers(3);
        Class mclass = MUseCase.class;
        
    // since when do we know abstract usecases?
    //    mPanel.add("isAbstract", mclass, "isAbstract", "setAbstract",
    //               Argo.localize("UMLMenu", "checkbox.abstract-lc"), this);
        mPanel.add("isLeaf", mclass, "isLeaf", "setLeaf",
                   Argo.localize("UMLMenu", "checkbox.final-lc"), this);
        mPanel.add("isRoot", mclass, "isRoot", "setRoot",
                   localize("root"), this);
		addField(Argo.localize("UMLMenu", "label.modifiers"),mPanel);
            	
    JList extensionPoints = new UMLMutableLinkedList(new UMLUseCaseExtensionPointListModel(), null, ActionNewUseCaseExtensionPoint.SINGLETON);
    addField(Argo.localize("UMLMenu", "label.extension-points"), 
        new JScrollPane(extensionPoints));
		
    add(LabelledLayout.getSeperator());
		
    addField(Argo.localize("UMLMenu", "label.generalizations"), getGeneralizationScroll());
    addField(Argo.localize("UMLMenu", "label.specializations"), getSpecializationScroll());
    	
    JList extendsList = new UMLLinkedList(new UMLUseCaseExtendListModel());
    addField(Argo.localize("UMLMenu", "label.extends"), 
        new JScrollPane(extendsList));
    
    JList includesList = new UMLLinkedList(new UMLUseCaseIncludeListModel());
    addField(Argo.localize("UMLMenu", "label.includes"), 
        new JScrollPane(includesList));

    add(LabelledLayout.getSeperator());
	
    
        new PropPanelButton(this, buttonPanel, _navUpIcon,
                            Argo.localize("UMLMenu", "button.go-up"), "navigateNamespace",
                            null);
        new PropPanelButton(this, buttonPanel, _navBackIcon,
                            Argo.localize("UMLMenu", "button.go-back"), "navigateBackAction",
                            "isNavigateBackEnabled");
        new PropPanelButton(this, buttonPanel, _navForwardIcon,
                            Argo.localize("UMLMenu", "button.go-forward"), "navigateForwardAction",
                            "isNavigateForwardEnabled");
        new PropPanelButton(this, buttonPanel, _useCaseIcon,
                            Argo.localize("UMLMenu", "button.add-usecase"), "newUseCase",
                            null);
        new PropPanelButton(this, buttonPanel, _extensionPointIcon,
                            localize("Add extension point"),
                            "newExtensionPoint",
                            null);
        new PropPanelButton(this, buttonPanel, _deleteIcon,
                            localize("Delete"), "removeElement",
                            null);

    }


    /**
     * <p>Invoked by the "Add use case" toolbar button to create a new use case
     *   property panel in the same namespace as the current use case.</p>
     *
     * <p>This code uses getFactory and adds the use case explicitly to the
     *   namespace. Extended to actually navigate to the new use case.</p>
     */

    public void newUseCase() {
        Object target = getTarget();

        if(target instanceof MUseCase) {
            MNamespace ns = ((MUseCase) target).getNamespace();

            if(ns != null) {
                MUseCase useCase = UseCasesFactory.getFactory().createUseCase();

                ns.addOwnedElement(useCase);
                ProjectBrowser.TheInstance.setTarget(useCase);
            }
        }
    }


    /**
     * <p>Invoked by the "Add extension point" toolbar button to create a new
     *   extension point for this use case in the same namespace as the current
     *   use case.</p>
     *
     * <p>This code uses getFactory and adds the extension point explicitly to
     *   the, making its associated use case the current use case.</p>
     */

    public void newExtensionPoint() {
        Object target = getTarget();

        if(target instanceof MUseCase) {
            MUseCase   useCase = (MUseCase) target;
            MNamespace ns      = useCase.getNamespace();

            MExtensionPoint extensionPoint =
                    UseCasesFactory.getFactory().buildExtensionPoint(useCase);
            ProjectBrowser.TheInstance.setTarget(extensionPoint);
            
        }
    }


    
    
   


} /* end class PropPanelUseCase */
