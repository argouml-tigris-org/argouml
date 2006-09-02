// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.PluggableImport;
import org.argouml.cognitive.Designer;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.swingext.JXButtonGroupPanel;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.explorer.ExplorerEventAdaptor;
import org.argouml.uml.diagram.static_structure.ClassDiagramGraphModel;
import org.argouml.uml.diagram.static_structure.layout.ClassdiagramLayouter;
import org.argouml.uml.diagram.ui.UMLDiagram;
import org.argouml.util.logging.SimpleTimer;
import org.tigris.gef.base.Globals;
import org.tigris.swidgets.GridLayout2;

/**
 * This is the main class for all import classes.<p>
 *
 * It provides JPanels for tailoring the import run in the FileChooser.<p>
 *
 * The Import run is started by calling doFile(Project, File)<p>
 *
 * Supports recursive search in folder for all .java classes.<p>
 *
 * There are now 3 levels of detail for import:<p>
 *
 * <ol>
 *   <li> 0 - classifiers only
 *   <li> 1 - classifiers plus feature specifications
 *   <li> 2 - full import, feature detail (ie. operations with methods)
 * </ol>
 *
 * TODO: Add registration methods for new languages.
 *
 * @author Andreas Rueckert a_rueckert@gmx.net
 */
public class Import {

    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(Import.class);

    /**
     * Imported directory.
     */
    private String srcPath;

    /**
     * Create a interface to the current diagram.
     */
    private DiagramInterface diagramInterface;

    /**
     * Current language module.
     */
    private PluggableImport module;

    /**
     * keys are module name, values are PluggableImport instance.
     */
    private Hashtable modules;

    private JComponent configPanel;

    private JCheckBox descend;

    private JCheckBox changedOnly;

    private JCheckBox createDiagrams;

    private JCheckBox minimiseFigs;

    private JCheckBox layoutDiagrams;

    // level 0 import detail
    private JRadioButton classOnly;

    // level 1 import detail
    private JRadioButton classAndFeatures;

    // level 2 import detail
    private JRadioButton fullImport;

    //import detail level var:
    private int importLevel;

    private JTextField inputSourceEncoding;

    private JDialog dialog;

    private ImportStatusScreen iss;

    private StringBuffer problems = new StringBuffer();

    private Hashtable attributes = new Hashtable();

    /**
     * Creates dialog window with chooser and configuration panel.
     */
    public Import() {
        modules = new Hashtable();
        List arraylist = Argo.getPlugins(PluggableImport.class);
        ListIterator iterator = arraylist.listIterator();
        while (iterator.hasNext()) {
            PluggableImport pIModule = (PluggableImport) iterator.next();
            modules.put(pIModule.getModuleName(), pIModule);
        }
        if (modules.size() == 0) {
            throw new RuntimeException("Internal error. "
                       + "No import modules defined");
        }
        // "Java" is a default module
        module = (PluggableImport) modules.get("Java");
        if (module == null) {
            throw new RuntimeException("Internal error. "
                       + "Default import module not found");
        }
        JComponent chooser = module.getChooser(this);
        dialog =
            new JDialog(ProjectBrowser.getInstance(),
                    Translator.localize("action.import-sources"), true);

        dialog.getContentPane().add(chooser, BorderLayout.CENTER);
        dialog.getContentPane().add(getConfigPanel(this), BorderLayout.EAST);
        dialog.pack();
        int x =
            (ProjectBrowser.getInstance().getSize().width
             - dialog.getSize().width)
            / 2;
        int y =
            (ProjectBrowser.getInstance().getSize().height
             - dialog.getSize().height)
            / 2;
        dialog.setLocation(x > 0 ? x : 0, y > 0 ? y : 0);
        dialog.setVisible(true);
    }

    /**
     * @param key the key of the attribute
     * @return the value of the attribute
     */
    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    /**
     * @param key the key of the attribute
     * @param value the value of the attribute
     */
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    /**
     * @return the text of this textfield
     */
    public String getInputSourceEncoding() {
        return inputSourceEncoding.getText();
    }

    /**
     * Close dialog window.
     *
     */
    public void disposeDialog() {
        Configuration.setString(Argo.KEY_INPUT_SOURCE_ENCODING,
            getInputSourceEncoding());
        dialog.setVisible(false);
        dialog.dispose();
    }

