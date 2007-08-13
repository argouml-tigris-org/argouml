// $Id:TestClassifierRoleNotationUml.java 12485 2007-05-03 05:59:35Z linus $
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

package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;

import org.argouml.model.Model;

/**
 * Test ClassifierRoleNotationUml (formerly ParserDisplay):
 * parsing classifier-role.
 *
 * @author Michiel
 */
public class TestClassifierRoleNotationUml extends TestCase {
    private final String clro01 = "/ roname : int";
    private final String clro02 = " : int , double / roname2 ";
    private final String clro03 = ":float,long/roname";

    private final String nclro01 = "/ roname : int / roname2 ";
    private final String nclro02 = "oname1 oname2 / roname : int , double";
    private final String nclro03 = "/ roname roname2 : int ";
    private final String nclro04 = "/ roname : int double ";

    /**
     * The constructor.
     *
     * @param str the name
     */
    public TestClassifierRoleNotationUml(String str) {
        super(str);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    public void setUp() throws Exception {
	super.setUp();
        InitializeModel.initializeDefault();
    }

    /**
     * Test the name of the ClassifierRole.
     */
    public void testClassifierRoleName() {
        Object cr;

        cr = Model.getCollaborationsFactory().createClassifierRole();
        checkNameClassifierRole(cr, clro01, "roname");
        checkNameClassifierRole(cr, clro02, "roname2");

        cr = Model.getCollaborationsFactory().createClassifierRole();
        checkNameClassifierRole(cr, clro03, "roname");
    }

    /**
     * Test the Base of the ClassifierRole.
     */
    public void testClassifierRoleBases() {
        Object cr;
        String[] res1 = {
            "int",
        };
        String[] res2 = {
            "int", "double",
        };
        String[] res3 = {
            "float", "long",
        };

        cr = Model.getCollaborationsFactory().createClassifierRole();
        checkBases(cr, clro01, res1);
        checkBases(cr, clro02, res2);
        checkBases(cr, clro03, res3);

        cr = Model.getCollaborationsFactory().createClassifierRole();
        checkBases(cr, clro03, res3);
    }

    /**
     * Test if parsing a ClassifierRole throws exceptions.
     */
    public void testClassifierRoleThrows() {
        Object cr;

        cr = Model.getCollaborationsFactory().createClassifierRole();
        checkThrowsClassifierRole(cr, nclro01, true, false, false);
        checkThrowsClassifierRole(cr, nclro02, true, false, false);
        checkThrowsClassifierRole(cr, nclro03, true, false, false);
        checkThrowsClassifierRole(cr, nclro04, true, false, false);
    }

    private void checkNameClassifierRole(Object ro, String text, String name) {
        try {
            ClassifierRoleNotationUml crn = new ClassifierRoleNotationUml(ro);
            crn.parseClassifierRole(ro, text);
        } catch (ParseException e) {
            fail("Could not parse expression " + text);
        }
        assertEquals(name, Model.getFacade().getName(ro));
    }

    private void checkThrowsClassifierRole(
                             Object ro,
                             String text,
                             boolean prsEx,
                             boolean ex2,
                             boolean ex3) {
        try {
            ClassifierRoleNotationUml crn = new ClassifierRoleNotationUml(ro);
            crn.parseClassifierRole(ro, text);
            fail("didn't throw for " + text);
        } catch (ParseException pe) {
            assertTrue(text + " threw ParseException " + pe, prsEx);
        } catch (Exception e) {
            assertTrue(text + " threw Exception " + e, !prsEx);
        }
    }

    private void checkBases(Object cr, String text, String[] bases) {
        int i;
        Collection c;
        Iterator it;
        Object cls;

        try {
            ClassifierRoleNotationUml crn = new ClassifierRoleNotationUml(cr);
            crn.parseClassifierRole(cr, text);
            c = Model.getFacade().getBases(cr);
            it = c.iterator();
        checkAllValid :
            while (it.hasNext()) {
                cls =  it.next();
                for (i = 0; i < bases.length; i++) {
                    if (bases[i].equals(Model.getFacade().getName(cls))) {
                        continue checkAllValid;
                    }
                }
                assertTrue(
                           "Base "
                           + Model.getFacade().getName(cls)
                           + " falsely "
                           + "generated by "
                           + text,
                           false);
            }

        checkAllExist :
            for (i = 0; i < bases.length; i++) {
                it = c.iterator();
                while (it.hasNext()) {
                    cls =  it.next();
                    if (bases[i].equals(Model.getFacade().getName(cls))) {
                        continue checkAllExist;
                    }
                }
                assertTrue("Base " + bases[i]
                           + " was not generated by " + text,
                           false);
            }
        } catch (Exception e) {
            assertTrue(text + " threw unexpectedly: " + e, false);
        }
    }

    /**
     * Test if help is correctly provided.
     */
    public void testGetHelp() {
        Object cr;
        cr = Model.getCollaborationsFactory().createClassifierRole();

        ClassifierRoleNotationUml notation = new ClassifierRoleNotationUml(cr);
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }
}
