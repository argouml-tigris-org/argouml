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
import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;

import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsFactory;
import org.argouml.model.uml.foundation.extensionmechanisms.ExtensionMechanismsHelper;

import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

public class TestParserDisplay extends TestCase {
	private final String attr01 = "name";
	private final String attr02 = "+name";
	private final String attr03 = "-name : void";
	private final String attr04 = "#name [1..1] : int {a=b}";
	private final String attr05 = "public name {a=b, c = d } : [1..*] int = 0";
	private final String attr06 =
		"private name {a=b, c = d } [*..*] : int = 15 {frozen}";
	private final String attr07 = "+name : String = \'val[15] \'";
	private final String attr08 = "  + name : String = \"a <<string>>\"";
	private final String attr09 = "+name : String = (a * (b+c) - d)";
	private final String attr10 =
		"+name << attrstereo1 >> : String = 2 * (b+c) - 10";
	private final String attr11 = "<<attrstereo2>> +name : String = a[15]";
	private final String attr12 = "+ name : String = a << 5";

	private final String nattr01 = "too many string in an attribute";
	private final String nattr02 = "+vis name";
	private final String nattr03 = "vis name : type : type";
	private final String nattr04 = "vis name = 0 = 1";
	private final String nattr05 = "vis name [1..1] [1..1]";
	private final String nattr06 = "vis name [1..1";
	private final String nattr07 = "vis name { a = b, cv = ";
	private final String nattr08 = "vis \"name\"";
	private final String nattr09 = "\"vis\" name";
	private final String nattr10 = "vis (name)";
	private final String nattr11 = "(vis) name";
	private final String nattr12 = "vis name : \"type\"";
	private final String nattr13 = "vis name : (type)";

	private final String oper01 = "name()";
	private final String oper02 =
		"<<opstereo1>> -name(in foo: float = 0) {root, abstract = false} : int";
	private final String oper03 =
		"<< opstereo2 >> protected name2(out foo: double = 0., inout bar = \"\"some\"\":String) {leaf,query} : String";
	private final String oper04 = "<<>> # name2()";

	private final String noper01 = "name(";
	private final String noper02 = "\"name\"()";
	private final String noper03 = "\"vis\" name()";
	private final String noper04 = "\'name\'()";
	private final String noper05 = "\'vis\' name()";
	private final String noper06 = "(name)()";
	private final String noper07 = "(vis) name()";
	private final String noper08 = "name() : \"type\"";
	private final String noper09 = "name() : \'type\'";
	private final String noper10 = "name() : (type)";

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

	public void testAttributeName() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkName(attr, attr01, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkName(attr, attr02, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkName(attr, attr03, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkName(attr, attr04, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkName(attr, attr05, "name");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkName(attr, attr06, "name");
	}

	public void testAttributeType() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkType(attr, attr03, "void");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkType(attr, attr04, "int");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkType(attr, attr05, "int");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkType(attr, attr06, "int");
	}

	public void testAttributeVisibility() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkVisibility(attr, attr02, "public");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkVisibility(attr, attr03, "private");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkVisibility(attr, attr04, "protected");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkVisibility(attr, attr05, "public");
		checkVisibility(attr, attr01, "public");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkVisibility(attr, attr06, "private");
		checkVisibility(attr, attr01, "private");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkVisibility(attr, attr08, "public");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkVisibility(attr, attr11, "public");
	}

	public void testAttributeProperty() {
		MAttribute attr;
		String res1[] = { "a", "b" };
		String res2[] = { "a", "b", "c", "d" };
		String res3[] = { "a", "b", "c", "d", "frozen", null };

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkProperties(attr, attr04, res1);

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkProperties(attr, attr05, res2);

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkProperties(attr, attr06, res3);
	}

	public void testAttributeMultiplicity() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkMultiplicity(attr, attr04, new MMultiplicity("1..1"));

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkMultiplicity(attr, attr05, new MMultiplicity("1..*"));

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkMultiplicity(attr, attr06, new MMultiplicity("*..*"));
	}

