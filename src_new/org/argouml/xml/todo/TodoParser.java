// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.xml.todo;

import org.argouml.application.api.*;
import java.util.*;
import java.io.*;
import java.net.URL;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ResolvedCritic;
import org.tigris.gef.util.VectorSet;
import org.argouml.xml.SAXParserBase;
import org.argouml.xml.XMLElement;
import org.xml.sax.*;
import org.apache.log4j.Category;

// Needs-more-work: Reuse the offender Vector.

/**
 * Class that reads a todo list from a todo xml file. This class is a
 * SINGLETON.
 *
 * @see	#SINGLETON
 * @author	Michael Stockman
 */
public class TodoParser extends SAXParserBase {
	protected static Category cat = Category.getInstance(TodoParser.class);

	/** The SINGLETON object of this class. */
	public static TodoParser SINGLETON = new TodoParser();

	private TodoTokenTable _tokens = new TodoTokenTable();
	private URL _url = null;

	/** The headline of the ToDoItem currently being read. */
	protected String _headline;

	/** The priority of the ToDoItem currently being read. */
	protected int    _priority;

	/** The moreInfoURL of the ToDoItem currently being read. */
	protected String _moreinfourl;

	/** The description of the ToDoItem currently being read. */
	protected String _description;

	/** The critic String of the ResolvedCritic currently being read. */
	protected String _critic;

	/**
	 * The offenders vector of the ResolvedCritic currently being
	 * read.
	 */
	protected Vector _offenders;

	/**
	 * Creates a new TodoParser.
	 */
	protected TodoParser()
	{
	}

	/**
	 * Reads a todo list from the file named in url.
	 *
	 * @param	url	The URL of the file to read from.
	 */
	public synchronized void readTodoList(URL url)
	{
		readTodoList(url, true);
	}

	/**
	 * Reads a todo list from the file named in url. addMembers is not
	 * currently used but included for concistency towards reading
	 * XML files in ArgoUML.
	 *
	 * @param	url	The URL of the file to read from.
	 * @param	addMembers	Ignored.
	 */
	public synchronized void readTodoList(URL url, boolean addMembers)
	{
		_url = url;

		try
		{
			readTodoList(url.openStream(), addMembers);
		}
		catch (IOException e)
		{
			cat.warn("Couldn't open InputStream in " +
				 "TodoParser.load(" + url + ") ",
				 e);
			e.printStackTrace();
		}
	}

	/**
	 * Sets the _url instance variable. This is mainly used for providing
	 * fancy log messages when reading a todo list from an InputStream.
	 *
	 * @param	url	The name of the file the we're eledgedly
	 *			reading from.
	 */
	public void setURL(URL url)
	{
		_url = url;
	}

	/**
	 * Reads an XML todo list from InputStream is and enters
	 * any todo items into the current designer.
	 *
	 * @param	is	The stream containing TodoList XML data.
	 * @param	addMembers	Ignored.
	 * @see	#setURL
	 */
	public synchronized void readTodoList(InputStream is,
						boolean addMembers)
	{
		String errmsg = "Exception reading todo list =============";
		try {
			cat.debug("=======================================");
			cat.debug("== READING TODO LIST "+_url);
			parse(is);
		}
		catch(SAXException saxEx)
		{
			/*
			 * A SAX exception could have been generated
			 * because of another exception.
			 * Then get the initial exception to display the
			 * location of the original error.
			 */
			Exception ex = saxEx.getException();
			if(ex == null)
			{
				cat.error(errmsg, saxEx);
			}
			else
			{
				cat.error(errmsg, ex);
			}
		}
		catch (Exception ex)
		{
			cat.error(errmsg, ex);
		}
	}

