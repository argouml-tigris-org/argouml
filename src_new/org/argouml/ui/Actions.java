// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.Poster;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ToDoList;
import org.argouml.cognitive.critics.ui.CriticBrowserDialog;
import org.argouml.cognitive.ui.AddToDoItemDialog;
import org.argouml.cognitive.ui.DesignIssuesDialog;
import org.argouml.cognitive.ui.DismissToDoItemDialog;
import org.argouml.cognitive.ui.GoalsDialog;
import org.argouml.cognitive.ui.TabToDo;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.swingext.ActionUtilities;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetListener;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.diagram.ui.SelectionWButtons;
import org.argouml.uml.ui.UMLAction;
import org.argouml.util.osdep.OsUtil;
import org.tigris.gef.base.CmdPrint;
import org.tigris.gef.base.Diagram;


public class Actions implements TargetListener {
    
    private static final Logger LOG = Logger.getLogger(Actions.class);

    private static final Actions INSTANCE = new Actions();

    public static Actions getInstance() {
        return INSTANCE;
    }

    private Actions() {
        TargetManager.getInstance().addTargetListener(this);
    }

    private static Vector allActions = new Vector(100);


    public static UMLAction Print = new ActionPrint();
    public static UMLAction PageSetup = new ActionPageSetup();

    public static UMLAction Undo = new ActionUndo();
    public static UMLAction Redo = new ActionRedo();

    //public static UMLAction NavBack = new ActionNavBack();
    //public static UMLAction NavForw = new ActionNavForw();
    //public static UMLAction NavFavs = new ActionNavFavs();
//    public static UMLAction NavConfig = new ActionNavConfig();

    public static UMLAction Find = new ActionFind();
    public static UMLAction GotoDiagram = new ActionGotoDiagram();

    public static UMLAction NextEditTab = new ActionNextEditTab();
    //  public static UMLAction NextDetailsTab = new ActionNextDetailsTab();
    public static UMLAction ShowRapidButtons = new ActionShowRapidButtons();

    public static UMLAction CreateMultiple = new ActionCreateMultiple();

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

    public static UMLAction SystemInfo = new ActionSystemInfo();
    public static UMLAction AboutArgoUML = new ActionAboutArgoUML();

    /**
     * @deprecated as of 0.15.3. Use updateAllEnabled(TargetEvent e) instead.
     *
     */
    public static void updateAllEnabled() {
	Enumeration actions = allActions.elements();
	while (actions.hasMoreElements()) {
	    UMLAction a = (UMLAction) actions.nextElement();
	    a.updateEnabled();
	}
    }

    /**
     * Updates all global actions as a consequence of the send TargetEvent.
     * @param e
     */
    private static void updateAllEnabled(TargetEvent e) {
	Iterator actions = allActions.iterator();
	while (actions.hasNext()) {
	    UMLAction a = (UMLAction) actions.next();
	    a.updateEnabled(e.getNewTarget());
	}
    }

    public static void addAction(AbstractAction newAction) {
        LOG.debug("Adding action: " + newAction.getClass().getName());
	allActions.addElement(newAction);
    }

