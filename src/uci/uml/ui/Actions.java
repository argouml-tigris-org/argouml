// Copyright (c) 1996-98 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation for educational, research and non-profit
// purposes, without fee, and without a written agreement is hereby granted,
// provided that the above copyright notice and this paragraph appear in all
// copies. Permission to incorporate this software into commercial products
// must be negotiated with University of California. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "as is",
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
import java.io.*;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import com.sun.java.swing.*;
import com.sun.java.swing.event.*;
import com.sun.java.swing.tree.*;
import com.sun.java.swing.preview.*;
import com.sun.java.swing.preview.filechooser.*;

import uci.util.*;
import uci.gef.*;
import uci.graph.*;
import uci.argo.kernel.*;
import uci.uml.Foundation.Core.*;
import uci.uml.Foundation.Data_Types.*;
import uci.uml.Behavioral_Elements.Common_Behavior.*;
import uci.uml.Behavioral_Elements.State_Machines.*;
import uci.uml.Behavioral_Elements.Use_Cases.*;
import uci.uml.Behavioral_Elements.Collaborations.*;
import uci.uml.Model_Management.*;
import uci.uml.visual.*;
import uci.uml.generate.*;
import uci.uml.xmi.*;
import uci.uml.ocl.*;

public class Actions {

  static Vector _allActions = new Vector(100);

  public static UMLAction New = new ActionNew();
  public static UMLAction Open = new ActionOpen();
  public static UMLAction OpenXMI = new ActionOpenXMI();
  public static UMLAction Save = new ActionSave();
  public static UMLAction SaveAs = new ActionSaveAs();
  public static UMLAction SaveAsXMI = new ActionSaveAsXMI();
  public static UMLAction AddToProj = new ActionAddToProj();
  public static UMLAction Print = new ActionPrint();
  public static UMLAction Exit = new ActionExit();

  public static UMLAction Undo = new ActionUndo();
  public static UMLAction Redo = new ActionRedo();
  public static UMLAction Cut = new ActionCut();
  public static UMLAction Copy = new ActionCopy();
  public static UMLAction Paste = new ActionPaste();
  public static UMLAction DeleteFromDiagram = new ActionDeleteFromDiagram();
  public static UMLAction RemoveFromModel = new ActionRemoveFromModel();
  public static UMLAction EmptyTrash = new ActionEmptyTrash();

  public static UMLAction NavUp = new ActionNavUp();
  public static UMLAction NavDown = new ActionNavDown();
  public static UMLAction NavBack = new ActionNavBack();
  public static UMLAction NavForw = new ActionNavForw();
  public static UMLAction NavFavs = new ActionNavFavs();
  public static UMLAction NavConfig = new ActionNavConfig();

  public static UMLAction Find = new ActionFind();
  public static UMLAction GotoDiagram = new ActionGotoDiagram();

  public static UMLAction NextEditTab = new ActionNextEditTab();
  public static UMLAction AddToFavs = new ActionAddToFavs();
  public static UMLAction NextDetailsTab = new ActionNextDetailsTab();

  public static UMLAction CreateMultiple = new ActionCreateMultiple();
  public static UMLAction ClassWizard = new ActionClassWizard();

  //public static UMLAction Model = new ActionModel();
  public static UMLAction ClassDiagram = new ActionClassDiagram();
  public static UMLAction UseCaseDiagram = new ActionUseCaseDiagram();
  public static UMLAction StateDiagram = new ActionStateDiagram();
  public static UMLAction ActivityDiagram = new ActionActivityDiagram();
  public static UMLAction CollaborationDiagram = new ActionCollaborationDiagram();

  //public static UMLAction Class = new ActionClass();
  //public static UMLAction Interface = new ActionInterface();
  //public static UMLAction Actor = new ActionActor();
  //public static UMLAction UseCase = new ActionUseCase();
  //public static UMLAction State = new ActionState();
  //public static UMLAction Pseudostate = new ActionPseudostate();
  //public static UMLAction Package = new ActionPackage();
  //public static UMLAction Instance = new ActionInstance();
  public static UMLAction Attr = new ActionAttr();
  public static UMLAction Oper = new ActionOper();
  public static UMLAction Message = new ActionMessage();
  public static UMLAction InternalTransition = new ActionInternalTransition();

  public static UMLAction GenerateOne = new ActionGenerateOne();
  public static UMLAction GenerateAll = new ActionGenerateAll();
  public static UMLAction GenerateWeb = new ActionGenerateWeb();


  public static UMLAction AutoCritique = new ActionAutoCritique();
  public static UMLAction OpenDecisions = new ActionOpenDecisions();
  public static UMLAction OpenGoals = new ActionOpenGoals();
  public static UMLAction OpenCritics = new ActionOpenCritics();

  public static UMLAction FlatToDo = new ActionFlatToDo();

  public static UMLAction NewToDoItem = new ActionNewToDoItem();
  public static UMLAction Resolve = new ActionResolve();
  public static UMLAction EmailExpert = new ActionEmailExpert();
  public static UMLAction MoreInfo = new ActionMoreInfo();
  public static UMLAction Snooze = new ActionSnooze();

  public static UMLAction RecordFix = new ActionRecordFix();
  public static UMLAction ReplayFix = new ActionReplayFix();

  //   public static UMLAction FixItNext = new ActionFixItNext();
  //   public static UMLAction FixItBack = new ActionFixItBack();
  //   public static UMLAction FixItFinish = new ActionFixItFinish();

  public static UMLAction AboutArgoUML = new ActionAboutArgoUML();




