package uci.uml.ui;

import java.awt.*;
import java.util.*;
import com.sun.java.swing.*;

public class TabSpawnable extends JPanel implements Cloneable {
  public final int OVERLAPP = 30;
  
  ////////////////////////////////////////////////////////////////
  // instance variables
  String _title = "untitled";


  ////////////////////////////////////////////////////////////////
  // constructor
  
  public TabSpawnable() { }
  
  public TabSpawnable(String title) {
    setTitle(title);
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
  
  public void spawn() {
    JFrame f = new JFrame();
    f.getContentPane().setLayout(new BorderLayout());
    f.setTitle(_title);
    TabSpawnable newPanel = (TabSpawnable) clone();
    if (newPanel == null) return; //failed to clone
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
  }

} /* end class TabSpawnable */


