// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

import javax.swing.*;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;
import org.argouml.model.uml.UmlFactory;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.MMUtil;

import java.awt.*;
import java.util.*;

public class PropPanelAssociationRole extends PropPanelModelElement {

  ////////////////////////////////////////////////////////////////
  // attributes
    protected JComboBox _baseField;

  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelAssociationRole() {
    super("Association Role",_associationRoleIcon, ConfigLoader.getTabPropsOrientation());

    //   this will cause the components on this page to be notified
    //      anytime a stereotype, namespace, operation, etc
    //      has its name changed or is removed anywhere in the model
    Class[] namesToWatch = { MStereotype.class,MNamespace.class,MAssociation.class, MMessage.class, MAssociationEndRole.class, MClassifierRole.class, MClassifier.class};
    setNameEventListening(namesToWatch); 
    
    Class mclass = MAssociationRole.class;

    addField(Argo.localize("UMLMenu", "label.name"), nameField);
    addField(Argo.localize("UMLMenu", "label.stereotype"), stereotypeBox);
    // commented out next line since changing namespace is not allowed
    addField(Argo.localize("UMLMenu", "label.namespace"), namespaceScroll);

    JComboBox baseComboBox = new UMLComboBox2(new UMLAssociationRoleBaseComboBoxModel(), ActionSetAssociationRoleBase.SINGLETON);
    addField(Argo.localize("UMLMenu", "label.base"), baseComboBox);
    
    add(LabelledLayout.getSeperator());
       
    JList assocEndList = new UMLLinkedList(this, new UMLAssociationRoleAssociationEndRoleListModel(this));
    assocEndList.setVisibleRowCount(2); // only binary associationroles are allowed
    addField(Argo.localize("UMLMenu", "label.associationrole-ends"), new JScrollPane(assocEndList));

    JList messageList = new UMLLinkedList(this, new UMLAssociationRoleMessageListModel(this));
    addField(Argo.localize("UMLMenu", "label.messages"), 
        new JScrollPane(messageList));

    

    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
    new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
    new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);

  }

} /* end class PropPanelAssociationRole */
