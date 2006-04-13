// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.language.cpp.reveng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.FileUtils;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.uml.reveng.DiagramInterface;
import org.argouml.uml.reveng.Import;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;

/**
 * Tests the {@link CppImport} class.
 *
 * NOTE: this is more like a module test, since here we also test the
 * {@link Modeler} implementation.
 *
 * FIXME: duplicate code from TestCppFileGeneration and BaseTestGeneratorCpp.
 *
 * @author Luis Sergio Oliveira (euluis)
 * @since 0.19.3
 * @see CppImport
 */
public class TestCppImport extends TestCase {

    /**
     * Constructor.
     *
     * @param testName The name of the test.
     */
    public TestCppImport(String testName) {
        super(testName);
    }

    /**
     * @return the test suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(TestCppImport.class);
        return suite;
    }

    /**
     * Enables debugging in IDEs that don't support debugging unit tests...
     *
     * @param args the arguments given on the commandline
     */
    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * System temporary directory property name.
     */
    static final String SYSPROPNAME_TMPDIR = "java.io.tmpdir";

    /**
     * Path of the temporary directory in the system.
     */
    private File tmpDir;

    /**
     * Directory to be deleted on tearDown if not null.
     */
    private File genDir;

    /**
     * The ArgoUML C++ reveng module.
     */
    private CppImport cppImp;

    /**
     * The ArgoUML project.
     */
    private Project proj;

    /**
     * The editor(?).
     */
    private Editor ed;

    /**
     * The diagram interface.
     */
    private DiagramInterface di;

    /**
     * The Import that contains details about the user configured import.
     */
    private Import imp;

    /**
     * @throws Exception if something goes wrong
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        tmpDir = new File(System.getProperty(SYSPROPNAME_TMPDIR));
        proj = ProjectManager.getManager().getCurrentProject();
        ed = Globals.curEditor();
        di = new DiagramInterface(ed);
        imp = null;
        // FIXME: The following fails because it can't find PluggableImport
        // modules. I must get ModuleLoader to load the CppImport module.
        // From a different perspective, I don't like being too tied to the
        // Import class, since it seams to be doing too much - i.e., it
        // contains things related to the view, to the model and to the
        // controller, while it should be only a controller. The view
        // (e.g., details pannel) and the model (e.g.,
        // CreateDiagramsChecked, DiscendDirectoriesRecursively and
        // Attribute properties).
        // Bottom line: for now I simply don't need it!
        //!imp = new Import();
        cppImp = new CppImport();
    }

    /**
     * @throws Exception
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        if (genDir != null && genDir.exists()) {
            FileUtils.deleteDirectory(genDir);
        }
        super.tearDown();
    }

    /**
     * Setup a directory with the given name for the caller test.
     *
     * @param dirName the directory to be created in the system temporary dir
     * @return the created directory
     */
    private File setUpDirectory4Test(String dirName) {
        File generationDir = new File(tmpDir, dirName);
        generationDir.mkdirs();
        return generationDir;
    }

    /**
     * Simple test for the <code>CppImport.parseFile(xxx)</code> method, using
     * the SimpleClass.cpp source file.
     *
     * @throws Exception something went wrong
     */
    public void testParseFileSimpleClass() throws Exception {
        genDir = setUpDirectory4Test("testParseFileSimpleClass");
        File srcFile = setupSrcFile4Reverse("SimpleClass.cpp");

        cppImp.parseFile(proj, srcFile, di, imp);

        Collection nss =
            Model.getModelManagementHelper().getAllNamespaces(proj.getModel());
        Object pack = findModelElementWithName(nss, "pack");
        assertNotNull("The pack namespace wasn't found in the model!", pack);

        Collection clss = Model.getCoreHelper().getAllClasses(pack);
        Object simpleClass = findModelElementWithName(clss, "SimpleClass");
        assertNotNull("The pack::SimpleClass class wasn't found in the model!",
            simpleClass);
        assertTrue(Model.getFacade().isPublic(simpleClass));

        Collection opers =
            Model.getCoreHelper().getBehavioralFeatures(simpleClass);
        Object newOperation = findModelElementWithName(opers, "newOperation");
        assertNotNull("The pack::SimpleClass::newOperation() model element "
            + "doesn't exist!", newOperation);
        assertTrue(Model.getFacade().isPublic(newOperation));

        Collection attrs = Model.getCoreHelper().getAllAttributes(simpleClass);
        Object newAttr = findModelElementWithName(attrs, "newAttr");
        assertNotNull(
            "The pack::SimpleClass::newAttr attribute doesn't exist!", newAttr);
        assertTrue(Model.getFacade().isPublic(newAttr));
        // TODO: verify reveng of SimpleClass.newOperation definition
        // TODO: 1. how to model a namespace alias in UML? A variation of the
        // import or access relationship would be nice, but, I don't know if
        // that would be to force things a bit...
        // TODO: 2. verify namespace alias p = pack.
    }

