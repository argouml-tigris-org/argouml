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

package org.argouml.language.java.cognitive.critics;

import java.util.ArrayList;
import java.util.List;

import org.argouml.uml.cognitive.critics.CrReservedName;

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

        // The following are in the same order they appear in java.g
        
        // package definition and imports start everything off
        javaReserved.add("package");
        javaReserved.add("import");

        javaReserved.add("extends");
        javaReserved.add("super");
        
        // primitive data types
        javaReserved.add("void");
        javaReserved.add("boolean");
        javaReserved.add("byte");
        javaReserved.add("char");
        javaReserved.add("short");
        javaReserved.add("int");
        javaReserved.add("float");
        javaReserved.add("long");
        javaReserved.add("double");
        
        javaReserved.add("interface");
        
        // modifiers
        javaReserved.add("private");
        javaReserved.add("public");
        javaReserved.add("protected");
        javaReserved.add("static");
        javaReserved.add("transient");
        javaReserved.add("final");
        javaReserved.add("abstract");
        javaReserved.add("native");
        javaReserved.add("threadsafe");        
        javaReserved.add("synchronized");
        javaReserved.add("const");
        javaReserved.add("volatile");
        javaReserved.add("strictfp");

        javaReserved.add("class");
        javaReserved.add("extends");
        javaReserved.add("implements");        
        
        javaReserved.add("enum");
        
        javaReserved.add("this");
        // super already handled
        
        javaReserved.add("new");
        javaReserved.add("throws");

        javaReserved.add("if");
        javaReserved.add("else");
        javaReserved.add("for");
        javaReserved.add("while");
        javaReserved.add("do");
        javaReserved.add("break");
        javaReserved.add("continue");
        javaReserved.add("return");
        javaReserved.add("switch");
        javaReserved.add("case");
        javaReserved.add("default");

        javaReserved.add("try");
        javaReserved.add("catch");
        javaReserved.add("finally");
        javaReserved.add("throw");
        
        javaReserved.add("assert");

        javaReserved.add("instanceof");

        javaReserved.add("true");
        javaReserved.add("false");
        javaReserved.add("null");

        // new already handled

        ////////////// end of Java grammar keywords //////////////////
        
        // Some common java.* classes that we've historically checked for
        // TODO: We should probably check for all or none
        javaReserved.add("String");
        javaReserved.add("Vector");
        javaReserved.add("Hashtable");
        javaReserved.add("Properties");        

        // The rest of these aren't reserved Java words, but they were 
        // historically things that were checked for.  Disabled for now to
        // minimize false alarms - tfm 20080506
//        javaReserved.add("java");
//        javaReserved.add("until");
//        javaReserved.add("var");        
//        javaReserved.add("rest");
//        javaReserved.add("operator");
//        javaReserved.add("inner");
//        javaReserved.add("outer");
//        javaReserved.add("byvalue");
//        javaReserved.add("cast");
//        javaReserved.add("future");
//        javaReserved.add("generic");
//        javaReserved.add("goto");

        return javaReserved;
    }

}

