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

package org.argouml.uml.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;

/**
 * UMLAddDialog allows the user to do a multiple select from a list of choices
 * in a separate dialog. The dialog has two possible uses:
 * <ol>
 * <li>As dialog as described above with a custom cellrenderer or a default
 * cellrenderer.
 * <li>As dialog with a UMLCellRenderer. Cells in the choices list and selected
 * list are presented with their name instead of their toString function.
 * </ol>
 */
public class UMLAddDialog extends JPanel implements ActionListener {

    /**
     * The choices a user has
     */
    private Vector choices = null;

    /**
     * The preselected choices
     */
    private Vector preSelected = null;

    /**
     * The selected choices.
     */
    private Vector selected = null;

    /**
     * The GUI list for the choices
     */
    private JList choicesList = null;

    /**
     * The GUI list for the selected choices
     */
    private JList selectedList = null;

    private JButton addButton = null;

    private JButton removeButton = null;

    private JButton okButton = null;

    private JButton cancelButton = null;

    private JDialog dialog = null;

    private String title = null;

    private boolean multiSelectAllowed = false;

    /**
     * The returnvalue of the method showDialog. Returnvalue can be either
     * JOptionPane.OK_OPTION or JOptionPane.CANCEL_OPTION
     */
    private int returnValue;

    /**
     * Constructs a UMLAddDialog with a UMLListCellRenderer. Modelelements are
     * represented with their names in the choices list and the selected list.
     *
     * @param theChoices
     *            A vector with the choices a user has.
     * @param preselected
     *            A vector with already preselected choices
     * @param theTitle
     *            The title of the dialog
     * @param multiselectAllowed
     *            True if the user may select multiple choices
     * @param exclusive
     *            True if choices in the selected list may not appear in the
     *            choices list. If true preselected choices are removed from the
     *            choices list.
     */
    public UMLAddDialog(Vector theChoices, Vector preselected, String theTitle,
            boolean multiselectAllowed, boolean exclusive) {
        this(theChoices, preselected, theTitle, new UMLListCellRenderer2(true),
                multiselectAllowed, exclusive);
    }

    /**
     * Constructs a UMLAddDialog with a given UMLListCellRenderer.
     *
     * @param theChoices
     *            A vector with the choices a user has.
     * @param preselected
     *            A vector with already preselected choices
     * @param theTitle
     *            The title of the dialog
     * @param renderer
     *            The cellrenderer of the choices list and the selected list
     * @param multiselectAllowed
     *            True if the user may select multiple choices
     * @param exclusive
     *            True if choices in the selected list may not appear in the
     *            choices list. If true preselected choices are removed from the
     *            choices list.
     */
    public UMLAddDialog(Vector theChoices, Vector preselected, String theTitle,
            ListCellRenderer renderer, boolean multiselectAllowed,
            boolean exclusive) {
        multiSelectAllowed = multiselectAllowed;
        if (theChoices == null) {
            throw new IllegalArgumentException(
                    "There should allways be choices in UMLAddDialog");
        }
        if (exclusive && preselected != null && !preselected.isEmpty()) {
            theChoices.removeAll(preselected);
        }
        choices = theChoices;
        preSelected = preselected;
        selected = new Vector();
        if (theTitle != null) {
            title = theTitle;
        } else {
            title = "";
        }
        if (preselected != null) {
            selected.addAll(preselected);
        }

        setLayout(new BorderLayout());

        JPanel upperPanel = new JPanel();
        JPanel panelChoices = new JPanel(new BorderLayout());
        JPanel panelSelected = new JPanel(new BorderLayout());

        choicesList = new JList(constructListModel(theChoices));
        choicesList.setMinimumSize(new Dimension(150, 300));
        if (renderer != null) {
            choicesList.setCellRenderer(renderer);
        }
        if (multiselectAllowed) {
            choicesList.setSelectionMode(
                    ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        } else {
            choicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        }
        choicesList.setVisibleRowCount(15);
        JScrollPane choicesScroll = new JScrollPane(choicesList);
        panelChoices.add(new JLabel(Translator.localize("label.choices")),
                BorderLayout.NORTH);
        panelChoices.add(choicesScroll, BorderLayout.CENTER);

        addButton = new JButton(ResourceLoaderWrapper
                .lookupIconResource("NavigateForward"));
        addButton.addActionListener(this);
        removeButton = new JButton(ResourceLoaderWrapper
                .lookupIconResource("NavigateBack"));
        removeButton.addActionListener(this);
        Box buttonBox = Box.createVerticalBox();
        // buttonBox.add(Box.createRigidArea(new Dimension(0, 20)));
        buttonBox.add(addButton);
        buttonBox.add(Box.createRigidArea(new Dimension(0, 5)));
        buttonBox.add(removeButton);

        selectedList = new JList(constructListModel(selected));
        selectedList.setMinimumSize(new Dimension(150, 300));
        if (renderer != null) {
            selectedList.setCellRenderer(renderer);
        }
        selectedList
                .setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        selectedList.setVisibleRowCount(15);
        JScrollPane selectedScroll = new JScrollPane(selectedList);
        panelSelected.add(new JLabel(Translator.localize("label.selected")),
                BorderLayout.NORTH);
        panelSelected.add(selectedScroll, BorderLayout.CENTER);

        upperPanel.add(panelChoices);
        upperPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        upperPanel.add(buttonBox);
        upperPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        upperPanel.add(panelSelected);
        // upperPanel.setBorder(BorderFactory.createEtchedBorder());

        add(upperPanel, BorderLayout.NORTH);

        JPanel okCancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        okButton = new JButton(Translator.localize("button.ok"));
        okButton.addActionListener(this);
        cancelButton = new JButton(Translator.localize("button.cancel"));
        cancelButton.addActionListener(this);
        okCancelPanel.add(okButton);
        okCancelPanel.add(cancelButton);
        okCancelPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 10));

