// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products may
// be obtained by contacting the University of California. David F. Redmiles
// Department of Information and Computer Science (ICS) University of
// California Irvine, California 92697-3425 Phone: 714-824-3823. This software
// program and documentation are copyrighted by The Regents of the University
// of California. The software program and documentation are supplied "as is",
// without any accompanying services from The Regents. The Regents do not
// warrant that the operation of the program will be uninterrupted or
// error-free. The end-user understands that the program was developed for
// research purposes and is advised not to rely exclusively on the program for
// any reason. IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
// PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES,
// INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
// DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY
// DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE
// SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
// ENHANCEMENTS, OR MODIFICATIONS.


package uci.uml.ui;

import java.util.*;
import java.beans.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.util.*;
import uci.argo.kernel.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Model_Management.*;
import uci.uml.visual.*;

public class Actions {

  static Vector _allActions = new Vector(100);
  

  public static UMLAction New = new ActionNew();
  public static UMLAction Open = new ActionOpen();
  public static UMLAction Save = new ActionSave();
  public static UMLAction SaveAs = new ActionSaveAs();
  public static UMLAction AddToProj = new ActionAddToProj();
  public static UMLAction Print = new ActionPrint();
  public static UMLAction Exit = new ActionExit();

  public static UMLAction Undo = new ActionUndo();
  public static UMLAction Redo = new ActionRedo();
  public static UMLAction Cut = new ActionCut();
  public static UMLAction Copy = new ActionCopy();
  public static UMLAction Paste = new ActionPaste();

  public static UMLAction NavUp = new ActionNavUp();
  public static UMLAction NavDown = new ActionNavDown();
  public static UMLAction NextTab = new ActionNextTab();
  public static UMLAction PrevTab = new ActionPrevTab();
  public static UMLAction ShowDiagramTab = new ActionShowDiagramTab();
  public static UMLAction ShowTableTab = new ActionShowTableTab();
  public static UMLAction ShowTextTab = new ActionShowTextTab();
  public static UMLAction AddToFavs = new ActionAddToFavs();
  //public static UMLAction DefinePerspective = new ActionDefinePerspective();

  
  public static UMLAction CreateMultiple = new ActionCreateMultiple();
  public static UMLAction ClassWizard = new ActionClassWizard();

  public static UMLAction Model = new ActionModel();
  public static UMLAction ClassDiagram = new ActionClassDiagram();
  public static UMLAction UseCaseDiagram = new ActionUseCaseDiagram();
  public static UMLAction StateDiagram = new ActionStateDiagram();

  public static UMLAction Class = new ActionClass();
  public static UMLAction Interface = new ActionInterface();
  public static UMLAction Actor = new ActionActor();
  public static UMLAction UseCase = new ActionUseCase();
  public static UMLAction State = new ActionState();
  public static UMLAction Pseudostate = new ActionPseudostate();
  public static UMLAction Package = new ActionPackage();
  public static UMLAction Instance = new ActionInstance();
  public static UMLAction Attr = new ActionAttr();
  public static UMLAction Oper = new ActionOper();

  public static UMLAction AboutArgoUML = new ActionAboutArgoUML();
  

  public static UMLAction AutoCritique = new ActionAutoCritique();
  public static UMLAction OpenDecisions = new ActionOpenDecisions();
  public static UMLAction OpenGoals = new ActionOpenGoals();
  public static UMLAction OpenCritics = new ActionOpenCritics();

  public static UMLAction NewToDoItem = new ActionNewToDoItem();
  public static UMLAction Resolve = new ActionResolve();
  public static UMLAction EmailExpert = new ActionEmailExpert();
  public static UMLAction MoreInfo = new ActionMoreInfo();
  public static UMLAction Hush = new ActionHush();
  public static UMLAction FixItNext = new ActionFixItNext();
  public static UMLAction FixItBack = new ActionFixItBack();
  public static UMLAction FixItFinish = new ActionFixItFinish();


  public static void updateAllEnabled() {
    java.util.Enumeration actions = _allActions.elements();
    while (actions.hasMoreElements()) {
      UMLAction a = (UMLAction) actions.nextElement();
      a.updateEnabled();
    }
  }

  
}  /* end class Actions */


class UMLAction extends AbstractAction {

  public UMLAction(String name) { this(name, true); }
  
