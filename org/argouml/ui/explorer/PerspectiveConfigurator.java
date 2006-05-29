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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.swingext.SpacerPanel;
import org.argouml.ui.ArgoDialog;
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
 * This dialog behaves almost exactly as described in
 * http://java.sun.com/products/jlf/at/book/Idioms6.html#57371
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
    private JPanel  configPanelSouth;
    private JSplitPane splitPane;
    private JTextField renameTextField;
    private JButton newPerspectiveButton;
    private JButton removePerspectiveButton;
    private JButton duplicatePerspectiveButton;
    private JButton moveUpButton, moveDownButton;
    private JButton addRuleButton;
    private JButton removeRuleButton;
    private JButton resetToDefaultButton;

    private JList   perspectiveList;
    private JList   perspectiveRulesList;
    private JList   ruleLibraryList;
    private DefaultListModel perspectiveListModel;       // at the top
    private DefaultListModel perspectiveRulesListModel;  // right bottom
    private DefaultListModel ruleLibraryListModel;       // left bottom

    private JLabel persLabel;
    private JLabel ruleLibLabel;
    private JLabel rulesLabel;

    /**
     * Creates a new instance of PerspectiveDesignerDialog.
     */
    public PerspectiveConfigurator() {
        super(Translator.localize("dialog.title.configure-perspectives"),
	      ArgoDialog.OK_CANCEL_OPTION,
	      true); // the dialog is modal

        configPanelNorth = new JPanel();
        configPanelSouth = new JPanel();
        
        makeLists();
        
        makeButtons();
        
        makeLayout();
        updateRuleLabel();
        
        makeListeners();
        
        loadPerspectives();
        loadLibrary();
        //sortJListModel(ruleLibraryList);
        
        splitPane =
            new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                    configPanelNorth, configPanelSouth);
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
        perspectiveRulesListModel = new DefaultListModel();
        perspectiveRulesList = new JList(perspectiveRulesListModel);
        ruleLibraryListModel = new DefaultListModel();
        ruleLibraryList = new JList(ruleLibraryListModel);

        perspectiveList.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        perspectiveRulesList.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        ruleLibraryList.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));

        perspectiveList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        perspectiveRulesList.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);
        ruleLibraryList.setSelectionMode(
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

        addRuleButton = new JButton(">>");
        addRuleButton.setToolTipText(Translator.localize("button.add-rule"));
        removeRuleButton = new JButton("<<");
        removeRuleButton.setToolTipText(Translator.localize(
                "button.remove-rule"));

        resetToDefaultButton = new JButton();
        nameButton(resetToDefaultButton, "button.restore-defaults");
        resetToDefaultButton.setToolTipText(
                Translator.localize("button.restore-defaults.tooltip"));

        //disable the buttons for now, since no selection has been made yet
        removePerspectiveButton.setEnabled(false);
        duplicatePerspectiveButton.setEnabled(false);
        moveUpButton.setEnabled(false);
        moveDownButton.setEnabled(false);
        addRuleButton.setEnabled(false);
        removeRuleButton.setEnabled(false);
        renameTextField.setEnabled(false);
    }

    /**
     * Make the layout for the dialog box.
     */
    private void makeLayout() {
        GridBagLayout gb = new GridBagLayout();
        configPanelNorth.setLayout(gb);
        configPanelSouth.setLayout(gb);
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

        ruleLibLabel = new JLabel(); // the text will be set later
        ruleLibLabel.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.ipadx = 3;
        c.ipady = 3;
        c.insets = new Insets(10, 0, 0, 0);
        gb.setConstraints(ruleLibLabel, c);
        configPanelSouth.add(ruleLibLabel);

        addRuleButton.setMargin(new Insets(2, 15, 2, 15));
        removeRuleButton.setMargin(new Insets(2, 15, 2, 15));
        JPanel xferButtons = new JPanel();
        xferButtons.setLayout(new BoxLayout(xferButtons, BoxLayout.Y_AXIS));
        xferButtons.add(addRuleButton);
        xferButtons.add(new SpacerPanel());
        xferButtons.add(removeRuleButton);
        c.gridx = 2;
        c.gridy = 4;
        c.weightx = 0.0;
        c.weighty = 0.0;
        c.insets = new Insets(0, 3, 0, 5);
        gb.setConstraints(xferButtons, c);
        configPanelSouth.add(xferButtons);

        rulesLabel = new JLabel(); // the text will be set later
        rulesLabel.setBorder(BorderFactory.createEmptyBorder(
                INSET_PX, INSET_PX, INSET_PX, INSET_PX));
        c.gridx = 3;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.insets = new Insets(10, 0, 0, 0);
        gb.setConstraints(rulesLabel, c);
        configPanelSouth.add(rulesLabel);

        c.gridx = 0;
        c.gridy = 4;
        c.weighty = 1.0;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.insets = new Insets(0, 0, 0, 0);
        JScrollPane ruleLibScroll =
	    new JScrollPane(ruleLibraryList,
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gb.setConstraints(ruleLibScroll, c);
        configPanelSouth.add(ruleLibScroll);

        c.gridx = 3;
        c.gridy = 4;
        c.gridwidth = 2;
        c.gridheight = 2;
        JScrollPane rulesScroll =
            new JScrollPane(perspectiveRulesList,
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gb.setConstraints(rulesScroll, c);
        configPanelSouth.add(rulesScroll);
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
        addRuleButton.addActionListener(new RuleListener());
        removeRuleButton.addActionListener(new RuleListener());
        resetToDefaultButton.addActionListener(new ResetListener());

        perspectiveList.addListSelectionListener(
                new PerspectiveListSelectionListener());
        perspectiveRulesList.addListSelectionListener(
                new RulesListSelectionListener());
        perspectiveRulesList.addMouseListener(new RuleListMouseListener());
        ruleLibraryList.addListSelectionListener(
                new LibraryListSelectionListener());
        ruleLibraryList.addMouseListener(new RuleListMouseListener());

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
        ruleLibraryListModel.clear();
        for (int i = 0; i < rulesLib.size(); i++) {
            ruleLibraryListModel.addElement(rulesLib.get(i));
        }
        updateLibLabel();
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
     * Update the label above the library of rules list with count.
     */
    private void updateLibLabel() {
        // update the label (which shows the number of rules)
        ruleLibLabel.setText(Translator.localize("label.rules-library")
                + " (" + ruleLibraryListModel.size() + ")");
    }

    /**
     * Update the label above the library of rules list with count.
     */
    private void updateRuleLabel() {
        // update the label (which shows the number of rules)
        rulesLabel.setText(Translator.localize("label.selected-rules")
                + " (" + perspectiveRulesListModel.size() + ")");
    }

    /**
     * @param list the JList to be sorted
     */
    private void sortJListModel(JList list) {
        DefaultListModel model = (DefaultListModel) list.getModel();
        List all = new ArrayList();
        for (int i = 0; i < model.getSize(); i++) {
            all.add(model.getElementAt(i));
        }
        model.clear();
        Collections.sort(all, new Comparator() {
            public int compare(Object o1, Object o2) {
            return o1.toString().compareTo(o2.toString());
            }
        });
        Iterator it = all.iterator();
        while (it.hasNext()) {
            model.addElement(it.next());
        }
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
     * Resets all prerspectives to the build-in defaults.
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
	    perspectiveRulesListModel.clear();
	    updatePersLabel();
	    updateRuleLabel();
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
    class RuleListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {

            Object src = e.getSource();
            if (perspectiveList.getSelectedValue() == null) {
                return;
            }
            if (src == addRuleButton) {
                doAddRule();
            } else if (src == removeRuleButton) {
                doRemoveRule();
            }
        }
    }

    /**
     * Handles double-clicking on the library list or on the ruleslist.
     * This triggers the same functions as ">>" or "<<".
     */
    class RuleListMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent me) {
            Object src = me.getSource();
            if (me.getClickCount() != 2
		|| perspectiveList.getSelectedValue() == null) {
		return;
	    }

            if (src == ruleLibraryList && addRuleButton.isEnabled()) {
                doAddRule();
	    }
            if (src == perspectiveRulesList && removeRuleButton.isEnabled()) {
                doRemoveRule();
	    }
        }
    }

    /**
     * Add the currently selected rule from the library to the rules list
     * for the current perspective.
     */
    private void doAddRule() {
        Object sel = ruleLibraryList.getSelectedValue();
        int selLibNr = ruleLibraryList.getSelectedIndex();
        try {
            String ruleName = sel.getClass().getName();
            PerspectiveRule newRule =
                (PerspectiveRule) Class.forName(ruleName).newInstance();

            perspectiveRulesListModel.insertElementAt(newRule, 0);
            ((ExplorerPerspective) perspectiveList.getSelectedValue())
                .addRule(newRule);
            sortJListModel(perspectiveRulesList);
            perspectiveRulesList.setSelectedValue(newRule, true);
            // remove the rule from the library list
            loadLibrary();
            // set the newly selected item in the library list
            if (!(ruleLibraryListModel.size() > selLibNr)) {
                selLibNr = ruleLibraryListModel.size() - 1;
            }
            ruleLibraryList.setSelectedIndex(selLibNr);
            updateRuleLabel();
        } catch (Exception e) {
            LOG.error("problem adding rule");
        }
    }

    /**
     * Remove the currently selected rule from the rules list
     * for the current perspective.
     */
    private void doRemoveRule() {
        int selLibNr = ruleLibraryList.getSelectedIndex();
        PerspectiveRule sel =
	    (PerspectiveRule) perspectiveRulesList.getSelectedValue();
        int selectedItem = perspectiveRulesList.getSelectedIndex();
	Object selPers = perspectiveList.getSelectedValue();

        perspectiveRulesListModel.removeElement(sel);
        ((ExplorerPerspective) selPers).removeRule(sel);

        if (perspectiveRulesListModel.getSize() > selectedItem) {
            perspectiveRulesList.setSelectedIndex(selectedItem);
        } else if (perspectiveRulesListModel.getSize() > 0) {
            perspectiveRulesList.setSelectedIndex(
                    perspectiveRulesListModel.getSize() - 1);
        }
        loadLibrary();
        // set the newly selected item in the library list
        ruleLibraryList.setSelectedIndex(selLibNr);
        updateRuleLabel();
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
                /* TODO: Replace the functioncall in the next line
                 * by .requestFocusInWindow() once
                 * we do not support Java 1.3 any more.
                 */
                perspectiveList.requestFocus();
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
            Object selRule = ruleLibraryList.getSelectedValue();
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
            perspectiveRulesListModel.clear();

            for (int i = 0; i < pers.getRulesArray().length; i++) {
                perspectiveRulesListModel.insertElementAt(
                                pers.getRulesArray()[i], 0);
            }
            sortJListModel(perspectiveRulesList);
            addRuleButton.setEnabled(selPers != null && selRule != null);
            updateRuleLabel();
        }
    }

    /**
     * Handles selection changes in the rules list.
     */
    class RulesListSelectionListener implements ListSelectionListener {
        /**
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }

            Object selPers = null;
            if (perspectiveListModel.size() > 0) {
                selPers = perspectiveList.getSelectedValue();
            }

            Object selRule = null;
            if (perspectiveRulesListModel.size() > 0) {
                selRule = perspectiveRulesList.getSelectedValue();
            }

            removeRuleButton.setEnabled(selPers != null && selRule != null);
        }
    }

    /**
     * Handles selection changes in the library list.
     *
     */
    class LibraryListSelectionListener implements ListSelectionListener {
        /**
         * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
         */
        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) {
                return;
            }

            Object selPers = perspectiveList.getSelectedValue();
            Object selRule = ruleLibraryList.getSelectedValue();
            addRuleButton.setEnabled(selPers != null && selRule != null);
        }
    }
}
