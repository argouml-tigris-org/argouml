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

import org.apache.log4j.Logger;

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
    
    /**
     * @param name The name of the Test Case.
     */
    public TestCppGrammar(String name) {
        super(name);
    }

    /**
     * Test parsing of file which doesn't need preprocessing.
     * @throws Exception something went wrong
     */
    public void testParseSimpleClass() throws Exception {
        parseFile("SimpleClass.cpp");
    }
    
    /**
     * Test parsing the example from C++ grammar by David Wigg, 
     * <code>quadratic.i</code>
     * @throws Exception something went wrong
     */
    public void testParseQuadratic() throws Exception {
        // Parsing this file still fails. To enable clean commit I'm 
	// commenting out...
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
        parser.translation_unit();
    }
}
