// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import junit.framework.TestCase;

import org.argouml.model.uml.CoreFactory;
import org.argouml.model.uml.UmlFactory;

/**
 * The Base class of all the TestCases for the GeneratorCpp class.
 * @see GeneratorCpp
 * @author euluis
 * @since 0.17.2
 */                                
class BaseTestGeneratorCpp extends TestCase {
    /**
     * The constructor.
     * 
     * @param testName the name of the test
     */
    public BaseTestGeneratorCpp(String testName) {
        super(testName);
    }

    /** factory for model elements */
    protected CoreFactory factory;

    /** The venerable C++ generator instance used in the test fixtures. */
    protected GeneratorCpp generator;

    /** the AClass model element */
    protected Object aClass;

    /** the AClass::foo() operation */
    protected Object fooMethod;
    
    /*
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() {
        generator = GeneratorCpp.getInstance();
        factory = UmlFactory.getFactory().getCore();
        aClass = factory.buildClass("AClass");
        fooMethod = factory.buildOperation(aClass, "foo");
    }
}
