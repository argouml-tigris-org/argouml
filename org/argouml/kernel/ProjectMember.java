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

import java.net.*;
import java.io.*;
import java.util.*;
import java.beans.*;

public abstract class ProjectMember {

  ////////////////////////////////////////////////////////////////
  // instance varables

  //protected String _name;
  private String _name;
  protected Project _project = null;

  ////////////////////////////////////////////////////////////////
  // constructors

  public ProjectMember(String name, Project project) {
    _project = project;
    setName(name);
  }


  ////////////////////////////////////////////////////////////////
  // accessors


  /**
   * In contrast to {@link #getName} returns the member's name without the
   * prepended name of the project. This is the name that
   * {@link Project#findMemberByName} goes by.
   *
   * @author Steffen Zschaler
   */
  public String getPlainName() {
    String s = _name;
    
    if (s != null) {
      if (! s.endsWith (getFileExtension())) {
        s += getFileExtension();
      }
    }
    
    return s;
  }

  /**
   * In contrast to {@link #getPlainName} returns the member's name including the
   * project's base name. The project's base name is prepended followed by an
   * underscore '_'.
   */
  public String getName() {
    String s = _name;
    
    if (s != null) {
      s = _project.getBaseName() + "_" + s;
    
      if (! s.endsWith (getFileExtension())) {
        s += getFileExtension();
      }
    }
    
    return s;
  }
  
  public void setName(String s) { 
    if ((s != null) &&
        (s.startsWith (_project.getBaseName()))) {
      //System.out.println ("Setting project member name excluding project base name...");
      _name = s.substring (_project.getBaseName().length());
    }
    else {
      //System.out.println ("Setting project member name including project base name...");
      _name = s;
    }
  }

  public Project getProject() { return _project; }

  public abstract String getType();
  public abstract String getFileExtension();

  public URL getURL() {
    return getProject().findMemberURLInSearchPath(getName());
//     try {
//       String base = getProject().getURL().toString();
//       base = base.substring(0, base.lastIndexOf("/") + 1);
//       if (!base.endsWith("/")) base += "/";
//       return new URL(base + _name);
//     }
//     catch (MalformedURLException murle) {
//       System.out.println("bad url in ProjectMember");
//       return null;
//     }
  }

  ////////////////////////////////////////////////////////////////
  // actions

  public abstract void load() throws IOException, org.xml.sax.SAXException;
  public abstract void save(String path, boolean overwrite) throws Exception;
  public abstract void save(String path, boolean overwrite, Writer writer) throws Exception;

} /* end class ProjectMember */

//   public Object getMember() { return member; }
//   public void setMember(Object m) {
//     name = null;
//     member = m;
//   }



//   public String getPathname() {
//     //needs-more-work: ignoring search path for now
//     return proj.getPathname();
//   }


//   protected static String stripJunk(String s) {
//     String res = "";
//     int len = s.length();
//     for (int i = 0; i < len; i++) {
//       char c = s.charAt(i);
//       if (Character.isJavaIdentifierPart(c)) res += c;
//     }
//     return res;
//   }

//   public String getName() {
//     if (name != null) return name;
//     String n = null;
//     if (type.equalsIgnoreCase("pgml"))
//       n = stripJunk(proj.getBaseName()) + "_" +
// 	stripJunk(((Diagram)member).getName()) + ".pgml";
//     if (type.equalsIgnoreCase("xmi"))
//       n = stripJunk(proj.getBaseName()) + ".xmi";
//     //needs-more-work other cases
//     return n;
//   }
//   public void setName(String s) { name = s; }
//   public String getType() { return type; }
//   public void setType(String s) { type = s; }


//   public void save(boolean overwrite) {
//     String path = getPathname();
//     String filename = getName();
//     if ("pgml".equalsIgnoreCase(type)) {
//       //System.out.println("save diagram:" + filename);
//       Hashtable templates = TemplateReader.readFile("/uci/dtd/PGML.tee");
//       OCLExpander expander = new OCLExpander(templates);

