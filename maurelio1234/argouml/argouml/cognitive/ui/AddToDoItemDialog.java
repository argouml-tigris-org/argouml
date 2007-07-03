// $Id: AddToDoItemDialog.java 12950 2007-07-01 08:10:04Z tfmorris $
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

package org.argouml.cognitive.ui;

import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.Translator;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.argouml.util.ArgoDialog;
import org.tigris.swidgets.Dialog;
import org.tigris.swidgets.LabelledLayout;

/**
 * The dialog to enter a new ToDoItem.
 */
public class AddToDoItemDialog extends ArgoDialog {

    ////////////////////////////////////////////////////////////////
    // constants
    private static final String[] PRIORITIES = {
        Translator.localize("misc.level.high"),
        Translator.localize("misc.level.medium"),
        Translator.localize("misc.level.low"),
    };
    private static final int TEXT_ROWS = 8;
    private static final int TEXT_COLUMNS = 30;
    /** Insets in pixels  */
    private static final int INSET_PX = 3;

    ////////////////////////////////////////////////////////////////
    // instance variables
    private JTextField headLineTextField;
    private JComboBox  priorityComboBox;
    private JTextField moreinfoTextField;
    private JList offenderList;
    private JTextArea  descriptionTextArea;


    /**
     * Create a new AddToDoItemDialog
     * @param renderer the ListCellRenderer to use in order
     *                 to display the offenders
     */
    public AddToDoItemDialog(ListCellRenderer renderer) {
        super(Translator.localize("dialog.title.add-todo-item"),
	      Dialog.OK_CANCEL_OPTION, true);

        headLineTextField = new JTextField(TEXT_COLUMNS);
        priorityComboBox = new JComboBox(PRIORITIES);
        moreinfoTextField = new JTextField(TEXT_COLUMNS);
        descriptionTextArea = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);

        DefaultListModel dlm = new DefaultListModel();
        Object[] offObj =
            TargetManager.getInstance().getModelTargets().toArray();
        for (int i = 0; i < offObj.length; i++) {
            if (offObj[i] != null) {
                dlm.addElement(offObj[i]);
            }
        }

        offenderList = new JList(dlm);
        offenderList.setCellRenderer(renderer);
        JScrollPane offenderScroll = new JScrollPane(offenderList);
        offenderScroll.setOpaque(true);

        JLabel headlineLabel =
            new JLabel(Translator.localize("label.headline"));
        JLabel priorityLabel =
            new JLabel(Translator.localize("label.priority"));
        JLabel moreInfoLabel =
            new JLabel(Translator.localize("label.more-info-url"));
        JLabel offenderLabel =
            new JLabel(Translator.localize("label.offenders"));
        priorityComboBox.setSelectedItem(PRIORITIES[0]);

        JPanel panel = new JPanel(new LabelledLayout(getLabelGap(),
                getComponentGap()));

        headlineLabel.setLabelFor(headLineTextField);
        panel.add(headlineLabel);
        panel.add(headLineTextField);

        priorityLabel.setLabelFor(priorityComboBox);
        panel.add(priorityLabel);
        panel.add(priorityComboBox);

        moreInfoLabel.setLabelFor(moreinfoTextField);
        panel.add(moreInfoLabel);
        panel.add(moreinfoTextField);

        offenderLabel.setLabelFor(offenderScroll);
        panel.add(offenderLabel);
        panel.add(offenderScroll);

        descriptionTextArea.setLineWrap(true);  //MVW - Issue 2422
        descriptionTextArea.setWrapStyleWord(true);   //MVW - Issue 2422
        descriptionTextArea.setText(Translator.localize("label.enter-todo-item")
                	    + "\n");
        descriptionTextArea.setMargin(new Insets(INSET_PX, INSET_PX,
                INSET_PX, INSET_PX));
        JScrollPane descriptionScroller = new JScrollPane(descriptionTextArea);
        descriptionScroller.setPreferredSize(
                descriptionTextArea.getPreferredSize());
        panel.add(descriptionScroller);

        setContent(panel);
    }


    /*
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        if (e.getSource() == getOkButton()) {
            doAdd();
        }
    }

    private void doAdd() {
        Designer designer = Designer.theDesigner();
        String headline = headLineTextField.getText();
        int priority = ToDoItem.HIGH_PRIORITY;
        switch (priorityComboBox.getSelectedIndex()) {
	case 0:
	    priority = ToDoItem.HIGH_PRIORITY;
	    break;
	case 1:
	    priority = ToDoItem.MED_PRIORITY;
	    break;
	case 2:
	    priority = ToDoItem.LOW_PRIORITY;
	    break;
        }
        String desc = descriptionTextArea.getText();
        String moreInfoURL = moreinfoTextField.getText();
        ToDoItem item =
	    new UMLToDoItem(designer, headline, priority, desc, moreInfoURL);
        ListSet newOffenders = new ListSet();
        for (int i = 0; i < offenderList.getModel().getSize(); i++) {
            newOffenders.add(offenderList.getModel().getElementAt(i));
        }
        item.setOffenders(newOffenders);
        designer.getToDoList().addElement(item); //? inform()
        Designer.firePropertyChange(Designer.MODEL_TODOITEM_ADDED, null, item);
    }
}

