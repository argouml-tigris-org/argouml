// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.argouml.model.Model;
/**
   This class handles information during the replacement of code
   pieces. One parse state for each classifier handled.
*/
public class ParseState {
    /**
     * The current namespace.
     */
    private Object namespace;

    /**
     * The inner classes not found yet.
     */
    private List newInnerClasses;

    /**
     * The features not found yet.
     */
    private List newFeatures;

    /**
     * The current classifier.
     */
    private Object mClassifier;

    /**
     * Create a new parse state.
     *
     * @param handle is the namespace the classifier belongs to.
     */
    public ParseState(Object handle) {
        if (Model.getFacade().isAClassifier(handle)) {
            this.mClassifier = handle;
            namespace = handle;
            newFeatures =
                new ArrayList(Model.getFacade().getFeatures(mClassifier));
            newInnerClasses =
                new ArrayList(Model.getFacade().getOwnedElements(mClassifier));
        } else {
            this.mClassifier = null;
            namespace = handle;
            newFeatures = new ArrayList();
            newInnerClasses = new ArrayList();
        }
    }

    /**
       Tell the parse state that an inner classifier is found.

       @param name The name of the classifier.
       @return The new classifier.
     */
    public Object newClassifier(String name) {
	Object mc = Model.getFacade().lookupIn(namespace, name);
	if (mc != null) {
	    newInnerClasses.remove(mc);
	}
	return mc;
    }

    /**
       Tell the parse state that a feature is found in the current
       classifier.

       @param mFeature The feature found.
    */
    public void newFeature(Object mFeature) {
	newFeatures.remove(mFeature);
    }

    /**
     * Get the current classifier.
     *
     * @return the current classifier
     */
    public Object getClassifier() {
	return mClassifier;
    }

    /**
     * Get all features not in the source.
     * 
     * @return all features not in the source
     * @deprecated for 0.25.4 by tfmorris. Use {@link #getNewFeaturesList()}.
     */
    @Deprecated
    public Vector getNewFeatures() {
	return new Vector(newFeatures);
    }

    /**
     * Get all features not in the source.
     *
     * @return all features not in the source
     */
    List getNewFeaturesList() {
        return newFeatures;
    }
    
    /**
     * Get all inner classes not in the source.
     * 
     * @return all inner classes not in the source
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #getNewInnerClassesList()}.
     */
    @Deprecated
    public Vector getNewInnerClasses() {
	return new Vector(newInnerClasses);
    }

    /**
     * Get all inner classes not in the source.
     *
     * @return all inner classes not in the source
     */
    List getNewInnerClassesList() {
        return newInnerClasses;
    }
    
    /**
     * Get the current namespace.
     *
     * @return the current namespace
     */
    public Object getNamespace() {
	return namespace;
    }

    /**
     * Get the association ends.
     *
     * @return the association ends
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #getAssociationEndsList()}.
     */
    @Deprecated
    public Vector getAssociationEnds() {
        Vector result = new Vector();
        if (mClassifier == null) {
            return result;
        }
        result.addAll(Model.getFacade().getAssociationEnds(mClassifier));
        return result;
    }
    
    /**
     * Get the association ends.
     *
     * @return the association ends
     */
    List getAssociationEndsList() {
        if (mClassifier == null) {
            return Collections.EMPTY_LIST;
        }
        List result = new ArrayList();
        result.addAll(Model.getFacade().getAssociationEnds(mClassifier));
        return result;
    }
}
