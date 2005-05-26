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
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.uml.ui.foundation.core;

import java.util.Collection;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.ActionAddAttribute;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateAssociation;
import org.argouml.uml.ui.ActionNavigateOppositeAssocEnd;
import org.argouml.uml.ui.ActionDeleteModelElements;
import org.argouml.uml.ui.PropPanelButton2;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMultiplicityComboBox2;
import org.argouml.uml.ui.UMLMultiplicityComboBoxModel;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.extension_mechanisms.ActionNewStereotype;
import org.argouml.util.ConfigLoader;
import org.tigris.gef.presentation.Fig;
import org.tigris.swidgets.GridLayout2;
import org.tigris.swidgets.Orientation;

/**
 * The properties panel for an association end.
 */
public class PropPanelAssociationEnd extends PropPanelModelElement {

    /**
     * The combobox that shows the type of this association end.
     */
    private JComboBox typeCombobox;

    /**
     * The scrollpane showing the association that owns this associationend
     */
    private JScrollPane associationScroll;

    /**
     * The combobox for the multiplicity of this type.
     */
    private UMLComboBox2 multiplicityComboBox;

    /**
     * Model for the MultiplicityComboBox
     */
    private static UMLMultiplicityComboBoxModel multiplicityComboBoxModel;

    /**
     * The checkbox that shows if this association end is navigable.
     */
    private JCheckBox navigabilityCheckBox;

    /**
     * The checkbox that shows the ordering of the associationend. It's selected
     * if it's an ordered associationend. Unselected if it's unordered. Sorted
     * is not supported atm.
     */
    private JCheckBox orderingCheckBox;

    /**
     * The checkbox that shows the scope of the associationend. Selected means
     * that the scope is the classifier. Unselected means that the scope is the
     * instance (the default).
     */
    private JCheckBox targetScopeCheckBox;

    /**
     * The panel with the radiobuttons the user can select to select the
     * aggregation of this associationend.
     */
    private JPanel aggregationRadioButtonpanel;

    /**
     * The panel with the radiobuttons the user can select to select the
     * changeability of this associationend.
     */
    private JPanel changeabilityRadioButtonpanel;

    /**
     * The panel with the radiobuttons to set the visibility (public, protected,
     * private) of this associationend. There is a bug (or inconsistency) with
     * NSUML since NSUML equals this visibility with the visibility of a
     * modelelement. The UML 1.3 spec does not. Since I try to follow the spec
     * as much as possible, the panel is defined here and not in
     * PropPanelModelElement
     */
    private JPanel visibilityRadioButtonPanel;

    /**
     * The list of classifiers that specify the operations that must be
     * implemented by the classifier type. These operations can be used by this
     * association.
     */
    private JScrollPane specificationScroll;

    /**
     * The list of qualifiers that owns this association end
     */
    private JScrollPane qualifiersScroll;

    /**
     * Button to add qualified attributes to this association end
     */
    private PropPanelButton2 qualifierButton;

    /**
     * Constructs the proppanel including initializing all scrollpanes, panels
     * etc. but excluding placing them on the proppanel itself.
     *
     * @see org.argouml.uml.ui.PropPanel#PropPanel(String, Orientation)
     */
    protected PropPanelAssociationEnd(String name, Orientation orientation) {
        super(name, orientation);
    }

    private String associationLabel;

    private PropPanelButton2 oppositeEndButton = 
        new PropPanelButton2(new ActionNavigateOppositeAssocEnd(),
            lookupIcon("AssociationEnd"));

    /**
     * Constructs the proppanel and places all scrollpanes etc. on the canvas.
     *
     * @see java.lang.Object#Object()
     */
    public PropPanelAssociationEnd() {
        super("AssociationEnd", ConfigLoader.getTabPropsOrientation());
        associationLabel = Translator.localize("label.association");
        createControls();
        positionStandardControls();
        positionControls();
    }

