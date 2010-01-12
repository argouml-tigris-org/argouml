/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    euluis
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

import java.util.Collection;

import org.argouml.model.Model;
import org.argouml.profile.internal.ocl.CompositeModelInterpreter;

/**
 * Interpreter for UML 1.4 / OCL 1.4
 * 
 * @author maurelio1234
 */
public class Uml14ModelInterpreter extends CompositeModelInterpreter {

    /**
     * Default Constructor
     */
    public Uml14ModelInterpreter() {
        addModelInterpreter(new ModelAccessModelInterpreter());
        addModelInterpreter(new OclAPIModelInterpreter());
        addModelInterpreter(new CollectionsModelInterpreter());
    }

    private String toString(Object obj) {
        if (Model.getFacade().isAModelElement(obj)) {
            return Model.getFacade().getName(obj);
        } else if (obj instanceof Collection) {
            return colToString((Collection) obj);
        } else {
            return "" + obj;
        }
    }

    private String colToString(Collection collection) {
        String ret = "[";
        for (Object object : collection) {
            ret += toString(object) + ",";
        }        
        return ret + "]";
    }
    
}
