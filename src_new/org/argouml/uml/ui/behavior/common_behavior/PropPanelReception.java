// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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
import org.argouml.model.ModelFacade;
import org.argouml.swingext.GridLayout2;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.ActionRemoveFromModel;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLTextArea2;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.util.ConfigLoader;

/**
 * PropertyPanel for a Reception.
 */
public class PropPanelReception extends PropPanelModelElement {

    public PropPanelReception() {
        super("Reception", _receptionIcon, ConfigLoader
                .getTabPropsOrientation());

        Class mclass = (Class) ModelFacade.RECEPTION;

        addField(Translator.localize("UMLMenu", "label.name"),
                getNameTextField());
        addField(Translator.localize("UMLMenu", "label.stereotype"),
                getStereotypeBox());
        addField(Translator.localize("UMLMenu", "label.namespace"),
                getNamespaceComboBox());

        JPanel _modifiersPanel = new JPanel(new GridLayout2(0, 2,
                GridLayout2.ROWCOLPREFERRED));

        _modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
        _modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
        _modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
        // Reception are by definition never queries! see WFRs.
        // therefore do not provide the according checkbox!

        addField(Translator.localize("UMLMenu", "label.modifiers"),
                _modifiersPanel);

        addSeperator();

        addField(Translator.localize("UMLMenu", "label.signal"),
                new UMLReceptionSignalComboBox(this,
                        new UMLReceptionSignalComboBoxModel()));

        UMLTextArea2 specText = new UMLTextArea2(
                new UMLReceptionSpecificationDocument());
        specText.setLineWrap(true);
        specText.setRows(5);
        JScrollPane specificationScroll = new JScrollPane(specText,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        addField(Translator.localize("UMLMenu", "label.specification"),
                specificationScroll);

        buttonPanel.add(new PropPanelButton2(this,
                new ActionNavigateContainerElement()));
        buttonPanel
                .add(new PropPanelButton2(this, new ActionRemoveFromModel()));
    }
}