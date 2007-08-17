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
        Profile profile = new FakeProfile();
        ProfileManager manager = ProfileManagerImpl.getInstance();
        manager.registerProfile(profile);        
        
        assertTrue("Profile was not correctly registered", manager
		.getRegisteredProfiles().contains(profile));

        assertTrue("getProfileForClass() doesn't work", manager
        		.getProfileForClass(FakeProfile.class.getName()) == profile);
    }

    /**
     * Test whether the ProfileManager can register a default profile
     */
    public void testRegisterDefaultProfile() {
        Profile profile = new FakeProfile();
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        manager.addToDefaultProfiles(profile);
        assertTrue("Unregistered profile was registered as default!", !manager
        		.getDefaultProfiles().contains(profile));
        
        manager.registerProfile(profile);        
        manager.addToDefaultProfiles(profile);
        
        assertTrue("Profile was not correctly registered as default!", manager
        		.getDefaultProfiles().contains(profile));
    }
    
    /**
     * Test whether the ProfileManager can remove a previously registered 
     * profile
     */
    public void testUnregisterRegisteredProfile() {
        Profile profile = new FakeProfile();
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        if (!manager.getRegisteredProfiles().contains(profile)) {
            manager.registerProfile(profile);
            manager.removeProfile(profile);

            assertTrue("Profile was not correctly unregistered", !manager
    		.getRegisteredProfiles().contains(profile));
        }
    }
    
    /**
     * Test whether the ProfileManager can handle a non previously registered 
     * profile being removed. In this case it should not raise any exception.
     */
    public void testUnregisterUnknownProfile() {
        Profile profile = new FakeProfile();
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        if (!manager.getRegisteredProfiles().contains(profile)) {
            manager.removeProfile(profile);
        }
    }       

    /**
     * Test whether the ProfileManager can remove a profile previously  
     * registered as default
     */
    public void testUnregisterAsDefaultRegisteredProfile() {
        Profile profile = new FakeProfile();
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        if (!manager.getRegisteredProfiles().contains(profile)) {
            manager.registerProfile(profile);
            manager.addToDefaultProfiles(profile);
            manager.removeProfile(profile);

            assertTrue("Profile was not correctly unregistered", !manager
    		.getRegisteredProfiles().contains(profile));
            
            assertTrue("Profile was not correctly unregistered from the default profiles", !manager
            		.getDefaultProfiles().contains(profile));            
        }
    }

    /**
     * Test whether the ProfileManager can remove a profile previously  
     * registered as default.
     */
    public void testUnregisterAsDefaultRegisteredProfile2() {
        Profile profile = new FakeProfile();
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        if (!manager.getRegisteredProfiles().contains(profile)) {
            manager.registerProfile(profile);
            manager.registerProfile(profile);
            manager.removeFromDefaultProfiles(profile);

            assertTrue("Profile was not correctly unregistered from the default profiles", !manager
            		.getDefaultProfiles().contains(profile));            
        }
    }
    
    /**
     * Test whether the ProfileManager can handle a non previously registered 
     * profile being removed from the default profiles.
     * In this case it should not raise any exception.
     */
    public void testUnregisterAsDefaultUnknownProfile() {
        Profile profile = new FakeProfile();
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        if (!manager.getRegisteredProfiles().contains(profile)) {
            manager.removeFromDefaultProfiles(profile);
        }
    }       

    /**
     * Test whether the ProfileManager can register a search path directory
     */
    public void testRegisterSearchPathDirectory() {
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        String dir = "c:\temp";
        manager.addSearchPathDirectory(dir);
        assertTrue("Search directory was not registered!", !manager
        		.getSearchPathDirectories().contains(dir));
    }

    /**
     * Test whether the ProfileManager can unregister a search path directory
     */
    public void testUnregisterSearchPathDirectory() {
        ProfileManager manager = ProfileManagerImpl.getInstance();
        
        String dir = "c:\temp";
        manager.addSearchPathDirectory(dir);
        manager.removeSearchPathDirectory(dir);
        assertTrue("Search directory was not unregistered!", manager
        		.getSearchPathDirectories().contains(dir));
    }

    /**
     * Test whether the ProfileManager can unregister an unknown
     *  search path directory. This should raise no exception.
     */
    public void testUnregisterUnknownSearchPathDirectory() {
        ProfileManager manager = ProfileManagerImpl.getInstance();
        manager.removeSearchPathDirectory("hello world!");
    }

    /**
     * Test whether the ProfileManager can register an invalid
     *  search path directory and run the refreshRegisteredProfiles() 
     *  without raising any exceptions.
     */
    public void testRegisterInvalidSearchPathDirectory() {
        ProfileManager manager = ProfileManagerImpl.getInstance();
        manager.addSearchPathDirectory("/#/@!*&");
        manager.refreshRegisteredProfiles();
    }

}
