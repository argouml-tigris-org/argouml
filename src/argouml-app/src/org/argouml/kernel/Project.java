/* $Id$
 *******************************************************************************
 * Copyright (c) 2010-2014 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Bob Tarling
 *******************************************************************************
 *
 * Some portions of this file were previously release using the BSD License:
 */

// Copyright (c) 2007-2008 The Regents of the University of California. All
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

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.argouml.model.Defaults;
import org.argouml.uml.diagram.ArgoDiagram;
import org.tigris.gef.presentation.Fig;

/**
 * The Project interface encapsulates all information about a designer's
 * project. It contains the list of diagrams and UML models, various project
 * properties such as the author's name, and defaults for various settings.
 * <p>
 * TODO: This interface was mechanically refactored from the implementation
 * class {@link ProjectImpl}. It needs to be reviewed and cleaned up,
 * eliminating methods which should be part of the public API and splitting the
 * interface into smaller function specific (e.g. TrashCan) interfaces.
 * 
 * @author Tom Morris <tfmorris@gmail.com>, Thomas Neustupny
 * @since 0.25.4 when it replaced the concrete class of the same name
 */
public interface Project {

    /**
     * Project type: an UML project
     */
    public static final int UML_PROJECT = 0;

    /**
     * Project type: a profile project
     */
    public static final int PROFILE_PROJECT = 1;

    /**
     * Get the project name. This is just the name part of the full filename.
     * @return the name of the project
     */
    public String getName();

    /**
     * Get the project type. (Currently needed for profile project; probably
     * no longer needed when ArgoUML supports multimodel projects.)
     *
     * @return The URI.
     */
    public int getProjectType();

    /**
     * Set the project type. (Currently needed for profile project; probably
     * no longer needed when ArgoUML supports multimodel projects.)
     * 
     * @param projectType The new project type.
     */
    public void setProjectType(int projectType);

    /**
     * Get the URI for this project.
     *
     * @return The URI.
     */
    public URI getURI();

    /**
     * Set the URI for this project. <p>
     * 
     * Don't use this directly! Use instead:
     * {@link org.argouml.persistence.PersistenceManager
     * #setProjectURI(URI, Project)}
     * <p>
     * TODO: Why isn't this deprecated or private if it is not to be used?
     * 
     * @param theUri The URI to set.
     */
    public void setUri(final URI theUri);

    /**
     * Set the project file.
     *
     * This only works if it is possible to convert the File to an uri.
     *
     * @param file File to set the project to.
     */
    public void setFile(final File file);

    
    /**
     * Used by "argo.tee".
     * 
     * @return the search path
     */
    // TODO: Unused?
    public List<String> getSearchPathList();

    /**
     * @param searchPathElement the element to be added to the searchpath
     */
    // TODO: Unused?
    public void addSearchPath(String searchPathElement);

    /**
     * Sets the searchpath.
     * @param theSearchpath The searchpath to set
     */
    // TODO: Unused?
    public void setSearchPath(final List<String> theSearchpath);
    
    /**
     * Get all members of the project.
     * Used by "argo.tee".
     *
     * @return all members.
     */
    public List<ProjectMember> getMembers();

    /**
     * Add a member: ArgoDiagram, a UML Model, or a ProjectMemberTodoList.
     * 
     * @param m the member to be added
     */
    public void addMember(final Object m);

    /**
     * @param model a namespace
     */
    public void addModel(final Object model);

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
     * Get the author e-mail address.
     * Used by "argo.tee".
     *
     * @return the author e-mail address
     */
    public String getAuthoremail();

    /**
     * Set the author e-mail address.
     *
     * @param s the new author e-mail address
     */
    public void setAuthoremail(final String s);

    /**
     * Get the version. 
     * This is the ArgoUML version that last saved this project.
     * This field is not editable by the user.
     * Used by "argo.tee".
     *
     * @return the version.
     */
    public String getVersion();

    /**
     * Set the new version.
     * This is the ArgoUML version that last saved this project.
     * This field is not editable by the user.
     * @param s The new version.
     */
    public void setVersion(final String s);

    /**
     * Get the description.
     * This is the description of the project, as entered by the user.
     * Used by "argo.tee".
     *
     * @return the description.
     */
    public String getDescription();

    /**
     * Set a new description.
     * This is the description of the project. 
     * It is freely editable by the user.
     *
     * @param s The new description.
     */
    public void setDescription(final String s);

    /**
     * Get the history file name.
     * Not used.
     * Used by "argo.tee".
     *
     * @return The history file.
     */
    public String getHistoryFile();

    /**
     * Set the history file name.
     *
     * @param s The new history file.
     */
    public void setHistoryFile(final String s);

    
    /**
     * Returns all models defined by the user. I.e. this does not return any
     * profile packages but all other top level Packages (usually Models).
     *
     * @return A List of all user defined models.
     */
    public List getUserDefinedModelList();

    /**
     * Returns all top level Packages (e.g. Models), including the profile
     * packages. 
     * <p>
     * <em>WARNING:</em> The models returned by this method are <em>not</em>
     * ordered.  Any code which makes the assumption that the user model is
     * first (or any other ordering assumption) is broken!
     * <p><em>NOTE:</em> Since user defined models and profiles are
     * handled quite differently, you normally want to use
     * {@link #getUserDefinedModelList()} instead of this method.
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
     * @deprecated for 0.25.4 by tfmorris.  Use 
     * {@link #getUserDefinedModelList()} or {@link #getModels()}.
     */
    @Deprecated
    public Object getModel();

