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

//$Id$

package org.argouml.uml.reveng;

import java.io.*;
import org.argouml.kernel.*;
import org.argouml.uml.reveng.java.*;
import org.argouml.uml.diagram.ui.*;
import org.argouml.uml.diagram.static_structure.layout.*;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.tigris.gef.base.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.*;

import org.argouml.ui.*;
import org.argouml.application.api.*;
import org.argouml.util.logging.*;
import org.argouml.cognitive.Designer;
import org.argouml.util.osdep.OsUtil;
import org.argouml.util.FileFilters;
import org.argouml.util.SuffixFilter;

import org.apache.log4j.Category;

/**
 * This is the main class for all import classes.
 *
 * <p>It provides JPanels for tailoring the import run in the FileChooser.
 *
 * <p>The Import run is started by calling doFile(Project, File)
 *
 * <p>Supports recursive search in folder for all .java classes.
 *
 * <p>$Revision$
 * <p>$Date$
 *
 * @author Andreas Rueckert <a_rueckert@gmx.net>
 */
public class Import {

    // Create a interface to the current diagram
    private DiagramInterface _diagram;

    private JComponent configPanel = null;
    private JCheckBox descend;

    /** The files that needs a second RE pass. */
    private Vector secondPassFiles;

	// Imported directory
	private String src_path;
	
	private JCheckBox create_diagrams;
    private JCheckBox minimise_figs;

    // log4j logging
    private static Category cat = Category.getInstance(org.argouml.uml.reveng.Import.class);

	public static final String separator = "/"; //System.getProperty("file.separator");
	private ProjectBrowser pb = ProjectBrowser.TheInstance;
	private Project p = ProjectManager.getManager().getCurrentProject();
	private JDialog dialog;
	// TODO: change to pluggable module
	private JavaImport module = new JavaImport();
	
	private ImportStatusScreen iss;
	
	/**
	 * Creates dialog window with chooser and configuration panel.
	 *
	 */
	public Import() {
			JComponent chooser = module.getChooser(this);
			dialog = new JDialog(pb, "Import sources");
			dialog.setModal(true);
			dialog.getContentPane().add(chooser, BorderLayout.WEST);			
			dialog.getContentPane().add(getConfigPanel(), BorderLayout.EAST);
			dialog.pack();
			int x = (pb.getSize().width-dialog.getSize().width)/2;
			int y = (pb.getSize().height-dialog.getSize().height)/2;
			dialog.setLocation(x > 0?x:0, y>0?y:0);
			dialog.setVisible(true);			
	}
	
	public Project getProject() {
		return p;
	}
	
	public ProjectBrowser getProjectBrowser() {
		return pb;
	}
	/**
	 * Close dialog window.
	 *
	 */
	public void disposeDialog() {
		dialog.dispose();
	}

    /**
     * Get the panel that lets the user set reverse engineering
     * parameters.
     */
    public JComponent getConfigPanel() {

	if(configPanel == null) {
	    JPanel general = new JPanel();
	    general.setLayout(new GridLayout(3,1));

	    descend = new JCheckBox("Descend directories recursively.");
	    descend.setSelected(true);
	    general.add(descend);
            
		create_diagrams = new JCheckBox("Create diagrams from imported code", true);
		general.add(create_diagrams);
                
                minimise_figs = new JCheckBox("Minimise Class icons in diagrams", true);
		general.add(minimise_figs);

                // de-selects the fig minimising if we are not creating diagrams
                create_diagrams.addActionListener(new ActionListener(){
                    public void actionPerformed(java.awt.event.ActionEvent actionEvent){
                        if(!create_diagrams.isSelected())
                            minimise_figs.setSelected(false);}});
                
	    JTabbedPane tab = new JTabbedPane();
	    tab.add(general, "General");
	    tab.add(module.getConfigPanel(), "Java");
	    configPanel = tab;
	}
	return configPanel;
    }


    /**
     *  <p> This method is called by ActionImportFromSources to
     * start the import run.
     *
     * <p>The method that for all parsing actions. It calls the
     * actual parser methods depending on the type of the
     * file.
     *
     * @param p The current Argo project.
     * @param f The file or directory, we want to parse.
     */
    public void doFile() {
		Vector files = module.getList(this);
		_diagram = getCurrentDiagram();

	
		pb.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        //turn off critiquing for reverse engineering
        boolean b = Designer.TheDesigner.getAutoCritique();
        if (b)  Designer.TheDesigner.setAutoCritique(false);
        iss = new ImportStatusScreen("Importing", "Splash");
		SwingUtilities.invokeLater(new ImportRun(files, b));
		iss.show();
    }

