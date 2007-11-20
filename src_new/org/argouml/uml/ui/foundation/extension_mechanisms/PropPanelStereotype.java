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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.argouml.uml.ui.ActionNavigateNamespace;
import org.argouml.uml.ui.UMLLinkedList;
import org.argouml.uml.ui.UMLModelElementListModel2;
import org.argouml.uml.ui.UMLMutableLinkedList;
import org.argouml.uml.ui.foundation.core.PropPanelModelElement;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementAbstractCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementGeneralizationListModel;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementLeafCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementRootCheckBox;
import org.argouml.uml.ui.foundation.core.UMLGeneralizableElementSpecializationListModel;
import org.tigris.gef.undo.UndoManager;

/**
 * The properties panel for a Stereotype.
 */
public class PropPanelStereotype extends PropPanelModelElement {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 8038077991746618130L;

    private List<String> metaClasses;
    
    private static UMLGeneralizableElementSpecializationListModel
    specializationListModel =
            new UMLGeneralizableElementSpecializationListModel();

    private static UMLGeneralizableElementGeneralizationListModel
    generalizationListModel =
            new UMLGeneralizableElementGeneralizationListModel();

    private static UMLStereotypeTagDefinitionListModel
    tagDefinitionListModel =
            new UMLStereotypeTagDefinitionListModel();

    private static UMLExtendedElementsListModel
    extendedElementsListModel =
            new UMLExtendedElementsListModel();

    private JScrollPane generalizationScroll;

    private JScrollPane specializationScroll;

    private JScrollPane tagDefinitionScroll;

    private JScrollPane extendedElementsScroll;

    /**
     * Construct a stereotype properties panel.
     */
    public PropPanelStereotype() {
        super("label.stereotype-title", lookupIcon("Stereotype"));

        addField(Translator.localize("label.name"), getNameTextField());

        addField(Translator.localize("label.namespace"),
                 getNamespaceSelector());


        JPanel modifiersPanel = createBorderPanel(
                Translator.localize("label.modifiers"));
        modifiersPanel.add(new UMLGeneralizableElementAbstractCheckBox());
        modifiersPanel.add(new UMLGeneralizableElementLeafCheckBox());
        modifiersPanel.add(new UMLGeneralizableElementRootCheckBox());
        add(modifiersPanel);
        
        add(getNamespaceVisibilityPanel());

        addSeparator();

        addField(Translator.localize("label.generalizations"),
                getGeneralizationScroll());

        addField(Translator.localize("label.specializations"),
                getSpecializationScroll());

        addField(Translator.localize("label.tagdefinitions"),
                getTagDefinitionScroll());

        addSeparator();

        initMetaClasses();
        UMLMutableLinkedList umll = new UMLMutableLinkedList(
                new UMLStereotypeBaseClassListModel(), 
                new ActionAddStereotypeBaseClass(), 
                null);
        umll.setDeleteAction(new ActionDeleteStereotypeBaseClass());
        umll.setCellRenderer(new DefaultListCellRenderer());
        addField(Translator.localize("label.base-class"),
            new JScrollPane(umll));

        addField(Translator.localize("label.extended-elements"),
                getExtendedElementsScroll());

        addAction(new ActionNavigateNamespace());
        addAction(new ActionNewStereotype());
        addAction(new ActionNewTagDefinition());
        addAction(getDeleteAction());
    }

    /**
     * Returns the generalizationScroll.
     *
     * @return JScrollPane
     */
    protected JScrollPane getGeneralizationScroll() {
        if (generalizationScroll == null) {
            JList list = new UMLLinkedList(generalizationListModel);
            generalizationScroll = new JScrollPane(list);
        }
        return generalizationScroll;
    }

    /**
     * Returns the specializationScroll.
     *
     * @return JScrollPane
     */
    protected JScrollPane getSpecializationScroll() {
        if (specializationScroll == null) {
            JList list = new UMLLinkedList(specializationListModel);
            specializationScroll = new JScrollPane(list);
        }
        return specializationScroll;
    }

