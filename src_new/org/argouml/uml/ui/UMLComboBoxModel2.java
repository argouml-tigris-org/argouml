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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;


import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * ComboBoxmodel for UML modelelements. This implementation does not use 
 * reflection and seperates Model, View and Controller better then does
 * UMLComboBoxModel. In the future UMLComboBoxModel and UMLComboBox will be
 * replaced with this implementation to improve performance.
 */
public abstract class UMLComboBoxModel2
    extends DefaultComboBoxModel
    implements UMLUserInterfaceComponent {

    protected UMLUserInterfaceContainer container = null;
    protected int selectedIndex = -1;
    protected String propertySetName;
    protected List list = new ArrayList();
    
	
    /**
     * Constructs a model for a combobox. The container given is usecd to retreive
     * the target that is manipulated through this combobox. The propertyname
     * must equal the name of the NSUML event thrown when the property is set.
     * @param container
     * @param propertySetName
     * @param roleAddedName
     * @throws IllegalArgumentException if one of the arguments is null
     */
    public UMLComboBoxModel2(UMLUserInterfaceContainer container, String propertySetName) {
        super();
        if (propertySetName == null || container == null) throw new IllegalArgumentException("In UMLComboBoxModel2: one of the arguments is null");
        setPropertySetName(propertySetName);
        // it would be better that we don't need the container to get the target
        // this constructor can be without parameters as soon as we improve
        // targetChanged
        setContainer(container);
        buildModelList();
    }
    
    

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
        System.out.println("listRoleItemSet");
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        if (e.getName().equals(getPropertySetName()) && e.getOldValue() != e.getNewValue()) {
            selectedIndex = getIndexOf(e.getNewValue());
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(MElementEvent)
     */
    public void recovered(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(MElementEvent)
     */
    public void removed(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (isValid((MModelElement)e.getAddedValue())) {
            addElement(e.getAddedValue());
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        
    }

    /**
     * Returns the container.
     * @return UMLUserInterfaceContainer
     */
    public UMLUserInterfaceContainer getContainer() {
        return container;
    }

    /**
     * Sets the container.
     * @param container The container to set
     */
    public void setContainer(UMLUserInterfaceContainer container) {
        this.container = container;
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        // targetchanged should actually propagate an event with the source of
        // the change (the actual old and new target)
        // this must be implemented in the whole of argo one time or another
        // to improve performance and reduce errors
        Object target = getContainer().getTarget();
        buildModelList();
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
        // in the current implementation of argouml, history is not implemented
        // this event is for future releases
    }
    
    /**
     * Returns true if the given modelelement m may be added to the model.
     * @param m
     * @return boolean
     */
    protected abstract boolean isValid(MModelElement m);
    
    /**
     * Builds the list of elements and sets the selectedIndex to the currently 
     * selected item if there is one. Called from targetChanged every time the 
     * target of the proppanel is changed.
     */
    protected abstract void buildModelList();
    
    /**
     * @see javax.swing.MutableComboBoxModel#addElement(Object)
     */
    public void addElement(Object arg0) {
        list.add(arg0);
        int size = list.size();
        fireIntervalAdded(this, size-1, size);
    }

    /**
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object getElementAt(int arg0) {
        return list.get(arg0);
    }

    /**
     * @see javax.swing.DefaultComboBoxModel#getIndexOf(Object)
     */
    public int getIndexOf(Object arg0) {
        return list.indexOf(arg0);
    }

    /**
     * @see javax.swing.ComboBoxModel#getSelectedItem()
     */
    public Object getSelectedItem() {
        if (selectedIndex >= 0 && selectedIndex < getSize()) {
            return list.get(selectedIndex);
        }
        return null;
    }

    /**
     * @see javax.swing.ListModel#getSize()
     */
    public int getSize() {
        return list.size();
    }

    /**
     * @see javax.swing.MutableComboBoxModel#insertElementAt(Object, int)
     */
    public void insertElementAt(Object arg0, int arg1) {
        if (arg1 >= 0 && arg1 <= list.size()) {
            list.add(arg1, arg0);
            fireIntervalAdded(this, arg1, arg1);
        }
    }

    /**
     * @see javax.swing.DefaultComboBoxModel#removeAllElements()
     */
    public void removeAllElements() {
        int size = list.size();
        if (size > 0) {
            list.removeAll(list);
            fireIntervalRemoved(this, 0, size-1);
        }
    }

    /**
     * @see javax.swing.MutableComboBoxModel#removeElement(Object)
     */
    public void removeElement(Object arg0) {
        int index = list.indexOf(arg0);
        if (index >= 0) {
            list.remove(arg0);
            fireIntervalRemoved(this, index, index);
        }
    }

    /**
     * @see javax.swing.MutableComboBoxModel#removeElementAt(int)
     */
    public void removeElementAt(int arg0) {
        if (arg0 >= 0 && arg0 < list.size()) {
            list.remove(arg0);
            fireIntervalRemoved(this, arg0, arg0);
        }
    }

    /**
     * @see javax.swing.ComboBoxModel#setSelectedItem(Object)
     */
    public void setSelectedItem(Object arg0) {
        selectedIndex = list.indexOf(arg0);
    }
    
    /**
     * Utility method to change all elements in the list with modelelements
     * at once.
     * @param elements
     */
    public void setElements(Collection elements) {
        if (elements != null) {
            int size = list.size() > 0 ? list.size()-1 : 0;
            list.clear();
            fireIntervalRemoved(this, 0, size);
            list.addAll(elements);
            fireIntervalAdded(this, 0, elements.size());
        } else
            throw new IllegalArgumentException("In setElements: may not set " +
                "elements to null collection");
    }
    /**
     * Returns the propertySetName.
     * @return String
     */
    public String getPropertySetName() {
        return propertySetName;
    }

    /**
     * Sets the propertySetName.
     * @param propertySetName The propertySetName to set
     */
    public void setPropertySetName(String propertySetName) {
        this.propertySetName = propertySetName;
    }

}
