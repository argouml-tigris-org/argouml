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

// File: PropPanelActor.java
// Classes: PropPanelActor
// Original Author: jrobbins@ics.uci.edu
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
import org.argouml.uml.ui.foundation.core.*;

public class PropPanelActor extends PropPanelClassifier {


  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelActor() {
    super("Actor Properties",2);

    Class mclass = MActor.class;
    
    addCaption(new JLabel("Name:"),0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

    
    addCaption(new JLabel("Stereotype:"),1,0,0);
    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
    addField(stereotypeBox,1,0,0);
    
    addCaption(new JLabel("Extends:"),2,0,0);

    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
    addLinkField(extendsList,2,0,0);
    
    addCaption(new JLabel("Modifiers:"),3,0,0);

    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox("Abstract",this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox("Final",this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox("Root",this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,3,0,0);

    addCaption(new JLabel("Namespace:"),4,0,0);
    JList namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
    addLinkField(namespaceList,4,0,0);
    
    addCaption(new JLabel("Derived:"),5,0,1);
    JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
    //derivedList.setBackground(getBackground());
    derivedList.setForeground(Color.blue);    
    derivedList.setVisibleRowCount(1);
    addField(new JScrollPane(derivedList),5,0,1);
    
    addCaption(new JLabel("Associations:"),0,1,0.5);
    JList connectList = new UMLList(new UMLConnectionListModel(this,null,true),true);
    connectList.setForeground(Color.blue);
    connectList.setVisibleRowCount(1);
    addField(new JScrollPane(connectList),0,1,0.5);
    
    addCaption(new JLabel("Implements:"),1,1,0.5);
    JList implementsList = new UMLList(new UMLClientDependencyListModel(this,null,true),true);
    implementsList.setBackground(getBackground());
    implementsList.setForeground(Color.blue);    
    addField(implementsList,1,1,0.5);
    
    JPanel buttonBorder = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new GridLayout(0,2));
    buttonBorder.add(buttonPanel,BorderLayout.NORTH);
    add(buttonBorder,BorderLayout.EAST);
    
    new PropPanelButton(this,buttonPanel,_addOpIcon,"Add operation","addOperation",null);
    new PropPanelButton(this,buttonPanel,_navUpIcon,"Go up","navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_addAttrIcon,"Add attribute","addAttribute",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,"Go back","navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_addAssocIcon,"Add association","addAssociation",null);
    new PropPanelButton(this,buttonPanel,_navForwardIcon,"Go forward","navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_generalizationIcon,"Add generalization","addGeneralization",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon,"Delete actor","removeElement",null);
    new PropPanelButton(this,buttonPanel,_realizationIcon,"Add realization","addRealization",null);
    new PropPanelButton(this,buttonPanel,_classIcon,"New actor","newActor",null);
    

    
    
  }


    public void newActor() {
        Object target = getTarget();
        if(target instanceof MActor) {
            MNamespace ns = ((MActor) target).getNamespace();
            if(ns != null) {
                MActor actor = ns.getFactory().createActor();
                ns.addOwnedElement(actor);
            }
        }
    }

} /* end class PropActor */
