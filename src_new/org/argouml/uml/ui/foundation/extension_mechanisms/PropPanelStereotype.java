// $Id$
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLGeneralizationListModel;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLMetaclassComboBox;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLSpecializationListModel;
import org.argouml.uml.ui.UMLTextField2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLModelElementNameDocument;

import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

public class PropPanelStereotype extends PropPanelModelElement {

    /**
     * Construct new stereotype properties tab
     * @todo convert to use LabelledLayout(Bob Tarling)
     */
    public PropPanelStereotype() {
        super("Stereotype", _stereotypeIcon ,2);  // Change this to call labelled layout constructor

        Class mclass = MStereotype.class;

        addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
        addField(new UMLTextField2(new UMLModelElementNameDocument()),1,0,0);

        addCaption(Argo.localize("UMLMenu", "label.base-class"),2,0,0);
        JComboBox baseClass = new UMLMetaclassComboBox(this,"baseClass","getBaseClass","setBaseClass");
        addField(baseClass,2,0,0);

        addCaption(Argo.localize("UMLMenu", "label.namespace"),3,0,0);
        addField(getNamespaceComboBox(),3,0,0);

        addCaption(Argo.localize("UMLMenu", "label.modifiers"),4,0,1);
        JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
        modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-lc"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
        modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-lc"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
        modifiersPanel.add(new UMLCheckBox(localize("root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
        addField(modifiersPanel,4,0,0);

        addCaption("Generalizations:",0,1,1);
        JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
        extendsList.setBackground(getBackground());
        extendsList.setForeground(Color.blue);
        addField(new JScrollPane(extendsList),0,1,1);

        addCaption("Specializations:",1,1,1);
        JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
        derivedList.setForeground(Color.blue);
        derivedList.setVisibleRowCount(1);
        addField(new JScrollPane(derivedList),1,1,1);

        new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
        new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
        new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
        new PropPanelButton(this,buttonPanel,_stereotypeIcon, Argo.localize("UMLMenu", "button.add-new-stereotype"),"newStereotype",null);
        new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-package"),"removeElement",null);
    }


    public void newStereotype() {
        Object target = getTarget();
        MStereotype newStereo = ExtensionMechanismsFactory.getFactory().buildStereotype(null, null);
        navigateTo(newStereo);
        /*
        if(target instanceof MStereotype) {
            MNamespace ns = ((MStereotype) target).getNamespace();
            if(ns != null) {
                MStereotype newStereo = ExtensionMechanismsFactory.getFactory().createStereotype();
                ns.addOwnedElement(newStereo);
                navigateTo(newStereo);
            }
        }
        */
    }

    public String getBaseClass() {
      String baseClass = "ModelElement";
      Object target = getTarget();
      if(target instanceof MStereotype) {
        baseClass = ((MStereotype) target).getBaseClass();
      }
      return baseClass;
    }

    public void setBaseClass(String baseClass) {
      Object target = getTarget();
      if(target instanceof MStereotype) {
        ((MStereotype) target).setBaseClass(baseClass);
      }
    }

} /* end class PropPanelStereotype */
