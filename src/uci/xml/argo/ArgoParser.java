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

package uci.xml.argo;

import java.util.*;
import java.io.*;
import java.beans.*;
import java.net.URL;

import uci.uml.ui.Project;
import uci.xml.pgml.PGMLParser;
import uci.xml.*;
import uci.gef.Diagram;
import uci.uml.Model_Management.*;

import com.ibm.xml.parser.*;
import org.w3c.dom.*;

public class ArgoParser implements TagHandler {

  ////////////////////////////////////////////////////////////////
  // static variables

  public static ArgoParser SINGLETON = new ArgoParser();

  ////////////////////////////////////////////////////////////////
  // instance variables

  protected Project _proj = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  protected ArgoParser() { }

  ////////////////////////////////////////////////////////////////
  // main parsing methods

  // needs-more-work: should be able to merge an existing project into
  // the current one.

  public synchronized void readProject(URL url) {
    try {
      InputStream is = url.openStream();
      String filename = url.getFile();
      System.out.println("=======================================");
      System.out.println("== READING PROJECT: " + url);
      Parser pc = new Parser(filename);
      pc.setTagHandler(this);
      pc.getEntityHandler().setEntityResolver(DTDEntityResolver.SINGLETON);
      //pc.setProcessExternalDTD(false);
      _proj = new Project(url);
      //_proj.setPathname(pathname);
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
    String n = e.getTagName();
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
    String n = e.getTagName();
    try {
      if (n.equals("authorname")) handleAuthorname(e);
      else if (n.equals("version")) handleVersion(e);
      else if (n.equals("description")) handleDescription(e);
      else if (n.equals("searchpath")) handleSearchpath(e);
      else if (n.equals("member")) handleMember(e);
      else if (n.equals("historyfile")) handleHistoryfile(e);
      else if (n.equals("stat")) handleStat(e);
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

  protected void handleStat(TXElement e) {
    String name = e.getAttribute("name").trim();
    String valueString = e.getAttribute("value").trim();
    int val = Integer.parseInt(valueString);
    //UsageStatistic us = new UsageStatistic(name, val);
    _proj.setStat(name, val);
  }

} /* end class ArgoParser */
