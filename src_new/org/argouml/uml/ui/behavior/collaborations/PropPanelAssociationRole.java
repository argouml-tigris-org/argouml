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

package org.argouml.uml.ui.behavior.collaborations;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.collaborations.*;
import javax.swing.*;
import org.argouml.uml.ui.*;
import java.awt.*;
import java.util.*;

public class PropPanelAssociationRole extends PropPanel {

  ////////////////////////////////////////////////////////////////
  // constants


  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelAssociationRole() {
    super("Association Role Properties",2);

    Class mclass = MAssociationRole.class;

    addCaption(new JLabel("Name:"),0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

    addCaption(new JLabel("Stereotype:"),1,0,0);
    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
    addField(stereotypeBox,1,0,0);

    addCaption(new JLabel("Namespace:"),2,0,0);
    JList namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
    namespaceList.setBackground(getBackground());
    namespaceList.setForeground(Color.blue);
    addField(namespaceList,2,0,0);

    addCaption(new JLabel("Modifiers:"),3,0,0);

    JPanel modifiersPanel = new JPanel(new GridLayout(0,3));
    modifiersPanel.add(new UMLCheckBox("Abstract",this,new UMLReflectionBooleanProperty("isAbstract",mclass,"isAbstract","setAbstract")));
    modifiersPanel.add(new UMLCheckBox("Final",this,new UMLReflectionBooleanProperty("isLeaf",mclass,"isLeaf","setLeaf")));
    modifiersPanel.add(new UMLCheckBox("Root",this,new UMLReflectionBooleanProperty("isRoot",mclass,"isRoot","setRoot")));
    addField(modifiersPanel,3,0,0);

    addCaption(new JLabel("Extends:"),4,0,0);

    JList extendsList = new UMLList(new UMLGeneralizationListModel(this,"generalization",true),true);
    extendsList.setBackground(getBackground());
    extendsList.setForeground(Color.blue);
    addField(extendsList,4,0,0);

    addCaption(new JLabel("Derived:"),5,0,1);
    JList derivedList = new UMLList(new UMLSpecializationListModel(this,null,true),true);
    //derivedList.setBackground(getBackground());
    derivedList.setForeground(Color.blue);
    derivedList.setVisibleRowCount(1);
    addField(new JScrollPane(derivedList),5,0,1);


    addCaption(new JLabel("AssociationRole Ends:"),0,1,1);
    JList assocEndList = new UMLList(new UMLReflectionListModel(this,"connection",true,"getAssociationEnds","setAssociationEnds","addAssociationEnd",null),true);
    assocEndList.setBackground(getBackground());
    assocEndList.setForeground(Color.blue);
    addField(assocEndList,0,1,1);


  }

    public Collection getAssociationEnds() {
        Collection ends = null;
        Object target = getTarget();
        if(target instanceof MAssociationRole) {
            ends = ((MAssociationRole) target).getConnections();
        }
        return ends;
    }

    public void setAssociationEnds(Collection ends) {
        Object target = getTarget();
        if(target instanceof MAssociationRole) {
            java.util.List list = null;
            if(ends instanceof java.util.List) {
                list = (java.util.List) ends;
            }
            else {
                list = new ArrayList(ends);
            }
            ((MAssociationRole) target).setConnections(list);
        }
    }

    public Object addAssociationEnd(Integer index) {
        Object target = getTarget();
        MAssociationEndRole newEnd = null;
        if(target instanceof MAssociationRole) {
            newEnd = new MAssociationEndRoleImpl();
            ((MAssociationRole) target).addConnection(newEnd);
        }
        return newEnd;
    }

    protected boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("AssociationRole");
    }


} /* end class PropPanelAssociationRole */
