// Copyright (c) 1996-2001 The Regents of the University of California. All
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


// File: UMLModelElementComboBoxModel.java
// Classes: UMLModelElementComboBoxModel
// Original Author: mail@jeremybennett.com
// $Id$

// 25 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created as a base for
// wider use of combo boxes with model elements.


package org.argouml.uml.ui;

import org.argouml.uml.*;
import org.apache.commons.logging.Log;
import org.argouml.application.api.*;

import javax.swing.event.*;
import javax.swing.*;
import java.lang.reflect.*;
import java.awt.event.*;
import java.util.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;

/**
 * <p>A general model for use with drop down combo boxes that list NSUML modele
 *   elements.</p>
 *
 * @author Jeremy Bennett (mail@jeremybennett.com), 25 Apr 2002.
 */

public class UMLModelElementComboBoxModel
    extends AbstractListModel
    implements ComboBoxModel, UMLUserInterfaceComponent, ActionListener {
        protected static Log logger = org.apache.commons.logging.LogFactory.getLog(UMLModelElementComboBoxModel.class);
       


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * <p>The container (PropPanel) in which we are used.</p>
     */

    private UMLUserInterfaceContainer _container;


    /**
     * <p>A method which gets the specific value currently associated with
     *   this combo box (the selected value) from the target NSUML object
     *   associated with the container. Returns a {@link MModelElement}. If
     *   the method is non-existent has the value <code>null</code>.</p> 
     */

    private Method _getMethod = null;


    /**
     * <p>A method which sets the specific value currently associated with
     *   this combo box (the selected value) in the target NSUML object
     *   associated with the container. Takes a {@link MModelElement} as
     *   argument. If the method is non-existent has the value
     *   <code>null</code>.</p>
     */

    private Method _setMethod = null;


    /**
     * <p>The currently selected object in the combo box (displayed when the
     *   drop-down is not being shown). <code>null</code> if there is none
     *   selected.</p>
     */

    private UMLModelElementComboBoxEntry _selectedItem = null;


    /**
     * <p>Flag to indicate that an empty entry should be included (first) in
     *   the drop down list.</p>
     */

    private boolean _allowVoid;


    /**
     * <p>The NSUML type of the elements included in this drop down.</p>
     */

    private Class _elementType;


    /**
     * <p>The profile for use in formatting elements. <code>null</code>
     *   indicates the default notation should be used.</p>
     */

    private Profile _profile = null;


    /**
     * <p>The {@link Collection} that holds the drop down elements.</p>
     *
     * <p>We provide a default ({@link TreeSet}), it is up to implementing
     *   classes to replace this by a call to {@link
     *   #setDropDown(Collection)}.</p>
     */

    private Collection _dropDown = new TreeSet();


    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>This method creates a UMLModelElementComboBoxModel.</p>
     *
     * @param container                    The container (PropPanel) that
     *                                     contains the ComboBox and provides
     *                                     access to target, formatting etc.
     *
     * @param getMethod                    Name of a method of the container
     *                                     which will get the value associated
     *                                     with this combo box. Returns an 
     *                                     object of type
     *                                     <code>elementType</code> (see
     *                                     below).
     *
     * @param putMethod                    Name of a method of the container
     *                                     which will set the value associated
     *                                     with this combo box. Takes as 
     *                                     argument an object of type
     *                                     <code>elementType</code> (see
     *                                     below). 
     *
     * @param allowVoid                    A flag to indicate that the drop
     *                                     down list should include (in first
     *                                     position) an empty entry. 
     *
     * @param elementType                  The base type for all elements in
     *                                     the combo box. 
     *
     * @param addElementsFromProfileModel  A flag to indicate that elements
     *                                     from the model associated with the
     *                                     profile should be included in the
     *                                     drop down list.
     */

    public UMLModelElementComboBoxModel(UMLUserInterfaceContainer container,
                                        String getMethod, String setMethod,
                                        boolean allowVoid, Class elementType) {

        // Record the simple arguments

        _container   = container;
        _allowVoid   = allowVoid;
        _elementType = elementType;

        // Get a profile for use in formatting an entry. The container ought to
        // be non-null, but we check just in case.

        if (_container != null) {
            _profile = _container.getProfile();
        }

        // Check the method arguments are valid if supplied

        if(getMethod != null) {
            Class[] getArgs = {};

            try {
                _getMethod = container.getClass().getMethod(getMethod,getArgs);
            }
            catch(Exception e) {
                logger.error(e.toString() + ". " +
                                   this.getClass().toString() +
                                   ": invalid get method " + getMethod, e);
            }
        }

        if(setMethod != null) {
            Class[] setArgs = { elementType };

            try {
                _setMethod = container.getClass().getMethod(setMethod,setArgs);
            }
            catch(Exception e) {
                logger.error(e.toString() + ". " +
                                   this.getClass().toString() +
                                   ": invalid set method " + setMethod, e);
            }
        }

        // Finally add a third party listener if the container is a prop panel,
        // so we can here of any changes to objects of meta class elementType.
        // Other containers will have to do this for themselves.

        if (container instanceof PropPanel) {
            Object[] eventsToWatch = {elementType, null};
            ((PropPanel) container).addThirdPartyEventListening(eventsToWatch);
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Accessor methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Return the container (PropPanel) in which this combo box is used.</p>
     *
     * @return  The container     
     */

    protected UMLUserInterfaceContainer getContainer() {
	return _container;
    }


    /**
     * <p>Return a flag indicating if the drop down should have an empty entry
     *   (as first element).</p>
     *
     * @return  <code>true</code> if the drop down should have an empty entry,
     *          <code>false</code> otherwise.
     */

    protected boolean allowVoid() {
	return _allowVoid;
    }


    /**
     * <p>Return the (NSUML) type of the elements used in the drop down.</p>
     *
     * @return  The (NSUML) type of elements used in the drop down.
     */

    protected Class getElementType() {
	return _elementType;
    }


    /**
     * <p>Return the method of the container, which is used to get the NSUML
     *   element associated with this comb box.</p>
     *
     * @return  The method which will get the NSUML element, or
     *          <code>null</code> if no method is provided.
     */

    protected Method getGetMethod() {
	return _getMethod;
    }


    /**
     * <p>Return the method of the container, which is used to set the NSUML
     *   element associated with this comb box.</p>
     *
     * @return  The method which will set the NSUML element, or
     *          <code>null</code> if no method is provided.
     */

    protected Method getSetMethod() {
	return _setMethod;
    }


    /**
     * <p>Set the collection of elements that are shown in the drop down of
     *   the combo box.</p>
     *
     * <p>This method may be overridden by implementing classes to use any
     *   other form of collecion.</p>
     *
     * @param dropDown  The collection of elements to be shown in the drop.
     *                  <em>Note</em>. There must always be a set, even if it
     *                  is empty.  This method may not set a <code>null</code>
     *                  collection.
     */

    protected void setDropDown(Collection dropDown) {

        if (dropDown == null) {
            logger.warn(getClass().toString() +
                               ": getDropDown() - " + 
                               "attempted to set null collection");
            return;
        }

        _dropDown = dropDown;
    }


    /**
     * <p>Return the collection of elements that are shown in the drop down of
     *   the combo box.</p>
     *
     * <p>This method may be overridden by implementing classes.</p>
     *
     * @return  The collection of elements shown in the drop down.
     *          <em>Note</em>. There must always be a set, even if it is empty.
     *          This method may not return <code>null</code>.
     */

    protected Collection getDropDown() {
        return _dropDown;
    }


    /**
     * <p>Set the given item as the selected item.</p>
     *
     * <p>Provided to comply with the {@link ComboBoxModel} interface.</p>
     *
     * @param selection  The object that should be used as the selected item in
     *                   the combo box.
     */

    public void setSelectedItem(Object selection) {
        _selectedItem = (UMLModelElementComboBoxEntry) selection;
    }


    /**
     * <p>Get the selected item.</p>
     *
     * <p>Provided to comply with the {@link ComboBoxModel} interface.</p>
     *
     * @return  The object that is currently to be used as the selected item in
     *          the combo box.
     */

    public Object getSelectedItem() {
        return _selectedItem;
    }


    /**
     * <p>Return the number of elements in the drop down list.</p>
     *
     * <p>May be overridden to give a more efficient implementation if
     *   desired.</p>
     *
     * <p>Provided to comply with the {@link ListModel} interface (the parent
     *   of {@link ComboBoxModel}).</p>
     *
     * @return  The number of elements in the drop down list.
     */

    public int getSize() {

        // Note that _dropDown can never be null.

        return _dropDown.size();
    }


    /**
     * <p>Provide the element at a specific index in the drop down list.</p>
     *
     * <p>May be overridden to give a more efficient implementation if
     *   desired (very likely for some drop down collection types).</p>
     *
     * <p>Provided to comply with the {@link ListModel} interface (the parent
     *   of {@link ComboBoxModel}).</p>
     *
     * @param index  The index of the desired element.
     *
     * @return       The element at that index, or <code>null</code> if index
     *               exceeds the number of elements in the set.
     */

    public Object getElementAt(int index) {

        // Loop through the desired number of elements (it might be quicker to
        // convert to an array).

        Iterator iter = _dropDown.iterator();

        for(int i = 0; iter.hasNext(); i++) {
            Object element = iter.next();

            if(i == index) {
                return element;
            }
        }

        // Index was bigger than the set

        return null;
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Main methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>A method to populate the drop down, having cleared anything already
     *   in there.</p>
     *
     * <p>This will usually be overridden by the implementing class. However we
     *   provide a fall back method, which walks over the model of the target
     *   of the container, adding any elements which pass the filter.</p>
     *
     * <p>Find all the elements in the namespace supplied. Then recurse on any
     *   of those elements that are themselves namespaces. The result is built
     *   up in <code>_set</code>.</p>
     */
    
    protected void populateDropDown() {

        // Clear the current drop down set.

        _dropDown.clear();

        // Put in an empty entry if wanted

        if(_allowVoid) {
            addEntry(null);
        }

        // Go no further if we have no container, or if we don't have a target
        // that is some sort of model element

        if (_container == null) {
            return ;
        }

        Object target = _container.getTarget();

        if (!(target instanceof MModelElement)) {
            return;
        }

        // Collect elements from the model of the model element. This works
        // fine even if the model is null.

        collectElements(((MModelElement) target).getModel());
    }


    /**
     * <p>A method to add a model element if it meets the criteria of the
     *   filter method.</p>
     *
     * <p>This is part of our default {@link #populateDropDown()} method and
     *   so is private to this abstract class.</p>
     *
     * <p>Consider adding the model element supplied. If it is a namespace
     *   recurse on all its owned elements. The result is built up in
     *   <code>_dropDown</code>.</p>
     *
     * @param modelElement  The model element we are to examine.
     */

    private void collectElements(MModelElement modelElement) {

        // Give up if we have a null model element

        if (modelElement == null) {
            return;
        }

        // If the model element is acceptable, add it to the drop down

        if(isAcceptable(modelElement)) {
            addEntry(modelElement);
        }

        // Give up if we don't have a namespace, or if the namespace has no
        // owned elements

        if (!(modelElement instanceof MNamespace)) {
            return;
        }

        MNamespace nameSpace     = (MNamespace) modelElement;
        Collection ownedElements = nameSpace.getOwnedElements();

        if(ownedElements == null) {
            return;
        }

        // Loop through each element in the namespace, recursing.

        Iterator iter = ownedElements.iterator();

        while(iter.hasNext()) {
            collectElements((MModelElement) (iter.next()));
        }
    }


    /**
     * <p>Determine if a given model element is acceptable.</p>
     *
     * <p>This should be overridden by implementing classes who wish to apply a
     *   different test.</p>
     *
     * <p>The default implementation just tests if the supplied model element
     *   is assignable from the element type supplied to the constructor.</p>
     *
     * @param modelElement  The model element to test.
     *
     * @return              <code>true</code> if the model element should be
     *                      included in the drop down list, <code>false</code>
     *                      otherwise.
     */

    protected boolean isAcceptable(MModelElement modelElement) {

        // If elementType is null we have all sorts of other problems. Here we
        // just return true, so everything is permitted

        if (_elementType == null) {
            return true;
        }

        // If the modelElement is null, that is not acceptable (the allowVoid
        // flag indicates suitability of a null entry, and we would not want
        // another).

        if (modelElement == null) {
            return false;
        }

        // Otherwise we are OK if the event type is assignable from the model
        // element

        return _elementType.isAssignableFrom(modelElement.getClass());
    }


    /**
     * <p>Find the selected item.</p>
     *
     * <p>May be overridden by the implementing class. The default provided
     *   here finds the entry whose value is the same as the target model
     *   elements get method returns.</p>
     *
     * @return          The selected element in the drop down, or null if we
     *                  can't find it.
     */

    UMLModelElementComboBoxEntry findSelectedItem() {

        // See if we have a selected element. First use the getMethod to find
        // the target object.

        Object   targetObj = null;
        Object[] args      = {};

        try {
            targetObj = _getMethod.invoke(_container, args);
        }
        catch(Exception e) {
            logger.error(e.toString() + ". " +
                               getClass().toString() +
                               ": findSelectedItem() - " +
                               "get method failed.", e);
        }

        // Find the selected item.

        return findEntry((MModelElement) targetObj);
    }


    /**
     * <p>A utility to find a model element entry in the drop down list (if it
     *   is there).</p>
     *
     * @param modelElement  The element to look for.
     *
     * @return              Its entry in the drop down, or <code>null</code> if
     *                      it is not there.
     */

    private UMLModelElementComboBoxEntry findEntry(
        MModelElement modelElement) {

        // Loop through the drop down, looking for the element, and return it
        // if round. Note that if the model element is null, this will find the
        // void entry if one has been set.

        Iterator iter = _dropDown.iterator();

        while (iter.hasNext()) {
            UMLModelElementComboBoxEntry entry =
                (UMLModelElementComboBoxEntry) (iter.next());

            if (entry.getModelElement() == modelElement) {
                return entry;
            }
        }

        // Not there, so return null.

        return null;
    }


    /**
     * <p>A utility to add a model element to the drop down list (if it is not
     *   already there).</p>
     *
     * @param modelElement  The element to add.
     *
     * @return              The entry added (or the existing one if it was
     *                      there).
     */

    private UMLModelElementComboBoxEntry addEntry(MModelElement modelElement) {

        // Try to find the entry and add it if it is not there.

        UMLModelElementComboBoxEntry entry = findEntry(modelElement);

        if (entry == null) {
            entry = new UMLModelElementComboBoxEntry(modelElement, _profile);
            _dropDown.add(entry);
        }

        return entry;
    }


    /**
     * <p>A utility to remove a model element from the drop down list (if it is
     *   there).</p>
     *
     * @param modelElement  The element to delete.
     */

    private void deleteEntry(MModelElement modelElement) {

        // Try to find the entry and delete it if it is there.

        UMLModelElementComboBoxEntry entry = findEntry(modelElement);

        if (entry != null) {
            _dropDown.remove(entry);
        }
    }


    /**
     * <p>A utility to update the entry for a model element in the drop down
     *   list (if it is there).</p>
     *
     * @param modelElement The element to update.
     */

    private void updateEntry(MModelElement modelElement) {

        // We must do this by a delete and add entry, since the ordering may
        // have changed. However if we are deleting the selected item, we'll
        // have to set it to the new entry.

        deleteEntry(modelElement);
        UMLModelElementComboBoxEntry entry = addEntry(modelElement);

        // The selected item will still be around even after we've deleted it
        // from the drop down list. If it had the model element (no duplicates
        // allowed in the drop down), then we will have changed it and must
        // reset it.

        if ((_selectedItem != null) &&
            (_selectedItem.getModelElement() == modelElement)) {

            _selectedItem = entry;
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Event handlers
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Invoked when the target associated with the container is changed.</p>
     *
     * <p>Recompute the membership of the drop down and its selected
     *   component.</p>
     *
     * <p>Provided to comply with the {@link UMLUserInterfaceComponent}
     *   interface.</p>
     */

    public void targetChanged() {

        // Populate the drop down. This will add the empty entry if
        // needed. Then set the selected item.

        populateDropDown();
        _selectedItem = findSelectedItem();

        // Finally tell Swing that things may have changed (I'm not sure we
        // shouldn't be using add and remove).

        fireContentsChanged(this, 0, getSize() - 1);
    }


    /**
     * <p>Called when navigation may have changed.</p>
     *
     * <p>Null implementation provided here.</p>
     *
     * <p>Provided to comply with the {@link UMLUserInterfaceComponent}
     *   interface.</p>
     */

    public void targetReasserted() {
    }


    /**
     * <p>Invoked if a listened to NSUML element has an entry added to a
     *   component with multiplicity.</p>
     *
     * <p>We are only interested in the "ownedElement" event name, which
     *   indicates an object has been added to a namespace.</p>

     * <p>Provided for compliance with the {@link MElementListener}
     *   interface.</p>
     *
     * @param p1 The event which triggered this method.
     */

    public void roleAdded(final MElementEvent event) {
        String eventName       = event.getName();
        Object eventAddedValue = event.getAddedValue();

        // If we have an "ownedElement" event and the added element is
        // acceptable, add a new entry to the drop down and tell Swing.

        if ((eventName != null) &&
            (eventName.equals("ownedElement")) &&
            (isAcceptable((MModelElement) eventAddedValue))) {

            addEntry((MModelElement) eventAddedValue);
            fireContentsChanged(this, 0, getSize() - 1);
        }
    }


    /**
     * <p>Invoked if a listened to NSUML element has an entry removed from a
     *   component with multiplicity.</p>
     *
     * <p>We're only interested in the case where the name is "ownedElement",
     *   indicating an element has been removed from the namespace.</p>
     *
     * <p>Provided for compliance with the {@link MElementListener}
     *   interface.</p>
     *
     * @param event  The event which triggered this method.
     */

    public void roleRemoved(final MElementEvent event) {
        String eventName         = event.getName();
        Object eventRemovedValue = event.getRemovedValue();

        if ((eventName != null) && (eventName.equals("ownedElement"))) {
            deleteEntry((MModelElement) event.getRemovedValue());
        }

        // If we have an "ownedElement" event and the removed element is
        // acceptable, remove the entry from the drop down and tell Swing.

        if ((eventName == null) &&
            (eventName.equals("ownedElement")) ||
            (isAcceptable((MModelElement) eventRemovedValue))) {

            deleteEntry((MModelElement) eventRemovedValue);
            fireContentsChanged(this, 0, getSize() - 1);
        }
    }


    /**
     * <p>Invoked if a listened to NSUML element has been restored (by an NSUML
     *   internal method), having been removed.</p>
     *
     * <p>Null implementation in this case. Implementing classes may override
     *   if they need to use this.</p>
     *
     * <p>Provided for compliance with the {@link MElementListener}
     *   interface.</p>
     *
     * @param event  The event which triggered this method.
     */

    public void recovered(final MElementEvent event) {
    }


    /**
     * <p>Invoked if a listened to NSUML element has an entry changed in a
     *   component with multiplicity.</p>
     *
     * <p>Null implementation in this case. Implementing classes may override
     *   if they need to use this.</p>
     *
     * <p>Provided for compliance with the {@link MElementListener}
     *   interface.</p>
     *
     * @param event  The event which triggered this method.
     */

    public void listRoleItemSet(final MElementEvent event) {
    }


    /**
     * <p>Invoked if a listened to NSUML element is deleted.</p>
     *
     * <p>We must remove it from the drop down collection if it is there.</p>
     *
     * <p>Provided for compliance with the {@link MElementListener}
     *   interface.</p>
     *
     * @param event  The event which triggered this method.
     */

    public void removed(final MElementEvent event) {

        // Remove from the drop down if it is one of interest and tell Swing.

        Object eventSource = event.getSource();

        if ((eventSource != null) &&
            (eventSource instanceof MModelElement) &&
            (isAcceptable((MModelElement) eventSource))) {

            deleteEntry((MModelElement) (event.getSource()));
            fireContentsChanged(this, 0, getSize() - 1);
        }
    }


    /**
     * <p>Invoked if a listened to NSUML object has an entry without
     *   multiplicity set (or an entry with multiplicity completely reset.</p>
     *
     * <p>We deal both with the case of an element of interest to us and an
     *   "ownedElement" event which may indicate an added value that is an
     *   element of interest to us in the set of ownedelements.
     * 
     * <p>Provided for compliance with the {@link MElementListener}
     *   interface.</p>
     *
     * @param event The event which triggered this method.
     */

    public void propertySet(final MElementEvent event) {
        Object eventSource = event.getSource();

        // First case, where the eventSource is of interest. Update it and tell
        // Swing.

        if ((eventSource != null) &&
            (eventSource instanceof MModelElement) &&
            (isAcceptable((MModelElement) eventSource))) {

            updateEntry((MModelElement) eventSource);
            fireContentsChanged(this, 0, getSize() - 1);
        }

        // Second case (which is not exclusive of the first), where the owned
        // elements of a namespace have been changed. Remove all the old ones
        // and add in all the new ones.

        // This is a very unlikely event to receive, so the inefficiency of
        // telling Swing about a second change is not significant.

        if (event.getName() == "ownedElement") {
            Collection oldElements = (Collection) (event.getOldValue());
            Collection newElements = (Collection) (event.getNewValue());

            Iterator   iter;

            // Remove all the old elements

            iter = oldElements.iterator();

            while(iter.hasNext()) {
                deleteEntry((MModelElement) iter.next());
            }

            // Add all the new elements

            iter = newElements.iterator();

            while(iter.hasNext()) {
                addEntry((MModelElement) iter.next());
            }

            // Tell Swing

            fireContentsChanged(this, 0, getSize() - 1);
        }
    }


    /**
     * <p>Called when an "action" is performed.</p>
     *
     * <p>Called when a new selected item is chosen from the drop down
     *   list. We try to set the associated model element accordingly.</p>
     *
     * <p>Provided to comply with the {@link ActionListener} interface, which
     *   provides for general listening support.</p>
     *
     * @param event  The event that triggered us.
     */

    public void actionPerformed(ActionEvent event) {

        // Only works if we actually have a selected item and a non-null
        // container.

        if ((_selectedItem == null) || (_container == null)) {
            return;
        }

        // Get the model element from the selectedItem and set it as the
        // value. Build up an argument list and invoke the set method
        // (remembering to catch any problem

        Object[] args  = { _selectedItem.getModelElement() };

        try {
            _setMethod.invoke(_container, args);
        }
        catch(Exception e) {
            logger.error(e.toString() + ". " +
                               this.getClass().toString() +
                               ": actionPerformed() - set method failed.", e );
        }
    }

}  /* End of class UMLModelElementComboBoxModel */