	/**
	 * Set path for processed directory.
	 */
	public void setSrcPath(String path) {
		src_path = path;
	}
	
	/**
	 * @return path for processed directory.
	 */
	public String getSrcPath() {
		return src_path;
	}
	

    /**
     * <p>Parse 1 Java file, using JavaImport.
     *
     * @param f The file to parse.
     * @exception Parser exception.
     */
    public void parseFile( Project p, Object f) throws Exception {

	// Is this file a Java source file?
	if ( module.isParseable(f)) {
		pb.showStatus("Parsing " + f.toString() + "...");
	    module.parseFile( p, f, _diagram, this);
	}
    }

	/**
	 * Check, if "Create diagrams from imported code" is selected.
	 * @return true, if "Create diagrams from imported code" is selected
	 */
	public boolean isCreateDiagramsChecked() {
			if(create_diagrams != null)
		return create_diagrams.isSelected();
			else
				return true;
	}

	/**
	 * Check, if "Discend directories recursively" is selected.
	 * @return true, if "Discend directories recursively" is selected
	 */
	public boolean isDiscendDirectoriesRecursively() {
			if(descend != null)
		return descend.isSelected();
			else
				return true;
	}

	/**
	 * Check, if "Minimise Class icons in diagrams" is selected.
	 * @return true, if "Minimise Class icons in diagrams" is selected
	 */
	public boolean isMinimiseFigsChecked() {
            if(minimise_figs != null)
		return minimise_figs.isSelected();
            else
                return false;
	}
        
    /**
     * If we have modified any diagrams, the project was modified and
     * should be saved. I don't consider a import, that only modifies
     * the metamodel, at this point (Andreas Rueckert <a_rueckert@gmx.net> ).
     * Calling Project.setNeedsSave(true) doesn't work here, because
     * Project.postLoad() is called after the import and it sets the
     * _needsSave flag to false.
     *
     * @return true, if any diagrams where modified and the project should be saved before exit.
     */
    public boolean needsSave() {
	return (_diagram.getModifiedDiagrams().size() > 0);
    }

	/** Set target diagram.
	 * @return selected diagram, if it is class diagram,
	 * else return null.
	 *
	 */
	private DiagramInterface getCurrentDiagram() {
		DiagramInterface result = null;
		if (Globals.curEditor().getGraphModel() instanceof ClassDiagramGraphModel) {
			result =  new DiagramInterface(Globals.curEditor());
		}
		return result;
	}
//}

/**
 * This class parses each file in turn and allows the GUI to refresh
 * itself by performing the run() once for each file.
 *
 * <p> this class also listens for a "Stop" message from the ImportStatusScreen,
 * in order to cancel long import runs.
 */
class ImportRun implements Runnable {
    Vector _filesLeft;
    int _countFiles;
    int _countFilesThisPass;
    Vector _nextPassFiles;
    SimpleTimer _st;
    boolean cancelled;
    
    boolean criticThreadWasOn;

    public ImportRun(Vector f, boolean critic) {
    iss.addCancelButtonListener(new ActionListener(){
            public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                cancel();
            }});
        
	_filesLeft = f;
	_countFiles = _filesLeft.size();
	_countFilesThisPass = _countFiles;
	_nextPassFiles = new Vector();
	_st = new SimpleTimer("ImportRun");
	_st.mark("start");
        cancelled=false;
        criticThreadWasOn = critic;
    }

