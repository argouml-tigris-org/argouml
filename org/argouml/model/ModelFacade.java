// $Id$
// Copyright (c) 2003 The Regents of the University of California. All
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

package org.argouml.model;

import java.util.*;

// NS-UML imports:
import ru.novosoft.uml.foundation.core.*;

// GEF imports:
import org.tigris.gef.base.*;
// import org.tigris.gef.presentation.*;
// import org.tigris.gef.util.*;

// Diagram model imports:
import org.argouml.model.uml.foundation.core.*;
// import org.argouml.uml.diagram.ui.*;
// import org.argouml.uml.diagram.deployment.ui.*;
// import org.argouml.uml.diagram.static_structure.ui.*;


/**
 * Facade object for the Model component in ArgoUML.
 *
 * The purpose of this Facade object is to allow for decoupling other modules
 * from the insides of the model. For this purpose all of the methods in this
 * class give away and accept handles (of type java.lang.Object) to the 
 * objects within the model.
 *
 * All methods in this facade are static.
 * 
 * <p>Signature for all recognizers in this Facade:
 * public static boolean isA<TYPE>(Object handle)
 *
 * <p>Signature for all getters in this Facade:
 * public static Object get<TYPE>(Object handle) - 1..1
 * public static Iterator get<TYPES>(Object handle) - 0..*
 * public static String getName(Object handle) - Name
 *  
 * @stereotype utility
 * @author Linus Tolke
 */
public class ModelFacade {
    /** Constructor that forbids instantiation.
     */
    private ModelFacade() {
    }

    // Recognizer methods for the UML model (in alphabetic order)
    /** Recognizer for Classifier
     *
     * @param handle candidate
     * @returns true if handle is a Classifier
     */
    public static boolean isAClassifier(Object handle) {
	return handle instanceof MClassifier;
    }

    // Recognizer methods for the diagrams (in alphabetic order)
    /** Recognizer for Diagram.
     *
     * @param handle candidate
     * @returns true if handle is a diagram.
     */
    public static boolean isADiagram(Object handle) { 
	return handle instanceof Diagram;
    }

    // Getters for the UML model (in alphabetic order)
    /** The list of Attributes.
     *
     * Only Classifiers have attributes so if this is called with something
     * else than a Classifier, an empty iterator is returned.
     *
     * @param handle classifier to examine.
     * @return iterator with structural features.
     */
    public static Iterator getAttributes(Object handle) {
	if (!(handle instanceof MClassifier)) return emptyIterator();

	MClassifier c = (MClassifier) handle;

	return CoreHelper.getHelper().getAttributes(c).iterator();
    }

    // Common getters
    /** The name of a model element or some diagram part.
     *
     * @param handle that points out the object.
     * @returns the name
     */
    public static String getName(Object handle) {
	if (handle instanceof MModelElement) {
	    MModelElement me = (MModelElement) handle;

	    return me.getName();
	}

	if (handle instanceof Diagram) {
	    Diagram d = (Diagram) handle;

	    return d.getName();
	}

	// ...

	return "<Unknown Name>";
    }



    // Convenience methods
    /** The empty set.
     *
     * @returns an empty iterator.
     */
    private static Iterator emptyIterator() {
	return Collections.EMPTY_SET.iterator();
    }
}
