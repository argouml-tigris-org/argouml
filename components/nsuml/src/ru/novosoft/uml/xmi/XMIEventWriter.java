/* NovoSoft UML API for Java. Version 0.4.19
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

import java.io.File;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import java.io.IOException;

import org.xml.sax.*;

public class XMIEventWriter extends PrintWriter implements DocumentHandler, ErrorHandler
{
  /**
   * Constructor
   * @param p_out
   */
  public XMIEventWriter(Writer p_out) throws IOException
  {
    super(p_out);
    setIndentationString(System.getProperty("nsuml.xmiwriter.indentstring", "  "));
  }

  /**
   * Constructor
   * @param p_filename
   * @param p_encoding
   */
  public XMIEventWriter(String p_filename, String p_encoding) throws IOException
  {
    //this(new OutputStreamWriter(new FileOutputStream(new File(p_filename)), p_encoding));
    this(new OutputStreamWriter(new FileOutputStream(new File(p_filename)), "UTF-8"));
  }

  /**
   * Constructor
   * @param filename a String
   */
  public XMIEventWriter(String p_filename) throws IOException
  {
    this(p_filename, "UTF-8");
  }



  /**
   * indentation level
   */
  protected int indentationLevel;

  /**
   * indentation string
   */
  private String indentationString = "  ";

  /**
   * get indentationString
   * @return a String
   */
  public String getIndentationString()
  {
    return indentationString;
  }

  /**
   * set indentation string
   * @param a String
   */
  public void setIndentationString(String arg)
  {
    indentationString = arg;
  }

  /**
   * increase indentation level
   */
  public void indent()
  {
    indentationLevel++;
  }

  /**
   * decrease indentation level
   */
  public void unindent()
  {
    indentationLevel--;
  }

  /**
   * print line with current indentation level
   * @param ln line to print
   */
  public void line(String ln)
  {
    sline(ln);
    println();
  }

  /**
   * indent then print line
   * @param line a String
   */
  public void iline(String ln)
  {
    indent();
    line(ln);
  }

  /**
   * unindent then print line
   * @param ln line to print
   */
  public void uline(String ln)
  {
    unindent();
    line(ln);
  }
  
  /**
   * indent then print line then unindent
   * @param ln line to print
   */
  public void ilineu(String ln)
  {
    iline(ln);
    unindent();
  }

  /**
   * indent then print line then unindent
   * @param ln line to print
   */
  public void ulinei(String ln)
  {
    unindent();
    line(ln);
    indent();
  }

  /**
   * start printing line, after this print 
   * and println methods should be called.
   * @param s a String
   */
  public void sline(String s)
  {
    sline();
    print(s);
  }

  /**
   * start printing line. After this print 
   * and println methods should be called.
   * This method should be overloaded if
   * other indentation policy is used.
   * @param s a String
   */
  public void sline()
  {
    for (int i=0; i<indentationLevel; i++)
    {
      print(indentationString);
    }
  }

  private void printXMIChar(char ch)
  {
    switch (ch)
    {
      case '<': 
      {
        print("&lt;");
        break;
      }

      case '>': 
      {
        print("&gt;");
        break;
      }

      case '&': 
      {
        print("&amp;");
        break;
      }

      case '"': 
      {
        print("&quot;");
        break;
      }

      case '\n':
      {
        println();
        break;
      }

      default:
      {
        print(ch);
      }
    }
  }

  protected void printXMIString(String s)
  {
    int len = s.length();
    for (int i=0; i<len; i++)
    {
      printXMIChar(s.charAt(i));
    }
  }

  protected boolean complexElement = true;
  protected boolean textElement = false;
  protected boolean charPrinted = true;
  protected boolean nextEndElement = false;

  public void setDocumentLocator(Locator locator)
  {
  }

  public void startDocument() throws SAXException
  {
    sline("<?xml version=\"1.0\" encoding=\"");

    //if (out instanceof OutputStreamWriter)
    //{
    //  print(((OutputStreamWriter)out).getEncoding());
    //}
    //else
    //{
      print("UTF-8");
    //}

    println("\"?>");

    //line("<!DOCTYPE XMI SYSTEM \"uml13.dtd\">");
  }

  public void endDocument() throws SAXException
  {
    flush();
    close();
  }

  public void startElement(String name, AttributeList atts) throws SAXException
  {
    if (!complexElement)
    {
      print(">");
    }

    if (!charPrinted)
    {
      println();
    }

    if (textElement)
    {
      print("<");
    }
    else
    {
      sline("<"); 
    }
    print(name);

    for (int i=0; i<atts.getLength(); i++)
    {
      print(" "); print(atts.getName(i)); print("=\""); printXMIString(atts.getValue(i));
       print("\"");
    }

    indent();

    complexElement = false;
    textElement = false;
    charPrinted = false;
    nextEndElement = false;
  }

  public void endElement(String name) throws SAXException
  {
    unindent();

    if (nextEndElement)
    {
      println();
    }

    if (complexElement)
    {
      if (textElement)
      {
        print("</");
      }
      else
      {
        sline("</");
      }
      print(name); print(">");
    }
    else
    {
      print("/>");
    }

    complexElement = true;
    textElement = false;
    charPrinted = false;
    nextEndElement = true;
  }

  public void characters(char[] ch, int start, int length) throws SAXException
  {
    if (!complexElement)
    {
      print(">");
    }

    for (int i=start; i<length; i++)
    {
      printXMIChar(ch[i]);
    }

    complexElement = true;
    textElement = true;
    charPrinted = true;
    nextEndElement = false;
  }

  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException
  {
  }

  public void processingInstruction(String target, String data) throws SAXException
  {
    if (!complexElement)
    {
      print(">");
    }

    if (!charPrinted)
    {
      println();
    }

    print("<?"); print(target); print(" "); print(data); print("?>");

    complexElement = true;
    textElement = true;
    charPrinted = false;
    nextEndElement = false;
  }



  public void warning(SAXParseException ex) 
  {
    System.err.println("[Warning] " + getLocationString(ex) + ": " + ex.getMessage());
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



  protected String getLocationString(SAXParseException ex) 
  {
    StringBuffer str = new StringBuffer();

    String systemId = ex.getSystemId();
    if (null != systemId) 
    {
      str.append(systemId);
    }

    str.append(':');
    str.append(ex.getLineNumber());
    str.append(':');
    str.append(ex.getColumnNumber());

    return str.toString();
  }

}
