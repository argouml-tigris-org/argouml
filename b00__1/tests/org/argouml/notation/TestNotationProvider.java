// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.notation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationProvider;

/**
 * @author Michiel
 */
public class TestNotationProvider extends TestCase 
    implements PropertyChangeListener {

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
    }

    private Object aClass;
    private boolean propChanged = false;

    /**
     * Test the existence of the 
     * toString(Object modelElement, HashMap args) method.
     * TODO: Need to find a more usefull test.
     */
    public void testToString() {
        NotationProvider np = new NPImpl();
        HashMap<String, String> args = new HashMap<String, String>();
        args.put("b", "c");
        assertTrue("Test toString()", "a1".equals(np.toString("a", args)));
        args.put("d", "e");
        assertTrue("Test toString()", "f2".equals(np.toString("f", args)));
    }
    
    /**
     * Test the isValue utility function.
     */
    public void testIsValue() {
        HashMap<String, Object> args = new HashMap<String, Object>();
        args.put("not a boolean", "c");
        args.put("true", Boolean.TRUE);
        args.put("false", Boolean.FALSE);
        args.put("null", null);
        assertTrue("Not a boolean", 
                !NotationProvider.isValue("not a boolean", args));
        assertTrue("Finding true", 
                NotationProvider.isValue("true", args));
        assertTrue("Finding false", 
                !NotationProvider.isValue("false", args));
        assertTrue("Finding null", 
                !NotationProvider.isValue("null", args));
        assertTrue("Not encountered", 
                !NotationProvider.isValue("xyz", args));
    }

    private class NPImpl extends NotationProvider {

        /*
         * @see org.argouml.notation.providers.NotationProvider#getParsingHelp()
         */
        public String getParsingHelp() {
            return null;
        }

        /*
         * @see org.argouml.notation.providers.NotationProvider#toString(java.lang.Object, java.util.HashMap)
         */
        public String toString(Object modelElement, HashMap args) {
            return modelElement.toString() + args.size();
        }

        /*
         * @see org.argouml.notation.providers.NotationProvider#parse(java.lang.Object, java.lang.String)
         */
        public void parse(Object modelElement, String text) {

        }
    }
    
    public void testListener() {
        Object model =
            Model.getModelManagementFactory().createModel();
        Project p = ProjectManager.getManager().getCurrentProject();
        p.setRoot(model);
        aClass = Model.getCoreFactory().buildClass(model);
        
        NotationProvider np = new NPImpl();
        np.initialiseListener(this, aClass);
        
        propChanged = false;
        Model.getCoreHelper().setName(aClass, "ClassA1");
        Model.getPump().flushModelEvents();
        assertTrue("No event received", propChanged);
        
        np.cleanListener(this, aClass);
        propChanged = false;
        Model.getCoreHelper().setName(aClass, "ClassA2");
        Model.getPump().flushModelEvents();
        assertTrue("Event received, despite not listening", !propChanged);

        np.updateListener(this, aClass, null);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        propChanged = true;
    }
}
