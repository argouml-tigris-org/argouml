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
import java.util.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

/**
   This code piece represents an operation declaration.
*/
public class OperationCodePiece extends NamedCodePiece
{
    /** The code piece this operation represents. */
    private CodePiece operationDef;

    /** The name of the operation. */
    private String name;

    /** Indicates that the typenames are fully qualified in the
        source. */
    private boolean fullyQualifiedTypeNames;

    /**
       Constructor.

       @param javadocDef The code piece for the javadoc.
       @param operationDef The code piece this operation represents.
       @param name The name of the operation.
    */
    public OperationCodePiece(CodePiece javadocDef,
                              CodePiece operationDef,
                              String name)
    {
	this.name = name;
	fullyQualifiedTypeNames = (operationDef.getText().toString().indexOf('.') != -1);
	if(javadocDef != null) {
	    CompositeCodePiece cp = new CompositeCodePiece(javadocDef);
	    cp.add(operationDef);
	    this.operationDef = cp;
	}
	else {
	    this.operationDef = operationDef;
	}
    }

    /**
       Return the string representation for this piece of code.
    */
    public StringBuffer getText()
    {
	return operationDef.getText();
    }

    /**
       Return the start position.
    */
    public int getStartPosition()
    {
	return operationDef.getStartPosition();
    }

    /**
       Return the end position.
    */
    public int getEndPosition()
    {
	return operationDef.getEndPosition();
    }

    /**
	Return the start line
    */
    public int getStartLine()
    {
	return operationDef.getStartLine();
    }

    /**
	Return the end line
    */
    public int getEndLine()
    {
	return operationDef.getEndLine();
    }

    /**
       Write the code this piece represents to file. Remove this
       feature from the top vector in the stack newFeaturesStack.
    */
    public void write (Writer writer,
                       Stack parseStateStack,
                       int column)
  	    throws Exception {
      ParseState parseState = (ParseState)parseStateStack.peek();
      Vector features = parseState.getNewFeatures();

      boolean found = false;

	    for(Iterator j=features.iterator(); j.hasNext() && !found; ) {
  	    MFeature feature = (MFeature)j.next();

	      if(feature.getName().equals(name)) {
		      found = true;
		      parseState.newFeature(feature);
		      MOperation mOperation = (MOperation)feature;

          writer.write (
              GeneratorJava.getInstance()
                           .generateOperation (mOperation, true)
            );

          /*
          if (GeneratorJava.generateConstraintEnrichedDocComment(mOperation, writer)) {
            for(int k=0; k<column; k++) {
              writer.write(" ");
            }
          }

          if (mOperation.isAbstract()) {
            writer.write("abstract ");
            column += 9;
          }
          if (mOperation.isLeaf()) {
            writer.write("final ");
            column += 6;
          }
          if (mOperation.getOwnerScope() == MScopeKind.CLASSIFIER) {
            writer.write("static ");
            column += 7;
          }
          if (mOperation.getVisibility() == MVisibilityKind.PUBLIC) {
            writer.write("public ");
            column += 7;
          }
          else if (mOperation.getVisibility() == MVisibilityKind.PROTECTED) {
            writer.write("protected ");
            column += 10;
          }
          else if (mOperation.getVisibility() == MVisibilityKind.PRIVATE) {
            writer.write("private ");
            column += 8;
          }

          Collection parameters = mOperation.getParameters();
          for(Iterator i = parameters.iterator(); i.hasNext(); ) {
            MParameter mParameter = (MParameter)i.next();

            if (mParameter.getKind() == MParameterDirectionKind.RETURN) {
              if (fullyQualifiedTypeNames) {
                writer.write (mParameter.getType().getNamespace().getName()
                              + ".");
              }

              writer.write(mParameter.getType().getName() + " ");
              column += mParameter.getType().getName().length() + 1;
            }
          }

          writer.write(mOperation.getName() + "(");
          column += mOperation.getName().length() + 1;

          boolean first = true;
          for (Iterator i = parameters.iterator(); i.hasNext(); ) {
            MParameter mParameter = (MParameter)i.next();

            if (mParameter.getKind() != MParameterDirectionKind.RETURN) {
              if(first) {
                first = false;
              }
              else {
                writer.write(",\n");
                for(int k=0; k<column; k++) {
                  writer.write(" ");
                }
              }

              if (fullyQualifiedTypeNames) {
                writer.write (mParameter.getType().getNamespace().getName()
                              + ".");
              }

              writer.write (mParameter.getType().getName() + " " +
                            mParameter.getName());
            }
          }

          writer.write(")");
          */
        }
      }

      if(!found) {
        writer.write("REMOVED");
      }
    }
}