        add(okCancelPanel, BorderLayout.SOUTH);
        setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        update();
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source.equals(addButton)) {
            addSelection();
            update();
        }
        if (source.equals(removeButton)) {
            removeSelection();
            update();
        }
        if (source.equals(okButton)) {
            ok();
        }
        if (source.equals(cancelButton)) {
            cancel();
        }
    }

    /**
     * Updates the add and remove button (sets enabled/disabled). Called
     * whenever the model is changed.
     */
    public void update() {
        if (choices.size() == 0) {
            addButton.setEnabled(false);
        } else {
            addButton.setEnabled(true);
        }
        if (selected.size() == 0) {
            removeButton.setEnabled(false);
        } else {
            removeButton.setEnabled(true);
        }
        if (selected.size() > 1 && !multiSelectAllowed) {
            addButton.setEnabled(false);
            okButton.setEnabled(false);
        }
        else {
            addButton.setEnabled(true);
            okButton.setEnabled(true);
        }
    }

    /**
     * Utility method to construct a DefaultListModel from a Vector
     *
     * @param vec
     *            the given list
     * @return DefaultListModel
     */
    protected DefaultListModel constructListModel(Vector vec) {
        DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < vec.size(); i++) {
            model.addElement(vec.get(i));
        }
        return model;
    }

    /**
     * Shows the dialog. First a dialog must be constructed using one of the
     * constructors of this class. After that this method should be called to
     * actually show the dialog. This method returns either
     * JOptionPane.OK_OPTION if the user wants to select his choices or
     * JOptionPane.CANCEL_OPTION if he does not want to.
     *
     * @param parent
     *            The parent frame of this dialog.
     * @return int The returnvalue, can be either JOptionPane.OK_OPTION or
     *         JOptionPane.CANCEL_OPTION
     */
    public int showDialog(Component parent) {
        Frame frame = parent instanceof Frame ? (Frame) parent
                : (Frame) SwingUtilities
                        .getAncestorOfClass(Frame.class, parent);

        // String title = getUI().getDialogTitle(this);

        dialog = new JDialog(frame, title, true);
        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);

        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                cancel();
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        dialog.setVisible(true);
        return returnValue;
    }

    /**
     * Returns the choices a user can make.
     *
     * @return Vector
     */
    public Vector getChoices() {
        int[] indices = choicesList.getSelectedIndices();
        Vector returnVector = new Vector();
        for (int i = 0; i < indices.length; i++) {
            returnVector.add(choices.get(indices[i]));
        }
        return returnVector;
    }

    /**
     * Returns the selected elements in the selected list
     *
     * @return Vector
     */
    public Vector getSelectedChoices() {
        Vector returnVector = new Vector();
        if (selectedList != null && selected != null) {
            int[] indices = selectedList.getSelectedIndices();
            for (int i = 0; i < indices.length; i++) {
                returnVector.add(selected.get(indices[i]));
            }
        }
        return returnVector;
    }

    /**
     * Returns the by the user selected elements. This method should be called
     * if the selected choices are to be known.
     *
     * @return Vector
     */
    public Vector getSelected() {
        return selected;
    }

    /**
     * Adds the selected elements in the choices list to the selected list.
     * Updates the GUI too.
     */
    public void addSelection() {
        Vector theChoices = getChoices();
        choices.removeAll(theChoices);
        for (int i = 0; i < theChoices.size(); i++) {
            ((DefaultListModel) choicesList.getModel())
                    .removeElement(theChoices.get(i));
        }
        selected.addAll(theChoices);
        for (int i = 0; i < theChoices.size(); i++) {
            ((DefaultListModel) selectedList.getModel()).addElement(theChoices
                    .get(i));
        }
    }

    /**
     * Removes the selected elements in the selected list and adds them to the
     * choices list. Updates the GUI too.
     */
    public void removeSelection() {
        Vector theChoices = getSelectedChoices();
        selected.removeAll(theChoices);
        for (int i = 0; i < theChoices.size(); i++) {
            ((DefaultListModel) selectedList.getModel())
                    .removeElement(theChoices.get(i));
        }
        choices.addAll(theChoices);
        for (int i = 0; i < theChoices.size(); i++) {
            ((DefaultListModel) choicesList.getModel()).addElement(theChoices
                    .get(i));
        }
    }

    /**
     * Called when the okbutton is pressed. Closes this dialog and sets the
     * returnvalue to JOptionPane.OK_OPTION.
     */
    public void ok() {
        if (dialog != null) {
            dialog.setVisible(false);
            returnValue = JOptionPane.OK_OPTION;
        }
    }

    /**
     * Called when the cancel button is pressed. Closes this dialog and sets the
     * returnvalue to JOptionPane.CANCEL_OPTION. Resets the selected list to the
     * originally preselection if there is one. Otherwise the selected list is
     * emptied.
     */
    public void cancel() {
        selected.removeAllElements();
        if (preSelected != null) {
            selected.addAll(preSelected);
        }
        if (dialog != null) {
            dialog.setVisible(false);
            returnValue = JOptionPane.CANCEL_OPTION;
        }
    }
}
