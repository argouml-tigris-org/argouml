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

import org.argouml.application.api.*;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesFactory;
import org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;
import org.argouml.util.ConfigLoader;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;


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

        super("UseCase", _useCaseIcon, ConfigLoader.getTabPropsOrientation());
        
        addField(Argo.localize("UMLMenu", "label.name"), nameField);
    	addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
    	addField(Argo.localize("UMLMenu", "label.namespace"),namespaceScroll);
		
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
            	
    JList extensionPoints = new UMLMutableLinkedList(this, new UMLUseCaseExtensionPointListModel(this), null, ActionNewUseCaseExtensionPoint.SINGLETON);
    addField(Argo.localize("UMLMenu", "label.extensionpoints"), 
        new JScrollPane(extensionPoints));
		
    add(LabelledLayout.getSeperator());
		
    addField(Argo.localize("UMLMenu", "label.generalizations"), extendsScroll);
    addField(Argo.localize("UMLMenu", "label.specializations"), derivedScroll);
    	
    JList extendsList = new UMLLinkedList(this, new UMLUseCaseExtendListModel(this));
    addField(Argo.localize("UMLMenu", "label.extends"), 
        new JScrollPane(extendsList));
    
    JList includesList = new UMLLinkedList(this, new UMLUseCaseIncludeListModel(this));
    addField(Argo.localize("UMLMenu", "label.includes"), 
        new JScrollPane(includesList));

    add(LabelledLayout.getSeperator());
	
    /*	
    JList connectList = new UMLLinkedList(this, new UMLUseCaseAssociationListModel(this));
   
      	connectScroll= new JScrollPane(connectList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		addField(Argo.localize("UMLMenu", "label.associations"), connectScroll);
    	addField(Argo.localize("UMLMenu", "label.operations"), opsScroll);
   	 	addField(Argo.localize("UMLMenu", "label.attributes"), attrScroll);	
*/		
	
		/*
        // The first column. All single line entries, so we just let the label
        // at the bottom (modifiers) take the vertical weighting.

        // nameField, stereotypeBox and namespaceScroll are all set up by
        // PropPanelModelElement

        addCaption(Argo.localize("UMLMenu", "label.name"), 1, 0, 0);
        addField(nameField, 1, 0, 0);

        addCaption(Argo.localize("UMLMenu", "label.stereotype"), 2, 0, 0);
        addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),
                 2, 0, 0);

        addCaption(Argo.localize("UMLMenu", "label.namespace"), 3, 0, 0);
        addField(namespaceScroll, 3, 0, 0);

        // For modifiers we create a grid with two columns. We really ought to
        // inherit this from GeneralizableElement, but since Java can't do
        // multiple inheritance, it gets done here (it would at least be better
        // in PropPanelClassifier).

        PropPanelModifiers mPanel = new PropPanelModifiers(2);
        Class              mclass = MUseCase.class;

        mPanel.add("isAbstract", mclass, "isAbstract", "setAbstract",
                   Argo.localize("UMLMenu", "checkbox.abstract-lc"), this);
        mPanel.add("isLeaf", mclass, "isLeaf", "setLeaf",
                   Argo.localize("UMLMenu", "checkbox.final-lc"), this);
        mPanel.add("isRoot", mclass, "isRoot", "setRoot",
                   localize("root"), this);

        addCaption(Argo.localize("UMLMenu", "label.modifiers"), 4, 0, 1);
        addField(mPanel, 4, 0, 0);


        // The second column. These are all potentially multi-valued, so share
        // the vertical weighting.

        // Generalization and specialization are inherited from
        // PropPanelClassifier

        addCaption("Generalizations:", 0, 1, 1);
        addField(extendsScroll, 0, 1, 1);

        addCaption("Specializations:", 1, 1, 1);
        addField(derivedScroll, 1, 1, 1);

        // Build up a panel for extend relationships

        JList extendList =
            new UMLList(new UMLExtendListModel(this, "extend", true), true);

        extendList.setBackground(getBackground());
        extendList.setForeground(Color.blue);

        JScrollPane extendScroll =
            new JScrollPane(extendList,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addCaption(Argo.localize("UMLMenu", "label.extends"), 2, 1, 1);
        addField(extendScroll, 2, 1, 1);

        // Build up a panel for include relationships

        JList includeList =
            new UMLList(new UMLIncludeListModel(this, "include", true), true);

        includeList.setBackground(getBackground());
        includeList.setForeground(Color.blue);

        JScrollPane includeScroll =
            new JScrollPane(includeList,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        addCaption("Includes:", 3, 1, 1);
        addField(includeScroll, 3, 1, 1);

        // Build up a panel for extension points.

        JList extensionPoints =
            new UMLList(new UMLExtensionPointListModel(this, true, false),
                        true);

        extensionPoints.setForeground(Color.blue);
        extensionPoints.setVisibleRowCount(1);

        JScrollPane extensionPointsScroll =
            new JScrollPane(extensionPoints,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


        addCaption("Extension Points:",4,1,1);
        addField(extensionPointsScroll,4,1,1);

        // The third column

        // The details of assocations, operations and attributes are all
        // inherited from PropPanelClassifier. Note that these last two ARE
        // allowed for use cases by the UML 1.3 standard.

        addCaption(Argo.localize("UMLMenu", "label.associations"), 0, 2, 1);
        addField(connectScroll, 0, 2, 1);

        addCaption(Argo.localize("UMLMenu", "label.operations"), 1, 2, 1);
        addField(opsScroll, 1, 2, 1);

        addCaption(Argo.localize("UMLMenu", "label.attributes"), 2, 2, 1);
        addField(attrScroll, 2, 2, 1);

        // The toolbar buttons that go at the top.

		*/
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
                navigateTo(useCase);
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
            navigateTo(extensionPoint);
            
        }
    }


    /**
     * <p>A predicate to test if a given base class (below ModelElement) is
     *   appropriate to us.</p>
     *
     * <p>For us this means UseCase, Classifier, Namespace or
     *   GeneralizableElement.</p>
     *
     * @param baseClass  a string with the name of a UML MetaClass (no leading
     *                   M)
     *
     * @return           <code>true</code> if this is a suitable base class for
     *                   us. <code>false</code> otherwise.
     */

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {

        return baseClass.equals("UseCase") ||
            baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement") ||
            baseClass.equals("Namespace");
    }
    
    /**
     * @see org.argouml.model.uml.behavioralelements.usecases.UseCasesHelper#getAllUseCases()
     */
	protected Vector getGeneralizationChoices() {
		Vector choices = new Vector();
		choices.addAll(UseCasesHelper.getHelper().getAllUseCases());
		return choices;
	}


} /* end class PropPanelUseCase */
