// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// $header$
package org.argouml.kernel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.event.EventListenerList;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Category;
import org.argouml.application.api.Argo;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.UmlHelper;
import org.argouml.model.uml.foundation.core.CoreHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;
import org.argouml.xml.xmi.XMIParser;
import org.argouml.xml.xmi.XMIReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.novosoft.uml.MBase;
import ru.novosoft.uml.model_management.MModel;

/**
 * This class manages the projects loaded in argouml. It is a singleton. Classes
 * in Argouml can ask this class for the current project and set the current project.
 * Since we only have one project in ArgoUML at the moment, this class does not 
 * manage a list of projects like one would expect. This could be a nice extension
 * for the future of argouml.
 * As soon as the current project is changed, a property changed event is fired.
 * @since Nov 17, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public final class ProjectManager {
    
    public final static String CURRENT_PROJECT_PROPERTY_NAME ="currentProject";

    /**
     * The singleton instance of this class
     */
    private static ProjectManager _instance;
    
    /**
     * The project that is visible in the projectbrowser
     */
    private static Project _currentProject;

    /**
     * The listener list
     */
    private EventListenerList _listenerList = new EventListenerList();

    /**
     * The event to fire
     */
    private PropertyChangeEvent _event;
    
    /**
     * The singleton accessor method of this class
     */
    public static ProjectManager getManager() {
        if (_instance == null) {
            _instance = new ProjectManager();
        }
        return _instance;
    }

    /**
     * Constructor for ProjectManager.
     */
    private ProjectManager() {
        super();
    }

    /**
     * Adds an instance implementing propertychangelistener to the listener list
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        _listenerList.add(PropertyChangeListener.class, listener);
    }

    /**
     * Removes a listener from the listener list.
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        _listenerList.remove(PropertyChangeListener.class, listener);
    }

    private void firePropertyChanged(Project oldProject, Project newProject) {
        // Guaranteed to return a non-null array
        Object[] listeners = _listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PropertyChangeListener.class) {
                // Lazily create the event:
                if (_event == null)
                    _event =
                        new PropertyChangeEvent(
                            this,
                            CURRENT_PROJECT_PROPERTY_NAME,
                            oldProject,
                            newProject);
                ((PropertyChangeListener) listeners[i + 1]).propertyChange(
                    _event);
            }            
        }
        _event = null;

    }

    /**
     * Sets the current project (the project that is viewable in the projectbrowser).
     * This method fires a propertychanged event. 
     * @param newProject The new project.
     */
    public void setCurrentProject(Project newProject) {
        Project oldProject = _currentProject;
        _currentProject = newProject;
        firePropertyChanged(oldProject, newProject);
    }

    /**
     * Returns the current project.
     * @return Project
     */
    public Project getCurrentProject() {
        if (_currentProject == null) {
            _currentProject = makeEmptyProject();
        }
        return _currentProject;
    }
    
    /**
     * Makes an empty project with two standard diagrams.
     * @return Project
     */
    public Project makeEmptyProject() {
        Argo.log.info("making empty project");
        Project p = new Project();
        p.makeUntitledProject();
        setCurrentProject(p);
        return p;
    }


}
