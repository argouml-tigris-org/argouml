// Copyright (c) 1996-99 The Regents of the University of California. All
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


package org.argouml.uml;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;

import java.util.*;

/**
 *   This abstract class captures the configurable behavior of Argo.
 *
 *   @author Curt Arnold
 */
abstract public class Profile {
    /**
     *    This method produces a string that represents the specific model element
     *    in the context of the specified namespace.
     *    @param element element to represent.
     *    @param namespace context namespace (may be null).
     *    @return a string representing the model element
     */
    abstract public String formatElement(MModelElement element,MNamespace namespace);
    /**
     *   This method produces a string the represents the collection of model elements
     *   in the context of the specified namespace.
     *   @param iter iterator over collection
     *   @param namespace context namespace (may be null).
     *   @return a string representing the collection
     */
    abstract public String formatCollection(Iterator iter,MNamespace namespace);
    /**
     *   This method adds common types (int, boolean, etc) to a set of classifiers.
     *   This allows the use of built-in types before an explicit model element
     *   has been added. 
     *
     *   @param model, classifiers already present in model will not be added.
     *   @param classifierType use MClassifier.class for all built-in classifiers, 
     *            MDataType.class for just built-in primitive types, etc.
     *   @param classifierSet  set of classifiers
     */
    abstract public void addBuiltinClassifiers(MModel model,Class classifierType,Set classifierSet,boolean addVoid);
    
    /**
     *   This method gets the default classifier name for a profile.  For example,
     *     Object for Java, IUnknown for COM, or int for C++.
     */
    abstract public ProfileClassifier getDefaultClassifier();
    
    /**
     *   This method gets the "void" classifier for a profile.
     *
     */
    abstract public ProfileClassifier getVoidClassifier();
    
    /**
     *   This method produces a vector of appropriate initial values for the
     *   specified type.  For example, if type.getName() == 'boolean', the vector
     *   might contain "true" and "false".
     *   @param type type under consideration
     *   @return vector containing common default values.
     */
    abstract public Vector getInitialValues(MClassifier type);
    
    /**
     *   This method is used to filter out well-known stereotypes
     *   from being applied to inappropriate model elements.
     *   @param targetClass class of model element, for example, MAttribute.class.
     *   @param stereotype  stereotype
     *   @return false if stereotype is inappropriate for model element class.
     */
    abstract public boolean isAppropriateStereotype(Class targetClass,MStereotype stereotype);
    
    /**
     *   This method is used to populate stereotype combo boxes with
     *   well-known stereotypes that have not been explicitly added to the model.
     *   @param targetClass class of model element, for example, MAttribute.class.
     *   @param stereotypes set of stereotypes to display.
     */
    abstract public void addWellKnownStereotypes(Class targetClass,Set stereotypes); 
    
    /**
     *   This method creates the corresponding stereotype when a
     *   stereotype entry created by addWellKnownStereotypes was selected.
     *   @param model model
     *   @param name name of stereotype
     *   @return corresponding stereotype
     */
    abstract public MStereotype constructWellKnownStereotype(MModel model,String name);
    
}