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


// File: PropPanelNode.java
// Classes: PropPanelNode
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

// 21 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Changed to use the
// labels "Generalizes:" and "Specializes:" for inheritance.

// 4 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Labels corrected to
// "Generalizations:" and "Specializations".


package org.argouml.uml.ui.foundation.core;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Collection;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLCheckBox;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLReflectionBooleanProperty;
import org.argouml.uml.ui.UMLReflectionListModel;

import ru.novosoft.uml.foundation.core.MNode;

public class PropPanelNode extends PropPanelClassifier {

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelNode() {
    super("Node",_nodeIcon,2);

    Class mclass = MNode.class;

    addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    addField(getNameTextField(),1,0,0);

    addCaption("Generalizations:",2,0,0);
    addField(getGeneralizationScroll(),2,0,0);

    addCaption(Argo.localize("UMLMenu", "label.stereotype"),3,0,0);
    addField(new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),getStereotypeBox()),3,0,0);

    addCaption(Argo.localize("UMLMenu", "label.modifiers"),4,0,0);
    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.abstract-lc"),this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox(Argo.localize("UMLMenu", "checkbox.final-lc"),this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox(localize("root"),this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,4,0,0);

    addCaption(Argo.localize("UMLMenu", "label.namespace"),5,0,0);
   addField(getNamespaceComboBox(),5,0,0);

    addCaption("Specializations:",6,0,1);
    addField(getSpecializationScroll(),6,0,1);

    addCaption(Argo.localize("UMLMenu", "label.components"),0,1,1);
    JList compList = new UMLList(new UMLReflectionListModel(this,"component",true,"getResidents","setResidents",null,null),true);
    compList.setForeground(Color.blue);
    compList.setVisibleRowCount(1);
    addField(new JScrollPane(compList),0,1,1);

    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);   
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete node"),"removeElement",null);

  }

  public Collection getResidents() {
    Collection components = null;
    Object target = getTarget();
    if(target instanceof MNode) {
        components = ((MNode) target).getResidents();
    }
    return components;
  }

    public void setResidents(Collection components) {
        Object target = getTarget();
        if(target instanceof MNode) {
            ((MNode) target).setResidents(components);
        }
    }

    
    
    


} /* end class PropPanelNode */

