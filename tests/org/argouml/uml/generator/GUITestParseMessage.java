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

package org.argouml.uml.generator;

import java.text.ParseException;
import java.util.Iterator;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.behavior.collaborations.MCollaboration;
import ru.novosoft.uml.behavior.collaborations.MInteraction;
import ru.novosoft.uml.behavior.collaborations.MMessage;

/**
 * Test class to test the parsing of a message.<p>
 *
 * For some reason these tests require the ProjectBrowser loaded when running.
 * For that reason they cannot be run in Headless mode.
 */
public class GUITestParseMessage extends TestCase {
    /**
     * @see junit.framework.TestCase#TestCase()
     */
    public GUITestParseMessage(String str) {
	super(str);
    }

    /**
     * Test the parsing of a message.
     *
     * @throws ParseException if the parsing was in error.
     */
    public void testParseMessage()
	throws ParseException
    {
	MMessage m;
	Iterator it;

	UmlFactory fact = UmlFactory.getFactory();

	MCollaboration coll =
	    fact.getCollaborations().createCollaboration();
	MInteraction inter =
	    fact.getCollaborations().buildInteraction(coll);

	MClassifierRole cl1 =
	    fact.getCollaborations().createClassifierRole();
	MClassifierRole cl2 =
	    fact.getCollaborations().createClassifierRole();
	MClassifierRole cl3 =
	    fact.getCollaborations().createClassifierRole();
	MClassifierRole cl4 =
	    fact.getCollaborations().createClassifierRole();
	MClassifierRole cl5 =
	    fact.getCollaborations().createClassifierRole();
	cl1.setNamespace(coll);
	cl2.setNamespace(coll);
	cl3.setNamespace(coll);
	cl4.setNamespace(coll);
	cl5.setNamespace(coll);

	MAssociationRole r1_2 = 
	    fact.getCollaborations().buildAssociationRole(cl1, cl2);
	MAssociationRole r2_3 =
	    fact.getCollaborations().buildAssociationRole(cl2, cl3);
	MAssociationRole r3_4 =
	    fact.getCollaborations().buildAssociationRole(cl3, cl4);
	MAssociationRole r4_5 =
	    fact.getCollaborations().buildAssociationRole(cl4, cl5);
	MAssociationRole r3_1 =
	    fact.getCollaborations().buildAssociationRole(cl3, cl1);
	MAssociationRole r5_3 =
	    fact.getCollaborations().buildAssociationRole(cl5, cl3);

	/* START TESTING STUFF */

	MMessage m1 =
	    fact.getCollaborations().buildMessage(inter, r1_2);
	assertTrue(m1.getSender() == cl1);
	assertTrue(m1.getReceiver() == cl2);
	assertTrue(m1.getInteraction() == inter);
	assertNull(m1.getActivator());
	assertTrue(m1.getAction() == null 
		   || m1.getAction().getRecurrence() == null);
	assertTrue(m1.getPredecessors().size() == 0);
	m1.setName("m1");

	MMessage m2 =
	    fact.getCollaborations().buildMessage(inter, r2_3);
	assertTrue(m2.getSender() == cl2);
	assertTrue(m2.getReceiver() == cl3);
	assertTrue(m2.getActivator() == m1);
	assertTrue(m2.getAction() == null 
		   || m2.getAction().getRecurrence() == null);
	assertTrue(m2.getPredecessors().size() == 0);
	m2.setName("m2");

	MMessage m3 =
	    fact.getCollaborations().buildMessage(inter, r2_3);
	assertTrue(m3.getActivator() == m1);
	assertTrue(
		   m3.getPredecessors().iterator().next() == m2
		   && m3.getPredecessors().size() == 1);
	m3.setName("m3");

	/* TRY MOVING IN A SIMPLE MANER */

	parseMessage(m3, " \t1.1 : ");
	assertTrue(m3.getActivator() == m1);
	assertTrue(m3.getPredecessors().size() == 0);
	assertTrue(
		   m2.getPredecessors().iterator().next() == m3
		   && m2.getPredecessors().size() == 1);

	parseMessage(m3, " / 1.2\t: ");
	assertTrue(m3.getActivator() == m1);
	assertTrue(m2.getPredecessors().size() == 0);
	assertTrue(
		   m3.getPredecessors().iterator().next() == m2
		   && m3.getPredecessors().size() == 1);

	/* TRY SOME ERRORS */

	checkParseException(m3, " 2.1 : ");
	checkParseException(m3, " 1.2 : 1.2 :");
	checkParseException(m3, " / / 1.2 : ");

	/* TRY SOME MORE COMPLEX MOVING */

	parseMessage(m3, " 1.1.1 : ");
	assertTrue(m3.getActivator() == m2);
	assertTrue(m3.getPredecessors().size() == 0);

	parseMessage(m3, " / 1..2 : ");
	assertTrue(m3.getActivator() == m1);
	assertTrue(m2.getPredecessors().size() == 0);
	assertTrue(
		   m3.getPredecessors().iterator().next() == m2
		   && m3.getPredecessors().size() == 1);
	parseMessage(m3, "");
	assertTrue(m3.getActivator() == m1);
	assertTrue(
		   m3.getPredecessors().iterator().next() == m2
		   && m3.getPredecessors().size() == 1);

	/* TRY SOME MORE ERRORS */

	checkParseException(m1, " 1.1 : ");
	checkParseException(m2, " 1.1.1 : ");

	/* TRY GUARD/ITERATOR SYNTAX */

	parseMessage(m3, " 1.2 [ x < 5 ] : ");
	assertNotNull(m3.getAction());
	assertNotNull(m3.getAction().getRecurrence());
	assertTrue("[x < 5]".equals(m3.getAction().getRecurrence().getBody()));

	parseMessage(m3, " 1.2 * [ i = 1..10 ] : ");
	assertEquals("*[i = 1..10]", m3.getAction().getRecurrence().getBody());

	parseMessage(m3, " 1.2 *// : ");
	assertEquals("*[i = 1..10]", m3.getAction().getRecurrence().getBody());

	parseMessage(m3, " * // [i=1..] 1.2 : ");
	assertEquals("*//[i=1..]", m3.getAction().getRecurrence().getBody());
	parseMessage(m3, " 1.2 : ");
	assertEquals("*//[i=1..]", m3.getAction().getRecurrence().getBody());

	/* TRY SOME GUARD/ITERATOR ERRORS */

	checkParseException(m3, " [x < 5] 1.2 [x > 6] : ");
	checkParseException(m3, " 1 [x < 5] / 1.2 : ");
	checkParseException(m3, " 1 * / 1.2 : ");
	checkParseException(m3, " 1 // / 1.2 : ");
	checkParseException(m3, " 1 , 2 [x < 5] / 1.2 : ");
	checkParseException(m3, " 1 , 2 * / 1.2 : ");
	checkParseException(m3, " 1 , 2 // / 1.2 : ");
	checkParseException(m3, "/ 1.2 , 2 : ");
	checkParseException(m3, "/1.2,2:");

	/* TRY THE ACTIONS */

	parseMessage(m3, " 1.2 : func() ");
	assertEquals("func", m3.getAction().getScript().getBody());
	parseMessage(m3, " 1.2 ");
	assertEquals("func", m3.getAction().getScript().getBody());
	parseMessage(m3, " 1.2 : ");
	assertEquals("", m3.getAction().getScript().getBody());

	parseMessage(m3, " 1.2 : var := func() ");
	assertEquals("var := func", m3.getAction().getScript().getBody());

	parseMessage(m3, " 1.2 : var = func() ");
	assertEquals("var := func", m3.getAction().getScript().getBody());

	parseMessage(m3, "1.2:var2:=func2()");
	assertEquals("var2 := func2", m3.getAction().getScript().getBody());

	parseMessage(m3, " 1.2 : var, var2, var3 := func() ");
	assertEquals("var, var2, var3 := func",
		     m3.getAction().getScript().getBody());

	parseMessage(m3, "1.2 : load_the_accumulating_taxes");

	/* TRY SOME ACTION ERRORS */

	checkParseException(m3, "1.2 : func() ()");
	checkParseException(m3, "1.2 : func() foo()");
	checkParseException(m3, "1.2 : func(), foo()");
	checkParseException(m3, "1.2 : var() = func()");
	checkParseException(m3, "1.2 : var = func(), foo()");
	checkParseException(m3, "1.2 : func() foo()");
	checkParseException(m3, "1.2 : var = ()");
	checkParseException(m3, "1.2 : var = () foo");
	checkParseException(m3, "1.2 : var = (foo(), bar())");
	checkParseException(m3, "1.2 : func(");
	checkParseException(m3, "1.2 : func(a, b");

	/* TRY THE PREDECESSORS */

	checkParseException(m3, "1.2.1 / 1.2 :");

	MMessage m4 =
	    fact.getCollaborations().buildMessage(inter, r3_4);
	MMessage m5 =
	    fact.getCollaborations().buildMessage(inter, r4_5);
	MMessage m6 =
	    fact.getCollaborations().buildMessage(inter, r5_3);
	MMessage m7 =
	    fact.getCollaborations().buildMessage(inter, r3_1);

	checkParseException(m6, "1.2.2 :");

	parseMessage(m7, "2:");
	assertTrue(m7.getSender() == cl1);
	assertTrue(m7.getReceiver() == cl3);
	assertNull(m7.getActivator());
	assertTrue(m7.getPredecessors().iterator().next() == m1);
	assertTrue(m7.getPredecessors().size() == 1);

	parseMessage(m7, "1.2.1.1.1.1:");
	assertTrue(m7.getSender() == cl3);
	assertTrue(m7.getReceiver() == cl1);
	assertTrue(m7.getActivator() == m6);
	assertTrue(m7.getPredecessors().size() == 0);

	assertNull(m1.getActivator());
	assertTrue(m1.getPredecessors().size() == 0);
	assertTrue(m4.getActivator() == m3);
	assertTrue(m4.getPredecessors().size() == 0);
	assertTrue(m3.getActivator() == m1);
	assertTrue(m3.getPredecessors().size() == 1);

	parseMessage(m7, "1.2.1 / 1.2.1.1.1.1:");
	assertTrue(m7.getPredecessors().iterator().next() == m4);
	assertTrue(m7.getPredecessors().size() == 1);

	parseMessage(m7, "1.2.1, 1.2.1.1 / 1.2.1.1.1.1:");
	boolean pre1 = false;
	boolean pre2 = false;
	it = m7.getPredecessors().iterator();
	m = (MMessage) it.next();
	if (m == m4)
	    pre1 = true;
	else if (m == m5)
	    pre2 = true;
	else
	    assertTrue("Strange message found", false);
	m = (MMessage) it.next();
	if (m == m4)
	    pre1 = true;
	else if (m == m5)
	    pre2 = true;
	else
	    assertTrue("Strange message found", false);
	assertTrue(pre1);
	assertTrue(pre2);
	assertTrue(!it.hasNext());

	/* TRY SOME PREDECESSOR ERRORS */
	checkParseException(m2, "1.2 / 1.1 :");
	checkParseException(m2, "1.2.1 / 1.1 :");
	checkParseException(m3, "1.2.1 / 1.2 :");
    }

    private void parseMessage(MMessage m, String s)
	throws ParseException
    {
	ParserDisplay.SINGLETON.parseMessage(m, s);
    }

    private void checkParseException(MMessage m, String s)
    {
	try {
	    ParserDisplay.SINGLETON.parseMessage(m, s);
	    assertTrue("Didn't throw for \"" + s + "\" in " + m, false);
	} catch (ParseException pe) {
	    // The expected exception is thrown.
	}
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
	super.setUp();
	ArgoSecurityManager.getInstance().setAllowExit(true);
    }
}
