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

// $header$
package org.argouml.uml.ui;

import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultListModel;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;

/**
 * The model for a list that Mbases contains. The state of the MBase is still 
 * kept in the Mbase itself. This list is only to be used as the model for some 
 * GUI element like UMLLinkedList 
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLModelElementListModel2 extends DefaultListModel implements UMLUserInterfaceComponent{

    private UMLUserInterfaceContainer _container = null;
    private Object _target = null;
    
    /**
     * Constructor for UMLModelElementListModel2.
     */
    public UMLModelElementListModel2(UMLUserInterfaceContainer container) {
        super();
        setContainer(container);
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        // we must build a new list here
        // we delegate that to the abstract method buildModelList to give
        // the user of this library class some influence
        
        setTarget(getContainer().getTarget());
        removeAllElements();
        buildModelList();
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
        setTarget(getContainer().getTarget());
        removeAllElements();
        buildModelList();
    }

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        if (isValidPropertySet(e)) {
            removeAllElements();
            buildModelList();
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) {
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (isValidRoleAdded(e)) {
            Object o = getChangedElement(e);
            if (o instanceof Collection) {
                Iterator it = ((Collection)o).iterator();
                while(it.hasNext()) {
                    Object o2 = it.next();
                    if (!contains(o2)) {
                        addElement(it.next());
                    }
                }
            } else {
                addElement(o);
            }
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
        if (isValidRoleRemoved(e)) {
            Object o = getChangedElement(e);
            if (o instanceof Collection) {
                Iterator it = ((Collection)o).iterator();
                while(it.hasNext()) {
                    removeElement(it.next());
                }
            } else {
                removeElement(o);
            }
        }
    }

    /**
     * Returns the container.
     * @return UMLUserInterfaceContainer
     */
    protected UMLUserInterfaceContainer getContainer() {
        return _container;
    }

    /**
     * Sets the container.
     * @param container The container to set
     */
    protected void setContainer(UMLUserInterfaceContainer container) {
        _container = container;
    }
    
    /**
     * Builds the list of elements. Called from targetChanged every time the 
     * target of the proppanel is changed. 
     */
    protected abstract void buildModelList();
    
    /**
     * Utility method to set the elements of this list to the contents of the
     * given collection.
     * @param col
     */
    protected void setAllElements(Collection col) {
        if (!isEmpty())
            removeAllElements();
        addAll(col);
    }
    
    /**
     * Utility method to add the contents of the given collection to the 
     * element list.
     * @param col
     */
    private void addAll(Collection col) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
            addElement(it.next());
        }
    }
    
    /**
     * Utility method to get the target. Sets the _target if the _target is null
     * via the method setTarget().
     * @return MModelElement
     */
    protected Object getTarget() {
        if (_target == null) {
            setTarget(getContainer().getTarget());
        }
        return _target;
    }
    
    /**
     * Utility method to get the changed element from some event e
     * @param e
     * @return Object
     */
    protected Object getChangedElement(MElementEvent e) {
        if (e.getAddedValue() != null) return e.getAddedValue();
        if (e.getRemovedValue() != null) return e.getRemovedValue();
        if (e.getNewValue() != null) return e.getNewValue();
        return null;
    }
    
    /**
     * Returns true if roleAdded(MElementEvent e) should be executed. Developers
     * should override this method and not directly override roleAdded.  
     * @param m
     * @return boolean
     */
    protected abstract boolean isValidRoleAdded(MElementEvent e);
    
    
    /**
     * Returns true if roleRemoved(MElementEvent e) should be executed. Standard
     * behaviour is that it is allways valid to remove an element that's in the
     * list.
     * @param m
     * @return boolean
     */
    protected boolean isValidRoleRemoved(MElementEvent e) {
        return indexOf(getChangedElement(e)) >= 0;
    }
    
    /**
     * Returns true if propertySet(MElementEvent e) should be executed. Developers
     * should override this method and not directly override propertySet in order
     * to let this listmodel and the component(s) representing this model 
     * function properly.  
     * @param m
     * @return boolean
     */
    protected boolean isValidPropertySet(MElementEvent e) {
        Object o = getChangedElement(e);
        if (contains(o)) return true;
        o = e.getSource();
        if (contains(o)) return true;
        return false;
    } 
            


    /**
     * @see javax.swing.DefaultListModel#contains(java.lang.Object)
     */
    public boolean contains(Object elem) {
        if (super.contains(elem)) return true;
        if (elem instanceof Collection) {
            Iterator it = ((Collection)elem).iterator();
            while(it.hasNext()) {
                if (super.contains(it.next())) return true;
            }
        }
        return false;
    }
    
    /**
     * Sets the target. If the old target is instanceof MBase, it also removes
     * the model from the element listener list of the target. If the new target
     * is instanceof MBase, the model is added as element listener to the new 
     * target.
     * @param target
     */
    protected void setTarget(Object target) {
        if (_target instanceof MBase) {
            ((MBase)_target).removeMElementListener(this);
        }
        _target = target;
        if (target instanceof MBase) {
            ((MBase)_target).addMElementListener(this);
        }
    }
    

}
