// Copyright (c) 1996-99 The Regents of the University of California. All
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


// File: UMLThirdPartyListener.java
// Classes: UMLThirdPartyListener
// Original Author: mail@jeremybennett.com
// $Id$

// 23 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Created as a generic
// replacement for the (non-functioning) UMLNameEventListener.


package org.argouml.uml.ui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;


/**
 * <p>This class is used to dispatch a NSUML change event from a third party
 *   NSUML object (which may occur on a non-UI) thread) to propPanels.</p>
 *
 * <p>The class is created by the setThirdPartyEventListener() method within
 *   some {@link PropPanel} and then is passed as an argument
 *   to {@link SwingUtilities#invokeLater(Runnable)} 
 *   to be run on the user interface
 *   thread.</p>
 *
 * @author  Jeremy Bennett (mail@jeremybennett.com), 23 Apr 2002.
 */

public class UMLThirdPartyEventListener implements MElementListener {


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>The container which is listening for all these events.</p>
     */

    private PropPanel _propPanel;


    /**
     * <p>The model we are listening on.</p>
     */

    private MModel _model = null;


    /**
     * <p>The list of pairs {metaclass, property, ...} to which we are applying
     *   listeners. The metaclass is of type {@link Class} and the property of
     *   type {@link String}. A <code>null</code> property means match all
     *   events for this metaclass.</p>
     */

    private java.util.List _pairList;


    /**
     * <p>The list of NSUML model elements to which we are listening. We keep
     *   this list so we can subsequently remove the listeners.</p>
     *
     * <p>We use {@link HashSet}, since we want a set (no duplicates), but
     *   don't need ordering, and this is the only such concrete class in the
     *   Java library (i.e implementing the {@link Set} interface).</p>
     */

    private HashSet _listenerList = new HashSet();


    ///////////////////////////////////////////////////////////////////////////
    //
    // Constructors
    //
    ///////////////////////////////////////////////////////////////////////////
    
    /**
     * <p>Creates a new listener.</p>
     *
     * <p>Sets up the various instance variables, adds listeners for all
     *   objects in the target list for the container and adds listeners to all
     *   namespaces for this object to find when new NSUML objects are
     *   created. We also listen to the target object itself, in case its model
     *   is changed.</p>
     *
     * <p>All listeners (even those for the propPanel) come through us, so we
     *   can check if they are what is wanted, and if so dispatch them on.</p>
     *
     *  @param propPanel   The propPanel (a {@link Container} implementing
     *                     {@link UMLUserInterfaceContainer}) to which changes
     *                     are dispatched. 
     *
     *  @param pairList    An array of pairs {metaclass, property, ...}
     *                     (possibly null) of NSUML classes and their
     *                     properties to be monitored. For example, passing
     *                     <code>{MExtend.class, "condition"}</code> will
     *                     monitor condition changes on extend relationships.
     */

    public UMLThirdPartyEventListener(PropPanel      propPanel,
                                      java.util.List pairList) {

        // Set up the instance variables

        _propPanel = propPanel;
        _pairList  = pairList;

        // Invoke the target changed method. This will start up listeners if we
        // now have a model.

        targetChanged();
    }
    

    ///////////////////////////////////////////////////////////////////////////
    //
    // Accessor methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Add a listener to a model element if it is either a namespace or has
     *   a metaclass on our list of targets to listen to. If it is a namespace,
     *   recurse to add any owned elements as well.</p>
     *
     * <p>Do nothing if we are given a <code>null</code> argument. This makes
     *   recursion easier.</p>
     *
     * <p>We listen to namespaces so we can detect any new owned elements.</p>
     *
     * @param modelElement  The model element to add. */

    private void addListeners(MModelElement modelElement) {

        // Nothing to do if this model element is null

        if (modelElement == null) {
            return;
        }

        // Add a listener to the modelElement if it is a namespace, or on the
        // pair list. Only add if there is not already a listener.

        if ((modelElement instanceof MNamespace) ||
            isTargetEvent(modelElement.getClass(), null)) {

            if (!(_listenerList.contains(modelElement))) {
                modelElement.addMElementListener(this);
                _listenerList.add(modelElement);
            }
        }

        // Give up here if this not a namespace or is a namespace with no owned
        // elements.

        if (!(modelElement instanceof MNamespace)) {
            return;
        }

        MNamespace ns            = (MNamespace) modelElement;
        Collection ownedElements = ns.getOwnedElements();

        if (ownedElements == null) {
            return;
        }

        // Loop through each element in the namespace, recursing to add any
        // matching

        Iterator iter = ownedElements.iterator();

        while(iter.hasNext()) {
            addListeners((MModelElement) iter.next());
        }
    }


