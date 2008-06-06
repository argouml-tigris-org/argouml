package org.argouml.uml.reveng.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;
import org.argouml.application.api.Argo;
import org.argouml.configuration.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.uml.reveng.ImportClassLoader;
import org.argouml.uml.reveng.ImportCommandInterface;
import org.tigris.gef.base.Globals;

/**
 * dialog to setup the import classpath.
 */
public class ImportClasspathDialog extends JDialog {

    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(ImportClasspathDialog.class);

    private JDialog importClasspathDialog;
    private JList paths;
    private DefaultListModel pathsModel;

    private JButton addFile;

    private JButton removeFile;

    private JButton ok;

    private ImportCommandInterface importCmd;

    /**
     * Construct a dialog to allow the user to set up the classpath for the
     * import.<p>
     * @param impCmd 
     */
    public ImportClasspathDialog(ImportCommandInterface impCmd) {

        super();
        importClasspathDialog = this;
        setTitle(Translator.localize("dialog.import.classpath.title"));
        importCmd = impCmd;

        Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();
        getContentPane().setLayout(new BorderLayout(0, 0));

        // Explanatory text
        JTextArea ta =
                new JTextArea(Translator
                        .localize("dialog.import.classpath.text"));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setFocusable(false);
        getContentPane().add(ta, BorderLayout.NORTH);

        // paths list
        pathsModel = new DefaultListModel();
        paths = new JList(pathsModel);
        paths.setVisibleRowCount(5);
        JScrollPane listScroller = new JScrollPane(paths);
        listScroller.setPreferredSize(new Dimension(300, 100));
        getContentPane().add(listScroller, BorderLayout.CENTER);

        initList();

        // controls
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(0, 3));
        addFile = new JButton(Translator.localize("button.add"));
        removeFile = new JButton(Translator.localize("button.remove"));
        ok = new JButton(Translator.localize("button.ok"));
        controlsPanel.add(addFile);
        controlsPanel.add(removeFile);
        controlsPanel.add(ok);
        getContentPane().add(controlsPanel, BorderLayout.SOUTH);

        addFile.addActionListener(new AddListener());
        removeFile.addActionListener(new RemoveListener());
        ok.addActionListener(new OkListener());

        //Display the window.
        Dimension contentPaneSize = getContentPane().getPreferredSize();
        setLocation(scrSize.width / 2 - contentPaneSize.width / 2,
            scrSize.height / 2 - contentPaneSize.height / 2);
        pack();
        ok.requestFocusInWindow();
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


    class OkListener implements ActionListener {
        /*
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            try {
                URL[] urls = new URL[pathsModel.size()];
                for (int i = 0; i < urls.length; i++) {
                    try {
                        urls[i] = new File((String) pathsModel.get(i)).toURI()
                                .toURL();
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
                setVisible(false);
                setModal(false);
                dispose();
                importCmd.execute();
            } finally {
                setVisible(false);
                setModal(false);
                dispose();
            }
        }
    }

    class RemoveListener implements ActionListener {
        /*
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
        /*
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
                public void actionPerformed(ActionEvent e1) {
                    if (e1.getActionCommand().equals(
                            JFileChooser.APPROVE_SELECTION)) {
                        File theFile = chooser.getSelectedFile();
                        if (theFile != null) {
                            pathsModel.addElement(theFile.toString());
                        }
                    } else if (e1.getActionCommand().equals(
                            JFileChooser.CANCEL_SELECTION)) {
                        // TODO: What shall we do here?
                    }
                    // bring the import classpath dialog to the front
                    importClasspathDialog.setVisible(true);
                }
            });

            chooser.showOpenDialog(new Frame());
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -8684620532717336574L;
}
