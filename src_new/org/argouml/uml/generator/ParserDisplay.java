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

// File: ParserDisplay.java
// Classes: ParserDisplay
// Original Author:
// $Id$

// 12 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// extension points.


package org.argouml.uml.generator;

import java.beans.*;
import java.util.*;

import javax.swing.plaf.multi.MultiButtonUI;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.MMultiplicity;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.foundation.data_types.MActionExpression;
import ru.novosoft.uml.foundation.data_types.MBooleanExpression;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.graph.*;

import org.argouml.kernel.Project;
import org.argouml.model.uml.UmlFactory;
import org.argouml.ui.ProjectBrowser;
import org.argouml.uml.MMUtil;
import org.argouml.uml.diagram.static_structure.*;
import org.argouml.uml.diagram.deployment.*;
import org.apache.log4j.Category;
import org.argouml.application.api.*;

public class ParserDisplay extends Parser {

  public static ParserDisplay SINGLETON = new ParserDisplay();
  /**
   * The standard error etc. logger
   */
  protected static final Category _cat = 
    Category.getInstance(ParserDisplay.class);

  ////////////////////////////////////////////////////////////////
  // parsing methods

    /**
     * <p>Parse an extension point.<p>
     *
     * <p>The syntax is "name: location", "name:", "location" or "". The
     *   fields of the extension point are updated appropriately.</p>
     *
     * @param useCase  The use case that owns this extension point
     *
     * @param ep       The extension point concerned
     *
     * @param text     The text to parse
     *
     */

    public void parseExtensionPointFig(MUseCase useCase, MExtensionPoint ep,
                                       String text) {

        // We can do nothing if we don't have both the use case and extension
        // point.

        if ((useCase == null) || (ep == null)) {
            return;
        }

        // Parse the string to creat a new extension point.

        MExtensionPoint newEp = parseExtensionPoint(text);

        // If we got back null we interpret this as meaning delete the
        // reference to the extension point from the use case, otherwise we set
        // the fields of the extension point to the values in newEp.

        if (newEp == null) {
            useCase.removeExtensionPoint(ep);
        }
        else {
            ep.setName(newEp.getName());
            ep.setLocation(newEp.getLocation());
        }
    }

/* not used ?
  public void parseOperationCompartment(MClassifier cls, String s) {
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, "\n\r");
    Vector newOps = new Vector();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      MOperation op = parseOperation(token);
      newOps.add(op);
    }
    // System.out.println("parsed " + newOps.size() + " operations");
	Vector features = new Vector(cls.getFeatures());
	Vector oldOps = new Vector(MMUtil.SINGLETON.getOperations(cls));
	features.removeAll(oldOps);

	// don't forget to remove old Operations!
	for (int i = 0; i < oldOps.size(); i++)
		cls.removeFeature((MOperation)oldOps.elementAt(i));

	// now re-set the attributes
	cls.setFeatures(features);

	//features.addAll(newOps);
	//add features with add-Operation, so a role-added-event is generated
	for (int i=0; i<newOps.size(); i++){
	    MOperation oper=(MOperation)newOps.elementAt(i);
	    cls.addFeature(oper);
        cls.getModel();
        oper.getModel();
        MMUtil.SINGLETON.getReturnParameter(oper).getModel();
        MMUtil.SINGLETON.getReturnParameter(oper).getType().getModel();  
	}
  }
*/

  public void parseOperationFig(MClassifier cls, MOperation op, String text) {
    if (cls == null || op == null)
      return;
    StringTokenizer st = new StringTokenizer(text,";");
    String s = st.hasMoreTokens() ? st.nextToken().trim() : null;
    int i = cls.getFeatures().indexOf(op);
    if (s != null && s.length() > 0) {
      MOperation newOp = parseOperation(s);
      if (newOp != null) {
        if (MMUtil.SINGLETON.getReturnParameter(newOp).getType().getModel() == null) {
            MNamespace model = null;
            if (cls.getModel() != null) {
                model = cls.getModel();
            } else {
                // somewhere there has been a misstake.
                // lets try to repair it
                model = ProjectBrowser.TheInstance.getProject().getModel();
                model.addOwnedElement(cls);
                // but better tell the developer (and maybe the user if he watches)
                _cat.error("The MClass " + cls.toString() + " returned null on getModel!");
            }
            model.addOwnedElement(MMUtil.SINGLETON.getReturnParameter(newOp).getType()); 
        }   
        newOp.setAbstract(op.isAbstract());
        newOp.setOwnerScope(op.getOwnerScope());
        if (i != -1) {
          cls.removeFeature(i);
          cls.addFeature(i,newOp);
        } else {
          cls.addFeature(newOp);
        }
      }
    } else {
      cls.removeFeature(i);
    }
    // more operations entered:
    while (st.hasMoreTokens()) {
      s = st.nextToken().trim();
      if (s != null && s.length() > 0) {
        MOperation newOp = parseOperation(s);
        if (newOp != null) {
          newOp.setAbstract(op.isAbstract());
          newOp.setOwnerScope(op.getOwnerScope());
          if (i != -1) {
            cls.addFeature(++i,newOp);
          } else {
            cls.addFeature(newOp);
          }
        }
      }
    }
  }

