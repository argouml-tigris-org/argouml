// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

/*
 * ActionUtilities.java
 */
package org.argouml.swingext;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JPopupMenu;

/**
 * A collection of utility methods for Swing Actions.
 *
 * @author Eugenio Alvarez
 * @stereotype utility
 */
public class ActionUtilities {

    /**
     * Intended for use inside an <code>actionPerformed</code> method eg:
     * <pre>
     *     public void actionPerformed(ActionEvent ae) {
     *         Container appRoot = ActionUtilities.getActionRoot(ae);
     *     }
     * </pre>
     *
     * Returns the root object, usually a <code>JFrame, JDialog or
     * JApplet</code> that is the owner of the source event source
     * object (JMenu, JMenuItem, JPopupMenu etc).
     */
    public static Container getActionRoot(ActionEvent ae) {
	return ActionUtilities.getActionRoot(ae.getSource());
    } // getActionRoot()

    /**
     * Intended for use inside an <code>actionPerformed</code> method eg:
     * <pre>
     *     public void actionPerformed(ActionEvent e) {
     *         Container appRoot = ActionUtilities.getActionRoot(e.getSource());
     *     }
     * </pre>
     * @return the root object, usually a JFrame, JDialog or JApplet
     *	     that is the owner of the source event source object 
     *         (JMenu, JMenuItem, JPopupMenu etc).
     *         null if none is found.
     */
    public static Container getActionRoot(Object source) {
	Container container = null;
	if (source instanceof Component) {
	    Component component = (Component) source;
	    container = ActionUtilities.getContainer(component);
	    if (container == null) {
		if (source instanceof Container) {
		    return (Container) source;
		} // end if
		return null;
	    } // end if
	    while (ActionUtilities.getContainer(container) != null) {
		container = ActionUtilities.getContainer(container);
	    } // end while
	} // end if
	return container;
    } // end getActionRoot()

    /**
     * Helper method to find the <code>Container</code> of
     * <code>Component</code>.
     */
    private static Container getContainer(Component source) {
	Container container = source.getParent();
	if (container != null) {
	    return container;
	}
	if (source instanceof JPopupMenu) {
	    JPopupMenu jPopupMenu = (JPopupMenu) source;
	    Component component = jPopupMenu.getInvoker();
	    if (component instanceof Container) {
		container = (Container) component;
	    } // end if
	} // end if
	return container;
    } // end getContainer()

} // end class ActionUtilities


