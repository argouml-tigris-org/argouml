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

package org.argouml.uml.ui.foundation.core;


import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import org.argouml.uml.ui.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

import org.tigris.gef.util.Util;

public class PropPanelClass extends PropPanelClassifier {


  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelClass() {
    super("Class Properties",2);

    Class mclass = MClass.class;
    Font smallFont = MetalLookAndFeel.getSubTextFont();


    //
    //   this will cause the components on this page to be notified
    //      anytime a stereotype, namespace, operation, etc
    //      has its name changed or is removed anywhere in the model
    Class[] namesToWatch = { MStereotype.class,MNamespace.class,MOperation.class,
        MParameter.class,MAttribute.class,MAssociation.class,MClassifier.class };
    setNameEventListening(namesToWatch);

    addCaption("Name:",0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);


    addCaption("Stereotype:",1,0,0);
    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
    addField(new UMLComboBoxNavigator(this,"NavStereo",stereotypeBox),1,0,0);

    addCaption("Extends:",2,0,0);

    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
    extendsList.setBackground(getBackground());
    extendsList.setForeground(Color.blue);
    extendsList.setFont(smallFont);
    addField(extendsList,2,0,0);

    addCaption("Implements:",3,0,0);
    JList implementsList = new UMLList(new UMLClientDependencyListModel(this,null,true),true);
    implementsList.setBackground(getBackground());
    implementsList.setForeground(Color.blue);
    addField(implementsList,3,0,0);

    addCaption("Modifiers:",4,0,0);

    _modifiersPanel.add(new UMLCheckBox(localize("active"),this,new UMLReflectionBooleanProperty("isActive",mclass,"isActive","setActive")));
    addField(_modifiersPanel,4,0,0);

    addCaption("Namespace:",5,0,0);
    addLinkField(new UMLList(new UMLNamespaceListModel(this),true),5,0,0);

    addCaption("Derived:",6,0,1);
    JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
    //derivedList.setBackground(getBackground());
    derivedList.setForeground(Color.blue);
    derivedList.setVisibleRowCount(1);
    JScrollPane derivedScroll = new JScrollPane(derivedList);
    addField(derivedScroll,6,0,1);

    addCaption("Operations:",0,1,0.25);
    JList opsList = new UMLList(new UMLOperationsListModel(this,"feature",true),true);
    opsList.setForeground(Color.blue);
    opsList.setVisibleRowCount(1);
    opsList.setFont(smallFont);
    JScrollPane opsScroll = new JScrollPane(opsList);
    addField(opsScroll,0,1,0.25);

    addCaption("Attributes:",1,1,0.25);
    JList attrList = new UMLList(new UMLAttributesListModel(this,"feature",true),true);
    attrList.setForeground(Color.blue);
    attrList.setVisibleRowCount(1);
    attrList.setFont(smallFont);
    JScrollPane attrScroll= new JScrollPane(attrList);
    addField(attrScroll,1,1,0.25);

    addCaption("Associations:",2,1,0.25);
    JList connectList = new UMLList(new UMLConnectionListModel(this,null,true),true);
    connectList.setForeground(Color.blue);
    connectList.setVisibleRowCount(1);
    connectList.setFont(smallFont);
    addField(new JScrollPane(connectList),2,1,0.25);



    addCaption("Owned Elements:",3,1,0.25);
    JList innerList = new UMLList(new UMLClassifiersListModel(this,"ownedElement",true),true);
    innerList.setForeground(Color.blue);
    innerList.setVisibleRowCount(1);
    innerList.setFont(smallFont);
    addField(new JScrollPane(innerList),3,1,0.25);

    JPanel buttonBorder = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new GridLayout(0,2));
    buttonBorder.add(buttonPanel,BorderLayout.NORTH);
    add(buttonBorder,BorderLayout.EAST);

    new PropPanelButton(this,buttonPanel,_addOpIcon,localize("Add operation"),"addOperation",null);
    new PropPanelButton(this,buttonPanel,_navUpIcon,localize("Go up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_addAttrIcon,localize("Add attribute"),"addAttribute",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,localize("Go back"),localize("navigateBackAction"),"isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_addAssocIcon,localize("Add association"),"addAssociation",null);
    new PropPanelButton(this,buttonPanel,_navForwardIcon,localize("Go forward"),localize("navigateForwardAction"),"isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_generalizationIcon,localize("Add generalization"),"addGeneralization",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete class"),"removeElement",null);
    new PropPanelButton(this,buttonPanel,_realizationIcon,localize("Add realization"),"addRealization",null);
    new PropPanelButton(this,buttonPanel,_classIcon,localize("New class"),"newClass",null);
    new PropPanelButton(this,buttonPanel,_innerClassIcon,localize("Add inner class"),"addInnerClass",null);

  }

    public void addInnerClass() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MClassifier inner = classifier.getFactory().createClass();
            classifier.addOwnedElement(inner);
            navigateTo(inner);
        }
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
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Class") ||
            baseClass.equals("Classifier") ||
            baseClass.equals("GeneralizableElement");
    }


} /* end class PropPanelClass */