    /**
     * Ditto for DerivedFromAbstract.cxx translation unit.
     *
     * @throws Exception something wen't wrong
     */
    public void testParseFileDerivedFromAbstract() throws Exception {
        genDir = setUpDirectory4Test("testParseFileDerivedFromAbstract");
        File srcFile = setupSrcFile4Reverse("DerivedFromAbstract.cxx");

        cppImp.parseFile(proj, srcFile, di, imp);

        // verify the Dummy struct reveng
        Collection classes =
            Model.getCoreHelper().getAllClasses(proj.getModel());
        Object dummyStruct = findModelElementWithName(classes, "Dummy");
        assertNotNull("The Dummy structure doesn't exist in the model!",
            dummyStruct);
        assertTaggedValueExistsAndValueIs(dummyStruct,
            ProfileCpp.TV_NAME_CLASS_SPECIFIER, "struct");

        Collection attributes =
            Model.getCoreHelper().getAllAttributes(dummyStruct);
        Object c = findModelElementWithName(attributes, "c");
        assertNotNull("The Dummy::c attribute doesn't exist in the model!", c);
        assertTrue("Dummy::c must be public!", Model.getFacade().isPublic(c));
        Object signedChar = Model.getFacade().getType(c);
        assertEquals("signed char", Model.getFacade().getName(signedChar));
        assertTrue("signed char must be a DataType!", Model.getFacade()
                .isADataType(signedChar));

        // verify Base reveng
        Object baseClass = findModelElementWithName(classes, "Base");
        assertNotNull("The Base class doesn't exist in the model!", baseClass);
        assertNull(Model.getFacade().getTaggedValue(baseClass,
            ProfileCpp.TV_NAME_CLASS_SPECIFIER));

        Collection opers =
            Model.getCoreHelper().getBehavioralFeatures(baseClass);
        Object baseFooOper = findModelElementWithName(opers, "foo");
        assertNotNull(
            "The Base::foo(xxx) operation doesn't exist in the model!",
            baseFooOper);
        assertTrue(Model.getFacade().isAbstract(baseFooOper));
        Object baseFooRv =
            Model.getCoreHelper().getReturnParameter(baseFooOper);
        assertEquals("unsigned int", Model.getFacade().getName(
            Model.getFacade().getType(baseFooRv)));
        Collection params = Model.getFacade().getParameters(baseFooOper);
        Object baseFooOtherParam = findModelElementWithName(params, "other");
        assertNotNull(baseFooOtherParam);
        assertEquals(baseClass, Model.getFacade().getType(baseFooOtherParam));
        assertTaggedValueExistsAndValueIs(baseFooOtherParam,
            ProfileCpp.TV_NAME_REFERENCE, "true");
        assertEquals("inout", Model.getFacade().getName(
            Model.getFacade().getKind(baseFooOtherParam)));

        attributes = Model.getCoreHelper().getAllAttributes(baseClass);
        Object baseUiAttr = findModelElementWithName(attributes, "ui");
        assertNotNull("The Base::ui attribute doesn't exist in the model!",
            baseUiAttr);
        assertTrue(Model.getFacade().isProtected(baseUiAttr));
        assertEquals("unsigned long", Model.getFacade().getName(
            Model.getFacade().getType(baseUiAttr)));

        Object baseMakeMeADummyOper =
            findModelElementWithName(opers, "makeMeADummy");
        assertNotNull(
            "The Base::makeMeADummy() operation doesn't exit in the model!",
            baseMakeMeADummyOper);
        assertTrue(Model.getFacade().isProtected(baseMakeMeADummyOper));
        assertEquals(dummyStruct, Model.getFacade().getType(
            Model.getCoreHelper().getReturnParameter(baseMakeMeADummyOper)));

        Object baseHelperMethodOper =
            findModelElementWithName(opers, "helperMethod");
        assertNotNull(
            "The Base::helperMethod(xxx) operation doesn't exist in the model!",
            baseHelperMethodOper);
        assertEquals("void", Model.getFacade()
                .getName(
                    Model.getFacade().getType(
                        Model.getCoreHelper().getReturnParameter(
                            baseHelperMethodOper))));
        assertTrue(Model.getFacade().isPrivate(baseHelperMethodOper));
        params = Model.getFacade().getParameters(baseHelperMethodOper);
        Object baseHelperMethodCstrParam =
            findModelElementWithName(params, "cstr");
        assertNotNull("Base::helperMethod(xxx) cstr parameter doesn't exist!",
            baseHelperMethodCstrParam);
        assertEquals("signed char", Model.getFacade().getName(
            Model.getFacade().getType(baseHelperMethodCstrParam)));
        assertTaggedValueExistsAndValueIs(baseHelperMethodCstrParam,
            ProfileCpp.TV_NAME_POINTER, "true");
        assertEquals("inout", Model.getFacade().getName(
            Model.getFacade().getKind(baseHelperMethodCstrParam)));

        // verify Derived reveng
        Object derivedClass = findModelElementWithName(classes, "Derived");
        assertNotNull("The Derived class doesn't exist in the model!",
            derivedClass);
        assertNull(Model.getFacade().getTaggedValue(derivedClass,
            ProfileCpp.TV_NAME_CLASS_SPECIFIER));
        // verify generatization relationship
        Collection derivedGeneralizations =
            Model.getFacade().getGeneralizations(derivedClass);
        assertEquals(1, derivedGeneralizations.size());
        Object baseGeneralization = derivedGeneralizations.iterator().next();
        assertNotNull("The Base generalization wasn't found!",
            baseGeneralization);
        assertEquals("Derived", Model.getFacade().getName(
            Model.getFacade().getChild(baseGeneralization)));
        assertEquals("Base", Model.getFacade().getName(
            Model.getFacade().getParent(baseGeneralization)));
        assertEquals("false", Model.getFacade().getTaggedValueValue(
            baseGeneralization, ProfileCpp.TV_VIRTUAL_INHERITANCE));
        assertTaggedValueExistsAndValueIs(baseGeneralization,
            ProfileCpp.TV_INHERITANCE_VISIBILITY, "public");
        // verify Derived constructor
        Collection derivedOpers =
            Model.getCoreHelper().getBehavioralFeatures(derivedClass);
        Object derivedCtor = findModelElementWithName(derivedOpers, "Derived");
        assertNotNull("The Derived constructor wasn't found!", derivedCtor);
        Collection derivedCtorStereotypes =
            Model.getFacade().getStereotypes(derivedCtor);
        assertNotNull(
                findModelElementWithName(derivedCtorStereotypes, "create"));
        // verify Derived destructor
        Object derivedDtor = findModelElementWithName(derivedOpers, "~Derived");
        assertNotNull("The Derived destructor wasn't found!", derivedDtor);
        Collection derivedDtorStereotypes =
            Model.getFacade().getStereotypes(derivedDtor);
        assertNotNull(findModelElementWithName(derivedDtorStereotypes,
            "destroy"));

        // TODO: function bodies as UML Methods
    }

