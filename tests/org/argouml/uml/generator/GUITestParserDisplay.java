// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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
import org.argouml.model.uml.foundation.extensionmechanisms.*;

import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;

/**
 * Testing the ParserDisplay class.<p>
 *
 * For some mysterious reason these tests depend on the ProjectBrowser
 * which means that they cannot be run in Headless mode.
 */
public class GUITestParserDisplay extends TestCase {
    private static final String ATTR01 = "name";
    private static final String ATTR02 = "+name";
    private static final String ATTR03 = "-name : void";
    private static final String ATTR04 = "#name [1..1] : int {a=b}";
    private static final String ATTR05 = 
	"public name {a=b, c = d } : [1..*] int = 0";
    private static final String ATTR06 =
	"private name {a=b, c = d } [*..*] : int = 15 {frozen}";
    private static final String ATTR07 = "+name : String = \'val[15] \'";
    private static final String ATTR08 = 
	"  + name : String = \"a <<string>>\"";
    private static final String ATTR09 = "+name : String = (a * (b+c) - d)";
    private static final String ATTR10 =
	"+name << attrstereo1 >> : String = 2 * (b+c) - 10";
    private static final String ATTR11 =
	"<<attrstereo2>> +name : String = a[15]";
    private static final String ATTR12 = "+ name : String = a << 5";

    private static final String NATTR01 = "too many string in an attribute";
    private static final String NATTR02 = "+vis name";
    private static final String NATTR03 = "vis name : type : type";
    private static final String NATTR04 = "vis name = 0 = 1";
    private static final String NATTR05 = "vis name [1..1] [1..1]";
    private static final String NATTR06 = "vis name [1..1";
    private static final String NATTR07 = "vis name { a = b, cv = ";
    private static final String NATTR08 = "vis \"name\"";
    private static final String NATTR09 = "\"vis\" name";
    private static final String NATTR10 = "vis (name)";
    private static final String NATTR11 = "(vis) name";
    private static final String NATTR12 = "vis name : \"type\"";
    private static final String NATTR13 = "vis name : (type)";

    private static final String OPER01 = "name()";
    private static final String OPER02 =
	"<<opstereo1>> -name(in foo: float = 0) "
	+ "{root, abstract = false} : int";
    private static final String OPER03 =
	"<< opstereo2 >> protected name2("
	+ "out foo: double = 0., inout bar = \"\"some\"\":String) "
	+ "{leaf,query} : String";
    private static final String OPER04 = "<<>> # name2()";

    private static final String NOPER01 = "name(";
    private static final String NOPER02 = "\"name\"()";
    private static final String NOPER03 = "\"vis\" name()";
    private static final String NOPER04 = "\'name\'()";
    private static final String NOPER05 = "\'vis\' name()";
    private static final String NOPER06 = "(name)()";
    private static final String NOPER07 = "(vis) name()";
    private static final String NOPER08 = "name() : \"type\"";
    private static final String NOPER09 = "name() : \'type\'";
    private static final String NOPER10 = "name() : (type)";


    /**
     * @see junit.framework.TestCase#TestCase()
     */
    public GUITestParserDisplay(String str) {
	super(str);
    }