    public Defaults getDefaults();

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
    * @deprecated for 0.27.2 by tfmorris. Since we can now have multiple top
     *             level packages in the project, there is no concept of a
     *             single current namespace. To add a new top-level package, use
     *             {@link #getRoots()}.add(Object).
     */
    @Deprecated
    public void setCurrentNamespace(final Object m);

    /**
     * @return the namespace
     * @deprecated for 0.27.2 by tfmorris. Since we can now have multiple top
     *             level packages in the project, there is no concept of a
     *             single current namespace. Callers should use
     *             {@link #getRoots()} and be prepared to handle multiple roots.
     */
    @Deprecated
    public Object getCurrentNamespace();

    
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
    public void addDiagram(final ArgoDiagram d);

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
     * This is executed before a save.
     */
    public void preSave();

    /**
     * This is executed after a save.
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
     * <p> 
     * TODO: This should just be named delete() or something which better 
     * tells what it does (since there really isn't a trash can).
     */
    public void moveToTrash(Object obj);

    /**
     * @param obj the object
     * @return true if the object is trashed
     * @deprecated for 0.27.3 by tfmorris. Not actually implemented. The
     *             (future) Undo facility is a better way to handle this.
     */
    @Deprecated
    public boolean isInTrash(Object obj);


    /**
     * Find a type by name in the default model.
     * <p>
     * <em>NOTE:</em>The behavior of this method changed after version 0.24.
     * Earlier versions copied the type from the profile or default model into
     * the user model.  The type is now returned directly and HREFs are used
     * to link to it when the model is written out.
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
     *             get packages/model elements which are at the top level.
     *             <p>
     *             TODO: We probably need a getDefaultNamespace() method or
     *             something similar to replace some uses of this.
     */
    @Deprecated
    public Object getRoot();

    /**
     * Sets the root package.
     * @param root The root to set, a UML Package
     * @deprecated for 0.25.4 by tfmorris - use {@link #setRoots}.
     */
    @Deprecated
    public void setRoot(final Object root);

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
    public void setRoots(final Collection elements);

    /**
     * Updates the top level ModelElements for all projects. In UML2, each
     * model knows it's root elements, so this could make setRoots(...)
     * obsolete. E.g., applying a stereotype in UML2 adds a new root.
     * 
     * TODO: This is redundant with setRoots/getRoots.  There are already too
     * many ways this stuff is managed without adding an additional one.
     * All current model subsystem implementations know their top level
     * elements.  Responsibility can be moved to the model subsystem, but
     * let's choose *one* way of managing this.
     */
    public void updateRoots();
    
    /**
     * Returns true if the given name is a valid name for a diagram. Valid means
     * that it does not occur as a name for a diagram yet.
     * @param name The name to test
     * @return boolean True if there are no problems with this name, false if
     * it's not valid.
     */
    public boolean isValidDiagramName(String name);


    /**
     * Returns the uri.
     * @return URI
     */
    public URI getUri();

    /**
     * Returns the uUIDRefs.
     * @return HashMap
     */
    public Map<String, Object> getUUIDRefs();

    /**
     * Sets the uUIDRefs.
     * @param uUIDRefs The uUIDRefs to set
     */
    public void setUUIDRefs(final Map<String, Object> uUIDRefs);

    /**
     * Get the current viewed diagram.
     * <p>
     * Used by "argo.tee" to save the name of this diagram, so that the same
     * diagram can be initially shown when reloading this project. This probably
     * needs to be converted to an ordered list of open diagram windows to
     * support MDI.
     * 
     * @return the current viewed diagram
     * @deprecated for 0.27.2 by tfmorris for all uses other than argo.tee. The
     *             active diagram is a concept associated with the current
     *             editing window, not a project. It can be retrieved from
     *          {@link org.argouml.uml.diagram.DiagramUtils#getActiveDiagram()},
     *          which will get the diagram for the window 
     *          that last contained the mouse (from GEF).
     *          Alternatively, to get the diagram from a Fig, use 
     *          ((LayerPerspective) getLayer()).getDiagram().
     */
    @Deprecated
    public ArgoDiagram getActiveDiagram();

    /**
     * @param theDiagram the ArgoDiagram
     * @deprecated for 0.27.2 by tfmorris.  The active diagram is a concept
     * associated with the current editing window, not a project.
     */
    @Deprecated
    public void setActiveDiagram(final ArgoDiagram theDiagram);
    
    /**
     * @param diagramName the name of the diagram to show 
     * by default after loading
     */
    public void setSavedDiagramName(String diagramName);

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

    /**
     * @return Returns the profile configuration.
     */
    public ProfileConfiguration getProfileConfiguration();


    /**
     * Set the profile configuration.
     * 
     * @param pc the profile configuration
     */
    public void setProfileConfiguration(final ProfileConfiguration pc);

    /**
     * Return the UndoManager for this project.  Undo is managed on a 
     * per-project basis.
     * 
     * @return the UndoManager for this project
     */
    public UndoManager getUndoManager();
    
    /**
     * @return true if Project has been modified since last save
     */
    public boolean isDirty();
    
    /**
     * Set the dirty flag for the project.  This has no direct effect other than
     * setting the flag.
     * 
     * @param isDirty true if the project should be marked as dirty
     */
    public void setDirty(boolean isDirty);
    
    /**
     * Add a project listener for any class interested in addition and removal of diagrams
     * from a  project
     * @param listener
     */
    public void addProjectListener(ProjectListener listener);

    /**
     * Remove a project listener
     * from a  project
     * @param listener
     */
    public void removeProjectListener(ProjectListener listener);
}
