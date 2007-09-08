// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

package org.argouml.kernel;

import java.beans.VetoableChangeSupport;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.argouml.uml.Profile;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.presentation.Fig;

/**
 * The Project interface encapsulates all information about a designer's
 * project. It contains the list of diagrams and UML models, various project
 * properties such as the author's name, and defaults for various settings.
 * <p>
 * TODO: This interface was mechanically refactored from the implemenation class
 * {@link ProjectImpl}. It needs to be reviewed and cleaned up, eliminating
 * methods which should be part of the public API and splitting the interface
 * into smaller function specific (e.g. TrashCan) interfaces.
 * 
 * @author Tom Morris <tfmorris@gmail.com>
 * @since 0.25.4 when it replaced the concrete class of the same name
 */
public interface Project {

    /**
     * Find the base name of this project.<p>
     *
     * This is the name minus any valid file extension.
     *
     * @return The name (a String).
     */
    public String getBaseName();

    /**
     * Get the project name. This is just the name part of the full filename.
     * @return the name of the project
     */
    public String getName();

    /**
     * Set the project URI.
     *
     * @param n The new URI (as a String).
     * @throws URISyntaxException if the argument cannot be converted to
     *         an URI.
     */
    public void setName(String n) throws URISyntaxException;

    /**
     * Get the URI for this project.
     *
     * @return The URI.
     */
    public URI getURI();

    /**
     * Set the URI for this project.
     *
     * @param theUri The URI to set.
     */
    public void setURI(URI theUri);

    /**
     * Set the project file.
     *
     * This only works if it is possible to convert the File to an uri.
     *
     * @param file File to set the project to.
     */
    public void setFile(File file);

    /**
     * Not used by "argo.tee" any more.
     * 
     * @return the search path
     * @deprecated by tfmorris for 0.25.4.  Use {@link #getSearchPathList()}.
     */
    @Deprecated
    public Vector<String> getSearchPath();
    
    /**
     * Used by "argo.tee".
     * 
     * @return the search path
     */
    public List<String> getSearchPathList();

    /**
     * @param searchPathElement the element to be added to the searchpath
     */
    public void addSearchPath(String searchPathElement);

    /**
     * Sets the searchpath.
     * @param theSearchpath The searchpath to set
     */
    public void setSearchPath(List<String> theSearchpath);
    
    /**
     * Get all members of the project.
     * Used by "argo.tee".
     *
     * @return all members.
     */
    public MemberList getMembers();

    /**
     * Add a member: ArgoDiagram, a UML Model, or a ProjectMemberTodoList.
     * 
     * @param m the member to be added
     */
    public void addMember(Object m);

    /**
     * @param model a namespace
     */
    public void addModel(Object model);

    /**
     * Get the author name. 
     * Used by "argo.tee".
     *
     * @return The author name.
     */
    public String getAuthorname();

    /**
     * Set the author name.
     *
     * @param s The new author name.
     */
    public void setAuthorname(final String s);

    /**
     * Get the author name.
     * Used by "argo.tee".
     *
     * @return The author name.
     */
    public String getAuthoremail();

    /**
     * Set the author name.
     *
     * @param s The new author name.
     */
    public void setAuthoremail(final String s);

    /**
     * Get the version.
     * Used by "argo.tee".
     *
     * @return the version.
     */
    public String getVersion();

    /**
     * Set the new version.
     * @param s The new version.
     */
    public void setVersion(String s);

    /**
     * Get the description.
     * Used by "argo.tee".
     *
     * @return the description.
     */
    public String getDescription();

    /**
     * Set a new description.
     *
     * @param s The new description.
     */
    public void setDescription(final String s);

    /**
     * Get the history file.
     * Used by "argo.tee".
     *
     * @return The history file.
     */
    public String getHistoryFile();

    /**
     * Set the history file.
     *
     * @param s The new history file.
     */
    public void setHistoryFile(String s);


    /**
     * Returns all models defined by the user. I.e. this does not return the
     * default model but all other models.
     * 
     * @return A Vector of all user defined models.
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #getUserDefinedModelList()}.
     */
    @Deprecated
    public Vector getUserDefinedModels();
    
    /**
     * Returns all models defined by the user. I.e. this does not return the
     * default model but all other models.
     *
     * @return A List of all user defined models.
     */
    public List getUserDefinedModelList();

    /**
     * Returns all models, including the default model (default.xmi).
     *
     * @return A Collection containing all models.
     */
    public Collection getModels();

