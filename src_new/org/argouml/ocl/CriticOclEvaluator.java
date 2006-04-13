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

package org.argouml.ocl;

import java.util.Iterator;

import org.argouml.model.Model;
import org.tigris.gef.base.Diagram;
import org.tigris.gef.ocl.ExpansionException;


/**
 * CriticOclEvaluator is responsible for evaluating simple OCL expressions
 * used in the critiques.<p>
 *
 * @stereotype singleton
 */
public class CriticOclEvaluator extends org.tigris.gef.ocl.OCLEvaluator {

    private static final CriticOclEvaluator INSTANCE =
        new CriticOclEvaluator();

    private CriticOclEvaluator() {
    }

    /**
     * @return the singleton of CriticOclEvaluator
     */
    public static final CriticOclEvaluator getInstance() {
        return INSTANCE;
    }

    /**
     * @see org.tigris.gef.ocl.OCLEvaluator#evalToString(
     * java.lang.Object, java.lang.String)
     */
    public synchronized String evalToString(Object self, String expr)
        throws ExpansionException {
        String res = null;
        if (GET_NAME_EXPR_1.equals(expr)
                && Model.getFacade().isAModelElement(self)) {
            res = Model.getFacade().getName(self);
            if (res == null || "".equals(res)) {
                res = "(anon)";
            }
        }
        if (GET_NAME_EXPR_2.equals(expr)
                && Model.getFacade().isAModelElement(self)) {
            res = Model.getFacade().getName(self);
            if (res == null || "".equals(res)) {
                res = "(anon)";
            }
        }
        if (GET_OWNER_EXPR.equals(expr) && Model.getFacade().isAFeature(self)) {
            Object owner = Model.getFacade().getOwner(self);
            if (owner != null) {
                res = Model.getFacade().getName(owner);
                if (res == null || "".equals(res)) {
                    res = "(anon)";
                }
            }
        }
        if (GET_NAME_EXPR_1.equals(expr) && self instanceof Diagram) {
            res = ((Diagram) self).getName();
            if (res == null || "".equals(res)) {
                res = "(anon)";
            }
        }
        if (GET_NAME_EXPR_2.equals(expr) && self instanceof Diagram) {
            res = ((Diagram) self).getName();
            if (res == null || "".equals(res)) {
                res = "(anon)";
            }
        }
    /*
        if (GET_OWNER_EXPR.equals(expr) && self instanceof Diagram) {
            res = ((Diagram)self).getOwner().getName();
            if (res == null || "".equals(res)) res = "(anon)";
        }
    */
        if (res == null) {
            res = evalToString(self, expr, ", ");
        }
        return res;
    }

    /**
     * @see org.tigris.gef.ocl.OCLEvaluator#evalToString(
     * java.lang.Object, java.lang.String, java.lang.String)
     */
    public synchronized String evalToString(
            Object self,
            String expr,
            String sep) throws ExpansionException {
        _scratchBindings.put("self", self);
        java.util.List values = eval(_scratchBindings, expr);
        _strBuf.setLength(0);
        Iterator iter = values.iterator();
        while (iter.hasNext()) {
            Object v = iter.next();
            if (Model.getFacade().isAModelElement(v)) {
                v = Model.getFacade().getName(v);
                if ("".equals(v)) {
                    v = "(anon)";
                }
            }
            if (Model.getFacade().isAExpression(v)) {
                v = Model.getFacade().getBody(v);
                if ("".equals(v)) {
                    v = "(unspecified)";
                }
            }
            if (!"".equals(v)) {
                _strBuf.append(v);
                if (iter.hasNext()) {
                    _strBuf.append(sep);
                }
            }
        }
        return _strBuf.toString();
    }

}  // end of OCLEvaluator