    /**
     * Returns the tagDefinitionScroll.
     *
     * @return JScrollPane
     */
    protected JScrollPane getTagDefinitionScroll() {
        if (tagDefinitionScroll == null) {
            JList list = new UMLLinkedList(tagDefinitionListModel);
            tagDefinitionScroll = new JScrollPane(list);
        }
        return tagDefinitionScroll;
    }

    protected JScrollPane getExtendedElementsScroll() {
        if (extendedElementsScroll == null) {
            JList list = new UMLLinkedList(extendedElementsListModel);
            extendedElementsScroll = new JScrollPane(list);
        }
        return extendedElementsScroll;
    }
    
    /**
     * Initialise the meta-classes list. <p>
     * 
     * All this code is necessary to be independent of 
     * model repository implementation, 
     * i.e. to ensure that we have a 
     * sorted list of strings.
     */
    void initMetaClasses() {
        Collection<String> tmpMetaClasses =
                Model.getCoreHelper().getAllMetatypeNames();
        if (tmpMetaClasses instanceof List) {
            metaClasses = (List<String>) tmpMetaClasses;
        } else {
            metaClasses = new LinkedList<String>(tmpMetaClasses);
        }
        try {
            Collections.sort(metaClasses);
        } catch (UnsupportedOperationException e) {
            // We got passed an unmodifiable List.  Copy it and sort the result
            metaClasses = new LinkedList<String>(tmpMetaClasses);
            Collections.sort(metaClasses);
        }
    }

    /**
     * The list model for the BaseClasses of the stereotype.
     *
     * @author Michiel
     */
    class UMLStereotypeBaseClassListModel extends UMLModelElementListModel2 {

        /**
         * Construct the model, listen to changes of "baseClass".
         */
        UMLStereotypeBaseClassListModel() {
            super("baseClass");
        }

        @Override
        protected void buildModelList() {
            removeAllElements();
            if (Model.getFacade().isAStereotype(getTarget())) {
                // keep them sorted
                LinkedList<String> lst = new LinkedList<String>(
                        Model.getFacade().getBaseClasses(getTarget()));
                Collections.sort(lst);
                addAll(lst);
            }
        }

        @Override
        protected boolean isValidElement(Object element) {
            if (Model.getFacade().isAStereotype(element)) {
                return true;
            }
            return false;
        }
    }
    
    /**
     * The Action to add a baseclass to the stereotype.
     *
     * @author Michiel
     */
    class ActionAddStereotypeBaseClass extends AbstractActionAddModelElement2 {
        
        @Override
        protected List getChoices() {
            return metaClasses;
        }

        @Override
        protected String getDialogTitle() {
            return Translator.localize("dialog.title.add-baseclasses");
        }

        @Override
        protected List getSelected() {
            List result = new ArrayList();
            if (Model.getFacade().isAStereotype(getTarget())) {
                Collection bases =
                        Model.getFacade().getBaseClasses(getTarget());
                result.addAll(bases);
            }
            return result;
        }

        @Override
        protected void doIt(List selected) {
            Object stereo = getTarget();
            Set<Object> oldSet = new HashSet<Object>(getSelected());
            Set toBeRemoved = new HashSet<Object>(oldSet);

            for (Object o : selected) {
                if (oldSet.contains(o)) {
                    toBeRemoved.remove(o);
                } else {
                    Model.getExtensionMechanismsHelper()
                            .addBaseClass(stereo, o);
                }
            }
            for (Object o : toBeRemoved) {
                Model.getExtensionMechanismsHelper().removeBaseClass(stereo, o);
            }
        }
        
    }
    
    /**
     * The Action to remove a baseclass from a stereotype.
     *
     * @author Michiel
     */
    class ActionDeleteStereotypeBaseClass extends AbstractActionRemoveElement {

        public ActionDeleteStereotypeBaseClass() {
            super(Translator.localize("menu.popup.remove"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            UndoManager.getInstance().startChain();
            Object baseclass = getObjectToRemove();
            if (baseclass != null) {
                Object st = getTarget();
                if (Model.getFacade().isAStereotype(st)) {
                    Model.getExtensionMechanismsHelper().removeBaseClass(st,
                            baseclass);
                }
            }
        }
    }
}