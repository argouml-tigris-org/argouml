// $Id$
// Copyright (c) 2005 The Regents of the University of California. All
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

package org.argouml.ui;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.events.StatusMonitor;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Facade;
import org.argouml.model.Model;
import org.argouml.persistence.AbstractFilePersister;
import org.argouml.persistence.LastLoadInfo;
import org.argouml.persistence.OpenException;
import org.argouml.persistence.PersistenceManager;
import org.argouml.persistence.ProjectFilePersister;
import org.argouml.persistence.UmlVersionException;
import org.argouml.persistence.VersionException;
import org.argouml.persistence.XmiFormatException;
import org.argouml.swingext.LoadSwingWorker;
import org.argouml.swingext.ProgressMonitorWindow;
import org.argouml.swingext.SaveSwingWorker;
import org.argouml.uml.diagram.DiagramFactory;
import org.argouml.uml.diagram.activity.ui.UMLActivityDiagram;
import org.argouml.uml.diagram.state.ui.UMLStateDiagram;
import org.argouml.uml.diagram.static_structure.ui.UMLClassDiagram;
import org.argouml.uml.ui.ProjectFileView;
import org.argouml.util.ThreadUtils;
import org.tigris.gef.undo.UndoManager;
import org.tigris.gef.util.Util;

public class ProjectLoadSave {
    
    private static final Logger LOG = Logger.getLogger(ProjectLoadSave.class);
    


