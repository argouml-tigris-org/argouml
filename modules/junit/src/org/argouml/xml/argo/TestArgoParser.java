// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.xml.argo;

import java.net.URL;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;

/** Testcase to load projects without exception. */
public class TestArgoParser extends TestCase {
    public TestArgoParser(String name) {
        super(name);
    }

    /**
     * Tests that a project is loadable.
     *
     * @param filename of the project file to load
     * @throws Exception if something goes wrong.
     */
    private void loadProject(String filename) throws Exception {
	URL url;
	try {
	    url = new URL(filename);
	    Project p = ProjectManager.getManager().loadProject(url);
	    assertTrue("Load Status for " + filename + ".",
		       ArgoParser.SINGLETON.getLastLoadStatus());
	} catch (java.net.MalformedURLException e) {
	    assertTrue("Incorrect test case, malformed filename: " 
		       + filename + ".", false);
	}	
    }

    public void testLoadProject1() throws Exception { 
	loadProject("file:testmodels/Empty.zargo");
    }
    public void testLoadProject2() throws Exception {
	loadProject("file:testmodels/Alittlebitofeverything.zargo");
    }

    public void testLoadGarbage() {
	URL url = null;
	boolean loaded = true;
	try {
	    url = new URL("file:testmodels/Garbage.zargo");
	    Project p = ProjectManager.getManager().loadProject(url);
	    assertTrue("Load Status", !ArgoParser.SINGLETON.getLastLoadStatus());
	} catch (java.net.MalformedURLException e) {
	    assertTrue("Incorrect test case.", false);
	} catch (Exception io) {
	    // This is the normal case.
	    loaded = false;
	}
	assertTrue("No exception was thrown.", !loaded);
    }
}



