// $Id$
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

package org.argouml.uml.reveng;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.PluggableImport;
import org.argouml.cognitive.Designer;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.uml.UmlModelEventPump;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.NavigatorPane;
import org.argouml.ui.StatusBar;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramLayouter;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.util.logging.SimpleTimer;
import org.tigris.gef.base.Globals;

/**
 * This is the main class for all import classes.
 *
 * <p>It provides JPanels for tailoring the import run in the FileChooser.
 *
 * <p>The Import run is started by calling doFile(Project, File)
 *
 * <p>Supports recursive search in folder for all .java classes.
 *
 * <p>$Id$
 *
 * @author Andreas Rueckert <a_rueckert@gmx.net>
 */
public class Import {
    
    /** Imported directory */
    private String src_path;
    
    /** Create a interface to the current diagram */
    private DiagramInterface _diagram;
    
    /**
     * current language module
     */
    private PluggableImport module;
    
    /**
     * key = module name, value = PluggableImport instance
     */
    private Hashtable modules;
    
    private JComponent configPanel;
    
    private JCheckBox descend;
    
    /** The files that needs a second RE pass. */
    private Vector secondPassFiles;
    
    private JCheckBox create_diagrams;
    
    private JCheckBox minimise_figs;
    
    private JCheckBox layout_diagrams;
    
    private JDialog dialog;
    
    private ImportStatusScreen iss;
    
    // log4j logging
    private static Logger cat =
	Logger.getLogger(org.argouml.uml.reveng.Import.class);
    
    /**
     * Unnecessary attribute
     * @deprecated As of ArgoUml version 0.13.5, don't use this!
     */
    private ProjectBrowser pb = ProjectBrowser.getInstance();
    
    /**
     * Unnecessary attribute
     * @deprecated As of ArgoUml version 0.13.5, don't use this!
     */
    private Project p = ProjectManager.getManager().getCurrentProject();
    
    /**
     * Creates dialog window with chooser and configuration panel.
     */
    public Import() {
        modules = new Hashtable();
        ArrayList arraylist = Argo.getPlugins(PluggableImport.class);
        ListIterator iterator = arraylist.listIterator();
        while (iterator.hasNext()) {
            PluggableImport module = (PluggableImport) iterator.next();
            modules.put(module.getModuleName(), module);
        }
        if (modules.size() == 0)
            throw new RuntimeException("Internal error. No import modules defined");
	// "Java" is a default module
        module = (PluggableImport) modules.get("Java");
        if (module == null)
	    throw new RuntimeException("Internal error. Default import module not found");
        JComponent chooser = module.getChooser(this);
        dialog = new JDialog(pb, "Import sources");
        dialog.setModal(true);
        dialog.getParent().setEnabled(false);
        
        dialog.getContentPane().add(chooser, BorderLayout.WEST);
        dialog.getContentPane().add(getConfigPanel(this), BorderLayout.EAST);
        dialog.pack();
        int x = (pb.getSize().width - dialog.getSize().width) / 2;
        int y = (pb.getSize().height - dialog.getSize().height) / 2;
        dialog.setLocation(x > 0 ? x : 0, y > 0 ? y : 0);
        dialog.setVisible(true);
    }
    
    /**
     * Unnecessary method
     * @deprecated As of ArgoUml version 0.13.5, don't use this!
     */
    public Project getProject() {
        return p;
    }
    
