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



// File: PropPanelStimulus.java
// Classes: PropPanelStimulus
// Original Author: agauthie@ics.uci.edu
// $Id$

package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.MElementEvent;

import org.argouml.application.api.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.ui.foundation.core.*;

import org.tigris.gef.util.*;

public class PropPanelStimulus extends PropPanelModelElement {

   protected static ImageIcon _stimulusIcon = ResourceLoader.lookupIconResource("Stimulus");

  public PropPanelStimulus() {
    super("Stimulus Properties",_stimulusIcon, 2);

    Class[] namesToWatch = { MAction.class};
    setNameEventListening(namesToWatch);

    Class mclass = MStimulus.class;

    addCaption(Argo.localize("UMLMenu", "label.name"),1,0,0);
    addField(nameField,1,0,0);
    
    addCaption("Action:",2,0,0);
    addField(new UMLStimulusActionTextField(this,new UMLStimulusActionTextProperty("name")),2,0,0);


    addCaption(Argo.localize("UMLMenu", "label.stereotype"),3,0,0);
    addField(stereotypeBox,3,0,0);

   
    addCaption("Sender:",4,0,0);
    UMLList senderList = new UMLList(new UMLReflectionListModel(this,"sender",true,"getSender",null,null,null),true);
    senderList.setForeground(Color.blue);
    senderList.setVisibleRowCount(1);
    senderList.setFont(smallFont);
    JScrollPane senderScroll = new JScrollPane(senderList);
    addField(senderScroll,4,0,0.5);

    addCaption(Argo.localize("UMLMenu", "label.receiver"),5,0,0);
    UMLList receiverList = new UMLList(new UMLReflectionListModel(this,"receiver",true,"getReceiver",null,null,null),true);
    receiverList.setForeground(Color.blue);
    receiverList.setVisibleRowCount(1);
    receiverList.setFont(smallFont);
    JScrollPane receiverScroll = new JScrollPane(receiverList);
    addField(receiverScroll,5,0,0.5);

     addCaption(Argo.localize("UMLMenu", "label.namespace"),6,0,1);
     addLinkField(namespaceScroll,6,0,0);

    

    /*
    UMLActionModel actionModel = new UMLActionModel(this,mclass,"name",
            MAction.class,"getDispatchAction","setDispatchAction");

        addCaption("Action:",5,0,0);
        addField(new UMLActionNameField(actionModel,true),5,0,0);
    */

    
   
    

    //
    //   this is really a property of the link
    //      but since the link has so few properties of its own
    //      more convenient to have it here
    /*
      addCaption(Argo.localize("UMLMenu", "label.association"),5,0,0);
      UML ComboBoxModel assocModel = new UMLComboBoxModel(this,"isAcceptibleAssociation",
      "association","getAssociation","setAssociation",false,MAssociation.class,true);
      addField(new UMLComboBox(assocModel),5,0,0);
    */

     new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateNamespace",null);
     new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu" ,"button.go-back"),"navigateBackAction","isNavigateBackEnabled");
     new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
     new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete object"),"removeElement",null);

    
     
  }

     public void navigateNamespace() {
        Object target = getTarget();
        if(target instanceof MModelElement) {
            MModelElement elem = (MModelElement) target;
            MNamespace ns = elem.getNamespace();
            if(ns != null) {
                navigateTo(ns);
            }
        }
    }

    public void removed(MElementEvent mee) {
	/*
	System.out.println("PropPanel.removed: event.name:" + mee.getName() + " event.type: " + mee.getType());
        UMLChangeDispatch dispatch = new UMLChangeDispatch(this,0);
        dispatch.removed(mee);
        SwingUtilities.invokeLater(dispatch);
	*/
    }


    public boolean isAcceptibleBaseMetaClass(String baseClass) {
        return baseClass.equals("Stimulus");
    }

    public boolean isAcceptibleSender(MModelElement classifier) {
        return classifier instanceof MClassifier;
    }

    public MInstance getSender() {
        MInstance sender = null;
        Object target = getTarget();
        if(target instanceof MStimulus) {
            sender =  ((MStimulus) target).getSender();
        }
        return sender;
    }

    public void setSender(MInstance element) {
        Object target = getTarget();
        if(target instanceof MStimulus) {
            ((MStimulus) target).setSender(element);
        }
    }




    public boolean isAcceptibleReceiver(MModelElement classifier) {
        return classifier instanceof MClassifier;
    }

    public MInstance getReceiver() {
        MInstance receiver = null;
        Object target = getTarget();
        if(target instanceof MStimulus) {
            receiver =  ((MStimulus) target).getReceiver();
        }
        return receiver;
    }

    public void setReceiver(MInstance element) {
        Object target = getTarget();
        if(target instanceof MStimulus) {
            ((MStimulus) target).setReceiver(element);
        }
    }

    public boolean isAcceptibleAssociation(MModelElement classifier) {
        return classifier instanceof MAssociation;
    }

    public MAssociation getAssociation() {
        MAssociation association = null;
        Object target = getTarget();
        if(target instanceof MStimulus) {
            MLink link = ((MStimulus) target).getCommunicationLink();
            if(link != null) {
                association = link.getAssociation();
            }
        }
        return association;
    }

    public void setAssociation(MAssociation element) {
        Object target = getTarget();
        if(target instanceof MStimulus) {
            MStimulus stimulus = (MStimulus) target;
            MLink link = stimulus.getCommunicationLink();
            if(link == null) {
                link = stimulus.getFactory().createLink();
                if(link != null) {
                    link.addStimulus(stimulus);
                    stimulus.setCommunicationLink(link);
                }
            }
            MAssociation oldAssoc = link.getAssociation();
            if(oldAssoc != element) {
                link.setAssociation(element);
                //
                //  TODO: more needs to go here
                //
            }
        }
    }

    public void removeElement() {
	System.out.println("PropPanelStimulus.removeElement");
        MStimulus target = (MStimulus) getTarget();        
	MModelElement newTarget = (MModelElement) target.getNamespace();
                
        target.remove();
		if(newTarget != null) { 
			navigateTo(newTarget);
		}
			 // 2002-07-15
            // Jaap Branderhorst
            // Force an update of the navigation pane to solve issue 323
            ProjectBrowser.TheInstance.getNavPane().forceUpdate();
            
    }

}
