package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.text.*;
//import com.sun.java.swing.border.*;

import uci.util.*;
import uci.uml.generate.*;


public class TabUMLDisplay extends TabText {
  ////////////////////////////////////////////////////////////////
  // constructor
  public TabUMLDisplay() {
    setTitle("Text");
    System.out.println("making TabUMLDisplay");
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  protected String genText() {
    if (_target == null) return "nothing selected";
    return GeneratorDisplay.Generate(_target);
  }

  protected void parseText(String s) {
    if (s == null) s = "(null)";
    System.out.println("TabUMLDisplay parsing text:" + s);
  }
  


  
} /* end class TabUMLDisplay */
