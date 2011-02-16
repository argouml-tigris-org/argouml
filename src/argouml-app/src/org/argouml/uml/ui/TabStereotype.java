/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Michiel ven der Wulp
 *    Laurent Braud
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.swingext.SpacerPanel;
import org.argouml.swingext.UpArrowIcon;
import org.argouml.ui.TabModelTarget;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.StereotypeUtility;
import org.tigris.gef.presentation.Fig;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Vertical;

/**
 * This the tab in the details pane for displaying the stereotypes applied to a
 * model element and allowing adding and removal of stereotypes to that list.<p>
 *
 * The code for the 2 lists and the buttons to move items
 * from one side to the other is based on the PerspectiveConfigurator class.
 */
public class TabStereotype extends PropPanel implements TabModelTarget {

    /**
     * Insets in pixels.
     */
    private static final int INSET_PX = 3;

    private static String orientation =
        Configuration.getString(Configuration
            .makeKey("layout", "tabstereotype"));

    private UMLModelElementStereotypeListModel selectedListModel;
    private UMLModelStereotypeListModel availableListModel;

    private JScrollPane selectedScroll;
    private JScrollPane availableScroll;
    private JPanel panel;
    private JButton addStButton;
    private JButton removeStButton;
    private JPanel xferButtons;
    private JList selectedList;
    private JList availableList;

    /**
     * Construct new Stereotype tab.
     */
    public TabStereotype() {
        super(Translator.localize("tab.stereotype"), (ImageIcon) null);
        setOrientation((orientation
                .equals("West") || orientation.equals("East")) ? Vertical
                .getInstance() : Horizontal.getInstance());
        setIcon(new UpArrowIcon());
        setLayout(new BorderLayout());
        remove(getTitleLabel()); // no title looks better

        panel = makePanel();
        add(panel);
    }

    /**
     * Create a JPanel with everything on it.
     *
     * @return a newly created JPanel.
     */
    private JPanel makePanel() {
        // make lists
        selectedListModel = new UMLModelElementStereotypeListModel();
        selectedList = new UMLStereotypeList(selectedListModel);
        selectedScroll = new JScrollPane(selectedList);
        selectedScroll.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        selectedScroll.setColumnHeaderView(new JLabel(
                Translator.localize("label.applied-stereotypes")));

        availableListModel = new UMLModelStereotypeListModel();
        availableList = new UMLStereotypeList(availableListModel);
        availableScroll = new JScrollPane(availableList);
        availableScroll.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        availableScroll.setColumnHeaderView(new JLabel(
                Translator.localize("label.available-stereotypes")));

        // make buttons
        addStButton = new JButton(">>");
        addStButton.setToolTipText(Translator.localize("button.add-stereo"));
        removeStButton = new JButton("<<");
        removeStButton.setToolTipText(Translator.localize(
                "button.remove-stereo"));
        addStButton.setEnabled(false);
        removeStButton.setEnabled(false);
        addStButton.setMargin(new Insets(2, 15, 2, 15));
        removeStButton.setMargin(new Insets(2, 15, 2, 15));
        addStButton.setPreferredSize(addStButton.getMinimumSize());
        removeStButton.setPreferredSize(removeStButton.getMinimumSize());

        // make buttons layout
        BoxLayout box;
        xferButtons = new JPanel();
        box = new BoxLayout(xferButtons, BoxLayout.Y_AXIS);
        xferButtons.setLayout(box);
        xferButtons.add(new SpacerPanel());
        xferButtons.add(addStButton);
        xferButtons.add(new SpacerPanel());
        xferButtons.add(removeStButton);
        Dimension dmax = box.maximumLayoutSize(xferButtons);
        Dimension dmin = box.minimumLayoutSize(xferButtons);
        xferButtons.setMaximumSize(new Dimension(dmin.width, dmax.height));

        // make listeners
        addStButton.addActionListener(new AddRemoveListener());
        removeStButton.addActionListener(new AddRemoveListener());
        availableList.addListSelectionListener(
                new AvailableListSelectionListener());
        selectedList.addListSelectionListener(
                new SelectedListSelectionListener());

        // put everything together
        JPanel thePanel = new JPanel();
        thePanel.setLayout(new BoxLayout(thePanel, BoxLayout.X_AXIS));
        thePanel.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        thePanel.add(availableScroll);
        thePanel.add(xferButtons);
        thePanel.add(Box.createRigidArea(new Dimension(5, 1)));
        thePanel.add(selectedScroll);

        return thePanel;
    }

    /**
     * Checks if the tab should be enabled. Returns true if the target
     * returned by getTarget is a modelelement or if that target shows up as Fig
     * on the active diagram and has a modelelement as owner.
     *
     * @return true if this tab should be enabled, otherwise false.
     */
    public boolean shouldBeEnabled() {
        Object target = getTarget();
        return shouldBeEnabled(target);
    }
    
    /*
     * @see org.argouml.uml.ui.PropPanel#shouldBeEnabled(java.lang.Object)
     */
    @Override
    public boolean shouldBeEnabled(Object target) {
        if (target instanceof Fig) {
            target = ((Fig) target).getOwner();
        }
        return Model.getFacade().isAModelElement(target);
    }

    /*
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    @Override
    public void setTarget(Object theTarget) {
        super.setTarget(theTarget);
        if (isVisible()) {
            Object me = getModelElement();
            if (me != null) {
                selectedListModel.setTarget(me);
                validate();
            }
        }
    }

    /**
     * Add the currently selected stereotype from the library
     * to the modelelement.
     */
    private void doAddStereotype() {
        Object stereotype = availableList.getSelectedValue();
        Object modelElement = TargetManager.getInstance().getModelTarget();
        if (modelElement == null) {
            return;
        }
        Model.getCoreHelper().addStereotype(modelElement, stereotype);
        ProjectManager.getManager().updateRoots();
    }

