// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.uml.reveng.classfile;

import java.io.*;
import org.argouml.kernel.*;
import org.argouml.application.api.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.static_structure.layout.*;
import org.argouml.uml.reveng.*;
import org.argouml.uml.reveng.classfile.ui.*;
import org.tigris.gef.base.*;
import ru.novosoft.uml.model_management.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.jar.*;
import java.util.zip.*;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;
import org.argouml.util.ClassFileFilter;


/**
 * This is the main class for the classfile import.
 *
 * $Revision$
 * $Date$
 *
 * @author Andreas Rueckert <a_rueckert@gmx.net>
 */
public class ClassfileImport extends FileImportSupport {

    /////////////////////////////////////////////////////////
    // Instance variables

    // The instance for a singleton pattern.
    private static ClassfileImport _INSTANCE = new ClassfileImport();

    // Create a interface to the current diagram
    org.argouml.uml.reveng.DiagramInterface _diagram;// = new org.argouml.uml.reveng.DiagramInterface(Globals.curEditor());

    //private JComponent   configPanel = null;
    //private JCheckBox    _descend;
    //private JRadioButton _attribute;
    //private JRadioButton _datatype;

    /** The files that needs a second RE pass. */
    private ArrayList _secondPassFiles;

    // The current project.
    private Project _currentProject = null;

    // A status window for the import process.
    //private RevEngStatusDialog _status = null;

    private Import _import;
 
    ///////////////////////////////////////////////////////
    // Methods

    /**
     * Return the singleton instance of the Import class.
     *
     * @return The only instance of this class.
     */
    public static ClassfileImport getInstance() {
	return _INSTANCE;
    }

    /**
     * Get the panel that lets the user set reverse engineering
     * parameters.
     *
    public JComponent getConfigPanel() {

	if(configPanel == null) {
	    JPanel general = new JPanel();
	    general.setLayout(new GridBagLayout());

	    _descend = new JCheckBox("Descend directories recursively.");
	    _descend.setSelected(true);
	    general.add(_descend,
			new GridBagConstraints(GridBagConstraints.RELATIVE,
					       GridBagConstraints.RELATIVE,
					       GridBagConstraints.REMAINDER,
					       GridBagConstraints.REMAINDER,
					       1.0, 1.0,
					       GridBagConstraints.NORTHWEST,
					       GridBagConstraints.NONE,
					       new Insets(5, 5, 5, 5),
					       0, 0));

	    JTabbedPane tab = new JTabbedPane();
	    tab.add(general, "General");

	    JPanel java = new JPanel();
            java.setLayout(new GridBagLayout());

            JLabel attributeLabel = new JLabel("Java attributes modelled as");
            java.add(attributeLabel,
                            new GridBagConstraints(GridBagConstraints.RELATIVE,
                                                   GridBagConstraints.RELATIVE,
                                                   GridBagConstraints.REMAINDER,
                                                   1,
                                                   1.0, 0.0,
                                                   GridBagConstraints.NORTHWEST,
                                                   GridBagConstraints.NONE,
                                                   new Insets(5, 5, 0, 5),
                                                   0, 0));
            ButtonGroup group1 = new ButtonGroup();
            _attribute =
                new JRadioButton("UML attributes.");
            _attribute.setSelected(true);
            group1.add(_attribute);
            java.add(_attribute,
                      new GridBagConstraints(GridBagConstraints.RELATIVE,
                                             GridBagConstraints.RELATIVE,
                                             GridBagConstraints.REMAINDER,
                                             1,
                                             1.0, 0.0,
                                             GridBagConstraints.NORTHWEST,
                                             GridBagConstraints.NONE,
                                             new Insets(0, 5, 0, 5),
                                             0, 0));
            JRadioButton association =
                new JRadioButton("UML associations.");
            group1.add(association);
            java.add(association,
                      new GridBagConstraints(GridBagConstraints.RELATIVE,
                                             GridBagConstraints.RELATIVE,
                                             GridBagConstraints.REMAINDER,
                                             1,
                                             1.0, 0.0,
                                             GridBagConstraints.NORTHWEST,
                                             GridBagConstraints.NONE,
                                             new Insets(0, 5, 5, 5),
                                             0, 0));

            ButtonGroup group2 = new ButtonGroup();
            _datatype =
                new JRadioButton("Arrays modelled as datatypes.");
            _datatype.setSelected(true);
            group2.add(_datatype);
            java.add(_datatype,
                      new GridBagConstraints(GridBagConstraints.RELATIVE,
                                             GridBagConstraints.RELATIVE,
                                             GridBagConstraints.REMAINDER,
                                             1,
                                             1.0, 0.0,
                                             GridBagConstraints.NORTHWEST,
                                             GridBagConstraints.NONE,
                                             new Insets(5, 5, 0, 5),
                                             0, 0));
            JRadioButton multi =
                new JRadioButton("Arrays modelled with multiplicity 1..n.");
            group2.add(multi);
            java.add(multi,
                      new GridBagConstraints(GridBagConstraints.RELATIVE,
                                             GridBagConstraints.RELATIVE,
                                             GridBagConstraints.REMAINDER,
                                             GridBagConstraints.REMAINDER,
                                             1.0, 1.0,
                                             GridBagConstraints.NORTHWEST,
                                             GridBagConstraints.NONE,
                                             new Insets(0, 5, 5, 5),
                                             0, 0));

	    tab.add(java, "Java classfile");
	    configPanel = tab;
	}
	return configPanel;
    }
    */
    
