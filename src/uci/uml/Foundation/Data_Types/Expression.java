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

package uci.uml.Foundation.Data_Types;

import java.util.*;
import uci.uml.Foundation.Core.ElementImpl;

public class Expression implements java.io.Serializable {
  public static final Name UNSPEC = new Name("Unspecified");
  public static final Name JAVA = new Name("Java");
  public static final Name C = new Name("C/C++");
  public static final Name OCL = new Name("OCL");
  public static final Name[] POSSIBLE_LANGUAGES = {
    UNSPEC, JAVA, C, OCL };

  public Name _language = UNSPEC;
  public Uninterpreted _body;

  String elementID = newElementID();

  public static String newElementID() {
    return ElementImpl.newElementID();
  }

  public Vector getNamedProperty(String propName) {
    Class voidArray[] = {};
    Object objArray[] = {};
    java.lang.reflect.Method methodToCall = null;
    Vector returnVector = new Vector();
    String realName = null;

    try {
      realName = "get" + propName.substring(0,1).toUpperCase() + propName.substring(1, propName.length());
      methodToCall = this.getClass().getMethod(realName, voidArray); 
    } catch (NoSuchMethodException ne) {
      System.err.println("NO method (" + realName + ") matched in getNamedProperty!");
    }

    try {
    returnVector.addElement(methodToCall.invoke(this, objArray));
    
    if (returnVector.firstElement() instanceof Vector) returnVector = (Vector)(returnVector.firstElement());

    } catch (Exception e) {
      System.err.println("Not happy with invoke!");
    }
    
    return returnVector;
  } 

  public String getId() { return "argo" + elementID; }
 
  public Expression() { }
  public Expression(Uninterpreted body) { setBody(body); }
  public Expression(Name lang, Uninterpreted body) {
    setLanguage(lang);
    setBody(body);
  }
  public Expression(String bodyStr) { setBody(bodyStr); }
  public Expression(String langStr, String bodyStr) {
    setLanguage(langStr);
    setBody(bodyStr);
  }

  public Name getLanguage() { return _language; }
  public void setLanguage(Name x) { _language = x; }
  public void setLanguage(String langStr) { _language = new Name(langStr); }
  
  public Uninterpreted getBody() { return _body; }
  public void setBody(Uninterpreted x) {
    _body = x;
  }

  public void setBody(String x) {
    setBody(new Uninterpreted(x));
  }

  ////////////////////////////////////////////////////////////////
  // debugging
  public String dbgString() {
    String s = getOCLTypeStr();
    if (_body != null) s += " " + _body.getBody();
    return s;
  }


  public String getOCLTypeStr() {
    String javaClassName = getClass().getName();
    int dotIndex = javaClassName.lastIndexOf(".");
    String OCLTypeString = javaClassName.substring(dotIndex+1);
    if (OCLTypeString.equals("MMAction")) return "Action";
    if (OCLTypeString.equals("MMClass")) return "Class";
    if (OCLTypeString.equals("MMException")) return "Exception";
    return OCLTypeString;
  }

  static final long serialVersionUID = -933319327759446691L;
}
