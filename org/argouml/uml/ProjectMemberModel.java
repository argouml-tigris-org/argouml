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

package org.argouml.uml;

import java.net.URL;
import java.io.*;
import java.util.zip.*;
import javax.swing.*;

import org.argouml.xml.xmi.XMIParser;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.xmi.*;

import org.tigris.gef.util.Dbg;

import org.argouml.kernel.*;
import org.argouml.ui.*;

/**
 * @author Piotr Kaminski
 */


/** This file updated by Jim Holt 1/17/00 for nsuml support **/


public class ProjectMemberModel extends ProjectMember {

  ////////////////////////////////////////////////////////////////
  // constants

  public static final String MEMBER_TYPE = "xmi";
  public static final String FILE_EXT = "." + MEMBER_TYPE;

  ////////////////////////////////////////////////////////////////
  // instance variables

  private MModel _model;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ProjectMemberModel(String name, Project p) { super(name, p); }

  public ProjectMemberModel(MModel m, Project p) {
    super(p.getBaseName() + FILE_EXT, p);
    setModel(m);
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public MModel getModel() { return _model; }
  protected void setModel(MModel model) { _model = model; }

    // public String getName() {
    // return _project.getBaseName() + FILE_EXT;
    // }

    // public void setName(String s) { }

  public String getType() { return MEMBER_TYPE; }
  public String getFileExtension() { return FILE_EXT; }


  ////////////////////////////////////////////////////////////////
  // actions

  public void load() throws java.io.IOException, org.xml.sax.SAXException {
    Dbg.log(getClass().getName(), "Reading " + getURL());
    XMIParser.SINGLETON.readModels(_project,getURL());
    _model = XMIParser.SINGLETON.getCurModel();
    _project._UUIDRefs = XMIParser.SINGLETON.getUUIDRefs();
    Dbg.log(getClass().getName(), "Done reading " + getURL());
  }

  public void save(String path, boolean overwrite) {
      save(path, overwrite, null);
  }

  public void save(String path, boolean overwrite, Writer writer) {

      if (writer == null) {
	  System.out.println("No Writer specified!");
	  return;
      }


      //if (!path.endsWith("/")) path += "/";
      //String fullpath = path + getName();

    try {
      ProjectBrowser pb = ProjectBrowser.TheInstance;
      //System.out.println("Writing " + fullpath + "...");
      //pb.showStatus("Writing " + fullpath + "...");

      XMIWriter xmiwriter = new XMIWriter(_model,writer);
      xmiwriter.gen();

      //System.out.println("Wrote " + fullpath);
      //pb.showStatus("Wrote " + fullpath);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }


} /* end class ProjectMemberModel */