    /**
     * Create the controls specific to an AssociationEnd
     */
    protected void createControls() {
        typeCombobox = new UMLComboBox2(
                new UMLAssociationEndTypeComboBoxModel(),
                ActionSetAssociationEndType.getInstance(), true);
        JList associationList = new UMLLinkedList(
                new UMLAssociationEndAssociationListModel());
        associationList.setVisibleRowCount(1);
        associationScroll = new JScrollPane(associationList);
        navigabilityCheckBox = new UMLAssociationEndNavigableCheckBox();
        orderingCheckBox = new UMLAssociationEndOrderingCheckBox();
        targetScopeCheckBox = new UMLAssociationEndTargetScopeCheckbox();
        aggregationRadioButtonpanel =
            new UMLAssociationEndAggregationRadioButtonPanel(
                Translator.localize("label.aggregation"), true);
        changeabilityRadioButtonpanel =
            new UMLAssociationEndChangeabilityRadioButtonPanel(
                Translator.localize("label.changeability"), true);
        visibilityRadioButtonPanel =
            new UMLModelElementVisibilityRadioButtonPanel(
                Translator.localize("label.visibility"), true);
        specificationScroll = new JScrollPane(new UMLMutableLinkedList(
                new UMLAssociationEndSpecificationListModel(),
                ActionAddAssociationSpecification.getInstance(),
                null, null, true));
        qualifiersScroll = new JScrollPane(new UMLLinkedList(
                new UMLAssociationEndQualifiersListModel()));
        qualifierButton = new PropPanelButton2(new ActionAddAttribute());
        qualifierButton.setToolTipText(
                Translator.localize("button.new-qualifier"));

    }

    /**
     * Add the standard controls to the panel.
     */
    protected void positionStandardControls() {
        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.stereotype"),
                getStereotypeSelector());
    }

    /**
     * Add the specific controls for an associationend to the panel.
     */
    protected void positionControls() {
        addField(associationLabel, associationScroll);
        addField(Translator.localize("label.type"), typeCombobox);
        addField(Translator.localize("label.multiplicity"),
                getMultiplicityComboBox());

        addSeperator();

        JPanel panel = new JPanel(new GridLayout2());
        panel.add(navigabilityCheckBox);
        panel.add(orderingCheckBox);
        panel.add(targetScopeCheckBox);
        panel.setBorder(new TitledBorder(Translator.localize(
                "label.modifiers")));
        panel.setVisible(true);
        add(panel);
        addField(Translator.localize("label.specification"),
                specificationScroll);
        addField(Translator.localize("label.qualifiers"),
                qualifiersScroll);


        addSeperator();

        add(aggregationRadioButtonpanel);
        add(changeabilityRadioButtonpanel);
        add(visibilityRadioButtonPanel);

        addButton(new PropPanelButton2(new ActionNavigateAssociation()));
        addButton(oppositeEndButton);
        addButton(qualifierButton);
        addButton(new PropPanelButton2(new ActionNewStereotype(),
                lookupIcon("Stereotype")));
        addButton(new PropPanelButton2(new ActionDeleteSingleModelElement(),
                lookupIcon("Delete")));;
    }

    /**
     * @param label the label
     */
    protected void setAssociationLabel(String label) {
        associationLabel = label;
    }

    /**
     * Returns the multiplicityComboBox.
     *
     * @return UMLMultiplicityComboBox2
     */
    protected UMLComboBox2 getMultiplicityComboBox() {
        if (multiplicityComboBox == null) {
            if (multiplicityComboBoxModel == null) {
                multiplicityComboBoxModel =
                    new UMLAssociationEndMultiplicityComboBoxModel();
            }
            multiplicityComboBox = new UMLMultiplicityComboBox2(
                    multiplicityComboBoxModel,
                    ActionSetAssociationEndMultiplicity.getInstance());
            multiplicityComboBox.setEditable(true);
        }
        return multiplicityComboBox;
    }

    /**
     * Action behind pressing the button go to other. TODO: as soon as we don't
     * support JDK 1.2 any more, drop this method and replace it with an action.
     */
    public void gotoOther() {
        Object target = getTarget();
        if (Model.getFacade().isAAssociationEnd(target)) {
            Object end = /* (MAssociationEnd) */target;
            TargetManager.getInstance().setTarget(
                    Model.getFacade().getOppositeEnd(end));
        }
    }

    /**
     * Checks if the delete button of the associationend panel should be
     * enabled. It should be disabled if there are two or less association ends.
     *
     * @return boolean
     */
    public boolean isDeleteEnabled() {
        if (Model.getFacade().isAAssociationEnd(getTarget())) {
            return Model.getFacade().getOtherAssociationEnds(getTarget()).size()
            	> 1;
        }
        return false;
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        super.targetSet(e);
        Object o = e.getNewTarget();
        if (o instanceof Fig) {
            o = ((Fig) o).getOwner();
        }
        if (o != null && Model.getFacade().isAAssociationEnd(o)) {
            Collection ascEnds =
                Model.getFacade().getConnections(
                        Model.getFacade().getAssociation(o));
            if (ascEnds.size() > 2) {
                oppositeEndButton.setEnabled(false);
            } else {
                oppositeEndButton.setEnabled(true);
            }
        }
    }

} /* end class PropPanelAssociationEnd */
