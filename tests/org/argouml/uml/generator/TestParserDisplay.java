// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;

import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;

public class TestParserDisplay extends TestCase {
	private final String clro01 = "/ roname : int";
	private final String clro02 = " : int , double / roname2 ";
	private final String clro03 = ":float,long/roname";

	private final String nclro01 = "/ roname : int / roname2 ";
	private final String nclro02 = "oname1 oname2 / roname : int , double";
	private final String nclro03 = "/ roname roname2 : int ";
	private final String nclro04 = "/ roname : int double ";

	public TestParserDisplay(String str) {
		super(str);
	}

	public void TestClassifierRoleObjectName() {
		/* Not implemented in ParserDisplay */
	}

	public void TestClassifierRoleName() {
		MClassifierRole cr;

		cr = UmlFactory.getFactory().getCollaborations().createClassifierRole();
		checkName(cr, clro01, "roname");
		checkName(cr, clro02, "roname2");

		cr = UmlFactory.getFactory().getCollaborations().createClassifierRole();
		checkName(cr, clro03, "roname");
	}

	public void TestClassifierRoleBases() {
		MClassifierRole cr;
		String res1[] = { "int" };
		String res2[] = { "int", "double" };
		String res3[] = { "float", "long" };

		cr = UmlFactory.getFactory().getCollaborations().createClassifierRole();
		checkBases(cr, clro01, res1);
		checkBases(cr, clro02, res2);
		checkBases(cr, clro03, res3);

		cr = UmlFactory.getFactory().getCollaborations().createClassifierRole();
		checkBases(cr, clro03, res3);
	}

	public void TestClassifierRoleThrows() {
		MClassifierRole cr;

		cr = UmlFactory.getFactory().getCollaborations().createClassifierRole();
		checkThrows(cr, nclro01, true, false, false);
		checkThrows(cr, nclro02, true, false, false);
		checkThrows(cr, nclro03, true, false, false);
		checkThrows(cr, nclro04, true, false, false);
	}

