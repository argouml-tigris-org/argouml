// $Id$
// Copyright (c) 1996-2008 The Regents of the University of California. All
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

package org.argouml.uml.reveng.ui;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.argouml.uml.reveng.SettingsTypes.PathListSelection;
import org.tigris.gef.base.Globals;

/**
 * Panel to collect a list of paths for an importer. <em>NOTE:</em> Although
 * this class is public it is <em>only</em> intended for use by the package
 * org.argouml.reveng.
 * <p>
 * This was originally included in Import.java and was called
 * ImportClasspathDialog.
 */
public class ImportClasspathDialog extends JPanel {

    private JList paths;

    private DefaultListModel pathsModel;

    private JButton addButton;

    private JButton removeButton;

    private JFileChooser chooser;
    
    private PathListSelection setting;


    /**
     * Construct a panel which provides controls for populating a list of 
     * paths.  This can be used for a Java classpath, C++ include path, etc.
     * 
     * @param pathListSetting the settings object for this pathlist
     */
    public ImportClasspathDialog(PathListSelection pathListSetting) {
        super();
        setting = pathListSetting;
        setToolTipText(setting.getDescription());
        
        setLayout(new BorderLayout(0, 0));

        JLabel label = new JLabel(setting.getLabel());
        add(label, BorderLayout.NORTH);

        pathsModel = new DefaultListModel();
        for (String path : setting.getDefaultPathList()) {
            pathsModel.addElement(path);
        }
        
        paths = new JList(pathsModel);
        paths.setVisibleRowCount(5);
        paths.setToolTipText(setting.getDescription());
        JScrollPane listScroller = new JScrollPane(paths);
        add(listScroller, BorderLayout.CENTER);

        // panel for controls
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(0, 2, 50, 0));
        
        addButton = new JButton(Translator.localize("button.add"));
        controlsPanel.add(addButton);
        addButton.addActionListener(new AddListener());
        
        removeButton = new JButton(Translator.localize("button.remove"));
        controlsPanel.add(removeButton);
        removeButton.addActionListener(new RemoveListener());
        
        // TODO: Add Up/Down buttons to control the ordering of items
        
        add(controlsPanel, BorderLayout.SOUTH);
    }


    private void updatePathList() {
        List<String> pathList = new ArrayList<String>();
        for (int i = 0; i < pathsModel.size(); i++) {
            String path = (String) pathsModel.getElementAt(i);
            pathList.add(path);
        }
        setting.setPathList(pathList);
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
            if (index < 0) {
                return;
            }
            pathsModel.remove(index);
            updatePathList();

            int size = pathsModel.getSize();

            if (size == 0) { //nothings left, disable firing.
                removeButton.setEnabled(false);

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

            if (chooser == null ) {
                chooser = new JFileChooser(Globals.getLastDirectory()); 
                if (chooser == null) {
                    chooser = new JFileChooser();
                }

                chooser.setFileSelectionMode(
                        JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setMultiSelectionEnabled(true);
                chooser.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e1) {
                        if (e1.getActionCommand().equals(
                                JFileChooser.APPROVE_SELECTION)) {
                            File[] files = chooser.getSelectedFiles();
                            for (File theFile : files) {
                                if (theFile != null) {
                                    pathsModel.addElement(theFile.toString());
                                }
                            }
                            updatePathList();
                        } else if (e1.getActionCommand().equals(
                                JFileChooser.CANCEL_SELECTION)) {
                            // Just quit
                        }

                    }
                });
            }

            chooser.showOpenDialog(new Frame());
        }
    }

}
