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



// File: PropPanelMessage.java
// Classes: PropPanelMessage
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.argouml.uml.ui.behavior.collaborations;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.behavioralelements.commonbehavior.CommonBehaviorFactory;
import org.argouml.swingext.LabelledLayout;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.common_behavior.MCallAction;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

public class PropPanelMessage extends PropPanelModelElement {

  ////////////////////////////////////////////////////////////////
  // constants


  ////////////////////////////////////////////////////////////////
  // contructors
  public PropPanelMessage() {
    super("Message", ConfigLoader.getTabPropsOrientation());

    Class mclass = MMessage.class;
    
    Class[] namesToWatch = { MStereotype.class, MClassifierRole.class, 
        MAction.class };
    setNameEventListening(namesToWatch);

    addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
    addField(Argo.localize("UMLMenu", "label.stereotype"), getStereotypeBox());
    // a message does not have a namespace. removed therefore
    // addField(Argo.localize("UMLMenu", "label.namespace"), getNamespaceScroll());
    JList interactionList = new UMLLinkedList(new UMLMessageInteractionListModel());
    interactionList.setVisibleRowCount(1);
    addField(Argo.localize("UMLMenu", "label.interaction"), new JScrollPane(interactionList));

    JList senderList = new UMLLinkedList(new UMLMessageSenderListModel());
    senderList.setVisibleRowCount(1);
    JScrollPane senderScroll = new JScrollPane(senderList);
    addField(Argo.localize("UMLMenu", "label.sender"), senderScroll);

    JList receiverList = new UMLLinkedList(new UMLMessageReceiverListModel());
    receiverList.setVisibleRowCount(1);
    JScrollPane receiverScroll = new JScrollPane(receiverList);
    addField(Argo.localize("UMLMenu", "label.receiver"), receiverScroll);
   
    add(LabelledLayout.getSeperator());

    addField(Argo.localize("UMLMenu", "label.activator"), new UMLMessageActivatorComboBox(this, new UMLMessageActivatorComboBoxModel()));

    JList actionList = new UMLMutableLinkedList(new UMLMessageActionListModel(), null, ActionNewAction.SINGLETON);
    actionList.setVisibleRowCount(1);
    JScrollPane actionScroll = new JScrollPane(actionList);
    addField(Argo.localize("UMLMenu", "label.action"), actionScroll);
    
    JScrollPane predecessorScroll = new JScrollPane(new UMLMutableLinkedList(new UMLMessagePredecessorListModel(), ActionAddMessagePredecessor.SINGLETON, null));
    addField(Argo.localize("UMLMenu", "label.predecessor"), predecessorScroll);

    new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateInteraction",null);   
    new PropPanelButton(this,buttonPanel,_actionIcon, Argo.localize("UMLMenu", "button.add-action"),"addAction","isAddActionEnabled");
    // ActionNewAction.SINGLETON.setTarget((MModelElement)getTarget());
    // buttonPanel.add(new PropPanelButton2(this, ActionNewAction.SINGLETON));
    new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);
 }
    

    
    public MCallAction addAction() {
    	MCallAction action = null;
        Object target = getTarget();
        if(target instanceof MMessage) {
            action = (MCallAction)CommonBehaviorFactory.getFactory().buildAction((MMessage)target);
        }
        return action;
    }
    
    public boolean isAddActionEnabled() {
    	return (getTarget() instanceof MMessage) && (((MMessage)getTarget()).getAction() == null);
    }
    
    public void navigateInteraction() {
    	Object target = getTarget();
        if(target instanceof MMessage) {
            TargetManager.getInstance().setTarget(((MMessage)target).getInteraction());
        }
    }

    

} /* end class PropPanelMessage */
