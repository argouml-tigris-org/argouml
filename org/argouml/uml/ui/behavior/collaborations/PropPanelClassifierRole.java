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

// File: PropPanelClassifierRole.java
// Classes: PropPanelClassifierRole
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.argouml.uml.ui.behavior.collaborations;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.behavior.collaborations.*;

import org.argouml.application.api.*;
import org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.PropPanelClassifier;
import org.argouml.util.ConfigLoader;


public class PropPanelClassifierRole extends PropPanelClassifier {


  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelClassifierRole() {
    super("ClassifierRole",_classifierRoleIcon, ConfigLoader.getTabPropsOrientation());

    Class mclass = MClassifierRole.class;
    
     Class[] namesToWatch = { MStereotype.class,MNamespace.class,MClassifier.class};
    setNameEventListening(namesToWatch);
    
    addField(Argo.localize("UMLMenu", "label.name"), nameField);
    addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
    addField(Argo.localize("UMLMenu", "label.namespace"),namespaceComboBox);
    
    addField(Argo.localize("UMLMenu", "label.multiplicity"),new UMLMultiplicityComboBox(this,mclass));
    
    JList baseList = new UMLMutableLinkedList(this, new UMLClassifierRoleBaseListModel(this), ActionAddClassifierRoleBase.SINGLETON, null);
    addField(Argo.localize("UMLMenu", "label.base"), new JScrollPane(baseList));
   

    add(LabelledLayout.getSeperator());
	
    addField(Argo.localize("UMLMenu", "label.generalizations"), extendsScroll);
    addField(Argo.localize("UMLMenu", "label.specializations"), derivedScroll);	
	
    JList connectList = new UMLList(new UMLClassifierRoleAssociationRoleListModel(this,null,true),true);
    addField(Argo.localize("UMLMenu", "label.association-roles"), 
        new JScrollPane(connectList));
    
    add(LabelledLayout.getSeperator());
     
    JList availableContentsList = new UMLLinkedList(this, new UMLClassifierRoleAvailableContentsListModel(this));
    addField(Argo.localize("UMLMenu", "label.available-contents"), 
        new JScrollPane(availableContentsList));   
        
    JList availableFeaturesList = new UMLLinkedList(this, new UMLClassifierRoleAvailableFeaturesListModel(this));
    addField(Argo.localize("UMLMenu", "label.available-features"), 
        new JScrollPane(availableFeaturesList));   
    /* 
    JList attributesList = new UMLList(new UMLAttributesClassifierRoleListModel(this, "attributes", true), true);
    addField(Argo.localize("UMLMenu", "label.attributes"), 
        new JScrollPane(attributesList));
    */
    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);
  }


    public boolean isAcceptibleBase(MModelElement classifier) {
        return classifier instanceof MClassifier && !(classifier instanceof MClassifierRole);
    }

     public MClassifier getClassifier() {
        MClassifier classifier = null;
        Object target = getTarget();
        if(target instanceof MClassifierRole) {
	    //    UML 1.3 apparently has this a 0..n multiplicity
	    Collection bases=((MClassifierRole)target).getBases();
	    Iterator it=bases.iterator();
	    if (it.hasNext())
		classifier=(MClassifier)it.next();
        }
        return classifier;
    }

    public void setClassifier(MClassifier element) {
	MClassifier classifier = null;
        Object target = getTarget();
        if(target instanceof MClassifierRole) {
	    Vector bases=new Vector();
	    bases.addElement(element);
	    ((MClassifierRole)target).setBases(bases);	    
	}
    }


    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return (baseClass.equals("ClassifierRole") ||
                baseClass.equals("Namespace") ||
                baseClass.equals("GeneralizableElement") ||
                baseClass.equals("Classifier"));
    }
    
    /**
     * @see org.argouml.model.uml.behavioralelements.collaborations.CollaborationsHelper#getAllClassifierRoles()
     */
	protected Vector getGeneralizationChoices() {
		Vector choices = new Vector();
		choices.addAll(CollaborationsHelper.getHelper().getAllClassifierRoles());
		return choices;
	}
	
	public MClassifier getBase() {
		if (getTarget() != null) {
			Vector list = new Vector();
			list.addAll(((MClassifierRole)getTarget()).getBases());
			if (list.size() > 0) {
				return (MClassifier)list.get(0);
			}
		}
		return null;
	}
	
	public void setBase(MClassifier base) {
		if (getTarget() != null) {
			Vector list = new Vector();
			list.add(base);
			((MClassifierRole)getTarget()).setBases(list);
		}
	}


} /* end class PropPanelClassifierRole */

