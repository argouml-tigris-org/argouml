// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
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


package uci.uml.util;

import java.util.*;

import uci.util.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Foundation.Extension_Mechanisms.*;
import uci.uml.Behavioral_Elements.State_Machines.*;

public class UMLDescription {

  public static String expand(String desc, Object dm) {
    int searchPos = 0;
    int matchPos = desc.indexOf('{', searchPos);

    if (matchPos == -1) return desc;
    if (!(dm instanceof Element)) return desc;

    // replace all occurances of OFFENDER with the name of the first offender
    while (matchPos != -1) {
      int endExpr = desc.indexOf('}', matchPos + 1);
      String expr = desc.substring(matchPos + 1, endExpr);
      desc = desc.substring(0, matchPos) +
	evaluateOCL(dm, expr) + desc.substring(endExpr + 1);
      searchPos = endExpr;
      matchPos = desc.indexOf('{', searchPos);
    }
    return desc;
  }

  public static String NAME = "name";
  public static String VIS = "visibility";
  public static String MULT = "multiplicity";
  public static String TYPE = "type";
  public static String OWNER = "owner";
  public static String STRUCT = "owner.structuralFeature";
  public static String BEHAV = "owner.behavioralFeature";
  public static String NAMESPACE = "namespace";

  public static String DEFAULT_STR = "(anon)";
  
  // needs-more-work: replace this with an OCL evaluator
  public static String evaluateOCL(Object dm, String expr) {
    //System.out.println("expr = " + expr);
    String res;
    if (NAME.equalsIgnoreCase(expr)) {
      if (!(dm instanceof Element)) return "(Non-Element)";
      Name n = ((Element) dm).getName();
      if (n == null) return DEFAULT_STR;
      res = n.getBody();
      if (res == null || res.length() == 0) return DEFAULT_STR;
      return res;
    }
    if (VIS.equalsIgnoreCase(expr)) {
      if (!(dm instanceof Feature)) return "(non-Feature)";
      VisibilityKind vk = ((Feature) dm).getVisibility();
      if (vk == null) return DEFAULT_STR;
      res = vk.toString();
      if (res == null || res.length() == 0) return DEFAULT_STR;
      return res;
    }
    if (MULT.equalsIgnoreCase(expr)) {
      if (!(dm instanceof StructuralFeature)) return "(non-StructuralFeature)";
      Multiplicity m = ((StructuralFeature) dm).getMultiplicity();
      if (m == null) return DEFAULT_STR;
      res = m.toString();
      if (res == null || res.length() == 0) return DEFAULT_STR;
      return res;
    }
    if (TYPE.equalsIgnoreCase(expr)) {
      if (!(dm instanceof StructuralFeature)) return "(non-StructuralFeature)";
      Classifier type = ((StructuralFeature) dm).getType();
      if (type == null) return DEFAULT_STR;
      res = type.toString();
      if (res == null || res.length() == 0) return DEFAULT_STR;
      return res;
    }
    if (OWNER.equalsIgnoreCase(expr)) {
      if (!(dm instanceof Feature)) return "(non-Feature)";
      Classifier cls = ((Feature) dm).getOwner();
      if (cls == null) return DEFAULT_STR;
      res = cls.getName().getBody();
      if (res == null || res.length() == 0) return DEFAULT_STR;
      return res;
    }
    if (STRUCT.equalsIgnoreCase(expr)) {
      if (!(dm instanceof Feature)) return "(non-Feature)";
      Classifier cls = ((Feature) dm).getOwner();
      if (cls == null) return DEFAULT_STR;
      res = "";
      Vector str = cls.getStructuralFeature();
      java.util.Enumeration enum = str.elements();
      while (enum.hasMoreElements()) {
	StructuralFeature sf = (StructuralFeature) enum.nextElement();
	res += sf.getName().getBody();
	if (enum.hasMoreElements()) res += ", ";
      }
      if (res == null || res.length() == 0) return DEFAULT_STR;
      return res;
    }
    if (BEHAV.equalsIgnoreCase(expr)) {
      if (!(dm instanceof Feature)) return "(non-Feature)";
      Classifier cls = ((Feature) dm).getOwner();
      if (cls == null) return DEFAULT_STR;
      res = "";
      Vector beh = cls.getBehavioralFeature();
      java.util.Enumeration enum = beh.elements();
      while (enum.hasMoreElements()) {
	BehavioralFeature bf = (BehavioralFeature) enum.nextElement();
	res += bf.getName().getBody();
	if (enum.hasMoreElements()) res += ", ";
      }
      if (res == null || res.length() == 0) return DEFAULT_STR;
      return res;
    }
    if (NAMESPACE.equalsIgnoreCase(expr)) {
      if (!(dm instanceof ModelElement)) return "(Non-ModelElement)";
      Namespace ns = ((ModelElement) dm).getNamespace();
      if (ns == null) return DEFAULT_STR;
      Name n = ns.getName();
      if (n == null) return DEFAULT_STR;
      res = n.getBody();
      if (res == null || res.length() == 0) return DEFAULT_STR;
      return res;
    }
    return "(invalid expression: " + expr + ")";
  }
  
  
} /* end class UMLDescription */

