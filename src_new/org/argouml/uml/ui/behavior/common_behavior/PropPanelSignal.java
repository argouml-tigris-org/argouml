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



// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" and "Specializes:" for inheritance.


package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import org.argouml.application.api.*;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.model_management.*;

public class PropPanelSignal extends PropPanelClassifier {


    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelSignal() {
        super("Signal", _signalIcon,2);

        Class mclass = MSignal.class;

        addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
        addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),1,0,0);


        addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,0);
        addField(stereotypeBox,2,0,0);

        addCaption("Specializes:",3,0,0);
	addField(extendsScroll,3,0,0);

        addCaption(Argo.localize("UMLMenu", "label.implements"),4,0,0);
        addField(implementsScroll,4,0,0);

        addCaption(Argo.localize("UMLMenu", "label.modifiers"),5,0,0);
        addField(_modifiersPanel,5,0,0);

        addCaption(Argo.localize("UMLMenu", "label.namespace"),6,0,0);
        addField(namespaceScroll,6,0,0);

        addCaption("Generalizes:",7,0,1);
        addField(derivedScroll,7,0,1);

        addCaption(Argo.localize("UMLMenu", "label.operations"),0,1,0.33);
        addField(opsScroll,0,1,0.33);

        addCaption(Argo.localize("UMLMenu", "label.attributes"),1,1,0.33);
        addField(attrScroll,1,1,0.33);

        addCaption(Argo.localize("UMLMenu", "label.associations"),2,1,0.33);
        addField(connectScroll,2,1,0.33);


	new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
	new PropPanelButton(this,buttonPanel,_addOpIcon, Argo.localize("UMLMenu", "button.add-operation"),"addOperation",null);
	new PropPanelButton(this,buttonPanel,_addAttrIcon, Argo.localize("UMLMenu", "button.add-attribute"),"addAttribute",null);
	//new PropPanelButton(this,buttonPanel,_addAssocIcon, Argo.localize("UMLMenu", "button.add-association"),"addAssociation",null);
	//new PropPanelButton(this,buttonPanel,_generalizationIcon, Argo.localize("UMLMenu", "button.add-generalization"),"addGeneralization",null);
	//new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-actor"),"removeElement",null);
	//new PropPanelButton(this,buttonPanel,_realizationIcon, Argo.localize("UMLMenu", "button.add-realization"),"addRealization",null);
	new PropPanelButton(this,buttonPanel,_classIcon, Argo.localize("UMLMenu", "button.add-signal"),"newSignal",null);


    }

    public void newSignal() {
        Object target = getTarget();
        if(target instanceof MSignal) {
            MNamespace ns = ((MSignal) target).getNamespace();
            if(ns != null) {
                MSignal newSig = ns.getFactory().createSignal();
                ns.addOwnedElement(newSig);
                navigateTo(newSig);
                // 2002-07-15
            	// Jaap Branderhorst
            	// Force an update of the navigation pane to solve issue 323
            	ProjectBrowser.TheInstance.getNavPane().forceUpdate();
            }
        }
    }


    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Signal") ||
            baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement") ||
            baseClass.equals("Namespace");
    }


} /* end class PropPanelSignal */