  public void parseAttributeCompartment(MClassifier cls, String s) {
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, "\n\r");
    Vector newAttrs = new Vector();
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      MAttribute attr = parseAttribute(token);
      newAttrs.add(attr);
    }
    // System.out.println("parsed " + newAttrs.size() + " attributes");
	Vector features = new Vector(cls.getFeatures());
	Vector oldAttrs = new Vector(MMUtil.SINGLETON.getAttributes(cls));
	features.removeAll(oldAttrs);

	// don't forget to remove old Attrbutes!
        oldAttrs.clear();

	// now re-set the operations
	cls.setFeatures(features);

	//features.addAll(newAttrs);
	//add features with add-Operation, so a role-added-event is generated
	for (int i=0; i<newAttrs.size(); i++){
	    MAttribute attr=(MAttribute)newAttrs.elementAt(i);
	    cls.addFeature(attr);
	}

  }

  public void parseAttributeFig(MClassifier cls, MAttribute at, String text) {
    if (cls == null || at == null)
      return;
    StringTokenizer st = new StringTokenizer(text,";");
    String s = st.hasMoreTokens() ? st.nextToken().trim() : null;
    int i = cls.getFeatures().indexOf(at);
    if (s != null && s.length() > 0) {
      MAttribute newAt = parseAttribute(s);
      if (newAt != null) {
        newAt.setOwnerScope(at.getOwnerScope());
        if (i != -1) {
          cls.removeFeature(i);
          cls.addFeature(i,newAt);
        } else {
          cls.addFeature(newAt);
        }
      }
    } else {
      cls.removeFeature(i);
    }
    // more attributes entered:
    while (st.hasMoreTokens()) {
      s = st.nextToken().trim();
      if (s != null && s.length() > 0) {
        MAttribute newAt = parseAttribute(s);
        if (newAt != null) {
          newAt.setOwnerScope(at.getOwnerScope());
          if (i != -1) {
            cls.addFeature(++i,newAt);
          } else {
            cls.addFeature(newAt);
          }
        }
      }
    }
  }

    /**
     * <p>Parse a string representing an extension point and return a new
     *   extension point.</p>
     *
     * <p>The syntax is "name: location", "name:", "location" or
     *   "". <em>Note</em>. If either field is blank, it will be set to null in
     *   the extension point.</p>
     *
     * <p>We break up the string into tokens at the ":". We must keep the ":"
     *   as a token, so we can distinguish between "name:" and "location". The
     *   number of tokens will distinguish our four cases.</p>
     *
     * @param text  The string to parse
     *
     * @return      A new extension point, with fields set appropriately, or
     *              <code>null</code> if we are given <code>null</code> or a
     *              blank string. <em>Note</em>. The string ":" can be used to
     *              set both name and location to null.
     */

    public MExtensionPoint parseExtensionPoint(String text) {

        // If we are given the null string, return immediately

        if (text == null) {
            return null;
        }

        // Build a new extension point

        MExtensionPoint ep = MMUtil.SINGLETON.buildExtensionPoint(null);

        StringTokenizer st        = new StringTokenizer(text.trim(), ":",
                                                        true);
        int             numTokens = st.countTokens();

        String epLocation;
        String colon;
        String epName;

        switch (numTokens) {

        case 0:

            // The empty string. Return null

            ep = null;

            break;

        case 1:

            // A string of the form "location". This will be confused by the
            // string ":", so we pick this out as an instruction to clear both
            // name and location.

            epLocation = st.nextToken().trim();

            if (epLocation.equals(":")) {
                ep.setName(null);
                ep.setLocation(null);
            }
            else {
                ep.setName(null);
                ep.setLocation(epLocation);
            }

            break;

        case 2:

            // A string of the form "name:"

            epName = st.nextToken().trim();

            ep.setName(epName);
            ep.setLocation(null);

            break;

        case 3:

            // A string of the form "name:location". Discard the middle token
            // (":")

            epName     = st.nextToken().trim();
            colon      = st.nextToken();
            epLocation = st.nextToken().trim();

            ep.setName(epName);
            ep.setLocation(epLocation);

            break;
        }

        return ep;
    }


  /** Parse a line of the form:
   *  visibility name (parameter list) : return-type-expression {property-string}
   * Same as in UML 1.3 spec. 
   */
   /* (formerly: [visibility] [keywords] returntype name(params)[;] ) */
  public MOperation parseOperation(String s) {
    s = s.trim();
    if (s.endsWith(";")) s = s.substring(0, s.length()-1);
    MOperation res = MMUtil.SINGLETON.buildOperation();
    s = parseOutVisibility(res, s);
    // s = parseOutKeywords(res, s);
    s = parseOutName(res, s);
    s = parseOutParams(res, s);
    s = parseOutReturnType(res, s);
    s = parseOutProperties(res, s);
    s = s.trim();
    if (s.length() > 2)
      System.out.println("leftover in parseOperation=|" + s + "|");
    return res;
  }


