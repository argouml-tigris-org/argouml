// $Id$
// Copyright (c) 2002 The Regents of the University of California. All
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

import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultListModel;

import org.argouml.model.uml.UmlModelEventPump;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * The model for a list that Mbases contains. The state of the MBase is still 
 * kept in the Mbase itself. This list is only to be used as the model for some 
 * GUI element like UMLLinkedList 
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLModelElementListModel2 extends DefaultListModel implements TargetChangedListener, MElementListener {

    private String _eventName = null; 
    protected Object _target = null;
    
    /**
     * Constructor for UMLModelElementListModel2.
     */
    public UMLModelElementListModel2(String eventName) {
        super();
        setEventName(eventName);
    }
    
    /**
     * Constructor to be used if the subclass does not depend on the 
     * MELementListener methods and setTarget method implemented in this
     * class
     * @param container
     */
    public UMLModelElementListModel2() {
        super();
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
        if (isValidEvent(e)) {
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
        if (isValidEvent(e)) {
            Object o = getChangedElement(e);
            if (o instanceof Collection) {
                Iterator it = ((Collection)o).iterator();
                while(it.hasNext()) {
                    Object o2 = it.next();
                    addElement(it.next());                    
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
        boolean valid = false;
        if (!(getChangedElement(e) instanceof Collection)) {
            valid = contains(getChangedElement(e));
        } else {
            Collection col = (Collection)getChangedElement(e);
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
    protected void addAll(Collection col) {
        Iterator it = col.iterator();
        while (it.hasNext()) {
            Object o = it.next();
            addElement(o);
        }
    }
    
    /**
     * Utility method to get the target. Sets the _target if the _target is null
     * via the method setTarget().
     * @return MModelElement
     */
    protected Object getTarget() {        
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
     * @see javax.swing.DefaultListModel#contains(java.lang.Object)
     */
    public boolean contains(Object elem) {
        if (super.contains(elem)) return true;
        if (elem instanceof Collection) {
            Iterator it = ((Collection)elem).iterator();
            while(it.hasNext()) {
                if (!super.contains(it.next())) {
                     return false;
                }
            }
            return true;
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
    public void setTarget(Object target) {
        if (_eventName == null || _eventName.equals("")) 
            throw new IllegalStateException("eventName not set!");
        if (_target instanceof MBase) {
            UmlModelEventPump.getPump().removeModelEventListener(this, (MBase)_target, _eventName);
        }
        _target = target;
        if (_target instanceof MBase) {
             // UmlModelEventPump.getPump().removeModelEventListener(this, (MBase)_target, _eventName);
             UmlModelEventPump.getPump().addModelEventListener(this, (MBase)_target, _eventName);
        }
        if (_target != null) {
            removeAllElements();
            buildModelList();
        }
    }
    
    /**
     * Returns true if the given element is valid, i.e. it may be added to the 
     * list of elements.
     *
     * @param element
     */
    protected abstract boolean isValidElement(MBase element);
    
    /**
     * Returns true if some event is valid. An event is valid if the element
     * changed in the event is valid. This is determined via a call to isValidElement.
     * This method can be overriden by subclasses if they cannot determine if
     * it is a valid event just by checking the changed element.
     * @param e
     * @return boolean
     */
    protected boolean isValidEvent(MElementEvent e) {
        boolean valid = false;
        if (!(getChangedElement(e) instanceof Collection)) {
            valid = isValidElement((MBase)getChangedElement(e));
            if (!valid && e.getNewValue() == null && e.getOldValue() != null) {
                valid = true; // we tried to remove a value
            }
        } else {
            Collection col = (Collection)getChangedElement(e);
            Iterator it = col.iterator();
            if (!col.isEmpty()) {
                valid = true;
                while (it.hasNext()) {
                    Object o = it.next();
                    if (!isValidElement((MBase)o)) {
                        valid = false;
                        break;
                    }
                }
            } else {
                if (e.getOldValue() instanceof Collection && !((Collection)e.getOldValue()).isEmpty()) {
                    valid = true;
                }
            }   
        }
        return valid;
    }
    

    /**
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
        return _eventName;
    }

    /**
     * Sets the eventName. The eventName is the name of the MElementEvent to
     * which the list should listen. The list is registred with UMLModelEventPump
     * and only gets events that have a name like eventName.
     * This method should be called in the constructor
     * of every subclass.
     * @param eventName The eventName to set
     */
    protected void setEventName(String eventName) {
        _eventName = eventName;
    }

    /**
     * @see org.argouml.uml.ui.TargetChangedListener#targetChanged(java.lang.Object)
     */
    public void targetChanged(Object newTarget) {
        setTarget(newTarget);
    }

    /**
     * @see org.argouml.uml.ui.TargetChangedListener#targetReasserted(java.lang.Object)
     */
    public void targetReasserted(Object newTarget) {
        setTarget(newTarget);
    }

}
