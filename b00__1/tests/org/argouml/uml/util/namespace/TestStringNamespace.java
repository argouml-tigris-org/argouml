// $Id:TestStringNamespace.java 10734 2006-06-11 15:43:58Z mvw $
// Copyright (c) 2003-2006 The Regents of the University of California. All
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

package org.argouml.uml.util.namespace;

import junit.framework.*;

/**
 * Tests for the StringNamespace class.
 *
 * @author mkl
 *
 */
public class TestStringNamespace extends TestCase
{
    /**
     * The constructor.
     *
     * @param testName the name of the test
     */
    public TestStringNamespace(java.lang.String testName)
    {
        super(testName);
    }

    /**
     * @param args the arguments given on the commandline
     */
    public static void main(java.lang.String[] args)
    {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * @return the test suite
     */
    public static Test suite()
    {
        TestSuite suite = new TestSuite(TestStringNamespace.class);

        return suite;
    }

    /**
     * Test getCommonNamespace().
     */
    public void testGetCommonNamespace()
    {
        StringNamespace sns1 =
            new StringNamespace(
                new String[] {"org", "argouml", "model" },
                Namespace.UML_NS_TOKEN);

        StringNamespace sns2 =
            new StringNamespace(
                new String[] {"org", "argouml", "model" },
                Namespace.UML_NS_TOKEN);

        StringNamespace result =
            (StringNamespace) sns1.getCommonNamespace(sns2);
        assertEquals("org::argouml::model", result.toString());

        sns1.popNamespaceElement();
        result = (StringNamespace) sns1.getCommonNamespace(sns2);
        assertEquals("org::argouml", result.toString());

        sns1.popNamespaceElement();
        result = (StringNamespace) sns1.getCommonNamespace(sns2);
        assertEquals("org", result.toString());

        sns1.popNamespaceElement();
        result = (StringNamespace) sns1.getCommonNamespace(sns2);
        assertEquals("", result.toString());

    }

    /**
     * Test parse() with a Java token.
     */
    public void testParseWithJavaToken()
    {
        StringNamespace sns =
            (StringNamespace) StringNamespace.parse(
                "org.argouml.model.",
                Namespace.JAVA_NS_TOKEN);

        assertEquals(sns.toString(), "org.argouml.model");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "org.argouml");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "org");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "");

        assertTrue(sns.isEmpty());
    }

    /**
     * Test parse() with a UML token.
     */
    public void testParseWithUMLToken()
    {
        StringNamespace sns =
            (StringNamespace) StringNamespace.parse(
                "org::argouml::model",
                Namespace.UML_NS_TOKEN);

        assertEquals(sns.toString(), "org::argouml::model");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "org::argouml");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "org");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "");

        assertTrue(sns.isEmpty());
    }

    /**
     * Test parse() with an esoteric token.
     */
    public void testParseWithEsotericToken()
    {
        StringNamespace sns =
            (StringNamespace) StringNamespace.parse(
                "org:!argouml:!m:odel",
                ":!");

        assertEquals(sns.toString(), "org:!argouml:!m:odel");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "org:!argouml");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "org");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "");

        assertTrue(sns.isEmpty());
    }

    /**
     * Class to test for String toString(String)
     */
    public void testToString()
    {
        StringNamespace sns = new StringNamespace("::");

        sns.pushNamespaceElement("org");
        assertEquals(sns.toString(), "org");

        sns.pushNamespaceElement("argouml");
        assertEquals(sns.toString(), "org::argouml");

        sns.popNamespaceElement();
        assertEquals(sns.toString(), "org");

    }

}