    /**
     * Assert that a tagged value exists in a model element and that its value
     * is equal to the given value.
     *
     * @param me the model element to check
     * @param tvName name of the tagged value
     * @param tvValue value of the tagged value
     */
    private void assertTaggedValueExistsAndValueIs(Object me, String tvName,
            String tvValue) {
        Object tv = Model.getFacade().getTaggedValue(me, tvName);
        assertNotNull("The tagged value " + tvName
            + " doesn't exist for the model element " + me, tv);
        assertEquals("The tagged value value is different from the expected!",
            tvValue, Model.getFacade().getValueOfTag(tv));
    }

    /**
     * When a file must be reversed, it must be open by the CppImport,
     * therefore, a copy of the file name given, which is a resource in the
     * package of this class, must be prepared. The <code>File</code> object
     * for this copy is returned, with its absolute path set.
     *
     * @param fn name of the source file which exists as a resource within the
     *            package of this class
     * @return the <code>File</code> object for the copy of the source file
     * @throws IOException if there are problems finding or reading the file.
     */
    private File setupSrcFile4Reverse(String fn) throws IOException {
        InputStream in = getClass().getResourceAsStream(fn);
        File srcFile = new File(genDir, fn);
        OutputStream out = null;
        try {
            out = new FileOutputStream(srcFile);
        } catch (FileNotFoundException e) {
            in.close();
            throw e;
        }
        try {
            CopyUtils.copy(in, out);
        } finally {
            in.close();
            out.close();
        }
        return srcFile;
    }

