// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JList;

import org.argouml.uml.diagram.state.ui.ButtonActionNewCallEvent;
import org.argouml.uml.diagram.state.ui.ButtonActionNewChangeEvent;
import org.argouml.uml.diagram.state.ui.ButtonActionNewSignalEvent;
import org.argouml.uml.diagram.state.ui.ButtonActionNewTimeEvent;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewActionSequence;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCallAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewCreateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewDestroyAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewReturnAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewSendAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewTerminateAction;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewUninterpretedAction;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ToolBarUtility;

/**
 * The properties panel for a Transition.
 *
 * @author jrobbins
 */
public class PropPanelTransition extends PropPanelModelElement {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 7249233994894343728L;

    /**
     * Construct a new property panel for a Transition.
     */
    public PropPanelTransition() {
        super("label.transition-title", lookupIcon("Transition"));

        addField("label.name",
                getNameTextField());
        addField("label.statemachine",
                getSingleRowScroll(new UMLTransitionStatemachineListModel()));

        addField("label.state",
                getSingleRowScroll(new UMLTransitionStateListModel()));

        addSeparator();

        addField("label.source",
                getSingleRowScroll(new UMLTransitionSourceListModel()));
        addField("label.target",
                getSingleRowScroll(new UMLTransitionTargetListModel()));
        addField("label.trigger",
                getSingleRowScroll( new UMLTransitionTriggerListModel()));
        addField("label.guard",
                getSingleRowScroll(new UMLTransitionGuardListModel()));
        addField("label.effect",
                getSingleRowScroll(new UMLTransitionEffectListModel()));

        addAction(new ActionNavigateContainerElement());
        addAction(getTriggerActions());
        addAction(new ButtonActionNewGuard());
        addAction(getEffectActions());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }
    
    private Object[] getTriggerActions() {
        Object[] actions = {
            new ButtonActionNewCallEvent(),
            new ButtonActionNewChangeEvent(),
            new ButtonActionNewSignalEvent(),
            new ButtonActionNewTimeEvent(),
        };
        ToolBarUtility.manageDefault(actions, "transition.state.trigger");
        return actions;
    }

    protected Object[] getEffectActions() {
        Object[] actions = {
                ActionNewCallAction.getButtonInstance(),
                ActionNewCreateAction.getButtonInstance(),
                ActionNewDestroyAction.getButtonInstance(),
                ActionNewReturnAction.getButtonInstance(),
                ActionNewSendAction.getButtonInstance(),
                ActionNewTerminateAction.getButtonInstance(),
                ActionNewUninterpretedAction.getButtonInstance(),
                ActionNewActionSequence.getButtonInstance(),
        };
        ToolBarUtility.manageDefault(actions, "transition.state.effect");
        return actions;
    }
}
