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

public class TestParseMessage extends TestCase {
	public TestParseMessage(String str) {
		super(str);
	}

	public void testParseMessage() {
		MMessage m;
		Iterator it;

		MCollaboration coll =
			UmlFactory.getFactory().getCollaborations().createCollaboration();
		MInteraction inter =
			UmlFactory.getFactory().getCollaborations().buildInteraction(coll);

		MClassifierRole cl1 =
			UmlFactory.getFactory().getCollaborations().createClassifierRole();
		MClassifierRole cl2 =
			UmlFactory.getFactory().getCollaborations().createClassifierRole();
		MClassifierRole cl3 =
			UmlFactory.getFactory().getCollaborations().createClassifierRole();
		MClassifierRole cl4 =
			UmlFactory.getFactory().getCollaborations().createClassifierRole();
		MClassifierRole cl5 =
			UmlFactory.getFactory().getCollaborations().createClassifierRole();
		cl1.setNamespace(coll);
		cl2.setNamespace(coll);
		cl3.setNamespace(coll);
		cl4.setNamespace(coll);
		cl5.setNamespace(coll);

		MAssociationRole r1_2 =
			UmlFactory.getFactory().getCollaborations().buildAssociationRole(
				cl1,
				cl2);
		MAssociationRole r2_3 =
			UmlFactory.getFactory().getCollaborations().buildAssociationRole(
				cl2,
				cl3);
		MAssociationRole r3_4 =
			UmlFactory.getFactory().getCollaborations().buildAssociationRole(
				cl3,
				cl4);
		MAssociationRole r4_5 =
			UmlFactory.getFactory().getCollaborations().buildAssociationRole(
				cl4,
				cl5);
		MAssociationRole r3_1 =
			UmlFactory.getFactory().getCollaborations().buildAssociationRole(
				cl3,
				cl1);
		MAssociationRole r5_3 =
			UmlFactory.getFactory().getCollaborations().buildAssociationRole(
				cl5,
				cl3);

		/* START TESTING STUFF */

		MMessage m1 =
			UmlFactory.getFactory().getCollaborations().buildMessage(
				inter,
				r1_2);
		assertTrue(m1.getSender() == cl1);
		assertTrue(m1.getReceiver() == cl2);
		assertTrue(m1.getInteraction() == inter);
		assertTrue(m1.getActivator() == null);
		assertTrue(
			m1.getAction() == null || m1.getAction().getRecurrence() == null);
		assertTrue(m1.getPredecessors().size() == 0);
		m1.setName("m1");

		MMessage m2 =
			UmlFactory.getFactory().getCollaborations().buildMessage(
				inter,
				r2_3);
		assertTrue(m2.getSender() == cl2);
		assertTrue(m2.getReceiver() == cl3);
		assertTrue(m2.getActivator() == m1);
		assertTrue(
			m2.getAction() == null || m2.getAction().getRecurrence() == null);
		assertTrue(m2.getPredecessors().size() == 0);
		m2.setName("m2");

		MMessage m3 =
			UmlFactory.getFactory().getCollaborations().buildMessage(
				inter,
				r2_3);
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
		assertTrue(
			m3.getAction() != null && m3.getAction().getRecurrence() != null);
		assertTrue("[x < 5]".equals(m3.getAction().getRecurrence().getBody()));

		parseMessage(m3, " 1.2 * [ i = 1..10 ] : ");
		assertTrue(
			"*[i = 1..10]".equals(m3.getAction().getRecurrence().getBody()));
		parseMessage(m3, " 1.2 *// : ");
		assertTrue(
			"*[i = 1..10]".equals(m3.getAction().getRecurrence().getBody()));

		parseMessage(m3, " * // [i=1..] 1.2 : ");
		assertTrue(
			"*//[i=1..]".equals(m3.getAction().getRecurrence().getBody()));
		parseMessage(m3, " 1.2 : ");
		assertTrue(
			"*//[i=1..]".equals(m3.getAction().getRecurrence().getBody()));

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
		assertTrue("func".equals(m3.getAction().getScript().getBody()));
		parseMessage(m3, " 1.2 ");
		assertTrue("func".equals(m3.getAction().getScript().getBody()));
		parseMessage(m3, " 1.2 : ");
		assertTrue("".equals(m3.getAction().getScript().getBody()));

		parseMessage(m3, " 1.2 : var := func() ");
		assertTrue("var := func".equals(m3.getAction().getScript().getBody()));

		parseMessage(m3, " 1.2 : var = func() ");
		assertTrue("var := func".equals(m3.getAction().getScript().getBody()));

		parseMessage(m3, "1.2:var2:=func2()");
		assertTrue(
			"var2 := func2".equals(m3.getAction().getScript().getBody()));

		parseMessage(m3, " 1.2 : var, var2, var3 := func() ");
		assertTrue(
			"var, var2, var3 := func".equals(
				m3.getAction().getScript().getBody()));

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
			UmlFactory.getFactory().getCollaborations().buildMessage(
				inter,
				r3_4);
		MMessage m5 =
			UmlFactory.getFactory().getCollaborations().buildMessage(
				inter,
				r4_5);
		MMessage m6 =
			UmlFactory.getFactory().getCollaborations().buildMessage(
				inter,
				r5_3);
		MMessage m7 =
			UmlFactory.getFactory().getCollaborations().buildMessage(
				inter,
				r3_1);

		checkParseException(m6, "1.2.2 :");

		parseMessage(m7, "2:");
		assertTrue(m7.getSender() == cl1);
		assertTrue(m7.getReceiver() == cl3);
		assertTrue(m7.getActivator() == null);
		assertTrue(
			m7.getPredecessors().iterator().next() == m1
				&& m7.getPredecessors().size() == 1);

		parseMessage(m7, "1.2.1.1.1.1:");
		assertTrue(m7.getSender() == cl3);
		assertTrue(m7.getReceiver() == cl1);
		assertTrue(m7.getActivator() == m6);
		assertTrue(m7.getPredecessors().size() == 0);

		assertTrue(m1.getActivator() == null);
		assertTrue(m1.getPredecessors().size() == 0);
		assertTrue(m4.getActivator() == m3);
		assertTrue(m4.getPredecessors().size() == 0);
		assertTrue(m3.getActivator() == m1);
		assertTrue(m3.getPredecessors().size() == 1);

		parseMessage(m7, "1.2.1 / 1.2.1.1.1.1:");
		assertTrue(
			m7.getPredecessors().iterator().next() == m4
				&& m7.getPredecessors().size() == 1);

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
		assertTrue(pre1 && pre2 && !it.hasNext());

		/* TRY SOME PREDECESSOR ERRORS */
		checkParseException(m2, "1.2 / 1.1 :");
		checkParseException(m2, "1.2.1 / 1.1 :");
		checkParseException(m3, "1.2.1 / 1.2 :");
	}

	private void parseMessage(MMessage m, String s) {
		try {
			ParserDisplay.SINGLETON.parseMessage(m, s);
		} catch (ParseException pe) {
			assertTrue("Unexpected throw: " + pe, false);
		}
	}

	private void checkParseException(MMessage m, String s) {
		try {
			ParserDisplay.SINGLETON.parseMessage(m, s);
			assertTrue("Didn't throw for \"" + s + "\" in " + m, false);
		} catch (ParseException pe) {
			assertTrue("Did throw", true);
		} catch (Exception e) {
			assertTrue("Unexpected throw " + e, false);
		}
	}
	/* (non-Javadoc)
	     * @see junit.framework.TestCase#setUp()
	     */
	protected void setUp() throws Exception {
		super.setUp();
		ArgoSecurityManager.getInstance().setAllowExit(true);
	}
}
