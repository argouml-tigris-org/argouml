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

import org.argouml.xml.XMLTokenTableBase;

/**
 * An utility class that defines the strings required by the XML framework
 * and other strings needed to work with todo XML files.
 *
 * @author Michael Stockman
 */
public class TodoTokenTable extends XMLTokenTableBase {

	public static final String STRING_todo                   = "todo";
	public static final String STRING_todolist               = "todolist";
	public static final String STRING_todoitem               = "todoitem";
	public static final String STRING_headline               = "headline";
	public static final String STRING_description            = "description";
	public static final String STRING_priority               = "priority";
	public static final String STRING_moreinfourl            = "moreinfourl";

	public static final String STRING_resolvedcritics        = "resolvedcritics";
	public static final String STRING_issue                  = "issue";
	public static final String STRING_poster                 = "poster";
	public static final String STRING_offender               = "offender";

	/** The String to use for High Priority. */
	public static final String STRING_prio_high              = "high";
	/** The String to use for Medium Priority. */
	public static final String STRING_prio_med               = "medium";
	/** The String to use for Low Priority. */
	public static final String STRING_prio_low               = "low";

	public static final int    TOKEN_todo                    = 1;
	public static final int    TOKEN_todolist                = 2;
	public static final int    TOKEN_todoitem                = 3;
	public static final int    TOKEN_headline                = 4;
	public static final int    TOKEN_description             = 5;
	public static final int    TOKEN_priority                = 6;
	public static final int    TOKEN_moreinfourl             = 7;

	public static final int    TOKEN_resolvedcritics         = 8;
	public static final int    TOKEN_issue                   = 9;
	public static final int    TOKEN_poster                  = 10;
	public static final int    TOKEN_offender                = 11;

	public static final int    TOKEN_undefined               = 12;

	public TodoTokenTable() {
		super(32);
	}

	protected void setupTokens()
	{
		addToken(STRING_todo, new Integer(TOKEN_todo));
		addToken(STRING_todolist, new Integer(TOKEN_todolist));
		addToken(STRING_todoitem, new Integer(TOKEN_todoitem));
		addToken(STRING_headline, new Integer(TOKEN_headline));
		addToken(STRING_description, new Integer(TOKEN_description));
		addToken(STRING_priority, new Integer(TOKEN_priority));
		addToken(STRING_moreinfourl, new Integer(TOKEN_moreinfourl));

		addToken(STRING_resolvedcritics, new Integer(TOKEN_resolvedcritics));
		addToken(STRING_issue, new Integer(TOKEN_issue));
		addToken(STRING_poster, new Integer(TOKEN_poster));
		addToken(STRING_offender, new Integer(TOKEN_offender));
	}
}

