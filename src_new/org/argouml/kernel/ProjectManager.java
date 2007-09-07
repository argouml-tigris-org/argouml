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

package org.argouml.kernel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.model.ModelCommandCreationObserver;
import org.argouml.model.Model;
import org.argouml.model.ModelCommand;
import org.argouml.uml.cognitive.ProjectMemberTodoList;
import org.argouml.uml.diagram.ArgoDiagram;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.diagram.use_case.ui.UMLUseCaseDiagram;
import org.tigris.gef.graph.MutableGraphSupport;

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
     * The name of the property that defines the current project.
     */
    public static final String CURRENT_PROJECT_PROPERTY_NAME =
        "currentProject";

    /**
     * The name of the property that there is no project.
     * @deprecated in 0.25.4 By Bob Tarling This is unused.
     */
    public static final String NO_PROJECT =
        "noProject";

    /**
     * The name of the property that defines the save state.
     * @deprecated in 0.25.4 By Bob Tarling This is unused.
     */
    public static final String SAVE_STATE_PROPERTY_NAME = "saveState";

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(ProjectManager.class);

    /**
     * The singleton instance of this class.
     */
    private static ProjectManager instance = new ProjectManager();

    /**
     * The project that is visible in the projectbrowser.
     */
    private static Project currentProject;

    /**
     * Flag to indicate we are creating a new current project.
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
    private void firePropertyChanged(String propertyName,
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
     * projectbrowser).
     * Sets the current diagram for the project (if one exists).
     * This method fires a propertychanged event.<p>
     *
     * If the argument is null, then the current project will be forgotten
     * about.
     *
     * @param newProject The new project.
     */
    public void setCurrentProject(Project newProject) {
        Project oldProject = currentProject;
        currentProject = newProject;
        if (currentProject != null
            && currentProject.getActiveDiagram() == null) {
            Vector diagrams = currentProject.getDiagrams();
            if (diagrams != null && !diagrams.isEmpty()) {
                ArgoDiagram activeDiagram =
                    (ArgoDiagram) currentProject.getDiagrams().get(0);
                currentProject.setActiveDiagram(activeDiagram);
            }
        }
        firePropertyChanged(CURRENT_PROJECT_PROPERTY_NAME,
                oldProject, newProject);
    }

    /**
     * Returns the current project.<p>
     *
     * If there is no project, a new one is created
     * (unless we are busy creating one).
     *
     * @return Project the current project
     */
    public Project getCurrentProject() {
        if (currentProject == null && !creatingCurrentProject) {
            makeEmptyProject();
        }
        return currentProject;
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
            public void execute() {
                Model.getPump().stopPumpingEvents();
                
                creatingCurrentProject = true;
                LOG.info("making empty project");
                final Project oldProject = currentProject;
                currentProject = new ProjectImpl();
                if (addDefaultDiagrams) {
                    createDefaultDiagrams();
                }
                firePropertyChanged(CURRENT_PROJECT_PROPERTY_NAME,
                                    oldProject, currentProject);
                creatingCurrentProject = false;

                Model.getPump().startPumpingEvents();
                
                if (saveAction != null) {
                    saveAction.setEnabled(false);
                }
            }
        };
        cmd.execute();
        currentProject.getUndoManager().addCommand(cmd);
        return currentProject;
    }

    private void createDefaultDiagrams() {
        Object model = Model.getModelManagementFactory().createModel();
        Model.getCoreHelper().setName(model,
                Translator.localize("misc.untitled-model"));
        currentProject.setRoot(model);
        currentProject.setCurrentNamespace(model);
        currentProject.addMember(model);
        DiagramFactory df = DiagramFactory.getInstance();
        ArgoDiagram d = df.createDiagram(UMLClassDiagram.class, model, null);
        currentProject.addMember(d);
        currentProject.addMember(
        	df.createDiagram(UMLUseCaseDiagram.class, model, null));
        currentProject.addMember(new ProjectMemberTodoList("",
                currentProject));
        currentProject.setActiveDiagram(d);
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
        MutableGraphSupport.setSaveAction(save);
    }
    
    /**
     * @return true is the save action is currently enabled
     */
    public boolean isSaveActionEnabled() {
        return this.saveAction.isEnabled();
    }

    /**
     * Notify the gui that the
     * current project's save state has changed. There are 2 receivers:
     * the SaveProject tool icon and the title bar (for showing a *).
     *
     * @param newValue The new state.
     */
    public void setSaveEnabled(boolean newValue) {
        if (saveAction != null) {
            saveAction.setEnabled(newValue);
        }
    }
    

    /**
     * Remove the project.
     *
     * @param oldProject The project to be removed.
     */
    public void removeProject(Project oldProject) {

        if (currentProject == oldProject) {
            currentProject = null;
        }

        oldProject.remove();
    }

    /**
     * Called when the model subsystem creates a memento.
     * We must add this to the UndoManager.
     *
     * @param memento the memento.
     * @see org.argouml.model.ModelCommandCreationObserver#modelCommandCreated(org.argouml.model.ModelCommand)
     */
    public void modelCommandCreated(final ModelCommand memento) {
        if (saveAction != null) {
            saveAction.setEnabled(true);
        }
        AbstractCommand wrappedMemento = new AbstractCommand() {
            private ModelCommand modelMemento = memento;
            public void undo() {
                modelMemento.undo();
            }
            public void execute() {
                modelMemento.execute();
            }
            public String toString() {
                return modelMemento.toString();
            }
        };
        getCurrentProject().getUndoManager().addCommand(wrappedMemento);
    }
}
