// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
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

import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.UMLAction;

/**
 * A class to perform the action of changing the unidirectional or
 * bidirectional navigation of an association.
 *
 * @author  Bob Tarling
 */
public class ActionNavigability extends UMLAction {
    /**
     * Enumeration constant for <code>BIDIRECTIONAL</code> navigability.
     */
    public static final int BIDIRECTIONAL = 0;
    
    /**
     * Enumeration constant for <code>STARTTOEND</code> navigability.
     */
    public static final int STARTTOEND = 1;
    
    /**
     * Enumeration constant for <code>ENDTOSTART</code> navigability.
     */
    public static final int ENDTOSTART = 2;

    private int nav = BIDIRECTIONAL;
    private Object assocStart = null;
    private Object assocEnd = null;

    /**
     * The <code>ActionNavigability</code> constructor.
     *
     * @param assocStart a <code>MAssociationEnd</code> object at the start
     * of an association.
     * @param assocEnd a <code>MAssociationEnd</code> object at the end of
     * an association.
     * @param nav the type of navigation required in the association
     * being either <ul> <li>BIDIRECTIONAL <li>STARTTOEND
     * <li>ENDTOSTART </ul>
     *
     * @return the constructed class
     */
    public static ActionNavigability newActionNavigability(Object assocStart,
							   Object assocEnd,
							   int nav) {
        return new ActionNavigability(getDescription(assocStart, assocEnd, nav),
				      assocStart,
				      assocEnd,
				      nav);
    }

    /**
     * Build a description string from the given association ends, 
     * and the navigability.
     * 
     * @param assocStart association end 1
     * @param assocEnd   association end 2
     * @param nav        the navigability
     * @return           the string containing a human-readible indication 
     *                   of the navigability
     */
    private static String getDescription(Object assocStart,
					 Object assocEnd,
					 int nav) {
        String startName = ModelFacade.getName(ModelFacade.getType(assocStart));
        String endName = ModelFacade.getName(ModelFacade.getType(assocEnd));

        if (startName == null || startName.length() == 0) startName = "anon";
        if (endName == null || endName.length() == 0) endName = "anon";

        if (nav == STARTTOEND) {
            return startName + " to " + endName;
        }
        else if (nav == ENDTOSTART) {
            return endName + " to " + startName;
        }
        else {
            return "Bidirectional";
        }
    }

    /**
     * The constructor. 
     * 
     * @param label   the description as build in <code>getDescription</code>
     * @param theAssociationStart association end 1
     * @param theAssociationEnd   association end 2
     * @param theNavigability     the navigability: one of 
     *                            BIDIRECTIONAL, STARTTOEND, ENDTOSTART
     */
    protected ActionNavigability(String label,
				 Object theAssociationStart,
				 Object theAssociationEnd,
				 int theNavigability) {
        super(label, NO_ICON);

        this.nav = theNavigability;
        this.assocStart = theAssociationStart;
        this.assocEnd = theAssociationEnd;
    }

    /**
     * To perform the action of changing navigability.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	ModelFacade.setNavigable(assocStart, 
	        (nav == BIDIRECTIONAL || nav == ENDTOSTART));
        ModelFacade.setNavigable(assocEnd,   
                (nav == BIDIRECTIONAL || nav == STARTTOEND));
    }

    /**
     * The is action is always enabled.
     *
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
	return true;
    }
} /* end class ActionNavigability */