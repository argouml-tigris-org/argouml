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

package org.argouml.uml.ui;
import ru.novosoft.uml.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;
import java.awt.*;

/**
 *  This class is an abstract superclass for classes that provide a list
 *  of UML model elements.
 *
 *  @author Curt Arnold
 */
abstract public class UMLModelElementListModel extends AbstractListModel implements UMLUserInterfaceComponent  {

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
    private int _currentModelElementSize = -1;
    /**
     * Set to true when an event suggests that the size needs to be recalculated.
     */
    private boolean _recalcSize;
    /** 
     *  A string indicating an NSUML event name that indicates that list may
     *  need to be updated.
     */
    private String _property;
    /*
     *  The string used to indicate no actual entries in the list.  Eventually,
     *    should be part of the profile or localization.
     */
    static String _none = "none";
    
    /**
     *  upper bound of length of list.
     */
    private int _upper;
    
    /**
     *   Creates a new list model
     *   @param container the container (typically a PropPanelClass or PropPanelInterface)
     *                    that provides access to the target classifier.
     *   @param property  a string that specifies the name of an event that should force a refresh
     *                       of the list model.  A null value will cause all events to trigger a refresh.
     *   @param showNone  if true, an element labelled "none" will be shown where there are
     *                        no actual entries in the list.
     */    
    public UMLModelElementListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        _container = container;
        _showNone = showNone;
        _property = property;
        _recalcSize = true;
        _upper = -1;
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
    protected void resetSize() {
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
     *   @returns number of "actual" list entries.
     */
    protected final int getModelElementSize()
    {
        if(_recalcSize) {
            _currentModelElementSize = recalcModelElementSize();
            if(_currentModelElementSize < 0) {
                return 0;
            }
            _recalcSize = false;
        }
        return _currentModelElementSize;
    }

    /**
     *  This method is called from getModelElementSize 
     *    when the list size has been marked as invalid.
     *  @returns number of "actual" list entries.
     *    
     */
    abstract protected int recalcModelElementSize();

    /**
     *  This method returns the model element that corresponds to
     *  to the specific index.  Called from getElementAt which handles
     *  entries for "none" and formatting of elements.
     *
     *  @param index index of model element (zero based).
     *  @returns corresponding model element
     */
    abstract protected MModelElement getModelElementAt(int index);
    
    /**
     *  This method returns the current "target" of the container.
     */
    final Object getTarget() {
        return _container.getTarget();
    }
    
    /**
     *  This method returns the container passed as an argument
     *  to the constructor 
     */
    final UMLUserInterfaceContainer getContainer() {
        return _container;
    }
    
    /**
     *  This method returns the size of the list (including any
     *     element for none).  
     *  @returns size of list
     *  @see #getModelElementSize
     */
    public int getSize() {
        int size = getModelElementSize();
        if(size == 0 && _showNone) {
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
     *  @returns representation of element
     *  @see #getModelElementAt
     *  @see #formatElement
     */
    public Object getElementAt(int index) {
        Object value = null;
        if(index >= 0 && index < getModelElementSize()) {
            MModelElement element = getModelElementAt(index);
            //
            //   shouldn't be null, here for debugging
            //
            if(element == null) {
                element = getModelElementAt(index);
            }
            value = formatElement(element);
        }
        else {
            if(index == 0 && _showNone) {
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
     *  @returns rendering of the ModelElement
     */
    public Object formatElement(MModelElement element) {
        return _container.formatElement(element);
    }

    //      documented in UMLUserInterfaceComponent
    public void targetChanged() {
        int oldSize = _currentModelElementSize;
        if(_showNone && oldSize == 0) oldSize = 1;
        resetSize();
        int newSize = getSize();
        
        if(newSize < oldSize) {
            if(newSize > 0) {
                fireContentsChanged(this,0,newSize-1);
            }
            fireIntervalRemoved(this,newSize,oldSize-1);
        }
        else {
            if(oldSize > 0) {
                fireContentsChanged(this,0,oldSize-1);
            }
            if(newSize > oldSize) {
                fireIntervalAdded(this,oldSize,newSize-1);
            }
        }
    }

    public void targetReasserted() {
    }
    
    //      documented in UMLUserInterfaceComponent
    public void roleAdded(final MElementEvent event) {
        String eventName = event.getName();
        if(_property == null || eventName == null || eventName.equals(_property)) {
            resetSize();
            Object addedValue = event.getAddedValue();
            boolean found = false;
            if(addedValue != null) {
                int size = getModelElementSize();
                for(int i = 0; i < size; i++) {
                    if(addedValue == getModelElementAt(i)) {
                        found = true;
                        fireIntervalAdded(this,i,i);
                    }
                }
            }
            //
            //  if that specific element wasn't found then
            //     mark everything as being changed
            if(!found) {
                fireContentsChanged(this,0,getSize()-1);
            }
        }
    }
    
    //      documented in UMLUserInterfaceComponent
    public void roleRemoved(final MElementEvent event) {
        String eventName = event.getName();
        if(_property == null || eventName == null || eventName.equals(_property)) {
            resetSize();
            fireContentsChanged(this,0,getSize()-1);
        }
    }
    
    //      documented in UMLUserInterfaceComponent
    public void recovered(final MElementEvent p1) {
    }
    
    //      documented in UMLUserInterfaceComponent
    public void listRoleItemSet(final MElementEvent p1) {
    }
    
    //      documented in UMLUserInterfaceComponent
    public void removed(final MElementEvent p1) {
    }
    
    //      documented in UMLUserInterfaceComponent
    public void propertySet(final MElementEvent p1) {
    }
    
    /**
     *  This method is called by context menu actions that
     *  desire to change to currently displayed object.
     *
     *  @param modelElement model element to display
     */
    public void navigateTo(MModelElement modelElement) {
        _container.navigateTo(modelElement);
    }
    
    /**
     *   This method is called in response to selecting "Open" from
     *   a context (pop-up) menu on this list.
     *
     *   @param index index of item to open (zero-based).
     */
    public void open(int index) {
        if(index >= 0 && index < _currentModelElementSize) {
            MModelElement modelElement = getModelElementAt(index);
            if(modelElement != null) {
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
     *  @returns "true" if popup menu should be displayed
     */
    public boolean buildPopup(JPopupMenu popup,int index) {
        UMLListMenuItem open = new UMLListMenuItem("Open...",this,"open",index);
        UMLListMenuItem delete = new UMLListMenuItem("Delete",this,"delete",index);
        if(_currentModelElementSize <= 0) {
            open.setEnabled(false);
            delete.setEnabled(false);
        }

        popup.add(open);
        UMLListMenuItem add =new UMLListMenuItem("Add...",this,"add",index);
        if(_upper >= 0 && _currentModelElementSize >= _upper) {
            add.setEnabled(false);
        }
        popup.add(add);
        popup.add(delete);

        UMLListMenuItem moveUp = new UMLListMenuItem("Move Up",this,"moveUp",index);
        if(index == 0) moveUp.setEnabled(false);
        popup.add(moveUp);
        UMLListMenuItem moveDown = new UMLListMenuItem("Move Down",this,"moveDown",index);
        if(index == getSize()-1) moveDown.setEnabled(false);
        popup.add(moveDown);
        return true;
    }
    
    /**
     *  This utility function may be called in the implemention of an Add action.
     *  It creates a new collection by adding an element at a specific offset
     *  in the sequence of an old collection.
     *
     *  @param oldCollection old collection
     *  @param newElement element to add to collection
     *  @param index position of element in new collection
     *  @returns new collection
     */
    static protected Collection addAtUtil(Collection oldCollection,MModelElement newItem,int index) {
        int oldSize = oldCollection.size();
        ArrayList newCollection = new ArrayList(oldSize + 1);
        Iterator iter = oldCollection.iterator();
        int i;
        for(i = 0; i < index; i ++) {
            newCollection.add(i,iter.next());
        }
        newCollection.add(i,newItem);
        for(;i <= oldSize;i++) {
            newCollection.add(i,iter.next());
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
     *  @returns new collection
     */
    static protected java.util.List moveUpUtil(Collection oldCollection,int index) {
        int size = oldCollection.size();
        ArrayList newCollection = new ArrayList(size);
        int i;
        Iterator iter = oldCollection.iterator();
        //
        //  move all the earliest elements 
        //
        for(i = 0; i < index -1; i++) {
            newCollection.add(i,iter.next());
        }
        Object swap1 = iter.next();
        Object swap2 = iter.next();
        newCollection.add(i++,swap2);
        newCollection.add(i++,swap1);
        for(; i < size; i++) {
            newCollection.add(i,iter.next());
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
     *  @returns new collection
     */
    static protected java.util.List moveDownUtil(Collection oldCollection,int index) {
        int size = oldCollection.size();
        ArrayList newCollection = new ArrayList(size);
        int i;
        Iterator iter = oldCollection.iterator();
        //
        //  move all the earliest elements 
        //
        for(i = 0; i < index; i++) {
            newCollection.add(i,iter.next());
        }
        Object swap1 = iter.next();
        Object swap2 = iter.next();
        newCollection.add(i++,swap2);
        newCollection.add(i++,swap1);
        for(; i < size; i++) {
            newCollection.add(i,iter.next());
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
     *  @returns new collection
     */
    static protected MModelElement elementAtUtil(Collection collection,int index,Class requiredClass) {
        Object obj = null;
        if(collection != null && index >= 0 && index < collection.size()) {
            if(collection instanceof java.util.List) {
                obj = ((java.util.List) collection).get(index);
            }
            else {
                Iterator iter = collection.iterator();
                Object temp;
                for(int i = 0; iter.hasNext(); i++) {
                    temp = iter.next();
                    if(i == index) {
                        obj = temp;
                        break;
                    }
                }
            }
        }
        return (MModelElement) obj;
    }
    
    
    
    
}




