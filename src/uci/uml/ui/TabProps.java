package uci.uml.ui;

//import jargo.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import uci.util.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
//import com.sun.java.swing.border.*;


public class TabProps extends TabSpawnable
implements TabModelTarget {
  ////////////////////////////////////////////////////////////////
  // instance variables
  Object _target;

  ////////////////////////////////////////////////////////////////
  // constructor
  public TabProps() {
    super("Properties");
    setLayout(new BorderLayout());
    setFont(new Font("Dialog", Font.PLAIN, 10));
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  public void setTarget(Object t) {
    _target = t;
    // needs-more-work
  }
  public Object getTarget() { return _target; }

} /* end class TabProps */