/**
 * Parses a string for multiplicity and sets the multiplicity with the given 
 * attribute.
 * @param f
 * @param s
 * @return String
 */
   protected String parseOutMultiplicity(MAttribute f, String s) {
    s = s.trim();
    String multiString = "";
    int colonappereance = s.indexOf(':');
    int equalappereance = s.indexOf('=');
    int braceappereance = s.indexOf('{');
    if (colonappereance < 0) { // no type def
        if (equalappereance < 0) { // no initial value
            if (braceappereance < 0) { // no property string
                multiString = s;
            } else {
                multiString = s.substring(0, braceappereance-1).trim();
            }
        } else {
            multiString = s.substring(0, equalappereance-1).trim();
        }
    } else {
        multiString = s.substring(0, colonappereance-1).trim();
    }
    f.setMultiplicity(UmlFactory.getFactory().getDataTypes().createMultiplicity(multiString));
 
    return s.substring(multiString.length(), s.length());
   }
   
   /** Parse a line of the form:
    * visibility name [multiplicity] : type-expression = initial-value {property-string}
    * Same as in the UML 1.3 spec. 
    */
   /* (formerly: [visibility] [keywords] type name [= init] [;] ) */
  public MAttribute parseAttribute(String s) {
      s = s.trim();

      if (s.endsWith(";")) {
          s = s.substring(0, s.length()-1);
      }
      MAttribute newAttribute = MMUtil.SINGLETON.buildAttribute();
      s = parseOutVisibility(newAttribute, s);
      s = parseOutName(newAttribute, s);
      s = parseOutMultiplicity(newAttribute, s);
      // s = parseOutColon(s);
      s = parseOutType(newAttribute, s);
      s = parseOutInitValue(newAttribute, s);
      s = parseOutProperties(newAttribute, s);
      if (s.length() > 2){
          System.out.println("Value left over in parseAttribute=|" + s + "|");
      }
      return newAttribute;
  }
  
