/* Novosoft UML API for Java. Version 0.4.19
 * Copyright (C) 1999, 2000, NovoSoft.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text of license can be also found 
 * at http://www.gnu.org/copyleft/lgpl.html
 */

package ru.novosoft.uml.xmi;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;

import java.util.StringTokenizer;

import java.io.*;

import org.jdom.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.model_management.*;


import org.xml.sax.*;
import javax.xml.parsers.*;

public class XMIReader extends HandlerBase
{
  public static final String HELPTEXT = "usage:\n  XMIReader <input .xmi file> <output .xmi file>\n";

  //org.apache.xerces.parsers.SAXParser parser = new org.apache.xerces.parsers.SAXParser();
  //com.ibm.xml.parsers.ValidatingSAXParser parser = new com.ibm.xml.parsers.ValidatingSAXParser();

  org.xml.sax.Parser parser = null;

  public static final Integer STATE_XMI = new Integer(0);
  public static final Integer STATE_XMI_CONTENT = new Integer(6);
  public static final Integer STATE_MODEL = new Integer(2);
  public static final Integer STATE_SINGLE = new Integer(1);
  public static final Integer STATE_MULTIPLE = new Integer(4);
  public static final Integer STATE_EXTENSION = new Integer(3);
  public static final Integer STATE_UNKNOWN = new Integer(5);

  public static final int INT_STATE_XMI = 0;
  public static final int INT_STATE_XMI_CONTENT = 6;
  public static final int INT_STATE_MODEL = 2;
  public static final int INT_STATE_SINGLE = 1;
  public static final int INT_STATE_MULTIPLE = 4;
  public static final int INT_STATE_EXTENSION = 3;
  public static final int INT_STATE_UNKNOWN = 5;

  ArrayList links = new ArrayList();
  private class Link
  {
    Object sourceObject;
    String methodName;
    boolean methodType;
    String parameterXMIID;
    String parameterXMIUUID;
  }

  protected void link(Object p_sourceObject, String p_methodName, boolean p_methodType, String p_parameterXMIID, String p_parameterXMIUUID)
  {
    Link l = new Link();
    l.sourceObject = p_sourceObject;
    l.methodName = p_methodName;
    l.methodType = p_methodType;
    l.parameterXMIID = p_parameterXMIID;
    l.parameterXMIUUID = p_parameterXMIUUID;
    links.add(l);
  }

  protected void link(Link p_link)
  {
    links.add(p_link);
  }

  protected void performLinking()
  {
    Link l = null;
    Iterator i = links.iterator();
    while(i.hasNext())
    {
      l = (Link)i.next();

      Object objectParameter = getObject(l.parameterXMIID, l.parameterXMIUUID);
/*
      System.out.println("[link] " + l.sourceObject);
      System.out.println("[link] " + l.methodType);
      System.out.println("[link] " + l.methodName);
      System.out.println("[link] " + objectParameter);
      System.out.println();
*/
      if (null == objectParameter)
      {
        throw new RuntimeException("Linking error: unknown parameter;");
      }

      if (l.sourceObject instanceof MBase) 
      {
        if (l.methodType)
        {
          ((MBase)l.sourceObject).reflectiveSetValue(l.methodName, objectParameter);
        }
        else
        {
          ((MBase)l.sourceObject).reflectiveAddValue(l.methodName, objectParameter);
        }
        removeXMIID(this);
        removeXMIUUID(this);
      }
      else if (l.sourceObject instanceof MMultiplicity)
      {
        throw new RuntimeException("Linking error: invalid argument MMultiplicity;");
      }
      else
      {
        throw new RuntimeException("Linking error: invalid argument;");
      }
    }
    links = new ArrayList(); 
  }

  MFactory factory;
  public MFactory getFactory()
  {
    return factory;
  }

  public XMIReader() throws SAXException, 
                            ParserConfigurationException 
                            
  {
    this(null);
  }

  public XMIReader(MFactory p_factory) throws SAXException,
                                              ParserConfigurationException
                                              
  {
    if (null == p_factory)
    {
      factory = MFactory.getDefaultFactory();
    }
    else
    {
      factory = p_factory;
    }
    SAXParserFactory saxpf = SAXParserFactory.newInstance();
    saxpf.setValidating(false);
    saxpf.setNamespaceAware(false);

    parser = saxpf.newSAXParser().getParser();

    parser.setErrorHandler(this);
    parser.setDocumentHandler(this);
    parser.setEntityResolver(this);
  }

  List rootList = null;
  public List getParsedElements()
  {
    return rootList;
  }

  MModel rootModel = null;
  public MModel getParsedModel()
  {
    return rootModel;
  }

  public void cleanup()
  {
    rootList = new ArrayList();
    rootModel = null;

    xmiid2Element = new HashMap();
    element2xmiid = new HashMap();

    xmiuuid2Element = new HashMap();
    element2xmiuuid = new HashMap();

    liStack = null;
    liNameStack = null;
    lastString = new StringBuffer();
    lastMethod = "";
    lastMethodType=false;
    lastObject = null;
    lastXMIExtension = null;
    links = new ArrayList(); 
    xmiid = 1;
    deepNotModelProcessing = 0;
  }

  protected void parseStream(InputSource p_is)
  {
    cleanup();

    try
    {
      parser.parse(p_is);
      performLinking();
    }
    catch(Exception ex)
    {
      ex.printStackTrace(System.err);
    }
  }

  public MModel parse(InputSource p_is)
  {
    parseStream(p_is);
    return rootModel;
  }

  public static void main(String p_args[]) 
  {
    try 
    {
      java.io.InputStream in;
      java.io.OutputStream out;

      if (p_args.length == 0)
      {
        in = System.in;
      }
      else
      {
        in = new java.io.FileInputStream(p_args[0]);
      }

      if (p_args.length < 2)
      {
        out = System.out;
      }
      else
      {
        out = new java.io.FileOutputStream(p_args[1]);
      }

      XMIReader xmiReader = new XMIReader();
      MModel mmodel = xmiReader.parse(new InputSource(in));

      if (null != mmodel)
      {
        XMIWriter xmiw = new XMIWriter(mmodel, new PrintWriter(out));

        try
        {
          xmiw.gen();
        }
        catch(IncompleteXMIException ex)
        {
          Iterator i = xmiw.getNotContainedElements().iterator();
          while(i.hasNext())
          {
            Object obj = i.next();
            System.err.println("[Error] Model element '" + obj.toString() + "' is not contained.");
          }
        }
      }
      else
      {
        throw new RuntimeException("Invalid XMI document");
      }
    } 
    catch (Exception ex) 
    {
      ex.printStackTrace(System.err);
    }
  }

  //==========================================================================

  List liStack = null;
  List liNameStack = null;
  StringBuffer lastString = new StringBuffer();
  String lastMethod = "";
  boolean lastMethodType=false;
  Object lastObject = null;
  Element lastXMIExtension = null;
  int deepNotModelProcessing = 0;

  public void processingInstruction(String p_target, String p_data)
  {
  }

  public void startDocument()
  {
    liStack = new ArrayList();
    liStack.add(STATE_XMI);
    liNameStack = new ArrayList();
    liNameStack.add(null);
  }

  public void characters(char p_ch[], int p_start, int p_length)
  {
    lastString.append(p_ch, p_start, p_length);
  }

  public void ignorableWhitespace(char p_ch[], int p_start, int p_length)
  {
  }

  public void endDocument()
  {
  }

  public void warning(SAXParseException ex) 
  {
    //tracer.trace("[Warning] " + getLocationString(ex) + ": " + ex.getMessage());
  }

  public void error(SAXParseException ex) 
  {
    System.err.println("[Error] " + getLocationString(ex) + ": " + ex.getMessage());
  }

  public void fatalError(SAXParseException ex) throws SAXException 
  {
    System.err.println("[Fatal Error] " + getLocationString(ex) + ": " + ex.getMessage());
    throw ex;
  }

  private String getLocationString(SAXParseException ex) 
  {
    StringBuffer str = new StringBuffer();

    String systemId = ex.getSystemId();
    if (systemId != null) {
      int index = systemId.lastIndexOf('/');
      if (index != -1) 
          systemId = systemId.substring(index + 1);
      str.append(systemId);
    }
    str.append(':');
    str.append(ex.getLineNumber());
    str.append(':');
    str.append(ex.getColumnNumber());

    return str.toString();
  }

  public InputSource resolveEntity (String publicId, String systemId) 
  {
    try
    {
      java.net.URL urlDTD = new java.net.URL(systemId);
      InputSource is = new InputSource(urlDTD.openStream());

      return is;
    } 
    catch (Exception ex) 
    {
      System.err.println("[Warning] Resource '" + systemId + "' not found. Default XMI DTD will be used instead.");

      java.net.URL urlDefaultDTD = getClass().getResource("/ru/novosoft/uml/xmi/uml13.dtd");
      InputSource is = new InputSource(urlDefaultDTD.toString());

      return is;
    }
  }

  //==========================================================================

  protected int xmiid = 1;
  protected HashMap xmiid2Element = new HashMap();
  protected HashMap element2xmiid = new HashMap();
  protected Object getXMIID(String p_xmiid)
  {
    return (Object)(xmiid2Element.get(p_xmiid));
  }

  protected String getXMIIDByElement(Object p_obj)
  {
    return (String)(element2xmiid.get(p_obj));
  }

  protected void putXMIID(String p_xmiid, Object p_o)
  {
    xmiid2Element.put(p_xmiid, p_o);
    element2xmiid.put(p_o, p_xmiid);
  }

  protected void removeXMIID(Object p_obj)
  {
    element2xmiid.remove(p_obj);
  }

  public Map getXMIIDToObjectMap()
  { 
    return Collections.unmodifiableMap(xmiid2Element);
  }

  public Map getObjectToXMIIDMap()
  { 
    return Collections.unmodifiableMap(element2xmiid);
  }

  public Map getXMIUUIDToObjectMap()
  { 
    return Collections.unmodifiableMap(xmiuuid2Element);
  }

  public Map getObjectToXMIUUIDMap()
  { 
    return Collections.unmodifiableMap(element2xmiuuid);
  }

  protected HashMap xmiuuid2Element = new HashMap();
  protected HashMap element2xmiuuid = new HashMap();
  protected Object getXMIUUID(String p_xmiuuid)
  {
    return (Object)(xmiuuid2Element.get(p_xmiuuid));
  }

  protected String getXMIUUIDByElement(Object p_obj)
  {
    return (String)(element2xmiuuid.get(p_obj));
  }

  protected void putXMIUUID(String p_xmiuuid, Object p_o)
  {
    xmiuuid2Element.put(p_xmiuuid, p_o);
    element2xmiuuid.put(p_o, p_xmiuuid);
  }

  protected void removeXMIUUID(Object p_obj)
  {
    element2xmiuuid.remove(p_obj);
  }

  protected boolean convertXMIBooleanValue(String p_s)
  {
    if (p_s.equalsIgnoreCase("true"))
    {
      return true;
    }

    return false;
  }

  protected String convertStringXMIValue(String s)
  {
    StringBuffer str = new StringBuffer();

    int len = (s != null) ? s.length() : 0;
    for(int i = 0; i < len; i++)
    {
      char ch = s.charAt(i);
      switch (ch)
      {
        case '<': 
        {
          str.append("&lt;");
          break;
        }

        case '>': 
        {
          str.append("&gt;");
          break;
        }

        case '&': 
        {
          str.append("&amp;");
          break;
        }

        case '"': 
        {
          str.append("&quot;");
          break;
        }

        case '\r':
        case '\n':
        {
          str.append("&#");
          str.append(Integer.toString(ch));
          str.append(';');
          //print(str.toString());
          //println();
          //str = new StringBuffer();
          break;
        }

        default: 
        {
          str.append(ch);
        }
      }
    }

    return str.toString();
  }

  public Object getObject(AttributeList p_attrs)
  {
    String nodeXMIID = p_attrs.getValue("xmi.id");
    String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

    Object o = null;
    if (null != nodeXMIID)
    {
      o = getXMIID(nodeXMIID);
    }
    else if (null == o && null != nodeXMIUUID)
    {
      o = getXMIUUID(nodeXMIUUID);
    }

    return o;
  }

  public Object getObject(String nodeXMIID, String nodeXMIUUID)
  {
    Object o = null;
    if (null != nodeXMIID)
    {
      o = getXMIID(nodeXMIID);
    }
    else if (null == o && null != nodeXMIUUID)
    {
      o = getXMIUUID(nodeXMIUUID);
    }

    return o;
  }

  public Object getObjectByRef(AttributeList p_attrs)
  {
    String nodeXMIID = p_attrs.getValue("xmi.idref");
    String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

    Object o = null;
    if (null != nodeXMIID)
    {
      o = getXMIID(nodeXMIID);
    }
    else if (null == o && null != nodeXMIUUID)
    {
      o = getXMIUUID(nodeXMIUUID);
    }

    return o;
  }


  public void putObject(AttributeList p_attrs, Object o)
  {
    String nodeXMIID = p_attrs.getValue("xmi.id");
    String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

    if (null != nodeXMIID)
    {
      putXMIID(nodeXMIID, o);
    }

    if (null != nodeXMIUUID)
    {
      putXMIUUID(nodeXMIUUID, o);
    }
  }

  public void putObjectByRef(AttributeList p_attrs, Object o)
  {
    String nodeXMIID = p_attrs.getValue("xmi.idref");
    String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

    if (null != nodeXMIID)
    {
      putXMIID(nodeXMIID, o);
    }

    if (null != nodeXMIUUID)
    {
      putXMIUUID(nodeXMIUUID, o);
    }
  }

  //========================= Default XMI tags ================================

  protected boolean processIntegerMain(String p_name, AttributeList p_attrs)
  {
    int i = ((Integer)liStack.get(liStack.size()-1)).intValue();

    switch (i)
    {
      case INT_STATE_XMI:
      {
        return processMain(p_name, p_attrs);
      }

      case INT_STATE_XMI_CONTENT:
      {
        return processXMIMain(p_name, p_attrs);
      }

      case INT_STATE_SINGLE:
      case INT_STATE_MULTIPLE:
      {
        Object o = process(p_name, p_attrs);
        if (o != null)
        {
          if (o instanceof Link && null != lastMethod)
          {
            Link l = (Link)o;
            l.sourceObject = liStack.get(liStack.size()-2);
            l.methodName = lastMethod;
            l.methodType = lastMethodType;
            link(l);
          }
          liStack.add(o);
          liNameStack.add(p_name);
        }
        else
        {
          //liStack.add(STATE_EXTENSION);
          //liNameStack.add(p_name);
          return false;
        }

        return true;
      }

      case INT_STATE_MODEL:
      {
        Object o = process(p_name, p_attrs);
        if (o != null)
        {
          rootList.add(o);
          liStack.add(o);
          liNameStack.add(p_name);
        }
        else
        {
          liStack.add(STATE_SINGLE);
          liNameStack.add(p_name);
        }

        return true;
      }

      case INT_STATE_EXTENSION:
      {
        if (0 != lastString.length())
        {
          lastXMIExtension.addContent(lastString.toString());
        }

        Element newelem = new Element(p_name);
        lastXMIExtension.addContent(newelem);

        int l = p_attrs.getLength();
        for (int j=0; j<l; j++)
        {
          newelem.addAttribute(p_attrs.getName(j), p_attrs.getValue(j));
        }
        lastXMIExtension = newelem;
  
        liStack.add(STATE_EXTENSION);
        liNameStack.add(p_name);

        return true;
      }
    }

    return false;
  }

  protected void postprocessIntegerMain(String p_name)
  {
    int i = ((Integer)liStack.get(liStack.size()-2)).intValue();

    switch (i)
    {
      case INT_STATE_EXTENSION:
      {
        if (0 != lastString.length())
        {
          lastXMIExtension.addContent(lastString.toString());
        }
        lastXMIExtension = lastXMIExtension.getParent();
      } 
      break;
    }
  }

  public boolean processMain(String p_name, AttributeList p_attrs)
  {
    Integer o = (Integer)liStack.get(liStack.size()-1);

    if (p_name.equals("XMI"))
    {
      liStack.add(STATE_XMI_CONTENT);
      liNameStack.add(p_name);

      return true;
    }

    //liStack.add(o);
    //liNameStack.add(p_name);

    return false;
  }

  public boolean processXMIMain(String p_name, AttributeList p_attrs)
  {
    Integer o = (Integer)liStack.get(liStack.size()-1);

    if (p_name.equals("XMI.content"))
    {
      liStack.add(STATE_MODEL);
      liNameStack.add(p_name);

      return true;
    }

    //liStack.add(o);
    //liNameStack.add(p_name);

    deepNotModelProcessing = -1;

    return false;
  }

  public boolean processMultiplicityAttributes(String p_name, AttributeList p_attrs, MMultiplicityEditor o)
  {
    return false;
  }

  public boolean postprocessMultiplicityAttributes(String p_name, MMultiplicityEditor o)
  {
    return false;
  }

