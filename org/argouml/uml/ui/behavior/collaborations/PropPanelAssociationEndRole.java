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
import org.argouml.uml.ui.foundation.core.*;
import org.argouml.uml.MMUtil;

import java.awt.*;
import java.util.*;


public class PropPanelAssociationEndRole extends PropPanelAssociationEnd {

  public PropPanelAssociationEndRole() {
    super("AssociationEndRole", _assocEndRoleIcon,3);
    Class mclass = MAssociationEndRole.class;
    makeFields(mclass);
  }

protected void makeFields(Class mclass) {
    super.makeFields(mclass);
/*
    addCaption("Type:",3,0,0);
    UMLComboBoxModel model = new UMLComboBoxModel(this,"isAcceptibleType",
        "type","getType","setType",false,MClassifier.class,true);
    UMLComboBox box = new UMLComboBox(model);
    box.setToolTipText("Warning: Do not use this to change an end that is already in a diagram.");
    addField(new UMLComboBoxNavigator(this,"NavClass",box),3,0,0);
*/
    addCaption("AssociationRole:",5,0,1);
/*
    JList namespaceList = new UMLList(
				      new UMLReflectionListModel(this,"association",false,"getAssociation",null,null,null),true);
    namespaceList.setBackground(getBackground());
    namespaceList.setForeground(Color.blue);
    namespaceList.setVisibleRowCount(1);
    addField(new JScrollPane(namespaceList,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),5,0,0);

    addCaption("Attributes:",1,1,0.4);
    JList attrList = new UMLList(new UMLAttributesListModel(this,"feature",true),true);
    attrList.setForeground(Color.blue);
    attrList.setVisibleRowCount(1);
    addField(new JScrollPane(attrList),1,1,0.4);
*/
  }

  protected boolean isAcceptibleBaseMetaClass(String baseClass) {
      return baseClass.equals("AssociationEndRole");
  }

} /* end class PropPanelAssociationEndRole */

