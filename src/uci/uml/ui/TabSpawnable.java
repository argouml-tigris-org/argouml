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




package uci.uml.ui;

import java.awt.*;
import java.util.*;
import javax.swing.*;

/** A subclass of JPanel that can act as a tab in the DetailsPane or
 *  MultiEditorPane.  When the tab is double-clicked, this JPanel will
 *  generate a separate window of the same size and with the same
 *  contents.  This is almost like "tearing off" a tab. */

public class TabSpawnable extends JPanel implements Cloneable {
  public final int OVERLAPP = 30;

  ////////////////////////////////////////////////////////////////
  // instance variables
  String _title = "untitled";
  boolean _tear = false; // if true, remove tab from parent JTabbedPane

  ////////////////////////////////////////////////////////////////
  // constructor

  public TabSpawnable() { this("untitled", false); }

  public TabSpawnable(String title) { this(title, false); }

  public TabSpawnable(String title, boolean tear) {
    setTitle(title);
    _tear = tear;
    System.out.println("making " + title);
  }

  public Object clone() {
    try { return this.getClass().newInstance(); }
    catch (Exception ex) {
      System.out.println("exception in clone()");
    }
    return null;
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public String getTitle() { return _title; }
  public void setTitle(String t) { _title = t; }


  ////////////////////////////////////////////////////////////////
  // actions

  public TabSpawnable spawn() {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new BorderLayout());
    f.setTitle(_title);
    TabSpawnable newPanel = (TabSpawnable) clone();
    if (newPanel == null) return null; //failed to clone
    newPanel.setTitle(_title);
    if (newPanel instanceof TabToDoTarget) {
      TabToDoTarget me = (TabToDoTarget) this;
      TabToDoTarget it = (TabToDoTarget) newPanel;
      it.setTarget(me.getTarget());
    }
    else if (newPanel instanceof TabModelTarget) {
      TabModelTarget me = (TabModelTarget) this;
      TabModelTarget it = (TabModelTarget) newPanel;
      it.setTarget(me.getTarget());
    }
    f.getContentPane().add(newPanel, BorderLayout.CENTER);
    Rectangle bounds = getBounds();
    bounds.height += OVERLAPP*2;
    f.setBounds(bounds);

    Point loc = new Point(0,0);
    SwingUtilities.convertPointToScreen(loc, this);
    loc.y -= OVERLAPP;
    f.setLocation(loc);
    f.setVisible(true);

    if (_tear && (getParent() instanceof JTabbedPane))
      ((JTabbedPane)getParent()).remove(this);

    return newPanel;
  }

} /* end class TabSpawnable */