  public boolean processMultiplicityRoles(String p_name, AttributeList p_attrs, MMultiplicityEditor o)
  {
    if (p_name.startsWith("Foundation.Data_Types.Multiplicity."))
    {
      String lastName = p_name.substring(35);

      if (lastName.equals("range"))
      {
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessMultiplicityRoles(String p_name, MMultiplicityEditor o)
  {
    if (p_name.startsWith("Foundation.Data_Types.Multiplicity."))
    {
      String lastName = p_name.substring(35);

      if (lastName.equals("range"))
      {
        MMultiplicityRange el = ((MMultiplicityRangeEditor)lastObject).toMultiplicityRange();
        if (null != el)
        {
          o.addRange(el);

          String xmiid = getXMIIDByElement(lastObject);
          if (null != xmiid)
          {
            removeXMIID(lastObject);
            putXMIID(xmiid, el);
          }

          String xmiuuid = getXMIUUIDByElement(lastObject);
          if (null != xmiuuid)
          {
            removeXMIUUID(lastObject);
            putXMIUUID(xmiuuid, el);
          }

          return true;
        }
      }
    }

    return false;
  }



  public boolean processMultiplicityRangeAttributes(String p_name, AttributeList p_attrs, MMultiplicityRangeEditor o)
  {
    if (p_name.startsWith("Foundation.Data_Types.MultiplicityRange."))
    {
      String lastName = p_name.substring(40);

      if (lastName.equals("lower"))
      {
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("upper"))
      {
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessMultiplicityRangeAttributes(String p_name, MMultiplicityRangeEditor o)
  {
    if (p_name.startsWith("Foundation.Data_Types.MultiplicityRange."))
    {
      String lastName = p_name.substring(40);

      String lastString = this.lastString.toString();

      if (lastName.equals("lower"))
      {
        int ilower = -1;

        try
        {
          ilower = Integer.parseInt(lastString);
        }
        catch (NumberFormatException ex)
        {
        }

        if (ilower < 0)
        {
          System.err.println("[Error] MultiplicityRange lower bound must be a nonnegative integer. Value defaulted to zero.");
          ilower = 0;
        }

        o.setLower(ilower);

        return true;
      }

      if (lastName.equals("upper"))
      {
        if ("*".equals(lastString) || "N".equalsIgnoreCase(lastString) || "unlimited".equals(lastString))
        {
          o.setUpper(MMultiplicity.N);
        }
        else
        {
          try
          {
            o.setUpper(Integer.parseInt(lastString));
          }
          catch (NumberFormatException ex)
          {
            System.err.println("[Error] Invalid MultiplicityRange upper bound. Value defaulted to \"unlimited\".");
            o.setUpper(MMultiplicity.N);
          }
        }

        return true;
      }
    }

    return false;
  }

  public boolean processMultiplicityRangeRoles(String p_name, AttributeList p_attrs, MMultiplicityRangeEditor o)
  {
    return false;
  }

  public boolean postprocessMultiplicityRangeRoles(String p_name, MMultiplicityRangeEditor o)
  {
    return false;
  }

  public boolean processXMIExtensionMain(String p_name, AttributeList p_attrs)
  {
    if (p_name.equals("XMI.extension"))
    {
      MExtension o = new MExtensionImpl();

      o.setExtender(p_attrs.getValue("xmi.extender"));
      o.setExtenderID(p_attrs.getValue("xmi.extenderID"));

      lastXMIExtension = new Element(p_name);
      int l = p_attrs.getLength();
      for (int i=0; i<l; i++)
      {
        lastXMIExtension.addAttribute(p_attrs.getName(i), p_attrs.getValue(i));
      }

      liStack.add(o);
      liNameStack.add(p_name);

      return true;
    }

    //liStack.add(liStack.get(liStack.size()-1));
    //liNameStack.add(liNameStack.get(liNameStack.size()-1));

    return false;
  }

  public void postprocessXMIExtensionMain(String p_name, Object p_o)
  {
    if (p_name.equals("XMI.extension"))
    {
      if (p_o instanceof MBase)
      {
        MExtension ext = (MExtension)(liStack.get(liStack.size()-1));

        if (0 != lastString.length())
        {
          lastXMIExtension.addContent(lastString.toString());
        }
        ext.setValue(lastXMIExtension);
        ((MBase)p_o).addExtension(ext);
      }
    }
  }

  //=============================== Cut here =================================

  public void startElement(String p_name, AttributeList p_attrs)
  {
    try
    {
      if (deepNotModelProcessing > 0)
      {
        deepNotModelProcessing++;
        return;
      }

      Object o = liStack.get(liStack.size()-1);

      boolean processed = false;
      if (o instanceof Integer)
      {
        processed = processIntegerMain(p_name, p_attrs);
      }
      else if (o instanceof MExtension)
      {
        if (0 != lastString.length())
        {
          lastXMIExtension.addContent(lastString.toString());
        }

        Element newelem1 = new Element(p_name);
        lastXMIExtension.addContent(newelem1);

        int l1 = p_attrs.getLength();
        for (int j1=0; j1<l1; j1++)
        {
          newelem1.addAttribute(p_attrs.getName(j1), p_attrs.getValue(j1));
        }
        lastXMIExtension = newelem1;

        liStack.add(STATE_EXTENSION);
        liNameStack.add(p_name);

        processed = true;
      }
      else if (o instanceof MCallAction)
      {
        processed = processCallActionMain(p_name, p_attrs);
      }
      else if (o instanceof MTerminateAction)
      {
        processed = processTerminateActionMain(p_name, p_attrs);
      }
      else if (o instanceof MComment)
      {
        processed = processCommentMain(p_name, p_attrs);
      }
      else if (o instanceof MDataType)
      {
        processed = processDataTypeMain(p_name, p_attrs);
      }
      else if (o instanceof MDestroyAction)
      {
        processed = processDestroyActionMain(p_name, p_attrs);
      }
      else if (o instanceof MArgListsExpressionEditor)
      {
        processed = processArgListsExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MExtend)
      {
        processed = processExtendMain(p_name, p_attrs);
      }
      else if (o instanceof MUsage)
      {
        processed = processUsageMain(p_name, p_attrs);
      }
      else if (o instanceof MPermission)
      {
        processed = processPermissionMain(p_name, p_attrs);
      }
      else if (o instanceof MActionSequence)
      {
        processed = processActionSequenceMain(p_name, p_attrs);
      }
      else if (o instanceof MCollaboration)
      {
        processed = processCollaborationMain(p_name, p_attrs);
      }
      else if (o instanceof MAssociationRole)
      {
        processed = processAssociationRoleMain(p_name, p_attrs);
      }
      else if (o instanceof MAttribute)
      {
        processed = processAttributeMain(p_name, p_attrs);
      }
      else if (o instanceof MMessage)
      {
        processed = processMessageMain(p_name, p_attrs);
      }
      else if (o instanceof MBooleanExpressionEditor)
      {
        processed = processBooleanExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MPseudostate)
      {
        processed = processPseudostateMain(p_name, p_attrs);
      }
      else if (o instanceof MTypeExpressionEditor)
      {
        processed = processTypeExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MClassifierInState)
      {
        processed = processClassifierInStateMain(p_name, p_attrs);
      }
      else if (o instanceof MStereotype)
      {
        processed = processStereotypeMain(p_name, p_attrs);
      }
      else if (o instanceof MSubsystem)
      {
        processed = processSubsystemMain(p_name, p_attrs);
      }
      else if (o instanceof MMappingExpressionEditor)
      {
        processed = processMappingExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MUseCase)
      {
        processed = processUseCaseMain(p_name, p_attrs);
      }
      else if (o instanceof MModel)
      {
        processed = processModelMain(p_name, p_attrs);
      }
      else if (o instanceof MLinkObject)
      {
        processed = processLinkObjectMain(p_name, p_attrs);
      }
      else if (o instanceof MLinkEnd)
      {
        processed = processLinkEndMain(p_name, p_attrs);
      }
      else if (o instanceof MTemplateParameter)
      {
        processed = processTemplateParameterMain(p_name, p_attrs);
      }
      else if (o instanceof MCreateAction)
      {
        processed = processCreateActionMain(p_name, p_attrs);
      }
      else if (o instanceof MPartition)
      {
        processed = processPartitionMain(p_name, p_attrs);
      }
      else if (o instanceof MAssociationEndRole)
      {
        processed = processAssociationEndRoleMain(p_name, p_attrs);
      }
      else if (o instanceof MAttributeLink)
      {
        processed = processAttributeLinkMain(p_name, p_attrs);
      }
      else if (o instanceof MSendAction)
      {
        processed = processSendActionMain(p_name, p_attrs);
      }
      else if (o instanceof MCallEvent)
      {
        processed = processCallEventMain(p_name, p_attrs);
      }
      else if (o instanceof MUninterpretedAction)
      {
        processed = processUninterpretedActionMain(p_name, p_attrs);
      }
      else if (o instanceof MSubactivityState)
      {
        processed = processSubactivityStateMain(p_name, p_attrs);
      }
      else if (o instanceof MTransition)
      {
        processed = processTransitionMain(p_name, p_attrs);
      }
      else if (o instanceof MOperation)
      {
        processed = processOperationMain(p_name, p_attrs);
      }
      else if (o instanceof MClassifierRole)
      {
        processed = processClassifierRoleMain(p_name, p_attrs);
      }
      else if (o instanceof MTimeExpressionEditor)
      {
        processed = processTimeExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MObjectFlowState)
      {
        processed = processObjectFlowStateMain(p_name, p_attrs);
      }
      else if (o instanceof MStimulus)
      {
        processed = processStimulusMain(p_name, p_attrs);
      }
      else if (o instanceof MSynchState)
      {
        processed = processSynchStateMain(p_name, p_attrs);
      }
      else if (o instanceof MInclude)
      {
        processed = processIncludeMain(p_name, p_attrs);
      }
      else if (o instanceof MArgument)
      {
        processed = processArgumentMain(p_name, p_attrs);
      }
      else if (o instanceof MComponent)
      {
        processed = processComponentMain(p_name, p_attrs);
      }
      else if (o instanceof MAssociationClass)
      {
        processed = processAssociationClassMain(p_name, p_attrs);
      }
      else if (o instanceof MActor)
      {
        processed = processActorMain(p_name, p_attrs);
      }
      else if (o instanceof MSignalEvent)
      {
        processed = processSignalEventMain(p_name, p_attrs);
      }
      else if (o instanceof MMethod)
      {
        processed = processMethodMain(p_name, p_attrs);
      }
      else if (o instanceof MChangeEvent)
      {
        processed = processChangeEventMain(p_name, p_attrs);
      }
      else if (o instanceof MParameter)
      {
        processed = processParameterMain(p_name, p_attrs);
      }
      else if (o instanceof MTaggedValue)
      {
        processed = processTaggedValueMain(p_name, p_attrs);
      }
      else if (o instanceof MTimeEvent)
      {
        processed = processTimeEventMain(p_name, p_attrs);
      }
      else if (o instanceof MUseCaseInstance)
      {
        processed = processUseCaseInstanceMain(p_name, p_attrs);
      }
      else if (o instanceof MStubState)
      {
        processed = processStubStateMain(p_name, p_attrs);
      }
      else if (o instanceof MNode)
      {
        processed = processNodeMain(p_name, p_attrs);
      }
      else if (o instanceof MNodeInstance)
      {
        processed = processNodeInstanceMain(p_name, p_attrs);
      }
      else if (o instanceof MElementResidence)
      {
        processed = processElementResidenceMain(p_name, p_attrs);
      }
      else if (o instanceof MGeneralization)
      {
        processed = processGeneralizationMain(p_name, p_attrs);
      }
      else if (o instanceof MReturnAction)
      {
        processed = processReturnActionMain(p_name, p_attrs);
      }
      else if (o instanceof MException)
      {
        processed = processExceptionMain(p_name, p_attrs);
      }
      else if (o instanceof MAbstraction)
      {
        processed = processAbstractionMain(p_name, p_attrs);
      }
      else if (o instanceof MConstraint)
      {
        processed = processConstraintMain(p_name, p_attrs);
      }
      else if (o instanceof MIterationExpressionEditor)
      {
        processed = processIterationExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MObjectSetExpressionEditor)
      {
        processed = processObjectSetExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MReception)
      {
        processed = processReceptionMain(p_name, p_attrs);
      }
      else if (o instanceof MGuard)
      {
        processed = processGuardMain(p_name, p_attrs);
      }
      else if (o instanceof MComponentInstance)
      {
        processed = processComponentInstanceMain(p_name, p_attrs);
      }
      else if (o instanceof MBinding)
      {
        processed = processBindingMain(p_name, p_attrs);
      }
      else if (o instanceof MFlow)
      {
        processed = processFlowMain(p_name, p_attrs);
      }
      else if (o instanceof MElementImport)
      {
        processed = processElementImportMain(p_name, p_attrs);
      }
      else if (o instanceof MProcedureExpressionEditor)
      {
        processed = processProcedureExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MMultiplicityRangeEditor)
      {
        processed = processMultiplicityRangeMain(p_name, p_attrs);
      }
      else if (o instanceof MActivityGraph)
      {
        processed = processActivityGraphMain(p_name, p_attrs);
      }
      else if (o instanceof MExtensionPoint)
      {
        processed = processExtensionPointMain(p_name, p_attrs);
      }
      else if (o instanceof MFinalState)
      {
        processed = processFinalStateMain(p_name, p_attrs);
      }
      else if (o instanceof MActionExpressionEditor)
      {
        processed = processActionExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MCallState)
      {
        processed = processCallStateMain(p_name, p_attrs);
      }
      else if (o instanceof MDataValue)
      {
        processed = processDataValueMain(p_name, p_attrs);
      }
      else if (o instanceof MInteraction)
      {
        processed = processInteractionMain(p_name, p_attrs);
      }
      else if (o instanceof MInterface)
      {
        processed = processInterfaceMain(p_name, p_attrs);
      }
      else if (o instanceof MMultiplicityEditor)
      {
        processed = processMultiplicityMain(p_name, p_attrs);
      }
      else if (o instanceof MSignal)
      {
        processed = processSignalMain(p_name, p_attrs);
      }
      else if (o instanceof MActionState)
      {
        processed = processActionStateMain(p_name, p_attrs);
      }
      else if (o instanceof MClass)
      {
        processed = processClassMain(p_name, p_attrs);
      }
      else if (o instanceof MAssociation)
      {
        processed = processAssociationMain(p_name, p_attrs);
      }
      else if (o instanceof MPackage)
      {
        processed = processPackageMain(p_name, p_attrs);
      }
      else if (o instanceof MAssociationEnd)
      {
        processed = processAssociationEndMain(p_name, p_attrs);
      }
      else if (o instanceof MDependency)
      {
        processed = processDependencyMain(p_name, p_attrs);
      }
      else if (o instanceof MObject)
      {
        processed = processObjectMain(p_name, p_attrs);
      }
      else if (o instanceof MStateMachine)
      {
        processed = processStateMachineMain(p_name, p_attrs);
      }
      else if (o instanceof MSubmachineState)
      {
        processed = processSubmachineStateMain(p_name, p_attrs);
      }
      else if (o instanceof MLink)
      {
        processed = processLinkMain(p_name, p_attrs);
      }
      else if (o instanceof MAction)
      {
        processed = processActionMain(p_name, p_attrs);
      }
      else if (o instanceof MExpressionEditor)
      {
        processed = processExpressionMain(p_name, p_attrs);
      }
      else if (o instanceof MCompositeState)
      {
        processed = processCompositeStateMain(p_name, p_attrs);
      }
      else if (o instanceof MInstance)
      {
        processed = processInstanceMain(p_name, p_attrs);
      }
      else if (o instanceof MClassifier)
      {
        processed = processClassifierMain(p_name, p_attrs);
      }
      else if (o instanceof MSimpleState)
      {
        processed = processSimpleStateMain(p_name, p_attrs);
      }
      else if (o instanceof MRelationship)
      {
        processed = processRelationshipMain(p_name, p_attrs);
      }
      else if (o instanceof MState)
      {
        processed = processStateMain(p_name, p_attrs);
      }
      else if (o instanceof MNamespace)
      {
        processed = processNamespaceMain(p_name, p_attrs);
      }

      if (!processed)
      {
        if (0 == deepNotModelProcessing)
        {
          System.err.println("[Error] Illegal element '" + p_name + "'");
        }

        deepNotModelProcessing = 1;

        liStack.add(STATE_UNKNOWN);
        liNameStack.add(p_name);

        return;
      }

      lastString = new StringBuffer();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public void endElement(String p_name)
  {
    try
    {
      if (deepNotModelProcessing > 0)
      {
        deepNotModelProcessing--;
        if (0 == deepNotModelProcessing)
        {
          liStack.remove(liStack.size()-1);
          liNameStack.remove(liNameStack.size()-1);

        }

        return;
      }

      Object o = liStack.get(liStack.size()-2);
      Object o2 = liStack.get(liStack.size()-1);
      boolean bMultiple = false;

      if (STATE_MULTIPLE == o)
      {
        p_name = (String)liNameStack.get(liNameStack.size()-2);
        lastObject = liStack.get(liStack.size()-1);
        liStack.remove(liStack.size()-1);
        liNameStack.remove(liNameStack.size()-1);
        o = liStack.get(liStack.size()-2);
        bMultiple = true;
      }

      try
      {
        if (STATE_MULTIPLE == o2)
        {
        }
        else if (o instanceof Integer)
        {
          postprocessIntegerMain(p_name);
        }
        else if (o instanceof MExtension)
        {
          if (0 != lastString.length())
          {
            lastXMIExtension.addContent(lastString.toString());
          }
          lastXMIExtension = lastXMIExtension.getParent();
        }
        else if (o instanceof MCallAction)
        {
          postprocessCallActionMain(p_name);
        }
        else if (o instanceof MTerminateAction)
        {
          postprocessTerminateActionMain(p_name);
        }
        else if (o instanceof MComment)
        {
          postprocessCommentMain(p_name);
        }
        else if (o instanceof MDataType)
        {
          postprocessDataTypeMain(p_name);
        }
        else if (o instanceof MDestroyAction)
        {
          postprocessDestroyActionMain(p_name);
        }
        else if (o instanceof MArgListsExpressionEditor)
        {
          postprocessArgListsExpressionMain(p_name);
        }
        else if (o instanceof MExtend)
        {
          postprocessExtendMain(p_name);
        }
        else if (o instanceof MUsage)
        {
          postprocessUsageMain(p_name);
        }
        else if (o instanceof MPermission)
        {
          postprocessPermissionMain(p_name);
        }
        else if (o instanceof MActionSequence)
        {
          postprocessActionSequenceMain(p_name);
        }
        else if (o instanceof MCollaboration)
        {
          postprocessCollaborationMain(p_name);
        }
        else if (o instanceof MAssociationRole)
        {
          postprocessAssociationRoleMain(p_name);
        }
        else if (o instanceof MAttribute)
        {
          postprocessAttributeMain(p_name);
        }
        else if (o instanceof MMessage)
        {
          postprocessMessageMain(p_name);
        }
        else if (o instanceof MBooleanExpressionEditor)
        {
          postprocessBooleanExpressionMain(p_name);
        }
        else if (o instanceof MPseudostate)
        {
          postprocessPseudostateMain(p_name);
        }
        else if (o instanceof MTypeExpressionEditor)
        {
          postprocessTypeExpressionMain(p_name);
        }
        else if (o instanceof MClassifierInState)
        {
          postprocessClassifierInStateMain(p_name);
        }
        else if (o instanceof MStereotype)
        {
          postprocessStereotypeMain(p_name);
        }
        else if (o instanceof MSubsystem)
        {
          postprocessSubsystemMain(p_name);
        }
        else if (o instanceof MMappingExpressionEditor)
        {
          postprocessMappingExpressionMain(p_name);
        }
        else if (o instanceof MUseCase)
        {
          postprocessUseCaseMain(p_name);
        }
        else if (o instanceof MModel)
        {
          postprocessModelMain(p_name);
        }
        else if (o instanceof MLinkObject)
        {
          postprocessLinkObjectMain(p_name);
        }
        else if (o instanceof MLinkEnd)
        {
          postprocessLinkEndMain(p_name);
        }
        else if (o instanceof MTemplateParameter)
        {
          postprocessTemplateParameterMain(p_name);
        }
        else if (o instanceof MCreateAction)
        {
          postprocessCreateActionMain(p_name);
        }
        else if (o instanceof MPartition)
        {
          postprocessPartitionMain(p_name);
        }
        else if (o instanceof MAssociationEndRole)
        {
          postprocessAssociationEndRoleMain(p_name);
        }
        else if (o instanceof MAttributeLink)
        {
          postprocessAttributeLinkMain(p_name);
        }
        else if (o instanceof MSendAction)
        {
          postprocessSendActionMain(p_name);
        }
        else if (o instanceof MCallEvent)
        {
          postprocessCallEventMain(p_name);
        }
        else if (o instanceof MUninterpretedAction)
        {
          postprocessUninterpretedActionMain(p_name);
        }
        else if (o instanceof MSubactivityState)
        {
          postprocessSubactivityStateMain(p_name);
        }
        else if (o instanceof MTransition)
        {
          postprocessTransitionMain(p_name);
        }
        else if (o instanceof MOperation)
        {
          postprocessOperationMain(p_name);
        }
        else if (o instanceof MClassifierRole)
        {
          postprocessClassifierRoleMain(p_name);
        }
        else if (o instanceof MTimeExpressionEditor)
        {
          postprocessTimeExpressionMain(p_name);
        }
        else if (o instanceof MObjectFlowState)
        {
          postprocessObjectFlowStateMain(p_name);
        }
        else if (o instanceof MStimulus)
        {
          postprocessStimulusMain(p_name);
        }
        else if (o instanceof MSynchState)
        {
          postprocessSynchStateMain(p_name);
        }
        else if (o instanceof MInclude)
        {
          postprocessIncludeMain(p_name);
        }
        else if (o instanceof MArgument)
        {
          postprocessArgumentMain(p_name);
        }
        else if (o instanceof MComponent)
        {
          postprocessComponentMain(p_name);
        }
        else if (o instanceof MAssociationClass)
        {
          postprocessAssociationClassMain(p_name);
        }
        else if (o instanceof MActor)
        {
          postprocessActorMain(p_name);
        }
        else if (o instanceof MSignalEvent)
        {
          postprocessSignalEventMain(p_name);
        }
        else if (o instanceof MMethod)
        {
          postprocessMethodMain(p_name);
        }
        else if (o instanceof MChangeEvent)
        {
          postprocessChangeEventMain(p_name);
        }
        else if (o instanceof MParameter)
        {
          postprocessParameterMain(p_name);
        }
        else if (o instanceof MTaggedValue)
        {
          postprocessTaggedValueMain(p_name);
        }
        else if (o instanceof MTimeEvent)
        {
          postprocessTimeEventMain(p_name);
        }
        else if (o instanceof MUseCaseInstance)
        {
          postprocessUseCaseInstanceMain(p_name);
        }
        else if (o instanceof MStubState)
        {
          postprocessStubStateMain(p_name);
        }
        else if (o instanceof MNode)
        {
          postprocessNodeMain(p_name);
        }
        else if (o instanceof MNodeInstance)
        {
          postprocessNodeInstanceMain(p_name);
        }
        else if (o instanceof MElementResidence)
        {
          postprocessElementResidenceMain(p_name);
        }
        else if (o instanceof MGeneralization)
        {
          postprocessGeneralizationMain(p_name);
        }
        else if (o instanceof MReturnAction)
        {
          postprocessReturnActionMain(p_name);
        }
        else if (o instanceof MException)
        {
          postprocessExceptionMain(p_name);
        }
        else if (o instanceof MAbstraction)
        {
          postprocessAbstractionMain(p_name);
        }
        else if (o instanceof MConstraint)
        {
          postprocessConstraintMain(p_name);
        }
        else if (o instanceof MIterationExpressionEditor)
        {
          postprocessIterationExpressionMain(p_name);
        }
        else if (o instanceof MObjectSetExpressionEditor)
        {
          postprocessObjectSetExpressionMain(p_name);
        }
        else if (o instanceof MReception)
        {
          postprocessReceptionMain(p_name);
        }
        else if (o instanceof MGuard)
        {
          postprocessGuardMain(p_name);
        }
        else if (o instanceof MComponentInstance)
        {
          postprocessComponentInstanceMain(p_name);
        }
        else if (o instanceof MBinding)
        {
          postprocessBindingMain(p_name);
        }
        else if (o instanceof MFlow)
        {
          postprocessFlowMain(p_name);
        }
        else if (o instanceof MElementImport)
        {
          postprocessElementImportMain(p_name);
        }
        else if (o instanceof MProcedureExpressionEditor)
        {
          postprocessProcedureExpressionMain(p_name);
        }
        else if (o instanceof MMultiplicityRangeEditor)
        {
          postprocessMultiplicityRangeMain(p_name);
        }
        else if (o instanceof MActivityGraph)
        {
          postprocessActivityGraphMain(p_name);
        }
        else if (o instanceof MExtensionPoint)
        {
          postprocessExtensionPointMain(p_name);
        }
        else if (o instanceof MFinalState)
        {
          postprocessFinalStateMain(p_name);
        }
        else if (o instanceof MActionExpressionEditor)
        {
          postprocessActionExpressionMain(p_name);
        }
        else if (o instanceof MCallState)
        {
          postprocessCallStateMain(p_name);
        }
        else if (o instanceof MDataValue)
        {
          postprocessDataValueMain(p_name);
        }
        else if (o instanceof MInteraction)
        {
          postprocessInteractionMain(p_name);
        }
        else if (o instanceof MInterface)
        {
          postprocessInterfaceMain(p_name);
        }
        else if (o instanceof MMultiplicityEditor)
        {
          postprocessMultiplicityMain(p_name);
        }
        else if (o instanceof MSignal)
        {
          postprocessSignalMain(p_name);
        }
        else if (o instanceof MActionState)
        {
          postprocessActionStateMain(p_name);
        }
        else if (o instanceof MClass)
        {
          postprocessClassMain(p_name);
        }
        else if (o instanceof MAssociation)
        {
          postprocessAssociationMain(p_name);
        }
        else if (o instanceof MPackage)
        {
          postprocessPackageMain(p_name);
        }
        else if (o instanceof MAssociationEnd)
        {
          postprocessAssociationEndMain(p_name);
        }
        else if (o instanceof MDependency)
        {
          postprocessDependencyMain(p_name);
        }
        else if (o instanceof MObject)
        {
          postprocessObjectMain(p_name);
        }
        else if (o instanceof MStateMachine)
        {
          postprocessStateMachineMain(p_name);
        }
        else if (o instanceof MSubmachineState)
        {
          postprocessSubmachineStateMain(p_name);
        }
        else if (o instanceof MLink)
        {
          postprocessLinkMain(p_name);
        }
        else if (o instanceof MAction)
        {
          postprocessActionMain(p_name);
        }
        else if (o instanceof MExpressionEditor)
        {
          postprocessExpressionMain(p_name);
        }
        else if (o instanceof MCompositeState)
        {
          postprocessCompositeStateMain(p_name);
        }
        else if (o instanceof MInstance)
        {
          postprocessInstanceMain(p_name);
        }
        else if (o instanceof MClassifier)
        {
          postprocessClassifierMain(p_name);
        }
        else if (o instanceof MSimpleState)
        {
          postprocessSimpleStateMain(p_name);
        }
        else if (o instanceof MRelationship)
        {
          postprocessRelationshipMain(p_name);
        }
        else if (o instanceof MState)
        {
          postprocessStateMain(p_name);
        }
        else if (o instanceof MNamespace)
        {
          postprocessNamespaceMain(p_name);
        }
      }
      catch (ClassCastException ex)
      {
      }
      if (!bMultiple)
      {
        lastObject = liStack.get(liStack.size()-1);
        liStack.remove(liStack.size()-1);
        liNameStack.remove(liNameStack.size()-1);
      }
      lastString = new StringBuffer();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public Object process(String p_name, AttributeList p_attrs)
  {
    boolean ref = false;
    if (null != p_attrs)
    {
      ref = (null != p_attrs.getValue("xmi.idref")) ||
        (null != p_attrs.getValue("xmi.uuidref"));
    }

    String lastName = null;
    String nodeName = p_name;

    if (nodeName.startsWith("Model_Management.",0))
    {
      lastName = nodeName.substring(17);

      if (lastName.equals("ElementImport"))
      {
        if (ref)
        {
          Object o = getObjectByRef(p_attrs);
          if (null == o || this == o)
          {
            putObjectByRef(p_attrs, this);
            Link l = new Link();
            l.parameterXMIID = p_attrs.getValue("xmi.idref");
            l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
            return l;
          }

          MElementImport o2 = (MElementImport)o;
          String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

          if (null != nodeXMIUUID)
          {
            o2.setUUID(nodeXMIUUID);
          }
          putObjectByRef(p_attrs, o2);

          return o2;
        }
        else
        {
          MElementImport o2;
          Object o = getObject(p_attrs);
          if (null == o || this == o)
          {
            o2 = factory.createElementImport();
          }
          else
          {
            o2 = (MElementImport)o;
          }

          String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

          if (null != nodeXMIUUID)
          {
            o2.setUUID(nodeXMIUUID);
          }
          putObject(p_attrs, o2);

          return o2;
        }
      }

      if (lastName.equals("Subsystem"))
      {
        if (ref)
        {
          Object o = getObjectByRef(p_attrs);
          if (null == o || this == o)
          {
            putObjectByRef(p_attrs, this);
            Link l = new Link();
            l.parameterXMIID = p_attrs.getValue("xmi.idref");
            l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
            return l;
          }

          MSubsystem o2 = (MSubsystem)o;
          String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

          if (null != nodeXMIUUID)
          {
            o2.setUUID(nodeXMIUUID);
          }
          putObjectByRef(p_attrs, o2);

          return o2;
        }
        else
        {
          MSubsystem o2;
          Object o = getObject(p_attrs);
          if (null == o || this == o)
          {
            o2 = factory.createSubsystem();
          }
          else
          {
            o2 = (MSubsystem)o;
          }

          String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

          if (null != nodeXMIUUID)
          {
            o2.setUUID(nodeXMIUUID);
          }
          putObject(p_attrs, o2);

          return o2;
        }
      }

      if (lastName.equals("Package"))
      {
        if (ref)
        {
          Object o = getObjectByRef(p_attrs);
          if (null == o || this == o)
          {
            putObjectByRef(p_attrs, this);
            Link l = new Link();
            l.parameterXMIID = p_attrs.getValue("xmi.idref");
            l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
            return l;
          }

          MPackage o2 = (MPackage)o;
          String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

          if (null != nodeXMIUUID)
          {
            o2.setUUID(nodeXMIUUID);
          }
          putObjectByRef(p_attrs, o2);

          return o2;
        }
        else
        {
          MPackage o2;
          Object o = getObject(p_attrs);
          if (null == o || this == o)
          {
            o2 = factory.createPackage();
          }
          else
          {
            o2 = (MPackage)o;
          }

          String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

          if (null != nodeXMIUUID)
          {
            o2.setUUID(nodeXMIUUID);
          }
          putObject(p_attrs, o2);

          return o2;
        }
      }

      if (lastName.equals("Model"))
      {
        if (ref)
        {
          Object o = getObjectByRef(p_attrs);
          if (null == o || this == o)
          {
            putObjectByRef(p_attrs, this);
            Link l = new Link();
            l.parameterXMIID = p_attrs.getValue("xmi.idref");
            l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
            return l;
          }

          MModel o2 = (MModel)o;
          if (null == rootModel)
          {
            rootModel = o2;
          }
          String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

          if (null != nodeXMIUUID)
          {
            o2.setUUID(nodeXMIUUID);
          }
          putObjectByRef(p_attrs, o2);

          return o2;
        }
        else
        {
          MModel o2;
          Object o = getObject(p_attrs);
          if (null == o || this == o)
          {
            o2 = factory.createModel();
            if (null == rootModel)
            {
              rootModel = o2;
            }
          }
          else
          {
            o2 = (MModel)o;
          }

          String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

          if (null != nodeXMIUUID)
          {
            o2.setUUID(nodeXMIUUID);
          }
          putObject(p_attrs, o2);

          return o2;
        }
      }

    }
    if (nodeName.startsWith("Standard_Elements.",0))
    {
    }
    if (nodeName.startsWith("Behavioral_Elements.",0))
    {
      if (nodeName.startsWith("Activity_Graphs.",20))
      {
        lastName = nodeName.substring(36);

        if (lastName.equals("ActionState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MActionState o2 = (MActionState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MActionState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createActionState();
            }
            else
            {
              o2 = (MActionState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ClassifierInState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MClassifierInState o2 = (MClassifierInState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MClassifierInState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createClassifierInState();
            }
            else
            {
              o2 = (MClassifierInState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ObjectFlowState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MObjectFlowState o2 = (MObjectFlowState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MObjectFlowState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createObjectFlowState();
            }
            else
            {
              o2 = (MObjectFlowState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("CallState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MCallState o2 = (MCallState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MCallState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createCallState();
            }
            else
            {
              o2 = (MCallState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("SubactivityState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MSubactivityState o2 = (MSubactivityState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MSubactivityState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createSubactivityState();
            }
            else
            {
              o2 = (MSubactivityState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Partition"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MPartition o2 = (MPartition)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MPartition o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createPartition();
            }
            else
            {
              o2 = (MPartition)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ActivityGraph"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MActivityGraph o2 = (MActivityGraph)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MActivityGraph o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createActivityGraph();
            }
            else
            {
              o2 = (MActivityGraph)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

      }
      if (nodeName.startsWith("Common_Behavior.",20))
      {
        lastName = nodeName.substring(36);

        if (lastName.equals("NodeInstance"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MNodeInstance o2 = (MNodeInstance)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MNodeInstance o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createNodeInstance();
            }
            else
            {
              o2 = (MNodeInstance)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ComponentInstance"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MComponentInstance o2 = (MComponentInstance)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MComponentInstance o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createComponentInstance();
            }
            else
            {
              o2 = (MComponentInstance)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Exception"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MException o2 = (MException)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MException o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createException();
            }
            else
            {
              o2 = (MException)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Stimulus"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MStimulus o2 = (MStimulus)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MStimulus o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createStimulus();
            }
            else
            {
              o2 = (MStimulus)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("TerminateAction"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MTerminateAction o2 = (MTerminateAction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MTerminateAction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createTerminateAction();
            }
            else
            {
              o2 = (MTerminateAction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ReturnAction"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MReturnAction o2 = (MReturnAction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MReturnAction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createReturnAction();
            }
            else
            {
              o2 = (MReturnAction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("LinkEnd"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MLinkEnd o2 = (MLinkEnd)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MLinkEnd o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createLinkEnd();
            }
            else
            {
              o2 = (MLinkEnd)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Link"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MLink o2 = (MLink)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MLink o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createLink();
            }
            else
            {
              o2 = (MLink)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Reception"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MReception o2 = (MReception)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MReception o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createReception();
            }
            else
            {
              o2 = (MReception)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Argument"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MArgument o2 = (MArgument)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MArgument o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createArgument();
            }
            else
            {
              o2 = (MArgument)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ActionSequence"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MActionSequence o2 = (MActionSequence)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MActionSequence o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createActionSequence();
            }
            else
            {
              o2 = (MActionSequence)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("SendAction"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MSendAction o2 = (MSendAction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MSendAction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createSendAction();
            }
            else
            {
              o2 = (MSendAction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("CallAction"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MCallAction o2 = (MCallAction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MCallAction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createCallAction();
            }
            else
            {
              o2 = (MCallAction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("DataValue"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MDataValue o2 = (MDataValue)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MDataValue o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createDataValue();
            }
            else
            {
              o2 = (MDataValue)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Object"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MObject o2 = (MObject)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MObject o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createObject();
            }
            else
            {
              o2 = (MObject)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("LinkObject"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MLinkObject o2 = (MLinkObject)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MLinkObject o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createLinkObject();
            }
            else
            {
              o2 = (MLinkObject)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("AttributeLink"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MAttributeLink o2 = (MAttributeLink)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MAttributeLink o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createAttributeLink();
            }
            else
            {
              o2 = (MAttributeLink)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Action"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MAction o2 = (MAction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MAction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createAction();
            }
            else
            {
              o2 = (MAction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("UninterpretedAction"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MUninterpretedAction o2 = (MUninterpretedAction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MUninterpretedAction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createUninterpretedAction();
            }
            else
            {
              o2 = (MUninterpretedAction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("DestroyAction"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MDestroyAction o2 = (MDestroyAction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MDestroyAction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createDestroyAction();
            }
            else
            {
              o2 = (MDestroyAction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("CreateAction"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MCreateAction o2 = (MCreateAction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MCreateAction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createCreateAction();
            }
            else
            {
              o2 = (MCreateAction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Signal"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MSignal o2 = (MSignal)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MSignal o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createSignal();
            }
            else
            {
              o2 = (MSignal)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Instance"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MInstance o2 = (MInstance)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MInstance o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createInstance();
            }
            else
            {
              o2 = (MInstance)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

      }
      if (nodeName.startsWith("Collaborations.",20))
      {
        lastName = nodeName.substring(35);

        if (lastName.equals("Interaction"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MInteraction o2 = (MInteraction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MInteraction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createInteraction();
            }
            else
            {
              o2 = (MInteraction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Message"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MMessage o2 = (MMessage)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MMessage o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createMessage();
            }
            else
            {
              o2 = (MMessage)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("AssociationEndRole"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MAssociationEndRole o2 = (MAssociationEndRole)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MAssociationEndRole o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createAssociationEndRole();
            }
            else
            {
              o2 = (MAssociationEndRole)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("AssociationRole"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MAssociationRole o2 = (MAssociationRole)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MAssociationRole o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createAssociationRole();
            }
            else
            {
              o2 = (MAssociationRole)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ClassifierRole"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MClassifierRole o2 = (MClassifierRole)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MClassifierRole o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createClassifierRole();
            }
            else
            {
              o2 = (MClassifierRole)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Collaboration"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MCollaboration o2 = (MCollaboration)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MCollaboration o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createCollaboration();
            }
            else
            {
              o2 = (MCollaboration)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

      }
      if (nodeName.startsWith("State_Machines.",20))
      {
        lastName = nodeName.substring(35);

        if (lastName.equals("FinalState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MFinalState o2 = (MFinalState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MFinalState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createFinalState();
            }
            else
            {
              o2 = (MFinalState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("StubState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MStubState o2 = (MStubState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MStubState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createStubState();
            }
            else
            {
              o2 = (MStubState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("SynchState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MSynchState o2 = (MSynchState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MSynchState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createSynchState();
            }
            else
            {
              o2 = (MSynchState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("SubmachineState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MSubmachineState o2 = (MSubmachineState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MSubmachineState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createSubmachineState();
            }
            else
            {
              o2 = (MSubmachineState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("SimpleState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MSimpleState o2 = (MSimpleState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MSimpleState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createSimpleState();
            }
            else
            {
              o2 = (MSimpleState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Pseudostate"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MPseudostate o2 = (MPseudostate)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MPseudostate o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createPseudostate();
            }
            else
            {
              o2 = (MPseudostate)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Guard"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MGuard o2 = (MGuard)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MGuard o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createGuard();
            }
            else
            {
              o2 = (MGuard)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ChangeEvent"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MChangeEvent o2 = (MChangeEvent)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MChangeEvent o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createChangeEvent();
            }
            else
            {
              o2 = (MChangeEvent)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("CompositeState"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MCompositeState o2 = (MCompositeState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MCompositeState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createCompositeState();
            }
            else
            {
              o2 = (MCompositeState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("StateVertex"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
        }

        if (lastName.equals("Transition"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MTransition o2 = (MTransition)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MTransition o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createTransition();
            }
            else
            {
              o2 = (MTransition)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("SignalEvent"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MSignalEvent o2 = (MSignalEvent)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MSignalEvent o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createSignalEvent();
            }
            else
            {
              o2 = (MSignalEvent)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("CallEvent"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MCallEvent o2 = (MCallEvent)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MCallEvent o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createCallEvent();
            }
            else
            {
              o2 = (MCallEvent)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("TimeEvent"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MTimeEvent o2 = (MTimeEvent)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MTimeEvent o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createTimeEvent();
            }
            else
            {
              o2 = (MTimeEvent)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("State"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MState o2 = (MState)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MState o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createState();
            }
            else
            {
              o2 = (MState)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Event"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
        }

        if (lastName.equals("StateMachine"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MStateMachine o2 = (MStateMachine)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MStateMachine o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createStateMachine();
            }
            else
            {
              o2 = (MStateMachine)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

      }
      if (nodeName.startsWith("Use_Cases.",20))
      {
        lastName = nodeName.substring(30);

        if (lastName.equals("ExtensionPoint"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MExtensionPoint o2 = (MExtensionPoint)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MExtensionPoint o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createExtensionPoint();
            }
            else
            {
              o2 = (MExtensionPoint)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Include"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MInclude o2 = (MInclude)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MInclude o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createInclude();
            }
            else
            {
              o2 = (MInclude)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Extend"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MExtend o2 = (MExtend)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MExtend o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createExtend();
            }
            else
            {
              o2 = (MExtend)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("UseCaseInstance"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MUseCaseInstance o2 = (MUseCaseInstance)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MUseCaseInstance o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createUseCaseInstance();
            }
            else
            {
              o2 = (MUseCaseInstance)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Actor"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MActor o2 = (MActor)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MActor o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createActor();
            }
            else
            {
              o2 = (MActor)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("UseCase"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MUseCase o2 = (MUseCase)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MUseCase o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createUseCase();
            }
            else
            {
              o2 = (MUseCase)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

      }
    }
    if (nodeName.startsWith("Foundation.",0))
    {
      if (nodeName.startsWith("Extension_Mechanisms.",11))
      {
        lastName = nodeName.substring(32);

        if (lastName.equals("TaggedValue"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MTaggedValue o2 = (MTaggedValue)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MTaggedValue o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createTaggedValue();
            }
            else
            {
              o2 = (MTaggedValue)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Stereotype"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MStereotype o2 = (MStereotype)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MStereotype o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createStereotype();
            }
            else
            {
              o2 = (MStereotype)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

      }
      if (nodeName.startsWith("Data_Types.",11))
      {
        lastName = nodeName.substring(22);

        if (lastName.equals("ProcedureExpression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MProcedureExpressionEditor o = new MProcedureExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("MappingExpression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MMappingExpressionEditor o = new MMappingExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("ArgListsExpression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MArgListsExpressionEditor o = new MArgListsExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("TypeExpression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MTypeExpressionEditor o = new MTypeExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("IterationExpression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MIterationExpressionEditor o = new MIterationExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("MultiplicityRange"))
        {
          if (ref)
          {
            throw new IllegalArgumentException("Reference to MultiplicityRange not supported!!!");
          }
          else
          {
            MMultiplicityRangeEditor o = new MMultiplicityRangeEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("ActionExpression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MActionExpressionEditor o = new MActionExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("BooleanExpression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MBooleanExpressionEditor o = new MBooleanExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("Expression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MExpressionEditor o = new MExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("TimeExpression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MTimeExpressionEditor o = new MTimeExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("ObjectSetExpression"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MObjectSetExpressionEditor o = new MObjectSetExpressionEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

        if (lastName.equals("Multiplicity"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
          else
          {
            MMultiplicityEditor o = new MMultiplicityEditor();
            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            putObject(p_attrs, o);

            return o;
          }
        }

      }
      if (nodeName.startsWith("Core.",11))
      {
        lastName = nodeName.substring(16);

        if (lastName.equals("TemplateParameter"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MTemplateParameter o2 = (MTemplateParameter)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MTemplateParameter o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createTemplateParameter();
            }
            else
            {
              o2 = (MTemplateParameter)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ElementResidence"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MElementResidence o2 = (MElementResidence)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MElementResidence o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createElementResidence();
            }
            else
            {
              o2 = (MElementResidence)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Relationship"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MRelationship o2 = (MRelationship)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MRelationship o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createRelationship();
            }
            else
            {
              o2 = (MRelationship)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Flow"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MFlow o2 = (MFlow)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MFlow o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createFlow();
            }
            else
            {
              o2 = (MFlow)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Comment"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MComment o2 = (MComment)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MComment o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createComment();
            }
            else
            {
              o2 = (MComment)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Permission"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MPermission o2 = (MPermission)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MPermission o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createPermission();
            }
            else
            {
              o2 = (MPermission)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Node"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MNode o2 = (MNode)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MNode o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createNode();
            }
            else
            {
              o2 = (MNode)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Component"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MComponent o2 = (MComponent)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MComponent o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createComponent();
            }
            else
            {
              o2 = (MComponent)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Binding"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MBinding o2 = (MBinding)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MBinding o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createBinding();
            }
            else
            {
              o2 = (MBinding)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Usage"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MUsage o2 = (MUsage)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MUsage o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createUsage();
            }
            else
            {
              o2 = (MUsage)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("PresentationElement"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
        }

        if (lastName.equals("Abstraction"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MAbstraction o2 = (MAbstraction)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MAbstraction o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createAbstraction();
            }
            else
            {
              o2 = (MAbstraction)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Dependency"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MDependency o2 = (MDependency)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MDependency o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createDependency();
            }
            else
            {
              o2 = (MDependency)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("ModelElement"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
        }

        if (lastName.equals("BehavioralFeature"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
        }

        if (lastName.equals("Feature"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
        }

        if (lastName.equals("AssociationClass"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MAssociationClass o2 = (MAssociationClass)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MAssociationClass o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createAssociationClass();
            }
            else
            {
              o2 = (MAssociationClass)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Generalization"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MGeneralization o2 = (MGeneralization)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MGeneralization o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createGeneralization();
            }
            else
            {
              o2 = (MGeneralization)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Method"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MMethod o2 = (MMethod)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MMethod o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createMethod();
            }
            else
            {
              o2 = (MMethod)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Parameter"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MParameter o2 = (MParameter)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MParameter o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createParameter();
            }
            else
            {
              o2 = (MParameter)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Operation"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MOperation o2 = (MOperation)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MOperation o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createOperation();
            }
            else
            {
              o2 = (MOperation)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Attribute"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MAttribute o2 = (MAttribute)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MAttribute o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createAttribute();
            }
            else
            {
              o2 = (MAttribute)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("GeneralizableElement"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
        }

        if (lastName.equals("Element"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
        }

        if (lastName.equals("Association"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MAssociation o2 = (MAssociation)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MAssociation o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createAssociation();
            }
            else
            {
              o2 = (MAssociation)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Constraint"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MConstraint o2 = (MConstraint)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MConstraint o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createConstraint();
            }
            else
            {
              o2 = (MConstraint)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Interface"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MInterface o2 = (MInterface)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MInterface o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createInterface();
            }
            else
            {
              o2 = (MInterface)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("AssociationEnd"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MAssociationEnd o2 = (MAssociationEnd)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MAssociationEnd o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createAssociationEnd();
            }
            else
            {
              o2 = (MAssociationEnd)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Namespace"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MNamespace o2 = (MNamespace)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MNamespace o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createNamespace();
            }
            else
            {
              o2 = (MNamespace)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("StructuralFeature"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            return o;
          }
        }

        if (lastName.equals("DataType"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MDataType o2 = (MDataType)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MDataType o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createDataType();
            }
            else
            {
              o2 = (MDataType)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Class"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MClass o2 = (MClass)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MClass o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createClass();
            }
            else
            {
              o2 = (MClass)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

        if (lastName.equals("Classifier"))
        {
          if (ref)
          {
            Object o = getObjectByRef(p_attrs);
            if (null == o || this == o)
            {
              putObjectByRef(p_attrs, this);
              Link l = new Link();
              l.parameterXMIID = p_attrs.getValue("xmi.idref");
              l.parameterXMIUUID = p_attrs.getValue("xmi.uuidref");
              return l;
            }

            MClassifier o2 = (MClassifier)o;
            String nodeXMIUUID = p_attrs.getValue("xmi.uuidref");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObjectByRef(p_attrs, o2);

            return o2;
          }
          else
          {
            MClassifier o2;
            Object o = getObject(p_attrs);
            if (null == o || this == o)
            {
              o2 = factory.createClassifier();
            }
            else
            {
              o2 = (MClassifier)o;
            }

            String nodeXMIUUID = p_attrs.getValue("xmi.uuid");

            if (null != nodeXMIUUID)
            {
              o2.setUUID(nodeXMIUUID);
            }
            putObject(p_attrs, o2);

            return o2;
          }
        }

      }
    }

    return null;
  }
  public boolean processCommentMain(String p_name, AttributeList p_attrs)
  {
    MComment o = (MComment)liStack.get(liStack.size()-1);

    if (processCommentAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processCommentRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessCommentMain(String p_name)
  {
    MComment o = (MComment)liStack.get(liStack.size()-2);

    if (postprocessCommentAttributes(p_name, o))
    {
      return;
    }

    if (postprocessCommentRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processCommentAttributes(String p_name, AttributeList p_attrs, MComment o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessCommentAttributes(String p_name, MComment o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processCommentRoles(String p_name, AttributeList p_attrs, MComment o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Comment."))
    {
      String lastName = p_name.substring(24);

      if (lastName.equals("annotatedElement"))
      {
        lastMethod = "annotatedElement";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessCommentRoles(String p_name, MComment o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Comment."))
    {
      String lastName = p_name.substring(24);

      if (lastName.equals("annotatedElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addAnnotatedElement(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processTerminateActionMain(String p_name, AttributeList p_attrs)
  {
    MTerminateAction o = (MTerminateAction)liStack.get(liStack.size()-1);

    if (processTerminateActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processTerminateActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessTerminateActionMain(String p_name)
  {
    MTerminateAction o = (MTerminateAction)liStack.get(liStack.size()-2);

    if (postprocessTerminateActionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessTerminateActionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processTerminateActionAttributes(String p_name, AttributeList p_attrs, MTerminateAction o)
  {
    if (processActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessTerminateActionAttributes(String p_name, MTerminateAction o)
  {
    if (postprocessActionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processTerminateActionRoles(String p_name, AttributeList p_attrs, MTerminateAction o)
  {
    if (processActionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessTerminateActionRoles(String p_name, MTerminateAction o)
  {
    if (postprocessActionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processClassMain(String p_name, AttributeList p_attrs)
  {
    MClass o = (MClass)liStack.get(liStack.size()-1);

    if (processClassAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processClassRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessClassMain(String p_name)
  {
    MClass o = (MClass)liStack.get(liStack.size()-2);

    if (postprocessClassAttributes(p_name, o))
    {
      return;
    }

    if (postprocessClassRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processClassAttributes(String p_name, AttributeList p_attrs, MClass o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Class."))
    {
      String lastName = p_name.substring(22);

      if (lastName.equals("isActive"))
      {
        lastMethod = "isActive";
        lastMethodType = true;
        o.setActive(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessClassAttributes(String p_name, MClass o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Class."))
    {
      String lastName = p_name.substring(22);

      if (lastName.equals("isActive"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processClassRoles(String p_name, AttributeList p_attrs, MClass o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessClassRoles(String p_name, MClass o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processRelationshipMain(String p_name, AttributeList p_attrs)
  {
    MRelationship o = (MRelationship)liStack.get(liStack.size()-1);

    if (processRelationshipAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processRelationshipRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessRelationshipMain(String p_name)
  {
    MRelationship o = (MRelationship)liStack.get(liStack.size()-2);

    if (postprocessRelationshipAttributes(p_name, o))
    {
      return;
    }

    if (postprocessRelationshipRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processRelationshipAttributes(String p_name, AttributeList p_attrs, MRelationship o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessRelationshipAttributes(String p_name, MRelationship o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processRelationshipRoles(String p_name, AttributeList p_attrs, MRelationship o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessRelationshipRoles(String p_name, MRelationship o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processActorMain(String p_name, AttributeList p_attrs)
  {
    MActor o = (MActor)liStack.get(liStack.size()-1);

    if (processActorAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processActorRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessActorMain(String p_name)
  {
    MActor o = (MActor)liStack.get(liStack.size()-2);

    if (postprocessActorAttributes(p_name, o))
    {
      return;
    }

    if (postprocessActorRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processActorAttributes(String p_name, AttributeList p_attrs, MActor o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessActorAttributes(String p_name, MActor o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processActorRoles(String p_name, AttributeList p_attrs, MActor o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessActorRoles(String p_name, MActor o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processExtensionPointMain(String p_name, AttributeList p_attrs)
  {
    MExtensionPoint o = (MExtensionPoint)liStack.get(liStack.size()-1);

    if (processExtensionPointAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processExtensionPointRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessExtensionPointMain(String p_name)
  {
    MExtensionPoint o = (MExtensionPoint)liStack.get(liStack.size()-2);

    if (postprocessExtensionPointAttributes(p_name, o))
    {
      return;
    }

    if (postprocessExtensionPointRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processExtensionPointAttributes(String p_name, AttributeList p_attrs, MExtensionPoint o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.ExtensionPoint."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("location"))
      {
        lastMethod = "location";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessExtensionPointAttributes(String p_name, MExtensionPoint o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.ExtensionPoint."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("location"))
      {
        o.setLocation(lastString.toString());
        return true;
      }

    }
    return false;
  }

  public boolean processExtensionPointRoles(String p_name, AttributeList p_attrs, MExtensionPoint o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.ExtensionPoint."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("extend"))
      {
        lastMethod = "extend";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("useCase"))
      {
        lastMethod = "useCase";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessExtensionPointRoles(String p_name, MExtensionPoint o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.ExtensionPoint."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("extend"))
      {
        MExtend el = (MExtend)lastObject;
        if (null != el)
        {
          o.addExtend(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("useCase"))
      {
        MUseCase el = (MUseCase)lastObject;
        if (null != el)
        {
          o.setUseCase(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processTimeEventMain(String p_name, AttributeList p_attrs)
  {
    MTimeEvent o = (MTimeEvent)liStack.get(liStack.size()-1);

    if (processTimeEventAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processTimeEventRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessTimeEventMain(String p_name)
  {
    MTimeEvent o = (MTimeEvent)liStack.get(liStack.size()-2);

    if (postprocessTimeEventAttributes(p_name, o))
    {
      return;
    }

    if (postprocessTimeEventRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processTimeEventAttributes(String p_name, AttributeList p_attrs, MTimeEvent o)
  {
    if (processEventAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.TimeEvent."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("when"))
      {
        lastMethod = "when";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessTimeEventAttributes(String p_name, MTimeEvent o)
  {
    if (postprocessEventAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.TimeEvent."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("when"))
      {
        if (!(lastObject instanceof Link))
        {
          MTimeExpression oexp = null;
          if (lastObject instanceof MTimeExpression)
          {
            oexp = (MTimeExpression)lastObject;
            o.setWhen(oexp);
          }
          else
          {
            oexp = ((MTimeExpressionEditor)lastObject).toTimeExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setWhen(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processTimeEventRoles(String p_name, AttributeList p_attrs, MTimeEvent o)
  {
    if (processEventRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessTimeEventRoles(String p_name, MTimeEvent o)
  {
    if (postprocessEventRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processBehavioralFeatureAttributes(String p_name, AttributeList p_attrs, MBehavioralFeature o)
  {
    if (processFeatureAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.BehavioralFeature."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("isQuery"))
      {
        lastMethod = "isQuery";
        lastMethodType = true;
        o.setQuery(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessBehavioralFeatureAttributes(String p_name, MBehavioralFeature o)
  {
    if (postprocessFeatureAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.BehavioralFeature."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("isQuery"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processBehavioralFeatureRoles(String p_name, AttributeList p_attrs, MBehavioralFeature o)
  {
    if (processFeatureRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.BehavioralFeature."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("raisedSignal"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("parameter"))
      {
        lastMethod = "parameter";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessBehavioralFeatureRoles(String p_name, MBehavioralFeature o)
  {
    if (postprocessFeatureRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.BehavioralFeature."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("raisedSignal"))
      {
        MSignal el = (MSignal)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("parameter"))
      {
        MParameter el = (MParameter)lastObject;
        if (null != el)
        {
          o.addParameter(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processStateMachineMain(String p_name, AttributeList p_attrs)
  {
    MStateMachine o = (MStateMachine)liStack.get(liStack.size()-1);

    if (processStateMachineAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processStateMachineRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessStateMachineMain(String p_name)
  {
    MStateMachine o = (MStateMachine)liStack.get(liStack.size()-2);

    if (postprocessStateMachineAttributes(p_name, o))
    {
      return;
    }

    if (postprocessStateMachineRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processStateMachineAttributes(String p_name, AttributeList p_attrs, MStateMachine o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessStateMachineAttributes(String p_name, MStateMachine o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processStateMachineRoles(String p_name, AttributeList p_attrs, MStateMachine o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.StateMachine."))
    {
      String lastName = p_name.substring(48);

      if (lastName.equals("subMachineState"))
      {
        lastMethod = "submachineState";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("transitions"))
      {
        lastMethod = "transition";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("top"))
      {
        lastMethod = "top";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("context"))
      {
        lastMethod = "context";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessStateMachineRoles(String p_name, MStateMachine o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.StateMachine."))
    {
      String lastName = p_name.substring(48);

      if (lastName.equals("subMachineState"))
      {
        MSubmachineState el = (MSubmachineState)lastObject;
        if (null != el)
        {
          o.addSubmachineState(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("transitions"))
      {
        MTransition el = (MTransition)lastObject;
        if (null != el)
        {
          o.addTransition(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("top"))
      {
        MState el = (MState)lastObject;
        if (null != el)
        {
          o.setTop(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("context"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.setContext(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processEventAttributes(String p_name, AttributeList p_attrs, MEvent o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessEventAttributes(String p_name, MEvent o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processEventRoles(String p_name, AttributeList p_attrs, MEvent o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Event."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("transition"))
      {
        lastMethod = "transition";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("state"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("parameter"))
      {
        lastMethod = "parameter";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessEventRoles(String p_name, MEvent o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Event."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("transition"))
      {
        MTransition el = (MTransition)lastObject;
        if (null != el)
        {
          o.addTransition(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("state"))
      {
        MState el = (MState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("parameter"))
      {
        MParameter el = (MParameter)lastObject;
        if (null != el)
        {
          o.addParameter(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processUninterpretedActionMain(String p_name, AttributeList p_attrs)
  {
    MUninterpretedAction o = (MUninterpretedAction)liStack.get(liStack.size()-1);

    if (processUninterpretedActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processUninterpretedActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessUninterpretedActionMain(String p_name)
  {
    MUninterpretedAction o = (MUninterpretedAction)liStack.get(liStack.size()-2);

    if (postprocessUninterpretedActionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessUninterpretedActionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processUninterpretedActionAttributes(String p_name, AttributeList p_attrs, MUninterpretedAction o)
  {
    if (processActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessUninterpretedActionAttributes(String p_name, MUninterpretedAction o)
  {
    if (postprocessActionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processUninterpretedActionRoles(String p_name, AttributeList p_attrs, MUninterpretedAction o)
  {
    if (processActionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessUninterpretedActionRoles(String p_name, MUninterpretedAction o)
  {
    if (postprocessActionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processElementResidenceMain(String p_name, AttributeList p_attrs)
  {
    MElementResidence o = (MElementResidence)liStack.get(liStack.size()-1);

    if (processElementResidenceAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processElementResidenceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessElementResidenceMain(String p_name)
  {
    MElementResidence o = (MElementResidence)liStack.get(liStack.size()-2);

    if (postprocessElementResidenceAttributes(p_name, o))
    {
      return;
    }

    if (postprocessElementResidenceRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processElementResidenceAttributes(String p_name, AttributeList p_attrs, MElementResidence o)
  {
    if (p_name.startsWith("Foundation.Core.ElementResidence."))
    {
      String lastName = p_name.substring(33);

      if (lastName.equals("visibility"))
      {
        lastMethod = "visibility";
        lastMethodType = true;
        o.setVisibility(MVisibilityKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessElementResidenceAttributes(String p_name, MElementResidence o)
  {
    if (p_name.startsWith("Foundation.Core.ElementResidence."))
    {
      String lastName = p_name.substring(33);

      if (lastName.equals("visibility"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processElementResidenceRoles(String p_name, AttributeList p_attrs, MElementResidence o)
  {
    if (p_name.startsWith("Foundation.Core.ElementResidence."))
    {
      String lastName = p_name.substring(33);

      if (lastName.equals("resident"))
      {
        lastMethod = "resident";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("implementationLocation"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessElementResidenceRoles(String p_name, MElementResidence o)
  {
    if (p_name.startsWith("Foundation.Core.ElementResidence."))
    {
      String lastName = p_name.substring(33);

      if (lastName.equals("resident"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.setResident(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("implementationLocation"))
      {
        MComponent el = (MComponent)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processArgumentMain(String p_name, AttributeList p_attrs)
  {
    MArgument o = (MArgument)liStack.get(liStack.size()-1);

    if (processArgumentAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processArgumentRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessArgumentMain(String p_name)
  {
    MArgument o = (MArgument)liStack.get(liStack.size()-2);

    if (postprocessArgumentAttributes(p_name, o))
    {
      return;
    }

    if (postprocessArgumentRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processArgumentAttributes(String p_name, AttributeList p_attrs, MArgument o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Argument."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("value"))
      {
        lastMethod = "value";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessArgumentAttributes(String p_name, MArgument o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Argument."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("value"))
      {
        if (!(lastObject instanceof Link))
        {
          MExpression oexp = null;
          if (lastObject instanceof MExpression)
          {
            oexp = (MExpression)lastObject;
            o.setValue(oexp);
          }
          else
          {
            oexp = ((MExpressionEditor)lastObject).toExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setValue(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processArgumentRoles(String p_name, AttributeList p_attrs, MArgument o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Argument."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("action"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessArgumentRoles(String p_name, MArgument o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Argument."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("action"))
      {
        MAction el = (MAction)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processTransitionMain(String p_name, AttributeList p_attrs)
  {
    MTransition o = (MTransition)liStack.get(liStack.size()-1);

    if (processTransitionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processTransitionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessTransitionMain(String p_name)
  {
    MTransition o = (MTransition)liStack.get(liStack.size()-2);

    if (postprocessTransitionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessTransitionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processTransitionAttributes(String p_name, AttributeList p_attrs, MTransition o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessTransitionAttributes(String p_name, MTransition o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processTransitionRoles(String p_name, AttributeList p_attrs, MTransition o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Transition."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("target"))
      {
        lastMethod = "target";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("source"))
      {
        lastMethod = "source";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("stateMachine"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("trigger"))
      {
        lastMethod = "trigger";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("state"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("effect"))
      {
        lastMethod = "effect";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("guard"))
      {
        lastMethod = "guard";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessTransitionRoles(String p_name, MTransition o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Transition."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("target"))
      {
        MStateVertex el = (MStateVertex)lastObject;
        if (null != el)
        {
          o.setTarget(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("source"))
      {
        MStateVertex el = (MStateVertex)lastObject;
        if (null != el)
        {
          o.setSource(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("stateMachine"))
      {
        MStateMachine el = (MStateMachine)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("trigger"))
      {
        MEvent el = (MEvent)lastObject;
        if (null != el)
        {
          o.setTrigger(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("state"))
      {
        MState el = (MState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("effect"))
      {
        MAction el = (MAction)lastObject;
        if (null != el)
        {
          o.setEffect(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("guard"))
      {
        MGuard el = (MGuard)lastObject;
        if (null != el)
        {
          o.setGuard(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processLinkMain(String p_name, AttributeList p_attrs)
  {
    MLink o = (MLink)liStack.get(liStack.size()-1);

    if (processLinkAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processLinkRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessLinkMain(String p_name)
  {
    MLink o = (MLink)liStack.get(liStack.size()-2);

    if (postprocessLinkAttributes(p_name, o))
    {
      return;
    }

    if (postprocessLinkRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processLinkAttributes(String p_name, AttributeList p_attrs, MLink o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessLinkAttributes(String p_name, MLink o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processLinkRoles(String p_name, AttributeList p_attrs, MLink o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Link."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("stimulus"))
      {
        lastMethod = "stimulus";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("connection"))
      {
        lastMethod = "connection";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("association"))
      {
        lastMethod = "association";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessLinkRoles(String p_name, MLink o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Link."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("stimulus"))
      {
        MStimulus el = (MStimulus)lastObject;
        if (null != el)
        {
          o.addStimulus(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("connection"))
      {
        MLinkEnd el = (MLinkEnd)lastObject;
        if (null != el)
        {
          o.addConnection(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("association"))
      {
        MAssociation el = (MAssociation)lastObject;
        if (null != el)
        {
          o.setAssociation(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processFeatureAttributes(String p_name, AttributeList p_attrs, MFeature o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Feature."))
    {
      String lastName = p_name.substring(24);

      if (lastName.equals("ownerScope"))
      {
        lastMethod = "ownerScope";
        lastMethodType = true;
        o.setOwnerScope(MScopeKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessFeatureAttributes(String p_name, MFeature o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Feature."))
    {
      String lastName = p_name.substring(24);

      if (lastName.equals("ownerScope"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processFeatureRoles(String p_name, AttributeList p_attrs, MFeature o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Feature."))
    {
      String lastName = p_name.substring(24);

      if (lastName.equals("classifierRole"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("owner"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessFeatureRoles(String p_name, MFeature o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Feature."))
    {
      String lastName = p_name.substring(24);

      if (lastName.equals("classifierRole"))
      {
        MClassifierRole el = (MClassifierRole)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("owner"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processStateMain(String p_name, AttributeList p_attrs)
  {
    MState o = (MState)liStack.get(liStack.size()-1);

    if (processStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessStateMain(String p_name)
  {
    MState o = (MState)liStack.get(liStack.size()-2);

    if (postprocessStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processStateAttributes(String p_name, AttributeList p_attrs, MState o)
  {
    if (processStateVertexAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessStateAttributes(String p_name, MState o)
  {
    if (postprocessStateVertexAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processStateRoles(String p_name, AttributeList p_attrs, MState o)
  {
    if (processStateVertexRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.State."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("doActivity"))
      {
        lastMethod = "doActivity";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("internalTransition"))
      {
        lastMethod = "internalTransition";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("deferrableEvent"))
      {
        lastMethod = "deferrableEvent";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("stateMachine"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("classifierInState"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("exit"))
      {
        lastMethod = "exit";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("entry"))
      {
        lastMethod = "entry";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessStateRoles(String p_name, MState o)
  {
    if (postprocessStateVertexRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.State."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("doActivity"))
      {
        MAction el = (MAction)lastObject;
        if (null != el)
        {
          o.setDoActivity(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("internalTransition"))
      {
        MTransition el = (MTransition)lastObject;
        if (null != el)
        {
          o.addInternalTransition(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("deferrableEvent"))
      {
        MEvent el = (MEvent)lastObject;
        if (null != el)
        {
          o.addDeferrableEvent(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("stateMachine"))
      {
        MStateMachine el = (MStateMachine)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("classifierInState"))
      {
        MClassifierInState el = (MClassifierInState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("exit"))
      {
        MAction el = (MAction)lastObject;
        if (null != el)
        {
          o.setExit(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("entry"))
      {
        MAction el = (MAction)lastObject;
        if (null != el)
        {
          o.setEntry(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processAssociationClassMain(String p_name, AttributeList p_attrs)
  {
    MAssociationClass o = (MAssociationClass)liStack.get(liStack.size()-1);

    if (processAssociationClassAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processAssociationClassRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessAssociationClassMain(String p_name)
  {
    MAssociationClass o = (MAssociationClass)liStack.get(liStack.size()-2);

    if (postprocessAssociationClassAttributes(p_name, o))
    {
      return;
    }

    if (postprocessAssociationClassRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processAssociationClassAttributes(String p_name, AttributeList p_attrs, MAssociationClass o)
  {
    if (processClassAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessAssociationClassAttributes(String p_name, MAssociationClass o)
  {
    if (postprocessClassAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processAssociationClassRoles(String p_name, AttributeList p_attrs, MAssociationClass o)
  {
    if (processClassRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Association."))
    {
      String lastName = p_name.substring(28);

      if (lastName.equals("link"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("associationRole"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("connection"))
      {
        lastMethod = "connection";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessAssociationClassRoles(String p_name, MAssociationClass o)
  {
    if (postprocessClassRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Association."))
    {
      String lastName = p_name.substring(28);

      if (lastName.equals("link"))
      {
        MLink el = (MLink)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("associationRole"))
      {
        MAssociationRole el = (MAssociationRole)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("connection"))
      {
        MAssociationEnd el = (MAssociationEnd)lastObject;
        if (null != el)
        {
          o.addConnection(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processUsageMain(String p_name, AttributeList p_attrs)
  {
    MUsage o = (MUsage)liStack.get(liStack.size()-1);

    if (processUsageAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processUsageRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessUsageMain(String p_name)
  {
    MUsage o = (MUsage)liStack.get(liStack.size()-2);

    if (postprocessUsageAttributes(p_name, o))
    {
      return;
    }

    if (postprocessUsageRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processUsageAttributes(String p_name, AttributeList p_attrs, MUsage o)
  {
    if (processDependencyAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessUsageAttributes(String p_name, MUsage o)
  {
    if (postprocessDependencyAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processUsageRoles(String p_name, AttributeList p_attrs, MUsage o)
  {
    if (processDependencyRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessUsageRoles(String p_name, MUsage o)
  {
    if (postprocessDependencyRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processSynchStateMain(String p_name, AttributeList p_attrs)
  {
    MSynchState o = (MSynchState)liStack.get(liStack.size()-1);

    if (processSynchStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processSynchStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessSynchStateMain(String p_name)
  {
    MSynchState o = (MSynchState)liStack.get(liStack.size()-2);

    if (postprocessSynchStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessSynchStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processSynchStateAttributes(String p_name, AttributeList p_attrs, MSynchState o)
  {
    if (processStateVertexAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.SynchState."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("bound"))
      {
        lastMethod = "bound";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessSynchStateAttributes(String p_name, MSynchState o)
  {
    if (postprocessStateVertexAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.SynchState."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("bound"))
      {
        if (0 != lastString.length())
        {
          o.setBound(Integer.parseInt(lastString.toString()));
        }
        else
        {
          o.setBound(0);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processSynchStateRoles(String p_name, AttributeList p_attrs, MSynchState o)
  {
    if (processStateVertexRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessSynchStateRoles(String p_name, MSynchState o)
  {
    if (postprocessStateVertexRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processModelMain(String p_name, AttributeList p_attrs)
  {
    MModel o = (MModel)liStack.get(liStack.size()-1);

    if (processModelAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processModelRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessModelMain(String p_name)
  {
    MModel o = (MModel)liStack.get(liStack.size()-2);

    if (postprocessModelAttributes(p_name, o))
    {
      return;
    }

    if (postprocessModelRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processModelAttributes(String p_name, AttributeList p_attrs, MModel o)
  {
    if (processPackageAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessModelAttributes(String p_name, MModel o)
  {
    if (postprocessPackageAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processModelRoles(String p_name, AttributeList p_attrs, MModel o)
  {
    if (processPackageRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessModelRoles(String p_name, MModel o)
  {
    if (postprocessPackageRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processAttributeLinkMain(String p_name, AttributeList p_attrs)
  {
    MAttributeLink o = (MAttributeLink)liStack.get(liStack.size()-1);

    if (processAttributeLinkAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processAttributeLinkRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessAttributeLinkMain(String p_name)
  {
    MAttributeLink o = (MAttributeLink)liStack.get(liStack.size()-2);

    if (postprocessAttributeLinkAttributes(p_name, o))
    {
      return;
    }

    if (postprocessAttributeLinkRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processAttributeLinkAttributes(String p_name, AttributeList p_attrs, MAttributeLink o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessAttributeLinkAttributes(String p_name, MAttributeLink o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processAttributeLinkRoles(String p_name, AttributeList p_attrs, MAttributeLink o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.AttributeLink."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("linkEnd"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("instance"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("value"))
      {
        lastMethod = "value";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("attribute"))
      {
        lastMethod = "attribute";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessAttributeLinkRoles(String p_name, MAttributeLink o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.AttributeLink."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("linkEnd"))
      {
        MLinkEnd el = (MLinkEnd)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("instance"))
      {
        MInstance el = (MInstance)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("value"))
      {
        MInstance el = (MInstance)lastObject;
        if (null != el)
        {
          o.setValue(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("attribute"))
      {
        MAttribute el = (MAttribute)lastObject;
        if (null != el)
        {
          o.setAttribute(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processTimeExpressionMain(String p_name, AttributeList p_attrs)
  {
    MTimeExpressionEditor o = (MTimeExpressionEditor)liStack.get(liStack.size()-1);

    if (processTimeExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processTimeExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessTimeExpressionMain(String p_name)
  {
    MTimeExpressionEditor o = (MTimeExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessTimeExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessTimeExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processTimeExpressionAttributes(String p_name, AttributeList p_attrs, MTimeExpressionEditor o)
  {
    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessTimeExpressionAttributes(String p_name, MTimeExpressionEditor o)
  {
    if (postprocessExpressionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processTimeExpressionRoles(String p_name, AttributeList p_attrs, MTimeExpressionEditor o)
  {
    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessTimeExpressionRoles(String p_name, MTimeExpressionEditor o)
  {
    if (postprocessExpressionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processReturnActionMain(String p_name, AttributeList p_attrs)
  {
    MReturnAction o = (MReturnAction)liStack.get(liStack.size()-1);

    if (processReturnActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processReturnActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessReturnActionMain(String p_name)
  {
    MReturnAction o = (MReturnAction)liStack.get(liStack.size()-2);

    if (postprocessReturnActionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessReturnActionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processReturnActionAttributes(String p_name, AttributeList p_attrs, MReturnAction o)
  {
    if (processActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessReturnActionAttributes(String p_name, MReturnAction o)
  {
    if (postprocessActionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processReturnActionRoles(String p_name, AttributeList p_attrs, MReturnAction o)
  {
    if (processActionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessReturnActionRoles(String p_name, MReturnAction o)
  {
    if (postprocessActionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processMultiplicityRangeMain(String p_name, AttributeList p_attrs)
  {
    MMultiplicityRangeEditor o = (MMultiplicityRangeEditor)liStack.get(liStack.size()-1);

    if (processMultiplicityRangeAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processMultiplicityRangeRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessMultiplicityRangeMain(String p_name)
  {
    MMultiplicityRangeEditor o = (MMultiplicityRangeEditor)liStack.get(liStack.size()-2);

    if (postprocessMultiplicityRangeAttributes(p_name, o))
    {
      return;
    }

    if (postprocessMultiplicityRangeRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processActionStateMain(String p_name, AttributeList p_attrs)
  {
    MActionState o = (MActionState)liStack.get(liStack.size()-1);

    if (processActionStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processActionStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessActionStateMain(String p_name)
  {
    MActionState o = (MActionState)liStack.get(liStack.size()-2);

    if (postprocessActionStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessActionStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processActionStateAttributes(String p_name, AttributeList p_attrs, MActionState o)
  {
    if (processSimpleStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ActionState."))
    {
      String lastName = p_name.substring(48);

      if (lastName.equals("dynamicMultiplicity"))
      {
        lastMethod = "dynamicMultiplicity";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("dynamicArguments"))
      {
        lastMethod = "dynamicArguments";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isDynamic"))
      {
        lastMethod = "isDynamic";
        lastMethodType = true;
        o.setDynamic(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessActionStateAttributes(String p_name, MActionState o)
  {
    if (postprocessSimpleStateAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ActionState."))
    {
      String lastName = p_name.substring(48);

      if (lastName.equals("dynamicMultiplicity"))
      {
        if (!(lastObject instanceof Link))
        {
          MMultiplicity oexp = null;
          if (lastObject instanceof MMultiplicity)
          {
            oexp = (MMultiplicity)lastObject;
            o.setDynamicMultiplicity(oexp);
          }
          else
          {
            oexp = ((MMultiplicityEditor)lastObject).toMultiplicity();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setDynamicMultiplicity(oexp);
        }
        return true;
      }

      if (lastName.equals("dynamicArguments"))
      {
        if (!(lastObject instanceof Link))
        {
          MArgListsExpression oexp = null;
          if (lastObject instanceof MArgListsExpression)
          {
            oexp = (MArgListsExpression)lastObject;
            o.setDynamicArguments(oexp);
          }
          else
          {
            oexp = ((MArgListsExpressionEditor)lastObject).toArgListsExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setDynamicArguments(oexp);
        }
        return true;
      }

      if (lastName.equals("isDynamic"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processActionStateRoles(String p_name, AttributeList p_attrs, MActionState o)
  {
    if (processSimpleStateRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessActionStateRoles(String p_name, MActionState o)
  {
    if (postprocessSimpleStateRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processActionMain(String p_name, AttributeList p_attrs)
  {
    MAction o = (MAction)liStack.get(liStack.size()-1);

    if (processActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessActionMain(String p_name)
  {
    MAction o = (MAction)liStack.get(liStack.size()-2);

    if (postprocessActionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessActionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processActionAttributes(String p_name, AttributeList p_attrs, MAction o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Action."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("script"))
      {
        lastMethod = "script";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isAsynchronous"))
      {
        lastMethod = "isAsynchronous";
        lastMethodType = true;
        o.setAsynchronous(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("target"))
      {
        lastMethod = "target";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("recurrence"))
      {
        lastMethod = "recurrence";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessActionAttributes(String p_name, MAction o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Action."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("script"))
      {
        if (!(lastObject instanceof Link))
        {
          MActionExpression oexp = null;
          if (lastObject instanceof MActionExpression)
          {
            oexp = (MActionExpression)lastObject;
            o.setScript(oexp);
          }
          else
          {
            oexp = ((MActionExpressionEditor)lastObject).toActionExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setScript(oexp);
        }
        return true;
      }

      if (lastName.equals("isAsynchronous"))
      {
        return true;
      }

      if (lastName.equals("target"))
      {
        if (!(lastObject instanceof Link))
        {
          MObjectSetExpression oexp = null;
          if (lastObject instanceof MObjectSetExpression)
          {
            oexp = (MObjectSetExpression)lastObject;
            o.setTarget(oexp);
          }
          else
          {
            oexp = ((MObjectSetExpressionEditor)lastObject).toObjectSetExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setTarget(oexp);
        }
        return true;
      }

      if (lastName.equals("recurrence"))
      {
        if (!(lastObject instanceof Link))
        {
          MIterationExpression oexp = null;
          if (lastObject instanceof MIterationExpression)
          {
            oexp = (MIterationExpression)lastObject;
            o.setRecurrence(oexp);
          }
          else
          {
            oexp = ((MIterationExpressionEditor)lastObject).toIterationExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setRecurrence(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processActionRoles(String p_name, AttributeList p_attrs, MAction o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Action."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("stimulus"))
      {
        lastMethod = "stimulus";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("actionSequence"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("actualArgument"))
      {
        lastMethod = "actualArgument";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("message"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("state3"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("transition"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("state2"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("state1"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessActionRoles(String p_name, MAction o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Action."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("stimulus"))
      {
        MStimulus el = (MStimulus)lastObject;
        if (null != el)
        {
          o.addStimulus(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("actionSequence"))
      {
        MActionSequence el = (MActionSequence)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("actualArgument"))
      {
        MArgument el = (MArgument)lastObject;
        if (null != el)
        {
          o.addActualArgument(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("message"))
      {
        MMessage el = (MMessage)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("state3"))
      {
        MState el = (MState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("transition"))
      {
        MTransition el = (MTransition)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("state2"))
      {
        MState el = (MState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("state1"))
      {
        MState el = (MState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processUseCaseInstanceMain(String p_name, AttributeList p_attrs)
  {
    MUseCaseInstance o = (MUseCaseInstance)liStack.get(liStack.size()-1);

    if (processUseCaseInstanceAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processUseCaseInstanceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessUseCaseInstanceMain(String p_name)
  {
    MUseCaseInstance o = (MUseCaseInstance)liStack.get(liStack.size()-2);

    if (postprocessUseCaseInstanceAttributes(p_name, o))
    {
      return;
    }

    if (postprocessUseCaseInstanceRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processUseCaseInstanceAttributes(String p_name, AttributeList p_attrs, MUseCaseInstance o)
  {
    if (processInstanceAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessUseCaseInstanceAttributes(String p_name, MUseCaseInstance o)
  {
    if (postprocessInstanceAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processUseCaseInstanceRoles(String p_name, AttributeList p_attrs, MUseCaseInstance o)
  {
    if (processInstanceRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessUseCaseInstanceRoles(String p_name, MUseCaseInstance o)
  {
    if (postprocessInstanceRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processMessageMain(String p_name, AttributeList p_attrs)
  {
    MMessage o = (MMessage)liStack.get(liStack.size()-1);

    if (processMessageAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processMessageRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessMessageMain(String p_name)
  {
    MMessage o = (MMessage)liStack.get(liStack.size()-2);

    if (postprocessMessageAttributes(p_name, o))
    {
      return;
    }

    if (postprocessMessageRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processMessageAttributes(String p_name, AttributeList p_attrs, MMessage o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessMessageAttributes(String p_name, MMessage o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processMessageRoles(String p_name, AttributeList p_attrs, MMessage o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.Message."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("action"))
      {
        lastMethod = "action";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("communicationConnection"))
      {
        lastMethod = "communicationConnection";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("message3"))
      {
        lastMethod = "message3";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("predecessor"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("receiver"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("sender"))
      {
        lastMethod = "sender";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("activator"))
      {
        lastMethod = "activator";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("message4"))
      {
        lastMethod = "message4";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("interaction"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessMessageRoles(String p_name, MMessage o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.Message."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("action"))
      {
        MAction el = (MAction)lastObject;
        if (null != el)
        {
          o.setAction(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("communicationConnection"))
      {
        MAssociationRole el = (MAssociationRole)lastObject;
        if (null != el)
        {
          o.setCommunicationConnection(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("message3"))
      {
        MMessage el = (MMessage)lastObject;
        if (null != el)
        {
          o.addMessage3(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("predecessor"))
      {
        MMessage el = (MMessage)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("receiver"))
      {
        MClassifierRole el = (MClassifierRole)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("sender"))
      {
        MClassifierRole el = (MClassifierRole)lastObject;
        if (null != el)
        {
          o.setSender(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("activator"))
      {
        MMessage el = (MMessage)lastObject;
        if (null != el)
        {
          o.setActivator(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("message4"))
      {
        MMessage el = (MMessage)lastObject;
        if (null != el)
        {
          o.addMessage4(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("interaction"))
      {
        MInteraction el = (MInteraction)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processSubactivityStateMain(String p_name, AttributeList p_attrs)
  {
    MSubactivityState o = (MSubactivityState)liStack.get(liStack.size()-1);

    if (processSubactivityStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processSubactivityStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessSubactivityStateMain(String p_name)
  {
    MSubactivityState o = (MSubactivityState)liStack.get(liStack.size()-2);

    if (postprocessSubactivityStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessSubactivityStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processSubactivityStateAttributes(String p_name, AttributeList p_attrs, MSubactivityState o)
  {
    if (processSubmachineStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.SubactivityState."))
    {
      String lastName = p_name.substring(53);

      if (lastName.equals("dynamicMultiplicity"))
      {
        lastMethod = "dynamicMultiplicity";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("dynamicArguments"))
      {
        lastMethod = "dynamicArguments";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isDynamic"))
      {
        lastMethod = "isDynamic";
        lastMethodType = true;
        o.setDynamic(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessSubactivityStateAttributes(String p_name, MSubactivityState o)
  {
    if (postprocessSubmachineStateAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.SubactivityState."))
    {
      String lastName = p_name.substring(53);

      if (lastName.equals("dynamicMultiplicity"))
      {
        if (!(lastObject instanceof Link))
        {
          MMultiplicity oexp = null;
          if (lastObject instanceof MMultiplicity)
          {
            oexp = (MMultiplicity)lastObject;
            o.setDynamicMultiplicity(oexp);
          }
          else
          {
            oexp = ((MMultiplicityEditor)lastObject).toMultiplicity();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setDynamicMultiplicity(oexp);
        }
        return true;
      }

      if (lastName.equals("dynamicArguments"))
      {
        if (!(lastObject instanceof Link))
        {
          MArgListsExpression oexp = null;
          if (lastObject instanceof MArgListsExpression)
          {
            oexp = (MArgListsExpression)lastObject;
            o.setDynamicArguments(oexp);
          }
          else
          {
            oexp = ((MArgListsExpressionEditor)lastObject).toArgListsExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setDynamicArguments(oexp);
        }
        return true;
      }

      if (lastName.equals("isDynamic"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processSubactivityStateRoles(String p_name, AttributeList p_attrs, MSubactivityState o)
  {
    if (processSubmachineStateRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessSubactivityStateRoles(String p_name, MSubactivityState o)
  {
    if (postprocessSubmachineStateRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processSendActionMain(String p_name, AttributeList p_attrs)
  {
    MSendAction o = (MSendAction)liStack.get(liStack.size()-1);

    if (processSendActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processSendActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessSendActionMain(String p_name)
  {
    MSendAction o = (MSendAction)liStack.get(liStack.size()-2);

    if (postprocessSendActionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessSendActionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processSendActionAttributes(String p_name, AttributeList p_attrs, MSendAction o)
  {
    if (processActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessSendActionAttributes(String p_name, MSendAction o)
  {
    if (postprocessActionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processSendActionRoles(String p_name, AttributeList p_attrs, MSendAction o)
  {
    if (processActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.SendAction."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("signal"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessSendActionRoles(String p_name, MSendAction o)
  {
    if (postprocessActionRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.SendAction."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("signal"))
      {
        MSignal el = (MSignal)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processProcedureExpressionMain(String p_name, AttributeList p_attrs)
  {
    MProcedureExpressionEditor o = (MProcedureExpressionEditor)liStack.get(liStack.size()-1);

    if (processProcedureExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processProcedureExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessProcedureExpressionMain(String p_name)
  {
    MProcedureExpressionEditor o = (MProcedureExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessProcedureExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessProcedureExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processProcedureExpressionAttributes(String p_name, AttributeList p_attrs, MProcedureExpressionEditor o)
  {
    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessProcedureExpressionAttributes(String p_name, MProcedureExpressionEditor o)
  {
    if (postprocessExpressionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processProcedureExpressionRoles(String p_name, AttributeList p_attrs, MProcedureExpressionEditor o)
  {
    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessProcedureExpressionRoles(String p_name, MProcedureExpressionEditor o)
  {
    if (postprocessExpressionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processSignalMain(String p_name, AttributeList p_attrs)
  {
    MSignal o = (MSignal)liStack.get(liStack.size()-1);

    if (processSignalAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processSignalRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessSignalMain(String p_name)
  {
    MSignal o = (MSignal)liStack.get(liStack.size()-2);

    if (postprocessSignalAttributes(p_name, o))
    {
      return;
    }

    if (postprocessSignalRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processSignalAttributes(String p_name, AttributeList p_attrs, MSignal o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessSignalAttributes(String p_name, MSignal o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processSignalRoles(String p_name, AttributeList p_attrs, MSignal o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Signal."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("context"))
      {
        lastMethod = "context";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("reception"))
      {
        lastMethod = "reception";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("occurrence"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("sendAction"))
      {
        lastMethod = "sendAction";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessSignalRoles(String p_name, MSignal o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Signal."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("context"))
      {
        MBehavioralFeature el = (MBehavioralFeature)lastObject;
        if (null != el)
        {
          o.addContext(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("reception"))
      {
        MReception el = (MReception)lastObject;
        if (null != el)
        {
          o.addReception(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("occurrence"))
      {
        MSignalEvent el = (MSignalEvent)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("sendAction"))
      {
        MSendAction el = (MSendAction)lastObject;
        if (null != el)
        {
          o.addSendAction(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processNodeInstanceMain(String p_name, AttributeList p_attrs)
  {
    MNodeInstance o = (MNodeInstance)liStack.get(liStack.size()-1);

    if (processNodeInstanceAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processNodeInstanceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessNodeInstanceMain(String p_name)
  {
    MNodeInstance o = (MNodeInstance)liStack.get(liStack.size()-2);

    if (postprocessNodeInstanceAttributes(p_name, o))
    {
      return;
    }

    if (postprocessNodeInstanceRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processNodeInstanceAttributes(String p_name, AttributeList p_attrs, MNodeInstance o)
  {
    if (processInstanceAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessNodeInstanceAttributes(String p_name, MNodeInstance o)
  {
    if (postprocessInstanceAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processNodeInstanceRoles(String p_name, AttributeList p_attrs, MNodeInstance o)
  {
    if (processInstanceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.NodeInstance."))
    {
      String lastName = p_name.substring(49);

      if (lastName.equals("resident"))
      {
        lastMethod = "resident";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessNodeInstanceRoles(String p_name, MNodeInstance o)
  {
    if (postprocessInstanceRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.NodeInstance."))
    {
      String lastName = p_name.substring(49);

      if (lastName.equals("resident"))
      {
        MComponentInstance el = (MComponentInstance)lastObject;
        if (null != el)
        {
          o.addResident(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processReceptionMain(String p_name, AttributeList p_attrs)
  {
    MReception o = (MReception)liStack.get(liStack.size()-1);

    if (processReceptionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processReceptionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessReceptionMain(String p_name)
  {
    MReception o = (MReception)liStack.get(liStack.size()-2);

    if (postprocessReceptionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessReceptionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processReceptionAttributes(String p_name, AttributeList p_attrs, MReception o)
  {
    if (processBehavioralFeatureAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Reception."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("isAbstarct"))
      {
        lastMethod = "isAbstarct";
        lastMethodType = true;
        o.setAbstarct(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        lastMethod = "isLeaf";
        lastMethodType = true;
        o.setLeaf(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        lastMethod = "isRoot";
        lastMethodType = true;
        o.setRoot(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("specification"))
      {
        lastMethod = "specification";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessReceptionAttributes(String p_name, MReception o)
  {
    if (postprocessBehavioralFeatureAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Reception."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("isAbstarct"))
      {
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        return true;
      }

      if (lastName.equals("specification"))
      {
        o.setSpecification(lastString.toString());
        return true;
      }

    }
    return false;
  }

  public boolean processReceptionRoles(String p_name, AttributeList p_attrs, MReception o)
  {
    if (processBehavioralFeatureRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Reception."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("signal"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessReceptionRoles(String p_name, MReception o)
  {
    if (postprocessBehavioralFeatureRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Reception."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("signal"))
      {
        MSignal el = (MSignal)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processTemplateParameterMain(String p_name, AttributeList p_attrs)
  {
    MTemplateParameter o = (MTemplateParameter)liStack.get(liStack.size()-1);

    if (processTemplateParameterAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processTemplateParameterRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessTemplateParameterMain(String p_name)
  {
    MTemplateParameter o = (MTemplateParameter)liStack.get(liStack.size()-2);

    if (postprocessTemplateParameterAttributes(p_name, o))
    {
      return;
    }

    if (postprocessTemplateParameterRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processTemplateParameterAttributes(String p_name, AttributeList p_attrs, MTemplateParameter o)
  {
    return false;
  }

  public boolean postprocessTemplateParameterAttributes(String p_name, MTemplateParameter o)
  {
    return false;
  }

  public boolean processTemplateParameterRoles(String p_name, AttributeList p_attrs, MTemplateParameter o)
  {
    if (p_name.startsWith("Foundation.Core.TemplateParameter."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("modelElement2"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("defaultElement"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("modelElement"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessTemplateParameterRoles(String p_name, MTemplateParameter o)
  {
    if (p_name.startsWith("Foundation.Core.TemplateParameter."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("modelElement2"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("defaultElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("modelElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processAssociationRoleMain(String p_name, AttributeList p_attrs)
  {
    MAssociationRole o = (MAssociationRole)liStack.get(liStack.size()-1);

    if (processAssociationRoleAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processAssociationRoleRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessAssociationRoleMain(String p_name)
  {
    MAssociationRole o = (MAssociationRole)liStack.get(liStack.size()-2);

    if (postprocessAssociationRoleAttributes(p_name, o))
    {
      return;
    }

    if (postprocessAssociationRoleRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processAssociationRoleAttributes(String p_name, AttributeList p_attrs, MAssociationRole o)
  {
    if (processAssociationAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.AssociationRole."))
    {
      String lastName = p_name.substring(51);

      if (lastName.equals("multiplicity"))
      {
        lastMethod = "multiplicity";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessAssociationRoleAttributes(String p_name, MAssociationRole o)
  {
    if (postprocessAssociationAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.AssociationRole."))
    {
      String lastName = p_name.substring(51);

      if (lastName.equals("multiplicity"))
      {
        if (!(lastObject instanceof Link))
        {
          MMultiplicity oexp = null;
          if (lastObject instanceof MMultiplicity)
          {
            oexp = (MMultiplicity)lastObject;
            o.setMultiplicity(oexp);
          }
          else
          {
            oexp = ((MMultiplicityEditor)lastObject).toMultiplicity();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setMultiplicity(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processAssociationRoleRoles(String p_name, AttributeList p_attrs, MAssociationRole o)
  {
    if (processAssociationRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.AssociationRole."))
    {
      String lastName = p_name.substring(51);

      if (lastName.equals("message"))
      {
        lastMethod = "message";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("base"))
      {
        lastMethod = "base";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessAssociationRoleRoles(String p_name, MAssociationRole o)
  {
    if (postprocessAssociationRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.AssociationRole."))
    {
      String lastName = p_name.substring(51);

      if (lastName.equals("message"))
      {
        MMessage el = (MMessage)lastObject;
        if (null != el)
        {
          o.addMessage(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("base"))
      {
        MAssociation el = (MAssociation)lastObject;
        if (null != el)
        {
          o.setBase(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processArgListsExpressionMain(String p_name, AttributeList p_attrs)
  {
    MArgListsExpressionEditor o = (MArgListsExpressionEditor)liStack.get(liStack.size()-1);

    if (processArgListsExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processArgListsExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessArgListsExpressionMain(String p_name)
  {
    MArgListsExpressionEditor o = (MArgListsExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessArgListsExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessArgListsExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processArgListsExpressionAttributes(String p_name, AttributeList p_attrs, MArgListsExpressionEditor o)
  {
    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessArgListsExpressionAttributes(String p_name, MArgListsExpressionEditor o)
  {
    if (postprocessExpressionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processArgListsExpressionRoles(String p_name, AttributeList p_attrs, MArgListsExpressionEditor o)
  {
    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessArgListsExpressionRoles(String p_name, MArgListsExpressionEditor o)
  {
    if (postprocessExpressionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processExtendMain(String p_name, AttributeList p_attrs)
  {
    MExtend o = (MExtend)liStack.get(liStack.size()-1);

    if (processExtendAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processExtendRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessExtendMain(String p_name)
  {
    MExtend o = (MExtend)liStack.get(liStack.size()-2);

    if (postprocessExtendAttributes(p_name, o))
    {
      return;
    }

    if (postprocessExtendRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processExtendAttributes(String p_name, AttributeList p_attrs, MExtend o)
  {
    if (processRelationshipAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.Extend."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("condition"))
      {
        lastMethod = "condition";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessExtendAttributes(String p_name, MExtend o)
  {
    if (postprocessRelationshipAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.Extend."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("condition"))
      {
        if (!(lastObject instanceof Link))
        {
          MBooleanExpression oexp = null;
          if (lastObject instanceof MBooleanExpression)
          {
            oexp = (MBooleanExpression)lastObject;
            o.setCondition(oexp);
          }
          else
          {
            oexp = ((MBooleanExpressionEditor)lastObject).toBooleanExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setCondition(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processExtendRoles(String p_name, AttributeList p_attrs, MExtend o)
  {
    if (processRelationshipRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.Extend."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("extensionPoint"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("extension"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("base"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessExtendRoles(String p_name, MExtend o)
  {
    if (postprocessRelationshipRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.Extend."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("extensionPoint"))
      {
        MExtensionPoint el = (MExtensionPoint)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("extension"))
      {
        MUseCase el = (MUseCase)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("base"))
      {
        MUseCase el = (MUseCase)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processStateVertexAttributes(String p_name, AttributeList p_attrs, MStateVertex o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessStateVertexAttributes(String p_name, MStateVertex o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processStateVertexRoles(String p_name, AttributeList p_attrs, MStateVertex o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.StateVertex."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("incoming"))
      {
        lastMethod = "incoming";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("outgoing"))
      {
        lastMethod = "outgoing";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("container"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessStateVertexRoles(String p_name, MStateVertex o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.StateVertex."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("incoming"))
      {
        MTransition el = (MTransition)lastObject;
        if (null != el)
        {
          o.addIncoming(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("outgoing"))
      {
        MTransition el = (MTransition)lastObject;
        if (null != el)
        {
          o.addOutgoing(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("container"))
      {
        MCompositeState el = (MCompositeState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processParameterMain(String p_name, AttributeList p_attrs)
  {
    MParameter o = (MParameter)liStack.get(liStack.size()-1);

    if (processParameterAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processParameterRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessParameterMain(String p_name)
  {
    MParameter o = (MParameter)liStack.get(liStack.size()-2);

    if (postprocessParameterAttributes(p_name, o))
    {
      return;
    }

    if (postprocessParameterRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processParameterAttributes(String p_name, AttributeList p_attrs, MParameter o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Parameter."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("kind"))
      {
        lastMethod = "kind";
        lastMethodType = true;
        o.setKind(MParameterDirectionKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("defaultValue"))
      {
        lastMethod = "defaultValue";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessParameterAttributes(String p_name, MParameter o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Parameter."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("kind"))
      {
        return true;
      }

      if (lastName.equals("defaultValue"))
      {
        if (!(lastObject instanceof Link))
        {
          MExpression oexp = null;
          if (lastObject instanceof MExpression)
          {
            oexp = (MExpression)lastObject;
            o.setDefaultValue(oexp);
          }
          else
          {
            oexp = ((MExpressionEditor)lastObject).toExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setDefaultValue(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processParameterRoles(String p_name, AttributeList p_attrs, MParameter o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Parameter."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("state"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("event"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("type"))
      {
        lastMethod = "type";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("behavioralFeature"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessParameterRoles(String p_name, MParameter o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Parameter."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("state"))
      {
        MObjectFlowState el = (MObjectFlowState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("event"))
      {
        MEvent el = (MEvent)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("type"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.setType(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("behavioralFeature"))
      {
        MBehavioralFeature el = (MBehavioralFeature)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processCompositeStateMain(String p_name, AttributeList p_attrs)
  {
    MCompositeState o = (MCompositeState)liStack.get(liStack.size()-1);

    if (processCompositeStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processCompositeStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessCompositeStateMain(String p_name)
  {
    MCompositeState o = (MCompositeState)liStack.get(liStack.size()-2);

    if (postprocessCompositeStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessCompositeStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processCompositeStateAttributes(String p_name, AttributeList p_attrs, MCompositeState o)
  {
    if (processStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.CompositeState."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("isConcurent"))
      {
        lastMethod = "isConcurent";
        lastMethodType = true;
        o.setConcurent(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessCompositeStateAttributes(String p_name, MCompositeState o)
  {
    if (postprocessStateAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.CompositeState."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("isConcurent"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processCompositeStateRoles(String p_name, AttributeList p_attrs, MCompositeState o)
  {
    if (processStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.CompositeState."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("subvertex"))
      {
        lastMethod = "subvertex";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessCompositeStateRoles(String p_name, MCompositeState o)
  {
    if (postprocessStateRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.CompositeState."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("subvertex"))
      {
        MStateVertex el = (MStateVertex)lastObject;
        if (null != el)
        {
          o.addSubvertex(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processStructuralFeatureAttributes(String p_name, AttributeList p_attrs, MStructuralFeature o)
  {
    if (processFeatureAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.StructuralFeature."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("targetScope"))
      {
        lastMethod = "targetScope";
        lastMethodType = true;
        o.setTargetScope(MScopeKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("changeability"))
      {
        lastMethod = "changeability";
        lastMethodType = true;
        o.setChangeability(MChangeableKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("multiplicity"))
      {
        lastMethod = "multiplicity";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessStructuralFeatureAttributes(String p_name, MStructuralFeature o)
  {
    if (postprocessFeatureAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.StructuralFeature."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("targetScope"))
      {
        return true;
      }

      if (lastName.equals("changeability"))
      {
        return true;
      }

      if (lastName.equals("multiplicity"))
      {
        if (!(lastObject instanceof Link))
        {
          MMultiplicity oexp = null;
          if (lastObject instanceof MMultiplicity)
          {
            oexp = (MMultiplicity)lastObject;
            o.setMultiplicity(oexp);
          }
          else
          {
            oexp = ((MMultiplicityEditor)lastObject).toMultiplicity();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setMultiplicity(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processStructuralFeatureRoles(String p_name, AttributeList p_attrs, MStructuralFeature o)
  {
    if (processFeatureRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.StructuralFeature."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("type"))
      {
        lastMethod = "type";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessStructuralFeatureRoles(String p_name, MStructuralFeature o)
  {
    if (postprocessFeatureRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.StructuralFeature."))
    {
      String lastName = p_name.substring(34);

      if (lastName.equals("type"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.setType(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processDataTypeMain(String p_name, AttributeList p_attrs)
  {
    MDataType o = (MDataType)liStack.get(liStack.size()-1);

    if (processDataTypeAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processDataTypeRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessDataTypeMain(String p_name)
  {
    MDataType o = (MDataType)liStack.get(liStack.size()-2);

    if (postprocessDataTypeAttributes(p_name, o))
    {
      return;
    }

    if (postprocessDataTypeRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processDataTypeAttributes(String p_name, AttributeList p_attrs, MDataType o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessDataTypeAttributes(String p_name, MDataType o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processDataTypeRoles(String p_name, AttributeList p_attrs, MDataType o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessDataTypeRoles(String p_name, MDataType o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processTypeExpressionMain(String p_name, AttributeList p_attrs)
  {
    MTypeExpressionEditor o = (MTypeExpressionEditor)liStack.get(liStack.size()-1);

    if (processTypeExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processTypeExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessTypeExpressionMain(String p_name)
  {
    MTypeExpressionEditor o = (MTypeExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessTypeExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessTypeExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processTypeExpressionAttributes(String p_name, AttributeList p_attrs, MTypeExpressionEditor o)
  {
    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessTypeExpressionAttributes(String p_name, MTypeExpressionEditor o)
  {
    if (postprocessExpressionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processTypeExpressionRoles(String p_name, AttributeList p_attrs, MTypeExpressionEditor o)
  {
    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessTypeExpressionRoles(String p_name, MTypeExpressionEditor o)
  {
    if (postprocessExpressionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processStubStateMain(String p_name, AttributeList p_attrs)
  {
    MStubState o = (MStubState)liStack.get(liStack.size()-1);

    if (processStubStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processStubStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessStubStateMain(String p_name)
  {
    MStubState o = (MStubState)liStack.get(liStack.size()-2);

    if (postprocessStubStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessStubStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processStubStateAttributes(String p_name, AttributeList p_attrs, MStubState o)
  {
    if (processStateVertexAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.StubState."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("referenceState"))
      {
        lastMethod = "referenceState";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessStubStateAttributes(String p_name, MStubState o)
  {
    if (postprocessStateVertexAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.StubState."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("referenceState"))
      {
        o.setReferenceState(lastString.toString());
        return true;
      }

    }
    return false;
  }

  public boolean processStubStateRoles(String p_name, AttributeList p_attrs, MStubState o)
  {
    if (processStateVertexRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessStubStateRoles(String p_name, MStubState o)
  {
    if (postprocessStateVertexRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processActivityGraphMain(String p_name, AttributeList p_attrs)
  {
    MActivityGraph o = (MActivityGraph)liStack.get(liStack.size()-1);

    if (processActivityGraphAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processActivityGraphRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessActivityGraphMain(String p_name)
  {
    MActivityGraph o = (MActivityGraph)liStack.get(liStack.size()-2);

    if (postprocessActivityGraphAttributes(p_name, o))
    {
      return;
    }

    if (postprocessActivityGraphRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processActivityGraphAttributes(String p_name, AttributeList p_attrs, MActivityGraph o)
  {
    if (processStateMachineAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessActivityGraphAttributes(String p_name, MActivityGraph o)
  {
    if (postprocessStateMachineAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processActivityGraphRoles(String p_name, AttributeList p_attrs, MActivityGraph o)
  {
    if (processStateMachineRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ActivityGraph."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("partition"))
      {
        lastMethod = "partition";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessActivityGraphRoles(String p_name, MActivityGraph o)
  {
    if (postprocessStateMachineRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ActivityGraph."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("partition"))
      {
        MPartition el = (MPartition)lastObject;
        if (null != el)
        {
          o.addPartition(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processPackageMain(String p_name, AttributeList p_attrs)
  {
    MPackage o = (MPackage)liStack.get(liStack.size()-1);

    if (processPackageAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processPackageRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessPackageMain(String p_name)
  {
    MPackage o = (MPackage)liStack.get(liStack.size()-2);

    if (postprocessPackageAttributes(p_name, o))
    {
      return;
    }

    if (postprocessPackageRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processPackageAttributes(String p_name, AttributeList p_attrs, MPackage o)
  {
    if (processNamespaceAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("isAbstract"))
      {
        lastMethod = "isAbstract";
        lastMethodType = true;
        o.setAbstract(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        lastMethod = "isLeaf";
        lastMethodType = true;
        o.setLeaf(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        lastMethod = "isRoot";
        lastMethodType = true;
        o.setRoot(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessPackageAttributes(String p_name, MPackage o)
  {
    if (postprocessNamespaceAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("isAbstract"))
      {
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processPackageRoles(String p_name, AttributeList p_attrs, MPackage o)
  {
    if (processNamespaceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("specialization"))
      {
        lastMethod = "specialization";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("generalization"))
      {
        lastMethod = "generalization";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }
    if (p_name.startsWith("Model_Management.Package."))
    {
      String lastName = p_name.substring(25);

      if (lastName.equals("elementImport"))
      {
        lastMethod = "elementImport";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessPackageRoles(String p_name, MPackage o)
  {
    if (postprocessNamespaceRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("specialization"))
      {
        MGeneralization el = (MGeneralization)lastObject;
        if (null != el)
        {
          o.addSpecialization(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("generalization"))
      {
        MGeneralization el = (MGeneralization)lastObject;
        if (null != el)
        {
          o.addGeneralization(el);
          return true;
        }
        return false;
      }
    }
    if (p_name.startsWith("Model_Management.Package."))
    {
      String lastName = p_name.substring(25);

      if (lastName.equals("elementImport"))
      {
        MElementImport el = (MElementImport)lastObject;
        if (null != el)
        {
          o.addElementImport(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processMultiplicityMain(String p_name, AttributeList p_attrs)
  {
    MMultiplicityEditor o = (MMultiplicityEditor)liStack.get(liStack.size()-1);

    if (processMultiplicityAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processMultiplicityRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessMultiplicityMain(String p_name)
  {
    MMultiplicityEditor o = (MMultiplicityEditor)liStack.get(liStack.size()-2);

    if (postprocessMultiplicityAttributes(p_name, o))
    {
      return;
    }

    if (postprocessMultiplicityRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processLinkObjectMain(String p_name, AttributeList p_attrs)
  {
    MLinkObject o = (MLinkObject)liStack.get(liStack.size()-1);

    if (processLinkObjectAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processLinkObjectRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessLinkObjectMain(String p_name)
  {
    MLinkObject o = (MLinkObject)liStack.get(liStack.size()-2);

    if (postprocessLinkObjectAttributes(p_name, o))
    {
      return;
    }

    if (postprocessLinkObjectRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processLinkObjectAttributes(String p_name, AttributeList p_attrs, MLinkObject o)
  {
    if (processLinkAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessLinkObjectAttributes(String p_name, MLinkObject o)
  {
    if (postprocessLinkAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processLinkObjectRoles(String p_name, AttributeList p_attrs, MLinkObject o)
  {
    if (processLinkRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Instance."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("stimulus2"))
      {
        lastMethod = "stimulus2";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("componentInstance"))
      {
        lastMethod = "componentInstance";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("stimulus3"))
      {
        lastMethod = "stimulus3";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("stimulus1"))
      {
        lastMethod = "stimulus1";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("slot"))
      {
        lastMethod = "slot";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("linkEnd"))
      {
        lastMethod = "linkEnd";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("attributeLink"))
      {
        lastMethod = "attributeLink";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("classifier"))
      {
        lastMethod = "classifier";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessLinkObjectRoles(String p_name, MLinkObject o)
  {
    if (postprocessLinkRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Instance."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("stimulus2"))
      {
        MStimulus el = (MStimulus)lastObject;
        if (null != el)
        {
          o.addStimulus2(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("componentInstance"))
      {
        MComponentInstance el = (MComponentInstance)lastObject;
        if (null != el)
        {
          o.setComponentInstance(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("stimulus3"))
      {
        MStimulus el = (MStimulus)lastObject;
        if (null != el)
        {
          o.addStimulus3(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("stimulus1"))
      {
        MStimulus el = (MStimulus)lastObject;
        if (null != el)
        {
          o.addStimulus1(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("slot"))
      {
        MAttributeLink el = (MAttributeLink)lastObject;
        if (null != el)
        {
          o.addSlot(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("linkEnd"))
      {
        MLinkEnd el = (MLinkEnd)lastObject;
        if (null != el)
        {
          o.addLinkEnd(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("attributeLink"))
      {
        MAttributeLink el = (MAttributeLink)lastObject;
        if (null != el)
        {
          o.addAttributeLink(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("classifier"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.addClassifier(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processBooleanExpressionMain(String p_name, AttributeList p_attrs)
  {
    MBooleanExpressionEditor o = (MBooleanExpressionEditor)liStack.get(liStack.size()-1);

    if (processBooleanExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processBooleanExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessBooleanExpressionMain(String p_name)
  {
    MBooleanExpressionEditor o = (MBooleanExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessBooleanExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessBooleanExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processBooleanExpressionAttributes(String p_name, AttributeList p_attrs, MBooleanExpressionEditor o)
  {
    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessBooleanExpressionAttributes(String p_name, MBooleanExpressionEditor o)
  {
    if (postprocessExpressionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processBooleanExpressionRoles(String p_name, AttributeList p_attrs, MBooleanExpressionEditor o)
  {
    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessBooleanExpressionRoles(String p_name, MBooleanExpressionEditor o)
  {
    if (postprocessExpressionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processAbstractionMain(String p_name, AttributeList p_attrs)
  {
    MAbstraction o = (MAbstraction)liStack.get(liStack.size()-1);

    if (processAbstractionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processAbstractionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessAbstractionMain(String p_name)
  {
    MAbstraction o = (MAbstraction)liStack.get(liStack.size()-2);

    if (postprocessAbstractionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessAbstractionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processAbstractionAttributes(String p_name, AttributeList p_attrs, MAbstraction o)
  {
    if (processDependencyAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Abstraction."))
    {
      String lastName = p_name.substring(28);

      if (lastName.equals("mapping"))
      {
        lastMethod = "mapping";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessAbstractionAttributes(String p_name, MAbstraction o)
  {
    if (postprocessDependencyAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Abstraction."))
    {
      String lastName = p_name.substring(28);

      if (lastName.equals("mapping"))
      {
        if (!(lastObject instanceof Link))
        {
          MMappingExpression oexp = null;
          if (lastObject instanceof MMappingExpression)
          {
            oexp = (MMappingExpression)lastObject;
            o.setMapping(oexp);
          }
          else
          {
            oexp = ((MMappingExpressionEditor)lastObject).toMappingExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setMapping(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processAbstractionRoles(String p_name, AttributeList p_attrs, MAbstraction o)
  {
    if (processDependencyRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessAbstractionRoles(String p_name, MAbstraction o)
  {
    if (postprocessDependencyRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processChangeEventMain(String p_name, AttributeList p_attrs)
  {
    MChangeEvent o = (MChangeEvent)liStack.get(liStack.size()-1);

    if (processChangeEventAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processChangeEventRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessChangeEventMain(String p_name)
  {
    MChangeEvent o = (MChangeEvent)liStack.get(liStack.size()-2);

    if (postprocessChangeEventAttributes(p_name, o))
    {
      return;
    }

    if (postprocessChangeEventRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processChangeEventAttributes(String p_name, AttributeList p_attrs, MChangeEvent o)
  {
    if (processEventAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.ChangeEvent."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("changeExpression"))
      {
        lastMethod = "changeExpression";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessChangeEventAttributes(String p_name, MChangeEvent o)
  {
    if (postprocessEventAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.ChangeEvent."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("changeExpression"))
      {
        if (!(lastObject instanceof Link))
        {
          MBooleanExpression oexp = null;
          if (lastObject instanceof MBooleanExpression)
          {
            oexp = (MBooleanExpression)lastObject;
            o.setChangeExpression(oexp);
          }
          else
          {
            oexp = ((MBooleanExpressionEditor)lastObject).toBooleanExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setChangeExpression(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processChangeEventRoles(String p_name, AttributeList p_attrs, MChangeEvent o)
  {
    if (processEventRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessChangeEventRoles(String p_name, MChangeEvent o)
  {
    if (postprocessEventRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processActionExpressionMain(String p_name, AttributeList p_attrs)
  {
    MActionExpressionEditor o = (MActionExpressionEditor)liStack.get(liStack.size()-1);

    if (processActionExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processActionExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessActionExpressionMain(String p_name)
  {
    MActionExpressionEditor o = (MActionExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessActionExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessActionExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processActionExpressionAttributes(String p_name, AttributeList p_attrs, MActionExpressionEditor o)
  {
    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessActionExpressionAttributes(String p_name, MActionExpressionEditor o)
  {
    if (postprocessExpressionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processActionExpressionRoles(String p_name, AttributeList p_attrs, MActionExpressionEditor o)
  {
    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessActionExpressionRoles(String p_name, MActionExpressionEditor o)
  {
    if (postprocessExpressionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processNodeMain(String p_name, AttributeList p_attrs)
  {
    MNode o = (MNode)liStack.get(liStack.size()-1);

    if (processNodeAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processNodeRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessNodeMain(String p_name)
  {
    MNode o = (MNode)liStack.get(liStack.size()-2);

    if (postprocessNodeAttributes(p_name, o))
    {
      return;
    }

    if (postprocessNodeRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processNodeAttributes(String p_name, AttributeList p_attrs, MNode o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessNodeAttributes(String p_name, MNode o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processNodeRoles(String p_name, AttributeList p_attrs, MNode o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Node."))
    {
      String lastName = p_name.substring(21);

      if (lastName.equals("resident"))
      {
        lastMethod = "resident";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessNodeRoles(String p_name, MNode o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Node."))
    {
      String lastName = p_name.substring(21);

      if (lastName.equals("resident"))
      {
        MComponent el = (MComponent)lastObject;
        if (null != el)
        {
          o.addResident(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processIterationExpressionMain(String p_name, AttributeList p_attrs)
  {
    MIterationExpressionEditor o = (MIterationExpressionEditor)liStack.get(liStack.size()-1);

    if (processIterationExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processIterationExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessIterationExpressionMain(String p_name)
  {
    MIterationExpressionEditor o = (MIterationExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessIterationExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessIterationExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processIterationExpressionAttributes(String p_name, AttributeList p_attrs, MIterationExpressionEditor o)
  {
    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessIterationExpressionAttributes(String p_name, MIterationExpressionEditor o)
  {
    if (postprocessExpressionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processIterationExpressionRoles(String p_name, AttributeList p_attrs, MIterationExpressionEditor o)
  {
    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessIterationExpressionRoles(String p_name, MIterationExpressionEditor o)
  {
    if (postprocessExpressionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processCallStateMain(String p_name, AttributeList p_attrs)
  {
    MCallState o = (MCallState)liStack.get(liStack.size()-1);

    if (processCallStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processCallStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessCallStateMain(String p_name)
  {
    MCallState o = (MCallState)liStack.get(liStack.size()-2);

    if (postprocessCallStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessCallStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processCallStateAttributes(String p_name, AttributeList p_attrs, MCallState o)
  {
    if (processActionStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessCallStateAttributes(String p_name, MCallState o)
  {
    if (postprocessActionStateAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processCallStateRoles(String p_name, AttributeList p_attrs, MCallState o)
  {
    if (processActionStateRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessCallStateRoles(String p_name, MCallState o)
  {
    if (postprocessActionStateRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processInterfaceMain(String p_name, AttributeList p_attrs)
  {
    MInterface o = (MInterface)liStack.get(liStack.size()-1);

    if (processInterfaceAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processInterfaceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessInterfaceMain(String p_name)
  {
    MInterface o = (MInterface)liStack.get(liStack.size()-2);

    if (postprocessInterfaceAttributes(p_name, o))
    {
      return;
    }

    if (postprocessInterfaceRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processInterfaceAttributes(String p_name, AttributeList p_attrs, MInterface o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessInterfaceAttributes(String p_name, MInterface o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processInterfaceRoles(String p_name, AttributeList p_attrs, MInterface o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessInterfaceRoles(String p_name, MInterface o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processMappingExpressionMain(String p_name, AttributeList p_attrs)
  {
    MMappingExpressionEditor o = (MMappingExpressionEditor)liStack.get(liStack.size()-1);

    if (processMappingExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processMappingExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessMappingExpressionMain(String p_name)
  {
    MMappingExpressionEditor o = (MMappingExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessMappingExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessMappingExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processMappingExpressionAttributes(String p_name, AttributeList p_attrs, MMappingExpressionEditor o)
  {
    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessMappingExpressionAttributes(String p_name, MMappingExpressionEditor o)
  {
    if (postprocessExpressionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processMappingExpressionRoles(String p_name, AttributeList p_attrs, MMappingExpressionEditor o)
  {
    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessMappingExpressionRoles(String p_name, MMappingExpressionEditor o)
  {
    if (postprocessExpressionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processComponentInstanceMain(String p_name, AttributeList p_attrs)
  {
    MComponentInstance o = (MComponentInstance)liStack.get(liStack.size()-1);

    if (processComponentInstanceAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processComponentInstanceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessComponentInstanceMain(String p_name)
  {
    MComponentInstance o = (MComponentInstance)liStack.get(liStack.size()-2);

    if (postprocessComponentInstanceAttributes(p_name, o))
    {
      return;
    }

    if (postprocessComponentInstanceRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processComponentInstanceAttributes(String p_name, AttributeList p_attrs, MComponentInstance o)
  {
    if (processInstanceAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessComponentInstanceAttributes(String p_name, MComponentInstance o)
  {
    if (postprocessInstanceAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processComponentInstanceRoles(String p_name, AttributeList p_attrs, MComponentInstance o)
  {
    if (processInstanceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.ComponentInstance."))
    {
      String lastName = p_name.substring(54);

      if (lastName.equals("resident"))
      {
        lastMethod = "resident";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("nodeInstance"))
      {
        lastMethod = "nodeInstance";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessComponentInstanceRoles(String p_name, MComponentInstance o)
  {
    if (postprocessInstanceRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.ComponentInstance."))
    {
      String lastName = p_name.substring(54);

      if (lastName.equals("resident"))
      {
        MInstance el = (MInstance)lastObject;
        if (null != el)
        {
          o.addResident(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("nodeInstance"))
      {
        MNodeInstance el = (MNodeInstance)lastObject;
        if (null != el)
        {
          o.setNodeInstance(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processObjectFlowStateMain(String p_name, AttributeList p_attrs)
  {
    MObjectFlowState o = (MObjectFlowState)liStack.get(liStack.size()-1);

    if (processObjectFlowStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processObjectFlowStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessObjectFlowStateMain(String p_name)
  {
    MObjectFlowState o = (MObjectFlowState)liStack.get(liStack.size()-2);

    if (postprocessObjectFlowStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessObjectFlowStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processObjectFlowStateAttributes(String p_name, AttributeList p_attrs, MObjectFlowState o)
  {
    if (processSimpleStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ObjectFlowState."))
    {
      String lastName = p_name.substring(52);

      if (lastName.equals("isSynch"))
      {
        lastMethod = "isSynch";
        lastMethodType = true;
        o.setSynch(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessObjectFlowStateAttributes(String p_name, MObjectFlowState o)
  {
    if (postprocessSimpleStateAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ObjectFlowState."))
    {
      String lastName = p_name.substring(52);

      if (lastName.equals("isSynch"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processObjectFlowStateRoles(String p_name, AttributeList p_attrs, MObjectFlowState o)
  {
    if (processSimpleStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ObjectFlowState."))
    {
      String lastName = p_name.substring(52);

      if (lastName.equals("parameter"))
      {
        lastMethod = "parameter";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("type"))
      {
        lastMethod = "type";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessObjectFlowStateRoles(String p_name, MObjectFlowState o)
  {
    if (postprocessSimpleStateRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ObjectFlowState."))
    {
      String lastName = p_name.substring(52);

      if (lastName.equals("parameter"))
      {
        MParameter el = (MParameter)lastObject;
        if (null != el)
        {
          o.addParameter(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("type"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.setType(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processSignalEventMain(String p_name, AttributeList p_attrs)
  {
    MSignalEvent o = (MSignalEvent)liStack.get(liStack.size()-1);

    if (processSignalEventAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processSignalEventRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessSignalEventMain(String p_name)
  {
    MSignalEvent o = (MSignalEvent)liStack.get(liStack.size()-2);

    if (postprocessSignalEventAttributes(p_name, o))
    {
      return;
    }

    if (postprocessSignalEventRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processSignalEventAttributes(String p_name, AttributeList p_attrs, MSignalEvent o)
  {
    if (processEventAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessSignalEventAttributes(String p_name, MSignalEvent o)
  {
    if (postprocessEventAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processSignalEventRoles(String p_name, AttributeList p_attrs, MSignalEvent o)
  {
    if (processEventRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.SignalEvent."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("signal"))
      {
        lastMethod = "signal";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessSignalEventRoles(String p_name, MSignalEvent o)
  {
    if (postprocessEventRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.SignalEvent."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("signal"))
      {
        MSignal el = (MSignal)lastObject;
        if (null != el)
        {
          o.setSignal(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processModelElementAttributes(String p_name, AttributeList p_attrs, MModelElement o)
  {
    if (processElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.ModelElement."))
    {
      String lastName = p_name.substring(29);

      if (lastName.equals("isSpecification"))
      {
        lastMethod = "isSpecification";
        lastMethodType = true;
        o.setSpecification(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("visibility"))
      {
        lastMethod = "visibility";
        lastMethodType = true;
        o.setVisibility(MVisibilityKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("name"))
      {
        lastMethod = "name";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessModelElementAttributes(String p_name, MModelElement o)
  {
    if (postprocessElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.ModelElement."))
    {
      String lastName = p_name.substring(29);

      if (lastName.equals("isSpecification"))
      {
        return true;
      }

      if (lastName.equals("visibility"))
      {
        return true;
      }

      if (lastName.equals("name"))
      {
        o.setName(lastString.toString());
        return true;
      }

    }
    return false;
  }

  public boolean processModelElementRoles(String p_name, AttributeList p_attrs, MModelElement o)
  {
    if (processElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.ModelElement."))
    {
      String lastName = p_name.substring(29);

      if (lastName.equals("elementImport"))
      {
        lastMethod = "elementImport2";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("classifierRole"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("collaboration"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("partition"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("behavior"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("stereotype"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("templateParameter2"))
      {
        lastMethod = "templateParameter2";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("elementResidence"))
      {
        lastMethod = "elementResidence";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("comment"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("binding"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("templateParameter3"))
      {
        lastMethod = "templateParameter3";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("sourceFlow"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("targetFlow"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("templateParameter"))
      {
        lastMethod = "templateParameter";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("presentation"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("supplierDependency"))
      {
        lastMethod = "supplierDependency";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("constraint"))
      {
        lastMethod = "constraint";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("taggedValue"))
      {
        lastMethod = "taggedValue";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("clientDependency"))
      {
        lastMethod = "clientDependency";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("namespace"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessModelElementRoles(String p_name, MModelElement o)
  {
    if (postprocessElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.ModelElement."))
    {
      String lastName = p_name.substring(29);

      if (lastName.equals("elementImport"))
      {
        MElementImport el = (MElementImport)lastObject;
        if (null != el)
        {
          o.addElementImport2(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("classifierRole"))
      {
        MClassifierRole el = (MClassifierRole)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("collaboration"))
      {
        MCollaboration el = (MCollaboration)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("partition"))
      {
        MPartition el = (MPartition)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("behavior"))
      {
        MStateMachine el = (MStateMachine)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("stereotype"))
      {
        MStereotype el = (MStereotype)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("templateParameter2"))
      {
        MTemplateParameter el = (MTemplateParameter)lastObject;
        if (null != el)
        {
          o.addTemplateParameter2(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("elementResidence"))
      {
        MElementResidence el = (MElementResidence)lastObject;
        if (null != el)
        {
          o.addElementResidence(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("comment"))
      {
        MComment el = (MComment)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("binding"))
      {
        MBinding el = (MBinding)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("templateParameter3"))
      {
        MTemplateParameter el = (MTemplateParameter)lastObject;
        if (null != el)
        {
          o.addTemplateParameter3(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("sourceFlow"))
      {
        MFlow el = (MFlow)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("targetFlow"))
      {
        MFlow el = (MFlow)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("templateParameter"))
      {
        MTemplateParameter el = (MTemplateParameter)lastObject;
        if (null != el)
        {
          o.addTemplateParameter(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("presentation"))
      {
        MPresentationElement el = (MPresentationElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("supplierDependency"))
      {
        MDependency el = (MDependency)lastObject;
        if (null != el)
        {
          o.addSupplierDependency(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("constraint"))
      {
        MConstraint el = (MConstraint)lastObject;
        if (null != el)
        {
          o.addConstraint(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("taggedValue"))
      {
        MTaggedValue el = (MTaggedValue)lastObject;
        if (null != el)
        {
          o.addTaggedValue(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("clientDependency"))
      {
        MDependency el = (MDependency)lastObject;
        if (null != el)
        {
          o.addClientDependency(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("namespace"))
      {
        MNamespace el = (MNamespace)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processDependencyMain(String p_name, AttributeList p_attrs)
  {
    MDependency o = (MDependency)liStack.get(liStack.size()-1);

    if (processDependencyAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processDependencyRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessDependencyMain(String p_name)
  {
    MDependency o = (MDependency)liStack.get(liStack.size()-2);

    if (postprocessDependencyAttributes(p_name, o))
    {
      return;
    }

    if (postprocessDependencyRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processDependencyAttributes(String p_name, AttributeList p_attrs, MDependency o)
  {
    if (processRelationshipAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessDependencyAttributes(String p_name, MDependency o)
  {
    if (postprocessRelationshipAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processDependencyRoles(String p_name, AttributeList p_attrs, MDependency o)
  {
    if (processRelationshipRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Dependency."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("supplier"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("client"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessDependencyRoles(String p_name, MDependency o)
  {
    if (postprocessRelationshipRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Dependency."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("supplier"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("client"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processInstanceMain(String p_name, AttributeList p_attrs)
  {
    MInstance o = (MInstance)liStack.get(liStack.size()-1);

    if (processInstanceAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processInstanceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessInstanceMain(String p_name)
  {
    MInstance o = (MInstance)liStack.get(liStack.size()-2);

    if (postprocessInstanceAttributes(p_name, o))
    {
      return;
    }

    if (postprocessInstanceRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processInstanceAttributes(String p_name, AttributeList p_attrs, MInstance o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessInstanceAttributes(String p_name, MInstance o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processInstanceRoles(String p_name, AttributeList p_attrs, MInstance o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Instance."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("stimulus2"))
      {
        lastMethod = "stimulus2";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("componentInstance"))
      {
        lastMethod = "componentInstance";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("stimulus3"))
      {
        lastMethod = "stimulus3";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("stimulus1"))
      {
        lastMethod = "stimulus1";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("slot"))
      {
        lastMethod = "slot";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("linkEnd"))
      {
        lastMethod = "linkEnd";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("attributeLink"))
      {
        lastMethod = "attributeLink";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("classifier"))
      {
        lastMethod = "classifier";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessInstanceRoles(String p_name, MInstance o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Instance."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("stimulus2"))
      {
        MStimulus el = (MStimulus)lastObject;
        if (null != el)
        {
          o.addStimulus2(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("componentInstance"))
      {
        MComponentInstance el = (MComponentInstance)lastObject;
        if (null != el)
        {
          o.setComponentInstance(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("stimulus3"))
      {
        MStimulus el = (MStimulus)lastObject;
        if (null != el)
        {
          o.addStimulus3(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("stimulus1"))
      {
        MStimulus el = (MStimulus)lastObject;
        if (null != el)
        {
          o.addStimulus1(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("slot"))
      {
        MAttributeLink el = (MAttributeLink)lastObject;
        if (null != el)
        {
          o.addSlot(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("linkEnd"))
      {
        MLinkEnd el = (MLinkEnd)lastObject;
        if (null != el)
        {
          o.addLinkEnd(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("attributeLink"))
      {
        MAttributeLink el = (MAttributeLink)lastObject;
        if (null != el)
        {
          o.addAttributeLink(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("classifier"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.addClassifier(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processAttributeMain(String p_name, AttributeList p_attrs)
  {
    MAttribute o = (MAttribute)liStack.get(liStack.size()-1);

    if (processAttributeAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processAttributeRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessAttributeMain(String p_name)
  {
    MAttribute o = (MAttribute)liStack.get(liStack.size()-2);

    if (postprocessAttributeAttributes(p_name, o))
    {
      return;
    }

    if (postprocessAttributeRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processAttributeAttributes(String p_name, AttributeList p_attrs, MAttribute o)
  {
    if (processStructuralFeatureAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Attribute."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("initialValue"))
      {
        lastMethod = "initialValue";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessAttributeAttributes(String p_name, MAttribute o)
  {
    if (postprocessStructuralFeatureAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Attribute."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("initialValue"))
      {
        if (!(lastObject instanceof Link))
        {
          MExpression oexp = null;
          if (lastObject instanceof MExpression)
          {
            oexp = (MExpression)lastObject;
            o.setInitialValue(oexp);
          }
          else
          {
            oexp = ((MExpressionEditor)lastObject).toExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setInitialValue(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processAttributeRoles(String p_name, AttributeList p_attrs, MAttribute o)
  {
    if (processStructuralFeatureRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Attribute."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("attributeLink"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("associationEndRole"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("associationEnd"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessAttributeRoles(String p_name, MAttribute o)
  {
    if (postprocessStructuralFeatureRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Attribute."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("attributeLink"))
      {
        MAttributeLink el = (MAttributeLink)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("associationEndRole"))
      {
        MAssociationEndRole el = (MAssociationEndRole)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("associationEnd"))
      {
        MAssociationEnd el = (MAssociationEnd)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processGeneralizationMain(String p_name, AttributeList p_attrs)
  {
    MGeneralization o = (MGeneralization)liStack.get(liStack.size()-1);

    if (processGeneralizationAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processGeneralizationRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessGeneralizationMain(String p_name)
  {
    MGeneralization o = (MGeneralization)liStack.get(liStack.size()-2);

    if (postprocessGeneralizationAttributes(p_name, o))
    {
      return;
    }

    if (postprocessGeneralizationRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processGeneralizationAttributes(String p_name, AttributeList p_attrs, MGeneralization o)
  {
    if (processRelationshipAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Generalization."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("discriminator"))
      {
        lastMethod = "discriminator";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessGeneralizationAttributes(String p_name, MGeneralization o)
  {
    if (postprocessRelationshipAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Generalization."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("discriminator"))
      {
        o.setDiscriminator(lastString.toString());
        return true;
      }

    }
    return false;
  }

  public boolean processGeneralizationRoles(String p_name, AttributeList p_attrs, MGeneralization o)
  {
    if (processRelationshipRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Generalization."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("powertype"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("parent"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("child"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessGeneralizationRoles(String p_name, MGeneralization o)
  {
    if (postprocessRelationshipRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Generalization."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("powertype"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("parent"))
      {
        MGeneralizableElement el = (MGeneralizableElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("child"))
      {
        MGeneralizableElement el = (MGeneralizableElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processCallActionMain(String p_name, AttributeList p_attrs)
  {
    MCallAction o = (MCallAction)liStack.get(liStack.size()-1);

    if (processCallActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processCallActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessCallActionMain(String p_name)
  {
    MCallAction o = (MCallAction)liStack.get(liStack.size()-2);

    if (postprocessCallActionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessCallActionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processCallActionAttributes(String p_name, AttributeList p_attrs, MCallAction o)
  {
    if (processActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessCallActionAttributes(String p_name, MCallAction o)
  {
    if (postprocessActionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processCallActionRoles(String p_name, AttributeList p_attrs, MCallAction o)
  {
    if (processActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.CallAction."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("operation"))
      {
        lastMethod = "operation";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessCallActionRoles(String p_name, MCallAction o)
  {
    if (postprocessActionRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.CallAction."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("operation"))
      {
        MOperation el = (MOperation)lastObject;
        if (null != el)
        {
          o.setOperation(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processGuardMain(String p_name, AttributeList p_attrs)
  {
    MGuard o = (MGuard)liStack.get(liStack.size()-1);

    if (processGuardAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processGuardRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessGuardMain(String p_name)
  {
    MGuard o = (MGuard)liStack.get(liStack.size()-2);

    if (postprocessGuardAttributes(p_name, o))
    {
      return;
    }

    if (postprocessGuardRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processGuardAttributes(String p_name, AttributeList p_attrs, MGuard o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Guard."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("expression"))
      {
        lastMethod = "expression";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessGuardAttributes(String p_name, MGuard o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Guard."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("expression"))
      {
        if (!(lastObject instanceof Link))
        {
          MBooleanExpression oexp = null;
          if (lastObject instanceof MBooleanExpression)
          {
            oexp = (MBooleanExpression)lastObject;
            o.setExpression(oexp);
          }
          else
          {
            oexp = ((MBooleanExpressionEditor)lastObject).toBooleanExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setExpression(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processGuardRoles(String p_name, AttributeList p_attrs, MGuard o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Guard."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("transition"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessGuardRoles(String p_name, MGuard o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Guard."))
    {
      String lastName = p_name.substring(41);

      if (lastName.equals("transition"))
      {
        MTransition el = (MTransition)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processClassifierMain(String p_name, AttributeList p_attrs)
  {
    MClassifier o = (MClassifier)liStack.get(liStack.size()-1);

    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessClassifierMain(String p_name)
  {
    MClassifier o = (MClassifier)liStack.get(liStack.size()-2);

    if (postprocessClassifierAttributes(p_name, o))
    {
      return;
    }

    if (postprocessClassifierRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processClassifierAttributes(String p_name, AttributeList p_attrs, MClassifier o)
  {
    if (processNamespaceAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("isAbstract"))
      {
        lastMethod = "isAbstract";
        lastMethodType = true;
        o.setAbstract(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        lastMethod = "isLeaf";
        lastMethodType = true;
        o.setLeaf(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        lastMethod = "isRoot";
        lastMethodType = true;
        o.setRoot(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessClassifierAttributes(String p_name, MClassifier o)
  {
    if (postprocessNamespaceAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("isAbstract"))
      {
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processClassifierRoles(String p_name, AttributeList p_attrs, MClassifier o)
  {
    if (processNamespaceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Classifier."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("createAction"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("instance"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("collaboration"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("classifierRole"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("classifierInState"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("objectFlowState"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("powertypeRange"))
      {
        lastMethod = "powertypeRange";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("participant"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("associationEnd"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("parameter"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("structuralFeature"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("feature"))
      {
        lastMethod = "feature";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("specialization"))
      {
        lastMethod = "specialization";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("generalization"))
      {
        lastMethod = "generalization";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessClassifierRoles(String p_name, MClassifier o)
  {
    if (postprocessNamespaceRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Classifier."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("createAction"))
      {
        MCreateAction el = (MCreateAction)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("instance"))
      {
        MInstance el = (MInstance)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("collaboration"))
      {
        MCollaboration el = (MCollaboration)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("classifierRole"))
      {
        MClassifierRole el = (MClassifierRole)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("classifierInState"))
      {
        MClassifierInState el = (MClassifierInState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("objectFlowState"))
      {
        MObjectFlowState el = (MObjectFlowState)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("powertypeRange"))
      {
        MGeneralization el = (MGeneralization)lastObject;
        if (null != el)
        {
          o.addPowertypeRange(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("participant"))
      {
        MAssociationEnd el = (MAssociationEnd)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("associationEnd"))
      {
        MAssociationEnd el = (MAssociationEnd)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("parameter"))
      {
        MParameter el = (MParameter)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("structuralFeature"))
      {
        MStructuralFeature el = (MStructuralFeature)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("feature"))
      {
        MFeature el = (MFeature)lastObject;
        if (null != el)
        {
          o.addFeature(el);
          return true;
        }
        return false;
      }
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("specialization"))
      {
        MGeneralization el = (MGeneralization)lastObject;
        if (null != el)
        {
          o.addSpecialization(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("generalization"))
      {
        MGeneralization el = (MGeneralization)lastObject;
        if (null != el)
        {
          o.addGeneralization(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processObjectMain(String p_name, AttributeList p_attrs)
  {
    MObject o = (MObject)liStack.get(liStack.size()-1);

    if (processObjectAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processObjectRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessObjectMain(String p_name)
  {
    MObject o = (MObject)liStack.get(liStack.size()-2);

    if (postprocessObjectAttributes(p_name, o))
    {
      return;
    }

    if (postprocessObjectRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processObjectAttributes(String p_name, AttributeList p_attrs, MObject o)
  {
    if (processInstanceAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessObjectAttributes(String p_name, MObject o)
  {
    if (postprocessInstanceAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processObjectRoles(String p_name, AttributeList p_attrs, MObject o)
  {
    if (processInstanceRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessObjectRoles(String p_name, MObject o)
  {
    if (postprocessInstanceRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processOperationMain(String p_name, AttributeList p_attrs)
  {
    MOperation o = (MOperation)liStack.get(liStack.size()-1);

    if (processOperationAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processOperationRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessOperationMain(String p_name)
  {
    MOperation o = (MOperation)liStack.get(liStack.size()-2);

    if (postprocessOperationAttributes(p_name, o))
    {
      return;
    }

    if (postprocessOperationRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processOperationAttributes(String p_name, AttributeList p_attrs, MOperation o)
  {
    if (processBehavioralFeatureAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Operation."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("specification"))
      {
        lastMethod = "specification";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isAbstract"))
      {
        lastMethod = "isAbstract";
        lastMethodType = true;
        o.setAbstract(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        lastMethod = "isLeaf";
        lastMethodType = true;
        o.setLeaf(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        lastMethod = "isRoot";
        lastMethodType = true;
        o.setRoot(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("concurrency"))
      {
        lastMethod = "concurrency";
        lastMethodType = true;
        o.setConcurrency(MCallConcurrencyKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessOperationAttributes(String p_name, MOperation o)
  {
    if (postprocessBehavioralFeatureAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Operation."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("specification"))
      {
        o.setSpecification(lastString.toString());
        return true;
      }

      if (lastName.equals("isAbstract"))
      {
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        return true;
      }

      if (lastName.equals("concurrency"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processOperationRoles(String p_name, AttributeList p_attrs, MOperation o)
  {
    if (processBehavioralFeatureRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Operation."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("callAction"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("collaboration"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("occurrence"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("method"))
      {
        lastMethod = "method";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessOperationRoles(String p_name, MOperation o)
  {
    if (postprocessBehavioralFeatureRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Operation."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("callAction"))
      {
        MCallAction el = (MCallAction)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("collaboration"))
      {
        MCollaboration el = (MCollaboration)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("occurrence"))
      {
        MCallEvent el = (MCallEvent)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("method"))
      {
        MMethod el = (MMethod)lastObject;
        if (null != el)
        {
          o.addMethod(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processInteractionMain(String p_name, AttributeList p_attrs)
  {
    MInteraction o = (MInteraction)liStack.get(liStack.size()-1);

    if (processInteractionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processInteractionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessInteractionMain(String p_name)
  {
    MInteraction o = (MInteraction)liStack.get(liStack.size()-2);

    if (postprocessInteractionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessInteractionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processInteractionAttributes(String p_name, AttributeList p_attrs, MInteraction o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessInteractionAttributes(String p_name, MInteraction o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processInteractionRoles(String p_name, AttributeList p_attrs, MInteraction o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.Interaction."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("context"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("message"))
      {
        lastMethod = "message";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessInteractionRoles(String p_name, MInteraction o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.Interaction."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("context"))
      {
        MCollaboration el = (MCollaboration)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("message"))
      {
        MMessage el = (MMessage)lastObject;
        if (null != el)
        {
          o.addMessage(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processFinalStateMain(String p_name, AttributeList p_attrs)
  {
    MFinalState o = (MFinalState)liStack.get(liStack.size()-1);

    if (processFinalStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processFinalStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessFinalStateMain(String p_name)
  {
    MFinalState o = (MFinalState)liStack.get(liStack.size()-2);

    if (postprocessFinalStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessFinalStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processFinalStateAttributes(String p_name, AttributeList p_attrs, MFinalState o)
  {
    if (processStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessFinalStateAttributes(String p_name, MFinalState o)
  {
    if (postprocessStateAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processFinalStateRoles(String p_name, AttributeList p_attrs, MFinalState o)
  {
    if (processStateRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessFinalStateRoles(String p_name, MFinalState o)
  {
    if (postprocessStateRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processBindingMain(String p_name, AttributeList p_attrs)
  {
    MBinding o = (MBinding)liStack.get(liStack.size()-1);

    if (processBindingAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processBindingRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessBindingMain(String p_name)
  {
    MBinding o = (MBinding)liStack.get(liStack.size()-2);

    if (postprocessBindingAttributes(p_name, o))
    {
      return;
    }

    if (postprocessBindingRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processBindingAttributes(String p_name, AttributeList p_attrs, MBinding o)
  {
    if (processDependencyAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessBindingAttributes(String p_name, MBinding o)
  {
    if (postprocessDependencyAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processBindingRoles(String p_name, AttributeList p_attrs, MBinding o)
  {
    if (processDependencyRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Binding."))
    {
      String lastName = p_name.substring(24);

      if (lastName.equals("argument"))
      {
        lastMethod = "argument";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessBindingRoles(String p_name, MBinding o)
  {
    if (postprocessDependencyRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Binding."))
    {
      String lastName = p_name.substring(24);

      if (lastName.equals("argument"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addArgument(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processCreateActionMain(String p_name, AttributeList p_attrs)
  {
    MCreateAction o = (MCreateAction)liStack.get(liStack.size()-1);

    if (processCreateActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processCreateActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessCreateActionMain(String p_name)
  {
    MCreateAction o = (MCreateAction)liStack.get(liStack.size()-2);

    if (postprocessCreateActionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessCreateActionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processCreateActionAttributes(String p_name, AttributeList p_attrs, MCreateAction o)
  {
    if (processActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessCreateActionAttributes(String p_name, MCreateAction o)
  {
    if (postprocessActionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processCreateActionRoles(String p_name, AttributeList p_attrs, MCreateAction o)
  {
    if (processActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.CreateAction."))
    {
      String lastName = p_name.substring(49);

      if (lastName.equals("instantiation"))
      {
        lastMethod = "instantiation";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessCreateActionRoles(String p_name, MCreateAction o)
  {
    if (postprocessActionRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.CreateAction."))
    {
      String lastName = p_name.substring(49);

      if (lastName.equals("instantiation"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.setInstantiation(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processStereotypeMain(String p_name, AttributeList p_attrs)
  {
    MStereotype o = (MStereotype)liStack.get(liStack.size()-1);

    if (processStereotypeAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processStereotypeRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessStereotypeMain(String p_name)
  {
    MStereotype o = (MStereotype)liStack.get(liStack.size()-2);

    if (postprocessStereotypeAttributes(p_name, o))
    {
      return;
    }

    if (postprocessStereotypeRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processStereotypeAttributes(String p_name, AttributeList p_attrs, MStereotype o)
  {
    if (processGeneralizableElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Extension_Mechanisms.Stereotype."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("baseClass"))
      {
        lastMethod = "baseClass";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("icon"))
      {
        lastMethod = "icon";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessStereotypeAttributes(String p_name, MStereotype o)
  {
    if (postprocessGeneralizableElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Extension_Mechanisms.Stereotype."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("baseClass"))
      {
        o.setBaseClass(lastString.toString());
        return true;
      }

      if (lastName.equals("icon"))
      {
        o.setIcon(lastString.toString());
        return true;
      }

    }
    return false;
  }

  public boolean processStereotypeRoles(String p_name, AttributeList p_attrs, MStereotype o)
  {
    if (processGeneralizableElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Extension_Mechanisms.Stereotype."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("stereotypeConstraint"))
      {
        lastMethod = "stereotypeConstraint";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("extendedElement"))
      {
        lastMethod = "extendedElement";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("requiredTag"))
      {
        lastMethod = "requiredTag";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessStereotypeRoles(String p_name, MStereotype o)
  {
    if (postprocessGeneralizableElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Extension_Mechanisms.Stereotype."))
    {
      String lastName = p_name.substring(43);

      if (lastName.equals("stereotypeConstraint"))
      {
        MConstraint el = (MConstraint)lastObject;
        if (null != el)
        {
          o.addStereotypeConstraint(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("extendedElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addExtendedElement(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("requiredTag"))
      {
        MTaggedValue el = (MTaggedValue)lastObject;
        if (null != el)
        {
          o.addRequiredTag(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processSimpleStateMain(String p_name, AttributeList p_attrs)
  {
    MSimpleState o = (MSimpleState)liStack.get(liStack.size()-1);

    if (processSimpleStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processSimpleStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessSimpleStateMain(String p_name)
  {
    MSimpleState o = (MSimpleState)liStack.get(liStack.size()-2);

    if (postprocessSimpleStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessSimpleStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processSimpleStateAttributes(String p_name, AttributeList p_attrs, MSimpleState o)
  {
    if (processStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessSimpleStateAttributes(String p_name, MSimpleState o)
  {
    if (postprocessStateAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processSimpleStateRoles(String p_name, AttributeList p_attrs, MSimpleState o)
  {
    if (processStateRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessSimpleStateRoles(String p_name, MSimpleState o)
  {
    if (postprocessStateRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processSubsystemMain(String p_name, AttributeList p_attrs)
  {
    MSubsystem o = (MSubsystem)liStack.get(liStack.size()-1);

    if (processSubsystemAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processSubsystemRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessSubsystemMain(String p_name)
  {
    MSubsystem o = (MSubsystem)liStack.get(liStack.size()-2);

    if (postprocessSubsystemAttributes(p_name, o))
    {
      return;
    }

    if (postprocessSubsystemRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processSubsystemAttributes(String p_name, AttributeList p_attrs, MSubsystem o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Model_Management.Subsystem."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("isInstantiable"))
      {
        lastMethod = "isInstantiable";
        lastMethodType = true;
        o.setInstantiable(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessSubsystemAttributes(String p_name, MSubsystem o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Model_Management.Subsystem."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("isInstantiable"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processSubsystemRoles(String p_name, AttributeList p_attrs, MSubsystem o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Model_Management.Package."))
    {
      String lastName = p_name.substring(25);

      if (lastName.equals("elementImport"))
      {
        lastMethod = "elementImport";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessSubsystemRoles(String p_name, MSubsystem o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Model_Management.Package."))
    {
      String lastName = p_name.substring(25);

      if (lastName.equals("elementImport"))
      {
        MElementImport el = (MElementImport)lastObject;
        if (null != el)
        {
          o.addElementImport(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processIncludeMain(String p_name, AttributeList p_attrs)
  {
    MInclude o = (MInclude)liStack.get(liStack.size()-1);

    if (processIncludeAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processIncludeRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessIncludeMain(String p_name)
  {
    MInclude o = (MInclude)liStack.get(liStack.size()-2);

    if (postprocessIncludeAttributes(p_name, o))
    {
      return;
    }

    if (postprocessIncludeRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processIncludeAttributes(String p_name, AttributeList p_attrs, MInclude o)
  {
    if (processRelationshipAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessIncludeAttributes(String p_name, MInclude o)
  {
    if (postprocessRelationshipAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processIncludeRoles(String p_name, AttributeList p_attrs, MInclude o)
  {
    if (processRelationshipRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.Include."))
    {
      String lastName = p_name.substring(38);

      if (lastName.equals("base"))
      {
        lastMethod = "base";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("addition"))
      {
        lastMethod = "addition";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessIncludeRoles(String p_name, MInclude o)
  {
    if (postprocessRelationshipRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.Include."))
    {
      String lastName = p_name.substring(38);

      if (lastName.equals("base"))
      {
        MUseCase el = (MUseCase)lastObject;
        if (null != el)
        {
          o.setBase(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("addition"))
      {
        MUseCase el = (MUseCase)lastObject;
        if (null != el)
        {
          o.setAddition(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processAssociationEndRoleMain(String p_name, AttributeList p_attrs)
  {
    MAssociationEndRole o = (MAssociationEndRole)liStack.get(liStack.size()-1);

    if (processAssociationEndRoleAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processAssociationEndRoleRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessAssociationEndRoleMain(String p_name)
  {
    MAssociationEndRole o = (MAssociationEndRole)liStack.get(liStack.size()-2);

    if (postprocessAssociationEndRoleAttributes(p_name, o))
    {
      return;
    }

    if (postprocessAssociationEndRoleRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processAssociationEndRoleAttributes(String p_name, AttributeList p_attrs, MAssociationEndRole o)
  {
    if (processAssociationEndAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessAssociationEndRoleAttributes(String p_name, MAssociationEndRole o)
  {
    if (postprocessAssociationEndAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processAssociationEndRoleRoles(String p_name, AttributeList p_attrs, MAssociationEndRole o)
  {
    if (processAssociationEndRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.AssociationEndRole."))
    {
      String lastName = p_name.substring(54);

      if (lastName.equals("availableQualifier"))
      {
        lastMethod = "availableQualifier";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("base"))
      {
        lastMethod = "base";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessAssociationEndRoleRoles(String p_name, MAssociationEndRole o)
  {
    if (postprocessAssociationEndRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.AssociationEndRole."))
    {
      String lastName = p_name.substring(54);

      if (lastName.equals("availableQualifier"))
      {
        MAttribute el = (MAttribute)lastObject;
        if (null != el)
        {
          o.addAvailableQualifier(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("base"))
      {
        MAssociationEnd el = (MAssociationEnd)lastObject;
        if (null != el)
        {
          o.setBase(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processCollaborationMain(String p_name, AttributeList p_attrs)
  {
    MCollaboration o = (MCollaboration)liStack.get(liStack.size()-1);

    if (processCollaborationAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processCollaborationRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessCollaborationMain(String p_name)
  {
    MCollaboration o = (MCollaboration)liStack.get(liStack.size()-2);

    if (postprocessCollaborationAttributes(p_name, o))
    {
      return;
    }

    if (postprocessCollaborationRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processCollaborationAttributes(String p_name, AttributeList p_attrs, MCollaboration o)
  {
    if (processGeneralizableElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessCollaborationAttributes(String p_name, MCollaboration o)
  {
    if (postprocessGeneralizableElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processCollaborationRoles(String p_name, AttributeList p_attrs, MCollaboration o)
  {
    if (processGeneralizableElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.Collaboration."))
    {
      String lastName = p_name.substring(49);

      if (lastName.equals("representedOperation"))
      {
        lastMethod = "representedOperation";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("representedClassifier"))
      {
        lastMethod = "representedClassifier";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("constrainingElement"))
      {
        lastMethod = "constrainingElement";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("interaction"))
      {
        lastMethod = "interaction";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }
    if (p_name.startsWith("Foundation.Core.Namespace."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("ownedElement"))
      {
        lastMethod = "ownedElement";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessCollaborationRoles(String p_name, MCollaboration o)
  {
    if (postprocessGeneralizableElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.Collaboration."))
    {
      String lastName = p_name.substring(49);

      if (lastName.equals("representedOperation"))
      {
        MOperation el = (MOperation)lastObject;
        if (null != el)
        {
          o.setRepresentedOperation(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("representedClassifier"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.setRepresentedClassifier(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("constrainingElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addConstrainingElement(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("interaction"))
      {
        MInteraction el = (MInteraction)lastObject;
        if (null != el)
        {
          o.addInteraction(el);
          return true;
        }
        return false;
      }
    }
    if (p_name.startsWith("Foundation.Core.Namespace."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("ownedElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addOwnedElement(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processCallEventMain(String p_name, AttributeList p_attrs)
  {
    MCallEvent o = (MCallEvent)liStack.get(liStack.size()-1);

    if (processCallEventAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processCallEventRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessCallEventMain(String p_name)
  {
    MCallEvent o = (MCallEvent)liStack.get(liStack.size()-2);

    if (postprocessCallEventAttributes(p_name, o))
    {
      return;
    }

    if (postprocessCallEventRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processCallEventAttributes(String p_name, AttributeList p_attrs, MCallEvent o)
  {
    if (processEventAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessCallEventAttributes(String p_name, MCallEvent o)
  {
    if (postprocessEventAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processCallEventRoles(String p_name, AttributeList p_attrs, MCallEvent o)
  {
    if (processEventRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.CallEvent."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("operation"))
      {
        lastMethod = "operation";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessCallEventRoles(String p_name, MCallEvent o)
  {
    if (postprocessEventRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.CallEvent."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("operation"))
      {
        MOperation el = (MOperation)lastObject;
        if (null != el)
        {
          o.setOperation(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processFlowMain(String p_name, AttributeList p_attrs)
  {
    MFlow o = (MFlow)liStack.get(liStack.size()-1);

    if (processFlowAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processFlowRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessFlowMain(String p_name)
  {
    MFlow o = (MFlow)liStack.get(liStack.size()-2);

    if (postprocessFlowAttributes(p_name, o))
    {
      return;
    }

    if (postprocessFlowRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processFlowAttributes(String p_name, AttributeList p_attrs, MFlow o)
  {
    if (processRelationshipAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessFlowAttributes(String p_name, MFlow o)
  {
    if (postprocessRelationshipAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processFlowRoles(String p_name, AttributeList p_attrs, MFlow o)
  {
    if (processRelationshipRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Flow."))
    {
      String lastName = p_name.substring(21);

      if (lastName.equals("source"))
      {
        lastMethod = "source";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("target"))
      {
        lastMethod = "target";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessFlowRoles(String p_name, MFlow o)
  {
    if (postprocessRelationshipRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Flow."))
    {
      String lastName = p_name.substring(21);

      if (lastName.equals("source"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addSource(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("target"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addTarget(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processDataValueMain(String p_name, AttributeList p_attrs)
  {
    MDataValue o = (MDataValue)liStack.get(liStack.size()-1);

    if (processDataValueAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processDataValueRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessDataValueMain(String p_name)
  {
    MDataValue o = (MDataValue)liStack.get(liStack.size()-2);

    if (postprocessDataValueAttributes(p_name, o))
    {
      return;
    }

    if (postprocessDataValueRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processDataValueAttributes(String p_name, AttributeList p_attrs, MDataValue o)
  {
    if (processInstanceAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessDataValueAttributes(String p_name, MDataValue o)
  {
    if (postprocessInstanceAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processDataValueRoles(String p_name, AttributeList p_attrs, MDataValue o)
  {
    if (processInstanceRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessDataValueRoles(String p_name, MDataValue o)
  {
    if (postprocessInstanceRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processComponentMain(String p_name, AttributeList p_attrs)
  {
    MComponent o = (MComponent)liStack.get(liStack.size()-1);

    if (processComponentAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processComponentRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessComponentMain(String p_name)
  {
    MComponent o = (MComponent)liStack.get(liStack.size()-2);

    if (postprocessComponentAttributes(p_name, o))
    {
      return;
    }

    if (postprocessComponentRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processComponentAttributes(String p_name, AttributeList p_attrs, MComponent o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessComponentAttributes(String p_name, MComponent o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processComponentRoles(String p_name, AttributeList p_attrs, MComponent o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Component."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("residentElement"))
      {
        lastMethod = "residentElement";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("deploymentLocation"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessComponentRoles(String p_name, MComponent o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Component."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("residentElement"))
      {
        MElementResidence el = (MElementResidence)lastObject;
        if (null != el)
        {
          o.addResidentElement(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("deploymentLocation"))
      {
        MNode el = (MNode)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processLinkEndMain(String p_name, AttributeList p_attrs)
  {
    MLinkEnd o = (MLinkEnd)liStack.get(liStack.size()-1);

    if (processLinkEndAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processLinkEndRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessLinkEndMain(String p_name)
  {
    MLinkEnd o = (MLinkEnd)liStack.get(liStack.size()-2);

    if (postprocessLinkEndAttributes(p_name, o))
    {
      return;
    }

    if (postprocessLinkEndRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processLinkEndAttributes(String p_name, AttributeList p_attrs, MLinkEnd o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessLinkEndAttributes(String p_name, MLinkEnd o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processLinkEndRoles(String p_name, AttributeList p_attrs, MLinkEnd o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.LinkEnd."))
    {
      String lastName = p_name.substring(44);

      if (lastName.equals("qualifiedValue"))
      {
        lastMethod = "qualifiedValue";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("associationEnd"))
      {
        lastMethod = "associationEnd";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("link"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("instance"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessLinkEndRoles(String p_name, MLinkEnd o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.LinkEnd."))
    {
      String lastName = p_name.substring(44);

      if (lastName.equals("qualifiedValue"))
      {
        MAttributeLink el = (MAttributeLink)lastObject;
        if (null != el)
        {
          o.addQualifiedValue(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("associationEnd"))
      {
        MAssociationEnd el = (MAssociationEnd)lastObject;
        if (null != el)
        {
          o.setAssociationEnd(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("link"))
      {
        MLink el = (MLink)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("instance"))
      {
        MInstance el = (MInstance)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processPartitionMain(String p_name, AttributeList p_attrs)
  {
    MPartition o = (MPartition)liStack.get(liStack.size()-1);

    if (processPartitionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processPartitionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessPartitionMain(String p_name)
  {
    MPartition o = (MPartition)liStack.get(liStack.size()-2);

    if (postprocessPartitionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessPartitionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processPartitionAttributes(String p_name, AttributeList p_attrs, MPartition o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessPartitionAttributes(String p_name, MPartition o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processPartitionRoles(String p_name, AttributeList p_attrs, MPartition o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.Partition."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("contents"))
      {
        lastMethod = "contents";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("activityGraph"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessPartitionRoles(String p_name, MPartition o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.Partition."))
    {
      String lastName = p_name.substring(46);

      if (lastName.equals("contents"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addContents(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("activityGraph"))
      {
        MActivityGraph el = (MActivityGraph)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processAssociationEndMain(String p_name, AttributeList p_attrs)
  {
    MAssociationEnd o = (MAssociationEnd)liStack.get(liStack.size()-1);

    if (processAssociationEndAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processAssociationEndRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessAssociationEndMain(String p_name)
  {
    MAssociationEnd o = (MAssociationEnd)liStack.get(liStack.size()-2);

    if (postprocessAssociationEndAttributes(p_name, o))
    {
      return;
    }

    if (postprocessAssociationEndRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processAssociationEndAttributes(String p_name, AttributeList p_attrs, MAssociationEnd o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.AssociationEnd."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("changeability"))
      {
        lastMethod = "changeability";
        lastMethodType = true;
        o.setChangeability(MChangeableKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("multiplicity"))
      {
        lastMethod = "multiplicity";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("targetScope"))
      {
        lastMethod = "targetScope";
        lastMethodType = true;
        o.setTargetScope(MScopeKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("aggregation"))
      {
        lastMethod = "aggregation";
        lastMethodType = true;
        o.setAggregation(MAggregationKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("ordering"))
      {
        lastMethod = "ordering";
        lastMethodType = true;
        o.setOrdering(MOrderingKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isNavigable"))
      {
        lastMethod = "isNavigable";
        lastMethodType = true;
        o.setNavigable(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessAssociationEndAttributes(String p_name, MAssociationEnd o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.AssociationEnd."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("changeability"))
      {
        return true;
      }

      if (lastName.equals("multiplicity"))
      {
        if (!(lastObject instanceof Link))
        {
          MMultiplicity oexp = null;
          if (lastObject instanceof MMultiplicity)
          {
            oexp = (MMultiplicity)lastObject;
            o.setMultiplicity(oexp);
          }
          else
          {
            oexp = ((MMultiplicityEditor)lastObject).toMultiplicity();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setMultiplicity(oexp);
        }
        return true;
      }

      if (lastName.equals("targetScope"))
      {
        return true;
      }

      if (lastName.equals("aggregation"))
      {
        return true;
      }

      if (lastName.equals("ordering"))
      {
        return true;
      }

      if (lastName.equals("isNavigable"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processAssociationEndRoles(String p_name, AttributeList p_attrs, MAssociationEnd o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.AssociationEnd."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("linkEnd"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("associationEndRole"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("specification"))
      {
        lastMethod = "specification";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("type"))
      {
        lastMethod = "type";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("qualifier"))
      {
        lastMethod = "qualifier";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("association"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessAssociationEndRoles(String p_name, MAssociationEnd o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.AssociationEnd."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("linkEnd"))
      {
        MLinkEnd el = (MLinkEnd)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("associationEndRole"))
      {
        MAssociationEndRole el = (MAssociationEndRole)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("specification"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.addSpecification(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("type"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.setType(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("qualifier"))
      {
        MAttribute el = (MAttribute)lastObject;
        if (null != el)
        {
          o.addQualifier(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("association"))
      {
        MAssociation el = (MAssociation)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processPermissionMain(String p_name, AttributeList p_attrs)
  {
    MPermission o = (MPermission)liStack.get(liStack.size()-1);

    if (processPermissionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processPermissionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessPermissionMain(String p_name)
  {
    MPermission o = (MPermission)liStack.get(liStack.size()-2);

    if (postprocessPermissionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessPermissionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processPermissionAttributes(String p_name, AttributeList p_attrs, MPermission o)
  {
    if (processDependencyAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessPermissionAttributes(String p_name, MPermission o)
  {
    if (postprocessDependencyAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processPermissionRoles(String p_name, AttributeList p_attrs, MPermission o)
  {
    if (processDependencyRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessPermissionRoles(String p_name, MPermission o)
  {
    if (postprocessDependencyRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processUseCaseMain(String p_name, AttributeList p_attrs)
  {
    MUseCase o = (MUseCase)liStack.get(liStack.size()-1);

    if (processUseCaseAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processUseCaseRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessUseCaseMain(String p_name)
  {
    MUseCase o = (MUseCase)liStack.get(liStack.size()-2);

    if (postprocessUseCaseAttributes(p_name, o))
    {
      return;
    }

    if (postprocessUseCaseRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processUseCaseAttributes(String p_name, AttributeList p_attrs, MUseCase o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessUseCaseAttributes(String p_name, MUseCase o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processUseCaseRoles(String p_name, AttributeList p_attrs, MUseCase o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.UseCase."))
    {
      String lastName = p_name.substring(38);

      if (lastName.equals("extensionPoint"))
      {
        lastMethod = "extensionPoint";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("include2"))
      {
        lastMethod = "include2";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("include"))
      {
        lastMethod = "include";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("extend"))
      {
        lastMethod = "extend";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("extend2"))
      {
        lastMethod = "extend2";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessUseCaseRoles(String p_name, MUseCase o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Use_Cases.UseCase."))
    {
      String lastName = p_name.substring(38);

      if (lastName.equals("extensionPoint"))
      {
        MExtensionPoint el = (MExtensionPoint)lastObject;
        if (null != el)
        {
          o.addExtensionPoint(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("include2"))
      {
        MInclude el = (MInclude)lastObject;
        if (null != el)
        {
          o.addInclude2(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("include"))
      {
        MInclude el = (MInclude)lastObject;
        if (null != el)
        {
          o.addInclude(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("extend"))
      {
        MExtend el = (MExtend)lastObject;
        if (null != el)
        {
          o.addExtend(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("extend2"))
      {
        MExtend el = (MExtend)lastObject;
        if (null != el)
        {
          o.addExtend2(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processActionSequenceMain(String p_name, AttributeList p_attrs)
  {
    MActionSequence o = (MActionSequence)liStack.get(liStack.size()-1);

    if (processActionSequenceAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processActionSequenceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessActionSequenceMain(String p_name)
  {
    MActionSequence o = (MActionSequence)liStack.get(liStack.size()-2);

    if (postprocessActionSequenceAttributes(p_name, o))
    {
      return;
    }

    if (postprocessActionSequenceRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processActionSequenceAttributes(String p_name, AttributeList p_attrs, MActionSequence o)
  {
    if (processActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessActionSequenceAttributes(String p_name, MActionSequence o)
  {
    if (postprocessActionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processActionSequenceRoles(String p_name, AttributeList p_attrs, MActionSequence o)
  {
    if (processActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.ActionSequence."))
    {
      String lastName = p_name.substring(51);

      if (lastName.equals("action"))
      {
        lastMethod = "action";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessActionSequenceRoles(String p_name, MActionSequence o)
  {
    if (postprocessActionRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.ActionSequence."))
    {
      String lastName = p_name.substring(51);

      if (lastName.equals("action"))
      {
        MAction el = (MAction)lastObject;
        if (null != el)
        {
          o.addAction(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processObjectSetExpressionMain(String p_name, AttributeList p_attrs)
  {
    MObjectSetExpressionEditor o = (MObjectSetExpressionEditor)liStack.get(liStack.size()-1);

    if (processObjectSetExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processObjectSetExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessObjectSetExpressionMain(String p_name)
  {
    MObjectSetExpressionEditor o = (MObjectSetExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessObjectSetExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessObjectSetExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processObjectSetExpressionAttributes(String p_name, AttributeList p_attrs, MObjectSetExpressionEditor o)
  {
    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessObjectSetExpressionAttributes(String p_name, MObjectSetExpressionEditor o)
  {
    if (postprocessExpressionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processObjectSetExpressionRoles(String p_name, AttributeList p_attrs, MObjectSetExpressionEditor o)
  {
    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessObjectSetExpressionRoles(String p_name, MObjectSetExpressionEditor o)
  {
    if (postprocessExpressionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processNamespaceMain(String p_name, AttributeList p_attrs)
  {
    MNamespace o = (MNamespace)liStack.get(liStack.size()-1);

    if (processNamespaceAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processNamespaceRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessNamespaceMain(String p_name)
  {
    MNamespace o = (MNamespace)liStack.get(liStack.size()-2);

    if (postprocessNamespaceAttributes(p_name, o))
    {
      return;
    }

    if (postprocessNamespaceRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processNamespaceAttributes(String p_name, AttributeList p_attrs, MNamespace o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessNamespaceAttributes(String p_name, MNamespace o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processNamespaceRoles(String p_name, AttributeList p_attrs, MNamespace o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Namespace."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("ownedElement"))
      {
        lastMethod = "ownedElement";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessNamespaceRoles(String p_name, MNamespace o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Namespace."))
    {
      String lastName = p_name.substring(26);

      if (lastName.equals("ownedElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addOwnedElement(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processMethodMain(String p_name, AttributeList p_attrs)
  {
    MMethod o = (MMethod)liStack.get(liStack.size()-1);

    if (processMethodAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processMethodRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessMethodMain(String p_name)
  {
    MMethod o = (MMethod)liStack.get(liStack.size()-2);

    if (postprocessMethodAttributes(p_name, o))
    {
      return;
    }

    if (postprocessMethodRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processMethodAttributes(String p_name, AttributeList p_attrs, MMethod o)
  {
    if (processBehavioralFeatureAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Method."))
    {
      String lastName = p_name.substring(23);

      if (lastName.equals("body"))
      {
        lastMethod = "body";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessMethodAttributes(String p_name, MMethod o)
  {
    if (postprocessBehavioralFeatureAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Method."))
    {
      String lastName = p_name.substring(23);

      if (lastName.equals("body"))
      {
        if (!(lastObject instanceof Link))
        {
          MProcedureExpression oexp = null;
          if (lastObject instanceof MProcedureExpression)
          {
            oexp = (MProcedureExpression)lastObject;
            o.setBody(oexp);
          }
          else
          {
            oexp = ((MProcedureExpressionEditor)lastObject).toProcedureExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setBody(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processMethodRoles(String p_name, AttributeList p_attrs, MMethod o)
  {
    if (processBehavioralFeatureRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Method."))
    {
      String lastName = p_name.substring(23);

      if (lastName.equals("specification"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessMethodRoles(String p_name, MMethod o)
  {
    if (postprocessBehavioralFeatureRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Method."))
    {
      String lastName = p_name.substring(23);

      if (lastName.equals("specification"))
      {
        MOperation el = (MOperation)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processDestroyActionMain(String p_name, AttributeList p_attrs)
  {
    MDestroyAction o = (MDestroyAction)liStack.get(liStack.size()-1);

    if (processDestroyActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processDestroyActionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessDestroyActionMain(String p_name)
  {
    MDestroyAction o = (MDestroyAction)liStack.get(liStack.size()-2);

    if (postprocessDestroyActionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessDestroyActionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processDestroyActionAttributes(String p_name, AttributeList p_attrs, MDestroyAction o)
  {
    if (processActionAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessDestroyActionAttributes(String p_name, MDestroyAction o)
  {
    if (postprocessActionAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processDestroyActionRoles(String p_name, AttributeList p_attrs, MDestroyAction o)
  {
    if (processActionRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessDestroyActionRoles(String p_name, MDestroyAction o)
  {
    if (postprocessActionRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processStimulusMain(String p_name, AttributeList p_attrs)
  {
    MStimulus o = (MStimulus)liStack.get(liStack.size()-1);

    if (processStimulusAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processStimulusRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessStimulusMain(String p_name)
  {
    MStimulus o = (MStimulus)liStack.get(liStack.size()-2);

    if (postprocessStimulusAttributes(p_name, o))
    {
      return;
    }

    if (postprocessStimulusRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processStimulusAttributes(String p_name, AttributeList p_attrs, MStimulus o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessStimulusAttributes(String p_name, MStimulus o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processStimulusRoles(String p_name, AttributeList p_attrs, MStimulus o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Stimulus."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("dispatchAction"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("communicationLink"))
      {
        lastMethod = "communicationLink";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("receiver"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("sender"))
      {
        lastMethod = "sender";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("argument"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessStimulusRoles(String p_name, MStimulus o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Common_Behavior.Stimulus."))
    {
      String lastName = p_name.substring(45);

      if (lastName.equals("dispatchAction"))
      {
        MAction el = (MAction)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("communicationLink"))
      {
        MLink el = (MLink)lastObject;
        if (null != el)
        {
          o.setCommunicationLink(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("receiver"))
      {
        MInstance el = (MInstance)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("sender"))
      {
        MInstance el = (MInstance)lastObject;
        if (null != el)
        {
          o.setSender(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("argument"))
      {
        MInstance el = (MInstance)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processConstraintMain(String p_name, AttributeList p_attrs)
  {
    MConstraint o = (MConstraint)liStack.get(liStack.size()-1);

    if (processConstraintAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processConstraintRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessConstraintMain(String p_name)
  {
    MConstraint o = (MConstraint)liStack.get(liStack.size()-2);

    if (postprocessConstraintAttributes(p_name, o))
    {
      return;
    }

    if (postprocessConstraintRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processConstraintAttributes(String p_name, AttributeList p_attrs, MConstraint o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Constraint."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("body"))
      {
        lastMethod = "body";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessConstraintAttributes(String p_name, MConstraint o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Constraint."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("body"))
      {
        if (!(lastObject instanceof Link))
        {
          MBooleanExpression oexp = null;
          if (lastObject instanceof MBooleanExpression)
          {
            oexp = (MBooleanExpression)lastObject;
            o.setBody(oexp);
          }
          else
          {
            oexp = ((MBooleanExpressionEditor)lastObject).toBooleanExpression();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setBody(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processConstraintRoles(String p_name, AttributeList p_attrs, MConstraint o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Constraint."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("constrainedElement2"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("constrainedElement"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessConstraintRoles(String p_name, MConstraint o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.Constraint."))
    {
      String lastName = p_name.substring(27);

      if (lastName.equals("constrainedElement2"))
      {
        MStereotype el = (MStereotype)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("constrainedElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processAssociationMain(String p_name, AttributeList p_attrs)
  {
    MAssociation o = (MAssociation)liStack.get(liStack.size()-1);

    if (processAssociationAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processAssociationRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessAssociationMain(String p_name)
  {
    MAssociation o = (MAssociation)liStack.get(liStack.size()-2);

    if (postprocessAssociationAttributes(p_name, o))
    {
      return;
    }

    if (postprocessAssociationRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processAssociationAttributes(String p_name, AttributeList p_attrs, MAssociation o)
  {
    if (processRelationshipAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("isAbstract"))
      {
        lastMethod = "isAbstract";
        lastMethodType = true;
        o.setAbstract(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        lastMethod = "isLeaf";
        lastMethodType = true;
        o.setLeaf(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        lastMethod = "isRoot";
        lastMethodType = true;
        o.setRoot(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessAssociationAttributes(String p_name, MAssociation o)
  {
    if (postprocessRelationshipAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("isAbstract"))
      {
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processAssociationRoles(String p_name, AttributeList p_attrs, MAssociation o)
  {
    if (processRelationshipRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("specialization"))
      {
        lastMethod = "specialization";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("generalization"))
      {
        lastMethod = "generalization";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }
    if (p_name.startsWith("Foundation.Core.Association."))
    {
      String lastName = p_name.substring(28);

      if (lastName.equals("link"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("associationRole"))
      {
        lastMethod = null;
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("connection"))
      {
        lastMethod = "connection";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessAssociationRoles(String p_name, MAssociation o)
  {
    if (postprocessRelationshipRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("specialization"))
      {
        MGeneralization el = (MGeneralization)lastObject;
        if (null != el)
        {
          o.addSpecialization(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("generalization"))
      {
        MGeneralization el = (MGeneralization)lastObject;
        if (null != el)
        {
          o.addGeneralization(el);
          return true;
        }
        return false;
      }
    }
    if (p_name.startsWith("Foundation.Core.Association."))
    {
      String lastName = p_name.substring(28);

      if (lastName.equals("link"))
      {
        MLink el = (MLink)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("associationRole"))
      {
        MAssociationRole el = (MAssociationRole)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("connection"))
      {
        MAssociationEnd el = (MAssociationEnd)lastObject;
        if (null != el)
        {
          o.addConnection(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processGeneralizableElementAttributes(String p_name, AttributeList p_attrs, MGeneralizableElement o)
  {
    if (processModelElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("isAbstract"))
      {
        lastMethod = "isAbstract";
        lastMethodType = true;
        o.setAbstract(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        lastMethod = "isLeaf";
        lastMethodType = true;
        o.setLeaf(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        lastMethod = "isRoot";
        lastMethodType = true;
        o.setRoot(convertXMIBooleanValue(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessGeneralizableElementAttributes(String p_name, MGeneralizableElement o)
  {
    if (postprocessModelElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("isAbstract"))
      {
        return true;
      }

      if (lastName.equals("isLeaf"))
      {
        return true;
      }

      if (lastName.equals("isRoot"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processGeneralizableElementRoles(String p_name, AttributeList p_attrs, MGeneralizableElement o)
  {
    if (processModelElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("specialization"))
      {
        lastMethod = "specialization";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("generalization"))
      {
        lastMethod = "generalization";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessGeneralizableElementRoles(String p_name, MGeneralizableElement o)
  {
    if (postprocessModelElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.GeneralizableElement."))
    {
      String lastName = p_name.substring(37);

      if (lastName.equals("specialization"))
      {
        MGeneralization el = (MGeneralization)lastObject;
        if (null != el)
        {
          o.addSpecialization(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("generalization"))
      {
        MGeneralization el = (MGeneralization)lastObject;
        if (null != el)
        {
          o.addGeneralization(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processExceptionMain(String p_name, AttributeList p_attrs)
  {
    MException o = (MException)liStack.get(liStack.size()-1);

    if (processExceptionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processExceptionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessExceptionMain(String p_name)
  {
    MException o = (MException)liStack.get(liStack.size()-2);

    if (postprocessExceptionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessExceptionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processExceptionAttributes(String p_name, AttributeList p_attrs, MException o)
  {
    if (processSignalAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessExceptionAttributes(String p_name, MException o)
  {
    if (postprocessSignalAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processExceptionRoles(String p_name, AttributeList p_attrs, MException o)
  {
    if (processSignalRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessExceptionRoles(String p_name, MException o)
  {
    if (postprocessSignalRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processPseudostateMain(String p_name, AttributeList p_attrs)
  {
    MPseudostate o = (MPseudostate)liStack.get(liStack.size()-1);

    if (processPseudostateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processPseudostateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessPseudostateMain(String p_name)
  {
    MPseudostate o = (MPseudostate)liStack.get(liStack.size()-2);

    if (postprocessPseudostateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessPseudostateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processPseudostateAttributes(String p_name, AttributeList p_attrs, MPseudostate o)
  {
    if (processStateVertexAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Pseudostate."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("kind"))
      {
        lastMethod = "kind";
        lastMethodType = true;
        o.setKind(MPseudostateKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessPseudostateAttributes(String p_name, MPseudostate o)
  {
    if (postprocessStateVertexAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.Pseudostate."))
    {
      String lastName = p_name.substring(47);

      if (lastName.equals("kind"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processPseudostateRoles(String p_name, AttributeList p_attrs, MPseudostate o)
  {
    if (processStateVertexRoles(p_name, p_attrs, o))
    {
      return true;
    }

    return false;
  }

  public boolean postprocessPseudostateRoles(String p_name, MPseudostate o)
  {
    if (postprocessStateVertexRoles(p_name, o))
    {
      return true;
    }

    return false;
  }

  public boolean processElementImportMain(String p_name, AttributeList p_attrs)
  {
    MElementImport o = (MElementImport)liStack.get(liStack.size()-1);

    if (processElementImportAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processElementImportRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessElementImportMain(String p_name)
  {
    MElementImport o = (MElementImport)liStack.get(liStack.size()-2);

    if (postprocessElementImportAttributes(p_name, o))
    {
      return;
    }

    if (postprocessElementImportRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processElementImportAttributes(String p_name, AttributeList p_attrs, MElementImport o)
  {
    if (p_name.startsWith("Model_Management.ElementImport."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("alias"))
      {
        lastMethod = "alias";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("visibility"))
      {
        lastMethod = "visibility";
        lastMethodType = true;
        o.setVisibility(MVisibilityKind.forName(p_attrs.getValue("xmi.value")));
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessElementImportAttributes(String p_name, MElementImport o)
  {
    if (p_name.startsWith("Model_Management.ElementImport."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("alias"))
      {
        o.setAlias(lastString.toString());
        return true;
      }

      if (lastName.equals("visibility"))
      {
        return true;
      }

    }
    return false;
  }

  public boolean processElementImportRoles(String p_name, AttributeList p_attrs, MElementImport o)
  {
    if (p_name.startsWith("Model_Management.ElementImport."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("modelElement"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("package"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessElementImportRoles(String p_name, MElementImport o)
  {
    if (p_name.startsWith("Model_Management.ElementImport."))
    {
      String lastName = p_name.substring(31);

      if (lastName.equals("modelElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("package"))
      {
        MPackage el = (MPackage)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processSubmachineStateMain(String p_name, AttributeList p_attrs)
  {
    MSubmachineState o = (MSubmachineState)liStack.get(liStack.size()-1);

    if (processSubmachineStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processSubmachineStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessSubmachineStateMain(String p_name)
  {
    MSubmachineState o = (MSubmachineState)liStack.get(liStack.size()-2);

    if (postprocessSubmachineStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessSubmachineStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processSubmachineStateAttributes(String p_name, AttributeList p_attrs, MSubmachineState o)
  {
    if (processCompositeStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessSubmachineStateAttributes(String p_name, MSubmachineState o)
  {
    if (postprocessCompositeStateAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processSubmachineStateRoles(String p_name, AttributeList p_attrs, MSubmachineState o)
  {
    if (processCompositeStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.SubmachineState."))
    {
      String lastName = p_name.substring(51);

      if (lastName.equals("submachine"))
      {
        lastMethod = "submachine";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessSubmachineStateRoles(String p_name, MSubmachineState o)
  {
    if (postprocessCompositeStateRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.State_Machines.SubmachineState."))
    {
      String lastName = p_name.substring(51);

      if (lastName.equals("submachine"))
      {
        MStateMachine el = (MStateMachine)lastObject;
        if (null != el)
        {
          o.setSubmachine(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processExpressionMain(String p_name, AttributeList p_attrs)
  {
    MExpressionEditor o = (MExpressionEditor)liStack.get(liStack.size()-1);

    if (processExpressionAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processExpressionRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessExpressionMain(String p_name)
  {
    MExpressionEditor o = (MExpressionEditor)liStack.get(liStack.size()-2);

    if (postprocessExpressionAttributes(p_name, o))
    {
      return;
    }

    if (postprocessExpressionRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processExpressionAttributes(String p_name, AttributeList p_attrs, MExpressionEditor o)
  {
    if (p_name.startsWith("Foundation.Data_Types.Expression."))
    {
      String lastName = p_name.substring(33);

      if (lastName.equals("body"))
      {
        lastMethod = "body";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("language"))
      {
        lastMethod = "language";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessExpressionAttributes(String p_name, MExpressionEditor o)
  {
    if (p_name.startsWith("Foundation.Data_Types.Expression."))
    {
      String lastName = p_name.substring(33);

      if (lastName.equals("body"))
      {
        o.setBody(lastString.toString());
        return true;
      }

      if (lastName.equals("language"))
      {
        o.setLanguage(lastString.toString());
        return true;
      }

    }
    return false;
  }

  public boolean processExpressionRoles(String p_name, AttributeList p_attrs, MExpressionEditor o)
  {

    return false;
  }

  public boolean postprocessExpressionRoles(String p_name, MExpressionEditor o)
  {

    return false;
  }

  public boolean processPresentationElementAttributes(String p_name, AttributeList p_attrs, MPresentationElement o)
  {
    if (processElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessPresentationElementAttributes(String p_name, MPresentationElement o)
  {
    if (postprocessElementAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processPresentationElementRoles(String p_name, AttributeList p_attrs, MPresentationElement o)
  {
    if (processElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.PresentationElement."))
    {
      String lastName = p_name.substring(36);

      if (lastName.equals("subject"))
      {
        lastMethod = "subject";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessPresentationElementRoles(String p_name, MPresentationElement o)
  {
    if (postprocessElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Core.PresentationElement."))
    {
      String lastName = p_name.substring(36);

      if (lastName.equals("subject"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addSubject(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processClassifierInStateMain(String p_name, AttributeList p_attrs)
  {
    MClassifierInState o = (MClassifierInState)liStack.get(liStack.size()-1);

    if (processClassifierInStateAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processClassifierInStateRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessClassifierInStateMain(String p_name)
  {
    MClassifierInState o = (MClassifierInState)liStack.get(liStack.size()-2);

    if (postprocessClassifierInStateAttributes(p_name, o))
    {
      return;
    }

    if (postprocessClassifierInStateRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processClassifierInStateAttributes(String p_name, AttributeList p_attrs, MClassifierInState o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    return false;
  }

  public boolean postprocessClassifierInStateAttributes(String p_name, MClassifierInState o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    return false;
  }

  public boolean processClassifierInStateRoles(String p_name, AttributeList p_attrs, MClassifierInState o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ClassifierInState."))
    {
      String lastName = p_name.substring(54);

      if (lastName.equals("type"))
      {
        lastMethod = "type";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("inState"))
      {
        lastMethod = "inState";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessClassifierInStateRoles(String p_name, MClassifierInState o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Activity_Graphs.ClassifierInState."))
    {
      String lastName = p_name.substring(54);

      if (lastName.equals("type"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.setType(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("inState"))
      {
        MState el = (MState)lastObject;
        if (null != el)
        {
          o.addInState(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processElementAttributes(String p_name, AttributeList p_attrs, MElement o)
  {
    return false;
  }

  public boolean postprocessElementAttributes(String p_name, MElement o)
  {
    return false;
  }

  public boolean processElementRoles(String p_name, AttributeList p_attrs, MElement o)
  {

    return false;
  }

  public boolean postprocessElementRoles(String p_name, MElement o)
  {

    return false;
  }

  public boolean processClassifierRoleMain(String p_name, AttributeList p_attrs)
  {
    MClassifierRole o = (MClassifierRole)liStack.get(liStack.size()-1);

    if (processClassifierRoleAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processClassifierRoleRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessClassifierRoleMain(String p_name)
  {
    MClassifierRole o = (MClassifierRole)liStack.get(liStack.size()-2);

    if (postprocessClassifierRoleAttributes(p_name, o))
    {
      return;
    }

    if (postprocessClassifierRoleRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processClassifierRoleAttributes(String p_name, AttributeList p_attrs, MClassifierRole o)
  {
    if (processClassifierAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.ClassifierRole."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("multiplicity"))
      {
        lastMethod = "multiplicity";
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessClassifierRoleAttributes(String p_name, MClassifierRole o)
  {
    if (postprocessClassifierAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.ClassifierRole."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("multiplicity"))
      {
        if (!(lastObject instanceof Link))
        {
          MMultiplicity oexp = null;
          if (lastObject instanceof MMultiplicity)
          {
            oexp = (MMultiplicity)lastObject;
            o.setMultiplicity(oexp);
          }
          else
          {
            oexp = ((MMultiplicityEditor)lastObject).toMultiplicity();

            String xmiid = getXMIIDByElement(lastObject);
            if (null != xmiid)
            {
              removeXMIID(lastObject);
              putXMIID(xmiid, oexp);
            }

            String xmiuuid = getXMIUUIDByElement(lastObject);
            if (null != xmiuuid)
            {
              removeXMIUUID(lastObject);
              putXMIUUID(xmiuuid, oexp);
            }
          }

          o.setMultiplicity(oexp);
        }
        return true;
      }

    }
    return false;
  }

  public boolean processClassifierRoleRoles(String p_name, AttributeList p_attrs, MClassifierRole o)
  {
    if (processClassifierRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.ClassifierRole."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("availableContents"))
      {
        lastMethod = "availableContents";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("message1"))
      {
        lastMethod = "message1";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("message2"))
      {
        lastMethod = "message2";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("availableFeature"))
      {
        lastMethod = "availableFeature";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("base"))
      {
        lastMethod = "base";
        lastMethodType = false;
        liStack.add(STATE_MULTIPLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessClassifierRoleRoles(String p_name, MClassifierRole o)
  {
    if (postprocessClassifierRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Behavioral_Elements.Collaborations.ClassifierRole."))
    {
      String lastName = p_name.substring(50);

      if (lastName.equals("availableContents"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          o.addAvailableContents(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("message1"))
      {
        MMessage el = (MMessage)lastObject;
        if (null != el)
        {
          o.addMessage1(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("message2"))
      {
        MMessage el = (MMessage)lastObject;
        if (null != el)
        {
          o.addMessage2(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("availableFeature"))
      {
        MFeature el = (MFeature)lastObject;
        if (null != el)
        {
          o.addAvailableFeature(el);
          return true;
        }
        return false;
      }
      if (lastName.equals("base"))
      {
        MClassifier el = (MClassifier)lastObject;
        if (null != el)
        {
          o.addBase(el);
          return true;
        }
        return false;
      }
    }

    return false;
  }

  public boolean processTaggedValueMain(String p_name, AttributeList p_attrs)
  {
    MTaggedValue o = (MTaggedValue)liStack.get(liStack.size()-1);

    if (processTaggedValueAttributes(p_name, p_attrs, o))
    {
      return true;
    }

    if (processTaggedValueRoles(p_name, p_attrs, o))
    {
      return true;
    }
    return processXMIExtensionMain(p_name, p_attrs);
  }

  public void postprocessTaggedValueMain(String p_name)
  {
    MTaggedValue o = (MTaggedValue)liStack.get(liStack.size()-2);

    if (postprocessTaggedValueAttributes(p_name, o))
    {
      return;
    }

    if (postprocessTaggedValueRoles(p_name, o))
    {
      return;
    }
    postprocessXMIExtensionMain(p_name, o);
  }

  public boolean processTaggedValueAttributes(String p_name, AttributeList p_attrs, MTaggedValue o)
  {
    if (processElementAttributes(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Extension_Mechanisms.TaggedValue."))
    {
      String lastName = p_name.substring(44);

      if (lastName.equals("value"))
      {
        lastMethod = "value";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

      if (lastName.equals("tag"))
      {
        lastMethod = "tag";
        lastMethodType = true;
        liStack.add(o);
        liNameStack.add(p_name);
        return true;
      }

    }
    return false;
  }

  public boolean postprocessTaggedValueAttributes(String p_name, MTaggedValue o)
  {
    if (postprocessElementAttributes(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Extension_Mechanisms.TaggedValue."))
    {
      String lastName = p_name.substring(44);

      if (lastName.equals("value"))
      {
        o.setValue(lastString.toString());
        return true;
      }

      if (lastName.equals("tag"))
      {
        o.setTag(lastString.toString());
        return true;
      }

    }
    return false;
  }

  public boolean processTaggedValueRoles(String p_name, AttributeList p_attrs, MTaggedValue o)
  {
    if (processElementRoles(p_name, p_attrs, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Extension_Mechanisms.TaggedValue."))
    {
      String lastName = p_name.substring(44);

      if (lastName.equals("stereotype"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
      if (lastName.equals("modelElement"))
      {
        lastMethod = null;
        lastMethodType = true;
        liStack.add(STATE_SINGLE);
        liNameStack.add(p_name);
        return true;
      }
    }

    return false;
  }

  public boolean postprocessTaggedValueRoles(String p_name, MTaggedValue o)
  {
    if (postprocessElementRoles(p_name, o))
    {
      return true;
    }
    if (p_name.startsWith("Foundation.Extension_Mechanisms.TaggedValue."))
    {
      String lastName = p_name.substring(44);

      if (lastName.equals("stereotype"))
      {
        MStereotype el = (MStereotype)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
      if (lastName.equals("modelElement"))
      {
        MModelElement el = (MModelElement)lastObject;
        if (null != el)
        {
          return true;
        }
        return false;
      }
    }

    return false;
  }

}
