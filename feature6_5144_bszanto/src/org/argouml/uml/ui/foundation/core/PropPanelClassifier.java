// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.ui.ScrollList;
import org.argouml.uml.ui.UMLDerivedCheckBox;
import org.argouml.uml.ui.behavior.common_behavior.ActionNewReception;
import org.tigris.swidgets.Orientation;

/**
 * The abstract properties panel for Classifiers.
 */
public abstract class PropPanelClassifier extends PropPanelNamespace {

    private JPanel modifiersPanel;

    /**
     * The action used to add a reception to the classifier.
     */
    private ActionNewReception actionNewReception = new ActionNewReception();

    private JScrollPane generalizationScroll;
    private JScrollPane specializationScroll;
    private JScrollPane featureScroll;
    private JScrollPane createActionScroll;
    private JScrollPane powerTypeRangeScroll;
    private JScrollPane associationEndScroll;
    private JScrollPane attributeScroll;
    private JScrollPane operationScroll;

    // all GUI models that can be singletons and
    // that are being used in subclasses
    // implemented as static (singleton) instances so that only one model is
    // registered for some modelevent and not an instance per proppanel.

    private static UMLGeneralizableElementGeneralizationListModel
    generalizationListModel =
            new UMLGeneralizableElementGeneralizationListModel();
    private static UMLGeneralizableElementSpecializationListModel
    specializationListModel =
            new UMLGeneralizableElementSpecializationListModel();
    private static UMLClassifierFeatureListModel featureListModel =
        new UMLClassifierFeatureListModel();
    private static UMLClassifierCreateActionListModel createActionListModel =
        new UMLClassifierCreateActionListModel();
    private static UMLClassifierPowertypeRangeListModel
    powertypeRangeListModel =
            new UMLClassifierPowertypeRangeListModel();
    private static UMLClassifierAssociationEndListModel
    associationEndListModel =
            new UMLClassifierAssociationEndListModel();
    private static UMLClassAttributeListModel attributeListModel =
        new UMLClassAttributeListModel();
    private static UMLClassOperationListModel operationListModel =
        new UMLClassOperationListModel();

    /**
     * Construct a property panel for a Classififer with the given name and
     * icon.
     * 
     * @param name the title of the properties panel
     * @param icon the icon shown next to the name
     */
    public PropPanelClassifier(String name, ImageIcon icon) {
        super(name, icon);
        initialize();
    }
    
    /**
     * The constructor.
     *
     * @param title the title of the properties panel
     * @param orientation the orientation of the panel
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #PropPanelClassifier(String, ImageIcon)} and
     *             setOrientation() after instantiation.
     */
    @Deprecated
    public PropPanelClassifier(String title, Orientation orientation) {
        super(title, orientation);
        initialize();
    }

    /**
     * The constructor.
     *
     * @param name the title of the properties panel
     * @param orientation the orientation of the panel
     * @param icon the icon shown next to the name
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #PropPanelClassifier(String, ImageIcon)} and
     *             setOrientation() after instantiation.
     */
    @Deprecated
    public PropPanelClassifier(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);
        initialize();
    }

    /**
     * Initialize the panel with the common fields and stuff.
     */
    private void initialize() {
        modifiersPanel = 
            createBorderPanel(Translator.localize("label.modifiers"));
        modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
        modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
        modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
        modifiersPanel.add(new UMLDerivedCheckBox());
    }

    /**
     * Returns the associationEndScroll.
     * @return JScrollPane
     */
    public JScrollPane getAssociationEndScroll() {
        if (associationEndScroll == null) {
            associationEndScroll = new ScrollList(associationEndListModel);
        }
        return associationEndScroll;

    }

    /**
     * Returns the createActionScroll.
     * @return JScrollPane
     */
    public JScrollPane getCreateActionScroll() {
        if (createActionScroll == null) {
            createActionScroll = new ScrollList(createActionListModel);
        }
        return createActionScroll;
    }

    /**
     * Returns the featureScroll.
     * @return JScrollPane
     */
    public JScrollPane getFeatureScroll() {
        if (featureScroll == null) {
            featureScroll = new ScrollList(featureListModel, true, false);
        }
        return featureScroll;
    }

    /**
     * Returns the generalizationScroll.
     * @return JScrollPane
     */
    public JScrollPane getGeneralizationScroll() {
        if (generalizationScroll == null) {
            generalizationScroll = new ScrollList(generalizationListModel);
        }
        return generalizationScroll;
    }

    /**
     * Returns the powerTypeRangeScroll.
     * @return JScrollPane
     */
    public JScrollPane getPowerTypeRangeScroll() {
        if (powerTypeRangeScroll == null) {
            powerTypeRangeScroll = new ScrollList(powertypeRangeListModel);
        }
        return powerTypeRangeScroll;
    }

    /**
     * Returns the specializationScroll.
     * @return JScrollPane
     */
    public JScrollPane getSpecializationScroll() {
        if (specializationScroll == null) {
            specializationScroll = new ScrollList(specializationListModel);
        }
        
        return specializationScroll;
    }

    /**
     * Returns the attributeScroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getAttributeScroll() {
        if (attributeScroll == null) {
            attributeScroll = new ScrollList(attributeListModel, true, false);
        }
        return attributeScroll;
    }

    /**
     * Returns the operationScroll.
     *
     * @return JScrollPane
     */
    public JScrollPane getOperationScroll() {
        if (operationScroll == null) {
            operationScroll = new ScrollList(operationListModel, true, false);
        }
        return operationScroll;
    }

    /**
     * @return the action for a new reception
     */
    protected ActionNewReception getActionNewReception() {
        return actionNewReception;
    }

    /**
     * @return Returns the modifiersPanel.
     */
    protected JPanel getModifiersPanel() {
        return modifiersPanel;
    }


} /* end class PropPanelClassifier */
