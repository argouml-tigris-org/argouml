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
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;

/**
 * Action to set the Aggregation kind.
 *
 */
public class ActionAggregation extends UMLAction {
    private String str = "";
    private Object/*MAggregationKind*/ agg = null;


    ////////////////////////////////////////////////////////////////
    // static variables

    // aggregation
    private static UMLAction srcAgg =
	new ActionAggregation(ModelFacade.AGGREGATE_AGGREGATIONKIND, "src");
    private static UMLAction destAgg =
	new ActionAggregation(ModelFacade.AGGREGATE_AGGREGATIONKIND, "dest");

    private static UMLAction srcAggComposite =
	new ActionAggregation(ModelFacade.COMPOSITE_AGGREGATIONKIND, "src");
    private static UMLAction destAggComposite =
	new ActionAggregation(ModelFacade.COMPOSITE_AGGREGATIONKIND, "dest");

    private static UMLAction srcAggNone =
	new ActionAggregation(ModelFacade.NONE_AGGREGATIONKIND, "src");
    private static UMLAction destAggNone =
	new ActionAggregation(ModelFacade.NONE_AGGREGATIONKIND, "dest");


    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * The constructor.
     * @param a the aggregation kind object
     * @param s "src" or "dest". Anything else is interpreted as "dest".
     */
    protected ActionAggregation(Object/*MAggregationKind*/ a, String s) {
	super(ModelFacade.getName(a), true, NO_ICON);
	str = s;
	agg = a;
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	Vector sels = Globals.curEditor().getSelectionManager().selections();
	if ( sels.size() == 1 ) {
	    Selection sel = (Selection) sels.firstElement();
	    Fig f = sel.getContent();
	    Object owner = ((FigEdgeModelElement) f).getOwner();
	    Collection ascEnds = ModelFacade.getConnections(owner);
            Iterator iter = ascEnds.iterator();
	    Object ascEnd = null;
	    if (str.equals("src")) {
		ascEnd = iter.next();
            } else {
                while (iter.hasNext()) {
                    ascEnd = iter.next();
                }
            }
	    ModelFacade.setAggregation(ascEnd, agg);
	}
    }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() { 
	return true; 
    }


    /**
     * @return Returns the srcAgg.
     */
    public static UMLAction getSrcAgg() {
        return srcAgg;
    }


    /**
     * @return Returns the destAgg.
     */
    public static UMLAction getDestAgg() {
        return destAgg;
    }


    /**
     * @return Returns the srcAggComposite.
     */
    public static UMLAction getSrcAggComposite() {
        return srcAggComposite;
    }


    /**
     * @return Returns the destAggComposite.
     */
    public static UMLAction getDestAggComposite() {
        return destAggComposite;
    }


    /**
     * @return Returns the srcAggNone.
     */
    public static UMLAction getSrcAggNone() {
        return srcAggNone;
    }


    /**
     * @return Returns the destAggNone.
     */
    public static UMLAction getDestAggNone() {
        return destAggNone;
    }
} /* end class ActionSrcMultOneToMany */