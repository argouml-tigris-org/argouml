/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.ocl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.DefaultOclEvaluator;
import org.argouml.profile.internal.ocl.InvalidOclException;
import org.argouml.profile.internal.ocl.ModelInterpreter;
import org.argouml.profile.internal.ocl.OclExpressionEvaluator;
import org.argouml.profile.internal.ocl.uml14.Uml14ModelInterpreter;
import org.tigris.gef.ocl.ExpansionException;


/**
 * OCLEvaluator is responsible for evaluating simple OCL expressions.
 * Such expressions are for example used in the critiques.<p>
 *
 * @stereotype singleton
 * @deprecated use {@link DefaultOclEvaluator} instead - maurelio1234
 */
@Deprecated
public class OCLEvaluator extends org.tigris.gef.ocl.OCLEvaluator {

    private OclExpressionEvaluator evaluator = new DefaultOclEvaluator();
    private HashMap<String, Object> vt = new HashMap<String, Object>();
    private ModelInterpreter modelInterpreter = new Uml14ModelInterpreter();
    
    /**
     * The constructor.
     *
     */
    public OCLEvaluator() {
    }

    /*
     * @see org.tigris.gef.ocl.OCLEvaluator#evalToString(java.lang.Object,
     * java.lang.String)
     */
    protected synchronized String evalToString(Object self, String expr)
        throws ExpansionException {
        if ("self".equals(expr)) {
            expr = "self.name";
        }
               
        vt.clear();
        vt.put("self", self);
        try {
            return value2String(evaluator.evaluate(vt, modelInterpreter, expr));
        } catch (InvalidOclException e) {
            return "<ocl>invalid expression</ocl>";
        }
    }

    /*
     * @see org.tigris.gef.ocl.OCLEvaluator#evalToString(java.lang.Object,
     * java.lang.String, java.lang.String)
     */
    protected synchronized String evalToString(
            Object self,
            String expr,
            String sep)
    	throws ExpansionException {

        _scratchBindings.put("self", self);
        java.util.List values = eval(_scratchBindings, expr);
        _strBuf.setLength(0);
        Iterator iter = values.iterator();
        while (iter.hasNext()) {
            Object v = value2String(iter.next());

            if (!"".equals(v)) {
                _strBuf.append(v);
                if (iter.hasNext()) {
                    _strBuf.append(sep);
                }
            }
        }
        return _strBuf.toString();
    }

    private String value2String(Object v) {
        if (Model.getFacade().isAExpression(v)) {
            v = Model.getFacade().getBody(v);
            if ("".equals(v)) {
                v = "(unspecified)";
            }
        } else if (Model.getFacade().isAUMLElement(v)) {
            v = Model.getFacade().getName(v);
            if ("".equals(v)) {
                v = Translator.localize("misc.name.anon");
            }
        } else if (v instanceof Collection) {
            String acc = "[";
            Collection collection = (Collection) v;

            for (Object object : collection) {
                acc += value2String(object) + ",";
            }        
            acc += "]";
            v = acc;
        }
        return "" + v;
    }
}  // end of OCLEvaluator
