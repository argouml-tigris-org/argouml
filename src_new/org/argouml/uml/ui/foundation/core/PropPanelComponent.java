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


// File: PropPanelComponent.java
// Classes: PropPanelComponent
// Original Author: 5eichler@informatik.uni-hamburg.de
// $Id$

package org.argouml.uml.ui.foundation.core;

import java.awt.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import org.argouml.uml.ui.*;


public class PropPanelComponent extends PropPanel {

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelComponent() {
    super("Component Properties",2);

    Class mclass = MComponent.class;

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

  }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Component");
    }


} /* end class PropPanelComponent */



