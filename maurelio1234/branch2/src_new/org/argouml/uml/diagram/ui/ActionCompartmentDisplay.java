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

package org.argouml.uml.diagram.ui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.AttributesCompartmentContainer;
import org.argouml.uml.diagram.ExtensionsCompartmentContainer;
import org.argouml.uml.diagram.OperationsCompartmentContainer;
import org.argouml.uml.diagram.use_case.ui.FigUseCase;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;


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
public class ActionCompartmentDisplay extends UndoableAction {


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
     * Compartment type(s) field.
     * Bitfield of flags with a bit for each compartment type
     */
    private int cType;

    private static final int COMPARTMENT_ATTRIBUTE = 1;
    private static final int COMPARTMENT_OPERATION = 2;
    private static final int COMPARTMENT_EXTENSIONPOINT = 4;
    private static final int COMPARTMENT_ENUMLITERAL = 8;



    ///////////////////////////////////////////////////////////////////////////
    //
    // Class variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Static instance to show the attribute compartment of a class.
     */
    private static final UndoableAction SHOW_ATTR_COMPARTMENT =
        new ActionCompartmentDisplay(true,
                "action.show-attribute-compartment", COMPARTMENT_ATTRIBUTE);

    /**
     * Static instance to hide the attribute compartment of a class.
     */
    private static final UndoableAction HIDE_ATTR_COMPARTMENT =
        new ActionCompartmentDisplay(false,
                "action.hide-attribute-compartment", COMPARTMENT_ATTRIBUTE);

    /**
     * Static instance to show the operation compartment of a class.
     */
    private static final UndoableAction SHOW_OPER_COMPARTMENT =
        new ActionCompartmentDisplay(true,
                "action.show-operation-compartment", COMPARTMENT_OPERATION);

    /**
     * Static instance to hide the operation compartment of a class.
     */
    private static final UndoableAction HIDE_OPER_COMPARTMENT =
        new ActionCompartmentDisplay(false,
		"action.hide-operation-compartment", COMPARTMENT_OPERATION);

    /**
     * Static instance to show the extension point compartment of a use
     * case.
     */
    private static final UndoableAction SHOW_EXTPOINT_COMPARTMENT =
        new ActionCompartmentDisplay(true,
                "action.show-extension-point-compartment", 
                COMPARTMENT_EXTENSIONPOINT);

    /**
     * Static instance to hide the extension point compartment of a use
     * case.
     */
    private static final UndoableAction HIDE_EXTPOINT_COMPARTMENT =
        new ActionCompartmentDisplay(false,
                "action.hide-extension-point-compartment", 
                COMPARTMENT_EXTENSIONPOINT);

    /**
     * Static instance to show both compartments of a class or enumeration.
     */
    private static final UndoableAction SHOW_ALL_COMPARTMENTS =
        new ActionCompartmentDisplay(true, "action.show-all-compartments", 
                COMPARTMENT_ATTRIBUTE 
                | COMPARTMENT_OPERATION 
                | COMPARTMENT_ENUMLITERAL);

    /**
     * Static instance to hide both compartments of a class or enumeration.
     */
    private static final UndoableAction HIDE_ALL_COMPARTMENTS =
        new ActionCompartmentDisplay(false, "action.hide-all-compartments", 
                COMPARTMENT_ATTRIBUTE 
                | COMPARTMENT_OPERATION
                | COMPARTMENT_ENUMLITERAL);

    /**
     * Static instance to show the enumeration literals compartment of an
     * enumeration.
     */
    private static final UndoableAction SHOW_ENUMLITERAL_COMPARTMENT =
        new ActionCompartmentDisplay(true,
                "action.show-enumeration-literal-compartment", 
                COMPARTMENT_ENUMLITERAL);