	public void testAttributeParseExceptions() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkThrows(attr, nattr01, true, false, false);
		checkThrows(attr, nattr02, true, false, false);
		checkThrows(attr, nattr03, true, false, false);
		checkThrows(attr, nattr04, true, false, false);
		checkThrows(attr, nattr05, true, false, false);
		checkThrows(attr, nattr06, true, false, false);
		checkThrows(attr, nattr07, true, false, false);
		checkThrows(attr, nattr08, true, false, false);
		checkThrows(attr, nattr09, true, false, false);
		checkThrows(attr, nattr10, true, false, false);
		checkThrows(attr, nattr11, true, false, false);
		checkThrows(attr, nattr12, true, false, false);
		checkThrows(attr, nattr13, true, false, false);
	}

	public void testAttributeValue() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkValue(attr, attr05, "0");
		checkValue(attr, attr01, "0");
		checkValue(attr, attr06, "15");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkValue(attr, attr07, "\'val[15] \'");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkValue(attr, attr08, "\"a <<string>>\"");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkValue(attr, attr09, "(a * (b+c) - d)");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkValue(attr, attr10, "2 * (b+c) - 10");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkValue(attr, attr11, "a[15]");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkValue(attr, attr12, "a << 5");
	}

	private void softAddStereotype(String name, MModelElement elem) {
		Iterator it =
			ExtensionMechanismsHelper
				.getHelper()
				.getStereotypes(
					ProjectManager.getManager().getCurrentProject().getModel())
				.iterator();
		while (it.hasNext()) {
			MStereotype s = (MStereotype) it.next();
			if (name.equals(s.getName()))
				return;
		}
		ExtensionMechanismsFactory.getFactory().buildStereotype(
			elem,
			name,
			ProjectManager.getManager().getCurrentProject().getModel());
	}

	public void testAttributeStereotype() {
		MAttribute attr;

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		softAddStereotype("attrstereo1", attr);
		softAddStereotype("attrstereo2", attr);

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkStereotype(attr, attr01, null);

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkStereotype(attr, attr10, "attrstereo1");

		attr = UmlFactory.getFactory().getCore().buildAttribute();
		attr.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());
		checkStereotype(attr, attr11, "attrstereo2");
		checkStereotype(attr, attr01, "attrstereo2");
	}

	public void testOperationName() {
		MOperation op;
		MClass cl = UmlFactory.getFactory().getCore().buildClass();
		cl.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkName(op, oper01, "name");

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkName(op, oper02, "name");

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkName(op, oper03, "name2");
	}

	public void testOperationType() {
		MOperation op;
		MClass cl = UmlFactory.getFactory().getCore().buildClass();
		cl.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkType(op, oper01, "void");

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkType(op, oper02, "int");
		checkType(op, oper01, "int");

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkType(op, oper03, "String");
	}

	public void testOperationVisibility() {
		MOperation op;
		MClass cl = UmlFactory.getFactory().getCore().buildClass();
		cl.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkVisibility(op, oper01, "public");

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkVisibility(op, oper02, "private");

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkVisibility(op, oper03, "protected");
		checkVisibility(op, oper01, "protected");
		checkVisibility(op, oper02, "private");

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkVisibility(op, oper04, "protected");
	}

	public void testOperationParameters() {
		MOperation op;
		MClass cl = UmlFactory.getFactory().getCore().buildClass();
		cl.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());

		String res1[] = {
		};
		String res2[] = { "in", "foo", "float", "0" };
		String res3[] =
			{
				"out",
				"foo",
				"double",
				"0.",
				"inout",
				"bar",
				"String",
				"\"\"some\"\"" };

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkParameters(op, oper01, res1);

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkParameters(op, oper02, res2);

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkParameters(op, oper03, res3);
		checkParameters(op, oper01, res1);
		checkParameters(op, oper02, res2);
	}

	public void testOperationProperties() {
		MOperation op;
		MClass cl = UmlFactory.getFactory().getCore().buildClass();
		cl.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());

		String res1[] =
			{
				"abstract",
				null,
				"concurrency",
				null,
				"concurrent",
				null,
				"guarded",
				null,
				"leaf",
				null,
				"query",
				null,
				"root",
				null,
				"sequential",
				null };

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkProperties(op, oper01, res1);

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkProperties(op, oper02, res1);

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkProperties(op, oper03, res1);
	}

	public void testOperationStereotype() {
		MOperation op;
		MClass cl = UmlFactory.getFactory().getCore().buildClass();
		cl.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		softAddStereotype("opstereo1", op);
		softAddStereotype("opstereo2", op);

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkStereotype(op, oper01, null);

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkStereotype(op, oper02, "opstereo1");

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkStereotype(op, oper03, "opstereo2");
		checkStereotype(op, oper01, "opstereo2");
		checkStereotype(op, oper04, null);
	}

	public void testOperationParseExceptions() {
		MOperation op;
		MClass cl = UmlFactory.getFactory().getCore().buildClass();
		cl.setNamespace(
			ProjectManager.getManager().getCurrentProject().getModel());

		op = UmlFactory.getFactory().getCore().buildOperation(cl);
		checkThrows(op, noper01, true, false, false);
		checkThrows(op, noper02, true, false, false);
		checkThrows(op, noper03, true, false, false);
		checkThrows(op, noper04, true, false, false);
		checkThrows(op, noper05, true, false, false);
		checkThrows(op, noper06, true, false, false);
		checkThrows(op, noper07, true, false, false);
		checkThrows(op, noper08, true, false, false);
		checkThrows(op, noper09, true, false, false);
		checkThrows(op, noper10, true, false, false);
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
	/* (non-Javadoc)
	     * @see junit.framework.TestCase#setUp()
	     */
	protected void setUp() throws Exception {
		super.setUp();
		ArgoSecurityManager.getInstance().setAllowExit(true);
	}
}
