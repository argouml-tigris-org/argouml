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

package org.argouml.uml.ui.behavior.use_cases;

import org.argouml.uml.ui.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.model_management.*;



public class PropPanelUseCase extends PropPanel {


  public PropPanelUseCase() {
    super("UseCase Properties",2);

    Class mclass = MUseCase.class;
    
    addCaption(new JLabel("Name:"),0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

    
    addCaption(new JLabel("Stereotype:"),1,0,0);
    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
    addField(stereotypeBox,1,0,0);
    
    addCaption(new JLabel("Modifiers:"),2,0,0);

    JPanel modifiersPanel = new JPanel(new GridLayout(0,2));
    modifiersPanel.add(new UMLCheckBox("Abstract",this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox("Final",this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox("Root",this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,2,0,0);

    
    //
    //  Generalization was labeled "Extends" in PropPanelClass and others 
    //     but since extension has a specific meaning in use cases a
    //     different term had to be used
    //
    addCaption(new JLabel("Generalizations:"),3,0,0);
    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
    extendsList.setBackground(getBackground());
    extendsList.setForeground(Color.blue);
    addField(extendsList,3,0,0);
    
    addCaption(new JLabel("Includes:"),4,0,0);
    JList includeList = new UMLList(new UMLIncludeListModel(this,"include",true),true);
    includeList.setBackground(getBackground());
    includeList.setForeground(Color.blue);
    addField(includeList,4,0,0);
    

    addCaption(new JLabel("Extends:"),5,0,0);
    JList extendList = new UMLList(new UMLExtendListModel(this,"extend",true),true);
    extendList.setBackground(getBackground());
    extendList.setForeground(Color.blue);
    addField(extendList,5,0,0);
    
    addCaption(new JLabel("Namespace:"),6,0,1);
    JList namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
    namespaceList.setBackground(getBackground());
    namespaceList.setForeground(Color.blue);
    addField(namespaceList,6,0,0);
    
    
    
    addCaption(new JLabel("Associations:"),0,1,0.25);
    JList connectList = new UMLList(new UMLConnectionListModel(this,null,true),true);
    connectList.setForeground(Color.blue);
    connectList.setVisibleRowCount(1);
    addField(new JScrollPane(connectList),0,1,0.25);
    

    
    addCaption(new JLabel("Operations:"),1,1,0.25);
    JList opsList = new UMLList(new UMLOperationsListModel(this,"feature",true),true);
    opsList.setForeground(Color.blue);
    opsList.setVisibleRowCount(1);
    JScrollPane opsScroll = new JScrollPane(opsList);
    addField(opsScroll,1,1,0.25);
    
    addCaption(new JLabel("Attributes:"),2,1,0.25);
    JList attrList = new UMLList(new UMLAttributesListModel(this,"feature",true),true);
    attrList.setForeground(Color.blue);
    attrList.setVisibleRowCount(1);
    JScrollPane attrScroll= new JScrollPane(attrList);
    addField(attrScroll,2,1,0.25);
    
    
    
    addCaption(new JLabel("Extension Points:"),3,1,0.25);
    JList extensionPoints = new UMLList(new UMLExtensionPointListModel(this,null,true),true);
    extensionPoints.setForeground(Color.blue);
    extensionPoints.setVisibleRowCount(1);
    addField(new JScrollPane(extensionPoints),3,1,0.25);
    
  }
  
} /* end class PropPanelUseCase */
