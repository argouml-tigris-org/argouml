// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import javax.swing.JPanel;

import org.argouml.i18n.Translator;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLInitialValueComboBox;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for an Attribute.
 *
 * @author jrobbins
 * @author jaap.branderhorst
 */
public class PropPanelAttribute extends PropPanelStructuralFeature {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -5596689167193050170L;

    /**
     * The constructor.
     *
     */
    public PropPanelAttribute() {
        super("Attribute", ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.owner"),
                getOwnerScroll());
        addField(Translator.localize("label.multiplicity"),
                getMultiplicityComboBox());

        addSeparator();
        
        add(getVisibilityPanel());
        add(getChangeabilityRadioButtonPanel());

        JPanel modifiersPanel = createBorderPanel(
                Translator.localize("label.modifiers"));
        modifiersPanel.add(getOwnerScopeCheckbox());
        add(modifiersPanel);
        
        addSeparator();

        addField(Translator.localize("label.type"),
                new UMLComboBoxNavigator(
                        this,
                        Translator.localize("label.class.navigate.tooltip"),
                        getTypeComboBox()));

        // addField(Translator.localize("label.initial-value"), new
        // JScrollPane(new UMLLinkedList(new
        // UMLAttributeInitialValueListModel())));
        // TODO: The following line is my hack fix for the above line.
        // this fixes issue 1378 but re-introduces a deprecated class
        // IMO the initial value should not be a combo or a list
        // but a simple text field. Bob Tarling 12 Feb 2004.
        addField(Translator.localize("label.initial-value"),
                new UMLInitialValueComboBox(this));

        addAction(new ActionNavigateContainerElement());
        addAction(TargetManager.getInstance().getAddAttributeAction());
        addAction(new ActionAddDataType());
        addAction(new ActionAddEnumeration());
        addAction(new ActionNewStereotype());
        addAction(new ActionDeleteSingleModelElement());
    }


} /* end class PropPanelAttribute */