/**
 * Parses the properties for some attribute a out of a string s. The properties 
 * are all keywords between the braces at the end of a string notation of an 
 * attribute.
 * @param a
 * @param s
 * @return String
 */
  protected String parseOutProperties(MAttribute a, String s) {
    s = s.trim();
    if (s.indexOf("{") >= 0) {
        StringTokenizer tokenizer = new StringTokenizer(
            s.substring(s.indexOf("{")+1, s.indexOf("}")), ",");
        List properties = new ArrayList();
        while (tokenizer.hasMoreElements()) {
            properties.add(tokenizer.nextToken().trim());
        }
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).equals("frozen")) {
                a.setChangeability(MChangeableKind.FROZEN);
            } else {
                String propertyStr = (String)properties.get(i);
                String tagStr = "";
                String valueStr = "";
                if (propertyStr.indexOf("=")>=0) {
                    tagStr = propertyStr.substring(0, propertyStr.indexOf("=")-1);
                    valueStr = propertyStr.substring(propertyStr.indexOf("="+1, propertyStr.length()));
                }
                MTaggedValue tag = UmlFactory.getFactory().getExtensionMechanisms().createTaggedValue();
 
                tag.setTag(tagStr);
                tag.setValue(valueStr);
                a.addTaggedValue(tag);
            }
        }
        return s.substring(s.indexOf("}"), s.length());
    }          
    return s;
  }
  
  protected String parseOutProperties(MOperation op, String s) {
    s = s.trim();
    if (s.indexOf("{") >= 0) {
        StringTokenizer tokenizer = new StringTokenizer(
            s.substring(s.indexOf("{")+1, s.indexOf("}")), ",");
        List properties = new ArrayList();
        while (tokenizer.hasMoreElements()) {
            properties.add(tokenizer.nextToken().trim());
        }
        for (int i = 0; i < properties.size(); i++) {
            if (properties.get(i).equals("query")) {
                op.setQuery(true);
            } else {
                if (properties.get(i).equals("sequential") || 
                    properties.get(i).equals("concurrency=sequential")) {
                        op.setConcurrency(MCallConcurrencyKind.SEQUENTIAL);
                } else {
                    if (properties.get(i).equals("guarded") ||
                        properties.get(i).equals("concurrency=guarded")) {
                        op.setConcurrency(MCallConcurrencyKind.GUARDED);
                    } else {
                        if (properties.get(i).equals("concurrent") || 
                            properties.get(i).equals("concurrency=concurrent")) {
                                op.setConcurrency(MCallConcurrencyKind.CONCURRENT);
                        } else {
                            String propertyStr = (String)properties.get(i);
                            String tagStr = "";
                            String valueStr = "";
                            if (propertyStr.indexOf("=")>=0) {
                                tagStr = propertyStr.substring(0, propertyStr.indexOf("=")-1);
                                valueStr = propertyStr.substring(propertyStr.indexOf("="+1, propertyStr.length()));
                            }
                            MTaggedValue tag = UmlFactory.getFactory().getExtensionMechanisms().createTaggedValue();
                            tag.setTag(tagStr);
                            tag.setValue(valueStr);
                            op.addTaggedValue(tag);
                        }
                    }
                }
            }
        }
        return s.substring(s.indexOf("}"), s.length());
    }          
    return s;
  }
  /*
  public String parseOutMultiplicity(MFeature f, String s) {

    s = s.trim();
    MMultiplicity multi = UmlFactory.getFactory().getDataTypes().createMultiplicity();
    boolean startMulti = false; // start of a multiplicity
    boolean inRange = false;    // true if we are in a range
    boolean formerNumber = false;   // true if last char was a number
    boolean inMultiString = true;  // true if we are still parsing a multi
                                     // string\
    boolean inDots = false      // true if we are in ..
    int dotCounter = 0;          // number of dots we passed
    StringBuffer startMultiSb = new StringBuffer();
    StringBuffer endMultiSb = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        switch (c) {
            case '0' : case '1' : case '2' : case '3' :
            case '4' : case '5' : case '6' : case '7' :
            case '8' : case '9' : case '*' :
                if (!inRange && inMultiString) { // we start possibly a range
                    startMulti = true;
                    startMultiSb.append(c);
                }
                if (inRange && inMultiString) { // end range
                    if (inDots) {               // first char of endrange
                        inDots = false;
                        endMultiSb = new StringBuffer();
                    }
                    endMultiSb.append(c);
                }
                break;
            case '.' :  
                if (!inDots && inMultiString) {
                    inDots = true;
                    dotCounter = 0;
                }
                dotCounter++; 
                break;
            case ',' :
                if (inMultiString) { // we have an end sign of a mutiplicity
                    
                break;
            case ' ' :
                break;
            default : // some other character
                inMultiString = false; //only here if we stopped parsing the
                                        // multistring
                break;
        }
  }
*/

