/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    bobtarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.util.List;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.UndoableAction;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.Selection;
import org.tigris.gef.presentation.Fig;

/**
 * Action to set the Aggregation kind.
 *
 */
public class ActionAggregation extends UndoableAction {
    
    public static final int NONE = 0;
    public static final int AGGREGATE_END1 = 1;
    public static final int AGGREGATE_END2 = 2;
    public static final int COMPOSITE_END1 = 3;
    public static final int COMPOSITE_END2 = 4;
    
    private String str = "";
    private Object agg = null;

    Object associationEnd1;
    Object associationEnd2;
    int aggr;

    ////////////////////////////////////////////////////////////////
    // static variables

    // aggregation
    private static UndoableAction srcAgg =
	new ActionAggregation(
	        Model.getAggregationKind().getAggregate(), "src");
    private static UndoableAction destAgg =
	new ActionAggregation(
	        Model.getAggregationKind().getAggregate(), "dest");

    private static UndoableAction srcAggComposite =
	new ActionAggregation(
	        Model.getAggregationKind().getComposite(), "src");
    private static UndoableAction destAggComposite =
	new ActionAggregation(
	        Model.getAggregationKind().getComposite(), "dest");

    private static UndoableAction srcAggNone =
	new ActionAggregation(Model.getAggregationKind().getNone(), "src");
    private static UndoableAction destAggNone =
	new ActionAggregation(
	        Model.getAggregationKind().getNone(), "dest");


    ////////////////////////////////////////////////////////////////
    // constructors
    
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
    public static ActionAggregation newActionAggregation(Object assocStart,
                                                           Object assocEnd,
                                                           int aggr) {
        return new ActionAggregation(getDescription(assocStart, assocEnd, aggr),
                                      assocStart,
                                      assocEnd,
                                      aggr);
    }

    /**
     * The constructor.
     *
     * @param label   the description as build in <code>getDescription</code>
     * @param associationEnd1 association end 1
     * @param associationEnd2   association end 2
     * @param aggr     the aggregation: one of
     *                            NONE, AGGREGATE_END1, AGGREGATE_END2,
     *                            COMPOSITE_END1, COMPOSITE_END  
     */
    private ActionAggregation(
            final String label,
            final Object associationEnd1,
            final Object associationEnd2,
            final int aggr) {
        super(label, null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, label);
        this.aggr = aggr;
        this.associationEnd1 = associationEnd1;
        this.associationEnd2 = associationEnd2;
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
    private static String getDescription(final Object assocEnd1,
                                         final Object assocEnd2,
                                         final int aggr) {
        String startName =
            Model.getFacade().getName(Model.getFacade().getType(assocEnd1));
        String endName =
            Model.getFacade().getName(Model.getFacade().getType(assocEnd2));

        if (startName == null || startName.length() == 0) {
            startName = Translator.localize("action.navigation.anon");
        }
        if (endName == null || endName.length() == 0) {
            endName = Translator.localize("action.navigation.anon");
        }
        
        if (aggr == COMPOSITE_END1) {
            return Translator.messageFormat(
                    "action.aggregation.composite",
                    new Object[] {
                        startName,
                        endName,
                    }
            );
        } else if (aggr == COMPOSITE_END2) {
            return Translator.messageFormat(
                    "action.aggregation.composite",
                    new Object[] {
                        endName,
                        startName,
                    }
            );
        } else if (aggr == AGGREGATE_END1) {
            return Translator.messageFormat(
                    "action.aggregation.aggregate",
                    new Object[] {
                        startName,
                        endName,
                    }
            );
        } else if (aggr == AGGREGATE_END2) {
            return Translator.messageFormat(
                    "action.aggregation.aggregate",
                    new Object[] {
                        endName,
                        startName,
                    }
            );
        } else {
            return Translator.localize("action.aggregation.none");
        }
    }
    
    /**
     * The constructor.
     * @param a the aggregation kind object
     * @param s "src" or "dest". Anything else is interpreted as "dest".
     * @deprecated by Bob Tarling in 0.33.1 by Bob Tarling use
     * ActionAggregation.newActionAggregation
     */
    protected ActionAggregation(Object a, String s) {
        super(Translator.localize(Model.getFacade().getName(a)),
                null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize(Model.getFacade().getName(a)));
	str = s;
	agg = a;
    }

    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (agg != null) {
            oldActionPerformed(ae);
        } else {
            super.actionPerformed(ae);
            if (aggr == AGGREGATE_END1) {
                Model.getCoreHelper().setAggregation2(associationEnd1, Model.getAggregationKind().getAggregate());
                Model.getCoreHelper().setAggregation2(associationEnd2, Model.getAggregationKind().getNone());
            } else if (aggr == AGGREGATE_END2) {
                Model.getCoreHelper().setAggregation2(associationEnd1, Model.getAggregationKind().getNone());
                Model.getCoreHelper().setAggregation2(associationEnd2, Model.getAggregationKind().getAggregate());
            } else if (aggr == COMPOSITE_END1) {
                Model.getCoreHelper().setAggregation2(associationEnd1, Model.getAggregationKind().getComposite());
                Model.getCoreHelper().setAggregation2(associationEnd2, Model.getAggregationKind().getNone());
            } else if (aggr == COMPOSITE_END2) {
                Model.getCoreHelper().setAggregation2(associationEnd1, Model.getAggregationKind().getNone());
                Model.getCoreHelper().setAggregation2(associationEnd2, Model.getAggregationKind().getComposite());
            } else {
                Model.getCoreHelper().setAggregation2(associationEnd1, Model.getAggregationKind().getNone());
                Model.getCoreHelper().setAggregation2(associationEnd2, Model.getAggregationKind().getNone());
            }
        }
    }

    public void oldActionPerformed(ActionEvent ae) {
        super.actionPerformed(ae);
        List sels = Globals.curEditor().getSelectionManager().selections();
        if (sels.size() == 1) {
            Selection sel = (Selection) sels.get(0);
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
            Model.getCoreHelper().setAggregation(ascEnd, agg);
        }
    }
    
    
    /*
     * @see org.tigris.gef.undo.UndoableAction#isEnabled()
     */
    @Override
    public boolean isEnabled() {
	return true;
    }


    /**
     * @return Returns the srcAgg.
     */
    public static UndoableAction getSrcAgg() {
        return srcAgg;
    }


    /**
     * @return Returns the destAgg.
     */
    public static UndoableAction getDestAgg() {
        return destAgg;
    }


    /**
     * @return Returns the srcAggComposite.
     */
    public static UndoableAction getSrcAggComposite() {
        return srcAggComposite;
    }


    /**
     * @return Returns the destAggComposite.
     */
    public static UndoableAction getDestAggComposite() {
        return destAggComposite;
    }


    /**
     * @return Returns the srcAggNone.
     */
    public static UndoableAction getSrcAggNone() {
        return srcAggNone;
    }


    /**
     * @return Returns the destAggNone.
     */
    public static UndoableAction getDestAggNone() {
        return destAggNone;
    }
} /* end class ActionSrcMultOneToMany */