    public void parseFile( Project p, Object o, DiagramInterface diagram, Import _import)
	throws Exception {
		if (o instanceof File ) {
                    File f = (File)o;
                    this._diagram = diagram;
                    this._import = _import;
                    startImport(p, f);
                }
    }
    
    /**
     * Start the import process for a project and a file.
     *
     * @param p The project, where the import results are added.
     * @param f The file to start with.
     */
    public void startImport(Project p, File f) throws Exception {
	_secondPassFiles = new ArrayList();
	_currentProject = p;

	// Create a new status window and pass the total number of files to import.
	//_status = new RevEngStatusDialog();
	//_status.show();

	// Start the 1st pass.
	//_status.startPass("Starting first pass", countFiles(f, true));

	// Process the current file. If it's a directory, process all the file in it.
	processFile(f, true);

	// End first pass.
	//_status.endPass("First pass completed");

	int secondPassCount = count2ndPassFiles(_secondPassFiles);

        if(secondPassCount > 0) {

	    //_status.startPass("Reparse files with unknown types", secondPassCount);

	    // Process all the files, that need a second pass.
	    for(Iterator i = _secondPassFiles.iterator(); i.hasNext();) {
		Object next = i.next();

		if(next instanceof ArrayList) {
		    do2ndJarPass((ArrayList)next);
		} else {
		    File nextFile = (File)next;
                    String fileName = nextFile.getName();
		    //_status.startFile(fileName);
		    do2ndFilePass(new FileInputStream(nextFile), fileName);
		    //_status.endFile();
		}
	    }

	    //_status.endPass("Reparse completed");
	}

	// Layout the modified diagrams.
	for(Enumeration e = _diagram.getModifiedDiagrams().elements(); e.hasMoreElements(); ) {
	    ClassdiagramLayouter layouter = new ClassdiagramLayouter((UMLDiagram)e.nextElement());
	    layouter.layout();

	    // Resize the diagram???
	}

	// Let the use close the status window.
	//_status.importCompleted();
    }

    /**
     * Count all the files, we're going to process, so
     * we can display a progress bar.
     *
     * @return The number of files to process
     */
    private int countFiles(File f, boolean subdirectories) throws Exception {
	if( f.isDirectory() && subdirectories) {
	    return countDirectory(f);
	} else {
	    if(f.getName().endsWith(".class")) {
		return 1;
	    } else {
		int total = 0;
		if(f.getName().endsWith(".jar")) {
		    for( Enumeration e=(new JarFile(f)).entries(); e.hasMoreElements(); ) {
			ZipEntry entry = (ZipEntry)e.nextElement();
			if( ! entry.isDirectory() && entry.getName().endsWith(".class")) {
			    total++;
			}
		    }
		}
		return total;
	    }
	}
    }

