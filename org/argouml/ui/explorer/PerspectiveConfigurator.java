// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.ui.explorer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.ui.ArgoDialog;
import org.argouml.ui.DualListBox;
import org.argouml.ui.explorer.rules.PerspectiveRule;

/**
 * The "Configure Perspectives" dialog.<p>
 *
 * This class implements the following features:<p>
 * <ul>
 * <li>- saving perspectives to the user profile.
 * <li>- adding new perspectives.
 * <li>- deleting perspectives.
 * <li>- renaming perspectives.
 * <li>- duplicating existing perspectives.
 * <li>- reordering perspectives.
 * <li>- selecting any number and combination of rules for a perspective.
 * </ul><p>
 *
 * This dialog uses the dual list box for its selection mechanism
 *
 * @since 21 December 2003.
 * @author  alexb
 */
public class PerspectiveConfigurator extends ArgoDialog {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(PerspectiveConfigurator.class);

    /**
     * Insets in pixels.
     */
    private static final int INSET_PX = 3;

    ////////////////////////////////////////////////////////////////
    // instance variables

    private JPanel  configPanelNorth;
    private DualListBox  configPanelS;
    private JSplitPane splitPane;
    private JTextField renameTextField;
    private JButton newPerspectiveButton;
    private JButton removePerspectiveButton;
    private JButton duplicatePerspectiveButton;
    private JButton moveUpButton, moveDownButton;
    private JButton resetToDefaultButton;

    private JList   perspectiveList;

    private DefaultListModel perspectiveListModel;       // at the top
    
    private DefaultListModel perspectiveRulesLstModel;  // right bottom
    private DefaultListModel ruleLibraryLstModel;       // left bottom

    private JLabel persLabel;

    private ListDataListener listChangeListener = new ListChangeListener();
    
    /**
     * Creates a new instance of PerspectiveDesignerDialog.
     *
     * @param parent the parent frame
     */
    public PerspectiveConfigurator(Frame parent) {

        super(parent,
              Translator.localize("dialog.title.configure-perspectives"),
              ArgoDialog.OK_CANCEL_OPTION,
              true); // the dialog is modal

        configPanelNorth = new JPanel();
        configPanelS = new DualListBox();
        configPanelS.setSourceItemsTitle(Translator
                .localize("label.rules-library"));
        configPanelS.setDestinationItemsTitle(Translator
                .localize("label.selected-rules"));
        configPanelS.setShowItemCountEnabled(true);

        makeLists();
        makeButtons();
        makeLayout();
        makeListeners();

        loadPerspectives();
        loadLibrary();

        splitPane =
            new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    configPanelNorth, configPanelS);
        splitPane.setContinuousLayout(true);

