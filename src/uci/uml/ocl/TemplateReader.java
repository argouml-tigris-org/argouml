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

package uci.uml.ocl;

import java.util.*;
import java.io.*;
import com.ibm.xml.parser.*;

public class TemplateReader implements com.ibm.xml.parser.ElementHandler {
  ////////////////////////////////////////////////////////////////
  // static variables
  public final static TemplateReader SINGLETON = new TemplateReader();

  Hashtable _templates;  /* Class -> Vector of TemplateRecord */
  Vector _macros;

  ////////////////////////////////////////////////////////////////
  // constructors
  protected TemplateReader() { }

  ////////////////////////////////////////////////////////////////
  // static methods
  public static Hashtable readFile(String fileName) {
    return SINGLETON.read(fileName);
  }

  ////////////////////////////////////////////////////////////////
  // reading methods
  public Hashtable read(String fileName) {
    InputStream in = null;
    try { in = TemplateReader.class.getResourceAsStream(fileName); }
    catch (Exception ex) { return null; }
    if (in == null) return null;

    _templates = new Hashtable();
    _macros = new Vector();
    Parser pc = new Parser(fileName);
    pc.addElementHandler(this, "template");
    pc.addElementHandler(this, "macro");
    try { pc.readStream(in); } //new FileInputStream(fileName)); }
    catch (Exception ex) { System.out.println("Exception"); }
    return _templates;
  }

  ////////////////////////////////////////////////////////////////
  // ElementHandler implementation
  public TXElement handleElement(TXElement e) {
    if (e.getTagName().equals("template")) {
      String body = e.getText().trim();
      String guard = e.getAttribute("guard");
      String className = e.getAttribute("class");
      java.lang.Class classObj = null;
      try { classObj = Class.forName(className); }
      catch (Exception ex) {
	System.out.println("TemplateReader: Class " + className + " not found");
	return null;
      }
      body = expandMacros(body);
      TemplateRecord rec = new TemplateRecord(classObj, guard, body);
      Vector existing = (Vector) _templates.get(classObj);
      if (existing == null) existing = new Vector();
      existing.addElement(rec);
      _templates.put(classObj, existing);
      //     System.out.println("read template for " + "[" + classObj + "]");
      //     System.out.println("[" + body + "]\n\n");
    }
    else if (e.getTagName().equals("macro")) {
      String body = e.getText().trim();
      String name = e.getAttribute("name");
      if (name == null) return null;
      int newNameLength = name.length();
      body = expandMacros(body);
      MacroRecord mr = new MacroRecord(name, body);
      boolean inserted = false;
      int size = _macros.size();
      for (int i = 0; i < size && !inserted; i++) {
	String n = ((MacroRecord)_macros.elementAt(i)).name;
	if (n.length() < newNameLength) {
	  _macros.insertElementAt(mr, i);
	  inserted = true;
	}
      }
      if (!inserted) _macros.addElement(mr);
    }
    else
      System.out.println("unknown tag: " + e.getTagName());
    return null;
  }

  public String expandMacros(String body) {
    StringBuffer resultBuffer = new StringBuffer(body.length()*2);
    StringTokenizer st = new StringTokenizer(body, "\n\r");
    while (st.hasMoreElements()) {
      String line = st.nextToken();
      String expanded = expandMacrosOnOneLine(line);
      resultBuffer.append(expanded);
      resultBuffer.append("\n");
    }
    return resultBuffer.toString();
  }

  /** each line can have at most one macro */
  public String expandMacrosOnOneLine(String body) {
    int numMacros = _macros.size();
    for (int i=0; i < numMacros; i++) {
      String k = ((MacroRecord)_macros.elementAt(i)).name;
      int findIndex = body.indexOf(k);
      if (findIndex != -1) {
	String mac = ((MacroRecord)_macros.elementAt(i)).body;
	StringBuffer resultBuffer;
	String prefix = body.substring(0, findIndex);
	String suffix = body.substring(findIndex + k.length());
	resultBuffer = new StringBuffer(mac.length() +
					(prefix.length() + suffix.length())*10);
	StringTokenizer st = new StringTokenizer(mac, "\n\r");
	while (st.hasMoreElements()) {
	  resultBuffer.append(prefix);
	  resultBuffer.append(st.nextToken());
	  resultBuffer.append(suffix);
	  if (st.hasMoreElements()) resultBuffer.append("\n");
	}
	return resultBuffer.toString();
      }
    }
    return body;
  }

} /* end class TemplateReader */


class MacroRecord {
  String name;
  String body;
  MacroRecord(String n, String b) {
    name = n;
    body = b;
  }
} /* end class MacroRecord */


