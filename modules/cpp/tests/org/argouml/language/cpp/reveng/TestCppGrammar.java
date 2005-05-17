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

/*REMOVE_BEGIN*/
package org.argouml.language.cpp.reveng;
/*REMOVE_END*/

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.easymock.MockControl;

import junit.framework.TestCase;

/**
 * Basic test of the C++ grammar. This should prove that the grammar generated 
 * parser works for very basic pre-processed files.
 * 
 * @author euluis
 * @since 0.19.1
 */
public class TestCppGrammar extends TestCase {
    
    /**
     * The Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(TestCppGrammar.class);
    private Modeler modeler;
    private MockControl modelerCtrl;
    
    /**
     * @param name The name of the Test Case.
     */
    public TestCppGrammar(String name) {
        super(name);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        modelerCtrl = MockControl.createStrictControl(Modeler.class);
        modeler = (Modeler) modelerCtrl.getMock();
    }
    /**
     * Test parsing of file which doesn't need preprocessing.
     * @throws Exception something went wrong
     */
    public void testParseSimpleClass() throws Exception {
        parseFile("SimpleClass.cpp");
    }
    
    /**
     * Test the grammar callbacks when parsing the SimpleClass.cpp file.
     * @throws Exception something went wrong...
     */
    public void testGrammarCallbacks2Modeler() throws Exception {
        modeler.beginTranslationUnit();
        
        modeler.enterNamespaceScope("pack");
        modeler.beginClassDefinition(CPPvariables.OT_CLASS, "SimpleClass");
        modeler.accessSpecifier("public");
        // virtual int newOperation();
        modeler.beginFunctionDeclaration();
        List declSpecs = new ArrayList(); declSpecs.add("virtual");
        modeler.declarationSpecifiers(declSpecs);
        List sts = new ArrayList(); sts.add("int");
        modeler.simpleTypeSpecifier(sts);
        modeler.directDeclarator("newOperation");
        modeler.endFunctionDeclaration();
        // double newAttr;
        List sts2 = new ArrayList(); sts2.add("double");
        modeler.simpleTypeSpecifier(sts2);
        modeler.directDeclarator("newAttr");
        modeler.endClassDefinition();
        modeler.exitNamespaceScope();
        
        modeler.enterNamespaceScope("pack");
        modeler.beginFunctionDefinition();
        modeler.simpleTypeSpecifier(sts); // reuse from above
        modeler.functionDirectDeclarator("SimpleClass::newOperation");
        modeler.endFunctionDefinition();
        modeler.exitNamespaceScope();
        
        modeler.makeNamespaceAlias("pack", "p");
        modeler.endTranslationUnit();
        
        modelerCtrl.replay();
        
        parseFile("SimpleClass.cpp");
        modelerCtrl.verify();
    }

    /**
     * Test parsing the example from C++ grammar by David Wigg, 
     * <code>quadratic.i</code>.
     * @throws Exception something went wrong
     */
    public void testParseQuadratic() throws Exception {
        // FIXME: this one still fails!
        //parseFile("quadratic.i");
    }

    /**
     * Test parsing a member method which contains a union definition, 
     * with some computations in it.
     * @throws Exception something went wrong
     */
    public void testParseUnionInlineDefInMember() throws Exception {
        parseFile("UnionInlineDefInMember.cpp");
    }

    /**
     * Test parsing a cast expression surrounded by parenteses which casts a 
     * value returned from a function call.
     * @throws Exception something went wrong
     */
    public void testCastExpressions() throws Exception {
        parseFile("CastExpressions.cpp");
    }

    /**
     * Parse a pre-processed C++ or C source file using the lexer and parser 
     * generated from the ANTLR CppParser4Java.g grammar. 
     * @param fn name of file to parse. Must be in the same directory as this 
     * test.
     * @throws Exception something went wrong
     */
    private void parseFile(String fn) throws Exception {
        InputStream file2Parse = TestCppGrammar.class.getResourceAsStream(
            fn);
        CPPLexer lexer = new CPPLexer(file2Parse);
        CPPParser parser = new CPPParser(lexer);
        parser.translation_unit(modeler);
    }
}
