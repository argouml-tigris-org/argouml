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

// File: PropPanelPseudostate.java
// Classes: PropPanelPseudostate
// Original Author: your email address here
// $Id$

package org.argouml.uml.ui.behavior.state_machines;

import java.awt.GridLayout;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;

import org.argouml.application.api.Argo;
import org.argouml.kernel.ProjectManager;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.diagram.state.ui.FigBranchState;
import org.argouml.uml.diagram.state.ui.FigDeepHistoryState;
import org.argouml.uml.diagram.state.ui.FigForkState;
import org.argouml.uml.diagram.state.ui.FigHistoryState;
import org.argouml.uml.diagram.state.ui.FigInitialState;
import org.argouml.uml.diagram.state.ui.FigJoinState;
import org.argouml.uml.diagram.state.ui.FigShallowHistoryState;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLEnumerationBooleanProperty;
import org.argouml.uml.ui.UMLRadioButton;
import org.argouml.util.ConfigLoader;
import ru.novosoft.uml.behavior.state_machines.MPseudostate;
import ru.novosoft.uml.foundation.data_types.MPseudostateKind;

public class PropPanelPseudostate extends PropPanelStateVertex {

    private ButtonGroup kindGroup = new ButtonGroup();
    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelPseudostate() {
        super("Pseudostate", null, ConfigLoader.getTabPropsOrientation());

        Class mclass = MPseudostate.class;

        addField(Argo.localize("UMLMenu", "label.name"), nameField);
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),stereotypeBox));
        addField(Argo.localize("UMLMenu", "label.namespace"), namespaceScroll);
        
        JPanel kindPanel = new JPanel(new GridLayout(0, 2));
        UMLRadioButton junctionButton =
            new UMLRadioButton(
                "junction",
                this,
                new UMLEnumerationBooleanProperty(
                    "kind",
                    mclass,
                    "getKind",
                    "setKind",
                    MPseudostateKind.class,
                    MPseudostateKind.JUNCTION,
                    null));
        junctionButton.setEnabled(false);
        kindPanel.add(junctionButton);
        kindGroup.add(junctionButton);

        UMLRadioButton branchButton =
            new UMLRadioButton(
                "branch",
                this,
                new UMLEnumerationBooleanProperty(
                    "kind",
                    mclass,
                    "getKind",
                    "setKind",
                    MPseudostateKind.class,
                    MPseudostateKind.BRANCH,
                    null));
        branchButton.setEnabled(false);
        kindPanel.add(branchButton);
        kindGroup.add(branchButton);

        UMLRadioButton forkButton =
            new UMLRadioButton(
                "fork",
                this,
                new UMLEnumerationBooleanProperty(
                    "kind",
                    mclass,
                    "getKind",
                    "setKind",
                    MPseudostateKind.class,
                    MPseudostateKind.FORK,
                    null));
        forkButton.setEnabled(false);
        kindPanel.add(forkButton);
        kindGroup.add(forkButton);

        UMLRadioButton joinButton =
            new UMLRadioButton(
                "join",
                this,
                new UMLEnumerationBooleanProperty(
                    "kind",
                    mclass,
                    "getKind",
                    "setKind",
                    MPseudostateKind.class,
                    MPseudostateKind.JOIN,
                    null));
        joinButton.setEnabled(false);
        kindPanel.add(joinButton);
        kindGroup.add(joinButton);

        UMLRadioButton deepButton =
            new UMLRadioButton(
                "deep history",
                this,
                new UMLEnumerationBooleanProperty(
                    "kind",
                    mclass,
                    "getKind",
                    "setKind",
                    MPseudostateKind.class,
                    MPseudostateKind.DEEP_HISTORY,
                    null));
        deepButton.setEnabled(false);
        kindPanel.add(deepButton);
        kindGroup.add(deepButton);

        UMLRadioButton shallowButton =
            new UMLRadioButton(
                "shallow history",
                this,
                new UMLEnumerationBooleanProperty(
                    "kind",
                    mclass,
                    "getKind",
                    "setKind",
                    MPseudostateKind.class,
                    MPseudostateKind.SHALLOW_HISTORY,
                    null));
        shallowButton.setEnabled(false);
        kindPanel.add(shallowButton);
        kindGroup.add(shallowButton);

        UMLRadioButton initialButton =
            new UMLRadioButton(
                "initial",
                this,
                new UMLEnumerationBooleanProperty(
                    "kind",
                    mclass,
                    "getKind",
                    "setKind",
                    MPseudostateKind.class,
                    MPseudostateKind.INITIAL,
                    null));
        initialButton.setEnabled(false);
        kindPanel.add(initialButton);
        kindGroup.add(initialButton);

        addField(Argo.localize("UMLMenu", "label.pseudostate-kind"), kindPanel);

        add(LabelledLayout.getSeperator());
        
        addField(Argo.localize("UMLMenu", "label.incoming"), incomingScroll);
        addField(Argo.localize("UMLMenu", "label.outgoing"), outgoingScroll);

    }
   

    /**
     * @see org.argouml.uml.ui.TabModelTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object o) {
        super.setTarget(o);
        Iterator it =
            ProjectManager
                .getManager()
                .getCurrentProject()
                .findFigsForMember(o)
                .iterator();
        boolean represented = false;
        while (it.hasNext()) {
            Object i = it.next();
            // TODO: find out what happened to the junction
            if (i instanceof FigForkState
                || i instanceof FigBranchState
                || i instanceof FigDeepHistoryState
                || i instanceof FigForkState
                || i instanceof FigHistoryState
                || i instanceof FigInitialState
                || i instanceof FigJoinState
                || i instanceof FigShallowHistoryState) {                
                represented = true;           
                break;     
            }
        }        
        Enumeration e = kindGroup.getElements();
        while (e.hasMoreElements()) {
            ((UMLRadioButton)e.nextElement()).setEnabled(!represented);
        }
    }

} /* end class PropPanelPseudostate */
