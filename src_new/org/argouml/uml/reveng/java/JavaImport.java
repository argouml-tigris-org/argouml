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

package org.argouml.uml.reveng.java;

import org.apache.log4j.Logger;
import org.argouml.kernel.*;
import org.argouml.uml.reveng.*;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;

import java.io.*;

/**
 * This is the main class for Java reverse engineering. It's based
 * on the Antlr Java example.
 *
 * @author Andreas Rueckert <a_rueckert@gmx.net>
 */
public class JavaImport extends FileImportSupport {

    /** logger */
    private static final Logger LOG = Logger.getLogger(JavaImport.class);

    /**
     * This method parses 1 Java file.
     * Throws a Parser exception.
     *
     * @see org.argouml.application.api.PluggableImport#parseFile(
     * org.argouml.kernel.Project, java.lang.Object,
     * org.argouml.uml.reveng.DiagramInterface, org.argouml.uml.reveng.Import)
     */
    public void parseFile(Project p, Object o, DiagramInterface diagram,
			  Import theImport)
	throws Exception {
	if (o instanceof File) {
	    File f = (File) o;
	    // Create a scanner that reads from the input stream passed to us
	    String encoding = theImport.getInputSourceEncoding();
	    FileInputStream in = new FileInputStream(f);
	    JavaLexer lexer =
		new JavaLexer(
		    new BufferedReader(new InputStreamReader(in, encoding)));
	    // We use a special Argo token, that stores the preceding
	    // whitespaces.
	    lexer.setTokenObjectClass("org.argouml.uml.reveng.java.ArgoToken");

	    // Create a parser that reads from the scanner
	    JavaRecognizer parser = new JavaRecognizer(lexer);

	    // Create a modeller for the parser
	    Modeller modeller = new Modeller(p.getModel(),
					     diagram, theImport,
					     getAttribute().isSelected(),
					     getDatatype().isSelected(),
					     f.getName());

	    // Print the name of the current file, so we can associate
	    // exceptions to the file.
	    LOG.info("Parsing " + f.getAbsolutePath());

            modeller.setAttribute("level", theImport.getAttribute("level"));

            try {
		// start parsing at the compilationUnit rule
		parser.compilationUnit(modeller, lexer);
            } catch (Exception e) {
                LOG.error(e.getClass().getName()
			  + " Exception in file: "
			  + f.getCanonicalPath() + " "
			  + f.getName(), e);
                throw e;
            }
	    in.close();
	}
    }

    /**
     * Provides an array of suffix filters for the module.
     * @return SuffixFilter[] files with these suffixes will be processed.
     */
    public SuffixFilter[] getSuffixFilters() {
	SuffixFilter[] result = {FileFilters.JAVA_FILE_FILTER};
	return result;
    }

    /**
     * Display name of the module.
     *
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() {
	return "Java";
    }

    /**
     * Textual description of the module.
     *
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() {
	return "Java import from files";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() {
	return "module.import.java-files";
    }

}
