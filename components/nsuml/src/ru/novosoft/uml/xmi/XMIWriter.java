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
import org.xml.sax.helpers.*;


public class XMIWriter extends PrintWriter
{
  public static final String EXPORTER_VERSION = "0.4.19";
  public static final String EXPORTER = "Novosoft UML Library";
  public static final String CDATA_TYPE = "CDATA";

  /**
   * a constructor
   * @param p_mmodel a MModel
   * @param p_filename a filename
   * @param p_encoding a encoding
   */
  public XMIWriter(MModel p_mmodel, String p_filename, String p_encoding) throws IOException
  {
    //this(p_mmodel, new OutputStreamWriter(new FileOutputStream(new File(p_filename)), p_encoding));
    this(p_mmodel, new OutputStreamWriter(new FileOutputStream(new File(p_filename)), "UTF-8"));
  }

  /**
   * a constructor
   * @param p_mmodel a MModel
   * @param p_filename a String
   */
  public XMIWriter(MModel p_mmodel, String p_filename) throws IOException
  {
    this(p_mmodel, p_filename, "UTF-8");
  }

  /**
   * a constructor
   * @param p_out a java.io.Writer
   */
  public XMIWriter(MModel p_mmodel, java.io.Writer p_out) throws IOException
  {
    super(p_out);
    mmodel = p_mmodel;
  }

  protected MModel mmodel = null;
  public MModel getModel()
  {
    return mmodel;
  }

  public void gen() throws IncompleteXMIException
  {
    DocumentHandler tmp = null;
    try
    {
      Class cls = Class.forName(
        System.getProperty(
          "ru.novosoft.uml.xmi.saxwriter", 
          "ru.novosoft.uml.xmi.XMIEventWriter"
        )
      );

      tmp = (DocumentHandler)(cls.getConstructor(
        new Class[] {Writer.class}
      ).newInstance(new Object[] {this.out}));
    }
    catch(Exception ex)
    {
      throw new RuntimeException(ex.getMessage());
    }

    gen(tmp);
  }

  protected AttributeListImpl al = new AttributeListImpl();
  protected org.xml.sax.DocumentHandler dh = null;
  public void gen(org.xml.sax.DocumentHandler p_dh) throws IncompleteXMIException
  {
    try
    {
      preprocessIDs(getModel());

      dh = p_dh;
  
      dh.startDocument();
  
      al.clear();
  
      al.addAttribute("xmi.version", CDATA_TYPE, "1.0");
      dh.startElement("XMI", al); al.clear();
  
      dh.startElement("XMI.header", al);

      dh.startElement("XMI.documentation", al);

      dh.startElement("XMI.exporter", al);
      dh.characters(EXPORTER.toCharArray(), 0, EXPORTER.length());
      dh.endElement("XMI.exporter");

      dh.startElement("XMI.exporterVersion", al);
      dh.characters(EXPORTER_VERSION.toCharArray(), 0, EXPORTER_VERSION.length());
      dh.endElement("XMI.exporterVersion");

      dh.endElement("XMI.documentation");
  
      al.addAttribute("xmi.name", CDATA_TYPE, "UML");
      al.addAttribute("xmi.version", CDATA_TYPE, "1.3");
      dh.startElement("XMI.metamodel", al); al.clear();
  
      dh.endElement("XMI.metamodel");
  
      dh.endElement("XMI.header");
  
      dh.startElement("XMI.content", al);
  
      printModelMain(getModel());
  
      dh.endElement("XMI.content");
      dh.endElement("XMI");
  
      dh.endDocument();
  
      if (!notContained.isEmpty())
      {
        throw new IncompleteXMIException();
      }
    }
    catch(SAXException ex)
    {
      throw new RuntimeException(ex.getMessage());
    }
  }

  //===========================================================================

  protected HashMap notContained = new HashMap();
  public Collection getNotContainedElements()
  {
    return Collections.unmodifiableCollection(notContained.values());
  }

  protected long xmiid = 1;
  protected HashMap xmiid2Element = new HashMap();
  protected HashMap element2xmiid = new HashMap();
  protected String getXMIID(Object el)
  {
    String s = (String)(element2xmiid.get(el));
    if (null != s)
    {
      return s;
    }

    do
    {
      s = "xmi."+(xmiid++);
    } 
    while (null != xmiid2Element.get(s));

    element2xmiid.put(el, s);
    xmiid2Element.put(s, el);
    return s;
  }


  public Map getObjectMap()
  { 
    return Collections.unmodifiableMap(xmiid2Element);
  }

  public Map getXMIIDMap()
  { 
    return Collections.unmodifiableMap(element2xmiid);
  }


  protected void addXMIUUID(Object el, AttributeListImpl attrlist)
  {
    if ((el instanceof MBase) && (null != ((MBase)el).getUUID()))
    {
      attrlist.addAttribute("xmi.uuid", CDATA_TYPE, ((MBase)el).getUUID());
    }
  }

  protected HashMap processedElements = new HashMap();
  protected void processElement(Object arg)
  {
    processedElements.put(arg, arg);
  }

  protected boolean isElementProcessed(Object arg)
  {
    return (null != processedElements.get(arg));
  }

  public void printXMIExtension(MBase arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    {
      Iterator i = arg.getExtensions().iterator();
      while(i.hasNext())
      {
        MExtension el = (MExtension)i.next();

        Object val = el.getValue();
        if (val instanceof String)
        {
          if (null != el.getExtender())
          {
            al.addAttribute("xmi.extender", CDATA_TYPE, el.getExtender());
          }
  
          if (null != el.getExtenderID())
          {
            al.addAttribute("xmi.extenderID", CDATA_TYPE, el.getExtenderID());
          }
  
          dh.startElement("XMI.extension", al); al.clear();
  
          Object value = el.getValue();
          if (value instanceof String)
          {
            String s = (String)value;
            dh.characters(s.toCharArray(), 0, s.length());
          }
          dh.endElement("XMI.extension");
        }
        else if (val instanceof Element)
        {
          printJDOMElement((Element)val);
        }
      }
    }
  }

  public void printJDOMElement(Element arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    al.clear();

    Iterator i = arg.getAttributes().iterator();
    while (i.hasNext())
    {
      Attribute attr = (Attribute)i.next();
      al.addAttribute(attr.getName(), CDATA_TYPE, attr.getValue());
    }

    dh.startElement(arg.getName(), al); al.clear();

    i = arg.getMixedContent().iterator();
    while (i.hasNext())
    {
      Object obj = i.next();

      if (obj instanceof String)
      {
        String s = (String)obj;
        dh.characters(s.toCharArray(), 0, s.length());
      }
      else if (obj instanceof Element)
      {
        printJDOMElement((Element)obj);
      }
    }

    dh.endElement(arg.getName());
  }

  protected String convertBooleanXMI(boolean b)
  {
    if (b)
    {
      return "true";
    }

    return "false";
  }

  public void characters(String s) throws SAXException
  {
    dh.characters(s.toCharArray(), 0, s.length());
  }


  public void preprocessJDOMElementID(Element arg)
  {
    if (null == arg)
    {
      return;
    }

    Attribute attr = arg.getAttribute("xmi.id");
    if (null != attr)
    {
      element2xmiid.put(arg, attr.getValue());
      xmiid2Element.put(attr.getValue(), arg);
    }

    Iterator i = arg.getMixedContent().iterator();
    while (i.hasNext())
    {
      Object obj = i.next();

      if (obj instanceof Element)
      {
        preprocessJDOMElementID((Element)obj);
      }
    }
  }

  //===========================================================================