	/**
	 * Called by the XML implementation to signal the start of
	 * an XML entity.
	 *
	 * @param	e	The entity being started.
	 */
	public void handleStartElement(XMLElement e)
	{
		//cat.debug("NOTE: TodoParser handleStartTag:" + e.getName());

		try
		{
			switch (_tokens.toToken(e.getName(), true))
			{
			case TodoTokenTable.TOKEN_todo:
				handleTodo(e);
				break;

			case TodoTokenTable.TOKEN_todolist:
				handleTodoList(e);
				break;

			case TodoTokenTable.TOKEN_todoitem:
				handleTodoItemStart(e);
				break;

			case TodoTokenTable.TOKEN_resolvedcritics:
				handleResolvedCritics(e);
				break;

			case TodoTokenTable.TOKEN_issue:
				handleIssueStart(e);
				break;

			default:
				cat.warn("WARNING: unknown tag:" + e.getName());
				break;
			}
		}
		catch (Exception ex)
		{
			cat.error("Exception in startelement", ex);
		}
	}

	/**
	 * Called by the XML implementation to signal the end of an XML
	 * entity.
	 *
	 * @param	e	The XML entity that ends.
	 */
	public void handleEndElement(XMLElement e)
	{
		//cat.debug("NOTE: TodoParser handleEndTag:"+e.getName()+".");

		try
		{
			switch (_tokens.toToken(e.getName(), false))
			{
			case TodoTokenTable.TOKEN_todoitem:
				handleTodoItemEnd(e);
				break;

			case TodoTokenTable.TOKEN_headline:
				handleHeadline(e);
				break;

			case TodoTokenTable.TOKEN_description:
				handleDescription(e);
				break;

			case TodoTokenTable.TOKEN_priority:
				handlePriority(e);
				break;

			case TodoTokenTable.TOKEN_moreinfourl:
				handleMoreInfoURL(e);
				break;

			case TodoTokenTable.TOKEN_issue:
				handleIssueEnd(e);
				break;

			case TodoTokenTable.TOKEN_poster:
				handlePoster(e);
				break;

			case TodoTokenTable.TOKEN_offender:
				handleOffender(e);
				break;

			default:
				cat.warn("WARNING: unknown end tag:"
					+ e.getName());
				break;
			}
		}
		catch (Exception ex)
		{
			cat.error("Exception in endelement", ex);
		}
	}

	/**
	 * Internal method.
	 */
	protected void handleTodo(XMLElement e)
	{
		/* do nothing */
	}

	/**
	 * Internal method.
	 */
	protected void handleTodoList(XMLElement e)
	{
		/* do nothing */
	}

	/**
	 * Internal method.
	 */
	protected void handleResolvedCritics(XMLElement e)
	{
		/* do nothing */
	}

	/**
	 * Internal method.
	 */
	protected void handleTodoItemStart(XMLElement e)
	{
		_headline = "";
		_priority = ToDoItem.HIGH_PRIORITY;
		_moreinfourl = "";
		_description = "";
	}

	/**
	 * Internal method.
	 */
	protected void handleTodoItemEnd(XMLElement e)
	{
		ToDoItem item;
		Designer dsgr;

		/* This is expected to be safe, don't add a try block here */

		dsgr = Designer.theDesigner();
		item = new ToDoItem(dsgr, _headline, _priority, _description, _moreinfourl, new VectorSet());
		dsgr.getToDoList().addElement(item);
		//cat.debug("Added ToDoItem: " + _headline);
	}

	/**
	 * Internal method.
	 */
	protected void handleHeadline(XMLElement e)
	{
		_headline = decode(e.getText()).trim();
	}

	/**
	 * Internal method.
	 */
	protected void handlePriority(XMLElement e)
	{
		String prio = decode(e.getText()).trim();
		int np;

		try
		{
			np = Integer.parseInt(prio);
		}
		catch (NumberFormatException nfe)
		{
			np = ToDoItem.HIGH_PRIORITY;

			if (TodoTokenTable.STRING_prio_high.equalsIgnoreCase(prio))
				np = ToDoItem.HIGH_PRIORITY;
			else if (TodoTokenTable.STRING_prio_med.equalsIgnoreCase(prio))
				np = ToDoItem.MED_PRIORITY;
			else if (TodoTokenTable.STRING_prio_low.equalsIgnoreCase(prio))
				np = ToDoItem.LOW_PRIORITY;
		}

		_priority = np;
	}

