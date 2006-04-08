// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,g
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.ui.foundation.core;

import javax.swing.JList;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLInitialValueComboBox;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;

/**
 * The property panel for parameters.
 */
public class PropPanelParameter extends PropPanelModelElement {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = -1207518946939283220L;

    private JScrollPane behFeatureScroll;

    private static UMLParameterBehavioralFeatListModel behFeatureModel;

    /**
     * Construct a property panel for UML Parameter elements.
     */
    public PropPanelParameter() {
        super(
	      "Parameter",
	      lookupIcon("Parameter"),
	      ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.owner"),
                getBehavioralFeatureScroll());

        addSeparator();

        addField(Translator.localize("label.type"),
                new UMLComboBox2(new UMLParameterTypeComboBoxModel(),
                        ActionSetParameterType.getInstance()));

        addField(Translator.localize("label.parameter.default-value"),
                 new UMLInitialValueComboBox(this));

        add(new UMLParameterDirectionKindRadioButtonPanel(
                Translator.localize("label.parameter.kind"), true));

        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewParameter());
        addAction(new ActionAddDataType());
        addAction(new ActionAddEnumeration());
        addAction(new ActionNewStereotype());
        addAction(new ActionDeleteSingleModelElement());
    }

    /**
     * Returns the behavioral Feature Scroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getBehavioralFeatureScroll() {
        if (behFeatureScroll == null) {
            if (behFeatureModel == null) {
                behFeatureModel = new UMLParameterBehavioralFeatListModel();
            }
            JList list = new UMLLinkedList(behFeatureModel);
            list.setVisibleRowCount(1);
            behFeatureScroll = new JScrollPane(list);
        }
        return behFeatureScroll;
    }

} /* end class PropPanelParameter */
