// Copyright (c) 1996-99 The Regents of the University of California. All
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

import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import javax.swing.filechooser.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;
import ru.novosoft.uml.foundation.data_types.MExpression;
import ru.novosoft.uml.behavior.common_behavior.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.model_management.*;

import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import org.tigris.gef.graph.*;
import org.tigris.gef.util.*;

import org.argouml.kernel.*;
import org.argouml.persistence.*;
import org.argouml.cognitive.*;
import org.argouml.cognitive.ui.*;
import org.argouml.cognitive.critics.ui.*;

//
//   Template reader, has nothing to do with OCL
//
import org.tigris.gef.ocl.*;
import org.argouml.uml.*;
import org.argouml.uml.ui.*;
import org.argouml.uml.generator.ui.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.activity.ui.*;
import org.argouml.uml.diagram.collaboration.ui.*;
import org.argouml.uml.diagram.deployment.ui.*;
import org.argouml.uml.diagram.state.ui.*;
import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;
import org.argouml.uml.diagram.sequence.ui.*;
import org.argouml.util.*;
import org.argouml.xml.argo.ArgoParser;

public class Actions {

  static Vector _allActions = new Vector(100);

  public static UMLAction New = new ActionNew();
  //public static UMLAction Open = new ActionOpen();
  //public static UMLAction OpenXMI = new ActionOpenXMI();
  //public static UMLAction Save = new ActionSave();
  //public static UMLAction SaveAs = new ActionSaveAs();
  //public static UMLAction SaveAsXMI = new ActionSaveAsXMI();
  public static UMLAction OpenProject = new ActionOpenProject();
  public static UMLAction SaveProject = new ActionSaveProject();
  public static UMLAction LoadModelFromDB = new ActionLoadModelFromDB();
  public static UMLAction StoreModelToDB = new ActionStoreModelToDB();
  public static UMLAction SaveProjectAs = new ActionSaveProjectAs();
  //public static UMLAction AddToProj = new ActionAddToProj();
  public static UMLAction Print = new ActionPrint();
  public static UMLAction SaveGIF = new ActionSaveGIF();
//   public static UMLAction SavePS = new ActionSavePS();
  public static UMLAction SaveGraphics = new ActionSaveGraphics();
  public static UMLAction Exit = new ActionExit();

  public static UMLAction Undo = new ActionUndo();
  public static UMLAction Redo = new ActionRedo();
  public static UMLAction Cut = new ActionCut();
  public static UMLAction Copy = new ActionCopy();
  public static UMLAction Paste = new ActionPaste();
  public static UMLAction DeleteFromDiagram = new ActionDeleteFromDiagram();
  public static UMLAction RemoveFromModel = new ActionRemoveFromModel();
  public static UMLAction EmptyTrash = new ActionEmptyTrash();

//   public static UMLAction NavUp = new ActionNavUp();
//   public static UMLAction NavDown = new ActionNavDown();
  public static UMLAction NavBack = new ActionNavBack();
  public static UMLAction NavForw = new ActionNavForw();
  //public static UMLAction NavFavs = new ActionNavFavs();
  public static UMLAction NavConfig = new ActionNavConfig();

  public static UMLAction Find = new ActionFind();
  public static UMLAction GotoDiagram = new ActionGotoDiagram();

  public static UMLAction NextEditTab = new ActionNextEditTab();
  //public static UMLAction AddToFavs = new ActionAddToFavs();
  public static UMLAction NextDetailsTab = new ActionNextDetailsTab();
  public static UMLAction ShowRapidButtons = new ActionShowRapidButtons();

  public static UMLAction CreateMultiple = new ActionCreateMultiple();
  //public static UMLAction ClassWizard = new ActionClassWizard();

  //public static UMLAction MModel = new ActionModel();
  public static UMLAction AddTopLevelPackage = new ActionAddTopLevelPackage();
  public static UMLAction ClassDiagram = new ActionClassDiagram();
  public static UMLAction UseCaseDiagram = new ActionUseCaseDiagram();
  public static UMLAction StateDiagram = new ActionStateDiagram();
  public static UMLAction ActivityDiagram = new ActionActivityDiagram();
  public static UMLAction CollaborationDiagram = new ActionCollaborationDiagram();
  public static UMLAction DeploymentDiagram = new ActionDeploymentDiagram();
  public static UMLAction SequenceDiagram = new ActionSequenceDiagram();

  //public static UMLAction Class = new ActionClass();
  //public static UMLAction MInterface = new ActionInterface();
  //public static UMLAction MActor = new ActionActor();
  //public static UMLAction MUseCase = new ActionUseCase();
  //public static UMLAction MState = new MActionState();
  //public static UMLAction MPseudostate = new ActionPseudostate();
  //public static UMLAction Package = new ActionPackage();
  //public static UMLAction MInstance = new ActionInstance();
  public static UMLAction AddAttribute = new ActionAddAttribute();
  public static UMLAction AddOperation = new ActionAddOperation();
  public static UMLAction AddMessage = new ActionAddMessage();
  public static UMLAction AddInternalTrans = new ActionAddInternalTrans();

  public static UMLAction GenerateOne = new ActionGenerateOne();
  public static UMLAction GenerateAll = new ActionGenerateAll();
  //public static UMLAction GenerateWeb = new ActionGenerateWeb();


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

  //public static UMLAction RecordFix = new ActionRecordFix();
  //public static UMLAction ReplayFix = new ActionReplayFix();

  //   public static UMLAction FixItNext = new ActionFixItNext();
  //   public static UMLAction FixItBack = new ActionFixItBack();
  //   public static UMLAction FixItFinish = new ActionFixItFinish();

  public static UMLAction AboutArgoUML = new ActionAboutArgoUML();
  public static UMLAction Properties = new ActionProperties();

  // multiplicity
  public static UMLAction SrcMultOne =
  new ActionMultiplicity(MMultiplicity.M1_1, "src");
  public static UMLAction DestMultOne =
  new ActionMultiplicity(MMultiplicity.M1_1, "dest");

  public static UMLAction SrcMultZeroToOne=
  new ActionMultiplicity(MMultiplicity.M0_1, "src");
  public static UMLAction DestMultZeroToOne =
  new ActionMultiplicity(MMultiplicity.M0_1, "dest");

  public static UMLAction SrcMultZeroToMany =
  new ActionMultiplicity(MMultiplicity.M0_N, "src");
  public static UMLAction DestMultZeroToMany =
  new ActionMultiplicity(MMultiplicity.M0_N, "dest");

  public static UMLAction SrcMultOneToMany =
  new ActionMultiplicity(MMultiplicity.M1_N, "src");
  public static UMLAction DestMultOneToMany =
  new ActionMultiplicity(MMultiplicity.M1_N, "dest");

  // aggregation
  public static UMLAction SrcAgg =
  new ActionAggregation(MAggregationKind.AGGREGATE, "src");
  public static UMLAction DestAgg =
  new ActionAggregation(MAggregationKind.AGGREGATE, "dest");

  public static UMLAction SrcAggComposite =
  new ActionAggregation(MAggregationKind.COMPOSITE, "src");
  public static UMLAction DestAggComposite =
  new ActionAggregation(MAggregationKind.COMPOSITE, "dest");

  public static UMLAction SrcAggNone =
  new ActionAggregation(MAggregationKind.NONE, "src");
  public static UMLAction DestAggNone =
  new ActionAggregation(MAggregationKind.NONE, "dest");

  // compartments
  public static UMLAction ShowAttrCompartment =
  new ActionCompartmentDisplay(true, "Show Attribute Compartment");
  public static UMLAction HideAttrCompartment =
  new ActionCompartmentDisplay(false, "Hide Attribute Compartment");

