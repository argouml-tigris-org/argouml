// $Id$
// Copyright (c) 2002-2004 The Regents of the University of California. All
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

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.tigris.gef.presentation.Fig;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * The model for a list that contains Mbases. The state of the MBase is still 
 * kept in the Mbase itself. This list is only to be used as the model for some 
 * GUI element like UMLLinkedList 
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLModelElementListModel2
    extends DefaultListModel
    implements TargetListener, MElementListener {

    private String eventName = null;
    private Object listTarget = null;
    
    /**
     * Flag to indicate that the list is ordered, 
     * and supports reordering functionality.
     */
    private boolean ordered = false;

    /**
     * Flag to indicate wether list events should be fired
     */
    private boolean fireListEvents = true;

    /**
     * Flag to indicate wether the model is being build
     */
    private boolean buildingModel = false;


    /**
     * Constructor to be used if the subclass does not depend on the 
     * MELementListener methods and setTarget method implemented in this
     * class.
     */
    public UMLModelElementListModel2() {
        super();
    }
    
    /**
     * Constructor for UMLModelElementListModel2.
     *
     * @param name the event name
     */
    public UMLModelElementListModel2(String name) {
        super();
        setEventName(name);
    }

    /**
     * Constructor for UMLModelElementListModel2.
     *
     * @param name the event name
     * @param isOrdered true if the list is an ordered list 
     *                  (and hence supports reordering functions)
     */
    public UMLModelElementListModel2(String name, boolean isOrdered) {
        super();
        setEventName(name);
        ordered = isOrdered;
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

    /**
     * @see ru.novosoft.uml.MElementListener#listRoleItemSet(
     *          ru.novosoft.uml.MElementEvent)
     */
    public void listRoleItemSet(MElementEvent e) { }

    /**
     * @see ru.novosoft.uml.MElementListener#propertySet(
     *          ru.novosoft.uml.MElementEvent)
     */
    public void propertySet(MElementEvent e) {
        if (isValidEvent(e)) {
            removeAllElements();
            buildingModel = true;
            buildModelList();
            buildingModel = false;
            if (getSize() > 0) {
                fireIntervalAdded(this, 0, getSize() - 1);
            }
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#recovered(
     *          ru.novosoft.uml.MElementEvent)
     */
    public void recovered(MElementEvent e) { }

    /**
     * @see ru.novosoft.uml.MElementListener#removed(
     *          ru.novosoft.uml.MElementEvent)
     */
    public void removed(MElementEvent e) { }

    /**
     * @see ru.novosoft.uml.MElementListener#roleAdded(
     *          ru.novosoft.uml.MElementEvent)
     */
    public void roleAdded(MElementEvent e) {
        if (isValidEvent(e)) {
            Object o = getChangedElement(e);
            if (o instanceof Collection) {
                Iterator it = ((Collection) o).iterator();
                while (it.hasNext()) {
                    Object o2 = it.next();
                    addElement(it.next());
                }
            } else {
                addElement(o);
            }
        }
    }

    /**
     * @see ru.novosoft.uml.MElementListener#roleRemoved(
     *          ru.novosoft.uml.MElementEvent)
     */
    public void roleRemoved(MElementEvent e) {
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
        Iterator it = col.iterator();
        fireListEvents = false;
        int oldSize = getSize();
        while (it.hasNext()) {
            Object o = it.next();
            addElement(o);
        }
        fireListEvents = true;
        fireIntervalAdded(this, oldSize - 1, getSize() - 1);
    }

    /**
     * Utility method to get the target. Sets the _target if the _target is null
     * via the method setTarget().
     * @return MModelElement
     */
    protected Object getTarget() {
        return listTarget;
    }

    /**
     * Utility method to get the changed element from some event e
     * @param e the event
     * @return Object the changed element
     */
    protected Object getChangedElement(MElementEvent e) {
        if (e.getAddedValue() != null)
            return e.getAddedValue();
        if (e.getRemovedValue() != null)
            return e.getRemovedValue();
        if (e.getNewValue() != null)
            return e.getNewValue();
        return null;
    }

    /**
     * @see javax.swing.DefaultListModel#contains(java.lang.Object)
     */
    public boolean contains(Object elem) {
        if (super.contains(elem))
            return true;
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
     * Sets the target. If the old target is instanceof MBase, it also removes
     * the model from the element listener list of the target. If the new target
     * is instanceof MBase, the model is added as element listener to the new 
     * target.
     * @param theNewTarget the new target
     */
    public void setTarget(Object theNewTarget) {
        theNewTarget = theNewTarget instanceof Fig 
            ? ((Fig) theNewTarget).getOwner() : theNewTarget;
        if (ModelFacade.isABase(theNewTarget) 
                || ModelFacade.isADiagram(theNewTarget)) {
            if (ModelFacade.isABase(listTarget)) {
                UmlModelEventPump.getPump()
		    .removeModelEventListener(this,
					      /*(MBase)*/listTarget,
					      eventName);
            }

            if (ModelFacade.isABase(theNewTarget)) {
                listTarget = theNewTarget;
                // UmlModelEventPump.getPump()
                // .removeModelEventListener(this, (MBase)_target,
                // _eventName);
                UmlModelEventPump.getPump()
		    .addModelEventListener(this,
					   /*(MBase)*/listTarget,
					   eventName);

                removeAllElements();
                buildingModel = true;
                buildModelList();
                buildingModel = false;
                if (getSize() > 0) {
                    fireIntervalAdded(this, 0, getSize() - 1);
                }
            } else {
                listTarget = null;
                removeAllElements();
            }

        }
    }

    /**
     * Returns true if the given element is valid, i.e. it may be added to the 
     * list of elements.
     *
     * @param element the element to be tested
     * @return true if valid
     */
    protected abstract boolean isValidElement(Object/*MBase*/ element);

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
    protected boolean isValidEvent(MElementEvent e) {
        boolean valid = false;
        if (!(getChangedElement(e) instanceof Collection)) {
            valid = isValidElement(/*(MBase)*/getChangedElement(e));
            if (!valid && e.getNewValue() == null && e.getOldValue() != null) {
                valid = true; // we tried to remove a value
            }
        } else {
            Collection col = (Collection) getChangedElement(e);
            Iterator it = col.iterator();
            if (!col.isEmpty()) {
                valid = true;
                while (it.hasNext()) {
                    Object o = it.next();
                    if (!isValidElement(/*(MBase)*/o)) {
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

    /**
     * @see TargetListener#targetAdded(TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /**
     * @see TargetListener#targetRemoved(TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /**
     * @see TargetListener#targetSet(TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        setTarget(e.getNewTarget());
    }

    /**
     * @see javax.swing.AbstractListModel#fireContentsChanged(
     *          Object, int, int)
     */
    protected void fireContentsChanged(Object source, int index0, int index1) {
        if (fireListEvents && !buildingModel)
            super.fireContentsChanged(source, index0, index1);
    }

    /**
     * @see javax.swing.AbstractListModel#fireIntervalAdded(
     *          Object, int, int)
     */
    protected void fireIntervalAdded(Object source, int index0, int index1) {
        if (fireListEvents && !buildingModel)
            super.fireIntervalAdded(source, index0, index1);
    }

    /**
     * @see javax.swing.AbstractListModel#fireIntervalRemoved(
     *          Object, int, int)
     */
    protected void fireIntervalRemoved(Object source, int index0, int index1) {
        if (fireListEvents && !buildingModel)
            super.fireIntervalRemoved(source, index0, index1);
    }

    /**
     * Override this if you want a popup menu.
     * See for an example UMLClassifierParameterListModel.
     * 
     * @param popup the popup menu
     * @param index the selected item in the list at the moment 
     *              the mouse was clicked
     * @return true if a popup menu is created, and needs to be shown
     */
    public boolean buildPopup(JPopupMenu popup, int index) {
        if (!ordered) return false;
        
        JMenuItem moveUp = new JMenuItem(
                new MoveUpAction(this, index));
        JMenuItem moveDown = new JMenuItem(
                new MoveDownAction(this, index));
        popup.add(moveUp);
        popup.add(moveDown);
        return true;
    }
}

/**
 * TODO: Once finished, this class should be extracted in a seperate file, 
 * for use in other places.
 * 
 * @author Michiel
 */
class MoveUpAction extends UMLAction {
    private UMLModelElementListModel2 model;
    private int index;
    
    /**
     * The constructor.
     * 
     * @param name the (to be localized) description of the action
     */
    public MoveUpAction(UMLModelElementListModel2 theModel, 
            int theIndex) {
        super("menu.popup.moveup", false, false);
        model = theModel;
        index = theIndex;
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // TODO: Auto-generated method stub
        super.actionPerformed(e);
    }
    /**
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        return index > 0;
    }
}

/**
 * TODO: Once finished, this class should be extracted in a seperate file, 
 * for use in other places.
 * 
 * @author Michiel
 */
class MoveDownAction extends UMLAction {
    private UMLModelElementListModel2 model;
    private int index;
    
    /**
     * The constructor.
     * 
     * @param name the (to be localized) description of the action
     */
    public MoveDownAction(UMLModelElementListModel2 theModel, 
            int theIndex) {
        super("menu.popup.movedown", false, false);
        model = theModel;
        index = theIndex;
    }
    
    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        // TODO: Auto-generated method stub
        super.actionPerformed(e);
    }
    /**
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        return model.getSize() > index + 1;
    }
}
