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


package org.argouml.uml.ui.foundation.extension_mechanisms;

import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import javax.swing.*;
import org.argouml.uml.ui.*;
import java.awt.*;
import java.awt.event.*;
import org.argouml.uml.ui.foundation.core.*;
import ru.novosoft.uml.foundation.core.*;


import org.tigris.gef.util.Util;

public class PropPanelStereotype extends PropPanelModelElement 
    implements ItemListener {

  private static ImageIcon _stereotypeIcon = Util.loadIconResource("Stereotype");

  private String[] _modelElements =
    { "ModelElement", "Classifier", "Class", "Interface", "DataType", "Exception", "Signal",
            "Association", "AssociationEnd", "Attribute", "Operation", "Generalization", "Flow", "Usage", "BehavioralFeature",
        "CallEvent", "Abstraction", "Component", "Package", "Constraint", "Comment","ObjectFlowState",
        "Model", "Subsystem", "Collaboration" };    
    
  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelStereotype() {
    super("Class Properties",2);

    Class mclass = MStereotype.class;
    
    addCaption(new JLabel("Name:"),0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

    //
    //   TODO TODO
    //      Quick hack that doesn't not monitor changes to the base
    //      class field
    //
    addCaption(new JLabel("Base Class:"),1,0,0);
    JComboBox baseClass = new JComboBox(_modelElements);
    baseClass.addItemListener(this);
    addField(baseClass,1,0,0);
    
    Object target = getTarget();
    int base = 0;
    if(target instanceof MStereotype) {
        String baseStr = ((MStereotype) target).getBaseClass();
        if(baseStr != null) {
            for(int i = 0; i < _modelElements.length; i++) {
                if(baseStr.equals(_modelElements[i])) {
                    base = i;
                    break;
                }
            }
        }
    }
    baseClass.setSelectedIndex(base);
             
    
    addCaption(new JLabel("Extends:"),2,0,0);

    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
    extendsList.setBackground(getBackground());
    extendsList.setForeground(Color.blue);
    addField(extendsList,2,0,0);
    
    addCaption(new JLabel("Modifiers:"),3,0,0);

    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox("abstract",this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox("final",this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox("root",this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,3,0,0);

    addCaption(new JLabel("Namespace:"),4,0,1);
    JList namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
    namespaceList.setBackground(getBackground());
    namespaceList.setForeground(Color.blue);
    addField(namespaceList,4,0,0);
    
    addCaption(new JLabel("Derived:"),0,1,1);
    JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
    derivedList.setForeground(Color.blue);    
    derivedList.setVisibleRowCount(1);
    addField(new JScrollPane(derivedList),0,1,1);
    
    JPanel buttonBorder = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new GridLayout(0,2));
    buttonBorder.add(buttonPanel,BorderLayout.NORTH);
    add(buttonBorder,BorderLayout.EAST);
    
    new PropPanelButton(this,buttonPanel,_stereotypeIcon,"New stereotype","newStereotype",null);
    new PropPanelButton(this,buttonPanel,_navUpIcon,"Go up","navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon,"Delete package","removeElement",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,"Go back","navigateBackAction","isNavigateBackEnabled");
    buttonPanel.add(new JPanel());
    new PropPanelButton(this,buttonPanel,_navForwardIcon,"Go forward","navigateForwardAction","isNavigateForwardEnabled");
    
  }

  public void itemStateChanged(ItemEvent event) {
    if(event.getStateChange() == ItemEvent.SELECTED) {
        Object target = getTarget();
        Object selected = event.getItem();
        if(target instanceof MStereotype && selected instanceof String) {
            ((MStereotype) target).setBaseClass((String) selected);
        }
    }
  }

    public void newStereotype() {
        Object target = getTarget();
        if(target instanceof MStereotype) {
            MNamespace ns = ((MStereotype) target).getNamespace();
            if(ns != null) {
                MStereotype newStereo = ns.getFactory().createStereotype();
                ns.addOwnedElement(newStereo);
                navigateTo(newStereo);
            }
        }
    }
                
} /* end class PropPanelStereotype */