    /**
     * Get the panel that lets the user set reverse engineering
     * parameters.
     *
     * @param importInstance the instance of the import
     * @return the panel
     */
    public JComponent getConfigPanel(final Import importInstance) {

        final JTabbedPane tab = new JTabbedPane();

        // build the configPanel:
        if (configPanel == null) {
            JXButtonGroupPanel general = new JXButtonGroupPanel();
            general.setLayout(new GridLayout2(13, 1, 0, 0, GridLayout2.NONE));

            general.add(new JLabel(
                    Translator.localize("action.import-select-lang")));

            Vector languages = new Vector();

            for (Enumeration keys = modules.keys(); keys.hasMoreElements();) {
                languages.add(keys.nextElement());
            }
            JComboBox selectedLanguage = new JComboBox(languages);
            selectedLanguage.addActionListener(
                    new SelectedLanguageListener(importInstance, tab));
            general.add(selectedLanguage);

            descend =
                new JCheckBox(Translator.localize(
                        "action.import-option-descend-dir-recur"));
            descend.setSelected(true);
            general.add(descend);

            changedOnly =
                new JCheckBox(Translator.localize(
                        "action.import-option-changed_new"), true);
            general.add(changedOnly);

            createDiagrams =
                new JCheckBox(Translator.localize(
                        "action.import-option-create-diagram"), true);
            general.add(createDiagrams);

            minimiseFigs =
                new JCheckBox(Translator.localize(
                        "action.import-option-min-class-icon"), true);
            general.add(minimiseFigs);

            layoutDiagrams =
                new JCheckBox(Translator.localize(
                        "action.import-option-perform-auto-diagram-layout"),
                        true);
            general.add(layoutDiagrams);

            // de-selects the fig minimising & layout
            // if we are not creating diagrams
            createDiagrams.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    if (!createDiagrams.isSelected()) {
                        minimiseFigs.setSelected(false);
                        layoutDiagrams.setSelected(false);
                    }
                }
            });

            // select the level of import
            // 0 - classifiers only
            // 1 - classifiers plus feature specifications
            // 2 - full import, feature detail

            JLabel importDetailLabel =
                new JLabel(Translator.localize(
                        "action.import-level-of-import-detail"));

            classOnly =
                new JRadioButton(Translator.localize(
                        "action.import-option-classfiers"));

            classAndFeatures =
                new JRadioButton(Translator.localize(
                        "action.import-option-classifiers-plus-specs"));

            fullImport =
                new JRadioButton(Translator.localize(
                        "action.import-option-full-import"));
            fullImport.setSelected(true);

            general.add(importDetailLabel);
            general.add(classOnly);
            general.add(classAndFeatures);
            general.add(fullImport);

            general.add(new JLabel(
                    Translator.localize("action.import-file-encoding")));
            String enc =
                Configuration.getString(Argo.KEY_INPUT_SOURCE_ENCODING);
            if (enc == null || enc.trim().equals("")) {
                inputSourceEncoding =
                    new JTextField(System.getProperty("file.encoding"));
            } else {
                inputSourceEncoding = new JTextField(enc);
            }
            general.add(inputSourceEncoding);

            tab.add(general, Translator.localize("action.import-general"));
            tab.add(module.getConfigPanel(), module.getModuleName());
            configPanel = tab;
        }
        return configPanel;
    }

    private class SelectedLanguageListener implements ActionListener {

        /**
         * The current import.
         */
        private Import importInstance;

        /**
         * The pane.
         */
        private JTabbedPane tab;

        /**
         * The constructor.
         * @param i The current import.
         * @param t The pane.
         */
        SelectedLanguageListener(Import i, JTabbedPane t) {
            importInstance = i;
            tab = t;
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox) e.getSource();
            String selected = (String) cb.getSelectedItem();
            module = (PluggableImport) modules.get(selected);
            dialog.getContentPane().remove(0);
            JComponent chooser = module.getChooser(importInstance);
            if (chooser == null) {
                chooser = new JPanel();
            }
            dialog.getContentPane().add(chooser, 0);
            JComponent config = module.getConfigPanel();
            if (config == null) {
                config = new JPanel();
            }
            tab.remove(1);
            tab.add(config, selected, 1);
            tab.validate();
            dialog.validate();
        }
    }

    /**
     * This method is called by ActionImportFromSources to start the
     * import run.<p>
     *
     * The method that for all parsing actions. It calls the actual
     * parser methods depending on the type of the file.<p>
     */
    public void doFile() {

        // determine how many files to process
        Vector files = module.getList(this);

        if (changedOnly.isSelected()) {
            // filter out all unchanged files
            Object model =
                ProjectManager.getManager().getCurrentProject().getModel();
            for (int i = files.size() - 1; i >= 0; i--) {
                File f = (File) files.elementAt(i);
                String fn = f.getAbsolutePath();
                String lm = String.valueOf(f.lastModified());
                if (lm.equals(
                        Model.getFacade().getTaggedValueValue(model, fn))) {
                    files.remove(i);
                }
            }
        }

        if (!classOnly.isSelected()) {
            // 2 passes needed
            files.addAll(files); // for the second pass

            if (classAndFeatures.isSelected()) {
                importLevel = 1;
            } else {
                importLevel = 2;
            }
        } else {
            importLevel = 0;
        }

        // we always start with a level 0 import
        setAttribute("level", new Integer(0));

        diagramInterface = getCurrentDiagram();

        ProjectBrowser.getInstance().setCursor(
                Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        // now start importing (with an empty problem list)
        problems = new StringBuffer();
        iss = new ImportStatusScreen("Importing", "Splash");
        SwingUtilities.invokeLater(
                   new ImportRun(files, layoutDiagrams.isSelected()));
        iss.setVisible(true);
    }

    /**
     * Set path for processed directory.
     *
     * @param path the given path
     */
    public void setSrcPath(String path) {
        srcPath = path;
    }

    /**
     * @return path for processed directory.
     */
    public String getSrcPath() {
        return srcPath;
    }

    /**
     * Parse 1 Java file, using JavaImport.
     *
     * @param project the project
     * @param f The file to parse.
     * @exception Exception ??? TODO: Couldn't we throw a narrower one?
     */
    public void parseFile(Project project, Object f) throws Exception {

        // Is this file a Java source file?
        if (module.isParseable(f)) {
            ProjectBrowser.getInstance()
                .showStatus("Parsing " + f.toString() + "...");
            module.parseFile(project, f, diagramInterface, this);
            // set the lastModified value
            String fn = ((File) f).getAbsolutePath();
            String lm = String.valueOf(((File) f).lastModified());
            if (lm != null) {
                Model.getCoreHelper()
                    .setTaggedValue(project.getModel(), fn, lm);
            }
        }
    }

    /**
     * Check, if "Create diagrams from imported code" is selected.<p>
     *
     * @return true, if "Create diagrams from imported code" is selected
     */
    public boolean isCreateDiagramsChecked() {
        if (createDiagrams != null) {
            return createDiagrams.isSelected();
        }
        return true;
    }

    /**
     * Check, if "Discend directories recursively" is selected.<p>
     *
     * @return true, if "Discend directories recursively" is selected
     */
    public boolean isDiscendDirectoriesRecursively() {
        if (descend != null) {
            return descend.isSelected();
        }
        return true;
    }

    /**
     * Check, if "Minimise Class icons in diagrams" is selected.<p>
     *
     * @return true, if "Minimise Class icons in diagrams" is selected
     */
    public boolean isMinimiseFigsChecked() {
        if (minimiseFigs != null) {
            return minimiseFigs.isSelected();
        }
        return false;
    }

    /**
     * If we have modified any diagrams, the project was modified and
     * should be saved. I don't consider a import, that only modifies
     * the metamodel, at this point (Andreas Rueckert).
     * Calling Project.setNeedsSave(true) doesn't work here, because
     * Project.postLoad() is called after the import and it sets the
     * needsSave flag to false.<p>
     *
     * @return true, if any diagrams where modified and the project
     * should be saved before exit.
     */
    public boolean needsSave() {
        return (diagramInterface.getModifiedDiagrams().size() > 0);
    }

    /**
     * Set target diagram.<p>
     *
     * @return selected diagram, if it is class diagram,
     * else return null.
     */
    private DiagramInterface getCurrentDiagram() {
        DiagramInterface result = null;
        if (Globals.curEditor().getGraphModel()
                instanceof ClassDiagramGraphModel) {

            result =  new DiagramInterface(Globals.curEditor());

        }
        return result;
    }

    /**
     * This class parses each file in turn and allows the GUI to refresh
     * itself by performing the run() once for each file.<p>
     *
     * This class also listens for a "Stop" message from the
     * ImportStatusScreen, in order to cancel long import runs.<p>
     */
    class ImportRun implements Runnable {
        private Vector filesLeft;

        private int countFiles;

        private int countFilesThisPass;

        private Vector nextPassFiles;

        private SimpleTimer st;

        private boolean cancelled;

        private boolean criticThreadWasOn;

        private boolean doLayout;

        /**
         * The constructor.
         *
         * @param f the files left to parse/import
         * @param layout do a autolayout afterwards
         */
        public ImportRun(Vector f,  boolean layout) {

            iss.addCancelButtonListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    cancel();
                }
            });

            filesLeft = f;
            countFiles = filesLeft.size();
            countFilesThisPass = countFiles;
            nextPassFiles = new Vector();
            st = new SimpleTimer();
            st.mark("start");
            cancelled = false;
            criticThreadWasOn = Designer.theDesigner().getAutoCritique();
            if (criticThreadWasOn) {
                Designer.theDesigner().setAutoCritique(false);
            }
            this.doLayout = layout;
        }

        /**
         * Called once for each file to be parsed.<p>
         *
         * To refresh the GUI it calls itself again using the
         * {@link SwingUtilities#invokeLater(Runnable)} method.<p>
         */
        public void run() {
            // We shouldn't really turn off events, but as long
            // as we are, wrap in try block to make sure they get
            // turned back on.
            Model.getPump().stopPumpingEvents();
            try {
                if (filesLeft.size() > 0) {

                    // if there ae 2 passes:
                    if (importLevel > 0) {
                        if (filesLeft.size() <= countFiles / 2) {
                            if (importLevel == 1) {
                                setAttribute("level", new Integer(1));
                            } else {
                                setAttribute("level", new Integer(2));
                            }
                        }
                    }

                    Object curFile = filesLeft.elementAt(0);
                    filesLeft.removeElementAt(0);

                    try {
                        st.mark(curFile.toString());
                        ProjectBrowser.getInstance().showStatus(
                                "Importing " + curFile.toString());
                        parseFile(
                                ProjectManager.getManager()
                                    .getCurrentProject(),
                                curFile); // Try to parse this file.

                        int tot = countFiles;
                        if (diagramInterface != null) {
                            tot +=
                                diagramInterface.getModifiedDiagrams().size()
                                / 10;
                        }
                        iss.setMaximum(tot);
                        int act =
                            countFiles
                            - filesLeft.size()
                            - nextPassFiles.size();
                        iss.setValue(act);
                        ProjectBrowser.getInstance().getStatusBar()
                                .showProgress(100 * act / tot);
                    } catch (Exception e1) {

                        nextPassFiles.addElement(curFile);
                        StringBuffer sb = new StringBuffer(80);
                        // RuntimeExceptions should be reported here!
                        if (e1 instanceof RuntimeException) {
                            sb.append("Program bug encountered while parsing ");
                            sb.append(curFile.toString());
                            sb.append(", so some elements are not"
                                    + " created in the model\n");
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new java.io.PrintWriter(sw);
                            e1.printStackTrace(pw);
                            sb.append(sw.getBuffer());
                            LOG.error(sb.toString(), e1);
                        } else {
                            sb.append("Uncaught exception encountered"
                                    + " while parsing ");
                            sb.append(curFile.toString());
                            sb.append(", so some elements are not "
                                    + "created in the model\n");
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new java.io.PrintWriter(sw);
                            e1.printStackTrace(pw);
                            sb.append(sw.getBuffer());
                            LOG.warn(sb.toString(), e1);
                        }
                        problems.append(sb);
                    }

                    if (!isCancelled()) {
                        SwingUtilities.invokeLater(this);
                        return;
                    }
                }

                if (nextPassFiles.size() > 0
                        && nextPassFiles.size() < countFilesThisPass) {
                    filesLeft = nextPassFiles;
                    nextPassFiles = new Vector();
                    countFilesThisPass = filesLeft.size();
                    SwingUtilities.invokeLater(this);
                    return;
                }

                // Do post load processings.
                st.mark("postprocessings");

                ProjectBrowser.getInstance().showStatus("Import done");

                // Layout the modified diagrams.
                if (!isCancelled() && doLayout) {
                    st.mark("layout");
                    if (diagramInterface != null) {
                        for (int i = 0; i < diagramInterface
                                .getModifiedDiagrams().size(); i++) {
                            UMLDiagram diagram =
                                (UMLDiagram) diagramInterface
                                    .getModifiedDiagrams().elementAt(i);
                            ClassdiagramLayouter layouter =
                                module.getLayout(diagram);

                            layouter.layout();

                            // Resize the diagram???
                            iss.setValue(countFiles + (i + 1) / 10);
                        }
                    }
                }

                iss.done();
                ProjectBrowser.getInstance().setCursor(
                        Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

                // if errors occured, display the collected messages here
                if (problems != null && problems.length() > 0) {
                    ProblemsDialog pd = new ProblemsDialog();
                    pd.setVisible(true);
                }

                // turn critiquing on again
                if (criticThreadWasOn) {
                    Designer.theDesigner().setAutoCritique(true);
                }

                ExplorerEventAdaptor.getInstance().structureChanged();

                LOG.info(st);
                ProjectBrowser.getInstance().getStatusBar().showProgress(0);
            } finally {
                // Be sure event pump gets turned back on again.
                // Note: this doesn't deal with the critics
                Model.getPump().startPumpingEvents();
            }
        }

        private void cancel() { cancelled = true; }

        private boolean isCancelled() { return cancelled; }

    }

    /**
     * A window that shows the progress bar and a cancel button.
     * TODO: React on the close button as if the Cancel button was pressed.
     */
    class ImportStatusScreen extends JDialog {

        private JButton cancelButton;

        private JLabel progressLabel;

        private int numberOfFiles;

        private JProgressBar progress;

        /**
         * The constructor.
         *
         * @param title
         * @param iconName
         */
        public ImportStatusScreen(String title, String iconName) {

            super();
            if (title != null) {
                setTitle(title);
            }
            Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            getContentPane().setLayout(new BorderLayout(0, 0));

            // Parsing file x of z.
            JPanel topPanel = new JPanel();
            progressLabel = new JLabel();
            topPanel.add(progressLabel);
            getContentPane().add(topPanel, BorderLayout.NORTH);

            // progress bar
            progress = new JProgressBar();
            getContentPane().add(progress, BorderLayout.CENTER);

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
            this.setModal(true);        //MVW - Issue 2539.
        }

        public void setMaximum(int i) {
            progress.setMaximum(i);
            numberOfFiles = i;
        }

        public void setValue(int i) {

            progress.setValue(i);

            String pass = "1-st pass";
            // if there are 2 passes:
            if (importLevel > 0 && i >= numberOfFiles / 2) {
                pass = "2-nd pass";
            }

            int fileNumber =
                (i != 1 && numberOfFiles != 0)
                    ? ((i - 1) % (numberOfFiles / 2) + 1)
                        : 1;

            progressLabel.setText("Parsing file "
                  + fileNumber + " of " + numberOfFiles / 2
                  + ", " + pass + ". ");
            pack(); // MVW: Is this not time-consuming?
                    // Better make the window big enough at the start,
                    // and only refresh the label.
        }

        public void addCancelButtonListener(ActionListener al) {
            cancelButton.addActionListener(al);
        }

        public void done() {
            setVisible(false);
            dispose();
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -1336242911879462274L;
    }


    /**
     * A window that shows the problems occured during import.
     */
    class ProblemsDialog extends JDialog implements ActionListener {

        private JButton closeButton;
        private JLabel northLabel;

        /**
         * The constructor.
         */
        public ProblemsDialog() {
            super();
            setResizable(true);
            setModal(false);
            setTitle(Translator.localize("dialog.title.import-problems"));

            Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            getContentPane().setLayout(new BorderLayout(0, 0));

            // the introducing label
            northLabel =
                new JLabel(Translator.localize("label.import-problems"));
            getContentPane().add(northLabel, BorderLayout.NORTH);

            // the text box containing the problem messages
            JEditorPane textArea = new JEditorPane();
            textArea.setText(problems.toString());
            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(new JScrollPane(textArea));
            centerPanel.setPreferredSize(new Dimension(300, 200));
            getContentPane().add(centerPanel);

            // close button
            closeButton = new JButton(Translator.localize("button.close"));
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(closeButton);
            getContentPane().add(bottomPanel, BorderLayout.SOUTH);

            // listeners
            closeButton.addActionListener(this);
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    disposeDialog();
                }
            });

            Dimension contentPaneSize = getContentPane().getPreferredSize();
            setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
                    scrSize.height / 2 - contentPaneSize.height / 2);
            pack();
        }

        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            disposeDialog();
        }

        public void disposeDialog() {
            setVisible(false); 
            dispose();
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -9221358976863603143L;
    }
}

