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




package org.argouml.uml.ui.model_management;

import org.argouml.uml.ui.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.*;
import org.argouml.uml.ui.foundation.core.*;

import org.tigris.gef.util.Util;

public class PropPanelPackage extends PropPanelNamespace {

  private static ImageIcon _classIcon = Util.loadIconResource("Class");
  private static ImageIcon _interfaceIcon = Util.loadIconResource("Interface");
  private static ImageIcon _dataTypeIcon = Util.loadIconResource("DataType");
  private static ImageIcon _packageIcon = Util.loadIconResource("Package");
  private static ImageIcon _stereotypeIcon = Util.loadIconResource("Stereotype");
//  private static ImageIcon _generalizationIcon = Util.loadIconResource("Generalization");
//  private static ImageIcon _realizationIcon = Util.loadIconResource("Realization");


    protected PropPanelButton _stereotypeButton;

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelPackage() {
    super("Package Properties",2);

    Class mclass = MPackage.class;
    
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

    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox("abstract",this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox("final",this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox("root",this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,4,0,0);

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
    JScrollPane derivedScroll = new JScrollPane(derivedList);
    addField(derivedScroll,6,0,1);
    
    double ygrowth = 0.2;
    addCaption(new JLabel("Packages:"),0,1,ygrowth);
    JList packList = new UMLList(new UMLPackagesListModel(this,"ownedElement",true),true);
    packList.setForeground(Color.blue);
    packList.setVisibleRowCount(1);
    addField(new JScrollPane(packList),0,1,ygrowth);

    addCaption(new JLabel("Classifiers:"),1,1,ygrowth);
    JList classList = new UMLList(new UMLClassifiersListModel(this,"ownedElement",true),true);
    classList.setForeground(Color.blue);
    classList.setVisibleRowCount(1);
    addField(new JScrollPane(classList),1,1,ygrowth);

    addCaption(new JLabel("Associations:"),2,1,ygrowth);
    JList assocList = new UMLList(new UMLAssociationsListModel(this,"ownedElement",true),true);
    assocList.setForeground(Color.blue);
    assocList.setVisibleRowCount(1);
    addField(new JScrollPane(assocList),2,1,ygrowth);

    addCaption(new JLabel("Generalizations:"),3,1,ygrowth);
    JList genList = new UMLList(new UMLGeneralizationsListModel(this,"ownedElement",true),true);
    genList.setForeground(Color.blue);
    genList.setVisibleRowCount(1);
    addField(new JScrollPane(genList),3,1,ygrowth);
    
    addCaption(new JLabel("Stereotypes:"),4,1,ygrowth);
    JList stereoList = new UMLList(new UMLStereotypesListModel(this,"ownedElement",true),true);
    stereoList.setForeground(Color.blue);
    stereoList.setVisibleRowCount(1);
    addField(new JScrollPane(stereoList),4,1,ygrowth);

    JPanel buttonBorder = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new GridLayout(0,2));
    buttonBorder.add(buttonPanel,BorderLayout.NORTH);
    add(buttonBorder,BorderLayout.EAST);
    
    new PropPanelButton(this,buttonPanel,_classIcon,"Add class","addClass",null);
    new PropPanelButton(this,buttonPanel,_navUpIcon,"Go up","navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_interfaceIcon,"Add interface","addInterface",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,"Go back","navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_dataTypeIcon,"Add datatype","addDataType",null);
    new PropPanelButton(this,buttonPanel,_navForwardIcon,"Go forward","navigateForwardAction","isNavigateForwardEnabled");
    _stereotypeButton = new PropPanelButton(this,buttonPanel,_stereotypeIcon,"Add stereotype","addStereotype",null);
    _stereotypeButton.setEnabled(false);
    
    new PropPanelButton(this,buttonPanel,_deleteIcon,"Delete package","removeElement",null);
    new PropPanelButton(this,buttonPanel,_packageIcon,"Add subpackage","addPackage",null);
//    new PropPanelButton(this,buttonPanel,_generalizationIcon,"Add generalization","addGeneralization",null);
//    new PropPanelButton(this,buttonPanel,_realizationIcon,"Add realization","addRealization",null);
    
    
  }

    public void addStereotype() {
        Object target = getTarget();
        if(target instanceof MPackage) {
            MPackage pkg = (MPackage) target;
            MStereotype stereo = pkg.getFactory().createStereotype();
            pkg.addOwnedElement(stereo);
            navigateTo(stereo);
        }
    }
    
  
} /* end class PropPanelPackage */
