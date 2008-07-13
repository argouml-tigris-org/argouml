// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.argouml.profile.internal.ocl.LambdaEvaluator;
import org.argouml.profile.internal.ocl.ModelInterpreter;

/**
 * Interprets invocations to OCL collections API
 * 
 * @author maurelio1234
 */
public class CollectionsModelInterpreter implements ModelInterpreter {

    /**
     * @see org.argouml.profile.internal.ocl.ModelInterpreter#invokeFeature(java.util.HashMap,
     *      java.lang.Object, java.lang.String, java.lang.String,
     *      java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    public Object invokeFeature(HashMap<String, Object> vt, Object subject,
            String feature, String type, Object[] parameters) {
        if (subject instanceof Collection) {
            if (type.equals("->")) {
                if (feature.toString().trim().equals("select")) {
                    Vector<String> vars = (Vector<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    Collection col = cloneCollection((Collection) subject);
                    Vector<Object> remove = new Vector<Object>();

                    // TODO is it possible to use more than one variable?
                    String varName = vars.elementAt(0);
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
                } else if (feature.toString().trim().equals("forAll")) {
                    Vector<String> vars = (Vector<String>) parameters[0];
                    Object exp = parameters[1];
                    LambdaEvaluator eval = (LambdaEvaluator) parameters[2];

                    return doForAll(vt, (Collection)subject, vars, exp, eval);
                }
            }
        }

        if (subject instanceof Set) {
            if (type.equals("->")) {
                if (feature.equals("size")) {
                    return ((Set) subject).size();
                }
            }
        }

        return null;
    }

    private boolean doForAll(HashMap<String,Object> vt, Collection collection, Vector<String> vars, Object exp, LambdaEvaluator eval) {
        if (vars.isEmpty()) {
            return (Boolean) eval.evaluate(vt, exp);            
        } else {
            String var = vars.firstElement();
            vars.removeElement(var);
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
        if (col instanceof Set) {
            return new HashSet(col);
        } else if (col instanceof List) {
            return new ArrayList(col);
        } else if (col instanceof Bag) {
            return new HashBag(col);
        } else {
            throw new IllegalArgumentException();
        }        
    }


}