  public static void updateAllEnabled() {
    java.util.Enumeration actions = _allActions.elements();
    while (actions.hasMoreElements()) {
      UMLAction a = (UMLAction) actions.nextElement();
      a.updateEnabled();
    }
  }

}  /* end class Actions */


class UMLChangeAction extends UMLAction {

  public UMLChangeAction(String s) { super(s); }

  public void actionPerformed(ActionEvent e) {
    markNeedsSave();
    Actions.updateAllEnabled();
  }
} /* end class UMLChangeAction */


////////////////////////////////////////////////////////////////
// file menu actions

class ActionNew extends UMLAction {
  public ActionNew() { super("New..."); }
  public void actionPerformed(ActionEvent e) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    if (p != null) { // && p.getNeedsSave()) {
      String t = "Save changes to " + p.getName();
      int response =
	JOptionPane.showConfirmDialog(pb, t, t,
				      JOptionPane.YES_NO_CANCEL_OPTION);
      if (response == JOptionPane.CANCEL_OPTION) return;
      if (response == JOptionPane.YES_OPTION)
	if (!((ActionSave)Actions.Save).trySave()) return;
      //needs-more-work: if you cancel the save it should cancel open
    }
    p = Project.makeEmptyProject();
    pb.setProject(p);
  }
} /* end class ActionNew */

class ActionOpen extends UMLAction {
  public ActionOpen() { super("Open..."); }
  public void actionPerformed(ActionEvent e) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    if (p != null) { // && p.getNeedsSave()) {
      String t = "Save changes to " + p.getName();
      int response =
	JOptionPane.showConfirmDialog(pb, t, t,
				      JOptionPane.YES_NO_CANCEL_OPTION);
      if (response == JOptionPane.CANCEL_OPTION) return;
      if (response == JOptionPane.YES_OPTION)
	if (!((ActionSave)Actions.Save).trySave()) return;
    }

    Hashtable stats;

    try {
      JFileChooser chooser;
      if (p != null && p.getPathname() != null && p.getPathname().length()>0) {
	System.out.println("open initial path=" + p.getPathname());
	chooser = new JFileChooser(p.getPathname());
      }
      else chooser = new JFileChooser();
      
      chooser.setDialogTitle("Open Project...");
      FileFilter filter = FileFilters.ArgoFilter;
      chooser.addChoosableFileFilter(filter);
      chooser.setFileFilter(filter);
      int retval = chooser.showOpenDialog(pb);
      if(retval == 0) {
	File theFile = chooser.getSelectedFile();
	if(theFile != null) {
	  String pathname = chooser.getSelectedFile().getAbsolutePath();
    	pb.showStatus("Reading " + pathname + "...");
    	FileInputStream fis = new FileInputStream(pathname);
    	ObjectInput s = new ObjectInputStream(fis);
	stats = (Hashtable) s.readObject();
    	p = (Project) s.readObject();
	p.postLoad();
	DocumentationManager._docs = (Hashtable) s.readObject();
	if (fis != null) fis.close();
    	pb.showStatus("Read " + pathname);
	pb.setProject(p);
	p.setStats(stats);
	return;
	}
      }
    }
//     try {
//       JFileChooser chooser = new JFileChooser();
//       ArgoFileFilter filter = new ArgoFileFilter();
//       chooser.addChoosableFileFilter(filter);
//       chooser.setFileFilter(filter);
//       int retval = chooser.showOpenDialog(pb);
//       if(retval == 0) {
// 	File theFile = chooser.getSelectedFile();
// 	if(theFile != null) {
// 	  String pathname = chooser.getSelectedFile().getAbsolutePath();
//     	pb.showStatus("Reading " + pathname + "...");
//     	FileInputStream fis = new FileInputStream(pathname);
//     	ObjectInput s = new ObjectInputStream(fis);
//     	Project p = (Project) s.readObject();
// 	p.postLoad();
// 	if (fis != null) fis.close();
//     	pb.showStatus("Read " + pathname);
// 	pb.setProject(p);
// 	return;
// 	}
//       }
//     }
    catch (FileNotFoundException ignore) {
      System.out.println("got an FileNotFoundException");
     }
    catch (java.lang.ClassNotFoundException ignore) {
      System.out.println("got an ClassNotFoundException");
     }
    catch (IOException ignore) {
      System.out.println("got an IOException");
    }
  }
} /* end class ActionOpen */

class ActionOpenXMI extends UMLAction {
  public ActionOpenXMI() { super("Open XMI..."); }
  public void actionPerformed(ActionEvent e) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    if (p != null) { // && p.getNeedsSave()) {
      String t = "Save changes to " + p.getName();
      int response =
	JOptionPane.showConfirmDialog(pb, t, t,
				      JOptionPane.YES_NO_CANCEL_OPTION);
      if (response == JOptionPane.CANCEL_OPTION) return;
      if (response == JOptionPane.YES_OPTION)
	if (!((ActionSave)Actions.Save).trySave()) return;
    }

    //    try {
      JFileChooser chooser;
      if (p != null && p.getPathname() != null && p.getPathname().length()>0) {
	System.out.println("open initial path=" + p.getPathname());
	chooser = new JFileChooser(p.getPathname());
      }
      else chooser = new JFileChooser();

