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


public class TabDocs extends TabText {
  ////////////////////////////////////////////////////////////////
  // constructor
  public TabDocs() {
    setTitle("Docs");
    System.out.println("making TabDocs");
  }

  ////////////////////////////////////////////////////////////////
  // accessors
  protected String genText() {
    return DocumentationManager.getDocs(_target);
  }

  protected void parseText(String s) {
    DocumentationManager.setDocs(_target, s);
  }

  
} /* end class TabDocs */
