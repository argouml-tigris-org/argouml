// $Id$
// Copyright (c) 2005-2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.util.Collection;
import java.util.Vector;


/**
 * The interface for the helper for ModelManagement.<p>
 *
 * Created from the old ModelManagementHelper.
 */
public interface ModelManagementHelper {
    
    public String FRIEND_STEREOTYPE = "friend";
    public String IMPORT_STEREOTYPE = "import";
    public String ACCESS_STEREOTYPE = "access";
    
    /**
     * Returns all subsystems found in this namespace and in its children.
     *
     * @param ns is the namespace
     * @return Collection
     */
    Collection getAllSubSystems(Object ns);

    /**
     * Returns all namespaces found in this namespace and in its children
     *
     * @param ns namespace to process
     * @return Collection of all namespaces found
     */
    Collection getAllNamespaces(Object ns);

    /**
     * Returns all modelelements of the given kind.
     *
     * @param model the model that is searched
     * @param type is the class kind that is sought
     *
     * @return Collection
     */
    Collection getAllModelElementsOfKindWithModel(Object model, Object type);

    /**
     * Returns all modelelements found in this namespace and its children
     * that are of some class kind.<p>
     *
     * @param nsa is the namespace
     * @param type is the class kind
     * @return Collection
     */
    Collection getAllModelElementsOfKind(Object nsa, Object type);

    /**
     * helper method for {@link #getAllModelElementsOfKind(Object, Object)}.
     *
     * @param nsa
     *            namespace.
     * @param kind
     *            name of class to find (without implementation-specific
     *            additions)
     * @return a Collection.
     */
    Collection getAllModelElementsOfKind(Object nsa, String kind);

    /**
     * Returns all surrounding namespaces of some namespace ns. See section
     * 2.5.3.26 of the UML 1.4 spec for a definition.
     * 
     * @param ns to process
     * @return Collection of surrounding namespaces.
     */
    Collection getAllSurroundingNamespaces(Object ns);

    /**
     * @param ns the given namespace
     * @return a collection of all behavioralfeatures in the given namespace
     */
    Collection getAllBehavioralFeatures(Object ns);
    
    /**
     * Returns a collection of ModelElements 
     * that may possibly be imported in the given package.
     * 
     * @param pack the given package
     * @return a collection with modelelements 
     * that may possibly be imported in the given package
     */
    Collection getAllPossibleImports(Object pack);

    /**
     * Get the modelelement a given path below a given root-namespace.
     *
     * @param path the given path
     * @param theRootNamespace the given namespace to start from
     * @return the modelelement looked for, or null if not found
     */
    Object getElement(Vector path, Object theRootNamespace);

    /**
     * Finds the absolute path of a ModelElement. Ie the name of each
     * namespace starting at the root (the Model) and ending with the
     * name of the element.<p>
     *
     * The returned Vector implicitly starts at the root (the model)
     * and follows element's chain of owning namespaces back down to
     * element. The first element will thus be the name of the top level
     * namespace below the model, and the last element will be the name
     * of element itself. Note thus that for the model the path will be
     * empty.
     *
     * @param  element is the object to resolve the path for.
     * @return A Vector as described above.
     * @throws IllegalArgumentException if element isn't a ModelElement
     *         properly owned by namespaces and a model.
     */
    Vector getPath(Object element);

    /**
     * Utility function for managing several overlayed models, eg a user
     * model to which elements from some profile models is imported when
     * needed. This version of the function assumes it is permissible to
     * create missing elements.
     *
     * This function may fail and return null eg if some required object
     * doesn't exist in the target model and cannot be copied.
     *
     * @param elem is some element.
     * @param model is the model the returned object shall belong to.
     * @return An element of the same type and at the same position in the
     *  model as elem, or if that would turn out impossible then null.
     */
    Object getCorrespondingElement(Object elem, Object model);

    /**
     * Utility function for managing several overlayed models, eg a user
     * model to which elements from some profile models is imported when
     * needed. This version of the function will only copy objects if
     * canCreate is true, but may then also copy other missing elements.
     *
     * This function may fail and return null eg if the required object
     * doesn't exist in the target model and canCreate is false or some
     * required object doesn't exist in the target model and cannot be
     * copied.
     *
     * @param elem is some element.
     * @param model is the model the returned object shall belong to.
     * @param canCreate determines if objects can be copied into model.
     * @return An element of the same type and at the same position in the
     *  model as elem, or if that would turn out impossible then null.
     */
    Object getCorrespondingElement(Object elem, Object model,
            			   boolean canCreate);

