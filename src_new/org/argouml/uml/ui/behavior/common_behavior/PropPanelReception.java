// $Id$
// Copyright (c) 2002-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.common_behavior;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.ui.LookAndFeelMgr;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.foundation.core.PropPanelFeature;
import org.argouml.uml.ui.foundation.core.UMLBehavioralFeatureQueryCheckBox;
import org.argouml.uml.ui.foundation.core.UMLFeatureOwnerScopeCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;

/**
 * PropertyPanel for a Reception.
 */
public class PropPanelReception extends PropPanelFeature {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -8572743081899344540L;
    
    private JPanel modifiersPanel;

    /**
     * Construct a property panel for a Reception.
     */
    public PropPanelReception() {
        super("label.reception", lookupIcon("Reception"));

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.owner"),
                getOwnerScroll());

        add(getVisibilityPanel());
        
        modifiersPanel = createBorderPanel(Translator.localize(
                    "label.modifiers"));
        modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
        modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
        modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
        modifiersPanel.add(new UMLBehavioralFeatureQueryCheckBox());
        modifiersPanel.add(new UMLFeatureOwnerScopeCheckBox());
        add(modifiersPanel);

        addSeparator();

        addField(Translator.localize("label.signal"),
                new UMLReceptionSignalComboBox(this,
                        new UMLReceptionSignalComboBoxModel()));

        UMLTextArea2 specText = new UMLTextArea2(
                new UMLReceptionSpecificationDocument());
        specText.setLineWrap(true);
        specText.setRows(5);
        specText.setFont(LookAndFeelMgr.getInstance().getStandardFont());
        JScrollPane specificationScroll = new JScrollPane(specText);
        addField(Translator.localize("label.specification"),
                specificationScroll);

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewStereotype());
        addAction(getDeleteAction());
    }
}