    /**
     * Count the files to process in a directory.
     *
     * @param f The directory as a file instance.
     */
    private int countDirectory(File f) throws Exception {
	int total = 0;
	String [] files = f.list();  // Get the content of the directory

	for( int i = 0; i < files.length; i++) {
	    //total += countFiles( new File( f, files[i]), _descend.isSelected());
	    total += countFiles( new File( f, files[i]), _import.isDiscendDirectoriesRecursively());
            
        }

	return total;
    }

    /**
     * Count the files in the 2nd pass buffer.
     *
     * @param buffer The buffer with the files for the 2nd pass.
     */
    private int count2ndPassFiles(ArrayList buffer) {
	int nfiles = 0;

	for(Iterator i = _secondPassFiles.iterator(); i.hasNext();) {
	    Object next = i.next();
	    nfiles += ((next instanceof ArrayList) ? ((ArrayList)next).size() - 1 : 1);
	}
	return nfiles;
    }

    /**
     * The main method for all parsing actions. It calls the
     * actual parser methods depending on the type of the
     * file.
     *
     * @param f The file or directory, we want to parse.
     * @exception Parser exceptions.
     */
    public void processFile(File f, boolean subdirectories) throws Exception {
        
	if ( f.isDirectory() && subdirectories) {    // If f is a directory and the subdirectory flag is set,
	    processDirectory(f);                     // import all the files in this directory
	} else {
	    // Is this file a Jarfile?
	    if( f.getName().endsWith(".jar")) {
		processJarFile(f);
	    } else {
		if( f.getName().endsWith(".class")) {
                    String fileName = f.getName();
		    //_status.startFile(fileName);          // Show the filename in the status window.
		    try {
			parseFile(new FileInputStream(f), fileName);   // Try to parse this file.
		    } catch(Exception e1) {
			// System.out.println("Exception: " + e1.getMessage());
			// e1.printStackTrace();
			_secondPassFiles.add(f);
		    }
		    //_status.endFile();
		}
	    }
	}
    }

    /**
     * This method imports an entire directory. It calls the parser for
     * files and creates packages for the directories.
     *
     * @param f The directory.
     *
     * @exception Parser exceptions.
     */
    protected void processDirectory(File f) throws Exception {
	boolean doSubdirs = _import.isDiscendDirectoriesRecursively();

	//_status.enterDirectory(f.getName());

	String [] files = f.list();  // Get the content of the directory

	for( int i = 0; i < files.length; i++) {
	    processFile( new File( f, files[i]), doSubdirs);
	}

	//_status.leaveDirectory();
    }

    /**
     * Process a Jar file, that contains classfiles.
     *
     * @param f The Jar file.
     */
    private void processJarFile(File f) throws Exception {
	JarFile jarfile = new JarFile(f);
	ArrayList jar_secondPassFiles = new ArrayList();  // A second pass buffer just for this jar.

	//_status.openJar(f.getName());  // Show the opening of the jar file in the status window.

	for( Enumeration e=jarfile.entries(); e.hasMoreElements(); ) {
	    ZipEntry entry = (ZipEntry)e.nextElement();
	    String entryName = entry.getName();
	    if( ! entry.isDirectory() && entryName.endsWith(".class")) {
		//_status.startFile(entryName);          // Show the filename in the status window.
		try {
		    parseFile(jarfile.getInputStream(entry), entryName);
		} catch(Exception e1) {
		    if(jar_secondPassFiles.isEmpty()) {     // If there are no files tagged for a second pass,
			jar_secondPassFiles.add(f);   // add the jar files as the 1st element.
		    }
		    System.out.println("Exception in " + entryName + " : " + e1.getMessage());
		    e1.printStackTrace();
		    jar_secondPassFiles.add(entryName);  // Store the entry to be parsed a 2nd time.
		}
		//_status.endFile();

		Thread.sleep(10);
	    }
	}

	//_status.closeJar();  // Show the closing of the jar file.

	// If there are files to parse again, add the jar to the 2nd pass.
	if( ! jar_secondPassFiles.isEmpty()) {
	    _secondPassFiles.add(jar_secondPassFiles);
        }

	jarfile.close();
    }