  public void printCommentMain(MComment arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Comment", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getAnnotatedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Comment.annotatedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Comment.annotatedElement");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Comment");
  }

  public void printTerminateActionMain(MTerminateAction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.TerminateAction", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getRecurrence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.recurrence", al);
      print(arg.getRecurrence(), false, "Foundation.Data_Types.IterationExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.recurrence");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.target", al);
      print(arg.getTarget(), false, "Foundation.Data_Types.ObjectSetExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.target");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAsynchronous()));    dh.startElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous");
    if (null != arg.getScript())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.script", al);
      print(arg.getScript(), false, "Foundation.Data_Types.ActionExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.script");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getActionSequence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.actionSequence", al);
      print(arg.getActionSequence(), true, "Behavioral_Elements.Common_Behavior.ActionSequence");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.actionSequence");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.stimulus");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getActualArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.actualArgument", al);
        while(i.hasNext())
        {
          MArgument el = (MArgument)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Argument");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.actualArgument");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.TerminateAction");
  }

  public void printClassMain(MClass arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Class", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isActive()));    dh.startElement("Foundation.Core.Class.isActive", al); al.clear();
    dh.endElement("Foundation.Core.Class.isActive");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Foundation.Core.Class");
  }

  public void printRelationshipMain(MRelationship arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Relationship", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Relationship");
  }

  public void printActorMain(MActor arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Use_Cases.Actor", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Behavioral_Elements.Use_Cases.Actor");
  }

  public void printExtensionPointMain(MExtensionPoint arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Use_Cases.ExtensionPoint", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getLocation())
    {
      dh.startElement("Behavioral_Elements.Use_Cases.ExtensionPoint.location", al);
      characters(arg.getLocation());
      dh.endElement("Behavioral_Elements.Use_Cases.ExtensionPoint.location");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getUseCase())
    {
      dh.startElement("Behavioral_Elements.Use_Cases.ExtensionPoint.useCase", al);
      print(arg.getUseCase(), true, "Behavioral_Elements.Use_Cases.UseCase");
      dh.endElement("Behavioral_Elements.Use_Cases.ExtensionPoint.useCase");
    }
    {
      Iterator i = arg.getExtends().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Use_Cases.ExtensionPoint.extend", al);
        while(i.hasNext())
        {
          MExtend el = (MExtend)i.next();
          print(el, true, "Behavioral_Elements.Use_Cases.Extend");
        }
        dh.endElement("Behavioral_Elements.Use_Cases.ExtensionPoint.extend");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.Use_Cases.ExtensionPoint");
  }

  public void printTimeEventMain(MTimeEvent arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.TimeEvent", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getWhen())
    {
      dh.startElement("Behavioral_Elements.State_Machines.TimeEvent.when", al);
      print(arg.getWhen(), false, "Foundation.Data_Types.TimeExpression");
      dh.endElement("Behavioral_Elements.State_Machines.TimeEvent.when");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getStates().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.state", al);
        while(i.hasNext())
        {
          MState el = (MState)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.State");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.state");
      }
    }
    {
      Iterator i = arg.getTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.transition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.transition");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, false, "Foundation.Core.Parameter");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.parameter");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.TimeEvent");
  }

  public void printBehavioralFeatureMain(MBehavioralFeature arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.BehavioralFeature", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getOwnerScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getOwnerScope().getName());      dh.startElement("Foundation.Core.Feature.ownerScope", al); al.clear();
      dh.endElement("Foundation.Core.Feature.ownerScope");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isQuery()));    dh.startElement("Foundation.Core.BehavioralFeature.isQuery", al); al.clear();
    dh.endElement("Foundation.Core.BehavioralFeature.isQuery");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getOwner())
    {
      dh.startElement("Foundation.Core.Feature.owner", al);
      print(arg.getOwner(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.Feature.owner");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.BehavioralFeature.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, false, "Foundation.Core.Parameter");
        }
        dh.endElement("Foundation.Core.BehavioralFeature.parameter");
      }
    }
    dh.endElement("Foundation.Core.BehavioralFeature");
  }

  public void printStateMachineMain(MStateMachine arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.StateMachine", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContext())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateMachine.context", al);
      print(arg.getContext(), true, "Foundation.Core.ModelElement");
      dh.endElement("Behavioral_Elements.State_Machines.StateMachine.context");
    }
    {
      Iterator i = arg.getSubmachineStates().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateMachine.subMachineState", al);
        while(i.hasNext())
        {
          MSubmachineState el = (MSubmachineState)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.SubmachineState");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateMachine.subMachineState");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getTop())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateMachine.top", al);
      print(arg.getTop(), false, "Behavioral_Elements.State_Machines.State");
      dh.endElement("Behavioral_Elements.State_Machines.StateMachine.top");
    }
    {
      Iterator i = arg.getTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateMachine.transitions", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateMachine.transitions");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.StateMachine");
  }

  public void printEventMain(MEvent arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.Event", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getStates().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.state", al);
        while(i.hasNext())
        {
          MState el = (MState)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.State");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.state");
      }
    }
    {
      Iterator i = arg.getTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.transition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.transition");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, false, "Foundation.Core.Parameter");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.parameter");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.Event");
  }

  public void printUninterpretedActionMain(MUninterpretedAction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.UninterpretedAction", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getRecurrence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.recurrence", al);
      print(arg.getRecurrence(), false, "Foundation.Data_Types.IterationExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.recurrence");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.target", al);
      print(arg.getTarget(), false, "Foundation.Data_Types.ObjectSetExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.target");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAsynchronous()));    dh.startElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous");
    if (null != arg.getScript())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.script", al);
      print(arg.getScript(), false, "Foundation.Data_Types.ActionExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.script");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getActionSequence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.actionSequence", al);
      print(arg.getActionSequence(), true, "Behavioral_Elements.Common_Behavior.ActionSequence");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.actionSequence");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.stimulus");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getActualArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.actualArgument", al);
        while(i.hasNext())
        {
          MArgument el = (MArgument)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Argument");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.actualArgument");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.UninterpretedAction");
  }

  public void printElementResidenceMain(MElementResidence arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.ElementResidence", al); al.clear();
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ElementResidence.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ElementResidence.visibility");
    }

    printXMIExtension(arg);
    if (null != arg.getResident())
    {
      dh.startElement("Foundation.Core.ElementResidence.resident", al);
      print(arg.getResident(), true, "Foundation.Core.ModelElement");
      dh.endElement("Foundation.Core.ElementResidence.resident");
    }
    if (null != arg.getImplementationLocation())
    {
      dh.startElement("Foundation.Core.ElementResidence.implementationLocation", al);
      print(arg.getImplementationLocation(), true, "Foundation.Core.Component");
      dh.endElement("Foundation.Core.ElementResidence.implementationLocation");
    }
    dh.endElement("Foundation.Core.ElementResidence");
  }

  public void printArgumentMain(MArgument arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.Argument", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getValue())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Argument.value", al);
      print(arg.getValue(), false, "Foundation.Data_Types.Expression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Argument.value");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getAction())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Argument.action", al);
      print(arg.getAction(), true, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.Common_Behavior.Argument.action");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.Argument");
  }

  public void printTransitionMain(MTransition arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.Transition", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getState())
    {
      dh.startElement("Behavioral_Elements.State_Machines.Transition.state", al);
      print(arg.getState(), true, "Behavioral_Elements.State_Machines.State");
      dh.endElement("Behavioral_Elements.State_Machines.Transition.state");
    }
    if (null != arg.getTrigger())
    {
      dh.startElement("Behavioral_Elements.State_Machines.Transition.trigger", al);
      print(arg.getTrigger(), true, "Behavioral_Elements.State_Machines.Event");
      dh.endElement("Behavioral_Elements.State_Machines.Transition.trigger");
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.Transition.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.Transition.stateMachine");
    }
    if (null != arg.getSource())
    {
      dh.startElement("Behavioral_Elements.State_Machines.Transition.source", al);
      print(arg.getSource(), true, "Behavioral_Elements.State_Machines.StateVertex");
      dh.endElement("Behavioral_Elements.State_Machines.Transition.source");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.State_Machines.Transition.target", al);
      print(arg.getTarget(), true, "Behavioral_Elements.State_Machines.StateVertex");
      dh.endElement("Behavioral_Elements.State_Machines.Transition.target");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getGuard())
    {
      dh.startElement("Behavioral_Elements.State_Machines.Transition.guard", al);
      print(arg.getGuard(), false, "Behavioral_Elements.State_Machines.Guard");
      dh.endElement("Behavioral_Elements.State_Machines.Transition.guard");
    }
    if (null != arg.getEffect())
    {
      dh.startElement("Behavioral_Elements.State_Machines.Transition.effect", al);
      print(arg.getEffect(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.Transition.effect");
    }
    dh.endElement("Behavioral_Elements.State_Machines.Transition");
  }

  public void printLinkMain(MLink arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.Link", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getAssociation())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Link.association", al);
      print(arg.getAssociation(), true, "Foundation.Core.Association");
      dh.endElement("Behavioral_Elements.Common_Behavior.Link.association");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Link.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Link.stimulus");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getConnections().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Link.connection", al);
        while(i.hasNext())
        {
          MLinkEnd el = (MLinkEnd)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.LinkEnd");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Link.connection");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.Link");
  }

  public void printFeatureMain(MFeature arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Feature", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getOwnerScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getOwnerScope().getName());      dh.startElement("Foundation.Core.Feature.ownerScope", al); al.clear();
      dh.endElement("Foundation.Core.Feature.ownerScope");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getOwner())
    {
      dh.startElement("Foundation.Core.Feature.owner", al);
      print(arg.getOwner(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.Feature.owner");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Feature");
  }

  public void printStateMain(MState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.State", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.State.stateMachine");
    }
    {
      Iterator i = arg.getDeferrableEvents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.deferrableEvent", al);
        while(i.hasNext())
        {
          MEvent el = (MEvent)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Event");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.deferrableEvent");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getEntry())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.entry", al);
      print(arg.getEntry(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.entry");
    }
    if (null != arg.getExit())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.exit", al);
      print(arg.getExit(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.exit");
    }
    {
      Iterator i = arg.getInternalTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.internalTransition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.internalTransition");
      }
    }
    if (null != arg.getDoActivity())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.doActivity", al);
      print(arg.getDoActivity(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.doActivity");
    }
    dh.endElement("Behavioral_Elements.State_Machines.State");
  }

  public void printAssociationClassMain(MAssociationClass arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.AssociationClass", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isActive()));    dh.startElement("Foundation.Core.Class.isActive", al); al.clear();
    dh.endElement("Foundation.Core.Class.isActive");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getConnections().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Association.connection", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, false, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Association.connection");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Foundation.Core.AssociationClass");
  }

  public void printUsageMain(MUsage arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Usage", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClients().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.client", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.client");
      }
    }
    {
      Iterator i = arg.getSuppliers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.supplier", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.supplier");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Usage");
  }

  public void printSynchStateMain(MSynchState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.SynchState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    dh.startElement("Behavioral_Elements.State_Machines.SynchState.bound", al);
    characters(String.valueOf(arg.getBound()));
    dh.endElement("Behavioral_Elements.State_Machines.SynchState.bound");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.SynchState");
  }

  public void printModelMain(MModel arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Model_Management.Model", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getElementImports().iterator();
      if (i.hasNext())
      {
        dh.startElement("Model_Management.Package.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Model_Management.Package.elementImport");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    dh.endElement("Model_Management.Model");
  }

  public void printAttributeLinkMain(MAttributeLink arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.AttributeLink", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getAttribute())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.AttributeLink.attribute", al);
      print(arg.getAttribute(), true, "Foundation.Core.Attribute");
      dh.endElement("Behavioral_Elements.Common_Behavior.AttributeLink.attribute");
    }
    if (null != arg.getValue())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.AttributeLink.value", al);
      print(arg.getValue(), true, "Behavioral_Elements.Common_Behavior.Instance");
      dh.endElement("Behavioral_Elements.Common_Behavior.AttributeLink.value");
    }
    if (null != arg.getInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.AttributeLink.instance", al);
      print(arg.getInstance(), true, "Behavioral_Elements.Common_Behavior.Instance");
      dh.endElement("Behavioral_Elements.Common_Behavior.AttributeLink.instance");
    }
    if (null != arg.getLinkEnd())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.AttributeLink.linkEnd", al);
      print(arg.getLinkEnd(), true, "Behavioral_Elements.Common_Behavior.LinkEnd");
      dh.endElement("Behavioral_Elements.Common_Behavior.AttributeLink.linkEnd");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.AttributeLink");
  }

  public void printTimeExpressionMain(MTimeExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.TimeExpression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.TimeExpression");
  }

  public void printReturnActionMain(MReturnAction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.ReturnAction", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getRecurrence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.recurrence", al);
      print(arg.getRecurrence(), false, "Foundation.Data_Types.IterationExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.recurrence");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.target", al);
      print(arg.getTarget(), false, "Foundation.Data_Types.ObjectSetExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.target");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAsynchronous()));    dh.startElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous");
    if (null != arg.getScript())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.script", al);
      print(arg.getScript(), false, "Foundation.Data_Types.ActionExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.script");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getActionSequence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.actionSequence", al);
      print(arg.getActionSequence(), true, "Behavioral_Elements.Common_Behavior.ActionSequence");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.actionSequence");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.stimulus");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getActualArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.actualArgument", al);
        while(i.hasNext())
        {
          MArgument el = (MArgument)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Argument");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.actualArgument");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.ReturnAction");
  }

  public void printMultiplicityRangeMain(MMultiplicityRange arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.MultiplicityRange", al); al.clear();
    dh.startElement("Foundation.Data_Types.MultiplicityRange.lower", al);
    characters(String.valueOf(arg.getLower()));
    dh.endElement("Foundation.Data_Types.MultiplicityRange.lower");
    dh.startElement("Foundation.Data_Types.MultiplicityRange.upper", al);
    characters(String.valueOf(arg.getUpper()));
    dh.endElement("Foundation.Data_Types.MultiplicityRange.upper");
    dh.endElement("Foundation.Data_Types.MultiplicityRange");
  }

  public void printActionStateMain(MActionState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Activity_Graphs.ActionState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isDynamic()));    dh.startElement("Behavioral_Elements.Activity_Graphs.ActionState.isDynamic", al); al.clear();
    dh.endElement("Behavioral_Elements.Activity_Graphs.ActionState.isDynamic");
    if (null != arg.getDynamicArguments())
    {
      dh.startElement("Behavioral_Elements.Activity_Graphs.ActionState.dynamicArguments", al);
      print(arg.getDynamicArguments(), false, "Foundation.Data_Types.ArgListsExpression");
      dh.endElement("Behavioral_Elements.Activity_Graphs.ActionState.dynamicArguments");
    }
    if (null != arg.getDynamicMultiplicity())
    {
      dh.startElement("Behavioral_Elements.Activity_Graphs.ActionState.dynamicMultiplicity", al);
      print(arg.getDynamicMultiplicity(), false, "Foundation.Data_Types.Multiplicity");
      dh.endElement("Behavioral_Elements.Activity_Graphs.ActionState.dynamicMultiplicity");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.State.stateMachine");
    }
    {
      Iterator i = arg.getDeferrableEvents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.deferrableEvent", al);
        while(i.hasNext())
        {
          MEvent el = (MEvent)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Event");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.deferrableEvent");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getEntry())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.entry", al);
      print(arg.getEntry(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.entry");
    }
    if (null != arg.getExit())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.exit", al);
      print(arg.getExit(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.exit");
    }
    {
      Iterator i = arg.getInternalTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.internalTransition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.internalTransition");
      }
    }
    if (null != arg.getDoActivity())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.doActivity", al);
      print(arg.getDoActivity(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.doActivity");
    }
    dh.endElement("Behavioral_Elements.Activity_Graphs.ActionState");
  }

  public void printActionMain(MAction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.Action", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getRecurrence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.recurrence", al);
      print(arg.getRecurrence(), false, "Foundation.Data_Types.IterationExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.recurrence");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.target", al);
      print(arg.getTarget(), false, "Foundation.Data_Types.ObjectSetExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.target");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAsynchronous()));    dh.startElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous");
    if (null != arg.getScript())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.script", al);
      print(arg.getScript(), false, "Foundation.Data_Types.ActionExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.script");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getActionSequence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.actionSequence", al);
      print(arg.getActionSequence(), true, "Behavioral_Elements.Common_Behavior.ActionSequence");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.actionSequence");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.stimulus");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getActualArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.actualArgument", al);
        while(i.hasNext())
        {
          MArgument el = (MArgument)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Argument");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.actualArgument");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.Action");
  }

  public void printUseCaseInstanceMain(MUseCaseInstance arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Use_Cases.UseCaseInstance", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClassifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.classifier", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.classifier");
      }
    }
    {
      Iterator i = arg.getAttributeLinks().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink");
      }
    }
    {
      Iterator i = arg.getLinkEnds().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd", al);
        while(i.hasNext())
        {
          MLinkEnd el = (MLinkEnd)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.LinkEnd");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd");
      }
    }
    {
      Iterator i = arg.getStimuli1().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1");
      }
    }
    {
      Iterator i = arg.getStimuli3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3");
      }
    }
    if (null != arg.getComponentInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance", al);
      print(arg.getComponentInstance(), true, "Behavioral_Elements.Common_Behavior.ComponentInstance");
      dh.endElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance");
    }
    {
      Iterator i = arg.getStimuli2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getSlots().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.slot", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.slot");
      }
    }
    dh.endElement("Behavioral_Elements.Use_Cases.UseCaseInstance");
  }

  public void printMessageMain(MMessage arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Collaborations.Message", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getInteraction())
    {
      dh.startElement("Behavioral_Elements.Collaborations.Message.interaction", al);
      print(arg.getInteraction(), true, "Behavioral_Elements.Collaborations.Interaction");
      dh.endElement("Behavioral_Elements.Collaborations.Message.interaction");
    }
    if (null != arg.getActivator())
    {
      dh.startElement("Behavioral_Elements.Collaborations.Message.activator", al);
      print(arg.getActivator(), true, "Behavioral_Elements.Collaborations.Message");
      dh.endElement("Behavioral_Elements.Collaborations.Message.activator");
    }
    {
      Iterator i = arg.getMessages4().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.Message.message4", al);
        while(i.hasNext())
        {
          MMessage el = (MMessage)i.next();
          print(el, true, "Behavioral_Elements.Collaborations.Message");
        }
        dh.endElement("Behavioral_Elements.Collaborations.Message.message4");
      }
    }
    if (null != arg.getSender())
    {
      dh.startElement("Behavioral_Elements.Collaborations.Message.sender", al);
      print(arg.getSender(), true, "Behavioral_Elements.Collaborations.ClassifierRole");
      dh.endElement("Behavioral_Elements.Collaborations.Message.sender");
    }
    if (null != arg.getReceiver())
    {
      dh.startElement("Behavioral_Elements.Collaborations.Message.receiver", al);
      print(arg.getReceiver(), true, "Behavioral_Elements.Collaborations.ClassifierRole");
      dh.endElement("Behavioral_Elements.Collaborations.Message.receiver");
    }
    {
      Iterator i = arg.getMessages3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.Message.message3", al);
        while(i.hasNext())
        {
          MMessage el = (MMessage)i.next();
          print(el, true, "Behavioral_Elements.Collaborations.Message");
        }
        dh.endElement("Behavioral_Elements.Collaborations.Message.message3");
      }
    }
    {
      Iterator i = arg.getPredecessors().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.Message.predecessor", al);
        while(i.hasNext())
        {
          MMessage el = (MMessage)i.next();
          print(el, true, "Behavioral_Elements.Collaborations.Message");
        }
        dh.endElement("Behavioral_Elements.Collaborations.Message.predecessor");
      }
    }
    if (null != arg.getCommunicationConnection())
    {
      dh.startElement("Behavioral_Elements.Collaborations.Message.communicationConnection", al);
      print(arg.getCommunicationConnection(), true, "Behavioral_Elements.Collaborations.AssociationRole");
      dh.endElement("Behavioral_Elements.Collaborations.Message.communicationConnection");
    }
    if (null != arg.getAction())
    {
      dh.startElement("Behavioral_Elements.Collaborations.Message.action", al);
      print(arg.getAction(), true, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.Collaborations.Message.action");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.Collaborations.Message");
  }

  public void printSubactivityStateMain(MSubactivityState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Activity_Graphs.SubactivityState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isDynamic()));    dh.startElement("Behavioral_Elements.Activity_Graphs.SubactivityState.isDynamic", al); al.clear();
    dh.endElement("Behavioral_Elements.Activity_Graphs.SubactivityState.isDynamic");
    if (null != arg.getDynamicArguments())
    {
      dh.startElement("Behavioral_Elements.Activity_Graphs.SubactivityState.dynamicArguments", al);
      print(arg.getDynamicArguments(), false, "Foundation.Data_Types.ArgListsExpression");
      dh.endElement("Behavioral_Elements.Activity_Graphs.SubactivityState.dynamicArguments");
    }
    if (null != arg.getDynamicMultiplicity())
    {
      dh.startElement("Behavioral_Elements.Activity_Graphs.SubactivityState.dynamicMultiplicity", al);
      print(arg.getDynamicMultiplicity(), false, "Foundation.Data_Types.Multiplicity");
      dh.endElement("Behavioral_Elements.Activity_Graphs.SubactivityState.dynamicMultiplicity");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.State.stateMachine");
    }
    {
      Iterator i = arg.getDeferrableEvents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.deferrableEvent", al);
        while(i.hasNext())
        {
          MEvent el = (MEvent)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Event");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.deferrableEvent");
      }
    }
    if (null != arg.getSubmachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.SubmachineState.submachine", al);
      print(arg.getSubmachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.SubmachineState.submachine");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getEntry())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.entry", al);
      print(arg.getEntry(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.entry");
    }
    if (null != arg.getExit())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.exit", al);
      print(arg.getExit(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.exit");
    }
    {
      Iterator i = arg.getInternalTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.internalTransition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.internalTransition");
      }
    }
    if (null != arg.getDoActivity())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.doActivity", al);
      print(arg.getDoActivity(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.doActivity");
    }
    {
      Iterator i = arg.getSubvertices().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.CompositeState.subvertex", al);
        while(i.hasNext())
        {
          MStateVertex el = (MStateVertex)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.StateVertex");
        }
        dh.endElement("Behavioral_Elements.State_Machines.CompositeState.subvertex");
      }
    }
    dh.endElement("Behavioral_Elements.Activity_Graphs.SubactivityState");
  }

  public void printSendActionMain(MSendAction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.SendAction", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getRecurrence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.recurrence", al);
      print(arg.getRecurrence(), false, "Foundation.Data_Types.IterationExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.recurrence");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.target", al);
      print(arg.getTarget(), false, "Foundation.Data_Types.ObjectSetExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.target");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAsynchronous()));    dh.startElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous");
    if (null != arg.getScript())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.script", al);
      print(arg.getScript(), false, "Foundation.Data_Types.ActionExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.script");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getActionSequence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.actionSequence", al);
      print(arg.getActionSequence(), true, "Behavioral_Elements.Common_Behavior.ActionSequence");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.actionSequence");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.stimulus");
      }
    }
    if (null != arg.getSignal())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.SendAction.signal", al);
      print(arg.getSignal(), true, "Behavioral_Elements.Common_Behavior.Signal");
      dh.endElement("Behavioral_Elements.Common_Behavior.SendAction.signal");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getActualArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.actualArgument", al);
        while(i.hasNext())
        {
          MArgument el = (MArgument)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Argument");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.actualArgument");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.SendAction");
  }

  public void printProcedureExpressionMain(MProcedureExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.ProcedureExpression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.ProcedureExpression");
  }

  public void printSignalMain(MSignal arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.Signal", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getReceptions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Signal.reception", al);
        while(i.hasNext())
        {
          MReception el = (MReception)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Reception");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Signal.reception");
      }
    }
    {
      Iterator i = arg.getContexts().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Signal.context", al);
        while(i.hasNext())
        {
          MBehavioralFeature el = (MBehavioralFeature)i.next();
          print(el, true, "Foundation.Core.BehavioralFeature");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Signal.context");
      }
    }
    {
      Iterator i = arg.getSendActions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Signal.sendAction", al);
        while(i.hasNext())
        {
          MSendAction el = (MSendAction)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.SendAction");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Signal.sendAction");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.Signal");
  }

  public void printNodeInstanceMain(MNodeInstance arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.NodeInstance", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClassifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.classifier", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.classifier");
      }
    }
    {
      Iterator i = arg.getAttributeLinks().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink");
      }
    }
    {
      Iterator i = arg.getLinkEnds().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd", al);
        while(i.hasNext())
        {
          MLinkEnd el = (MLinkEnd)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.LinkEnd");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd");
      }
    }
    {
      Iterator i = arg.getStimuli1().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1");
      }
    }
    {
      Iterator i = arg.getStimuli3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3");
      }
    }
    if (null != arg.getComponentInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance", al);
      print(arg.getComponentInstance(), true, "Behavioral_Elements.Common_Behavior.ComponentInstance");
      dh.endElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance");
    }
    {
      Iterator i = arg.getStimuli2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2");
      }
    }
    {
      Iterator i = arg.getResidents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.NodeInstance.resident", al);
        while(i.hasNext())
        {
          MComponentInstance el = (MComponentInstance)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.ComponentInstance");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.NodeInstance.resident");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getSlots().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.slot", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.slot");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.NodeInstance");
  }

  public void printReceptionMain(MReception arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.Reception", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getOwnerScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getOwnerScope().getName());      dh.startElement("Foundation.Core.Feature.ownerScope", al); al.clear();
      dh.endElement("Foundation.Core.Feature.ownerScope");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isQuery()));    dh.startElement("Foundation.Core.BehavioralFeature.isQuery", al); al.clear();
    dh.endElement("Foundation.Core.BehavioralFeature.isQuery");
    if (null != arg.getSpecification())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Reception.specification", al);
      characters(arg.getSpecification());
      dh.endElement("Behavioral_Elements.Common_Behavior.Reception.specification");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Behavioral_Elements.Common_Behavior.Reception.isRoot", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Reception.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Behavioral_Elements.Common_Behavior.Reception.isLeaf", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Reception.isLeaf");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getOwner())
    {
      dh.startElement("Foundation.Core.Feature.owner", al);
      print(arg.getOwner(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.Feature.owner");
    }
    if (null != arg.getSignal())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Reception.signal", al);
      print(arg.getSignal(), true, "Behavioral_Elements.Common_Behavior.Signal");
      dh.endElement("Behavioral_Elements.Common_Behavior.Reception.signal");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.BehavioralFeature.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, false, "Foundation.Core.Parameter");
        }
        dh.endElement("Foundation.Core.BehavioralFeature.parameter");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.Reception");
  }

  public void printTemplateParameterMain(MTemplateParameter arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.TemplateParameter", al); al.clear();

    printXMIExtension(arg);
    if (null != arg.getDefaultElement())
    {
      dh.startElement("Foundation.Core.TemplateParameter.defaultElement", al);
      print(arg.getDefaultElement(), true, "Foundation.Core.ModelElement");
      dh.endElement("Foundation.Core.TemplateParameter.defaultElement");
    }
    if (null != arg.getModelElement())
    {
      dh.startElement("Foundation.Core.TemplateParameter.modelElement", al);
      print(arg.getModelElement(), true, "Foundation.Core.ModelElement");
      dh.endElement("Foundation.Core.TemplateParameter.modelElement");
    }
    if (null != arg.getModelElement2())
    {
      dh.startElement("Foundation.Core.TemplateParameter.modelElement2", al);
      print(arg.getModelElement2(), true, "Foundation.Core.ModelElement");
      dh.endElement("Foundation.Core.TemplateParameter.modelElement2");
    }
    dh.endElement("Foundation.Core.TemplateParameter");
  }

  public void printAssociationRoleMain(MAssociationRole arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Collaborations.AssociationRole", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");
    if (null != arg.getMultiplicity())
    {
      dh.startElement("Behavioral_Elements.Collaborations.AssociationRole.multiplicity", al);
      print(arg.getMultiplicity(), false, "Foundation.Data_Types.Multiplicity");
      dh.endElement("Behavioral_Elements.Collaborations.AssociationRole.multiplicity");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    if (null != arg.getBase())
    {
      dh.startElement("Behavioral_Elements.Collaborations.AssociationRole.base", al);
      print(arg.getBase(), true, "Foundation.Core.Association");
      dh.endElement("Behavioral_Elements.Collaborations.AssociationRole.base");
    }
    {
      Iterator i = arg.getMessages().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.AssociationRole.message", al);
        while(i.hasNext())
        {
          MMessage el = (MMessage)i.next();
          print(el, true, "Behavioral_Elements.Collaborations.Message");
        }
        dh.endElement("Behavioral_Elements.Collaborations.AssociationRole.message");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getConnections().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Association.connection", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, false, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Association.connection");
      }
    }
    dh.endElement("Behavioral_Elements.Collaborations.AssociationRole");
  }

  public void printArgListsExpressionMain(MArgListsExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.ArgListsExpression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.ArgListsExpression");
  }

  public void printExtendMain(MExtend arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Use_Cases.Extend", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getCondition())
    {
      dh.startElement("Behavioral_Elements.Use_Cases.Extend.condition", al);
      print(arg.getCondition(), false, "Foundation.Data_Types.BooleanExpression");
      dh.endElement("Behavioral_Elements.Use_Cases.Extend.condition");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getBase())
    {
      dh.startElement("Behavioral_Elements.Use_Cases.Extend.base", al);
      print(arg.getBase(), true, "Behavioral_Elements.Use_Cases.UseCase");
      dh.endElement("Behavioral_Elements.Use_Cases.Extend.base");
    }
    if (null != arg.getExtension())
    {
      dh.startElement("Behavioral_Elements.Use_Cases.Extend.extension", al);
      print(arg.getExtension(), true, "Behavioral_Elements.Use_Cases.UseCase");
      dh.endElement("Behavioral_Elements.Use_Cases.Extend.extension");
    }
    {
      Iterator i = arg.getExtensionPoints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Use_Cases.Extend.extensionPoint", al);
        while(i.hasNext())
        {
          MExtensionPoint el = (MExtensionPoint)i.next();
          print(el, true, "Behavioral_Elements.Use_Cases.ExtensionPoint");
        }
        dh.endElement("Behavioral_Elements.Use_Cases.Extend.extensionPoint");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.Use_Cases.Extend");
  }

  public void printStateVertexMain(MStateVertex arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.StateVertex", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.StateVertex");
  }

  public void printParameterMain(MParameter arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Parameter", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getDefaultValue())
    {
      dh.startElement("Foundation.Core.Parameter.defaultValue", al);
      print(arg.getDefaultValue(), false, "Foundation.Data_Types.Expression");
      dh.endElement("Foundation.Core.Parameter.defaultValue");
    }
    if (null != arg.getKind())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getKind().getName());      dh.startElement("Foundation.Core.Parameter.kind", al); al.clear();
      dh.endElement("Foundation.Core.Parameter.kind");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getBehavioralFeature())
    {
      dh.startElement("Foundation.Core.Parameter.behavioralFeature", al);
      print(arg.getBehavioralFeature(), true, "Foundation.Core.BehavioralFeature");
      dh.endElement("Foundation.Core.Parameter.behavioralFeature");
    }
    if (null != arg.getType())
    {
      dh.startElement("Foundation.Core.Parameter.type", al);
      print(arg.getType(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.Parameter.type");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Parameter");
  }

  public void printCompositeStateMain(MCompositeState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.CompositeState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.State.stateMachine");
    }
    {
      Iterator i = arg.getDeferrableEvents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.deferrableEvent", al);
        while(i.hasNext())
        {
          MEvent el = (MEvent)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Event");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.deferrableEvent");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getEntry())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.entry", al);
      print(arg.getEntry(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.entry");
    }
    if (null != arg.getExit())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.exit", al);
      print(arg.getExit(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.exit");
    }
    {
      Iterator i = arg.getInternalTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.internalTransition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.internalTransition");
      }
    }
    if (null != arg.getDoActivity())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.doActivity", al);
      print(arg.getDoActivity(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.doActivity");
    }
    {
      Iterator i = arg.getSubvertices().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.CompositeState.subvertex", al);
        while(i.hasNext())
        {
          MStateVertex el = (MStateVertex)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.StateVertex");
        }
        dh.endElement("Behavioral_Elements.State_Machines.CompositeState.subvertex");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.CompositeState");
  }

  public void printStructuralFeatureMain(MStructuralFeature arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.StructuralFeature", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getOwnerScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getOwnerScope().getName());      dh.startElement("Foundation.Core.Feature.ownerScope", al); al.clear();
      dh.endElement("Foundation.Core.Feature.ownerScope");
    }
    if (null != arg.getMultiplicity())
    {
      dh.startElement("Foundation.Core.StructuralFeature.multiplicity", al);
      print(arg.getMultiplicity(), false, "Foundation.Data_Types.Multiplicity");
      dh.endElement("Foundation.Core.StructuralFeature.multiplicity");
    }
    if (null != arg.getChangeability())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getChangeability().getName());      dh.startElement("Foundation.Core.StructuralFeature.changeability", al); al.clear();
      dh.endElement("Foundation.Core.StructuralFeature.changeability");
    }
    if (null != arg.getTargetScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getTargetScope().getName());      dh.startElement("Foundation.Core.StructuralFeature.targetScope", al); al.clear();
      dh.endElement("Foundation.Core.StructuralFeature.targetScope");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getOwner())
    {
      dh.startElement("Foundation.Core.Feature.owner", al);
      print(arg.getOwner(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.Feature.owner");
    }
    if (null != arg.getType())
    {
      dh.startElement("Foundation.Core.StructuralFeature.type", al);
      print(arg.getType(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.StructuralFeature.type");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.StructuralFeature");
  }

  public void printDataTypeMain(MDataType arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.DataType", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Foundation.Core.DataType");
  }

  public void printTypeExpressionMain(MTypeExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.TypeExpression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.TypeExpression");
  }

  public void printStubStateMain(MStubState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.StubState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getReferenceState())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StubState.referenceState", al);
      characters(arg.getReferenceState());
      dh.endElement("Behavioral_Elements.State_Machines.StubState.referenceState");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.StubState");
  }

  public void printActivityGraphMain(MActivityGraph arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Activity_Graphs.ActivityGraph", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContext())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateMachine.context", al);
      print(arg.getContext(), true, "Foundation.Core.ModelElement");
      dh.endElement("Behavioral_Elements.State_Machines.StateMachine.context");
    }
    {
      Iterator i = arg.getSubmachineStates().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateMachine.subMachineState", al);
        while(i.hasNext())
        {
          MSubmachineState el = (MSubmachineState)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.SubmachineState");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateMachine.subMachineState");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getTop())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateMachine.top", al);
      print(arg.getTop(), false, "Behavioral_Elements.State_Machines.State");
      dh.endElement("Behavioral_Elements.State_Machines.StateMachine.top");
    }
    {
      Iterator i = arg.getTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateMachine.transitions", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateMachine.transitions");
      }
    }
    {
      Iterator i = arg.getPartitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Activity_Graphs.ActivityGraph.partition", al);
        while(i.hasNext())
        {
          MPartition el = (MPartition)i.next();
          print(el, false, "Behavioral_Elements.Activity_Graphs.Partition");
        }
        dh.endElement("Behavioral_Elements.Activity_Graphs.ActivityGraph.partition");
      }
    }
    dh.endElement("Behavioral_Elements.Activity_Graphs.ActivityGraph");
  }

  public void printPackageMain(MPackage arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Model_Management.Package", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getElementImports().iterator();
      if (i.hasNext())
      {
        dh.startElement("Model_Management.Package.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Model_Management.Package.elementImport");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    dh.endElement("Model_Management.Package");
  }

  public void printMultiplicityMain(MMultiplicity arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.Multiplicity", al); al.clear();
    {
      Iterator i = arg.getRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Data_Types.Multiplicity.range", al);
        while(i.hasNext())
        {
          MMultiplicityRange el = (MMultiplicityRange)i.next();
          print(el, false, "Foundation.Data_Types.MultiplicityRange");
        }
        dh.endElement("Foundation.Data_Types.Multiplicity.range");
      }
    }
    dh.endElement("Foundation.Data_Types.Multiplicity");
  }

  public void printLinkObjectMain(MLinkObject arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.LinkObject", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClassifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.classifier", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.classifier");
      }
    }
    {
      Iterator i = arg.getAttributeLinks().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink");
      }
    }
    {
      Iterator i = arg.getLinkEnds().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd", al);
        while(i.hasNext())
        {
          MLinkEnd el = (MLinkEnd)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.LinkEnd");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd");
      }
    }
    {
      Iterator i = arg.getStimuli1().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1");
      }
    }
    {
      Iterator i = arg.getStimuli3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3");
      }
    }
    if (null != arg.getComponentInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance", al);
      print(arg.getComponentInstance(), true, "Behavioral_Elements.Common_Behavior.ComponentInstance");
      dh.endElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance");
    }
    {
      Iterator i = arg.getStimuli2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2");
      }
    }
    if (null != arg.getAssociation())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Link.association", al);
      print(arg.getAssociation(), true, "Foundation.Core.Association");
      dh.endElement("Behavioral_Elements.Common_Behavior.Link.association");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Link.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Link.stimulus");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getSlots().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.slot", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.slot");
      }
    }
    {
      Iterator i = arg.getConnections().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Link.connection", al);
        while(i.hasNext())
        {
          MLinkEnd el = (MLinkEnd)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.LinkEnd");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Link.connection");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.LinkObject");
  }

  public void printBooleanExpressionMain(MBooleanExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.BooleanExpression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.BooleanExpression");
  }

  public void printAbstractionMain(MAbstraction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Abstraction", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getMapping())
    {
      dh.startElement("Foundation.Core.Abstraction.mapping", al);
      print(arg.getMapping(), false, "Foundation.Data_Types.MappingExpression");
      dh.endElement("Foundation.Core.Abstraction.mapping");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClients().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.client", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.client");
      }
    }
    {
      Iterator i = arg.getSuppliers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.supplier", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.supplier");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Abstraction");
  }

  public void printChangeEventMain(MChangeEvent arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.ChangeEvent", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getChangeExpression())
    {
      dh.startElement("Behavioral_Elements.State_Machines.ChangeEvent.changeExpression", al);
      print(arg.getChangeExpression(), false, "Foundation.Data_Types.BooleanExpression");
      dh.endElement("Behavioral_Elements.State_Machines.ChangeEvent.changeExpression");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getStates().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.state", al);
        while(i.hasNext())
        {
          MState el = (MState)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.State");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.state");
      }
    }
    {
      Iterator i = arg.getTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.transition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.transition");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, false, "Foundation.Core.Parameter");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.parameter");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.ChangeEvent");
  }

  public void printActionExpressionMain(MActionExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.ActionExpression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.ActionExpression");
  }

  public void printNodeMain(MNode arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Node", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getResidents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Node.resident", al);
        while(i.hasNext())
        {
          MComponent el = (MComponent)i.next();
          print(el, true, "Foundation.Core.Component");
        }
        dh.endElement("Foundation.Core.Node.resident");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Foundation.Core.Node");
  }

  public void printIterationExpressionMain(MIterationExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.IterationExpression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.IterationExpression");
  }

  public void printCallStateMain(MCallState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Activity_Graphs.CallState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isDynamic()));    dh.startElement("Behavioral_Elements.Activity_Graphs.ActionState.isDynamic", al); al.clear();
    dh.endElement("Behavioral_Elements.Activity_Graphs.ActionState.isDynamic");
    if (null != arg.getDynamicArguments())
    {
      dh.startElement("Behavioral_Elements.Activity_Graphs.ActionState.dynamicArguments", al);
      print(arg.getDynamicArguments(), false, "Foundation.Data_Types.ArgListsExpression");
      dh.endElement("Behavioral_Elements.Activity_Graphs.ActionState.dynamicArguments");
    }
    if (null != arg.getDynamicMultiplicity())
    {
      dh.startElement("Behavioral_Elements.Activity_Graphs.ActionState.dynamicMultiplicity", al);
      print(arg.getDynamicMultiplicity(), false, "Foundation.Data_Types.Multiplicity");
      dh.endElement("Behavioral_Elements.Activity_Graphs.ActionState.dynamicMultiplicity");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.State.stateMachine");
    }
    {
      Iterator i = arg.getDeferrableEvents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.deferrableEvent", al);
        while(i.hasNext())
        {
          MEvent el = (MEvent)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Event");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.deferrableEvent");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getEntry())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.entry", al);
      print(arg.getEntry(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.entry");
    }
    if (null != arg.getExit())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.exit", al);
      print(arg.getExit(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.exit");
    }
    {
      Iterator i = arg.getInternalTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.internalTransition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.internalTransition");
      }
    }
    if (null != arg.getDoActivity())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.doActivity", al);
      print(arg.getDoActivity(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.doActivity");
    }
    dh.endElement("Behavioral_Elements.Activity_Graphs.CallState");
  }

  public void printInterfaceMain(MInterface arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Interface", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Foundation.Core.Interface");
  }

  public void printMappingExpressionMain(MMappingExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.MappingExpression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.MappingExpression");
  }

  public void printComponentInstanceMain(MComponentInstance arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.ComponentInstance", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClassifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.classifier", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.classifier");
      }
    }
    {
      Iterator i = arg.getAttributeLinks().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink");
      }
    }
    {
      Iterator i = arg.getLinkEnds().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd", al);
        while(i.hasNext())
        {
          MLinkEnd el = (MLinkEnd)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.LinkEnd");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd");
      }
    }
    {
      Iterator i = arg.getStimuli1().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1");
      }
    }
    {
      Iterator i = arg.getStimuli3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3");
      }
    }
    if (null != arg.getComponentInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance", al);
      print(arg.getComponentInstance(), true, "Behavioral_Elements.Common_Behavior.ComponentInstance");
      dh.endElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance");
    }
    {
      Iterator i = arg.getStimuli2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2");
      }
    }
    if (null != arg.getNodeInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.ComponentInstance.nodeInstance", al);
      print(arg.getNodeInstance(), true, "Behavioral_Elements.Common_Behavior.NodeInstance");
      dh.endElement("Behavioral_Elements.Common_Behavior.ComponentInstance.nodeInstance");
    }
    {
      Iterator i = arg.getResidents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.ComponentInstance.resident", al);
        while(i.hasNext())
        {
          MInstance el = (MInstance)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Instance");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.ComponentInstance.resident");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getSlots().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.slot", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.slot");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.ComponentInstance");
  }

  public void printObjectFlowStateMain(MObjectFlowState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Activity_Graphs.ObjectFlowState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSynch()));    dh.startElement("Behavioral_Elements.Activity_Graphs.ObjectFlowState.isSynch", al); al.clear();
    dh.endElement("Behavioral_Elements.Activity_Graphs.ObjectFlowState.isSynch");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.State.stateMachine");
    }
    {
      Iterator i = arg.getDeferrableEvents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.deferrableEvent", al);
        while(i.hasNext())
        {
          MEvent el = (MEvent)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Event");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.deferrableEvent");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Activity_Graphs.ObjectFlowState.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, true, "Foundation.Core.Parameter");
        }
        dh.endElement("Behavioral_Elements.Activity_Graphs.ObjectFlowState.parameter");
      }
    }
    if (null != arg.getType())
    {
      dh.startElement("Behavioral_Elements.Activity_Graphs.ObjectFlowState.type", al);
      print(arg.getType(), true, "Foundation.Core.Classifier");
      dh.endElement("Behavioral_Elements.Activity_Graphs.ObjectFlowState.type");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getEntry())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.entry", al);
      print(arg.getEntry(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.entry");
    }
    if (null != arg.getExit())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.exit", al);
      print(arg.getExit(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.exit");
    }
    {
      Iterator i = arg.getInternalTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.internalTransition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.internalTransition");
      }
    }
    if (null != arg.getDoActivity())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.doActivity", al);
      print(arg.getDoActivity(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.doActivity");
    }
    dh.endElement("Behavioral_Elements.Activity_Graphs.ObjectFlowState");
  }

  public void printSignalEventMain(MSignalEvent arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.SignalEvent", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getStates().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.state", al);
        while(i.hasNext())
        {
          MState el = (MState)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.State");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.state");
      }
    }
    {
      Iterator i = arg.getTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.transition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.transition");
      }
    }
    if (null != arg.getSignal())
    {
      dh.startElement("Behavioral_Elements.State_Machines.SignalEvent.signal", al);
      print(arg.getSignal(), true, "Behavioral_Elements.Common_Behavior.Signal");
      dh.endElement("Behavioral_Elements.State_Machines.SignalEvent.signal");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, false, "Foundation.Core.Parameter");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.parameter");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.SignalEvent");
  }

  public void printModelElementMain(MModelElement arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.ModelElement", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.ModelElement");
  }

  public void printDependencyMain(MDependency arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Dependency", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClients().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.client", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.client");
      }
    }
    {
      Iterator i = arg.getSuppliers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.supplier", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.supplier");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Dependency");
  }

  public void printInstanceMain(MInstance arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.Instance", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClassifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.classifier", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.classifier");
      }
    }
    {
      Iterator i = arg.getAttributeLinks().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink");
      }
    }
    {
      Iterator i = arg.getLinkEnds().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd", al);
        while(i.hasNext())
        {
          MLinkEnd el = (MLinkEnd)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.LinkEnd");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd");
      }
    }
    {
      Iterator i = arg.getStimuli1().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1");
      }
    }
    {
      Iterator i = arg.getStimuli3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3");
      }
    }
    if (null != arg.getComponentInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance", al);
      print(arg.getComponentInstance(), true, "Behavioral_Elements.Common_Behavior.ComponentInstance");
      dh.endElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance");
    }
    {
      Iterator i = arg.getStimuli2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getSlots().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.slot", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.slot");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.Instance");
  }

  public void printAttributeMain(MAttribute arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Attribute", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getOwnerScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getOwnerScope().getName());      dh.startElement("Foundation.Core.Feature.ownerScope", al); al.clear();
      dh.endElement("Foundation.Core.Feature.ownerScope");
    }
    if (null != arg.getMultiplicity())
    {
      dh.startElement("Foundation.Core.StructuralFeature.multiplicity", al);
      print(arg.getMultiplicity(), false, "Foundation.Data_Types.Multiplicity");
      dh.endElement("Foundation.Core.StructuralFeature.multiplicity");
    }
    if (null != arg.getChangeability())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getChangeability().getName());      dh.startElement("Foundation.Core.StructuralFeature.changeability", al); al.clear();
      dh.endElement("Foundation.Core.StructuralFeature.changeability");
    }
    if (null != arg.getTargetScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getTargetScope().getName());      dh.startElement("Foundation.Core.StructuralFeature.targetScope", al); al.clear();
      dh.endElement("Foundation.Core.StructuralFeature.targetScope");
    }
    if (null != arg.getInitialValue())
    {
      dh.startElement("Foundation.Core.Attribute.initialValue", al);
      print(arg.getInitialValue(), false, "Foundation.Data_Types.Expression");
      dh.endElement("Foundation.Core.Attribute.initialValue");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getOwner())
    {
      dh.startElement("Foundation.Core.Feature.owner", al);
      print(arg.getOwner(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.Feature.owner");
    }
    if (null != arg.getType())
    {
      dh.startElement("Foundation.Core.StructuralFeature.type", al);
      print(arg.getType(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.StructuralFeature.type");
    }
    if (null != arg.getAssociationEnd())
    {
      dh.startElement("Foundation.Core.Attribute.associationEnd", al);
      print(arg.getAssociationEnd(), true, "Foundation.Core.AssociationEnd");
      dh.endElement("Foundation.Core.Attribute.associationEnd");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Attribute");
  }

  public void printGeneralizationMain(MGeneralization arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Generalization", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getDiscriminator())
    {
      dh.startElement("Foundation.Core.Generalization.discriminator", al);
      characters(arg.getDiscriminator());
      dh.endElement("Foundation.Core.Generalization.discriminator");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getChild())
    {
      dh.startElement("Foundation.Core.Generalization.child", al);
      print(arg.getChild(), true, "Foundation.Core.GeneralizableElement");
      dh.endElement("Foundation.Core.Generalization.child");
    }
    if (null != arg.getParent())
    {
      dh.startElement("Foundation.Core.Generalization.parent", al);
      print(arg.getParent(), true, "Foundation.Core.GeneralizableElement");
      dh.endElement("Foundation.Core.Generalization.parent");
    }
    if (null != arg.getPowertype())
    {
      dh.startElement("Foundation.Core.Generalization.powertype", al);
      print(arg.getPowertype(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.Generalization.powertype");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Generalization");
  }

  public void printCallActionMain(MCallAction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.CallAction", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getRecurrence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.recurrence", al);
      print(arg.getRecurrence(), false, "Foundation.Data_Types.IterationExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.recurrence");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.target", al);
      print(arg.getTarget(), false, "Foundation.Data_Types.ObjectSetExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.target");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAsynchronous()));    dh.startElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous");
    if (null != arg.getScript())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.script", al);
      print(arg.getScript(), false, "Foundation.Data_Types.ActionExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.script");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getActionSequence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.actionSequence", al);
      print(arg.getActionSequence(), true, "Behavioral_Elements.Common_Behavior.ActionSequence");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.actionSequence");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.stimulus");
      }
    }
    if (null != arg.getOperation())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.CallAction.operation", al);
      print(arg.getOperation(), true, "Foundation.Core.Operation");
      dh.endElement("Behavioral_Elements.Common_Behavior.CallAction.operation");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getActualArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.actualArgument", al);
        while(i.hasNext())
        {
          MArgument el = (MArgument)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Argument");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.actualArgument");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.CallAction");
  }

  public void printGuardMain(MGuard arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.Guard", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getExpression())
    {
      dh.startElement("Behavioral_Elements.State_Machines.Guard.expression", al);
      print(arg.getExpression(), false, "Foundation.Data_Types.BooleanExpression");
      dh.endElement("Behavioral_Elements.State_Machines.Guard.expression");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getTransition())
    {
      dh.startElement("Behavioral_Elements.State_Machines.Guard.transition", al);
      print(arg.getTransition(), true, "Behavioral_Elements.State_Machines.Transition");
      dh.endElement("Behavioral_Elements.State_Machines.Guard.transition");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.Guard");
  }

  public void printClassifierMain(MClassifier arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Classifier", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Foundation.Core.Classifier");
  }

  public void printObjectMain(MObject arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.Object", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClassifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.classifier", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.classifier");
      }
    }
    {
      Iterator i = arg.getAttributeLinks().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink");
      }
    }
    {
      Iterator i = arg.getLinkEnds().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd", al);
        while(i.hasNext())
        {
          MLinkEnd el = (MLinkEnd)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.LinkEnd");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd");
      }
    }
    {
      Iterator i = arg.getStimuli1().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1");
      }
    }
    {
      Iterator i = arg.getStimuli3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3");
      }
    }
    if (null != arg.getComponentInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance", al);
      print(arg.getComponentInstance(), true, "Behavioral_Elements.Common_Behavior.ComponentInstance");
      dh.endElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance");
    }
    {
      Iterator i = arg.getStimuli2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getSlots().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.slot", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.slot");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.Object");
  }

  public void printOperationMain(MOperation arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Operation", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getOwnerScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getOwnerScope().getName());      dh.startElement("Foundation.Core.Feature.ownerScope", al); al.clear();
      dh.endElement("Foundation.Core.Feature.ownerScope");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isQuery()));    dh.startElement("Foundation.Core.BehavioralFeature.isQuery", al); al.clear();
    dh.endElement("Foundation.Core.BehavioralFeature.isQuery");
    if (null != arg.getConcurrency())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getConcurrency().getName());      dh.startElement("Foundation.Core.Operation.concurrency", al); al.clear();
      dh.endElement("Foundation.Core.Operation.concurrency");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.Operation.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.Operation.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.Operation.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.Operation.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.Operation.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.Operation.isAbstract");
    if (null != arg.getSpecification())
    {
      dh.startElement("Foundation.Core.Operation.specification", al);
      characters(arg.getSpecification());
      dh.endElement("Foundation.Core.Operation.specification");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getOwner())
    {
      dh.startElement("Foundation.Core.Feature.owner", al);
      print(arg.getOwner(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.Feature.owner");
    }
    {
      Iterator i = arg.getMethods().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Operation.method", al);
        while(i.hasNext())
        {
          MMethod el = (MMethod)i.next();
          print(el, true, "Foundation.Core.Method");
        }
        dh.endElement("Foundation.Core.Operation.method");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.BehavioralFeature.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, false, "Foundation.Core.Parameter");
        }
        dh.endElement("Foundation.Core.BehavioralFeature.parameter");
      }
    }
    dh.endElement("Foundation.Core.Operation");
  }

  public void printInteractionMain(MInteraction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Collaborations.Interaction", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContext())
    {
      dh.startElement("Behavioral_Elements.Collaborations.Interaction.context", al);
      print(arg.getContext(), true, "Behavioral_Elements.Collaborations.Collaboration");
      dh.endElement("Behavioral_Elements.Collaborations.Interaction.context");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getMessages().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.Interaction.message", al);
        while(i.hasNext())
        {
          MMessage el = (MMessage)i.next();
          print(el, false, "Behavioral_Elements.Collaborations.Message");
        }
        dh.endElement("Behavioral_Elements.Collaborations.Interaction.message");
      }
    }
    dh.endElement("Behavioral_Elements.Collaborations.Interaction");
  }

  public void printFinalStateMain(MFinalState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.FinalState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.State.stateMachine");
    }
    {
      Iterator i = arg.getDeferrableEvents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.deferrableEvent", al);
        while(i.hasNext())
        {
          MEvent el = (MEvent)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Event");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.deferrableEvent");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getEntry())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.entry", al);
      print(arg.getEntry(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.entry");
    }
    if (null != arg.getExit())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.exit", al);
      print(arg.getExit(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.exit");
    }
    {
      Iterator i = arg.getInternalTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.internalTransition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.internalTransition");
      }
    }
    if (null != arg.getDoActivity())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.doActivity", al);
      print(arg.getDoActivity(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.doActivity");
    }
    dh.endElement("Behavioral_Elements.State_Machines.FinalState");
  }

  public void printBindingMain(MBinding arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Binding", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClients().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.client", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.client");
      }
    }
    {
      Iterator i = arg.getSuppliers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.supplier", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.supplier");
      }
    }
    {
      Iterator i = arg.getArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Binding.argument", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Binding.argument");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Binding");
  }

  public void printCreateActionMain(MCreateAction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.CreateAction", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getRecurrence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.recurrence", al);
      print(arg.getRecurrence(), false, "Foundation.Data_Types.IterationExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.recurrence");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.target", al);
      print(arg.getTarget(), false, "Foundation.Data_Types.ObjectSetExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.target");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAsynchronous()));    dh.startElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous");
    if (null != arg.getScript())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.script", al);
      print(arg.getScript(), false, "Foundation.Data_Types.ActionExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.script");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getActionSequence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.actionSequence", al);
      print(arg.getActionSequence(), true, "Behavioral_Elements.Common_Behavior.ActionSequence");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.actionSequence");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.stimulus");
      }
    }
    if (null != arg.getInstantiation())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.CreateAction.instantiation", al);
      print(arg.getInstantiation(), true, "Foundation.Core.Classifier");
      dh.endElement("Behavioral_Elements.Common_Behavior.CreateAction.instantiation");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getActualArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.actualArgument", al);
        while(i.hasNext())
        {
          MArgument el = (MArgument)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Argument");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.actualArgument");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.CreateAction");
  }

  public void printStereotypeMain(MStereotype arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Extension_Mechanisms.Stereotype", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");
    if (null != arg.getIcon())
    {
      dh.startElement("Foundation.Extension_Mechanisms.Stereotype.icon", al);
      characters(arg.getIcon());
      dh.endElement("Foundation.Extension_Mechanisms.Stereotype.icon");
    }
    if (null != arg.getBaseClass())
    {
      dh.startElement("Foundation.Extension_Mechanisms.Stereotype.baseClass", al);
      characters(arg.getBaseClass());
      dh.endElement("Foundation.Extension_Mechanisms.Stereotype.baseClass");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getExtendedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Extension_Mechanisms.Stereotype.extendedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Extension_Mechanisms.Stereotype.extendedElement");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getRequiredTags().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Extension_Mechanisms.Stereotype.requiredTag", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Extension_Mechanisms.Stereotype.requiredTag");
      }
    }
    {
      Iterator i = arg.getStereotypeConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Extension_Mechanisms.Stereotype.stereotypeConstraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, false, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Extension_Mechanisms.Stereotype.stereotypeConstraint");
      }
    }
    dh.endElement("Foundation.Extension_Mechanisms.Stereotype");
  }

  public void printSimpleStateMain(MSimpleState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.SimpleState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.State.stateMachine");
    }
    {
      Iterator i = arg.getDeferrableEvents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.deferrableEvent", al);
        while(i.hasNext())
        {
          MEvent el = (MEvent)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Event");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.deferrableEvent");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getEntry())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.entry", al);
      print(arg.getEntry(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.entry");
    }
    if (null != arg.getExit())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.exit", al);
      print(arg.getExit(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.exit");
    }
    {
      Iterator i = arg.getInternalTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.internalTransition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.internalTransition");
      }
    }
    if (null != arg.getDoActivity())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.doActivity", al);
      print(arg.getDoActivity(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.doActivity");
    }
    dh.endElement("Behavioral_Elements.State_Machines.SimpleState");
  }

  public void printSubsystemMain(MSubsystem arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Model_Management.Subsystem", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isInstantiable()));    dh.startElement("Model_Management.Subsystem.isInstantiable", al); al.clear();
    dh.endElement("Model_Management.Subsystem.isInstantiable");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getElementImports().iterator();
      if (i.hasNext())
      {
        dh.startElement("Model_Management.Package.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Model_Management.Package.elementImport");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Model_Management.Subsystem");
  }

  public void printIncludeMain(MInclude arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Use_Cases.Include", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getAddition())
    {
      dh.startElement("Behavioral_Elements.Use_Cases.Include.addition", al);
      print(arg.getAddition(), true, "Behavioral_Elements.Use_Cases.UseCase");
      dh.endElement("Behavioral_Elements.Use_Cases.Include.addition");
    }
    if (null != arg.getBase())
    {
      dh.startElement("Behavioral_Elements.Use_Cases.Include.base", al);
      print(arg.getBase(), true, "Behavioral_Elements.Use_Cases.UseCase");
      dh.endElement("Behavioral_Elements.Use_Cases.Include.base");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.Use_Cases.Include");
  }

  public void printAssociationEndRoleMain(MAssociationEndRole arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Collaborations.AssociationEndRole", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isNavigable()));    dh.startElement("Foundation.Core.AssociationEnd.isNavigable", al); al.clear();
    dh.endElement("Foundation.Core.AssociationEnd.isNavigable");
    if (null != arg.getOrdering())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getOrdering().getName());      dh.startElement("Foundation.Core.AssociationEnd.ordering", al); al.clear();
      dh.endElement("Foundation.Core.AssociationEnd.ordering");
    }
    if (null != arg.getAggregation())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getAggregation().getName());      dh.startElement("Foundation.Core.AssociationEnd.aggregation", al); al.clear();
      dh.endElement("Foundation.Core.AssociationEnd.aggregation");
    }
    if (null != arg.getTargetScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getTargetScope().getName());      dh.startElement("Foundation.Core.AssociationEnd.targetScope", al); al.clear();
      dh.endElement("Foundation.Core.AssociationEnd.targetScope");
    }
    if (null != arg.getMultiplicity())
    {
      dh.startElement("Foundation.Core.AssociationEnd.multiplicity", al);
      print(arg.getMultiplicity(), false, "Foundation.Data_Types.Multiplicity");
      dh.endElement("Foundation.Core.AssociationEnd.multiplicity");
    }
    if (null != arg.getChangeability())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getChangeability().getName());      dh.startElement("Foundation.Core.AssociationEnd.changeability", al); al.clear();
      dh.endElement("Foundation.Core.AssociationEnd.changeability");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getAssociation())
    {
      dh.startElement("Foundation.Core.AssociationEnd.association", al);
      print(arg.getAssociation(), true, "Foundation.Core.Association");
      dh.endElement("Foundation.Core.AssociationEnd.association");
    }
    if (null != arg.getType())
    {
      dh.startElement("Foundation.Core.AssociationEnd.type", al);
      print(arg.getType(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.AssociationEnd.type");
    }
    {
      Iterator i = arg.getSpecifications().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.AssociationEnd.specification", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Foundation.Core.AssociationEnd.specification");
      }
    }
    if (null != arg.getBase())
    {
      dh.startElement("Behavioral_Elements.Collaborations.AssociationEndRole.base", al);
      print(arg.getBase(), true, "Foundation.Core.AssociationEnd");
      dh.endElement("Behavioral_Elements.Collaborations.AssociationEndRole.base");
    }
    {
      Iterator i = arg.getAvailableQualifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.AssociationEndRole.availableQualifier", al);
        while(i.hasNext())
        {
          MAttribute el = (MAttribute)i.next();
          print(el, true, "Foundation.Core.Attribute");
        }
        dh.endElement("Behavioral_Elements.Collaborations.AssociationEndRole.availableQualifier");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getQualifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.AssociationEnd.qualifier", al);
        while(i.hasNext())
        {
          MAttribute el = (MAttribute)i.next();
          print(el, false, "Foundation.Core.Attribute");
        }
        dh.endElement("Foundation.Core.AssociationEnd.qualifier");
      }
    }
    dh.endElement("Behavioral_Elements.Collaborations.AssociationEndRole");
  }

  public void printCollaborationMain(MCollaboration arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Collaborations.Collaboration", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    if (null != arg.getRepresentedClassifier())
    {
      dh.startElement("Behavioral_Elements.Collaborations.Collaboration.representedClassifier", al);
      print(arg.getRepresentedClassifier(), true, "Foundation.Core.Classifier");
      dh.endElement("Behavioral_Elements.Collaborations.Collaboration.representedClassifier");
    }
    if (null != arg.getRepresentedOperation())
    {
      dh.startElement("Behavioral_Elements.Collaborations.Collaboration.representedOperation", al);
      print(arg.getRepresentedOperation(), true, "Foundation.Core.Operation");
      dh.endElement("Behavioral_Elements.Collaborations.Collaboration.representedOperation");
    }
    {
      Iterator i = arg.getConstrainingElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.Collaboration.constrainingElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Behavioral_Elements.Collaborations.Collaboration.constrainingElement");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getInteractions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.Collaboration.interaction", al);
        while(i.hasNext())
        {
          MInteraction el = (MInteraction)i.next();
          print(el, false, "Behavioral_Elements.Collaborations.Interaction");
        }
        dh.endElement("Behavioral_Elements.Collaborations.Collaboration.interaction");
      }
    }
    dh.endElement("Behavioral_Elements.Collaborations.Collaboration");
  }

  public void printCallEventMain(MCallEvent arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.CallEvent", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getStates().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.state", al);
        while(i.hasNext())
        {
          MState el = (MState)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.State");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.state");
      }
    }
    {
      Iterator i = arg.getTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.transition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.transition");
      }
    }
    if (null != arg.getOperation())
    {
      dh.startElement("Behavioral_Elements.State_Machines.CallEvent.operation", al);
      print(arg.getOperation(), true, "Foundation.Core.Operation");
      dh.endElement("Behavioral_Elements.State_Machines.CallEvent.operation");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.Event.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, false, "Foundation.Core.Parameter");
        }
        dh.endElement("Behavioral_Elements.State_Machines.Event.parameter");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.CallEvent");
  }

  public void printFlowMain(MFlow arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Flow", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getTargets().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Flow.target", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Flow.target");
      }
    }
    {
      Iterator i = arg.getSources().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Flow.source", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Flow.source");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Flow");
  }

  public void printDataValueMain(MDataValue arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.DataValue", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClassifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.classifier", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.classifier");
      }
    }
    {
      Iterator i = arg.getAttributeLinks().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.attributeLink");
      }
    }
    {
      Iterator i = arg.getLinkEnds().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd", al);
        while(i.hasNext())
        {
          MLinkEnd el = (MLinkEnd)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.LinkEnd");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.linkEnd");
      }
    }
    {
      Iterator i = arg.getStimuli1().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus1");
      }
    }
    {
      Iterator i = arg.getStimuli3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus3");
      }
    }
    if (null != arg.getComponentInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance", al);
      print(arg.getComponentInstance(), true, "Behavioral_Elements.Common_Behavior.ComponentInstance");
      dh.endElement("Behavioral_Elements.Common_Behavior.Instance.componentInstance");
    }
    {
      Iterator i = arg.getStimuli2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.stimulus2");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getSlots().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Instance.slot", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Instance.slot");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.DataValue");
  }

  public void printComponentMain(MComponent arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Component", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getDeploymentLocations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Component.deploymentLocation", al);
        while(i.hasNext())
        {
          MNode el = (MNode)i.next();
          print(el, true, "Foundation.Core.Node");
        }
        dh.endElement("Foundation.Core.Component.deploymentLocation");
      }
    }
    {
      Iterator i = arg.getResidentElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Component.residentElement", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.Component.residentElement");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Foundation.Core.Component");
  }

  public void printLinkEndMain(MLinkEnd arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.LinkEnd", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getInstance())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.LinkEnd.instance", al);
      print(arg.getInstance(), true, "Behavioral_Elements.Common_Behavior.Instance");
      dh.endElement("Behavioral_Elements.Common_Behavior.LinkEnd.instance");
    }
    if (null != arg.getLink())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.LinkEnd.link", al);
      print(arg.getLink(), true, "Behavioral_Elements.Common_Behavior.Link");
      dh.endElement("Behavioral_Elements.Common_Behavior.LinkEnd.link");
    }
    if (null != arg.getAssociationEnd())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.LinkEnd.associationEnd", al);
      print(arg.getAssociationEnd(), true, "Foundation.Core.AssociationEnd");
      dh.endElement("Behavioral_Elements.Common_Behavior.LinkEnd.associationEnd");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getQualifiedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.LinkEnd.qualifiedValue", al);
        while(i.hasNext())
        {
          MAttributeLink el = (MAttributeLink)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.AttributeLink");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.LinkEnd.qualifiedValue");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.LinkEnd");
  }

  public void printPartitionMain(MPartition arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Activity_Graphs.Partition", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getContents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Activity_Graphs.Partition.contents", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Behavioral_Elements.Activity_Graphs.Partition.contents");
      }
    }
    if (null != arg.getActivityGraph())
    {
      dh.startElement("Behavioral_Elements.Activity_Graphs.Partition.activityGraph", al);
      print(arg.getActivityGraph(), true, "Behavioral_Elements.Activity_Graphs.ActivityGraph");
      dh.endElement("Behavioral_Elements.Activity_Graphs.Partition.activityGraph");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.Activity_Graphs.Partition");
  }

  public void printAssociationEndMain(MAssociationEnd arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.AssociationEnd", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isNavigable()));    dh.startElement("Foundation.Core.AssociationEnd.isNavigable", al); al.clear();
    dh.endElement("Foundation.Core.AssociationEnd.isNavigable");
    if (null != arg.getOrdering())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getOrdering().getName());      dh.startElement("Foundation.Core.AssociationEnd.ordering", al); al.clear();
      dh.endElement("Foundation.Core.AssociationEnd.ordering");
    }
    if (null != arg.getAggregation())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getAggregation().getName());      dh.startElement("Foundation.Core.AssociationEnd.aggregation", al); al.clear();
      dh.endElement("Foundation.Core.AssociationEnd.aggregation");
    }
    if (null != arg.getTargetScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getTargetScope().getName());      dh.startElement("Foundation.Core.AssociationEnd.targetScope", al); al.clear();
      dh.endElement("Foundation.Core.AssociationEnd.targetScope");
    }
    if (null != arg.getMultiplicity())
    {
      dh.startElement("Foundation.Core.AssociationEnd.multiplicity", al);
      print(arg.getMultiplicity(), false, "Foundation.Data_Types.Multiplicity");
      dh.endElement("Foundation.Core.AssociationEnd.multiplicity");
    }
    if (null != arg.getChangeability())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getChangeability().getName());      dh.startElement("Foundation.Core.AssociationEnd.changeability", al); al.clear();
      dh.endElement("Foundation.Core.AssociationEnd.changeability");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getAssociation())
    {
      dh.startElement("Foundation.Core.AssociationEnd.association", al);
      print(arg.getAssociation(), true, "Foundation.Core.Association");
      dh.endElement("Foundation.Core.AssociationEnd.association");
    }
    if (null != arg.getType())
    {
      dh.startElement("Foundation.Core.AssociationEnd.type", al);
      print(arg.getType(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.AssociationEnd.type");
    }
    {
      Iterator i = arg.getSpecifications().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.AssociationEnd.specification", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Foundation.Core.AssociationEnd.specification");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getQualifiers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.AssociationEnd.qualifier", al);
        while(i.hasNext())
        {
          MAttribute el = (MAttribute)i.next();
          print(el, false, "Foundation.Core.Attribute");
        }
        dh.endElement("Foundation.Core.AssociationEnd.qualifier");
      }
    }
    dh.endElement("Foundation.Core.AssociationEnd");
  }

  public void printPermissionMain(MPermission arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Permission", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getClients().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.client", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.client");
      }
    }
    {
      Iterator i = arg.getSuppliers().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Dependency.supplier", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Dependency.supplier");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Permission");
  }

  public void printUseCaseMain(MUseCase arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Use_Cases.UseCase", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getExtends2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Use_Cases.UseCase.extend2", al);
        while(i.hasNext())
        {
          MExtend el = (MExtend)i.next();
          print(el, true, "Behavioral_Elements.Use_Cases.Extend");
        }
        dh.endElement("Behavioral_Elements.Use_Cases.UseCase.extend2");
      }
    }
    {
      Iterator i = arg.getExtends().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Use_Cases.UseCase.extend", al);
        while(i.hasNext())
        {
          MExtend el = (MExtend)i.next();
          print(el, true, "Behavioral_Elements.Use_Cases.Extend");
        }
        dh.endElement("Behavioral_Elements.Use_Cases.UseCase.extend");
      }
    }
    {
      Iterator i = arg.getIncludes().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Use_Cases.UseCase.include", al);
        while(i.hasNext())
        {
          MInclude el = (MInclude)i.next();
          print(el, true, "Behavioral_Elements.Use_Cases.Include");
        }
        dh.endElement("Behavioral_Elements.Use_Cases.UseCase.include");
      }
    }
    {
      Iterator i = arg.getIncludes2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Use_Cases.UseCase.include2", al);
        while(i.hasNext())
        {
          MInclude el = (MInclude)i.next();
          print(el, true, "Behavioral_Elements.Use_Cases.Include");
        }
        dh.endElement("Behavioral_Elements.Use_Cases.UseCase.include2");
      }
    }
    {
      Iterator i = arg.getExtensionPoints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Use_Cases.UseCase.extensionPoint", al);
        while(i.hasNext())
        {
          MExtensionPoint el = (MExtensionPoint)i.next();
          print(el, true, "Behavioral_Elements.Use_Cases.ExtensionPoint");
        }
        dh.endElement("Behavioral_Elements.Use_Cases.UseCase.extensionPoint");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Behavioral_Elements.Use_Cases.UseCase");
  }

  public void printActionSequenceMain(MActionSequence arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.ActionSequence", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getRecurrence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.recurrence", al);
      print(arg.getRecurrence(), false, "Foundation.Data_Types.IterationExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.recurrence");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.target", al);
      print(arg.getTarget(), false, "Foundation.Data_Types.ObjectSetExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.target");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAsynchronous()));    dh.startElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous");
    if (null != arg.getScript())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.script", al);
      print(arg.getScript(), false, "Foundation.Data_Types.ActionExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.script");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getActionSequence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.actionSequence", al);
      print(arg.getActionSequence(), true, "Behavioral_Elements.Common_Behavior.ActionSequence");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.actionSequence");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.stimulus");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getActualArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.actualArgument", al);
        while(i.hasNext())
        {
          MArgument el = (MArgument)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Argument");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.actualArgument");
      }
    }
    {
      Iterator i = arg.getActions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.ActionSequence.action", al);
        while(i.hasNext())
        {
          MAction el = (MAction)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Action");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.ActionSequence.action");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.ActionSequence");
  }

  public void printObjectSetExpressionMain(MObjectSetExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.ObjectSetExpression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.ObjectSetExpression");
  }

  public void printNamespaceMain(MNamespace arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Namespace", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    dh.endElement("Foundation.Core.Namespace");
  }

  public void printMethodMain(MMethod arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Method", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getOwnerScope())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getOwnerScope().getName());      dh.startElement("Foundation.Core.Feature.ownerScope", al); al.clear();
      dh.endElement("Foundation.Core.Feature.ownerScope");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isQuery()));    dh.startElement("Foundation.Core.BehavioralFeature.isQuery", al); al.clear();
    dh.endElement("Foundation.Core.BehavioralFeature.isQuery");
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Core.Method.body", al);
      print(arg.getBody(), false, "Foundation.Data_Types.ProcedureExpression");
      dh.endElement("Foundation.Core.Method.body");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getOwner())
    {
      dh.startElement("Foundation.Core.Feature.owner", al);
      print(arg.getOwner(), true, "Foundation.Core.Classifier");
      dh.endElement("Foundation.Core.Feature.owner");
    }
    if (null != arg.getSpecification())
    {
      dh.startElement("Foundation.Core.Method.specification", al);
      print(arg.getSpecification(), true, "Foundation.Core.Operation");
      dh.endElement("Foundation.Core.Method.specification");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.BehavioralFeature.parameter", al);
        while(i.hasNext())
        {
          MParameter el = (MParameter)i.next();
          print(el, false, "Foundation.Core.Parameter");
        }
        dh.endElement("Foundation.Core.BehavioralFeature.parameter");
      }
    }
    dh.endElement("Foundation.Core.Method");
  }

  public void printDestroyActionMain(MDestroyAction arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.DestroyAction", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getRecurrence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.recurrence", al);
      print(arg.getRecurrence(), false, "Foundation.Data_Types.IterationExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.recurrence");
    }
    if (null != arg.getTarget())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.target", al);
      print(arg.getTarget(), false, "Foundation.Data_Types.ObjectSetExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.target");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAsynchronous()));    dh.startElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous", al); al.clear();
    dh.endElement("Behavioral_Elements.Common_Behavior.Action.isAsynchronous");
    if (null != arg.getScript())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.script", al);
      print(arg.getScript(), false, "Foundation.Data_Types.ActionExpression");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.script");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getActionSequence())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Action.actionSequence", al);
      print(arg.getActionSequence(), true, "Behavioral_Elements.Common_Behavior.ActionSequence");
      dh.endElement("Behavioral_Elements.Common_Behavior.Action.actionSequence");
    }
    {
      Iterator i = arg.getStimuli().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.stimulus", al);
        while(i.hasNext())
        {
          MStimulus el = (MStimulus)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Stimulus");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.stimulus");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getActualArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Action.actualArgument", al);
        while(i.hasNext())
        {
          MArgument el = (MArgument)i.next();
          print(el, false, "Behavioral_Elements.Common_Behavior.Argument");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Action.actualArgument");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.DestroyAction");
  }

  public void printStimulusMain(MStimulus arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.Stimulus", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getArguments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Stimulus.argument", al);
        while(i.hasNext())
        {
          MInstance el = (MInstance)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Instance");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Stimulus.argument");
      }
    }
    if (null != arg.getSender())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Stimulus.sender", al);
      print(arg.getSender(), true, "Behavioral_Elements.Common_Behavior.Instance");
      dh.endElement("Behavioral_Elements.Common_Behavior.Stimulus.sender");
    }
    if (null != arg.getReceiver())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Stimulus.receiver", al);
      print(arg.getReceiver(), true, "Behavioral_Elements.Common_Behavior.Instance");
      dh.endElement("Behavioral_Elements.Common_Behavior.Stimulus.receiver");
    }
    if (null != arg.getCommunicationLink())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Stimulus.communicationLink", al);
      print(arg.getCommunicationLink(), true, "Behavioral_Elements.Common_Behavior.Link");
      dh.endElement("Behavioral_Elements.Common_Behavior.Stimulus.communicationLink");
    }
    if (null != arg.getDispatchAction())
    {
      dh.startElement("Behavioral_Elements.Common_Behavior.Stimulus.dispatchAction", al);
      print(arg.getDispatchAction(), true, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.Common_Behavior.Stimulus.dispatchAction");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.Stimulus");
  }

  public void printConstraintMain(MConstraint arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Constraint", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Core.Constraint.body", al);
      print(arg.getBody(), false, "Foundation.Data_Types.BooleanExpression");
      dh.endElement("Foundation.Core.Constraint.body");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getConstrainedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Constraint.constrainedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Constraint.constrainedElement");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.Constraint");
  }

  public void printAssociationMain(MAssociation arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Association", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getConnections().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Association.connection", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, false, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Association.connection");
      }
    }
    dh.endElement("Foundation.Core.Association");
  }

  public void printGeneralizableElementMain(MGeneralizableElement arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.GeneralizableElement", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Foundation.Core.GeneralizableElement");
  }

  public void printExceptionMain(MException arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Common_Behavior.Exception", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getReceptions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Signal.reception", al);
        while(i.hasNext())
        {
          MReception el = (MReception)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.Reception");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Signal.reception");
      }
    }
    {
      Iterator i = arg.getContexts().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Signal.context", al);
        while(i.hasNext())
        {
          MBehavioralFeature el = (MBehavioralFeature)i.next();
          print(el, true, "Foundation.Core.BehavioralFeature");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Signal.context");
      }
    }
    {
      Iterator i = arg.getSendActions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Common_Behavior.Signal.sendAction", al);
        while(i.hasNext())
        {
          MSendAction el = (MSendAction)i.next();
          print(el, true, "Behavioral_Elements.Common_Behavior.SendAction");
        }
        dh.endElement("Behavioral_Elements.Common_Behavior.Signal.sendAction");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Behavioral_Elements.Common_Behavior.Exception");
  }

  public void printPseudostateMain(MPseudostate arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.Pseudostate", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    if (null != arg.getKind())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getKind().getName());      dh.startElement("Behavioral_Elements.State_Machines.Pseudostate.kind", al); al.clear();
      dh.endElement("Behavioral_Elements.State_Machines.Pseudostate.kind");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.Pseudostate");
  }

  public void printElementImportMain(MElementImport arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Model_Management.ElementImport", al); al.clear();
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Model_Management.ElementImport.visibility", al); al.clear();
      dh.endElement("Model_Management.ElementImport.visibility");
    }
    if (null != arg.getAlias())
    {
      dh.startElement("Model_Management.ElementImport.alias", al);
      characters(arg.getAlias());
      dh.endElement("Model_Management.ElementImport.alias");
    }

    printXMIExtension(arg);
    if (null != arg.getModelElement())
    {
      dh.startElement("Model_Management.ElementImport.modelElement", al);
      print(arg.getModelElement(), true, "Foundation.Core.ModelElement");
      dh.endElement("Model_Management.ElementImport.modelElement");
    }
    if (null != arg.getPackage())
    {
      dh.startElement("Model_Management.ElementImport.package", al);
      print(arg.getPackage(), true, "Model_Management.Package");
      dh.endElement("Model_Management.ElementImport.package");
    }
    dh.endElement("Model_Management.ElementImport");
  }

  public void printSubmachineStateMain(MSubmachineState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.State_Machines.SubmachineState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    if (null != arg.getContainer())
    {
      dh.startElement("Behavioral_Elements.State_Machines.StateVertex.container", al);
      print(arg.getContainer(), true, "Behavioral_Elements.State_Machines.CompositeState");
      dh.endElement("Behavioral_Elements.State_Machines.StateVertex.container");
    }
    {
      Iterator i = arg.getOutgoings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.outgoing", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.outgoing");
      }
    }
    {
      Iterator i = arg.getIncomings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.StateVertex.incoming", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.StateVertex.incoming");
      }
    }
    if (null != arg.getStateMachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.stateMachine", al);
      print(arg.getStateMachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.State.stateMachine");
    }
    {
      Iterator i = arg.getDeferrableEvents().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.deferrableEvent", al);
        while(i.hasNext())
        {
          MEvent el = (MEvent)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.Event");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.deferrableEvent");
      }
    }
    if (null != arg.getSubmachine())
    {
      dh.startElement("Behavioral_Elements.State_Machines.SubmachineState.submachine", al);
      print(arg.getSubmachine(), true, "Behavioral_Elements.State_Machines.StateMachine");
      dh.endElement("Behavioral_Elements.State_Machines.SubmachineState.submachine");
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    if (null != arg.getEntry())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.entry", al);
      print(arg.getEntry(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.entry");
    }
    if (null != arg.getExit())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.exit", al);
      print(arg.getExit(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.exit");
    }
    {
      Iterator i = arg.getInternalTransitions().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.State.internalTransition", al);
        while(i.hasNext())
        {
          MTransition el = (MTransition)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.Transition");
        }
        dh.endElement("Behavioral_Elements.State_Machines.State.internalTransition");
      }
    }
    if (null != arg.getDoActivity())
    {
      dh.startElement("Behavioral_Elements.State_Machines.State.doActivity", al);
      print(arg.getDoActivity(), false, "Behavioral_Elements.Common_Behavior.Action");
      dh.endElement("Behavioral_Elements.State_Machines.State.doActivity");
    }
    {
      Iterator i = arg.getSubvertices().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.State_Machines.CompositeState.subvertex", al);
        while(i.hasNext())
        {
          MStateVertex el = (MStateVertex)i.next();
          print(el, false, "Behavioral_Elements.State_Machines.StateVertex");
        }
        dh.endElement("Behavioral_Elements.State_Machines.CompositeState.subvertex");
      }
    }
    dh.endElement("Behavioral_Elements.State_Machines.SubmachineState");
  }

  public void printExpressionMain(MExpression arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Data_Types.Expression", al); al.clear();
    if (null != arg.getLanguage())
    {
      dh.startElement("Foundation.Data_Types.Expression.language", al);
      characters(arg.getLanguage());
      dh.endElement("Foundation.Data_Types.Expression.language");
    }
    if (null != arg.getBody())
    {
      dh.startElement("Foundation.Data_Types.Expression.body", al);
      characters(arg.getBody());
      dh.endElement("Foundation.Data_Types.Expression.body");
    }
    dh.endElement("Foundation.Data_Types.Expression");
  }

  public void printPresentationElementMain(MPresentationElement arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.PresentationElement", al); al.clear();

    printXMIExtension(arg);
    {
      Iterator i = arg.getSubjects().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.PresentationElement.subject", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.PresentationElement.subject");
      }
    }
    dh.endElement("Foundation.Core.PresentationElement");
  }

  public void printClassifierInStateMain(MClassifierInState arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Activity_Graphs.ClassifierInState", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    if (null != arg.getType())
    {
      dh.startElement("Behavioral_Elements.Activity_Graphs.ClassifierInState.type", al);
      print(arg.getType(), true, "Foundation.Core.Classifier");
      dh.endElement("Behavioral_Elements.Activity_Graphs.ClassifierInState.type");
    }
    {
      Iterator i = arg.getInStates().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Activity_Graphs.ClassifierInState.inState", al);
        while(i.hasNext())
        {
          MState el = (MState)i.next();
          print(el, true, "Behavioral_Elements.State_Machines.State");
        }
        dh.endElement("Behavioral_Elements.Activity_Graphs.ClassifierInState.inState");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Behavioral_Elements.Activity_Graphs.ClassifierInState");
  }

  public void printElementMain(MElement arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Core.Element", al); al.clear();

    printXMIExtension(arg);
    dh.endElement("Foundation.Core.Element");
  }

  public void printClassifierRoleMain(MClassifierRole arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Behavioral_Elements.Collaborations.ClassifierRole", al); al.clear();
    if (null != arg.getName())
    {
      dh.startElement("Foundation.Core.ModelElement.name", al);
      characters(arg.getName());
      dh.endElement("Foundation.Core.ModelElement.name");
    }
    if (null != arg.getVisibility())
    {
      al.addAttribute("xmi.value", CDATA_TYPE, arg.getVisibility().getName());      dh.startElement("Foundation.Core.ModelElement.visibility", al); al.clear();
      dh.endElement("Foundation.Core.ModelElement.visibility");
    }
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isSpecification()));    dh.startElement("Foundation.Core.ModelElement.isSpecification", al); al.clear();
    dh.endElement("Foundation.Core.ModelElement.isSpecification");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isRoot()));    dh.startElement("Foundation.Core.GeneralizableElement.isRoot", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isRoot");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isLeaf()));    dh.startElement("Foundation.Core.GeneralizableElement.isLeaf", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isLeaf");
    al.addAttribute("xmi.value", CDATA_TYPE, convertBooleanXMI(arg.isAbstract()));    dh.startElement("Foundation.Core.GeneralizableElement.isAbstract", al); al.clear();
    dh.endElement("Foundation.Core.GeneralizableElement.isAbstract");
    if (null != arg.getMultiplicity())
    {
      dh.startElement("Behavioral_Elements.Collaborations.ClassifierRole.multiplicity", al);
      print(arg.getMultiplicity(), false, "Foundation.Data_Types.Multiplicity");
      dh.endElement("Behavioral_Elements.Collaborations.ClassifierRole.multiplicity");
    }

    printXMIExtension(arg);
    if (null != arg.getNamespace())
    {
      dh.startElement("Foundation.Core.ModelElement.namespace", al);
      print(arg.getNamespace(), true, "Foundation.Core.Namespace");
      dh.endElement("Foundation.Core.ModelElement.namespace");
    }
    {
      Iterator i = arg.getClientDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.clientDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.clientDependency");
      }
    }
    {
      Iterator i = arg.getConstraints().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.constraint", al);
        while(i.hasNext())
        {
          MConstraint el = (MConstraint)i.next();
          print(el, true, "Foundation.Core.Constraint");
        }
        dh.endElement("Foundation.Core.ModelElement.constraint");
      }
    }
    {
      Iterator i = arg.getSupplierDependencies().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.supplierDependency", al);
        while(i.hasNext())
        {
          MDependency el = (MDependency)i.next();
          print(el, true, "Foundation.Core.Dependency");
        }
        dh.endElement("Foundation.Core.ModelElement.supplierDependency");
      }
    }
    {
      Iterator i = arg.getPresentations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.presentation", al);
        while(i.hasNext())
        {
          MPresentationElement el = (MPresentationElement)i.next();
          print(el, true, "Foundation.Core.PresentationElement");
        }
        dh.endElement("Foundation.Core.ModelElement.presentation");
      }
    }
    {
      Iterator i = arg.getTargetFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.targetFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.targetFlow");
      }
    }
    {
      Iterator i = arg.getSourceFlows().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.sourceFlow", al);
        while(i.hasNext())
        {
          MFlow el = (MFlow)i.next();
          print(el, true, "Foundation.Core.Flow");
        }
        dh.endElement("Foundation.Core.ModelElement.sourceFlow");
      }
    }
    {
      Iterator i = arg.getTemplateParameters3().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter3", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter3");
      }
    }
    {
      Iterator i = arg.getBindings().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.binding", al);
        while(i.hasNext())
        {
          MBinding el = (MBinding)i.next();
          print(el, true, "Foundation.Core.Binding");
        }
        dh.endElement("Foundation.Core.ModelElement.binding");
      }
    }
    {
      Iterator i = arg.getComments().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.comment", al);
        while(i.hasNext())
        {
          MComment el = (MComment)i.next();
          print(el, true, "Foundation.Core.Comment");
        }
        dh.endElement("Foundation.Core.ModelElement.comment");
      }
    }
    {
      Iterator i = arg.getElementResidences().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementResidence", al);
        while(i.hasNext())
        {
          MElementResidence el = (MElementResidence)i.next();
          print(el, true, "Foundation.Core.ElementResidence");
        }
        dh.endElement("Foundation.Core.ModelElement.elementResidence");
      }
    }
    {
      Iterator i = arg.getTemplateParameters2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter2", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, true, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter2");
      }
    }
    {
      Iterator i = arg.getElementImports2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.elementImport", al);
        while(i.hasNext())
        {
          MElementImport el = (MElementImport)i.next();
          print(el, true, "Model_Management.ElementImport");
        }
        dh.endElement("Foundation.Core.ModelElement.elementImport");
      }
    }
    {
      Iterator i = arg.getGeneralizations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.generalization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.generalization");
      }
    }
    {
      Iterator i = arg.getSpecializations().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.GeneralizableElement.specialization", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.GeneralizableElement.specialization");
      }
    }
    {
      Iterator i = arg.getParticipants().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.participant", al);
        while(i.hasNext())
        {
          MAssociationEnd el = (MAssociationEnd)i.next();
          print(el, true, "Foundation.Core.AssociationEnd");
        }
        dh.endElement("Foundation.Core.Classifier.participant");
      }
    }
    {
      Iterator i = arg.getPowertypeRanges().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.powertypeRange", al);
        while(i.hasNext())
        {
          MGeneralization el = (MGeneralization)i.next();
          print(el, true, "Foundation.Core.Generalization");
        }
        dh.endElement("Foundation.Core.Classifier.powertypeRange");
      }
    }
    {
      Iterator i = arg.getBases().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.ClassifierRole.base", al);
        while(i.hasNext())
        {
          MClassifier el = (MClassifier)i.next();
          print(el, true, "Foundation.Core.Classifier");
        }
        dh.endElement("Behavioral_Elements.Collaborations.ClassifierRole.base");
      }
    }
    {
      Iterator i = arg.getAvailableFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.ClassifierRole.availableFeature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, true, "Foundation.Core.Feature");
        }
        dh.endElement("Behavioral_Elements.Collaborations.ClassifierRole.availableFeature");
      }
    }
    {
      Iterator i = arg.getMessages2().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.ClassifierRole.message2", al);
        while(i.hasNext())
        {
          MMessage el = (MMessage)i.next();
          print(el, true, "Behavioral_Elements.Collaborations.Message");
        }
        dh.endElement("Behavioral_Elements.Collaborations.ClassifierRole.message2");
      }
    }
    {
      Iterator i = arg.getMessages1().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.ClassifierRole.message1", al);
        while(i.hasNext())
        {
          MMessage el = (MMessage)i.next();
          print(el, true, "Behavioral_Elements.Collaborations.Message");
        }
        dh.endElement("Behavioral_Elements.Collaborations.ClassifierRole.message1");
      }
    }
    {
      Iterator i = arg.getAvailableContentses().iterator();
      if (i.hasNext())
      {
        dh.startElement("Behavioral_Elements.Collaborations.ClassifierRole.availableContents", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, true, "Foundation.Core.ModelElement");
        }
        dh.endElement("Behavioral_Elements.Collaborations.ClassifierRole.availableContents");
      }
    }
    {
      Iterator i = arg.getTemplateParameters().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.templateParameter", al);
        while(i.hasNext())
        {
          MTemplateParameter el = (MTemplateParameter)i.next();
          print(el, false, "Foundation.Core.TemplateParameter");
        }
        dh.endElement("Foundation.Core.ModelElement.templateParameter");
      }
    }
    {
      Iterator i = arg.getTaggedValues().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.ModelElement.taggedValue", al);
        while(i.hasNext())
        {
          MTaggedValue el = (MTaggedValue)i.next();
          print(el, false, "Foundation.Extension_Mechanisms.TaggedValue");
        }
        dh.endElement("Foundation.Core.ModelElement.taggedValue");
      }
    }
    {
      Iterator i = arg.getOwnedElements().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Namespace.ownedElement", al);
        while(i.hasNext())
        {
          MModelElement el = (MModelElement)i.next();
          print(el, false, "Foundation.Core.ModelElement");
        }
        dh.endElement("Foundation.Core.Namespace.ownedElement");
      }
    }
    {
      Iterator i = arg.getFeatures().iterator();
      if (i.hasNext())
      {
        dh.startElement("Foundation.Core.Classifier.feature", al);
        while(i.hasNext())
        {
          MFeature el = (MFeature)i.next();
          print(el, false, "Foundation.Core.Feature");
        }
        dh.endElement("Foundation.Core.Classifier.feature");
      }
    }
    dh.endElement("Behavioral_Elements.Collaborations.ClassifierRole");
  }

  public void printTaggedValueMain(MTaggedValue arg) throws SAXException
  {
    if (null == arg)
    {
      return;
    }

    processElement(arg);

    al.addAttribute("xmi.id", CDATA_TYPE, getXMIID(arg));
    addXMIUUID(arg, al);
    dh.startElement("Foundation.Extension_Mechanisms.TaggedValue", al); al.clear();
    if (null != arg.getTag())
    {
      dh.startElement("Foundation.Extension_Mechanisms.TaggedValue.tag", al);
      characters(arg.getTag());
      dh.endElement("Foundation.Extension_Mechanisms.TaggedValue.tag");
    }
    if (null != arg.getValue())
    {
      dh.startElement("Foundation.Extension_Mechanisms.TaggedValue.value", al);
      characters(arg.getValue());
      dh.endElement("Foundation.Extension_Mechanisms.TaggedValue.value");
    }

    printXMIExtension(arg);
    if (null != arg.getStereotype())
    {
      dh.startElement("Foundation.Extension_Mechanisms.TaggedValue.stereotype", al);
      print(arg.getStereotype(), true, "Foundation.Extension_Mechanisms.Stereotype");
      dh.endElement("Foundation.Extension_Mechanisms.TaggedValue.stereotype");
    }
    if (null != arg.getModelElement())
    {
      dh.startElement("Foundation.Extension_Mechanisms.TaggedValue.modelElement", al);
      print(arg.getModelElement(), true, "Foundation.Core.ModelElement");
      dh.endElement("Foundation.Extension_Mechanisms.TaggedValue.modelElement");
    }
    dh.endElement("Foundation.Extension_Mechanisms.TaggedValue");
  }


  public void print(Object arg, boolean ref, String roleTypeXMIName) throws SAXException
  {
    ref = ref || isElementProcessed(arg);

    if ((arg != getModel()) && (arg instanceof MBase) && (null == ((MBase)arg).getModelElementContainer()))
    {
      notContained.put(arg, arg);
    }

    if (ref)
    {
      al.addAttribute("xmi.idref", CDATA_TYPE, getXMIID(arg));
      dh.startElement(roleTypeXMIName, al); al.clear();
      dh.endElement(roleTypeXMIName);

      return;
    }

    if (arg instanceof MCallAction)
    {
      printCallActionMain((MCallAction)arg);
      return;
    }

    if (arg instanceof MTerminateAction)
    {
      printTerminateActionMain((MTerminateAction)arg);
      return;
    }

    if (arg instanceof MComment)
    {
      printCommentMain((MComment)arg);
      return;
    }

    if (arg instanceof MDataType)
    {
      printDataTypeMain((MDataType)arg);
      return;
    }

    if (arg instanceof MDestroyAction)
    {
      printDestroyActionMain((MDestroyAction)arg);
      return;
    }

    if (arg instanceof MArgListsExpression)
    {
      printArgListsExpressionMain((MArgListsExpression)arg);
      return;
    }

    if (arg instanceof MExtend)
    {
      printExtendMain((MExtend)arg);
      return;
    }

    if (arg instanceof MUsage)
    {
      printUsageMain((MUsage)arg);
      return;
    }

    if (arg instanceof MPermission)
    {
      printPermissionMain((MPermission)arg);
      return;
    }

    if (arg instanceof MActionSequence)
    {
      printActionSequenceMain((MActionSequence)arg);
      return;
    }

    if (arg instanceof MCollaboration)
    {
      printCollaborationMain((MCollaboration)arg);
      return;
    }

    if (arg instanceof MAssociationRole)
    {
      printAssociationRoleMain((MAssociationRole)arg);
      return;
    }

    if (arg instanceof MAttribute)
    {
      printAttributeMain((MAttribute)arg);
      return;
    }

    if (arg instanceof MMessage)
    {
      printMessageMain((MMessage)arg);
      return;
    }

    if (arg instanceof MBooleanExpression)
    {
      printBooleanExpressionMain((MBooleanExpression)arg);
      return;
    }

    if (arg instanceof MPseudostate)
    {
      printPseudostateMain((MPseudostate)arg);
      return;
    }

    if (arg instanceof MTypeExpression)
    {
      printTypeExpressionMain((MTypeExpression)arg);
      return;
    }

    if (arg instanceof MClassifierInState)
    {
      printClassifierInStateMain((MClassifierInState)arg);
      return;
    }

    if (arg instanceof MStereotype)
    {
      printStereotypeMain((MStereotype)arg);
      return;
    }

    if (arg instanceof MSubsystem)
    {
      printSubsystemMain((MSubsystem)arg);
      return;
    }

    if (arg instanceof MMappingExpression)
    {
      printMappingExpressionMain((MMappingExpression)arg);
      return;
    }

    if (arg instanceof MUseCase)
    {
      printUseCaseMain((MUseCase)arg);
      return;
    }

    if (arg instanceof MModel)
    {
      printModelMain((MModel)arg);
      return;
    }

    if (arg instanceof MLinkObject)
    {
      printLinkObjectMain((MLinkObject)arg);
      return;
    }

    if (arg instanceof MLinkEnd)
    {
      printLinkEndMain((MLinkEnd)arg);
      return;
    }

    if (arg instanceof MTemplateParameter)
    {
      printTemplateParameterMain((MTemplateParameter)arg);
      return;
    }

    if (arg instanceof MCreateAction)
    {
      printCreateActionMain((MCreateAction)arg);
      return;
    }

    if (arg instanceof MPartition)
    {
      printPartitionMain((MPartition)arg);
      return;
    }

    if (arg instanceof MAssociationEndRole)
    {
      printAssociationEndRoleMain((MAssociationEndRole)arg);
      return;
    }

    if (arg instanceof MAttributeLink)
    {
      printAttributeLinkMain((MAttributeLink)arg);
      return;
    }

    if (arg instanceof MPresentationElement)
    {
      printPresentationElementMain((MPresentationElement)arg);
      return;
    }

    if (arg instanceof MSendAction)
    {
      printSendActionMain((MSendAction)arg);
      return;
    }

    if (arg instanceof MCallEvent)
    {
      printCallEventMain((MCallEvent)arg);
      return;
    }

    if (arg instanceof MUninterpretedAction)
    {
      printUninterpretedActionMain((MUninterpretedAction)arg);
      return;
    }

    if (arg instanceof MSubactivityState)
    {
      printSubactivityStateMain((MSubactivityState)arg);
      return;
    }

    if (arg instanceof MTransition)
    {
      printTransitionMain((MTransition)arg);
      return;
    }

    if (arg instanceof MOperation)
    {
      printOperationMain((MOperation)arg);
      return;
    }

    if (arg instanceof MClassifierRole)
    {
      printClassifierRoleMain((MClassifierRole)arg);
      return;
    }

    if (arg instanceof MTimeExpression)
    {
      printTimeExpressionMain((MTimeExpression)arg);
      return;
    }

    if (arg instanceof MObjectFlowState)
    {
      printObjectFlowStateMain((MObjectFlowState)arg);
      return;
    }

    if (arg instanceof MStimulus)
    {
      printStimulusMain((MStimulus)arg);
      return;
    }

    if (arg instanceof MSynchState)
    {
      printSynchStateMain((MSynchState)arg);
      return;
    }

    if (arg instanceof MInclude)
    {
      printIncludeMain((MInclude)arg);
      return;
    }

    if (arg instanceof MArgument)
    {
      printArgumentMain((MArgument)arg);
      return;
    }

    if (arg instanceof MComponent)
    {
      printComponentMain((MComponent)arg);
      return;
    }

    if (arg instanceof MAssociationClass)
    {
      printAssociationClassMain((MAssociationClass)arg);
      return;
    }

    if (arg instanceof MActor)
    {
      printActorMain((MActor)arg);
      return;
    }

    if (arg instanceof MSignalEvent)
    {
      printSignalEventMain((MSignalEvent)arg);
      return;
    }

    if (arg instanceof MMethod)
    {
      printMethodMain((MMethod)arg);
      return;
    }

    if (arg instanceof MChangeEvent)
    {
      printChangeEventMain((MChangeEvent)arg);
      return;
    }

    if (arg instanceof MParameter)
    {
      printParameterMain((MParameter)arg);
      return;
    }

    if (arg instanceof MTaggedValue)
    {
      printTaggedValueMain((MTaggedValue)arg);
      return;
    }

    if (arg instanceof MTimeEvent)
    {
      printTimeEventMain((MTimeEvent)arg);
      return;
    }

    if (arg instanceof MUseCaseInstance)
    {
      printUseCaseInstanceMain((MUseCaseInstance)arg);
      return;
    }

    if (arg instanceof MStubState)
    {
      printStubStateMain((MStubState)arg);
      return;
    }

    if (arg instanceof MNode)
    {
      printNodeMain((MNode)arg);
      return;
    }

    if (arg instanceof MNodeInstance)
    {
      printNodeInstanceMain((MNodeInstance)arg);
      return;
    }

    if (arg instanceof MElementResidence)
    {
      printElementResidenceMain((MElementResidence)arg);
      return;
    }

    if (arg instanceof MGeneralization)
    {
      printGeneralizationMain((MGeneralization)arg);
      return;
    }

    if (arg instanceof MReturnAction)
    {
      printReturnActionMain((MReturnAction)arg);
      return;
    }

    if (arg instanceof MException)
    {
      printExceptionMain((MException)arg);
      return;
    }

    if (arg instanceof MAbstraction)
    {
      printAbstractionMain((MAbstraction)arg);
      return;
    }

    if (arg instanceof MConstraint)
    {
      printConstraintMain((MConstraint)arg);
      return;
    }

    if (arg instanceof MIterationExpression)
    {
      printIterationExpressionMain((MIterationExpression)arg);
      return;
    }

    if (arg instanceof MObjectSetExpression)
    {
      printObjectSetExpressionMain((MObjectSetExpression)arg);
      return;
    }

    if (arg instanceof MReception)
    {
      printReceptionMain((MReception)arg);
      return;
    }

    if (arg instanceof MGuard)
    {
      printGuardMain((MGuard)arg);
      return;
    }

    if (arg instanceof MComponentInstance)
    {
      printComponentInstanceMain((MComponentInstance)arg);
      return;
    }

    if (arg instanceof MBinding)
    {
      printBindingMain((MBinding)arg);
      return;
    }

    if (arg instanceof MFlow)
    {
      printFlowMain((MFlow)arg);
      return;
    }

    if (arg instanceof MElementImport)
    {
      printElementImportMain((MElementImport)arg);
      return;
    }

    if (arg instanceof MProcedureExpression)
    {
      printProcedureExpressionMain((MProcedureExpression)arg);
      return;
    }

    if (arg instanceof MMultiplicityRange)
    {
      printMultiplicityRangeMain((MMultiplicityRange)arg);
      return;
    }

    if (arg instanceof MActivityGraph)
    {
      printActivityGraphMain((MActivityGraph)arg);
      return;
    }

    if (arg instanceof MExtensionPoint)
    {
      printExtensionPointMain((MExtensionPoint)arg);
      return;
    }

    if (arg instanceof MFinalState)
    {
      printFinalStateMain((MFinalState)arg);
      return;
    }

    if (arg instanceof MActionExpression)
    {
      printActionExpressionMain((MActionExpression)arg);
      return;
    }

    if (arg instanceof MCallState)
    {
      printCallStateMain((MCallState)arg);
      return;
    }

    if (arg instanceof MDataValue)
    {
      printDataValueMain((MDataValue)arg);
      return;
    }

    if (arg instanceof MInteraction)
    {
      printInteractionMain((MInteraction)arg);
      return;
    }

    if (arg instanceof MInterface)
    {
      printInterfaceMain((MInterface)arg);
      return;
    }

    if (arg instanceof MMultiplicity)
    {
      printMultiplicityMain((MMultiplicity)arg);
      return;
    }

    if (arg instanceof MSignal)
    {
      printSignalMain((MSignal)arg);
      return;
    }

    if (arg instanceof MActionState)
    {
      printActionStateMain((MActionState)arg);
      return;
    }

    if (arg instanceof MClass)
    {
      printClassMain((MClass)arg);
      return;
    }

    if (arg instanceof MAssociation)
    {
      printAssociationMain((MAssociation)arg);
      return;
    }

    if (arg instanceof MPackage)
    {
      printPackageMain((MPackage)arg);
      return;
    }

    if (arg instanceof MEvent)
    {
      printEventMain((MEvent)arg);
      return;
    }

    if (arg instanceof MAssociationEnd)
    {
      printAssociationEndMain((MAssociationEnd)arg);
      return;
    }

    if (arg instanceof MDependency)
    {
      printDependencyMain((MDependency)arg);
      return;
    }

    if (arg instanceof MStructuralFeature)
    {
      printStructuralFeatureMain((MStructuralFeature)arg);
      return;
    }

    if (arg instanceof MObject)
    {
      printObjectMain((MObject)arg);
      return;
    }

    if (arg instanceof MBehavioralFeature)
    {
      printBehavioralFeatureMain((MBehavioralFeature)arg);
      return;
    }

    if (arg instanceof MStateMachine)
    {
      printStateMachineMain((MStateMachine)arg);
      return;
    }

    if (arg instanceof MSubmachineState)
    {
      printSubmachineStateMain((MSubmachineState)arg);
      return;
    }

    if (arg instanceof MLink)
    {
      printLinkMain((MLink)arg);
      return;
    }

    if (arg instanceof MAction)
    {
      printActionMain((MAction)arg);
      return;
    }

    if (arg instanceof MExpression)
    {
      printExpressionMain((MExpression)arg);
      return;
    }

    if (arg instanceof MCompositeState)
    {
      printCompositeStateMain((MCompositeState)arg);
      return;
    }

    if (arg instanceof MInstance)
    {
      printInstanceMain((MInstance)arg);
      return;
    }

    if (arg instanceof MClassifier)
    {
      printClassifierMain((MClassifier)arg);
      return;
    }

    if (arg instanceof MSimpleState)
    {
      printSimpleStateMain((MSimpleState)arg);
      return;
    }

    if (arg instanceof MRelationship)
    {
      printRelationshipMain((MRelationship)arg);
      return;
    }

    if (arg instanceof MFeature)
    {
      printFeatureMain((MFeature)arg);
      return;
    }

    if (arg instanceof MGeneralizableElement)
    {
      printGeneralizableElementMain((MGeneralizableElement)arg);
      return;
    }

    if (arg instanceof MState)
    {
      printStateMain((MState)arg);
      return;
    }

    if (arg instanceof MNamespace)
    {
      printNamespaceMain((MNamespace)arg);
      return;
    }

    if (arg instanceof MStateVertex)
    {
      printStateVertexMain((MStateVertex)arg);
      return;
    }

    if (arg instanceof MModelElement)
    {
      printModelElementMain((MModelElement)arg);
      return;
    }

    if (arg instanceof MElement)
    {
      printElementMain((MElement)arg);
      return;
    }

  }

  public void preprocessIDs(MBase arg)
  {
    if (null == arg)
    {
      return;
    }

    if (arg instanceof MCallAction)
    {
      MCallAction arg2 = (MCallAction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActualArguments().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MTerminateAction)
    {
      MTerminateAction arg2 = (MTerminateAction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActualArguments().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MComment)
    {
      MComment arg2 = (MComment)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MDataType)
    {
      MDataType arg2 = (MDataType)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MDestroyAction)
    {
      MDestroyAction arg2 = (MDestroyAction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActualArguments().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MExtend)
    {
      MExtend arg2 = (MExtend)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MUsage)
    {
      MUsage arg2 = (MUsage)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MPermission)
    {
      MPermission arg2 = (MPermission)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MActionSequence)
    {
      MActionSequence arg2 = (MActionSequence)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActualArguments().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MCollaboration)
    {
      MCollaboration arg2 = (MCollaboration)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getInteractions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MAssociationRole)
    {
      MAssociationRole arg2 = (MAssociationRole)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getConnections().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MAttribute)
    {
      MAttribute arg2 = (MAttribute)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MMessage)
    {
      MMessage arg2 = (MMessage)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MPseudostate)
    {
      MPseudostate arg2 = (MPseudostate)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MClassifierInState)
    {
      MClassifierInState arg2 = (MClassifierInState)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MStereotype)
    {
      MStereotype arg2 = (MStereotype)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getStereotypeConstraints().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getRequiredTags().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MSubsystem)
    {
      MSubsystem arg2 = (MSubsystem)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MUseCase)
    {
      MUseCase arg2 = (MUseCase)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MModel)
    {
      MModel arg2 = (MModel)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MLinkObject)
    {
      MLinkObject arg2 = (MLinkObject)arg;
      {
        Iterator i = arg2.getConnections().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getSlots().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MLinkEnd)
    {
      MLinkEnd arg2 = (MLinkEnd)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getQualifiedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MTemplateParameter)
    {
      MTemplateParameter arg2 = (MTemplateParameter)arg;
    }
    else if (arg instanceof MCreateAction)
    {
      MCreateAction arg2 = (MCreateAction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActualArguments().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MPartition)
    {
      MPartition arg2 = (MPartition)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MAssociationEndRole)
    {
      MAssociationEndRole arg2 = (MAssociationEndRole)arg;
      {
        Iterator i = arg2.getQualifiers().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MAttributeLink)
    {
      MAttributeLink arg2 = (MAttributeLink)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MPresentationElement)
    {
      MPresentationElement arg2 = (MPresentationElement)arg;
    }
    else if (arg instanceof MSendAction)
    {
      MSendAction arg2 = (MSendAction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActualArguments().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MCallEvent)
    {
      MCallEvent arg2 = (MCallEvent)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MUninterpretedAction)
    {
      MUninterpretedAction arg2 = (MUninterpretedAction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActualArguments().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MSubactivityState)
    {
      MSubactivityState arg2 = (MSubactivityState)arg;
      {
        Iterator i = arg2.getSubvertices().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getDoActivity());
      {
        Iterator i = arg2.getInternalTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getExit());
      preprocessIDs(arg2.getEntry());
    }
    else if (arg instanceof MTransition)
    {
      MTransition arg2 = (MTransition)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getEffect());
      preprocessIDs(arg2.getGuard());
    }
    else if (arg instanceof MOperation)
    {
      MOperation arg2 = (MOperation)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MClassifierRole)
    {
      MClassifierRole arg2 = (MClassifierRole)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MObjectFlowState)
    {
      MObjectFlowState arg2 = (MObjectFlowState)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getDoActivity());
      {
        Iterator i = arg2.getInternalTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getExit());
      preprocessIDs(arg2.getEntry());
    }
    else if (arg instanceof MStimulus)
    {
      MStimulus arg2 = (MStimulus)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MSynchState)
    {
      MSynchState arg2 = (MSynchState)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MInclude)
    {
      MInclude arg2 = (MInclude)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MArgument)
    {
      MArgument arg2 = (MArgument)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MComponent)
    {
      MComponent arg2 = (MComponent)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MAssociationClass)
    {
      MAssociationClass arg2 = (MAssociationClass)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getConnections().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MActor)
    {
      MActor arg2 = (MActor)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MSignalEvent)
    {
      MSignalEvent arg2 = (MSignalEvent)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MMethod)
    {
      MMethod arg2 = (MMethod)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MChangeEvent)
    {
      MChangeEvent arg2 = (MChangeEvent)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MParameter)
    {
      MParameter arg2 = (MParameter)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MTaggedValue)
    {
      MTaggedValue arg2 = (MTaggedValue)arg;
    }
    else if (arg instanceof MTimeEvent)
    {
      MTimeEvent arg2 = (MTimeEvent)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MUseCaseInstance)
    {
      MUseCaseInstance arg2 = (MUseCaseInstance)arg;
      {
        Iterator i = arg2.getSlots().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MStubState)
    {
      MStubState arg2 = (MStubState)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MNode)
    {
      MNode arg2 = (MNode)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MNodeInstance)
    {
      MNodeInstance arg2 = (MNodeInstance)arg;
      {
        Iterator i = arg2.getSlots().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MElementResidence)
    {
      MElementResidence arg2 = (MElementResidence)arg;
    }
    else if (arg instanceof MGeneralization)
    {
      MGeneralization arg2 = (MGeneralization)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MReturnAction)
    {
      MReturnAction arg2 = (MReturnAction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActualArguments().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MException)
    {
      MException arg2 = (MException)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MAbstraction)
    {
      MAbstraction arg2 = (MAbstraction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MConstraint)
    {
      MConstraint arg2 = (MConstraint)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MReception)
    {
      MReception arg2 = (MReception)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MGuard)
    {
      MGuard arg2 = (MGuard)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MComponentInstance)
    {
      MComponentInstance arg2 = (MComponentInstance)arg;
      {
        Iterator i = arg2.getSlots().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MBinding)
    {
      MBinding arg2 = (MBinding)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MFlow)
    {
      MFlow arg2 = (MFlow)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MElementImport)
    {
      MElementImport arg2 = (MElementImport)arg;
    }
    else if (arg instanceof MActivityGraph)
    {
      MActivityGraph arg2 = (MActivityGraph)arg;
      {
        Iterator i = arg2.getTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getTop());
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getPartitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MExtensionPoint)
    {
      MExtensionPoint arg2 = (MExtensionPoint)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MFinalState)
    {
      MFinalState arg2 = (MFinalState)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getDoActivity());
      {
        Iterator i = arg2.getInternalTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getExit());
      preprocessIDs(arg2.getEntry());
    }
    else if (arg instanceof MCallState)
    {
      MCallState arg2 = (MCallState)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getDoActivity());
      {
        Iterator i = arg2.getInternalTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getExit());
      preprocessIDs(arg2.getEntry());
    }
    else if (arg instanceof MDataValue)
    {
      MDataValue arg2 = (MDataValue)arg;
      {
        Iterator i = arg2.getSlots().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MInteraction)
    {
      MInteraction arg2 = (MInteraction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getMessages().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MInterface)
    {
      MInterface arg2 = (MInterface)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MSignal)
    {
      MSignal arg2 = (MSignal)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MActionState)
    {
      MActionState arg2 = (MActionState)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getDoActivity());
      {
        Iterator i = arg2.getInternalTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getExit());
      preprocessIDs(arg2.getEntry());
    }
    else if (arg instanceof MClass)
    {
      MClass arg2 = (MClass)arg;
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MAssociation)
    {
      MAssociation arg2 = (MAssociation)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getConnections().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MPackage)
    {
      MPackage arg2 = (MPackage)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MEvent)
    {
      MEvent arg2 = (MEvent)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MAssociationEnd)
    {
      MAssociationEnd arg2 = (MAssociationEnd)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getQualifiers().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MDependency)
    {
      MDependency arg2 = (MDependency)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MStructuralFeature)
    {
      MStructuralFeature arg2 = (MStructuralFeature)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MObject)
    {
      MObject arg2 = (MObject)arg;
      {
        Iterator i = arg2.getSlots().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MBehavioralFeature)
    {
      MBehavioralFeature arg2 = (MBehavioralFeature)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MStateMachine)
    {
      MStateMachine arg2 = (MStateMachine)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getTop());
    }
    else if (arg instanceof MSubmachineState)
    {
      MSubmachineState arg2 = (MSubmachineState)arg;
      {
        Iterator i = arg2.getSubvertices().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getDoActivity());
      {
        Iterator i = arg2.getInternalTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getExit());
      preprocessIDs(arg2.getEntry());
    }
    else if (arg instanceof MLink)
    {
      MLink arg2 = (MLink)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getConnections().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MAction)
    {
      MAction arg2 = (MAction)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getActualArguments().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MCompositeState)
    {
      MCompositeState arg2 = (MCompositeState)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getDoActivity());
      {
        Iterator i = arg2.getInternalTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getExit());
      preprocessIDs(arg2.getEntry());
      {
        Iterator i = arg2.getSubvertices().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MInstance)
    {
      MInstance arg2 = (MInstance)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getSlots().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MClassifier)
    {
      MClassifier arg2 = (MClassifier)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getFeatures().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MSimpleState)
    {
      MSimpleState arg2 = (MSimpleState)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getDoActivity());
      {
        Iterator i = arg2.getInternalTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getExit());
      preprocessIDs(arg2.getEntry());
    }
    else if (arg instanceof MRelationship)
    {
      MRelationship arg2 = (MRelationship)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MFeature)
    {
      MFeature arg2 = (MFeature)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MGeneralizableElement)
    {
      MGeneralizableElement arg2 = (MGeneralizableElement)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MState)
    {
      MState arg2 = (MState)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getDoActivity());
      {
        Iterator i = arg2.getInternalTransitions().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      preprocessIDs(arg2.getExit());
      preprocessIDs(arg2.getEntry());
    }
    else if (arg instanceof MNamespace)
    {
      MNamespace arg2 = (MNamespace)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getOwnedElements().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MStateVertex)
    {
      MStateVertex arg2 = (MStateVertex)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MModelElement)
    {
      MModelElement arg2 = (MModelElement)arg;
      {
        Iterator i = arg2.getTemplateParameters().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
      {
        Iterator i = arg2.getTaggedValues().iterator();
        while(i.hasNext())
        {
          preprocessIDs((MBase)i.next());
        }
      }
    }
    else if (arg instanceof MElement)
    {
      MElement arg2 = (MElement)arg;
    }

    Iterator i = arg.getExtensions().iterator();
    while(i.hasNext())
    {
      MExtension ext = (MExtension)i.next();
      if (ext.getValue() instanceof Element)
      {
        preprocessJDOMElementID((Element)(ext.getValue()));
      }
    }
  }
}
