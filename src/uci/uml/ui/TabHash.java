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


public class TabHash extends TabText {

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabHash() {
    setTitle("hashcode()");
    System.out.println("making TabHash");
  }

  ////////////////////////////////////////////////////////////////
  // accessors

  public String genText() {
    if (_target == null) return "Nothing selected";
    return Integer.toString(_target.hashCode());
  }
  

  
} /* end class TabHash */
