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

package org.argouml.ui.explorer;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.ui.ArgoDialog;
import org.argouml.ui.SpacerPanel;
import org.argouml.ui.explorer.rules.PerspectiveRule;

/**
 *
 * The "Configure Perspectives" dialog.
 *
 * This class replaces the old Perspective configurator and improves it by:
 * - saving perspectives to the user profile.
 *
 * Created on 21 December 2003, 21:47
 * @author  alexb
 */
public class PerspectiveConfigurator extends ArgoDialog {
    
    private static final Logger LOG =
	Logger.getLogger(PerspectiveConfigurator.class);
    
    private static int numNavConfig = 0;

    /** Insets in pixels  */
    private static final int INSET_PX = 3;
    
    ////////////////////////////////////////////////////////////////
    // instance variables
    
    private JPanel  configPanel;
    private JList   perspectiveList;
    private JList   perspectiveRulesList;
    private JList   ruleLibraryList;
    private JButton newPerspectiveButton;
    private JButton removePerspectiveButton;
    private JButton dupPersButton;
    private JButton addRuleButton;
    private JButton removeRuleButton;
    
    private DefaultListModel perspectiveListModel;
    private DefaultListModel perspectiveRulesListModel;
    private DefaultListModel ruleLibraryListModel;
    
    /** 
     * Creates a new instance of PerspectiveDesignerDialog.
     * 
     * @param parent the parent frame
     */
    public PerspectiveConfigurator(Frame parent) {
        
        super(parent,
	      Translator.localize("dialog.title.configure-perspectives"),
	      ArgoDialog.OK_CANCEL_OPTION,
	      true);
        
        initPersPanel();
        loadData();
        
        setContent(configPanel);
        
        getOkButton().addActionListener(new OkListener());

        numNavConfig++;
    }
    
    /**
     * load the perspectives from the perspective manager for presentation.
     */
    private void loadData() {
        
        Vector perspectives = new Vector();
        Vector perspectivesBackup = new Vector();
        Vector rulesLib = new Vector();
        
        perspectives.addAll(PerspectiveManager.getInstance().getPerspectives());
        rulesLib.addAll(PerspectiveManager.getInstance().getRules());
        
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
        for (int i = 0; i < rulesLib.size(); i++) {
            ruleLibraryListModel.addElement(rulesLib.get(i));
        }
    }
    
