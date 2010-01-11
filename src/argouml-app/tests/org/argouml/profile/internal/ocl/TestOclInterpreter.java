/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    maurelio1234
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.profile.internal.ocl;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.model.Model;

/**
 * Tests for the OclInterpreter class.
 * 
 * @author maurelio1234
 */
public class TestOclInterpreter extends TestCase {

    @SuppressWarnings("unused")
    private class DefaultModelInterpreter implements ModelInterpreter {
        public Object invokeFeature(HashMap<String, Object> vt, Object subject,
                String feature, String type, Object[] parameters) {
            return null;
        }

        public Object getBuiltInSymbol(String sym) {
            return null;
        }

        public Object invokeFeature(Map<String, Object> vt, Object subject,
                String feature, String type, Object[] parameters) {
            return null;
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Test <code>applicable</code> operation
     * 
     * @throws Exception if something goes wrong
     */
    public void testApplicable() throws Exception {
        Object obj1 = Model.getUseCasesFactory().createActor();
        Object obj2 = Model.getActivityGraphsFactory().createPartition();

        String ocl = "context Actor inv: 2 > 0";

        OclInterpreter interpreter = new OclInterpreter(ocl,
                new DefaultModelInterpreter());

        assertTrue(interpreter.applicable(obj1));
        assertFalse(interpreter.applicable(obj2));
    }

    /**
     * Test <code>getTriggers</code> operation
     * 
     * @throws Exception if something goes wrong
     */
    public void testGetTriggers() throws Exception {
        String ocl = "context Actor inv: 2 > 0";

        OclInterpreter interpreter = new OclInterpreter(ocl,
                new DefaultModelInterpreter());

        assertTrue(interpreter.getTriggers().contains("actor"));
    }

    /**
     * Test <code>check</code> operation (general)
     * 
     * @throws Exception if something goes wrong
     */
    public void testCheckGeneral() throws Exception {
        Object obj = Model.getUseCasesFactory().createActor();

        String ocl1 = "context Actor inv: 2 > 0";
        String ocl2 = "context Actor inv: 2 < 0";

        OclInterpreter interpreter1 = new OclInterpreter(ocl1,
                new DefaultModelInterpreter());
        OclInterpreter interpreter2 = new OclInterpreter(ocl2,
                new DefaultModelInterpreter());

        assertTrue(interpreter1.check(obj));
        assertFalse(interpreter2.check(obj));
    }

}
