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


/** 
 * These strings are saved in the final output in a zargo.
 * 
 * @author Jim Holt
 */

class ArgoTokenTable extends XMLTokenTableBase {

    /**
     * The constructor.
     */
    public ArgoTokenTable() {
	super(32);
    }

    ////////////////////////////////////////////////////////////////
    // constants
    private static final String STRING_ARGO                   = "argo";
    private static final String STRING_AUTHORNAME            = "authorname";
    private static final String STRING_AUTHOREMAIL            = "authoremail";
    private static final String STRING_VERSION               = "version";
    private static final String STRING_DESCRIPTION           = "description";
    private static final String STRING_SEARCHPATH            = "searchpath";
    private static final String STRING_MEMBER                = "member";
    private static final String STRING_HISTORYFILE           = "historyfile";
    private static final String STRING_DOCUMENTATION         = "documentation";

    private static final String STRING_SETTINGS = "settings";
    private static final String STRING_NOTATIONLANGUAGE = "notationlanguage";
    private static final String STRING_SHOWBOLDNAMES = "showboldnames";
    private static final String STRING_USEGUILLEMOTS = "useguillemots";
    private static final String STRING_SHOWVISIBILITY = "showvisibility";
    private static final String STRING_SHOWMULTIPLICITY = "showmultiplicity";
    private static final String STRING_SHOWINITIALVALUE = "showinitialvalue";
    private static final String STRING_SHOWPROPERTIES = "showproperties";
    private static final String STRING_SHOWTYPES = "showtypes";
    private static final String STRING_SHOWSTEREOTYPES = "showstereotypes";
    private static final String STRING_DEFAULTSHADOWWIDTH 
        = "defaultshadowwidth";

    /** The token for argo. */
    public static final int    TOKEN_ARGO                    = 1;
    /** The token for author name. */
    public static final int    TOKEN_AUTHORNAME              = 2;
    /** The token for author email. */
    public static final int    TOKEN_AUTHOREMAIL              = 3;
    /** The token for version. */
    public static final int    TOKEN_VERSION                 = 4;
    /** The token for description. */
    public static final int    TOKEN_DESCRIPTION             = 5;
    /** The token for search path. */
    public static final int    TOKEN_SEARCHPATH              = 6;
    /** The token for member. */
    public static final int    TOKEN_MEMBER                  = 7;
    /** The token for history file. */
    public static final int    TOKEN_HISTORYFILE             = 8;
    /** The token for documentation. */
    public static final int    TOKEN_DOCUMENTATION           = 9;

    /** The token for settings. */
    public static final int    TOKEN_SETTINGS           = 10;
    /** The token for the notation language setting. */
    public static final int    TOKEN_NOTATIONLANGUAGE           = 11;
    /** A token for Notation Settings. */
    public static final int    TOKEN_USEGUILLEMOTS           = 12;
    /** A token for Notation Settings. */
    public static final int    TOKEN_SHOWVISIBILITY           = 13;
    /** A token for Notation Settings. */
    public static final int    TOKEN_SHOWMULTIPLICITY           = 14;
    /** A token for Notation Settings. */
    public static final int    TOKEN_SHOWINITIALVALUE           = 15;
    /** The token for the notation setting to show properties. */
    public static final int    TOKEN_SHOWPROPERTIES           = 16;
    /** A token for Notation Settings. */
    public static final int    TOKEN_SHOWTYPES           = 17;
    /** A token for Notation Settings. */
    public static final int    TOKEN_SHOWSTEREOTYPES           = 18;
    /** A token for Notation Settings. */
    public static final int    TOKEN_DEFAULTSHADOWWIDTH           = 19;
    /** A token for Notation Settings. */
    public static final int    TOKEN_SHOWBOLDNAMES           = 20;


    /** The token for undefined. */
    public static final int    TOKEN_UNDEFINED               = 99;

    ////////////////////////////////////////////////////////////////
    // protected methods

    /*
     * @see org.argouml.persistence.XMLTokenTableBase#setupTokens()
     */
    protected void setupTokens() {
	addToken(STRING_ARGO, new Integer(TOKEN_ARGO));
        addToken(STRING_AUTHORNAME, new Integer(TOKEN_AUTHORNAME));
        addToken(STRING_AUTHOREMAIL, new Integer(TOKEN_AUTHOREMAIL));
	addToken(STRING_VERSION, new Integer(TOKEN_VERSION));
	addToken(STRING_DESCRIPTION, new Integer(TOKEN_DESCRIPTION));
	addToken(STRING_SEARCHPATH, new Integer(TOKEN_SEARCHPATH));
	addToken(STRING_MEMBER, new Integer(TOKEN_MEMBER));
	addToken(STRING_HISTORYFILE, new Integer(TOKEN_HISTORYFILE));
        addToken(STRING_DOCUMENTATION, new Integer(TOKEN_DOCUMENTATION));
        addToken(STRING_SETTINGS, new Integer(TOKEN_SETTINGS));
        addToken(STRING_NOTATIONLANGUAGE, new Integer(TOKEN_NOTATIONLANGUAGE));
        addToken(STRING_SHOWBOLDNAMES, new Integer(TOKEN_SHOWBOLDNAMES));
        addToken(STRING_USEGUILLEMOTS, new Integer(TOKEN_USEGUILLEMOTS));
        addToken(STRING_SHOWVISIBILITY, new Integer(TOKEN_SHOWVISIBILITY));
        addToken(STRING_SHOWMULTIPLICITY, new Integer(TOKEN_SHOWMULTIPLICITY));
        addToken(STRING_SHOWINITIALVALUE, new Integer(TOKEN_SHOWINITIALVALUE));
        addToken(STRING_SHOWPROPERTIES, new Integer(TOKEN_SHOWPROPERTIES));
        addToken(STRING_SHOWTYPES, new Integer(TOKEN_SHOWTYPES));
        addToken(STRING_SHOWSTEREOTYPES, new Integer(TOKEN_SHOWSTEREOTYPES));
        addToken(STRING_DEFAULTSHADOWWIDTH, 
                new Integer(TOKEN_DEFAULTSHADOWWIDTH));
    }

} /* end class ArgoTokenTable */
