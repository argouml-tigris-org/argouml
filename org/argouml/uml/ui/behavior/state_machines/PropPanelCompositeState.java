// $Id$
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



// File: PropPanelCompositeState.java
// Classes: PropPanelCompositeState
// Original Author: 5heyden
// $Id:

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.swingext.Orientation;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for a Composite State.
 *
 */
public class PropPanelCompositeState extends PropPanelState {

    private JList subverticesList = null;

    /**
     * Constructor for PropPanelCompositeState.
     * @param name the name of the properties panel
     * @param icon the icon to be shown next to the name
     * @param orientation the orientation of the panel
     */
    public PropPanelCompositeState(String name, ImageIcon icon, 
            Orientation orientation) {
        super(name, icon, orientation);
        initialize();
    }

    /**
     * The constructor.
     * 
     */
    public PropPanelCompositeState() {
        super("Composite State", lookupIcon("CompositeState"), 
                ConfigLoader.getTabPropsOrientation());
        initialize();

        addField(Translator.localize("UMLMenu", "label.name"), 
                getNameTextField());
        // addField(Translator.localize("UMLMenu", "label.stereotype"), 
        //     new UMLComboBoxNavigator(this, Translator.localize("UMLMenu", 
        //     "tooltip.nav-stereo"), getStereotypeBox()));
        addField(Translator.localize("UMLMenu", "label.stereotype"), 
                getStereotypeBox());
        addField(Translator.localize("UMLMenu", "label.container"), 
                getContainerScroll());
        addField(Translator.localize("UMLMenu", "label.modifiers"), 
                new UMLCompositeStateConcurentCheckBox());
        addField(Translator.localize("UMLMenu", "label.entry"), 
                getEntryScroll());
        addField(Translator.localize("UMLMenu", "label.exit"), 
                getExitScroll());
        addField(Translator.localize("UMLMenu", "label.do-activity"), 
                getDoScroll());

        addSeperator();

        addField(Translator.localize("UMLMenu", "label.incoming"), 
                getIncomingScroll());
        addField(Translator.localize("UMLMenu", "label.outgoing"), 
                getOutgoingScroll());
        addField(Translator.localize("UMLMenu", "label.internal-transitions"), 
                getInternalTransitionsScroll());

        addSeperator();

        addField(Translator.localize("UMLMenu", "label.subvertex"), 
                new JScrollPane(subverticesList));

    }

    /**
     * Initialize the panel with its specific fields, in casu 
     * the substate vertex list
     */
    protected void initialize() {
	subverticesList = new UMLCompositeStateSubvertexList(
            new UMLCompositeStateSubvertexListModel());
    }

} /* end class PropPanelCompositeState */