/**
 * Parses a string for visibilitykind. Visibilitykind can both be specified 
 * using the standard #, +, - and the keywords public, private, protected.
 * @param f The feature the visibility is part of
 * @param s The string that possibly identifies some visibility
 * @return String The string s WITHOUT the visibility signs. 
 */
  public String parseOutVisibility(MFeature f, String s) {
    s = s.trim();
    // We only support UML 1.3 notation in this parser
    // get the first char
    String visStr = s.substring(0, 1);
    if (visStr.equals("#")) {
        f.setVisibility(MVisibilityKind.PROTECTED);
        return s.substring(1, s.length());
    } else {
        if (visStr.equals("-")) {
            f.setVisibility(MVisibilityKind.PRIVATE);
            return s.substring(1, s.length());
        } else {
            if (visStr.equals("+")) {
                f.setVisibility(MVisibilityKind.PUBLIC);
                return s.substring(1, s.length());
            } 
        }
    }
    // public, private, protected as keyword
    int firstSpace = s.indexOf(' ');
    visStr = s.substring(0, firstSpace);
    if (visStr.equals("public")) {
        f.setVisibility(MVisibilityKind.PUBLIC);
        return s.substring(firstSpace, s.length());
    } else {
        if (visStr.equals("protected")) {
            f.setVisibility(MVisibilityKind.PROTECTED);
            return s.substring(firstSpace, s.length());
        } else {
            if (visStr.equals("private")) {
                f.setVisibility(MVisibilityKind.PRIVATE);
                return s.substring(firstSpace, s.length());
            } else {
                return s;
            }
        }
    }
    
  }

/*
 * removed next method since it is obsolete. We now use the standard notation
 * for UML 1.3 as defined in the spec.
  public String parseOutKeywords(MFeature f, String s) {
    s = s.trim();
    int firstSpace = s.indexOf(" ");
    if (firstSpace == -1) return s;
    String visStr = s.substring(0, firstSpace);

      if (visStr.equals("static"))
	f.setOwnerScope(MScopeKind.CLASSIFIER);
      else if (visStr.equals("synchronized") && (f instanceof MOperation))
	((MOperation)f).setConcurrency(MCallConcurrencyKind.GUARDED);
      else if (visStr.equals("transient"))
	System.out.println("'transient' keyword is currently ignored");
      else if (visStr.equals("final"))
	System.out.println("'final' keyword is currently ignored");
      else if (visStr.equals("abstract"))
	System.out.println("'abstract' keyword is currently ignored");
      else {
	return s;
      }

    return parseOutKeywords(f, s.substring(firstSpace+1));
  }
*/

    /**
     * Parses the parameters with an operation. The string containing the 
     * parameters must be the first string within the given string s. It must 
     * start with ( and the end of the string containing the parameters is ).
     * @param op
     * @param s
     * @return String
     */
	public String parseOutParams(MOperation op, String s) {
		s = s.trim();
		String leftOver = s;
		int end = s.lastIndexOf(")");
		if (end != -1) {
			java.util.StringTokenizer st = new java.util.StringTokenizer(s.substring(1,end), ",");
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				MParameter p = parseParameter(token);
				if (p != null) op.addParameter(p);
			}
			leftOver = s.substring(end+1);
		}
		return leftOver;
	}

