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
 * This helper class generates CodePiece based code.
 * It needs some work. See issue
 * http://argouml.tigris.org/issues/show_bug.cgi?id=435
 */
class CodeGenerator
{
    /**
       Generate code for a class.

       @param mClass The class to generate code for.
       @param writer The writer to write to.
    */
    static public void generateClass(MClass mClass, Writer writer)
	throws Exception
    {
	ClassCodePiece ccp = new ClassCodePiece(null, mClass.getName());
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mClass.getNamespace()));
	ccp.write(writer, parseStateStack, 0);

	writer.write("{\n");

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

	writer.write("}\n");
    }

    /**
       Generate code for an interface.

       @param mInterface The interface to generate code for.
       @param writer The writer to write to.
    */
    static public void generateInterface(MInterface mInterface, Writer writer)
	throws Exception
    {
	InterfaceCodePiece icp =
	    new InterfaceCodePiece(null, mInterface.getName());
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mInterface.getNamespace()));
	icp.write(writer, parseStateStack, 0);

	writer.write("{\n");

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

	writer.write("}\n");
    }

    /**
       Generate code for an operation.

       @param mOperation The operation to generate code for.
       @param mClassifier The classifier the operation belongs to.
       @param writer The writer to write to.
    */
    static public void generateOperation(MOperation mOperation, MClassifier mClassifier, Writer writer)
	throws Exception
    {
	OperationCodePiece ocp =
	    new OperationCodePiece(new SimpleCodePiece(new StringBuffer(), 0, 0, 0),
				   new SimpleCodePiece(new StringBuffer(), 0, 0, 0),
				   mOperation.getName());
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mClassifier));
	ocp.write(writer, parseStateStack, 0);

	if(mOperation.isAbstract() || mClassifier instanceof MInterface) {
	    writer.write(";\n");
	}
	else {
	    writer.write("{}\n");
	}
    }

    /**
       Generate code for an attribute.

       @param mAttribute The attribute to generate code for.
       @param mClassifier The classifier the attribute belongs to.
       @param writer The writer to write to.
    */
    static public void generateAttribute(MAttribute mAttribute, MClassifier mClassifier, Writer writer)
	throws Exception
    {
	Vector names = new Vector();
	names.addElement(new SimpleCodePiece(new StringBuffer(mAttribute.getName()), 0, 0, 0));
	AttributeCodePiece acp =
	    new AttributeCodePiece(null,
				   new SimpleCodePiece(new StringBuffer(), 0, 0, 0),
				   names);
	Stack parseStateStack = new Stack();
	parseStateStack.push(new ParseState(mClassifier));
	acp.write(writer, parseStateStack, 0);
	writer.write(";\n");
    }
}
