// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.persistence;

import java.io.InputStream;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;

import org.argouml.cognitive.ResolvedCritic;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ListSet;
import org.xml.sax.SAXException;


// TODO: Reuse the offender Vector.

/**
 * Class that reads a todo list from a todo xml file.
 *
 * @author	Michael Stockman
 */
public class TodoParser extends SAXParserBase {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(TodoParser.class);

    private TodoTokenTable tokens = new TodoTokenTable();

    /**
     * The headline of the ToDoItem currently being read.
     */
    private String headline;

    /**
     * The priority of the ToDoItem currently being read.
     */
    private int    priority;

    /**
     * The moreInfoURL of the ToDoItem currently being read.
     */
    private String moreinfourl;

    /**
     * The description of the ToDoItem currently being read.
     */
    private String description;

    /**
     * The critic String of the ResolvedCritic currently being read.
     */
    private String critic;

    /**
     * The offenders vector of the ResolvedCritic currently being
     * read.
     */
    private Vector offenders;

    /**
     * Creates a new TodoParser.
     */
    public TodoParser() {
    }


    /**
     * Reads an XML todo list from InputStream is and enters
     * any todo items into the current designer.
     *
     * @param	is	The stream containing TodoList XML data.
     * @throws SAXException on any error
     */
    public synchronized void readTodoList(
            InputStream is) throws SAXException {

        LOG.info("=======================================");
        LOG.info("== READING TO DO LIST");
        parse(is);
    }

    /**
     * Called by the XML implementation to signal the start of
     * an XML entity.
     *
     * @param	e	The entity being started.
     */
    public void handleStartElement(XMLElement e) {
	//cat.debug("NOTE: TodoParser handleStartTag:" + e.getName());

	try {
	    switch (tokens.toToken(e.getName(), true)) {
	    case TodoTokenTable.TOKEN_HEADLINE:
	    case TodoTokenTable.TOKEN_DESCRIPTION:
	    case TodoTokenTable.TOKEN_PRIORITY:
	    case TodoTokenTable.TOKEN_MOREINFOURL:
	    case TodoTokenTable.TOKEN_POSTER:
	    case TodoTokenTable.TOKEN_OFFENDER:
		// NOP
		break;

	    case TodoTokenTable.TOKEN_TO_DO:
		handleTodo(e);
		break;

	    case TodoTokenTable.TOKEN_TO_DO_LIST:
		handleTodoList(e);
		break;

	    case TodoTokenTable.TOKEN_TO_DO_ITEM:
		handleTodoItemStart(e);
		break;

	    case TodoTokenTable.TOKEN_RESOLVEDCRITICS:
		handleResolvedCritics(e);
		break;

	    case TodoTokenTable.TOKEN_ISSUE:
		handleIssueStart(e);
		break;

	    default:
		LOG.warn("WARNING: unknown tag:" + e.getName());
		break;
	    }
	} catch (Exception ex) {
	    LOG.error("Exception in startelement", ex);
	}
    }

