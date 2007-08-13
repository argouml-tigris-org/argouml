// $Id$
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

package org.argouml.uml;

import java.util.Collection;

import org.argouml.model.Model;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

/**
 * 
 * @author euluis
 * @since 0.20
 * @version 0.00
 */
public class TestProfileJava extends TestCase {
    
    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestProfileJava(String name) {
        super(name);
    }
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Test whether we can load default model (profile). ProfileJava will throw
     * an exception for an invalid profile model, but just create and return an
     * empty model if the file for the profile doesn't exist. Check
     * for both failure modes.
     * 
     * @throws ProfileException
     *             exception thrown by ProfileJava for invalid profile file.
     */
    public void testLoadProfileModel() throws ProfileException {
        ProfileJava profile = new ProfileJava();
        Object model = profile.getProfileModel();
        assertNotNull("Can't load profile model", model);
        Collection stereos = Model.getModelManagementHelper()
                .getAllModelElementsOfKind(model,
                        Model.getMetaTypes().getStereotype());
        assertTrue("No stereotypes found in profile model", stereos.size() > 0);
    }
}
