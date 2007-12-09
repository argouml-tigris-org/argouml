// $Id$
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

package org.argouml.profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.model.InitializeModel;
import org.argouml.profile.internal.InitProfileSubsystem;

import junit.framework.TestCase;

/**
 * Tests for a ProfileManager implementation.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestProfileManager extends TestCase {
    
    private Profile mockProfile;
    private ProfileManager manager;
    private String mockProfileClassName;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
        mockProfile = new Profile() {
            @Override
            public String getDisplayName() {
                return null;
            }
            @Override
            public Collection<Object> getProfilePackages() 
                throws ProfileException {
                return null;
            }
        };
        manager = ProfileFacade.getManager();
        assertNotNull(manager);
    }
    
    @Override
    protected void tearDown() throws Exception {
        ProfileFacade.reset();
        super.tearDown();
    }

    public void testRegisterProfile() {
        manager.registerProfile(mockProfile);
        assertTrue(manager.getRegisteredProfiles().contains(mockProfile));
    }
    
    public void testRemoveProfile() {
        assertFalse(manager.getRegisteredProfiles().contains(mockProfile));
        manager.registerProfile(mockProfile);
        assertTrue(manager.getRegisteredProfiles().contains(mockProfile));
        manager.removeProfile(mockProfile);
        assertFalse(manager.getRegisteredProfiles().contains(mockProfile));
    }

    public void testGetProfileForClass() {
        mockProfileClassName = mockProfile.getClass().getName();
        assertNull(manager.getProfileForClass(mockProfileClassName));
        manager.registerProfile(mockProfile);
        assertEquals(mockProfile, manager.getProfileForClass(
                mockProfileClassName));
        manager.removeProfile(mockProfile);
        assertNull(manager.getProfileForClass(mockProfileClassName));
    }
    
    public void testDefaultProfilesManagement() {
        List<Object> initialDefaultProfiles = 
            new ArrayList<Object>(manager.getDefaultProfiles());
        assertNull(manager.getProfileForClass(mockProfileClassName));
        manager.registerProfile(mockProfile);
        manager.addToDefaultProfiles(mockProfile);
        assertEquals(initialDefaultProfiles.size() + 1, 
                manager.getDefaultProfiles().size());
        manager.removeFromDefaultProfiles(mockProfile);
        assertEquals(initialDefaultProfiles.size(), 
                manager.getDefaultProfiles().size());
        assertFalse(manager.getDefaultProfiles().contains(mockProfile));
    }
    
    public void testGetUMLProfile() throws Exception {
        Profile uml = manager.getUMLProfile();
        assertNotNull(uml);
        assertTrue(uml.getDisplayName().indexOf("UML") != -1);
    }
}
