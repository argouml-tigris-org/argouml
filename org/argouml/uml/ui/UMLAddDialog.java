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

import org.argouml.application.api.Argo;
import org.argouml.application.helpers.ResourceLoaderWrapper;

/**
 * UMLAddDialog allows the user to do a multiple select from a list of choices in a seperate dialog. The 
 * dialog has two possible uses:
 * <p>
 * 1. As dialog as described above with a custom cellrenderer or a default cellrenderer.
 * </p>
 * <p>
 * 2. As dialog with a UMLCellRenderer. Cells in the choices list and selected list are presented with 
 * their name instead of their toString function.
 * </p>
 *
 * <p>$Id$
 */
public class UMLAddDialog extends JPanel implements ActionListener {
	
    /**
     * The choices a user has
     */
    protected Vector _choices = null;
    /**
     * The preselected choices
     */
    protected Vector _preSelected = null;
    /**
     * The selected choices.
     */
    protected Vector _selected = null;
	
    /**
     * The GUI list for the choices
     */
    protected JList _choicesList = null;
    /**
     * The GUI list for the selected choices
     */
    protected JList _selectedList = null;
    protected JButton _addButton = null;
    protected JButton _removeButton = null;
    protected JButton _okButton = null;
    protected JButton _cancelButton = null;
	
    protected JDialog _dialog = null;
    protected String _title = null;
    /**
     * The returnvalue of the method showDialog. Returnvalue can be either JOptionPane.OK_OPTION or
     * JOptionPane.CANCEL_OPTION
     */
    protected int _returnValue;
	
    /**
     * Constructs a UMLAddDialog with a UMLListCellRenderer. Modelelements are represented with their names in
     * the choices list and the selected list.
     * @param choices A vector with the choices a user has.
     * @param selected A vector with allready preselected choices
     * @param title The title of the dialog
     * @param multiselectAllowed True if the user may select multiple choices
     * @param exclusive True if choices in the selected list may not appear in the choices list. If true preselected
     * choices are removed from the choices list.
     */
    public UMLAddDialog(Vector choices, Vector selected, String title, boolean multiselectAllowed, boolean exclusive) {
	this(choices, selected, title, new UMLListCellRenderer(), multiselectAllowed, exclusive);
    }	
	