    /**
     * Initialize the Perspectives tab panel.
     */
    public void initPersPanel() {
        configPanel = new JPanel();
                
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
        
        newPerspectiveButton = new JButton();
        nameButton(newPerspectiveButton, "button.new");
        removePerspectiveButton = new JButton();
        nameButton(removePerspectiveButton, "button.remove");
        dupPersButton = new JButton();
        nameButton(dupPersButton, "button.duplicate");
        addRuleButton = new JButton(">>");
        addRuleButton.setToolTipText(Translator.localize("button.add-rule"));
        removeRuleButton = new JButton("<<");
        removeRuleButton.setToolTipText(Translator.localize(
                "button.remove-rule"));
        
        perspectiveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        perspectiveRulesList
            .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ruleLibraryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        GridBagLayout gb = new GridBagLayout();
        configPanel.setLayout(gb);
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = 3;      c.ipady = 3;
        
        JLabel persLabel = new JLabel(
            Translator.localize("label.perspectives"));
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;      c.gridy = 0;
        c.gridwidth = 3;
        c.weightx = 1.0;  c.weighty = 0.0;
        gb.setConstraints(persLabel, c);
        configPanel.add(persLabel);
        
        JScrollPane persScroll = new JScrollPane(perspectiveList,
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        c.gridx = 0;	  c.gridy = 1;
        c.gridwidth = 4;
        c.weightx = 1.0;  c.weighty = 1.0;
        gb.setConstraints(persScroll, c);
        configPanel.add(persScroll);
        
        JPanel persButtons = new JPanel(new GridLayout(3, 1, 0, 5));
        persButtons.add(newPerspectiveButton);
        persButtons.add(removePerspectiveButton);
        persButtons.add(dupPersButton);
        JPanel persButtonWrapper =
	    new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        persButtonWrapper.add(persButtons);
        c.gridx = 4;      c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0.0;  c.weighty = 0.0;
        c.ipadx = 0;      c.ipady = 0;
        c.insets = new Insets(0, 5, 0, 0);
        gb.setConstraints(persButtonWrapper, c);
        configPanel.add(persButtonWrapper);
        
        JLabel ruleLibLabel = new JLabel(
            Translator.localize("label.rules-library"));
        c.gridx = 0; c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1.0;  c.weighty = 0.0;
        c.ipadx = 3;      c.ipady = 3;
        c.insets = new Insets(10, 0, 0, 0);
        gb.setConstraints(ruleLibLabel, c);
        configPanel.add(ruleLibLabel);
        
        addRuleButton.setMargin(new Insets(2, 15, 2, 15));
        removeRuleButton.setMargin(new Insets(2, 15, 2, 15));
        JPanel xferButtons = new JPanel();
        xferButtons.setLayout(new BoxLayout(xferButtons, BoxLayout.Y_AXIS));
        xferButtons.add(addRuleButton);
        xferButtons.add(new SpacerPanel());
        xferButtons.add(removeRuleButton);
        c.gridx = 2;      c.gridy = 4;
        c.weightx = 0.0;  c.weighty = 0.0;
        c.insets = new Insets(0, 3, 0, 5);
        gb.setConstraints(xferButtons, c);
        configPanel.add(xferButtons);
        
        JLabel rulesLabel = new JLabel(
            Translator.localize("label.selected-rules"));
        c.gridx = 3;      c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.insets = new Insets(10, 0, 0, 0);
        gb.setConstraints(rulesLabel, c);
        configPanel.add(rulesLabel);
        
        c.gridx = 0;      c.gridy = 4;
        c.weighty = 1.0;
        c.gridwidth = 2;  c.gridheight = 2;
        c.insets = new Insets(0, 0, 0, 0);
        JScrollPane ruleLibScroll =
	    new JScrollPane(ruleLibraryList,
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gb.setConstraints(ruleLibScroll, c);
        configPanel.add(ruleLibScroll);
        
        c.gridx = 3;	  c.gridy = 4;
        c.gridwidth = 2;  c.gridheight = 2;
        JScrollPane rulesScroll =
	    new JScrollPane(perspectiveRulesList,
			    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        gb.setConstraints(rulesScroll, c);
        configPanel.add(rulesScroll);
        
        // add action listeners:
        newPerspectiveButton.addActionListener(new NewPerspectiveListener());
        removePerspectiveButton.addActionListener(
                new RemovePerspectiveListener());
        dupPersButton.addActionListener(new DuplicatePerspectiveListener());
        addRuleButton.addActionListener(new RuleListener());
        removeRuleButton.addActionListener(new RuleListener());
        perspectiveList.addListSelectionListener(
                new PerspectiveListSelectionListener());
        perspectiveRulesList.addListSelectionListener(
                new RulesListSelectionListener());
        perspectiveRulesList.addMouseListener(new RuleListMouseListener());
        ruleLibraryList.addListSelectionListener(
                new LibraryListSelectionListener());
        ruleLibraryList.addMouseListener(new RuleListMouseListener());
        
        removePerspectiveButton.setEnabled(false);
        dupPersButton.setEnabled(false);
        addRuleButton.setEnabled(false);
        removeRuleButton.setEnabled(false);
    }
    
    /**
     *updates the perspectives in the explorer,
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
    
    class NewPerspectiveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
	    ExplorerPerspective newPers =
		new ExplorerPerspective("Explorer Perspective "
					+ (perspectiveList.getModel().getSize()
					   + 1));
	    perspectiveListModel.insertElementAt(newPers, 0);
	    perspectiveList.setSelectedValue(newPers, true);
	    perspectiveRulesListModel.clear();
        }
    }
    
    class RemovePerspectiveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            Object sel = perspectiveList.getSelectedValue();
            perspectiveListModel.removeElement(sel);
            perspectiveList.setSelectedIndex(0);
            if (perspectiveListModel.getSize() == 1) {
                removeRuleButton.setEnabled(false);
	    }
        }
    }
    
    class DuplicatePerspectiveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
        }
    }
    
    class RuleListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            
            Object src = e.getSource();
            if (perspectiveList.getSelectedValue() == null) return;
            
            if (src == addRuleButton) doAddRule();
            else if (src == removeRuleButton) doRemoveRule();
            //            else if (src == _ruleLibList) doAddRule();
            //            else if (src == _rulesList) doRemoveRule();
        }
    }
    
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
    
    private void doAddRule() {
        Object sel = ruleLibraryList.getSelectedValue();
        try {
            String ruleName = sel.getClass().getName();
            PerspectiveRule newRule =  
                (PerspectiveRule) Class.forName(ruleName).newInstance();
            
            perspectiveRulesListModel.insertElementAt(newRule, 0);
            ((ExplorerPerspective) perspectiveList
                .getSelectedValue()).addRule(newRule);
        } catch (Exception e) {
            LOG.error("problem adding rule");
        }
    }
    
    private void doRemoveRule() {
        PerspectiveRule sel =
	    (PerspectiveRule) perspectiveRulesList.getSelectedValue();
	Object selPers = perspectiveList.getSelectedValue();
        
        perspectiveRulesListModel.removeElement(sel);
        ((ExplorerPerspective) selPers).removeRule(sel);
        
        perspectiveRulesList.setSelectedIndex(0);
    }
    
    class PerspectiveListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) return;
            
            Object selPers = perspectiveList.getSelectedValue();
            Object selRule = ruleLibraryList.getSelectedValue();
            removePerspectiveButton.setEnabled(selPers != null);
            
            if (selPers == null) return;
            
            ExplorerPerspective pers = (ExplorerPerspective) selPers;
            perspectiveRulesListModel.clear();
            
            for (int i = 0; i < pers.getRulesArray().length; i++) {
                perspectiveRulesListModel.insertElementAt(
                                pers.getRulesArray()[i], 0);
            }
            addRuleButton.setEnabled(selPers != null && selRule != null);
        }
    }
    
    class RulesListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) return;
            
            Object selPers = perspectiveList.getSelectedValue();
            Object selRule = perspectiveRulesList.getSelectedValue();
            removeRuleButton.setEnabled(selPers != null && selRule != null);
        }
    }
    
    class LibraryListSelectionListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent lse) {
            if (lse.getValueIsAdjusting()) return;
            
            Object selPers = perspectiveList.getSelectedValue();
            Object selRule = ruleLibraryList.getSelectedValue();
            addRuleButton.setEnabled(selPers != null && selRule != null);
        }
    }
}


// -------------------

//    ////////////////////////////////////////////////////////////////
//    // actions
//
//    /**
//     * Create a new perspective, add to the list.
//     *
//     * TODO: Not a robust naming scheme since duplicates
//     * are still possible; initPersPanel() mentions the need to allow editing.
//     */
//    public void doNewPers() {
//	NavPerspective newPers =
//	    new NavPerspective("New Perspective " +
//			       (_navPane.buildPerspectives().size() + 1));
//	_navPane.addPerspective(newPers);
//	_persList.setListData(Converter.convert(_navPane.buildPerspectives()));
//	_persList.setSelectedValue(newPers, true);
//    }
//
//    /**
//     * Remove selected perspective from the list.
//     */
//    public void doRemovePers() {
//	Object sel = _persList.getSelectedValue();
//	if (!(sel instanceof NavPerspective)) {
//	    cat.warn("doRemovePers: unexepected non-NavPerspective");
//	    return;
//	}
//	NavPerspective np = (NavPerspective) sel;
//
//	// are you sure?
//	int response =
//	    JOptionPane.showConfirmDialog(this,
//					  "Remove Perspective, \""
//					  + np.getName() + "\"?",
//					  "Are you sure?",
//					  JOptionPane.YES_NO_OPTION);
//	if (response == JOptionPane.YES_OPTION) {
////	    _navPane.removePerspective(np);
////
////	    // Remove it from the UI list
////	    _persList
////		.setListData(Converter.convert(_navPane.buildPerspectives()));
//	}
//    }
//
//    /**
//     * not currently supported.
//     */
//    public void doDupPers() {
//	Object sel = _persList.getSelectedValue();
//	if (!(sel instanceof NavPerspective)) {
//	    cat.warn("doDupPers: unexepected non-NavPerspective");
//	    return;
//	}
//	//NavPerspective np = (NavPerspective) sel;
//	//try {
//	//  NavPerspective newNP = (NavPerspective) np.clone();
//	//  NavPerspective.unregisterPerspective(newNP);
//	//}
//	//catch (CloneNotSupportedException cnse) {
//	//    cat.error("exception while cloning NavPerspective", cnse);
//	//}
//    }
//
//    public void doAddRule() {
//	Object sel = _persList.getSelectedValue();
//	if (!(sel instanceof NavPerspective)) {
//	    cat.warn("doAddRule: unexepected non-NavPerspective");
//	    return;
//	}
//	NavPerspective np = (NavPerspective) sel;
//	Object selRule = _ruleLibList.getSelectedValue();
//	if (!(selRule instanceof TreeModel)) {
//	    cat.warn("doAddRule: unexepected non-TreeModel");
//	    return;
//	}
//	TreeModel tm = (TreeModel) selRule;
//	np.addSubTreeModel(tm);
//	_rulesList.clearSelection();
//	_rulesList.setListData(Converter.convert(np.getSubTreeModels()));
//    }
//
//    public void doRemoveRule() {
//	Object sel = _persList.getSelectedValue();
//	if (!(sel instanceof NavPerspective)) {
//	    cat.warn("doRemoveRule: unexepected non-NavPerspective");
//	    return;
//	}
//	NavPerspective np = (NavPerspective) sel;
//	Object selRule = _rulesList.getSelectedValue();
//	if (!(selRule instanceof TreeModel)) {
//	    cat.warn("doRemoveRule: unexepected non-TreeModel");
//	    return;
//	}
//	TreeModel tm = (TreeModel) selRule;
//	np.removeSubTreeModel(tm);
//	_rulesList.clearSelection();
//	_rulesList.setListData(Converter.convert(np.getSubTreeModels()));
//    }
//
//    public void doSelectPers() {
//	Object selPers = _persList.getSelectedValue();
//	Object selRule = _ruleLibList.getSelectedValue();
//	_removePersButton.setEnabled(selPers != null);
//	_dupPersButton.setEnabled(selPers != null);
//	if (selPers == null) return;
//	NavPerspective np = (NavPerspective) selPers;
//	_rulesList.setListData(Converter.convert(np.getSubTreeModels()));
//	_addRuleButton.setEnabled(selPers != null && selRule != null);
//    }
//
//    public void doSelectLibRule() {
//	Object selPers = _persList.getSelectedValue();
//	Object selRule = _ruleLibList.getSelectedValue();
//	_addRuleButton.setEnabled(selPers != null && selRule != null);
//    }
//
//    public void doSelectRule() {
//	Object selPers = _persList.getSelectedValue();
//	Object selRule = _rulesList.getSelectedValue();
//	_removeRuleButton.setEnabled(selPers != null && selRule != null);
//    }
//
//    public void doOk() {
//	NavigatorPane np = NavigatorPane.getInstance();
//	//np.setPerspectives(NavPerspective.getRegisteredPerspectives());
//	//np.updateTree();
//	setVisible(false);
//	dispose();
//    }
//
//    ////////////////////////////////////////////////////////////////
//    // event handlers
//
//    public void actionPerformed(ActionEvent e) {
//	Object src = e.getSource();
//	if (src == _okButton) doOk();
//	else if (src == _newPersButton) doNewPers();
//	else if (src == _removePersButton) doRemovePers();
//	else if (src == _dupPersButton) doDupPers();
//	else if (src == _addRuleButton) doAddRule();
//	else if (src == _removeRuleButton) doRemoveRule();
//	else if (src == _ruleLibList) doAddRule();
//	else if (src == _rulesList) doRemoveRule();
//    }
//
//
//    public void stateChanged(ChangeEvent ce) {
//	Object src = ce.getSource();
//	cat.debug("stateChanged " + src);
//    }
//
//
//    /** Called when the user changes selections in a list. */
//    public void valueChanged(ListSelectionEvent lse) {
//	if (lse.getValueIsAdjusting()) return;
//	Object src = lse.getSource();
//	if (src == _persList) doSelectPers();
//	else if (src == _ruleLibList) doSelectLibRule();
//	else if (src == _rulesList) doSelectRule();
//    }
//
//    public void mousePressed(MouseEvent me) { }
//    public void mouseReleased(MouseEvent me) { }
//    public void mouseEntered(MouseEvent me) { }
//    public void mouseExited(MouseEvent me) { }
//    public void mouseClicked(MouseEvent me) {
//	Object src = me.getSource();
//	if (me.getClickCount() != 2) return;
//	if (src == _ruleLibList && _addRuleButton.isEnabled()) doAddRule();
//	if (src == _rulesList && _removeRuleButton.isEnabled()) doRemoveRule();
//    }
//
//}

