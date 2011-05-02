/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2006-2008 The Regents of the University of California. All
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
import org.argouml.model.ExtensionMechanismsFactory;
import org.argouml.model.ExtensionMechanismsHelper;
import org.argouml.model.Facade;
import org.argouml.model.InitializeModel;
import org.argouml.model.Model;
import org.argouml.notation.InitNotation;
import org.argouml.notation.NotationProvider;
import org.argouml.notation.NotationSettings;
import org.argouml.notation.providers.java.InitNotationJava;
import org.argouml.profile.ProfileFacade;
import org.argouml.profile.init.InitProfileSubsystem;

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
        "private name {a=b, c = d } [0..*] : int = 15 {frozen}";
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
    private static final String ATTR14 = "a : int; b : Foo";
    private static final String ATTR15 = " / name";
    private static final String ATTR16 = " /name";
    private static final String ATTR17 = "/-name : void";
    private static final String ATTR18 =
        "/ private name {a=b, c = d } [0..*] : int = 15 {frozen}";
    private static final String ATTR19 = "x2 : Integer {frozen}";
    
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
    private static final String NATTR14 =
        "private / name {a=b, c = d } [0..*] : int = 15 {frozen}";

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
    private static final String OPER06 = "name";
    private static final String OPER07 = "<<stereo>>#name(a:Integer=1,b:String):Boolean{query, x=1,y=2,z}";

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

    private Project project;
    private Object model;
    
    /**
     * The constructor.
     *
     * @param str the name of the test
     */
    public TestAttributeAndOperationNotationUml(String str) {
        super(str);
        InitializeModel.initializeDefault();
    }

    /*
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() {
        (new InitProfileSubsystem()).init();
        (new InitNotation()).init();
        (new InitNotationUml()).init();
        (new InitNotationJava()).init();
        project = ProjectManager.getManager().makeEmptyProject();
        ProjectManager.getManager().setCurrentProject(project);
        model = project.getUserDefinedModelList().iterator().next();
    }
    
    @Override
    protected void tearDown() throws Exception {
        ProjectManager.getManager().removeProject(
                ProjectManager.getManager().getCurrentProject());
        ProfileFacade.reset();
        super.tearDown();
    }

    /**
     * Test the parsing of an attribute name.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeName()
        throws ParseException {
        Object attr;
        Object attrType = project.getDefaultAttributeType();

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
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkName(attr, ATTR15, "name");
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkName(attr, ATTR16, "name");
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkName(attr, ATTR17, "name");
    }


    /**
     * Test the parsing of an attribute's type.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeType() throws ParseException {
        Object attr;

        Object attrType = project.getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkType(attr, ATTR03, "void");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkType(attr, ATTR04, "int");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkType(attr, ATTR05, "int");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkType(attr, ATTR06, "int");
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkType(attr, ATTR17, "void");
    }
    
    /**
     * Test the parsing of several attributes with semicolons,
     * and the correct assignation of the new datatypes to
     * the same namespace of the owner
     * See issue 5229 (http://argouml.tigris.org/issues/show_bug.cgi?id=5229) 
     * @throws ParseException
     */
    public void testAttributeNewTypeNamespace() throws ParseException {
        Object attr;

        Object cl = Model.getCoreFactory().buildClass();
        Model.getCoreHelper().setNamespace(cl, model);
        
        Object attrType = project.getDefaultAttributeType();
        attr = Model.getCoreFactory().buildAttribute2(cl, attrType);       

        AttributeNotationUml anu = new AttributeNotationUml(attr); 
        anu.parse(attr, ATTR14);
                
        List attrs = Model.getFacade().getAttributes(cl);
        assertEquals("Wrong number of attributes", 2, attrs.size());
        
        // we want the b: Foo attribute, that is the second in the list
        attr = attrs.get(1);
        
        attrType = Model.getFacade().getType(attr);
        Object namespace = Model.getFacade().getNamespace(attrType);
        
        assertNotNull("Namespace of the created type for the attribute must not be null",
                namespace);
        assertEquals("The namespace of the created type is not the same than" +
        		" the namespace of the original class.", model, namespace);
        
    }

    /**
     * Test the parsing of the attribute's visibility.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeVisibility() throws ParseException {
        Object attr;

        Object attrType = project.getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkVisibility(attr, ATTR02, "public");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkVisibility(attr, ATTR03, "private");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkVisibility(attr, ATTR04, "protected");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkVisibility(attr, ATTR05, "public");
        checkVisibility(attr, ATTR01, "public");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkVisibility(attr, ATTR06, "private");
        checkVisibility(attr, ATTR01, "private");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkVisibility(attr, ATTR08, "public");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkVisibility(attr, ATTR11, "public");
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkVisibility(attr, ATTR17, "private");
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

        Object attrType = project
                .getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkProperties(attr, ATTR04, res1);

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkProperties(attr, ATTR05, res2);

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkProperties(attr, ATTR06, res3);
    }

    /**
     * Test parsing an attribute's multiplicity.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeMultiplicity() throws ParseException {
        Object attr;

        Object attrType = project.getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkMultiplicity(attr, ATTR04, 1, 1);

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkMultiplicity(attr, ATTR05, 1, -1);

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkMultiplicity(attr, ATTR06, 0, -1);
    }

    /**
     * Test the parsing of derived for an attribute.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeDerived()
        throws ParseException {
        Object attr;
        Object tv;
        Object attrType = project.getDefaultAttributeType();
        ExtensionMechanismsFactory emFactory =
            Model.getExtensionMechanismsFactory();
        ExtensionMechanismsHelper emHelper =
            Model.getExtensionMechanismsHelper();
        Object stereo = emFactory.buildStereotype("mystereo", model);
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkDerived(attr, ATTR15, true);
        checkDerived(attr, ATTR01, false);
        checkDerived(attr, ATTR15, true);
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkDerived(attr, ATTR16, true);
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkDerived(attr, ATTR17, true);
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkDerived(attr, ATTR18, true);
        
        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkDerived(attr, ATTR19, false);
        
        Object td = emFactory.buildTagDefinition(
                Facade.DERIVED_TAG, stereo, null);
        
        tv = emFactory.buildTaggedValue(td, 
                new String[] {"true"});
        emHelper.addTaggedValue(attr, tv);

        checkDerived(attr, ATTR19, false);

        tv = emFactory.buildTaggedValue(td, 
                new String[] {"false"});
        emHelper.addTaggedValue(attr, tv);

        checkDerived(attr, ATTR15, true);
    }

    /**
     * Test that the parser throws the correct exceptions.
     */
    public void testAttributeParseExceptions() {
        Object attr;

        Object attrType = project.getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkThrows(attr, NATTR01, true);
        checkThrows(attr, NATTR02, true);
        checkThrows(attr, NATTR03, true);
        checkThrows(attr, NATTR04, true);
        checkThrows(attr, NATTR05, true);
        checkThrows(attr, NATTR06, true);
        checkThrows(attr, NATTR07, true);
        checkThrows(attr, NATTR08, true);
        checkThrows(attr, NATTR09, true);
        checkThrows(attr, NATTR10, true);
        checkThrows(attr, NATTR11, true);
        checkThrows(attr, NATTR12, true);
        checkThrows(attr, NATTR13, true);
        checkThrows(attr, NATTR14, true);
    }

    /**
     * Test the parsing of an attribute's value.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeValue() throws ParseException {
        Object attr;

        Object attrType = project.getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkValue(attr, ATTR05, "0");
        checkValue(attr, ATTR01, "0");
        checkValue(attr, ATTR06, "15");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkValue(attr, ATTR07, "\'val[15] \'");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkValue(attr, ATTR08, "\"a <<string>>\"");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkValue(attr, ATTR09, "(a * (b+c) - d)");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkValue(attr, ATTR10, "2 * (b+c) - 10");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkValue(attr, ATTR11, "a[15]");

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkValue(attr, ATTR12, "a << 5");
    }

    /**
     * Add stereotype to a model element if it doesn't already have it.
     */
    private void softAddStereotype(String name, Object elem) {
        for (Object s 
                : Model.getExtensionMechanismsHelper().getStereotypes(model)) {
            if (name.equals(Model.getFacade().getName(s))) {
                return;
            }
        }
        Model.getExtensionMechanismsFactory().buildStereotype(
            elem,
            name,
            model);
    }


    /**
     * Test the parsing of an attribute's stereotype.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testAttributeStereotype() throws ParseException {
        Object attr;

        Object attrType = project
                .getDefaultAttributeType();

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        softAddStereotype("attrstereo1", attr);
        softAddStereotype("attrstereo2", attr);

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkStereotype(attr, ATTR01, new String[] {});

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkStereotype(attr, ATTR10, new String[] {"attrstereo1"});

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

        checkStereotype(attr, ATTR11, new String[] {"attrstereo2"});

        attr = Model.getCoreFactory().buildAttribute2(attrType);
        Model.getCoreHelper().setNamespace(attr, model);

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

        Model.getCoreHelper().setNamespace(cl, model);
        Object returnType = project
                .getDefaultReturnType();

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkName(op, OPER01, "name");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkName(op, OPER02, "name");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkName(op, OPER03, "name2");
        
        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkName(op, OPER06, "name");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkName(op, OPER07, "name");
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
 
        Model.getCoreHelper().setNamespace(cl, model);
        Object returnType =
            project.findType("void");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkType(op, OPER01, "void");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkType(op, OPER02, "int");
        checkType(op, OPER01, "int");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkType(op, OPER03, "String");

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkType(op, OPER07, "Boolean");
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

        Model.getCoreHelper().setNamespace(cl, model);
        Object returnType = project
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
        
        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkVisibility(op, OPER07, "protected");
    }

    /**
     * Test the parsing of an operation's parameters.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationParameters() throws ParseException {
        Object op;

        Object returnType = project.getDefaultReturnType();
        Object cl = Model.getCoreFactory().buildClass();

        Model.getCoreHelper().setNamespace(cl, model);

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
        String[] res4 = {
                "in", "a", "Integer", "1",
                "in", "b", "String", null,
        };
        
        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkParameters(op, OPER01, res1);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkParameters(op, OPER02, res2);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkParameters(op, OPER03, res3);
        checkParameters(op, OPER01, res1);
        checkParameters(op, OPER02, res2);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkParameters(op, OPER07, res4);
    }

    /**
     * Test the parsing of an operation's properties.
     *
     * @throws ParseException if the parsing fails.
     */
    public void testOperationProperties() throws ParseException {
        Object op;
        Object returnType = project.getDefaultReturnType();
        Object cl = Model.getCoreFactory().buildClass();

        Model.getCoreHelper().setNamespace(cl, model);

        String[] res1 = {
            "abstract", null,
            "concurrency", null,
            "concurrent", null,
            "guarded", null,
            "leaf", null,
            "query", null,
            "root", null,
            "sequential", null,
            "derived", null,
        };
        
        String[] res7 = {
                "abstract", null,
                "concurrency", null,
                "concurrent", null,
                "guarded", null,
                "leaf", null,
                "query", null,
                "root", null,
                "sequential", null,
                "x", "1",
                "y", "2",
                "z", "",
            };

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkProperties(op, OPER01, res1);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkProperties(op, OPER02, res1);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkProperties(op, OPER03, res1);

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkProperties(op, OPER07, res7);
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

        Model.getCoreHelper().setNamespace(cl, model);
        Object returnType = project.getDefaultReturnType();

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

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkStereotype(op, OPER07, new String[] {"stereo"});
    }

    /**
     * Test the parsing of an operation's parse exceptions.
     */
    public void testOperationParseExceptions() {
        Object op;
        Object cl = Model.getCoreFactory().buildClass();

        Model.getCoreHelper().setNamespace(cl, model);
        Object returnType = project.getDefaultReturnType();

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        checkThrows(op, NOPER01, true);
        checkThrows(op, NOPER02, true);
        checkThrows(op, NOPER03, true);
        checkThrows(op, NOPER04, true);
        checkThrows(op, NOPER05, true);
        checkThrows(op, NOPER06, true);
        checkThrows(op, NOPER07, true);
        checkThrows(op, NOPER08, true);
        checkThrows(op, NOPER09, true);
        checkThrows(op, NOPER10, true);
    }

    /**
     * Parse a given text, and check if the given element 
     * was renamed to "name".
     * 
     * @param element an Attribute or an Operation
     * @param text the text to parse
     * @param name this should be the resulting name of the element
     * @throws ParseException if it went wrong
     */
    private void checkName(Object element, String text, String name)
        throws ParseException {

        if (Model.getFacade().isAAttribute(element)) {
            AttributeNotationUml anu = new AttributeNotationUml(element); 
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
            AttributeNotationUml anu = new AttributeNotationUml(feature); 
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
            AttributeNotationUml anu = new AttributeNotationUml(feature); 
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
            AttributeNotationUml anu = new AttributeNotationUml(feature);
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
                    assertTrue("TaggedValue " + props[i] + " does not exist!", 
                            tv != null);
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
                   int lower,
                   int upper)
        throws ParseException {

        AttributeNotationUml anu = new AttributeNotationUml(attr); 
        anu.parseAttribute(text, attr);
        Object m = Model.getFacade().getMultiplicity(attr);
        int l = Model.getFacade().getLower(m);
        int u = Model.getFacade().getUpper(m);
        assertTrue(text + " gave wrong multiplicity: "
                + (Model.getFacade().getMultiplicity(attr) == null ? "(null)"
                        : Model.getFacade().toString(m)), 
                        (lower == l && upper == u));
        
    }
    
    private void checkDerived(
            Object feature,
            String text,
            boolean derived)
        throws ParseException {

        if (Model.getFacade().isAAttribute(feature)) {
            AttributeNotationUml anu = new AttributeNotationUml(feature);
            anu.parseAttribute(text, feature);
        } else if (Model.getFacade().isAOperation(feature)) {
            OperationNotationUml onu = new OperationNotationUml(feature);
            onu.parseOperation(text, feature);
        }
        
        Object tv =
            Model.getFacade().getTaggedValue(feature, Facade.DERIVED_TAG);
        if (derived) {
            assertTrue(
                    "TaggedValue " + Facade.DERIVED_TAG + " does not exist!",
                    tv != null);
            String tvValue = Model.getFacade().getValueOfTag(tv);
            assertTrue(
                    "TaggedValue " + Facade.DERIVED_TAG + " wrong!",
                    "true".equalsIgnoreCase(tvValue));
        } else {
            if (tv != null) {
                String tvValue = Model.getFacade().getValueOfTag(tv);
                assertFalse(
                        "TaggedValue " + Facade.DERIVED_TAG + " wrong!",
                        "true".equalsIgnoreCase(tvValue));
            }
        }
    }


    private void checkThrows(
                 Object element,
                 String text,
                 boolean prsEx) {
        if (Model.getFacade().isAAttribute(element)) {
            try {
                AttributeNotationUml anu = new AttributeNotationUml(element);
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

        AttributeNotationUml anu = new AttributeNotationUml(attr);
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
            AttributeNotationUml anu = new AttributeNotationUml(feature);
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
        List<String> stereoNames = new ArrayList<String>();
        for (Object stereo : stereos) {
            stereoNames.add(Model.getFacade().getName(stereo));
        }
        boolean stereosMatch = true;
        for (String v : val) {
            if (!stereoNames.contains(v)) {
                stereosMatch = false;
            }
        }
        assertTrue(
               text + " gave wrong stereotype " + stereos.toArray(),
                  val.length == stereos.size()
                  && stereosMatch);
        
        String str = np
                .toString(feature, NotationSettings.getDefaultSettings());
        assertTrue("Empty string", str.length() > 0);
        // TODO: Test if the generated string is correct.
    }

    /**
     * Test if help is correctly provided.
     */
    public void testGetHelpAttribute() {
        Object attr;
        attr = Model.getCoreFactory().createAttribute();
        
        AttributeNotationUml notation = new AttributeNotationUml(attr);
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

        Model.getCoreHelper().setNamespace(cl, model);
        Object returnType = project.getDefaultReturnType();

        op = Model.getCoreFactory().buildOperation(cl, returnType);
        
        OperationNotationUml notation = new OperationNotationUml(op);
        String help = notation.getParsingHelp();
        assertTrue("No help at all given", help.length() > 0);
        assertTrue("Parsing help not conform for translation", 
                help.startsWith("parsing."));
    }
}
