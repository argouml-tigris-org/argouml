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

import java.io.Writer;

import org.apache.log4j.Logger;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectMember;
import org.argouml.ui.ArgoDiagram;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.xml.pgml.PGMLParser;
import org.tigris.gef.ocl.OCLExpander;
import org.tigris.gef.ocl.TemplateReader;
import org.tigris.gef.util.Util;

import ru.novosoft.uml.foundation.core.MModelElement;

public class ProjectMemberDiagram extends ProjectMember {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final String MEMBER_TYPE = "pgml";
  public static final String FILE_EXT = "." + MEMBER_TYPE;
  public static final String PGML_TEE = "/org/argouml/xml/dtd/PGML.tee";


  ////////////////////////////////////////////////////////////////
  // static variables

  public static OCLExpander expander = null;
    private static Logger cat = 
        Logger.getLogger(ProjectMemberDiagram.class);

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
    cat.debug("Reading " + getURL());
    PGMLParser.SINGLETON.setOwnerRegistry(getProject().getUUIDRefs());
    ArgoDiagram d = (ArgoDiagram)PGMLParser.SINGLETON.readDiagram(getURL());
    setDiagram(d);
    getProject().addDiagram(d);
    
  }

  public void save(String path, boolean overwrite) {
      save(path, overwrite, null);
  }

  public void save(String path, boolean overwrite, Writer writer) {
    if (expander == null)
      expander = new OCLExpander(TemplateReader.readFile(PGML_TEE));
      expander.expand(writer, _diagram, "", "");
  }

  protected void setDiagram(ArgoDiagram diagram) {
    _diagram = diagram;
  }

} /* end class ProjectMemberDiagram */
