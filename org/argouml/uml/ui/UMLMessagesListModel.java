// Copyright (c) 1996-01 The Regents of the University of California. All
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
package org.argouml.uml.ui;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.collaborations.*;

import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

import java.util.*;
import java.awt.*;
import javax.swing.*;

public class UMLMessagesListModel extends UMLModelElementListModel{
    final private static String _nullLabel = "null";
    /**
     * <p> constuctor for the Messages list model</p>
     **/
    public UMLMessagesListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
		super(container,property, showNone);
    }
    /**
     * <p> recalculate the size of the list /p>
     * @return the size recalculated
     **/   
    protected int recalcModelElementSize() {
	int size   = 0;
        Collection messages = getMessages();
        if(messages != null) {
            size = messages.size();
        }
        return size;
    }
    /**
     * <p> get the model element saved at the index "index" </p>
     * @param index the index of the model element to return
     * @return the model element found at this index 
     **/
    protected MModelElement getModelElementAt(int index) {
        return elementAtUtil(getMessages(), index, MMessage.class);
    }
    /**
     * <p> get the messages of the model element (i.e. AssociationRole) </p>
     * @return the collection of messages from the model element
     **/
    public Collection getMessages() {
        Collection messages = null;
        Object target = getTarget();
        if(target instanceof MAssociationRole) {
            messages = ((MAssociationRole) target).getMessages();
        }
        return messages;
    }
    /**
     * <p> set the messages of the model element (i.e. AssociationRole) </p>
     **/    
    public void setMessages(Collection messages) {
        Object target = getTarget();
        if(target instanceof MAssociationRole) {
            java.util.List list = null;
            if(messages instanceof java.util.List) {
                list = (java.util.List) messages;
            }
            else {
                messages = new ArrayList(messages);
            }
            ((MAssociationRole) target).setMessages(list);
        }
    }
    /**
     * <p> adds a message to the list </p>
     * @param index the index where to add the message
     **/
    public void add(int index) {
        Object target = getTarget();
        MMessage newMessage = null;
        if(target instanceof MAssociationRole) {
            MAssociationRole ar=(MAssociationRole) target;
            ActionAddMessage action=new ActionAddMessage();
            MMessage m=action.addMessage(ar);							
            ProjectBrowser.TheInstance.getProject().setNeedsSave(true);
            // Advise Swing that we have added something at this index and
            // navigate there.
            fireIntervalAdded(this,index,index);
            navigateTo(m);
        }
    }
    /**
     * <p> deletes the message at the index "index" in the list </p>
     * @param index the index of the message to delete
     **/
    public void delete(int index){
        MMessage message=(MMessage) getModelElementAt(index);
        Object target = getTarget();
        if(target instanceof MAssociationRole){
            MAssociationRole ar=(MAssociationRole) target;
            if(ar.getMessages().contains(message))
                ar.removeMessage(message);
        	ProjectBrowser.TheInstance.getProject().setNeedsSave(true);
        	// Tell Swing this entry has gone
        	fireIntervalRemoved(this,index,index);
        }
    }    
    /**
     * <p>moves the message at index "index" up in the list</p>
     * @param index the index of the message to move
     **/
   public void moveUp(int index) {
        Object target = getTarget();
        if (target instanceof MAssociationRole) {
            MAssociationRole ar=(MAssociationRole) target;
            ar.setMessages(moveUpUtil(ar.getMessages(), index));
	}
        //mark as needing saving
        ProjectBrowser.TheInstance.getProject().setNeedsSave(true);
        // Tell Swing
        fireContentsChanged(this, index - 1, index);
    }
    /**
     * <p>moves the message at index "index" down in the list</p>
     * @param index the index of the message to move
     **/   
   public void moveDown(int index) {
        Object target = getTarget();
        if (target instanceof MAssociationRole) {
            MAssociationRole ar=(MAssociationRole) target;
            ar.setMessages(moveDownUtil(ar.getMessages(), index));
	}
        //mark as needing saving
        ProjectBrowser.TheInstance.getProject().setNeedsSave(true);
        // Tell Swing
        fireContentsChanged(this, index - 1, index);
    }
}