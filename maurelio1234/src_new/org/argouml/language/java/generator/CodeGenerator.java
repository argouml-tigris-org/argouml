// $Id: CodeGenerator.java 11296 2006-10-13 11:19:34Z thn $
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

package org.argouml.language.java.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

import org.argouml.model.Model;

/**
 * This helper class generates CodePiece based code.
 * It needs some work. See issue
 * http://argouml.tigris.org/issues/show_bug.cgi?id=435
 *
 * JavaRE - Code generation and reverse engineering for UML and Java.
 *
 * @author Marcus Andersson andersson@users.sourceforge.net
 */
class CodeGenerator {
    /**
     * Generate code for a class.
     *
     * @param mClass The class to generate code for.
     * @param reader The reader to read from.
     * @param writer The writer to write to.
     * @throws IOException if there is a problem with reading/writing.
     */
    public static void generateClass(Object/*MClass*/ mClass,
				     BufferedReader reader,
				     BufferedWriter writer)
	throws IOException {

	ClassCodePiece ccp =
	    new ClassCodePiece(null, Model.getFacade().getName(mClass));
	Stack parseStateStack = new Stack();
	parseStateStack.push(
	        new ParseState(Model.getFacade().getNamespace(mClass)));
	ccp.write(reader, writer, parseStateStack);

	writer.write("{\n");

	// Features
	Collection features = Model.getFacade().getFeatures(mClass);
	for (Iterator i = features.iterator(); i.hasNext();) {
	    Object feature = /*(MFeature)*/ i.next();
	    if (Model.getFacade().isAOperation(feature)) {
		generateOperation(/*(MOperation)*/ feature, mClass,
				  reader, writer);
	    }
	    if (Model.getFacade().isAAttribute(feature)) {
		generateAttribute(/*(MAttribute)*/ feature, mClass,
				  reader, writer);
	    }
	}

	// Inner classes
	Collection elements = Model.getFacade().getOwnedElements(mClass);
	for (Iterator i = elements.iterator(); i.hasNext();) {
	    Object element = /*(MModelElement)*/ i.next();
	    if (Model.getFacade().isAClass(element)) {
		generateClass(element, reader, writer);
	    } else if (Model.getFacade().isAInterface(element)) {
		generateInterface(element, reader, writer);
	    }
	}

	writer.write("}\n");
    }

    /**
     * Generate code for an interface.
     *
     * @param mInterface The interface to generate code for.
     * @param reader The reader to read from.
     * @param writer The writer to write to.
     * @throws IOException if there is a problem with reading/writing.
     */
    public static void generateInterface(Object mInterface,
					 BufferedReader reader,
					 BufferedWriter writer)
	throws IOException {
	InterfaceCodePiece icp =
	    new InterfaceCodePiece(null, Model.getFacade().getName(mInterface));
	Stack parseStateStack = new Stack();
	parseStateStack.push(
	        new ParseState(Model.getFacade().getNamespace(mInterface)));
	icp.write(reader, writer, parseStateStack);

	writer.write("{\n");

	// Features
	Collection features = Model.getFacade().getFeatures(mInterface);
	for (Iterator i = features.iterator(); i.hasNext();) {
	    Object feature = /*(MFeature)*/ i.next();
	    if (Model.getFacade().isAOperation(feature)) {
		generateOperation(/*(MOperation)*/ feature,
				  mInterface, reader, writer);
	    }
	    if (Model.getFacade().isAAttribute(feature)) {
		generateAttribute(/*(MAttribute)*/ feature,
				  mInterface, reader, writer);
	    }
	}

	// Inner classes
	Collection elements = Model.getFacade().getOwnedElements(mInterface);
	for (Iterator i = elements.iterator(); i.hasNext();) {
	    Object element = /*(MModelElement)*/ i.next();
	    if (Model.getFacade().isAClass(element)) {
		generateClass(element, reader, writer);
	    } else if (Model.getFacade().isAInterface(element)) {
		generateInterface(element, reader, writer);
	    }
	}

	writer.write("}\n");
    }

    /**
     * Generate code for an operation.
     *
     * @param mOperation The operation to generate code for.
     * @param mClassifier The classifier the operation belongs to.
     * @param reader The reader to read from.
     * @param writer The writer to write to.
     * @throws IOException if there is a problem with reading/writing.
     */
    public static void generateOperation(Object mOperation,
					 Object mClassifier,
					 BufferedReader reader,
					 BufferedWriter writer)
	throws IOException {

	OperationCodePiece ocp =
	    new OperationCodePiece(new SimpleCodePiece(new StringBuffer(),
						       0, 0, 0),
				   new SimpleCodePiece(new StringBuffer(),
						       0, 0, 0),
				   Model.getFacade().getName(mOperation));
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mClassifier));
	ocp.write(reader, writer, parseStateStack);

	if (Model.getFacade().isAbstract(mOperation)
	    || Model.getFacade().isAInterface(mClassifier)) {

	    writer.write(";\n");
	} else {
	    writer.write("{}\n");
	}
    }

    /**
     * Generate code for an attribute.
     *
     * @param mAttribute The attribute to generate code for.
     * @param mClassifier The classifier the attribute belongs to.
     * @param reader The reader to read from.
     * @param writer The writer to write to.
     * @throws IOException if there is a problem with reading/writing.
     */
    public static void generateAttribute(Object mAttribute,
					 Object mClassifier,
					 BufferedReader reader,
					 BufferedWriter writer)
	throws IOException {

	Vector names = new Vector();
	StringBuffer sbName =
	    new StringBuffer(Model.getFacade().getName(mAttribute));
	names.addElement(new SimpleCodePiece(sbName, 0, 0, 0));
	AttributeCodePiece acp =
	    new AttributeCodePiece(null,
				   new SimpleCodePiece(new StringBuffer(),
						       0, 0, 0),
				   names);
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mClassifier));
	acp.write(reader, writer, parseStateStack);
	writer.write(";\n");
    }
}
