// $Id: TodoTokenTable.java 11516 2006-11-25 04:30:15Z tfmorris $
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


/**
 * An utility class that defines the strings required by the XML framework
 * and other strings needed to work with todo XML files.
 *
 * @author Michael Stockman
 */
class TodoTokenTable extends XMLTokenTableBase {

    private static final String STRING_TO_DO                = "todo";
    private static final String STRING_TO_DO_LIST           = "todolist";
    private static final String STRING_TO_DO_ITEM           = "todoitem";
    private static final String STRING_HEADLINE             = "headline";
    private static final String STRING_DESCRIPTION          = "description";
    private static final String STRING_PRIORITY             = "priority";
    private static final String STRING_MOREINFOURL          = "moreinfourl";

    private static final String STRING_RESOLVEDCRITICS      = "resolvedcritics";
    private static final String STRING_ISSUE                = "issue";
    private static final String STRING_POSTER               = "poster";
    private static final String STRING_OFFENDER             = "offender";

    /** The String to use for High Priority. */
    public static final String STRING_PRIO_HIGH            = "high";
    /** The String to use for Medium Priority. */
    public static final String STRING_PRIO_MED             = "medium";
    /** The String to use for Low Priority. */
    public static final String STRING_PRIO_LOW             = "low";

    /** The token for todo. */
    public static final int    TOKEN_TO_DO                 = 1;
    /** The token for todo list. */
    public static final int    TOKEN_TO_DO_LIST            = 2;
    /** The token for todoitem. */
    public static final int    TOKEN_TO_DO_ITEM            = 3;
    /** The token for headline. */
    public static final int    TOKEN_HEADLINE              = 4;
    /** The token for description. */
    public static final int    TOKEN_DESCRIPTION           = 5;
    /** The token for priority. */
    public static final int    TOKEN_PRIORITY              = 6;
    /** The token for moreinfourl. */
    public static final int    TOKEN_MOREINFOURL           = 7;

    /** The token for resolvedcritics. */
    public static final int    TOKEN_RESOLVEDCRITICS       = 8;
    /** The token for issue. */
    public static final int    TOKEN_ISSUE                 = 9;
    /** The token for poster. */
    public static final int    TOKEN_POSTER                = 10;
    /** The token for offender. */
    public static final int    TOKEN_OFFENDER              = 11;

    /** The token for undefined. */
    public static final int    TOKEN_UNDEFINED             = 12;

    /**
     * The constructor.
     *
     */
    public TodoTokenTable() {
	super(32);
    }

    /*
     * @see org.argouml.persistence.XMLTokenTableBase#setupTokens()
     */
    protected void setupTokens() {
	addToken(STRING_TO_DO, new Integer(TOKEN_TO_DO));
	addToken(STRING_TO_DO_LIST, new Integer(TOKEN_TO_DO_LIST));
	addToken(STRING_TO_DO_ITEM, new Integer(TOKEN_TO_DO_ITEM));
	addToken(STRING_HEADLINE, new Integer(TOKEN_HEADLINE));
	addToken(STRING_DESCRIPTION, new Integer(TOKEN_DESCRIPTION));
	addToken(STRING_PRIORITY, new Integer(TOKEN_PRIORITY));
	addToken(STRING_MOREINFOURL, new Integer(TOKEN_MOREINFOURL));

	addToken(STRING_RESOLVEDCRITICS, new Integer(TOKEN_RESOLVEDCRITICS));
	addToken(STRING_ISSUE, new Integer(TOKEN_ISSUE));
	addToken(STRING_POSTER, new Integer(TOKEN_POSTER));
	addToken(STRING_OFFENDER, new Integer(TOKEN_OFFENDER));
    }
}

