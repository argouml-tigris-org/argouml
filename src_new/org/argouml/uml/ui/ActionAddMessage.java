// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;

import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.FigMessage;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Layer;
import org.tigris.gef.graph.GraphModel;
import org.tigris.gef.graph.GraphNodeRenderer;
import org.tigris.gef.presentation.FigNode;

import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;

/** Action to add a message.
 *  @stereotype singleton
 */
public class ActionAddMessage extends UMLChangeAction {

    ////////////////////////////////////////////////////////////////
    // static variables
    
    public static ActionAddMessage SINGLETON = new ActionAddMessage(); 


    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionAddMessage() { super("action.add-message"); }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
    	Object target =  TargetManager.getInstance().getModelTarget();
    
    	
    	if (!(target instanceof MAssociationRole) && ((MAssociationRole) target).getNamespace() instanceof MCollaboration) return;
    	MAssociationRole ar = (MAssociationRole) target;
        this.addMessage(ar);
        super.actionPerformed(ae);
    }
    
    /**
     * <p> add a message to an associationRole: it builds it using the Factory method
     * and then it creates the Fig and adds it to the diagram </p>
     * @param ar the associationRole to which the new message must be added
     **/
    public MMessage addMessage(MAssociationRole ar) {
        MCollaboration collab = (MCollaboration) ar.getNamespace();
        MMessage msg = UmlFactory.getFactory().getCollaborations().buildMessage(collab, ar);
        String nextStr = "" + ((MInteraction) (collab.getInteractions().toArray())[0]).getMessages().size();	
        Editor e = Globals.curEditor();
        GraphModel gm = e.getGraphModel();
        Layer lay = e.getLayerManager().getActiveLayer();
        GraphNodeRenderer gr = e.getGraphNodeRenderer();
        FigNode figMsg = gr.getFigNodeFor(gm, lay, msg);
        ((FigMessage) figMsg).addPathItemToFigAssociationRole(lay);
        e.damageAll();                
        return msg;
    }

    public boolean shouldBeEnabled() {
	Object target =  TargetManager.getInstance().getModelTarget();
	return super.shouldBeEnabled() && target instanceof MAssociationRole;
    }
    
}  /* end class ActionAddMessage */
