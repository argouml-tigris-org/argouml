// Copyright (c) 1995, 1996 Regents of the University of California.
// All rights reserved.
//
// This software was developed by the Arcadia project
// at the University of California, Irvine.
//
// Redistribution and use in source and binary forms are permitted
// provided that the above copyright notice and this paragraph are
// duplicated in all such forms and that any documentation,
// advertising materials, and other materials related to such
// distribution and use acknowledge that the software was developed
// by the University of California, Irvine.  The name of the
// University may not be used to endorse or promote products derived
// from this software without specific prior written permission.
// THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
// IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
// WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.

// File: ActionShowURL.java
// Classes: ActionShowURL
// Original Author: jrobbins@ics.uci.edu
// $Id$

package uci.gef;

import java.awt.Event;
import java.net.URL;

/** Action to display the contents of the given URL in the browser.
 *  Needs-More-Work: This action can only be used from an applet.
 *  <A HREF="../features.html#show_document">
 *  <TT>FEATURE: show_document</TT></A>
 */

public class ActionShowURL extends Action {

  protected URL _url;

  public ActionShowURL(URL url) { url(url); }

  public ActionShowURL(String s) throws java.net.MalformedURLException {
    url(s);
  }

  public ActionShowURL() { }

  public void url(URL u) { _url = u; }

  public void url(String u) throws java.net.MalformedURLException {
    _url = new URL(u);
  }

  public URL url() { return _url; }

  public String name() { return "Show URL in browser"; }

  /** Translate all selected Fig's in the current editor. */
  public void doIt(Event e) {
    Globals.showDocument(_url);
  }

  public void undoIt() { System.out.println("Needs-More-Work"); }

} /* end class ActionShowURL */

