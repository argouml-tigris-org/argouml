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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.uml.diagram.use_case.ui.FigUseCase;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;


/**
 * A class to implement the actions involved in hiding and showing
 * compartments on interfaces, classes and use cases.<p>
 *
 * This implementation may easily be extended for other
 * compartments of other figs.<p>
 *
 * The class declares a number of static instances, each with an
 * actionPerformed method that performs the required action.
 */
public class ActionCompartmentDisplay extends UMLAction {


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * A flag to indicate whether the action should show or hide the
     * relevant compartment.
     */
    private boolean display = false;

    /**
     * The compartment type:<p>
     * 0: attribute
     * 1: operation
     * 2: attribute and operation
     * 3: extensionpoint
     */
    private int cType;

    ///////////////////////////////////////////////////////////////////////////
    //
    // Class variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Static instance to show the attribute compartment of a class.
     */
    private static final UMLAction SHOW_ATTR_COMPARTMENT =
        new ActionCompartmentDisplay(true,
                "action.show-attribute-compartment", 0);

    /**
     * Static instance to hide the attribute compartment of a class.
     */
    private static final UMLAction HIDE_ATTR_COMPARTMENT =
        new ActionCompartmentDisplay(false,
                "action.hide-attribute-compartment", 0);

    /**
     * Static instance to show the operation compartment of a class.
     */
    private static final UMLAction SHOW_OPER_COMPARTMENT =
        new ActionCompartmentDisplay(true,
                "action.show-operation-compartment", 1);

    /**
     * Static instance to hide the operation compartment of a class.
     */
    private static final UMLAction HIDE_OPER_COMPARTMENT =
        new ActionCompartmentDisplay(false,
		"action.hide-operation-compartment", 1);

    /**
     * Static instance to show the extension point compartment of a use
     * case.
     */
    private static final UMLAction SHOW_EXTPOINT_COMPARTMENT =
        new ActionCompartmentDisplay(true,
                "action.show-extension-point-compartment", 3);

    /**
     * Static instance to hide the extension point compartment of a use
     *   case.
     */
    private static final UMLAction HIDE_EXTPOINT_COMPARTMENT =
        new ActionCompartmentDisplay(false,
                "action.hide-extension-point-compartment", 3);

    /**
     * Static instance to show both compartments of a class.
     */
    private static final UMLAction SHOW_ALL_COMPARTMENTS =
        new ActionCompartmentDisplay(true, "action.show-all-compartments", 2);

    /**
     * Static instance to hide both compartments of a class.
     */
    private static final UMLAction HIDE_ALL_COMPARTMENTS =
        new ActionCompartmentDisplay(false, "action.hide-all-compartments", 2);


    ///////////////////////////////////////////////////////////////////////////
    //
    // constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Constructor for a new instance. Can only be called by this class or
     * its children, since used to create static instances only.
     *
     * @param d    <code>true</code> if the compartment is to be shown,
     *             <code>false</code> if it is to be hidden
     *
     * @param c    the text to be displayed for this action
     * @param type the type of compartment. See definition at {@link #cType}
     */
    protected ActionCompartmentDisplay(boolean d, String c, int type) {

        // Invoke the parent constructor
	super(c, true, NO_ICON);

        // Save copies of the parameters
	display = d;
        cType = type;
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // main methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the compartment show and/or hide actions
     * needed for the selected Figs.
     * Only returns the actions for the menu-items that make sense
     * for the current selection.
     */
    public static Collection getActions() {
        Collection actions = new ArrayList();
        Editor ce = Globals.curEditor();
        Vector figs = ce.getSelectionManager().getFigs();
        Iterator i = figs.iterator();
        boolean aOn = false;
        boolean aOff = false;
        boolean oOn = false;
        boolean oOff = false;
        boolean eOn = false;
        boolean eOff = false;
        while (i.hasNext()) {
            Fig f = (Fig) i.next();
            if (f instanceof AttributesCompartmentContainer) {
                boolean v =
                    ((AttributesCompartmentContainer) f).isAttributesVisible();
                if (v) aOn = true;
                else aOff = true;
            }
            if (f instanceof OperationsCompartmentContainer) {
                boolean v =
                    ((OperationsCompartmentContainer) f).isOperationsVisible();
                if (v) oOn = true;
                else oOff = true;
            }
            if (f instanceof ExtensionsCompartmentContainer) {
                boolean v =
                    ((ExtensionsCompartmentContainer) f).isExtensionPointVisible();
                if (v) eOn = true;
                else eOff = true;
            }
        }

        if ((aOn || oOn) && (aOn || aOff)) {
            actions.add(HIDE_ALL_COMPARTMENTS);
        }
        if ((aOff || oOff) && (aOn || aOff)) {
            actions.add(SHOW_ALL_COMPARTMENTS);
        }
        if (aOn) {
            actions.add(HIDE_ATTR_COMPARTMENT);
        }
        if (aOff) {
            actions.add(SHOW_ATTR_COMPARTMENT);
        }
        if (oOn) {
            actions.add(HIDE_OPER_COMPARTMENT);
        }
        if (oOff) {
            actions.add(SHOW_OPER_COMPARTMENT);
        }
        if (eOn) {
            actions.add(HIDE_EXTPOINT_COMPARTMENT);
        }
        if (eOff) {
            actions.add(SHOW_EXTPOINT_COMPARTMENT);
        }

        return actions;
    }


    /**
     * Action method invoked when an event triggers this action.<p>
     *
     * The {@link #cType} instance variable defines the action to
     * take, and the {@link #display} instance variable whether it should
     * set visibility or not.<p>
     *
     * @param ae  The event that triggered us.
     */
    public void actionPerformed(ActionEvent ae) {
	Iterator i =
            Globals.curEditor().getSelectionManager().selections().iterator();
	while (i.hasNext()) {
	    Selection sel = (Selection) i.next();
	    Fig       f   = sel.getContent();

            // Perform the action
            if ((cType == 0) || (cType == 2)) {
		if (f instanceof AttributesCompartmentContainer)
		    ((AttributesCompartmentContainer) f)
                        .setAttributesVisible(display);
            }
            if ((cType == 1) || (cType == 2)) {
		if (f instanceof OperationsCompartmentContainer)
		    ((OperationsCompartmentContainer) f)
                        .setOperationsVisible(display);
            }
            if (cType == 3) {
		((FigUseCase) f).setExtensionPointVisible(display);
            }
	}
    }

} /* end class ActionCompartmentDisplay */