	private void checkName(MAttribute attr, String text, String name) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assertTrue(
				text + " gave wrong name: " + attr.getName(),
				name.equals(attr.getName()));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkName(MOperation op, String text, String name) {
		try {
			ParserDisplay.SINGLETON.parseOperation(text, op);
			assertTrue(
				text + " gave wrong name: " + op.getName() + " != " + name,
				name.equals(op.getName()));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkName(MClassifierRole ro, String text, String name) {
		try {
			ParserDisplay.SINGLETON.parseClassifierRole(ro, text);
			assertTrue(
				text + " gave wrong name: " + ro.getName() + " != " + name,
				name.equals(ro.getName()));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkType(MAttribute attr, String text, String type) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assertTrue(
				text
					+ " gave wrong type: "
					+ (attr.getType() == null
						? "(null)"
						: attr.getType().getName()),
				attr.getType() != null
					&& type.equals(attr.getType().getName()));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkType(MOperation op, String text, String type) {
		try {
			ParserDisplay.SINGLETON.parseOperation(text, op);
			Collection ret =
				UmlHelper.getHelper().getCore().getReturnParameters(op);
			Iterator it = ret.iterator();
			assertTrue(
				text + " gave extra return value",
				!(type == null && it.hasNext()));
			assertTrue(
				text + " lacks return value",
				!(type != null && !it.hasNext()));
			if (it.hasNext()) {
				MParameter p = (MParameter) it.next();
				assertTrue(
					text + " gave wrong return",
					(type == null && p.getType() == null)
						|| (type != null && type.equals(p.getType().getName())));
			}
			assertTrue(text + " gave extra return value", !it.hasNext());
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkParameters(MOperation op, String text, String params[]) {
		int i;

		try {
			ParserDisplay.SINGLETON.parseOperation(text, op);
			Collection prm = op.getParameters();
			Iterator it = prm.iterator();
			assertTrue(
				text + " lacks parameters",
				!(params.length > 0 && !it.hasNext()));
			for (i = 0; i + 3 < params.length; i += 4) {
				MParameter p;
				do {
					assertTrue(text + " lacks parameters", it.hasNext());
					p = (MParameter) it.next();
				} while (p.getKind().equals(MParameterDirectionKind.RETURN));
				assertTrue(
					text + "gave wrong inout in parameter " + (i / 4),
					params[i].equals(p.getKind().getName()));
				assertTrue(
					text + "gave wrong name in parameter " + (i / 4),
					params[i + 1].equals(p.getName()));
				assertTrue(
					text + "gave wrong type in parameter " + (i / 4),
					params[i + 2].equals(p.getType().getName()));
				assertTrue(
					text + "gave wrong default value in parameter " + (i / 4),
					(params[i + 3] == null && p.getDefaultValue() == null)
						|| (params[i + 3] != null && p.getDefaultValue() != null)
						&& params[i + 3].equals(p.getDefaultValue().getBody()));
			}
			while (it.hasNext()) {
				MParameter p = (MParameter) it.next();
				assertTrue(
					text + " gave extra parameters",
					p.getKind().equals(MParameterDirectionKind.RETURN));
			}
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkVisibility(MAttribute attr, String text, String vis) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assertTrue(
				text
					+ " gave wrong visibility: "
					+ (attr.getVisibility() == null
						? "(null)"
						: attr.getVisibility().getName()),
				attr.getVisibility() != null
					&& vis.equals(attr.getVisibility().getName()));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkVisibility(MOperation op, String text, String vis) {
		try {
			ParserDisplay.SINGLETON.parseOperation(text, op);
			assertTrue(
				text
					+ " gave wrong visibility: "
					+ (op.getVisibility() == null
						? "(null)"
						: op.getVisibility().getName()),
				op.getVisibility() != null
					&& vis.equals(op.getVisibility().getName()));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkProperties(
		MAttribute attr,
		String text,
		String props[]) {
		int i;
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			for (i = 0; i + 1 < props.length; i += 2) {
				if (props[i + 1] == null)
					assertTrue(
						"TaggedValue " + props[i] + " exists!",
						attr.getTaggedValue(props[i]) == null);
				else
					assertTrue(
						"TaggedValue " + props[i] + " wrong!",
						props[i + 1].equals(attr.getTaggedValue(props[i])));
			}
		} catch (Exception e) {
			assertTrue(text + " threw Exception " + e, false);
		}
	}

	private void checkProperties(MOperation op, String text, String props[]) {
		int i;
		try {
			ParserDisplay.SINGLETON.parseOperation(text, op);
			for (i = 0; i + 1 < props.length; i += 2) {
				if (props[i + 1] == null)
					assertTrue(
						"TaggedValue " + props[i] + " exists!",
						op.getTaggedValue(props[i]) == null);
				else
					assertTrue(
						"TaggedValue " + props[i] + " wrong!",
						props[i + 1].equals(op.getTaggedValue(props[i])));
			}
		} catch (Exception e) {
			assertTrue(text + " threw Exception " + e, false);
		}
	}

	private void checkMultiplicity(
		MAttribute attr,
		String text,
		MMultiplicity mult) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assertTrue(
				text
					+ " gave wrong multiplicity: "
					+ (attr.getMultiplicity() == null
						? "(null)"
						: attr.getMultiplicity().toString()),
				mult == null
					&& attr.getMultiplicity() == null
					|| mult != null
					&& mult.equals(attr.getMultiplicity()));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkThrows(
		MAttribute attr,
		String text,
		boolean prsEx,
		boolean ex2,
		boolean ex3) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assertTrue("didn't throw for " + text, false);
		} catch (ParseException pe) {
			assertTrue(text + " threw ParseException " + pe, prsEx);
		} catch (Exception e) {
			assertTrue(text + " threw Exception " + e, !prsEx);
		}
	}

	private void checkThrows(
		MOperation op,
		String text,
		boolean prsEx,
		boolean ex2,
		boolean ex3) {
		try {
			ParserDisplay.SINGLETON.parseOperation(text, op);
			assertTrue("didn't throw for " + text, false);
		} catch (ParseException pe) {
			assertTrue(text + " threw ParseException " + pe, prsEx);
		} catch (Exception e) {
			assertTrue(text + " threw Exception " + e, !prsEx);
		}
	}

	private void checkThrows(
		MClassifierRole ro,
		String text,
		boolean prsEx,
		boolean ex2,
		boolean ex3) {
		try {
			ParserDisplay.SINGLETON.parseClassifierRole(ro, text);
			assertTrue("didn't throw for " + text, false);
		} catch (ParseException pe) {
			assertTrue(text + " threw ParseException " + pe, prsEx);
		} catch (Exception e) {
			assertTrue(text + " threw Exception " + e, !prsEx);
		}
	}

	private void checkValue(MAttribute attr, String text, String val) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assertTrue(
				text
					+ " gave wrong visibility: "
					+ (attr.getInitialValue() == null
						? "(null)"
						: attr.getInitialValue().getBody()),
				val == null
					&& (attr.getInitialValue() == null
						|| "".equals(attr.getInitialValue().getBody()))
					|| val != null
					&& attr.getInitialValue() != null
					&& val.equals(attr.getInitialValue().getBody()));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkStereotype(MAttribute attr, String text, String val) {
		try {
			ParserDisplay.SINGLETON.parseAttribute(text, attr);
			assertTrue(
				text
					+ " gave wrong stereotype "
					+ (attr.getStereotype() != null
						? attr.getStereotype().getName()
						: "(null)"),
				(val == null && attr.getStereotype() == null)
					|| (val != null
						&& attr.getStereotype() != null
						&& val.equals(attr.getStereotype().getName())));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkStereotype(MOperation op, String text, String val) {
		try {
			ParserDisplay.SINGLETON.parseOperation(text, op);
			assertTrue(
				text
					+ " gave wrong stereotype "
					+ (op.getStereotype() != null
						? op.getStereotype().getName()
						: "(null)"),
				(val == null && op.getStereotype() == null)
					|| (val != null
						&& op.getStereotype() != null
						&& val.equals(op.getStereotype().getName())));
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

	private void checkBases(MClassifierRole cr, String text, String bases[]) {
		int i;
		Collection c;
		Iterator it;
		MClassifier cls;

		try {
			ParserDisplay.SINGLETON.parseClassifierRole(cr, text);
			c = cr.getBases();
			it = c.iterator();
			checkAllValid : while (it.hasNext()) {
				cls = (MClassifier) it.next();
				for (i = 0; i < bases.length; i++)
					if (bases[i].equals(cls.getName()))
						continue checkAllValid;
				assertTrue(
					"Base "
						+ cls.getName()
						+ " falsely "
						+ "generated by "
						+ text,
					false);
			}

			checkAllExist : for (i = 0; i < bases.length; i++) {
				it = c.iterator();
				while (it.hasNext()) {
					cls = (MClassifier) it.next();
					if (bases[i].equals(cls.getName()))
						continue checkAllExist;
				}
				assertTrue(
					"Base " + bases[i] + " was not " + "generated by " + text,
					false);
			}
		} catch (Exception e) {
			assertTrue(text + " threw unexpectedly: " + e, false);
		}
	}

    /**
     * Dummy test.
     */
    public void testDummy() { }

    /**
     * @see junit.framework.TestCase#setUp()
     */
	protected void setUp() throws Exception {
		super.setUp();
		ArgoSecurityManager.getInstance().setAllowExit(true);
	}
}