//       try {
// 	Diagram d = (Diagram) member;
// 	if (filename != null) {
// 	  System.out.println("Writing " + path + filename + "...");
// 	  Globals.showStatus("Writing " + path + filename + "...");
// 	  File f = new File(path + filename);
// 	  if (f.exists() && !overwrite) {
// 	    System.out.println("Are you sure you want to overwrite " +
// 			       path + filename + "?");
// 	  }
// 	  FileWriter fw = new FileWriter(f);
// 	  expander.expand(fw, d, "", "");
// 	  System.out.println("Wrote " + path + filename);
// 	  Globals.showStatus("Wrote " + path + filename);
// 	  // needs-more-work: progress bar in ProjectBrowser
// 	  fw.close();
// 	}
//       }
//       catch (FileNotFoundException ignore) {
// 	System.out.println("got an FileNotFoundException");
//       }
//       catch (IOException ignore) {
// 	System.out.println("got an IOException");
// 	ignore.printStackTrace();

//       }
//     }
//     else if ("xmi".equalsIgnoreCase(type)) {
//       //System.out.println("save model:" + filename);

//       //@@@: just for rapid edig-compile-debug 
//       Hashtable templates = TemplateReader.readFile("/uci/dtd/XMI.tee");
//       OCLExpander expander = new OCLExpander(templates);

//       try {
// 	System.out.println("Writing " + path + filename + "...");
// 	Globals.showStatus("Writing " + path + filename + "...");
// 	FileWriter fw = new FileWriter(path + filename);
// 	expander.expand(fw, proj, "", "");
// 	fw.close();
// 	System.out.println("Wrote " + path + filename);
// 	Globals.showStatus("Wrote " + path + filename);
//       }
//       catch (FileNotFoundException ignore) {
// 	System.out.println("got an FileNotFoundException");
//       }
//       //       catch (PropertyVetoException ignore) {
//       // 	System.out.println("got an PropertyVetoException in Save XMI");
//       //       }
//       //    catch (java.lang.ClassMismatchException ignore) {
//       //      System.out.println("got an ClassMismatchException");
//       //    }
//       catch (IOException ignore) {
// 	System.out.println("got an IOException");
// 	ignore.printStackTrace();
//       }
//     }
//     else if ("text".equalsIgnoreCase(type)) {
//       System.out.println("save text file:" + filename);
//     }
//     else if ("html".equalsIgnoreCase(type)) {
//       System.out.println("save html file:" + filename);
//     }
//     else if ("other".equalsIgnoreCase(type)) {
//       System.out.println("save other file:" + filename);
//     }
//     else if ("argo".equalsIgnoreCase(type)) {
//       System.out.println("save nested project?:" + filename);
//     }
//   }



//   public void load() {
//     //needs-more-work: should be done with subclasses?
//     String path = getPathname();
//     //System.out.println("search path found: " + path);
//     if ("pgml".equalsIgnoreCase(type)) {
//       System.out.println("Reading " + path + name + "...");
//       //System.out.println("_idRegistry size=" + proj.getIDRegistry().size());
//       PGMLParserIBM.SINGLETON.setOwnerRegistery(proj.getIDRegistry());
//       Diagram d = PGMLParserIBM.SINGLETON.readDiagram(path, name);
//       setMember(d);
//       try { proj.addDiagram(d); }
//       catch (PropertyVetoException pve) { }
//     }
//     else if ("xmi".equalsIgnoreCase(type)) {
//       System.out.println("Reading " + path + name + "...");
//       XMIParserIBM.SINGLETON.setIDs(proj.getIDRegistry());
//       XMIParserIBM.SINGLETON.setProject(proj);
//       XMIParserIBM.SINGLETON.readModels(path, name);
//       //System.out.println("_idRegistry size=" + proj.getIDRegistry().size());
//       setName(null); //needs-more-work: awkward!
//     }
//     else if ("text".equalsIgnoreCase(type)) {
//       System.out.println("load text file");
//     }
//     else if ("html".equalsIgnoreCase(type)) {
//       System.out.println("load html file");
//     }
//     else if ("other".equalsIgnoreCase(type)) {
//       System.out.println("load other file");
//     }
//     else if ("argo".equalsIgnoreCase(type)) {
//       System.out.println("load nested project?");
//     }
//   }

//   public String name = "untitled";
//   public String type = "xmi";
//   public Object member = null;

//   public ProjectMember(String n, String t, Project p) {
//     name = n;
//     type = t;
//     proj = p;
//   }
