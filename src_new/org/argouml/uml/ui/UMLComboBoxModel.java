// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: UMLComboBoxModel.java
// Classes: UMLComboBoxModel
// Original Author: 
// $Id$

package org.argouml.uml.ui;

import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.kernel.*;
import org.apache.log4j.Category;
import org.argouml.application.api.*;

import java.lang.reflect.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.event.*;
import javax.swing.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;


/**
 * <p>A model for use with drop down combo boxes. Used to supply the correct
 *   model for the "type" and "stereotype" drop downs used on several of the
 *   property panels, as well as others.</p>
 *
 * <p><em>Warning</em>. This is implemented by {@link TreeSet}, a set class,
 *   that therefore does not permit duplicates. If two entries in the drop down
 *   have the same fully qualified name, then they will appear only once in the
 *   list. This causes problems for example with lists of objects in diagrams,
 *   where default naming means newly created objects have the same name.</p>
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.UMLComboBoxModel2},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */

public class UMLComboBoxModel extends AbstractListModel implements
    ComboBoxModel, UMLUserInterfaceComponent, ActionListener {
    protected static Category cat = 
        Category.getInstance(UMLComboBoxModel.class);
        


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////


    /**
     * <p>The container (PropPanel) in which we are used.</p>
     */

    protected UMLUserInterfaceContainer _container;


    /**
     * <p>The name of the NSUML event that affects our data model.</p>
     */

    protected String _property;


    /**
     * <p>A method returning a boolean, to indicate whether a specific {@link
     *   MModelElement} should be allowed in the combo box. If the method is
     *   non-existent has the value <code>null</code>.</p>
     */

    protected Method _filter = null;


    /**
     * <p>A method which gets the specific value currently associated with
     *   this combo box (the selected value) from the target NSUML object
     *   associated with the container. Returns a {@link MModelElement}. If
     *   the method is non-existent has the value <code>null</code>.</p> 
     */

    protected Method _getMethod;


    /**
     * <p>A method which sets the specific value currently associated with
     *   this combo box (the selected value) in the target NSUML object
     *   associated with the container. Takes a {@link MModelElement} as
     *   argument. If the method is non-existent has the value
     *   <code>null</code>.</p>
     */

    protected Method _setMethod;


    /**
     * <p>The set of objects that are displayed in the drop down.</p>
     */

    protected TreeSet _set;


    /**
     * <p>The currently selected object in the combo box (displayed when the
     *   drop-down is not being shown).</p>
     */

    protected Object _selectedItem;


    /**
     * <p>Flag to indicate that an empty entry should be included (first) in
     *   the drop down list.</p>
     */

    protected boolean _allowVoid;


    /**
     * </p>Flag to indicate that the drop down should include elements from the
     *   model included within the profile.</p>
     */

    protected boolean _addElementsFromProfileModel;


    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>This method creates a UMLComboBoxModel.</p>
     *
     * @param container                    The container (PropPanel) that
     *                                     contains the ComboBox and provides
     *                                     access to target, formatting etc.
     *
     * @param filter                       Name of method on container that
     *                                     takes a {@link MModelElement} and
     *                                     returns <code>true</code> if the 
     *                                     element should be in the combo box
     *                                     list, <code>false</code>
     *                                     otherwise. <em>Note</em>. The 
     *                                     supplied model element may be
     *                                     <code>null</code>.
     *
     * @param property                     Name of the NSUML event that would
     *                                     indicate that the selected value for
     *                                     the combo box has changed.
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

    public UMLComboBoxModel(UMLUserInterfaceContainer container,
                            String filter, String property, String getMethod,
                            String setMethod, boolean allowVoid, Class
                            elementType, boolean addElementsFromProfileModel) {

        // Record the simple arguments

        _container                   = container;
        _property                    = property;
        _allowVoid                   = allowVoid;
        _addElementsFromProfileModel = addElementsFromProfileModel;

        // Create an empty sorted set for the elements in the drop down

        _set = new TreeSet();

        // Check the method arguments are valid if supplied

        if(filter != null) {
            Class[] args =  { MModelElement.class };

            try {
                _filter = _container.getClass().getMethod(filter, args);
            }
            catch(Exception e) {
                cat.error(e.toString() + ". " +
                                   this.getClass().toString() +
                                   ": invalid filter method " + filter, e);
            }
        }

        if(getMethod != null) {
            Class[] getArgs = {};

            try {
                _getMethod = container.getClass().getMethod(getMethod,getArgs);
            }
            catch(Exception e) {
                cat.error(e.toString() + ". " +
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
                cat.error(e.toString() + ". " +
                                   this.getClass().toString() +
                                   ": invalid set method " + setMethod, e);
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Accessor methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Return the sorted set of elements that are shown in the drop down of
     *   the combo box.</p>
     *
     * @return  The set of elements shown in the drop down.
     */

    protected TreeSet getSet() {
	return _set;
    }


    /**
     * <p>Return a flag indicating if elements from the model associated with
     * the profile are also to be used in the drop down.</p>
     *
     * @return  <code>true</code> if elements should be used from the model
     *          associated with the profile, <code>false</code> otherwise.
     */

    protected boolean addElementsFromProfileModel() {
	return _addElementsFromProfileModel;
    }


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
     * <p>Set the given item as the selected item.</p>
     *
     * <p>Provided to comply with the {@link ComboBoxModel} interface.</p>
     *
     * @param selection  The object that should be used as the selected item in
     *                   the combo box.
     */

    public void setSelectedItem(final java.lang.Object selection) {
        _selectedItem = selection;
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
     * <p>Provided to comply with the {@link ListModel} interface (the parent
     *   of {@link ComboBoxModel}).</p>
     *
     * @return  The number of elements in the drop down list.
     */

    public int getSize() {
        return _set.size();
    }


    /**
     * <p>Provide the element at a specific index in the drop down list.</p>
     *
     * <p>Provided to comply with the {@link ListModel} interface (the parent
     *   of {@link ComboBoxModel}).</p>
     *
     * @param index  The index of the desired element.
     *
     * @return       The element at that index, or <code>null</code> if index
     *               exceeds the number of elements in the set.
     */

    public java.lang.Object getElementAt(int index) {

        // Loop through the desired number of elements (it might be quicker to
        // convert to an array).

        Object   element = null;
        Iterator iter    = _set.iterator();

        for(int i = 0; iter.hasNext(); i++) {
            element = iter.next();

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
     * <p>A method to run over the current model (or part thereof), collecting
     *   all elements that meet the criteria of the filter method.</p>
     *
     * <p>Find all the elements in the namespace supplied. Then recurse on any
     *   of those elements that are themselves namespaces. The result is built
     *   up in <code>_set</code>.</p>
     *
     * @param ns         The namespace of the model (or part of the model) we
     *                   are to examine.
     *
     * @param profile    The profile to be used for additional elements and
     *                   supplying formatting methods.
     *
     * @param isPhantom  A flag which seems to indicate an entry that should
     *                   not normally be displayed. This is typically used for
     *                   entries in the profile model, which are only displayed
     *                   when they start being used in the "real" model. A
     *                   combo box entry is created for these, but they are not
     *                   added to the set if there is already an element with
     *                   the same "short" (i.e. formatted) name.
     */

    public void collectElements(MNamespace ns, Profile profile,
                                boolean isPhantom) {

        // Give up if there are no elements in the supplied namespace

        Collection collection = ns.getOwnedElements();

        if(collection == null) {
            return;
        }

        // Loop through each element in the namespace, looking for valid
        // elements to add.

        Iterator iter = collection.iterator();

        while(iter.hasNext()) {
            MModelElement element = (MModelElement) iter.next();

            // If its passed by the filter, make a new combo box entry, and if
            // appropriate add to the set.

            if(isAcceptible(element)) {
                UMLComboBoxEntry entry =
                    new UMLComboBoxEntry(element, profile, isPhantom);

                // Only add if this is not a phantom entry whose formatted name
                // is already in the set.

                boolean addMe = true;

                if(isPhantom) {
                    String   shortName = entry.getShortName();
                    Iterator setIter   = _set.iterator();

                    // Is the formatted name in the set?

                    while(setIter.hasNext()) {
                        UMLComboBoxEntry setEntry =
                            (UMLComboBoxEntry) setIter.next();
                        String           setName = setEntry.getShortName();

                        if(setName.equals(shortName)) {
                            addMe = false;
                            break;
                        }
                    }
                }

                // Add if not already there

                if(addMe) {
                    _set.add(entry);
                }
            }

            // If the element was a namespace, we recurse to find more elements

            if(element instanceof MNamespace) {
                collectElements((MNamespace) element, profile, isPhantom);
            }
        }
    }


    /**
     * <p>Use the supplied filter method to identify if the supplied model
     *   element should be included in the drop down list.</p>
     *
     * @param element  The model element to test.
     *
     * @return         <code>true</code> if the element should be included in
     *                 the drop down list, <code>false</code> otherwise.
     */

    private boolean isAcceptible(MModelElement element) {

        boolean isAcceptible = false;

        // Try the routine, remembering it could blow up

        try {
            Object[] args = { element };
            Boolean  boo  =(Boolean) _filter.invoke(_container, args);

            if(boo != null) {
                isAcceptible = boo.booleanValue();
            }
        }
        catch(Exception e) {
            cat.error(e.toString() + ". " +
                               this.getClass().toString() +
                               ": isAcceptible() - filter failed.", e);
        }
        return isAcceptible;
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
     *
     * @author  psager@tigris.org  Oct. 13, 2001. Fixed problem with reload or
     *   2nd project load where stereotype would be set to <code>null</code> on
     *   first class selected after load.
     */

    public void targetChanged() {

        // Give up if we don't have a target that is some sort of model
        // element.

        Object target = _container.getTarget();

        if (!(target instanceof MModelElement)) {
            return;
        }

        // Make sure we have a model as well. PJS comments that this needs more
        // work - it should never happen.

        MModelElement element = (MModelElement) target;
        MModel        model   = element.getModel();

        if (model == null) {
        	// extra attempt
            Argo.log.error(this.getClass().toString() + " targetChanged() - " +
                           "getModel() == null for " + 
                           target.getClass().toString());
            return;
        }

        // Needs_work...

        // This if statement was not allowing us to update the Stereotype or
        // Type information once the system had previously loaded the
        // combo-box.

        // Perhaps what is required is a way to invalidate the model, instead
        // of collecting the elements everytime we change property panels. For
        // now this does not seem to cause us any performance problems...

        // Modified Jan.06, 2002...pjs

        // Empty the set and get the profile we can use for creating new
        // entries, formatting etc.

        _set.clear();
        Profile profile = _container.getProfile();

        // Add an empty entry if allowed

        if(_allowVoid) {
            _set.add(new UMLComboBoxEntry(null, profile, false));
        }

        // Collect elements from the model supplied.

        if (model != null) {
            collectElements(model,profile,false);
        }

        // Add elements from the model associated with the profile if allowed.

        if(_addElementsFromProfileModel) {
            MModel profileModel = profile.getProfileModel();

            if(profileModel != null) {
                collectElements(profileModel,profile,true);
            }
        }

        // Scan for name collisions, where the short (i.e. formatted) name of
        // entries is identical. Where this is the case, use the long name as
        // the display name.

        Iterator iter = _set.iterator();

        String before = null;
        UMLComboBoxEntry currentEntry = null;
        String current = null;
        UMLComboBoxEntry afterEntry = null;
        String after = null;

        while(iter.hasNext()) {
            before = current;
            currentEntry = afterEntry;
            current = after;
            afterEntry = (UMLComboBoxEntry) iter.next();
            after = afterEntry.getShortName();

            if(currentEntry != null) {
                currentEntry.checkCollision(before,after);
            }
        }

        if(afterEntry != null) {
            afterEntry.checkCollision(current,null);
        }

        // The original version had a fireContentsChanged statement here as
        // follows:

        //    fireContentsChanged(this,0,_set.size());
 
        // Phil Sager suggested removing this statement to solve a problem
        // where on reload of a project or load of a new project the
        // stereotype, attribute or parameter of the first class chosen would
        // be changed to null in the case of a stereotype or to BigDecimal in
        // the case of an attribute or parameter.

        // Jeremy Bennett's fix is i) to keep the fireContentsChanged() call,
        // but move it to *after* the check on the selected item and ii) to use
        // _set.size()-1 as the final argument (it is the index of the last
        // element, not the size of the set).

        // Get current value, and make sure it is the selected entry. Allow for
        // the possibility that this blows up.

        // WARNING. If two entries have identical fully qualified names, only
        // the first will appear in this set!

        try {
            Object[] _noArgs    = {};
            Object   currentObj = _getMethod.invoke(_container,_noArgs);

            // Now find this one in the set

            Iterator iter2 = _set.iterator();

            while(iter2.hasNext()) {
                UMLComboBoxEntry entry = (UMLComboBoxEntry) iter2.next();
                // 2002-07-13
                // Jaap Branderhorst
                // the next if statement doesn't work since the element from the model
                // equals the currentObj but is not the same object in case of an abstraction
                // with a stereotype.
                // this is an error while constructing the abstraction probably.

                if(!(entry.isPhantom()) &&
                   (entry.getElement(model) == currentObj)) {
                    _selectedItem = entry;
                    // 2002-07-15
                    // Jaap Branderhorst
                    // probably good idea for performance to break out of the while loop
                    break;
                }
            }
        }
        catch(Exception e) {
            cat.error(e.toString() + ". " +
                               this.getClass().toString() +
                               ": targetChanged() - get method failed.", e);
            _selectedItem = null;
        }

        // Finally tell Swing that things may have changed (I'm not sure we
        // shouldn't be using add and remove).

        fireContentsChanged(this,0,_set.size() - 1);

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
     * <p>Check that an entry is in the set for the drop down, and add it if it
     *   is not.</p>
     *
     * <p>Used to support the NSUML roleAdded event, where an element is added
     *   to the namespace.</p>
     *
     * @param addedElement  The model element that should be in the set (if it
     *                      has an acceptable type).
     */

    public void updateElement(MModelElement addedElement) {

        // Nothing to do if the element is not of the right type.

        if (!(isAcceptible(addedElement))) {
            return;
        }

        // Double check that it doesn't have an entry already. Loop until we
        // find it.

        String   addedName = addedElement.getName();
        boolean  inSet     = false;
        Iterator iter      = _set.iterator();
                 
        while(iter.hasNext() && !inSet) {
            UMLComboBoxEntry existingEntry = (UMLComboBoxEntry) iter.next();

            // If this is a phantom entry, look to see if it has the same short
            // name as the added element. If so turn it into a real entry. If
            // it's not a phantom entry, see if it is the same as the entry.

            if (existingEntry.isPhantom()) {
                
                if ((addedName != null) &&
                    addedName.equals(existingEntry.getShortName())) {

                    existingEntry.setElement(addedElement,false);
                    inSet = true;
                }
            }
            else {
                inSet = (addedElement == existingEntry.getElement(null));
            }
        }

        // If we didn't find it, add a new (non-phantom) entry and tell Swing
        // things have changed.

        if(!inSet) {
            _set.add(new UMLComboBoxEntry(addedElement,
                                          _container.getProfile(), false));
            fireContentsChanged(this, 0, _set.size());
        }
    }


    /**
     * <p>Remove an entry if it is currently in the drop down.</p>
     *
     * <p>Used to support the NSUML roleRemoved event, where an element is
     *   removed from the namespace.</p>
     *
     * @param removedElement  The model element that should no longer be in the
     *                        set (if it is currently there).
     */

    public void deleteElement(MModelElement removedElement) {

        // Nothing to do if the element is not of the right type.

        if (!(isAcceptible(removedElement))) {
            return;
        }

        // See if it has an entry already. Loop until we find it. It doesn't
        // matter if the entry is phantom or not.

        UMLComboBoxEntry  foundEntry = null;
        Iterator          iter       = _set.iterator();
                 
        while(iter.hasNext() && (foundEntry == null)) {
            UMLComboBoxEntry existingEntry = (UMLComboBoxEntry) iter.next();

            if (removedElement == existingEntry.getElement(null)) {
                foundEntry = existingEntry;
            }
        }

        // If we found find it, remove the entry and tell Swing
        // things have changed.

        if(foundEntry != null) {
            _set.remove(foundEntry);
            fireContentsChanged(this, 0, _set.size());
        }
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
        String eventName = event.getName();

        if ((eventName != null) && (eventName.equals("ownedElement"))) {
            updateElement((MModelElement) event.getAddedValue());
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
        String eventName = event.getName();

        if ((eventName != null) && (eventName.equals("ownedElement"))) {
            deleteElement((MModelElement) event.getRemovedValue());
        }

    }


    /**
     * <p>Invoked if a listened to NSUML element has been restored (by an NSUML
     *   internal method), having been removed.</p>
     *
     * <p>Null implementation in this case.</p>
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
     * <p>Null implementation in this case.</p>
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
     * <p>We must remove it from the set if it is there.</p>
     *
     * <p>Provided for compliance with the {@link MElementListener}
     *   interface.</p>
     *
     * @param event  The event which triggered this method.
     */

    public void removed(final MElementEvent event) {

        // Loop round looking for a non-phantom entry that has the source of
        // this event as its element.

        Object   source = event.getSource();
        Iterator iter   = _set.iterator();

        while(iter.hasNext()) {
            UMLComboBoxEntry entry = (UMLComboBoxEntry) iter.next();

            if((!entry.isPhantom()) && (entry.getElement(null) == source)) {
                _set.remove(entry);
                break;
            }
        }
    }


    /**
     * <p>Invoked if a listened to NSUML object has an entry without
     *   multiplicity set (or an entry with multiplicity completely reset.</p>
     *
     * <p>We are interested in changes to "name" fields, since they may appear
     *   in our set.</p>
     *
     * <p><em>Warning</em>. This only works if setNameEventListener is enabled
     *   to listen for name changes on NSUML elements other than the
     *   target.</p>
     *
     * <p>Provided for compliance with the {@link MElementListener}
     *   interface.</p>
     *
     * @param event The event which triggered this method.
     */

    public void propertySet(final MElementEvent event) {

        // Reject any events other than "name"

        String eventName = event.getName();

        if ((eventName == null) ||
            (!(eventName.equals("name")))) {
            return;
        }

        // Only work if we have an acceptable model element

        Object source = event.getSource();

        if ((!(source instanceof MModelElement)) ||
             (!(isAcceptible((MModelElement) source)))) {
            return;
        }

        // See if there is a (non-phantom) entry with that element. Count the
        // index, so we can tell where the changed happened.

        Iterator iter = _set.iterator();

        for(int i = 0 ; iter.hasNext() ; i++) {
            UMLComboBoxEntry entry = (UMLComboBoxEntry) iter.next();

            if (!entry.isPhantom() && (entry.getElement(null) == source)) {

                // Found the entry. Change its name and tell Swing

                entry.nameChanged((MModelElement) source);
                fireContentsChanged(this, i, i);
                break;
            }
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

        // Only works if we actually have a selected item

        if (_selectedItem == null) {
            return;
        }

        UMLComboBoxEntry entry = (UMLComboBoxEntry) _selectedItem;

        // If we are a model element, we should have an associated model.

        Object target = _container.getTarget();
        MModel model  = null;

        if(target instanceof MModelElement) {
            model = ((MModelElement) target).getModel();
        }

        // Build up an argument list and invoke the set method (remembering to
        // catch any problem

        Object[] args = { entry.getElement(model) };

        try {
            _setMethod.invoke(_container, args);
        }
        catch(Exception e) {
            cat.error(e.toString() + ". " +
                               this.getClass().toString() +
                               ": actionPerformed() - set method failed.", e);
        }

        // Having set a property, mark as needing saving. Commented out for
        // now, because this triggers even when we just show the combo for
        // the first time on selection.

        //Project p = ProjectBrowser.TheInstance.getProject();
        //p.setNeedsSave(true);
    }

}  /* End of class UMLComboBoxModel */