      chooser.setDialogTitle("Open Project...");
      FileFilter filter = FileFilters.XMIFilter;
      chooser.addChoosableFileFilter(filter);
      chooser.setFileFilter(filter);
      int retval = chooser.showOpenDialog(pb);
      if(retval == 0) {
	File theFile = chooser.getSelectedFile();
	if(theFile != null) {
	  String pathname = chooser.getSelectedFile().getAbsolutePath();
    	System.out.println("Reading " + pathname + "...");
	pb.showStatus("Reading " + pathname + "...");
	XMIParserIBM.SINGLETON.readModels("", pathname);
	//p.postLoad();
    	pb.showStatus("Read " + pathname);
	//p.setStats(stats);
	return;
	}
      }
//     }
//     catch (FileNotFoundException ignore) {
//       System.out.println("got an FileNotFoundException");
//      }
//     catch (java.lang.ClassNotFoundException ignore) {
//       System.out.println("got an ClassNotFoundException");
//      }
//     catch (IOException ignore) {
//       System.out.println("got an IOException");
//     }
  }
} /* end class ActionOpenXMI */

class ActionSave extends UMLAction {
  protected static Hashtable _templates = new Hashtable();

  static {
    _templates = TemplateReader.readFile("/uci/dtd/XMI.tee");
  }

  public ActionSave() { super("Save"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null; // && p.getNeedsSave();
  }
  public void actionPerformed(ActionEvent ae) {
    // needs-more-work: should just save
    trySave();
  }
  public boolean trySave() {
    // needs-more-work: should just save
    return ((ActionSaveAs)Actions.SaveAs).trySave();
  }
} /* end class ActionSave */

class ActionSaveAs extends UMLAction {
  public ActionSaveAs() { super("Save As..."); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
  public void actionPerformed(ActionEvent ae) {
    trySave();
  }

  public boolean trySave() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p =  pb.getProject();
    try {
      JFileChooser chooser;
      if (p != null && p.getPathname() != null && p.getPathname().length()>0) {
	chooser  = new JFileChooser(p.getPathname());
      }
      else chooser = new JFileChooser();

      chooser.setDialogTitle("Save " + p.getName());
      FileFilter filter = FileFilters.ArgoFilter;
      chooser.addChoosableFileFilter(filter);
      chooser.setFileFilter(filter);
      Hashtable stats = p.getStats();

      int retval = chooser.showSaveDialog(pb);
      if(retval == 0) {
	File theFile = chooser.getSelectedFile();
	if (theFile != null) {
	  String pathname = chooser.getSelectedFile().getAbsolutePath();
	  pb.showStatus("Writing " + pathname + "...");
	  p.setPathname(chooser.getSelectedFile().getParent());
	  String name = chooser.getSelectedFile().getName();
	  if (!name.endsWith(".argo")) name += ".argo";
	  if (!pathname.endsWith(".argo")) pathname += ".argo";
	  p.setName(name);
	  FileOutputStream fos = new FileOutputStream(pathname);
	  ObjectOutput oo = new ObjectOutputStream(fos);
	  p.preSave();
	  oo.writeObject(stats);
	  oo.writeObject(p);
	  oo.writeObject(DocumentationManager._docs);
	  p.postSave();
	  if (fos != null) fos.close();
	  pb.showStatus("Wrote " + pathname);
	  pb.updateTitle();
	  return true;
	}
      }
    }
//     try {
//       JFileChooser chooser = new JFileChooser();
//       ArgoFileFilter filter = new ArgoFileFilter();
//       chooser.addChoosableFileFilter(filter);
//       chooser.setFileFilter(filter);
//       int retval = chooser.showSaveDialog(pb);
//       if(retval == 0) {
// 	File theFile = chooser.getSelectedFile();
// 	if (theFile != null) {
// 	  String pathname = chooser.getSelectedFile().getAbsolutePath();
// 	  pb.showStatus("Writing " + pathname + "...");
// 	  FileOutputStream fos = new FileOutputStream(pathname);
// 	  ObjectOutput oo = new ObjectOutputStream(fos);
// 	  p.preSave();
// 	  oo.writeObject(p);
// 	  p.postSave();
// 	  if (fos != null) fos.close();
// 	  pb.showStatus("Wrote " + pathname);
// 	  return true;
// 	}
//       }
//     }
    catch (FileNotFoundException ignore) {
      System.out.println("got an FileNotFoundException");
    }
    catch (PropertyVetoException ignore) {
      System.out.println("got an PropertyVetoException in SaveAs");
    }
//     catch (PropertyVetoException ignore) {
//       System.out.println("got an PropertyVetoException");
//     }
    //    catch (java.lang.ClassMismatchException ignore) {
    //      System.out.println("got an ClassMismatchException");
    //    }
    catch (IOException ignore) {
      System.out.println("got an IOException");
      ignore.printStackTrace();
    }
    return false;
  }
} /* end class ActionSaveAS */

class ActionSaveAsXMI extends UMLAction {
  protected static OCLExpander expander = null;
  public ActionSaveAsXMI() {
    super("Save As XMI");
    Hashtable templates = TemplateReader.readFile("/uci/dtd/XMI.tee");
    expander = new OCLExpander(templates);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
  public void actionPerformed(ActionEvent e) {
    trySave();
  }

  public boolean trySave() {
    //@@@: just for rapid edig-compile-debug 
    Hashtable templates = TemplateReader.readFile("/uci/dtd/XMI.tee");
    expander = new OCLExpander(templates);

    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p =  pb.getProject();
    try {
      JFileChooser chooser;
      if (p != null && p.getPathname() != null && p.getPathname().length()>0) {
	chooser  = new JFileChooser(p.getPathname());
      }
      else chooser = new JFileChooser();

      chooser.setDialogTitle("Save " + p.getName());
      FileFilter filter = FileFilters.XMIFilter;
      chooser.addChoosableFileFilter(filter);
      chooser.setFileFilter(filter);
      Hashtable stats = p.getStats();

      int retval = chooser.showSaveDialog(pb);
      if(retval == 0) {
	File theFile = chooser.getSelectedFile();
	if (theFile != null) {
	  String pathname = chooser.getSelectedFile().getAbsolutePath();
	  pb.showStatus("Writing " + pathname + "...");
	  p.setPathname(chooser.getSelectedFile().getParent());
	  String name = chooser.getSelectedFile().getName();
	  if (!name.endsWith(".xmi")) name += ".xmi";
	  if (!pathname.endsWith(".xmi")) pathname += ".xmi";
	  p.setName(name);
	  FileWriter fw = new FileWriter(pathname);
	  p.preSave();
	  expander.expand(fw, p, "", "");
	  p.postSave();
	  fw.close();
	  pb.showStatus("Wrote " + pathname);
	  pb.updateTitle();
	  return true;
	}
      }
    }
    catch (FileNotFoundException ignore) {
      System.out.println("got an FileNotFoundException");
    }
    catch (PropertyVetoException ignore) {
      System.out.println("got an PropertyVetoException in SaveAs");
    }
//     catch (PropertyVetoException ignore) {
//       System.out.println("got an PropertyVetoException");
//     }
    //    catch (java.lang.ClassMismatchException ignore) {
    //      System.out.println("got an ClassMismatchException");
    //    }
    catch (IOException ignore) {
      System.out.println("got an IOException");
      ignore.printStackTrace();
    }
    return false;
  }
} /* end class ActionSaveAsXMI */

class ActionPrint extends UMLAction {
  public ActionPrint() { super("Print..."); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
  public void actionPerformed(ActionEvent ae) {
    CmdPrint cmd = new CmdPrint();
    Object target = ProjectBrowser.TheInstance.getTarget();
    if (target instanceof Diagram) {
      String n = ((Diagram)target).getName();
      cmd.setDiagramName(n);
      cmd.doIt();
    }
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

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    if (p != null) { // && p.getNeedsSave()) {
      String t = "Save changes to " + p.getName();
      int response =
	JOptionPane.showConfirmDialog(pb, t, t,
				      JOptionPane.YES_NO_CANCEL_OPTION);
      if (response == JOptionPane.CANCEL_OPTION) return;
      if (response == JOptionPane.YES_OPTION)
	if (!((ActionSave)Actions.Save).trySave()) return;
    }
    System.exit(0);
  }
} /* end class ActionExit */

////////////////////////////////////////////////////////////////
// generic editing actions

class ActionUndo extends UMLAction {
  public ActionUndo() { super("Undo"); }
  public boolean shouldBeEnabled() { return false; }
} /* end class ActionUndo */

class ActionRedo extends UMLAction {
  public ActionRedo() { super("Redo"); }
  public boolean shouldBeEnabled() { return false; }
} /* end class ActionRedo */

class ActionCut extends UMLAction {
  public ActionCut() { super("Cut"); }
  public boolean shouldBeEnabled() { return false; }
} /* end class ActionCut */

class ActionCopy extends UMLChangeAction {
  public ActionCopy() { super("Copy"); }
  public boolean shouldBeEnabled() {
    int size = Globals.curEditor().getSelectionManager().selections().size();
    return (size > 0);
  }
  public void actionPerformed(ActionEvent ae) {
    CmdCopy cmd = new CmdCopy();
    cmd.doIt();
    super.actionPerformed(ae);
  }
} /* end class ActionCopy */

class ActionPaste extends UMLChangeAction {
  public ActionPaste() { super("Paste"); }
  public boolean shouldBeEnabled() {
    if (Globals.clipBoard != null)
      return true;
    return false;
  }
  public void actionPerformed(ActionEvent ae) {
    CmdPaste cmd = new CmdPaste();
    cmd.doIt();
    super.actionPerformed(ae);
  }

} /* end class ActionPaste */


class ActionDeleteFromDiagram extends UMLChangeAction {
  public ActionDeleteFromDiagram() { super("Delete From Diagram"); }
  public boolean shouldBeEnabled() {
    Editor ce = Globals.curEditor();
    Vector figs = ce.getSelectionManager().getFigs();
    return figs.size() > 0;
  }
  public void actionPerformed(ActionEvent ae) {
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    sm.delete();
  }
}


class ActionRemoveFromModel extends UMLChangeAction {
  public ActionRemoveFromModel() { super("Remove From Model"); }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    if (target instanceof ModelElement) return true;

