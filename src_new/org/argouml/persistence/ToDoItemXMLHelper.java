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

import org.argouml.cognitive.ToDoItem;

/**
 * Helper class to help save todo items properly in the .todo XML file.
 * It provides a view of A ToDoItem particularly suited for saving in an
 * XML file by encoding strings to preserve graphic characters and allow
 * lines to be broken and still be able to regain the original contents.
 *
 * @see	ToDoItem
 * @author Michael Stockman
 */
public class ToDoItemXMLHelper
{
    private final ToDoItem item;

    /**
     * Creates a new ToDoItemXMLHelper for item.
     *
     * @param	todoItem	A ToDoItem.
     */
    public ToDoItemXMLHelper(ToDoItem todoItem)
    {
	if (todoItem == null)
	    throw new NullPointerException();
	item = todoItem;
    }

    /**
     * Encodes the headline of this ToDoItem in an XML safe way and
     * returns the new String. The String can be regained by running the
     * returned String through
     * {@link TodoParser#decode TodoParser::decode}.
     *
     * @return	The encoded headline.
     */
    public String getHeadline()
    {
	return TodoParser.encode(item.getHeadline());
    }

    /**
     * Encodes the priority of this ToDoItem in an XML safe way and
     * returns the new String. The String can be regained by running the
     * returned String through
     * {@link TodoParser#decode TodoParser::decode} and comparing to the
     * STRING_prio_* values in TodoTokenTable.
     *
     * @return	The encoded priority.
     */
    public String getPriority()
    {
	String s = TodoTokenTable.STRING_PRIO_HIGH;
	switch (item.getPriority())
	{
	case ToDoItem.HIGH_PRIORITY:
	    s = TodoTokenTable.STRING_PRIO_HIGH;
	    break;

	case ToDoItem.MED_PRIORITY:
	    s = TodoTokenTable.STRING_PRIO_MED;
	    break;

	case ToDoItem.LOW_PRIORITY:
	    s = TodoTokenTable.STRING_PRIO_LOW;
	    break;
	}

	return TodoParser.encode(s);
    }

    /**
     * Encodes the moreInfoURL of this ToDoItem in an XML safe way and
     * returns the new String. The String can be regained by running the
     * returned String through
     * {@link TodoParser#decode TodoParser::decode}.
     *
     * @return	The encoded moreInfoURL.
     */
    public String getMoreInfoURL()
    {
	return TodoParser.encode(item.getMoreInfoURL());
    }

    /**
     * Encodes the description of this ToDoItem in an XML safe way and
     * returns the new String. The String can be regained by running the
     * returned String through
     * {@link TodoParser#decode TodoParser::decode}.
     *
     * @return	The encoded description.
     */
    public String getDescription()
    {
	return TodoParser.encode(item.getDescription());
    }
}

