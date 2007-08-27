// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.extension_mechanisms;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.swingext.SpacerPanel;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLList2;
import org.argouml.uml.ui.UMLModelElementListModel2;


/**
 * A panel to select the BaseClasses of a Stereotype. <p>
 * 
 * It consists of 2 panels, each containing a 
 * JScrollPane, with at the left the complete list of meta-types,
 * and at the right the selected ones. <p>
 *  
 * A couple of buttons in the center allow moving 
 * selected items from left to right and vice versa.
 *
 * @author Michiel
 */
class StereotypeBCSelectionPanel extends JPanel {

    /**
     * Insets in pixels.
     */
    private static final int INSET_PX = 3;

    private List<String> metaClasses;
    private JScrollPane selectedScroll;
    private JScrollPane availableScroll;
    private JButton addBCButton;
    private JButton removeBCButton;
    private JPanel xferButtons;
    private JList selectedList;
    private JList availableList;
    private UMLModelElementListModel2 selectedListModel;
    private UMLModelElementListModel2 availableListModel;
    
    /**
     * The constructor.
     */
    StereotypeBCSelectionPanel() {
        super();
        initMetaClasses();
        initModels();
        initGUI();
    }
    
    void initModels() {
        selectedListModel = new UMLStereotypeBaseClassListModel();
        availableListModel = 
            new UMLStereotypeAvailableBaseClassListModel(metaClasses);
    }

    void initGUI() {
        selectedList = new BCList(selectedListModel);
        availableList = new BCList(availableListModel);
        selectedScroll = new JScrollPane(selectedList);
        availableScroll = new JScrollPane(availableList);
        selectedList.setToolTipText(Translator.localize(
                "misc.tooltip.base-classes"));
        availableList.setToolTipText(Translator.localize(
                "misc.tooltip.available-meta-classes"));
        
        // make buttons
        addBCButton = new JButton(">>");
        addBCButton.setToolTipText(Translator.localize(
                "button.add-base-class"));
        removeBCButton = new JButton("<<");
        removeBCButton.setToolTipText(Translator.localize(
                "button.remove-base-class"));
        addBCButton.setEnabled(false);
        removeBCButton.setEnabled(false);
        addBCButton.setMargin(new Insets(0, 2, 0, 2));
        removeBCButton.setMargin(new Insets(0, 2, 0, 2));
        addBCButton.setPreferredSize(addBCButton.getMinimumSize());
        removeBCButton.setPreferredSize(removeBCButton.getMinimumSize());

        // make buttons layout
        BoxLayout box;
        xferButtons = new JPanel();
        box = new BoxLayout(xferButtons, BoxLayout.Y_AXIS);
        xferButtons.setLayout(box);
        xferButtons.add(new SpacerPanel());
        xferButtons.add(addBCButton);
        xferButtons.add(new SpacerPanel());
        xferButtons.add(removeBCButton);
        Dimension dmax = box.maximumLayoutSize(xferButtons);
        Dimension dmin = box.minimumLayoutSize(xferButtons);
        xferButtons.setMaximumSize(new Dimension(dmin.width, dmax.height));

        // make listeners
        addBCButton.addActionListener(new AddRemoveListener());
        removeBCButton.addActionListener(new AddRemoveListener());
        availableList.addListSelectionListener(
                new AvailableListSelectionListener());
        selectedList.addListSelectionListener(
                new SelectedListSelectionListener());

        // put everything together
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(availableScroll);
        add(xferButtons);
        add(Box.createRigidArea(new Dimension(5, 1)));
        add(selectedScroll);
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
     * Add the currently selected baseclass from the meta-classes list
     * to the stereotype.
     */
    private void doAddBaseClass() {
        Object name = availableList.getSelectedValue();
        if (name == null) {
            return;
        }
        Object stereotype = TargetManager.getInstance().getModelTarget();
        if (!Model.getFacade().isAStereotype(stereotype)) {
            return;
        }

        Model.getExtensionMechanismsHelper().addBaseClass(
                stereotype,
                name);
    }

    /**
     * Add the currently selected baseclass from the meta-classes list
     * to the stereotype.
     */
    private void doRemoveBaseClass() {
        Object name = selectedList.getSelectedValue();
        if (name == null) {
            return;
        }
        Object stereotype = TargetManager.getInstance().getModelTarget();
        if (!Model.getFacade().isAStereotype(stereotype)) {
            return;
        }

        Model.getExtensionMechanismsHelper().removeBaseClass(
                stereotype,
                name);
    }
    
    /**
     * A UMLList2 that does not use a custom sellRenderer, 
     * like the UMLLinkedList does. 
     *
     * @author Michiel
     */
    class BCList extends UMLList2 {

        public BCList(ListModel dataModel) {
            super(dataModel, null);
        }
        
    }

    /**
     * The list model for the selected BaseClasses of the stereotype.
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
                addAll(Model.getFacade().getBaseClasses(getTarget()));
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
     * The list model for the available (but not applied) BaseClasses
     * of the stereotype.
     *
     * @author Michiel
     */
    class UMLStereotypeAvailableBaseClassListModel 
        extends UMLModelElementListModel2 {

        private List<String> availableClasses;

        /**
         * Construct the model, listen to changes of "baseClass".
         */
        public UMLStereotypeAvailableBaseClassListModel(
                List<String> metaClassNames) {
            super("baseClass");
            availableClasses = metaClassNames;
        }

        @Override
        protected void buildModelList() {
            removeAllElements();
            if (Model.getFacade().isAStereotype(getTarget())) {
                Collection bases = 
                    Model.getFacade().getBaseClasses(getTarget());
                for (String name : availableClasses) {
                    if (!bases.contains(name)) {
                        addElement(name);
                    }
                }
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
     * Handles pressing the ">>" or "<<" buttons.
     */
    private class AddRemoveListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Object src = e.getSource();
            if (src == addBCButton) {
                doAddBaseClass();
            } else if (src == removeBCButton) {
                doRemoveBaseClass();
            }
        }
    }
    
    /**
     * Handles selection changes in the available metaclasses list.
     */
    private class AvailableListSelectionListener
        implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }
            Object selRule = availableList.getSelectedValue();
            addBCButton.setEnabled(selRule != null);
        }
    }

    /**
     * Handles selection changes in the stereotype's baseclasses list.
     */
    private class SelectedListSelectionListener
        implements ListSelectionListener {

        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }
            Object selRule = selectedList.getSelectedValue();
            removeBCButton.setEnabled(selRule != null);
        }
    }

}