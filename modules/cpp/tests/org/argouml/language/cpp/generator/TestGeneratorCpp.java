// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.language.cpp.generator;

import org.argouml.kernel.ProjectManager;

import java.util.Collection;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.argouml.model.Model;

/**
 * Tests for the GeneratorCpp class.
 * @see GeneratorCpp
 * @author euluis
 * @since 0.17.2
 */
public class TestGeneratorCpp extends BaseTestGeneratorCpp {

    /** The Logger for this class */
    private static final Logger LOG = Logger.getLogger(TestGeneratorCpp.class);

    /**
     * The constructor.
     *
     * @param testName the name of the test
     */
    public TestGeneratorCpp(java.lang.String testName) {
        super(testName);
    }

    /**
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(TestGeneratorCpp.class);
        return suite;
    }

    /**
     * to enable debugging in poor IDEs...
     * @param args the arguments given on the commandline
     */
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /** The AInterface interface */
    private Object aInterface;

    /** The AInterface::foo() operation */
    private Object aInterfaceFooOp;

    /** AClass realize AInterface realization */
    private Object aRealization;

    /** The AExtended class that generalizes AClass */
    private Object aExtended;

    /** AExtended extends AClass generalization */
    private Object aGeneralization;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        super.setUp();
        setUpAInterface();
        setUpAExtended();
        setUpNamespaces("AModel");
        setUpARealization(getAClass(), aInterface);
        setUpAGeneralization(aExtended, getAClass());
    }

    /**
     * Generate the AInterface interface
     */
    private void setUpAInterface() {
        aInterface = getFactory().buildInterface("AInterface");
        Object voidType =
            ProjectManager.getManager().getCurrentProject().findType("void");
        aInterfaceFooOp = buildOperation(aInterface, voidType, "foo");
    }

    private void setUpAExtended() {
        aExtended = getFactory().buildClass("AExtended");
    }

    /**
     * Setup the namespaces, giving a name to the model and assigning the 
     * model as the namespace of the AClass, AInterface and AExtended
     * objects.
     * @param modelName name to give to the model
     */
    private void setUpNamespaces(String modelName) {
        Model.getCoreHelper().setName(getModel(), modelName);
        Model.getCoreHelper().setNamespace(getAClass(), getModel());
        Model.getCoreHelper().setNamespace(aInterface, getModel());
        Model.getCoreHelper().setNamespace(aExtended, getModel());
    }

    /**
     * Generate a generalization from child to parent
     */
    private void setUpAGeneralization(Object child, Object parent) {
        aGeneralization = getFactory().buildGeneralization(child, parent);
    }

    /**
     * Generate a realization from cls to iface
     */
    private void setUpARealization(Object cls, Object iface) {
        aRealization = getFactory().buildRealization(cls, iface, getModel());
    }

    /**
     * Test of getInstance method.
     */
    public void testGetInstance() {
        assertNotNull(getGenerator());
    }

    /**
     * Test of cppGenerate method.
     */
    public void testCppGenerate() {
        // generate void AClass::foo();
        String strFooMethod = getGenerator().generate(getFooMethod());
        assertNotNull(strFooMethod);
        assertEquals("void AClass::foo()", strFooMethod.trim());
    }

    /**
     * Test of generateOperation method.
     */
    public void testGenerateOperationAndIssue2862() {
        Collection params = Model.getFacade().getParameters(getFooMethod());
        assertEquals(1, params.size());
        Object returnVal = params.iterator().next();
        Model.getCoreHelper().setTaggedValue(returnVal, "pointer", "true");
        Model.getCoreHelper().setType(returnVal, getAClass());
        String genOp = getGenerator().generateOperation(getFooMethod(), false);
        LOG.info(genOp);
        assertTrue(genOp.indexOf("*") != -1);
    }

    /**
     * Test of getModuleName method.
     */
    public void testGetModuleName() {
        assertNonNullNonZeroLengthString(getGenerator().getModuleName());
    }

    /**
     * Test of getModuleDescription method.
     */
    public void testGetModuleDescription() {
        assertNonNullNonZeroLengthString(getGenerator().getModuleDescription());
    }

    private void assertNonNullNonZeroLengthString(String s) {
        assertNotNull(s);
        assertTrue(s.length() > 0);
    }

    /**
     * Test of getModuleAuthor method.
     */
    public void testGetModuleAuthor() {
        assertNonNullNonZeroLengthString(getGenerator().getModuleAuthor());
    }

    /**
     * Test of getModuleVersion method.
     */
    public void testGetModuleVersion() {
        assertNonNullNonZeroLengthString(getGenerator().getModuleVersion());
    }

    /**
     * Test of getModuleKey method.
     */
    public void testGetModuleKey() {
        assertNonNullNonZeroLengthString(getGenerator().getModuleKey());
    }

    private void setTaggedValue(Object o, String name, String value) {
        Vector tvs = new Vector(
            Model.getFacade().getTaggedValuesCollection(o));
        Object tv =
            Model.getExtensionMechanismsFactory()
                .buildTaggedValue(name, value);
        tvs.addElement(tv);
        Model.getCoreHelper().setTaggedValues(o, tvs);
    }
    /**
     * Test that default inheritance is public for classes and 
     * "virtual public" for interfaces. Test also if the tag
     * "visibility" is used, overriding any default (issue 3055).
     */
    public void testInheritanceDefaultsToPublicOrVirtualPublic() {
        String code;
        String re;
        code = getGenerator().generateH(getAClass());
        re = "(?m)(?s).*class\\s+AClass\\s*:\\s*virtual\\s*public" 
            + "\\s*AInterface.*";
        assertTrue(code.matches(re));

        code = getGenerator().generate(aExtended);
        re = "(?m)(?s).*class\\s+AExtended\\s*:\\s*public\\s*AClass.*";
        assertTrue(code.matches(re));

        // add tagged value visibility and check that it's used
        setTaggedValue(aRealization, "visibility", "private");
        setTaggedValue(aGeneralization, "visibility", "private");

        code = getGenerator().generateH(getAClass());
        re = "(?m)(?s).*class\\s+AClass\\s*:\\s*private\\s*AInterface.*";
        assertTrue(code.matches(re));

        code = getGenerator().generate(aExtended);
        re = "(?m)(?s).*class\\s+AExtended\\s*:\\s*private\\s*AClass.*";
        assertTrue(code.matches(re));   
    }
}