    /**
     * <p>A method to remove all listeners associated with an element (and its
     *   sub-namespaces if it is a namespace).</p> 
     *
     * <p>Do nothing if we are given a <code>null</code> argument. This makes
     *   recursion easier.</p>
     *
     * @param modelElement  The model element whose listeners should be
     *                      removed.
     */

    public void removeListeners(MModelElement modelElement) {

        // Give up if the model element is null

        if (modelElement == null) {
            return;
        }

        // Remove the listener to the element if it has one

        if (_listenerList.contains(modelElement)) {
            modelElement.removeMElementListener(this);
            _listenerList.remove(modelElement);
        }

        // Give up here if this not a namespace or is a namespace with no owned
        // elements.

        if (!(modelElement instanceof MNamespace)) {
            return;
        }

        MNamespace ns            = (MNamespace) modelElement;
        Collection ownedElements = ns.getOwnedElements();

        if (ownedElements == null) {
            return;
        }

        // Loop through the owned elements, removing their listeners and
        // recursing if they are a namespace.

        Iterator iter = ownedElements.iterator();

        while (iter.hasNext()) {
            removeListeners((MModelElement) iter.next());
        }
    }
            

    /**
     * <p>A method to remove all listeners.</p> 
     *
     * <p>Used prior to replacing a listener. Just loops through all elements
     *   on the listener list.</p>
     */

    public void removeAllListeners() {
        Iterator iter = _listenerList.iterator();

        while (iter.hasNext()) {
            ((MModelElement)(iter.next())).removeMElementListener(this);
        }
    }


    /**
     * <p>Set the target list.</p>
     *
     * @param targetList  The target list to set.
     */

    public void setPairList(java.util.List pairList) {

        // Copy to the instance variable

        _pairList = pairList;
    }


    /**
     * <p>The target of the container has changed. If this has led to a change
     *   of model, we need to start/restart everything.</p>
     */