    /**
     * Called by the XML implementation to signal the end of an XML
     * entity.
     *
     * @param	e	The XML entity that ends.
     * @throws SAXException on any error
     */
    public void handleEndElement(XMLElement e) throws SAXException {

        try {
            switch (tokens.toToken(e.getName(), false)) {
            case TodoTokenTable.TOKEN_TO_DO:
            case TodoTokenTable.TOKEN_RESOLVEDCRITICS:
            case TodoTokenTable.TOKEN_TO_DO_LIST:
        	// NOP
        	break;

            case TodoTokenTable.TOKEN_TO_DO_ITEM:
        	handleTodoItemEnd(e);
        	break;

            case TodoTokenTable.TOKEN_HEADLINE:
        	handleHeadline(e);
        	break;

            case TodoTokenTable.TOKEN_DESCRIPTION:
        	handleDescription(e);
        	break;

            case TodoTokenTable.TOKEN_PRIORITY:
        	handlePriority(e);
        	break;

            case TodoTokenTable.TOKEN_MOREINFOURL:
        	handleMoreInfoURL(e);
        	break;

            case TodoTokenTable.TOKEN_ISSUE:
        	handleIssueEnd(e);
        	break;

            case TodoTokenTable.TOKEN_POSTER:
        	handlePoster(e);
        	break;

            case TodoTokenTable.TOKEN_OFFENDER:
        	handleOffender(e);
        	break;

            default:
        	LOG.warn("WARNING: unknown end tag:"
        		 + e.getName());
        	break;
            }
        } catch (Exception ex) {
            throw new SAXException(ex);
        }
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleTodo(XMLElement e) {
	/* do nothing */
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleTodoList(XMLElement e) {
	/* do nothing */
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleResolvedCritics(XMLElement e) {
	/* do nothing */
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleTodoItemStart(XMLElement e) {
        headline = "";
        priority = ToDoItem.HIGH_PRIORITY;
        moreinfourl = "";
        description = "";
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleTodoItemEnd(XMLElement e) {
        ToDoItem item;
        Designer dsgr;

        /* This is expected to be safe, don't add a try block here */

        dsgr = Designer.theDesigner();
        item =
            new ToDoItem(dsgr, headline, priority, description, moreinfourl,
                         new ListSet());
        dsgr.getToDoList().addElement(item);
        //cat.debug("Added ToDoItem: " + _headline);
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleHeadline(XMLElement e) {
	headline = decode(e.getText()).trim();
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handlePriority(XMLElement e) {
        String prio = decode(e.getText()).trim();
        int np;

        try {
            np = Integer.parseInt(prio);
        } catch (NumberFormatException nfe) {
            np = ToDoItem.HIGH_PRIORITY;

            if (TodoTokenTable.STRING_PRIO_HIGH.equalsIgnoreCase(prio)) {
                np = ToDoItem.HIGH_PRIORITY;
            } else if (TodoTokenTable.STRING_PRIO_MED.equalsIgnoreCase(prio)) {
                np = ToDoItem.MED_PRIORITY;
            } else if (TodoTokenTable.STRING_PRIO_LOW.equalsIgnoreCase(prio)) {
                np = ToDoItem.LOW_PRIORITY;
            }
        }

        priority = np;
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleMoreInfoURL(XMLElement e) {
	moreinfourl = decode(e.getText()).trim();
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleDescription(XMLElement e) {
	description = decode(e.getText()).trim();
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleIssueStart(XMLElement e) {
	critic = null;
	offenders = null;
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleIssueEnd(XMLElement e) {
        Designer dsgr;
        ResolvedCritic item;

        if (critic == null) {
            return;
        }

        item = new ResolvedCritic(critic, offenders);
        dsgr = Designer.theDesigner();
        dsgr.getToDoList().addResolvedCritic(item);
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handlePoster(XMLElement e) {
        critic = decode(e.getText()).trim();
    }

    /**
     * Internal method.
     *
     * @param e the element
     */
    protected void handleOffender(XMLElement e) {
        if (offenders == null) {
            offenders = new Vector();
        }
        offenders.add(decode(e.getText()).trim());
    }

    /**
     * Utility method to decode a String filtering out any noise that
     * an XML framework might have seen fit to add and thus regaining
     * the original unmodified String.
     *
     * @param	str	The String to decode.
     * @return	A copy of the original String.
     */
    public static String decode(String str) {
        if (str == null) {
            return null;
        }

        StringBuffer sb;
        int i1, i2;
        char c;

        sb = new StringBuffer();
        for (i1 = 0, i2 = 0; i2 < str.length(); i2++) {
            c = str.charAt(i2);
            if (c == '%') {
                if (i2 > i1) {
                    sb.append(str.substring(i1, i2));
                }
                for (i1 = ++i2; i2 < str.length(); i2++) {
                    if (str.charAt(i2) == ';') {
                        break;
                    }
                }
                if (i2 >= str.length()) {
                    i1 = i2;
                    break;
                }

                if (i2 > i1) {
                    String ent = str.substring(i1, i2);
                    if ("proc".equals(ent)) {
                        sb.append('%');
                    } else {
                        try {
                            sb.append((char) Integer.parseInt(ent));
                        } catch (NumberFormatException nfe) {
                        }
                    }
                }
                i1 = i2 + 1;
            }
        }
        if (i2 > i1) {
            sb.append(str.substring(i1, i2));
        }
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
    public static String encode(String str) {
	StringBuffer sb;
	int i1, i2;
	char c;

	if (str == null) {
            return null;
        }
	sb = new StringBuffer();
	for (i1 = 0, i2 = 0; i2 < str.length(); i2++) {
	    c = str.charAt(i2);
	    if (c == '%') {
		if (i2 > i1) {
                    sb.append(str.substring(i1, i2));
                }
		sb.append("%proc;");
		i1 = i2 + 1;
	    } else if (c < 0x28
                ||  (c >= 0x3C && c <= 0x40 && c != 0x3D && c != 0x3F)
                ||  (c >= 0x5E && c <= 0x60 && c != 0x5F)
                ||   c >= 0x7B) {
		if (i2 > i1) {
                    sb.append(str.substring(i1, i2));
                }
		sb.append("%" + Integer.toString(c) + ";");
		i1 = i2 + 1;
	    }
	}
	if (i2 > i1) {
            sb.append(str.substring(i1, i2));
        }

	//cat.debug("encode:\n" + str + "\n -> " + sb.toString());
	return sb.toString();
    }
}

