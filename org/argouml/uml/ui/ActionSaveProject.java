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
import org.tigris.gef.ocl.*;
import java.io.*;
import java.awt.event.*;
import java.util.zip.*;

import javax.swing.JOptionPane;

public class ActionSaveProject extends UMLAction {
  
  ////////////////////////////////////////////////////////////////
  // static variables

  public static ActionSaveProject SINGLETON = new ActionSaveProject(); 

  protected static OCLExpander expander = null;

  public static String ARGO_TEE = "/org/argouml/xml/dtd/argo.tee";


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
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    
    try {
      if (expander == null) {
        java.util.Hashtable templates = TemplateReader.readFile (ARGO_TEE);
        expander = new OCLExpander(templates);
      }
      
      Project p =  pb.getProject();
      
      String fullpath = "Untitled.zargo";
      if (p.getURL() != null) fullpath = p.getURL().getFile();
      
      if (fullpath.charAt (0) == '/' && fullpath.charAt (2) == ':') {
        fullpath = fullpath.substring(1); // for Windows /D: -> D:
      }
      
      File f = new File (fullpath);
      if (f.exists() && !overwrite) {
        //Argo.log.info ("Are you sure you want to overwrite " + fullpath + "?");
        int nResult = JOptionPane.showConfirmDialog (
            pb,
            "Are you sure you want to overwrite " + fullpath + "?",
            "Confirm overwrite",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
          );
        
        if (nResult != JOptionPane.YES_OPTION) {
          return false;
        }
      }
      
      pb.showStatus ("Writing " + fullpath + "...");      
      {
        ZipOutputStream zos = new ZipOutputStream (new FileOutputStream (f));
        ZipEntry zipEntry = new ZipEntry (p.getBaseName() + ".argo");
        zos.putNextEntry (zipEntry);
        OutputStreamWriter fw = new OutputStreamWriter (zos, "UTF-8");
        p.preSave();
        expander.expand (fw, p, "", "");
        fw.flush();
        // zos.flush();
        zos.closeEntry();
        String parentDirName = fullpath.substring (0, fullpath.lastIndexOf ("/"));
        Argo.log.info ("Dir ==" + parentDirName);
        p.saveAllMembers (parentDirName, overwrite, fw, zos);
        //needs-more-work: in future allow independent saving
        p.postSave();
        fw.close();
        // zos.close();
      }
      pb.showStatus ("Wrote " + p.getURL());
      
      return true;
    }
    catch (FileNotFoundException fnfe) {
      JOptionPane.showMessageDialog (
          pb,
          "A problem occurred while saving: \"" + fnfe.getMessage() + "\".\n"+
          "Your file might be corrupted.",
          "Problem while saving",
          JOptionPane.ERROR_MESSAGE
        );
      
      fnfe.printStackTrace();
    }
    catch (IOException ioe) {
      JOptionPane.showMessageDialog (
          pb,
          "A problem occurred while saving: \"" + ioe.getMessage() + "\".\n" +
          "Your file might be corrupted.",
          "Problem while saving",
          JOptionPane.ERROR_MESSAGE
        );
      
      ioe.printStackTrace();
    }
    
    return false;
  }

  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() &&
           p != null &&
           p.getURL() != null &&
           p.getURL().toString().indexOf ("templates") == -1;  // something of a hack, isn't it???
  }
} /* end class ActionSaveProject */
