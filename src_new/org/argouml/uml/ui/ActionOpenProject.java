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

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.util.*;

import org.tigris.gef.base.*;
import org.tigris.gef.util.Localizer;

import java.io.*;
import java.net.*;
import java.awt.event.*;
import java.text.MessageFormat;

import javax.swing.*;

public class ActionOpenProject extends UMLAction {
  
  ////////////////////////////////////////////////////////////////
  // static variables
  
  public static ActionOpenProject SINGLETON = new ActionOpenProject();
  
  public static final String separator = System.getProperty ("file.separator");
  
  ////////////////////////////////////////////////////////////////
  // constructors
  
  public ActionOpenProject() { super ("Open Project..."); }
  
  ////////////////////////////////////////////////////////////////
  // main methods
  
  public void actionPerformed (ActionEvent e) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    
    if (p != null && p.needsSave()) {
      String t = MessageFormat.format (
          Localizer.localize ("Actions", "template.open_project.save_changes_to"),
          new Object[] {p.getName()}
        );

      int response = JOptionPane.showConfirmDialog (
          pb,
          t,
          t,
          JOptionPane.YES_NO_CANCEL_OPTION
        );
      
      if (response == JOptionPane.CANCEL_OPTION) return;
      if (response == JOptionPane.YES_OPTION) {
        boolean safe = false;
        
        if (ActionSaveProject.SINGLETON.shouldBeEnabled()) {
          safe = ActionSaveProject.SINGLETON.trySave (true);
        }
        if (!safe) {
          safe = ActionSaveProjectAs.SINGLETON.trySave (false);
        }
        if (!safe) return;
      }
    }
    
    try {
      String directory = Globals.getLastDirectory();
      JFileChooser chooser = new JFileChooser (directory);
      
      if (chooser == null) chooser = new JFileChooser();
      
      chooser.setDialogTitle (Localizer.localize ("Actions", "text.open_project.chooser_title"));
      SuffixFilter filter = FileFilters.ZArgoFilter;
      chooser.addChoosableFileFilter (filter);
      chooser.addChoosableFileFilter (FileFilters.ArgoFilter);
      chooser.addChoosableFileFilter (FileFilters.XMIFilter);
      chooser.setFileFilter (filter);
      
      int retval = chooser.showOpenDialog (pb);
      if (retval == 0) {
        File theFile = chooser.getSelectedFile();
        if (theFile != null) {
          String path = theFile.getParent();
          Globals.setLastDirectory (path);
          URL url = theFile.toURL();
          if(url != null) {
            // This is actually a hack! Some diagram types
            // (like the state diagrams) access the current
            // diagram to get some info. This might cause 
            // problems if there's another state diagram
            // active, so I remove the current project, before
            // loading the new one.
            pb.setProject(Project.makeEmptyProject());
            
            p = Project.loadProject (url);
            pb.setProject (p);
            pb.showStatus (MessageFormat.format (
                Localizer.localize ("Actions", "template.open_project.status_read"),
                new Object[] {url.toString()}
              ));
          }
          
          return;
        }
      }
    }
    catch (IOException ignore) {
      System.out.println ("got an IOException in ActionOpenProject");
    }
  }
} /* end class ActionOpenProject */