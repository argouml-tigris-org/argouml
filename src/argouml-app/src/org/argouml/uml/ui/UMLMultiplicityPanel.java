/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2009 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.uml.ui.behavior.collaborations.ActionSetClassifierRoleMultiplicity;

/**
 * A compound control containing all the visual controls for specifying
 * multiplicity.
 * @author Bob Tarling
 * @since 0.23 alpha2
 */
public class UMLMultiplicityPanel extends JPanel implements ItemListener {

    private JComboBox multiplicityComboBox;
    private JCheckBox checkBox;
    private MultiplicityComboBoxModel multiplicityComboBoxModel;
    
    private static List<String> multiplicityList = new ArrayList<String>();
    
    static {
        multiplicityList.add("1");
        multiplicityList.add("0..1");
        multiplicityList.add("0..*");
        multiplicityList.add("1..*");
    }

    /**
     * Constructor
     */
    public UMLMultiplicityPanel() {
        super(new BorderLayout());
        
        multiplicityComboBoxModel =
            new MultiplicityComboBoxModel("multiplicity");
        
        checkBox = new MultiplicityCheckBox();
        multiplicityComboBox =
		new MultiplicityComboBox(
		        multiplicityComboBoxModel,
		        ActionSetClassifierRoleMultiplicity.getInstance());
        multiplicityComboBox.setEditable(true);
        multiplicityComboBox.addItemListener(this);
        add(checkBox, BorderLayout.WEST);
        add(multiplicityComboBox, BorderLayout.CENTER);
    }
    
    /**
     * Enforce that the preferred height is the minimum height.
     * This works around a bug in Windows LAF of JRE5 where a change
     * in the preferred/min size of a combo has changed and has a knock
     * on effect here.
     * If the layout manager for prop panels finds the preferred
     * height is greater than the minimum height then it will allow
     * this component to resize in error.
     * See issue 4333 - Sun has now fixed this bug in JRE6 and so this
     * method can be removed once JRE5 is no longer supported.
     * @return the preferred dimension
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                super.getPreferredSize().width,
                getMinimumSize().height);
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getSource() == multiplicityComboBox && getTarget() != null) {
            Object item = multiplicityComboBox.getSelectedItem();
            Object target = multiplicityComboBoxModel.getTarget();
            Object multiplicity = Model.getFacade().getMultiplicity(target);
            if (Model.getFacade().isAMultiplicity(item)) {
                if (!item.equals(multiplicity)) {
                    Model.getCoreHelper().setMultiplicity(target, item);
                    delete(multiplicity);
                }
            } else if (item instanceof String) {
                if (!item.equals(Model.getFacade().toString(multiplicity))) {
                    Model.getCoreHelper().setMultiplicity(
                            target,
                            Model.getDataTypesFactory().createMultiplicity(
                                    (String) item));
                    delete(multiplicity);
                }
            } else {
                if (multiplicity != null) {
                    Model.getCoreHelper().setMultiplicity(target, null);
                    delete(multiplicity);
                }
            }
        }
    }

    private void delete(Object multiplicity) {
        if ("1.4".equals(Model.getFacade().getUmlVersion()) 
                && multiplicity != null) {
            // For UML 1.4 Multiplicities are value objects, but
            // for UML 2.x, the bounds are contained by the Property
            // so they shouldn't be cleaned up
            Model.getUmlFactory().delete(multiplicity);
        }
    }
    
    private Object getTarget() {
	return multiplicityComboBoxModel.getTarget();
    }

    /**
     * An editable and searchable combobox to edit the multiplicity attribute of
     * some modelelement.
     *
     * @author jaap.branderhorst@xs4all.nl
     * @since Jan 5, 2003
     */
    private class MultiplicityComboBox extends UMLSearchableComboBox {

        /**
         * Constructor for UMLMultiplicityComboBox2.
         * @param arg0 the combobox model
         * @param selectAction the action
         */
        public MultiplicityComboBox(UMLComboBoxModel2 arg0,
                Action selectAction) {
            super(arg0, selectAction);
        }

        /**
         * On enter, the text the user has filled in the textfield is first
         * checked to see if it's a valid multiplicity. If so then that is the
         * multiplicity to be set. If not, the combobox searches for a
         * multiplicity starting with the given text. If there is no
         * multiplicity starting with the given text, the old value is reset
         * in the comboboxeditor.
         * 
         * {@inheritDoc}
         */
        @Override
        protected void doOnEdit(Object item) {
            String text = (String) item;
            try {
                Object multi = 
                    Model.getDataTypesFactory().createMultiplicity(text);
                if (multi != null) {
                    setSelectedItem(text);
                    delete(multi);
                    return;
                }
            } catch (IllegalArgumentException e) {
                Object o = search(text);
                if (o != null ) {
                    setSelectedItem(o);
                    return;
                }
            }
            getEditor().setItem(getSelectedItem());
        }

