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

// 26 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Fixed bug in
// roleAdded, which seemed to be setting _upper to zero for any element that
// didn't match. The effect was to disable all add functions for everything
// else, once a promiscuous listener caused all events to be sent here.

// 28 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Converted the return
// type of addAtUtil to List, since it does return an ordered result, and some
// NSUML uses require an ordered (subclass of List) result. The input value is
// left as Collection for backwards compatibility.

// 28 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Uncovered a deep bug
// in addAtUtil, which forgets to increment the counter after adding the new
// element. The reason that this does not usually cause problems "I think" is
// that people have been adding NSUML elements at both ends of association (not
// needed - if you set one end, NSUML will do the other), which compensates for
// this.

// 16 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Added implementation
// of NotationContext interface, to allow underlying classes to use the
// Notation package. Ultimately we should consider moving this into
// UMLUserInterfaceComponent.

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.AbstractListModel;
import javax.swing.JPopupMenu;

import org.argouml.application.api.NotationContext;
import org.argouml.application.api.NotationName;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 *  This class is an abstract superclass for classes that provide a list
 *  of UML model elements.
 *
 *  @author Curt Arnold
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by {@link org.argouml.uml.ui.UMLModelElementListModel2},
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
abstract public class UMLModelElementListModel
    extends AbstractListModel
    implements UMLUserInterfaceComponent, NotationContext {

    /**
     *   The container that provides the "target" model element.
     */
    private UMLUserInterfaceContainer _container;
    /**
     *  If "true" then a list entry (typically labelled "none") will
     *  be displayed when there are no actual entries in the list.
     */
    private boolean _showNone;
    /**
     * The number of actual entries in the list.
     */
    private int _currentModelElementSize = 0;
    /**
     * Set to true when an event suggests that the size needs to be recalculated.
     */
    private boolean _recalcSize;
    /**
     *  A string indicating an NSUML event name that indicates that list may
     *  need to be updated.
     */
    private String _property;
    /**
     *  The string used to indicate no actual entries in the list.  Eventually,
     *    should be part of the profile or localization.
     */
    private String _none = "none";

    /**
     *  upper bound of length of list.
     */
    protected int _upper;
    public final int NO_LIMIT = -1;

    /**
     *   Creates a new list model
     *   @param container the container (typically a PropPanelClass or PropPanelInterface)
     *                    that provides access to the target classifier.
     *   @param property  a string that specifies the name of an event that should force a refresh
     *                       of the list model.  A null value will cause all events to trigger a refresh.
     *   @param showNone  if true, an element labelled "none" will be shown where there are
     *                        no actual entries in the list.
     */
    public UMLModelElementListModel(
        UMLUserInterfaceContainer container,
        String property,
        boolean showNone) {
        _container = container;
        _showNone = showNone;
        _property = property;
        _recalcSize = true;
        _upper = NO_LIMIT;
        _none = _container.localize("none");
        if (_none == null)
            _none = "none";
    }

    public int getUpperBound() {
        return _upper;
    }

    public void setUpperBound(int newBound) {
        _upper = newBound;
    }

    /**
     *  Called when an external event may have changed the size of the list
     *  to force recalculation of list size.
     */
    public void resetSize() {
        _recalcSize = true;
    }

    /**
     *  Returns NSUML event name that is monitored, may be null.
     */
    public final String getProperty() {
        return _property;
    }

    /**
     *  Determines the number of "actual" entries in the list.  May be
     *     overriden in combination with getModelElementAt,
     *     but typically recalcModelElementSize is overriden.
     *   @return number of "actual" list entries.
     */
    protected final int getModelElementSize() {
        // if(_recalcSize) {
        _currentModelElementSize = recalcModelElementSize();
        if (_currentModelElementSize < 0) {
            return 0;
        }
        //    _recalcSize = false;
        // }
        return _currentModelElementSize;
    }

    /**
     *  This method is called from getModelElementSize
     *    when the list size has been marked as invalid.
     *  @return number of "actual" list entries.
     *
     */
    abstract protected int recalcModelElementSize();

    /**
     *  This method returns the model element that corresponds to
     *  to the specific index.  Called from getElementAt which handles
     *  entries for "none" and formatting of elements.
     *
     *  @param index index of model element (zero based).
     *  @return corresponding model element
     */
    abstract protected MModelElement getModelElementAt(int index);

    /**
     *  This method returns the current "target" of the container.
     */
    protected final Object getTarget() {
        return _container.getTarget();
    }

    /**
     *  This method returns the container passed as an argument
     *  to the constructor
     */
    protected final UMLUserInterfaceContainer getContainer() {
        return _container;
    }

    /**
     *  This method returns the size of the list (including any
     *     element for none).
     *  @return size of list
     *  @see #getModelElementSize
     */
    public int getSize() {
        int size = getModelElementSize();
        if (size == 0 && _showNone) {
            size = 1;
        }
        return size;
    }

    /**
     *  This method returns an object (typically a String)
     *  to represent a particular element in this list (including
     *  any element for "none").
     *
     *  @param index index for element (zero-based)
     *  @return representation of element
     *  @see #getModelElementAt
     *  @see #formatElement
     */
    public Object getElementAt(int index) {
        Object value = null;
        if (index >= 0 && index < getModelElementSize()) {
            MModelElement element = getModelElementAt(index);
            //
            //   shouldn't be null, here for debugging
            //
            if (element == null) {
                element = getModelElementAt(index);
            }
            value = formatElement(element);
        } else {
            if (index == 0 && _showNone) {
                value = _none;
            }
        }
        return value;
    }

    /**
     *   This method returns a rendering (typically a String) of the model element for the list.
     *   Default implementation defers to the current Profile of the container, but this
     *   method may be overriden.
     *
     *  @param @element model element
     *  @return rendering of the ModelElement
     */
    public Object formatElement(MModelElement element) {
        return _container.formatElement(element);
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        int oldSize = _currentModelElementSize;
        if (_showNone && oldSize == 0)
            oldSize = 1;
        resetSize();
        int newSize = getSize();

        if (newSize < oldSize) {
            if (newSize > 0) {
                fireContentsChanged(this, 0, newSize - 1);
            }
            fireIntervalRemoved(this, newSize, oldSize - 1);
        } else {
            if (oldSize > 0) {
                fireContentsChanged(this, 0, oldSize - 1);
            }
            if (newSize > oldSize) {
                fireIntervalAdded(this, oldSize, newSize - 1);
            }
        }
    }

    public void targetReasserted() {
        resetSize();
        fireContentsChanged(this, 0, getSize() - 1);
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(MElementEvent)
     */
    public void roleAdded(final MElementEvent event) {
        String eventName = event.getName();
        if (_property == null
            || eventName == null
            || eventName.equals(_property)) {
            resetSize();
            Object addedValue = event.getAddedValue();
            boolean found = false;
            if (addedValue != null) {
                int size = getModelElementSize();
                for (int i = 0; i < size; i++) {
                    if (addedValue == getModelElementAt(i)) {
                        found = true;
                        fireIntervalAdded(this, i, i);
                    }
                }
            }
            //
            //  if that specific element wasn't found then
            //     mark everything as being changed
            if (!found) {
                resetSize();
                fireContentsChanged(this, 0, getSize() - 1);
            }
        } else {
            // The following two lines appear to be an error. It has the effect
            // of disabling add for other elements when any element has put on
            // a promiscuous listener. Replaced by the following line

            // if(_upper < 0) _upper = 0;
            // fireIntervalAdded(this,_upper,_upper);
            fireIntervalAdded(this, 0, 0);
        }
    }

    //      documented in UMLUserInterfaceComponent
    public void roleRemoved(final MElementEvent event) {
        String eventName = event.getName();
        if (_property == null
            || eventName == null
            || eventName.equals(_property)) {
            resetSize();
            fireContentsChanged(this, 0, getSize() - 1);
        }
    }

    //      documented in UMLUserInterfaceComponent
    public void recovered(final MElementEvent p1) {
        resetSize();
        fireContentsChanged(this, 0, getSize() - 1);
    }

    //      documented in UMLUserInterfaceComponent
    public void listRoleItemSet(final MElementEvent p1) {
        resetSize();
        fireContentsChanged(this, 0, getSize() - 1);
    }

    //      documented in UMLUserInterfaceComponent
    public void removed(final MElementEvent p1) {
        resetSize();
        fireContentsChanged(this, 0, getSize() - 1);
    }

    //      documented in UMLUserInterfaceComponent
    public void propertySet(final MElementEvent p1) {
        resetSize();
        fireContentsChanged(this, 0, getSize() - 1);
    }

    /**
     *  This method is called by context menu actions that
     *  desire to change to currently displayed object.
     *  @deprecated 
     *  @param modelElement model element to display
     */
    public void navigateTo(MModelElement modelElement) {
        TargetManager.getInstance().setTarget(modelElement);
    }
    
    public void navigateTo(Object modelElement) {
        navigateTo((MModelElement)modelElement);
    }

    /**
     *   This method is called in response to selecting "Open" from
     *   a context (pop-up) menu on this list.
     *
     *   @param index index of item to open (zero-based).
     */
    public void open(int index) {
        if (index >= 0 && index < _currentModelElementSize) {
            MModelElement modelElement = getModelElementAt(index);
            if (modelElement != null) {
                navigateTo(modelElement);
            }
        }
    }

    /**
     *  This method builds a context (pop-up) menu for the list.  This method
     *  may be overriden for lists that have additional menu items or when
     *  the default list of actions is inappropriate.
     *
     *  @param popup popup menu
     *  @param index index of selected list item
     *  @return "true" if popup menu should be displayed
     */
    public boolean buildPopup(JPopupMenu popup, int index) {
        UMLUserInterfaceContainer container = getContainer();
        UMLListMenuItem open =
            new UMLListMenuItem(
                container.localize("Open"),
                this,
                "open",
                index);
        UMLListMenuItem delete =
            new UMLListMenuItem(
                container.localize("Delete"),
                this,
                "delete",
                index);
        if (_currentModelElementSize <= 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }

        popup.add(open);
        UMLListMenuItem add =
            new UMLListMenuItem(container.localize("Add"), this, "add", index);
        if (_upper >= 0 && _currentModelElementSize >= _upper) {
            add.setEnabled(false);
        }
        popup.add(add);
        popup.add(delete);

        UMLListMenuItem moveUp =
            new UMLListMenuItem(
                container.localize("Move Up"),
                this,
                "moveUp",
                index);
        if (index == 0)
            moveUp.setEnabled(false);
        popup.add(moveUp);
        UMLListMenuItem moveDown =
            new UMLListMenuItem(
                container.localize("Move Down"),
                this,
                "moveDown",
                index);
        if (index == getSize() - 1)
            moveDown.setEnabled(false);
        popup.add(moveDown);
        return true;
    }

    public static java.util.List addAtUtil(
        Collection oldCollection,
        Object newItem,
        int index) {
        return addAtUtil(oldCollection, (MModelElement)newItem, index);
    }

    /**
     * <p>This utility function may be called in the implemention of an Add
     *   action.  It creates a new collection by adding an element at a
     *   specific offset in the sequence of an old collection.</p>
     *
     * <p>Historically this took as argument and returned result of type {@link
     *   Collection}. However this is not specifically an ordered
     *   interface. The current version returns a result of type {@link
     *   java.util.List}, which is the ordered sub-interface of
     *   Collection. This will keep some NSUML routines (which have ordered
     *   arguments, and expect a List object) happy.</p>
     *
     * <p><em>Note</em>. There are two List types in Java (the other is part of
     *   awt). This is java.util.List.</p>
     *
     * <p>For compatibility with existing code, the argument is left as type
     *   {@link Collection}, although it would be wise to always use {@link
     *   java.util.List} in new code.</p>
     *
     *  @param oldCollection  old collection
     *
     *  @param newElement     element to add to collection
     *
     *  @param index          position of element in new collection
     *
     *  @return               new list */

    public static java.util.List addAtUtil(
        Collection oldCollection,
        MModelElement newItem,
        int index) {

        int oldSize = oldCollection.size();

        ArrayList newCollection = new ArrayList(oldSize + 1);
        Iterator iter = oldCollection.iterator();

        int i;
        for (i = 0; i < index; i++) {
            newCollection.add(i, iter.next());
        }

        newCollection.add(i, newItem);

        // Don't forget to step past the one we've just added (bug fix by
        // Jeremy Bennett).

        for (i++; i <= oldSize; i++) {
            newCollection.add(i, iter.next());
        }

        return newCollection;
    }

    /**
     *  This utility function may be called in the implemention of an MoveUp action.
     *  It creates a new collection by swapping the element at index with the element
     *  at index-1.
     *
     *  @param oldCollection old collection
     *  @param index index of element to move up.
     *  @return new collection
     */
    public static java.util.List moveUpUtil(
        Collection oldCollection,
        int index) {
        int size = oldCollection.size();
        ArrayList newCollection = new ArrayList(size);
        int i;
        Iterator iter = oldCollection.iterator();
        //
        //  move all the earliest elements
        //
        for (i = 0; i < index - 1; i++) {
            newCollection.add(i, iter.next());
        }
        Object swap1 = iter.next();
        Object swap2 = iter.next();
        newCollection.add(i++, swap2);
        newCollection.add(i++, swap1);
        for (; i < size; i++) {
            newCollection.add(i, iter.next());
        }
        return newCollection;
    }

    /**
     *  This utility function may be called in the implemention of an MoveDown action.
     *  It creates a new collection by swapping the element at index with the element
     *  at index+1.
     *
     *  @param oldCollection old collection
     *  @param index index of element to move down.
     *  @return new collection
     */
    public static java.util.List moveDownUtil(
        Collection oldCollection,
        int index) {
        int size = oldCollection.size();
        ArrayList newCollection = new ArrayList(size);
        int i;
        Iterator iter = oldCollection.iterator();
        //
        //  move all the earliest elements
        //
        for (i = 0; i < index; i++) {
            newCollection.add(i, iter.next());
        }
        Object swap1 = iter.next();
        Object swap2 = iter.next();
        newCollection.add(i++, swap2);
        newCollection.add(i++, swap1);
        for (; i < size; i++) {
            newCollection.add(i, iter.next());
        }
        return newCollection;
    }

    /**
     *  This utility function may be called in the implemention of getElementAt.
     *  It determines the element at a specific index by brute iteration through
     *  a collection if necessary.
     *
     *  @param oldCollection old collection
     *  @param index index of element to move down.
     *  @return new collection
     */
    public static MModelElement elementAtUtil(
        Collection collection,
        int index,
        Class requiredClass) {
        Object obj = null;
        if (collection != null && index >= 0 && index < collection.size()) {
            if (collection instanceof java.util.List) {
                obj = ((java.util.List)collection).get(index);
            } else {
                Iterator iter = collection.iterator();
                Object temp;
                for (int i = 0; iter.hasNext(); i++) {
                    temp = iter.next();
                    if (i == index) {
                        obj = temp;
                        break;
                    }
                }
            }
        }
        return (MModelElement)obj;
    }

    /**
     * <p>Gives a notation name, so subclasses can use the Notation
     *   package.</p>
     *
     * <p>This default implementation simply requests the default notation.</p>
     *
     * @return  The notation to use. In this implementation always
     *          <code>null</code>, meaning use the default notation.
     */

    public NotationName getContextNotation() {
        return null;
    }

    /**
     * Standard delete method.
     * @param index
     */
    public void delete(int index) {
        MModelElement modElem = getModelElementAt(index);
        Object target = TargetManager.getInstance().getTarget();
        ProjectBrowser.getInstance().setTarget(modElem);
        ActionEvent event = new ActionEvent(this, 1, "delete");
        ActionRemoveFromModel.SINGLETON.actionPerformed(event);
        fireIntervalRemoved(this, index, index);
        if (!target.equals(modElem)) {
            ProjectBrowser.getInstance().setTarget(target);
        }
    }
}
