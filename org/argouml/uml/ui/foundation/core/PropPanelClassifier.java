// $Id$
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

package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;
import org.argouml.swingext.GridLayout2;
import org.argouml.swingext.Orientation;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLLinkedList;

import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;

abstract public class PropPanelClassifier extends PropPanelNamespace {

    protected JPanel _modifiersPanel;

    private JScrollPane _generalizationScroll;
    private JScrollPane _specializationScroll;
    private JScrollPane _featureScroll;
    private JScrollPane _participantScroll;
    private JScrollPane _createActionScroll;
    private JScrollPane _instanceScroll;
    private JScrollPane _collaborationScroll;
    private JScrollPane _classifierRoleScroll;
    private JScrollPane _classifierInStateScroll;
    private JScrollPane _objectFlowStateScroll;
    private JScrollPane _powerTypeRangeScroll;
    private JScrollPane _associationEndScroll;
    private JScrollPane _parameterScroll;
    private JScrollPane _structuralFeatureScroll;

    // all GUI models that can be singletons and that are being used in subclasses
    // implemented as static (singleton) instances so that only one model is
    // registred for some modelevent and not an instance per proppanel.
    private static UMLGeneralizableElementGeneralizationListModel generalizationListModel =
        new UMLGeneralizableElementGeneralizationListModel();
    private static UMLGeneralizableElementSpecializationListModel specializationListModel =
        new UMLGeneralizableElementSpecializationListModel();
    private static UMLClassifierFeatureListModel featureListModel =
        new UMLClassifierFeatureListModel();
    private static UMLClassifierParticipantListModel participantListModel =
        new UMLClassifierParticipantListModel();
    private static UMLClassifierCreateActionListModel createActionListModel =
        new UMLClassifierCreateActionListModel();
    private static UMLClassifierInstanceListModel instanceListModel =
        new UMLClassifierInstanceListModel();
    private static UMLClassifierCollaborationListModel collaborationListModel =
        new UMLClassifierCollaborationListModel();
    private static UMLClassifierClassifierRoleListModel classifierRoleListModel =
        new UMLClassifierClassifierRoleListModel();
    private static UMLClassifierClassifierInStateListModel classifierInStateListModel =
        new UMLClassifierClassifierInStateListModel();
    private static UMLClassifierObjectFlowStateListModel objectFlowStateListModel =
        new UMLClassifierObjectFlowStateListModel();
    private static UMLClassifierPowertypeRangeListModel powertypeRangeListModel =
        new UMLClassifierPowertypeRangeListModel();
    private static UMLClassifierAssociationEndListModel associationEndListModel =
        new UMLClassifierAssociationEndListModel();
    private static UMLClassifierParameterListModel parameterListModel =
        new UMLClassifierParameterListModel();
    private static UMLClassifierStructuralFeatureListModel structuralFeatureListModel =
        new UMLClassifierStructuralFeatureListModel();

    ////////////////////////////////////////////////////////////////
    // contructors
    public PropPanelClassifier(String title, Orientation orientation) {
        super(title, orientation);
        initialize();
    }

    public PropPanelClassifier(String name, ImageIcon icon, Orientation orientation) {
        super(name, icon, orientation);
        initialize();
    }

    public void addOperation() {
        Object target = getTarget();
        if (target instanceof MClassifier) {
            MOperation newOper =
                UmlFactory.getFactory().getCore().buildOperation(
                    (MClassifier) target);
            TargetManager.getInstance().setTarget(newOper);
        }
    }

    public void addAttribute() {
        Object target = getTarget();
        if (target instanceof MClassifier) {
            MClassifier cls = (MClassifier) target;
            MAttribute attr =
                UmlFactory.getFactory().getCore().buildAttribute(cls);
            TargetManager.getInstance().setTarget(attr);
        }
    }

    private void initialize() { 

        _modifiersPanel =
            new JPanel(new GridLayout2(0, 2, GridLayout2.ROWCOLPREFERRED));          
        _modifiersPanel.add(
            new UMLGeneralizableElementAbstractCheckBox());
        _modifiersPanel.add(
            new UMLGeneralizableElementLeafCheckBox());
        _modifiersPanel.add(
            new UMLGeneralizableElementRootCheckBox());

    }

    public void addDataType() {
        Object target = getTarget();
        if (target instanceof MNamespace) {
            MNamespace ns = (MNamespace) target;
            MModelElement ownedElem = CoreFactory.getFactory().createDataType();
            ns.addOwnedElement(ownedElem);
            TargetManager.getInstance().setTarget(ownedElem);
        }
    }

