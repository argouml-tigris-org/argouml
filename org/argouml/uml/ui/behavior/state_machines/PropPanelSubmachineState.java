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

// $header$
package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.swingext.LabelledLayout;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.util.ConfigLoader;

/**
 * @since Dec 15, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class PropPanelSubmachineState extends PropPanelCompositeState {

    /**
     * Constructor for PropPanelSubmachineState.
     */
    public PropPanelSubmachineState() {
        // TODO: get the submachine state it's own icon
        super("Submachine State", _compositeStateIcon, ConfigLoader.getTabPropsOrientation());
        addField(Argo.localize("UMLMenu", "label.name"), nameField);
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"), stereotypeBox));
        addField(Argo.localize("UMLMenu", "label.container"), containerScroll);
        JComboBox submachineBox = new UMLComboBox2(new UMLSubmachineStateComboBoxModel(), ActionSetSubmachineStateSubmachine.SINGLETON);
        addField(Argo.localize("UMLMenu", "label.submachine"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-submachine"), submachineBox));

        add(LabelledLayout.getSeperator());

        addField(Argo.localize("UMLMenu", "label.incoming"), incomingScroll);
        addField(Argo.localize("UMLMenu", "label.outgoing"), outgoingScroll);

        add(LabelledLayout.getSeperator());

        addField(Argo.localize("UMLMenu", "label.subvertex"), new JScrollPane(new UMLMutableLinkedList(this, new UMLCompositeStateSubvertexListModel(), null, ActionNewStubState.SINGLETON)));
    }

}
