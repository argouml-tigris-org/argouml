// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.tigris.swidgets.Orientation;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for a Statemachine.
 * 
 * @since Dec 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelStateMachine extends PropPanelModelElement {

    /**
     * Constructor for PropPanelStateMachine.
     */
    public PropPanelStateMachine() {
        this("StateMachine", ConfigLoader.getTabPropsOrientation());

    }

    /**
     * The constructor.
     * 
     * @param name the title of the properties panel, to be shown at the top
     * @param orient the orientation of the panel
     */
    public PropPanelStateMachine(String name, Orientation orient) {
        super(name, orient);
        initialize();
    }

    /**
     * Initialise the panel with fields and stuff.
     */
    protected void initialize() {
        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeBox());
        addField(Translator.localize("label.namespace"),
                getNamespaceScroll());

        // the context in which the statemachine resides
        JList contextList = new UMLLinkedList(
                new UMLStateMachineContextListModel());
        addField(Translator.localize("label.represented-modelelement"), 
                new JScrollPane(contextList));

        // the top state
        JList topList = new UMLLinkedList(new UMLStateMachineTopListModel());
        addField(Translator.localize("label.top-state"),
                new JScrollPane(topList));

        addSeperator();

        // the transitions the statemachine has
        JList transitionList = new UMLLinkedList(
                new UMLStateMachineTransitionListModel());
        addField(Translator.localize("label.transition"),
                new JScrollPane(transitionList));

        // the submachinestates
        // maybe this should be a mutable linked list but that's for the future
        // to decide
        JList submachineStateList = new UMLLinkedList(
                new UMLStateMachineSubmachineStateListModel());
        addField(Translator.localize("label.submachinestate"),
                new JScrollPane(submachineStateList));

        addButton(new PropPanelButton2(this,
                new ActionNavigateNamespace()));
        addButton(new PropPanelButton2(this, new ActionRemoveFromModel()));
    }

}
