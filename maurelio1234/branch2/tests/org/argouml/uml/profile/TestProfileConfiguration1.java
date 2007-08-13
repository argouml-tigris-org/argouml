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

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectImpl;
import org.argouml.model.InitializeModel;

/**
 * Tests the ProfileConfiguration
 *
 * @author Marcos Aurélio
 */
public class TestProfileConfiguration1 extends TestCase {
    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestProfileConfiguration1(String name) {
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
         * Tests whether the UML profile and formating strategy is set as the
         * default ones
         */
    public void testUMLDefaultStrategyAndModel() {
		Project p = new ProjectImpl();
		ProfileConfiguration pc = new ProfileConfiguration(p);

		assertTrue("UML profile has not been set as the default one!", pc
				.getProfiles().contains(ProfileUML.getInstance()));
		assertTrue("Java formating strategy has not been "
				+ "set as the default one!",
				pc.getFormatingStrategy() instanceof JavaFormatingStrategy);
		assertTrue("Java's defaulf profile formating strategy is not being "
				+ "used as the default formating strategy!", pc
				.getFormatingStrategy() == ProfileUML.getInstance()
				.getFormatingStrategy());
	}
    
    /**
     * Tests whether a new profile can be successfully added
     */
    public void testAddingNewProfile() {
    	Project p = new ProjectImpl();
    	ProfileConfiguration pc = new ProfileConfiguration(p);
        Profile pr = new UserDefinedProfile(new File("someprofile"));

        pc.addProfile(pr);
        assertTrue("Profile was not correctly added to the configuration!", pc
		.getProfiles().contains(pr));        
    }

    /**
     * Tests whether a new profile can be successfully removed 
     * from the configuration
     */
    public void testRemovingProfile() {
    	Project p = new ProjectImpl();
    	ProfileConfiguration pc = new ProfileConfiguration(p);
        Profile pr = new UserDefinedProfile(new File("someprofile"));

        if (!pc.getProfiles().contains(pr)) {
            pc.addProfile(pr);
            pc.removeProfile(pr);
            assertTrue(
		    "Profile was not correctly removed from the configuration!",
		    !pc.getProfiles().contains(pr));
        }
    }
    
    /**
     * Tests whether the configuration reacts properly when an unknow profile 
     * is being removed. It should not throw any exceptions.
     */
    public void testRemovingUnknownProfile() {
    	Project p = new ProjectImpl();
		ProfileConfiguration pc = new ProfileConfiguration(p);
		Profile pr = new UserDefinedProfile(new File("someprofile"));

		if (!pc.getProfiles().contains(pr)) {
			pc.removeProfile(pr);
		}
    }    
    
}