    /**
     * Do a 2nd pass on a Jar file.
     *
     * @param secondPassBuffer A buffer, that holds the jarfile and
     *                         the names of the entries to parse again.
     */
    private void do2ndJarPass(ArrayList secondPassBuffer) throws Exception {
	if( ! secondPassBuffer.isEmpty()) {
	    Iterator iterator = secondPassBuffer.iterator();
	    JarFile jarfile = new JarFile( (File)iterator.next());

	    //_status.openJar(jarfile.getName());  // Show the opening of the jar file in the status window.

	    while(iterator.hasNext()) {
		String filename = (String)iterator.next();
		//_status.startFile(filename);
		do2ndFilePass(jarfile.getInputStream(jarfile.getEntry(filename)), filename);
		//_status.endFile();
	    }
	    //_status.closeJar();  // Show the closing of the jar file.
	    jarfile.close();
	}
    }

    /**
     * Parse a file for 2nd time. The main difference is, that the exception are printed,
     * instead of storing the file for a 2nd pass.
     *
     * @param is The input stream of the file.
     */
    private void do2ndFilePass(InputStream is, String fileName) {
	try {                    // Try to parse the file.
	    parseFile(is, fileName);
	} catch(Exception e2) {    // If there were errors, show them.
	    System.out.println("ERROR: " + e2.getMessage());
	    e2.printStackTrace();
	}
    }

    /**
     * This method parses 1 Java classfile.
     *
     * @param is The inputStream for the file to parse.
     *
     * @exception Parser exception.
     */
    public void parseFile(InputStream is, String fileName) throws Exception {

        ClassfileParser parser = new ClassfileParser(new SimpleByteLexer(new BufferedInputStream(is)));

        // start parsing at the classfile rule
        parser.classfile();

        // Create a modeller for the parser
        org.argouml.uml.reveng.java.Modeller modeller = new org.argouml.uml.reveng.java.Modeller((MModel)_currentProject.getModel(),
												 _diagram, _import,
												 attribute.isSelected(),
												 datatype.isSelected(), fileName);

	// do something with the tree
	ClassfileTreeParser tparser = new ClassfileTreeParser();
	tparser.classfile(parser.getAST(), modeller);

        // Was there an exception thrown during modelling?
        //Exception e = modeller.getException();
        //if(e != null) {
        //    throw e;
        //}
    }

    /**
     * If we have modified any diagrams, the project was modified and
     * should be saved. I don't consider a import, that only modifies
     * the metamodel, at this point (Andreas Rueckert <a_rueckert@gmx.net> ).
     * Calling Project.setNeedsSave(true) doesn't work here, because
     * Project.postLoad() is called after the import and it sets the
     * _needsSave flag to false.
     *
     * @return true, if any diagrams where modified and the project should be saved before exit.
     */
    public boolean needsSave() {
	return ( ! _diagram.getModifiedDiagrams().isEmpty());
    }
    
    /** Textual description of the module.  */
    public String getModuleDescription() {
	return "Java import from class or jar files";
    }
    
    public String getModuleKey() {
	return "module.import.java-classes";
    }

    public boolean initializeModule() {

	// Advertise a little
	Argo.log.info ("+---------------------------------------+");
	Argo.log.info ("| Java classfile import module enabled! |");
	Argo.log.info ("+---------------------------------------+");

        return true;
    }
    
    /** Display name of the module.  */
    public String getModuleName() {
        return "Java from classes";
    }
    
    /**
     * Provides an array of suffixe filters for the module.
     * Must be implemented in child class.
     * @return SuffixFilter[] suffixes for processing
     */
    public SuffixFilter[] getSuffixFilters() {
		SuffixFilter[] result = 
                    {FileFilters.JavaClassFilter, FileFilters.JavaJarFilter};
		return result;
    }
    
}












