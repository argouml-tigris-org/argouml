/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2002-2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.SwingUtilities;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.uml.ui.AbstractActionAddModelElement2;
import org.argouml.uml.ui.AbstractActionNewModelElement;
import org.argouml.uml.ui.AbstractActionRemoveElement;
import org.tigris.toolbar.ToolBar;

/**
 * The model for a list that contains ModelElements. The state of the Element is
 * still kept in the model subsystem itself. This list is only to be used as the
 * model for some GUI element like UMLLinkedList.
 *
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
abstract class UMLModelElementListModel
        extends DefaultListModel implements PropertyChangeListener {

    private static final Logger LOG =
        Logger.getLogger(UMLModelElementListModel.class.getName());

    private String eventName = null;
    private Object listTarget = null;

    private AbstractActionAddModelElement2 addAction = null;
    private AbstractActionNewModelElement newAction = null;
    private AbstractActionRemoveElement removeAction = null;

    private static int count = 0;
    private final int instance = count++;

    /**
     * Flag to indicate whether list events should be fired
     */
    private boolean fireListEvents = true;

    /**
     * Flag to indicate whether the model is being build
     */
    private boolean buildingModel = false;

    /**
     * The type of model elements this list model is designed to hold.
     */
    private Object metaType;

    /**
     * Indicates that drops onto this list should connect in the opposite
     * way to standard.
     */
    private boolean reverseDropConnection;

    /**
     * True if the icon for the modelelement should be shown. The icon is, for
     * instance, a small class symbol for a class.
     */
    final private boolean showIcon;

    /**
     * True if the containment path should be shown
     * (to help the user disambiguate elements with the same name);
     */
    final private boolean showPath;


    /**
     * Constructor to be used if the subclass does not depend on the
     * MELementListener methods and setTarget method implemented in this
     * class.
     */
    UMLModelElementListModel() {
        super();
        showIcon = true;
        showPath = true;
    }

    /**
     * Constructor for UMLModelElementListModel2.
     *
     * @param name the name of the event to listen to, which triggers us
     *             to update the list model from the UML data
     */
    UMLModelElementListModel(String name) {
        super();
        eventName = name;
        showIcon = true;
        showPath = true;
    }

    UMLModelElementListModel(
            final String name,
            final boolean showIcon,
            final boolean showPath) {
        super();
        eventName = name;
        this.showIcon = showIcon;
        this.showPath = showPath;
    }

    UMLModelElementListModel(
            final String name,
            final boolean showIcon,
            final boolean showPath,
            final Object metaType) {
        super();
        eventName = name;
        this.showIcon = showIcon;
        this.showPath = showPath;
        this.metaType = metaType;
    }

    /**
     * Constructor for UMLModelElementListModel2.
     *
     * @param name the name of the event to listen to, which triggers us
     *             to update the list model from the UML data
     * @param theMetaType the type of model element that the list model
     *                 is designed to contain.
     */
    UMLModelElementListModel(String name, Object theMetaType) {
        super();
        this.metaType = theMetaType;
        eventName = name;
        showIcon = true;
        showPath = true;
    }

    /**
     * Constructor for UMLModelElementListModel2.
     *
     * @param name the name of the event to listen to, which triggers us
     *             to update the list model from the UML data
     * @param theMetaType the type of model element that the list model
     *                 is designed to contain.
     */
    public UMLModelElementListModel(
            final String name,
            final Object theMetaType,
            final AbstractActionAddModelElement2 addAction,
            final AbstractActionNewModelElement newAction,
            final AbstractActionRemoveElement removeAction) {
        super();
        this.metaType = theMetaType;
        eventName = name;
        showIcon = true;
        showPath = true;
        this.addAction = addAction;
        this.newAction = newAction;
        this.removeAction = removeAction;
    }

    public UMLModelElementListModel(
            final String name,
            final Object theMetaType,
            final AbstractActionAddModelElement2 addAction) {
        super();
        this.metaType = theMetaType;
        eventName = name;
        showIcon = true;
        showPath = true;
        this.addAction = addAction;
    }

    public UMLModelElementListModel(
            final String name,
            final Object theMetaType,
            final AbstractActionNewModelElement newAction) {
        super();
        this.metaType = theMetaType;
        eventName = name;
        showIcon = true;
        showPath = true;
        this.newAction = newAction;
    }

    public UMLModelElementListModel(
            final String name,
            final Object theMetaType,
            final AbstractActionAddModelElement2 addAction,
            final AbstractActionRemoveElement removeAction) {
        super();
        this.metaType = theMetaType;
        eventName = name;
        showIcon = true;
        showPath = true;
        this.addAction = addAction;
        this.removeAction = removeAction;
    }

    public UMLModelElementListModel(
            final String name,
            final Object theMetaType,
            final AbstractActionAddModelElement2 addAction,
            final AbstractActionNewModelElement newAction) {
        super();
        this.metaType = theMetaType;
        eventName = name;
        showIcon = true;
        showPath = true;
        this.addAction = addAction;
        this.newAction = newAction;
    }

    /**
     * Constructor for UMLModelElementListModel2.
     *
     * @param name the name of the event to listen to, which triggers us
     *             to update the list model from the UML data
     * @param theMetaType the type of model element that the list model
     *                 is designed to contain.
     * @param reverseTheDropConnection tells the JList to reverse the
     *              connection made and drop during dnd.
     */
    public UMLModelElementListModel(
	    String name,
	    Object theMetaType,
	    boolean reverseTheDropConnection) {
        super();
        this.metaType = theMetaType;
        eventName = name;
        this.reverseDropConnection = reverseTheDropConnection;
        showIcon = true;
        showPath = true;
    }

    /**
     * Get the type of objects that this list model is designed to contain.
     * @return metaType the meta type.
     */
    public Object getMetaType() {
	return metaType;
    }

    public boolean isReverseDropConnection() {
	return reverseDropConnection;
    }

    /**
     * @param building The buildingModel to set.
     */
    protected void setBuildingModel(boolean building) {
        this.buildingModel = building;
    }

    /**
     * @param t the list target to set
     */
    protected void setListTarget(Object t) {
        this.listTarget = t;
    }

    /*
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     *
     * TODO: This should be reviewed to see if it can be improved with a view
     * towards removing some of the overrriding methods used as workarounds for
     * differences between NSUML and MDR - tfm - 20060302
     */
    public void propertyChange(final PropertyChangeEvent e) {
        final UMLModelElementListModel lm = this;
        Runnable doWorkRunnable = new Runnable() {
            public void run() {
                try {
// The original controls on property panels regularly rebuild themselves
// from scratch rather than add and remove elements
// For now we just try and add/remove but we can reinstate this if required.
//
//                    if (e instanceof AttributeChangeEvent) {
//                        try {
//                            if (isValidEvent(e)) {
//                                LOG.log(Level.INFO, "Rebuilding model");
//                                rebuildModelList();
//                            }
//                        } catch (InvalidElementException iee) {
//                            return;
//                        }
//                    } else
                    if (e instanceof AddAssociationEvent) {
                        if (isValidEvent(e)) {
                            Object o = getChangedElement(e);
                            if (o instanceof Collection) {
                                LOG.log(Level.INFO, "Elements added");
                                ArrayList tempList = new ArrayList((Collection) o);
                                Iterator it = tempList.iterator();
                                while (it.hasNext()) {
                                    Object o2 = it.next();
                                    addElement(o2);
                                }
                            } else {
                                /* TODO: If this is an ordered list, then you have to
                                    add in the right location! */
                                if (!contains(o)) {
                                    if (lm instanceof Ordered) {
                                        Ordered ordered = (Ordered) lm;
                                        Collection elements = ordered.getModelElements();
                                        if (elements instanceof List) {
                                            final int posn = ((List) elements).indexOf(o);
                                            // We tested this above - do we need to test again?
                                            add(posn, o);
                                        } else {
                                            int posn = 0;
                                            for (Object element : elements) {
                                                if (element == o) {
                                                    break;
                                                }
                                                posn++;
                                            }
                                            add(posn, o);
                                        }
                                    } else {
                                        addElement(o);
                                    }
                                }
                            }
                        }
                    } else if (e instanceof RemoveAssociationEvent) {
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
                    }
                } catch (InvalidElementException e) {
                    LOG.log(Level.FINE,
                            "updateLayout method accessed deleted element ",
                            e);
                }
            }
        };
        SwingUtilities.invokeLater(doWorkRunnable);


    }

    /**
     * Delete and rebuild the model list from scratch.
     */
    private void rebuildModelList() {
        LOG.log(Level.INFO, "Rebuilding");
        removeAllElements();
        buildingModel = true;
        try {
            buildModelList();
        } catch (InvalidElementException exception) {
            /*
             * This can throw an exception if the target has been
             * deleted. We don't want to try locking the repository
             * because this is called from the event delivery thread and
             * could cause a deadlock. Instead catch the exception and
             * leave the model empty.
             */
            LOG.log(Level.FINE, "buildModelList threw exception for target "
                    + getTarget() + ": "
                    + exception);
        } finally {
            buildingModel = false;
        }
        if (getSize() > 0) {
            fireIntervalAdded(this, 0, getSize() - 1);
        }
    }

    /**
     * Builds the list of elements. Called from targetChanged every time the
     * target of the proppanel is changed. Usually the method setAllElements is
     * called with the result.
     */
    protected abstract void buildModelList();

    /**
     * Utility method to set the elements of this list to the contents of the
     * given collection.
     * @param col the given collection
     */
    protected void setAllElements(Collection col) {
        if (!isEmpty())
            removeAllElements();
        addAll(col);
    }

    /**
     * Utility method to add a collection of elements to the model
     * @param col the given collection
     */
    protected void addAll(Collection col) {
        if (col.size() == 0) return;
        Iterator it = col.iterator();
        fireListEvents = false;
        int intervalStart = getSize() == 0 ? 0 : getSize() - 1;
        while (it.hasNext()) {
            Object o = it.next();
            addElement(o);
        }
        fireListEvents = true;
        fireIntervalAdded(this, intervalStart, getSize() - 1);
    }

    /**
     * Utility method to get the target. Sets the target if the target is null
     * via the method setTarget().
     * @return MModelElement
     */
    public Object getTarget() {
        return listTarget;
    }

    /**
     * Utility method to get the changed element from some event e
     * @param e the event
     * @return Object the changed element
     */
    protected Object getChangedElement(PropertyChangeEvent e) {
        if (e instanceof AssociationChangeEvent) {
            return ((AssociationChangeEvent) e).getChangedValue();
        }
        if (e instanceof AttributeChangeEvent) {
            return ((AttributeChangeEvent) e).getSource();
        }
        return e.getNewValue();
    }

    /*
     * @see javax.swing.DefaultListModel#contains(java.lang.Object)
     */
    public boolean contains(Object elem) {
        if (super.contains(elem)) {
            return true;
        }
        if (elem instanceof Collection) {
            Iterator it = ((Collection) elem).iterator();
            while (it.hasNext()) {
                if (!super.contains(it.next())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Sets the target. If the old target is a ModelElement, it also removes
     * the model from the element listener list of the target. If the new target
     * is instanceof ModelElement, the model is added as element listener to the
     * new target. <p>
     *
     * This function is called when the user changes the target.
     * Hence, this shall not result in any UML model changes.
     * Hence, we block firing list events completely by setting
     * buildingModel to true for the duration of this function. <p>
     *
     * This function looks a lot like the one in UMLComboBoxModel.
     *
     * @param theNewTarget the new target
     */
    protected void setTarget(final Object theNewTarget) {
        assert (getTarget() == null);
        assert (Model.getFacade().isAUMLElement(theNewTarget));

        listTarget = theNewTarget;
        Model.getPump().addModelEventListener(this, listTarget, eventName);
        // Allow listening to other elements:
        addOtherModelEventListeners(listTarget);

        rebuildModelList();
    }

    public void removeModelEventListener() {
        Model.getPump().removeModelEventListener(this, listTarget, eventName);
    }

    /**
     * This function allows subclasses to listen to more modelelements.
     * The given target is guaranteed to be a UML modelelement.
     *
     * @param oldTarget the UML modelelement
     */
    protected void removeOtherModelEventListeners(Object oldTarget) {
        /* Do nothing by default. */
    }

    /**
     * This function allows subclasses to listen to more modelelements.
     * The given target is guaranteed to be a UML modelelement.
     *
     * @param newTarget the UML modelelement
     */
    protected void addOtherModelEventListeners(Object newTarget) {
        /* Do nothing by default. */
    }

    /**
     * Returns true if the given element is valid, i.e. it may be added to the
     * list of elements.
     *
     * @param element the element to be tested
     * @return true if valid
     */
    protected abstract boolean isValidElement(Object element);

    /**
     * Returns true if some event is valid. An event is valid if the
     * element changed in the event is valid. This is determined via a
     * call to isValidElement.  This method can be overriden by
     * subclasses if they cannot determine if it is a valid event just
     * by checking the changed element.
     *
     * @param e the event
     * @return boolean true if valid
     */
    protected boolean isValidEvent(PropertyChangeEvent e) {
        boolean valid = false;
        if (!(getChangedElement(e) instanceof Collection)) {
            // TODO: Considering all delete events to be valid like below
            // is going to cause lots of unecessary work and some problems
            if ((e.getNewValue() == null && e.getOldValue() != null)
                    // Don't test changed element if it was deleted
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

    /*
     * @see javax.swing.DefaultListModel#addElement(java.lang.Object)
     */
    public void addElement(Object obj) {
        if (obj != null && !contains(obj)) {
            super.addElement(obj);
        }
    }

    /**
     * Returns the eventName. This method is only here for testing goals.
     * @return String
     */
    String getEventName() {
        return eventName;
    }

    /**
     * Sets the eventName. The eventName is the name of the
     * MElementEvent to which the list should listen. The list is
     * registred with UMLModelEventPump and only gets events that have
     * a name like eventName.  This method should be called in the
     * constructor of every subclass.
     *
     * @param theEventName The eventName to set
     */
    protected void setEventName(String theEventName) {
        eventName = theEventName;
    }

    /*
     * @see javax.swing.AbstractListModel#fireContentsChanged(
     *          Object, int, int)
     */
    protected final void fireContentsChanged(Object source, int index0, int index1) {
        if (fireListEvents && !buildingModel)
            super.fireContentsChanged(source, index0, index1);
    }

    /*
     * @see javax.swing.AbstractListModel#fireIntervalAdded(
     *          Object, int, int)
     */
    protected final void fireIntervalAdded(Object source, int index0, int index1) {
        if (fireListEvents && !buildingModel)
            super.fireIntervalAdded(source, index0, index1);
    }

    /*
     * @see javax.swing.AbstractListModel#fireIntervalRemoved(
     *          Object, int, int)
     */
    protected final void fireIntervalRemoved(Object source, int index0, int index1) {
        if (fireListEvents && !buildingModel)
            super.fireIntervalRemoved(source, index0, index1);
    }

    /**
     * Override this if you want a popup menu.
     * See for an example UMLModelElementOrderedListModel2.
     *
     * @param popup the popup menu
     * @param index the selected item in the list at the moment
     *              the mouse was clicked
     * @return true if a popup menu is created, and needs to be shown
     */
    public boolean buildPopup(JPopupMenu popup, int index) {
        return false;
    }

    public List<Action> getActions() {
        final List<Action> actions = new ArrayList<Action>();
        final JPopupMenu popup = new JPopupMenu();
        buildPopup(popup, 0);
        final MenuElement[] elements = popup.getSubElements();
        for (MenuElement element : elements) {
            if (element instanceof JMenuItem) {
                actions.add(((JMenuItem) element).getAction());
            }
        }
        return actions;
    }

    /**
     * Get a toolbar containing all the actions available on this JList.
     * @return the toolbar.
     */
    public ToolBar getToolbar() {
        // TODO: First implementation will be to call buildPopop
        // and extract all the contained actions. The toolbar will
        // be built to the same structure.
        // The toolbar will be displayed to the left of the JList
        // under the label.
        return null;
    }

    protected boolean hasPopup() {
        return false;
    }

    boolean isShowIcon() {
        return showIcon;
    }

    boolean isShowPath() {
        return showPath;
    }

    public AbstractActionAddModelElement2 getAddAction() {
        return addAction;
    }

    public AbstractActionNewModelElement getNewAction() {
        return newAction;
    }

    public AbstractActionRemoveElement getRemoveAction() {
        return removeAction;
    }

    public List<Action> getNewActions() {
    	return Collections.emptyList();
    }
}
