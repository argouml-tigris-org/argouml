// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.ui;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.UMLAction;


/**
 * Collects ArgoUML's global actions, and takes care of the
 * "enabled" status of all these actions.<p>
 *
 * Which means: <br>
 * Any instance of UMLAction may (at construction time)
 * tell Actions by calling addAction()
 * about UMLActions of which the availability depends on the currently
 * selected Target or the state of ArgoUML. <p>
 *
 * From here on, Actions will check the availability
 * of all enlisted UMLActions after every Target change and
 * after every ArgoUML state change (i.e. every user action),
 * and downlight or enable the corresponding UI element,
 * e.g. menu item or toolbar item. <p>
 *
 * Once UMLActions are enlisted, they can never be removed!<p>
 *
 * This class is a Singleton.
 * 
 *  @deprecated since V0.20 by MVW, see issue 2325.
 *  The Actions registered here should listen themselves to the target changes.
 */
public class Actions implements TargetListener {

    private static final Logger LOG = Logger.getLogger(Actions.class);

    private static final Actions INSTANCE = new Actions();

    /**
     * @return the singleton
     */
    public static Actions getInstance() {
        return INSTANCE;
    }

    private Actions() {
        TargetManager.getInstance().addTargetListener(this);
    }

    /**
     * <code>allActions</code> is the list of global UMLActions in ArgoUML.
     * All these are UMLActions!
     */
    private static Vector allActions = new Vector(100);

    /**
     * Updates all global actions: check if enabled or not.
     * This function used to be deprecated for unclear reasons - see issue 2735.
     */
    public static void updateAllEnabled() {
	Enumeration actions = allActions.elements();
	while (actions.hasMoreElements()) {
	    UMLAction a = (UMLAction) actions.nextElement();
	    a.updateEnabled();
	}
    }

    /**
     * Updates all global actions as a consequence of the send TargetEvent.
     * @param e the target event, which is used to determine the new target
     */
    private static void updateAllEnabled(TargetEvent e) {
	Iterator actions = allActions.iterator();
	while (actions.hasNext()) {
	    UMLAction a = (UMLAction) actions.next();
	    a.updateEnabled(e.getNewTarget());
	}
    }

    /**
     * Add actions to the global actions list.
     * Only done at construction time for UMLActions.
     *
     * @param newAction the new action to be added
     */
    public static void addAction(UMLAction newAction) {
        LOG.debug("Adding action: " + newAction.getClass().getName());
        allActions.addElement(newAction);
    }

    /**
     * @param action the given action
     * @return true if this is a global action
     */
    public static boolean isGlobalAction(UMLAction action) {
        return allActions.contains(action);
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        updateAllEnabled(e);

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        updateAllEnabled(e);

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        updateAllEnabled(e);

    }

}  /* end class Actions */
