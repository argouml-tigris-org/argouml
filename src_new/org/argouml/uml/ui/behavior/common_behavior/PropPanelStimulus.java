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

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.behavior.common_behavior.*;

import javax.swing.*;
import org.argouml.uml.ui.*;
import java.awt.*;
import java.util.*;
import org.argouml.uml.ui.*;

public class PropPanelStimulus extends PropPanel {

  public PropPanelStimulus() {
    super("Stimulus Properties",2);

    Class mclass = MStimulus.class;

    addCaption(new JLabel("Name:"),0,0,0);
    addField(new UMLTextField(this,new UMLTextProperty(mclass,"name","getName","setName")),0,0,0);

    addCaption(new JLabel("Stereotype:"),2,0,0);
    JComboBox stereotypeBox = new UMLStereotypeComboBox(this);
    addField(stereotypeBox,2,0,0);

    addCaption(new JLabel("Sender:"),3,0,0);
    UMLComboBoxModel senderModel = new UMLComboBoxModel(this,"isAcceptibleSender",
        "sender","getSender","setSender",false,MInstance.class,true);
    addField(new UMLComboBox(senderModel),3,0,0);

    addCaption(new JLabel("Receiver:"),4,0,1);
    UMLComboBoxModel receiverModel = new UMLComboBoxModel(this,"isAcceptibleReceiver",
        "receiver","getReceiver","setReceiver",false,MInstance.class,true);
    addField(new UMLComboBox(receiverModel),4,0,1);

    //
    //   this is really a property of the link
    //      but since the link has so few properties of its own
    //      more convienient to have it here
    addCaption(new JLabel("Association:"),5,0,0);
    UMLComboBoxModel assocModel = new UMLComboBoxModel(this,"isAcceptibleAssociation",
        "association","getAssociation","setAssociation",false,MAssociation.class,true);
    addField(new UMLComboBox(assocModel),5,0,0);

    addCaption(new JLabel("Namespace:"),6,0,1);
    JList namespaceList = new UMLList(new UMLNamespaceListModel(this),true);
    namespaceList.setBackground(getBackground());
    namespaceList.setForeground(Color.blue);
    addField(namespaceList,6,0,0);

    addCaption(new JLabel("Connections:"),0,1,1);
    addField(new JList(),0,1,1);

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

}
