// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

/**
 * This class is used to dispatch a UML model change event (which may
 * occur on a non-UI) thread) to user interface components.  The class
 * is created in response to a UML Model change event being captured by a
 * UMLUserInterfaceContainer and then is passed as an argument to
 * InvokeLater to be run on the user interface thread.<p>
 *
 * This class is updated to cope with changes to the targetchanged
 * mechanism.
 */
public class UMLChangeDispatch implements Runnable, UMLUserInterfaceComponent {
    private int eventType;
    private Container container;

    /**
     * The target of the proppanel that constructs this umlchangedispatch.
     */
    private Object target;

    /**
     * Dispatch a target changed event
     */
    public static final int TARGET_CHANGED_ADD = -1;


    /**
     * Dispatch a target changed event.
     */
    public static final int TARGET_CHANGED = 0;
    /**
     * Dispatch a target reasserted event.
     */
    public static final int TARGET_REASSERTED = 7;

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

    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetChanged()
     */
    public void targetChanged() {
        eventType = 0;
    }
    
    /*
     * @see org.argouml.uml.ui.UMLUserInterfaceComponent#targetReasserted()
     */
    public void targetReasserted() {
        eventType = 7;
    }

    /**
     * Called by InvokeLater on user interface thread.  Dispatches
     * event to all contained objects implementing
     * UMLUserInterfaceComponent.  If event == TARGET_CHANGED_ADD, adds change
     * listener to new target on completion of dispatch.
     */
    public void run() {
        if (target != null) {
            synchronizedDispatch(container);
        } else {
	    dispatch(container);
        }
    }

    /**
     * Iterates through all children of this container.  If a child
     * is another container then calls dispatch recursively, if
     * a child supports UMLUserInterfaceComponent then calls the
     * appropriate method.
     *
     * @param theAWTContainer AWT container
     */
    private void dispatch(Container theAWTContainer) {

        int count = theAWTContainer.getComponentCount();
        Component component;
        for (int i = 0; i < count; i++) {
            component = theAWTContainer.getComponent(i);
            if (component instanceof Container) {
                dispatch((Container) component);
            }
            if (component instanceof UMLUserInterfaceComponent
                    && component.isVisible()) {

                switch(eventType) {
                case TARGET_CHANGED_ADD:
                case TARGET_CHANGED:
                    ((UMLUserInterfaceComponent) component).targetChanged();
                    break;

                case TARGET_REASSERTED:
                    ((UMLUserInterfaceComponent) component).targetReasserted();
                    break;
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
