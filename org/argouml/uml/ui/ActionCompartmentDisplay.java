// Copyright (c) 1996-01 The Regents of the University of California. All
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

// File: ActionCompartmentDisplay.java
// Classes: ActionCompartmentDisplay
// Original Author: your email address here
// $Id$

// 8 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// compartments for extension points on use cases.


package org.argouml.uml.ui;

import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;
import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import java.awt.event.*;
import java.util.*;


/**
 * <p>A class to implement the actions involved in hiding and showing
 *   compartments on interfaces, classes and use cases.</p>
 *
 * <p>This implementation extended to handle compartments for extension points
 *   on use cases.</p>
 *
 * <p>The class declares a number of static instances, each with an
 *   actionPerformed method that performs the required action.</p>
 */

public class ActionCompartmentDisplay extends UMLAction {


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>A flag to indicate whether the action should show or hide the
     *   relevant compartment.</p>
     */

    protected boolean _display = false;


    /**
     * <p>A string indicating the action desired.</p>
     */

    protected String _compartment = "";


    ///////////////////////////////////////////////////////////////////////////
    //
    // Class variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Static instance to show the attribute compartment of a class.</p>
     */

    public static UMLAction ShowAttrCompartment =
        new ActionCompartmentDisplay(true, "Show Attribute Compartment");


    /**
     * <p>Static instance to hide the attribute compartment of a class.</p>
     */

    public static UMLAction HideAttrCompartment =
        new ActionCompartmentDisplay(false, "Hide Attribute Compartment");


    /**
     * <p>Static instance to show the operation compartment of a class.</p>
     */

    public static UMLAction ShowOperCompartment =
        new ActionCompartmentDisplay(true, "Show Operation Compartment");


    /**
     * <p>Static instance to hide the operation compartment of a class.</p>
     */

    public static UMLAction HideOperCompartment =
        new ActionCompartmentDisplay(false, "Hide Operation Compartment");


    /**
     * <p>Static instance to show the extension point compartment of a use
     *   case.</p>
     */

    public static UMLAction ShowExtPointCompartment =
        new ActionCompartmentDisplay(true,
                                     "Show Extension Point Compartment");


    /**
     * <p>Static instance to hide the extension point compartment of a use
     *   case.</p>
     */

    public static UMLAction HideExtPointCompartment =
        new ActionCompartmentDisplay(false,
                                     "Hide Extension Point Compartment");


    /**
     * <p>Static instance to show both compartments of a class.</p>
     */

    public static UMLAction ShowAllCompartments =
        new ActionCompartmentDisplay(true, "Show All Compartments");


    /**
     * <p>Static instance to hide both compartments of a class.</p>
     */

    public static UMLAction HideAllCompartments =
        new ActionCompartmentDisplay(false, "Hide All Compartments");


    ///////////////////////////////////////////////////////////////////////////
    //
    // constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Constructor for a new instance. Can only be called by this class or
     * its children, since used to create static instances only.</p>
     *
     * @param d  <code>true</code> if the compartment is to be shown,
     *           <code>false</code> if it is to be hidden.
     *
     * @param c  The text to be displayed for this action.
     */

    protected ActionCompartmentDisplay(boolean d, String c) {

        // Invoke the parent constructor

	super(c, NO_ICON);

        // Save copies of the parameters

	_display = d;
	_compartment = c;
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // main methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Action method invoked when an event triggers this action.</p>
     *
     * <p>The {@link #_compartment} instance variable defines the action to
     *   take, and the {@link #_display} instance variable whether it should
     *   set visibility or note.</p>
     *
     * <p><em>Note</em>. The {@link #_display} instance variable is really
     *   redundant. Its value is implied by the operation.</p>
     *
     * @param ae  The event that triggered us.
     */

    public void actionPerformed(ActionEvent ae) {

        // Only do anything if we have a single item selected (surely this
        // should work for multiple selections as well?).

	Vector sels = Globals.curEditor().getSelectionManager().selections();

	if( sels.size() == 1 ) {
	    Selection sel = (Selection) sels.firstElement();
	    Fig       f   = sel.getContent();

            // Perform the action

	    if (_compartment.equals("Show Attribute Compartment")) {
		((FigClass)f).setAttributeVisible(_display);
            }
	    else if (_compartment.equals("Hide Attribute Compartment")) {
		((FigClass)f).setAttributeVisible(_display);
            }
	    else if (_compartment.equals("Show Operation Compartment") ||
	             _compartment.equals("Hide Operation Compartment")) {
		if (f instanceof FigClass)
			((FigClass)f).setOperationVisible(_display);
		if (f instanceof FigInterface)
			((FigInterface)f).setOperationVisible(_display);
            }
	    else if (_compartment.equals("Show Extension Point Compartment")) {
		((FigUseCase)f).setExtensionPointVisible(_display);
            }
	    else if (_compartment.equals("Hide Extension Point Compartment")) {
		((FigUseCase)f).setExtensionPointVisible(_display);
            }
	    else if (_compartment.equals("Show All Compartments")) {
		((FigClass)f).setAttributeVisible(_display);
		((FigClass)f).setOperationVisible(_display);
	    }
	    else {
		((FigClass)f).setAttributeVisible(_display);
		((FigClass)f).setOperationVisible(_display);
	    }
	}
    }


    /**
     * <p>Indicate whether this action should be enabled.</p>
     *
     * <p>Always returns <code>true</code> in this implementation.</p>
     *
     * @return  <code>true</code> if the action should be enabled,
     *          <code>false</code> otherwise. Always returns <code>true</code>
     *          in this implementation.
     */

    public boolean shouldBeEnabled() {
	return true;
    }

} /* end class ActionCompartmentDisplay */