    /**
     * Returns the associationEndScroll.
     * @return JScrollPane
     */
    public JScrollPane getAssociationEndScroll() {
        if (_associationEndScroll == null) {
            JList list = new UMLLinkedList(associationEndListModel);
            _associationEndScroll = new JScrollPane(list);
        }
        return _associationEndScroll;

    }

    /**
     * Returns the classifierInStateScroll.
     * @return JScrollPane
     */
    public JScrollPane getClassifierInStateScroll() {
        if (_classifierInStateScroll == null) {
            JList list = new UMLLinkedList(classifierInStateListModel);
            _classifierInStateScroll = new JScrollPane(list);
        }
        return _classifierInStateScroll;
    }

    /**
     * Returns the classifierRoleScroll.
     * @return JScrollPane
     */
    public JScrollPane getClassifierRoleScroll() {
        if (_classifierRoleScroll == null) {
            JList list = new UMLLinkedList(classifierRoleListModel);
            _classifierRoleScroll = new JScrollPane(list);
        }
        return _classifierRoleScroll;
    }

    /**
     * Returns the collaborationScroll.
     * @return JScrollPane
     */
    public JScrollPane getCollaborationScroll() {
        if (_collaborationScroll == null) {
            JList list = new UMLLinkedList(collaborationListModel);
            _collaborationScroll = new JScrollPane(list);
        }
        return _collaborationScroll;
    }

    /**
     * Returns the createActionScroll.
     * @return JScrollPane
     */
    public JScrollPane getCreateActionScroll() {
        if (_createActionScroll == null) {
            JList list = new UMLLinkedList(createActionListModel);
            _createActionScroll = new JScrollPane(list);
        }
        return _createActionScroll;
    }

    /**
     * Returns the featureScroll.
     * @return JScrollPane
     */
    public JScrollPane getFeatureScroll() {
        if (_featureScroll == null) {
            JList list = new UMLLinkedList(featureListModel);
            _featureScroll = new JScrollPane(list);
        }
        return _featureScroll;
    }

    /**
     * Returns the generalizationScroll.
     * @return JScrollPane
     */
    public JScrollPane getGeneralizationScroll() {
        if (_generalizationScroll == null) {
            JList list = new UMLLinkedList(generalizationListModel);
            _generalizationScroll = new JScrollPane(list);
        }
        return _generalizationScroll;
    }

    /**
     * Returns the instanceScroll.
     * @return JScrollPane
     */
    public JScrollPane getInstanceScroll() {
        if (_instanceScroll == null) {
            JList list = new UMLLinkedList(instanceListModel);
            _instanceScroll = new JScrollPane(list);
        }
        return _instanceScroll;
    }

    /**
     * Returns the objectFlowStateScroll.
     * @return JScrollPane
     */
    public JScrollPane getObjectFlowStateScroll() {
        if (_objectFlowStateScroll == null) {
            JList list = new UMLLinkedList(objectFlowStateListModel);
            _objectFlowStateScroll = new JScrollPane(list);
        }
        return _objectFlowStateScroll;
    }

    /**
     * Returns the parameterScroll.
     * @return JScrollPane
     */
    public JScrollPane getParameterScroll() {
        if (_parameterScroll == null) {
            JList list = new UMLLinkedList(parameterListModel);
            _parameterScroll = new JScrollPane(list);
        }
        return _parameterScroll;
    }

    /**
     * Returns the participantScroll.
     * @return JScrollPane
     */
    public JScrollPane getParticipantScroll() {
        if (_participantScroll == null) {
            JList list = new UMLLinkedList(participantListModel);
            _participantScroll = new JScrollPane(list);
        }
        return _participantScroll;
    }

    /**
     * Returns the powerTypeRangeScroll.
     * @return JScrollPane
     */
    public JScrollPane getPowerTypeRangeScroll() {
        if (_powerTypeRangeScroll == null) {
            JList list = new UMLLinkedList(powertypeRangeListModel);
            _powerTypeRangeScroll = new JScrollPane(list);
        }
        return _powerTypeRangeScroll;
    }

    /**
     * Returns the specializationScroll.
     * @return JScrollPane
     */
    public JScrollPane getSpecializationScroll() {
        if (_specializationScroll == null) {
            JList list = new UMLLinkedList(specializationListModel);
            _specializationScroll = new JScrollPane(list);
        }
        return _specializationScroll;
    }

    /**
     * Returns the structuralFeatureScroll.
     * @return JScrollPane
     */
    public JScrollPane getStructuralFeatureScroll() {
        if (_structuralFeatureScroll == null) {
            JList list = new UMLLinkedList(structuralFeatureListModel);
            _structuralFeatureScroll = new JScrollPane(list);
        }
        return _structuralFeatureScroll;
    }

} /* end class PropPanelClassifier */