/**
 * Parses the name of modelelement me from some input string s. The name must be
 * the first word of the string. 
 * @param me
 * @param s
 * @return String
 */
  public String parseOutName(MModelElement me, String s) {
    s = s.trim();
    if (s.equals("") || s.charAt(0) == '=') { //duh there is no name
        if (me.getName() == null || me.getName().equals("")) {
            me.setName("anno");
        } 
        return s;
    }
    // next sign can be: ' ', '=', ':', '{', '}', '\n', '[', ']', '(', ')'
    // any of these indicate that name is to an end.    
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, ": \t()[]{}=;");
    if (!st.hasMoreTokens()) {
      System.out.println("name not parsed");
      return s;
    }
    String nameStr = st.nextToken();

    // needs-more-work: wasteful
     me.setName(nameStr);

    int namePos = s.indexOf(nameStr);
    return s.substring(namePos + nameStr.length());
  }
    
    /**
     * Parses the user given string s for the type of an attribute. The string 
     * should start with :. The part between : and { (if there are properties) 
     * or the end of the string if there are no properties.
     * @param attr
     * @param s
     * @return String
     */
	public String parseOutType(MAttribute attr, String s) {
        s = s.trim();       
        if (s.startsWith(":")) { // we got ourselves a type expression
            MClassifier type = null;
            s = s.substring(1, s.length()).trim();
            String typeExpr = "";
            StringTokenizer tokenizer = new StringTokenizer(s, " ={");
            typeExpr = tokenizer.nextToken();
            Project p = ProjectBrowser.TheInstance.getProject();
            type = p.findType(typeExpr);
            if (type == null) { // no type defined yet
                type = MMUtil.SINGLETON.buildClass(typeExpr);                
            }
            if (attr.getNamespace() != null) {
                type.setNamespace(attr.getNamespace());
            }
            attr.setType(type);
            s = s.substring(typeExpr.length(), s.length());
        }
        return s;
    }
    
    
    /**
     * Parses the return type for an operation.
     * @param op
     * @param s
     * @return String
     */
    protected String parseOutReturnType(MOperation op, String s) {
        s = s.trim();       
        if (s.startsWith(":")) { // we got ourselves a type expression
            MClassifier type = null;
            s = s.substring(1, s.length()).trim();
            String typeExpr = "";
            StringTokenizer tokenizer = new StringTokenizer(s, " ={");
            typeExpr = tokenizer.nextToken();
            Project p = ProjectBrowser.TheInstance.getProject();
            type = p.findType(typeExpr);
            if (type == null) { // no type defined yet
                type = MMUtil.SINGLETON.buildClass(typeExpr); 
                // the owner of this type should be the model in which
                // the class that contains the operation lives
                // since we don't know that class, the model is not set here
                // but in the method that calls parseOperation(String s)               
            }
            MParameter param = MMUtil.SINGLETON.buildParameter();
            MMUtil.SINGLETON.setReturnParameter(op,param);
            param.setType(type);
            s = s.substring(typeExpr.length(), s.length());
        }
        return s;
    }


    protected String parseOutInitValue(MAttribute attr, String s) {
        s = s.trim();
        int equalsIndex = s.indexOf("=");
        int braceIndex = s.indexOf("{");
        if (equalsIndex >=0 && ((braceIndex >= 0 && braceIndex>equalsIndex) || 
            (braceIndex < 0 && equalsIndex >=0 ))) { // we have ourselves some init
                                                    // expression
            s = s.substring(equalsIndex, s.length());
            String initExprStr = s.substring(s.indexOf("=")+1, (braceIndex <0)?
                s.length():s.indexOf("{"));
            MExpression initExpr = UmlFactory.getFactory().getDataTypes().createExpression(Notation.getDefaultNotation().toString(), initExprStr);
            attr.setInitialValue(initExpr);
            return s.substring(initExprStr.length(), s.length());
        }
        return s;
  }

  public MParameter parseParameter(String s) {
    java.util.StringTokenizer st = new java.util.StringTokenizer(s, ": = \t");
    String typeStr = "int";
    String paramNameStr = "parameterName?";

    if (st.hasMoreTokens()) paramNameStr = st.nextToken();
    if (st.hasMoreTokens()) typeStr = st.nextToken();
    Project p = ProjectBrowser.TheInstance.getProject();
    MClassifier cls = p.findType(typeStr);
    MParameter param = MMUtil.SINGLETON.buildParameter();
    param.setType(cls);
    param.setKind(MParameterDirectionKind.IN);
    param.setName(paramNameStr);

    return param;
  }


  //   public abstract Package parsePackage(String s);
//   public abstract MClassImpl parseClassifier(String s);

  public MStereotype parseStereotype(String s) {
    return null;
  }

  public MTaggedValue parseTaggedValue(String s) {
    return null;
  }

