 // Copyright (c) 1996-99 The Regents of the University of California. All
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

package org.argouml.uml.reveng.java;

import java.io.*;
import antlr.collections.AST;
import antlr.collections.impl.*;
import antlr.*;
import org.argouml.kernel.*;
import org.argouml.uml.reveng.*;
import ru.novosoft.uml.model_management.*;
import org.tigris.gef.base.*;

/**
 * This is the main class for Java reverse engineering. It's based
 * on the Antlr Java example.
 *
 * $Revision$
 * $Date$
 *
 * @author Andreas Rueckert <a_rueckert@gmx.net>
 */
public class JavaImport {

    /**
     * This method parses 1 Java file.
     *
     * @param f The input file for the parser.
     * @exception Parser exception.
     */
    public static void parseFile( Project p, File f) throws Exception {
	try {
	    // Create a scanner that reads from the input stream passed to us
	    JavaLexer lexer = new JavaLexer( new FileInputStream( f));

	    // We use a special Argo token, that stores the preceding whitespaces.
	    lexer.setTokenObjectClass( "org.argouml.uml.reveng.java.ArgoToken");     

	    // Create a parser that reads from the scanner
	    JavaRecognizer parser = new JavaRecognizer( lexer);

	    // Create a interface to the current diagram
	    DiagramInterface diagram = new DiagramInterface(Globals.curEditor());

	    // Create a modeller for the parser
	    Modeller modeller = new Modeller((MModel)p.getModel(), diagram);   

	    // Print the name of the current file, so we can associate exceptions
	    // to the file.
	    System.out.println("Starting java parser for file: " + f.getAbsolutePath());

	    // start parsing at the compilationUnit rule
	    parser.compilationUnit(modeller, lexer);
	} catch ( Exception e) {
	    System.err.println("parser exception: "+e);
	    e.printStackTrace();   // so we can get stack trace		
	}
    }
}