    /**
     * Tests if two objects are of the same type, have the same name and the
     * same relative position in the model.
     *
     * Same relative position implies either:<ul>
     * <li>their owners correspond to eachother.
     * <li>they are both owned by model objects.
     * </ul>
     *
     * @param obj1 is an object.
     * @param obj2 is another object.
     * @return true if obj1 corresponds to obj2, false otherwise.
     */
    boolean corresponds(Object obj1, Object obj2);

    /**
     * Checks if a child for some ownershiprelationship (as in a
     * namespace A is owned by a namespace B) is allready in the
     * ownerhship relation.
     *
     * @param parent The current leaf for the ownership relation
     * @param child The child that should be owned by the parent
     * @return true if the child is allready in the ownership relationship
     */
    boolean isCyclicOwnership(Object parent, Object child);

    /**
     * This method removes the import 
     * of a modelelement from a package.
     *
     * @param handle is the package
     * @param me is the modelelement that was imported
     */
    void removeImportedElement(Object handle, Object me);

    /**
     * Set the complete collection of imported elements 
     * for a package.
     * 
     * @param pack the package to import in
     * @param imports the collection of modelelements to import
     */
    void setImportedElements(Object pack, Collection imports);
    
    /**
     * Set the alias of the ElementImport.
     * 
     * @param handle is the ElementImport
     * @param alias the alias name
     */
    void setAlias(Object handle, String alias);
    
    /**
     * Set the isSpecification attribute for the ElementImport.
     * 
     * @param handle
     *            ElementImport
     * @param isSpecification
     *            true of the element is specification
     */
    void setSpecification(Object handle, boolean isSpecification);

    /**
     * This method calculates the "contents" additional operation 
     * from the standard: <p>
     * 
     * For a Namespace: <pre>
     * [1] The operation contents results in a Set containing 
     * all ModelElements contained by the Namespace.
     * contents : Set(ModelElement)
     * contents = self.ownedElement -> union(self.namespace, contents)
     * </pre> <p>
     * 
     * For a Package: <pre>
     * [1] The operation contents results in a Set containing 
     * the ModelElements owned by or imported by the Package.
     * contents : Set(ModelElement)
     * contents = self.ownedElement->union(self.importedElement)
     * </pre>
     * 
     * For a Instance: <pre>
     * [5] The operation contents results in a Set containing all 
     * ModelElements contained by the Instance.
     *   contents: Set(ModelElement);
     *   contents = self.ownedInstance->union(self.ownedLink)
     * </pre>
     * 
     * @param namespace the ns to get the contents from
     * @return a collection of modelelements
     */
    Collection getContents(Object namespace);
    
    /**
     * This method calculates the collection 
     * of imported elements of a Package 
     * following from the standard: <p>
     * <pre>
     * [2] The operation allImportedElements results 
     * in a Set containing the ModelElements imported 
     * by the Package or one of its parents.
     * allImportedElements : Set(ModelElement)
     * allImportedElements = self.importedElement->union(
     * self.parent.oclAsType(Package).allImportedElements->select( re |
     *   re.elementImport.visibility = #public or
     *   re.elementImport.visibility = #protected))
     *</pre>
     *
     * @param pack the package to get the imported elements from
     * @return a collection of modelelements
     */
    Collection getAllImportedElements(Object pack);
    
    /**
     * This method calculates the following from the standard: <p>
     * 
     * For a Namespace:
     * <pre>
     * [2] The operation allContents results in a Set containing 
     * all ModelElements contained by the Namespace.
     * allContents : Set(ModelElement);
     * allContents = self.contents
     * </pre><p>
     * 
     * For a Classifier:
     * <pre>
     * [10] The operation allContents returns a Set containing 
     * all ModelElements contained in the Classifier together
     * with the contents inherited from its parents.
     * allContents : Set(ModelElement);
     * allContents = self.contents->union(
     *   self.parent.allContents->select(e |
     *        e.elementOwnership.visibility = #public or
     *        e.elementOwnership.visibility = #protected))
     * </pre><p>
     * 
     * For a Package:
     * <pre>
     * [3]  The operation allContents results in a Set containing 
     * the ModelElements owned by or imported 
     * by the Package or one of its ancestors.
     * allContents : Set(ModelElement);
     * allContents = self.contents->union(
     *   self.parent.allContents->select(e |
     *        e.elementOwnership.visibility = #public or
     *        e.elementOwnership.visibility = #protected))
     * </pre>
     * 
     * For a Collaboration:
     * <pre>
     * [1 ] The operation allContents results in the set of 
     * all ModelElements contained in the Collaboration
     * together with those contained in the parents 
     * except those that have been specialized.
     * allContents : Set(ModelElement);
     * allContents = self.contents->union (
     *   self.parent.allContents->reject ( e |
     *        self.contents.name->include (e.name) ))
     * </pre>
     *
     * @param namespace the ns to get the contents from
     * @return a collection of modelelements
     */
    Collection getAllContents(Object namespace);

}