    /**
     * Return the model.<p>
     *
     * If there isn't exactly one model, <code>null</code> is returned.
     *
     * @return the model.
     */
    public Object getModel();


    /**
     * Return the default type for an attribute.
     * 
     * @return a Classifier to use as the default type
     * TODO: This belongs in ProjectSettings. - tfm
     */
    public Object getDefaultAttributeType();

    /**
     * Return the default type for a parameter.
     * 
     * @return a Classifier to use as the default type
     * TODO: This belongs in ProjectSettings. - tfm
     */
    public Object getDefaultParameterType();

    /**
     * Return the default type for the return parameter of a method.
     * 
     * @return a Classifier to use as the default type
     * TODO: This belongs in ProjectSettings. - tfm
     */
    public Object getDefaultReturnType();

    /**
     * Searches for a type/classifier with name s. If the type is not found, a
     * new type is created and added to the current namespace.
     * <p>
     * TODO: Move to Model subsystem - tfm 20070307
     * 
     * @param s
     *            the name of the type/classifier to be found
     * @return Classifier
     */
    public Object findType(String s);
    
    /**
     * Searches for a type/classifier with name s. If defineNew is
     * true, a new type is defined if the type/classifier is not
     * found. The newly created type is added to the currentNamespace
     * and given the name s.
     * <p>
     * TODO: Move to Model subsystem - tfm 20070307
     * 
     * @param s the name of the type/classifier to be found
     * @param defineNew if true, define a new one
     * @return Classifier the found classifier
     */
    public Object findType(String s, boolean defineNew);

    /**
     * Finds all figs on the diagrams for some project member,
     * including the figs containing the member (so for some
     * operation, the containing figclass is returned).
     *
     * @param member The member we are looking for.
     *              This can be a model element object but also another object.
     * @return Collection The collection with the figs.
     */
    public Collection<Fig> findFigsForMember(Object member);

    /**
     * Returns a list with all figs for some UML object on all diagrams.
     *
     * @param obj the given UML object
     * @return List the list of figs
     */
    public Collection findAllPresentationsFor(Object obj);

    /**
     * Finds a classifier with a certain name.<p>
     *
     * Will only return first classifier with the matching name.
     *
     * TODO: Move to Model subsystem - tfm 20070307
     * 
     * @param s is short name.
     * @param ns Namespace where we do the search.
     * @return the found classifier (or <code>null</code> if not found).
     */
    public Object findTypeInModel(String s, Object ns);

    /**
     * @param m the namespace
     */
    public void setCurrentNamespace(Object m);

    /**
     * @return the namespace
     */
    public Object getCurrentNamespace();


    /**
     * @return the diagrams
     * @deprecated for 0.25.4 by tfmorris. Use {@link #getDiagramList()}.
     */
    @Deprecated
    public Vector<ArgoDiagram> getDiagrams();
    
    /**
     * @return the diagrams
     */
    public List<ArgoDiagram> getDiagramList();

    /**
     * Get the number of diagrams in this project.
     * Used by argo2.tee!!
     * @return the number of diagrams in this project.
     */
    public int getDiagramCount();

    /**
     * Finds a diagram with a specific name or UID.
     *
     * @return the diagram object (if found). Otherwise null.
     * @param name is the name to search for.
     */
    public ArgoDiagram getDiagram(String name);

    /**
     * @param d the diagram to be added
     */
    public void addDiagram(ArgoDiagram d);

    /**
     * @param me the given modelelement
     * @return the total number of presentation
     *         for the given modelelement in the project
     */
    public int getPresentationCountFor(Object me);

    /**
     * @return an initial target, in casu a diagram or a model
     */
    public Object getInitialTarget();

    /**
     * @return the VetoableChangeSupport
     */
    public VetoableChangeSupport getVetoSupport();

    /**
     * This is executed before a save.
     */
    public void preSave();

    /**
     * This is execcuted after a save.
     */
    public void postSave();

    /**
     * This is executed after a load.
     */
    public void postLoad();

    /**
     * Moves some object to trash, i.e. deletes it completely with all
     * dependent structures. <p>
     *
     * Deleting an object involves: <pre>
     * - Removing Target history
     * - Deleting all Fig representations for the object
     * - Deleting the UML element
     * - Deleting all dependent UML modelelements
     * - Deleting CommentEdges (which are not UML elements)
     * - Move to trash for enclosed objects, i.e. graphically drawn on top of
     * - Move to trash subdiagrams for the object
     * - Saveguard that there is always at least 1 diagram left
     * - If the current diagram has been deleted, select a new one to show
     * - Trigger the explorer when a diagram is deleted
     * - Set the needsSave (dirty) flag of the projectmanager
     * </pre>
     *
     * @param obj The object to be deleted
     * @see org.argouml.kernel.ProjectImpl#trashInternal(Object)
     */
    public void moveToTrash(Object obj);

