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

package org.argouml.xml.xmi;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.kernel.IllegalFormatException;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.foundation.core.CoreFactory;

import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;

/**
 * Testclass for the XMIReader. Placeholder for all saving/loading tests
 * concerning XMIReader (like the dreaded ClassCastException issues).
 * @author jaap.branderhorst@xs4all.nl	
 * @since Jan 17, 2003
 */
public class TestXMIReader extends TestCase {

    /**
     * Constructor for TestXMIReader.
     * @param arg0
     */
    public TestXMIReader(String arg0) {
        super(arg0);
    }
    
    protected void setUp() {
        
	ArgoSecurityManager.getInstance().setAllowExit(true);
        UmlFactory.getFactory().setGuiEnabled(false); 
    }

    /**
     * This is a regression test for issue 1504. Unfortunately this test does
     * not work since ArgoUML crashes on a classcastexception that is catched by
     * our dear friends of NSUML. However you can use it to test things quite
     * easily :)
     */
    public void testReadReturnParameter()
	throws IOException, MalformedURLException, IllegalFormatException,
	       Exception
    {
        // next statement should be in a ArgoTestCase or something, is allmost 
        // allways needed
        ArgoSecurityManager.getInstance().setAllowExit(true);
        Project p = ProjectManager.getManager().makeEmptyProject();
        MClass clazz = CoreFactory.getFactory().buildClass(p.getModel());
        MOperation oper = CoreFactory.getFactory().buildOperation(clazz);
        ModelFacade.setType(oper.getParameter(0), p.findType("String"));
        File file = new File("test.zargo");

	p.save(true, file);

        p = null;
        p = ProjectManager.getManager().makeEmptyProject();

	URL url = file.toURL();
	ProjectManager.getManager().loadProject(url);
    }

}
