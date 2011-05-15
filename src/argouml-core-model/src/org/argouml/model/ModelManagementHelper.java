/* $Id$
 *******************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *******************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

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
import java.util.List;


/**
 * The interface for the helper for ModelManagement.<p>
 *
 * Created from the old ModelManagementHelper.
 */
public interface ModelManagementHelper {
    
    /**
     * Name of stereotype applied to Permission which is a friend.
     */
    String FRIEND_STEREOTYPE = "friend";
    /**
     * Name of stereotype applied to a Permission which is for import.
     */
    String IMPORT_STEREOTYPE = "import";
    /**
     * Name of stereotype applied to Permission which is a package access.
     */
    String ACCESS_STEREOTYPE = "access";
    
    /**
     * Returns all subsystems found in this namespace and in its containing
     * namespaces.
     *
     * @param ns is the namespace
     * @return Collection
     */
    Collection getAllSubSystems(Object ns);

    /**
     * Returns all namespaces found in this namespace and in its containing
     * namespaces.
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
     * Returns all modelelements found in this namespace and its containing
     * namespaces that are of some class kind.<p>
     *
     * @param namespace is the namespace
     * @param type is the class kind
     * @return Collection
     */
    Collection getAllModelElementsOfKind(Object namespace, Object type);

    /**
     * helper method for {@link #getAllModelElementsOfKind(Object, Object)}.
     *
     * @param namespace
     *            namespace.
     * @param kind
     *            name of class to find (without implementation-specific
     *            additions)
     * @return a Collection.
     * @deprecated in 0.33.4 by Bob Tarling. This assumes knowledge of class
     * names outside of the model implementation. Use
     * {@link #getAllModelElementsOfKind(Object, Object)} instead.
     */
    @Deprecated
    Collection getAllModelElementsOfKind(Object namespace, String kind);

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
     * Get the modelelement with the given path, starting at the root of
     * repository.
     * 
     * @param path the given path
     * @return the modelelement looked for, or null if not found
     */
    Object getElement(List<String> path);
    
    /**
     * Get the modelelement a given path below a given root-namespace.
     * 
     * @param path the given path
     * @param theRootNamespace the given namespace to start from. If null, start
     *                from the root (equivalent to {@link #getElement(List)}.
     * @return the modelelement looked for, or null if not found
     */
    Object getElement(List<String> path, Object theRootNamespace);
    

    /**
     * Find the absolute path of a ModelElement. Ie the name of each namespace
     * starting a root or top level model element and ending with the name of
     * the element.
     * <p>
     * The returned List implicitly starts with a root element and follows
     * element's chain of owning namespaces back down to element. The first
     * element will thus be the name of the top level namespace, and the last
     * element will be the name of element itself.
     * <p>
     * <em>COMPATIBILITY WARNING</em> - The previous version of this method
     * getPath(Object) would only return the path to the <em>first</em>
     * enclosing Model, not the root, and it would not include the name of the
     * model itself. This version will keep going up until it finds an element
     * with no parent and it includes the name of that top level element which
     * means that it will normally be one element longer than the previous
     * method.
     * 
     * @param element
     *                is the object to resolve the path for.
     * @return A List of namespaces as described above.
     * @throws IllegalArgumentException
     *                 if element isn't a ModelElement properly owned by
     *                 namespaces and a model.
     */
    List<String> getPathList(Object element);
    
    /**
     * Find the root elements in the context of a given model. The model itself
     * also belongs to the list, so the resulting list cannot be empty.
     * <p>
     * Note: in UML 1.x, the model itself is the only element that is returned.
     * But in UML 2.x, e.g., stereotype applications are also included.
     * <p>
     * 
     * @param model
     *                is the model the root elements belong to.
     * @return A List of elements as described above.
     * @throws IllegalArgumentException
     *                 if model isn't a Model object.
     */
    List<Object> getRootElements(Object model);

    /**
     * Checks if a child for some ownership relationship (as in a
     * namespace A is owned by a namespace B) is already in the
     * ownerhship relation.
     *
     * @param parent The current leaf for the ownership relation
     * @param child The child that should be owned by the parent
     * @return true if the child is already in the ownership relationship
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
     * For a Subsystem: <pre>
     * [2] The operation contents results in a Set containing 
     * the ModelElements owned by or imported by the Subsystem.
     *   contents : Set(ModelElement)
     *   contents = self.ownedElement->union(self.importedElement)
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
     * @param namespace the namespace to get the contents from
     * @return a collection of modelelements
     */
    Collection getAllContents(Object namespace);

    /**
     * Return the true if the model element is read only (ie not modifiable).
     * Note that this differs from {@link Facade#isReadOnly(Object)} which
     * refers to <em>instances</em> of a given model (M0 level) as opposed to
     * the model itself (M1).
     * 
     * @param element a UML Element
     * @return true if the element is readonly
     * @since 0.26
     */
    boolean isReadOnly(Object element);
}