    /**
     * Returns true if we are allowed to overwrite the given file.
     *
     * @param overwrite if true, then the user is not asked
     * @param file the given file
     * @return true if we are allowed to overwrite the given file
     */
    private static boolean confirmOverwrite(boolean overwrite, File file) {
        if (file.exists() && !overwrite) {
            String sConfirm =
                Translator.messageFormat(
                    "optionpane.confirm-overwrite",
                    new Object[] {file});
            int nResult =
                JOptionPane.showConfirmDialog(
                        null,
                        sConfirm,
                        Translator.localize(
                            "optionpane.confirm-overwrite-title"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
            if (nResult != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        return true;
    }


    /**
     * @return the File to save to
     */
    protected static File getNewFile() {
        Project p = ProjectManager.getManager().getCurrentProject();

        JFileChooser chooser = null;
        URI uri = p.getURI();
        
        if (uri != null) {
            File projectFile = new File(uri);
            if (projectFile.length() > 0) {
                chooser = new JFileChooser(projectFile);
            } else {
                chooser = new JFileChooser();
            }
            chooser.setSelectedFile(projectFile);
        } else {
            chooser = new JFileChooser();
        }

        String sChooserTitle =
            Translator.localize("filechooser.save-as-project");
        chooser.setDialogTitle(sChooserTitle + " " + p.getName());

        // adding project files icon
        chooser.setFileView(ProjectFileView.getInstance());

        chooser.setAcceptAllFileFilterUsed(false);
        
        PersistenceManager.getInstance().setSaveFileChooserFilters(
                chooser, 
                uri != null ? Util.URIToFilename(uri.toString()) : null);


        int retval = chooser.showSaveDialog(ArgoFrame.getInstance());
        if (retval == JFileChooser.APPROVE_OPTION) {
            File theFile = chooser.getSelectedFile();
            AbstractFilePersister filter =
                (AbstractFilePersister) chooser.getFileFilter();
            if (theFile != null) {
                Configuration.setString(
                        PersistenceManager.KEY_PROJECT_NAME_PATH,
                        PersistenceManager.getInstance().getBaseName(
                                theFile.getPath()));
                String name = theFile.getName();
                if (!name.endsWith("." + filter.getExtension())) {
                    theFile =
                        new File(
                            theFile.getParent(),
                            name + "." + filter.getExtension());
                }
            }
            return theFile;
        }
        return null;
    }


    /**
     * Create and register diagrams for activity and statemachines in the
     * model(s) of the project. If no other diagrams are created, then a
     * default Class Diagram will be created. ArgoUML currently requires at
     * least one diagram for proper operation.
     * 
     */
    public static void registerDiagrams(Project project) {
        Facade facade = Model.getFacade();
        Collection diagramsElement = 
            Model.getModelManagementHelper().getAllModelElementsOfKind(
                    project.getModel(),
                    Model.getMetaTypes().getStateMachine());

        DiagramFactory diagramFactory = DiagramFactory.getInstance();
        Iterator it = diagramsElement.iterator();
        while (it.hasNext()) {
            Object statemachine = it.next();
            // TODO: test whether diagram already exists or needs to be created
            // ?? project.findFigsForMember(statemachine); ??
            Object namespace = facade.getNamespace(statemachine);
            if (namespace == null) {
                namespace = facade.getContext(statemachine);
                Model.getCoreHelper().setNamespace(statemachine, namespace);
            }
            ArgoDiagram diagram = null;
            if (facade.isAActivityGraph(statemachine)) {
                LOG.info("Creating activity diagram for "
                        + facade.getUMLClassName(statemachine)
                        + "<<" + facade.getName(statemachine) + ">>");
                diagram =
                    diagramFactory.createDiagram(UMLActivityDiagram.class,
                                                 namespace, statemachine);
            } else {
                LOG.info("Creating state diagram for "
                        + facade.getUMLClassName(statemachine)
                        + "<<" + facade.getName(statemachine) + ">>");
                diagram =
                    diagramFactory.createDiagram(UMLStateDiagram.class,
                                                 namespace, statemachine);
            }
            if (diagram != null) {
                project.addMember(diagram);
            }
        }
        // ISSUE 3516 : Make sure there is at least one diagram because
        // ArgoUML requires it for correct operation
        if (project.getDiagramCount() < 1) {
            ArgoDiagram d =
                diagramFactory.createDiagram(UMLClassDiagram.class,
                        project.getModel(), null);
            project.addMember(d);
        }
        if (project.getDiagramCount() >= 1 
                && project.getActiveDiagram() == null) {
            project.setActiveDiagram(
                    (ArgoDiagram) project.getDiagrams().get(0));
        }
    }

    //////////////// From ProjectBrowser ////////////////////
    

    /**
     * Try to save the project, possibly not creating a new file
     * @param overwrite if true, then we overwrite without asking
     */
    public static void trySave(boolean overwrite) {
        trySave(overwrite, false);
    }
   
   
    /**
     * Try to save the project.
     * @param overwrite if true, then we overwrite without asking
     * @param saveNewFile if true, we'll ask for a new file even if
     *                    the current project already had one  
     */
    public static void trySave(boolean overwrite, boolean saveNewFile) {
        URI uri = ProjectManager.getManager().getCurrentProject().getURI();

        File file = null;

        // this method is invoked from several places, so we have to check
        // whether if the project uri is set or not
        if (uri != null && !saveNewFile) {
            file = new File(uri);

            // does the file really exists?
            if (!file.exists()) {
                // project file doesn't exist. let's pop up a message dialog..
                int response = JOptionPane.showConfirmDialog(
                        null,
                        Translator.localize(
                        "optionpane.save-project-file-not-found"),
                        Translator.localize(
                        "optionpane.save-project-file-not-found-title"),
                        JOptionPane.YES_NO_OPTION);

                // ..and let's ask the user whether he wants to save the actual
                // project into a new file or not
                if (response == JOptionPane.YES_OPTION) {
                    saveNewFile = true;
                } else {
                    // save action has been cancelled
                    return;
                }
            }
        } else {
            // Attempt to save this project under a new name.
            saveNewFile = true;
        }

        // Prompt the user for the new name.
        if (saveNewFile) {
            file = getNewFile();

            // if the user cancelled the operation,
            // we don't have to save anything
            if (file == null) {
                return;
            }
        }

        // let's call the real save method
        trySaveWithProgressMonitor(overwrite, file);
    }

   
    /**
     * Checks if the given file is writable or read-only
     * @param file the file to be checked
     * @return true if the given file is read-only
     */
    private static boolean isFileReadonly(File file) {
        try {
            return (file == null) 
                    || (file.exists() && !file.canWrite()) 
                    || (!file.exists() && !file.createNewFile());

        } catch (IOException ioExc) {
            return true;
        }
    }

    /**
     * Loads a project displaying a nice ProgressMonitor
     * 
     * @param overwrite if true, the file is going to be overwritten
     * @param file      the target file
     */
    public static void trySaveWithProgressMonitor(boolean overwrite, File file) {
        SaveSwingWorker worker = new SaveSwingWorker(overwrite, file);
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        worker.start();
    }

   /**
    * Try to save the project.
    * @param overwrite if true, then we overwrite without asking
    * @param file the File to save to
    * @param pmw       the ProgressMonitorWindow to be updated;  
    * @return true if successful
    */
    public static boolean trySave(boolean overwrite, 
            File file, 
            ProgressMonitorWindow pmw) {
        LOG.info("Saving the project");
        Project project = ProjectManager.getManager().getCurrentProject();
        PersistenceManager pm = PersistenceManager.getInstance();
        ProjectFilePersister persister = null;

        try {
            if (!confirmOverwrite(overwrite, file)) {
                return false;
            }

            if (isFileReadonly(file)) {
                JOptionPane.showMessageDialog(null, 
                       Translator.localize(
                               "optionpane.save-project-cant-write"),
                       Translator.localize(
                               "optionpane.save-project-cant-write-title"),
                             JOptionPane.INFORMATION_MESSAGE);
               
                return false;
            }

            String sStatus =
                MessageFormat.format(Translator.localize(
                    "label.save-project-status-writing"),
                    new Object[] {file});
            StatusMonitor.notify(null, sStatus);

            persister = pm.getPersisterFromFileName(file.getName());
            if (persister == null) {
                throw new IllegalStateException("Filename " + project.getName()
                        + " is not of a known file type");
            }

           // Simulate some errors to repair.
           // TODO: Replace with junits - Bob
//           Layer lay =
//               Globals.curEditor().getLayerManager().getActiveLayer();
//           List figs = lay.getContentsNoEdges();
//           // A Fig with a null owner
//           if (figs.size() > 0) {
//               Fig fig = (Fig)figs.get(0);
//               LOG.error("Setting owner of " 
//                   + fig.getClass().getName() + " to null");
//               fig.setOwner(null);
//           }
//           // A Fig with a null layer
//           if (figs.size() > 1) {
//               Fig fig = (Fig)figs.get(1);
//               fig.setLayer(null);
//           }
//           // A Fig with a removed model element
//           if (figs.size() > 2) {
//               Fig fig = (Fig)figs.get(2);
//               Object owner = fig.getOwner();
//               Model.getUmlFactory().delete(owner);
//           }

            // Repair any errors in the project
            String report = project.repair();
            if (report.length() > 0) {
                reportError(
                        Translator.localize("dialog.repair"), true, report);
            }

            if (pmw != null) {
                pmw.progress(25);
                persister.addProgressListener(pmw);
            }

            project.preSave();
            persister.save(project, file);
            project.postSave();

            sStatus =
                MessageFormat.format(Translator.localize(
                        "label.save-project-status-wrote"),
                        new Object[] {project.getURI()});
            StatusMonitor.notify(null, sStatus);
            LOG.debug ("setting most recent project file to "
                    + file.getCanonicalPath());

           /*
            * notification of menu bar
            */
// TODO: Fix actions
//            saveAction.setEnabled(false);
            // TODO: Send file saved event
//            ProjectBrowser.getInstance().addFileSaved(file);

            Configuration.setString(Argo.KEY_MOST_RECENT_PROJECT_FILE,
                    file.getCanonicalPath());

            return true;
        } catch (Exception ex) {
            String sMessage =
                MessageFormat.format(Translator.localize(
                        "optionpane.save-project-general-exception"),
                        new Object[] {ex.getMessage()});

            JOptionPane.showMessageDialog(null, sMessage,
                    Translator.localize(
                    "optionpane.save-project-general-exception-title"),
                    JOptionPane.ERROR_MESSAGE);

            reportError(
                    Translator.localize(
                            "dialog.error.save.error",
                            new Object[] {file.getName()}),
                            true, ex);

            LOG.error(sMessage, ex);
        }

        return false;
    }


    /**
     * Register a new file saved.
     *
     * @param file The file.
     * @throws IOException if we cannot get the file name from the file.
     */
    // TODO: clean up - tfm 
//    public void addFileSaved(File file) throws IOException {
//        GenericArgoMenuBar menu = (GenericArgoMenuBar) getJMenuBar();
//        menu.addFileSaved(file.getCanonicalPath());
//    }

    /**
     * If the current project is dirty (needs saving) then this function will
     * ask confirmation from the user.
     * If the user indicates that saving is needed, then saving is attempted.
     * See ActionExit.actionPerformed() for a very similar procedure!
     *
     * @return true if we can continue with opening
     */
    public static boolean askConfirmationAndSave() {
        Project p = ProjectManager.getManager().getCurrentProject();


        if (p != null && ArgoActions.getSaveAction().isEnabled()) {
            String t =
                MessageFormat.format(Translator.localize(
                        "optionpane.open-project-save-changes-to"),
                        new Object[] {p.getName()});

            int response =
                JOptionPane.showConfirmDialog(null, t, t,
                        JOptionPane.YES_NO_CANCEL_OPTION);

            if (response == JOptionPane.CANCEL_OPTION
                    || response == JOptionPane.CLOSED_OPTION) {
                return false;
            }
            if (response == JOptionPane.YES_OPTION) {

                trySave(ProjectManager.getManager().getCurrentProject() != null
                        && ProjectManager.getManager().getCurrentProject()
                        .getURI() != null);
                if (ArgoActions.getSaveAction().isEnabled()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Loads a project displaying a nice ProgressMonitor
     * 
     * @param file      the project to be opened
     * @param showUI    whether to show the GUI or not
     */
    public static void loadProjectWithProgressMonitor(File file, boolean showUI) {
        LoadSwingWorker worker = new LoadSwingWorker(file, showUI);
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        worker.start();
    }

    /**
     * Loads the project file and opens all kinds of error message windows
     * if it doesn't work for some reason. In those cases it preserves
     * the old project.
     *
     * @param file the file to open.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     * @param pmw        the ProgressMonitorWindow to be updated;  
     *                           if not needed, use null 
     * @return true if the file was successfully opened
     */
    public static boolean loadProject(File file, boolean showUI, 
            ProgressMonitorWindow pmw) {
        LOG.info("Loading project.");

        PersistenceManager pm = PersistenceManager.getInstance();
        Project oldProject = ProjectManager.getManager().getCurrentProject();
        boolean success = true;

        // TODO:
        // This is actually a hack! Some diagram types
        // (like the statechart diagrams) access the current
        // diagram to get some info. This might cause
        // problems if there's another statechart diagram
        // active, so I remove the current project, before
        // loading the new one.

        Designer.disableCritiquing();
        Designer.clearCritiquing();
        // TODO: fix up
//      ProjectBrowser.getInstance().clearDialogs();
        Project project = null;

        if (!(file.canRead())) {
            reportError("File not found " + file + ".", showUI);
            Designer.enableCritiquing();
            success = false;
        } else {
            // Hide save action during load. Otherwise we get the
            // * appearing in title bar as models are updated
//          TODO: Fix up actions
            Action rememberedSaveAction = ArgoActions.getSaveAction();
//            ArgoActions.setSaveAction(null);
            ProjectManager.getManager().setSaveAction(null);
            try {
                ProjectFilePersister persister =
                    pm.getPersisterFromFileName(file.getName());
                if (persister == null) {
                    success = false;
                    throw new IllegalStateException("Filename "
                            + file.getName()
                            + " is not of a known file type");
                }

                if (pmw != null) {
                    persister.addProgressListener(pmw);
                }

                DiagramFactory.getInstance().getDiagram().clear();

                project = persister.doLoad(file);

                if (pmw != null) {
                    persister.removeProgressListener(pmw);
                }
                ThreadUtils.checkIfInterrupted();

                if (Model.getDiagramInterchangeModel() != null) {
                    Collection diagrams =
                        DiagramFactory.getInstance().getDiagram();
                    Iterator diag = diagrams.iterator();
                    while (diag.hasNext()) {
                        project.addMember(diag.next());
                    }
                    if (!diagrams.isEmpty()) {
                        project.setActiveDiagram(
                                (ArgoDiagram) diagrams.iterator().next());
                    }
                }

                // Let's save this project in the mru list
                // TODO: send file saved event?
//              ProjectBrowser.getInstance().addFileSaved(file);
                // Let's save this project as the last used one
                // in the configuration file
                Configuration.setString(Argo.KEY_MOST_RECENT_PROJECT_FILE,
                        file.getCanonicalPath());

                StatusMonitor.notify(null,
                        Translator.localize(
                                "label.open-project-status-read",
                                new Object[] {file.getName(), }));
            } catch (VersionException ex) {
                project = oldProject;
                success = false;
                reportError(
                        Translator.localize(
                                "dialog.error.file.version",
                                new Object[] {ex.getMessage()}),
                                showUI);
            } catch (OutOfMemoryError ex) {
                project = oldProject;
                success = false;
                LOG.error("Out of memory while loading project", ex);
                reportError(
                        Translator.localize("dialog.error.memory.limit.error"),
                        showUI);
            } catch (java.lang.InterruptedException ex) {
                project = oldProject;
                success = false;
                LOG.error("Project loading interrupted by user");
            } catch (UmlVersionException ex) {
                project = oldProject;
                success = false;
                reportError(
                        Translator.localize(
                                "dialog.error.file.version.error",
                                new Object[] {ex.getMessage()}),
                                showUI, ex);
            } catch (XmiFormatException ex) {
                project = oldProject;
                success = false;
                reportError(
                        Translator.localize(
                                "dialog.error.xmi.format.error",
                                new Object[] {ex.getMessage()}),
                                showUI, ex);
            } catch (Exception ex) {
                success = false;
                project = oldProject;
                LOG.error("Exception while loading project", ex);
                reportError(
                        Translator.localize(
                                "dialog.error.open.error",
                                new Object[] {file.getName()}),
                                showUI, ex);
            } finally {

                if (!LastLoadInfo.getInstance().getLastLoadStatus()) {
                    project = oldProject;
                    success = false;
                    // TODO: This seems entirely redundant
                    // for now I've made the message more generic, but it
                    // should be removed at a convenient time - tfm
                    reportError(
                            "Problem loading the project "
                            + file.getName()
                            + "\n"
                            + "Error message:\n"
                            + LastLoadInfo.getInstance().getLastLoadMessage()
                            + "\n"
                            + "Some (or all) information may be missing "
                            + "from the project.\n"
                            + "Please report this problem at "
                            + "http://argouml.tigris.org\n",
                            showUI);
                } else if (oldProject != null) {
                    // if p equals oldProject there was an exception and we do
                    // not have to gc (garbage collect) the old project
                    if (project != null && !project.equals(oldProject)) {
                        //prepare the old project for gc
                        LOG.info("There are " + oldProject.getMembers().size()
                                + " members in the old project");
                        LOG.info("There are " + project.getMembers().size()
                                + " members in the new project");
                        // Set new project before removing old so we always have
                        // a valid current project
                        ProjectManager.getManager().setCurrentProject(project);
                        ProjectManager.getManager().removeProject(oldProject);
                    }
                }

                if (project == null) {
                    LOG.info("The current project is null");
                } else {
                    LOG.info("There are " + project.getMembers().size()
                            + " members in the current project");
                }
                UndoManager.getInstance().empty();
                Designer.enableCritiquing();

                // Make sure save action is always reinstated
//              TODO: Fix up actions
//              this.saveAction = rememberedSaveAction;
//              ProjectManager.getManager().setSaveAction(rememberedSaveAction);
//              if (success) {
//              rememberedSaveAction.setEnabled(false);
//              }
            }
        }

        return success;
    }

    /**
     * Open a Message Dialog with an error message.
     *
     * @param message the message to display.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     */
    private static void reportError(String message, boolean showUI) {
        if (showUI) {
            JOptionPane.showMessageDialog(
                    null,
                    message,
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            System.err.print(message);
        }
    }

    /**
     * Open a Message Dialog with an error message.
     *
     * @param message the message to display.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     * @param error the error
     * 
     * TODO: This appears to have been cloned from the method below
     * without updating the Javadoc. Not sure what the difference
     * is meant to be... - tfm
     */
    private static void reportError(String message, boolean showUI, 
            String error) {
        if (showUI) {
            JDialog dialog =
                new ExceptionDialog(
                        ArgoFrame.getInstance(),
                        message,
                        error);
            dialog.setVisible(true);
        } else {
            // TODO:  Does anyone use command line?
            // If so, localization is needed - tfm
            reportError("Please report the error below to the ArgoUML"
                    + "development team at http://argouml.tigris.org.\n"
                    + message + "\n\n" + error, showUI);
        }
    }

    /**
     * Open a Message Dialog with an error message.
     *
     * @param message the message to display.
     * @param showUI true if an error message may be shown to the user,
     *               false if run in commandline mode
     * @param ex The exception that was thrown.
     */
    private static void reportError(String message, boolean showUI, 
            Throwable ex) {
        if (showUI) {
            JDialog dialog =
                new ExceptionDialog(
                        ArgoFrame.getInstance(),
                        message,
                        ex,
                        ex instanceof OpenException);
            dialog.setVisible(true);
        } else {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exception = sw.toString();
            // TODO:  Does anyone use command line?
            // If so, localization is needed - tfm
            reportError("Please report the error below to the ArgoUML"
                    + "development team at http://argouml.tigris.org.\n"
                    + message + "\n\n" + exception, showUI);
        }
    }

}