    /**
     * called once for each file to be parsed.
     *
     * <p>to refresh the GUI it calls itself again using the SwingUtilities.invokeLater(...)
     * method.
     */
    public void run() {

	if (_filesLeft.size() > 0) {
	    Object curFile = _filesLeft.elementAt(0);
	    _filesLeft.removeElementAt(0);

	    try {
		_st.mark(curFile.toString());
		pb.showStatus("Importing " + curFile.toString());
			      //+ " (in " + curFile.getParent());
		parseFile(p, curFile); // Try to parse this file.

		int tot;
		iss.setMaximum(tot = _countFiles
				+ (_diagram == null ? 0 : _diagram.getModifiedDiagrams().size()/10));
		int act;
		iss.setValue(act = _countFiles
			      - _filesLeft.size() - _nextPassFiles.size());
		pb.getStatusBar().showProgress(100 * act/tot);
	    }
	    catch(Exception e1) {
		//Argo.log.debug(e1);
		_nextPassFiles.addElement(curFile);
                
                // RuntimeExceptions should be reported here!
                if(e1 instanceof RuntimeException)
                    cat.error("program bug encountered in reverese engineering, the project file will be corrupted\n"
                              +e1);
                else
                    cat.warn("exception encountered in reverese engineering, the project file will be corrupted\n"
                              +e1);
                e1.printStackTrace();
	    }

            if(!isCancelled()){
                SwingUtilities.invokeLater(this);
	    return;}
	}

	if (_nextPassFiles.size() > 0
	    && _nextPassFiles.size() < _countFilesThisPass) {
	    _filesLeft = _nextPassFiles;
	    _nextPassFiles = new Vector();
	    _countFilesThisPass = _filesLeft.size();

	    SwingUtilities.invokeLater(this);
	    return;
	}

	// Do post load processings.
	_st.mark("postprocessings");

	// Check if any diagrams where modified and the project
	// should be saved before exiting.
	if(_diagram != null && needsSave()) {
	    p.setNeedsSave(true);
	}

	pb.showStatus("Import done");

	// Layout the modified diagrams.
	_st.mark("layout");
	if (_diagram != null) {
		for(int i=0; i < _diagram.getModifiedDiagrams().size(); i++) {
	    	ClassdiagramLayouter layouter =
			new ClassdiagramLayouter((UMLDiagram)
					 (_diagram.getModifiedDiagrams()
					  .elementAt(i)));
	    	layouter.layout();

	    	// Resize the diagram???
	    	iss.setValue(_countFiles +(i + 1)/10);
		}
	}

	iss.done();
    pb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    // turn criticing on again
    if(criticThreadWasOn)  Designer.TheDesigner.setAutoCritique(true);
        
	Argo.log.info(_st);
	pb.getStatusBar().showProgress(0);

    }
    
    private void cancel(){cancelled=true;}
    
    private boolean isCancelled(){return cancelled;}

}

/**
 * The statusbar showing the progress of the Import run.
 */
class ImportStatusBar extends StatusBar {
    public void setMaximum(int i) { _progress.setMaximum(i); }
    public void setValue(int i) { _progress.setValue(i); }
}

/**
 * A window that shows the progress bar and a cancel button.
 */
class ImportStatusScreen extends JDialog {
    protected ImportStatusBar _statusBar = new ImportStatusBar();
    
    private JButton cancelButton;
    private JLabel progressLabel;
    
    private int numberOfFiles;

    public ImportStatusScreen(String title, String iconName) {
	super();
	if (title != null) setTitle(title);
	Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
	getContentPane().setLayout(new BorderLayout(0, 0));
        
        // Parsing file x of z.
        JPanel topPanel = new JPanel();
        progressLabel = new JLabel();
        topPanel.add(progressLabel);
        getContentPane().add(topPanel, BorderLayout.NORTH);
        
        // progress bar
        getContentPane().add(_statusBar, BorderLayout.CENTER);
        
        // stop button
        cancelButton = new JButton("Stop");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(cancelButton);
	getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        
        Dimension contentPaneSize = getContentPane().getPreferredSize();
	setSize(contentPaneSize.width, contentPaneSize.height);
	setLocation(scrSize.width/2 - contentPaneSize.width/2,
		    scrSize.height/2 - contentPaneSize.height/2);
        pack();
        this.setResizable(false);
    }

    public void setMaximum(int i) { _statusBar.setMaximum(i);numberOfFiles = i; }
    public void setValue(int i) {
	_statusBar.setValue(i);
        progressLabel.setText("Parsing file "+i+" of "+numberOfFiles+".");
	repaint();
    }
    
    public void addCancelButtonListener(ActionListener al){
        cancelButton.addActionListener(al);
    }

    public void done() { hide(); dispose(); }

}
}