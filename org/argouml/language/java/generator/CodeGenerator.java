// Copyright (c) 1996-2001 The Regents of the University of California. All
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

/*
  JavaRE - Code generation and reverse engineering for UML and Java
  Author: Marcus Andersson andersson@users.sourceforge.net
*/


package org.argouml.language.java.generator;

import java.io.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.model_management.*;
import java.util.*;

// Added 2001-10-05 STEFFEN ZSCHALER
import org.argouml.uml.DocumentationManager;

/**
   This class generates or updates code.
*/
class CodeGenerator
{
    /**
       Constructor.
    */
    public CodeGenerator()
    {
    }

    /**
       This is the main entry point to the class. It decides if code
       should be generated from scratch or updated.

       @param mClassifier The classifier to generate code for.
       @param sourcePath The root directory of the source code.
    */
    public void process(MClassifier mClassifier,
                        String sourcePath)
	throws Exception
    {
	File file = new File(sourcePath + File.separator +
			     mClassifier.getName() + ".java");
	if(file.exists()) {
	    update(mClassifier, file);
	}
	else {
	    generate(mClassifier, file);
	}
    }

    /**
       Generate code for a classifier.

       @param mClassifier The classifier to generate code for.
       @param file The file to write to.
    */
    private void generate(MClassifier mClassifier,
                          File file)
	throws Exception
    {
	System.out.println("Generating " + file.getPath());

	file.getParentFile().mkdirs();
	file.createNewFile();
	BufferedWriter writer =
	    new BufferedWriter(new FileWriter(file));

	writer.write("// $Id$\n\n");

	if(!(mClassifier.getNamespace() instanceof MModel)) {
	    writer.write("package " +
			     mClassifier.getNamespace().getName() + ";\n\n");
	}

	if(mClassifier instanceof MClass) {
	    generateClass((MClass)mClassifier, writer);
	}
	else {
	    generateInterface((MInterface)mClassifier, writer);
	}

	writer.close();
    }

