package uci.uml;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.beans.*;
import com.sun.java.swing.*;

import uci.util.*;
import uci.uml.ui.*;
import uci.uml.visual.*;
import uci.uml.critics.*;
import uci.uml.Model_Management.Model;
import uci.uml.test.omg.*;

import uci.argo.kernel.*;


public class Main {
  ////////////////////////////////////////////////////////////////
  // constants

  public static int WIDTH = 800;
  public static int HEIGHT = 600;
  public static int SPLASH_WIDTH = 400;
  public static int SPLASH_HEIGHT = 200;

  ////////////////////////////////////////////////////////////////
  // static variables

  public static Vector UMLPerspectives = new Vector();
  public static Vector ToDoPerspectives = new Vector();

  // static initializer
  static {
    UMLPerspectives.addElement(new NavPackageCentric());
    UMLPerspectives.addElement(new NavDiagramCentric());
    UMLPerspectives.addElement(new NavInheritance());

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
    //pb.setSize(INITIAL_WIDTH, INITIAL_HEIGHT);
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

    Designer dsgr = Designer.theDesigner();
    uci.uml.critics.Init.init();


    //should be done in ProjectBrowser.setProject();
    Designer.theDesigner().spawnCritiquer(pb.getProject());
    Designer.theDesigner().setChildGenerator(new ChildGenUML());
    System.out.println("spawned");

    // should be in logon wizard?
    dsgr.startConsidering(uci.uml.critics.CrUML.decINHERITANCE);
    dsgr.startConsidering(uci.uml.critics.CrUML.decCONTAINMENT);

  }


  private static void defineMockHistory() {
    History h = History.TheHistory;
    h.addItem("In the beginning there was Argo");
    h.addItem("And then I wrote a bunch of papers");
    h.addItem("Now there is ArgoUML!");
  }

} /* end Class Main */


class WindowCloser extends WindowAdapter {
  public WindowCloser() { }
  public void windowClosing(WindowEvent e) { System.exit(0); }
};

class MockProject extends Project {

  public MockProject() {
    super("MockProject");
    Model m1 = makeModel1();
    Model m2 = makeModel2();
    try {
      addDiagram(makeDiagram(m1));
      addDiagram(makeDiagram(m2));
      addModel(m1);
      addModel(m2);
    }
    catch (PropertyVetoException pve) { }
  }

  public UMLDiagram makeDiagram(Model m) {
    return new UMLClassDiagram(m);
  }

  public Model makeModel1() {
    GraphicsExample ge = new GraphicsExample();
    return ge.model;
  }

  public Model makeModel2() {
    ShapesExample she = new ShapesExample();
    return she.model;
  }

} /* end class MockProject */
