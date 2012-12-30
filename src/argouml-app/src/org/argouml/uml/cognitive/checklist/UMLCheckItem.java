/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    linus
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

package org.argouml.uml.cognitive.checklist;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.argouml.cognitive.checklist.CheckItem;
import org.argouml.i18n.Translator;
import org.argouml.model.InvalidElementException;
import org.argouml.ocl.CriticOclEvaluator;
import org.argouml.ocl.OCLEvaluator;
import org.tigris.gef.ocl.ExpansionException;


/** A special kind of CheckItem that can replace OCL expressions with
 *  their values in the generated advice.
 *
 * @see org.argouml.ocl.OCLEvaluator */

public class UMLCheckItem extends CheckItem {
    private static final Logger LOG =
        Logger.getLogger(UMLCheckItem.class.getName());

    /**
     * The constructor.
     *
     * @param c the category
     * @param d the description
     */
    public UMLCheckItem(String c, String d) { super(c, d); }

    /**
     * The constructor.
     *
     * @param c the category
     * @param d the description
     * @param m the more-info-url
     * @param p the predicate
     */
    public UMLCheckItem(String c, String d, String m,
            org.argouml.util.Predicate p) {
        super(c, d, m, p);
    }

    /*
     * @see org.argouml.cognitive.checklist.CheckItem#expand(java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public String expand(String res, Object dm) {
	int searchPos = 0;
	int matchPos = res.indexOf(OCLEvaluator.OCL_START, searchPos);

	// replace all occurances of OFFENDER with the name of the
	// first offender
	while (matchPos != -1) {
	    int endExpr = res.indexOf(OCLEvaluator.OCL_END, matchPos + 1);
	    String expr = res.substring(matchPos
                + OCLEvaluator.OCL_START.length(), endExpr);
	    String evalStr = null;

	    try {
	        evalStr = CriticOclEvaluator.getInstance()
	                            .evalToString(dm, expr);
	    } catch (ExpansionException e) {
	        // Really ought to have a CriticException to throw here.
                LOG.log(Level.SEVERE,
                        "Failed to evaluate critic expression", e);
	    } catch (InvalidElementException e) {
                /* The modelelement must have been
                 * deleted - ignore this - it will pass. */
                evalStr = Translator.localize("misc.name.deleted");
            }
            LOG.log(Level.FINE, "expr={0} = {1}", new Object[]{expr, evalStr});

	    res = res.substring(0, matchPos) + evalStr
	        + res.substring(endExpr + OCLEvaluator.OCL_END.length());
	    searchPos = endExpr + 1;
	    matchPos = res.indexOf(OCLEvaluator.OCL_START, searchPos);
	}
	return res;
    }

}