	/**
	 * Internal method.
	 */
	protected void handleMoreInfoURL(XMLElement e)
	{
		_moreinfourl = decode(e.getText()).trim();
	}

	/**
	 * Internal method.
	 */
	protected void handleDescription(XMLElement e)
	{
		_description = decode(e.getText()).trim();
	}

	/**
	 * Internal method.
	 */
	protected void handleIssueStart(XMLElement e)
	{
		_critic = null;
		_offenders = null;
	}

	/**
	 * Internal method.
	 */
	protected void handleIssueEnd(XMLElement e)
	{
		Designer dsgr;
		ResolvedCritic item;

		if (_critic == null)
			return;

		item = new ResolvedCritic(_critic, _offenders);
		dsgr = Designer.theDesigner();
		dsgr.getToDoList().getResolvedItems().addElement(item);
		// cat.debug("Added ResolvedCritic: " + item);
	}

	/**
	 * Internal method.
	 */
	protected void handlePoster(XMLElement e)
	{
		_critic = decode(e.getText()).trim();
	}

	/**
	 * Internal method.
	 */
	protected void handleOffender(XMLElement e)
	{
		if (_offenders == null)
			_offenders = new Vector();
		_offenders.add(decode(e.getText()).trim());
	}

	/**
	 * Utility method to decode a String filtering out any noice that
	 * an XML framework might have seen fit to add and thus regaining
	 * the original unmodified String.
	 *
	 * @param	str	The String to decode.
	 * @return	A copy of the original String.
	 */
	public static String decode(String str)
	{
		StringBuffer sb;
		int i1, i2;
		char c;

		if (str == null)
			return null;
		sb = new StringBuffer();
		for (i1 = i2 = 0; i2 < str.length(); i2++)
		{
			c = str.charAt(i2);
			if (c == '%')
			{
				if (i2 > i1)
					sb.append(str.substring(i1, i2));
				for (i1 = ++i2; i2 < str.length(); i2++)
					if (str.charAt(i2) == ';')
						break;
				if (i2 >= str.length())
				{
					i1 = i2;
					break;
				}

				if (i2 > i1)
				{
					String ent = str.substring(i1, i2);
					if ("proc".equals(ent))
						sb.append('%');
					else
					{
						try
						{
							sb.append((char)Integer.parseInt(ent));
						}
						catch (NumberFormatException nfe)
						{
						}
					}
				}

				i1 = i2+1;
			}
		}
		if (i2 > i1)
			sb.append(str.substring(i1, i2));
		//cat.debug("decode:\n" + str + "\n -> " + sb.toString());
		return sb.toString();
	}

	/**
	 * Utility method to encode a String in a way that allows it to be
	 * saved properly in an XML file and regained filtering out any noice
	 * that an XML framework might have seen fit to add.
	 *
	 * @param	str	The String to encode.
	 * @return	The encoded String.
	 */
	public static String encode(String str)
	{
		StringBuffer sb;
		int i1, i2;
		char c;

		if (str == null)
			return null;
		sb = new StringBuffer();
		for (i1 = i2 = 0; i2 < str.length(); i2++)
		{
			c = str.charAt(i2);
			if (c == '%')
			{
				if (i2 > i1)
					sb.append(str.substring(i1, i2));
				sb.append("%proc;");
				i1 = i2+1;
			}
			else if (c < 0x28 ||
				 (c >= 0x3C && c <= 0x40 && c != 0x3D && c != 0x3F) ||
				 (c >= 0x5E && c <= 0x60 && c != 0x5F) ||
				 c >= 0x7B)
			{
				if (i2 > i1)
					sb.append(str.substring(i1, i2));
				sb.append("%" + Integer.toString((int)c) + ";");
				i1 = i2+1;
			}
		}
		if (i2 > i1)
			sb.append(str.substring(i1, i2));

		//cat.debug("encode:\n" + str + "\n -> " + sb.toString());
		return sb.toString();
	}
}

