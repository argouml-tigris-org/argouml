// $Id$
// Copyright (c) 2004 The Regents of the University of California. All
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

package org.argouml.uml.reveng.idl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramLayouter;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.uml.reveng.DiagramInterface;
import org.argouml.uml.reveng.FileImportSupport;
import org.argouml.uml.reveng.Import;
import org.argouml.uml.reveng.java.Modeller;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;

/**
 * This is the main class for the IDL import.
 * 
 * @author Andreas Rueckert a_rueckert@gmx.net
 */
public class IDLFileImport extends FileImportSupport {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(IDLFileImport.class);

    /////////////////////////////////////////////////////////
    // Instance variables

    /**
     * The instance for a singleton pattern.
     */
    private static final IDLFileImport INSTANCE = new IDLFileImport();

    /**
     * An interface to the current diagram.
     */ 
    private DiagramInterface diagram;

    /** The files that needs a second RE pass. */
    private ArrayList secondPassFiles;

    // The current project.
    private Project currentProject = null;

    private Import myImport;

    /**
     * Return the singleton instance of the Import class.
     * 
     * @return The only instance of this class.
     */
    public static IDLFileImport getInstance() {
	return INSTANCE;
    }

    /**
     * @see org.argouml.application.api.PluggableImport#parseFile(
     *         org.argouml.kernel.Project, java.lang.Object, 
     *         org.argouml.uml.reveng.DiagramInterface, 
     *         org.argouml.uml.reveng.Import)
     */
    public void parseFile(
			  Project p,
			  Object o,
			  DiagramInterface theDiagram,
			  Import theImport)
	throws Exception {
	if (o instanceof File) {
	    File f = (File) o;
	    diagram = theDiagram;
	    myImport = theImport;
	    startImport(p, f);
	}
    }

    /**
     * Start the import process for a project and a file.
     * 
     * @param p The project, where the import results are added.
     * @param f The file to start with.
     * @throws Exception if something goes wrong.
     */
    public void startImport(Project p, File f) throws Exception {
	secondPassFiles = new ArrayList();
	currentProject = p;

	// Process the current file. If it's a directory, process all the file
	// in it.
	processFile(f, true);

	// Layout the modified diagrams.
	for (Enumeration e = diagram.getModifiedDiagrams().elements();
	     e.hasMoreElements();
	     ) {
	    ClassdiagramLayouter layouter =
		new ClassdiagramLayouter((UMLDiagram) e.nextElement());
	    layouter.layout();

	    // Resize the diagram???
	}

	// Let the use close the status window.
	//_status.importCompleted();
    }

    /**
     * Count all the files, we're going to process, so we can display a
     * progress bar.
     *
     * @param f The directory as a File.
     * @param subdirectories If <tt>true</tt> we process subdirectories.
     * @return The number of files to process.
     * @throws Exception if something goes wrong.
     */
    private int countFiles(File f, boolean subdirectories) throws Exception {
	if (f.isDirectory() && subdirectories) {
	    return countDirectory(f);
	} else {
	    if (f.getName().endsWith(".idl")) {
		return 1;
	    }
	    return 0;
	}
    }

    /**
     * Count the files to process in a directory.
     * 
     * @param f  The directory as a File.
     * @return The number of files in that directory.
     * @throws Exception if something goes wrong.
     */
    private int countDirectory(File f) throws Exception {
	int total = 0;
	String[] files = f.list(); // Get the content of the directory

	for (int i = 0; i < files.length; i++) {
	    total
		+= countFiles(
			      new File(f, files[i]),
			      myImport.isDiscendDirectoriesRecursively());

	}

	return total;
    }

    /**
     * The main method for all parsing actions. It calls the actual parser
     * methods depending on the type of the file.
     * 
     * @param f The file or directory, we want to parse.
     * @param subdirectories If <tt>true</tt> we process subdirectories.
     * @throws Exception Parser exceptions.
     */
    public void processFile(File f, boolean subdirectories) throws Exception {
		
	if (f.isDirectory()
	    && subdirectories) { // If f is a directory and the subdirectory
	    // flag is set,
	    processDirectory(f); // import all the files in this directory
	} else {
			
	    if (f.getName().endsWith(".idl")) {
		String fileName = f.getName();
		try {
		    parseFile(new FileInputStream(f), fileName);
		    // Try to parse this file.
		} catch (Exception e1) {
		    e1.printStackTrace();
		    secondPassFiles.add(f);
		}
				
	    }
	}
    }

    /**
     * This method imports an entire directory. It calls the parser for files
     * and creates packages for the directories.
     * 
     * @param f The directory.
     * 
     * @throws Exception Parser exceptions.
     */
    protected void processDirectory(File f) throws Exception {
	boolean doSubdirs = myImport.isDiscendDirectoriesRecursively();

	String[] files = f.list(); // Get the content of the directory

	for (int i = 0; i < files.length; i++) {
	    processFile(new File(f, files[i]), doSubdirs);
	}
    }


    /**
     * This method parses 1 Java classfile.
     * 
     * @param is The InputStream for the file to parse.
     * @param fileName The name of the parsed file.
     * @throws Exception Parser exception.
     */
    public void parseFile(InputStream is, String fileName) throws Exception {

	int lastSlash = fileName.lastIndexOf('/');
	if (lastSlash != -1) {
	    fileName = fileName.substring(lastSlash + 1);
	}

	IDLParser parser = 
	    new IDLParser(new IDLLexer(new BufferedInputStream(is)));

	// Create a modeller for the parser
	Modeller modeller = new Modeller(currentProject.getModel(),
	        			 diagram,
	        			 myImport,
	        			 getAttribute().isSelected(),
	        			 getDatatype().isSelected(),
	        			 fileName);
	// start parsing at the classfile rule
	parser.specification(modeller);
    }

    /**
     * If we have modified any diagrams, the project was modified and should be
     * saved. I don't consider a import, that only modifies the metamodel, at
     * this point (Andreas Rueckert). Calling
     * Project.setNeedsSave(true) doesn't work here, because Project.postLoad()
     * is called after the import and it sets the _needsSave flag to false.
     * 
     * @return true, if any diagrams where modified and the project should be
     *         saved before exit.
     */
    public boolean needsSave() {
	return (!diagram.getModifiedDiagrams().isEmpty());
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     *
     * Textual description of the module.
     */
    public String getModuleDescription() {
	return "Java import from IDL files";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() {
	return "module.import.idl";
    }

    /**
     * @see org.argouml.application.api.ArgoModule#initializeModule()
     */
    public boolean initializeModule() {

	// Advertise a little
	LOG.info("IDL import module enabled!");

	return true;
    }

    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     * 
     * Display name of the module.
     */
    public String getModuleName() {
	return "IDL";
    }

    /**
     * Provides an array of suffixe filters for the module. Must be implemented
     * in child class.
     * 
     * @return SuffixFilter[] suffixes for processing
     */
    public SuffixFilter[] getSuffixFilters() {
	SuffixFilter[] result = {
	    FileFilters.IDL_FILTER,
	};
	return result;
    }

}
