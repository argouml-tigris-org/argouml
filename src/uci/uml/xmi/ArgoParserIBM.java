// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.

package uci.uml.xmi;

import java.util.*;
import java.io.*;
import java.beans.*;
import java.net.URL;

import uci.uml.ui.Project;
import uci.gef.PGMLParserIBM;
import uci.gef.Diagram;
import uci.uml.Model_Management.*;

import com.ibm.xml.parser.*;
import org.w3c.dom.*;

public class ArgoParserIBM implements TagHandler {

  ////////////////////////////////////////////////////////////////
  // static variables

  public static ArgoParserIBM SINGLETON = new ArgoParserIBM();

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Project _proj = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  protected ArgoParserIBM() { }

  ////////////////////////////////////////////////////////////////
  // main parsing methods

  // needs-more-work: should be able to merge an existing project into
  // the current one.

  public void readProject(File f) {
    try {
      FileInputStream fis = new FileInputStream(f);
      String filename = f.getName();
      String pathname = f.getParent();
      //System.out.println("== pathname: " + pathname);
      readProject(filename, pathname, fis);
    }
    catch (Exception ex) {
      System.out.println("Exception reading project 1================");
      ex.printStackTrace();
    }
  }

  public void readProject(String filename, URL url) {
    try {
      InputStream is = url.openStream();
      String pathname = url.toString();
      pathname = pathname.substring(0, pathname.length() - filename.length());
      readProject(filename, pathname, is);
    }
    catch (IOException ex) {
      System.out.println("could not read project from URL:" + url);
      ex.printStackTrace();
    }
  }

  public void readProject(String filename, String pathname, InputStream is) {
    try {
      System.out.println("=======================================");
      System.out.println("== READING PROJECT: " + filename);
      Parser pc = new Parser(filename);
      pc.setTagHandler(this);
      pc.setProcessExternalDTD(false);
      _proj = new Project(filename);
      _proj.setPathname(pathname);
      // needs-more-work: predefined types now defined twice
      pc.readStream(is);
      is.close();
    }
    catch (Exception ex) {
      System.out.println("Exception reading project================");
      ex.printStackTrace();
    }
  }

  public Project getProject() { return _proj; }

  public void handleStartTag(TXElement e, boolean empty) {
    String n = e.getName();

    try {
      if (n.equals("argo")) handleArgo(e);
      else if (n.equals("documentation")) handleDocumentation(e);

    }
    catch (Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }


  public void handleEndTag(TXElement e, boolean empty) {
    String n = e.getName();
    try {
      if (n.equals("authorname")) handleAuthorname(e);
      else if (n.equals("version")) handleVersion(e);
      else if (n.equals("description")) handleDescription(e);
      else if (n.equals("searchpath")) handleSearchpath(e);
      else if (n.equals("member")) handleMember(e);
      else if (n.equals("historyfile")) handleHistoryfile(e);
    }
    catch (Exception ex) {
      System.out.println("Exception!");
      ex.printStackTrace();
    }
  }

  protected void handleArgo(TXElement e) {
    /* do nothing */
  }

  protected void handleDocumentation(TXElement e) {
    /* do nothing */
  }


  protected void handleAuthorname(TXElement e) {
    String authorname = e.getText().trim();
    _proj._authorname = authorname;
  }

  protected void handleVersion(TXElement e) {
    String version = e.getText().trim();
    _proj._version = version;
  }

  protected void handleDescription(TXElement e) {
    String description = e.getText().trim();
    _proj._description = description;
  }

  protected void handleSearchpath(TXElement e) {
    String searchpath = e.getAttribute("href").trim();
    _proj.addSearchPath(searchpath);
  }

  protected void handleMember(TXElement e) {
    String name = e.getAttribute("name").trim();
    String type = e.getAttribute("type").trim();
    _proj.addMember(name, type);
  }

  protected void handleHistoryfile(TXElement e) {
    String historyfile = e.getAttribute("name").trim();
    _proj._historyFile = historyfile;
  }

} /* end class ArgoParserIBM */
