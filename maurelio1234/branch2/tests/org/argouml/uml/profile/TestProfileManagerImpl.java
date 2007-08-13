// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
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

package org.argouml.uml.profile;

import java.io.File;

import junit.framework.TestCase;

/**
 * Tests for the default implementation of the ProfileManager
 *
 * @author Marcos Aurélio
 */
public class TestProfileManagerImpl extends TestCase {
    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestProfileManagerImpl(String name) {
        super(name);
    }
    
    /**
     * Test whether the ProfileManager can register a new profile
     */
    public void testRegisterProfile() {
        Profile profile = new UserDefinedProfile(new File("someprofile"));
        ProfileManager manager = ProfileManagerImpl.getInstance();
        manager.registerProfile(profile);        
        assertTrue("Profile was not correctly registered", manager
		.getRegisteredProfiles().contains(profile));
    }

    /**
     * Test whether the ProfileManager can remove a previously registered 
     * profile
     */
    public void testUnregisterRegisteredProfile() {
        Profile profile = new UserDefinedProfile(new File("someprofile"));
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        if (!manager.getRegisteredProfiles().contains(profile)) {
            manager.registerProfile(profile);
            manager.removeProfile(profile);
            assertTrue("Profile was not correctly unregistered", !manager
    		.getRegisteredProfiles().contains(profile));
        }
    }
    
    /**
     * Test whether the ProfileManager can handle a non proviously registered 
     * profile being removed. In this case it should not raise any exception.
     */
    public void testUnregisterUnknownProfile() {
        Profile profile = new UserDefinedProfile(new File("someprofile"));
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        if (!manager.getRegisteredProfiles().contains(profile)) {
            manager.removeProfile(profile);
        }
    }       
}
