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

package org.argouml.xml.argo;

import org.argouml.xml.XMLTokenTableBase;

/** this needs work,AFAIK none of these strings are
 * saved in the final output in a zargo.
 * @author Jim Holt
 */

public class ArgoTokenTable extends XMLTokenTableBase {

  ////////////////////////////////////////////////////////////////
  // constructors

  public ArgoTokenTable() {
    super(32);
  }

  ////////////////////////////////////////////////////////////////
  // constants
  public static final String STRING_argo                   = "argo";
  /** doesn't work
   */  
  public static final String STRING_authorname             = "authorname";
  public static final String STRING_version                = "version";
  public static final String STRING_description            = "description";
  public static final String STRING_searchpath             = "searchpath";
  public static final String STRING_member                 = "member";
  public static final String STRING_historyfile            = "historyfile";
  public static final String STRING_documentation          = "documentation";

  public static final int    TOKEN_argo                    = 1;
  public static final int    TOKEN_authorname              = 2;
  public static final int    TOKEN_version                 = 3;
  public static final int    TOKEN_description             = 4;
  public static final int    TOKEN_searchpath              = 5;
  public static final int    TOKEN_member                  = 6;
  public static final int    TOKEN_historyfile             = 7;
  /** This can be saved successfully however there is no
   * way to output this information.
   */  
  public static final int    TOKEN_documentation           = 8;
  public static final int    TOKEN_undefined               = 9;

  ////////////////////////////////////////////////////////////////
  // protected methods

  protected void setupTokens()
  {
    addToken(STRING_argo, new Integer(TOKEN_argo));
    addToken(STRING_authorname, new Integer(TOKEN_authorname));
    addToken(STRING_version, new Integer(TOKEN_version));
    addToken(STRING_description, new Integer(TOKEN_description));
    addToken(STRING_searchpath, new Integer(TOKEN_searchpath));
    addToken(STRING_member, new Integer(TOKEN_member));
    addToken(STRING_historyfile, new Integer(TOKEN_historyfile));
  }

} /* end class ArgoTokenTable */
