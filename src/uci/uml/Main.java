package uci.uml;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import com.sun.java.swing.*;

import uci.util.*;
import uci.uml.ui.*;
import uci.uml.Model_Management.Model;
import uci.uml.test.omg.*;

import uci.argo.kernel.*;


public class Main {
  ////////////////////////////////////////////////////////////////
  // constants

  public static int WIDTH = 800;
  public static int HEIGHT = 600;
  public static int INITIAL_WIDTH = 400; // for showing progress bar
  public static int INITIAL_HEIGHT = 200;

  ////////////////////////////////////////////////////////////////
  // class variables

  public static Vector UMLPerspectives = new Vector();
  public static Vector ToDoPerspectives = new Vector();

  // static initializer
  static {
    UMLPerspectives.addElement(new NavM_DE_F());
    UMLPerspectives.addElement(new NavM_DE_F());

    ToDoPerspectives.addElement(new ToDoByOffender());
    ToDoPerspectives.addElement(new ToDoByDecision());
    ToDoPerspectives.addElement(new ToDoByPriority());
    ToDoPerspectives.addElement(new ToDoByGoal());
    ToDoPerspectives.addElement(new ToDoByType());
    ToDoPerspectives.addElement(new ToDoByPoster());
  }
  
  ////////////////////////////////////////////////////////////////
  // main
  
  public static void main(String args[]) {
    defineMockHistory();
    com.sun.java.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme(new uci.uml.ui.JasonsTheme());
    ProjectBrowser pb = new ProjectBrowser("ProjectBrowser");
    pb.addWindowListener(new WindowCloser());
    JOptionPane.setRootFrame(pb);
    pb.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
    Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
    pb.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    pb.setLocation(scrSize.width/2 - WIDTH/2,
		       scrSize.height/2 - HEIGHT/2);
    pb.setSize(WIDTH, HEIGHT);
    pb.show();
    pb.setProject(new MockProject());
    pb.setPerspectives(UMLPerspectives);
    pb.setToDoPerspectives(ToDoPerspectives);

    pb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    //pb.validate();
    //pb.repaint();
    //pb.requestDefaultFocus();
  }


  private static void defineMockHistory() {
    History h = History.TheHistory;
    h.addItem("In the beginning there was Argo");
    h.addItem("And then I wrote a bunch of papers");
    h.addItem("Now there is ArgoUML!");
  }
  
} /* end class Main */


class WindowCloser extends WindowAdapter {
  public WindowCloser() { }
  public void windowClosing(WindowEvent e) { System.exit(0); }
};

class MockProject extends Project {
  
  public MockProject() {
    super("MockProject");
    _diagrams.addElement(makeDiagram());
    _models.addElement(makeModel());
  }

  public uci.gef.LayerDiagram makeDiagram() {
    return new uci.gef.LayerDiagram();
  }

  public Model makeModel() {
    GraphicsExample ge = new GraphicsExample();
    return ge.model;
  }

} /* end class MockProject */
