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

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import javax.swing.*;
import org.argouml.uml.ui.*;
import java.awt.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import org.tigris.gef.util.Util;

public class PropPanelAssociation extends PropPanelModelElement {

  ////////////////////////////////////////////////////////////////
  // constants
  private static ImageIcon _associationIcon = Util.loadIconResource("Association");
  private static ImageIcon _assocEndIcon = Util.loadIconResource("AssociationEnd");
  private static ImageIcon _generalizationIcon = Util.loadIconResource("Generalization");
  private static ImageIcon _realizationIcon = Util.loadIconResource("Realization");



  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelAssociation() {
    super("Association Properties",2);

    Class mclass = MAssociation.class;
    //
    //   this will cause the components on this page to be notified
    //      anytime a stereotype, namespace, operation, etc
    //      has its name changed or is removed anywhere in the model
    Class[] namesToWatch = { MStereotype.class,MNamespace.class,MClassifier.class };
    setNameEventListening(namesToWatch);


    addCaption(new JLabel("Name:"),0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

    addCaption(new JLabel("Stereotype:"),1,0,0);
    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
    addField(stereotypeBox,1,0,0);

    addCaption(new JLabel("Namespace:"),2,0,0);
    addLinkField(new UMLList(new UMLNamespaceListModel(this),true),2,0,0);


    addCaption(new JLabel("Modifiers:"),3,0,1);

    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox("Abstract",this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox("Final",this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox("Root",this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,3,0,0);



    addCaption(new JLabel("Association Ends:"),0,1,0);
    JList assocEndList = new UMLList(new UMLAssociationEndListModel(this,"connection",true),true);
    assocEndList.setBackground(getBackground());
    assocEndList.setForeground(Color.blue);
    addField(assocEndList,0,1,0);

    addCaption(new JLabel("Extends:"),1,1,0);
    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
    addLinkField(extendsList,1,1,0);

    addCaption(new JLabel("Derived:"),2,1,1);
    JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
    //derivedList.setBackground(getBackground());
    derivedList.setForeground(Color.blue);
    derivedList.setVisibleRowCount(1);
    addField(new JScrollPane(derivedList),2,1,1);

    JPanel buttonBorder = new JPanel(new BorderLayout());
    JPanel buttonPanel = new JPanel(new GridLayout(0,2));
    buttonBorder.add(buttonPanel,BorderLayout.NORTH);
    add(buttonBorder,BorderLayout.EAST);


    new PropPanelButton(this,buttonPanel,_assocEndIcon,"Add association end","addAssociationEnd",null);
    new PropPanelButton(this,buttonPanel,_navUpIcon,"Go up","navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_deleteIcon,"Delete association","removeElement",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon,"Go back","navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_generalizationIcon,"Add generalization","addGeneralization",null);
    new PropPanelButton(this,buttonPanel,_navForwardIcon,"Go forward","navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_realizationIcon,"Add realization","addRealization",null);
    new PropPanelButton(this,buttonPanel,_associationIcon,"New association","newAssociation",null);

  }

   public void addAssociationEnd() {
        Object target = getTarget();
        if(target instanceof MAssociation) {
            MAssociation assoc = (MAssociation) target;
            MAssociationEnd assocEnd = assoc.getFactory().createAssociationEnd();
            assoc.addConnection(assocEnd);
            navigateTo(assocEnd);
        }
    }

    public void newAssociation() {
        Object target = getTarget();
        if(target instanceof MAssociation) {
            MAssociation assoc = (MAssociation) target;
            MNamespace ns = assoc.getNamespace();
            if(ns != null) {
                MFactory factory = ns.getFactory();
                MAssociation newAssoc = factory.createAssociation();
                newAssoc.addConnection(factory.createAssociationEnd());
                newAssoc.addConnection(factory.createAssociationEnd());
                ns.addOwnedElement(newAssoc);
                navigateTo(newAssoc);
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

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Association");
    }


} /* end class PropPanelAssociation */
