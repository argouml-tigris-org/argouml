// Copyright (c) 1996-01 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ui.ProjectBrowser;

/**
 * Action that saves the project.
 *
 * @see ActionOpenProject
 * @stereotype singleton
 */
public class ActionSaveProject extends UMLAction {
    private Logger log = Logger.getLogger(this.getClass());
  
  ////////////////////////////////////////////////////////////////
  // static variables

  public static ActionSaveProject SINGLETON = new ActionSaveProject(); 

  ////////////////////////////////////////////////////////////////
  // constructors

  public ActionSaveProject() {
    super("action.save-project");
  }

  public ActionSaveProject(String title, boolean icon) {
    super(title, icon);
  }


  ////////////////////////////////////////////////////////////////
  // main methods

  public void actionPerformed(ActionEvent e) {
    URL url = ProjectManager.getManager().getCurrentProject() != null ?
        ProjectManager.getManager().getCurrentProject().getURL() : null;
    if (url == null) { 
        ActionSaveProjectAs.SINGLETON.actionPerformed(e);
    } else {
        trySave(true);
    }
  }

  public boolean trySave (boolean overwrite) {
    URL url = ProjectManager.getManager().getCurrentProject().getURL();
    return url == null ? false : trySave(overwrite, new File(url.getFile()));
  }

  public boolean trySave(boolean overwrite, File file) {
    ProjectBrowser pb = ProjectBrowser.getInstance();
    Project p = ProjectManager.getManager().getCurrentProject();

    try {

      if (file.exists() && !overwrite) {
        //Argo.log.info ("Are you sure you want to overwrite " + fullpath + "?");
        String sConfirm = MessageFormat.format (
            Argo.localize ("Actions",
                                "optionpane.save-project-confirm-overwrite"),
            new Object[] {file}
          );
        int nResult = JOptionPane.showConfirmDialog (
            pb,
            sConfirm,
            Argo.localize ("Actions", "optionpane.save-project-confirm-overwrite-title"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
          );
        
        if (nResult != JOptionPane.YES_OPTION) {
          return false;
        }
      }
      
      String sStatus = MessageFormat.format (
          Argo.localize ("Actions", "label.save-project-status-writing"),
          new Object[] {file}
        );
      pb.showStatus (sStatus);
		
	  
      p.save(overwrite, file);
      	

      sStatus = MessageFormat.format (
          Argo.localize ("Actions", "label.save-project-status-wrote"),
          new Object[] {p.getURL()}
        );
      pb.showStatus (sStatus);
      Argo.log.debug ("setting most recent project file to " +
                      file.getCanonicalPath());
      Configuration.setString(Argo.KEY_MOST_RECENT_PROJECT_FILE, file.getCanonicalPath());
      
      return true;
    }
    catch (FileNotFoundException fnfe) {
      String sMessage = MessageFormat.format (
          Argo.localize ("Actions", "optionpane.save-project-file-not-found"),
          new Object[] {fnfe.getMessage()}
        );
      
      JOptionPane.showMessageDialog (
          pb,
          sMessage,
          Argo.localize ("Actions", "optionpane.save-project-file-not-found-title"),
          JOptionPane.ERROR_MESSAGE
        );
      
     log.error(sMessage, fnfe);
    }
    catch (IOException ioe) {
      String sMessage = MessageFormat.format (
          Argo.localize ("Actions", "optionpane.save-project-io-exception"),
          new Object[] {ioe.getMessage()}
        );
      
      JOptionPane.showMessageDialog (
          pb,
          sMessage,
          Argo.localize ("Actions", "optionpane.save-project-io-exception-title"),
          JOptionPane.ERROR_MESSAGE
        );
      
        log.error(sMessage, ioe);
    }
    catch (Exception ex) {
    	String sMessage = MessageFormat.format (
          Argo.localize ("Actions", "optionpane.save-project-general-exception"),
          new Object[] {ex.getMessage()}
        );
      
      JOptionPane.showMessageDialog (
          pb,
          sMessage,
          Argo.localize ("Actions", "optionpane.save-project-general-exception-title"),
          JOptionPane.ERROR_MESSAGE
        );
      
        log.error(sMessage, ex);
    }
    
    return false;
  }

  public boolean shouldBeEnabled() {
    
    return super.shouldBeEnabled();
  }
} /* end class ActionSaveProject */