    public static boolean isGlobalAction(AbstractAction action) {
        return allActions.contains(action);
    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetAdded(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        updateAllEnabled(e);

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetRemoved(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        updateAllEnabled(e);

    }

    /**
     * @see org.argouml.ui.targetmanager.TargetListener#targetSet(
     *         org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        updateAllEnabled(e);

    }

}  /* end class Actions */


////////////////////////////////////////////////////////////////
// file menu actions
/** print the current active diagram.
 */
class ActionPrint extends UMLAction {

    private final CmdPrint cmd = new CmdPrint();

    public ActionPrint() { super("action.print"); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	Object target =
	    ProjectManager.getManager().getCurrentProject().getActiveDiagram();
	if (target instanceof Diagram) {
	    String n = ((Diagram) target).getName();
	    cmd.setDiagramName(n);
	    cmd.doIt();
	}
    }

    public CmdPrint getCmdPrint() {
	return cmd;
    }
} /* end class ActionPrint */

/** Page setup for printing.
 */
class ActionPageSetup extends UMLAction {

    public ActionPageSetup() { super("action.page-setup", HAS_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	((ActionPrint) Actions.Print).getCmdPrint().doPageSetup();
    }
} /* end class ActionPageSetup */


////////////////////////////////////////////////////////////////
// generic editing actions

class ActionUndo extends UMLAction {

    public ActionUndo() { super("action.undo"); }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() { return false; }
} /* end class ActionUndo */

class ActionRedo extends UMLAction {

    public ActionRedo() { super("action.redo"); }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() { return false; }
} /* end class ActionRedo */


////////////////////////////////////////////////////////////////
// items on view menu

class ActionFind extends UMLAction {

    public ActionFind() { super("action.find"); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	FindDialog.getInstance().setVisible(true);
    }
} /* end class ActionFind */

class ActionGotoDiagram extends UMLAction {

    public ActionGotoDiagram() { super("action.goto-diagram", NO_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	Project p = ProjectManager.getManager().getCurrentProject();

	final TabResults allDiagrams = new TabResults(false); // no related
	allDiagrams.setResults(p.getDiagrams(), p.getDiagrams());

	// TabResults has really large preferred height, so divide in
	// half to reduce size of dialog which will be sized based on
	// this preferred size.
	allDiagrams.
	    setPreferredSize(new Dimension(
	            allDiagrams.getPreferredSize().width,
		    allDiagrams.getPreferredSize().height / 2));
	allDiagrams.selectResult(0);

	//TODO: class TearOffHostFrame and TearOffManager.
	//idea: pop-up on tab that lists docking locations, new window.
	ArgoDialog f =
	    new ArgoDialog(pb, "Goto Diagram...",
			   ArgoDialog.OK_CANCEL_OPTION, false) {
		public void actionPerformed(ActionEvent e) {
		    if (e.getSource() == getOkButton()) {
			allDiagrams.doDoubleClick();
		    }
		    else {
			super.actionPerformed(e);
		    }
		}

		protected void nameButtons() {
		    super.nameButtons();
		    nameButton(getOkButton(), "button.go-to-selection");
		    nameButton(getCancelButton(), "button.close");
		}
	    };
	JPanel mainPanel = new JPanel(new BorderLayout());
	//JTabbedPane tabs = new JTabbedPane();
	//mainPanel.add(tabs, BorderLayout.CENTER);
	//tabs.addTab("All Diagrams", allDiagrams);
	mainPanel.add(allDiagrams, BorderLayout.CENTER);
	f.setContent(mainPanel);
	//TODO: tabs for class, state, usecase, help
	f.setVisible(true);
    }
} /* end class ActionGotoDiagram */

/*
class ActionNavBack extends UMLAction {

  public ActionNavBack() { super("action.navigate-back"); }

  public boolean shouldBeEnabled() {
    if (ProjectBrowser.getInstance() != null) {
        Project p = ProjectManager.getManager().getCurrentProject();
        if (!(super.shouldBeEnabled() && p != null)) return false;
        NavigatorPane np = ProjectBrowser.getInstance().getNavigatorPane();
	if ((np == null)) return false;
	boolean b = np.canNavBack();
        return b;
    }
    else
        return false;
  }

  public void actionPerformed(ActionEvent ae) {
    NavigatorPane np = ProjectBrowser.getInstance().getNavigatorPane();
    np.navBack();
  }
} /* end class ActionNavBack */

/*
class ActionNavForw extends UMLAction {

  public ActionNavForw() { super("action.navigate-forward"); }

  public boolean shouldBeEnabled() {
    if (ProjectBrowser.getInstance() != null) {
        Project p = ProjectManager.getManager().getCurrentProject();
        if (!(super.shouldBeEnabled() && p != null)) return false;
        NavigatorPane np = ProjectBrowser.getInstance().getNavigatorPane();
        return np.canNavForw();
    } else
        return false;
  }

  public void actionPerformed(ActionEvent ae) {
    NavigatorPane np = ProjectBrowser.getInstance().getNavigatorPane();
    np.navForw();
  }
} /* end class ActionNavForw */

// class ActionNavFavs extends UMLAction {
//   public ActionNavFavs() { super("Favorites"); }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.getInstance().getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionNavFavs */

//class ActionNavConfig extends UMLAction {
//
//    public ActionNavConfig() { super("action.nav-config"); }
//
//    public void actionPerformed(ActionEvent ae) {
//	NavigatorConfigDialog ncd =
//            new NavigatorConfigDialog(ProjectBrowser.getInstance(),
//                                      NavigatorPane.getInstance());
//	ncd.setVisible(true);
//    }
//} /* end class ActionNavConfig */

class ActionNextEditTab extends UMLAction {

    public ActionNextEditTab() { super("action.next-editing-tab", NO_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.getInstance();
	MultiEditorPane mep = pb.getEditorPane();
	mep.selectNextTab();
    }
} /* end class ActionNextEditTab */

// class ActionAddToFavs extends UMLAction {
//   public ActionAddToFavs() { super("Add To Favorites"); }
// } /* end class ActionAddToFavs */

// This option does not make much sense now that tabpanes can be in
// different panels.  Direct manipulation seems a better option anyway
// Bob Tarling 18/8/2002 //class ActionNextDetailsTab extends
// UMLAction { public ActionNextDetailsTab() {
// super("action.next-details-tab", NO_ICON); } public void
// actionPerformed(ActionEvent ae) { ProjectBrowser pb =
// ProjectBrowser.getInstance(); DetailsPane dp = pb.getDetailsPane();
// dp.selectNextTab(); } //} /* end class ActionNextDetailsTab */

// class ActionPrevDetailsTab extends UMLAction {
//   public ActionPrevDetailsTab() { super("Previous Details Tab"); }
// } /* end class ActionPrevDetailsTab */


class ActionShowRapidButtons extends UMLAction {

    public ActionShowRapidButtons() {
	super("action.buttons-on-selection", NO_ICON);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	SelectionWButtons.toggleShowRapidButtons();
    }
} /* end class ActionShowRapidButtons */


////////////////////////////////////////////////////////////////
// items on create menu

class ActionCreateMultiple extends UMLAction {

    public ActionCreateMultiple() { super("action.create-multiple", NO_ICON); }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
	//Project p = ProjectBrowser.getInstance().getProject();
	//return super.shouldBeEnabled() && p != null;
	return false;
    }
} /* end class ActionCreateMultiple */


////////////////////////////////////////////////////////////////
// generate menu actions

// class ActionGenerateWeb extends UMLAction {
//   public ActionGenerateWeb() { super("Generate Web Site", NO_ICON); }
//   public void actionPerformed(ActionEvent ae) {
//   }
//   public boolean shouldBeEnabled() {
//     return false;
//   }
// } /* end class ActionGenerateWeb */

////////////////////////////////////////////////////////////////
// critiquing related actions

/**
 * Stops critiquing and the TodoList validity checking thread in ToDoList.
 */
class ActionAutoCritique extends UMLAction {

    public ActionAutoCritique() {
	super("action.toggle-auto-critique", NO_ICON);
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        
        // stop/start creating more critics
	Designer d = Designer.TheDesigner;
	boolean b = d.getAutoCritique();
	d.setAutoCritique(!b);
        
        // stop/start cleaning up invalid TodoItems.
        ToDoList.getInstance().setPaused(!ToDoList.getInstance().isPaused());
    }

} /* end class ActionAutoCritique */

class ActionOpenDecisions extends UMLAction {

    public ActionOpenDecisions() { super("action.design-issues", NO_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	DesignIssuesDialog d =
	    new DesignIssuesDialog(ProjectBrowser.getInstance());
	d.show();
    }
} /* end class ActionOpenDecisions */

class ActionOpenGoals extends UMLAction {

    public ActionOpenGoals() { super("action.design-goals", NO_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	GoalsDialog d = new GoalsDialog(ProjectBrowser.getInstance());
	d.show();
    }
} /* end class ActionOpenGoals */

class ActionOpenCritics extends UMLAction {

    public ActionOpenCritics() { super("action.browse-critics", NO_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	CriticBrowserDialog dialog = new CriticBrowserDialog();
	dialog.show();
    }

} /* end class ActionOpenCritics */

class ActionFlatToDo extends UMLAction {

    public ActionFlatToDo() { super("action.toggle-flat-view", NO_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser.getInstance().getTodoPane().toggleFlat();
    }
} /* end class ActionFlatToDo */

class ActionNewToDoItem extends UMLAction {

    public ActionNewToDoItem() {
        super("action.new-todo-item");
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {       
        AddToDoItemDialog dialog = new AddToDoItemDialog();
        dialog.show();
    }
} /* end class ActionNewToDoItem */

class ToDoItemAction extends UMLAction {

    private Object rememberedTarget = null;

    public ToDoItemAction(String name) { super(name, false, HAS_ICON); }

    public ToDoItemAction(String name, boolean hasIcon) {
	super(name, false, hasIcon);
    }
    
    /**
     * @return Returns the rememberedTarget.
     */
    protected Object getRememberedTarget() {
        return rememberedTarget;
    }    

    /**
     * @see org.argouml.uml.ui.UMLAction#updateEnabled(java.lang.Object)
     */
    public void updateEnabled(Object target) {
	if (target == null) {
	    setEnabled(false);
	    return;
	}

	rememberedTarget = target;
	setEnabled(shouldBeEnabled(target));
    }

    public boolean shouldBeEnabled(Object target) {
	return target instanceof ToDoItem;
    }
}

class ActionResolve extends ToDoItemAction {

    public ActionResolve() { super("action.resolve-item"); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	DismissToDoItemDialog dialog = new DismissToDoItemDialog();
	dialog.setTarget(getRememberedTarget());
	dialog.setVisible(true);
    }
} /* end class ActionResolve */

class ActionEmailExpert extends ToDoItemAction {

    public ActionEmailExpert() { super("action.send-email-to-expert"); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
        if (OsUtil.isWin32()) {
            ToDoItem target = (ToDoItem) getRememberedTarget();
            Poster p = target.getPoster();
            String to = p.getExpertEmail();
            String subject = target.getHeadline();
            subject = subject.replaceAll("\\s", "%20");
            Designer dsgr = Designer.TheDesigner;
            try {                
                //MVW: This works under MSWindows only, I guess.
                Runtime.getRuntime().exec(
                    "cmd /c start mailto:" + to 
                    + "?subject=" + subject 
                    + "&body=" + dsgr); 
            } catch (Exception ex) {
                /*ignore for now*/;
            }
        } else {
            EmailExpertDialog dialog = new EmailExpertDialog();
            dialog.setTarget(getRememberedTarget());
            dialog.show();
        }
    }

    /**
     * @see org.argouml.ui.ToDoItemAction#shouldBeEnabled(java.lang.Object)
     */
    public boolean shouldBeEnabled(Object target) {
        return getRememberedTarget() != null 
            && getRememberedTarget() instanceof ToDoItem;
    }

} /* end class ActionEmailExpert */

class ActionMoreInfo extends ToDoItemAction {

    public ActionMoreInfo() { super("action.more-info", NO_ICON); }
} /* end class ActionMoreInfo */

class ActionSnooze extends ToDoItemAction {

    public ActionSnooze() { super("action.snooze-critic"); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	if (!(getRememberedTarget() instanceof ToDoItem)) return;

	ToDoItem item = (ToDoItem) getRememberedTarget();
	Poster p = item.getPoster();
	p.snooze();
	TabToDo._numHushes++;
    }
} /* end class ActionSnooze */

////////////////////////////////////////////////////////////////
// general user interface actions

/**
 * System information dialog. 
 */

class ActionSystemInfo extends UMLAction {

    public ActionSystemInfo() { super("action.system-information", HAS_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	JFrame jFrame = (JFrame) ActionUtilities.getActionRoot(ae);
	SystemInfoDialog sysInfoDialog = new SystemInfoDialog(jFrame, true);
	Dimension siDim = sysInfoDialog.getSize();
	Dimension pbDim = jFrame.getSize();

	if ( siDim.width > pbDim.width / 2 ) {
	    sysInfoDialog.setSize(pbDim.width / 2, siDim.height + 45);
	} else {
	    sysInfoDialog.setSize(siDim.width, siDim.height + 45);
	}

	sysInfoDialog.setLocationRelativeTo(jFrame);
	sysInfoDialog.show();
    }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() { return true; }

} /* end class ActionSystemInfo */

/**
 * About ArgoUML dialog.
 */
class ActionAboutArgoUML extends UMLAction {

    public ActionAboutArgoUML() { super("action.about-argouml", HAS_ICON); }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent ae) {
	JFrame jframe = ((JFrame)(ProjectBrowser.getInstance()));
	AboutBox box = new AboutBox(jframe, true);

	box.setLocationRelativeTo(jframe);
	box.show();
    }

    /**
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() { return true; }

} /* end class ActionAboutArgoUML */

