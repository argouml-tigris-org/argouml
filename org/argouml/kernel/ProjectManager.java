// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.event.EventListenerList;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ProjectMemberTodoList;
import org.argouml.model.uml.UmlHelper;
import org.argouml.ui.ArgoDiagram;
import org.argouml.util.FileConstants;
import org.argouml.xml.argo.ArgoParser;
import org.argouml.xml.xmi.XMIParser;
import org.argouml.xml.xmi.XMIReader;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class manages the projects loaded in argouml.
 *
 * Classes in Argouml can ask this class for the current
 * project and set the current project.  Since we only have one
 * project in ArgoUML at the moment, this class does not manage a list
 * of projects like one would expect. This could be a nice extension
 * for the future of argouml.  As soon as the current project is
 * changed, a property changed event is fired.
 *
 * @since Nov 17, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public final class ProjectManager {

    public static final String CURRENT_PROJECT_PROPERTY_NAME =
        "currentProject";
    
    public static final String SAVE_STATE_PROPERTY_NAME = "saveState";

    /** logger */
    private static final Logger LOG = Logger.getLogger(ProjectManager.class);

    /**
     * The singleton instance of this class
     */
    private static ProjectManager instance;

    /**
     * The project that is visible in the projectbrowser
     */
    private static Project currentProject;

    /**
     * Flag to indicate we are creating a new current project
     */
    private boolean creatingCurrentProject;

    /**
     * The listener list
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
        if (instance == null) {
            instance = new ProjectManager();
        }
        return instance;
    }

    /**
     * Constructor for ProjectManager.
     */
    private ProjectManager() {
        super();
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
				     Object oldValue, Object newValue) 
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == PropertyChangeListener.class) {
                // Lazily create the event:
                if (event == null)
                    event =
                        new PropertyChangeEvent(
                            this,
                            propertyName,
                            oldValue,
                            newValue);
                ((PropertyChangeListener) listeners[i + 1]).propertyChange(
                    event);
            }
        }
        event = null;
    }

    /**
     * Sets the current project (the project that is viewable in the 
     * projectbrowser).
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
     * If there is no project, a new one is created.
     * 
     * @return Project The current project.
     */
    public Project getCurrentProject() {
        if (currentProject == null && !creatingCurrentProject) {
            currentProject = makeEmptyProject();
        }
        return currentProject;
    }

    /**
     * Makes an empty project with two standard diagrams.
     * @return Project
     */
    public Project makeEmptyProject() {
        creatingCurrentProject = true;
        LOG.info("making empty project");
        Project p = new Project();
        // the following line should not normally be here,
        // but is necessary for argouml start up.
        setCurrentProject(p);
        p.makeUntitledProject();
        // set the current project after making it!
        setCurrentProject(p);
        creatingCurrentProject = false;
        return p;
    }

    /**   
     * This method creates a project from the specified URL
     *
     * Unlike the constructor which forces an .argo extension This
     * method will attempt to load a raw XMI file
     * 
     * This method can fail in several different ways. Either by
     * throwing an exception or by having the
     * ArgoParser.SINGLETON.getLastLoadStatus() set to not true.
     * 
     * @param url The URL to load the project from.
     * @return The newly loaded project.
     * @throws IOException if the file cannot be read.
     * @throws IllegalFormatException if we don't understand the contents.
     * @throws SAXException if there is some syntax error in the file.
     * @throws ParserConfigurationException if the XML parser is not 
     *         configured properly - shouldn't happen.
     */
    public Project loadProject(URL url)
        throws IOException, IllegalFormatException, SAXException,
	       ParserConfigurationException 
    {
        Project p = null;
        String urlString = url.toString();
        int lastDot = urlString.lastIndexOf(".");
        String suffix = "";
        if (lastDot >= 0) {
            suffix = urlString.substring(lastDot).toLowerCase();
        }
        if (suffix.equals(".xmi")) {
            p = loadProjectFromXMI(url);
        } else if (suffix.equals(FileConstants.COMPRESSED_FILE_EXT)) {
	    // normal case, .zargo
            p = loadProjectFromZargo(url);
        } else if (suffix.equals(FileConstants.UNCOMPRESSED_FILE_EXT)) {
	    // the old argo format probably
            p = loadProjectFromZargo(url);
        } else {
            throw new IllegalFormatException(
                "No legal format found for url " + url.toString());
        }
        return p;
    }

    /**
     * Reads an XMI file.<p>
     *
     * This could be used to import models from other tools.
     *
     * @param url is the file name of the file
     * @return Project is a new project containing the read model
     * @throws IOException is thrown if some error occurs
     */
    private Project loadProjectFromXMI(URL url) throws IOException {
        Project p = new Project();
        XMIParser.SINGLETON.readModels(p, url);
        Object model = XMIParser.SINGLETON.getCurModel();
        UmlHelper.getHelper().addListenersToModel(model);
        p.setUUIDRefs(XMIParser.SINGLETON.getUUIDRefs());
        p.addMember(new ProjectMemberTodoList("", p));
        p.addMember(model);
        p.setNeedsSave(false);
        return p;
    }

    /**
     * Reads an url of the .zargo format.
     * 
     * @param url The URL to load the project from.
     * @return The newly created Project.
     * @throws IOException if we cannot read the file.
     * @throws SAXException if there is a syntax error in the file.
     * @throws ParserConfigurationException if the parser is incorrectly 
     *         configured. - Shouldn't happen.
     */
    private Project loadProjectFromZargo(URL url)
            throws IOException, SAXException, ParserConfigurationException {
        Project p = null;
        // read the argo 
        try {
            // first read the .argo file from Zip
            ZipInputStream zis = 
                openZipStreamAt(url, FileConstants.PROJECT_FILE_EXT);

            // the "false" means that members should not be added,
            // we want to do this by hand from the zipped stream.
            ArgoParser.SINGLETON.setURL(url);
            ArgoParser.SINGLETON.readProject(zis, false);
            p = ArgoParser.SINGLETON.getProject();
            ArgoParser.SINGLETON.setProject(null); // clear up project refs

            zis.close();
        } catch (IOException e) {
            // exception can occur both due to argouml code as to J2SE
            // code, so lets log it
            LOG.error(e);
            throw e;
        }
        p.loadZippedProjectMembers(url);
        p.postLoad();
        return p;
    }

    /**
     * Open a ZipInputStream to the first file found with
     * a given extension.
     * @param url The URL of the zip file.
     * @param ext The required extension.
     * @return the zip stream positioned at the required location.
     */
    private ZipInputStream openZipStreamAt(URL url, String ext)
            throws IOException{
        ZipInputStream zis = new ZipInputStream(url.openStream());
        ZipEntry entry = zis.getNextEntry();
        while (entry != null
                && !entry.getName().endsWith(FileConstants.PROJECT_FILE_EXT)) {
            entry = zis.getNextEntry();
        }
        return zis;
    }
    
    /**
     * Notify the gui from the project manager that the
     * current project's save state has changed.
     * 
     * @param newValue The new state.
     */
    public void notifySavePropertyChanged(boolean newValue) {
        
        firePropertyChanged(SAVE_STATE_PROPERTY_NAME,
                            new Boolean(!newValue),
                            new Boolean(newValue));
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
}