  public UMLAction(String name, boolean global) { 
    super(name, loadIconResource(imageName(name), name));
    putValue(Action.SHORT_DESCRIPTION, name);
    if (global) Actions._allActions.addElement(this);
  }

  protected static ImageIcon loadIconResource(String imgName, String desc) {
    ImageIcon res = null;
    try {
      java.net.URL imgURL = UMLAction.class.getResource(imgName);
      return new ImageIcon(imgURL, desc);
    }
    catch (Exception ex) {
      return new ImageIcon(desc);
    }
  }

  protected static String imageName(String name) {
    return "/uci/Images/" + stripJunk(name) + ".gif";
  }
  

  /** Perform the work the action is supposed to do. */
  // needs-more-work: should actions run in their own threads?
  public void actionPerformed(ActionEvent e) {
    System.out.println("pushed " + getValue(Action.NAME));
    StatusBar sb = ProjectBrowser.TheInstance.getStatusBar();
    sb.doFakeProgress(stripJunk(getValue(Action.NAME).toString()), 100);
    History.TheHistory.addItem("pushed " + getValue(Action.NAME));
    Actions.updateAllEnabled();
  }


  public void updateEnabled(Object target) { setEnabled(shouldBeEnabled()); }
  public void updateEnabled() { setEnabled(shouldBeEnabled()); }

  /** return true if this action should be available to the user. This
   *  method should examine the ProjectBrowser that owns it.  Sublass
   *  implementations of this method should always call
   *  super.shouldBeEnabled first. */
  public boolean shouldBeEnabled() { return true; }


  protected static String stripJunk(String s) {
    String res = "";
    int len = s.length();
    for (int i = 0; i < len; i++) {
      char c = s.charAt(i);
      if (Character.isJavaLetterOrDigit(c)) res += c;
    }
    return res;
  }
} /* end class UMLAction */

////////////////////////////////////////////////////////////////
// file menu actions

class ActionNew extends UMLAction {
  public ActionNew() { super("New..."); }
} /* end class ActionNew */

class ActionOpen extends UMLAction {
  public ActionOpen() { super("Open..."); }
} /* end class ActionOpen */

