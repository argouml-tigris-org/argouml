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

//$Id$

package org.argouml.uml.reveng.java;

import org.argouml.kernel.*;
import org.argouml.uml.reveng.*;
import org.argouml.application.api.*;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;

import java.io.*;

/**
 * This is the main class for Java reverse engineering. It's based
 * on the Antlr Java example.
 *
 * $Revision$
 * $Date$
 *
 * @author Andreas Rueckert <a_rueckert@gmx.net>
 */
public class JavaImport extends FileImportSupport {

    /**
     * This method parses 1 Java file.
     *
     * @param f The input file for the parser.
     * @exception Exception Parser exception.
     */
    public void parseFile( Project p, Object o, DiagramInterface diagram, Import _import)
	throws Exception {
		if (o instanceof File ) {
			File f = (File)o;
			// Create a scanner that reads from the input stream passed to us
			JavaLexer lexer = new JavaLexer(new BufferedReader(new FileReader(f)));

			// We use a special Argo token, that stores the preceding
			// whitespaces.
			lexer.setTokenObjectClass( "org.argouml.uml.reveng.java.ArgoToken");

			// Create a parser that reads from the scanner
			JavaRecognizer parser = new JavaRecognizer( lexer);

			// Create a modeller for the parser
			Modeller modeller = new Modeller(p.getModel(),
                                         diagram, _import,
					 attribute.isSelected(),
					 datatype.isSelected(),
                                         f.getName());

			// Print the name of the current file, so we can associate
			// exceptions to the file.
			Argo.log.info("Parsing " + f.getAbsolutePath());

			// start parsing at the compilationUnit rule
			parser.compilationUnit(modeller, lexer);
		}
    }

	/** 
	 * Provides an array of suffix filters for the module.
	 * @return SuffixFilter[] files with these suffixes will be processed.
	 */
	public SuffixFilter[] getSuffixFilters() {
		SuffixFilter[] result = {FileFilters.JavaFilter};
		return result;
	}
	
		/** Display name of the module. */
		public String getModuleName() {
			return "Java";
		}

		/** Textual description of the module. */
		public String getModuleDescription() {
			return "Java import from files";
		}

		public String getModuleKey() {
			return "module.import.java-files";
		}

}







