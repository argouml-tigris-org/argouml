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

import org.argouml.application.api.InitSubsystem;
import org.argouml.model.InitializeModel;
import org.argouml.profile.internal.InitProfileSubsystem;

import junit.framework.TestCase;

/**
 * Test cases for the initialization of the profile subsystem.
 *
 * @author Luis Sergio Oliveira (euluis)
 */
public class TestSubsystemInit extends TestCase {
    
    private InitSubsystem initSubsystem;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        initSubsystem = new InitProfileSubsystem();
    }

    public void testInitMakesManagerAvailableInFacade() {
        try {
            ProfileFacade.getManager();
            fail("ProfileFacade shouldn't be initialized!");
        } catch (RuntimeException e) { }
        initSubsystem.init();
        assertNotNull(ProfileFacade.getManager());
    }
    
    public void testSettingsTabsEmpty() {
        assertEquals(0, initSubsystem.getSettingsTabs().size());
    }
    
    public void testProjectSettingsTabsEmpty() {
        assertEquals(0, initSubsystem.getProjectSettingsTabs().size());
    }
}