    /**
       Generate code for a class.

       @param mClass The class to generate code for.
       @param writer The writer to write to.
    */
    public void generateClass(MClass mClass,
                              Writer writer)
	throws Exception
    {
	ClassCodePiece ccp = new ClassCodePiece(null, mClass.getName());
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mClass.getNamespace()));
	ccp.write(writer, parseStateStack, 0);

	writer.write("\n{\n");

	// Features
	Collection features = mClass.getFeatures();
	for(Iterator i = features.iterator(); i.hasNext(); ) {
	    MFeature feature = (MFeature)i.next();
	    if(feature instanceof MOperation) {
		generateOperation((MOperation)feature, mClass, writer);
	    }
	    if(feature instanceof MAttribute) {
		generateAttribute((MAttribute)feature, mClass, writer);
	    }
	}

	// Inner classes
	Collection elements = mClass.getOwnedElements();
	for(Iterator i = elements.iterator(); i.hasNext(); ) {
	    MModelElement element = (MModelElement)i.next();
	    if(element instanceof MClass) {
		generateClass((MClass)element, writer);
	    }
	    else if(element instanceof MInterface) {
		generateInterface((MInterface)element, writer);
	    }
	}

	writer.write("}\n\n");
    }

    /**
       Generate code for an interface.

       @param mInterface The interface to generate code for.
       @param writer The writer to write to.
    */
    public void generateInterface(MInterface mInterface,
                                  Writer writer)
	throws Exception
    {
	InterfaceCodePiece icp =
	    new InterfaceCodePiece(null, mInterface.getName());
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mInterface.getNamespace()));
	icp.write(writer, parseStateStack, 0);

	writer.write("\n{\n");

	// Features
	Collection features = mInterface.getFeatures();
	for(Iterator i = features.iterator(); i.hasNext(); ) {
	    MFeature feature = (MFeature)i.next();
	    if(feature instanceof MOperation) {
		generateOperation((MOperation)feature, mInterface, writer);
	    }
	    if(feature instanceof MAttribute) {
		generateAttribute((MAttribute)feature, mInterface, writer);
	    }
	}

	// Inner classes
	Collection elements = mInterface.getOwnedElements();
	for(Iterator i = elements.iterator(); i.hasNext(); ) {
	    MModelElement element = (MModelElement)i.next();
	    if(element instanceof MClass) {
		generateClass((MClass)element, writer);
	    }
	    else if(element instanceof MInterface) {
		generateInterface((MInterface)element, writer);
	    }
	}

	writer.write("}\n\n");
    }

    /**
       Generate code for an operation.

       @param mOperation The operation to generate code for.
       @param mClassifier The classifier the operation belongs to.
       @param writer The writer to write to.
    */
    public void generateOperation(MOperation mOperation,
                                  MClassifier mClassifier,
                                  Writer writer)
	throws Exception
    {
	OperationCodePiece ocp =
	    new OperationCodePiece(new SimpleCodePiece("", 0, 0, 0),
				   new SimpleCodePiece("", 0, 0, 0),
				   mOperation.getName());
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mClassifier));
	ocp.write(writer, parseStateStack, 0);

	if(mOperation.isAbstract() || mClassifier instanceof MInterface) {
	    writer.write(";\n\n");
	}
	else {
	    writer.write("\n{\n}\n\n");
	}
    }

    /**
       Generate code for an attribute.

       @param mAttribute The attribute to generate code for.
       @param mClassifier The classifier the attribute belongs to.
       @param writer The writer to write to.
    */
    public void generateAttribute(MAttribute mAttribute,
                                  MClassifier mClassifier,
                                  Writer writer)
	throws Exception
    {
	Vector names = new Vector();
	names.addElement(new SimpleCodePiece(mAttribute.getName(), 0, 0, 0));
	AttributeCodePiece acp =
	    new AttributeCodePiece(null,
				   new SimpleCodePiece("", 0, 0, 0),
				   names);
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mClassifier));
	acp.write(writer, parseStateStack, 0);
	writer.write(";\n\n");
    }

    /* 
     * Changed 2001-10-05 STEFFEN ZSCHALER
     *
     * Was:
     *
    /**
       Generate the javadoc comment found in the tagged value
       "javadocs".

       @param element The element to generate the doc for.
       @param writer The writer to write to.
       @returns True if comment exists, false otherwise.
     *
     */
    /**
     * Generate the javadoc comment associated with the specified model element
     * as defined by org.argouml.uml.DocumentationManager.
     *
     * @param element the element to generate the doc for
     * @param writer the writer to write to
     * @returns true if comment exists, false otherwise
     */
    public boolean generateJavadoc(MModelElement element,
                                   Writer writer)
	throws Exception
    {
      /*
       * Changed 2001-10-05 STEFFEN ZSCHALER
       *
       * Was:
       *
	for(Iterator i = element.getTaggedValues().iterator(); i.hasNext(); ) {
	    MTaggedValue tv = (MTaggedValue)i.next();
	    if(tv.getTag().equals("javadocs")) {
		writer.write(tv.getValue());
		writer.write("\n");
		return true;
	    }
	}
	return false;
    }
       *
       */

      String sDocComment = null;
      if (DocumentationManager.hasDocs (element)) {
        sDocComment = DocumentationManager.getDocs (element);
      }

      /*
      if (sDocComment != null) {
        // Fix Bug in documentation manager.defaultFor --> look for current INDENT
        // and use it
        for (int i = sDocComment.indexOf ('\n');
             i >= 0 && i < sDocComment.length();
             i = sDocComment.indexOf ('\n', i + 1)) {
          sDocComment = sDocComment.substring (0, i + 1) + 
                        INDENT + sDocComment.substring (i + 1);
        }
      }*/

      // Extract constraints
      Collection cConstraints = element.getConstraints();

      if (cConstraints.size() != 0) {
        // Prepare doccomment
        if (sDocComment != null) {
          // Just remove closing */
          sDocComment = sDocComment.substring (0, sDocComment.indexOf ("*/") + 1);
        }
        else {
          sDocComment = "/**\n" +
                        " * \n" +
                        " *";
        }
    
        // Add each constraint

        class TagExtractor extends tudresden.ocl.parser.analysis.DepthFirstAdapter {
          private LinkedList m_llsTags = new LinkedList();
          private String m_sConstraintName;
          private int m_nConstraintID = 0;

          public TagExtractor (String sConstraintName) {
            super();

            m_sConstraintName = sConstraintName;
          }

          public Iterator getTags() {
            return m_llsTags.iterator();
          }

          public void caseAConstraintBody (tudresden.ocl.parser.node.AConstraintBody node) {
            // We don't care for anything below this node, so we do not use apply anymore.
            String sKind = (node.getStereotype() != null)?
                           (node.getStereotype().toString()):
                           (null);
            String sExpression = (node.getExpression() != null)?
                                 (node.getExpression().toString()):
                                 (null);
            String sName = (node.getName() != null)?
                           (node.getName().getText()):
                           (m_sConstraintName + "_" + (m_nConstraintID++));

            if ((sKind == null) ||
                (sExpression == null)) {
              return;
            }

            String sTag;
            if (sKind.equals ("inv ")) {
              sTag = "@invariant ";
            }
            else if (sKind.equals ("post ")) {
              sTag = "@post-condition ";
            }
            else if (sKind.equals ("pre ")) {
              sTag = "@pre-condition ";
            }
            else {
              return;
            }

            sTag += sName + ": " + sExpression;
            m_llsTags.addLast (sTag);
          }
        }

        tudresden.ocl.check.types.ModelFacade mf =
            new org.argouml.ocl.ArgoFacade (element);
        
        for (Iterator i = cConstraints.iterator(); i.hasNext();) {
          MConstraint mc = (MConstraint) i.next();

          try {
            tudresden.ocl.OclTree otParsed = tudresden.ocl.OclTree.createTree (
                mc.getBody().getBody(),
                mf
              );

            TagExtractor te = new TagExtractor (mc.getName());
            otParsed.apply (te);

            for (Iterator j = te.getTags(); j.hasNext();) {
              sDocComment += " " + j.next() + "\n" +
                             " *";
            }
          }
          catch (java.io.IOException ioe) {
            // Nothing to be done, should not happen anyway ;-)
          }
        }
    
        sDocComment += "/";
      }
    

      if (sDocComment != null) {
        writer.write (sDocComment);
        writer.write ("\n");
        return true;
      }
      else {
        return false;
      }
    }

    /**
       Update a source code file.

       @param mClassifier The classifier to update from.
       @param file The file to update.
    */
    private void update(MClassifier mClassifier,
                        File file)
	throws Exception
    {
	System.out.println("Parsing " + file.getPath());

	JavaLexer lexer =
	    new JavaLexer(new FileInputStream(file));
	JavaRecognizer parser = new JavaRecognizer(lexer);
	CodePieceCollector cpc = new CodePieceCollector();
	parser.compilationUnit(cpc);

	File origFile = new File(file.getAbsolutePath());
	File newFile = new File(file.getAbsolutePath()+".updated");
	cpc.filter(file, newFile, mClassifier.getNamespace());
	file.renameTo(new File(file.getAbsolutePath()+".backup"));
	newFile.renameTo(origFile);
    }
}
