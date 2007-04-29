// $Id$
// Copyright (c) 2006-2007 The Regents of the University of California. All
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

package org.argouml.notation.providers.uml;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.argouml.notation.providers.NotationProvider;

/**
 * @author michiel
 */
public class TestAttributeAndOperationNotationUml extends TestCase {


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
    private static final String ATTR13 =
        "<<attrstereo1,attrstereo2>> +name : String = a[15]";

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
    private static final String OPER05 = "<< opstereo1, opstereo2 >>  name5()";

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
     * The constructor.
     *
     * @param str the name of the test
     */
    public TestAttributeAndOperationNotationUml(String str) {
        super(str);
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        NotationProviderFactory2.getInstance();
    }

    /**
     * Test the parsing of an attribute name.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeName()
        throws ParseException {
        Object attr;

        Project p = ProjectManager.getManager().getCurrentProject();
        Object model = p.getModel();
        Object attrType = p.getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkName(attr, ATTR01, "name");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkName(attr, ATTR02, "name");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkName(attr, ATTR03, "name");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkName(attr, ATTR04, "name");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkName(attr, ATTR05, "name");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkName(attr, ATTR06, "name");
    }


    /**
     * Test the parsing of an attribute's type.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeType() throws ParseException {
        Object attr;

        Project project = ProjectManager.getManager().getCurrentProject();
        Object ns = project.getModel();
        Object attrType = project.getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkType(attr, ATTR03, "void");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkType(attr, ATTR04, "int");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkType(attr, ATTR05, "int");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkType(attr, ATTR06, "int");
    }

    /**
     * Test the parsing of the attribute's visibility.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeVisibility() throws ParseException {
        Object attr;

        Project project = ProjectManager.getManager().getCurrentProject();
        Object ns = project.getModel();
        Object attrType = project.getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkVisibility(attr, ATTR02, "public");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkVisibility(attr, ATTR03, "private");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkVisibility(attr, ATTR04, "protected");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkVisibility(attr, ATTR05, "public");
        checkVisibility(attr, ATTR01, "public");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkVisibility(attr, ATTR06, "private");
        checkVisibility(attr, ATTR01, "private");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkVisibility(attr, ATTR08, "public");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkVisibility(attr, ATTR11, "public");
    }

    /**
     * Test the parsing of an attribute's property.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeProperty() throws ParseException {
        Object attr;
        String[] res1 = {
            "a", "b",
        };
        String[] res2 = {
            "a", "b", "c", "d",
        };
        String[] res3 = {
            "a", "b", "c", "d", "frozen",
            null,
        };

        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();
        Object attrType = ProjectManager.getManager().getCurrentProject()
                .getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkProperties(attr, ATTR04, res1);

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkProperties(attr, ATTR05, res2);

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkProperties(attr, ATTR06, res3);
    }

    /**
     * Test parsing an attribute's multiplicity.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeMultiplicity() throws ParseException {
        Object attr;

        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();
        Object attrType = ProjectManager.getManager().getCurrentProject()
                .getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkMultiplicity(attr, ATTR04,
                Model.getDataTypesFactory().createMultiplicity("1..1"));

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkMultiplicity(attr, ATTR05,
                Model.getDataTypesFactory().createMultiplicity("1..*"));

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkMultiplicity(attr, ATTR06,
                Model.getDataTypesFactory().createMultiplicity("*..*"));
    }

    /**
     * Test that the parser throws the correct exceptions.
     */
    public void testAttributeParseExceptions() {
        Object attr;

        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();
        Object attrType = ProjectManager.getManager().getCurrentProject()
                .getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

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
    public void testAttributeValue() throws ParseException {
        Object attr;

        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();
        Object attrType = ProjectManager.getManager().getCurrentProject()
                .getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkValue(attr, ATTR05, "0");
        checkValue(attr, ATTR01, "0");
        checkValue(attr, ATTR06, "15");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkValue(attr, ATTR07, "\'val[15] \'");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkValue(attr, ATTR08, "\"a <<string>>\"");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkValue(attr, ATTR09, "(a * (b+c) - d)");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkValue(attr, ATTR10, "2 * (b+c) - 10");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkValue(attr, ATTR11, "a[15]");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkValue(attr, ATTR12, "a << 5");
    }

    /**
     * Add stereotype to a model element if it doesn't already have it.
     */
    private void softAddStereotype(String name, Object elem) {
        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();

        Iterator it =
            Model.getExtensionMechanismsHelper()
                .getStereotypes(ns).iterator();
        while (it.hasNext()) {
            Object s =  it.next();
            if (name.equals(Model.getFacade().getName(s))) {
                return;
            }
        }
        Model.getExtensionMechanismsFactory().buildStereotype(
            elem,
            name,
            ns);
    }


    /**
     * Test the parsing of an attribute's stereotype.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeStereotype() throws ParseException {
        Object attr;

        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();
        Object attrType = ProjectManager.getManager().getCurrentProject()
                .getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        softAddStereotype("attrstereo1", attr);
        softAddStereotype("attrstereo2", attr);

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkStereotype(attr, ATTR01, new String[] {});

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkStereotype(attr, ATTR10, new String[] {"attrstereo1"});

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkStereotype(attr, ATTR11, new String[] {"attrstereo2"});

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, ns);

        checkStereotype(attr, ATTR13, new String[] {"attrstereo1",
                                                    "attrstereo2", });
    }


    /**
     * Test the parsing of an operation's name.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationName() throws ParseException {
        Object op;
        Object cl = Model.getCoreFactory().buildClass();

        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();

        Model.getCoreHelper().setNamespace(cl, ns);
        Object returnType = ProjectManager.getManager().getCurrentProject()
                .getDefaultReturnType();

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkName(op, OPER01, "name");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkName(op, OPER02, "name");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkName(op, OPER03, "name2");
    }

    /**
     * Test the parsing of an operation's type.
     * 
     * @throws ParseException
     *             if the parsing fails.
     */
    public void testOperationType() throws ParseException {
        Object op;
        Object cl = Model.getCoreFactory().buildClass();
        Object ns = ProjectManager.getManager().getCurrentProject().getModel();

        Model.getCoreHelper().setNamespace(cl, ns);
        Object returnType =
            ProjectManager.getManager().getCurrentProject().findType("void");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkType(op, OPER01, "void");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkType(op, OPER02, "int");
        checkType(op, OPER01, "int");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkType(op, OPER03, "String");
    }

    /**
     * Test the parsing of an operation's visibility.
     * 
     * @throws ParseException
     *             if the parsing fails.
     */
    public void testOperationVisibility() throws ParseException {
        Object op;
        Object cl = Model.getCoreFactory().buildClass();
        Object ns = ProjectManager.getManager().getCurrentProject().getModel();

        Model.getCoreHelper().setNamespace(cl, ns);
        Object returnType = ProjectManager.getManager().getCurrentProject()
                .getDefaultReturnType();

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkVisibility(op, OPER01, "public");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkVisibility(op, OPER02, "private");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkVisibility(op, OPER03, "protected");
        checkVisibility(op, OPER01, "protected");
        checkVisibility(op, OPER02, "private");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkVisibility(op, OPER04, "protected");
    }

    /**
     * Test the parsing of an operation's parameters.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationParameters() throws ParseException {
        Object op;
        Object cl = Model.getCoreFactory().buildClass();
        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();
        Object returnType = ProjectManager.getManager().getCurrentProject()
                .getDefaultReturnType();

        Model.getCoreHelper().setNamespace(cl, ns);

        String[] res1 = {
        };
        String[] res2 = {
            "in", "foo", "float", "0",
        };
        String[] res3 = {
            "out",
            "foo",
            "double",
            "0.",
            "inout",
            "bar",
            "String",
            "\"\"some\"\"",
        };

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkParameters(op, OPER01, res1);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkParameters(op, OPER02, res2);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkParameters(op, OPER03, res3);
        checkParameters(op, OPER01, res1);
        checkParameters(op, OPER02, res2);
    }

    /**
     * Test the parsing of an operation's properties.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationProperties() throws ParseException {
        Object op;
        Object cl = Model.getCoreFactory().buildClass();
        Object ns = ProjectManager.getManager().getCurrentProject().getModel();
        Object returnType = ProjectManager.getManager().getCurrentProject()
                .getDefaultReturnType();

        Model.getCoreHelper().setNamespace(cl, ns);

        String[] res1 = {
            "abstract", null,
            "concurrency", null,
            "concurrent", null,
            "guarded", null,
            "leaf", null,
            "query", null,
            "root", null,
            "sequential", null,
        };

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkProperties(op, OPER01, res1);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkProperties(op, OPER02, res1);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkProperties(op, OPER03, res1);
    }

    /**
     * Test the parsing of an operation's stereotype.
     * 
     * @throws ParseException
     *             if the parsing fails.
     */
    public void testOperationStereotype()
        throws ParseException {

        Object op;
        Object cl = Model.getCoreFactory().buildClass();
        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();

        Model.getCoreHelper().setNamespace(cl, ns);
        Object returnType = ProjectManager.getManager().getCurrentProject()
                .getDefaultReturnType();

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        softAddStereotype("opstereo1", op);
        softAddStereotype("opstereo2", op);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkStereotype(op, OPER01, new String[] {});

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkStereotype(op, OPER02, new String[] {"opstereo1"});

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkStereotype(op, OPER03, new String [] {"opstereo2"});
        checkStereotype(op, OPER04, new String[] {});
        checkStereotype(op, OPER05, new String [] {"opstereo1", "opstereo2"});
    }

    /**
     * Test the parsing of an operation's parse exceptions.
     */
    public void testOperationParseExceptions() {
        Object op;
        Object cl = Model.getCoreFactory().buildClass();
        Object ns =
            ProjectManager.getManager().getCurrentProject().getModel();

        Model.getCoreHelper().setNamespace(cl, ns);
        Object returnType = ProjectManager.getManager().getCurrentProject()
                .getDefaultReturnType();

        op = Model.getCoreFactory().buildOperation(cl, returnType);
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

    private void checkName(Object element, String text, String name)
        throws ParseException {

        if (Model.getFacade().isAAttribute(element)) {
            AttributeNotationUml anu = new AttributeNotationUml(); 
            anu.parseAttribute(text, element);
            assertTrue(text
                       + " gave wrong name: "
                       + Model.getFacade().getName(element),
                       name.equals(Model.getFacade().getName(element)));
        } else if (Model.getFacade().isAOperation(element)) {
            OperationNotationUml onu = new OperationNotationUml(element);
            onu.parseOperation(text, element);
            assertTrue(text
                       + " gave wrong name: "
                       + Model.getFacade().getName(element)
                       + " != " + name,
                       name.equals(Model.getFacade().getName(element)));
        } else {
            fail("Can only check name of a feature or classifier role");
        }
    }

    private void checkType(Object feature, String text, String type)
        throws ParseException {
        if (Model.getFacade().isAAttribute(feature)) {
            AttributeNotationUml anu = new AttributeNotationUml(); 
            anu.parseAttribute(text, feature);
            assertTrue(text + " gave wrong type: (null)",
                       Model.getFacade().getType(feature) != null);
            assertTrue(text + " gave wrong type: "
                       + Model.getFacade().getName(
                               Model.getFacade().getType(feature)),
                       type.equals(Model.getFacade().getName(
                               Model.getFacade().getType(feature))));
        } else if (Model.getFacade().isAOperation(feature)) {
            OperationNotationUml onu = new OperationNotationUml(feature);
            onu.parseOperation(text, feature);
            Collection ret =
                Model.getCoreHelper().getReturnParameters(feature);
            Iterator it = ret.iterator();
            assertTrue(
                   text + " gave extra return value",
                   !(type == null && it.hasNext()));
            assertTrue(
                   text + " lacks return value",
                   !(type != null && !it.hasNext()));
            if (it.hasNext()) {
                Object p =  it.next();
                Object pType = Model.getFacade().getType(p);
                assertTrue(
                       text + " gave wrong return",
                       (type == null && pType == null)
                       || (type != null
                           && type.equals(Model.getFacade().getName(pType))));
            }
            assertTrue(text + " gave extra return value", !it.hasNext());
        } else {
            fail("Can only check type of a feature");
        }
    }


    private void checkParameters(Object op, String text, String[] params)
        throws ParseException {
        int i;

        OperationNotationUml onu = new OperationNotationUml(op);
        onu.parseOperation(text, op);
        Collection prm = Model.getFacade().getParameters(op);
        Iterator it = prm.iterator();
        assertTrue(
               text + " lacks parameters",
               !(params.length > 0 && !it.hasNext()));
        for (i = 0; i + 3 < params.length; i += 4) {
            Object p;
            do {
                assertTrue(text + " lacks parameters", it.hasNext());
                p =  it.next();
            } while (Model.getFacade().getKind(p).equals(
                    Model.getDirectionKind().getReturnParameter()));
            assertTrue(text + "gave wrong inout in parameter " + (i / 4),
                   params[i].equals(Model.getFacade()
                           .getName(Model.getFacade().getKind(p))));
            assertTrue(text + "gave wrong name in parameter " + (i / 4),
                   params[i + 1].equals(Model.getFacade().getName(p)));
            Object pType = Model.getFacade().getType(p);
            assertTrue(text + "gave wrong type in parameter " + (i / 4),
                   params[i + 2].equals(Model.getFacade().getName(pType)));
            Object defaultValue = Model.getFacade().getDefaultValue(p);
            assertTrue(text + "gave wrong default value in parameter "
                   + (i / 4),
                   (params[i + 3] == null && defaultValue == null)
                   || ((params[i + 3] != null
                       && defaultValue != null)
                       && params[i + 3].equals(
                               Model.getFacade().getBody(defaultValue))));
        }
        while (it.hasNext()) {
            Object p =  it.next();
            assertTrue(
                text + " gave extra parameters",
                Model.getFacade().getKind(p).equals(
                       Model.getDirectionKind().getReturnParameter()));
        }
    }

    private void checkVisibility(Object feature, String text, String vis)
        throws ParseException {
        if (Model.getFacade().isAAttribute(feature)) {
            AttributeNotationUml anu = new AttributeNotationUml(); 
            anu.parseAttribute(text, feature);
            assertTrue(text + " gave wrong visibility: (null)",
                       Model.getFacade().getVisibility(feature) != null);
            assertTrue(text + " gave wrong visibility: "
                       + Model.getFacade().getName(
                               Model.getFacade().getVisibility(feature)),
                       vis.equals(Model.getFacade().getName(
                               Model.getFacade().getVisibility(feature))));
        } else if (Model.getFacade().isAOperation(feature)) {
            OperationNotationUml onu = new OperationNotationUml(feature);
            onu.parseOperation(text, feature);
            assertTrue(text + " gave wrong visibility: (null)",
                       Model.getFacade().getVisibility(feature) != null);
            assertTrue(text + " gave wrong visibility: "
                       + Model.getFacade().getName(
                               Model.getFacade().getVisibility(feature)),
                       vis.equals(Model.getFacade().getName(
                               Model.getFacade().getVisibility(feature))));
        } else {
            fail("Can only check visibility of a feature");
        }
    }

    private void checkProperties(
            Object feature,
            String text,
            String[] props)
        throws ParseException {

        if (Model.getFacade().isAAttribute(feature)) {
            int i;
            AttributeNotationUml anu = new AttributeNotationUml(); 
            anu.parseAttribute(text, feature);
            for (i = 0; i + 1 < props.length; i += 2) {
                if (props[i + 1] == null) {
                    assertTrue(
                            "TaggedValue " + props[i] + " exists!",
                            Model.getFacade().getTaggedValue(feature, props[i])
                            == null);
                } else {
                    Object tv =
                        Model.getFacade().getTaggedValue(feature, props[i]);
                    Object tvValue = Model.getFacade().getValueOfTag(tv);

                    assertTrue(
                            "TaggedValue " + props[i] + " wrong!",
                            props[i + 1].equals(tvValue)
                    );
                }
            }
        } else if (Model.getFacade().isAOperation(feature)) {
            int i;

            OperationNotationUml onu = new OperationNotationUml(feature);
            onu.parseOperation(text, feature);
            for (i = 0; i + 1 < props.length; i += 2) {
                if (props[i + 1] == null) {
                    assertTrue(
                            "TaggedValue " + props[i] + " exists!",
                            Model.getFacade().getTaggedValue(feature, props[i])
                            == null);
                } else {
                    Object tv =
                        Model.getFacade().getTaggedValue(feature, props[i]);
                    Object tvValue = Model.getFacade().getValueOfTag(tv);
                    assertTrue(
                            "TaggedValue " + props[i] + " wrong!",
                            props[i + 1].equals(tvValue));
                }
            }
        }
    }

    private void checkMultiplicity(
                   Object attr,
                   String text,
                   Object mult)
        throws ParseException {

        AttributeNotationUml anu = new AttributeNotationUml(); 
        anu.parseAttribute(text, attr);
        if (mult == null) {
            assertTrue(
                    text
                    + " gave wrong multiplicity: "
                    + (Model.getFacade().getMultiplicity(attr) == null
                       ? "(null)"
                       : Model.getFacade().toString(Model.getFacade().
                               getMultiplicity(attr))),
                    Model.getFacade().getMultiplicity(attr) == null);
        } else {
            assertTrue(
                    text
                    + " gave wrong multiplicity: "
                    + (Model.getFacade().getMultiplicity(attr) == null
                       ? "(null)"
                       : Model.getFacade().toString(Model.getFacade().
                               getMultiplicity(attr))),
                    Model.getFacade().toString(mult).equals(
                            Model.getFacade().toString(
                                    Model.getFacade().getMultiplicity(attr))));
        }
    }

    private void checkThrows(
                 Object element,
                 String text,
                 boolean prsEx,
                 boolean ex2,
                 boolean ex3) {
        if (Model.getFacade().isAAttribute(element)) {
            try {
                AttributeNotationUml anu = new AttributeNotationUml(); 
                anu.parseAttribute(text, element);
                fail("didn't throw for " + text);
            } catch (ParseException pe) {
                assertTrue(text + " threw ParseException " + pe, prsEx);
            }
        } else if (Model.getFacade().isAOperation(element)) {
            try {
                OperationNotationUml onu = new OperationNotationUml(element);
                onu.parseOperation(text, element);
                fail("didn't throw for " + text);
            } catch (ParseException pe) {
                assertTrue(text + " threw ParseException " + pe, prsEx);
            }
        }
    }

    private void checkValue(Object attr, String text, String val)
        throws ParseException {

        AttributeNotationUml anu = new AttributeNotationUml(); 
        anu.parseAttribute(text, attr);
        if (val == null) {
            assertTrue(
                    text
                    + " gave wrong visibility: "
                    + (Model.getFacade().getInitialValue(attr) == null
                       ? "(null)"
                       : Model.getFacade().getBody(
                                Model.getFacade().getInitialValue(attr))),
                    Model.getFacade().getInitialValue(attr) == null
                    || "".equals(Model.getFacade().getBody(
                            Model.getFacade().getInitialValue(attr))));
        } else {
            assertTrue(
                text
                + " gave wrong visibility: "
                + (Model.getFacade().getInitialValue(attr) == null
                  ? "(null)"
                  : Model.getFacade().getBody(
                      Model.getFacade().getInitialValue(attr))),
                      Model.getFacade().getInitialValue(attr) != null
                          && val.equals(Model.getFacade().getBody(
                              Model.getFacade().getInitialValue(attr))));
        }
    }

    /**
     * Parses the text into the feature and checks that the feature gets
     * the stereotype with the name val.
     *
     * @param feature The feature.
     * @param text The text to parse.
     * @param val The name of the stereotype(s).
     * @throws ParseException if we cannot parse the text.
     */
    private void checkStereotype(Object feature, String text, String[] val)
        throws ParseException {

        NotationProvider np = null;
        if (Model.getFacade().isAAttribute(feature)) {
            AttributeNotationUml anu = new AttributeNotationUml(); 
            anu.parseAttribute(text, feature);
            np = anu;
        } else if (Model.getFacade().isAOperation(feature)) {
            OperationNotationUml onu = new OperationNotationUml(feature);
            onu.parseOperation(text, feature);
            np = onu;
        } else {
            fail("Unknown feature type " + feature);
        }

        Collection stereos = Model.getFacade().getStereotypes(feature);
        List<Object> stereoNames = new ArrayList<Object>();
        for (Iterator i = stereos.iterator(); i.hasNext();) {
            stereoNames.add(Model.getFacade().getName(i.next()));
        }
        boolean stereosMatch = true;
        for (int i = 0; i < val.length; i++) {
            if (!stereoNames.contains(val[i])) {
                stereosMatch = false;
            }
        }
        assertTrue(
               text + " gave wrong stereotype " + stereos.toArray(),
                  val.length == stereos.size()
                  && stereosMatch);
        
        String str = np.toString(feature, null);
        assertTrue("Empty string", str.length() > 0);
        // TODO: Test if the generated string is correct.
    }

    /**
     * Test if help is correctly provided.
     */
    public void testGetHelpAttribute() {
        AttributeNotationUml notation = new AttributeNotationUml();
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }

    /**
     * Test if help is correctly provided.
     */
    public void testGetHelpOperation() {
        Object op;
        Object cl = Model.getCoreFactory().buildClass();

        Object ns = ProjectManager.getManager().getCurrentProject().getModel();

        Model.getCoreHelper().setNamespace(cl, ns);
        Object returnType = ProjectManager.getManager().getCurrentProject()
                .getDefaultReturnType();

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        
        OperationNotationUml notation = new OperationNotationUml(op);
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }

    /**
     * Dummy test.
     */
    public void testDummy() { }


}
