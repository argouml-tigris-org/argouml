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

package org.argouml.notation.providers;

import java.util.HashMap;

import junit.framework.TestCase;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.notation.Notation;
import org.argouml.notation.NotationName;



/**
 * Test the NotationProviderFactory.
 *
 * @author Michiel
 */
public class TestNotationProviderFactory2 extends TestCase {

    private NotationName name;
    private Project proj;

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        name = Notation.makeNotation(
                    "Test",
                    null,
                    ResourceLoaderWrapper.lookupIconResource("JavaNotation"));
        Notation.setDefaultNotation(name);
        proj = ProjectManager.getManager().getCurrentProject();
        proj.getProjectSettings().setNotationLanguage(name);
    }

    public void testFactory() {
        assertTrue("Test notation not found", 
                Notation.findNotation("Test") != null);
        assertTrue("Default notation is not as expected", 
                Notation.getConfiguredNotation().getName().equals("Test"));
        assertTrue("Project notation is not as expected", 
                proj.getProjectSettings().getNotationName()
                    .sameNotationAs(name));        
        
        /* TODO: Why does the next part not work? */
//        NotationProviderFactory2.getInstance().addNotationProvider(1234, 
//                name, MyNP.class);
//        NotationProvider notationProvider =
//            NotationProviderFactory2.getInstance().getNotationProvider(
//                    1234, new Object());
//        assertTrue("Test notation provider for 1234 not found", 
//                notationProvider != null);
    }

    class MyNP extends NotationProvider {

        /*
         * Constructor.
         */
        public MyNP() {
        }

        /*
         * Constructor.
         *  
         * @param me an Object
         */
        public MyNP(Object me) {
            if (me == null) {
                throw new IllegalArgumentException("This is not an Object.");
            }
        }

        public MyNP getInstance() {
            return new MyNP();
        }

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

}