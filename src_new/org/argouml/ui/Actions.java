// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
 * Manages ArgoUML's list of global actions.
 * Takes care of the "enabled" status of all these actions. 
 *
 * TODO: (MVW) If the responsibility of this class is 
 * to manage the list of actions, 
 * how come we know about some global actions like "print"? 
 * Lets move these to the menu or project or something...
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
     * The action to print.
     */
    //public static UMLAction print = new ActionPrint();
    /**
     * The action to start page setup.
     */
    //public static UMLAction pageSetup = new ActionPageSetup();

    /**
     * The action to undo.
     */
    //public static UMLAction Undo = new ActionUndo();
    
    /**
     * The action to Redo.
     */
    //public static UMLAction Redo = new ActionRedo();

    //public static UMLAction NavBack = new ActionNavBack();
    //public static UMLAction NavForw = new ActionNavForw();
    //public static UMLAction NavFavs = new ActionNavFavs();
    //public static UMLAction NavConfig = new ActionNavConfig();

    /**
     * The action to Find.
     */
    //public static UMLAction find = new ActionFind();
    
    /**
     * The action to Goto a Diagram.
     */
    //public static UMLAction gotoDiagram = new ActionGotoDiagram();

    //public static UMLAction NextEditTab = new ActionNextEditTab();
    //public static UMLAction NextDetailsTab = new ActionNextDetailsTab();
    
    /**
     * The action to show or hide the RapidButtons aka toolbelt buttons
     * aka buttons on selection aka ...
     */
    //public static UMLAction showRapidButtons = new ActionShowRapidButtons();

    //public static UMLAction CreateMultiple = new ActionCreateMultiple();

    /**
     * The action to toggle AutoCritique.
     */
    //public static UMLAction autoCritique = new ActionAutoCritique();
    
    /**
     * The action to Open the Decisions dialog.
     */
    //public static UMLAction openDecisions = new ActionOpenDecisions();
    
    /**
     * The action to Open the Goals dialog.
     */
    //public static UMLAction openGoals = new ActionOpenGoals();
    
    /**
     * The action to browse the critics.
     */
    //public static UMLAction openCritics = new ActionOpenCritics();

    /**
     * The action to toggle the Flat setting for the Todo item tree.
     */
    //public static UMLAction flatToDo = new ActionFlatToDo();

    /**
     * The action to create a NewToDoItem.
     */
    //public static UMLAction newToDoItem = new ActionNewToDoItem();
    
    /**
     * The action to Resolve todo items.
     */
    //public static UMLAction resolve = new ActionResolve();
    
    /**
     * The action to send Email to an Expert.
     */
    //public static UMLAction emailExpert = new ActionEmailExpert();
    
    /**
     * The action that shows information about the selected critic. 
     * Invoked by pressing the Help button.
     * Never used. See {@link org.argouml.cognitive.ui.WizStep#doHelp()}.
     */
    //public static UMLAction MoreInfo = new ActionMoreInfo();
    
    /**
     * The action to snooze the critics.
     */
    //public static UMLAction snooze = new ActionSnooze();

    /**
     * The action to open the SystemInfo dialog box.
     */
    //public static UMLAction systemInfo = new ActionSystemInfo();
    
    /**
     * The action to open the About ArgoUML dialog box.
     */
    //public static UMLAction aboutArgoUML = new ActionAboutArgoUML();

    /**
     * @deprecated as of 0.15.3. Use updateAllEnabled(TargetEvent e) instead.
     *
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
     * @param e
     */
    private static void updateAllEnabled(TargetEvent e) {
	Iterator actions = allActions.iterator();
	while (actions.hasNext()) {
	    UMLAction a = (UMLAction) actions.next();
	    a.updateEnabled(e.getNewTarget());
	}
    }

    /**
     * @param newAction the new action to be added
     */
    public static void addAction(UMLAction newAction) {
        LOG.debug("Adding action: " + newAction.getClass().getName());
        allActions.addElement(newAction);
    }

    /**
     * @param action the given action
     * @return truee if this is a global action
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


class ActionUndo extends UMLAction {

    public ActionUndo() { super("action.undo"); }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() { return false; }
} /* end class ActionUndo */

class ActionRedo extends UMLAction {

    public ActionRedo() { super("action.redo"); }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() { return false; }
} /* end class ActionRedo */


////////////////////////////////////////////////////////////////
// items on view menu

class ActionCreateMultiple extends UMLAction {

    public ActionCreateMultiple() { super("action.create-multiple", NO_ICON); }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
	//Project p = ProjectBrowser.getInstance().getProject();
	//return super.shouldBeEnabled() && p != null;
	return false;
    }
} /* end class ActionCreateMultiple */

