/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractListModel;
import javax.swing.Action;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import org.argouml.model.AddAssociationEvent;
import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.DeleteInstanceEvent;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;
import org.argouml.model.RemoveAssociationEvent;
import org.argouml.model.UmlChangeEvent;

/**
 * ComboBox Model for UML modelelements. <p>
 *
 * This combobox allows selecting no value, if so indicated
 * at construction time of this class. I.e. it is "clearable".
 */
abstract class UMLComboBoxModel extends AbstractListModel
        implements PropertyChangeListener, ComboBoxModel, PopupMenuListener {

    /**
     * The UID
     */
    private static final long serialVersionUID = 6038919811554379037L;

    private static final Logger LOG =
        Logger.getLogger(UMLComboBoxModel.class.getName());

    /**
     * The target of the comboboxmodel. This is some UML modelelement
     */
    private Object target = null;

    /**
     * The list with objects that should be shown in the combobox.
     * TODO: Using a list here forces a linear search when we're trying to add
     * a new element to the model which can be very slow for large models.
     */
    private List objects = new LinkedList();

    /**
     * The selected object.
     */
    private Object selectedObject = null;

    /**
     * Flag to indicate if the user may select the special CLEARED choice
     * ("<none>") as value in the combobox. If true the attribute that is shown
     * by this combobox may be set to null. Makes sure that there is always an
     * entry in the list with objects so the user has the opportunity to select
     * this to clear the attribute.
     */
    private boolean isClearable = false;

    /**
     * The name of the property that we will use to listen for change events
     * associated with this model element.
     */
    private String propertySetName;

    /**
     * Flag to indicate whether list events should be fired.
     */
    private boolean fireListEvents = true;

    /**
     * Flag to indicate whether the model is being build.
     */
    private boolean buildingModel = false;

    /**
     * Flag needed to prevent infinite recursion during processing of
     * popup visibility notification event.
     */
    private boolean processingWillBecomeVisible = false;

    private boolean modelValid;

    private String filter;


    /**
     * Constructs a model for a combobox. The container given is used to
     * retrieve the target that is manipulated through this combobox. If
     * clearable is true, the user can select null in the combobox and thereby
     * clear the attribute in the model.
     *
     * @param name The name of the property change event that must be fired to
     *            set the selected item programmatically (via changing the
     *            model)
     * @param clearable Flag to indicate if the user may select the special
     *            CLEARED value (<none>) as value in the combobox. If true the
     *            attribute that is shown by this combobox may be set to null.
     *            Makes sure that there is always an entry for this in the list
     *            with objects so the user has the opportunity to select this to
     *            clear the attribute.
     * @throws IllegalArgumentException if one of the arguments is null
     */
    public UMLComboBoxModel(Object target, String name, boolean clearable) {
        super();
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("A property name must be provided");
        }
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        // It would be better if we didn't need the container to get
        // the target. This constructor can have zero parameters as
        // soon as we improve targetChanged.
        isClearable = clearable;
        propertySetName = name;
        modelValid = false;

        /* Add new listeners: */
        this.target = target;
        Model.getPump().addModelEventListener(this, target,
                propertySetName);
        // Allow listening to other elements:
        addOtherModelEventListeners(target);

        buildingModel = true;
        buildMinimalModelList();
        // Do not set buildingModel = false here,
        // otherwise the action for selection is performed.
        setSelectedItem(getSelectedModelElement());
        buildingModel = false;

        if (getSize() > 0) {
            fireIntervalAdded(this, 0, getSize() - 1);
        }
        if (getSelectedItem() != null && isClearable) {
            addElement(null); // makes sure we can select 'none'
        }
    }

    public final void propertyChange(final PropertyChangeEvent pve) {
        if (pve instanceof UmlChangeEvent) {
            final UmlChangeEvent event = (UmlChangeEvent) pve;

            Runnable doWorkRunnable = new Runnable() {
                public void run() {
                    try {
                        modelChanged(event);
                    } catch (InvalidElementException e) {
                        LOG.log(Level.FINE, "event = {0}", event);
                        LOG.log(Level.FINE,
                                "updateLayout method accessed deleted element ",
                                e);
                    }
                }
            };
            SwingUtilities.invokeLater(doWorkRunnable);
        }
    }



    /**
     * If the property that this comboboxmodel depicts is changed in the UML
     * model, this method will make sure that the changes will be
     * done in the combobox-model equally. <p>
     * TODO: This function is not yet completely written!
     *
     * {@inheritDoc}
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void modelChanged(UmlChangeEvent evt) {
        buildingModel = true;
        if (evt instanceof DeleteInstanceEvent) {
            if (contains(getChangedElement(evt))) {
                Object o = getChangedElement(evt);
                removeElement(o);
            }
        } else if (evt instanceof AddAssociationEvent) {
            if (getTarget() != null && isValidEvent(evt)) {
                if (evt.getPropertyName().equals(propertySetName)
                    && (evt.getSource() == getTarget())) {
                    Object elem = evt.getNewValue();
                    if (elem != null && !contains(elem)) {
                        addElement(elem);
                    }
                    /* MVW: for this case, I had to move the
                     * call to setSelectedItem() outside the "buildingModel",
                     * otherwise the combo does not update
                     * with the new selection. See issue 5418.
                     **/
                    buildingModel = false;
                    setSelectedItem(elem);
                } else {
                    Object o = getChangedElement(evt);
                    addElement(o);
                }
            }
        } else if (evt instanceof RemoveAssociationEvent && isValidEvent(evt)) {
            if (evt.getPropertyName().equals(propertySetName)
                    && (evt.getSource() == getTarget())) {
                if (evt.getOldValue() == getSelectedItem()) {
                    /* TODO: Here too? */
                    setSelectedItem(evt.getNewValue());
                }
            } else {
                Object o = getChangedElement(evt);
                if (contains(o)) {
                    removeElement(o);
                }
            }
        }
        buildingModel = false;
    }

    /**
     * Returns true if the given element is valid.<p>
     *
     * It is valid if it may be added to the list of elements.
     *
     * @param element the given element
     * @return true if the given element is valid
     */
    protected abstract boolean isValidElement(Object element);

    /**
     * Builds the list of elements and sets the selectedIndex to the currently
     * selected item if there is one. Called from targetChanged every time the
     * target of the proppanel is changed.
     */
    protected abstract void buildModelList();

    /**
     * @param obj an UML object
     * @return its name or "" (if it was not named or deleted)
     */
    protected String getName(Object obj) {
        try {
            Object n = Model.getFacade().getName(obj);
            String name = (n != null ? (String) n : "");
            return name;
        } catch (InvalidElementException e) {
            return "<invalid>";
        }
    }

    /**
     * Utility method to change all elements in the list with modelelements
     * at once.  A minimal update strategy is used to minimize event firing
     * for unchanged elements.
     *
     * @param elements the given elements
     */
    protected void setElements(Collection elements) {
        if (elements != null) {
            ArrayList toBeRemoved = new ArrayList();
            for (Object o : objects) {
                if (!elements.contains(o)
                        && !(isClearable
                                // Check against "" is needed for backward
                                // compatibility.  Don't remove without
                                // checking subclasses and warning downstream
                                // developers - tfm - 20081211
                                && (o == null || "".equals(o)))) {
                    toBeRemoved.add(o);
                }
            }
            removeAll(toBeRemoved);
            addAll(elements);

            if (isClearable && !elements.contains(null)) {
                addElement(null);
            }
            if (!objects.contains(selectedObject)) {
                selectedObject = null;
            }
        } else {
            throw new IllegalArgumentException("In setElements: may not set "
                                               + "elements to null collection");
        }
    }

    /**
     * Utility method to get the target.
     *
     * @return  the ModelElement
     */
    protected Object getTarget() {
        return target;
    }

    /**
     * Utility method to remove a collection of elements from the model.
     *
     * @param col the elements to be removed
     */
    protected void removeAll(Collection col) {
        int first = -1;
        int last = -1;
        fireListEvents = false;
        for (Object o : col) {
            int index = getIndexOf(o);
            removeElement(o);
            if (first == -1) { // start of interval
                first = index;
                last = index;
            } else {
                if (index  != last + 1) { // end of interval
                    fireListEvents = true;
                    fireIntervalRemoved(this, first, last);
                    fireListEvents = false;
                    first = index;
                    last = index;
                } else { // in middle of interval
                    last++;
                }
            }
        }
        fireListEvents = true;
    }

    /**
     * Utility method to add a collection of elements to the model.
     *
     * @param col the elements to be addd
     */
    protected void addAll(Collection col) {
        Object selected = getSelectedItem();
        fireListEvents = false;
        int oldSize = objects.size();
        for (Object o : col) {
            if (filter != null && filter.length() > 0) {
        	if (Model.getFacade().getName(o).toLowerCase().startsWith(filter.toLowerCase())) {
                    addElement(o);
        	}
            } else {
                addElement(o);
            }
        }
        setSelectedItem(selected);
        fireListEvents = true;
        if (objects.size() != oldSize) {
            fireIntervalAdded(this, oldSize == 0 ? 0 : oldSize - 1,
                    objects.size() - 1);
        }
    }

    /**
     * Utility method to get the changed element from some event e.
     *
     * @param e the given event
     * @return Object the changed element
     */
    protected Object getChangedElement(PropertyChangeEvent e) {
        if (e instanceof AssociationChangeEvent) {
            return ((AssociationChangeEvent) e).getChangedValue();
        }
        return e.getNewValue();
    }

    /**
     * Sets the target. If the old target is a ModelElement, it also removes
     * the model from the element listener list of the target. If the new target
     * is a ModelElement, the model is added as element listener to the new
     * target. <p>
     *
     * This function is called when the user changes the target.
     * Hence, this shall not result in any UML model changes.
     * Hence, we block firing list events completely by setting
     * buildingModel to true for the duration of this function. <p>
     *
     * This function looks a lot like the one in UMLModelElementListModel2.
     *
     * @param theNewTarget the target
     */
    final protected void setTarget(Object theNewTarget) {
        assert (getTarget() == null);
        assert (theNewTarget != null);
        modelValid = false;

        LOG.log(Level.FINE, "setTarget target: {0}", theNewTarget);

        /* Add new listeners: */
        target = theNewTarget;
        Model.getPump().addModelEventListener(this, target,
                propertySetName);
        // Allow listening to other elements:
        addOtherModelEventListeners(target);

        buildingModel = true;
        buildMinimalModelList();
        // Do not set buildingModel = false here,
        // otherwise the action for selection is performed.
        setSelectedItem(getSelectedModelElement());
        buildingModel = false;

        if (getSize() > 0) {
            fireIntervalAdded(this, 0, getSize() - 1);
        }
        if (getSelectedItem() != null && isClearable) {
            addElement(null); // makes sure we can select 'none'
        }
    }

    public void removeModelEventListener() {
        Model.getPump().removeModelEventListener(this, target,
                propertySetName);
        removeOtherModelEventListeners(target);
    }


    /**
     * Build the minimal number of items in the model for the edit box
     * to be populated. By default this calls buildModelList but it
     * can be overridden in subclasses to delay population of the list
     * till the list is displayed. <p>
     *
     * If this lazy list building is used, do call setModelInvalid() here!
     */
    protected void buildMinimalModelList() {
        buildModelListTimed();
    }

    private void buildModelListTimed() {
        long startTime = System.currentTimeMillis();
        try {
            buildModelList();

            if (LOG.isLoggable(Level.FINE)) {
                long endTime = System.currentTimeMillis();
                LOG.log(Level.FINE,
                        "buildModelList took " + (endTime - startTime)
                        + " msec. for " + this.getClass().getName());
            }
        } catch (InvalidElementException e) {
            LOG.log(Level.WARNING,
                    "buildModelList attempted to operate on "
                    + "deleted element");
        }
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
     * Gets the modelelement that is selected in the UML model. For
     * example, say that this ComboBoxmodel contains all namespaces
     * (as in UMLNamespaceComboBoxmodel) , this method should return
     * the namespace that owns the target then.
     *
     * @return Object
     */
    protected abstract Object getSelectedModelElement();

    /*
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int index) {
        if (index >= 0 && index < objects.size()) {
            return objects.get(index);
        }
        return null;
    }

    /*
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return objects.size();
    }

    /**
     * @param o the given element
     * @return the index of the given element
     */
    public int getIndexOf(Object o) {
        return objects.indexOf(o);
    }

    /**
     * @param o the element to be added
     */
    public void addElement(Object o) {
        // TODO: For large lists, this is doing a linear search of literally thousands of elements
        if (!objects.contains(o)) {
            objects.add(o);
            fireIntervalAdded(this, objects.size() - 1, objects.size() - 1);
        }
    }

    /*
     * @see javax.swing.ComboBoxModel#setSelectedItem(java.lang.Object)
     */
    public void setSelectedItem(Object o) {
        if ((selectedObject != null && !selectedObject.equals(o))
                || (selectedObject == null && o != null)) {
            selectedObject = o;
            fireContentsChanged(this, -1, -1);
        }
    }

    /**
     * @param o the element to be removed
     */
    public void removeElement(Object o) {
        int index = objects.indexOf(o);
        if (getElementAt(index) == selectedObject) {
            if (!isClearable) {
                if (index == 0) {
                    setSelectedItem(getSize() == 1
                                    ? null
                                    : getElementAt(index + 1));
                } else {
                    setSelectedItem(getElementAt(index - 1));
                }
            }
        }
        if (index >= 0) {
            objects.remove(index);
            fireIntervalRemoved(this, index, index);
        }
    }

    /**
     * Remove all elements.
     */
    public void removeAllElements() {
        int startIndex = 0;
        int endIndex = Math.max(0, objects.size() - 1);
        objects.clear();
        selectedObject = null;
        fireIntervalRemoved(this, startIndex, endIndex);
    }

    /*
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    public Object getSelectedItem() {
        return selectedObject;
    }

    /**
     * Returns true if some object elem is contained by the list of choices.
     *
     * @param elem the given element
     * @return boolean true if it is in the selection
     */
    public boolean contains(Object elem) {
        if (objects.contains(elem)) {
            return true;
        }
        if (elem instanceof Collection) {
            for (Object o : (Collection) elem) {
                if (!objects.contains(o)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void setFilter(String filter) {
	this.filter = filter;
	modelValid = false;
    }

    /**
     * Returns true if some event is valid. An event is valid if the
     * element changed in the event is valid. This is determined via a
     * call to isValidElement.  This method can be overriden by
     * subclasses if they cannot determine if it is a valid event just
     * by checking the changed element.
     *
     * @param e the event
     * @return boolean true if the event is valid
     */
    protected boolean isValidEvent(PropertyChangeEvent e) {
        boolean valid = false;
        if (!(getChangedElement(e) instanceof Collection)) {
            if ((e.getNewValue() == null && e.getOldValue() != null)
                    // Don't try to test this if we're removing the element
                    || isValidElement(getChangedElement(e))) {
                valid = true; // we tried to remove a value
            }
        } else {
            Collection col = (Collection) getChangedElement(e);
            if (!col.isEmpty()) {
                valid = true;
                for (Object o : col) {
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
     * @see javax.swing.AbstractListModel#fireContentsChanged(
     *          Object, int, int)
     */
    @Override
    protected void fireContentsChanged(Object source, int index0, int index1) {
        if (fireListEvents && !buildingModel) {
            super.fireContentsChanged(source, index0, index1);
        }
    }

    /*
     * @see javax.swing.AbstractListModel#fireIntervalAdded(
     *          Object, int, int)
     */
    @Override
    protected void fireIntervalAdded(Object source, int index0, int index1) {
        if (fireListEvents && !buildingModel) {
            super.fireIntervalAdded(source, index0, index1);
        }
    }

    /*
     * @see javax.swing.AbstractListModel#fireIntervalRemoved(
     *          Object, int, int)
     */
    @Override
    protected void fireIntervalRemoved(Object source, int index0, int index1) {
        if (fireListEvents && !buildingModel) {
            super.fireIntervalRemoved(source, index0, index1);
        }
    }

    /**
     * Return boolean indicating whether combo allows empty string.  This
     * flag can only be specified in the constructor, so it will never change.
     * The flag is checked directly internally, so overriding this method will
     * have no effect.
     *
     * @return state of isClearable flag
     */
    protected boolean isClearable() {
        return isClearable;
    }

    /**
     * @return name of property registered with event listener
     */
    protected String getPropertySetName() {
        return propertySetName;
    }

    /**
     * @return Returns the fireListEvents.
     */
    protected boolean isFireListEvents() {
        return fireListEvents;
    }

    /**
     * @param events The fireListEvents to set.
     */
    protected void setFireListEvents(boolean events) {
        this.fireListEvents = events;
    }

    protected boolean isLazy() {
        return false;
    }

    /**
     * Indicate that the model has to be rebuild.
     * For a lazy model, this suffices to get the model rebuild
     * the next time the user opens the combo.
     */
    protected void setModelInvalid() {
        assert isLazy(); // catch callers attempting to use one without other
        modelValid = false;
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent ev) {
        if (isLazy() && !modelValid && !processingWillBecomeVisible) {
            Object selectedElement = getSelectedModelElement();

            buildModelListTimed();
            modelValid = true;
            // We should be able to just do the above, but Swing has already
            // computed the size of the popup menu.  The rest of this is
            // a workaround for Swing bug
            // http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4743225
            JComboBox list = (JComboBox) ev.getSource();
            processingWillBecomeVisible = true;
            try {
                list.getUI().setPopupVisible( list, true );
            } finally {
                processingWillBecomeVisible = false;
            }
            if (selectedElement != null) {
                setSelectedItem(selectedElement);
            }
        }
    }

    abstract public Action getAction();

    public List<Action> getActions() {
        return Collections.emptyList();
    }
}