    /**
     * Find in a <code>Collection</code> of model elements one with the
     * specified name.
     *
     * @param mes the model elements in which to search
     * @param meName simple name of the ME
     * @return the ME if found or null
     */
    private Object findModelElementWithName(Collection mes, String meName) {
        Iterator it = mes.iterator();
        Object me = null;
        while (it.hasNext()) {
            Object possibleME = it.next();
            if (meName.equals(Model.getFacade().getName(possibleME))) {
                me = possibleME;
                break;
            }
        }
        return me;
    }

    /**
     * Test two passes - call twice the
     * {@link CppImport#parseFile(Project, Object, DiagramInterface, Import)
     * CppImport.parseFile(xxx)}
     * method on the same translation unit. The model elements shouldn't get
     * duplicated.
     *
     * @throws Exception something went wrong
     */
    public void testCallParseFileTwiceCheckingNoDuplicationOfModelElements()
        throws Exception {
        genDir = setUpDirectory4Test("testParseFileSimpleClass");
        File srcFile = setupSrcFile4Reverse("SimpleClass.cpp");

        cppImp.parseFile(proj, srcFile, di, imp);
        cppImp.parseFile(proj, srcFile, di, imp); // 2nd call on purpose!

        Collection nss =
            Model.getModelManagementHelper().getAllNamespaces(proj.getModel());
        Object pack = getModelElementAndAssertNotDuplicated(nss, "pack");

        Collection clss = Model.getCoreHelper().getAllClasses(pack);
        Object simpleClass =
            getModelElementAndAssertNotDuplicated(clss, "SimpleClass");

        Collection opers =
            Model.getCoreHelper().getBehavioralFeatures(simpleClass);
        getModelElementAndAssertNotDuplicated(opers, "newOperation");

        Collection attrs = Model.getCoreHelper().getAllAttributes(simpleClass);
        getModelElementAndAssertNotDuplicated(attrs, "newAttr");
    }

    /**
     * @param modelElements collection of model elements in which to look for
     * @param modelElementName the model element name
     * @return the model element with the given name
     */
    private Object getModelElementAndAssertNotDuplicated(
            Collection modelElements, String modelElementName) {
        Object pack = findModelElementWithName(modelElements, modelElementName);
        Collection mes2 = new ArrayList(modelElements);
        assertTrue(mes2.remove(pack));
        assertNull(findModelElementWithName(mes2, modelElementName));
        return pack;
    }
}
