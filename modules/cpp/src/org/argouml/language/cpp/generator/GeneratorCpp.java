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


// File: GeneratorCpp.java
// Classes: GeneratorCpp
// Original Author:
// $Id$


package org.argouml.language.cpp.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.server.UID;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import org.argouml.application.api.Argo;
import org.argouml.application.api.Notation;
import org.argouml.application.api.PluggableNotation;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlHelper;
import org.argouml.uml.DocumentationManager;
import org.argouml.uml.generator.FileGenerator;
import org.argouml.uml.generator.Generator;

import ru.novosoft.uml.behavior.collaborations.MAssociationRole;
import ru.novosoft.uml.behavior.collaborations.MMessage;
import ru.novosoft.uml.behavior.common_behavior.MAction;
import ru.novosoft.uml.behavior.state_machines.MGuard;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.behavior.state_machines.MTransition;
import ru.novosoft.uml.behavior.use_cases.MExtensionPoint;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MBehavioralFeature;
import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MConstraint;
import ru.novosoft.uml.foundation.core.MDataType;
import ru.novosoft.uml.foundation.core.MDependency;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MGeneralizableElement;
import ru.novosoft.uml.foundation.core.MGeneralization;
import ru.novosoft.uml.foundation.core.MInterface;
import ru.novosoft.uml.foundation.core.MMethod;
import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.core.MParameter;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.data_types.MCallConcurrencyKind;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MMultiplicityRange;
import ru.novosoft.uml.foundation.data_types.MParameterDirectionKind;
import ru.novosoft.uml.foundation.data_types.MScopeKind;
import ru.novosoft.uml.foundation.data_types.MVisibilityKind;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.foundation.extension_mechanisms.MTaggedValue;
import ru.novosoft.uml.model_management.MPackage;

/** Generator subclass to generate text for display in diagrams in in
 * text fields in the Argo/UML user interface.  The generated code
 * looks a lot like (invalid) Java.  The idea is that other generators
 * could be written for outher languages.  This code is just a
 * placeholder for future development, I expect it to be totally
 * replaced. */

// TODO: always check for null!!!