/**
 * dialog to setup the import classpath.
 */
class ImportClasspathDialog extends JDialog {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ImportClasspathDialog.class);

    private JList paths;
    private DefaultListModel pathsModel;

    private JButton addFile;

    private JButton removeFile;

//    private JButton save;

    private JButton ok;

    private Import importProcess;

    public ImportClasspathDialog(Import importProcess1) {

        super();
        setTitle("Set up the import classpath"); //MVW - Issue 2539.
        importProcess = importProcess1;

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        getContentPane().setLayout(new BorderLayout(0, 0));

        // paths list
        pathsModel = new DefaultListModel();
        paths = new JList(pathsModel);
        paths.setVisibleRowCount(5);
        JScrollPane listScroller = new JScrollPane(paths);
        listScroller.setPreferredSize(new Dimension(250, 80));
        getContentPane().add(listScroller, BorderLayout.CENTER);

        initList();

        // controls
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(0, 3));
        addFile = new JButton("Add");
        removeFile = new JButton("Remove");
//        save = new JButton("Save");
        ok = new JButton("Ok");
        controlsPanel.add(addFile);
        controlsPanel.add(removeFile);
//        controlsPanel.add(save);
        controlsPanel.add(ok);
        getContentPane().add(controlsPanel, BorderLayout.SOUTH);

        addFile.addActionListener(new AddListener());
        removeFile.addActionListener(new RemoveListener());