  public static UMLAction ShowOperCompartment =
  new ActionCompartmentDisplay(true, "Show Operation Compartment");
  public static UMLAction HideOperCompartment =
  new ActionCompartmentDisplay(false, "Hide Operation Compartment");

  public static UMLAction ShowAllCompartments =
  new ActionCompartmentDisplay(true, "Show All Compartments");
  public static UMLAction HideAllCompartments =
  new ActionCompartmentDisplay(false, "Hide All Compartments");


  public static void updateAllEnabled() {
    java.util.Enumeration actions = _allActions.elements();
    while (actions.hasMoreElements()) {
      UMLAction a = (UMLAction) actions.nextElement();
      a.updateEnabled();
    }
  }

	public static void addAction(AbstractAction newAction) {
		_allActions.addElement(newAction);
	}

}  /* end class Actions */


class UMLChangeAction extends UMLAction {

  public UMLChangeAction(String s) { super(s, HAS_ICON); }
  public UMLChangeAction(String s, boolean hasIcon) { super(s, hasIcon); }

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
      if (response == JOptionPane.YES_OPTION) {
	boolean safe = false;
	if (((ActionSaveProject)Actions.SaveProject).shouldBeEnabled())
	  safe = ((ActionSaveProject)Actions.SaveProject).trySave(true);
	if (!safe)
	  safe = ((ActionSaveProjectAs)Actions.SaveProjectAs).trySave(false);
	if (!safe) return;
      }
      //needs-more-work: if you cancel the save it should cancel open
    }
    p = Project.makeEmptyProject();
    pb.setProject(p);
    ProjectBrowser.TheInstance.setTitle("Untitled");
  }
} /* end class ActionNew */

class ActionOpenProject extends UMLAction {
  public static final String separator = System.getProperty("file.separator");
  public ActionOpenProject() { super("Open Project..."); }
  public void actionPerformed(ActionEvent e) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    if (p != null) { // && p.getNeedsSave()) {
      String t = "Save changes to " + p.getName();
      int response =
	JOptionPane.showConfirmDialog(pb, t, t,
				      JOptionPane.YES_NO_CANCEL_OPTION);
      if (response == JOptionPane.CANCEL_OPTION) return;
      if (response == JOptionPane.YES_OPTION) {
	boolean safe = false;
	if (((ActionSaveProject)Actions.SaveProject).shouldBeEnabled())
	  safe = ((ActionSaveProject)Actions.SaveProject).trySave(true);
	if (!safe)
	  safe = ((ActionSaveProjectAs)Actions.SaveProjectAs).trySave(false);
	if (!safe) return;
      }
    }

    try {
        String directory = Globals.getLastDirectory();
        JFileChooser chooser = new JFileChooser(directory);

        if (chooser == null) chooser = new JFileChooser();

        chooser.setDialogTitle("Open Project");
        SuffixFilter filter = FileFilters.ArgoFilter;
        chooser.addChoosableFileFilter(filter);
        chooser.addChoosableFileFilter(FileFilters.XMIFilter);
        chooser.setFileFilter(filter);

        int retval = chooser.showOpenDialog(pb);
        if (retval == 0) {
	    File theFile = chooser.getSelectedFile();
	    if (theFile != null) {
	        String path = theFile.getParent();
	        Globals.setLastDirectory(path);
                URL url = theFile.toURL();
                if(url != null) {
                     p = Project.loadProject(url);
                    pb.setProject(p);
                    pb.showStatus("Read " + url.toString());
                }
	        return;
	    }
        }
    }
    //catch (FileNotFoundException ignore) {
    //  System.out.println("got an FileNotFoundException");
    //}
    //     catch (java.lang.ClassNotFoundException ignore) {
    //       System.out.println("got an ClassNotFoundException");
    //      }
    catch (IOException ignore) {
      System.out.println("got an IOException in ActionOpenProject");
    }
//     catch (org.xml.sax.SAXException ignore) {
//       System.out.println("got an SAXException in ActionOpenProject");
//     }
  }
} /* end class ActionOpenProject */

class ActionSaveProject extends UMLAction {
  protected static OCLExpander expander = null;
  public static String ARGO_TEE = "/org/argouml/xml/dtd/argo.tee";
  public ActionSaveProject() {
    super("Save Project");
  }

  public void actionPerformed(ActionEvent e) {
    trySave(true);
  }

  public boolean trySave(boolean overwrite) {

      StringBuffer msg = new StringBuffer();
      msg.append("This is a developer release of ArgoUML. You should not use it \n");
      msg.append("for production use, it's only for testing. You may save your models,\n");
      msg.append("but do not expect future releases of ArgoUML to be able to read them.\n");
      msg.append("If you want to use a \"stable\" release, please go to www.argouml.org\n");
      msg.append("and get one there. Thank you.");
      JOptionPane.showMessageDialog(null, msg.toString(), "Warning", JOptionPane.WARNING_MESSAGE);
    try {
      if (expander == null) {
	java.util.Hashtable templates = TemplateReader.readFile(ARGO_TEE);
	expander = new OCLExpander(templates);
      }
      ProjectBrowser pb = ProjectBrowser.TheInstance;
      Project p =  pb.getProject();
      //       String name = p.getFilename();
      //       String path = p.getPathname();
      System.out.println("ActionSaveProject at " + p.getURL());
      //       System.out.println("ActionSaveProject name = " + name);
      String fullpath = "Untitled.argo";
      if (p.getURL() != null) fullpath = p.getURL().getFile();
      System.out.println("filename is " + fullpath);
      if (fullpath.charAt(0) == '/' && fullpath.charAt(2) == ':')
	fullpath = fullpath.substring(1); // for Windows /D: -> D:
      File f = new File(fullpath);
      if (f.exists() && !overwrite) {
	System.out.println("Are you sure you want to overwrite " +
			   fullpath + "?");
      }
      FileWriter fw = new FileWriter(f);
      p.preSave();
      expander.expand(fw, p, "", "");
      String parentDirName = fullpath.substring(0, fullpath.lastIndexOf("/"));
      System.out.println("Dir ==" + parentDirName);
      p.saveAllMembers(parentDirName, overwrite);
      //needs-more-work: in future allow independent saving
      p.postSave();
      fw.close();
      pb.showStatus("Wrote " + p.getURL());
      return true;
    }
    catch (FileNotFoundException ignore) {
      System.out.println("got an FileNotFoundException in SaveProject");
      ignore.printStackTrace();
    }
    catch (IOException ignore) {
      System.out.println("got an IOException");
      ignore.printStackTrace();
    }
    return false;
  }
  public boolean shouldBeEnabled() {
    Project p = ProjectBrowser.TheInstance.getProject();
    return super.shouldBeEnabled() &&
      p != null &&
      p.getURL() != null &&
      p.getURL().toString().indexOf("templates") == -1;
  }

} /* end class ActionSaveProject */

class ActionSaveProjectAs extends UMLAction {
  public static final String separator = "/"; //System.getProperty("file.separator");

  protected static OCLExpander expander = null;
  public ActionSaveProjectAs() {
    super("Save Project As...", NO_ICON);
    java.util.Hashtable templates = TemplateReader.readFile("/org/argouml/xml/dtd/argo.tee");
    expander = new OCLExpander(templates);
  }

  public void actionPerformed(ActionEvent e) {
    trySave(false);
  }