    /**
     * Test the parsing of an attribute name.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeName()
	throws ParseException
    {
	MAttribute attr;

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkName(attr, ATTR01, "name");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkName(attr, ATTR02, "name");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkName(attr, ATTR03, "name");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkName(attr, ATTR04, "name");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkName(attr, ATTR05, "name");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkName(attr, ATTR06, "name");
    }


    /**
     * Test the parsing of an attribute's type.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeType()
	throws ParseException
    {
	MAttribute attr;

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkType(attr, ATTR03, "void");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkType(attr, ATTR04, "int");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkType(attr, ATTR05, "int");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkType(attr, ATTR06, "int");
    }

    /**
     * Test the parsing of the attribute's visibility.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeVisibility()
	throws ParseException
    {
	MAttribute attr;

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkVisibility(attr, ATTR02, "public");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkVisibility(attr, ATTR03, "private");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkVisibility(attr, ATTR04, "protected");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkVisibility(attr, ATTR05, "public");
	checkVisibility(attr, ATTR01, "public");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkVisibility(attr, ATTR06, "private");
	checkVisibility(attr, ATTR01, "private");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkVisibility(attr, ATTR08, "public");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkVisibility(attr, ATTR11, "public");
    }

    /**
     * Test the parsing of an attribute's property.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeProperty()
	throws ParseException
    {
	MAttribute attr;
	String res1[] = {
	    "a", "b" 
	};
	String res2[] = {
	    "a", "b", "c", "d"
	};
	String res3[] = {
	    "a", "b", "c", "d", "frozen", null 
	};

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkProperties(attr, ATTR04, res1);

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkProperties(attr, ATTR05, res2);

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkProperties(attr, ATTR06, res3);
    }

    /**
     * Test parsing an attribute's multiplicity.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeMultiplicity()
	throws ParseException
    {
	MAttribute attr;

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkMultiplicity(attr, ATTR04, new MMultiplicity("1..1"));

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkMultiplicity(attr, ATTR05, new MMultiplicity("1..*"));

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkMultiplicity(attr, ATTR06, new MMultiplicity("*..*"));
    }

    /**
     * Test that the parser throws the correct exceptions.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeParseExceptions()
	throws ParseException
    {
	MAttribute attr;

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkThrows(attr, NATTR01, true, false, false);
	checkThrows(attr, NATTR02, true, false, false);
	checkThrows(attr, NATTR03, true, false, false);
	checkThrows(attr, NATTR04, true, false, false);
	checkThrows(attr, NATTR05, true, false, false);
	checkThrows(attr, NATTR06, true, false, false);
	checkThrows(attr, NATTR07, true, false, false);
	checkThrows(attr, NATTR08, true, false, false);
	checkThrows(attr, NATTR09, true, false, false);
	checkThrows(attr, NATTR10, true, false, false);
	checkThrows(attr, NATTR11, true, false, false);
	checkThrows(attr, NATTR12, true, false, false);
	checkThrows(attr, NATTR13, true, false, false);
    }

    /**
     * Test the parsing of an attribute's value.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeValue()
	throws ParseException
    {
	MAttribute attr;

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkValue(attr, ATTR05, "0");
	checkValue(attr, ATTR01, "0");
	checkValue(attr, ATTR06, "15");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkValue(attr, ATTR07, "\'val[15] \'");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkValue(attr, ATTR08, "\"a <<string>>\"");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkValue(attr, ATTR09, "(a * (b+c) - d)");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkValue(attr, ATTR10, "2 * (b+c) - 10");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkValue(attr, ATTR11, "a[15]");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkValue(attr, ATTR12, "a << 5");
    }

    private void softAddStereotype(String name, MModelElement elem)
	throws ParseException
    {
	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	Iterator it =
	    ExtensionMechanismsHelper
	    .getHelper()
	    .getStereotypes(ns)
	    .iterator();
	while (it.hasNext()) {
	    MStereotype s = (MStereotype) it.next();
	    if (name.equals(s.getName()))
		return;
	}
	ExtensionMechanismsFactory.getFactory().buildStereotype(elem, 
								name,
								ns);
    }


    /**
     * Test the parsing of an attribute's stereotype.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeStereotype()
	throws ParseException
    {
	MAttribute attr;

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	softAddStereotype("attrstereo1", attr);
	softAddStereotype("attrstereo2", attr);

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkStereotype(attr, ATTR01, null);

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkStereotype(attr, ATTR10, "attrstereo1");

	attr = UmlFactory.getFactory().getCore().buildAttribute();
	attr.setNamespace(ns);

	checkStereotype(attr, ATTR11, "attrstereo2");
	checkStereotype(attr, ATTR01, "attrstereo2");
    }

    /**
     * Test the parsing of an operation's name.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationName()
	throws ParseException
    {
	MOperation op;
	MClass cl = UmlFactory.getFactory().getCore().buildClass();

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	cl.setNamespace(ns);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkName(op, OPER01, "name");

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkName(op, OPER02, "name");

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkName(op, OPER03, "name2");
    }

    /**
     * Test the parsing of an operation's type.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationType()
	throws ParseException
    {
	MOperation op;
	MClass cl = UmlFactory.getFactory().getCore().buildClass();

	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	cl.setNamespace(ns);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkType(op, OPER01, "void");

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkType(op, OPER02, "int");
	checkType(op, OPER01, "int");

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkType(op, OPER03, "String");
    }

    /**
     * Test the parsing of an operation's visibility.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationVisibility()
	throws ParseException
    {
	MOperation op;
	MClass cl = UmlFactory.getFactory().getCore().buildClass();
	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	cl.setNamespace(ns);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkVisibility(op, OPER01, "public");

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkVisibility(op, OPER02, "private");

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkVisibility(op, OPER03, "protected");
	checkVisibility(op, OPER01, "protected");
	checkVisibility(op, OPER02, "private");

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkVisibility(op, OPER04, "protected");
    }

    /**
     * Test the parsing of an operation's parameters.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationParameters()
	throws ParseException
    {
	MOperation op;
	MClass cl = UmlFactory.getFactory().getCore().buildClass();
	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	cl.setNamespace(ns);

	String res1[] = {
	};
	String res2[] = {
	    "in", "foo", "float", "0" 
	};
	String res3[] = {
	    "out",
	    "foo",
	    "double",
	    "0.",
	    "inout",
	    "bar",
	    "String",
	    "\"\"some\"\"" 
	};

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkParameters(op, OPER01, res1);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkParameters(op, OPER02, res2);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkParameters(op, OPER03, res3);
	checkParameters(op, OPER01, res1);
	checkParameters(op, OPER02, res2);
    }

    /**
     * Test the parsing of an operation's properties.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationProperties()
	throws ParseException
    {
	MOperation op;
	MClass cl = UmlFactory.getFactory().getCore().buildClass();
	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	cl.setNamespace(ns);

	String res1[] = {
	    "abstract", null,
	    "concurrency", null,
	    "concurrent", null,
	    "guarded", null,
	    "leaf", null,
	    "query", null,
	    "root", null,
	    "sequential", null 
	};

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkProperties(op, OPER01, res1);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkProperties(op, OPER02, res1);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkProperties(op, OPER03, res1);
    }

    /**
     * Test the parsing of an operation's stereotype.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationStereotype()
	throws ParseException
    {
	MOperation op;
	MClass cl = UmlFactory.getFactory().getCore().buildClass();
	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	cl.setNamespace(ns);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	softAddStereotype("opstereo1", op);
	softAddStereotype("opstereo2", op);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkStereotype(op, OPER01, null);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkStereotype(op, OPER02, "opstereo1");

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkStereotype(op, OPER03, "opstereo2");
	checkStereotype(op, OPER01, "opstereo2");
	checkStereotype(op, OPER04, null);
    }

    /**
     * Test the parsing of an operation's parse exceptions.
     */
    public void testOperationParseExceptions() {
	MOperation op;
	MClass cl = UmlFactory.getFactory().getCore().buildClass();
	MNamespace ns = 
	    ProjectManager.getManager().getCurrentProject().getModel();

	cl.setNamespace(ns);

	op = UmlFactory.getFactory().getCore().buildOperation(cl);
	checkThrows(op, NOPER01, true, false, false);
	checkThrows(op, NOPER02, true, false, false);
	checkThrows(op, NOPER03, true, false, false);
	checkThrows(op, NOPER04, true, false, false);
	checkThrows(op, NOPER05, true, false, false);
	checkThrows(op, NOPER06, true, false, false);
	checkThrows(op, NOPER07, true, false, false);
	checkThrows(op, NOPER08, true, false, false);
	checkThrows(op, NOPER09, true, false, false);
	checkThrows(op, NOPER10, true, false, false);
    }

