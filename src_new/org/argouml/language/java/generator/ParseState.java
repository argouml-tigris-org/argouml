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

/*
  JavaRE - Code generation and reverse engineering for UML and Java
  Author: Marcus Andersson andersson@users.sourceforge.net
*/


package org.argouml.language.java.generator;

import ru.novosoft.uml.foundation.core.*;
import java.util.*;

/**
   This class handles information during the replacement of code
   pieces. One parse state for each classifier handled.
*/
public class ParseState
{
    /** The current namespace. */
    private MNamespace namespace;

    /** The inner classes not found yet */
    private Vector newInnerClasses;

    /** The features not found yet */
    private Vector newFeatures;

    /** The current classifier */
    private MClassifier mClassifier;

    /**
       Create a new parse state.

       @param The namespace the classifier belongs to.
     */
    public ParseState(MNamespace mNamespace)
    {
	this.mClassifier = null;
	namespace = mNamespace;
	newFeatures = new Vector();
	newInnerClasses = new Vector();
    }

    /**
       Create a new parse state.

       @param mClassifier Current classifier.
     */
    public ParseState(MClassifier mClassifier)
    {
	this.mClassifier = mClassifier;
	namespace = mClassifier;
	newFeatures = new Vector(mClassifier.getFeatures());
	newInnerClasses = new Vector(mClassifier.getOwnedElements());
    }

    /**
       Tell the parse state that an inner classifier is found.

       @param name The name of the classifier.
       @return The new classifier.
     */
    public MClassifier newClassifier(String name)
    {
	MClassifier mClassifier = (MClassifier) namespace.lookup(name);
	if (mClassifier != null) {
	    newInnerClasses.remove(mClassifier);
	}
	return mClassifier;
    }

    /**
       Tell the parse state that a feature is found in the current
       classifier.

       @param mFeature The feature found.
    */
    public void newFeature(MFeature mFeature)
    {
	newFeatures.remove(mFeature);
    }

    /**
       Get the current classifier.
     */
    public MClassifier getClassifier()
    {
	return mClassifier;
    }

    /**
       Get all features not in the source.
     */
    public Vector getNewFeatures()
    {
	return new Vector(newFeatures);
    }

    /**
       Get all inner classes not in the source.
     */
    public Vector getNewInnerClasses()
    {
	return new Vector(newInnerClasses);
    }

    /**
       Get the current namespace.
     */
    public MNamespace getNamespace()
    {
	return namespace;
    }

    /**
       Get the association ends.
     */
    public Vector getAssociationEnds()
    {
        Vector result = new Vector();
        if (mClassifier == null)
            return result;
        result.addAll(mClassifier.getAssociationEnds());
        return result;
    }
}
