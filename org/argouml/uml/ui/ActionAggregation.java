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

package org.argouml.uml.ui;

import org.argouml.uml.diagram.ui.*;
import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import java.awt.event.*;
import java.util.*;
import org.argouml.model.ModelFacade;

public class ActionAggregation extends UMLAction {
    String str = "";
    Object/*MAggregationKind*/ agg = null;


    ////////////////////////////////////////////////////////////////
    // static variables

    // aggregation
    public static UMLAction SrcAgg =
	new ActionAggregation(ModelFacade.AGGREGATE_AGGREGATIONKIND, "src");
    public static UMLAction DestAgg =
	new ActionAggregation(ModelFacade.AGGREGATE_AGGREGATIONKIND, "dest");

    public static UMLAction SrcAggComposite =
	new ActionAggregation(ModelFacade.COMPOSITE_AGGREGATIONKIND, "src");
    public static UMLAction DestAggComposite =
	new ActionAggregation(ModelFacade.COMPOSITE_AGGREGATIONKIND, "dest");

    public static UMLAction SrcAggNone =
	new ActionAggregation(ModelFacade.NONE_AGGREGATIONKIND, "src");
    public static UMLAction DestAggNone =
	new ActionAggregation(ModelFacade.NONE_AGGREGATIONKIND, "dest");


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionAggregation(Object/*MAggregationKind*/ a, String s) {
	super(ModelFacade.getName(a), NO_ICON);
	str = s;
	agg = a;
    }


    ////////////////////////////////////////////////////////////////
    // main methods

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

    public boolean shouldBeEnabled() { 
	return true; 
    }
} /* end class ActionSrcMultOneToMany */