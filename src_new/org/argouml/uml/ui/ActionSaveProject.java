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

import org.argouml.application.api.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import java.io.*;
import java.awt.event.*;
import java.util.zip.*;
import java.text.MessageFormat;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 * Action that saves the project.
 *
 * @see ActionOpenProject
 * @stereotype singleton
 */
public class ActionSaveProject extends UMLAction {
  
  ////////////////////////////////////////////////////////////////
  // static variables

  public static ActionSaveProject SINGLETON = new ActionSaveProject(); 

  ////////////////////////////////////////////////////////////////
  // constructors

  public ActionSaveProject() {
    super("Save Project");
  }

  public ActionSaveProject(String title, boolean icon) {
    super(title, icon);
  }


  ////////////////////////////////////////////////////////////////
  // main methods

  public void actionPerformed(ActionEvent e) {
    trySave(true);
  }

  public boolean trySave (boolean overwrite) {
    URL url = ProjectBrowser.TheInstance.getProject().getURL();
    return trySave(overwrite, new File(url.getFile()));
  }

  public boolean trySave(boolean overwrite, File file) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();

    try {

      if (file.exists() && !overwrite) {
        //Argo.log.info ("Are you sure you want to overwrite " + fullpath + "?");
        String sConfirm = MessageFormat.format (
            Argo.localize ("Actions",
                                "template.save_project.confirm_overwrite"),
            new Object[] {file}
          );
        int nResult = JOptionPane.showConfirmDialog (
            pb,
            sConfirm,
            Argo.localize ("Actions", "text.save_project.confirm_overwrite_title"),
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
          );
        
        if (nResult != JOptionPane.YES_OPTION) {
          return false;
        }
      }
      
      String sStatus = MessageFormat.format (
          Argo.localize ("Actions", "template.save_project.status_writing"),
          new Object[] {file}
        );
      pb.showStatus (sStatus);
		
	  
      p.save(overwrite, file);
      	

      sStatus = MessageFormat.format (
          Argo.localize ("Actions", "template.save_project.status_wrote"),
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
          Argo.localize ("Actions", "template.save_project.file_not_found"),
          new Object[] {fnfe.getMessage()}
        );
      
      JOptionPane.showMessageDialog (
          pb,
          sMessage,
          Argo.localize ("Actions", "text.save_project.file_not_found_title"),
          JOptionPane.ERROR_MESSAGE
        );
      
      fnfe.printStackTrace();
    }
    catch (IOException ioe) {
      String sMessage = MessageFormat.format (
          Argo.localize ("Actions", "template.save_project.io_exception"),
          new Object[] {ioe.getMessage()}
        );
      
      JOptionPane.showMessageDialog (
          pb,
          sMessage,
          Argo.localize ("Actions", "text.save_project.io_exception_title"),
          JOptionPane.ERROR_MESSAGE
        );
      
      ioe.printStackTrace();
    }
    catch (Exception ex) {
    	String sMessage = MessageFormat.format (
          Argo.localize ("Actions", "template.save_project.general_exception"),
          new Object[] {ex.getMessage()}
        );
      
      JOptionPane.showMessageDialog (
          pb,
          sMessage,
          Argo.localize ("Actions", "text.save_project.general_exception_title"),
          JOptionPane.ERROR_MESSAGE
        );
      
      ex.printStackTrace();
    }
    
    return false;
  }

  public boolean shouldBeEnabled() {
    URL url = ProjectBrowser.TheInstance.getProject() != null ?
		ProjectBrowser.TheInstance.getProject().getURL() : null;
    return super.shouldBeEnabled() && url != null;
  }
} /* end class ActionSaveProject */
