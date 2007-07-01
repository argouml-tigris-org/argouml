// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.critics;

import java.util.ArrayList;
import java.util.List;

/**
 * Critic to check whether the name of a ModelElement in the Model resembles or
 * matches a Java keyword.
 * 
 * @author Tom Morris
 */
public class CrReservedNameJava extends CrReservedName {

    /**
     * Constructor.
     */
    public CrReservedNameJava() {
        super(getJavaNames());
    }

    /**
     * Return a list of Java keywords and reserved names.
     * @return the list of names
     */
    private static List<String> getJavaNames() {

        List<String> javaReserved = new ArrayList<String>();

        javaReserved.add("public");
        javaReserved.add("private");
        javaReserved.add("protected");
        javaReserved.add("package");
        javaReserved.add("import");
        javaReserved.add("java");
        javaReserved.add("class");
        javaReserved.add("interface");
        javaReserved.add("extends");
        javaReserved.add("implements");
        javaReserved.add("native");
        javaReserved.add("boolean");
        javaReserved.add("void");
        javaReserved.add("int");
        javaReserved.add("char");
        javaReserved.add("float");
        javaReserved.add("long");
        javaReserved.add("short");
        javaReserved.add("byte");
        javaReserved.add("double");
        javaReserved.add("String");
        javaReserved.add("Vector");
        javaReserved.add("Hashtable");
        javaReserved.add("Properties");

        javaReserved.add("null");
        javaReserved.add("true");
        javaReserved.add("false");
        javaReserved.add("rest");
        javaReserved.add("operator");
        javaReserved.add("inner");
        javaReserved.add("outer");
        javaReserved.add("this");
        javaReserved.add("super");
        javaReserved.add("byvalue");
        javaReserved.add("cast");
        javaReserved.add("const");
        javaReserved.add("future");
        javaReserved.add("generic");
        javaReserved.add("goto");
        javaReserved.add("throws");
        javaReserved.add("try");
        javaReserved.add("catch");
        javaReserved.add("finally");
        javaReserved.add("new");

        javaReserved.add("synchronized");
        javaReserved.add("static");
        javaReserved.add("final");
        javaReserved.add("abstract");
        javaReserved.add("for");
        javaReserved.add("if");
        javaReserved.add("else");
        javaReserved.add("while");
        javaReserved.add("return");
        javaReserved.add("continue");
        javaReserved.add("break");
        javaReserved.add("do");
        javaReserved.add("until");
        javaReserved.add("switch");
        javaReserved.add("case");
        javaReserved.add("default");
        javaReserved.add("instanceof");
        javaReserved.add("var");
        javaReserved.add("volatile");
        javaReserved.add("transient");

        javaReserved.add("assert");

        return javaReserved;
    }

}

