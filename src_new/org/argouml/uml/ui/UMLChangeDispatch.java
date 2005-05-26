// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

import java.awt.Component;
import java.awt.Container;

import org.argouml.model.Model;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelComponentInstance;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelNodeInstance;
import org.argouml.uml.ui.behavior.common_behavior.PropPanelObject;

import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/**
 * This class is used to dispatch a NSUML change event (which may
 * occur on a non-UI) thread) to user interface components.  The class
 * is created in response to a NSUML change event being captures by a
 * UMLUserInterfaceContainer and then is passed as an argument to
 * InvokeLater to be run on the user interface thread.<p>
 *
 * This class is updated to cope with changes to the targetchanged
 * mechanism.
 */
public class UMLChangeDispatch
        implements Runnable, UMLUserInterfaceComponent, MElementListener {
    private MElementEvent event;
    private int eventType;
    private Container container;

    /**
     * The target of the proppanel that constructs this umlchangedispatch.
     */
    private Object target;

    /**
     * Dispatch a target changed event <em>and</em> add a NSUML listener to
     * the target afterwards.
     */
    public static final int TARGET_CHANGED_ADD = -1;


    /**
     * Dispatch a target changed event.
     */
    public static final int TARGET_CHANGED = 0;


    /**
     * Dispatch a NSUML property set event.
     */
    public static final int PROPERTY_SET = 1;


    /**
     * Dispatch a NSUML list role item set event.
     */
    public static final int LIST_ROLE_ITEM_SET = 2;


    /**
     * Dispatch a NSUML recovered event.
     */

    public static final int RECOVERED = 3;


    /**
     * Dispatch a NSUML removed event.
     */
    public static final int REMOVED = 4;


    /**
     * Dispatch a NSUML role added event.
     */
    public static final int ROLE_ADDED = 5;


    /**
     * Dispatch a NSUML role removed event.
     */
    public static final int ROLE_REMOVED = 6;


    /**
     * Dispatch a target reasserted event.
     */
    public static final int TARGET_REASSERTED = 7;


    /**
     * Dispatch a default (target changed) event.
     */
    public static final int DEFAULT = TARGET_CHANGED;


    /**
     * Creates a UMLChangeDispatch.  eventType is overriden if a call to
     * one of the event functions is called.
     *
     * @param uic user interface container to which changes are dispatched.
     * @param et -1 will add event listener to new target, 0 for default.
     */
    public UMLChangeDispatch(Container uic, int et) {
        synchronized (uic) {
	    container = uic;
	    eventType = et;
	    if (uic instanceof PropPanel) {
		target = ((PropPanel) uic).getTarget();
	    }
	}
    }

    /**
     * Configures this instance to dispatch a targetChanged event.
     */
    public void targetChanged() {
        eventType = 0;
    }

    /**
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
        eventType = 7;
    }

    /**
     * Configures this instance to dispatch a propertySet event.
     * @param mee NSUML event
     */
    public void propertySet(MElementEvent mee) {
        event = mee;
        eventType = 1;
    }

    /**
     * Configures this instance to dispatch a listRoleItemSet event.
     * @param mee NSUML event
     */
    public void listRoleItemSet(MElementEvent mee) {
        event = mee;
        eventType = 2;
    }

    /**
     * Configures this instance to dispatch a recovered event.
     * @param mee NSUML event.
     */
    public void recovered(MElementEvent mee) {
        event = mee;
        eventType = 3;
    }

    /**
     * Configures this instance to dispatch a removed event.
     * @param mee NSUML event.
     */
    public void removed(MElementEvent mee) {
        event = mee;
        eventType = 4;
    }

    /**
     * Configures this instance to dispatch a roleAdded event.
     * @param mee NSUML event.
     */
    public void roleAdded(MElementEvent mee) {
        event = mee;
        eventType = 5;
    }

    /**
     * Configures this instance to dispatch a roleRemoved event.
     * @param mee NSUML event
     */
    public void roleRemoved(MElementEvent mee) {
        event = mee;
        eventType = 6;
    }


    /**
     * Called by InvokeLater on user interface thread.  Dispatches
     * event to all contained objects implementing
     * UMLUserInterfaceComponent.  If event == -1, adds change listener to
     * new target on completion of dispatch.
     */
    public void run() {
        if (target != null) {
            synchronizedDispatch(container);
        } else
	    dispatch(container);
        //
        //   now that we have finished all the UI updating
        //
        //   if we were doing an object change then
        //      add a listener to our new target
        //
        if (eventType == -1 && container instanceof PropPanel
                && !((container instanceof PropPanelObject)
                 || (container instanceof PropPanelNodeInstance)
                 || (container instanceof PropPanelComponentInstance))) {
            PropPanel propPanel = (PropPanel) container;
            Object t = propPanel.getTarget();

            if (Model.getFacade().isABase(t)) {

            	// 2002-07-15
            	// Jaap Branderhorst
            	// added next statement to prevent PropPanel getting
            	// added again and again to the target's listeners
                UmlModelEventPump.getPump().addModelEventListener(propPanel.getComponentDispatcher(), t);
            }
        }
    }

    /**
     * Iterates through all children of this container.  If a child
     * is another container then calls dispatch iteratively, if
     * a child supports UMLUserInterfaceComponent then calls the
     * appropriate method.
     *
     * @param theAWTContainer AWT container
     */
    private void dispatch(Container theAWTContainer) {

        int count = theAWTContainer.getComponentCount();
        Component component;
        UMLUserInterfaceComponent uiComp;
        for (int i = 0; i < count; i++) {
            component = theAWTContainer.getComponent(i);
            if (component instanceof Container)
                dispatch((Container) component);
            if (component instanceof Component
                    && ((Component) component).isVisible()) {
                
                if (component instanceof UMLUserInterfaceComponent) {
                
                    switch(eventType) {
                        case -1:
                        case 0:
                            ((UMLUserInterfaceComponent) component).targetChanged();
                            break;
    
                        case 7:
                            ((UMLUserInterfaceComponent) component).targetReasserted();
                            break;
                    }
                }
                
                if (component instanceof MElementListener) {
                
                    switch(eventType) {
                        case 1:
                            ((MElementListener) component).propertySet(event);
                            break;
    
                        case 2:
                            ((MElementListener) component).listRoleItemSet(event);
                            break;
    
                        case 3:
                            ((MElementListener) component).recovered(event);
                            break;
    
                        case 4:
                            ((MElementListener) component).removed(event);
                            break;
    
                        case 5:
                            ((MElementListener) component).roleAdded(event);
                            break;
    
                        case 6:
                            ((MElementListener) component).roleRemoved(event);
                            break;
                    }
                }
            }
        }
    }

    private void synchronizedDispatch(Container cont) {
        if (target == null) {
	    throw new IllegalStateException("Target may not be null in "
					    + "synchronized dispatch");
	}
        synchronized (target) {
            dispatch(cont);
        }
    }
}
