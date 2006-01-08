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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;

import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.ui.ActionDeleteSingleModelElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLAction;
import org.argouml.uml.ui.UMLComboBox2;
import org.argouml.uml.ui.UMLComboBoxModel2;
import org.argouml.uml.ui.UMLComboBoxNavigator;
import org.argouml.uml.ui.UMLMultiplicityComboBox2;
import org.argouml.uml.ui.UMLMultiplicityComboBoxModel;
import org.argouml.uml.ui.UMLSearchableComboBox;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLModelElementNamespaceComboBoxModel;
import org.argouml.util.ConfigLoader;

/**
 * The properties panel for a Class.
 */
public class PropPanelTagDefinition extends PropPanelModelElement {

    private JComponent ownerSelector;

    private static UMLTagDefinitionOwnerComboBoxModel 
        ownerComboBoxModel = 
            new UMLTagDefinitionOwnerComboBoxModel();

    private UMLComboBoxModel2 tdNamespaceComboBoxModel =
        new UMLTagDefinitionNamespaceComboBoxModel();
    
    private JComponent tdNamespaceSelector;
    
    /**
     * The combobox for the multiplicity of this type.
     */
    private UMLComboBox2 multiplicityComboBox;

    /**
     * Model for the MultiplicityComboBox
     */
    private static UMLMultiplicityComboBoxModel multiplicityComboBoxModel;

    ////////////////////////////////////////////////////////////////
    // contructors
    /**
     * The constructor.
     */
    public PropPanelTagDefinition() {
        super("TagDefinition",
            lookupIcon("TagDefinition"),
            ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.owner"),
                getOwnerSelector());
        addField(Translator.localize("label.namespace"),
                getTDNamespaceSelector());
        addField(Translator.localize("label.multiplicity"),
                getMultiplicityComboBox());
        add(getNamespaceVisibilityPanel());

        addSeperator();

        addAction(new ActionNavigateNamespace());
        addAction(new ActionNewTagDefinition());
        addAction(new ActionDeleteSingleModelElement());
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
            ownerSelector.add(new UMLComboBoxNavigator(this,
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
    protected UMLComboBox2 getMultiplicityComboBox() {
        if (multiplicityComboBox == null) {
            if (multiplicityComboBoxModel == null) {
                multiplicityComboBoxModel =
                    new UMLTagDefinitionMultiplicityComboBoxModel();
            }
            multiplicityComboBox = new UMLMultiplicityComboBox2(
                    multiplicityComboBoxModel,
                    new ActionSetTagDefinitionMultiplicity());
            multiplicityComboBox.setEditable(true);
        }
        return multiplicityComboBox;
    }


} /* end class PropPanelClass */

class UMLTagDefinitionNamespaceComboBoxModel 
    extends UMLModelElementNamespaceComboBoxModel {

    /**
     * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object o) {
        return Model.getFacade().isANamespace(o);
    }

    /**
     * @see org.argouml.uml.ui.foundation.core.UMLModelElementNamespaceComboBoxModel#buildModelList()
     */
    protected void buildModelList() {
        Object model =
            ProjectManager.getManager().getCurrentProject().getRoot();
        Collection c = new ArrayList();
        c.add(null);
        c.addAll(Model.getModelManagementHelper().getAllNamespaces(model));
        setElements(c);
    }

    /**
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
         /*
          * Rebuild the list from scratch to be sure it's correct.
          */
         Object t = getTarget();
         if (t != null && evt.getSource() == t) {
             // allow the evt.getNewValue() to be null (see parent class)
             buildModelList();
             setSelectedItem(getSelectedModelElement());
         }
     }
}

class ActionSetTagDefinitionNamespace extends UMLAction {
    /**
     * Constructor for ActionSetModelElementNamespace.
     */
    protected ActionSetTagDefinitionNamespace() {
        super("Set", true, NO_ICON);
    }

    /**
     * @see org.tigris.gef.undo.UndoableAction#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        Object oldNamespace = null;
        Object newNamespace = null;
        Object m = null;
        if (source instanceof UMLComboBox2) {
            UMLComboBox2 box = (UMLComboBox2) source;
            Object o = box.getTarget();
            if (Model.getFacade().isAModelElement(o)) {
                m = /*(MModelElement)*/ o;
                oldNamespace = Model.getFacade().getNamespace(m);
            }
            o = box.getSelectedItem();
            if (Model.getFacade().isANamespace(o)) {
                newNamespace = /*(MNamespace)*/ o;
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