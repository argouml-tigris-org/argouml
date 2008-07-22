// $Id$
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

/**
 * Unit tests for UserProfileReference class.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestUserProfileReference extends TestCase {
    
    /**
     * Tests {@link UserProfileReference#UserProfileReference(String)} in the 
     * happy path.
     * 
     * @throws MalformedURLException if the built URL is incorrect.
     */
    public void testCtorHappyPath() throws MalformedURLException {
        String fileName = "profileName.xmi";
        // [euluis] Using Windows style initial path, don't know if this fails 
        // in *nixes.
        String path = "C:" + File.separatorChar + "userProfilesDir" 
            + File.separatorChar + fileName;
        ProfileReference reference = new UserProfileReference(path);
        assertEquals(path, reference.getPath());
        assertEquals(
            new URL(UserProfileReference.DEFAULT_USER_PROFILE_BASE_URL 
                + fileName), 
            reference.getPublicReference());
    }
    
    /**
     * Tests that the constructor checks for empty file name.
     * 
     * @throws MalformedURLException if the built URL is incorrect.
     */
    public void testCtorFailsWhenFileNameIsEmpty() 
        throws MalformedURLException {
        try {
            new UserProfileReference("");
            fail("Expecting AssertionError due to empty file name.");
        } catch (AssertionError e) {
            // expected
        }
    }
    
    /**
     * Tests that the constructor checks for null file name.
     * 
     * @throws MalformedURLException if the built URL is incorrect.
     */
    public void testCtorFailsWhenFileNameIsNull() 
        throws MalformedURLException {
        try {
            new UserProfileReference(null);
            fail("Expecting NullPointerException due to null path.");
        } catch (NullPointerException e) {
            // expected
        }
    }
    
}