    /**
     * @param obj the object
     * @return true if the object is trashed
     */
    public boolean isInTrash(Object obj);

    /**
     * @param theDefaultModel a uml model
     * @deprecated for 0.25.4 by tfmorris. Use 
     *          {@link #setProfiles(Collection)}.
     */
    @Deprecated
    public void setDefaultModel(Object theDefaultModel);

    /**
     * @param packages a Collection of packages containing profiles.
     */
    public void setProfiles(Collection packages);

    /**
     * Get the default model.
     *
     * @return A model.
     * @deprecated for 0.25.4 by tfmorris. Use {@link #getProfiles()}.
     */
    @Deprecated
    public Object getDefaultModel();

    /**
     * Get the collection of profile packages.
     *
     * @return collection of Packages containing profiles.
     */
    public Object getProfiles();

    /**
     * Find a type by name in the default model.
     *
     * @param name the name.
     * @return the type.
     */
    public Object findTypeInDefaultModel(String name);

    /**
     * Returns the root package.
     * 
     * @return the Package which is the root
     * @deprecated for 0.25.4 by tfmorris - use {@link #getRoots()} to
     *             packages/model elements which are at the top level. TODO: We
     *             probably need a getDefaultNamespace() method or something
     *             similar to replace some uses of this.
     */
    @Deprecated
    public Object getRoot();

    /**
     * Sets the root package.
     * @param root The root to set, a UML Package
     * @deprecated for 0.25.4 by tfmorris - use {@link #setRoots}.
     */
    @Deprecated
    public void setRoot(Object root);


    /**
     * Return a collection of top level Model Elements. Normally for ArgoUML
     * created models, this will be a single Package or Model, but other tools
     * may allow more liberal structures.
     * 
     * @return Collection of top level ModelElements
     */
    public Collection getRoots();

    /**
     * Set the top level ModelElements for this project.
     * 
     * @param elements Collection of top level ModelElements
     */
    public void setRoots(Collection elements);
    
    /**
     * Returns true if the given name is a valid name for a diagram. Valid means
     * that it does not occur as a name for a diagram yet.
     * @param name The name to test
     * @return boolean True if there are no problems with this name, false if
     * it's not valid.
     */
    public boolean isValidDiagramName(String name);

    /**
     * Returns the searchpath.
     * @return Vector
     * @deprecated for 0.25.4 by tfmorris.  Use {@link #getSearchPathList()}.
     */
    @Deprecated
    public Vector<String> getSearchpath();

    /**
     * Returns the uri.
     * @return URI
     */
    public URI getUri();

    /**
     * Returns the uUIDRefs.
     * @return HashMap
     */
    public Map getUUIDRefs();

    /**
     * Sets the searchpath.
     * @param theSearchpath The searchpath to set
     * @deprecated for 0.25.4 by tfmorris. Use {@link #setSearchPath(List)}.
     */
    @Deprecated
    public void setSearchpath(Vector<String> theSearchpath);

    /**
     * Sets the uUIDRefs.
     * @param uUIDRefs The uUIDRefs to set
     */
    public void setUUIDRefs(Map<String, Object> uUIDRefs);

    /**
     * Sets the vetoSupport.
     * @param theVetoSupport The vetoSupport to set
     */
    public void setVetoSupport(VetoableChangeSupport theVetoSupport);

    /**
     * Get the current viewed diagram.
     *
     * @return the current viewed diagram
     */
    public ArgoDiagram getActiveDiagram();

    /**
     * @param theDiagram the ArgoDiagram
     */
    public void setActiveDiagram(ArgoDiagram theDiagram);

    /**
     * Remove the project.
     */
    public void remove();

    /**
     * Used by "argo.tee".
     * 
     * @return Returns the persistenceVersion.
     */
    public int getPersistenceVersion();

    /**
     * @param pv The persistenceVersion to set.
     */
    public void setPersistenceVersion(int pv);

    /**
     * @return Returns the profile.
     */
    public Profile getProfile();

    /**
     * Repair all parts of the project before a save takes place.
     * @return a report of any fixes
     */
    public String repair();

    /**
     * Used by "argo.tee".
     * 
     * @return the settings of this project
     */
    public ProjectSettings getProjectSettings();

    public UndoManager getUndoManager();
}