class ActionSave extends UMLAction {
  public ActionSave() { super("Save"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null && p.getNeedsSave();
  }
} /* end class ActionSave */

class ActionSaveAs extends UMLAction {
  public ActionSaveAs() { super("Save As..."); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionSaveAS */

class ActionPrint extends UMLAction {
  public ActionPrint() { super("Print..."); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionPrint */

class ActionAddToProj extends UMLAction {
  public ActionAddToProj() { super("Add To Project..."); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionAddToProj */

class ActionExit extends UMLAction {
  public ActionExit() { super("Exit"); }

  public void actionPerformed(ActionEvent e) {
    //needs-more-work: are you sure you want to quit?
    System.exit(0);
  }
} /* end class ActionExit */

////////////////////////////////////////////////////////////////
// generic editing actions

class ActionUndo extends UMLAction {
  public ActionUndo() { super("Undo"); }
} /* end class ActionUndo */

class ActionRedo extends UMLAction {
  public ActionRedo() { super("Redo"); }
} /* end class ActionRedo */

class ActionCut extends UMLAction {
  public ActionCut() { super("Cut"); }
} /* end class ActionCut */

class ActionCopy extends UMLAction {
  public ActionCopy() { super("Copy"); }
} /* end class ActionCopy */

class ActionPaste extends UMLAction {
  public ActionPaste() { super("Paste"); }
} /* end class ActionPaste */


////////////////////////////////////////////////////////////////
// navigation actions

class ActionNavUp extends UMLAction {
  public ActionNavUp() { super("Navigate Up"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionNavUp */

class ActionNavDown extends UMLAction {
  public ActionNavDown() { super("Navigate Down"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionNavDown */

class ActionPrevTab extends UMLAction {
  public ActionPrevTab() { super("Previous Tab"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionPrevTab */

class ActionNextTab extends UMLAction {
  public ActionNextTab() { super("Next Tab"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionNextTab */

class ActionShowDiagramTab extends UMLAction {
  public ActionShowDiagramTab() { super("Show Diagram Tab"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionShowDiagramTab */

class ActionShowTableTab extends UMLAction {
  public ActionShowTableTab() { super("Show Table Tab"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionShowTableTab */

class ActionShowTextTab extends UMLAction {
  public ActionShowTextTab() { super("Show Text Tab"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionShowTextTab */

class ActionAddToFavs extends UMLAction {
  public ActionAddToFavs() { super("Add To Favorites"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionAddToFavs */


class ActionCreateMultiple extends UMLAction {
  public ActionCreateMultiple() { super("Create Multiple..."); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionCreateMultiple */

class ActionClassWizard extends UMLAction {
  public ActionClassWizard() { super("Class Wizard..."); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionClassWizard */

////////////////////////////////////////////////////////////////
// diagram creation actions

class ActionClassDiagram extends UMLAction {
  public ActionClassDiagram() { super("ClassDiagram"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making class diagram...");
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      p.addDiagram(new UMLClassDiagram(p.getCurrentModel()));
    }
    catch (PropertyVetoException pve) { }
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionClassDiagram */

class ActionUseCaseDiagram extends UMLAction {
  public ActionUseCaseDiagram() { super("UseCaseDiagram"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making use case diagram...");
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      p.addDiagram(new UMLUseCaseDiagram(p.getCurrentModel()));
    }
    catch (PropertyVetoException pve) { }
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionUseCaseDiagram */

class ActionStateDiagram extends UMLAction {
  public ActionStateDiagram() { super("StateDiagram"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making state diagram...");
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      p.addDiagram(new UMLStateDiagram(p.getCurrentModel()));
    }
    catch (PropertyVetoException pve) { }
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionStateDiagram */


////////////////////////////////////////////////////////////////
// model element creation actions

class ActionClass extends UMLAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(MMClass.class, "Class");
  
  public ActionClass() { super("Class"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making class...");
    _cmdCreateNode.doIt();
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionClass */

class ActionInterface extends UMLAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Interface.class, "Interface");

  public ActionInterface() { super("Interface"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making interface...");
    _cmdCreateNode.doIt();
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionInterface */

class ActionActor extends UMLAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Actor.class, "Actor");

  public ActionActor() { super("Actor"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making actor...");
    _cmdCreateNode.doIt();
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionActor */

class ActionUseCase extends UMLAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(UseCase.class, "UseCase");

  public ActionUseCase() { super("UseCase"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making Use Case...");
    _cmdCreateNode.doIt();
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionUseCase */

class ActionState extends UMLAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(State.class, "State");

  public ActionState() { super("State"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making state...");
    _cmdCreateNode.doIt();
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionState */

class ActionPseudostate extends UMLAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Pseudostate.class, "Pseudostate");

  public ActionPseudostate() { super("Pseudostate"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making Pseudostate...");
    _cmdCreateNode.doIt();
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionPseudostate */

class ActionPackage extends UMLAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Subsystem.class, "Package");

  public ActionPackage() { super("Package"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making Package...");
    _cmdCreateNode.doIt();
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionPackage */

class ActionInstance extends UMLAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Instance.class, "Instance");

  public ActionInstance() { super("Instance"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making Instance...");
    _cmdCreateNode.doIt();
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionInstance */

class ActionAttr extends UMLAction {
  // needs-more-work: should be part of java binding or common elements
  public static DataType INT_TYPE = new DataType("int");

  public ActionAttr() { super("Attribute"); }

  public void actionPerformed(ActionEvent e) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getTarget();
    if (!(target instanceof Classifier)) return;
    Classifier cls = (Classifier) target;
    try {
      Attribute attr = new Attribute("newAttr", INT_TYPE, "0");
      attr.setVisibility(VisibilityKind.PUBLIC);
      cls.addStructuralFeature(attr);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in ActionOper");
    }
  }

  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getTarget();
    return super.shouldBeEnabled() && target instanceof Classifier;
  }
} /* end class ActionAttr */

class ActionOper extends UMLAction {
  // needs-more-work: should be part of java binding or common elements
  public static DataType VOID_TYPE = new DataType("void");

  public ActionOper() { super("Operation"); }

  public void actionPerformed(ActionEvent e) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getTarget();
    if (!(target instanceof Classifier)) return;
    Classifier cls = (Classifier) target;
    try {
      Operation oper = new Operation(VOID_TYPE, "newOperation");
      oper.setVisibility(VisibilityKind.PUBLIC);
      cls.addBehavioralFeature(oper);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in ActionOper");
    }
  }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getTarget();
    return super.shouldBeEnabled() && target instanceof Classifier;
  }
} /* end class ActionOper */


class ActionModel extends UMLAction {
  //uci.gef.Cmd _cmdCreateNode = new
  //uci.gef.CmdCreateNode(uci.uml.Model_Management.Model.class, "Model");
  // needs-more-work: need FigModel and UMLPackageDiagram
  public ActionModel() { super("Model"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("making model...");
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      p.addModel(new Model());
    }
    catch (PropertyVetoException pve) { }
  }

  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionModel */

////////////////////////////////////////////////////////////////
// general user interface actions

class ActionAboutArgoUML extends UMLAction {
  public ActionAboutArgoUML() { super("About Argo/UML"); }

  public void actionPerformed(ActionEvent e) {
    System.out.println("opening about box...");
    Project p = ProjectBrowser.TheInstance.getProject();
  }
  public boolean shouldBeEnabled() { return true; }
} /* end class ActionAboutArgoUML */


////////////////////////////////////////////////////////////////
// critiquing related actions

class ActionAutoCritique extends UMLAction {
  public ActionAutoCritique() { super("Toggle Auto-Critique"); }
  public void actionPerformed(ActionEvent e) {
    super.actionPerformed(e);
    Designer d = Designer.TheDesigner;
    boolean b = d.getAutoCritique();
    d.setAutoCritique(!b);
    System.out.println("setting autoCritique to " + !b);
  }
} /* end class ActionAutoCritique */

class ActionOpenDecisions extends UMLAction {
  public ActionOpenDecisions() { super("Design Issues..."); }
  public void actionPerformed(ActionEvent e) {
    DesignIssuesDialog d = new DesignIssuesDialog(ProjectBrowser.TheInstance);
    d.show();
  }
} /* end class ActionOpenDecisions */

class ActionOpenGoals extends UMLAction {
  public ActionOpenGoals() { super("Design Goals..."); }
  public void actionPerformed(ActionEvent e) {
    GoalsDialog d = new GoalsDialog(ProjectBrowser.TheInstance);
    d.show();
  }  
} /* end class ActionOpenGoals */

class ActionOpenCritics extends UMLAction {
  public ActionOpenCritics() { super("Browse Critics..."); }
} /* end class ActionOpenCritics */


class ActionNewToDoItem extends UMLAction {
  public ActionNewToDoItem() { super("New To Do Item..."); }
  public void actionPerformed(ActionEvent e) {
    AddToDoItemDialog dialog = new AddToDoItemDialog();
    dialog.show();
  }
} /* end class ActionNewToDoItem */

class ToDoItemAction extends UMLAction {
  Object _target = null;
  public ToDoItemAction(String name) { super(name, false); }

  public void updateEnabled(Object target) {
    _target = target;
    setEnabled(shouldBeEnabled(target));
  }
  
  public boolean shouldBeEnabled(Object target) {
    return target instanceof ToDoItem;
  }  
}

class ActionFixItNext extends ToDoItemAction {
  public ActionFixItNext() { super("Fix It Next..."); }
} /* end class ActionFixItNext */

class ActionFixItBack extends ToDoItemAction {
  public ActionFixItBack() { super("Fix It Back..."); }
} /* end class ActionFixItBack */

class ActionFixItFinish extends ToDoItemAction {
  public ActionFixItFinish() { super("Fix It Finish..."); }
} /* end class ActionFixItFinish */

class ActionResolve extends ToDoItemAction {
  public ActionResolve() { super("Resolve Item..."); }
  public void actionPerformed(ActionEvent e) {
    DismissToDoItemDialog dialog = new DismissToDoItemDialog();
    dialog.setTarget(_target);
    dialog.show();
  }
} /* end class ActionResolve */

class ActionEmailExpert extends ToDoItemAction {
  public ActionEmailExpert() { super("Send Email To Expert..."); }
  public void actionPerformed(ActionEvent e) {
    EmailExpertDialog dialog = new EmailExpertDialog();
    dialog.setTarget(_target);
    dialog.show();
  }
  
} /* end class ActionEmailExpert */

class ActionMoreInfo extends ToDoItemAction {
  public ActionMoreInfo() { super("More Info..."); }
} /* end class ActionMoreInfo */

class ActionHush extends ToDoItemAction {
  public ActionHush() { super("Hush Critic"); }
} /* end class ActionHush */