//        save.addActionListener(new SaveListener());
        ok.addActionListener(new OkListener());

        //Display the window.
        Dimension contentPaneSize = getContentPane().getPreferredSize();
        setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
            scrSize.height / 2 - contentPaneSize.height / 2);
        pack();
        setVisible(true);
        this.setModal(true);        //MVW   Issue 2539.
    }

    private void initList() {

        URL[] urls =
            ImportClassLoader.getURLs(Configuration.getString(
                Argo.KEY_USER_IMPORT_CLASSPATH, ""));

        for (int i = 0; i < urls.length; i++) {
            pathsModel.addElement(urls[i].getFile());
        }

        paths.setSelectedIndex(0);
    }


    private void doFiles() {
        importProcess.doFile();
    }

//    class SaveListener implements ActionListener {
//        public void actionPerformed(ActionEvent e) {
//
//            try{
//            ImportClassLoader.getInstance().setPath(pathsModel.toArray());
//            ImportClassLoader.getInstance().saveUserPath();
//        }catch(Exception e1){LOG.warn("could not do save "+e1);}
//        }
//    }

    class OkListener implements ActionListener {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {

            URL[] urls = new URL[pathsModel.size()];
            for (int i = 0; i < urls.length; i++) {
                try {
                    urls[i] = new File((String) pathsModel.get(i)).toURL();
                } catch (Exception e1) {
                    LOG.warn("could not do ok: could not make"
                            + "url " + pathsModel.get(i) + ", " + e1,
                            e1);
                }
            }

            try {
                ImportClassLoader.getInstance(urls);
                ImportClassLoader.getInstance().saveUserPath();
            } catch (Exception e1) {
                LOG.warn("could not do ok", e1);
            }
            doFiles();
            dispose();
        }
    }

    class RemoveListener implements ActionListener {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            //This method can be called only if
            //there's a valid selection
            //so go ahead and remove whatever's selected.
            int index = paths.getSelectedIndex();
            pathsModel.remove(index);

            int size = pathsModel.getSize();

            if (size == 0) { //nothings left, disable firing.
                removeFile.setEnabled(false);

            } else { //Select an index.
                if (index == pathsModel.getSize()) {
                    //removed item in last position
                    index--;
                }

                paths.setSelectedIndex(index);
                paths.ensureIndexIsVisible(index);
            }
        }
    }


    class AddListener implements ActionListener {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {

            String directory = Globals.getLastDirectory();
            JFileChooser ch = new JFileChooser(directory);
            if (ch == null) {
                ch = new JFileChooser();
            }

            final JFileChooser chooser = ch;

            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

            chooser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals(
                            JFileChooser.APPROVE_SELECTION)) {
                        File theFile = chooser.getSelectedFile();
                        if (theFile != null) {
                            pathsModel.addElement(theFile.toString());
                        }
                    } else if (e.getActionCommand().equals(
                            JFileChooser.CANCEL_SELECTION)) {
                        // TODO: What shall we do here?
                    }
                }
            });

            chooser.showOpenDialog(ProjectBrowser.getInstance());
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8684620532717336574L;
}

