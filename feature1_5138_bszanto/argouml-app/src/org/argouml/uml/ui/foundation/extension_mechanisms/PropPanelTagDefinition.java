// $Id$
// Copyright (c) 2005-2008 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.HashSet;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionNavigateContainerElement;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLMultiplicityPanel;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLModelElementNamespaceComboBoxModel;
import org.tigris.gef.undo.UndoableAction;

/**
 * The properties panel for a TagDefinition.
 */
public class PropPanelTagDefinition extends PropPanelModelElement {

    private static final long serialVersionUID = 3563940705352568635L;
    private JComponent ownerSelector;
    private JComponent tdNamespaceSelector;
    private UMLComboBox2 typeComboBox;
    private JScrollPane typedValuesScroll;

    
    private static UMLTagDefinitionOwnerComboBoxModel 
    ownerComboBoxModel = 
            new UMLTagDefinitionOwnerComboBoxModel();
    private UMLComboBoxModel2 tdNamespaceComboBoxModel = 
        new UMLTagDefinitionNamespaceComboBoxModel();
    private static UMLMetaClassComboBoxModel typeComboBoxModel;

    private static UMLTagDefinitionTypedValuesListModel typedValuesListModel = 
        new UMLTagDefinitionTypedValuesListModel();

    /**
     * The combobox for the multiplicity of this type.
     */
    private JPanel multiplicityComboBox;
    
    /**
     * Construct a property panel for TagDefinition elements.
     */
    public PropPanelTagDefinition() {
        super("label.tag-definition-title", lookupIcon("TagDefinition"));

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.owner"),
                getOwnerSelector());
        addField(Translator.localize("label.namespace"),
                getTDNamespaceSelector());
        addField(Translator.localize("label.multiplicity"),
                getMultiplicityComboBox());
        add(getNamespaceVisibilityPanel());

        addSeparator();

        UMLComboBoxNavigator typeComboBoxNav = new UMLComboBoxNavigator(
                Translator.localize("label.class.navigate.tooltip"),
                getTypeComboBox());
        typeComboBoxNav.setEnabled(false);
        addField(Translator.localize("label.type"), typeComboBoxNav);

        /* This field shows the ModelElements 
         * that have a TaggedValue 
         * according this TaggedDefinition: */
        addField(Translator.localize("label.tagged-values"),
                getTypedValuesScroll());
        
        addAction(new ActionNavigateContainerElement());
        addAction(new ActionNewTagDefinition());
        addAction(getDeleteAction());
    }

    protected JComponent getTDNamespaceSelector() {
        if (tdNamespaceSelector == null) {
            tdNamespaceSelector = new UMLSearchableComboBox(
                    tdNamespaceComboBoxModel,
                    new ActionSetTagDefinitionNamespace(), true);
        }
        return tdNamespaceSelector;

    }
    
    /**
     * Returns the stereotype selecter. This is a component which allows the
     * user to select a single item as the stereotype.
     *
     * @return the stereotype selecter
     */
    protected JComponent getOwnerSelector() {
        if (ownerSelector == null) {
            ownerSelector = new Box(BoxLayout.X_AXIS);
            ownerSelector.add(new UMLComboBoxNavigator(
                    Translator.localize("label.owner.navigate.tooltip"),
                    new UMLComboBox2(ownerComboBoxModel,
                            new ActionSetTagDefinitionOwner())
            ));
        }
        return ownerSelector;
    }

    /**
     * Returns the multiplicityComboBox.
     *
     * @return UMLMultiplicityComboBox2
     */
    protected JPanel getMultiplicityComboBox() {
        if (multiplicityComboBox == null) {
            multiplicityComboBox = new UMLMultiplicityPanel();
        }
        return multiplicityComboBox;
    }
    
    /**
     * Returns the typeComboBox.
     * @return UMLComboBox2
     */
    public UMLComboBox2 getTypeComboBox() {
        if (typeComboBox == null) {
            if (typeComboBoxModel == null) {
                typeComboBoxModel = new UMLMetaClassComboBoxModel();
            }
            typeComboBox =
                    new UMLComboBox2(typeComboBoxModel,
                            ActionSetTagDefinitionType.getInstance());
            typeComboBox.setEnabled(false);
        }
        return typeComboBox;
    }
    
    /**
     * Returns the typedValuesScroll.
     * @return JScrollPane
     */
    public JScrollPane getTypedValuesScroll() {
        if (typedValuesScroll == null) {
            JList typedValuesList  = new UMLLinkedList(typedValuesListModel);
            typedValuesScroll = new JScrollPane(typedValuesList);
        }
        return typedValuesScroll;

    }


} /* end class PropPanelClass */

class UMLTagDefinitionNamespaceComboBoxModel 
    extends UMLModelElementNamespaceComboBoxModel {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(UMLTagDefinitionNamespaceComboBoxModel.class);

    /*
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    @Override
    protected boolean isValidElement(Object o) {
        return Model.getFacade().isANamespace(o);
    }

    /*
     * @see org.argouml.uml.ui.foundation.core.UMLModelElementNamespaceComboBoxModel#buildModelList()
     */
    @Override
    protected void buildModelList() {
        Collection roots =
            ProjectManager.getManager().getCurrentProject().getRoots();
        Collection c = new HashSet();
        c.add(null);
        for (Object root : roots) {
            c.add(root);
            c.addAll(Model.getModelManagementHelper().getAllNamespaces(root));
        }

        Object target = getTarget();
        /* These next lines for the case that the current namespace
         * is not a valid one... Which of course should not happen,
         * but it does - in this case for TDs from profiles.
         */
        /* TODO: Enhance so that this never happens.
         */
        if (target != null) {
            Object namespace = Model.getFacade().getNamespace(target);
            if (namespace != null) {
                c.add(namespace);
                LOG.warn("The current TD namespace is not a valid one!");
            }
        }
        setElements(c);
    }

    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        /*
         * Rebuild the list from scratch to be sure it's correct.
         */
        Object t = getTarget();
        if (t != null && evt.getSource() == t 
                && (evt instanceof AttributeChangeEvent 
                        || evt instanceof AssociationChangeEvent)) {
            // allow the evt.getNewValue() to be null (see parent class)
            buildModelList();
        }
    }
}

class ActionSetTagDefinitionNamespace extends UndoableAction {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 366165281490799874L;

    /**
     * Constructor for ActionSetModelElementNamespace.
     */
    protected ActionSetTagDefinitionNamespace() {
        super(Translator.localize("Set"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("Set"));
    }

    /*
     * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
    	super.actionPerformed(e);
        Object source = e.getSource();
        Object oldNamespace = null;
        Object newNamespace = null;
        Object m = null;
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 box = (UMLComboBox2) source;
            Object o = box.getTarget();
            if (Model.getFacade().isAModelElement(o)) {
                m = o;
                oldNamespace = Model.getFacade().getNamespace(m);
            }
            o = box.getSelectedItem();
            if (Model.getFacade().isANamespace(o)) {
                newNamespace = o;
            }
        }
        if (newNamespace != oldNamespace && m != null && newNamespace != null) {
            // if there is a namespace, 
            // then there may not be a owner (stereotype)
            Model.getCoreHelper().setOwner(m, null);
            Model.getCoreHelper().setNamespace(m, newNamespace);
            super.actionPerformed(e);
        }
    }


}
