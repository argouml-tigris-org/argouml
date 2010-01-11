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

package org.argouml.profile.internal.ocl;

import junit.framework.TestCase;

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;

/**
 * These test cases were written according to OCL 1.4 Spec Section 8
 * 
 * @author maurelio1234
 */
public class TestCrOCL2 extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Test the self variable (section 8.3.1)
     * 
     * @throws Exception if something goes wrong
     */
    public void testSelf() throws Exception {
        Object obj = Model.getUseCasesFactory().createActor();

        Model.getCoreHelper().setName(obj, "actor1");
        testOclOK(obj, "context Actor inv: self.name = 'actor1'");
        testOclFails(obj, "context Actor inv: self.name.size() = 7");
    }
    
    private void testOclOK(Object obj, String oclExp) 
            throws InvalidOclException {
        CrOCL ocl = new CrOCL(oclExp, null, null,
                null, null, null, null);

        assertEquals(ocl.predicate2(obj, Designer.theDesigner()),
                Critic.NO_PROBLEM);
    }

    private void testOclFails(Object obj, String oclExp) 
            throws InvalidOclException {
        CrOCL ocl = new CrOCL(oclExp, null, null,
                null, null, null, null);

        assertEquals(ocl.predicate2(obj, Designer.theDesigner()),
                Critic.PROBLEM_FOUND);
    }

}