//   public abstract MAssociation parseAssociation(String s);
//   public abstract MAssociationEnd parseAssociationEnd(String s);

  /** Parse a string of the form: "range, ...", where range is of the
   *  form "lower..upper", or "integer" */
  public MMultiplicity parseMultiplicity(String s) {
	  return UmlFactory.getFactory().getDataTypes().createMultiplicity(s);
 
  }


  public MState parseState(String s) {
    return null;
  }

  public void parseStateBody(MState st, String s) {
      //remove all old transitions; needs-more-work: this should be done better!!
      st.setEntry(null);
      st.setExit(null);

      Collection trans = new ArrayList();
      java.util.StringTokenizer lines = new java.util.StringTokenizer(s, "\n\r");
      while (lines.hasMoreTokens()) {
	  String line = lines.nextToken().trim();
	  if (line.startsWith("entry")) parseStateEntyAction(st, line);
	  else if (line.startsWith("exit")) parseStateExitAction(st, line);
	  else {
	      MTransition t = parseTransition(
	          UmlFactory.getFactory().getStateMachines().createTransition(),
                  line);
 

	      if (t == null) continue;
	      //System.out.println("just parsed:" + GeneratorDisplay.Generate(t));
	      t.setStateMachine(st.getStateMachine());
	      t.setTarget(st);
	      t.setSource(st);
	      trans.add(t);
	  }
      }

      Vector internals = new Vector(st.getInternalTransitions());
      Vector oldinternals = new Vector(st.getInternalTransitions());
      internals.removeAll(oldinternals); //now the vector is empty

      // don't forget to remove old internals!
      for (int i = 0; i < oldinternals.size(); i++)
	  ((MTransition)oldinternals.elementAt(i)).remove();
      internals.addAll(trans);

      st.setInternalTransitions(trans);
  }

    public void parseStateEntyAction(MState st, String s) {
    if (s.startsWith("entry") && s.indexOf("/") > -1)
	s = s.substring(s.indexOf("/")+1).trim();
    MCallAction entryAction=(MCallAction)parseAction(s);
    entryAction.setName("anon");
    st.setEntry(entryAction);
  }

  public void parseStateExitAction(MState st, String s) {
    if (s.startsWith("exit") && s.indexOf("/") > -1)
      s = s.substring(s.indexOf("/")+1).trim();
    MCallAction exitAction=(MCallAction)parseAction(s);
    exitAction.setName("anon");
    st.setExit(exitAction);
  }

  /** Parse a line of the form: "name: trigger [guard] / actions" */
  public MTransition parseTransition(MTransition trans, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return null;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String trigger = "";
    String guard = "";
    String actions = "";
    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      s = s.substring(s.indexOf(":") + 1).trim();
    }

    if (s.indexOf("[", 0) > -1 && s.indexOf("]", 0) > -1) {
      guard = s.substring(s.indexOf("[", 0)+1, s.indexOf("]")).trim();
      s = s.substring(0, s.indexOf("[")) + s.substring(s.indexOf("]")+1);
      s = s.trim();
    }

    if (s.indexOf("/", 0) > -1) {
      actions = s.substring(s.indexOf("/")+1).trim();
      s = s.substring(0, s.indexOf("/")).trim();
    }

    trigger = s;

    /*     System.out.println("name=|" + name +"|");
     System.out.println("trigger=|" + trigger +"|");
     System.out.println("guard=|" + guard +"|");
     System.out.println("actions=|" + actions +"|");
    */
    trans.setName(parseName(name));

    if (trigger.length()>0) {
	MEvent evt=parseEvent(trigger);
	if (evt!=null){
	    trans.setTrigger((MCallEvent)evt);
	}
    }
    else
	trans.setTrigger(null);

    if (guard.length()>0){
	MGuard g=parseGuard(guard);
	if (g!=null){
	    g.setName("anon");
	    g.setTransition(trans);
	    trans.setGuard(g);
	}
    }
    else
	trans.setGuard(null);

    if (actions.length()>0){
	MCallAction effect=(MCallAction)parseAction(actions);
	effect.setName("anon");
	trans.setEffect(effect);
    }
    else
	trans.setEffect(null);

    return trans;
  }

  /** Parse a line of the form: "name: base" */
  public void parseClassifierRole(MClassifierRole cls, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String basefirst = "";
    String bases = "";
    StringTokenizer baseTokens = null;

    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      bases = s.substring(s.indexOf(":") + 1).trim();
      baseTokens = new StringTokenizer(bases,",");
    }
    else {
      name = s;
    }

    cls.setName(name);

    Collection col = cls.getBases();
    if ((col != null) && (col.size()>0)) {
      Iterator itcol = col.iterator();
      while (itcol.hasNext()) {
        MClassifier bse = (MClassifier) itcol.next();
	if (bse!=null)
	    cls.removeBase(bse);
      }
    }

    if (baseTokens!=null){
	while(baseTokens.hasMoreElements()){
	    String typeString = baseTokens.nextToken();
	    MClassifier type = ProjectBrowser.TheInstance.getProject().findType(typeString);
	    if (type!=null)
		cls.addBase(type);
	}
    }

   }

  /** Parse a line of the form: "name: action" */
  public void parseMessage(MMessage mes, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String action = "";
    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      //System.out.println("set message name to: '" + name + "'");
      action = s.substring(s.indexOf(":") + 1).trim();
    }
    else action = s;

     MAction ua = (MAction) mes.getAction();
     ua.setName(action);
     mes.setName(name);

  }

  /** Parse a line of the form: "name: action" */
  public void parseStimulus(MStimulus sti, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    //cut trailing string "new Action"
    s = s.trim();
    if (s.length() == 0) return;
    if (s.endsWith("new Action"))
      s = s.substring(0, s.length() - 10);

    String name = "";
    String action = "";
    String actionfirst = "";
    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      actionfirst = s.substring(s.indexOf(":") + 1).trim();
      if (actionfirst.indexOf(":", 0) > 1) {
        action = actionfirst.substring(0, actionfirst.indexOf(":")).trim();
      }
      else action = actionfirst;
    }
    else name = s;

     MAction act = (MAction) sti.getDispatchAction();
     act.setName(action);
     sti.setName(name);
  }

  public MAction parseAction(String s) {
	  MCallAction a = UmlFactory.getFactory().getCommonBehavior().createCallAction();
 
	  a.setScript(UmlFactory.getFactory().getDataTypes().createActionExpression("Java",s));
 
	  return a;
  }

    /*  public MActionSequence parseActions(String s) {
    MActionSequence as = UmlFactory.getFactory().getCommonBehavior().createActionSequence(s);
 
    as.setName(s);
    return as;
    }*/

  public MGuard parseGuard(String s) {
	MGuard g = UmlFactory.getFactory().getStateMachines().createGuard();
	g.setExpression(UmlFactory.getFactory().getDataTypes().createBooleanExpression("Java",s));
 
        return g;
  }

  public MEvent parseEvent(String s) {
	MCallEvent ce = UmlFactory.getFactory().getStateMachines().createCallEvent();
 
	ce.setName(s);
	ce.setNamespace(ProjectBrowser.TheInstance.getProject().getModel());
        return ce;
  }

  /** Parse a line of the form: "name: base-class" */
  public void parseObject(MObject obj, String s) {
    // strip any trailing semi-colons
    s = s.trim();

    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String basefirst = "";
    String bases = "";
    StringTokenizer baseTokens = null;

    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":",0)).trim();
      bases = s.substring(s.indexOf(":",0) + 1).trim();
      baseTokens = new StringTokenizer(bases,",");
    }
    else {
      name = s;
    }

    obj.setName(name);

    obj.setClassifiers(new Vector());
    if (baseTokens != null) {
      while(baseTokens.hasMoreElements()){
  	String typeString = baseTokens.nextToken();
	MClassifier type = ProjectBrowser.TheInstance.getProject().findType(typeString);
	obj.addClassifier(type);
      }
    }
  }

  /** Parse a line of the form: "name : base-node" */
  public void parseNodeInstance(MNodeInstance noi, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);



    String name = "";
    String bases = "";
    StringTokenizer tokenizer = null;

    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      bases = s.substring(s.indexOf(":") + 1).trim();
    }
    else {
      name = s;
    }

    tokenizer = new StringTokenizer(bases,",");

    Vector v = new Vector();
    MNamespace ns = noi.getNamespace();
    if (ns !=null) {
	while (tokenizer.hasMoreElements()) {
	    String newBase = tokenizer.nextToken();
	    MClassifier cls = (MClassifier)ns.lookup(newBase.trim());
	    if (cls != null)
		v.add(cls);
	}
    }

    noi.setClassifiers(v);
    noi.setName(new String(name));

  }

  /** Parse a line of the form: "name : base-component" */
  public void parseComponentInstance(MComponentInstance coi, String s) {
    // strip any trailing semi-colons
    s = s.trim();
    if (s.length() == 0) return;
    if (s.charAt(s.length()-1) == ';')
      s = s.substring(0, s.length() - 2);

    String name = "";
    String bases = "";
    StringTokenizer tokenizer = null;

    if (s.indexOf(":", 0) > -1) {
      name = s.substring(0, s.indexOf(":")).trim();
      bases = s.substring(s.indexOf(":") + 1).trim();
    }
    else {
      name = s;
    }

    tokenizer = new StringTokenizer(bases,",");

    Vector v = new Vector();
    MNamespace ns = coi.getNamespace();
    if (ns !=null) {
	while (tokenizer.hasMoreElements()) {
	    String newBase = tokenizer.nextToken();
	    MClassifier cls = (MClassifier)ns.lookup(newBase.trim());
	    if (cls != null)
		v.add(cls);
	}
    }

    coi.setClassifiers(v);
    coi.setName(new String(name));

  }

} /* end class ParserDisplay */