    private void checkName(MAttribute attr, String text, String name) 
	throws ParseException
    {
	ParserDisplay.SINGLETON.parseAttribute(text, attr);
	assertTrue(text + " gave wrong name: " + attr.getName(),
		   name.equals(attr.getName()));
    }

    private void checkName(MOperation op, String text, String name)
	throws ParseException
    {
	ParserDisplay.SINGLETON.parseOperation(text, op);
	assertTrue(text + " gave wrong name: " + op.getName() + " != " + name,
		   name.equals(op.getName()));
    }

    private void checkName(MClassifierRole ro, String text, String name)
	throws ParseException
    {
	ParserDisplay.SINGLETON.parseClassifierRole(ro, text);
	assertTrue(text + " gave wrong name: " + ro.getName() + " != " + name,
		   name.equals(ro.getName()));
    }

    private void checkType(MAttribute attr, String text, String type)
	throws ParseException
    {
	ParserDisplay.SINGLETON.parseAttribute(text, attr);
	assertTrue(
		   text
		   + " gave wrong type: "
		   + (attr.getType() == null
		      ? "(null)"
		      : attr.getType().getName()),
		   attr.getType() != null
		   && type.equals(attr.getType().getName()));
    }

    private void checkType(MOperation op, String text, String type)
	throws ParseException
    {
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
    }

