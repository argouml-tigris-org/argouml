/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2012 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    thn
 *    Michiel van der Wulp
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.kernel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.event.EventListenerList;

import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.model.ModelCommand;
import org.argouml.model.ModelCommandCreationObserver;
import org.argouml.profile.Profile;
import org.argouml.profile.ProfileException;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;

/**
 * This class manages the projects loaded in argouml,
 * and what the current project is. <p>
 *
 * Classes in ArgoUML can ask this class for the current
 * project and set the current project.  Since we only have one
 * project in ArgoUML at the moment, this class does not manage a list
 * of projects like one would expect. This could be a nice extension
 * for the future of ArgoUML.  As soon as the current project is
 * changed, a property changed event is fired. <p>
 *
 * TODO: Move everything related to the creation of a project
 * into the ProjectFactory.
 *
 * @since Nov 17, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public final class ProjectManager implements ModelCommandCreationObserver {

    /**
     * The name of the property that defines the current project.  The values
     * passed are Projects, not Strings.  The 'name' here refers to the name
     * of this property, not the name of the project.
     *
     * @deprecated for 0.27.2 by tfmorris. Listeners of this event which expect
     *             it to indicate a new project being opened should listen for
     *             {@link #OPEN_PROJECTS_PROPERTY}. Listeners who think
     *             they need to know a single global current project need
     *             to be changed to deal with things on a per-project basis.
     */
    @Deprecated
    public static final String CURRENT_PROJECT_PROPERTY_NAME = "currentProject";

    /**
     * Property name for list of current open projects.  Old and new property
     * values are of type Project[] (arrays of Projects).
     */
    public static final String OPEN_PROJECTS_PROPERTY = "openProjects";

    private static final Logger LOG =
        Logger.getLogger(ProjectManager.class.getName());

    /**
     * The singleton instance of this class.
     */
    private static ProjectManager instance = new ProjectManager();

    /**
     * The project that is visible in the projectbrowser.
     */
    private static Project currentProject;

    private static LinkedList<Project> openProjects = new LinkedList<Project>();

    /**
     * Flag to indicate we are creating a new current project.
     * TODO: This isn't a thread-safe way of doing mutual exclusion.
     */
    private boolean creatingCurrentProject;

    private Action saveAction;

    /**
     * The listener list.
     */
    private EventListenerList listenerList = new EventListenerList();

    /**
     * The event to fire.
     *
     * TODO: Investigate! Is the purpose really to let the next call to
     * {@link #firePropertyChanged(String, Object, Object)} fire the old
     * event again if the previous invocation resulted in an exception?
     * If so, please document why. If not, fix it.
     */
    private PropertyChangeEvent event;

    /**
     * The singleton accessor method of this class.
     *
     * @return The singleton.
     */
    public static ProjectManager getManager() {
        return instance;
    }

    /**
     * Constructor for ProjectManager.
     */
    private ProjectManager() {
        super();
        Model.setModelCommandCreationObserver(this);
    }

    /**
     * Adds a listener to the listener list.
     *
     * @param listener The listener to add.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listenerList.add(PropertyChangeListener.class, listener);
    }

    /**
     * Removes a listener from the listener list.
     *
     * @param listener The listener to remove.
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listenerList.remove(PropertyChangeListener.class, listener);
    }

    /**
     * Fire an event to all members of the listener list.
     *
     * @param propertyName The name of the event.
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    void firePropertyChanged(String propertyName,
                                     Object oldValue, Object newValue) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PropertyChangeListener.class) {
                // Lazily create the event:
                if (event == null) {
                    event =
                        new PropertyChangeEvent(
                            this,
                            propertyName,
                            oldValue,
                            newValue);
                }
                ((PropertyChangeListener) listeners[i + 1]).propertyChange(
                    event);
            }
        }
        event = null;
    }

    /**
     * Sets the current project (the project that is viewable in the
     * projectbrowser). Sets the current diagram for the project (if one
     * exists). This method fires a propertychanged event.
     * <p>
     * If the argument is null, then the current project will be forgotten
     * about.
     *
     * @param newProject The new project.
     * @deprecated for 0.27.2 by tfmorris. There is no longer the concept of a
     *             single global "current" project. In the future, multiple
     *             projects will be able to be open at a time, so all code
     *             should be prepared to deal with multiple projects and should
     *             require a Project to be passed as an argument if they need
     *             access.
     */
    public void setCurrentProject(Project newProject) {
        Project oldProject = currentProject;
        currentProject = newProject;
        addProject(newProject);
        if (currentProject != null
            && currentProject.getActiveDiagram() == null) {
            List<ArgoDiagram> diagrams = currentProject.getDiagramList();
            if (diagrams != null && !diagrams.isEmpty()) {
                ArgoDiagram activeDiagram = diagrams.get(0);
                currentProject.setActiveDiagram(activeDiagram);
            }
        }
        notifyProjectAdded(newProject, oldProject);
    }

    private void notifyProjectAdded(Project newProject, Project oldProject) {
        firePropertyChanged(CURRENT_PROJECT_PROPERTY_NAME,
                oldProject, newProject);
        // TODO: Tentative implementation. Do we want something that updates
        // the list of open projects or just simple open and close events? -tfm
        firePropertyChanged(OPEN_PROJECTS_PROPERTY,
                new Project[] {oldProject}, new Project[] {newProject});
    }

    /**
     * Returns the current project (ie the project which most recently had the
     * user focus) or null if there is no current project.
     * <p>
     * This should only be used by callers who need to know the global state.
     * Most things which need a project want the project that contains them,
     * which they can discover by traversing their containing elements (e.g.
     * Fig->Diagram->DiagramSettings).
     * <p>
     *
     * @return Project the current project or null if none
     * @deprecated for 0.27.2 by tfmorris. There is no longer the concept of a
     *             single global "current" project. In the future, multiple
     *             projects will be able to be open at a time, so all code
     *             should be prepared to deal with multiple projects and should
     *             require a Project to be passed as an argument if they need
     *             access. To get a list of all currently open projects, use
     *             {@link #getOpenProjects()}. For settings which affect
     *             renderings in diagrams use
     *             {@link org.argouml.uml.diagram.ui.ArgoFig#getSettings()}.
     */
    @Deprecated
    public Project getCurrentProject() {
        return currentProject;
    }

    /**
     * @return a list of the currently open Projects in the order they were
     * opened
     */
    public List<Project> getOpenProjects() {
        List<Project> result = new ArrayList<Project>();
        if (currentProject != null) {
            result.add(currentProject);
        }
        return result;
    }

    /**
     * Makes an empty project.
     * @return Project the empty project
     */
    public Project makeEmptyProject() {
        return makeEmptyProject(true);
    }

    /**
     * Make a new empty project optionally including default diagrams.
     * <p>
     * Historically new projects have been created with two default diagrams
     * (Class and Use Case). NOTE: ArgoUML currently requires at least one
     * diagram for proper operation.
     *
     * @param addDefaultDiagrams
     *            if true the project will be be created with the two standard
     *            default diagrams (Class and Use Case)
     * @return Project the newly created project
     */
    public Project makeEmptyProject(final boolean addDefaultDiagrams) {
        final Command cmd = new NonUndoableCommand() {

            @Override
            public Object execute() {
                Model.getPump().stopPumpingEvents();

                creatingCurrentProject = true;
                LOG.log(Level.INFO, "making empty project");
                Project newProject = new ProjectImpl();
                createDefaultModel(newProject);
                if (addDefaultDiagrams) {
                    createDefaultDiagrams(newProject);
                }
                creatingCurrentProject = false;
                setCurrentProject(newProject);
                Model.getPump().startPumpingEvents();
                return null;
            }
        };
        cmd.execute();
        currentProject.getUndoManager().addCommand(cmd);
        setSaveEnabled(false);
        return currentProject;
    }

    /**
     * Makes an empty profile project.
     * @return Project the empty profile project
     */
    public Project makeEmptyProfileProject() {
        return makeEmptyProfileProject(true);
    }

    /**
     * Make a new empty profile project optionally including default diagrams.
     * <p>
     * Historically new projects have been created with two default diagrams
     * (Class and Use Case). NOTE: ArgoUML currently requires at least one
     * diagram for proper operation.
     *
     * @param addDefaultDiagrams
     *            if true the project will be be created with the standard
     *            default diagram (Class)
     * @return Project the newly created profile project
     */
    public Project makeEmptyProfileProject(final boolean addDefaultDiagrams) {
        final Command cmd = new NonUndoableCommand() {

            @Override
            public Object execute() {
                Model.getPump().stopPumpingEvents();

                creatingCurrentProject = true;
                LOG.log(Level.INFO, "making empty profile project");
                Project newProject = new ProjectImpl(Project.PROFILE_PROJECT);
                createDefaultProfile(newProject);
                if (addDefaultDiagrams) {
                    ArgoDiagram d = createClassDiagram(newProject);
                    createTodoList(newProject);
                    newProject.setActiveDiagram(d);
                }
                creatingCurrentProject = false;
                setCurrentProject(newProject);
                Model.getPump().startPumpingEvents();
                return null;
            }
        };
        cmd.execute();
        currentProject.getUndoManager().addCommand(cmd);
        setSaveEnabled(false);
        return currentProject;
    }

    /**
     * Apply all profiles from the profile configuration to a model (can be a
     * profile too).
     *
     * @param project The project with the profile configuration.
     * @param model The model to apply the profiles to.
     */
    private void applyProfileConfiguration(Project project, Object model) {
        Collection<Profile> c =
            project.getProfileConfiguration().getProfiles();
        if (c != null) {
            for (Profile p : c) {
                try {
                    for (Object profile : p.getProfilePackages()) {
                        Model.getExtensionMechanismsHelper()
                            .applyProfile(model, profile);
                    }
                } catch (ProfileException pe) {
                    LOG.log(Level.WARNING,
                            "Failed to get profile packages from profile {0}",
                            p);
                }
            }
        }
    }

    /**
     * Create the default diagrams for the project. Currently a Class Diagram
     * and a UseCase diagram.
     *
     * @param project the project to create the diagrams in.
     */
    private void createDefaultDiagrams(Project project) {
        LOG.log(Level.FINE, "Creating default diagrams");
        Object model = project.getRoots().iterator().next();
        DiagramFactory df = DiagramFactory.getInstance();
        ArgoDiagram d = createClassDiagram(project);
        LOG.log(Level.FINE, "Creating use case diagram");
        project.addMember(df.create(
                DiagramFactory.DiagramType.UseCase, model,
                project.getProjectSettings().getDefaultDiagramSettings()));
        project.addMember(new ProjectMemberTodoList("",
                project));
        createTodoList(project);
        project.setActiveDiagram(d);
    }

    /**
     * Create a class diagrams for the project.
     *
     * @param project the project to create the diagram in.
     * @return the created class diagram
     */
    private ArgoDiagram createClassDiagram(Project project) {
        LOG.log(Level.FINE, "Creating class diagram");
        Object model = project.getRoots().iterator().next();
        DiagramFactory df = DiagramFactory.getInstance();
        ArgoDiagram d = df.create(DiagramFactory.DiagramType.Class,
                model,
                project.getProjectSettings().getDefaultDiagramSettings());
        project.addMember(d);
        return d;
    }

    /**
     * Create a todo list for the project.
     *
     * @param project the project to create the todo list in.
     */
    private void createTodoList(Project project) {
        LOG.log(Level.FINE, "Creating todo list");
        project.addMember(new ProjectMemberTodoList("",
                project));
    }

    /**
     * Create the top level model for the project and set it as a root and the
     * current namespace.
     *
     * @param project the project to create the model in.
     */
    private void createDefaultModel(Project project) {
        Object model = Model.getModelManagementFactory().createModel();
        Model.getCoreHelper().setName(model,
                Translator.localize("misc.untitled-model"));
        Collection roots = new ArrayList();
        roots.add(model);
        project.setRoots(roots);
        project.setCurrentNamespace(model);
        project.addMember(model);
        // finally, apply profile configuration to the model
        applyProfileConfiguration(project, model);
    }

    /**
     * Create the top level profile for the project and set it as a root and the
     * current namespace.
     *
     * @param project the project to create the model in.
     */
    private void createDefaultProfile(Project project) {
        Object model = Model.getModelManagementFactory().createProfile();
        Model.getCoreHelper().setName(model,
                Translator.localize("misc.untitled-profile"));
        Collection roots = new ArrayList();
        roots.add(model);
        project.setRoots(roots);
        project.setCurrentNamespace(model);
        project.addMember(model);
        // finally, apply profile configuration to the model
        applyProfileConfiguration(project, model);
    }

    /**
     * Set the save action.
     *
     * @param save the action to be used
     */
    public void setSaveAction(Action save) {
        this.saveAction = save;
        // Register with the save action with other subsystems so that
        // any changes in those subsystems will enable the
        // save button/menu item etc.
        Designer.setSaveAction(save);
    }

    /**
     * @return true is the save action is currently enabled
     * <p>
     * @deprecated for 0.27.2 by tfmorris.  Use {@link Project#isDirty()}.
     */
    public boolean isSaveActionEnabled() {
        return this.saveAction.isEnabled();
    }

    /**
     * Notify the gui that the
     * current project's save state has changed. There are 2 receivers:
     * the SaveProject tool icon and the title bar (for showing a *).
     * <p>
     * @deprecated for 0.27.2 by tfmorris.  Use
     * {@link Project#setDirty(boolean)}.
     */
    public void setSaveEnabled(boolean newValue) {
        if (saveAction != null) {
            saveAction.setEnabled(newValue);
        }
    }

    private void addProject(Project newProject) {
        openProjects.addLast(newProject);
    }

    /**
     * Remove the project.
     *
     * @param oldProject The project to be removed.
     */
    public void removeProject(Project oldProject) {
        openProjects.remove(oldProject);

        // TODO: This code can be removed when getCurrentProject is removed
        if (currentProject == oldProject) {
            if (openProjects.size() > 0) {
                currentProject = openProjects.getLast();
            } else {
                currentProject = null;
            }
        }
        oldProject.remove();
    }

    /**
     * Updates the top level ModelElements for all projects.
     */
    public void updateRoots() {
        if (Model.getFacade().getUmlVersion().charAt(0) == '1') {
            // not needed in UML 1.x
            return;
        }
        for (Project p : getOpenProjects()) {
            p.updateRoots();
        }
        firePropertyChanged(OPEN_PROJECTS_PROPERTY,
                new Project[] {currentProject}, new Project[] {currentProject});
    }

    /**
     * Called when the model subsystem creates a command.
     * We must add this to the UndoManager.
     *
     * @param command the command.
     * @return result of the command, if any
     * @see org.argouml.model.ModelCommandCreationObserver#execute(ModelCommand)
     */
    public Object execute(final ModelCommand command) {
        setSaveEnabled(true);
        AbstractCommand wrappedCommand = new AbstractCommand() {
            private ModelCommand modelCommand = command;
            public void undo() {
                modelCommand.undo();
            }
            public boolean isUndoable() {
                return modelCommand.isUndoable();
            }
            public boolean isRedoable() {
                return modelCommand.isRedoable();
            }
            public Object execute() {
                return modelCommand.execute();
            }
            public String toString() {
                return modelCommand.toString();
            }
        };
        Project p = getCurrentProject();
        if (p != null) {
            return getCurrentProject().getUndoManager().execute(wrappedCommand);
        } else {
            return wrappedCommand.execute();
        }
    }
}