    void targetChanged() {

        // Get the target. If its not a model element, we can do nothing more.

        Object target = _propPanel.getTarget();

        if (!(target instanceof MModelElement)) {
            return;
        }

        // Get the model. If it's changed, we close down all listeners, and
        // start up some new ones (which will do nothing if it has changed to
        // null).

        MModel model = ((MModelElement) target).getModel();

        if(model != _model) {
            removeAllListeners();
            _model = model;
            addListeners(model);
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // Main methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>A utility to see if a metaclass/property pair is on our target
     *   list.</p>
     *
     * <p>The metaClass should not be <code>null</code> (if it is the method
     *   prints a rude message and returns <code>false</code>).</p>
     *
     * <p>The property may be <code>null</code> in which case the method will
     *   succeed if the metaClass is on the target list with any property.</p>
     */

     private boolean isTargetEvent(Class metaClass, String property) {

         // Deal with the pathological case

         if (metaClass == null) {
             System.out.println(getClass().toString() + ": isTargetEvent() " +
                                "called with null metaclass");
             return false;
         }

         // Search through the metaclasses in the list until we find one which
         // is the same as or a superclass/superinterface of that given.

         for (int index = 0 ; index < _pairList.size() ; index += 2) {

             Class targetClass = (Class) (_pairList.get(index));

             // Skip on if we don't match classes

             if (!(targetClass.isAssignableFrom(metaClass))) {
                 continue;
             }

             // We succeed if we are looking for a null property or the
             // property is the same as the target property

             String targetProperty = (String) (_pairList.get(index + 1));

             if ((property == null) || (targetProperty == null) ||
                 (property.equals(targetProperty))) {
                 return true ;
             }
         }

         // If we ran off the end we failed

         return false;
     }


    /**
     * <p>A utility to provide the difference between two collections.</p>
     *
     * @param a  A collection
     *
     * @param b  A collection
     *
     * @return   The collection of all entities in <code>a</code> that are not
     *           in <code>b</code>. 
     */

    private Collection difference(Collection a, Collection b) {

        // Deal with the pathalogical cases

        if ((a == null) || (b == null)) {
            return a;
        }

        // Loop through the first collection adding any elements not in b to
        // the new result vector.

        Vector   res  = new Vector();
        Iterator iter = a.iterator();

        while (iter.hasNext()) {
            Object element = iter.next();

            if (!(b.contains(element))) {
                res.add(element);
            }
        }

        return res;
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    // Event handlers
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Invoked by NSUML when there is a propertySet event on some model
     *   element to which we are listening.</p>
     *
     * <p>We deal with two cases.</p>
     *
     * <ol>
     *   <li><p>Setting the ownedElements set of a namespace. In this case we
     *     must clear down all listeners on the old elements that are not in
     *     the new elements (recursing through sub-namespaces) and add
     *     listeners to all the new elements that were not in the old elements
     *     (recursing through sub-namespaces).</p></li>
     *
     *   <li><p>Setting a property for a metaclass that is on our target
     *     list. In this case, we must dispatch the event to the propPanel to
     *     process.</p></li>
     * </ol>
     *
     * <p><em>Note</em>. These two options are not exclusive.</p>
     *
     * @param mee  NSUML event that triggered us.
     */

    public void propertySet(MElementEvent mee) {

        //System.out.println("**** property set: " + mee);

        // We can only do something if we have a non-null source object that is
        // an MModelElement and a property that is non-null.

        Object eventSource = mee.getSource();
        String eventName   = mee.getName();

        if ((!(eventSource instanceof MModelElement)) ||
            (eventName == null)) {
            return ;
        }

        MModelElement modelElement = (MModelElement) eventSource;

        // First case: setting the owned elements of a namespace. This is a
        // little unlikely - we are more likely to see individual additions and
        // removals, but we must cover the case.

        if ((modelElement instanceof MNamespace) &&
            eventName.equals("ownedElement")) {

            Collection oldElements = (Collection) (mee.getOldValue());
            Collection newElements = (Collection) (mee.getNewValue());

            // Remove any listeners associated with old elements that are not
            // in the set of new elements (recursing to take out
            // sub-namespaces)

            Collection removedSet = difference(oldElements, newElements);
            Iterator iter = removedSet.iterator();

            while (iter.hasNext()) {
                removeListeners((MModelElement) (iter.next()));
            }

            // Add listeners for all the new elements that were not in the old
            // set (anything owned by a namespace must be a model element).

            Collection addedSet = difference(newElements, oldElements);
            iter = addedSet.iterator();

            while (iter.hasNext()) {
                addListeners((MModelElement) (iter.next()));
            }
        }

        // Second case: setting a property of a metaclass on our target
        // list. If so dispatch the event to the propPanel.

        if (isTargetEvent(modelElement.getClass(), eventName)) {
            UMLChangeDispatch dispatch =
                new UMLChangeDispatch(_propPanel,
                                      UMLChangeDispatch.PROPERTY_SET);
            dispatch.propertySet(mee);
            SwingUtilities.invokeLater(dispatch);
        }
    }
           

    /**
     * <p>Invoked by NSUML when there is a listRoleItemSet event on some model
     *   element to which we are listening.</p>
     *
     * <p>We need only deal with the case of setting a property for a metaclass
     *   that is on our target list. This event cannot be affecting the
     *   target's model, nor its owned elements, since it applies only to UML
     *   elements that are part of an ordered multiplicity
     *   (e.g. parameters).</p>
     *
     * <p>In this case, we must dispatch the event to the propPanel to
     *   process.</p>
     *
     * @param mee  NSUML event that triggered us.
     */

    public void listRoleItemSet(MElementEvent mee) {

        System.out.println("**** list role item set: " + mee);

        // We can only do something if we have a non-null source object that is
        // a MModelElement and a property that is non-null.

        Object eventSource = mee.getSource();
        String eventName   = mee.getName();

        if ((!(eventSource instanceof MModelElement)) ||
            (eventName == null)) {
            return ;
        }

        // Are we setting a property of a metaclass on our target list? If
        // so dispatch the event to the propPanel.

        if (isTargetEvent(eventSource.getClass(), eventName)) {
            UMLChangeDispatch dispatch =
                new UMLChangeDispatch(_propPanel,
                                      UMLChangeDispatch.LIST_ROLE_ITEM_SET);
            dispatch.listRoleItemSet(mee);
            SwingUtilities.invokeLater(dispatch);
        }
    }


    /**
     * <p>Invoked by NSUML when there is a recovered event on some model
     *   element to which we are listening. This means a model
     *   element to which we are listening, that was deleted has now been
     *   restored.</p>
     *
     * <p><em>Note</em>. Its not clear that we can ever receive this, since we
     *   will have deleted the listeners when the element was originally
     *   deleted.</p>
     *
     * <p>We deal with two cases.</p>
     *
     * <ol>
     *   <li><p>Recovering a namespace. In this case
     *     we must clear down all listeners on the old elements (recursing
     *     through sub-namespaces) and add listeners to all the new elements
     *     (recursing through sub-namespaces).</p></li>
     *
     *   <li><p>Recovering a metaclass that is on our target
     *     list. In this case, we must dispatch the event to the propPanel to
     *     process and add a listener for it.</p></li>
     * </ol>
     *
     * <p><em>Note</em>. These two options are not exclusive.</p>
     *
     * @param mee  NSUML event that triggered us.
     */

    public void recovered(MElementEvent mee) {

        System.out.println("**** recovered: " + mee);

        // We can only do something if we have a non-null source object that is
        // a MModelElement.

        Object eventSource = mee.getSource();

        if (!(eventSource instanceof MModelElement)) {
            return ;
        }

        MModelElement modelElement = (MModelElement) eventSource;

        // First case: recovering a namespace. We assume that all listeners
        // had been removed and just add them back.

        if (modelElement instanceof MNamespace) {
            addListeners(modelElement);
        }

        // Second case: recovering a metaclass on our target list. If so set a
        // new listener on it and dispatch the event to the propPanel.

        if (isTargetEvent(modelElement.getClass(), null)) {
            addListeners(modelElement);

            UMLChangeDispatch dispatch =
                new UMLChangeDispatch(_propPanel,
                                      UMLChangeDispatch.RECOVERED);
            dispatch.recovered(mee);
            SwingUtilities.invokeLater(dispatch);
        }
    }
    

    /**
     * <p>Invoked by NSUML when there is a removed event on some model
     *   element to which we are listening. This means a model element
     *   to which we were listening has been deleted from the model.</p>
     *
     * <p>We deal with two cases.</p>
     *
     * <ol>
     *   <li><p>Removing a namespace. In this case we must clear down all
     *     listeners on the elements in the namespace (recursing through
     *     sub-namespaces). Again We ought not to see this, since we should
     *     know of the deletion of the namespace (as an owned element of some
     *     other namespace) and have already deleted all listeners.</p></li>
     *
     *   <li><p>Removing an instance of a metaclass that is on our target
     *     list. In this case, we must dispatch the event to the propPanel to
     *     process, and then delete all listeners on that element.</p></li>
     * </ol>
     *
     * <p><em>Note</em>. These two options are not exclusive.</p>
     *
     * @param mee  NSUML event that triggered us.
     */

    public void removed(MElementEvent mee) {

        System.out.println("**** removed: " + mee);

        // We can only do something if we have a non-null source object that is
        // an MModelElement and a property that is non-null.

        Object eventSource = mee.getSource();

        if (!(eventSource instanceof MModelElement)) {
            return ;
        }

        MModelElement modelElement = (MModelElement) eventSource;

        // First case: removing a namespace. Just take out its listeners

        if (eventSource instanceof MNamespace) {
            removeListeners(modelElement) ;
        }

        // Second case: removing an instance of a metaclass on our target
        // list. If so dispatch the event to the propPanel, then remove all
        // listeners on the source.

        if (isTargetEvent(modelElement.getClass(), null)) {
            UMLChangeDispatch dispatch =
                new UMLChangeDispatch(_propPanel,
                                      UMLChangeDispatch.REMOVED);
            dispatch.removed(mee);
            SwingUtilities.invokeLater(dispatch);

            removeListeners(modelElement);
        }
    }


    /**
     * <p>Invoked by NSUML when there is a role added event on some model
     *   element to which we are listening. This means that some property with
     *   multiplicity > 1 of an element to which we are listening has had a new
     *   instance of that property added.</p>
     *
     * <p>We deal with two cases.</p>
     *
     * <ol>
     *   <li><p>Adding to the ownedElements set of a namespace. In this case we
     *     must add a listener to the added element if appropriate (recursing
     *     through sub-namespaces if it is a namespace). If the object added is
     *     of interest (whatever the property) We also must dispatch
     *     a roleAdded event to the propPanel.</p></li>
     *
     *   <li><p>Adding to a property for a metaclass that is on our target
     *     list. In this case, we must dispatch the event to the propPanel to
     *     process.</p></li>
     * </ol>
     *
     * <p><em>Note</em>. These two options are not exclusive.</p>
     *
     * @param mee  NSUML event that triggered us.
     */

    public void roleAdded(MElementEvent mee) {

        //System.out.println("**** role added: " + mee);

        // We can only do something if we have a non-null source object that is
        // an MModelElement and a property that is non-null.

        Object eventSource = mee.getSource();
        String eventName   = mee.getName();

        if ((!(eventSource instanceof MModelElement)) ||
            (eventName == null)) {
            return ;
        }

        MModelElement modelElement = (MModelElement) eventSource;

        // First case: adding to the owned elements of a namespace.

        if ((modelElement instanceof MNamespace) &&
            eventName.equals("ownedElement")) {

            Object addedValue = mee.getAddedValue();

            // Only know how to handle model elements

            if (addedValue instanceof MModelElement) { 

                // Add listener for the new element if appropriate. For a
                // namespace, we can just use collectListeners, but for
                // everything else, we check if we can add it.

                addListeners((MModelElement) addedValue);

                // If the addedValue is of interest, we notify the propPanel

                if (isTargetEvent(addedValue.getClass(), null)) {
                    UMLChangeDispatch dispatch =
                        new UMLChangeDispatch(_propPanel,
                                              UMLChangeDispatch.ROLE_ADDED);
                    dispatch.roleAdded(mee);
                    SwingUtilities.invokeLater(dispatch);
                }
            }
        }

        // Second case: adding to a property of a metaclass on our target
        // list. If so dispatch the event to the propPanel.

        if (isTargetEvent(modelElement.getClass(), eventName)) {

            UMLChangeDispatch dispatch =
                new UMLChangeDispatch(_propPanel,
                                      UMLChangeDispatch.ROLE_ADDED);
            dispatch.roleAdded(mee);
            SwingUtilities.invokeLater(dispatch);
        }
    }


    /**
     * <p>Invoked by NSUML when there is a role removed event on some model
     *   element to which we are listening. This means a property with
     *   multiplicity >1 of something to which we were listening has had an
     *   element deleted.</p>
     *
     * <p>We deal with two cases.</p>
     *
     * <ol>
     *   <li><p>Removing a member of the ownedElements set of a namespace. In
     *     this case we must clear down all listeners on the old element
     *     (recursing through sub-namespaces). If it is a class of interest, we
     *     should also dispatch to the property panel.</p></li>
     *
     *   <li><p>Removing a member of a property for a metaclass that is on our
     *     pairs list. In this case, we must dispatch the event to the
     *     propPanel to process.</p></li>
     * </ol>
     *
     * <p><em>Note</em>. These two options are not exclusive.</p>
     *
     * @param mee  NSUML event that triggered us.
     */

    public void roleRemoved(MElementEvent mee) {

        //System.out.println("**** Role removed: " + mee);

        // We can only do something if we have a non-null source object that is
        // an MModelElement and a property that is non-null.

        Object eventSource = mee.getSource();
        String eventName   = mee.getName();

        if ((!(eventSource instanceof MModelElement)) ||
            (eventName == null)) {
            return ;
        }

        MModelElement modelElement = (MModelElement) eventSource;

        // First case: removing one of the owned elements of a namespace.

        if ((modelElement instanceof MNamespace) &&
            eventName.equals("ownedElement")) {

            Object removedValue = mee.getRemovedValue();

            // Only know how to handle model elements

            if (removedValue instanceof MModelElement) { 

                // Remove any listeners associated with the old element
                // (recursing to take out sub-namespaces)

                removeListeners((MModelElement) removedValue);

                // If the removed element was on our list of interest, we
                // should tell the propPanel

                if (isTargetEvent(removedValue.getClass(), null)) {

                    UMLChangeDispatch dispatch =
                        new UMLChangeDispatch(_propPanel,
                                              UMLChangeDispatch.ROLE_REMOVED);
                    dispatch.roleRemoved(mee);
                    SwingUtilities.invokeLater(dispatch);
                }
            }
        }

        // Second case: removing a member of a property of a metaclass on our
        // target list. If so dispatch the event to the propPanel.

        if (isTargetEvent(modelElement.getClass(), eventName)) {
            UMLChangeDispatch dispatch =
                new UMLChangeDispatch(_propPanel,
                                      UMLChangeDispatch.ROLE_REMOVED);
            dispatch.roleRemoved(mee);
            SwingUtilities.invokeLater(dispatch);
        }
    }
    
} /* End of class UMLThirdPartyListener */


