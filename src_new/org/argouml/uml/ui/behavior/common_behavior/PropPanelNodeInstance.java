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

// File: PropPanelNodeInstance.java
// Classes: PropPanelNodeInstance
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;

public class PropPanelNodeInstance extends PropPanelModelElement {
    public PropPanelNodeInstance() {
        super("Node Instance", _nodeInstanceIcon,2);

	Class mclass = MNodeInstance.class;
	
	addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
	addField(nameField,1,0,0);

    addCaption("Classifier:",2,0,0);
    UMLClassifierComboBoxModel classifierModel = new UMLClassifierComboBoxModel(this,"isAcceptibleClassifier","classifier","getClassifier","setClassifier",false,MClassifier.class,true);
	UMLComboBox clsComboBox = new UMLComboBox(classifierModel);
   	addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-class"),clsComboBox),2,0,0);


	addCaption(Argo.localize("UMLMenu", "label.stereotype"),3,0,0);
	addField(stereotypeBox,3,0,0);


	addCaption(Argo.localize("UMLMenu", "label.namespace"),4,0,1);
	addField(namespaceScroll,4,0,0);


	new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu" , "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
	new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);


    }

    public boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("NodeInstance") ||
            baseClass.equals("Instance");
    }

    public boolean isAcceptibleClassifier(MModelElement classifier) {
        return classifier instanceof MClassifier;
    }


    public void setClassifier(MClassifier element) {
        Object target = getTarget();
	
        if(target instanceof MInstance) {
	    MInstance inst = (MInstance)target;

	    // delete all classifiers
	    Collection col = inst.getClassifiers();
	    if (col != null) {
		Iterator iter = col.iterator();
		if (iter != null && iter.hasNext()) {
		    MClassifier classifier = (MClassifier)iter.next();
		    inst.removeClassifier(classifier);
		}
	    }
	    // add classifier
	    inst.addClassifier( element);

        }
    }

    public MClassifier getClassifier() {
        MClassifier classifier = null;
        Object target = getTarget();
        if(target instanceof MInstance) {
            // at the moment , we only deal with one classifier
            Collection col = ((MInstance)target).getClassifiers();
            if (col != null) {
                Iterator iter = col.iterator();
                if (iter != null && iter.hasNext()) {
                    classifier = (MClassifier)iter.next();
                }
            }
		    
        }
        return classifier;
    }


}
