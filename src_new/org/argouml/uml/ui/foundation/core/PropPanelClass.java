// Copyright (c) 1996-2001 The Regents of the University of California. All
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

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" and "Specializes:" for inheritance.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Labels corrected to
// "Generalizations:" and "Specializations".


package org.argouml.uml.ui.foundation.core;


import java.awt.*;
import java.util.Vector;

import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.argouml.application.api.*;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.*;
import org.argouml.util.ConfigLoader;

public class PropPanelClass extends PropPanelClassifier {


  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelClass() {
    super("Class",_classIcon, ConfigLoader.getTabPropsOrientation());

    Class mclass = MClass.class;

    //
    //   this will cause the components on this page to be notified
    //      anytime a stereotype, namespace, operation, etc
    //      has its name changed or is removed anywhere in the model
    Class[] namesToWatch = { MStereotype.class,MNamespace.class,MOperation.class,
        MParameter.class,MAttribute.class,MAssociation.class,MClassifier.class };
    setNameEventListening(namesToWatch);
    
    addField(Argo.localize("UMLMenu", "label.name"), nameField);
    addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
    addField(Argo.localize("UMLMenu", "label.namespace"),namespaceScroll);
    _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.active-uc"),this,new UMLReflectionBooleanProperty("isActive",mclass,"isActive","setActive")));
    addField(Argo.localize("UMLMenu", "label.modifiers"), _modifiersPanel);
    
    add(LabelledLayout.getSeperator());
    
    addField(Argo.localize("UMLMenu", "label.implements"), implementsScroll);
    addField(Argo.localize("UMLMenu", "label.generalizations"), extendsScroll);
    addField(Argo.localize("UMLMenu", "label.specializations"), derivedScroll);
    
    add(LabelledLayout.getSeperator());
     
    addField(Argo.localize("UMLMenu", "label.associations"), connectScroll);
    addField(Argo.localize("UMLMenu", "label.operations"), opsScroll);
    addField(Argo.localize("UMLMenu", "label.attributes"), attrScroll);
    addField(Argo.localize("UMLMenu", "label.owned-elements"),innerScroll);
    
    

	/*
    addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    addField(nameField,1,0,0);

    addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
    addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),2,0,0);

    addCaption(Argo.localize("UMLMenu", "label.namespace"),3,0,0);
    addLinkField(namespaceScroll,3,0,0);

    addCaption("Generalizations:",4,0,0);
    addField(extendsScroll,4,0,0);

    addCaption(Argo.localize("UMLMenu", "label.modifiers"),5,0,1);
    _modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.active"),this,new UMLReflectionBooleanProperty("isActive",mclass,"isActive","setActive")));
    addField(_modifiersPanel,5,0,0);

    addCaption(Argo.localize("UMLMenu", "label.associations"),0,1,0);
    addField(connectScroll,0,1,0.5);

    addCaption(Argo.localize("UMLMenu", "label.implements"),1,1,0);
    addField(implementsScroll,1,1,0);
   

    addCaption("Specializations:",2,1,0);
    addField(derivedScroll,2,1,0);

    addCaption(Argo.localize("UMLMenu", "label.operations"),0,2,0);
    addField(opsScroll,0,2,0);

    addCaption(Argo.localize("UMLMenu", "label.attributes"),1,2,0);
    addField(attrScroll,1,2,0);

    addCaption(Argo.localize("UMLMenu", "label.owned-elements"),2,2,0);
    addField(innerScroll,2,2,0);
	*/
    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_addAttrIcon, Argo.localize("UMLMenu", "button.add-attribute"),"addAttribute",null);
    new PropPanelButton(this,buttonPanel,_addOpIcon, Argo.localize("UMLMenu", "button.add-operation"),"addOperation",null);
    new PropPanelButton(this,buttonPanel,_innerClassIcon, Argo.localize("UMLMenu", "button.add-inner-class"),"addInnerClass",null);
    new PropPanelButton(this,buttonPanel,_classIcon, Argo.localize("UMLMenu", "button.add-new-class"),"newClass",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-class"),"removeElement",null);

  }

    public void addInnerClass() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MClass inner = CoreFactory.getFactory().buildClass();
            classifier.addOwnedElement(inner);
            navigateTo(inner);
        }
        // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
    }

    public void newClass() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MNamespace ns = classifier.getNamespace();
            if(ns != null) {
                MClassifier peer = classifier.getFactory().createClass();
                ns.addOwnedElement(peer);
                navigateTo(peer);
            }
        }
        // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Class") ||
            baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement");
    }


    /**
     * @see org.argouml.model.uml.foundation.core.CoreHelper#getAllDataClasses()
     */
	protected Vector getGeneralizationChoices() {
		Vector choices = new Vector();
		choices.addAll(CoreHelper.getHelper().getAllClasses());
		return choices;
	}

} /* end class PropPanelClass */
