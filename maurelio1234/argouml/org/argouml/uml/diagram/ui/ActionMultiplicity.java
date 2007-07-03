// $Id: ActionMultiplicity.java 11516 2006-11-25 04:30:15Z tfmorris $
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
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Action;

import org.argouml.model.Model;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.undo.UndoableAction;


/**
 * Action to set the Multiplicity.
 *
 */
public class ActionMultiplicity extends UndoableAction {
    private String str = "";
    private Object mult = null;


    ////////////////////////////////////////////////////////////////
    // static variables

    // multiplicity
    private static UndoableAction srcMultOne = 
        new ActionMultiplicity("1", "src");

    private static UndoableAction destMultOne = 
        new ActionMultiplicity("1", "dest");

    private static UndoableAction srcMultZeroToOne = 
        new ActionMultiplicity("0..1", "src");

    private static UndoableAction destMultZeroToOne = 
        new ActionMultiplicity("0..1", "dest");

    private static UndoableAction srcMultZeroToMany = 
        new ActionMultiplicity("0..*", "src");

    private static UndoableAction destMultZeroToMany = 
        new ActionMultiplicity("0..*", "dest");

    private static UndoableAction srcMultOneToMany = 
        new ActionMultiplicity("1..*", "src");

    private static UndoableAction destMultOneToMany = 
        new ActionMultiplicity("1..*", "dest");


    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The Constructor.
     *
     * @param m the multiplicity
     * @param s "src" or "dest". Anything else is interpreted as "dest".
     */
    protected ActionMultiplicity(String m, String s) {
        super(m, null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, m);
	str = s;
	mult = m;
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
    	Vector sels = Globals.curEditor().getSelectionManager().selections();
	if (sels.size() == 1) {
	    Selection sel = (Selection) sels.firstElement();
	    Fig f = sel.getContent();
	    Object owner = ((FigEdgeModelElement) f).getOwner();
	    Collection ascEnds = Model.getFacade().getConnections(owner);
            Iterator iter = ascEnds.iterator();
	    Object ascEnd = null;
	    if (str.equals("src")) {
		ascEnd = iter.next();
            } else {
                while (iter.hasNext()) {
                    ascEnd = iter.next();
                }
            }

            if (!mult.equals(Model.getFacade().toString(
                    Model.getFacade().getMultiplicity(ascEnd)))) {
                Model.getCoreHelper().setMultiplicity(
                        ascEnd,
                        Model.getDataTypesFactory().createMultiplicity(
                                (String) mult));
            }

	}
    }

    /**
     * @return true if the action is enabled
     * @see org.tigris.gef.undo.UndoableAction#isEnabled()
     */
    public boolean isEnabled() {
	return true;
    }


    /**
     * @return Returns the srcMultOne.
     */
    public static UndoableAction getSrcMultOne() {
        return srcMultOne;
    }


    /**
     * @return Returns the destMultOne.
     */
    public static UndoableAction getDestMultOne() {
        return destMultOne;
    }


    /**
     * @return Returns the srcMultZeroToOne.
     */
    public static UndoableAction getSrcMultZeroToOne() {
        return srcMultZeroToOne;
    }


    /**
     * @return Returns the destMultZeroToOne.
     */
    public static UndoableAction getDestMultZeroToOne() {
        return destMultZeroToOne;
    }


    /**
     * @return Returns the srcMultZeroToMany.
     */
    public static UndoableAction getSrcMultZeroToMany() {
        return srcMultZeroToMany;
    }


    /**
     * @return Returns the destMultZeroToMany.
     */
    public static UndoableAction getDestMultZeroToMany() {
        return destMultZeroToMany;
    }


    /**
     * @return Returns the srcMultOneToMany.
     */
    public static UndoableAction getSrcMultOneToMany() {
        return srcMultOneToMany;
    }


    /**
     * @return Returns the destMultOneToMany.
     */
    public static UndoableAction getDestMultOneToMany() {
        return destMultOneToMany;
    }
} /* end class ActionSrcMultOneToMany */
