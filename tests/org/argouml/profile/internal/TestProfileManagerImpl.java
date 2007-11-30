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

package org.argouml.profile.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.argouml.model.InitializeModel;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileManager;

/**
 * Tests for the ProfileManagerImpl class.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestProfileManagerImpl extends TestCase {
    
    private ProfileManager manager;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        manager = new ProfileManagerImpl();
    }

    public void testProfileManagerImpl() {
        List<Profile> defaultProfiles = manager.getDefaultProfiles();
        // FIXME: this is prone to failures because it depends on the user's 
        // configuration.
        assertEquals(1, defaultProfiles.size());
        assertEquals(ProfileUML.NAME, 
                defaultProfiles.iterator().next().getDisplayName());
        List<Profile> registeredProfiles = manager.getRegisteredProfiles();
        assertEquals(2, registeredProfiles.size());
        Set<String> profileNameSet = new HashSet<String>();
        for (Profile profile : registeredProfiles) {
            assertTrue(profile.getDisplayName().equals(ProfileUML.NAME) 
                    || profile.getDisplayName().equals(ProfileJava.NAME));
            profileNameSet.add(profile.getDisplayName());
        }
        assertEquals(2, profileNameSet.size());
    }
    
    public void testRemoveProfileThatIsntDefault() {
        Profile javaProfile = manager.getProfileForClass(
                ProfileJava.class.getName());
        assertNotNull(javaProfile);
        assertTrue(manager.getRegisteredProfiles().contains(javaProfile));
        manager.removeProfile(javaProfile);
        assertFalse(manager.getRegisteredProfiles().contains(javaProfile));
    }
    
    public void testRemoveDefaultProfile() {
        Profile umlProfile = manager.getProfileForClass(
                ProfileUML.class.getName());
        assertNotNull(umlProfile);
        assertTrue(manager.getRegisteredProfiles().contains(umlProfile));
        assertTrue(manager.getDefaultProfiles().contains(umlProfile));
        manager.removeProfile(umlProfile);
        assertFalse(manager.getRegisteredProfiles().contains(umlProfile));
        assertFalse(manager.getDefaultProfiles().contains(umlProfile));
    }

}