    // needs-more-work: trashing diagrams
    Editor ce = Globals.curEditor();
    Vector figs = ce.getSelectionManager().getFigs();
    int size = figs.size();
    for (int i = 0; i < size; i++) {
      Fig f = (Fig) figs.elementAt(i);
      Object owner = f.getOwner();
      if (owner instanceof ModelElement) return true;
    }
    return false;
  }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    Project p = pb.getProject();
    if (target instanceof ModelElement)
      p.moveToTrash(target);
    // needs-more-work: trashing diagrams
    else {
      Editor ce = Globals.curEditor();
      Vector figs = ce.getSelectionManager().getFigs();
      int size = figs.size();
      for (int i = 0; i < size; i++) {
	Fig f = (Fig) figs.elementAt(i);
	Object owner = f.getOwner();
	if (owner instanceof ModelElement)
	  p.moveToTrash(owner);
      }
    }
    super.actionPerformed(ae);
  }
} /* end class ActionRemoveFromModel */

class ActionEmptyTrash extends UMLChangeAction {
  public ActionEmptyTrash() { super("Empty Trash"); }
  public boolean shouldBeEnabled() {
    return Trash.SINGLETON.getSize() > 0;
  }
  public void actionPerformed(ActionEvent ae) {
    Trash.SINGLETON.emptyTrash();
    super.actionPerformed(ae);
  }

} /* end class ActionEmptyTrash */


////////////////////////////////////////////////////////////////
// navigation actions

class ActionNavUp extends UMLAction {
  public ActionNavUp() { super("Navigate Up"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
  public void actionPerformed(ActionEvent ae) {
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
    np.navUp();
  }
} /* end class ActionNavUp */

class ActionNavDown extends UMLAction {
  public ActionNavDown() { super("Navigate Down"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
  public void actionPerformed(ActionEvent ae) {
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
    np.navDown();
  }
} /* end class ActionNavDown */

class ActionFind extends UMLAction {
  public ActionFind() { super("Find..."); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
  public void actionPerformed(ActionEvent ae) {
    FindDialog.SINGLETON.setVisible(true);
  }
} /* end class ActionFind */

class ActionGotoDiagram extends UMLAction {
  public ActionGotoDiagram() { super("Goto Diagram..."); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    //needs-more-work: class TearOffHostFrame and TearOffManager.
    //idea: pop-up on tab that lists docking locations, new window.
    JFrame f = new JFrame("Goto Diagram...");
    f.getContentPane().setLayout(new BorderLayout());
    JTabbedPane tabs = new JTabbedPane();
    f.getContentPane().add(tabs, BorderLayout.CENTER);
    TabResults allDiagrams = new TabResults(false); // no related
    allDiagrams.setResults(p.getDiagrams(), p.getDiagrams());
    tabs.addTab("All Diagrams", allDiagrams);
    //needs-more-work: tabs for class, state, usecase, help
    f.setSize(500, 300);
    f.setLocation(pb.getLocation().x + 100, pb.getLocation().y + 100);
    f.setVisible(true);
  }
} /* end class ActionGotoDiagram */

class ActionNavBack extends UMLAction {
  public ActionNavBack() { super("Navigate Back"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    if (!(super.shouldBeEnabled() && p != null)) return false;
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
    return np.canNavBack();
  }
  public void actionPerformed(ActionEvent ae) {
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
    np.navBack();
  }
} /* end class ActionNavBack */

class ActionNavForw extends UMLAction {
  public ActionNavForw() { super("Navigate Forward"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    if (!(super.shouldBeEnabled() && p != null)) return false;
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
    return np.canNavForw();
  }
  public void actionPerformed(ActionEvent ae) {
    NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
    np.navForw();
  }
} /* end class ActionNavForw */

class ActionNavFavs extends UMLAction {
  public ActionNavFavs() { super("Favorites"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionNavFavs */

class ActionNavConfig extends UMLAction {
  public ActionNavConfig() { super("NavConfig"); }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    NavigatorPane nav = pb.getNavPane();
    NavigatorConfigDialog ncd = new NavigatorConfigDialog(pb);
    ncd.setVisible(true);
  }
} /* end class ActionNavConfig */

class ActionNextEditTab extends UMLAction {
  public ActionNextEditTab() { super("Next Editing Tab"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    MultiEditorPane mep = pb.getEditorPane();
    mep.selectNextTab();
  }
} /* end class ActionNextEditTab */

class ActionAddToFavs extends UMLAction {
  public ActionAddToFavs() { super("Add To Favorites"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionAddToFavs */

class ActionNextDetailsTab extends UMLAction {
  public ActionNextDetailsTab() { super("Next Details Tab"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    DetailsPane dp = pb.getDetailsPane();
    dp.selectNextTab();
  }
} /* end class ActionNextDetailsTab */

class ActionPrevDetailsTab extends UMLAction {
  public ActionPrevDetailsTab() { super("Previous Details Tab"); }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionPrevDetailsTab */

////////////////////////////////////////////////////////////////

class ActionCreateMultiple extends UMLAction {
  public ActionCreateMultiple() { super("Create Multiple..."); }
  public boolean shouldBeEnabled() {
    //Project p = ProjectBrowser.TheInstance.getProject();
    //return super.shouldBeEnabled() && p != null;
    return false;
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

class ActionClassDiagram extends UMLChangeAction {
  public ActionClassDiagram() { super("ClassDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making class diagram...");
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      Diagram d = new UMLClassDiagram(p.getCurrentModel());
      p.addDiagram(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      ProjectBrowser.TheInstance.setTarget(d);
    }
    catch (PropertyVetoException pve) { }
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionClassDiagram */

class ActionUseCaseDiagram extends UMLChangeAction {
  public ActionUseCaseDiagram() { super("UseCaseDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making use case diagram...");
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      Diagram d  = new UMLUseCaseDiagram(p.getCurrentModel());
      p.addDiagram(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      ProjectBrowser.TheInstance.setTarget(d);
    }
    catch (PropertyVetoException pve) { }
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionUseCaseDiagram */

class ActionStateDiagram extends UMLChangeAction {
  public ActionStateDiagram() { super("StateDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making state diagram...");
    //_cmdCreateNode.doIt();
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    try {
      Object contextObj = pb.getDetailsTarget();
      Name contextName = null;
      if (contextObj instanceof ModelElement) {
	contextName = ((ModelElement)contextObj).getName();
      }
      String contextNameStr = "untitled";
      if (contextName != null && !contextName.equals(Name.UNSPEC))
	contextNameStr = contextName.getBody();
      StateMachine sm = new StateMachine(contextNameStr + "States");
      CompositeState cs = new CompositeState("state_machine_top");
      if (contextObj instanceof Namespace) {
	//cs.setNamespace((Namespace)contextObj);
	//sm.setNamespace((Namespace)contextObj);
      }
      sm.setTop(cs);
      if (contextObj instanceof ModelElement)
	((ModelElement)contextObj).addBehavior(sm);
      UMLStateDiagram d = new UMLStateDiagram(p.getCurrentModel(), sm);
      p.addDiagram(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      pb.setTarget(d);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in ActionStateDiagram");
    }
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    Object target = pb.getDetailsTarget();
    return super.shouldBeEnabled() && p != null;
    //&& (target instanceof ModelElement);
  }
} /* end class ActionStateDiagram */

class ActionActivityDiagram extends UMLChangeAction {
  public ActionActivityDiagram() { super("ActivityDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making state diagram...");
    //_cmdCreateNode.doIt();
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    try {
      ModelElement context = null;
      if (pb.getDetailsTarget() instanceof ModelElement)
	context = (ModelElement) pb.getDetailsTarget();
      Name contextName = (context == null) ? null : context.getName();
      String contextNameStr = "untitled";
      if (contextName != null && !contextName.equals(Name.UNSPEC))
	contextNameStr = contextName.getBody();
      ActivityModel am = new ActivityModel(contextNameStr + "Activities");
      CompositeState cs = new CompositeState("activities_top");
      if (context instanceof Namespace) {
	//cs.setNamespace((Namespace)context);
	//am.setNamespace((Namespace)context);
      }
      am.setTop(cs);
      if (context != null) context.addBehavior(am);
      UMLActivityDiagram d = new UMLActivityDiagram(p.getCurrentModel(), am);
      p.addDiagram(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      pb.setTarget(d);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in ActionActivityDiagram");
    }
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    Object target = pb.getDetailsTarget();
    return super.shouldBeEnabled() && p != null;
    // && (target instanceof UseCase || target instanceof Operation);
  }
} /* end class ActionActivityDiagram */

class ActionCollaborationDiagram extends UMLChangeAction {
  public ActionCollaborationDiagram() { super("CollaborationDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making use case diagram...");
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      Diagram d  = new UMLCollaborationDiagram(p.getCurrentModel());
      p.addDiagram(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      ProjectBrowser.TheInstance.setTarget(d);
    }
    catch (PropertyVetoException pve) { }
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionCollaborationDiagram */


////////////////////////////////////////////////////////////////
// model element creation actions

class ActionClass extends UMLChangeAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(MMClass.class, "Class");

  public ActionClass() { super("Class"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making class...");
    _cmdCreateNode.doIt();
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionClass */

class ActionInterface extends UMLChangeAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Interface.class, "Interface");

  public ActionInterface() { super("Interface"); }

  public void actionPerformed(ActionEvent ae) {
    System.out.println("making interface...");
    _cmdCreateNode.doIt();
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionInterface */

class ActionActor extends UMLChangeAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Actor.class, "Actor");

  public ActionActor() { super("Actor"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making actor...");
    _cmdCreateNode.doIt();
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionActor */

class ActionUseCase extends UMLChangeAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(UseCase.class, "UseCase");

  public ActionUseCase() { super("UseCase"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making Use Case...");
    _cmdCreateNode.doIt();
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionUseCase */

// class ActionState extends UMLChangeAction {
//   uci.gef.Cmd _cmdCreateNode = new
//   uci.gef.CmdCreateNode(CompositeState.class, "State");

//   public ActionState() { super("State"); }

//   public void actionPerformed(ActionEvent ae) {
//     //System.out.println("making state...");
//     _cmdCreateNode.doIt();
//     super.actionPerformed(ae);
//   }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionState */

class ActionInternalTransition extends UMLChangeAction {
  public ActionInternalTransition() { super("Internal Transition"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("adding internal transition...");
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    if (!(target instanceof State)) return;
    State st = (State) target;
    try {
      Transition t = new Transition(st, st);
      t.setTrigger(new
		   uci.uml.Behavioral_Elements.State_Machines.Event("event"));
      t.setGuard(new Guard("condition"));
      t.setEffect(new ActionSequence(new Name("actions")));
      t.setState(st);
      super.actionPerformed(ae);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in ActionInternalTransition");
    }
  }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    return super.shouldBeEnabled() && target instanceof State;
  }
} /* end class ActionInternalTransition */

class ActionPseudostate extends UMLChangeAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Pseudostate.class, "Pseudostate");

  public ActionPseudostate() { super("Pseudostate"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making Pseudostate...");
    _cmdCreateNode.doIt();
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionPseudostate */

class ActionPackage extends UMLChangeAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Subsystem.class, "Package");

  public ActionPackage() { super("Package"); }

  public void actionPerformed(ActionEvent ae) {
    //System.out.println("making Package...");
    _cmdCreateNode.doIt();
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionPackage */

class ActionInstance extends UMLChangeAction {
  uci.gef.Cmd _cmdCreateNode = new
  uci.gef.CmdCreateNode(Instance.class, "Instance");

  public ActionInstance() { super("Instance"); }

  public void actionPerformed(ActionEvent ae) {
    System.out.println("making Instance...");
    _cmdCreateNode.doIt();
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionInstance */

class ActionAttr extends UMLChangeAction {
  // needs-more-work: should be part of java binding or common elements
  public static DataType INT_TYPE = new DataType("int");

  public ActionAttr() { super("Attribute"); }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    if (!(target instanceof Classifier)) return;
    Classifier cls = (Classifier) target;
    try {
      Attribute attr = new Attribute("newAttr", INT_TYPE, "0");
      attr.setVisibility(VisibilityKind.PUBLIC);
      cls.addStructuralFeature(attr);
      super.actionPerformed(ae);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in ActionOper");
    }
  }

  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    return super.shouldBeEnabled() && target instanceof Classifier;
  }
} /* end class ActionAttr */

class ActionOper extends UMLChangeAction {
  // needs-more-work: should be part of java binding or common elements
  public static DataType VOID_TYPE = new DataType("void");

  public ActionOper() { super("Operation"); }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    if (!(target instanceof Classifier)) return;
    Classifier cls = (Classifier) target;
    try {
      Operation oper = new Operation(VOID_TYPE, "newOperation");
      oper.setVisibility(VisibilityKind.PUBLIC);
      cls.addBehavioralFeature(oper);
      super.actionPerformed(ae);
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in ActionOper");
    }
  }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    return super.shouldBeEnabled() && target instanceof Classifier;
  }
} /* end class ActionOper */


class ActionMessage extends UMLChangeAction {
  public ActionMessage() { super("Message"); }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    Object d = pb.getTarget();
    if (!(d instanceof UMLCollaborationDiagram)) return;
    if (!(target instanceof AssociationRole)) return;
    AssociationRole ar = (AssociationRole) target;
    try {
      Editor _curEditor = Globals.curEditor();
      GraphModel gm = _curEditor.getGraphModel();
      GraphNodeRenderer renderer = _curEditor.getGraphNodeRenderer();
      Layer lay = _curEditor.getLayerManager().getActiveLayer();
      SelectionManager _sm = _curEditor.getSelectionManager();
      Vector figs = _sm.selections();
      Selection _cf = (Selection) figs.firstElement();
      FigEdge _curFig = (FigEdge) _cf.getContent();
      Point _center = _curFig.center();

      Message msg = new Message();
      Vector ascEnds = ar.getAssociationEndRole();

      AssociationEndRole aer1 = (AssociationEndRole) ascEnds.firstElement();
      AssociationEndRole aer2 = (AssociationEndRole) ascEnds.lastElement();
      ClassifierRole crSrc = (ClassifierRole) aer1.getType();
      ClassifierRole crDst = (ClassifierRole) aer2.getType();
      msg.setSender(crSrc);
      msg.setReceiver(crDst);
      UninterpretedAction ua = new UninterpretedAction();
      msg.setAction(ua);
      ar.addMessage(msg);

      FigNode _pers = renderer.getFigNodeFor(gm, lay, msg);
      Vector messages = ar.getMessages();
      int size = messages.size();
      int offsetY = _pers.getHeight()/2;
      _pers.setLocation(_center.x, _center.y - (offsetY * size));
      _curFig.addPathItem(_pers, new PathConvPercent(_curFig, 50, 10));
      lay.add(_pers);
      super.actionPerformed(ae);
      //System.out.println("sender = " + msg.getSender().getName());
      //System.out.println("receiver = " + msg.getReceiver().getName());
    }
    catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in ActionMessage");
    }
  }

  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    return super.shouldBeEnabled() && target instanceof AssociationRole;
  }
} /* end class ActionMessage */

class ActionModel extends UMLChangeAction {
  //uci.gef.Cmd _cmdCreateNode = new
  //uci.gef.CmdCreateNode(uci.uml.Model_Management.Model.class, "Model");
  // needs-more-work: need FigModel and UMLPackageDiagram
  public ActionModel() { super("Model"); }

  public void actionPerformed(ActionEvent ae) {
    System.out.println("making model...");
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      p.addModel(new Model());
      super.actionPerformed(ae);
    }
    catch (PropertyVetoException pve) { }
  }

  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() && p != null;
  }
} /* end class ActionModel */

////////////////////////////////////////////////////////////////
// generate menu actions

class ActionGenerateOne extends UMLAction {
  public ActionGenerateOne() { super("Generate Selected Classes"); }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Editor ce = uci.gef.Globals.curEditor();
    Vector sels = ce.getSelectionManager().getFigs();
    java.util.Enumeration enum = sels.elements();
    Vector classes = new Vector();
    while (enum.hasMoreElements()) {
      Fig f = (Fig) enum.nextElement();
      Object owner = f.getOwner();
      if (!(owner instanceof MMClass) && !(owner instanceof Interface))
	continue;
      Classifier cls = (Classifier) owner;
      String name = cls.getName().getBody();
      if (name == null || name.length() == 0) continue;
      classes.addElement(cls);
    }
    ClassGenerationDialog cgd = new ClassGenerationDialog(classes);
    cgd.show();
  }

  public boolean shouldBeEnabled() {
    if (!super.shouldBeEnabled()) return false;
    Editor ce = uci.gef.Globals.curEditor();
    Vector sels = ce.getSelectionManager().getFigs();
    java.util.Enumeration enum = sels.elements();
    boolean foundOne = false;
    while (enum.hasMoreElements()) {
      Fig f = (Fig) enum.nextElement();
      Object owner = f.getOwner();
      if (!(owner instanceof MMClass) && !(owner instanceof Interface))
	continue;
      Classifier cls = (Classifier) owner;
      String name = cls.getName().getBody();
      if (name == null || name.length() == 0) return false;
      foundOne = true;
    }
    return foundOne;
  }
} /* end class ActionGenerateOne */

class ActionGenerateAll extends UMLAction {
  public ActionGenerateAll() { super("Generate All Classes"); }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getTarget();
    if (!(target instanceof UMLClassDiagram)) return;
    UMLClassDiagram d = (UMLClassDiagram) target;
    Vector classes = new Vector();
    Vector nodes = d.getGraphModel().getNodes();
    java.util.Enumeration enum = nodes.elements();
    while (enum.hasMoreElements()) {
      Object owner = enum.nextElement();
      if (!(owner instanceof MMClass) && !(owner instanceof Interface))
	continue;
      Classifier cls = (Classifier) owner;
      String name = cls.getName().getBody();
      if (name == null || name.length() == 0) continue;
      classes.addElement(cls);
    }
    ClassGenerationDialog cgd = new ClassGenerationDialog(classes);
    cgd.show();
  }

  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getTarget();
    return super.shouldBeEnabled() && target instanceof UMLClassDiagram;
  }
} /* end class ActionGenerateAll */

class ActionGenerateWeb extends UMLAction {
  public ActionGenerateWeb() { super("Generate Web Site"); }

  public void actionPerformed(ActionEvent ae) {

  }

  public boolean shouldBeEnabled() {
    return false;
  }
} /* end class ActionGenerateWeb */

////////////////////////////////////////////////////////////////
// critiquing related actions

class ActionAutoCritique extends UMLAction {
  public ActionAutoCritique() { super("Toggle Auto-Critique"); }
  public void actionPerformed(ActionEvent ae) {
    Designer d = Designer.TheDesigner;
    boolean b = d.getAutoCritique();
    d.setAutoCritique(!b);
    //System.out.println("setting autoCritique to " + !b);
  }
} /* end class ActionAutoCritique */

class ActionOpenDecisions extends UMLAction {
  public ActionOpenDecisions() { super("Design Issues..."); }
  public void actionPerformed(ActionEvent ae) {
    DesignIssuesDialog d = new DesignIssuesDialog(ProjectBrowser.TheInstance);
    d.show();
  }
} /* end class ActionOpenDecisions */

class ActionOpenGoals extends UMLAction {
  public ActionOpenGoals() { super("Design Goals..."); }
  public void actionPerformed(ActionEvent ae) {
    GoalsDialog d = new GoalsDialog(ProjectBrowser.TheInstance);
    d.show();
  }  
} /* end class ActionOpenGoals */

class ActionOpenCritics extends UMLAction {
  public ActionOpenCritics() { super("Browse Critics..."); }
  public void actionPerformed(ActionEvent ae) {
    CriticBrowserDialog dialog = new CriticBrowserDialog();
    dialog.show();
  }

} /* end class ActionOpenCritics */


class ActionFlatToDo extends UMLAction {
  public ActionFlatToDo() { super("Toggle Flat View"); }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    ToDoPane pane = pb.getToDoPane();
    pane.toggleFlat();
  }
} /* end class ActionFlatToDo */

class ActionNewToDoItem extends UMLAction {
  public ActionNewToDoItem() { super("New To Do Item..."); }
  public void actionPerformed(ActionEvent ae) {
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

class ActionRecordFix extends ToDoItemAction {
  public ActionRecordFix() { super("Record My Fix..."); }
} /* end class ActionRecordFix */

class ActionReplayFix extends ToDoItemAction {
  public ActionReplayFix() { super("Replay My Fix..."); }
} /* end class ActionReplayFix */

// class ActionFixItNext extends ToDoItemAction {
//   public ActionFixItNext() { super("Fix It Next..."); }
// } /* end class ActionFixItNext */

// class ActionFixItBack extends ToDoItemAction {
//   public ActionFixItBack() { super("Fix It Back..."); }
// } /* end class ActionFixItBack */

// class ActionFixItFinish extends ToDoItemAction {
//   public ActionFixItFinish() { super("Fix It Finish..."); }
// } /* end class ActionFixItFinish */

class ActionResolve extends ToDoItemAction {
  public ActionResolve() { super("Resolve Item..."); }
  public void actionPerformed(ActionEvent ae) {
    DismissToDoItemDialog dialog = new DismissToDoItemDialog();
    dialog.setTarget(_target);
    dialog.setVisible(true);
  }
} /* end class ActionResolve */

class ActionEmailExpert extends ToDoItemAction {
  public ActionEmailExpert() { super("Send Email To Expert..."); }
  public void actionPerformed(ActionEvent ae) {
    EmailExpertDialog dialog = new EmailExpertDialog();
    dialog.setTarget(_target);
    dialog.show();
  }
} /* end class ActionEmailExpert */

class ActionMoreInfo extends ToDoItemAction {
  public ActionMoreInfo() { super("More Info..."); }
} /* end class ActionMoreInfo */

class ActionSnooze extends ToDoItemAction {
  public ActionSnooze() { super("Snooze Critic"); }
  public void actionPerformed(ActionEvent ae) {
    if (!(_target instanceof ToDoItem)) return;
    ToDoItem item = (ToDoItem) _target;
    Poster p = item.getPoster();
    p.snooze();
    TabToDo._numHushes++;
  }
} /* end class ActionSnooze */


////////////////////////////////////////////////////////////////
// general user interface actions

class ActionAboutArgoUML extends UMLAction {
  public ActionAboutArgoUML() { super("About Argo/UML"); }

  public void actionPerformed(ActionEvent ae) {
    AboutBox box = new AboutBox();
    box.show();
  }
  public boolean shouldBeEnabled() { return true; }
} /* end class ActionAboutArgoUML */
