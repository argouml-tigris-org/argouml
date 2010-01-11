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

package org.argouml.profile;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

/**
 * Tests for the ProfileReference class.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestProfileReference extends TestCase {
    
    /**
     * Test correct call to constructor.
     * 
     * @throws MalformedURLException if the URL is incorrect.
     */
    public void testCtorHappyPath() throws MalformedURLException {
        new ProfileReference("systemId/name.xmi", 
                new URL("file:///publicId/name.xmi"));
    }
    
    /**
     * Test call to constructor with inconsistent file names in the path and 
     * the publicReference arguments.
     * 
     * NOTE: to run successfully this test you'll have to enable assertions.
     * 
     * @throws MalformedURLException if the URL is incorrect.
     */
    public void testCtorInconsistentFileNameDetected() 
        throws MalformedURLException {
        try {
            new ProfileReference("/org/argouml/language/x/profile/name.xmi", 
                    new URL("http://argouml-x.tigris.org/iconsistentName.xmi"));
            fail("Expected an AssertionError to be thrown!");
        } catch (MalformedURLException e) {
            throw e;
        } catch (AssertionError e) {
            // expected
        }
    }
    
    /**
     * Checks that the path handed to the constructor is correctly returned by 
     * {@link ProfileReference#getPath()}.
     * 
     * @throws MalformedURLException if the URL is incorrect.
     */
    public void testGetPath() throws MalformedURLException {
        String path = "/org/argouml/language/x/profile/name.xmi";
        ProfileReference profileReference = new ProfileReference(
            path, new URL("http://x.org/name.xmi"));
        assertEquals(path, profileReference.getPath());
    }
    
    /**
     * Checks that the publicReference handed to the constructor is correctly 
     * returned by {@link ProfileReference#getPublicReference()}.
     * 
     * @throws MalformedURLException if the URL is incorrect.
     */
    public void testGetPublicReference() throws MalformedURLException {
        URL publicReference = new URL("http://x.org/name.xmi");
        ProfileReference profileReference = new ProfileReference(
            "/org/argouml/language/x/profile/name.xmi", publicReference);
        assertEquals(publicReference, profileReference.getPublicReference());
    }
}
