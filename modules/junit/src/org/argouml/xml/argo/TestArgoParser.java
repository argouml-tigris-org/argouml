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

import java.net.*;
import junit.framework.*;
import org.argouml.ui.*;
import org.argouml.kernel.*;

/** Testcase to load projects without exception. */
public class TestArgoParser extends TestCase {
    public TestArgoParser(String name) {
        super(name);
    }


    /** try to load a project from the web.
     */
    public void testLoadProjectWeb() {
        Project p;
        try {
            URL url = new URL("http://argouml.tigris.org/models/argouml/ocl.zargo");
            p = Project.loadProject(url);
            assert("Load Status", ArgoParser.SINGLETON.getLastLoadStatus());
        }
        catch(Exception e) {
            assert(e.toString(), false);
        }
    }

        /** try to load a project from the local file system.
     */
    public void testLoadProjectFile() {
        Project p;
        try {
            URL url = new URL("file:///home/mkl/Editor.zargo");
            p = Project.loadProject(url);
            assert("Load Status", ArgoParser.SINGLETON.getLastLoadStatus());
        }
        catch(Exception e) {
            assert(e.toString(), false);
        }
    }
}