        setContent(splitPane);
    }

    /**
     * Make the lists on the dialog box and fill them.
     */
    private void makeLists() {
        renameTextField = new JTextField();

        perspectiveListModel = new DefaultListModel();
        perspectiveList = new JList(perspectiveListModel);
        perspectiveRulesLstModel = configPanelS.getDestinationListModel();
        ruleLibraryLstModel = configPanelS.getSourceListModel();

        perspectiveList.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        perspectiveList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

    }

    /**
     * Make the buttons on the dialog box with localized strings and mnemonics.
     */
    private void makeButtons() {
        newPerspectiveButton = new JButton();
        nameButton(newPerspectiveButton, "button.new");
        newPerspectiveButton.setToolTipText(
                Translator.localize("button.new.tooltip"));

        removePerspectiveButton = new JButton();
        nameButton(removePerspectiveButton, "button.remove");
        removePerspectiveButton.setToolTipText(
                Translator.localize("button.remove.tooltip"));

        duplicatePerspectiveButton = new JButton();
        nameButton(duplicatePerspectiveButton, "button.duplicate");
        duplicatePerspectiveButton.setToolTipText(
                Translator.localize("button.duplicate.tooltip"));

        moveUpButton = new JButton();
        nameButton(moveUpButton, "button.move-up");
        moveUpButton.setToolTipText(
                Translator.localize("button.move-up.tooltip"));

        moveDownButton = new JButton();
        nameButton(moveDownButton, "button.move-down");
        moveDownButton.setToolTipText(
                Translator.localize("button.move-down.tooltip"));

        resetToDefaultButton = new JButton();
        nameButton(resetToDefaultButton, "button.restore-defaults");
        resetToDefaultButton.setToolTipText(
                Translator.localize("button.restore-defaults.tooltip"));

        //disable the buttons for now, since no selection has been made yet
        removePerspectiveButton.setEnabled(false);
        duplicatePerspectiveButton.setEnabled(false);
        moveUpButton.setEnabled(false);
        moveDownButton.setEnabled(false);
        renameTextField.setEnabled(false);
    }

    /**
     * Make the layout for the dialog box.
     */
    private void makeLayout() {
        GridBagLayout gb = new GridBagLayout();
        configPanelNorth.setLayout(gb);
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = 3;
        c.ipady = 3;

        persLabel = new JLabel(); // the text will be set later
        persLabel.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.weightx = 1.0;  c.weighty = 0.0;
        gb.setConstraints(persLabel, c);
        configPanelNorth.add(persLabel);

        JPanel persPanel = new JPanel(new BorderLayout());
        JScrollPane persScroll =
            new JScrollPane(perspectiveList,
                            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        persPanel.add(renameTextField, BorderLayout.NORTH);
        persPanel.add(persScroll, BorderLayout.CENTER);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 4;
        c.weightx = 1.0;  c.weighty = 1.0;
        gb.setConstraints(persPanel, c);
        configPanelNorth.add(persPanel);

        JPanel persButtons = new JPanel(new GridLayout(6, 1, 0, 5));
        persButtons.add(newPerspectiveButton);
        persButtons.add(removePerspectiveButton);
        persButtons.add(duplicatePerspectiveButton);
        persButtons.add(moveUpButton);
        persButtons.add(moveDownButton);
        persButtons.add(resetToDefaultButton);
        JPanel persButtonWrapper =
            new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        persButtonWrapper.add(persButtons);
        c.gridx = 4;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0.0;  c.weighty = 0.0;
        c.ipadx = 0;      c.ipady = 0;
        c.insets = new Insets(0, 5, 0, 0);
        gb.setConstraints(persButtonWrapper, c);
        configPanelNorth.add(persButtonWrapper);

    }

    /**
     * Add action listeners to the buttons and lists.
     */
    private void makeListeners() {
        renameTextField.addActionListener(new RenameListener());
        renameTextField.getDocument().addDocumentListener(
                new RenameDocumentListener());


        newPerspectiveButton.addActionListener(new NewPerspectiveListener());
        removePerspectiveButton.addActionListener(
                new RemovePerspectiveListener());
        duplicatePerspectiveButton.addActionListener(
                new DuplicatePerspectiveListener());
        moveUpButton.addActionListener(new MoveUpListener());
        moveDownButton.addActionListener(new MoveDownListener());

        resetToDefaultButton.addActionListener(new ResetListener());

        perspectiveList.addListSelectionListener(
                new PerspectiveListSelectionListener());

        getOkButton().addActionListener(new OkListener());
    }

    /**
     * Load all the existing rules from the perspective manager
     * for presentation. These will be presented as the library of rules
     * the user may pick from.
     */
    private void loadLibrary() {
        List rulesLib = new ArrayList();
        // get them
        rulesLib.addAll(PerspectiveManager.getInstance().getRules());
        // sort them
        Collections.sort(rulesLib, new Comparator() {
            public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
            }
        });

        // We don't need to listen to our own modifications
        configPanelS.getSourceListModel().removeListDataListener(
                listChangeListener);
        configPanelS.getDestinationListModel().removeListDataListener(
                listChangeListener);

        // remove the ones already selected (if a perspective is selected)
        ExplorerPerspective selPers =
            (ExplorerPerspective) perspectiveList.getSelectedValue();
        if (selPers != null) {
            Iterator it1 = selPers.getList().iterator();
            while (it1.hasNext()) {
                Object persRule = it1.next();
                Iterator it2 = rulesLib.iterator();
                while (it2.hasNext()) {
                    Object libRule = it2.next();
                    if (libRule.toString().equals(persRule.toString())) {
                        rulesLib.remove(libRule);
                        break;
                    }
                }
            }
        }
        // add them
        ruleLibraryLstModel.clear();
        for (int i = 0; i < rulesLib.size(); i++) {
            ruleLibraryLstModel.addElement(rulesLib.get(i));
        }
        configPanelS.getSourceListModel().addListDataListener(
                listChangeListener);
        configPanelS.getDestinationListModel().addListDataListener(
                listChangeListener);
    }

    /**
     * Load the perspectives from the perspective manager for presentation.
     */
    private void loadPerspectives() {
        List perspectives = new ArrayList();
        perspectives.addAll(PerspectiveManager.getInstance().getPerspectives());

        // must add an editable list of new ExplorerPerspective's
        // to the list model so that the orginal ones are not changed
        // in the case of a cancel action by the user.
        for (int i = 0; i < perspectives.size(); i++) {
            ExplorerPerspective perspective =
                (ExplorerPerspective) perspectives.get(i);
            Object[] ruleArray = perspective.getRulesArray();

            ExplorerPerspective editablePerspective =
                new ExplorerPerspective(perspective.toString());
            for (int r = 0; r < ruleArray.length; r++) {
                editablePerspective.addRule((PerspectiveRule) ruleArray[r]);
            }

            perspectiveListModel.addElement(editablePerspective);
        }

        updatePersLabel();
    }

    /**
     * Update the label above the list of perspectives with count.
     */
    private void updatePersLabel() {
        persLabel.setText(Translator.localize("label.perspectives")
                + " (" + perspectiveListModel.size() + ")");
    }


    /**
     * Handles pressing the OK button. <p>
     *
     * Updates the perspectives in the explorer,
     * saves the user perspectives and exits.
     */
    class OkListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            PerspectiveManager.getInstance().removeAllPerspectives();

            for (int i = 0; i < perspectiveListModel.getSize(); i++) {
                Object elem = perspectiveListModel.getElementAt(i);
                PerspectiveManager.getInstance().addPerspective(elem);
            }

            PerspectiveManager.getInstance().saveUserPerspectives();
        }
    }

    /**
     * Handles pressing the Reset-To-Default button. <p>
     *
     * Resets all prerspectives to the built-in defaults.
     */
    class ResetListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            Collection c =
                PerspectiveManager.getInstance().getDefaultPerspectives();
            if (c.size() > 0) {
                perspectiveListModel.removeAllElements();
                Iterator it = c.iterator();
                while (it.hasNext()) {
                    perspectiveListModel.addElement(it.next());
                }
                updatePersLabel();
            }
        }
    }

    /**
     * Handles pressing the "New" button.
     */
    class NewPerspectiveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object[] msgArgs = {
                new Integer((perspectiveList.getModel().getSize() + 1)),
            };
            ExplorerPerspective newPers =
                new ExplorerPerspective(Translator.messageFormat(
                    "dialog.perspective.explorer-perspective", msgArgs));
            perspectiveListModel.insertElementAt(newPers, 0);
            perspectiveList.setSelectedValue(newPers, true);
            perspectiveRulesLstModel.clear();
            updatePersLabel();
        }
    }

    /**
     * Handles pressing the "Remove" button.
     */
    class RemovePerspectiveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object sel = perspectiveList.getSelectedValue();
            if (perspectiveListModel.getSize() > 1) {
                perspectiveListModel.removeElement(sel);
            }
            perspectiveList.setSelectedIndex(0);
            if (perspectiveListModel.getSize() == 1) {
                removePerspectiveButton.setEnabled(false);
            }
            updatePersLabel();
        }
    }

    /**
     * Handles pressing the Duplicate button.
     */
    class DuplicatePerspectiveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object sel = perspectiveList.getSelectedValue();
            if (sel != null) {
                Object[] msgArgs = {sel.toString() };
                ExplorerPerspective newPers =
                    ((ExplorerPerspective) sel).makeNamedClone(Translator
                        .messageFormat("dialog.perspective.copy-of", msgArgs));
                perspectiveListModel.insertElementAt(newPers, 0);
                perspectiveList.setSelectedValue(newPers, true);
            }
            updatePersLabel();
        }
    }

    /**
     * Handles pressing the ">>" or "<<" buttons.
     */
    class ListChangeListener implements ListDataListener {
        
        public void contentsChanged(ListDataEvent event) {
            throw new RuntimeException("Got unexpected event " + event);
        }

        public void intervalAdded(ListDataEvent event) {
            // Add
            if (perspectiveRulesLstModel.equals(event.getSource())) {
                // We should only ever get one at a time, but just in case...
                for (int i = event.getIndex0(); i <= event.getIndex1(); i++) {
                    Object sel = perspectiveRulesLstModel.getElementAt(i);
                    try {
                        String ruleName = sel.getClass().getName();
                        PerspectiveRule newRule =
                            (PerspectiveRule) Class
                                .forName(ruleName).newInstance();
                        ((ExplorerPerspective) perspectiveList
                                .getSelectedValue()).addRule(newRule);
                    } catch (Exception e) {
                        LOG.error("problem adding rule");
                    }
                }
            } else if (ruleLibraryLstModel.equals(event.getSource())) {
                // Remove
                // We should only ever get one at a time, but just in case...
                for (int i = event.getIndex0(); i <= event.getIndex1(); i++) {
                    PerspectiveRule sel = (PerspectiveRule) ruleLibraryLstModel
                            .getElementAt(i);
                    Object selPers = perspectiveList.getSelectedValue();
                    ((ExplorerPerspective) selPers).removeRule(sel);
                }
            } else {
                LOG.error("Got unexpected event from source "
                        + event.getSource());
            }
            
        }

        public void intervalRemoved(ListDataEvent event) {
            // We ignore remove events because we don't get the elements removed
            // Instead we listen for adds on the companion list
        }
    }

    /**
     * Handles pressing the move up button.
     */
    class MoveUpListener implements ActionListener {
        /**
         * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
         */
        public void actionPerformed(ActionEvent e) {
            int sel = perspectiveList.getSelectedIndex();
            if (sel > 0) {
                Object selObj = perspectiveListModel.get(sel);
                Object prevObj = perspectiveListModel.get(sel - 1);
                perspectiveListModel.set(sel, prevObj);
                perspectiveListModel.set(sel - 1, selObj);
                perspectiveList.setSelectedIndex(sel - 1);
                perspectiveList.ensureIndexIsVisible(sel - 1);
            }
        }
    }

    /**
     * Handles pressing the move down button.
     */
    class MoveDownListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int sel = perspectiveList.getSelectedIndex();
            if (sel < (perspectiveListModel.getSize() - 1)) {
                Object selObj = perspectiveListModel.get(sel);
                Object nextObj = perspectiveListModel.get(sel + 1);
                perspectiveListModel.set(sel, nextObj);
                perspectiveListModel.set(sel + 1, selObj);
                perspectiveList.setSelectedIndex(sel + 1);
                perspectiveList.ensureIndexIsVisible(sel + 1);
            }
        }
    }

    /**
     * Handles confirming a changed text in the text-entry field
     * (e.g. pressing Enter) for the perspective name.
     */
    class RenameListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int sel = perspectiveList.getSelectedIndex();
            Object selPers = perspectiveList.getSelectedValue();
            String newName = renameTextField.getText();
            if (sel >= 0 && newName.length() > 0) {
                ((ExplorerPerspective) selPers).setName(newName);
                perspectiveListModel.set(sel, selPers);
                perspectiveList.requestFocusInWindow();
            }
        }
    }

    /**
     * Handles changes in the text in the text-entry field
     * for the perspective name.
     */
    class RenameDocumentListener implements DocumentListener {
        public void insertUpdate(DocumentEvent e) {
            update();
        }
        public void removeUpdate(DocumentEvent e) {
            update();
        }
        public void changedUpdate(DocumentEvent e) {
            update();
        }
        private void update() {
            int sel = perspectiveList.getSelectedIndex();
            Object selPers = perspectiveList.getSelectedValue();
            String newName = renameTextField.getText();
            if (sel >= 0 && newName.length() > 0) {
                ((ExplorerPerspective) selPers).setName(newName);
                perspectiveListModel.set(sel, selPers);
            }
        }

    }

    /**
     * Handles selection changes in the perspective list.
     */
    class PerspectiveListSelectionListener implements ListSelectionListener {
        /**
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }

            Object selPers = perspectiveList.getSelectedValue();
            loadLibrary();
            renameTextField.setEnabled(selPers != null);
            removePerspectiveButton.setEnabled(selPers != null);
            duplicatePerspectiveButton.setEnabled(selPers != null);
            moveUpButton.setEnabled(perspectiveList.getSelectedIndex() > 0);
            moveDownButton.setEnabled((selPers != null)
                    && (perspectiveList.getSelectedIndex()
                            < (perspectiveList.getModel().getSize() - 1)));

            if (selPers == null) {
                return;
            }
            renameTextField.setText(selPers.toString());

            ExplorerPerspective pers = (ExplorerPerspective) selPers;
            perspectiveRulesLstModel.clear();

            for (int i = 0; i < pers.getRulesArray().length; i++) {
                perspectiveRulesLstModel.addElement(pers.getRulesArray()[i]);
            }
        }
    }

}
