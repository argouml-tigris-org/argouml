// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.uml.CoreFactory;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.ActionNewParameter;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;

import org.tigris.swidgets.Orientation;

/**
 * The properties panel for an Event.
 *
 * @author oliver.heyden
 */
public abstract class PropPanelEvent extends PropPanelModelElement {

    private JScrollPane paramScroll;

    private UMLEventParameterListModel paramListModel;

    /**
     * Constructor for PropPanelEvent.
     * 
     * @param name the name string of the properties panel
     * @param icon the icon to be shown next to the name
     * @param orientation the orientation
     */
    public PropPanelEvent(String name, ImageIcon icon, 
            Orientation orientation) {
        super(name, icon, orientation);
        initialize();
    }

    /**
     * Initialize the panel with all fields and stuff.
     */
    protected void initialize() {

        paramScroll = getParameterScroll();
        addButton(new PropPanelButton2(this,
                new ActionNavigateContainerElement()));

        new PropPanelButton(this, getButtonPanel(), lookupIcon("Parameter"), 
                Translator.localize("button.new-parameter"),
                "buttonAddParameter", null);

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeBox());
        addField(Translator.localize("label.namespace"),
                getNamespaceScroll());
        addField(Translator.localize("label.parameters"),
                getParameterScroll());

        addSeperator();

        addButton(new PropPanelButton2(this, new ActionRemoveFromModel()));
    }

    /**
     * Adds a parameter to the event and navigates towards it.
     */
    public void buttonAddParameter() {
        Object param = CoreFactory.getFactory().buildParameter(getTarget());
        TargetManager.getInstance().setTarget(param);
    }

    /**
     * @return the parameter scroll
     */
    protected JScrollPane getParameterScroll() {
        if (paramScroll == null) {
            paramListModel = new UMLEventParameterListModel();
            JList paramList = new UMLMutableLinkedList(paramListModel,
                    ActionNewParameter.getInstance());
            paramList.setVisibleRowCount(3);
            paramScroll = new JScrollPane(paramList);
        }
        return paramScroll;
    }

} /* end class PropPanelEvent */
