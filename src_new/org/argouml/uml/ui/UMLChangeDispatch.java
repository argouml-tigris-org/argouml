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


// File: UMLChangeDispatch.java
// Classes: UMLChangeDispatch
// Original Author:
// $Id$

// 23 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Added named constants

// for the various event types.

// 15 Juli 2002: Jaap Branderhorst (jaap.branderhorst@xs4all.nl). 
//  Removed double registration of MEventListeners (see run for extra comment) Hopefully solves issue 827
// for the various event types.




package org.argouml.uml.ui;

import org.argouml.uml.ui.behavior.common_behavior.*;

import javax.swing.event.*;
import java.awt.*;

import ru.novosoft.uml.*;

/**
 *  This class is used to dispatch a NSUML change event (which may occur on a non-UI)
 *  thread) to user interface components.  The class is created in response to a 
 *  NSUML change event being captures by a UMLUserInterfaceContainer and then
 *  is passed as an argument to InvokeLater to be run on the user interface thread.
 */
public class UMLChangeDispatch implements Runnable, UMLUserInterfaceComponent {
    private MElementEvent _event;
    private int _eventType;
    private Container _container;

    /**
     * <p>Dispatch a target changed event <em>and</em> add a NSUML listener to
     *   the target afterwards.</p>
     */

    public static final int TARGET_CHANGED_ADD = -1;


    /**
     * <p>Dispatch a target changed event.</p>
     */

    public static final int TARGET_CHANGED = 0;


    /**
     * <p>Dispatch a NSUML property set event.</p>
     */

    public static final int PROPERTY_SET = 1;


    /**
     * <p>Dispatch a NSUML list role item set event.</p>
     */

    public static final int LIST_ROLE_ITEM_SET = 2;


    /**
     * <p>Dispatch a NSUML recovered event.</p>
     */

    public static final int RECOVERED = 3;


    /**
     * <p>Dispatch a NSUML removed event.</p>
     */

    public static final int REMOVED = 4;


    /**
     * <p>Dispatch a NSUML role added event.</p>
     */

    public static final int ROLE_ADDED = 5;


    /**
     * <p>Dispatch a NSUML role removed event.</p>
     */

    public static final int ROLE_REMOVED = 6;


    /**
     * <p>Dispatch a target reasserted event.</p>
     */

    public static final int TARGET_REASSERTED = 7;


    /**
     * <p>Dispatch a default (target changed) event.</p>
     */

    public static final int DEFAULT = TARGET_CHANGED;


    /**
     *  Creates a UMLChangeDispatch.  eventType is overriden if a call to 
     *  one of the event functions is called.
     *  @param container user interface container to which changes are dispatched.
     *  @param eventType -1 will add event listener to new target, 0 for default.
     *      
     */
    public UMLChangeDispatch(Container container,int eventType) {
        _container = container;
        _eventType = eventType;
    }
    
    /**
     *   configures this instance to dispatch a targetChanged event.
     */
    public void targetChanged()
    {
        _eventType = 0;
    }

    public void targetReasserted() {
        _eventType = 7;
    }
    
    /**
     *   configures this instance to dispatch a propertySet event.
     *   @param mee NSUML event
     */
    public void propertySet(MElementEvent mee) {
        _event = mee;
        _eventType = 1;
    }
           
    /**
     *   configures this instance to dispatch a listRoleItemSet event.
     *   @param mee NSUML event
     */
    public void listRoleItemSet(MElementEvent mee) {
        _event = mee;
        _eventType = 2;
    }

    /**
     *   configures this instance to dispatch a recovered event.
     *   @param mee NSUML event.
     */
    public void recovered(MElementEvent mee) {
        _event = mee;
        _eventType = 3;
    }
    
    /**  
     *    configures this instance to dispatch a removed event.
     *    @param mee NSUML event.
     */
    public void removed(MElementEvent mee) {
        _event = mee;
        _eventType = 4;
    }
	
    /**
     *    configures this instance to dispatch a roleAdded event.
     *    @param mee NSUML event.
     */
    public void roleAdded(MElementEvent mee) {
        _event = mee;
        _eventType = 5;
    }
	
    /**
     *    configures this instance to dispatch a roleRemoved event.
     *    @param mee NSUML event
     */
    public void roleRemoved(MElementEvent mee) {
        _event = mee;
        _eventType = 6;
    }
    
    
    /**
     *    Called by InvokeLater on user interface thread.  Dispatches
     *    event to all contained objects implementing
     *    UMLUserInterfaceComponent.  If event == -1, adds change listener to
     *    new target on completion of dispatch.
     */
    public void run() {
        dispatch(_container); 
        //
        //   now that we have finished all the UI updating
        //
        //   if we were doing an object change then
        //      add a listener to our new target
        //
        if(_eventType == -1 && _container instanceof PropPanel &&
           !((_container instanceof PropPanelObject) ||
            (_container instanceof PropPanelNodeInstance) ||
            (_container instanceof PropPanelComponentInstance))) {
           PropPanel propPanel = (PropPanel) _container;
            Object target = propPanel.getTarget();

            if(target instanceof MBase) {
            	
            	// 2002-07-15
            	// Jaap Branderhorst
            	// added next statement to prevent PropPanel getting added again and again to the target's listeners
      			propPanel.removeMElementListener((MBase) target);
                propPanel.addMElementListener((MBase) target);
            }
        }
    }
    
    /**
     *    Iterates through all children of this container.  If a child
     *    is another container then calls dispatch iteratively, if
     *    a child supports UMLUserInterfaceComponent then calls the
     *    appropriate method.
     *    @param container AWT container
     */
    private void dispatch(Container container) {
        int count = container.getComponentCount();
        Component component;
        UMLUserInterfaceComponent uiComp;
        for(int i = 0; i < count; i++) {
            component = container.getComponent(i);
            if(component instanceof Container)
                dispatch((Container) component);
            if(component instanceof UMLUserInterfaceComponent) {
                uiComp = (UMLUserInterfaceComponent) component;
                switch(_eventType) {
                    case -1:
                    case 0:
                        uiComp.targetChanged();
                        break;
                    
                    case 1:
                        uiComp.propertySet(_event);
                        break;
                    
                    case 2:
                        uiComp.listRoleItemSet(_event);
                        break;
                    
                    case 3:
                        uiComp.recovered(_event);
                        break;
                    
                    case 4:
                        uiComp.removed(_event);
                        break;
                        
                    case 5:
                        uiComp.roleAdded(_event);
                        break;
                        
                    case 6:
                        uiComp.roleRemoved(_event);
                        break;

                    case 7:
                        uiComp.targetReasserted();
                        break;
                }
            }       
        }
    }
}

