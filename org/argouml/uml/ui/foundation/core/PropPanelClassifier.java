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

package org.argouml.uml.ui.foundation.core;


import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import org.argouml.uml.ui.*;

import org.tigris.gef.util.Util;

abstract public class PropPanelClassifier extends PropPanelNamespace {

  protected static ImageIcon _classIcon = Util.loadIconResource("Class");
  protected static ImageIcon _interfaceIcon = Util.loadIconResource("Interface");
  protected static ImageIcon _addOpIcon = Util.loadIconResource("AddOperation");
  protected static ImageIcon _addAttrIcon = Util.loadIconResource("AddAttribute");
  protected static ImageIcon _addAssocIcon = Util.loadIconResource("Association");
  protected static ImageIcon _packageIcon = Util.loadIconResource("Package");
  protected static ImageIcon _realizationIcon = Util.loadIconResource("Realization");
  protected JPanel _modifiersPanel;
    protected static ImageIcon _generalizationIcon = Util.loadIconResource("Generalization");
  protected static ImageIcon _innerClassIcon = Util.loadIconResource("InnerClass");
    

  
  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelClassifier(String name,int columns) {
    super(name,columns);

    Class mclass = MClassifier.class;
    
    //
    //   this will cause the components on this page to be notified
    //      anytime a stereotype, namespace, operation, etc
    //      has its name changed or is removed anywhere in the model
    Class[] namesToWatch = { MStereotype.class,MNamespace.class,MOperation.class,
        MParameter.class,MAttribute.class,MAssociation.class,MClassifier.class };        
    setNameEventListening(namesToWatch);
    
//    addCaption(new JLabel("Name:"),0,0,0);
//    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

    
//    addCaption(new JLabel("Stereotype:"),1,0,0);
//    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
//    addField(stereotypeBox,1,0,0);
    
//    addCaption(new JLabel("Extends:"),2,0,0);
//    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
//    extendsList.setBackground(getBackground());
//    extendsList.setForeground(Color.blue);
//    addField(extendsList,2,0,0);
    
//    addCaption(new JLabel("Implements:"),3,0,0);
//    JList implementsList = new UMLList(new UMLClientDependencyListModel(this,null,true),true);
//    implementsList.setBackground(getBackground());
//    implementsList.setForeground(Color.blue);    
//    addField(implementsList,3,0,0);

//    addCaption(new JLabel("Modifiers:"),4,0,0);

    _modifiersPanel = new JPanel(new GridLayout(0,3));
    _modifiersPanel.add(new UMLCheckBox("public",this,new UMLEnumerationBooleanProperty("visibility",mclass,"getVisibility","setVisibility",MVisibilityKind.class,MVisibilityKind.PUBLIC,null)));
    _modifiersPanel.add(new UMLCheckBox("abstract",this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    _modifiersPanel.add(new UMLCheckBox("final",this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    _modifiersPanel.add(new UMLCheckBox("root",this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));

//    addCaption(new JLabel("Namespace:"),5,0,0);
//    addLinkField(new UMLList(new UMLNamespaceListModel(this),true),5,0,0);
    
//    addCaption(new JLabel("Derived:"),6,0,1);
//    JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
    //derivedList.setBackground(getBackground());
//    derivedList.setForeground(Color.blue);    
//    derivedList.setVisibleRowCount(1);
//    JScrollPane derivedScroll = new JScrollPane(derivedList);
//    addField(derivedScroll,6,0,1);
    
//    addCaption(new JLabel("Operations:"),0,1,0.25);
//    JList opsList = new UMLList(new UMLOperationsListModel(this,"feature",true),true);
//    opsList.setForeground(Color.blue);
//    opsList.setVisibleRowCount(1);
//    JScrollPane opsScroll = new JScrollPane(opsList);
//    addField(opsScroll,0,1,0.25);
    
//    addCaption(new JLabel("Attributes:"),1,1,0.25);
//    JList attrList = new UMLList(new UMLAttributesListModel(this,"feature",true),true);
//    attrList.setForeground(Color.blue);
//    attrList.setVisibleRowCount(1);
//    JScrollPane attrScroll= new JScrollPane(attrList);
//    addField(attrScroll,1,1,0.25);
    
//    addCaption(new JLabel("Associations:"),2,1,0.25);
//    JList connectList = new UMLList(new UMLConnectionListModel(this,null,true),true);
//    connectList.setForeground(Color.blue);
//    connectList.setVisibleRowCount(1);
//    addField(new JScrollPane(connectList),2,1,0.25);
    
    
    
//    addCaption(new JLabel("Owned Elements:"),3,1,0.25);
//    JList innerList = new UMLList(new UMLClassifiersListModel(this,"ownedElement",true),true);
//    innerList.setForeground(Color.blue);
//    innerList.setVisibleRowCount(1);
//    addField(new JScrollPane(innerList),3,1,0.25);
    
//    JPanel buttonBorder = new JPanel(new BorderLayout());
//    JPanel buttonPanel = new JPanel(new GridLayout(0,2));
//    buttonBorder.add(buttonPanel,BorderLayout.NORTH);
//    add(buttonBorder,BorderLayout.EAST);
    
//    new PropPanelButton(this,buttonPanel,_classIcon,"Add new class","addClass",null);
//    new PropPanelButton(this,buttonPanel,_packageIcon,"Go to package","navigateOwner",null);
//    new PropPanelButton(this,buttonPanel,_addOpIcon,"Add operation","addOperation",null);
//    new PropPanelButton(this,buttonPanel,_navBackIcon,"Go back","navigateBack","isNavigateBackEnabled");
//    new PropPanelButton(this,buttonPanel,_addAttrIcon,"Add attribute","addAttribute",null);
//    new PropPanelButton(this,buttonPanel,_navForwardIcon,"Go forward","navigationForward","isNavigateForwardEnabled");
//    new PropPanelButton(this,buttonPanel,_addAssocIcon,"Add association","addAssociation",null);
//    new PropPanelButton(this,buttonPanel,_deleteIcon,"Delete class","deleteClass",null);
//    new PropPanelButton(this,buttonPanel,_generalizationIcon,"Add generalization","addGeneralization",null);
//    new PropPanelButton(this,buttonPanel,_realizationIcon,"Add realization","addRealization",null);

  }


    public void addOperation() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MOperation oper = classifier.getFactory().createOperation();
            classifier.addFeature(oper);
            navigateTo(oper);
        }
    }
    
    public void addAttribute() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MAttribute attr = classifier.getFactory().createAttribute();
            classifier.addFeature(attr);
            navigateTo(attr);
        }
    }
    
    public void addAssociation() {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MNamespace ns = classifier.getNamespace();
            if(ns != null) {
                MFactory factory = classifier.getFactory();
                MAssociation newAssociation = factory.createAssociation();
                if(newAssociation != null) {
                    MAssociationEnd end = factory.createAssociationEnd();
                    end.setType(classifier);
                    newAssociation.addConnection(end);
                    end = ns.getFactory().createAssociationEnd();
                    newAssociation.addConnection(end);
                    ns.addOwnedElement(newAssociation);
                    navigateTo(newAssociation);
                }
            }
        }
    }
    
    public void addGeneralization() {
        Object target = getTarget();
        if(target instanceof MGeneralizableElement) {
            MGeneralizableElement genElem = (MGeneralizableElement) target;
            MNamespace ns = genElem.getNamespace();
            if(ns != null) {
                MGeneralization newGen = ns.getFactory().createGeneralization();
                if(newGen != null) {
                    newGen.setChild(genElem);
                    ns.addOwnedElement(newGen);
                    navigateTo(newGen);
                }
            }
        }
    }
    
    
} /* end class PropPanelClassifier */