    /**
     * Unnecessary method
     * @deprecated As of ArgoUml version 0.13.5, don't use this!
     */
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
    public JComponent getConfigPanel(final Import importInstance) {
        
        final JTabbedPane tab = new JTabbedPane();
        
        // build the configPanel:
        if (configPanel == null) {
            JPanel general = new JPanel();
            general.setLayout(new GridLayout(10, 1));
            
            general.add(new JLabel("Select language for import:"));
            
            Vector languages = new Vector();
            
            for (Enumeration keys = modules.keys(); keys.hasMoreElements();) {
                languages.add((String) keys.nextElement());
            }
            JComboBox selectedLanguage = new JComboBox(languages);
            selectedLanguage.addActionListener(new ActionListener() 
		{
		    public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox) e.getSource();
			String selected = (String) cb.getSelectedItem();
			module = (PluggableImport) modules.get(selected);
			dialog.getContentPane().remove(0);
			JComponent chooser =  module.getChooser(importInstance);
			if (chooser == null) chooser = new JPanel();
			dialog.getContentPane().add(chooser, 0);
			JComponent config = module.getConfigPanel();
			if (config == null) config = new JPanel();
			tab.remove(1);
			tab.add(config, selected, 1);
			tab.validate();
			dialog.validate();
		    }
		});
            general.add(selectedLanguage);
            
            descend = new JCheckBox("Descend directories recursively.");
            descend.setSelected(true);
            general.add(descend);
            
            
            create_diagrams =
		new JCheckBox("Create diagrams from imported code", true);
            general.add(create_diagrams);
            
            minimise_figs =
		new JCheckBox("Minimise Class icons in diagrams", true);
            general.add(minimise_figs);
            
            layout_diagrams =
		new JCheckBox("Perform Automatic Diagram Layout", true);
            general.add(layout_diagrams);
            
