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

package org.argouml.uml.diagram;

/**
 * @author Piotr Kaminski
 */

import java.net.URL;
import java.util.*;
import java.beans.*;
import java.io.*;

import javax.swing.*;

import org.argouml.xml.pgml.PGMLParser;

import org.tigris.gef.base.*;
import org.tigris.gef.util.*;

import org.tigris.gef.ocl.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;

import org.argouml.uml.*;
import org.argouml.uml.diagram.ui.*;
import ru.novosoft.uml.foundation.core.*;

public class ProjectMemberDiagram extends ProjectMember {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final String MEMBER_TYPE = "pgml";
  public static final String FILE_EXT = "." + MEMBER_TYPE;
  public static final String PGML_TEE = "/org/argouml/xml/dtd/PGML.tee";


  ////////////////////////////////////////////////////////////////
  // static variables

  public static OCLExpander expander = null;

  ////////////////////////////////////////////////////////////////
  // instance variables

  private ArgoDiagram _diagram;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ProjectMemberDiagram(String name, Project p) { super(name, p); }

  public ProjectMemberDiagram(ArgoDiagram d, Project p) {
    super(null, p);
    String s = Util.stripJunk(d.getName());
    //if (!(s.startsWith(_project.getBaseName() + "_")))
    //  s = _project.getBaseName() + "_" + s;
    setName(s);
    setDiagram(d);
    // Make sure that the namespace has an UUID, otherwise we will not
    // be able to match them after a save-load cycle.
    if (d instanceof UMLDiagram) {
	UMLDiagram u = (UMLDiagram)d;
	if (u.getNamespace() instanceof MModelElement) {
	    MModelElement me = (MModelElement)u.getNamespace();
	    // if (me.getUUID() == null)
		//   me.setUUID(UUIDManager.SINGLETON.getNewUUID());
	}
    }
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public ArgoDiagram getDiagram() { return _diagram; }
  public String getType() { return MEMBER_TYPE; }
  public String getFileExtension() { return FILE_EXT; }

  public void load() {
    Dbg.log(getClass().getName(), "Reading " + getURL());
    PGMLParser.SINGLETON.setOwnerRegistry(getProject()._UUIDRefs);
    ArgoDiagram d = (ArgoDiagram)PGMLParser.SINGLETON.readDiagram(getURL());
    setDiagram(d);
    try { getProject().addDiagram(d); }
    catch (PropertyVetoException pve) { }
  }

  public void save(String path, boolean overwrite) {
      save(path, overwrite, null);
  }

  public void save(String path, boolean overwrite, Writer writer) {
    if (expander == null)
      expander = new OCLExpander(TemplateReader.readFile(PGML_TEE));

//     if (!path.endsWith("/")) path += "/";
//     String fullpath = path + getName();
//     try {
//       System.out.println("Writing " + fullpath + "...");
//       Globals.showStatus("Writing " + fullpath + "...");
//       File f = new File(fullpath);
//       if (f.exists() && !overwrite) {
// 	String t = "Overwrite " + fullpath;
// 	ProjectBrowser pb = ProjectBrowser.TheInstance;
// 	int response =
// 	  JOptionPane.showConfirmDialog(pb, t, t,
// 					JOptionPane.YES_NO_OPTION);
// 	if (response == JOptionPane.NO_OPTION) return;
//       }
//       FileWriter fw = new FileWriter(f);
      expander.expand(writer, _diagram, "", "");
//       System.out.println("Wrote " + fullpath);
//       Globals.showStatus("Wrote " + fullpath);
      // needs-more-work: progress bar in ProjectBrowser
//      fw.close();
//     }
//     catch (FileNotFoundException ignore) {
//       System.out.println("got an FileNotFoundException");
//     }
//     catch (IOException ignore) {
//       System.out.println("got an IOException");
//       ignore.printStackTrace();
//     }
  }

  protected void setDiagram(ArgoDiagram diagram) {
    _diagram = diagram;
  }

} /* end class ProjectMemberDiagram */
