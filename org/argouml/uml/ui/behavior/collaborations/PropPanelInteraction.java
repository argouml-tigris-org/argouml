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

import java.awt.Color;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLMessagesInteractionListModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import ru.novosoft.uml.behavior.collaborations.MInteraction;

/**
 * Proppanel for interactions. 
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelInteraction extends PropPanelModelElement {

	public PropPanelInteraction() {
		super("Interaction", _interactionIcon, 2);
		
		addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    	addField(nameField,1,0,0);
    	
    	addCaption(Argo.localize("UMLMenu", "label.stereotype"),2,0,1);
    	addField(stereotypeBox,2,0,0);

		// no namespace since this should not be altered
    	// addCaption(Argo.localize("UMLMenu", "label.namespace"),3,0,1);
    	// addField(namespaceScroll,3,0,0);
    	
    	addCaption(Argo.localize("UMLMenu", "label.messages"),1,1,0);
            
        JList messagesList = new UMLList(new UMLMessagesInteractionListModel(this,"messages",true),true);
      	messagesList.setBackground(getBackground());
      	messagesList.setForeground(Color.blue);
      	JScrollPane messagesScroll= new JScrollPane(messagesList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 	
        addField(messagesScroll,1,1,1);
    	
    	new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
        new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
        new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
        new PropPanelButton(this,buttonPanel,_deleteIcon, Argo.localize("UMLMenu", "button.delete-attribute"),"removeElement",null);
    	
    	/*
    	addCaption("Messages:",0,1,0);
    	JList messageList = new UMLList(new UMLMessagesListModel(this,"message",true), true);
    	messageList.setBackground(getBackground());
    	messageList.setForeground(Color.blue);
    	addField(new JScrollPane(messageList),0,1,1);
    	*/
	

	}

	

	/**
	 * Used to determine which stereotypes are legal with an interaction. At 
     * the moment, only the stereotypes of namespace and generlizable elements
     * are shown.
	 * @see org.argouml.uml.ui.PropPanel#isAcceptibleBaseMetaClass(String)
	 */
	protected boolean isAcceptibleBaseMetaClass(String baseClass) {
		return (baseClass.equals("Interaction") || baseClass.equals("Modelelement"));
	}
	
	/**
	 * Navigates to the owning collaboration
	 * @see org.argouml.uml.ui.foundation.core.PropPanelModelElement#navigateUp()
	 */
	public void navigateUp() {
		navigateTo(((MInteraction)getTarget()).getContext());
	}

}