  public boolean trySave(boolean overwrite) {

      StringBuffer msg = new StringBuffer();
      msg.append("This is a developer release of ArgoUML. You should not use it \n");
      msg.append("for production use, it's only for testing. You may save your models,\n");
      msg.append("but do not expect future releases of ArgoUML to be able to read them.\n");
      msg.append("If you want to use a \"stable\" release, please go to www.argouml.org\n");
      msg.append("and get one there. Thank you.");
      JOptionPane.showMessageDialog(null, msg.toString(), "Warning", JOptionPane.WARNING_MESSAGE);

    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p =  pb.getProject();
    try {
      JFileChooser chooser = null;
      try {
	if (p != null && p.getURL() != null &&
	    p.getURL().getFile().length()>0) {
	  String filename = p.getURL().getFile();
	  if (!filename.startsWith("/FILE1/+/"))
	    chooser  = new JFileChooser(p.getURL().getFile());
	}
      }
      catch (Exception ex) {
	System.out.println("exception in opening JFileChooser");
	ex.printStackTrace();
      }
      if (chooser == null) chooser = new JFileChooser();

      chooser.setDialogTitle("Save Project: " + p.getName());
      FileFilter filter = FileFilters.ArgoFilter;
      chooser.addChoosableFileFilter(filter);
      chooser.setFileFilter(filter);

      int retval = chooser.showSaveDialog(pb);
      if(retval == 0) {
	File theFile = chooser.getSelectedFile();
	if (theFile != null) {
	  //String pathname = chooser.getSelectedFile().getAbsolutePath();
	  String path = chooser.getSelectedFile().getParent();
	  String name = chooser.getSelectedFile().getName();
	  if (!name.endsWith(".argo")) name += ".argo";
	  if (!path.endsWith(separator)) path += separator;
	  pb.showStatus("Writing " + path + name + "...");
	  p.setFile(chooser.getSelectedFile());
	  //p.setPathname(path);
	  File f = new File(path + name);
	  p.setURL(Util.fileToURL(f));
	  if (f.exists() && !overwrite) {
	    System.out.println("Are you sure you want to overwrite " +
			       name + "?");
	  }
	  FileWriter fw = new FileWriter(f);
	  p.preSave();
	  expander.expand(fw, p, "", "");
	  p.saveAllMembers(path, overwrite);
	  //needs-more-work: in future allow indendent saving
	  p.postSave();
	  fw.close();
	  pb.showStatus("Wrote " + path + name);
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
} /* end class ActionSaveProjectAs */


/* class ActionLoadModelFromDB */
class ActionLoadModelFromDB extends UMLAction {
	public ActionLoadModelFromDB() {
		super("Load model from DB", NO_ICON);
	}

	public void actionPerformed(ActionEvent e) {
	  // when the action is performed, i.e. someone clicked on the menuitem,
	  // create a new DBLoader, ask the user for the models name to load,
	  // then load it and put it into an empty project.

		String modelName = JOptionPane.showInputDialog("What is the name of the model?");
		if ((modelName == null)|| (modelName.equals(""))) return;
		DBLoader loader = new DBLoader();
		if (loader.hasConnection()) {
		    MModel newModel = loader.read(modelName);
		    ProjectBrowser.TheInstance.setProject(new Project(newModel));
		}
	}
	public boolean shouldBeEnabled() {

		// my way of finding out whether these actions should be enabled
		// is to look for the Properties file. Not nice, but working.
		// should be replaecd by a proper plug-in mechanism as soon as there
		// is one for Argo

		java.util.Properties props =  new java.util.Properties();
		String configFile =  System.getProperty("argo.dbconfig", "/db.ini");
		// System.out.println("Using: "+configFile);
		try {
			java.io.InputStream is = new java.io.FileInputStream(configFile);
			props.load(is);
		}
		catch (java.io.IOException e) {
			return false;
		}
		return true;
	}

} /* end class ActionLoadModelFromDB */


/* class ActionStoreModelToDB */
class ActionStoreModelToDB extends UMLAction {
  public ActionStoreModelToDB() {
    super("Store model to DB", NO_ICON);
  }

  public void actionPerformed(ActionEvent e) {

	  // when the action is performed, i.e. someone clicked on the menuitem,
	  // create a new writer, get the Project and its curent Model,
	  // then store it. Simple as this.

	  DBWriter writer = new DBWriter();

	  if (writer.hasConnection()) {
	      ProjectBrowser pb = ProjectBrowser.TheInstance;
	      Project p =  pb.getProject();

	      MNamespace nm = p.getCurrentNamespace();
	      if (!(nm instanceof MModel)) {
		  JOptionPane.showMessageDialog(null, "Error", "Current Namespace is not a Model", JOptionPane.ERROR_MESSAGE);
		  return;
	      }
	      writer.store((MModel)nm);
	  }

	  // one could also store all models of the project,
	  //don't know which is better.

// 	  Vector models = p.getModels();
//  	  for (int i = 0; i< models.size(); i++) {
// 		  MNamespace nm = (MNamespace)models.elementAt(i);
// 		  if (!(nm instanceof MModel)) {
// 			  JOptionPane.showMessageDialog(null, "Error", "Current Namespace is not a Model", JOptionPane.ERROR_MESSAGE);
// 			  return;
// 		  }
// 		  pb.showStatus("Wrote " + ((MModel)models.elementAt(i)).getName());
// 		  writer.store((MModel)models.elementAt(i));
// 	  }
  }
	public boolean shouldBeEnabled() {

		// my way of finding out whether these actions should be enabled
		// is to look for the Properties file. Not nice, but working.
		// should be replaecd by a proper plug-in mechanism as soon as there
		// is one for Argo

		java.util.Properties props =  new java.util.Properties();
		String configFile =  System.getProperty("argo.dbconfig", "/db.ini");
		// System.out.println("Using: "+configFile);
		try {
			java.io.InputStream is = new java.io.FileInputStream(configFile);
			props.load(is);
		}
		catch (java.io.IOException e) {
			return false;
		}
		return true;
	}

} /* end class ActionStoreModelToDB */

class ActionPrint extends UMLAction {
  public ActionPrint() { super("Print..."); }

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


/** Wraps a CmdSaveGIF to allow selection of an output file. */

class ActionSaveGIF extends UMLAction {
  public static final String separator = "/";

  public ActionSaveGIF() {
    super( "Save GIF...", NO_ICON);
  }


  public void actionPerformed( ActionEvent ae ) {
    trySave( false );
  }

  public boolean trySave( boolean overwrite ) {
    CmdSaveGIF cmd = new CmdSaveGIF();
    Object target = ProjectBrowser.TheInstance.getTarget();
    if( target instanceof Diagram ) {
      String defaultName = ((Diagram)target).getName();
      defaultName = Util.stripJunk(defaultName);

      // FIX - It's probably worthwhile to abstract and factor this chooser
      // and directory stuff. More file handling is coming, I'm sure.

      ProjectBrowser pb = ProjectBrowser.TheInstance;
      Project p =  pb.getProject();
      try {
	JFileChooser chooser = null;
	try {
	  if ( p != null && p.getURL() != null &&
	       p.getURL().getFile().length() > 0 ) {
	    String filename = p.getURL().getFile();
	    if( !filename.startsWith( "/FILE1/+/" ) )
	      chooser  = new JFileChooser( p.getURL().getFile() );
	  }
	}
	catch( Exception ex ) {
	    System.out.println( "exception in opening JFileChooser" );
	    ex.printStackTrace();
	  }

	if( chooser == null ) chooser = new JFileChooser();

	chooser.setDialogTitle( "Save Diagram as GIF: " + defaultName );
	FileFilter filter = FileFilters.GIFFilter;
	chooser.addChoosableFileFilter( filter );
	chooser.setFileFilter( filter );
	File def = new File(  defaultName + ".gif" ); // is .GIF preferred?
	chooser.setSelectedFile( def );

	int retval = chooser.showSaveDialog( pb );
	if( retval == 0 ) {
	  File theFile = chooser.getSelectedFile();
	  if( theFile != null ) {
	    String path = theFile.getParent();
	    String name = theFile.getName();
	    if( !path.endsWith( separator ) ) path += separator;
	    pb.showStatus( "Writing " + path + name + "..." );
	    if( theFile.exists() && !overwrite ) {
	      System.out.println( "Are you sure you want to overwrite " + name + "?");
	      String t = "Overwrite " + path + name;
	      int response =
		JOptionPane.showConfirmDialog(pb, t, t,
					      JOptionPane.YES_NO_OPTION);
	      if (response == JOptionPane.NO_OPTION) return false;
	    }
	    FileOutputStream fo = new FileOutputStream( theFile );
	    cmd.setStream( fo );
	    cmd.doIt();
	    fo.close();
	    pb.showStatus( "Wrote " + path + name );
	    return true;
	  }
	}
      }
      catch( FileNotFoundException ignore )
	{
	  System.out.println( "got a FileNotFoundException" );
	}
      catch( IOException ignore )
	{
	  System.out.println( "got an IOException" );
	  ignore.printStackTrace();
	}
    }

    return false;
  }
} /* end class ActionSaveGIF */

/** Wraps a CmdSaveGIF or CmdSave(E)PS to allow selection of an output file. */

class ActionSaveGraphics extends UMLAction {
  public static final String separator = "/";

  public ActionSaveGraphics() {
    super( "Save Graphics...", NO_ICON);
  }


  public void actionPerformed( ActionEvent ae ) {
    trySave( false );
  }

  public boolean trySave( boolean overwrite ) {
    Object target = ProjectBrowser.TheInstance.getTarget();
    if( target instanceof Diagram ) {
      String defaultName = ((Diagram)target).getName();
      defaultName = Util.stripJunk(defaultName);

      // FIX - It's probably worthwhile to abstract and factor this chooser
      // and directory stuff. More file handling is coming, I'm sure.

      ProjectBrowser pb = ProjectBrowser.TheInstance;
      Project p =  pb.getProject();
      try {
	JFileChooser chooser = null;
	try {
	  if ( p != null && p.getURL() != null &&
	       p.getURL().getFile().length() > 0 ) {
	    String filename = p.getURL().getFile();
	    if( !filename.startsWith( "/FILE1/+/" ) )
	      chooser  = new JFileChooser( p.getURL().getFile() );
	  }
	}
	catch( Exception ex ) {
	    System.out.println( "exception in opening JFileChooser" );
	    ex.printStackTrace();
	  }

	if( chooser == null ) chooser = new JFileChooser();

	chooser.setDialogTitle( "Save Diagram as Graphics: " + defaultName );
	chooser.addChoosableFileFilter( FileFilters.GIFFilter );
	chooser.addChoosableFileFilter( FileFilters.PSFilter );
	chooser.addChoosableFileFilter( FileFilters.EPSFilter );
	//chooser.addChoosableFileFilter( FileFilters.SVGFilter );
	// concerning the following lines: is .GIF preferred?
	chooser.setFileFilter( FileFilters.GIFFilter );
	File def = new File(  defaultName + "."
			      + FileFilters.GIFFilter._suffix );
	chooser.setSelectedFile( def );

	int retval = chooser.showSaveDialog( pb );
	if( retval == 0 ) {
	  File theFile = chooser.getSelectedFile();
	  if( theFile != null ) {
	    String path = theFile.getParent();
	    String name = theFile.getName();
	    String extension=SuffixFilter.getExtension(name);

	    CmdSaveGraphics cmd=null;
	    if (FileFilters.PSFilter._suffix.equals(extension))
		cmd = new CmdSavePS();
	    else if (FileFilters.EPSFilter._suffix.equals(extension))
		cmd = new CmdSaveEPS();
	    else if (FileFilters.GIFFilter._suffix.equals(extension))
		cmd = new CmdSaveGIF();
	    //else if (FileFilters.SVGFilter._suffix.equals(extension))
		//cmd = new CmdSaveSVG();
	    else {
		pb.showStatus("Unknown graphics file type withextension "
			      +extension);
		return false;
	    }

	    if( !path.endsWith( separator ) ) path += separator;
	    pb.showStatus( "Writing " + path + name + "..." );
	    if( theFile.exists() && !overwrite ) {
	      System.out.println( "Are you sure you want to overwrite " + name + "?");
	      String t = "Overwrite " + path + name;
	      int response =
		JOptionPane.showConfirmDialog(pb, t, t,
					      JOptionPane.YES_NO_OPTION);
	      if (response == JOptionPane.NO_OPTION) return false;
	    }
	    FileOutputStream fo = new FileOutputStream( theFile );
	    cmd.setStream(fo);
	    cmd.doIt();
	    fo.close();
	    pb.showStatus( "Wrote " + path + name );
	    return true;
	  }
	}
      }
      catch( FileNotFoundException ignore )
	{
	  System.out.println( "got a FileNotFoundException" );
	}
      catch( IOException ignore )
	{
	  System.out.println( "got an IOException" );
	  ignore.printStackTrace();
	}
    }

    return false;
  }
} /* end class ActionSaveGraphics */



// class ActionAddToProj extends UMLAction {
//   public ActionAddToProj() { super("Add To Project..."); }
// } /* end class ActionAddToProj */

class ActionExit extends UMLAction {
  public ActionExit() { super("Exit", NO_ICON); }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    if (p != null) { // && p.getNeedsSave()) {
      String t = "Save changes to " + p.getName();
      int response =
        JOptionPane.showConfirmDialog(pb, t, t,
                                      JOptionPane.YES_NO_CANCEL_OPTION);
      if (response == JOptionPane.CANCEL_OPTION) return;
      if (response == JOptionPane.YES_OPTION) {
        boolean safe = false;
        if (((ActionSaveProject)Actions.SaveProject).shouldBeEnabled()) {
          safe = ((ActionSaveProject)Actions.SaveProject).trySave(true);
        }
        if (!safe) {
          safe = ((ActionSaveProjectAs)Actions.SaveProjectAs).trySave(false);
        }
        if (!safe) {
          return;
        }
      }
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
    int size = 0;
    try {
      size = Globals.curEditor().getSelectionManager().selections().size();
    }
    //
    //   this can happen when running in a debugger, not sure why
    //
    catch(Exception e) {
      size = 0;
    }
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
  public ActionDeleteFromDiagram() { super("Remove From Diagram", NO_ICON); }
  public boolean shouldBeEnabled() {
    int size = 0;
    try {
      Editor ce = Globals.curEditor();
      Vector figs = ce.getSelectionManager().getFigs();
      size = figs.size();
    }
    catch(Exception e) {
    }
    return size > 0;
  }
  public void actionPerformed(ActionEvent ae) {
    Editor ce = Globals.curEditor();
    SelectionManager sm = ce.getSelectionManager();
    sm.delete();
  }
}


class ActionRemoveFromModel extends UMLChangeAction {
  public ActionRemoveFromModel() { super("Delete From Model", NO_ICON); }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    if (target instanceof MModelElement) return true;

    int size = 0;
    try {
    // needs-more-work: trashing diagrams
      Editor ce = Globals.curEditor();
      Vector figs = ce.getSelectionManager().getFigs();
      size = figs.size();
    }
    catch(Exception e) {}
    if (size > 0) return true;
    //     for (int i = 0; i < size; i++) {
    //       Fig f = (Fig) figs.elementAt(i);
    //       Object owner = f.getOwner();
    //       if (owner instanceof MModelElement) return true;
    //     }
    return false;
  }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
	Project p = pb.getProject();
    if (target instanceof MModelElement) {
		if (sureRemove((MModelElement)target)) {
			// System.out.println("deleting "+target+"+ "+(((MModelElement)target).getMElementListeners()).size());
			p.moveToTrash(target);
		}
    }

    // needs-more-work: trashing diagrams
    else {
      int size = 0;
      try {
        Editor ce = Globals.curEditor();
        Vector figs = ce.getSelectionManager().getFigs();
        size = figs.size();
        for (int i = 0; i < size; i++) {
		  Fig f = (Fig) figs.elementAt(i);
		  Object owner = f.getOwner();
		  if (owner instanceof MModelElement) {
			  if (!sureRemove((MModelElement)owner)) return;
		  }
        }
        for (int i = 0; i < size; i++) {
		  Fig f = (Fig) figs.elementAt(i);
		  Object owner = f.getOwner();
		  if (owner == null) f.delete();
		  else if (owner instanceof MModelElement) p.moveToTrash(owner);
        }
      }
      catch(Exception ex) {}
    }
    super.actionPerformed(ae);
  }

  public boolean sureRemove(MModelElement me) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    int count = p.getPresentationCountFor(me);
    String confirmStr = "";
    if (count > 1) confirmStr += "\nIt will be removed from all diagrams.";

    Collection beh = me.getBehaviors();
    if (beh != null && beh.size() > 0)
      confirmStr += "\nIt's subdiagram will also be removed.";

    if (confirmStr.equals("")) return true;
    String name = me.getName();
    if (name == null || name.equals("")) name = "this element";
    confirmStr = "Are you sure you want to remove " + name + "?" + confirmStr;
    int response =
      JOptionPane.showConfirmDialog(pb, confirmStr, "Are you sure?",
				    JOptionPane.YES_NO_OPTION);
    return (response == JOptionPane.YES_OPTION);
  }
} /* end class ActionRemoveFromModel */

class ActionEmptyTrash extends UMLChangeAction {
  public ActionEmptyTrash() { super("Empty Trash", NO_ICON); }
  public boolean shouldBeEnabled() {
    return Trash.SINGLETON.getSize() > 0;
  }
  public void actionPerformed(ActionEvent ae) {
    Trash.SINGLETON.emptyTrash();
    super.actionPerformed(ae);
  }

} /* end class ActionEmptyTrash */


////////////////////////////////////////////////////////////////
// items on view menu


// class ActionNavUp extends UMLAction {
//   public ActionNavUp() { super("Navigate Up"); }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
//   public void actionPerformed(ActionEvent ae) {
//     NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
//     np.navUp();
//   }
// } /* end class ActionNavUp */

// class ActionNavDown extends UMLAction {
//   public ActionNavDown() { super("Navigate Down"); }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
//   public void actionPerformed(ActionEvent ae) {
//     NavigatorPane np = ProjectBrowser.TheInstance.getNavPane();
//     np.navDown();
//   }
// } /* end class ActionNavDown */

class ActionFind extends UMLAction {
  public ActionFind() { super("Find..."); }
  public void actionPerformed(ActionEvent ae) {
    FindDialog.SINGLETON.setVisible(true);
  }
} /* end class ActionFind */

class ActionGotoDiagram extends UMLAction {
  public ActionGotoDiagram() { super("Goto Diagram...", NO_ICON); }
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
	if ((np == null)) return false;
	boolean b = np.canNavBack();
    return b;
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

// class ActionNavFavs extends UMLAction {
//   public ActionNavFavs() { super("Favorites"); }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionNavFavs */

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
  public ActionNextEditTab() { super("Next Editing Tab", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    MultiEditorPane mep = pb.getEditorPane();
    mep.selectNextTab();
  }
} /* end class ActionNextEditTab */

// class ActionAddToFavs extends UMLAction {
//   public ActionAddToFavs() { super("Add To Favorites"); }
// } /* end class ActionAddToFavs */

class ActionNextDetailsTab extends UMLAction {
  public ActionNextDetailsTab() { super("Next Details Tab", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    DetailsPane dp = pb.getDetailsPane();
    dp.selectNextTab();
  }
} /* end class ActionNextDetailsTab */

// class ActionPrevDetailsTab extends UMLAction {
//   public ActionPrevDetailsTab() { super("Previous Details Tab"); }
// } /* end class ActionPrevDetailsTab */


class ActionShowRapidButtons extends UMLAction {
  public ActionShowRapidButtons() {
    super("Buttons on Selection", NO_ICON);
  }
  public void actionPerformed(ActionEvent ae) {
    SelectionWButtons.toggleShowRapidButtons();
  }
} /* end class ActionShowRapidButtons */


////////////////////////////////////////////////////////////////
// items on create menu

class ActionCreateMultiple extends UMLAction {
  public ActionCreateMultiple() { super("Create Multiple...", NO_ICON); }
  public boolean shouldBeEnabled() {
    //Project p = ProjectBrowser.TheInstance.getProject();
    //return super.shouldBeEnabled() && p != null;
    return false;
  }
} /* end class ActionCreateMultiple */

// class ActionClassWizard extends UMLAction {
//   public ActionClassWizard() { super("Class Wizard..."); }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionClassWizard */

////////////////////////////////////////////////////////////////
// diagram creation actions

class ActionClassDiagram extends UMLChangeAction {
  public ActionClassDiagram() { super("ClassDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    Object target = ProjectBrowser.TheInstance.getDetailsTarget();
    MNamespace ns = p.getCurrentNamespace();
    if (target instanceof MPackage) ns = (MNamespace) target;
    try {
      Diagram d = new UMLClassDiagram(ns);
      p.addMember(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      ProjectBrowser.TheInstance.setTarget(d);
    }
    catch (PropertyVetoException pve) { }
    super.actionPerformed(ae);
  }
} /* end class ActionClassDiagram */

class ActionUseCaseDiagram extends UMLChangeAction {
  public ActionUseCaseDiagram() { super("UseCaseDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      Object target = ProjectBrowser.TheInstance.getDetailsTarget();
      MNamespace ns = p.getCurrentNamespace();
      if (target instanceof MPackage) ns = (MNamespace) target;
      Diagram d  = new UMLUseCaseDiagram(ns);
      p.addMember(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      ProjectBrowser.TheInstance.setTarget(d);
    }
    catch (PropertyVetoException pve) { }
    super.actionPerformed(ae);
  }
} /* end class ActionUseCaseDiagram */

class ActionStateDiagram extends UMLChangeAction {
  public ActionStateDiagram() { super("StateDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    //_cmdCreateNode.doIt();
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
	try {
		Object contextObj = pb.getDetailsTarget();
		if (!(contextObj instanceof MClass)) return;
		MClass cls = (MClass) contextObj;
		String contextNameStr = cls.getName();
		if (contextNameStr == null) contextNameStr = "untitled";
		MStateMachine sm = new MStateMachineImpl();
		sm.setUUID(UUIDManager.SINGLETON.getNewUUID());
		sm.setName(contextNameStr + "StateMachine");
		MCompositeState cs = new MCompositeStateImpl();
		cs.setName("state_machine_top");
		//cs.setNamespace(cls);
		sm.setNamespace(cls);
		sm.setTop(cs);
		cls.addBehavior(sm);
		UMLStateDiagram d = new UMLStateDiagram(cls,sm);
		p.addMember(d);
		ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
		pb.setTarget(d);
    } catch (PropertyVetoException e) {
		System.out.println("PropertyVetoException in ActionStateDiagram");
	}
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    Object target = pb.getDetailsTarget();
    return super.shouldBeEnabled() && p != null &&
      (target instanceof MClass);
  }
} /* end class ActionStateDiagram */

class ActionActivityDiagram extends UMLChangeAction {
  public ActionActivityDiagram() { super("ActivityDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    try {
      MNamespace ns=(MNamespace)pb.getDetailsTarget();
      if (!((ns instanceof MUseCase) || (ns instanceof MClass))) return;
      String contextNameStr = ns.getName();
      if (contextNameStr == null) contextNameStr = "untitled";
      MActivityGraph am = new MActivityGraphImpl();
      am.setUUID(UUIDManager.SINGLETON.getNewUUID());
      am.setName(contextNameStr + "ActivityGraph");
      MCompositeState cs = new MCompositeStateImpl();
      cs.setName("activities_top");
      //cs.setNamespace(ns);
      am.setNamespace(ns);
      am.setTop(cs);
      ns.addBehavior(am);
      UMLActivityDiagram d = new UMLActivityDiagram(ns, am);
      p.addMember(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      pb.setTarget(d);
    } catch (PropertyVetoException pve) {
      System.out.println("PropertyVetoException in ActionActivityDiagram");
    }
    super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Project p = pb.getProject();
    Object target = pb.getDetailsTarget();
    return super.shouldBeEnabled() && p != null &&
      ((target instanceof MUseCase)||(target instanceof MClass)); // or MOperation
  }
} /* end class ActionActivityDiagram */

class ActionCollaborationDiagram extends UMLChangeAction {
  public ActionCollaborationDiagram() { super("CollaborationDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
		MCollaboration c = new MCollaborationImpl();
		c.setUUID(UUIDManager.SINGLETON.getNewUUID());
		c.setName("Collaboration");
		p.getModel().addOwnedElement(c);
		MInteraction interaction = new MInteractionImpl();
		interaction.setName("default Interaction");
		c.addInteraction(interaction);
		UMLCollaborationDiagram d  = new UMLCollaborationDiagram(c);
		p.addMember(d);
		ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
		ProjectBrowser.TheInstance.setTarget(d);
    }
    catch (PropertyVetoException pve) { }
    super.actionPerformed(ae);
  }
} /* end class ActionCollaborationDiagram */

class ActionDeploymentDiagram extends UMLChangeAction {
  public ActionDeploymentDiagram() { super("DeploymentDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    //_cmdCreateNode.doIt();
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      Object target = ProjectBrowser.TheInstance.getDetailsTarget();
      MNamespace ns = p.getCurrentNamespace();
      if (target instanceof MPackage) ns = (MNamespace) target;
      Diagram d  = new UMLDeploymentDiagram(ns);
      p.addMember(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      ProjectBrowser.TheInstance.setTarget(d);
    }
    catch (PropertyVetoException pve) { }
    super.actionPerformed(ae);
  }
} /* end class ActionDeploymentDiagram */


class ActionSequenceDiagram extends UMLChangeAction {
  public ActionSequenceDiagram() { super("SequenceDiagram"); }

  public void actionPerformed(ActionEvent ae) {
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      Object target = ProjectBrowser.TheInstance.getDetailsTarget();
      MNamespace ns = p.getCurrentNamespace();
      if (target instanceof MModel) ns = (MNamespace) target;
      Diagram d  = new UMLSequenceDiagram(ns);
      p.addMember(d);
      ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
      ProjectBrowser.TheInstance.setTarget(d);
    }
    catch (PropertyVetoException pve) { }
    super.actionPerformed(ae);
  }
}  /* end class ActionSequenceDiagram */

////////////////////////////////////////////////////////////////
// model element creation actions

// class ActionClass extends UMLChangeAction {
//   org.tigris.gef.base.Cmd _cmdCreateNode = new
//   org.tigris.gef.base.CmdCreateNode(MClass.class, "Class");

//   public ActionClass() { super("Class"); }

//   public void actionPerformed(ActionEvent ae) {
//     //System.out.println("making class...");
//     _cmdCreateNode.doIt();
//     super.actionPerformed(ae);
//   }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionClass */

// class ActionInterface extends UMLChangeAction {
//   org.tigris.gef.base.Cmd _cmdCreateNode = new
//   org.tigris.gef.base.CmdCreateNode(MInterface.class, "MInterface");

//   public ActionInterface() { super("MInterface"); }

//   public void actionPerformed(ActionEvent ae) {
//     System.out.println("making interface...");
//     _cmdCreateNode.doIt();
//     super.actionPerformed(ae);
//   }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionInterface */

// class ActionActor extends UMLChangeAction {
//   org.tigris.gef.base.Cmd _cmdCreateNode = new
//   org.tigris.gef.base.CmdCreateNode(MActor.class, "MActor");

//   public ActionActor() { super("MActor"); }

//   public void actionPerformed(ActionEvent ae) {
//     //System.out.println("making actor...");
//     _cmdCreateNode.doIt();
//     super.actionPerformed(ae);
//   }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionActor */

// class ActionUseCase extends UMLChangeAction {
//   org.tigris.gef.base.Cmd _cmdCreateNode = new
//   org.tigris.gef.base.CmdCreateNode(MUseCase.class, "MUseCase");

//   public ActionUseCase() { super("MUseCase"); }

//   public void actionPerformed(ActionEvent ae) {
//     //System.out.println("making Use Case...");
//     _cmdCreateNode.doIt();
//     super.actionPerformed(ae);
//   }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionUseCase */

// class MActionState extends UMLChangeAction {
//   org.tigris.gef.base.Cmd _cmdCreateNode = new
//   org.tigris.gef.base.CmdCreateNode(MCompositeState.class, "MState");

//   public MActionState() { super("MState"); }

//   public void actionPerformed(ActionEvent ae) {
//     //System.out.println("making state...");
//     _cmdCreateNode.doIt();
//     super.actionPerformed(ae);
//   }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class MActionState */

class ActionAddInternalTrans extends UMLChangeAction {
  public ActionAddInternalTrans() { super("Add Internal Transition"); }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    if (!(target instanceof MState)) return;
    MState st = (MState) target;
	MTransition t = new MTransitionImpl();
	t.setSource(st);
	t.setTarget(st);
	MStateMachine sm = (MStateMachine)st.getStateMachine();
	sm.addTransition(t);

	// which nsuml-Event to generate as default? is SignalEvent ok?
	MEvent triggerEvent = new MSignalEventImpl();
	triggerEvent.setName("event");
	t.setTrigger(triggerEvent);

	MGuard guard = new MGuardImpl();
	guard.setName("condition");
	t.setGuard(guard);
	MActionSequence as = new MActionSequenceImpl();
	as.setName("actions");
	t.setEffect(as);
	t.setState(st);
	super.actionPerformed(ae);
  }
  public boolean shouldBeEnabled() {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getDetailsTarget();
    return super.shouldBeEnabled() && target instanceof MState;
  }
} /* end class ActionAddInternalTrans */

// class ActionPseudostate extends UMLChangeAction {
//   org.tigris.gef.base.Cmd _cmdCreateNode = new
//   org.tigris.gef.base.CmdCreateNode(MPseudostate.class, "MPseudostate");

//   public ActionPseudostate() { super("MPseudostate"); }

//   public void actionPerformed(ActionEvent ae) {
//     _cmdCreateNode.doIt();
//     super.actionPerformed(ae);
//   }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionPseudostate */

// class ActionPackage extends UMLChangeAction {
//   org.tigris.gef.base.Cmd _cmdCreateNode = new
//   org.tigris.gef.base.CmdCreateNode(MSubsystem.class, "Package");

//   public ActionPackage() { super("Package"); }

//   public void actionPerformed(ActionEvent ae) {
//     _cmdCreateNode.doIt();
//     super.actionPerformed(ae);
//   }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionPackage */

// class ActionInstance extends UMLChangeAction {
//   org.tigris.gef.base.Cmd _cmdCreateNode = new
//   org.tigris.gef.base.CmdCreateNode(MInstance.class, "MInstance");

//   public ActionInstance() { super("MInstance"); }

//   public void actionPerformed(ActionEvent ae) {
//     _cmdCreateNode.doIt();
//     super.actionPerformed(ae);
//   }
//   public boolean shouldBeEnabled() {
//     Project p = ProjectBrowser.TheInstance.getProject();
//     return super.shouldBeEnabled() && p != null;
//   }
// } /* end class ActionInstance */

class ActionAddAttribute extends UMLChangeAction {
    // needs-more-work: should be part of java binding or common elements
 //    public static MDataType INT_TYPE = new MDataTypeImpl();
//     static{ INT_TYPE.setName("int"); }

    public ActionAddAttribute() { super("Add Attribute"); }

    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Project p = pb.getProject();
	Object target = pb.getDetailsTarget();
	if (!(target instanceof MClassifier)) return;
	MClassifier cls = (MClassifier) target;
	MClassifier intType = p.findType("int");
	MAttribute attr = new MAttributeImpl();
	attr.setName("newAttr");
	attr.setType(intType);
	attr.setInitialValue(new MExpression("Java", "0"));
	attr.setVisibility(MVisibilityKind.PUBLIC);
	cls.addFeature(attr);
	super.actionPerformed(ae);
    }

    public boolean shouldBeEnabled() {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Object target = pb.getDetailsTarget();
	return super.shouldBeEnabled() && target instanceof MClassifier;
    }
} /* end class ActionAddAttribute */

class ActionAddOperation extends UMLChangeAction {
    // needs-more-work: should be part of java binding or common elements
//     public static MDataType VOID_TYPE = new MDataTypeImpl();
//     static { VOID_TYPE.setName("void"); }

    public ActionAddOperation() { super("Add Operation"); }

    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Project p = pb.getProject();
	Object target = pb.getDetailsTarget();
	if (!(target instanceof MClassifier)) return;
	MClassifier cls = (MClassifier) target;
	MClassifier voidType = p.findType("void");
	MOperation oper = new MOperationImpl();
	MParameter returnParameter = new MParameterImpl();
	returnParameter.setKind(MParameterDirectionKind.RETURN);
	oper.addParameter(returnParameter);
	oper.setName("newOperation");
	oper.setVisibility(MVisibilityKind.PUBLIC);
	cls.addFeature(oper);
	super.actionPerformed(ae);
    }
    public boolean shouldBeEnabled() {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Object target = pb.getDetailsTarget();
	return super.shouldBeEnabled() && target instanceof MClassifier;
    }
} /* end class ActionAddOperation */

class ActionAddMessage extends UMLChangeAction {
	public ActionAddMessage() { super("Add Message"); }

	public void actionPerformed(ActionEvent ae) {
		ProjectBrowser pb = ProjectBrowser.TheInstance;
		Object target = pb.getDetailsTarget();
		Object d = pb.getTarget();
		if (!(d instanceof UMLCollaborationDiagram)) return;
		UMLCollaborationDiagram cd = (UMLCollaborationDiagram) d;
		if (!(target instanceof MAssociationRole)) return;
		MAssociationRole ar = (MAssociationRole) target;

		Editor ce = Globals.curEditor();
		GraphModel gm = ce.getGraphModel();
		GraphNodeRenderer renderer = ce.getGraphNodeRenderer();
		Layer lay = ce.getLayerManager().getActiveLayer();
		SelectionManager sm = ce.getSelectionManager();
		Vector figs = sm.selections();
		Selection cf = (Selection) figs.firstElement();
		FigEdge curFig = (FigEdge) cf.getContent();
		Point center = curFig.center();

		String nextStr = "" + (cd.getNumMessages() + 1);
		MMessage msg = new MMessageImpl();
		msg.setName(nextStr);
		Collection ascEnds = ar.getConnections();

		if (ascEnds.size() != 2 ) return;
		Iterator iter = ascEnds.iterator();
		MAssociationEndRole aer1 = (MAssociationEndRole)iter.next();
		MAssociationEndRole aer2 = (MAssociationEndRole)iter.next();

		// by default the "first" Classifierrole is the Sender,
		// should be configurable in PropPanelMessage!
		MClassifierRole crSrc = (MClassifierRole)aer1.getType();
		MClassifierRole crDst = (MClassifierRole)aer2.getType();
		msg.setSender(crSrc);
		msg.setReceiver(crDst);
		MUninterpretedAction ua = new MUninterpretedActionImpl();
		msg.setAction(ua);
		ar.addMessage(msg);
		MCollaboration collab = (MCollaboration) ar.getNamespace();
		// collab.addOwnedElement(msg);
		Collection interactions = collab.getInteractions();
		// at the moment there can be only one Interaction per Collaboration
		Iterator iter2 = interactions.iterator();
		((MInteraction)iter2.next()).addMessage(msg);
		FigNode pers = renderer.getFigNodeFor(gm, lay, msg);
		Collection messages = ar.getMessages();
		int size = messages.size();
		int percent = 15 + size*10;
		if (percent > 100) percent = 100;
		curFig.addPathItem(pers, new PathConvPercent(curFig, percent, 10));
		curFig.updatePathItemLocations();
		lay.add(pers);
		super.actionPerformed(ae);
	}

	public boolean shouldBeEnabled() {
		ProjectBrowser pb = ProjectBrowser.TheInstance;
		Object target = pb.getDetailsTarget();
		return super.shouldBeEnabled() && target instanceof MAssociationRole;
	}
}


/* end class ActionAddMessage */

// class ActionModel extends UMLChangeAction {
//   //org.tigris.gef.base.Cmd _cmdCreateNode = new
//   //org.tigris.gef.base.CmdCreateNode(ru.novosoft.uml.model_management.MModel.class, "MModel");
//   // needs-more-work: need FigModel and UMLPackageDiagram
//   public ActionModel() { super("MModel"); }

//   public void actionPerformed(ActionEvent ae) {
//     //_cmdCreateNode.doIt();
//     Project p = ProjectBrowser.TheInstance.getProject();
//     //try {
//       p.addMember(new MModel());
//       super.actionPerformed(ae);
//       //}
//     //catch (PropertyVetoException pve) { }
//   }

// } /* end class ActionModel */

class ActionAddTopLevelPackage extends UMLChangeAction {
  public ActionAddTopLevelPackage() {
    super("Add Top-Level Package", NO_ICON);
  }

  public void actionPerformed(ActionEvent ae) {
    Project p = ProjectBrowser.TheInstance.getProject();
    try {
      int numPacks = p.getModels().size();
      String nameStr = "package_" + (numPacks + 1);
	  MModel model = new MModelImpl();
	  model.setName(nameStr);
      p.addMember(model);
      super.actionPerformed(ae);
      Actions.ClassDiagram.actionPerformed(ae);
    }
    catch (PropertyVetoException pve) { }
  }

} /* end class ActionAddTopLevelPackage */


////////////////////////////////////////////////////////////////
// generate menu actions

class ActionGenerateOne extends UMLAction {
  public ActionGenerateOne() {
    super("Generate Selected Classes", NO_ICON);
  }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Editor ce = org.tigris.gef.base.Globals.curEditor();
    Vector sels = ce.getSelectionManager().getFigs();
    java.util.Enumeration enum = sels.elements();
    Vector classes = new Vector();
    while (enum.hasMoreElements()) {
      Fig f = (Fig) enum.nextElement();
      Object owner = f.getOwner();
      if (!(owner instanceof MClass) && !(owner instanceof MInterface))
	continue;
      MClassifier cls = (MClassifier) owner;
      String name = cls.getName();
      if (name == null || name.length() == 0) continue;
      classes.addElement(cls);
    }
    ClassGenerationDialog cgd = new ClassGenerationDialog(classes);
    cgd.show();
  }

  public boolean shouldBeEnabled() {
    if (!super.shouldBeEnabled()) return false;
    boolean foundOne = false;
    Editor ce = org.tigris.gef.base.Globals.curEditor();
    if(ce != null) {
      Vector sels = ce.getSelectionManager().getFigs();
      java.util.Enumeration enum = sels.elements();
      while (enum.hasMoreElements()) {
        Fig f = (Fig) enum.nextElement();
        Object owner = f.getOwner();
        if (!(owner instanceof MClass) && !(owner instanceof MInterface))
          continue;
        MClassifier cls = (MClassifier) owner;
        String name = cls.getName();
        if (name == null || name.length() == 0) return false;
        foundOne = true;
      }
    }
    return foundOne;
  }
} /* end class ActionGenerateOne */

class ActionGenerateAll extends UMLAction {
  public ActionGenerateAll() {
    super("Generate All Classes", NO_ICON);
  }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    Object target = pb.getTarget();
    if (!(target instanceof UMLClassDiagram)) return;
    UMLClassDiagram d = (UMLClassDiagram) target;
    Vector classes = new Vector();
    Vector nodes = d.getNodes();
    java.util.Enumeration enum = nodes.elements();
    while (enum.hasMoreElements()) {
      Object owner = enum.nextElement();
      if (!(owner instanceof MClass) && !(owner instanceof MInterface))
	continue;
      MClassifier cls = (MClassifier) owner;
      String name = cls.getName();
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

class ActionAutoCritique extends UMLAction {
  public ActionAutoCritique() {
    super("Toggle Auto-Critique", NO_ICON);
  }
  public void actionPerformed(ActionEvent ae) {
    Designer d = Designer.TheDesigner;
    boolean b = d.getAutoCritique();
    d.setAutoCritique(!b);
  }
} /* end class ActionAutoCritique */

class ActionOpenDecisions extends UMLAction {
  public ActionOpenDecisions() { super("Design Issues...", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    DesignIssuesDialog d = new DesignIssuesDialog(ProjectBrowser.TheInstance);
    d.show();
  }
} /* end class ActionOpenDecisions */

class ActionOpenGoals extends UMLAction {
  public ActionOpenGoals() { super("Design Goals...", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    GoalsDialog d = new GoalsDialog(ProjectBrowser.TheInstance);
    d.show();
  }
} /* end class ActionOpenGoals */

class ActionOpenCritics extends UMLAction {
  public ActionOpenCritics() { super("Browse Critics...", NO_ICON); }
  public void actionPerformed(ActionEvent ae) {
    CriticBrowserDialog dialog = new CriticBrowserDialog();
    dialog.show();
  }

} /* end class ActionOpenCritics */


class ActionFlatToDo extends UMLAction {
  public ActionFlatToDo() { super("Toggle Flat View", NO_ICON); }
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
  public ToDoItemAction(String name) { super(name, false, HAS_ICON); }
  public ToDoItemAction(String name, boolean hasIcon) {
    super(name, false, hasIcon);
  }

  public void updateEnabled(Object target) {
    if (target == null) return;
    _target = target;
    setEnabled(shouldBeEnabled(target));
  }

  public boolean shouldBeEnabled(Object target) {
    return target instanceof ToDoItem;
  }
}

// class ActionRecordFix extends ToDoItemAction {
//   public ActionRecordFix() { super("Record My Fix..."); }
// } /* end class ActionRecordFix */

// class ActionReplayFix extends ToDoItemAction {
//   public ActionReplayFix() { super("Replay My Fix..."); }
// } /* end class ActionReplayFix */

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
  public ActionMoreInfo() { super("More Info...", NO_ICON); }
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
  public ActionAboutArgoUML() { super("About Argo/UML", NO_ICON); }

  public void actionPerformed(ActionEvent ae) {
    AboutBox box = new AboutBox();
    box.show();
  }
  public boolean shouldBeEnabled() { return true; }
} /* end class ActionAboutArgoUML */


class ActionProperties extends UMLAction {
  public ActionProperties() { super("Properties", NO_ICON); }

  public void actionPerformed(ActionEvent ae) {
    ProjectBrowser pb = ProjectBrowser.TheInstance;
    if (pb == null) return;
    DetailsPane dp = pb.getDetailsPane();
    dp.selectTabNamed("Properties");
  }
  public boolean shouldBeEnabled() { return true; }
} /* end class ActionShowProperties */


class ActionMultiplicity extends UMLAction {
  String str = "";
  MMultiplicity mult = null;
  public ActionMultiplicity(MMultiplicity m, String s) {
      //super(m.getLower() + ".." + m.getUpper(), NO_ICON);
    super(m.toString(), NO_ICON);
    str = s;
    mult = m;
  }

  public void actionPerformed(ActionEvent ae) {
    Vector sels = Globals.curEditor().getSelectionManager().selections();
    if( sels.size() == 1 ) {
      Selection sel = (Selection) sels.firstElement();
      Fig f = sel.getContent();
      Object owner = ((FigEdgeModelElement) f).getOwner();
      java.util.List ascEnds = ((MAssociation) owner).getConnections();
      MAssociationEnd ascEnd = null;
      if(str.equals("src"))
        ascEnd = (MAssociationEnd) ascEnds.get(0);
      else
        ascEnd = (MAssociationEnd) ascEnds.get(ascEnds.size()-1);
      ascEnd.setMultiplicity(mult);
    }
  }
  public boolean shouldBeEnabled() { return true; }
} /* end class ActionSrcMultOneToMany */

class ActionAggregation extends UMLAction {
  String str = "";
  MAggregationKind agg = null;
  public ActionAggregation(MAggregationKind a, String s) {
    super(a.getName(), NO_ICON);
    str = s;
    agg = a;
  }

  public void actionPerformed(ActionEvent ae) {
    Vector sels = Globals.curEditor().getSelectionManager().selections();
    if( sels.size() == 1 ) {
      Selection sel = (Selection) sels.firstElement();
      Fig f = sel.getContent();
      Object owner = ((FigEdgeModelElement) f).getOwner();
      java.util.List ascEnds = ((MAssociation) owner).getConnections();
      MAssociationEnd ascEnd = null;
      if(str.equals("src"))
        ascEnd = (MAssociationEnd) ascEnds.get(0);
      else
        ascEnd = (MAssociationEnd) ascEnds.get(ascEnds.size()-1);
      ascEnd.setAggregation(agg);
    }
  }
  public boolean shouldBeEnabled() { return true; }
} /* end class ActionSrcMultOneToMany */


class ActionCompartmentDisplay extends UMLAction {
  boolean display = false;
  String compartment = "";
  public ActionCompartmentDisplay(boolean d, String c) {
    super(c, NO_ICON);
    display = d;
    compartment = c;
  }

  public void actionPerformed(ActionEvent ae) {
    Vector sels = Globals.curEditor().getSelectionManager().selections();
    if( sels.size() == 1 ) {
      Selection sel = (Selection) sels.firstElement();
      Fig f = sel.getContent();
      if (compartment.equals("Show Attribute Compartment"))
        ((FigClass)f).setAttributeVisible(display);
      else if (compartment.equals("Hide Attribute Compartment"))
        ((FigClass)f).setAttributeVisible(display);
      else if (compartment.equals("Show Operation Compartment"))
        ((FigClass)f).setOperationVisible(display);
      else if (compartment.equals("Hide Operation Compartment"))
        ((FigClass)f).setOperationVisible(display);
      else if (compartment.equals("Show All Compartments")) {
        ((FigClass)f).setAttributeVisible(display);
        ((FigClass)f).setOperationVisible(display);
      }
      else {
        ((FigClass)f).setAttributeVisible(display);
        ((FigClass)f).setOperationVisible(display);
      }
    }
  }
  public boolean shouldBeEnabled() { return true; }
} /* end class ActionCompartmentDisplay */


