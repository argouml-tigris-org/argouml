package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.text.*;
//import com.sun.java.swing.border.*;


public class TabText extends TabSpawnable
implements TabModelTarget, DocumentListener {
  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;
  JTextArea _text = new JTextArea();
  

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabText() {
    super("toString()");
    System.out.println("making TabText");
    setLayout(new BorderLayout());
    setFont(new Font("Dialog", Font.PLAIN, 10));
    add(new JScrollPane(_text), BorderLayout.CENTER);
    _text.getDocument().addDocumentListener(this);
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
    _target = t;
    if (_target == null) {
      _text.setEnabled(false);
      _text.setText("Nothing selected");
    }
    else {
      _text.setEnabled(true);
      _text.setText(genText());
    }
  }
  public Object getTarget() { return _target; }


  protected String genText() {
    if (_target == null) return "nothing selected";
    return _target.toString();
  }

  protected void parseText(String s) {
    if (s == null) s = "(null)";
    System.out.println("parsing text:" + s);
  }
  
  ////////////////////////////////////////////////////////////////
  // event handlers
  public void insertUpdate(DocumentEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    System.out.println(getClass().getName() + " insert");
    parseText(_text.getText());
  }

  public void removeUpdate(DocumentEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    System.out.println(getClass().getName() +  " remove");
    parseText(_text.getText());
  }

  public void changedUpdate(DocumentEvent e) {
    //needs-more-work: should fire its own event and ProjectBrowser
    //should register a listener
    System.out.println(getClass().getName() + " changed");
    parseText(_text.getText());
  }


  
} /* end class TabText */
