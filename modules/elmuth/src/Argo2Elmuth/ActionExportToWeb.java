// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package Argo2Elmuth;

import org.argouml.application.api.*;
import org.argouml.uml.ui.*;
import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.util.*;
import org.argouml.util.osdep.*;
import org.tigris.gef.base.*;
import org.tigris.gef.util.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Vector;
import javax.swing.*;
// import Argo2Elmuth.Argo2Elmuth;



/** Action object for exporting ArgoUML projects into Elmuth
 *
 *  @author Paolo Pinciani
 *  @since  0.9.10
 */

public class ActionExportToWeb extends UMLAction
implements PluggableMenu {

	//File filter for choosing directories only
	private class DirectoryFileFilter extends javax.swing.filechooser.FileFilter {
		public DirectoryFileFilter() { }
		
		public boolean accept(File f) {
			if (f!=null && f.isDirectory())
				return true;
			return false;	
		}
		
		public String getDescription() {
			return "All Directories";
		}
	}

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionExportToWeb SINGLETON = new ActionExportToWeb(); 

    private static JMenuItem _menuItem = null;

    private final String ElmuthArchive = Argo.getArgoHome() + System.getProperty("file.separator") + "ext" + System.getProperty("file.separator") + "argo_elmuth.jar";


    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionExportToWeb() {
		super( "Export to Web...", NO_ICON);
    }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed( ActionEvent ae ) {
		trySave( false );
    }

    public boolean trySave( boolean overwrite ) {
		Object target = ProjectBrowser.TheInstance.getActiveDiagram();
		if( target instanceof Diagram ) {
	
		    ProjectBrowser pb = ProjectBrowser.TheInstance;
		    Project p =  pb.getProject();
			
		    String defaultName = p.getName();
		    defaultName = Util.stripJunk(defaultName);
			if (defaultName.endsWith("zargo"))
				defaultName = defaultName.substring(0,defaultName.length()-6);

			//get File Chooser:
			JFileChooser chooser = null;
		    try {
			    if ( p != null && p.getURL() != null && p.getURL().getFile().length() > 0 ) {
					String filename = p.getURL().getFile();
					if( !filename.startsWith( "/FILE1/+/" ) )
						chooser  = OsUtil.getFileChooser( p.getURL().getFile() );
		    	}
			}
			catch( Exception ex ) {
				System.out.println( "exception in opening JFileChooser" );
				ex.printStackTrace();
			}
			if( chooser == null ) chooser = OsUtil.getFileChooser();

			chooser.setDialogTitle( "Export project to web: " + defaultName );
			chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());

			chooser.setAccessory(getInfoPanel());
			

			DirectoryFileFilter filter = new DirectoryFileFilter();
			chooser.setFileFilter(filter);
			File def = new File(defaultName);
			chooser.setSelectedFile(def);

			int retval = chooser.showSaveDialog( pb );
			if( retval == 0 ) {
			    File theFile = chooser.getSelectedFile();
			    if( theFile != null ) {
				String path = theFile.getParent();
				String name = theFile.getName();
			
				if( theFile.exists() && !overwrite ) {
				    System.out.println( "Are you sure you want to overwrite " + name + "?");
				    String t = "Overwrite " + path + name;
				    int response = JOptionPane.showConfirmDialog(pb, t, t, JOptionPane.YES_NO_OPTION);
				    if (response == JOptionPane.NO_OPTION) return false;
				}
				
				Argo2Elmuth webExporter = new Argo2Elmuth(ElmuthArchive,path,name);
				if (!webExporter.exportToWeb()) 
				{
					pb.showStatus("Can't export project");
					System.out.println("Can't export project: " + webExporter.getErrorMessage());
				}
				else
					pb.showStatus( "Wrote " + name );
				return true;
			    }
		    }
		}

		return false;
	}
	
	private JPanel getInfoPanel() {
		JPanel infoPanel = new JPanel();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		infoPanel.setLayout(gridbag);
		infoPanel.setFont(new Font("Dialog", Font.PLAIN, 10));
		
		Label l1 = new Label("This command exports ArgoUML");
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(0,10,0,0);
		c.ipadx = 0;
		c.ipady = 0;
		c.anchor = GridBagConstraints.NORTHWEST;
		gridbag.setConstraints(l1, c);
		infoPanel.add(l1);
		
		Label l2 = new Label("projects into Elmuth");
		c.gridy = 1;
		gridbag.setConstraints(l2, c);
		infoPanel.add(l2);
		
		Label l5 = new Label("");
		c.gridy = 2;
		gridbag.setConstraints(l5, c);
		infoPanel.add(l5);
		
		Label l3 = new Label("For more information see:");
		c.gridy = 3;
		gridbag.setConstraints(l3, c);
		infoPanel.add(l3);
		
		Label l4 = new Label("http://www.cs.unibo.it/~pinciani/unibo/elmuth");
		c.gridy = 4;
		gridbag.setConstraints(l4, c);
		infoPanel.add(l4);
		
		return infoPanel;
	}
	
	public boolean initializeModule() {
	      // Make sure "argo_elmuth.jar" exists in "$ARGO_HOME/ext/emuth.jar"
		File elmuth_jar = new File(ElmuthArchive);
		if (!elmuth_jar.exists()) {
			Argo.log.error("Archive \"argo_elmuth.jar\" does not exists in \"" + elmuth_jar.getAbsolutePath() + "\"");
			Argo.log.error("Unable to initialize Elmuth plugin.");
			return false;
		}
		
		// Make sure Xalan-Java1 is available:
		try {
			Class xalan = Class.forName("org.apache.xalan.xslt.XSLTProcessor");
		} catch (Exception e) {
	    		Argo.log.error("Xalan does not appear to be in the classpath.");
			Argo.log.error("Unable to initialize Elmuth plugin.");
			return false;
		}

		Argo.log.info ("+----------------------------+");
		Argo.log.info ("| \"Export to Web\" enabled! |");
		Argo.log.info ("+----------------------------+");
		return true;
    }

    public Object[] buildContext(JMenuItem a, String b) {
        return new Object[] { a, b };
    }

    public void setModuleEnabled(boolean enabled) { }
    public boolean isModuleEnabled() { return true; }
    public Vector getModulePopUpActions(Vector v, Object o) { return null; }
    public boolean shutdownModule() { return true; }

    public String getModuleName() { return "ActionExportToWeb"; }
    public String getModuleDescription() { return "Export to Web command"; }
    public String getModuleAuthor() { return "Paolo Pinciani"; }
    public String getModuleVersion() { return "0.9.10"; }
    public String getModuleKey() { return "module.tools.elmuth"; }

    public boolean inContext(Object[] o) {
        if (o.length < 2) return false;
	// Allow ourselves on the "Tools" menu.
	if ((o[0] instanceof JMenuItem) &&
	        ("Tools".equals(o[1]))) {
	    return true;
	}
        return false;
    }

    public JMenuItem getMenuItem(JMenuItem mi, String s) {
        if (_menuItem == null) {
            _menuItem = new JMenuItem(Argo.localize(Argo.MENU_BUNDLE,
	                                            "Export to Web..."));
	    _menuItem.addActionListener(this);
	}
        return _menuItem;
    }
	
}
/* end class ActionExportToWeb */   













