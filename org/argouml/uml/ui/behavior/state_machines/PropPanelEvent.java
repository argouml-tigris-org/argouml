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

// File: PropPanelEvent.java
// Classes: PropPanelEvent
// Original Author: oliver.heyden@gentleware.de
// $Id: 

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.swingext.Orientation;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;

public abstract class PropPanelEvent extends PropPanelModelElement {

    protected JScrollPane paramScroll;
    protected UMLEventParameterListModel paramListModel;

    /**
     * Constructor for PropPanelEvent.
     * @param name
     * @param icon
     * @param orientation
     */
    public PropPanelEvent(
        String name,
        ImageIcon icon,
        Orientation orientation) {
        super(name, icon, orientation);
        initialize();
    }

    public void initialize() {
        paramListModel = new UMLEventParameterListModel();
        JList paramList =
            new UMLLinkedList(paramListModel);
        paramScroll = getParameterScroll();
        new PropPanelButton(
            this,
            buttonPanel,
            _navUpIcon,
            Argo.localize("UMLMenu", "button.go-up"),
            "navigateUp",
            null);        
        new PropPanelButton(
            this,
            buttonPanel,
            _parameterIcon,
            Argo.localize("UMLMenu", "button.add-parameter"),
            "buttonAddParameter",
            null);

        addField(Argo.localize("UMLMenu", "label.name"), getNameTextField());
        addField(Argo.localize("UMLMenu", "label.stereotype"), new UMLComboBoxNavigator(this, Argo.localize("UMLMenu", "tooltip.nav-stereo"),getStereotypeBox()));
        addField(Argo.localize("UMLMenu", "label.namespace"),getNamespaceScroll());
        addField(Argo.localize("UMLMenu", "label.parameters"),getParameterScroll());

        addSeperator();
    }

    /**
     * Adds a parameter to the event and navigates towards it.
     */
    public void buttonAddParameter() {
        Object param = CoreFactory.getFactory().buildParameter(getTarget());
        TargetManager.getInstance().setTarget(param);
    }

    protected JScrollPane getParameterScroll() {
        if (paramScroll == null) {
            JList paramList = new UMLLinkedList(paramListModel);
            paramList.setVisibleRowCount(3);
            paramScroll = new JScrollPane(paramList);
        }
        return paramScroll;
    }

} /* end class PropPanelEvent */
