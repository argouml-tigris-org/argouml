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

// Copyright (c) 2008 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.profile.internal.ocl.uml14;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.argouml.profile.internal.ocl.LambdaEvaluator;
import org.argouml.profile.internal.ocl.ModelInterpreter;

/**
 * Interprets invocations to OCL collections API
 * 
 * @author maurelio1234
 */
public class CollectionsModelInterpreter implements ModelInterpreter {

    /*
     * @see org.argouml.profile.internal.ocl.ModelInterpreter#invokeFeature(java.util.HashMap,
     *      java.lang.Object, java.lang.String, java.lang.String,
     *      java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    public Object invokeFeature(Map<String, Object> vt, Object subject,
            String feature, String type, Object[] parameters) {

        if (subject == null) {
            return null;
        }
        
        if (!(subject instanceof Collection)) {
            if (type.equals("->")) {
                Set ns = new HashSet();
                ns.add(subject);                    
                subject = ns;
            }
        }

        if (subject instanceof Collection) {
            if (type.equals("->")) {
                if (feature.toString().trim().equals("select")) {
                    List<String> vars = (List<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    Collection col = cloneCollection((Collection) subject);
                    List remove = new ArrayList();

                    // TODO is it possible to use more than one variable?
                    String varName = vars.get(0);
                    Object oldVal = vt.get(varName);

                    for (Object object : col) {
                        vt.put(varName, object);
                        Object res = eval.evaluate(vt, exp);
                        if (res instanceof Boolean && (Boolean) res) {
                            // do nothing
                        } else {
                            // if test fails this element should not
                            // be in the result set
                            remove.add(object);
                        }
                    }

                    col.removeAll(remove);
                    vt.put(varName, oldVal);

                    return col;
                } else if (feature.toString().trim().equals("reject")) {
                    List<String> vars = (ArrayList<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    Collection col = cloneCollection((Collection) subject);
                    List remove = new ArrayList();

                    // TODO is it possible to use more than one variable?
                    String varName = vars.get(0);
                    Object oldVal = vt.get(varName);

                    for (Object object : col) {
                        vt.put(varName, object);
                        Object res = eval.evaluate(vt, exp);
                        if (res instanceof Boolean && (Boolean) res) {
                            // if test is ok this element should not
                            // be in the result set
                            remove.add(object);
                        }
                    }

                    col.removeAll(remove);
                    vt.put(varName, oldVal);

                    return col;
                } else if (feature.toString().trim().equals("forAll")) {
                    List<String> vars = (ArrayList<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    return doForAll(vt, (Collection) subject, vars, exp, eval);
                } else if (feature.toString().trim().equals("collect")) {
                    List<String> vars = (ArrayList<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    Collection col = (Collection) subject;
                    Bag res = new HashBag();

                    // TODO is it possible to use more than one variable?
                    String varName = vars.get(0);
                    Object oldVal = vt.get(varName);

                    for (Object object : col) {
                        vt.put(varName, object);

                        Object val = eval.evaluate(vt, exp);
                        res.add(val);
                    }

                    vt.put(varName, oldVal);

                    return res;
                } else if (feature.toString().trim().equals("exists")) {
                    List<String> vars = (ArrayList<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    Collection col = (Collection) subject;

                    // TODO is it possible to use more than one variable?
                    String varName = vars.get(0);
                    Object oldVal = vt.get(varName);

                    for (Object object : col) {
                        vt.put(varName, object);

                        Object val = eval.evaluate(vt, exp);
                        if (val instanceof Boolean && (Boolean) val) {
                            return true;
                        }
                    }

                    vt.put(varName, oldVal);

                    return false;
                } else if (feature.toString().trim().equals("isUnique")) {
                    List<String> vars = (ArrayList<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    Collection col = (Collection) subject;
                    Bag<Object> res = new HashBag<Object>();

                    // TODO is it possible to use more than one variable?
                    String varName = vars.get(0);
                    Object oldVal = vt.get(varName);

                    for (Object object : col) {
                        vt.put(varName, object);

                        Object val = eval.evaluate(vt, exp);
                        res.add(val);
                        if (res.count(val) > 1) {
                            return false;
                        }
                    }

                    vt.put(varName, oldVal);

                    return true;
                } else if (feature.toString().trim().equals("one")) {
                    // TODO: This code is cloned over and over again! - tfm
                    List<String> vars = (ArrayList<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    Collection col = (Collection) subject;

                    // TODO is it possible to use more than one variable?
                    String varName = vars.get(0);
                    Object oldVal = vt.get(varName);
                    boolean found = false;

                    for (Object object : col) {
                        vt.put(varName, object);

                        Object val = eval.evaluate(vt, exp);
                        if (val instanceof Boolean && (Boolean) val) {
                            if (!found) {
                                found = true;
                            } else {
                                return false;
                            }
                        }
                    }

                    vt.put(varName, oldVal);

                    return found;
                } else if (feature.toString().trim().equals("any")) {
                    List<String> vars = (ArrayList<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    Collection col = (Collection) subject;

                    // TODO is it possible to use more than one variable?
                    String varName = vars.get(0);
                    Object oldVal = vt.get(varName);

                    for (Object object : col) {
                        vt.put(varName, object);

                        Object val = eval.evaluate(vt, exp);
                        if (val instanceof Boolean && (Boolean) val) {
                            return object;
                        }
                    }

                    vt.put(varName, oldVal);

                    return null;
                }

                // TODO implement iterate()
                // TODO implement sortedBy()
                // TODO implement subSequence()
            }
        }

        // these operations are ok for lists too
        if (subject instanceof Collection) {
            if (type.equals("->")) {
                if (feature.equals("size")) {
                    return ((Collection) subject).size();
                }

                if (feature.equals("includes")) {
                    return ((Collection) subject).contains(parameters[0]);
                }

                if (feature.equals("excludes")) {
                    return !((Collection) subject).contains(parameters[0]);
                }

                if (feature.equals("count")) {
                    return (new HashBag<Object>((Collection) subject))
                            .count(parameters[0]);
                }

                if (feature.equals("includesAll")) {

                    Collection col = (Collection) parameters[0];
                    for (Object object : col) {
                        if (!((Collection) subject).contains(object)) {
                            return false;
                        }
                    }
                    return true;
                }

                if (feature.equals("excludesAll")) {

                    Collection col = (Collection) parameters[0];
                    for (Object object : col) {
                        if (((Collection) subject).contains(object)) {
                            return false;
                        }
                    }
                    return true;
                }

                if (feature.equals("isEmpty")) {
                    return ((Collection) subject).isEmpty();
                }

                if (feature.equals("notEmpty")) {
                    return !((Collection) subject).isEmpty();
                }

                if (feature.equals("asSequence")) {
                    return new ArrayList<Object>((Collection) subject);
                }

                if (feature.equals("asBag")) {
                    return new HashBag<Object>((Collection) subject);
                }

                if (feature.equals("asSet")) {
                    return new HashSet<Object>((Collection) subject);
                }

                // TODO support real numbers
                if (feature.equals("sum")) {
                    Integer sum = 0;

                    Collection col = (Collection) subject;
                    for (Object object : col) {
                        sum += (Integer) object;
                    }
                    return sum;
                }

                if (feature.equals("union")) {
                    Collection copy = cloneCollection((Collection) subject);
                    copy.addAll((Collection) parameters[0]);
                    return copy;
                }

                if (feature.equals("append")) {
                    Collection copy = cloneCollection((Collection) subject);
                    copy.add(parameters[0]);
                    return copy;
                }

                if (feature.equals("prepend")) {
                    Collection copy = cloneCollection((Collection) subject);
                    if (copy instanceof List) {
                        ((List) copy).add(0, parameters[0]);
                    } else {
                        copy.add(parameters[0]);
                    }
                    return copy;
                }                
            }
        }

        if (subject instanceof List) {
            if (type.equals("->")) {
                if (feature.equals("at")) {
                    return ((List) subject).get((Integer) parameters[0]);
                }

                if (feature.equals("first")) {
                    return ((List) subject).get(0);
                }

                if (feature.equals("last")) {
                    return ((List) subject).get(((List) subject).size());
                }                
            }
        }
        // these operations are ok for bags too
        if (subject instanceof Set) {
            if (type.equals("->")) {

                if (feature.equals("intersection")) {
                    Set c1 = (Set) subject;
                    Set c2 = (Set) parameters[0];
                    Set r = new HashSet<Object>();

                    for (Object o : c1) {
                        if (c2.contains(o)) {
                            r.add(o);
                        }
                    }

                    for (Object o : c2) {
                        if (c1.contains(o)) {
                            r.add(o);
                        }
                    }

                    return r;
                }

                if (feature.equals("including")) {
                    Set copy = (Set) cloneCollection((Set) subject);
                    copy.add(parameters[0]);
                    return copy;
                }

                if (feature.equals("excluding")) {
                    Set copy = (Set) cloneCollection((Set) subject);
                    copy.remove(parameters[0]);
                    return copy;
                }

                if (feature.equals("symmetricDifference")) {
                    Set c1 = (Set) subject;
                    Set c2 = (Set) parameters[0];
                    Set r = new HashSet<Object>();

                    for (Object o : c1) {
                        if (!c2.contains(o)) {
                            r.add(o);
                        }
                    }

                    for (Object o : c2) {
                        if (!c1.contains(o)) {
                            r.add(o);
                        }
                    }

                    return r;
                }
            }
        }
        
        if (subject instanceof Bag) {
            if (type.equals("->")) {
                if (feature.equals("count")) {
                    return ((Bag) subject).count(parameters[0]);
                }
            }
        }

        return null;
    }

    private boolean doForAll(Map<String, Object> vt, Collection collection,
            List<String> vars, Object exp, LambdaEvaluator eval) {
        if (vars.isEmpty()) {
            return (Boolean) eval.evaluate(vt, exp);
        } else {
            String var = vars.get(0);
            vars.remove(var);
            Object oldval = vt.get(var);

            for (Object element : collection) {
                vt.put(var, element);

                boolean ret = doForAll(vt, collection, vars, exp, eval);

                if (!ret) {
                    return false;
                }
            }

            vt.put(var, oldval);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    private Collection cloneCollection(Collection col) {
        if (col instanceof List) {
            return new ArrayList(col);
        } else if (col instanceof Bag) {
            return new HashBag(col);
        } else if (col instanceof Set) {
            return new HashSet(col);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /*
     * @see org.argouml.profile.internal.ocl.ModelInterpreter#getBuiltInSymbol(java.lang.String)
     */
    public Object getBuiltInSymbol(String sym) {
        return null;
    }

}
