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




// File: CmdShowURL.java
// Classes: CmdShowURL
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.Event;
import java.net.URL;

/** Cmd to display the contents of the given URL in the browser.
 *  Needs-More-Work: This Cmd can only be used from an applet.
 */

public class CmdShowURL extends Cmd {

  protected URL _url;

  public CmdShowURL(URL url) { this(); url(url); }

  public CmdShowURL(String s) throws java.net.MalformedURLException {
    this();
    url(s);
  }

  public CmdShowURL() { super("Show URL in browser"); }

  public void url(URL u) { _url = u; }

  public void url(String u) throws java.net.MalformedURLException {
    _url = new URL(u);
  }

  public URL url() { return _url; }

  /** Translate all selected Fig's in the current editor. */
  public void doIt() {
    Globals.showDocument(_url);
  }

  public void undoIt() { System.out.println("Needs-More-Work"); }

  static final long serialVersionUID = 6935852249858580737L;

} /* end class CmdShowURL */

