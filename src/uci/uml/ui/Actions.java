package uci.uml.ui;

import java.util.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;

import uci.util.*;
import uci.argo.kernel.*;

class UMLAction extends AbstractAction {

  protected static Vector _allActions = new Vector(100);
  
  public UMLAction(String name) { this(name, true); }
  
  public UMLAction(String name, boolean global) { 
    super(name, loadIconResource(imageName(name), name));
    putValue(Action.SHORT_DESCRIPTION, name);
    if (global) _allActions.addElement(this);
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
    return "Images/" + stripJunk(name) + ".gif";
  }
  
  public static void updateAllEnabled() {
    Enumeration actions = _allActions.elements();
    while (actions.hasMoreElements()) {
      UMLAction a = (UMLAction) actions.nextElement();
      a.updateEnabled();
    }
  }

  /** Perform the work the action is supposed to do. */
  // needs-more-work: should actions run in their own threads?
  public void actionPerformed(ActionEvent e) {
    System.out.println("pushed " + getValue(Action.NAME));
    StatusBar sb = ProjectBrowser.TheInstance.getStatusBar();
    sb.doFakeProgress(stripJunk(getValue(Action.NAME).toString()), 100);
    History.TheHistory.addItem("pushed " + getValue(Action.NAME));
    UMLAction.updateAllEnabled();
  }


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

class ActionDelete extends UMLAction {
  public ActionDelete() { super("Delete"); }
} /* end class ActionDelete */


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

class ActionClass extends UMLAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(uci.gef.demo.SampleNode2.class, "SampleNode2");
  
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
  public ActionOpenDecisions() { super("Open Decisions..."); }
} /* end class ActionOpenDecisions */

class ActionOpenGoals extends UMLAction {
  public ActionOpenGoals() { super("Open Goals..."); }
} /* end class ActionOpenGoals */

class ActionOpenCritics extends UMLAction {
  public ActionOpenCritics() { super("Open Critics..."); }
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

class ActionFixIt extends ToDoItemAction {
  public ActionFixIt() { super("Fix It..."); }
} /* end class ActionFixIt */

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
  public ActionMoreInfo() { super("Mode Info..."); }
} /* end class ActionMoreInfo */

class ActionHush extends ToDoItemAction {
  public ActionHush() { super("Hush Critic"); }
} /* end class ActionHush */

