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

package org.argouml.sequence2;

import junit.framework.TestCase;


/**
 * Tests the SequenceDiagramModule class.
 * @author penyaskito
 */
public class TestSequenceDiagramModule extends TestCase {

    private SequenceDiagramModule theModule;
       
    protected void setUp() throws Exception {
        super.setUp();
        theModule = new SequenceDiagramModule();
        theModule.enable();
    }
    
    /**
     * Tests than the name of the module is correct
     * @see SequenceDiagramModule#getName()
     */
    public void testGetName() {
        assertEquals("The name of the module is incorrect",
		     "ArgoUML-Sequence",
		     theModule.getName());
    }

    /**
     * Tests than the name of the module is correct
     * @see SequenceDiagramModule#getName()
     */
    public void testGetInfo() {
        assertEquals("The version of the module is incorrect",
		     "0.28",
		     theModule.getInfo(SequenceDiagramModule.VERSION));
        assertEquals("The version of the module is incorrect",
		     "http://argouml-sequence.tigris.org",
		     theModule.getInfo(SequenceDiagramModule.DOWNLOADSITE));

    }

    
}