        /**
         * When we change target make sure that the check box is only selected
         * if the multiplicity exists
         * @param e
         * @see org.argouml.uml.ui.UMLComboBox2#targetSet(org.argouml.ui.targetmanager.TargetEvent)
         */
        @Override
	public void targetSet(TargetEvent e) {
	    super.targetSet(e);
	    Object target = getTarget();
	    boolean exists = target != null 
                && Model.getFacade().getMultiplicity(target) != null;
	    multiplicityComboBox.setEnabled(exists);
	    multiplicityComboBox.setEditable(exists);
	    // This will cause itemStateChanged to be called because of
	    // us rather than a user action, but we don't care because we're
	    // going to check if the value is the same
	    checkBox.setSelected(exists);
	}
    }
    
    
    /**
     * A model for multiplicities.
     */
    private class MultiplicityComboBoxModel
        extends UMLComboBoxModel2 {

        /**
         * Constructor for UMLMultiplicityComboBoxModel.
         *
         * @param propertySetName the name of the property set
         */
        public MultiplicityComboBoxModel(String propertySetName) {
            super(propertySetName, false);
        }
    
        /*
         * @see org.argouml.uml.ui.UMLComboBoxModel2#isValidElement(Object)
         */
        protected boolean isValidElement(Object element) {
            return element instanceof String;
        }
    
        /*
         * @see org.argouml.uml.ui.UMLComboBoxModel2#buildModelList()
         */
        protected void buildModelList() {
            setElements(multiplicityList);
            Object t = getTarget();
            if (Model.getFacade().isAModelElement(t)) {
                addElement(Model.getFacade().getMultiplicity(t));
            }
        }
    
        /*
         * @see org.argouml.uml.ui.UMLComboBoxModel2#addElement(java.lang.Object)
         */
        @Override
        public void addElement(Object o) {
            if (o == null) {
                return;
            }
            String text;
            if (Model.getFacade().isAMultiplicity(o)) {
                text = Model.getFacade().toString(o);
                if ("".equals(text)) {
                    text = "1";
                }
            } else if (o instanceof String) {
                text = (String) o;
            } else {
                return;
            }
            if (!multiplicityList.contains(text) && isValidElement(text)) {
                multiplicityList.add(text);
            }
            super.addElement(text);
        }
    
        /*
         * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
         */
        @Override
        public void setSelectedItem(Object anItem) {
            addElement(anItem);
            super.setSelectedItem((anItem == null) ? null 
                    : Model.getFacade().toString(anItem));
        }

        protected Object getSelectedModelElement() {
            if (getTarget() != null) {
                return Model.getFacade().toString(
                        Model.getFacade().getMultiplicity(getTarget()));
            }
            return null;
        }
    }
    
    private class MultiplicityCheckBox extends JCheckBox
        implements ItemListener {
	
	public MultiplicityCheckBox() {
	    addItemListener(this);
	}

	public void itemStateChanged(ItemEvent e) {
	    Object target = getTarget();
	    Object oldValue = Model.getFacade().getMultiplicity(target);
	    // Note: MultiplicityComboBox.targetSet() can cause this event
	    // as well as user actions, so be sure to consider this in 
	    // changing the following logic 
	    if (e.getStateChange() == ItemEvent.SELECTED) {
                String comboText = 
                    (String) multiplicityComboBox.getSelectedItem();
                if (oldValue == null
                        || !comboText.equals(Model.getFacade().toString(
                                oldValue))) {
                    Object multi = Model.getDataTypesFactory()
                            .createMultiplicity(comboText);
                    if (multi == null) {
                        Model.getCoreHelper().setMultiplicity(target, "1");
                    } else {
                        Model.getCoreHelper().setMultiplicity(target, multi);
                    }
                    delete(oldValue);
                }
		multiplicityComboBox.setEnabled(true);
		multiplicityComboBox.setEditable(true);
	    } else {
		multiplicityComboBox.setEnabled(false);
		multiplicityComboBox.setEditable(false);
                Model.getCoreHelper().setMultiplicity(target, null);
                delete(oldValue);
	    }
	}
    }
}
