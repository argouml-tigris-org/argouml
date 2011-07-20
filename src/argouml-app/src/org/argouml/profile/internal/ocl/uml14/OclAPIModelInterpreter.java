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

import java.util.Map;

import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.ModelInterpreter;

/**
 * OCL API
 * 
 * @author maurelio1234
 */
public class OclAPIModelInterpreter implements ModelInterpreter {

    /*
     * @see org.argouml.profile.internal.ocl.ModelInterpreter#invokeFeature(java.util.Map,
     *      java.lang.Object, java.lang.String, java.lang.String,
     *      java.lang.Object[])
     */
    public Object invokeFeature(Map<String, Object> vt, Object subject,
            String feature, String type, Object[] parameters) {
        if (type.equals(".")) {
            // TODO implement the difference between oclIsKindOf and oclIsTypeOf
            if (feature.toString().trim().equals("oclIsKindOf")
                    || feature.toString().trim().equals("oclIsTypeOf")) {

                String typeName = ((OclType) parameters[0]).getName();

                if (typeName.equals("OclAny")) {
                    return true;
                } else {
                    return  Model.getFacade().isA(typeName, subject);
                }
            }

            if (feature.toString().trim().equals("oclAsType")) {
                return subject;
            }

            if (subject instanceof OclType) {
                if (feature.toString().trim().equals("name")) {
                    return ((OclType) subject).getName();
                }
            }

            if (subject instanceof String) {
                if (feature.toString().trim().equals("size")) {
                    return ((String) subject).length();
                }
                
                if (feature.toString().trim().equals("concat")) {
                    return ((String) subject).concat((String) parameters[0]);
                }

                if (feature.toString().trim().equals("toLower")) {
                    return ((String) subject).toLowerCase();
                }
                
                if (feature.toString().trim().equals("toUpper")) {
                    return ((String) subject).toUpperCase();
                }
                
                if (feature.toString().trim().equals("substring")) {
                    return ((String) subject).substring(
                            (Integer) parameters[0], (Integer) parameters[1]);
                }
                
            }

        }
        return null;
    }

    /*
     * @see org.argouml.profile.internal.ocl.ModelInterpreter#getBuiltInSymbol(java.lang.String)
     */
    public Object getBuiltInSymbol(String sym) {        
        if (sym.equals("OclType")) {
            return new OclType("OclType");
        // TODO implement OCLExpression
        } else if (sym.equals("OclExpression")) {
            return new OclType("OclExpression");
        }
        if (sym.equals("OclAny")) {
            return new OclType("OclAny");
        }
        return null;
    }

}
