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



// File: PropPanelAttribute.java
// Classes: PropPanelAttribute
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.ui.foundation.core;

import java.awt.*;
import javax.swing.*;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import sun.security.action.GetPropertyAction;

import org.argouml.application.api.*;
import org.argouml.kernel.Project;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.MMUtil;
import org.argouml.uml.ui.*;


public class PropPanelAttribute extends PropPanelModelElement {

    public PropPanelAttribute() {
        super("Attribute", _addAttrIcon, 2);

        Class mclass = MAttribute.class;

        //
        //   this will cause the components on this page to be notified
        //      anytime a stereotype, namespace, operation, etc
        //      has its name changed or is removed anywhere in the model
        Class[] namesToWatch = { MStereotype.class,MNamespace.class,MClassifier.class, MAttribute.class };
        setNameEventListening(namesToWatch);


        addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
        addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),1,0,0);

	addCaption(Argo.localize("UMLMenu", "label.multiplicity"),2,0,0);
        addField(new UMLMultiplicityComboBox(this,MAttribute.class),2,0,0);

        addCaption(Argo.localize("UMLMenu", "label.stereotype"),3,0,0);
        addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox),3,0,0);

        addCaption(Argo.localize("UMLMenu", "label.owner"),4,0,1);
        JList ownerList = new UMLList(new UMLReflectionListModel(this,"owner",false,"getOwner",null,null,null),true);
        JScrollPane ownerScroll=new JScrollPane(ownerList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	addLinkField(ownerScroll,4,0,0);

	addCaption(Argo.localize("UMLMenu", "label.type"),0,1,0);
        addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-class"),
            new UMLComboBox2(this, new UMLAttributeTypeComboBoxModel(this), ActionSetAttributeType.SINGLETON)),0,1,0);

        addCaption("Initial Value:",1,1,0);
        addField(new UMLInitialValueComboBox(this),1,1,0);

        addCaption(Argo.localize("UMLMenu", "label.visibility"),2,1,0);
        addField(new UMLVisibilityPanel(this,mclass,3,false),2,1,0);

        addCaption(Argo.localize("UMLMenu", "label.modifiers"),3,1,1);
        JPanel modPanel = new JPanel(new GridLayout(0,2));
        modPanel.add(new UMLCheckBox(localize("static"),this,new UMLEnumerationBooleanProperty("ownerscope",mclass,"getOwnerScope","setOwnerScope",MScopeKind.class,MScopeKind.CLASSIFIER,MScopeKind.INSTANCE)));
        modPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-lc"),this,new UMLEnumerationBooleanProperty("changeability",mclass,"getChangeability","setChangeability",MChangeableKind.class,MChangeableKind.FROZEN,MChangeableKind.CHANGEABLE)));
        modPanel.add(new UMLCheckBox(localize("transient"),this,new UMLTaggedBooleanProperty("transient")));
        modPanel.add(new UMLCheckBox(localize("volatile"),this,new UMLTaggedBooleanProperty("volatile")));
        addField(modPanel,3,1,0);

        new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
        new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
        new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
        new PropPanelButton(this,buttonPanel,_addAttrIcon, Argo.localize("UMLMenu", "button.add-attribute"),"newAttribute",null);
        new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-attribute"),"removeElement",null);
    }


    /**
     *    Gets the type of the current target.  This method is called
     *    by UMLClassifierComboBox which invokes methods on the container
     *    (not the target like most reflection models) so that PropPanelOperation
     *    make return type look like a Operation property.
     */
    public MClassifier getType() {
        MClassifier type = null;
        Object target = getTarget();
        if(target instanceof MAttribute) {
            type = ((MAttribute) target).getType();
        }
        return type;
    }

    public void setType(MClassifier type) {
        Object target = getTarget();
        if(target instanceof MAttribute) {
            ((MAttribute) target).setType(type);
        }
    }

    public boolean isAcceptibleType(MModelElement element) {
      return element instanceof MClassifier;
    }

    public Object getOwner() {
        Object owner = null;
        Object target = getTarget();
        if(target instanceof MAttribute) {
            owner = ((MAttribute) target).getOwner();
        }
        return owner;
    }

    public void newAttribute() {
        Object target = getTarget();
        if(target instanceof MAttribute) {
            MClassifier owner = ((MAttribute) target).getOwner();
            if(owner != null) {
		MAttribute attr = UmlFactory.getFactory().getCore().buildAttribute(owner);
                navigateTo(attr);
            }
        }
    }

    public void navigateUp() {
        Object target = getTarget();
        if(target instanceof MAttribute) {
            MClassifier owner = ((MAttribute) target).getOwner();
            if(owner != null) {
                navigateTo(owner);
            }
        }
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Attribute") ||
            baseClass.equals("StructuralFeature") ||
            baseClass.equals("Feature");
    }

    /**
     *   Appropriate namespace is the namespace of our class,
     *      not the class itself
     */
    protected MNamespace getDisplayNamespace() {
      MNamespace ns = null;
      Object target = getTarget();
      if(target instanceof MAttribute) {
        MAttribute attr = ((MAttribute) target);
        MClassifier owner = attr.getOwner();
        if(owner != null) {
          ns = owner.getNamespace();
        }
      }
      return ns;
    }
} /* end class PropPanelAttribute */