    /**
     * Static instance to hide the enumeration literals compartment of an
     * enumeration.
     */
    private static final UndoableAction HIDE_ENUMLITERAL_COMPARTMENT =
        new ActionCompartmentDisplay(false,
                "action.hide-enumeration-literal-compartment", 
                COMPARTMENT_ENUMLITERAL);

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
	super(Translator.localize(c));
	display = d;
        cType = type;
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // main methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the compartment show and/or hide actions needed for the selected
     * Figs.
     * 
     * @return Only returns the actions for the menu-items that make sense for
     *         the current selection.
     */
    public static Collection getActions() {
        Collection actions = new ArrayList();
        Editor ce = Globals.curEditor();
        Vector figs = ce.getSelectionManager().getFigs();
        Iterator i = figs.iterator();
        
        int present = 0;
        int visible = 0;
        
        boolean operPresent = false;
        boolean operVisible = false;
        
        boolean attrPresent = false;
        boolean attrVisible = false;
        
        boolean epPresent = false;
        boolean epVisible = false;
        
        boolean enumPresent = false;
        boolean enumVisible = false;

        while (i.hasNext()) {
            Fig f = (Fig) i.next();
            
            if (f instanceof AttributesCompartmentContainer) {
                present++;
                attrPresent = true;
                attrVisible = 
                    ((AttributesCompartmentContainer) f).isAttributesVisible();
                if (attrVisible) {
                    visible++;
                }
            }
            if (f instanceof OperationsCompartmentContainer) {
                present++;
                operPresent = true;
                operVisible =
                    ((OperationsCompartmentContainer) f).isOperationsVisible();
                if (operVisible) {
                    visible++;
                }
            }
            if (f instanceof ExtensionsCompartmentContainer) {
                present++;
                epPresent = true;
                epVisible =
                        ((ExtensionsCompartmentContainer) f)
                                .isExtensionPointVisible();
                if (epVisible) {
                    visible++;
                }
            }
            if (f instanceof EnumLiteralsCompartmentContainer) {
                present++;
                enumPresent = true;
                enumVisible =
                        ((EnumLiteralsCompartmentContainer) f)
                                .isEnumLiteralsVisible();
                if (enumVisible) {
                    visible++;
                }
            }
        }

        // Set up hide all / show all
        if (present > 1) {
            if (visible > 0) {
                actions.add(HIDE_ALL_COMPARTMENTS);
            }
            if (present - visible > 0) {
                actions.add(SHOW_ALL_COMPARTMENTS);
            }
        }

        if (attrPresent) {
            if (attrVisible) {
                actions.add(HIDE_ATTR_COMPARTMENT);                
            } else {
                actions.add(SHOW_ATTR_COMPARTMENT);               
            }
        }

        if (enumPresent) {
            if (enumVisible) {
                actions.add(HIDE_ENUMLITERAL_COMPARTMENT);
            } else {
                actions.add(SHOW_ENUMLITERAL_COMPARTMENT);
            }
        }
        
        if (operPresent) {
            if (operVisible) {
                actions.add(HIDE_OPER_COMPARTMENT);                
            } else {
                actions.add(SHOW_OPER_COMPARTMENT);               
            }
        }


        if (epPresent) {
            if (epVisible) {
                actions.add(HIDE_EXTPOINT_COMPARTMENT);
            } else {
                actions.add(SHOW_EXTPOINT_COMPARTMENT);               
            }
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
            if ((cType & COMPARTMENT_ATTRIBUTE) != 0) {
		if (f instanceof AttributesCompartmentContainer)
		    ((AttributesCompartmentContainer) f)
                        .setAttributesVisible(display);
            }
            if ((cType & COMPARTMENT_OPERATION) != 0) {
		if (f instanceof OperationsCompartmentContainer)
		    ((OperationsCompartmentContainer) f)
                        .setOperationsVisible(display);
            }

            if ((cType & COMPARTMENT_EXTENSIONPOINT) != 0) {
                if (f instanceof FigUseCase) {
                    ((FigUseCase) f).setExtensionPointVisible(display);
                }
            }
            if ((cType & COMPARTMENT_ENUMLITERAL) != 0) {
                if (f instanceof EnumLiteralsCompartmentContainer) {
                    ((EnumLiteralsCompartmentContainer) f)
                            .setEnumLiteralsVisible(display);
                }
            }
	}
    }

} /* end class ActionCompartmentDisplay */



