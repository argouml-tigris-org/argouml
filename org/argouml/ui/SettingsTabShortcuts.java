// $Id$
// Copyright (c) 2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.swingext.ShortcutField;
import org.argouml.ui.cmd.Action;
import org.argouml.ui.cmd.ShortcutChangedEvent;
import org.argouml.ui.cmd.ShortcutChangedListener;
import org.argouml.ui.cmd.ShortcutMgr;
import org.argouml.util.KeyEventUtils;

/**
 * Tab for the settings dialog that makes it possible to customize Actions'
 * shortcuts
 * 
 * @author andrea.nironi@gmail
 */
public class SettingsTabShortcuts extends JPanel implements
        GUISettingsTabInterface, ActionListener, ListSelectionListener,
        ShortcutChangedListener {

    /**
     * The UID.
     */
    private static final long serialVersionUID = -2033414439459450620L;

    private static final Logger LOG = Logger
            .getLogger(SettingsTabShortcuts.class);

    private static final String NONE_NAME = Translator
            .localize("label.shortcut-none");

    private static final String DEFAULT_NAME = Translator
            .localize("label.shortcut-default");

    private static final String CUSTOM_NAME = Translator
            .localize("label.shortcut-custom");

    /**
     * The table of modules.
     */
    private JTable table;

    private JPanel selectedContainer;

    // private JTextField actionField = new JTextField("", 12);

    private ShortcutField shortcutField = new ShortcutField("", 12);

    private Color shortcutFieldDefaultBg = null;

    private JRadioButton customButton = new JRadioButton(CUSTOM_NAME);

    private JRadioButton defaultButton = new JRadioButton(DEFAULT_NAME);

    private JRadioButton noneButton = new JRadioButton(NONE_NAME);

    private JLabel warningLabel = new JLabel(" ");

    private Action target;

    private Action[] actions = ShortcutMgr.getShortcuts();

    private int lastRowSelected = -1;

    /**
     * The names of the columns in the table.
     */
    private final String[] columnNames = {
            Translator.localize("misc.column-name.action"),
            Translator.localize("misc.column-name.shortcut"),
            Translator.localize("misc.column-name.default") };

    /**
     * The objects representing the available actions
     */
    private Object[][] elements;

    /**
     * @see GUISettingsTabInterface#getTabKey()
     */
    public String getTabKey() {
        return "tab.shortcuts";
    }

    /**
     * @see GUISettingsTabInterface#getTabPanel()
     */
    public JPanel getTabPanel() {
        if (table == null) {
            setLayout(new GridBagLayout());
            GridBagConstraints panelConstraints = new GridBagConstraints();
            panelConstraints.gridx = 0;
            panelConstraints.gridy = 0;
            panelConstraints.anchor = GridBagConstraints.NORTH;
            panelConstraints.fill = GridBagConstraints.BOTH;
            panelConstraints.weightx = 5;
            panelConstraints.weighty = 15;

            // let's add the table, inside a JScrollPane
            table = new JTable(new ShortcutTableModel());
            table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
            table.setShowVerticalLines(true);
            table.setDefaultRenderer(KeyStroke.class,
                    new KeyStrokeCellRenderer());
            table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table.getSelectionModel().addListSelectionListener(this);

            JPanel tableContainer = new JPanel(new BorderLayout());
            tableContainer.setBorder(
                    BorderFactory.createTitledBorder(
                            Translator.localize(
                                    "dialog.shortcut.titled-border.actions")));
            tableContainer.add(new JScrollPane(table));
            add(tableContainer, panelConstraints);

            // now, let's set up the "selected action" container
            customButton.addActionListener(this);
            defaultButton.addActionListener(this);
            noneButton.addActionListener(this);

            selectedContainer = new JPanel(new GridBagLayout());
            selectedContainer.setBorder(
                    BorderFactory.createTitledBorder(
                            Translator.localize(
                                    "dialog.shortcut.titled-border.selected")));
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.insets = new Insets(0, 5, 10, 0);

            noneButton.setActionCommand(NONE_NAME);
            defaultButton.setActionCommand(DEFAULT_NAME);
            customButton.setActionCommand(CUSTOM_NAME);
            noneButton.addActionListener(this);
            defaultButton.addActionListener(this);
            customButton.addActionListener(this);
            ButtonGroup radioButtonGroup = new ButtonGroup();
            radioButtonGroup.add(noneButton);
            radioButtonGroup.add(defaultButton);
            radioButtonGroup.add(customButton);
            selectedContainer.add(noneButton, constraints);
            constraints.gridx = 1;
            constraints.insets = new Insets(0, 5, 10, 0);
            selectedContainer.add(defaultButton, constraints);
            constraints.gridx = 2;
            constraints.insets = new Insets(0, 5, 10, 0);
            selectedContainer.add(customButton, constraints);

            constraints.gridx = 3;
            constraints.weightx = 10.0;
            constraints.insets = new Insets(0, 10, 10, 15);
            constraints.fill = GridBagConstraints.HORIZONTAL;
            shortcutField.addShortcutChangedListener(this);
            shortcutFieldDefaultBg = shortcutField.getBackground();
            selectedContainer.add(shortcutField, constraints);
            constraints.gridwidth = 4;
            constraints.gridy = 1;
            constraints.gridx = 0;
            constraints.anchor = GridBagConstraints.WEST;
            constraints.insets = new Insets(0, 10, 5, 10);
            warningLabel.setForeground(Color.RED);
            selectedContainer.add(warningLabel, constraints);

            panelConstraints.gridy = 1;
            panelConstraints.anchor = GridBagConstraints.CENTER;
            panelConstraints.fill = GridBagConstraints.BOTH;
            panelConstraints.weightx = 1;
            panelConstraints.weighty = 1;
            add(selectedContainer, panelConstraints);

            this.enableFields(false);
        }

        return this;
    }

    /**
     * Enable/disable the field of the lower panel
     * 
     * @param enable
     *            whether to enable the fields or not
     */
    private void enableFields(boolean enable) {
        shortcutField.setEditable(enable);
        customButton.setEnabled(enable);
        defaultButton.setEnabled(enable);
        noneButton.setEnabled(enable);
    }

    /**
     * Sets the current target
     * 
     * @param t
     *            the new target
     */
    private void setTarget(Object t) {
        target = (Action) t;
        // let's enable the radiobuttons container
        enableFields(true);
        // updating the radiobuttons container's title
        selectedContainer.setBorder(BorderFactory.createTitledBorder(Translator
                .localize("dialog.shortcut.titled-border.selected-partial")
                + " \"" + target.getActionName() + "\""));
        shortcutField.setText(KeyEventUtils.formatKeyStroke(target
                .getCurrentShortcut()));
        resetKeyStrokeConflict();

        // let's select the correct radio button
        if (target.getCurrentShortcut() == null) {
            // no shortcuts --> NONE
            noneButton.setSelected(true);
            shortcutField.setEnabled(false);
        } else if (target.getDefaultShortcut() != null
                && target.getCurrentShortcut().equals(
                        target.getDefaultShortcut())) {
            // current shortcut == default --> DEFAULT
            defaultButton.setSelected(true);
            shortcutField.setEnabled(false);
        } else {
            // customized shortcut --> CUSTOM
            customButton.setSelected(true);
            shortcutField.setEnabled(true);
            shortcutField.requestFocus();
        }
    }

    /**
     * @see org.argouml.ui.GUISettingsTabInterface#handleResetToDefault()
     */
    public void handleResetToDefault() {
        // Do nothing - these buttons are not shown.
    }

    /**
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() {
        // Do nothing!
        // The next time we refresh, we will fetch the values again.
    }

    /**
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        // let's reload the shortcuts
        actions = ShortcutMgr.getShortcuts();
        table.setModel(new ShortcutTableModel());
    }

    /**
     * @see org.argouml.ui.GUISettingsTabInterface#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        if (getActionAlreadyAssigned(ShortcutMgr
                .decodeKeyStroke(shortcutField.getText())) != null) {
            // conflict detected: showing a warning to the user, instead of
            // saving shortcuts
            JOptionPane.showMessageDialog(this, 
                    Translator.localize(
                            "optionpane.shortcut-save-conflict"),
                    Translator.localize(
                            "optionpane.shortcut-save-conflict-title"),
                          JOptionPane.WARNING_MESSAGE);
        } else {
            // saving shortcuts
            ShortcutMgr.saveShortcuts(actions);
        }
    }

    /**
     * This is called every time a row is selected. It just updates the fields
     * and the buttons status
     * 
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent lse) {
        if (lse.getValueIsAdjusting()) {
            return;
        }
        Object src = lse.getSource();
        if (src != table.getSelectionModel() || table.getSelectedRow() == -1) {
            return;
        }
        // if a shortcut has been select then we have to check if the actual
        // action is in conflict with other ones
        if (!noneButton.isSelected()) {
            Action oldAction = getActionAlreadyAssigned(ShortcutMgr
                    .decodeKeyStroke(shortcutField.getText()));
            if (oldAction != null) {
                // this shortcut was already been assigned to another action;
                // let's pop-up a message for the user
                String t = MessageFormat.format(Translator
                        .localize("optionpane.conflict-shortcut"),
                        new Object[] {shortcutField.getText(),
                                oldAction.getActionName() });
                int response = JOptionPane.showConfirmDialog(this, t, t,
                        JOptionPane.YES_NO_OPTION);
                switch (response) {
                case JOptionPane.YES_OPTION:
                    oldAction.setCurrentShortcut(null);
                    // blanking the old action's shortcut..
                    // and now refreshing the table.
                    table.setValueAt(oldAction, -1, -1);
                    break;
                case JOptionPane.NO_OPTION:
                    // re-selecting the old row, without changing the target -
                    // and without throwing another ListSelectionEvent!
                    table.getSelectionModel().removeListSelectionListener(this);
                    table.getSelectionModel().setSelectionInterval(
                            lastRowSelected, lastRowSelected);
                    table.getSelectionModel().addListSelectionListener(this);
                    return;
                }
            }
        }
        // let's change the target
        setTarget(actions[table.getSelectedRow()]);

        lastRowSelected = table.getSelectedRow();
    }

    /**
     * Verifies if the given keyStroke has already been assigned
     * to another action 
     * 
     * @param keyStroke
     *            the KeyStroke to be checked
     * @return the Action that has already been assigned
     */
    public Action getActionAlreadyAssigned(KeyStroke keyStroke) {
        for (int i = 0; i < actions.length; i++) {
            if (actions[i].getCurrentShortcut() != null
                    && actions[i].getCurrentShortcut().equals(keyStroke)
                    && !actions[i].getActionName().equals(
                            target.getActionName())) {
                return actions[i];
            }
        }
        return null;
    }

    /**
     * This is called every time a panel button (custom / default / none) is
     * pressed.
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        resetKeyStrokeConflict();

        if (e.getSource() == customButton) {
            setKeyStrokeValue(ShortcutMgr.decodeKeyStroke(shortcutField
                    .getText()));
            shortcutField.setEnabled(true);
            shortcutField.requestFocus();
        } else if (e.getSource() == defaultButton) {
            setKeyStrokeValue(target.getDefaultShortcut());
            shortcutField.resetLastValidKey();
            shortcutField.setEnabled(false);
            checkShortcutAlreadyAssigned(target.getDefaultShortcut());
        } else if (e.getSource() == noneButton) {
            setKeyStrokeValue(null);
            shortcutField.resetLastValidKey();
            shortcutField.setEnabled(false);
        }
    }

    /**
     * This method simply reset the "conflict" gui, by blanking the warning
     * label and resetting the shortcut's background color
     *
     */
    private void resetKeyStrokeConflict() {
        this.warningLabel.setText(" ");
        this.shortcutField.setBackground(shortcutFieldDefaultBg);
    }

    /**
     * Updates the GUI with the given new KeyStroke
     * 
     * @param newKeyStroke      the KeyStroke to be set
     */
    private void setKeyStrokeValue(KeyStroke newKeyStroke) {
        String formattedKeyStroke = KeyEventUtils.formatKeyStroke(newKeyStroke);
        // updating the shortcut field
        shortcutField.setText(formattedKeyStroke);
        // updating the table data
        table.getModel().setValueAt(newKeyStroke, table.getSelectedRow(), 1);
        // updating the actions
        actions[table.getSelectedRow()].setCurrentShortcut(newKeyStroke);
        table.repaint();
    }

    /**
     * Listener method for ShortcutChangedEvents
     * 
     * @see org.argouml.ui.cmd.ShortcutChangedListener#shortcutChange(org.argouml.ui.cmd.ShortcutChangedEvent)
     */
    public void shortcutChange(ShortcutChangedEvent event) {
        LOG.error("shortcutChange");
        checkShortcutAlreadyAssigned(event.getKeyStroke());
        setKeyStrokeValue(event.getKeyStroke());
    }

    /**
     * Update the GUI, if there is a conflict for the given key stroke
     * 
     * @param newKeyStroke      the key stroke to be checked
     */
    private void checkShortcutAlreadyAssigned(KeyStroke newKeyStroke) {
        Action oldAction = getActionAlreadyAssigned(newKeyStroke);
        if (oldAction != null) {
            // the shortcut has already been assigned to another action!
            this.shortcutField.setBackground(Color.YELLOW);
            this.warningLabel.setText(MessageFormat.format(Translator
                    .localize("misc.shortcuts.conflict"),
                    new Object[] {KeyEventUtils.formatKeyStroke(oldAction
                            .getCurrentShortcut()), 
                            oldAction.getActionName()}));
        } else {
            resetKeyStrokeConflict();
        }
    }

    /**
     * Table model for the table with modules.
     */
    class ShortcutTableModel extends AbstractTableModel {

        /**
         * Constructor.
         */
        public ShortcutTableModel() {
            elements = new Object[actions.length][3];

            for (int i = 0; i < elements.length; i++) {
                Action currentAction = (Action) actions[i];
                elements[i][0] = currentAction.getActionName();
                elements[i][1] = currentAction.getCurrentShortcut();
                elements[i][2] = currentAction.getDefaultShortcut();
            }
        }

        /**
         * @see javax.swing.table.TableModel#getColumnCount()
         */
        public int getColumnCount() {
            return columnNames.length;
        }

        /**
         * @see javax.swing.table.TableModel#getColumnName(int)
         */
        public String getColumnName(int col) {
            return columnNames[col];
        }

        /**
         * @see javax.swing.table.TableModel#getRowCount()
         */
        public int getRowCount() {
            return elements.length;
        }

        /**
         * @see javax.swing.table.TableModel#getValueAt(int, int)
         */
        public Object getValueAt(int row, int col) {
            return elements[row][col];
        }

        /**
         * Sets the value in the cell at <code>col</code> and
         * <code>row</code> to <code>ob</code>. If <code>ob</code> is
         * an Action instance, then the <code>col</code> and <code>row</code>
         * parameters are ignored, and the ob Action is searched among the 
         * elements and the actions.
         * 
         * @see javax.swing.table.TableModel#setValueAt( java.lang.Object, int,
         *      int)
         */
        public void setValueAt(Object ob, int row, int col) {
            // if the given object is a KeyStroke instance, then we ca
            if (ob instanceof Action) {
                Action newValueAction = (Action) ob;
                for (int i = 0; i < elements.length; i++) {
                    if (elements[i][0].equals(newValueAction.getActionName())) {
                        elements[i][1] = newValueAction.getCurrentShortcut();
                        repaint();
                        break;
                    }
                }
                // let's update also the actions array
                for (int i = 0; i < actions.length; i++) {
                    if (actions[i].getKey().equals(newValueAction.getKey())) {
                        actions[i].setCurrentShortcut(newValueAction
                                .getCurrentShortcut());
                        break;
                    }
                }

            } else {
                elements[row][col] = ob;
            }
        }

        /**
         * @see javax.swing.table.TableModel#getColumnClass(int)
         */
        public Class getColumnClass(int col) {
            switch (col) {
            case 0:
                return String.class;
            case 1:
                return KeyStroke.class;
            case 2:
                return KeyStroke.class;
            default:
                return null;
            }
        }

        /**
         * @see javax.swing.table.TableModel#isCellEditable(int, int)
         */
        public boolean isCellEditable(int row, int col) {
            return false;
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = -5970280716477119863L;
    }
}

/**
 * Argo's renderer for a KeyStroke object
 *
 * @author andrea.nironi@gmail.com
 */
class KeyStrokeCellRenderer extends DefaultTableCellRenderer {
    /**
     * The UID.
     */
    private static final long serialVersionUID = -7086302679799095974L;

    public KeyStrokeCellRenderer() {
        super();
        setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    }

    /**
     * Format the given KeyStroke
     * 
     * @see javax.swing.table.DefaultTableCellRenderer#setValue(java.lang.Object)
     */
    public void setValue(Object value) {
        if (value != null && value instanceof KeyStroke) {
            value = KeyEventUtils.formatKeyStroke((KeyStroke) value);
        }
        super.setValue(value);
    }
}