public class GeneratorCpp extends Generator
implements PluggableNotation, FileGenerator {

  /*
    	 * 2002-06-09
    	 * changed visibility of VERBOSE_DOCS and LF_BEFORE_CURLY to public instead of private
    	 * Reason: needed for testing
    	 * 2002-06-11
    	 * removed VERBOSE_DOCS and LF_BEFORE_CURLY and changed them in configurable items (not yet implemented in GUI)
    	 */

    protected boolean _verboseDocs = false;
    protected boolean _lfBeforeCurly = false;private static final boolean VERBOSE_DOCS = false; // TODO: make it configurable

  public static boolean VERBOSE = false;

  private static Section sect;

	/**
		* 2002-11-21
		* aim: convert package nesting in C++ namespaces
		* store names of namespaces during packagePath parsing, so that
		* the names of namespaces can be put after closing curly braces for each namespace
		*/
	protected Vector _NamespaceNames = new Vector();
	/** 2002-12-07 Achim Spangler
	  * store actual namespace, to avoid unneeded curley braces
		*/
	private static MNamespace ActualNamespace;
	/** 2002-12-12 Achim Spangler
	  * store extra include dependencies which are generated during generation of
		* multiplicity to get needed container type
		*/
	private static String _ExtraIncludes = "";


	/**
	  * 2002-11-28 Achim Spangler
		* C++ doesn't place visibility information for each class member
		* --> sort items during generation and store visibility state
		* of lastly generated member in central class variable, so that
		* the appropriate lines: "public:\n", "protected:\n", "private:\n"
		* can be created
		*/
	static public int undefined_part = 0;
	static public int public_part = 1;
	static public int protected_part = 2;
	static public int private_part = 3;
	private static int VisibilityPart = undefined_part;

	/**
	  * 2002-11-28 Achim Spangler
		* C++ uses two files for each class: header (.h) with class definition
		* and source (.cpp) with methods implementation
		* --> two generation passes are needed
		*/
	static public int none_pass = 1;
  static public int header_pass = 2;
  static public int source_pass = 3;
	private static int GeneratorPass = none_pass;

	/**
	  * 2002-12-05 Achim Spangler
		* use Tag generation for generation of: doccomment, simlpe tags of
		* tags which are not used for doccomment or simple tags for all
		*/
	static public int DocCommentTags = 1;
	static public int AllButDocTags = 2;
	static public int AllTags = 3;

	/**
	  * 2002-12-06 Achim Spangler
		* C++ developers need to specify for parameters whether they are
		* pointers or references (especially for class-types)
		* -> a general check function must get the searched tag
		*/
	static public int SearchReferenceTag = 1;
	static public int SearchPointerTag = 2;
	static public int SearchReferencePointerTag = 3;



  private static GeneratorCpp SINGLETON = new GeneratorCpp();

  public static GeneratorCpp getInstance() { return SINGLETON; }

  protected GeneratorCpp() {
    super (Notation.makeNotation ("Cpp",
                                  null,
                                  Argo.lookupIconResource ("CppNotation")));
  }

  public static String Generate (Object o) {
    return SINGLETON.generate (o);
  }


	/** 2002-11-28 Achim Spangler
	  * @return file extension for actual generation pass
		*/
	private String GetFileExtension()
	{
		// for Java simply answer ".java" every time
		if (GeneratorPass == header_pass) return ".h";
		else return ".cpp";
	}

	/**
	  * create the needed directories for the derived appropriate pathname
		* @return full pathname
		*/
	private String GenerateDirectoriesPathname(MClassifier cls, String path)
	{
    String name = cls.getName();
    if (name == null || name.length() == 0) return null;
    String filename = name + GetFileExtension();
    if (!path.endsWith (FILE_SEPARATOR)) path += FILE_SEPARATOR;

    String packagePath = cls.getNamespace().getName();
    MNamespace parent = cls.getNamespace().getNamespace();
    while (parent != null) {
	  // ommit root package name; it's the model's root
	  if (parent.getNamespace() != null)
        packagePath = parent.getName() + "." + packagePath;
      parent = parent.getNamespace();
    }

	  int lastIndex=-1;
    do {
      File f = new File (path);
      if (!f.isDirectory()) {
		    if (!f.mkdir()) {
			    Argo.log.error(" could not make directory "+path);
			    return null;
		    }
      }

	    if (lastIndex == packagePath.length())
		    break;

      int index = packagePath.indexOf (".", lastIndex+1);
	    if (index == -1)
  		  index = packagePath.length();

      path += packagePath.substring (lastIndex+1, index) + FILE_SEPARATOR;
      lastIndex = index;
	  } while (true);

    String pathname = path + filename;
		//Argo.log.info("-----" + pathname + "-----");
		return pathname;
	}

    /** Generates a file for the classifier.
     * This method could have been static if it where not for the need to
     * call it through the Generatorinterface.
     * @returns the full path name of the the generated file.
     */
  public String GenerateFile(Object o, String path) {
	MClassifier cls = (MClassifier)o;
    String packagePath = cls.getNamespace().getName();
   	String pathname = null;

		// use unique section for both passes -> allow move of
		// normal function body to inline and vice versa
   	sect = new Section();
		/**
		  * 2002-11-28 Achim Spangler
			* first read header and source file into global/unique section
			*/
		for ( GeneratorPass = header_pass; GeneratorPass <= source_pass; GeneratorPass++ )
		{
			pathname = GenerateDirectoriesPathname(cls, path);
    	//String pathname = path + filename;
    	// needs-more-work: package, project basepath, tagged values to configure
    	File f = new File(pathname);
    	if (f.exists()){
        	Argo.log.info("Generating (updated) " + f.getPath());
        	sect.read(pathname);
    	} else {
        	Argo.log.info("Generating (new) " + f.getPath());
    	}
		}

		/**
		  * 2002-11-28 Achim Spangler
			* run basic generation function two times for header and implementation
			*/
		for ( GeneratorPass = header_pass; GeneratorPass <= source_pass; GeneratorPass++ )
		{
			pathname = GenerateDirectoriesPathname(cls, path);
    	//String pathname = path + filename;
    	// needs-more-work: package, project basepath, tagged values to configure
    	File f = new File(pathname);
			String header_top = SINGLETON.generateHeaderTop(cls, pathname, packagePath);
    	String header = SINGLETON.generateHeader (cls, pathname, packagePath);
    	String src = SINGLETON.generate (cls);
    	BufferedWriter fos = null;
    	try {
      	fos = new BufferedWriter (new FileWriter (f));
				writeTemplate( cls, path, fos );
				fos.write( header_top );
				fos.write( _ExtraIncludes );
      	fos.write (header);
      	fos.write (src);
    	}
    	catch (IOException exp) { }
    	finally {
      	try {
        	if (fos != null) fos.close();
      	}
      	catch (IOException exp) {
        	Argo.log.error("FAILED: " + f.getPath());
      	}
    	}

			// clear extra includes after usage for each pass
			_ExtraIncludes = "";

			// output lost sections only in the second path
			// -> sections which are moved from header(inline) to source
			// file are prevented to be outputted in header pass
    	if ( GeneratorPass == header_pass )	sect.write(pathname, INDENT, false);
			else sect.write(pathname, INDENT, true);

    	Argo.log.info("written: " + pathname);


    	File f1 = new File(pathname + ".bak");
    	if (f1.exists()){
        	f1.delete();
    	}

    	File f2 = new File(pathname);
    	if (f2.exists()){
        	f2.renameTo(new File(pathname + ".bak"));
    	}

    	File f3 = new File(pathname + ".out");
    	if (f3.exists()){
        	f3.renameTo(new File(pathname));
    	}

    	Argo.log.info("----- end updating -----");
		}
   	return pathname;
  }

	/** 2002-12-07 Achim Spangler
	  * @return date
		*/
	private String getDate() {
		GregorianCalendar cal = new GregorianCalendar();
		DateFormat df;
		df = DateFormat.getDateInstance(DateFormat.DEFAULT);
		return df.format(cal.getTime());
	}
	/** 2002-12-07 Achim Spangler
	  * @return year
		*/
	private String getYear() {
		GregorianCalendar cal = new GregorianCalendar();
		return Integer.toString(cal.get(Calendar.YEAR));
	}
	/** 2002-12-07 Achim Spangler
	  * @return time
		*/
	private String getTime() {
		GregorianCalendar cal = new GregorianCalendar();
		DateFormat df;
		df = DateFormat.getTimeInstance(DateFormat.DEFAULT);
		return df.format(cal.getTime());
	}

	/** 2002-12-07 Achim Spangler
	  * write template content on top of file
		*/
	private void writeTemplate( MClassifier cls, String path, BufferedWriter fos )
	{
		String TemplatePathName = path + "/templates/";
		String FileName = cls.getName();
		String TagTemplatePathName = cls.getTaggedValue( "TemplatePath" );
		String AuthorTag = cls.getTaggedValue( "author" );
		String EmailTag = cls.getTaggedValue( "email" );
		if ( TagTemplatePathName != null && TagTemplatePathName.length() > 0 )
			TemplatePathName = TagTemplatePathName;
		if ( GeneratorPass == header_pass ) {
			TemplatePathName = TemplatePathName + "header_template";
			FileName = FileName + ".h";
		}
		else {
			TemplatePathName = TemplatePathName + "cpp_template";
			FileName = FileName + ".cpp";
		}
    // Argo.log.info("Try to read Template: " + TemplatePathName);
		File TemplateFile = new File( TemplatePathName );
		if ( TemplateFile.exists())
		{
    	boolean eof = false;
   		BufferedReader TemplateFileReader = null;
    	try {
				TemplateFileReader = new BufferedReader(new FileReader(TemplateFile.getAbsolutePath()));
    		while (!eof){
					String line_str = TemplateFileReader.readLine();
        	if (line_str == null) {
        		eof = true;
       		} else {
	      		StringBuffer line = new StringBuffer(line_str);
						int TokenStart = line.toString().indexOf( "|FILENAME|");
						if ( TokenStart != -1) {
							line.replace(TokenStart, (TokenStart + "|FILENAME|".length()), FileName);
						}
						TokenStart = line.toString().indexOf( "|DATE|");
						if ( TokenStart != -1) {
							line.replace(TokenStart, (TokenStart + "|DATE|".length()), getDate());
						}
						TokenStart = line.toString().indexOf( "|YEAR|");
						if ( TokenStart != -1) {
							line.replace(TokenStart, (TokenStart + "|YEAR|".length()), getYear());
						}
						TokenStart = line.toString().indexOf( "|AUTHOR|");
						if ( ( TokenStart != -1) && ( AuthorTag != null && AuthorTag.length() > 0 )) {
							line.replace(TokenStart, (TokenStart + "|AUTHOR|".length()), AuthorTag);
						}
						TokenStart = line.toString().indexOf( "|EMAIL|");
						if ( ( TokenStart != -1) && ( EmailTag != null && EmailTag.length() > 0 )) {
							line.replace(TokenStart, (TokenStart + "|EMAIL|".length()), EmailTag);
						}

         		fos.write(line + "\n");
      		}
    		}
    		TemplateFileReader.close();
			}
    	catch (IOException exp) { }
    	finally {
      	try {
        	if (TemplateFileReader != null) TemplateFileReader.close();
      	}
      	catch (IOException exp) {
        	Argo.log.error("FAILED: " + TemplateFile.getPath());
      	}
    	}
		}
	}

	/** 2002-11-28 Achim Spangler
	  * seperate constant Header Top into function
		*/
	private String generateHeaderTop(MClassifier cls, String pathname, String packagePath)
	{
    StringBuffer sb = new StringBuffer(80);
    //TODO: add user-defined copyright
    if (VERBOSE_DOCS) sb.append("// FILE: ").append(pathname.replace('\\','/')).append("\n\n");
    return sb.toString();
	}

  private String generateHeaderImportLine4ItemList(Collection classifiers) {
    StringBuffer sb = new StringBuffer(80);
		Iterator clsEnum = classifiers.iterator();
		while (clsEnum.hasNext()) {
			sb.append(generateHeaderImportLine4Item((MClass)clsEnum.next()));
		}
		return sb.toString();
	}

	/** 2002-11-28 Achim Spangler
	  * as each language has its own syntax to incorporate other elements
		* the command for this inclusion is created in a seperate function
		*/
	private String generateHeaderImportLine4Item( MModelElement cls_depend )
	{
    // Argo.log.info("generateHeaderImportLine4Item: fuer Item " + cls_depend.getName() + " in Namespace: " + cls_depend.getNamespace().getName() );
    StringBuffer sb = new StringBuffer(80);
		String packagePath = cls_depend.getName();
    MNamespace parent = cls_depend.getNamespace().getNamespace();
		if ( parent != null ) {
    	packagePath = cls_depend.getNamespace().getName() + "/" + packagePath;
    	while (parent != null) {
	  		// ommit root package name; it's the model's root
	  		if (parent.getNamespace() != null)
      		packagePath = parent.getName() + "/" + packagePath;
	    	// Argo.log.info("generateHeaderImportLine4Item: Runde mit Parent" + parent.getName() );
    		parent = parent.getNamespace();
    	}
		}

		// if class depends on a list of other classes, all inheritance
		// elements are seperated by Tokens ", " -> use StringTokenizer
		// to access the single elements
		StringTokenizer st = new StringTokenizer(packagePath, ", ");
		while (st.hasMoreTokens()) {
			sb.append( "#include <" )
		  	.append( st.nextToken() )
				.append( ".h>\n" );
		}
    return sb.toString();
	}

	private boolean checkInclude4UsageIndirection( boolean isIndirect, String usage_tag) {
		boolean result = false;
/*
		if ( usage_tag.length() > 0 ) Argo.log.info("usage tag " + usage_tag + " gefunden");
		if ( isIndirect ) Argo.log.info("indirection tag gefunden");
		if ( GeneratorPass == header_pass ) Argo.log.info("Header pass");
*/

		if (( GeneratorPass != header_pass ) && ( usage_tag.indexOf("source") != -1 ))
		{ // generate include line for source .cpp pass only if
			// element has usage tag which specifies exclusive use in
			// source file
			result = true;
		}
		else if (( GeneratorPass == header_pass ) && ( usage_tag.indexOf("source") == -1 ))
		{ // generate include line for header, if not specified as only accessed from .cpp
			result = true;
		}

		// only predeclare candidates can be ignored in include block of header
		if (( GeneratorPass == header_pass ) && ( !isIndirect ) ) result = true;

		return result;
	}

	private boolean checkIncludeNeeded4Element( MAssociation cls ) {
		String usage_tag = "";
		boolean result = false,
		        predeclare_candidate = false;
		Collection tValues = cls.getTaggedValues();
    if (!tValues.isEmpty()) {
      Iterator iter = tValues.iterator();
      while(iter.hasNext()) {
        MTaggedValue tv = (MTaggedValue)iter.next();
        String tag = tv.getTag();
        if (tag.equals("usage")) usage_tag = tv.getValue();
				// Argo.log.info("Tag fuer: " + cls.getName() + " mit Tag: " + tag + " mit Wert:" + tv.getValue() + ":");

      	if (tag.indexOf("ref") != -1 || tag.equals("&")
				 || tag.indexOf("pointer") != -1 || tag.equals("*"))
					predeclare_candidate = true;
      }
		}
		return checkInclude4UsageIndirection(predeclare_candidate, usage_tag) ;
	}

	private boolean checkIncludeNeeded4Element( MDependency cls ) {
		String usage_tag = "";
		boolean result = false,
		        predeclare_candidate = false;
		Collection tValues = cls.getTaggedValues();
    if (!tValues.isEmpty()) {
      Iterator iter = tValues.iterator();
      while(iter.hasNext()) {
        MTaggedValue tv = (MTaggedValue)iter.next();
        String tag = tv.getTag();
        if (tag.equals("usage")) usage_tag = tv.getValue();
				// Argo.log.info("Tag fuer: " + cls.getName() + " mit Tag: " + tag + " mit Wert:" + tv.getValue() + ":");

      	if (tag.indexOf("ref") != -1 || tag.equals("&")
				 || tag.indexOf("pointer") != -1 || tag.equals("*"))
					predeclare_candidate = true;
      }
		}
		return checkInclude4UsageIndirection(predeclare_candidate, usage_tag) ;
	}
	private boolean checkIncludeNeeded4Element( MAttribute cls ) {
    // Argo.log.info("checkIncludeNeeded4Element: fuer Item" + cls );
		if (!(((MAttribute)cls).getType() instanceof MClass)) return false;

		String usage_tag = "";
		boolean result = false,
		        predeclare_candidate = false;

    Collection tValues = cls.getTaggedValues();
    if (!tValues.isEmpty()) {
      Iterator iter = tValues.iterator();
      while(iter.hasNext()) {
        MTaggedValue tv = (MTaggedValue)iter.next();
        String tag = tv.getTag();
        if (tag.equals("usage")) usage_tag = tv.getValue();
				// Argo.log.info("Tag fuer: " + cls.getName() + " mit Tag: " + tag + " mit Wert:" + tv.getValue() + ":");

      	if (tag.indexOf("ref") != -1 || tag.equals("&")
				 || tag.indexOf("pointer") != -1 || tag.equals("*"))
					predeclare_candidate = true;
      }
		}
		return checkInclude4UsageIndirection(predeclare_candidate, usage_tag) ;
	}

	private String generateHeaderDependencies(MClassifier cls)
	{
    StringBuffer sb = new StringBuffer(160),
		             predeclare_statements = new StringBuffer(60);

		if ( GeneratorPass != header_pass )
		{ // include header in .cpp
			sb.append( SINGLETON.generateHeaderImportLine4Item( (MClass) cls ) );

      Collection tValues = cls.getTaggedValues();
      if (!tValues.isEmpty()) {
        Iterator iter = tValues.iterator();
        while(iter.hasNext()) {
          MTaggedValue tv = (MTaggedValue)iter.next();
          String tag = tv.getTag();
          if (tag.equals("source_incl") || tag.equals("source_include")) {
            sb.append( "#include ").append(tv.getValue()).append("\n");
          }
        }
			}
		}
		else {
    	Collection baseClassList = getGeneralizationClassList(cls.getGeneralizations());
			sb.append( generateHeaderImportLine4ItemList( baseClassList ) );

      Collection tValues = cls.getTaggedValues();
      if (!tValues.isEmpty()) {
        Iterator iter = tValues.iterator();
        while(iter.hasNext()) {
          MTaggedValue tv = (MTaggedValue)iter.next();
          String tag = tv.getTag();
          if (tag.equals("header_incl") || tag.equals("header_include")) {
            sb.append( "#include ").append(tv.getValue()).append("\n");
          }
        }
			}
		}

    // check if the class has dependencies
    {
	    Collection col = UmlHelper.getHelper().getCore().getAssociateEnds(cls);
      if (col != null){
        Iterator itr = col.iterator();
        while (itr.hasNext()) {
        	MAssociationEnd ae = (MAssociationEnd) itr.next();
        	if (ae.isNavigable()) {
            MClassifier cls2 = ae.getType();
            String name = cls2.getName();
            String name2 = cls.getName();
            if (name != name2){
              if ( checkIncludeNeeded4Element( ae.getAssociation() ) ) {
								sb.append( SINGLETON.generateHeaderImportLine4Item( (MClass) cls2 ) );
							}
							else if ( GeneratorPass == header_pass ) {
								// predeclare classes which are not directly used in header
								// usefull for classes which are only used indirectly as pointer
								// where no knowledge about internals of class are needed
								predeclare_statements.append( generateHeaderPackageStart( cls2 ) )
								                     .append( "class " ).append( name ).append(";\n");
							}
            }
        	}
        }
      }
    }

    {
	    Collection col = UmlHelper.getHelper().getCore().getAttributes(cls);
      if (col != null){
        Iterator itr = col.iterator();
    		// Argo.log.info("Attribut gefunden" );
        while (itr.hasNext()) {
        	MAttribute attr = (MAttribute) itr.next();
	    		// Argo.log.info("untersuche name " + attr.getName() + " mit Typ: " + attr.getType() );
        	if (attr.getType() instanceof MClass ) {
            String name = attr.getName();
            if ( checkIncludeNeeded4Element( attr ) ) {
							sb.append( SINGLETON.generateHeaderImportLine4Item( (MClass) attr.getType() ) );
						}
						else if ( GeneratorPass == header_pass ) {
							// predeclare classes which are not directly used in header
							// usefull for classes which are only used indirectly as pointer
							// where no knowledge about internals of class are needed
							predeclare_statements.append( generateHeaderPackageStart( attr.getType() ) )
								                   .append( "class " ).append( name ).append(";\n");
						}
        	}
        }
      }
    }

    {
    	Collection col = cls.getClientDependencies();
    	// Argo.log.info("col: " + col);
    	if (col != null){
      	Iterator itr = col.iterator();
      	while (itr.hasNext()) {
          MDependency dep = (MDependency) itr.next();
					if ( dep == null ) break;
          Collection clients_col = dep.getSuppliers();
					if ( clients_col == null ) break;
          Iterator itr2 = clients_col.iterator();
					MClassifier temp;
          while (itr2.hasNext()){
							temp = (MClassifier) itr2.next();
              if ( checkIncludeNeeded4Element( dep ) ) {
								sb.append( SINGLETON.generateHeaderImportLine4Item( temp ) );
							}
							else if ( GeneratorPass == header_pass ) {
								// predeclare classes which are not directly used in header
								// usefull for classes which are only used indirectly as pointer
								// where no knowledge about internals of class are needed
								predeclare_statements.append( generateHeaderPackageStart( temp ) )
								                     .append( "class " ).append( temp.getName() ).append(";\n");
							}
          }
      	}
    	}
    }

		if ( predeclare_statements.toString().length() > 0 ) {
			sb.append("\n\n").append(predeclare_statements.toString());
		}
    return sb.toString();
	}

	private String generateHeaderPackageStartSingle( MNamespace pkg )
	{
    // Argo.log.info("generateHeaderPackageStartSingle: " + pkg.getName() );
    StringBuffer sb = new StringBuffer(30);
		StringTokenizer st = new StringTokenizer(pkg.getName(), ".");
		String token = "";

		sb.append( generateTaggedValues(pkg, DocCommentTags) );
		while ( st.hasMoreTokens() ) {
			token = st.nextToken();
			// create line: namespace FOO {"
			sb.append( "namespace " )
			  .append( token )
				.append( " {\n" );
		}
    return sb.toString();
	}

	private String generateHeaderPackageEndSingle( MNamespace pkg )
	{
    // Argo.log.info("generateHeaderPackageEndSingle: " + pkg.getName() );
    StringBuffer sb = new StringBuffer(30);
		StringTokenizer st = new StringTokenizer(pkg.getName(), ".");
		String token = "";
		while ( st.hasMoreTokens() ) {
			token = st.nextToken();
	    StringBuffer temp_buf = new StringBuffer(20);
			String absolute_name = generatePackageAbsoluteName(pkg);
			if ( absolute_name.indexOf( token ) != -1 )
			{
				absolute_name = absolute_name.substring(0, (absolute_name.indexOf( token ) + token.length() ) );
			}

			// create line: namespace FOO {"
			temp_buf.append( "} /* End of namespace ")
			        .append( absolute_name )
				      .append( " */\n");
			sb.insert( 0, temp_buf.toString() );
		}
    return sb.toString();
	}

	private String generatePackageAbsoluteName( MNamespace pkg )
	{
    // Argo.log.info("generatePackageAbsoluteName: " + pkg.getName() );
    StringBuffer sb = new StringBuffer(30);
		String token = "";
		for ( MNamespace actual = pkg; actual != null; actual = actual.getNamespace() )
		{
			StringTokenizer st = new StringTokenizer(actual.getName(), ".");
	    StringBuffer temp_buf = new StringBuffer(20);
			while ( st.hasMoreTokens() ) {
				token = st.nextToken();
				if ( temp_buf.length() > 0) temp_buf.append( "::" );
				temp_buf.append( token );
			}
			if (( temp_buf.length() > 0) && (sb.length() > 0 )) temp_buf.append( "::" );
			sb.insert( 0, temp_buf.toString() );
		}
    return sb.toString();
	}

	private String generateNameWithPkgSelection( MModelElement item, MNamespace local_pkg ) {
		if ( item == null ) {
	    // Argo.log.info("generateNameWithPkgSelection: zu void" );
			return "void ";
		}
    // Argo.log.info("generateNameWithPkgSelection: " + item.getName() );
		MNamespace pkg = null;
		if ( item instanceof MDataType ) return generateName( item.getName() );
		else if ( item instanceof MParameter ) pkg = ((MParameter)item).getNamespace();
		else if ( item instanceof MAttribute ) pkg = ((MAttribute)item).getNamespace();
		else if ( item instanceof MAssociationEnd ) pkg = ((MAssociationEnd)item).getNamespace();
		else if ( item instanceof MClassifier ) pkg = ((MClassifier)item).getNamespace();

		if ( pkg == null ) return generateName( item.getName() );
		if (local_pkg == null )
	    Argo.log.info("LOCAL NAMESPACE IS NULL" );

		String localPkgName = generatePackageAbsoluteName( local_pkg );
		String targetPkgName = generatePackageAbsoluteName( pkg );
    // Argo.log.info("targetNamespace:" + targetPkgName + ":" );
    // Argo.log.info("localNamespace:" + localPkgName + ":" );
		int localPkgNameLen = localPkgName.length();
		int targetPkgNameLen = targetPkgName.length();
		if ( localPkgName.equals( targetPkgName ) ) return generateName( item.getName() );
		else {
			if ( targetPkgName.indexOf( localPkgName ) != -1 ) {
				/*
				Argo.log.info("target is subpackage of local with |"
					+ targetPkgName.substring(localPkgNameLen, localPkgNameLen+2 ) + "|");
				*/
				if (targetPkgName.substring(localPkgNameLen, localPkgNameLen+2 ).equals("::")) {
					// target is in Sub-Package of local class
					return (targetPkgName.substring(localPkgNameLen+2, targetPkgNameLen ) + "::" + generateName( item.getName() ));
				}
			}
		}
		return ( targetPkgName + "::" + generateName( item.getName() ));
	}

	private String generateNameWithPkgSelection( MModelElement item ) {
		return generateNameWithPkgSelection( item, ActualNamespace );
	}

	private String generateHeaderPackageStart(MClassifier cls)
	{
    // Argo.log.info("generateHeaderPackageStart: " + cls.getName() + " aus Namespace: " + cls.getNamespace().getName() );
    StringBuffer sb = new StringBuffer(80);

		if ( ActualNamespace != null )
		{
			for ( MNamespace FromSearch = ActualNamespace; FromSearch != null; FromSearch = FromSearch.getNamespace() )
			{
		    // Argo.log.info("FromSearch: " + FromSearch.getName() );
				StringBuffer ContPath = new StringBuffer(80);
				MNamespace ToSearch = cls.getNamespace();
				for ( ; ((ToSearch != null) && (ToSearch != FromSearch)); ToSearch = ToSearch.getNamespace() )
				{
			    // Argo.log.info("ToSearch: " + ToSearch.getName() );
					ContPath.insert(0, generateHeaderPackageStartSingle( ToSearch ));
				}
				if ( ToSearch == FromSearch)
				{
					sb.append( ContPath.toString() );
					break;
				}
				else {
					// close one namespace
					sb.append( generateHeaderPackageEndSingle( FromSearch ) );
				}
			}
		}
		else
		{ // initial start
			for ( MNamespace ToSearch = cls.getNamespace(); (ToSearch != null); ToSearch = ToSearch.getNamespace() )
			{
				sb.insert(0, generateHeaderPackageStartSingle( ToSearch ));
			}
		}
		if ( sb.length() > 0) sb.insert(0, "\n").append("\n");
		ActualNamespace = cls.getNamespace();
    return sb.toString();
	}

	private String generateHeaderPackageEnd() {
    StringBuffer sb = new StringBuffer(20);

		for ( MNamespace CloseIt = ActualNamespace; CloseIt != null; CloseIt = CloseIt.getNamespace() )
		{
			sb.append( generateHeaderPackageEndSingle( CloseIt ) );
		}
		ActualNamespace = null;
		if ( sb.length() > 0) sb.insert(0, "\n").append("\n");
		return sb.toString();
	}

  public String generateHeader(MClassifier cls, String pathname, String packagePath) {
    StringBuffer sb = new StringBuffer(240);

    // check if the class has a base class
		sb.append( SINGLETON.generateHeaderDependencies( cls ) );

    sb.append( "\n" );

    if (packagePath.length() > 0) {
			sb.append( SINGLETON.generateHeaderPackageStart( cls ) );
		}

    return sb.toString();
  }

  /**
   * <p>Generate code for an extension point.</p>
   *
   * <p>Provided to comply with the interface, but returns null
   *   since no code will be generated. This should prevent a source tab
   *   being shown.</p>
   *
   * @param ep  The extension point to generate for
   *
   * @return    The generated code string. Always empty in this
   *            implementation.
   */
  public String generateExtensionPoint (MExtensionPoint ep) {
      return null;
  }

  public String generateAssociationRole(MAssociationRole m) {
      return "";
  }

	/** 2002-11-28 Achim Spangler
	  * seperate generation of Operation Prefix from generateOperation
		* so that generateOperation is language independent
		*/
	private String generateOperationPrefix(MOperation op) {
    StringBuffer sb = new StringBuffer(80);
    sb.append(generateConcurrency(op));
		if ( GeneratorPass == header_pass ) {
			// make all operations to virtual - as long as they are not "leaf"
	    MScopeKind scope = op.getOwnerScope();
  	  // generate a function as virtual, if it can be overriden or override another function AND if
			// this function is not marked as static, which disallows "virtual"
			// alternatively every abstract function is defined as virtual
			if ( ( !op.isLeaf() && !op.isRoot() && (!(MScopeKind.CLASSIFIER.equals(scope))) ) || ( op.isAbstract() ) ) sb.append( "virtual " );
	    sb.append(generateScope(op));
		}
    return sb.toString();
	}

	/** 2002-11-28 Achim Spangler
	  * seperate generation of Operation Suffix from generateOperation
		* so that generateOperation is language independent
		*/
	private String generateOperationSuffix(MOperation op) {
    StringBuffer sb = new StringBuffer(80);
    sb.append(generateChangeability(op));
    sb.append(generateAbstractness(op));
    return sb.toString();
	}

	/** 2002-11-28 Achim Spangler
	  * seperate generation of Operation Name from generateOperation
		* so that generateOperation is language independent
		* -> for C++: if we create .cpp we must prepend Owner name
		*/
	private boolean generateOperationNameAndTestForConstructor(MOperation op, StringBuffer nameStr)
	{
    // Argo.log.info("generate Operation for File" + GeneratorPass + " fuer Op: " + op.getName() );
		if ( GeneratorPass != header_pass )
		{
    	// Argo.log.info("generate Operation for CPP File");
			nameStr.append( op.getOwner().getName() )
			  .append( "::" );
		}
    boolean constructor = false;
    MStereotype stereo = op.getStereotype();
    if (stereo != null && stereo.getName().equals("create")) { // constructor
    	nameStr.append( generateName (op.getOwner().getName()) );
    	constructor = true;
    } else {
    	// Argo.log.info("generate Operation for File" + GeneratorPass + " fuer Op: " + op.getName() );
    	nameStr.append( generateName (op.getName()) );
    }
		return constructor;
	}

	/** 2002-11-28 Achim Spangler
	  * modified version from Jaap Branderhorst
		* -> generateOperation is language independent and seperates different tasks
		*/
  public String generateOperation(MOperation op, boolean documented) {
		// generate nothing for abstract functions, if we generate the
		// source .cpp file at the moment
		if ( ( GeneratorPass != header_pass ) && ( op.isAbstract() ) ) return "";
    StringBuffer sb = new StringBuffer(80);
		StringBuffer nameBuffer = new StringBuffer(20);
		String OperationIndent = (GeneratorPass == header_pass )?INDENT:"";
    // Argo.log.info("generate Operation for File" + GeneratorPass + " fuer Op: " + op.getName() );
    boolean constructor = SINGLETON.generateOperationNameAndTestForConstructor( op, nameBuffer );

		sb.append('\n'); // begin with a blank line
		// generate DocComment from tagged values
    String tv = generateTaggedValues (op, DocCommentTags);
    if (tv != null && tv.length() > 0) {
      sb.append ("\n")
        .append (OperationIndent)
        .append (tv);
    }

    // 2002-07-14
    // Jaap Branderhorst
    // missing concurrency generation
		sb.append(OperationIndent)
		  .append( SINGLETON.generateOperationPrefix( op ) );

    // pick out return type
    MParameter rp = UmlHelper.getHelper().getCore().getReturnParameter(op);
	  if ( rp != null) {
      MClassifier returnType = rp.getType();
      if (returnType == null && !constructor) {
        sb.append("void ");
      }
      else if (returnType != null) {
        sb.append(generateNameWithPkgSelection(returnType)).append(' ');
      }
    }

    // name and params
    Vector params = new Vector (op.getParameters());
    params.remove (rp);

    sb.append(nameBuffer.toString()).append('(');

    if (params != null) {
      boolean first = true;

      for (int i=0; i < params.size(); i++) {
        MParameter p = (MParameter) params.elementAt (i);

        if (!first) sb.append(", ");

        sb.append(generateParameter(p));
        first = false;
      }
    }

    sb.append(") ")
		  .append( SINGLETON.generateOperationSuffix( op ) );

    return sb.toString();

  }

	/** 2002-12-06 Achim Spangler
	  * check if a parameter is tagged as pointer or reference (not part of UML - as far
		* as author knows - but important for C++ developers)
		* @param elem element to check
		* @param tag_type tag type to check
		*/
	private boolean checkAttributeParameter4Tag(MModelElement elem, int tag_type) {
		// first check whether the parameter shall be a pointer of reference
  	Collection tValues = elem.getTaggedValues();
  	if (!tValues.isEmpty()) {
    	Iterator iter = tValues.iterator();
    	while(iter.hasNext()) {
      	MTaggedValue tv = (MTaggedValue)iter.next();
      	String tag = tv.getTag();
      	if ((tag.indexOf("ref") != -1 || tag.equals("&"))
				 && (tag_type != SearchPointerTag)) return true;
      	else if ((tag.indexOf("pointer") != -1 || tag.equals("*"))
				      && (tag_type != SearchReferenceTag)) return true;
    	}
		}
		return false;
	}


	private String generateAttributeParameterModifier(MModelElement attr) {
		boolean isReference = checkAttributeParameter4Tag( attr, SearchReferenceTag),
		        isPointer = checkAttributeParameter4Tag( attr, SearchPointerTag);
    StringBuffer sb = new StringBuffer(2);

		if ( isReference ) sb.append("&");
		else if ( isPointer ) sb.append("*");
		else if ( attr instanceof MParameter ) {
			if ((( ((MParameter)attr).getKind()).equals(MParameterDirectionKind.OUT))
			 || (( ((MParameter)attr).getKind()).equals(MParameterDirectionKind.INOUT)))
			{ // out or inout parameters are defaulted to reference if not specified else
				sb.append("&");
			}
		}

		return sb.toString();
	}

  public String generateAttribute (MAttribute attr, boolean documented) {
    StringBuffer sb = new StringBuffer(80);
		sb.append('\n'); // begin with a blank line

    // list tagged values for documentation
    String tv = generateTaggedValues (attr, DocCommentTags);
    if (tv != null && tv.length() > 0) {
      sb.append ("\n")
        .append (INDENT)
        .append (tv);
    }


    sb.append(INDENT);
		// Argo.log.info("generate Visibility for Attribute");
    sb.append(generateVisibility(attr));
    sb.append(generateScope(attr));
    sb.append(generateChangability(attr));
    /*
         * 2002-07-14
         * Jaap Branderhorst
         * Generating the multiplicity should not lead to putting the range in the generated code
         * (no 0..1 as modifier)
         * Therefore removed the multiplicity generation
         * START OLD CODE

        if (!MMultiplicity.M1_1.equals(attr.getMultiplicity()))
        {
        	String m = generateMultiplicity(attr.getMultiplicity());
        	if (m != null && m.trim().length() > 0)
        		sb.append(m).append(' ');
        }
        */
        // END OLD CODE
/*
    MClassifier type = attr.getType();
    MMultiplicity multi = attr.getMultiplicity();
    // handle multiplicity here since we need the type
    // actually the API of generator is buggy since to generate multiplicity correctly we need the attribute too
    if (type != null && multi != null) {
    	if (multi.equals(MMultiplicity.M1_1)) {
    		sb.append(generateClassifierRef(type)).append(' ');
    	} else
    	if (type instanceof MDataType) {
    		sb.append(generateClassifierRef(type)).append("[] ");
    	} else
    	sb.append("java.util.Vector ");
    }

		sb.append( generateAttributeParameterModifier(attr));
    sb.append(generateName(attr.getName()));
*/
		sb.append( generateMultiplicity(attr, generateName(attr.getName()),
		                                attr.getMultiplicity(), generateAttributeParameterModifier(attr)));
    MExpression init = attr.getInitialValue();
    if (init != null) {
      String initStr = generateExpression(init).trim();
      if (initStr.length() > 0)
	      sb.append(" = ").append(initStr);
    }

    sb.append(";\n");

    return sb.toString();
  }


  public String generateParameter(MParameter param) {
    StringBuffer sb = new StringBuffer(20);
    //TODO: qualifiers (e.g., const)
		// generate const for references or pointers which are
		// defined as IN - other qualifiers are not important for
		// C++ parameters
		sb.append(generateChangeability( param ));
    //TODO: stereotypes...
    sb.append(generateNameWithPkgSelection(param.getType())).append(' ');
		sb.append( generateAttributeParameterModifier(param));
    sb.append(generateName(param.getName()));

		// insert default value, if we are generating the header
		if (( GeneratorPass == header_pass ) && (param.getDefaultValue() != null)) {
			sb.append(" = ").append( param.getDefaultValue() );
		}

    return sb.toString();
  }


  public String generatePackage(MPackage p) {
    String s = "";
    String packName = generateName(p.getName());
    s += "// package " + packName + " {\n";
    Collection ownedElements = p.getOwnedElements();
    if (ownedElements != null) {
      Iterator ownedEnum = ownedElements.iterator();
      while (ownedEnum.hasNext()) {
	MModelElement me = (MModelElement) ownedEnum.next();
	s += generate(me);
	s += "\n\n";
      }
    }
    else {
      s += "(no elements)";
    }
    s += "\n}\n";
    return s;
  }


  /**
   * Generate the start sequence for a classifier. The start sequence is
   * everything from the preceding javadoc comment to the opening curly brace.
   * Start sequences are non-empty for classes and interfaces only.
   *
   * This method is intented for package internal usage only.
   *
   * @param cls the classifier for which to generate the start sequence
   *
   * @return the generated start sequence
   */
  StringBuffer generateClassifierStart (MClassifier cls) {
    StringBuffer sb = new StringBuffer (80);

		// don't create class-Start for implementation in .cpp
		if ( GeneratorPass != header_pass ) return sb;

    String sClassifierKeyword;
    if (cls instanceof MClass) sClassifierKeyword = "class";
    else if (cls instanceof MInterface) sClassifierKeyword = "interface";
    else return null; // actors, use cases etc.
		boolean has_bas_class = false;

    // Add the comments for this classifier first.
    sb.append ('\n')
      .append (DocumentationManager.getComments(cls));

    // list tagged values for documentation
    String tv = generateTaggedValues (cls, DocCommentTags);
    if (tv != null && tv.length() > 0) {
      sb.append ("\n")
        .append (INDENT)
        .append (tv);
    }

    // Now add visibility
    sb.append (generateVisibility (cls.getVisibility()));

    // Add other modifiers
    if (cls.isAbstract() && !(cls instanceof MInterface)) {
      sb.append("abstract ");
    }

    if (cls.isLeaf()) {
      sb.append("final ");
    }

    // add classifier keyword and classifier name
    sb.append (sClassifierKeyword)
      .append(" ")
      .append (generateName (cls.getName()));

    // add base class/interface
    String baseClass = generateGeneralization (cls.getGeneralizations());
    if (!baseClass.equals ("")) {
      sb.append (" : ")
        .append (baseClass);
			has_bas_class = true;
    }

    // add implemented interfaces, if needed
    // nsuml: realizations!
    if (cls instanceof MClass) {
      String interfaces = generateSpecification ((MClass) cls);
      if (!interfaces.equals ("")) {
				if ( ! has_bas_class ) sb.append (" : ");
				else sb.append ( ", " );
        sb.append (interfaces);
      }
    }

    // add opening brace
	  sb.append(_lfBeforeCurly ? "\n{" : " {");

    // list tagged values for documentation
    tv = generateTaggedValues (cls, AllButDocTags);
    if (tv != null && tv.length() > 0) {
      sb.append ("\n")
        .append (INDENT)
        .append (tv);
    }

    return sb;
  }
  protected StringBuffer generateClassifierEnd(MClassifier cls)
  {
    StringBuffer sb = new StringBuffer();
    if (cls instanceof MClass || cls instanceof MInterface)
    {
        if ( (_verboseDocs) && ( GeneratorPass == header_pass ) )
        {
            String classifierkeyword = null;
            if (cls instanceof MClass)
            {
                classifierkeyword = "class";
            }
            else
            {
                classifierkeyword = "class";
            }
            sb.append(
                "\n//end of "
                    + classifierkeyword
                    + " "
                    + cls.getName()
                    + "\n");
        }
        if ( GeneratorPass == header_pass ) sb.append("};\n");
				sb.append( generateHeaderPackageEnd());
    }
    return sb;
  }
	/**
   * Generates code for a classifier. In case of Java code is generated for classes and interfaces only at the moment.
   * @see org.argouml.application.api.NotationProvider#generateClassifier(MClassifier)
   */
  public String generateClassifier(MClassifier cls)
  {
  	StringBuffer returnValue = new StringBuffer();
		StringBuffer start = generateClassifierStart(cls);
  	if (((start != null) && (start.length() > 0)) || ( GeneratorPass != header_pass ) )
  	{
				StringBuffer typedefs = generateGlobalTypedefs(cls);
      	StringBuffer body = generateClassifierBody(cls);
      	StringBuffer end = generateClassifierEnd(cls);
				returnValue.append((typedefs!=null)?typedefs.toString():"");
      	returnValue.append(start);
      	if ((body != null) && (body.length() > 0))
      	{
          	returnValue.append("\n");
          	returnValue.append(body);
          	if (_lfBeforeCurly)
          	{
              	returnValue.append("\n");
          	}
      	}
      	returnValue.append((end!=null)?end.toString():"");
  	}
  	return returnValue.toString();
  }

	/** 2002-12-12 Achim Spangler
	  * generate global typedefs
		*/
	private StringBuffer generateGlobalTypedefs( MClassifier cls ) {
  	StringBuffer sb = new StringBuffer();
  	if (cls instanceof MClass || cls instanceof MInterface )
  	{ // add typedefs
			if ( GeneratorPass == header_pass ) {
				sb.append( "// global type definitions for header defined by Tag entries in ArgoUML\n" )
				  .append( "// Result: typedef <typedef_global_header> <tag_value>;\n" );
				Collection global_typedef_statements = findTagValues( cls, "typedef_global_header" );
				if (!global_typedef_statements.isEmpty()) {
    			Iterator typedef_enum = global_typedef_statements.iterator();
    			while (typedef_enum.hasNext()) sb.append( "typedef " ).append( typedef_enum.next() ).append( ";\n" );
				}
			}
			else {
				sb.append( "// global type definitions for class implementation in source file defined by Tag entries in ArgoUML\n" )
				  .append( "// Result: typedef <typedef_global_source> <tag_value>;\n" );
				Collection global_typedef_statements = findTagValues( cls, "typedef_global_source" );
				if (!global_typedef_statements.isEmpty()) {
    			Iterator typedef_enum = global_typedef_statements.iterator();
    			while (typedef_enum.hasNext()) sb.append( "typedef " ).append( typedef_enum.next() ).append( ";\n" );
				}
			}
		}
		return sb;
	}

	/**
	 * Generates the attributes of the body of a class or interface.
	 * @param cls
	 * @return StringBuffer
	 */
	private void generateClassifierBodyAttributes(MClassifier cls, StringBuffer sb)
	{
		Collection strs = UmlHelper.getHelper().getCore().getAttributes(cls);
		if (strs.isEmpty() || ( GeneratorPass != header_pass ) ) return;
   	String tv = null; // helper for tagged values
		//
		// 2002-06-08
		// Jaap Branderhorst
		// Bugfix: strs is never null. Should check for isEmpty instead
		// old code:
		// if (strs != null)
		// new code:
		//
		sb.append('\n');
		if (_verboseDocs && cls instanceof MClass)
		{
    		sb.append(INDENT).append("// Attributes\n");
		}

		// generate attributes in order public, protected, private
		for ( int public_protected_private = public_part; public_protected_private <= private_part; public_protected_private++)
		{
  		Iterator strEnum = strs.iterator();
			boolean isVisibilityLinePrinted = false;
  		while (strEnum.hasNext())
  		{
    		MStructuralFeature sf = (MStructuralFeature) strEnum.next();
				MVisibilityKind vis = sf.getVisibility();
				if ( ( ( public_protected_private == public_part ) && (MVisibilityKind.PUBLIC.equals(vis) ) )
					|| ( ( public_protected_private == protected_part ) && (MVisibilityKind.PROTECTED.equals(vis) ) )
					|| ( ( public_protected_private == private_part ) && (MVisibilityKind.PRIVATE.equals(vis) ) )
					 )
				{
					if ( !isVisibilityLinePrinted )
					{
						isVisibilityLinePrinted = true;
						if ( public_protected_private == public_part ) sb.append( "\n public:" );
						else if ( public_protected_private == protected_part ) sb.append( "\n protected:" );
						else if ( public_protected_private == private_part ) sb.append( "\n private:" );
					}
      		sb.append(generate((MAttribute) sf));

      		tv = generateTaggedValues(sf, AllButDocTags);
      		if (tv != null && tv.length() > 0)
      		{
        		sb.append(INDENT).append(tv);
    			}
				}
			}
		}
	}

	/**
	 * Generates the association ends of the body of a class or interface.
	 * @param cls
	 * @return StringBuffer
	 */
	private void generateClassifierBodyAssociations(MClassifier cls, StringBuffer sb)
	{
		Collection ends = cls.getAssociationEnds();
		if (ends.isEmpty() || ( GeneratorPass != header_pass ) ) return;
   	String tv = null; // helper for tagged values
		// 2002-06-08
		// Jaap Branderhorst
		// Bugfix: ends is never null. Should check for isEmpty instead
		// old code:
		// if (ends != null)
		// new code:
		sb.append('\n');
		if (_verboseDocs && cls instanceof MClass)
		{
    		sb.append(INDENT).append("// Associations\n");
		}

		// generate attributes in order public, protected, private
		for ( int public_protected_private = public_part; public_protected_private <= private_part; public_protected_private++)
		{
  		Iterator endEnum = ends.iterator();
			boolean isVisibilityLinePrinted = false;
  		while (endEnum.hasNext())
  		{
    		MAssociationEnd ae = (MAssociationEnd) endEnum.next();
    		MAssociation a = ae.getAssociation();
				MVisibilityKind vis = ae.getVisibility();
				if ( ( ( public_protected_private == public_part ) && (MVisibilityKind.PUBLIC.equals(vis) ) )
					|| ( ( public_protected_private == protected_part ) && (MVisibilityKind.PROTECTED.equals(vis) ) )
					|| ( ( public_protected_private == private_part ) && (MVisibilityKind.PRIVATE.equals(vis) ) )
					 )
				{
					if ( !isVisibilityLinePrinted )
					{
						isVisibilityLinePrinted = true;
						if ( public_protected_private == public_part ) sb.append( "\n public:" );
						else if ( public_protected_private == protected_part ) sb.append( "\n protected:" );
						else if ( public_protected_private == private_part ) sb.append( "\n private:" );
					}

      		sb.append(generateAssociationFrom(a, ae));

      		tv = generateTaggedValues(a, AllButDocTags);
      		if (tv != null && tv.length() > 0)
      		{
          		sb.append(INDENT).append(tv);
      		}
				}
  		}
		}
	}

	/**
	 * Check whether an operation body shall be generated within the actual
	 * pass. This is normally done during the imeplementation path.
	 * But if the Tag "inline" exists, the method body shall be defined as
	 * as inline in header file
	 * @return true -> generate body in actual path
	 */
	private boolean checkGenerateOperationBody(MOperation cls )
	{
		boolean result = ( ( GeneratorPass == header_pass ) || (cls.isAbstract() ) )?false:true;

		// if this operation has Tag "inline" the method shall be generated in header
    Collection tValues = cls.getTaggedValues();
    if (!tValues.isEmpty()) {
      Iterator iter = tValues.iterator();
      while(iter.hasNext()) {
        MTaggedValue tv = (MTaggedValue)iter.next();
        String tag = tv.getTag();
        if (tag.equals("inline")) {
          result = ( GeneratorPass == header_pass )?true:false;
        }
      }
		}
		return result;
	}

	/** 2002-12-13 Achim Spangler
	  * generate a single set function for a given attribute and StringBuffer
		*/
	private void generateSingleAttributeSet( MAttribute attr, StringBuffer sb ) {
		if ( attr.getType() == null ) return;
		// generate for attributes with class-type: "INDENT void set_<name>( const <type> &value ) { <name> = value; };"
		// generate for other (small) data types: "INDENT void set_<name>( <type> value ) { <name> = value; };"
		// generate: "INDENT void set_<name>( "
		sb.append( '\n' ).append( INDENT ).append( "/** simple access function to set the attribute " ).append( attr.getName() )
		  .append( " by function\n" ).append( INDENT ).append( "  * @param value value to set for the attribute " )
			.append( attr.getName() ).append( "\n" ).append( INDENT ).append( "  */\n" )
		  .append( INDENT ).append( "void set_" ).append( attr.getName() ).append( "( " );
		String modifier = generateAttributeParameterModifier( attr );
		if ( modifier != null && modifier.length() > 0 ) // generate: "const <type> <modifier>value"
		{
			if ( modifier.equals( "&" ) ) sb.append( "const " );
			sb.append( generateClassifierRef( attr.getType() ) )
			  .append( ' ' ).append( modifier ).append( "value" );
		}
		else if ( attr.getType() instanceof MClass ) // generate: "const <type> &value"
			sb.append( "const " ).append( generateClassifierRef( attr.getType() ) ).append( " &value" );
		else // generate: "<type> value"
			sb.append( generateClassifierRef( attr.getType() ) )
			  .append( " value" );
		// generate: " ) { <name> = value; };"
		sb.append( " ) { " ).append( attr.getName() ).append( " = value; };" );
	}

	/** 2002-12-13 Achim Spangler
	  * generate a single get function for a given attribute and StringBuffer
		*/
	private void generateSingleAttributeGet( MAttribute attr, StringBuffer sb ) {
		if ( attr.getType() == null ) return;
		// generate for attributes with class-type: "const <type>& get_<name>( void ) { return <name>; };"
		// generate for other (small) data types: "<type> get_<name>( void ) { return <name>; };"
		// generate: "INDENT"
		sb.append( '\n' ).append( INDENT ).append( "/** simple access function to get the attribute " ).append( attr.getName() )
		  .append( " by function */\n" )
		  .append( INDENT );
		String modifier = generateAttributeParameterModifier( attr );
		if ( modifier != null && modifier.length() > 0 ) // generate: "const <type><modifier>"
		{
			sb.append( "const " ).append( generateClassifierRef( attr.getType() ) )
			  .append( modifier );
		}
		else if ( attr.getType() instanceof MClass ) // generate: "const <type>&"
			sb.append( "const " ).append( generateClassifierRef( attr.getType() ) ).append( "&" );
		else // generate: "<type>"
			sb.append( generateClassifierRef( attr.getType() ) );
		// generate: " get_<name>( void ) const { return <name>; };"
		sb.append( " get_" ).append( attr.getName() ).append( "( void ) const { return ").append( attr.getName() ).append( "; };" );
	}

	/**
	 * Generates the attributes of the body of a class or interface.
	 * @param cls
	 * @return StringBuffer
	 */
	private void generateClassifierBodyTaggedAccess4Attributes(MClassifier cls, StringBuffer func_private,
		                                                         StringBuffer func_protected, StringBuffer func_public)
	{
		Collection strs = UmlHelper.getHelper().getCore().getAttributes(cls);
		if (strs.isEmpty() || ( GeneratorPass != header_pass ) ) return;
		String access_tag = null;

 		Iterator strEnum = strs.iterator();
 		while (strEnum.hasNext())
 		{
			MAttribute attr = (MAttribute)strEnum.next();
			access_tag = attr.getTaggedValue( "set" );
			if ( access_tag != null && access_tag.length() > 0 ) {
				if ( access_tag.indexOf( "public" ) != -1 ) generateSingleAttributeSet( attr, func_public );
				if ( access_tag.indexOf( "protected" ) != -1 ) generateSingleAttributeSet( attr, func_protected );
				if ( access_tag.indexOf( "private" ) != -1 ) generateSingleAttributeSet( attr, func_private );
			}

			access_tag = attr.getTaggedValue( "get" );
			if ( access_tag != null && access_tag.length() > 0 ) {
				if ( access_tag.indexOf( "public" ) != -1 ) generateSingleAttributeGet( attr, func_public );
				if ( access_tag.indexOf( "protected" ) != -1 ) generateSingleAttributeGet( attr, func_protected );
				if ( access_tag.indexOf( "private" ) != -1 ) generateSingleAttributeGet( attr, func_private );
			}
		}
	}

	/**
	 * Generates the association ends of the body of a class or interface.
	 * @param cls
	 * @return StringBuffer
	 */
	private void generateClassifierBodyOperations(MClassifier cls, StringBuffer sb)
	{
		Collection behs = UmlHelper.getHelper().getCore().getOperations(cls);
		if (behs.isEmpty()) return;
   	String tv = null; // helper for tagged values
		//
		// 2002-06-08
		// Jaap Branderhorst
		// Bugfix: behs is never null. Should check for isEmpty instead
		// old code:
		// if (behs != null)
		// new code:
		//
		sb.append('\n');
		if (_verboseDocs)
		{
    		sb.append(INDENT).append("// Operations\n");
		}

		// generate tag controlled access functions for attributes
		StringBuffer func_private = new StringBuffer(80),
		             func_protected = new StringBuffer(80),
								 func_public = new StringBuffer(80);
		generateClassifierBodyTaggedAccess4Attributes(cls, func_private, func_protected, func_public);

		// generate attributes in order public, protected, private
		for ( int public_protected_private = public_part; public_protected_private <= private_part; public_protected_private++)
		{
    	Iterator behEnum = behs.iterator();
			boolean isVisibilityLinePrinted = false;

			if ( ( public_protected_private == private_part ) && ( func_private.length() > 0 ) ) {
				sb.append( "\n private:" ).append( func_private.toString() );
				isVisibilityLinePrinted = true;
			}
			if ( ( public_protected_private == protected_part ) && ( func_protected.length() > 0 ) ) {
				sb.append( "\n protected:" ).append( func_protected.toString() );
				isVisibilityLinePrinted = true;
			}
			if ( ( public_protected_private == public_part ) && ( func_public.length() > 0 ) ) {
				sb.append( "\n public:" ).append( func_public.toString() );
				isVisibilityLinePrinted = true;
			}

    	while (behEnum.hasNext())
    	{
        	MBehavioralFeature bf = (MBehavioralFeature) behEnum.next();
					MVisibilityKind vis = bf.getVisibility();
					if ( ( ( ( public_protected_private == public_part ) && (MVisibilityKind.PUBLIC.equals(vis) ) )
							|| ( ( public_protected_private == protected_part ) && (MVisibilityKind.PROTECTED.equals(vis) ) )
							|| ( ( public_protected_private == private_part ) && (MVisibilityKind.PRIVATE.equals(vis) ) )
							 )
						&& (( GeneratorPass == header_pass ) || ( checkGenerateOperationBody((MOperation) bf) ))
						 )
					{
						if ( ( !isVisibilityLinePrinted ) && ( GeneratorPass == header_pass ) )
						{
							isVisibilityLinePrinted = true;
							if ( public_protected_private == public_part ) sb.append( "\n public:" );
							else if ( public_protected_private == protected_part ) sb.append( "\n protected:" );
							else if ( public_protected_private == private_part ) sb.append( "\n private:" );
						}

						sb.append(generate(bf));

          	tv = generateTaggedValues((MModelElement) bf, AllButDocTags);

          	if ((cls instanceof MClass)
              	&& (bf instanceof MOperation)
              	&& (!((MOperation) bf).isAbstract())
								&& ( checkGenerateOperationBody((MOperation) bf) ))
          	{
              	// there is no ReturnType in behavioral feature (nsuml)
              	sb.append( "\n" )
									.append(generateMethodBody((MOperation) bf));
          	}
          	else
          	{
              	sb.append(";\n");
              	if (tv.length() > 0)
              	{
                  	sb.append(INDENT).append(tv).append('\n');
              	}
          	}
				}
    	}
		}
	}

	/**
	 * Generates the association ends of the body of a class or interface.
	 * @param cls
	 * @return StringBuffer
	 */
	private void generateClassifierBodyTypedefs(MClassifier cls, StringBuffer sb)
	{
		if ( GeneratorPass == header_pass ) {
			Collection public_typedef_statements = findTagValues( cls, "typedef_public" );
			Collection protected_typedef_statements = findTagValues( cls, "typedef_protected" );
			Collection private_typedef_statements = findTagValues( cls, "typedef_private" );
			if (!public_typedef_statements.isEmpty()) {
				sb.append( "\n public:\n" )
				  .append( INDENT ).append( "// public type definitions for header defined by Tag entries in ArgoUML\n" )
				  .append( INDENT ).append( "// Result: typedef <typedef_public> <tag_value>;\n" );
    		Iterator typedef_enum = public_typedef_statements.iterator();

    		while (typedef_enum.hasNext())
					sb.append( INDENT ).append( "typedef " ).append( typedef_enum.next() ).append( ";\n" );
			}
			if (!protected_typedef_statements.isEmpty()) {
				sb.append( "\n protected:\n" )
				  .append( INDENT ).append( "// protected type definitions for header defined by Tag entries in ArgoUML\n" )
				  .append( INDENT ).append( "// Result: typedef <typedef_protected> <tag_value>;\n" );
    		Iterator typedef_enum = protected_typedef_statements.iterator();

    		while (typedef_enum.hasNext())
					sb.append( INDENT ).append( "typedef " ).append( typedef_enum.next() ).append( ";\n" );
			}
			if (!private_typedef_statements.isEmpty()) {
				sb.append( "\n private:\n" )
				  .append( INDENT ).append( "// private type definitions for header defined by Tag entries in ArgoUML\n" )
				  .append( INDENT ).append( "// Result: typedef <typedef_private> <tag_value>;\n" );
    		Iterator typedef_enum = private_typedef_statements.iterator();

    		while (typedef_enum.hasNext())
					sb.append( INDENT ).append( "typedef " ).append( typedef_enum.next() ).append( ";\n" );
			}
		}
	}

	/**
	 * Generates the body of a class or interface.
	 * @param cls
	 * @return StringBuffer
	 */
	protected StringBuffer generateClassifierBody(MClassifier cls)
	{
  	StringBuffer sb = new StringBuffer();
  	if (cls instanceof MClass || cls instanceof MInterface )
  	{ // add operations
    	// TODO: constructors
			generateClassifierBodyOperations( cls, sb );

    	// add attributes
			generateClassifierBodyAttributes( cls, sb );

    	// add attributes implementing associations
			generateClassifierBodyAssociations( cls, sb );

			// add typedefs
			generateClassifierBodyTypedefs( cls, sb );
  	}
  	return sb;
  }

  /**
   * Generate the body of a method associated with the given operation. This
   * assumes there's at most one method associated!
   *
   * If no method is associated with the operation, a default method body will
   * be generated.
   */
  public String generateMethodBody (MOperation op) {
    if (op != null) {
			StringBuffer sb = new StringBuffer(80);
      Collection methods = op.getMethods();
      Iterator i = methods.iterator();
      MMethod m = null;
			boolean MethodFound = false;
			String tv = generateTaggedValues(op, AllButDocTags);
			String OperationIndent = ( GeneratorPass == header_pass )?INDENT:"";

			// append tags which are not Doc-Comments
      if (tv.length() > 0)
      {
      	sb.append(OperationIndent).append(tv).append('\n');
      }

			// place the curley braces within the protected area, to allow
			// placement of preserved contructor initialisers in this area
			// otherwise all possible constructor-attribute initialisers would have
			// to be autogenerated with an army of special tags
			sb.append( generateSectionTop(op, OperationIndent) )
			  .append( OperationIndent ).append( "{\n" );

      // System.out.print(", op!=null, size="+methods.size());
      // return INDENT + INDENT + "/* method body for " + op.getName() + " */";

      while (i != null && i.hasNext()) {
        //System.out.print(", i!= null");
        m = (MMethod) i.next();

        if (m != null) {
          //Argo.log.info(", BODY of "+m.getName());
          //Argo.log.info("|"+m.getBody().getBody()+"|");
          if (( m.getBody() != null) && ( !MethodFound ) ) {
            sb.append( m.getBody().getBody() );
						MethodFound = true;
						break;
					}
        }
      }

			if ( !MethodFound ) {
      	// pick out return type as default method body
		    MParameter rp = UmlHelper.getHelper().getCore().getReturnParameter(op);
      	if (rp != null) {
        	MClassifier returnType = rp.getType();
        	sb.append( generateDefaultReturnStatement (returnType) );
      	}
			}
			sb.append( OperationIndent ).append( "}\n" )
		  	.append( generateSectionBottom(op, OperationIndent) );
			return sb.toString();
    }
		return generateDefaultReturnStatement (null);
  }


	public String generateSectionTop(MOperation op, String LocalIndent){
    	String id = op.getUUID();
    	if (id == null){
        	id = (new UID().toString());
        	// id =  op.getName() + "__" + static_count;
        	op.setUUID(id);
    	}
    	return Section.generateTop(id, LocalIndent);
	}
	public String generateSectionBottom(MOperation op, String LocalIndent){
    	String id = op.getUUID();
    	if (id == null){
        	id = (new UID().toString());
        	// id =  op.getName() + "__" + static_count;
        	op.setUUID(id);
    	}
    	return Section.generateBottom(id, LocalIndent);
	}
	public String generateSection(MOperation op){
    	String id = op.getUUID();
    	if (id == null){
        	id = (new UID().toString());
        	// id =  op.getName() + "__" + static_count;
        	op.setUUID(id);
    	}
    	// String s = "";
    	// s += INDENT + "// section " + id + " begin\n";
    	// s += INDENT + "// section " + id + " end\n";
    	return Section.generate(id, INDENT);
	}




  public String generateDefaultReturnStatement(MClassifier cls) {
    if (cls == null) return "";

    String clsName = cls.getName();
    if (clsName.equals("void")) return "";
    if (clsName.equals("char")) return INDENT + "return 'x';\n";
    if (clsName.equals("int")) return INDENT + "return 0;\n";
    if (clsName.equals("boolean")) return INDENT + "return false;\n";
    if (clsName.equals("byte")) return INDENT + "return 0;\n";
    if (clsName.equals("long")) return INDENT + "return 0;\n";
    if (clsName.equals("float")) return INDENT + "return 0.0;\n";
    if (clsName.equals("double")) return INDENT + "return 0.0;\n";
    return INDENT + "return null;\n";
  }

  public String generateTaggedValues(MModelElement e, int tag_selection) {
    // Argo.log.info("generateTaggedValues for element: "  + e.getName() + " und selection " + tag_selection);

    Collection tvs = e.getTaggedValues();
    if (tvs == null || tvs.size() == 0) return "";
    boolean first=true;
    StringBuffer buf = new StringBuffer();

    Iterator iter = tvs.iterator();
    String s = null;
    while(iter.hasNext())
		{
        s = generateTaggedValue((MTaggedValue)iter.next(), tag_selection);
        if (s != null && s.length() > 0)
				{
            if (first)
        /*
         * Corrected 2001-09-26 STEFFEN ZSCHALER
         *
         * Was:
                                buf.append("// {");
         *
         * which caused problems with new lines characters in tagged values
         * (e.g. comments...). The new version still has some problems with
         * tagged values containing "*"+"/" as this closes the comment
         * prematurely, but comments should be taken out of the tagged values
         * list anyway...
         */
				  	{
                if ( tag_selection == DocCommentTags ) {
									// insert main documentation for DocComment at first
    							String doc = (DocumentationManager.hasDocs(e)) ? DocumentationManager.getDocs(e,INDENT) : null;
    							if (doc != null && doc.trim().length() > 0) {
      							buf.append(doc.substring(0,doc.indexOf("*/")+1)).append("  ");
    							}
									else
									{
										buf.append("/** ");
									}
								}
								else {
									buf.append("/* {");
								}
                first = false;
            } // end first
						else
						{
							if ( tag_selection == DocCommentTags )
							{
								buf.append("\n").append(INDENT).append(" *  ");
							}
							else
							{
                buf.append(", ");
							}
            } // end not first tag
            buf.append(s);
        } // end tag not empty
    } // end while
    /*
     * Corrected 2001-09-26 STEFFEN ZSCHALER
     *
     * Was:
    if (!first) buf.append("}\n");
     *
     * which caused problems with new-lines in tagged values.
     */
    if (!first)
		{
			if ( tag_selection == DocCommentTags )
			{
				buf.append("\n").append(INDENT).append(" */\n");
			}
			else
			{
				buf.append ("}*/\n");
			}
		}
		else if ( tag_selection == DocCommentTags )
		{ // create at least main documentation field, if no other tag found
    	String doc = (DocumentationManager.hasDocs(e)) ? DocumentationManager.getDocs(e,INDENT) : null;
    	if (doc != null && doc.trim().length() > 0) {
      	buf.append(doc).append('\n');
    	}
		}

    return buf.toString();
  }

  public String generateTaggedValue(MTaggedValue tv, int tag_selection) {
    // Argo.log.info("generateTaggedValue: "  + generateName(tv.getTag()) + " mit selection: " + tag_selection );
    if (tv == null) return "";
    String s=generateUninterpreted(tv.getValue());
    if (s == null || s.length() == 0 || s.equals("/** */")
		   || (tv.getTag().indexOf("include") != -1)|| (tv.getTag().indexOf("_incl") != -1)) return "";
		if ( ( tag_selection == DocCommentTags ) && ( isDocCommentTag(tv.getTag())))
		{
			return generateDocComment4Tag(generateName(tv.getTag())) + s;
		}
		else if ( ( ( tag_selection == AllButDocTags )
		         && ( !isDocCommentTag(tv.getTag()))
						 && (!tv.getTag().equals("documentation"))
						 && (!tv.getTag().equals("javadocs"))
						 )
					 || ( tag_selection == AllTags ) )
		{
	    return generateName(tv.getTag()) + "=" + s;
		}
		else
		{
			return "";
		}
  }

	private Collection findTagValues( MModelElement item, String searched_name ) {
		Collection result = new Vector();
    Collection tvs = item.getTaggedValues();
    if (tvs == null || tvs.size() == 0) return result;

    Iterator iter = tvs.iterator();
		MTaggedValue tag;
    String s = null;
    while(iter.hasNext())
		{
			tag = (MTaggedValue)iter.next();
			if ( tag.getTag().equals( searched_name ) ) {
				s = tag.getValue();
				if (s != null && s.length() != 0 ) result.add(s);
			}
		}
		return result;
	}


	private boolean isDocCommentTag( String tag_name ) {
    // Argo.log.info("isDocCommentTag:"  + tag_name + ":");
  	boolean result = false;
		if (tag_name.equals ("inv")) {
	    // Argo.log.info("yes it is doc-comment");
    	result = true;
  	}
  	else if (tag_name.equals ("post")) {
	    // Argo.log.info("yes it is doc-comment");
    	result = true;
  	}
  	else if (tag_name.equals ("pre")) {
	    // Argo.log.info("yes it is doc-comment");
    	result = true;
  	}
  	else if (tag_name.equals ("author")) {
	    // Argo.log.info("yes it is doc-comment");
    	result = true;
  	}
  	else if (tag_name.equals ("version")) {
	    // Argo.log.info("yes it is doc-comment");
    	result = true;
  	}
  	else if (tag_name.equals ("see")) {
	    // Argo.log.info("yes it is doc-comment");
    	result = true;
		}
  	else if (tag_name.equals ("param")) {
	    // Argo.log.info("yes it is doc-comment");
    	result = true;
		}
		return result;
	}
	private String generateDocComment4Tag( String tag_name ) {
    if (tag_name.equals ("inv")) {
      return "@invariant ";
    }
    else if (tag_name.equals ("post")) {
      return "@postcondition ";
    }
    else if (tag_name.equals ("pre")) {
      return "@precondition ";
    }
  	else if (tag_name.equals ("author")) {
      return "@author ";
  	}
  	else if (tag_name.equals ("version")) {
      return "@version ";
  	}
    else if (tag_name.equals ("see")) {
      return "@see ";
    }
    else if (tag_name.equals ("param")) {
      return "@param ";
    }
		else return "";
	}
  public String generateTaggedValues(MModelElement e) {
    Collection tvs = e.getTaggedValues();
    if (tvs == null || tvs.size() == 0) return "";
    boolean first=true;
    StringBuffer buf = new StringBuffer();
    Iterator iter = tvs.iterator();
    String s = null;
    while(iter.hasNext()) {
        /*
         * 2002-11-07
         * Jaap Branderhorst
         * Was
         * s = generateTaggedValue((MTaggedValue) iter.next());
         * which caused problems because the test tags (i.e. tags with name <NotationName.getName()>+TEST_SUFFIX)
         * were still generated.
         * New code:
         */
        s = generate((MTaggedValue) iter.next());
        // end new code
        if (s != null && s.length() > 0) {
			if (first) {
        /*
         * Corrected 2001-09-26 STEFFEN ZSCHALER
         *
         * Was:
				buf.append("// {");
         *
         * which caused problems with new lines characters in tagged values
         * (e.g. comments...). The new version still has some problems with
         * tagged values containing "*"+"/" as this closes the comment
         * prematurely, but comments should be taken out of the tagged values
         * list anyway...
         */
        buf.append ("/* {");

				first = false;
			} else {
				buf.append(", ");
			}
			buf.append(s);
		}
    }
    /*
     * Corrected 2001-09-26 STEFFEN ZSCHALER
     *
     * Was:
    if (!first) buf.append("}\n");
     *
     * which caused problems with new-lines in tagged values.
     */
    if (!first) buf.append ("}*/\n");

    return buf.toString();
  }

  public String generateTaggedValue(MTaggedValue tv) {
    if (tv == null) return "";
    String s=generateUninterpreted(tv.getValue());
    if (s == null || s.length() == 0 || s.equals("/** */")) return "";
    String t = tv.getTag();
    if (t.equals("documentation")) return "";
    return generateName(t) + "=" + s;
  }
  /**
   * Enhance/Create the doccomment for the given model element, including tags
   * for any OCL constraints connected to the model element. The tags generated
   * are suitable for use with the ocl injector which is part of the Dresden OCL
   * Toolkit and are in detail:
   *
   * &nbsp;@invariant for each invariant specified
   * &nbsp;@precondition for each precondition specified
   * &nbsp;@postcondition for each postcondition specified
   * &nbsp;@key-type specifying the class of the keys of a mapped association
   * &nbsp; Currently mapped associations are not supported yet...
   * &nbsp;@element-type specifying the class referenced in an association
   *
   * @since 2001-09-26 ArgoUML 0.9.3
   * @author Steffen Zschaler
   *
   * @param me the model element for which the documentation comment is needed
   * @param ae the association end which is represented by the model element
   * @return the documentation comment for the specified model element, either
   * enhanced or completely generated
   */
  public String generateConstraintEnrichedDocComment (MModelElement me,
                                                      MAssociationEnd ae) {
    // list tagged values for documentation
    String s = generateTaggedValues (me, DocCommentTags);


    MMultiplicity m = ae.getMultiplicity();
    if (! (MMultiplicity.M1_1.equals(m) || MMultiplicity.M0_1.equals (m))) {
      // Multiplicity greater 1, that means we will generate some sort of
      // collection, so we need to specify the element type tag
      StringBuffer sDocComment = new StringBuffer(80);

      // Prepare doccomment
      if (!(s == null || "".equals(s))) {
        // Just remove closing "*/"
        sDocComment.append(s.substring(0,s.indexOf("*/")+1));
      }
      else {
        sDocComment.append(INDENT).append("/**\n")
                   .append(INDENT).append(" * \n")
                   .append(INDENT).append(" *");
      }

      // Build doccomment
      MClassifier type = ae.getType();
      if (type != null) {
          sDocComment.append(" @element-type ").append(type.getName());
      } else {
          // REMOVED: 2002-03-11 STEFFEN ZSCHALER: element type unknown is not recognized by the OCL injector...
          //sDocComment += " @element-type unknown";
      }
      sDocComment.append('\n').append(INDENT).append(" */\n");
      return sDocComment.toString();
    }
    else {
      return (s != null) ? s : "";
    }
  }

  public String generateConstraints(MModelElement me) {

    // This method just adds comments to the generated java code. This should be code generated by ocl-argo int he future?
    Collection cs = me.getConstraints();
    if (cs == null || cs.size() == 0) return "";
    String s = INDENT + "// constraints\n";
    int size = cs.size();
    // MConstraint[] csarray = (MConstraint[])cs.toArray();
    // Argo.log.info("Got " + csarray.size() + " constraints.");
    for (Iterator i = cs.iterator(); i.hasNext();) {
      MConstraint c = (MConstraint) i.next();
      String constrStr = generateConstraint(c);
      java.util.StringTokenizer st = new java.util.StringTokenizer(constrStr, "\n\r");
      while (st.hasMoreElements()) {
				String constrLine = st.nextToken();
				s += INDENT + "// " + constrLine + "\n";
      }
    }
    s += "\n";
    return s;
  }

  public String generateConstraint(MConstraint c) {
    if (c == null) return "";
    String s = "";
    if (c.getName() != null && c.getName().length() != 0)
      s += generateName(c.getName()) + ": ";
    s += generateExpression(c);
    return s;
  }


  public String generateAssociationFrom(MAssociation a, MAssociationEnd ae) {
    // TODO: does not handle n-ary associations
    StringBuffer sb = new StringBuffer(80);

    /*
     * Moved into while loop 2001-09-26 STEFFEN ZSCHALER
     *
     * Was:
     *
    s += DocumentationManager.getDocs(a) + "\n" + INDENT;
     */

    Collection connections = a.getConnections();
    Iterator connEnum = connections.iterator();
    while (connEnum.hasNext()) {
      MAssociationEnd ae2 = (MAssociationEnd) connEnum.next();
      if (ae2 != ae) {
        /**
         * Added generation of doccomment 2001-09-26 STEFFEN ZSCHALER
         *
         */
				sb.append("\n").append(INDENT);
				String comment = generateConstraintEnrichedDocComment(a,ae2);
				// the comment line ends with simple newline -> place INDENT
				// after comment, if not empty
				if ( comment.length() > 0)
        	sb.append(comment).append(INDENT);

				sb.append(generateAssociationEnd(ae2));
      }
    }

    return sb.toString();
  }

  public String generateAssociation(MAssociation a) {
//    String s = "";
//     String generatedName = generateName(a.getName());
//     s += "MAssociation " + generatedName + " {\n";

//     Iterator endEnum = a.getConnection().iterator();
//     while (endEnum.hasNext()) {
//       MAssociationEnd ae = (MAssociationEnd)endEnum.next();
//       s += generateAssociationEnd(ae);
//       s += ";\n";
//     }
//     s += "}\n";
//    return s;
    return "";
  }

  public String generateAssociationEnd(MAssociationEnd ae) {
    if (!ae.isNavigable()) return "";
    if (ae.getAssociation().isAbstract()) return "";
    //String s = INDENT + "protected ";
    // must be public or generate public navigation method!
    //String s = INDENT + "public ";
    StringBuffer sb = new StringBuffer(80);
		// Argo.log.info("generate Visibility for Attribute");

//    sb.append(INDENT).append(generateVisibility(ae.getVisibility()));

    sb.append( generateScope(ae) );
//     String n = ae.getName();
//     if (n != null && !String.UNSPEC.equals(n)) s += generateName(n) + " ";
//     if (ae.isNavigable()) s += "navigable ";
//     if (ae.getIsOrdered()) s += "ordered ";

		String n = ae.getName();
    MAssociation asc = ae.getAssociation();
    String ascName = asc.getName();
		String name = null;

    if (n != null  && n != null && n.length() > 0) {
			name = generateName(n);
    }
    else if (ascName != null  && ascName != null && ascName.length() > 0) {
			name = generateName(ascName);
    }
    else {
			name = "my" + generateClassifierRef(ae.getType());
    }

		sb.append( generateMultiplicity(ae, name, ae.getMultiplicity(), generateAttributeParameterModifier(asc)));

    return (sb.append(";\n")).toString();
  }


  ////////////////////////////////////////////////////////////////
  // internal methods?


  public String generateGeneralization(Collection generalizations) {
    if (generalizations == null) return "";
		StringBuffer sb = new StringBuffer(80);
    Collection classes = new ArrayList();
    Iterator enum = generalizations.iterator();
    while (enum.hasNext()) {
      MGeneralization g = (MGeneralization) enum.next();
      MGeneralizableElement ge = g.getParent();
      // assert ge != null
      if (ge != null) {
				String visibility_tag = g.getTaggedValue( "visibility" );
				if ( visibility_tag != null && visibility_tag != "" )
					sb.append( visibility_tag ).append( " " );
				sb.append(generateNameWithPkgSelection(ge));
				if (enum.hasNext()) sb.append(", ");
			}
    }
    return sb.toString();
  }

  public Collection getGeneralizationClassList(Collection generalizations) {
    if (generalizations == null) return null;
    Collection classes = new ArrayList();
    Iterator enum = generalizations.iterator();
    while (enum.hasNext()) {
      MGeneralization g = (MGeneralization) enum.next();
      MGeneralizableElement ge = g.getParent();
      // assert ge != null
      if (ge != null) classes.add(ge);
    }
    return classes;
  }

    //  public String generateSpecification(Collection realizations) {
	public String generateSpecification(MClass cls) {
		Collection deps = cls.getClientDependencies();
		Iterator depIterator = deps.iterator();
		StringBuffer sb = new StringBuffer(80);

		while (depIterator.hasNext()) {
			MDependency dep = (MDependency)depIterator.next();
			if (ModelFacade.isAAbstraction(dep) &&
			    dep.getStereotype() != null &&
			    dep.getStereotype().getName() != null &&
			    dep.getStereotype().getName().equals("realize")) {
			    MInterface i = (MInterface)dep.getSuppliers().toArray()[0];
					String visibility_tag = dep.getTaggedValue( "visibility" );
					if ( visibility_tag != null && visibility_tag != "" )
						sb.append( visibility_tag ).append( " " );
					sb.append(generateNameWithPkgSelection(i));
					if (depIterator.hasNext()) sb.append(", ");
			}
		}
		return sb.toString();
	}

	public String generateClassList(Collection classifiers) {
		if (classifiers == null) return "";
		StringBuffer sb = new StringBuffer(80);
		Iterator clsEnum = classifiers.iterator();
		while (clsEnum.hasNext()) {
			MClassifier cls = (MClassifier)clsEnum.next();
			String visibility_tag = cls.getTaggedValue( "visibility" );
			if ( visibility_tag != null && visibility_tag != "" )
				sb.append( visibility_tag ).append( " " );
			sb.append(generateNameWithPkgSelection(cls));
			if (clsEnum.hasNext()) sb.append(", ");
		}
		return sb.toString();
	}

  public String generateVisibility(MVisibilityKind vis) {
    if (MVisibilityKind.PUBLIC.equals(vis)) return "public ";
    if (MVisibilityKind.PRIVATE.equals(vis)) return "private ";
    if (MVisibilityKind.PROTECTED.equals(vis)) return "protected ";
    return "";
  }

  public String generateVisibility(MFeature f) {
		// Argo.log.info("generate Visibility for MFeature");
		if (f instanceof MAttribute) return "";
    MVisibilityKind vis = f.getVisibility();
    if (MVisibilityKind.PUBLIC.equals(vis)) return "public ";
    if (MVisibilityKind.PRIVATE.equals(vis)) return "private ";
    if (MVisibilityKind.PROTECTED.equals(vis)) return "protected ";
    return "";
  }

  public String generateScope(MAssociationEnd ae) {
    if (MScopeKind.CLASSIFIER.equals(ae.getTargetScope()))
			return "static ";
		else
			return "";
  }

  public String generateScope(MFeature f) {
    MScopeKind scope = f.getOwnerScope();
    //if (scope == null) return "";
    if (MScopeKind.CLASSIFIER.equals(scope)) return "static ";
    return "";
  }

  /**
   * Generate "abstract" keyword for an abstract operation.
   */
  public String generateAbstractness (MOperation op) {
    if (op.isAbstract()) {
      return " = 0";
    }
    else {
      return "";
    }
  }

  /**
   * Generate "final" keyword for final operations.
   */
  public String generateChangeability (MOperation op) {
    if (op.isLeaf() || op.isQuery()) {
      return "const ";
    }
    else {
      return "";
    }
  }

  /**
   * Generate "const" keyword for const pointer/reference parameters.
   */
  public String generateChangeability (MParameter par) {
		if ( ( checkAttributeParameter4Tag( par, SearchReferencePointerTag ))
		  && ((par.getKind()).equals(MParameterDirectionKind.IN)) ) {
			return "const ";
		}
    else {
      return "";
    }
  }

  public String generateChangability(MStructuralFeature sf) {
    MChangeableKind ck = sf.getChangeability();
    //if (ck == null) return "";
    if (MChangeableKind.FROZEN.equals(ck)) return "final ";
    //if (MChangeableKind.ADDONLY.equals(ck)) return "final ";
    return "";
  }

  /**
   * Generates "synchronized" keyword for guarded operations.
   * @param op The operation
   * @return String The synchronized keyword if the operation is guarded, else ""
   */
  public String generateConcurrency(MOperation op)
  {
      if (op.getConcurrency() != null
          && op.getConcurrency().getValue() == MCallConcurrencyKind._GUARDED)
      {
          return "synchronized ";
      }
      return "";
  }

  public String generateMultiplicity(MMultiplicity m) {
    if (m == null) { return ""; }
    if (MMultiplicity.M0_N.equals(m)) return ANY_RANGE;
    String s = "";
    Collection v = m.getRanges();
    if (v == null) return s;
    Iterator rangeEnum = v.iterator();
    while (rangeEnum.hasNext()) {
      MMultiplicityRange mr = (MMultiplicityRange) rangeEnum.next();
      s += generateMultiplicityRange(mr);
      if (rangeEnum.hasNext()) s += ",";
    }
    return s;
  }

  public String generateMultiplicity(MModelElement item, String name, MMultiplicity m, String modifier) {
		String type = null,
					 container_type = null;
    // Argo.log.info("generateMultiplicity mit item" + item.getName() + ", name: " + name + ", modifier: " + modifier);

		MClassifier type_cls = null;
		if ( item instanceof MAssociationEnd ) type_cls = ((MAssociationEnd)item).getType();
		else if ( item instanceof MAttribute ) type_cls = ((MAttribute)item).getType();
		else if ( item instanceof MClassifier ) type = ((MClassifier)item).getName();
		else type = "";
		if ( type_cls != null ) type = generateNameWithPkgSelection(type_cls);
		// Argo.log.info("resolved type_name: " + type );
    if (m == null) { return (type + " " + modifier + name); }
    StringBuffer sb = new StringBuffer(80);
		int countUpper = m.getUpper(),
		    countLower = m.getLower();
		// Argo.log.info("resolved int upper/lower bounds" );
    Integer lower = new Integer(countLower);
    Integer upper = new Integer(countUpper);
		// Argo.log.info("resolved Integer upper/lower bounds" );

		if ( countUpper	== 1 ) {
			// simple generate identifier for default 0:1, 1:1 association
			sb.append( type ).append( ' ' ).append( modifier ).append( name );
		}
		else if ( countUpper == countLower ) {
			// fixed array -> <type> <name>[<count>];
			sb.append( type ).append( ' ' ).append( modifier ).append( name )
			  .append( "[ " ).append( upper.toString() ).append( " ]");
		}
		else {
			// variable association -> if no tag found use []
			// else search for tag: <MultipliciyType> : array|vector|list|slist|map|stack
			String MultType = item.getTaggedValue( "MultiplicityType" );
			if ( MultType == null ) {
				// no known container type found
				sb.append( type ).append( ' ' ).append( modifier ).append( name ).append( "[]" );
			}
			else if ( MultType.equals( "vector" ) ) {
				if ( _ExtraIncludes.indexOf( "#include <vector>" ) == -1 )
					_ExtraIncludes += "#include <vector>\n";
				container_type = "vector";
			}
			else if ( MultType.equals( "list" ) ) {
				if ( _ExtraIncludes.indexOf( "#include <list>" ) == -1 )
					_ExtraIncludes += "#include <list>\n";
				container_type = "list";
			}
			else if ( MultType.equals( "slist" ) ) {
				if ( _ExtraIncludes.indexOf( "#include <slist>" ) == -1 )
					_ExtraIncludes += "#include <slist>\n";
				container_type = "slist";
			}
			else if ( MultType.equals( "map" ) ) {
				if ( _ExtraIncludes.indexOf( "#include <map>" ) == -1 )
					_ExtraIncludes += "#include <map>\n";
				container_type = "map";
			}
			else if ( MultType.equals( "stack" ) ) {
				if ( _ExtraIncludes.indexOf( "#include <stack>" ) == -1 )
					_ExtraIncludes += "#include <stack>\n";
				container_type = "stack";
			}

			if ( container_type != null ) {
				// known container type
				String include_line = "#include <" + container_type + ">";
				if ( _ExtraIncludes.indexOf( include_line ) == -1 )
					_ExtraIncludes += include_line + "\n";
				sb.append( container_type ).append( "< " ).append( type ).append( modifier ).append( " > ")
				  .append( name );
			}
		}
		return sb.toString();
	}

  public static final String ANY_RANGE = "0..*";
  //public static final String ANY_RANGE = "*";
  // needs-more-work: user preference between "*" and "0..*"

  public String generateMultiplicityRange(MMultiplicityRange mr) {
    Integer lower = new Integer(mr.getLower());
    Integer upper = new Integer(mr.getUpper());
    if (lower == null && upper == null) return ANY_RANGE;
    if (lower == null) return "*.."+ upper.toString();
    if (upper == null) return lower.toString() + "..*";
    if (lower.intValue() == upper.intValue()) return lower.toString();
    return lower.toString() + ".." + upper.toString();

  }

  public String generateState(MState m) {
    return m.getName();
  }

  public String generateStateBody(MState m) {
      // Argo.log.info("GeneratorCpp: generating state body");
    String s = "";
    MAction entry = m.getEntry();
    MAction exit = m.getExit();
    if (entry != null) {
      String entryStr = Generate(entry);
      if (entryStr.length() > 0) s += "entry / " + entryStr;
    }
    if (exit != null) {
      String exitStr = Generate(exit);
      if (s.length() > 0) s += "\n";
      if (exitStr.length() > 0) s += "exit / " + exitStr;
    }
    Collection trans = m.getInternalTransitions();
    if (trans != null) {
	  Iterator iter = trans.iterator();
	  while(iter.hasNext())
	      {
		  if (s.length() > 0) s += "\n";
		  s += generateTransition((MTransition)iter.next());
	      }
      }

  /*   if (trans != null) {
      int size = trans.size();
	  MTransition[] transarray = (MTransition[])trans.toArray();
      for (int i = 0; i < size; i++) {
		if (s.length() > 0) s += "\n";
		s += Generate(transarray[i]);
      }
      }*/
    return s;
  }

  public String generateTransition(MTransition m) {
    String s = generate(m.getName());
    String t = generate(m.getTrigger());
    String g = generate(m.getGuard());
    String e = generate(m.getEffect());
    if (s.length() > 0) s += ": ";
    s += t;
    if (g.length() > 0) s += " [" + g + "]";
    if (e.length() > 0) s += " / " + e;
    return s;

    /*  String s = m.getName();
    String t = generate(m.getTrigger());
    String g = generate(m.getGuard());
    String e = generate(m.getEffect());
    if(s == null) s = "";
    if(t == null) t = "";
    if (s.length() > 0 &&
        (t.length() > 0 ||
        (g != null && g.length() > 0) ||
        (e != null && e.length() > 0)))
        s += ": ";
    s += t;
    if (g != null && g.length() > 0) s += " [" + g + "]";
    if (e != null && e.length() > 0) s += " / " + e;
    return s;*/
  }

  public String generateAction(MAction m) {
      // return m.getName();
      if ((m.getScript() != null) && (m.getScript().getBody() != null))
	  return m.getScript().getBody();
      return "";
  }

  public String generateGuard(MGuard m) {
      //return generateExpression(m.getExpression());
      if (m.getExpression() != null)
	  return generateExpression(m.getExpression());
      return "";
  }

    public String generateMessage(MMessage m) {
    	if (m == null) return "";
	return generateName(m.getName()) + "::" +
	    generateAction(m.getAction());
    }


    /**
       Update a source code file.

       @param mClassifier The classifier to update from.
       @param file The file to update.
    */
    private static void update(MClassifier mClassifier,
                        File file)
	throws Exception
    {
	Argo.log.info("Parsing " + file.getPath());

        // read the existing file and store preserved sections

        /*
	BufferedReader in = new BufferedReader(new FileReader(file));
	JavaLexer lexer = new JavaLexer(in);
	JavaRecognizer parser = new JavaRecognizer(lexer);
	CodePieceCollector cpc = new CodePieceCollector();
	parser.compilationUnit(cpc);
	in.close();
        */


	File origFile = new File(file.getAbsolutePath());
	File newFile = new File(file.getAbsolutePath()+".updated");
	Argo.log.info("Generating " + newFile.getPath());

        boolean eof = false;
        BufferedReader origFileReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
        FileWriter newFileWriter = new FileWriter(file.getAbsolutePath()+".updated");
        while (!eof){
            String line = origFileReader.readLine();
            if (line == null){
                eof = true;
            } else {
                newFileWriter.write(line + "\n");
            }
        }
        newFileWriter.close();
        origFileReader.close();

	// cpc.filter(file, newFile, mClassifier.getNamespace());
	Argo.log.info("Backing up " + file.getPath());
	file.renameTo(new File(file.getAbsolutePath()+".backup"));
	Argo.log.info("Updating " + file.getPath());
	newFile.renameTo(origFile);
    }

    public String generateSection(MClassifier cls){
        String id = cls.getUUID();
        if (id == null){
            id = (new UID().toString());
            // id = cls.getName() + "__" + static_count;
            cls.setUUID(id);
        }
        // String s = "";
        // s += INDENT + "// section " + id + " begin\n";
        // s += INDENT + "// section " + id + " end\n";
        return Section.generate(id, INDENT);
    }
    public boolean canParse() {
        return true;
    }

    public boolean canParse(Object o) {
        return true;
    }


    public String getModuleName() { return "GeneratorCpp"; }
    public String getModuleDescription() {
        return "Cpp Notation and Code Generator";
    }
    public String getModuleAuthor() { return "Achim Spangler"; }
    public String getModuleVersion() { return "0.9.8"; }
    public String getModuleKey() { return "module.language.cpp.generator"; }
/**
     * Returns the _lfBeforeCurly.
     * @return boolean
     */
    public boolean isLfBeforeCurly()
    {
        return _lfBeforeCurly;
    }

    /**
     * Returns the _verboseDocs.
     * @return boolean
     */
    public boolean isVerboseDocs()
    {
        return _verboseDocs;
    }
    /**
     * Sets the _lfBeforeCurly.
     * @param _lfBeforeCurly The _lfBeforeCurly to set
     */
    public void setLfBeforeCurly(boolean _lfBeforeCurly)
    {
        this._lfBeforeCurly = _lfBeforeCurly;
    }

    /**
     * Sets the _verboseDocs.
     * @param _verboseDocs The _verboseDocs to set
     */
    public void setVerboseDocs(boolean _verboseDocs)
    {
        this._verboseDocs = _verboseDocs;
    }


} /* end class GeneratorCpp */

