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



// File: PropPanelStateVertex.java
// Classes: PropPanelStateVertex
// Original Author: oliver.heyden@gentleware.de
// $Id:

package org.argouml.uml.ui.behavior.state_machines;

import java.awt.Color;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.uml.UmlFactory;
import org.argouml.swingext.Orientation;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLList;
import org.argouml.uml.ui.UMLReflectionListModel;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MStateMachine;
import ru.novosoft.uml.behavior.state_machines.MStateVertex;

public abstract class PropPanelStateVertex extends PropPanelModelElement {

    ////////////////////////////////////////////////////////////////
    // constants
    protected static ImageIcon _stateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("State");
    protected static ImageIcon _actionStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("ActionState");
    protected static ImageIcon _compositeStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("CompositeState");
    protected static ImageIcon _simpleStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("SimpleState");
    protected static ImageIcon _shallowHistoryIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("ShallowHistory");
    protected static ImageIcon _deepHistoryIcon =ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("DeepHistory");
    protected static ImageIcon _finalStateIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("FinalState");
    protected static ImageIcon _initialIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Initial");
    protected static ImageIcon _forkIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Fork");
    protected static ImageIcon _joinIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Join");
    protected static ImageIcon _transitionIcon = ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Transition");

    ////////////////////////////////////////////////////////////////

    protected JScrollPane incomingScroll;
    protected JScrollPane outgoingScroll;

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelStateVertex(String name, int columns) {
	this(name, null, columns);
    }

    /**
     * Constructor for PropPanelStateVertex.
     * @param name
     * @param icon
     * @param orientation
     */
    public PropPanelStateVertex(
        String name,
        ImageIcon icon,
        Orientation orientation) {
        super(name, icon, orientation);
        JList incomingList = new UMLLinkedList(this, new UMLStateVertexIncomingListModel(this));
        incomingScroll = new JScrollPane(incomingList);
        JList outgoingList = new UMLLinkedList(this, new UMLStateVertexOutgoingListModel(this));
        outgoingScroll = new JScrollPane(outgoingList);
        
        // TODO: add a combobox for the CompositeState that contains this StateVertex
        new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
        new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
        new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
        new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);
        
    }

    /**
     * Constructor for PropPanelStateVertex.
     */
    public PropPanelStateVertex() {
        super();
    }

    public PropPanelStateVertex(String name,ImageIcon icon, int columns) {
	super(name, icon, columns);

	Class mclass = MStateVertex.class;

	JList incomingList = new UMLList(new UMLReflectionListModel(this,"incomings",true,"getIncomings",null,null,null),true);
	incomingList.setForeground(Color.blue);
	incomingList.setVisibleRowCount(1);
	incomingList.setFont(smallFont);
        incomingScroll = new JScrollPane(incomingList);

	JList outgoingList = new UMLList(new UMLReflectionListModel(this,"outgoings",true,"getOutgoings",null,null,null),true);
	outgoingList.setForeground(Color.blue);
	outgoingList.setVisibleRowCount(1);
	outgoingList.setFont(smallFont);
        outgoingScroll = new JScrollPane(outgoingList);


	new PropPanelButton(this,buttonPanel,_navUpIcon, Argo.localize("UMLMenu", "button.go-up"),"navigateUp",null);
	new PropPanelButton(this,buttonPanel,_navBackIcon, Argo.localize("UMLMenu", "button.go-back"),"navigateBackAction","isNavigateBackEnabled");
	new PropPanelButton(this,buttonPanel,_navForwardIcon, Argo.localize("UMLMenu", "button.go-forward"),"navigateForwardAction","isNavigateForwardEnabled");
	new PropPanelButton(this,buttonPanel,_deleteIcon,localize("Delete"),"removeElement",null);
    }

    public void navigateUp() {
        Object target = getTarget();
        if(target instanceof MStateVertex) {
            MStateVertex elem = (MStateVertex) target;
            MStateVertex container = elem.getContainer();
            if(container != null) {
                navigateTo(container);
            }
        }
    }

      public MStateMachine getStateMachine() {
        MStateMachine machine = null;
        Object target = getTarget();
        if(target instanceof MState) {
            machine = ((MState) target).getStateMachine();
        }
        return machine;
    }

    public java.util.List getIncomings() {
        java.util.Collection incomings = null;
        Object target = getTarget();
        if(target instanceof MStateVertex) {
            incomings = ((MStateVertex) target).getIncomings();
        }
        return new Vector(incomings);
    }

    public java.util.List getOutgoings() {
        java.util.Collection outgoings = null;
        Object target = getTarget();
        if(target instanceof MStateVertex) {
            outgoings = ((MStateVertex) target).getOutgoings();
        }
        return new Vector(outgoings);
    }

      public void removeElement() {
	//overrides removeElement in PropPanel
        Object target = getTarget();
        if(target instanceof MStateVertex) {
            MStateVertex sv = (MStateVertex) target;

            Object newTarget = sv.getContainer();

            UmlFactory.getFactory().delete(sv);

            if(newTarget != null) {
                navigateTo(newTarget);
            }
        }
    }


} /* end class PropPanelStateVertex */

