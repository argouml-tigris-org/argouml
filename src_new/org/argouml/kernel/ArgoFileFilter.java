// Copyright (c) 1996-99 The Regents of the University of California. All
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

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/** <B>Currently only .zargos are supported</B>
 */
public class ArgoFileFilter extends FileFilter {


    /**
     * Return true if this file should be shown in the directory pane,
     * false if it shouldn't.
     *
     * Files that begin with "." are ignored.
     *
     * @see #getExtension
     * @see FileFilter#accept
     */
    public boolean accept(File f) {
      if (f == null) return false;
      if (f.isDirectory()) return true;
      String extension = getExtension(f);
      if ("argo".equalsIgnoreCase(extension)) return true;
      return false;
    }

    /**
     * Return the extension portion of the file's name .
     *
     * @see #getExtension
     * @see FileFilter#accept
     * @param f is the file name
     * @return the extension of the file or null
     */
     public String getExtension(File f) {
       if (f == null) return null;
       String filename = f.getName();
       int i = filename.lastIndexOf('.');
       if (i>0 && i<filename.length()-1) {
	 return filename.substring(i+1).toLowerCase();
       }
       return null;
    }


     /**
      * @return  a textual description of the file type */     
    public String getDescription() {
      return "Argo v0.5.2 file format (*.argo)";// shouldn't this be updated if in fact the user
      //gets this message, maybe getArgoVersion instead?
    }

}