    /**
     * Constructs a UMLAddDialog with a given UMLListCellRenderer. 
     * @param choices A vector with the choices a user has.
     * @param selected A vector with allready preselected choices
     * @param title The title of the dialog
     * @param renderer The cellrenderer of the choices list and the selected list
     * @param multiselectAllowed True if the user may select multiple choices
     * @param exclusive True if choices in the selected list may not appear in the choices list. If true preselected
     * choices are removed from the choices list.
     */
    public UMLAddDialog(Vector choices, Vector selected, String title, ListCellRenderer renderer, boolean multiselectAllowed, boolean exclusive) {
	if (choices == null) throw new IllegalArgumentException("There should allways be choices in UMLAddDialog");
	if (exclusive && selected != null && !selected.isEmpty()) {
	    choices.removeAll(selected);
	}
	_choices = choices;
	_preSelected = selected;
	_selected = new Vector();
	if (title != null) {
	    _title = title;
	} else {
	    _title = "";
	}
	if (selected != null) {
	    _selected.addAll(selected);
	}
		
	setLayout(new BorderLayout());
		
	JPanel upperPanel = new JPanel();	
	JPanel panelChoices = new JPanel(new BorderLayout());
	JPanel panelSelected = new JPanel(new BorderLayout());
		
		
	_choicesList = new JList(constructListModel(choices));
	_choicesList.setPrototypeCellValue("123456789:123456789:123456789");
	if (renderer != null) {
	    _choicesList.setCellRenderer(renderer);
	}
	if (multiselectAllowed) {
	    _choicesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	} else {
	    _choicesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	_choicesList.setVisibleRowCount(15);
	JScrollPane choicesScroll = new JScrollPane(_choicesList);
	panelChoices.add(new JLabel(Argo.localize("UMLMenu", "label.choices")), BorderLayout.NORTH);
	panelChoices.add(choicesScroll, BorderLayout.CENTER);
		
	_addButton = new JButton(ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("NavigateForward"));
	_addButton.addActionListener(this);
	_removeButton = new JButton(ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("NavigateBack"));
	_removeButton.addActionListener(this);
	Box buttonBox = Box.createVerticalBox();
	// buttonBox.add(Box.createRigidArea(new Dimension(0, 20)));
	buttonBox.add(_addButton);
	buttonBox.add(Box.createRigidArea(new Dimension(0, 5)));
	buttonBox.add(_removeButton);
		
	_selectedList = new JList(constructListModel(_selected));	
	_selectedList.setPrototypeCellValue("123456789:123456789;123456789");
	if (renderer != null) {
	    _selectedList.setCellRenderer(renderer);
	}
	_selectedList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	_selectedList.setVisibleRowCount(15);
	JScrollPane selectedScroll = new JScrollPane(_selectedList);
	panelSelected.add(new JLabel(Argo.localize("UMLMenu", "label.selected")), BorderLayout.NORTH);
	panelSelected.add(selectedScroll, BorderLayout.CENTER);
		
	upperPanel.add(panelChoices);	
	upperPanel.add(Box.createRigidArea(new Dimension(5, 0)));
	upperPanel.add(buttonBox);
	upperPanel.add(Box.createRigidArea(new Dimension(5, 0)));
	upperPanel.add(panelSelected);	
	// upperPanel.setBorder(BorderFactory.createEtchedBorder());
		
	add(upperPanel, BorderLayout.NORTH);
		
		
	JPanel okCancelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
	_okButton = new JButton(Argo.localize("UMLMenu", "button.ok"));
	_okButton.addActionListener(this);
	_cancelButton = new JButton(Argo.localize("UMLMenu", "button.cancel"));
	_cancelButton.addActionListener(this);		
	okCancelPanel.add(_okButton);
	okCancelPanel.add(_cancelButton);
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
	if (source.equals(_addButton)) {
	    addSelection();
	    update();
	}
	if (source.equals(_removeButton)) {
	    removeSelection();
	    update();
	}
	if (source.equals(_okButton)) {
	    ok();
	}
	if (source.equals(_cancelButton)) {
	    cancel();
	}
    }
	
    /**
     * Updates the add and remove button (sets enabled/disabled). Called whenever the model is changed.
     */
    public void update() {
	if (_choices.size() == 0) {
	    _addButton.setEnabled(false);
	} else {
	    _addButton.setEnabled(true);
	}
	if (_selected.size() == 0) {
	    _removeButton.setEnabled(false);
	} else {
	    _removeButton.setEnabled(true);
	}
    }
	
    /**
     * Utility method to construct a DefaultListModel from a Vector
     * @param vec
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
     * Shows the dialog. First a dialog must be constructed using one of the constructors of this class.
     * After that this method should be called to actually show the dialog. This method returns either 
     * JOptionPane.OK_OPTION if the user wants to select his choices or JOptionPane.CANCEL_OPTION if he
     * does not want to.
     * @param parent The parent frame of this dialog.
     * @return int The returnvalue, can be either JOptionPane.OK_OPTION or JOptionPane.CANCEL_OPTION
     */
    public int showDialog(Component parent) {
	Frame frame = parent instanceof Frame ? (Frame) parent
	    : (Frame) SwingUtilities.getAncestorOfClass(Frame.class, parent);

	// String title = getUI().getDialogTitle(this);

        _dialog = new JDialog(frame, _title, true);
        Container contentPane = _dialog.getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(this, BorderLayout.CENTER);
 
	_dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	_dialog.addWindowListener(new WindowAdapter() 
	    {
    		public void windowClosing(WindowEvent we) {
		    cancel();
    		}
	    });

        _dialog.pack();
        _dialog.setLocationRelativeTo(parent);
 
        _dialog.show();
	return _returnValue;
    }
	
    /**
     * Returns the choices a user can make.
     * @return Vector
     */
    public Vector getChoices() {
	int[] indices = _choicesList.getSelectedIndices();
	Vector returnVector = new Vector();
	for (int i = 0; i < indices.length; i++) {
	    returnVector.add(_choices.get(indices[i]));
	}
	return returnVector;
    }
	
    /**
     * Returns the selected elements in the selected list
     * @return Vector
     */
    public Vector getSelectedChoices() {
	Vector returnVector = new Vector();
	if (_selectedList != null && _selected != null) {
	    int[] indices = _selectedList.getSelectedIndices();			
	    for (int i = 0; i < indices.length; i++) {
		returnVector.add(_selected.get(indices[i]));
	    }
	}
	return returnVector;
    }
	
    /**
     * Returns the by the user selected elements. This method should be called if the selected choices
     * are to be known. 
     * @return Vector
     */
    public Vector getSelected() {
	return _selected;
    }
	
    /**
     * Adds the selected elements in the choices list to the selected list. Updates the GUI too.
     */
    public void addSelection() {
	Vector choices = getChoices();
	_choices.removeAll(choices);
	for (int i = 0; i < choices.size(); i++) {
	    ((DefaultListModel) _choicesList.getModel()).removeElement(choices.get(i));
	}
	_selected.addAll(choices);
	for (int i = 0; i < choices.size(); i++) {
	    ((DefaultListModel) _selectedList.getModel()).addElement(choices.get(i));
	}
    }
	
    /**
     * Removes the selected elements in the selected list and adds them to the choices list. Updates the GUI too.
     */
    public void removeSelection() {
	Vector choices = getSelectedChoices();
	_selected.removeAll(choices);
	for (int i = 0; i < choices.size(); i++) {
	    ((DefaultListModel) _selectedList.getModel()).removeElement(choices.get(i));
	}
	_choices.addAll(choices);
	for (int i = 0; i < choices.size(); i++) {
	    ((DefaultListModel) _choicesList.getModel()).addElement(choices.get(i));
	}
    }
	
    /**
     * Called when the okbutton is pressed. Closes this dialog and sets the returnvalue to JOptionPane.OK_OPTION
     */
    public void ok() {
	if (_dialog != null) {
	    _dialog.setVisible(false);
	    _returnValue = JOptionPane.OK_OPTION;
	}
    }
	
    /**
     * Called when the cancel button is pressed. Closes this dialog and sets the returnvalue to JOptionPane.CANCEL_OPTION.
     * Resets the selected list to the originally preselection if there is one. Otherwise the selected list
     * is emptied.
     */
    public void cancel() {
	_selected.removeAllElements();
	if (_preSelected != null) {
	    _selected.addAll(_preSelected);
	}
	if (_dialog != null) {
	    _dialog.setVisible(false);
	    _returnValue = JOptionPane.CANCEL_OPTION;
	}
    }
}

/*
class UMLCellRenderer extends JLabel implements ListCellRenderer {
	/**
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(JList, Object, int, boolean, boolean)
	 */
	 /*
	public Component getListCellRendererComponent(
		JList list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus) {
		
		String s = null;
		if (value instanceof MModelElement) {
			s = ((MModelElement)value).getName();
		} else {
			s = value.toString();
		}
     	setText(s);
   		if (isSelected) {
        	setBackground(list.getSelectionBackground());
       		setForeground(list.getSelectionForeground());
   		}
     	else {
       		setBackground(list.getBackground());
       		setForeground(list.getForeground());
   		}
   		setEnabled(list.isEnabled());
   		setFont(list.getFont());
     	return this;
	}

}
*/
	