    private void checkParameters(MOperation op, String text, String params[])
	throws ParseException
    {
	int i;

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
	    assertTrue(text + "gave wrong default value in parameter " 
		       + (i / 4),
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
    }

    private void checkVisibility(MAttribute attr, String text, String vis)
	throws ParseException
    {
	ParserDisplay.SINGLETON.parseAttribute(text, attr);
	assertTrue(
		   text
		   + " gave wrong visibility: "
		   + (attr.getVisibility() == null
		      ? "(null)"
		      : attr.getVisibility().getName()),
		   attr.getVisibility() != null
		   && vis.equals(attr.getVisibility().getName()));
    }

    private void checkVisibility(MOperation op, String text, String vis)
	throws ParseException
    {
	ParserDisplay.SINGLETON.parseOperation(text, op);
	assertTrue(
		   text
		   + " gave wrong visibility: "
		   + (op.getVisibility() == null
		      ? "(null)"
		      : op.getVisibility().getName()),
		   op.getVisibility() != null
		   && vis.equals(op.getVisibility().getName()));
    }

    private void checkProperties(
				 MAttribute attr,
				 String text,
				 String props[])
	throws ParseException
    {
	int i;
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
    }

    private void checkProperties(MOperation op, String text, String props[])
	throws ParseException
    {
	int i;

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
    }

    private void checkMultiplicity(
				   MAttribute attr,
				   String text,
				   MMultiplicity mult)
	throws ParseException
    {
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
	}
    }

    private void checkValue(MAttribute attr, String text, String val)
	throws ParseException
    {
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
    }

    private void checkStereotype(MAttribute attr, String text, String val)
	throws ParseException
    {
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
    }

    private void checkStereotype(MOperation op, String text, String val)
	throws ParseException
    {
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
    }

    private void checkBases(MClassifierRole cr, String text, String bases[])
	throws ParseException
    {
	int i;
	Collection c;
	Iterator it;
	MClassifier cls;

	ParserDisplay.SINGLETON.parseClassifierRole(cr, text);
	c = cr.getBases();
	it = c.iterator();
    checkAllValid : 
	while (it.hasNext()) {
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

    checkAllExist : 
	for (i = 0; i < bases.length; i++) {
	    it = c.iterator();
	    while (it.hasNext()) {
		cls = (MClassifier) it.next();
		if (bases[i].equals(cls.getName()))
		    continue checkAllExist;
	    }
	    assertTrue("Base " + bases[i] + " was not generated by " + text,
		       false);
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
