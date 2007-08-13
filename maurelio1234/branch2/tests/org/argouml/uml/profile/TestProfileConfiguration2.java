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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.ImageIcon;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectImpl;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;

/**
 * Tests the handling of the strategies and models provided by the installed
 * profiles. 
 *
 * @author Marcos Aurélio
 */
public class TestProfileConfiguration2 extends TestCase {
    /**
     * The constructor.
     *
     * @param name the name of the test.
     */
    public TestProfileConfiguration2(String name) {
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
     * This class simulates a real profile, containing a model and 
     * some strategies
     * 
     * @author Marcos Aurélio
     */
    private class FakeProfile extends Profile {
	private Object model = null;
	private FigNodeStrategy figNodeStrategy = null;
	private FormatingStrategy formatStrategy = null;
	
	public FakeProfile() {
	    model = Model.getModelManagementFactory().createModel();
	    figNodeStrategy = new FakeFigNodeStrategy();
	    formatStrategy = new FakeFormatingStrategy();
	}
	
	public String getDisplayName() {
	    return toString();
	}

	public FigNodeStrategy getFigureStrategy() {
	    return figNodeStrategy;
	}

	public FormatingStrategy getFormatingStrategy() {
	    return formatStrategy;
	}

	public Object getModel() {
	    return model;
	}

	public FigNodeStrategy getFigNodeStrategy() {
	    return figNodeStrategy;
	}

	public void setFigNodeStrategy(FigNodeStrategy figNodeStrategy) {
	    this.figNodeStrategy = figNodeStrategy;
	}

	public FormatingStrategy getFormatStrategy() {
	    return formatStrategy;
	}

	public void setFormatStrategy(FormatingStrategy formatStrategy) {
	    this.formatStrategy = formatStrategy;
	}

	@Override
	public Collection getProfilePackages() throws ProfileException {
		return new ArrayList();
	}
	
    }
    
    /**
     * This class simulates a FigNodeStrategy
     * 
     * @author Marcos Aurélio
     */
    private class FakeFigNodeStrategy implements FigNodeStrategy {
	
	public Image getIconForStereotype(Object stereotype) {
	    return null;
	}
	
    }

    /**
     * This class simulates a FormatingStrategy
     * 
     * @author Marcos Aurélio
     */
    private class FakeFormatingStrategy implements FormatingStrategy {

	public String formatCollection(Iterator iter, Object namespace) {
	    return "";
	}

	public String formatElement(Object element, Object namespace) {
	    return "";
	}
	
    }
    
    /**
     * Whether a formating strategy can be property activated
     */
    public void testActivateFormatingStrategy() {
    	Project p = new ProjectImpl();
		ProfileConfiguration pc = new ProfileConfiguration(p);

		Profile pr = new FakeProfile();

		pc.addProfile(pr);
		pc.activateFormatingStrategy(pr);

		assertTrue("FormatingStrategy was not properly activated!", pc
				.getFormatingStrategy() == pr.getFormatingStrategy());
    }

    /**
     * When a profile is removed its formating strategy should also be removed  
     */
    public void testActivateFormatingStrategyAndRemoveProfile() {
    	Project p = new ProjectImpl();
		ProfileConfiguration pc = new ProfileConfiguration(p);

		Profile pr = new FakeProfile();

		pc.addProfile(pr);
		pc.activateFormatingStrategy(pr);
		pc.removeProfile(pr);

		assertTrue("FormatingStrategy was not properly removed "
				+ "when the profile was removed!",
				pc.getFormatingStrategy() != pr.getFormatingStrategy());
    }
    
    /**
     * The formating strategy should come from a profile that has been 
     * registered and provide one.   
     */
    public void testActivateInvalidFormatingStrategy() {
    	Project p = new ProjectImpl();
		ProfileConfiguration pc = new ProfileConfiguration(p);

		Profile pr1 = new FakeProfile();
		Profile pr2 = new FakeProfile();
		Profile pr3 = new FakeProfile();

		((FakeProfile) pr2).setFormatStrategy(null);

		pc.addProfile(pr1);
		pc.addProfile(pr2);

		// this should not raise any exception
		pc.activateFormatingStrategy(null);

		pc.activateFormatingStrategy(pr1);
		pc.activateFormatingStrategy(pr2);
		assertTrue("Activating the formating strategy of a profile that "
				+ "does not provide one should not take any effect!", pc
				.getFormatingStrategy() == pr1.getFormatingStrategy());

		pc.removeProfile(pr1);
		assertTrue("FormatingStrategy was not properly removed "
				+ "when the providing profile was removed!", pc
				.getFormatingStrategy() == null);

		FormatingStrategy before = pc.getFormatingStrategy();
		pc.activateFormatingStrategy(pr3);
		assertTrue("Activating the formating strategy of a profile that "
				+ "is has not been registered should not take any effect!", pc
				.getFormatingStrategy() == before);
    }
}
