
// $Id$
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

// $Id$

/*
  JavaRE - Code generation and reverse engineering for UML and Java
  Author: Marcus Andersson andersson@users.sourceforge.net
*/


package org.argouml.language.java.generator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.Stack;
import java.util.Vector;
import java.util.Iterator;

import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MAttribute;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MAssociation;


/**
   This code piece represents an attribute. Even though the code can
   handle several attributes in the same statement, the code generated
   will be separate statements and initialization code for all but the
   last will be removed.
*/
public class AttributeCodePiece extends NamedCodePiece
{
    /** The code piece this attribute represents. */
    private CompositeCodePiece attributeDef;

    /** The names of declared attributes. */
    private Vector attributeNames;

    /** Indicating that the type name is fully qualified in the
        original source code. */
    private boolean typeFullyQualified;

    /**
       Constructor.

       @param modifiers The code piece for modifiers.
       @param type The code piece for the type.
       @param names Vector with attribute names.
    */
    public AttributeCodePiece(CodePiece modifiers,
                              CodePiece type,
                              Vector names)
    {
	attributeNames = new Vector();
	attributeDef = new CompositeCodePiece(modifiers);
	attributeDef.add(type);
	for (Iterator i = names.iterator(); i.hasNext(); ) {
	    CodePiece cp = (CodePiece) i.next();
	    String cpText = cp.getText().toString().trim();
            if (cpText.indexOf('\n') > 0) 
                cpText = cpText.substring(0, cpText.indexOf('\n')).trim();
	    attributeDef.add(cp);
	    int pos = 0;
	    if ((pos = cpText.indexOf('[')) != -1) {
		attributeNames.add(cpText.substring(0, pos));
	    }
	    else {
		attributeNames.add(cpText);
	    }
	}
	typeFullyQualified = (type.getText().toString().indexOf('.') != -1);
    }

    /**
       Return the string representation for this piece of code.
    */
    public StringBuffer getText()
    {
	return attributeDef.getText();
    }

    /**
       Return the start position.
    */
    public int getStartPosition()
    {
	return attributeDef.getStartPosition();
    }

    /**
       Return the end position.
    */
    public int getEndPosition()
    {
	return attributeDef.getEndPosition();
    }

    /**
       Return the start line
    */
    public int getStartLine()
    {
	return attributeDef.getStartLine();
    }

    /**
       Return the end line
    */
    public int getEndLine()
    {
	return attributeDef.getEndLine();
    }

    /**
       Write the code this piece represents to file.
       (Does not check for uniqueness of names.)
    */
    public void write(BufferedReader reader,
                      BufferedWriter writer,
                      Stack parseStateStack)
	throws Exception
    {
	ParseState parseState = (ParseState) parseStateStack.peek();
	Vector features = parseState.getNewFeatures();
	int k = 1, count = attributeNames.size();
	boolean found = false;
	// there might be multiple variable declarations in one line, so loop:
	for (Iterator i = attributeNames.iterator(); i.hasNext(); k++) {
	    boolean checkAssociations = true;
	    String name = (String) i.next();
	    Iterator j;
	    // now find the matching feature
	    for (j = features.iterator(); j.hasNext();) {
		MFeature mFeature = (MFeature) j.next();
		if (org.argouml.model.ModelFacade.isAAttribute(mFeature)
		    && mFeature.getName().equals(name)) {
		    // feature found, so it's an attribute (and no
		    // association end)
		    found = true;
		    checkAssociations = false;
		    // deletes feature from current ParseState
		    parseState.newFeature(mFeature);

		    MAttribute attr = (MAttribute) mFeature;
		    writer.write(generator().generateCoreAttribute(attr));

		    if ( k < count ) {
			writer.write("; "); // fixed comma separated attributes
		    }
		    break;
		}
	    }
	    if (checkAssociations) {
		// feature not found: we need to check associations,
		// because the parser can't distinguish between attributes
		// and associations represented as class variables:
		Vector ends = parseState.getAssociationEnds();
		if (!ends.isEmpty()) {
		    // now find the first matching association end
		    for (j = ends.iterator(); j.hasNext(); ) {
			MAssociationEnd ae = (MAssociationEnd) j.next();
			MAssociation a = ae.getAssociation();
			Iterator connEnum = a.getConnections().iterator();
			while (connEnum.hasNext()) {
			    MAssociationEnd ae2 =
				(MAssociationEnd) connEnum.next();
			    if (ae2 != ae
				&& ae2.isNavigable()
				&& !ae2.getAssociation().isAbstract()
				&& generator()
				   .generateAscEndName(ae2).equals(name))
			    {
				// association end found
				found = true;
				writer.write(generator()
					     .generateCoreAssociationEnd(ae2));
				break;
			    }
			}
		    }
		}
	    }
	}
	if (found) {
	    // fast forward original code (overwriting)
	    ffCodePiece(reader, null);
	} else {
	    // not in model, so write the original code
	    ffCodePiece(reader, writer);
	}
    }

    /**
     * Get the generator.
     *
     * @returns the generator.
     */
    private GeneratorJava generator() {
	return GeneratorJava.getInstance();
    }
}