            // de-selects the fig minimising & layout
            // if we are not creating diagrams
            create_diagrams.addActionListener(new ActionListener()
		{
		    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
			if (!create_diagrams.isSelected()) {
			    minimise_figs.setSelected(false);
			    layout_diagrams.setSelected(false);
			}
		    } });
                
	    tab.add(general, "General");
	    tab.add(module.getConfigPanel(), module.getModuleName());
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
        UmlModelEventPump.getPump().stopPumpingEvents();
        iss = new ImportStatusScreen("Importing", "Splash");
        SwingUtilities.invokeLater(
				   new ImportRun(files, b,
						 layout_diagrams.isSelected()));
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
        if (create_diagrams != null)
            return create_diagrams.isSelected();
        else
            return true;
    }
    
    /**
     * Check, if "Discend directories recursively" is selected.
     * @return true, if "Discend directories recursively" is selected
     */
    public boolean isDiscendDirectoriesRecursively() {
        if (descend != null)
            return descend.isSelected();
        else
            return true;
    }
    
    /**
     * Check, if "Minimise Class icons in diagrams" is selected.
     * @return true, if "Minimise Class icons in diagrams" is selected
     */
    public boolean isMinimiseFigsChecked() {
        if (minimise_figs != null)
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
     * @return true, if any diagrams where modified and the project
     * should be saved before exit.
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
    
    /**
     * This class parses each file in turn and allows the GUI to refresh
     * itself by performing the run() once for each file.
     *
     * <p> this class also listens for a "Stop" message from the
     * ImportStatusScreen, in order to cancel long import runs.
     */
    class ImportRun implements Runnable {
        
        Vector _filesLeft;
        
        int _countFiles;
        
        int _countFilesThisPass;
        
        Vector _nextPassFiles;
        
        SimpleTimer _st;
        
        boolean cancelled;
        
        boolean criticThreadWasOn;
        
        boolean doLayout;
        
        public ImportRun(Vector f, boolean critic, boolean doLayout) {
            
            iss.addCancelButtonListener(new ActionListener() 
		{
		    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
			cancel();
		    }
		});
                
            _filesLeft = f;
            _countFiles = _filesLeft.size();
            _countFilesThisPass = _countFiles;
            _nextPassFiles = new Vector();
            _st = new SimpleTimer("ImportRun");
            _st.mark("start");
            cancelled = false;
            criticThreadWasOn = critic;
            this.doLayout = doLayout;
        }
        
        /**
         * called once for each file to be parsed.
         *
         * <p>to refresh the GUI it calls itself again using the
         * SwingUtilities.invokeLater(...)  method.
         */
        public void run() {
            
            if (_filesLeft.size() > 0) {
                
                Object curFile = _filesLeft.elementAt(0);
                _filesLeft.removeElementAt(0);
                
                try {
                    _st.mark(curFile.toString());
                    pb.showStatus("Importing " + curFile.toString());
                    parseFile(p, curFile); // Try to parse this file.
                    
                    int tot;
                    iss.setMaximum(tot = _countFiles
				   + (_diagram == null
				      ? 0
				      : _diagram.getModifiedDiagrams().size() / 10));
                    int act;
                    iss.setValue(act = _countFiles
                                 - _filesLeft.size()
                                 - _nextPassFiles.size());
                    pb.getStatusBar().showProgress(100 * act / tot);
                    
                    // flush model events after every 50 classes
                    // to avoid too many events in the event queue
                    // after a long r.e. run
                    if( act % 50 == 0){
                        UmlModelEventPump.getPump().flushModelEvents();
                        UmlModelEventPump.getPump().stopPumpingEvents();
                    }
                }
                catch (Exception e1) {
                    
                    _nextPassFiles.addElement(curFile);
                    
                    // RuntimeExceptions should be reported here!
                    if (e1 instanceof RuntimeException)
                        cat.error("program bug encountered in reverese engineering, the project file will be corrupted\n"
				  + e1);
                    else
                        cat.warn("exception encountered in reverese engineering, the project file will be corrupted\n"
				 + e1);
                    e1.printStackTrace();
                }
                
                if (!isCancelled()) {
                    SwingUtilities.invokeLater(this);
                    return;
		}
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
            if (_diagram != null && needsSave()) {
                p.setNeedsSave(true);
            }
            
            pb.showStatus("Import done");
            
            // Layout the modified diagrams.
            if (doLayout) {
                _st.mark("layout");
                if (_diagram != null) {
                    for (int i = 0;
			 i < _diagram.getModifiedDiagrams().size();
			 i++)
		    {
                        UMLDiagram diagram =
			    (UMLDiagram) _diagram.getModifiedDiagrams().elementAt(i);
                        ClassdiagramLayouter layouter =
			    module.getLayout(diagram);
                        layouter.layout();
                        
                        // Resize the diagram???
                        iss.setValue(_countFiles + (i + 1) / 10);
                    }
                }
            }
            
            iss.done();
            pb.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            
            // turn criticing on again
            if (criticThreadWasOn)  Designer.TheDesigner.setAutoCritique(true);
            
            UmlModelEventPump.getPump().startPumpingEvents();
            
            NavigatorPane.getInstance().forceUpdate();
            pb.setEnabled(true);
            
            Argo.log.info(_st);
            pb.getStatusBar().showProgress(0);
            
        }
        
        private void cancel() { cancelled = true; }
        
        private boolean isCancelled() { return cancelled; }
        
    }
    
    /**
     * The statusbar showing the progress of the Import run.
     */
    class ImportStatusBar extends StatusBar {
        
        public void setMaximum(int i) { _progress.setMaximum(i); }
        
        public void setValue(int i) { _progress.setValue(i); }
        
        public ImportStatusBar() {
            
            super();
            _progress.setPreferredSize(new Dimension(150, 30));
        }
    }
    
    /**
     * A window that shows the progress bar and a cancel button.
     */
    class ImportStatusScreen extends JDialog {
        
        protected ImportStatusBar _statusBar;
        
        private JButton cancelButton;
        
        private JLabel progressLabel;
        
        private int numberOfFiles;
        
        public ImportStatusScreen(String title, String iconName) {
            
            super();
            _statusBar = new ImportStatusBar();
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
            setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
			scrSize.height / 2 - contentPaneSize.height / 2);
            pack();
            this.setResizable(false);
        }
        
        public void setMaximum(int i) {
	    _statusBar.setMaximum(i); numberOfFiles = i;
	}
        
        public void setValue(int i) {
            
            _statusBar.setValue(i);
            progressLabel.setText("Parsing file " + i + " of " + numberOfFiles
				  + ".");
            repaint();
        }
        
        public void addCancelButtonListener(ActionListener al) {
            cancelButton.addActionListener(al);
        }
        
        public void done() { hide(); dispose(); }
    }
}
