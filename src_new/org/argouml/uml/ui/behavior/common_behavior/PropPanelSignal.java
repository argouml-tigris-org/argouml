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



package org.argouml.uml.ui.behavior.common_behavior;
import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import org.argouml.uml.ui.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import java.awt.event.*;
import java.util.*;
import org.argouml.uml.ui.foundation.core.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;


public class PropPanelSignal extends PropPanelClassifier {

    public PropPanelSignal() {
        super("Signal Properties",2);

        Class mclass = MSignal.class;

        addCaption(new JLabel("Name:"),0,0,0);
        addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);


        addCaption(new JLabel("Stereotype:"),1,0,0);
        JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
        addField(stereotypeBox,1,0,0);

        addCaption(new JLabel("Extends:"),2,0,0);

        JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
        extendsList.setBackground(getBackground());
        extendsList.setForeground(Color.blue);
        addField(extendsList,2,0,0);

        addCaption(new JLabel("Implements:"),3,0,0);
        JList implementsList = new UMLList(new UMLClientDependencyListModel(this,null,true),true);
        implementsList.setBackground(getBackground());
        implementsList.setForeground(Color.blue);    
        addField(implementsList,3,0,0);

        addCaption(new JLabel("Modifiers:"),4,0,0);

        addField(_modifiersPanel,4,0,0);

        addCaption(new JLabel("Namespace:"),5,0,0);
        JList namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
        namespaceList.setBackground(getBackground());
        namespaceList.setForeground(Color.blue);
        addField(namespaceList,5,0,0);

        addCaption(new JLabel("Derived:"),6,0,1);
        JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
        //derivedList.setBackground(getBackground());
        derivedList.setForeground(Color.blue);    
        derivedList.setVisibleRowCount(1);
        JScrollPane derivedScroll = new JScrollPane(derivedList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addField(derivedScroll,6,0,1);

        addCaption(new JLabel("Operations:"),0,1,0.33);
        JList opsList = new UMLList(new UMLOperationsListModel(this,"feature",true),true);
        opsList.setForeground(Color.blue);
        opsList.setVisibleRowCount(1);
        JScrollPane opsScroll = new JScrollPane(opsList);
        addField(opsScroll,0,1,0.33);

        addCaption(new JLabel("Attributes:"),1,1,0.33);
        JList attrList = new UMLList(new UMLAttributesListModel(this,"feature",true),true);
        attrList.setForeground(Color.blue);
        attrList.setVisibleRowCount(1);
        JScrollPane attrScroll= new JScrollPane(attrList);
        addField(attrScroll,1,1,0.33);

        addCaption(new JLabel("Associations:"),2,1,0.33);
        JList connectList = new UMLList(new UMLConnectionListModel(this,null,true),true);
        connectList.setForeground(Color.blue);
        connectList.setVisibleRowCount(1);
        addField(new JScrollPane(connectList),2,1,0.33);


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
    new PropPanelButton(this,buttonPanel,_classIcon,"New signal","newSignal",null);


        
    }
    
    public void newSignal() {
        Object target = getTarget();
        if(target instanceof MSignal) {
            MNamespace ns = ((MSignal) target).getNamespace();
            if(ns != null) {
                MSignal newSig = ns.getFactory().createSignal();
                ns.addOwnedElement(newSig);
                navigateTo(newSig);
            }
        }
    }

} /* end class PropPanelSignal */

