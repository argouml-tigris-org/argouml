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
import org.tigris.gef.util.*;
import org.tigris.gef.ocl.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.awt.event.*;
import java.beans.*;
import javax.swing.*;
import javax.swing.filechooser.*;


public class ActionSaveProjectAs extends ActionSaveProject {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionSaveProjectAs SINGLETON = new ActionSaveProjectAs(); 

    public static final String separator = "/"; //System.getProperty("file.separator");

    protected static OCLExpander expander = null;


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionSaveProjectAs() {
	super("Save Project As...", NO_ICON);
	java.util.Hashtable templates = TemplateReader.readFile("/org/argouml/xml/dtd/argo.tee");
	expander = new OCLExpander(templates);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent e) {
	trySave(false);
    }

  public boolean trySave(boolean overwrite) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    
    JOptionPane.showMessageDialog (
        pb,
        Localizer.localize ("Actions", "text.save_as_project.unstable_release"),
        Localizer.localize ("Actions", "text.save_as_project.unstable_release_title"),
        JOptionPane.WARNING_MESSAGE
      );
    
    Project p =  pb.getProject();
    
    try {
      JFileChooser chooser = null;
      try {
        if (p != null && p.getURL() != null &&
            p.getURL().getFile().length()>0) {
          String filename = p.getURL().getFile();
          
          if (!filename.startsWith("/FILE1/+/")) {
            chooser  = new JFileChooser (p.getURL().getFile());
          }
        }
      }
	    catch (Exception ex) {
		    System.out.println ("exception in opening JFileChooser");
        ex.printStackTrace();
      }
      
      if (chooser == null) chooser = new JFileChooser();
      
      String sChooserTitle = Localizer.localize ("Actions", "text.save_as_project.chooser_title");
      chooser.setDialogTitle (sChooserTitle + p.getName());
      FileFilter filter = FileFilters.ZArgoFilter;
      chooser.addChoosableFileFilter (filter);
      chooser.setFileFilter (filter);
      
      int retval = chooser.showSaveDialog (pb);
      if(retval == 0) {
        File theFile = chooser.getSelectedFile();
        
        if (theFile != null) {
          String path = chooser.getSelectedFile().getParent();
          String name = chooser.getSelectedFile().getName();
          if (!name.endsWith (".zargo")) name += ".zargo";
          if (!path.endsWith (separator)) path += separator;
          
          URL oldURL = p.getURL();
          p.setFile (chooser.getSelectedFile());
          //p.setPathname(path);
          File f = new File (path + name);
          p.setURL (Util.fileToURL (f));
          pb.updateTitle();
          
          boolean fResult = super.trySave(false);
          
          if (! fResult) {
            p.setURL (oldURL);
            pb.updateTitle();
          }
          
          return fResult;
        }
      }
    }
    catch (FileNotFoundException ignore) {
      System.out.println("got an FileNotFoundException");
    }
    catch (PropertyVetoException ignore) {
      System.out.println("got an PropertyVetoException in SaveAs");
    }
    catch (IOException ignore) {
      System.out.println("got an IOException");
      ignore.printStackTrace();
    }
    
    return false;
  }

    public boolean shouldBeEnabled() {
	return true;
    }
} /* end class ActionSaveProjectAs */