    /**
     * Add the currently selected stereotype from the library
     * to the modelelement.
     */
    private void doRemoveStereotype() {
        Object stereotype = selectedList.getSelectedValue();
        Object modelElement = TargetManager.getInstance().getModelTarget();
        if (modelElement == null) {
            return;
        }

        if (Model.getFacade().getStereotypes(modelElement)
                .contains(stereotype)) {
            Model.getCoreHelper().removeStereotype(modelElement, stereotype);
            ProjectManager.getManager().updateRoots();
        }
    }

    /**
     * The list model for all stereotypes available in all the models - except
     * the ones already applied.
     */
    private static class UMLModelStereotypeListModel
        extends UMLStereotypeListModel {

        /**
         * Constructor for UMLModelElementNamespaceListModel.
         */
        public UMLModelStereotypeListModel() {
            super("stereotype");
        }

        /*
         * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
         */
        protected void buildModelList() {
            removeAllElements();
            if (Model.getFacade().isAModelElement(getTarget())) {
                Collection s;
                s = StereotypeUtility.getAvailableStereotypes(getTarget());
                // now remove the ones already applied.
                s.removeAll(Model.getFacade().getStereotypes(getTarget()));
                addAll(s);
            }
        }
        
        
        /**
         * @param e
         * @return
         * @see org.argouml.uml.ui.UMLStereotypeListModel#isValidEvent(java.beans.PropertyChangeEvent)
         * @since 20110215
         */
        private boolean isValidEventRemove(PropertyChangeEvent e) {
            boolean valid = false;
            if (!(getChangedElement(e) instanceof Collection)) {
               
                if ((e.getNewValue() != null && e.getOldValue() == null) 
                        || isValidElement(getChangedElement(e))) {
                    valid = true; // we tried to remove a value
                }
            } else {
                Collection col = (Collection) getChangedElement(e);
                Iterator it = col.iterator();
                if (!col.isEmpty()) {
                    valid = true;
                    while (it.hasNext()) {
                        Object o = it.next();
                        if (!isValidElement(o)) {
                            valid = false;
                            break;
                        }
                    }
                } else {
                    if (e.getOldValue() instanceof Collection
                        && !((Collection) e.getOldValue()).isEmpty()) {
                        valid = true;
                    }
                }
            }
            return valid;
        }
        
        /**
         * Must be different from the extends class because 
         * when we add a sterotype=> remove from available
         *         remove a stereotype=> add to available
         * Called twices: with a AttributeChangeEvent, and with AddAssociationEvent or RemoveAssociationEvent
         * 
         * @param e
         * 
         * @see org.argouml.uml.ui.UMLStereotypeListModel#propertyChange(PropertyChangeEvent)
         * @since 20110215
         */
        public void propertyChange(PropertyChangeEvent e) {
            if (e instanceof AttributeChangeEvent) {
               // ignored this event
            } else if (e instanceof AddAssociationEvent) {
                //Remove the element from Available List
                boolean valid = false;
                if (!(getChangedElement(e) instanceof Collection)) {
                    valid = contains(getChangedElement(e));
                } else {
                    Collection col = (Collection) getChangedElement(e);
                    Iterator it = col.iterator();
                    valid = true;
                    while (it.hasNext()) {
                        Object o = it.next();
                        if (!contains(o)) {
                            valid = false;
                            break;
                        }
                    }
                }
                if (valid) {
                    Object o = getChangedElement(e);
                    if (o instanceof Collection) {
                        Iterator it = ((Collection) o).iterator();
                        while (it.hasNext()) {
                            Object o3 = it.next();
                            removeElement(o3);
                        }
                    } else {
                        removeElement(o);
                    }
                }
                
               
                
            } else if (e instanceof RemoveAssociationEvent) {
                if (isValidEventRemove(e)) {
                    Object o = getChangedElement(e);
                    if (o instanceof Collection) {
                        ArrayList tempList = new ArrayList((Collection) o);
                        Iterator it = tempList.iterator();
                        while (it.hasNext()) {
                            Object o2 = it.next();
                            addElement(o2);
                        }
                    } else {
                        addElement(o); 
                    }
                }
                
            }
        }

        /*
         * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
         */
        protected boolean isValidElement(Object element) {
            return Model.getFacade().isAStereotype(element);
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = 7247425177890724453L;
    }

    /**
     * Handles pressing the ">>" or "<<" buttons.
     */
    private class AddRemoveListener implements ActionListener {
        /*
         * @see java.awt.event.ActionListener#actionPerformed(
         *         java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {

            Object src = e.getSource();
            if (src == addStButton) {
                doAddStereotype();
            } else if (src == removeStButton) {
                doRemoveStereotype();
            }
        }
    }

    /**
     * Handles selection changes in the available stereotypes list.
     */
    private class AvailableListSelectionListener
        implements ListSelectionListener {
        /*
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }

            Object selRule = availableList.getSelectedValue();
            addStButton.setEnabled(selRule != null);
        }
    }

    /**
     * Handles selection changes in the stereotypes list.
     */
    private class SelectedListSelectionListener
        implements ListSelectionListener {
        /*
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }

            Object selRule = selectedList.getSelectedValue();
            removeStButton.setEnabled(selRule != null);
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4741653225927138553L;
}
