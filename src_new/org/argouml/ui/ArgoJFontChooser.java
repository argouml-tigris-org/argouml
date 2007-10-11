// $Id$
// Copyright (c) 2007 The Regents of the University of California. All
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

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.argouml.i18n.Translator;

/**
 * A font chooser dialog for selecting font family, size and style.
 *
 * @author Aleksandar
 */
public class ArgoJFontChooser extends JDialog {

    private JPanel jContentPane = null;

    private JList jlstFamilies = null;

    private JList jlstSizes = null;

//    private JCheckBox jchbBold = null;

//    private JCheckBox jchbItalic = null;

    private JLabel jlblFamilies = null;

    private JLabel jlblSize = null;

    private JLabel jlblPreview = null;

    private JButton jbtnOk = null;

    private JButton jbtnCancel = null;

    private int resultSize;

    private String resultName;

    private boolean isOk = false;

    /**
     * @param owner the <code>Frame</code> from which the dialog is displayed
     * @param parent determines the position of the chooser
     * @param name the initial font name value
     * @param size the initial font size value
     */
    public ArgoJFontChooser(Frame owner, JComponent parent, String name,
            int size) {
        super(owner, true);
        setLocationRelativeTo(parent);

        this.resultName = name;
        this.resultSize = size;

        initialize();
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.setSize(299, 400);
        this.setTitle(Translator.localize("dialog.fontchooser"));
        this.setContentPane(getJContentPane());

        updatePreview();
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
            gridBagConstraints8.gridx = 4;
            gridBagConstraints8.anchor = GridBagConstraints.NORTHEAST;
            gridBagConstraints8.insets = new Insets(0, 0, 5, 5);
            gridBagConstraints8.weightx = 0.0;
            gridBagConstraints8.ipadx = 0;
            gridBagConstraints8.gridy = 5;
            GridBagConstraints gridBagConstraints7 = new GridBagConstraints();
            gridBagConstraints7.gridx = 3;
            gridBagConstraints7.fill = GridBagConstraints.NONE;
            gridBagConstraints7.weightx = 1.0;
            gridBagConstraints7.anchor = GridBagConstraints.NORTHEAST;
            gridBagConstraints7.insets = new Insets(0, 0, 5, 5);
            gridBagConstraints7.weighty = 0.0;
            gridBagConstraints7.gridwidth = 1;
            gridBagConstraints7.ipadx = 0;
            gridBagConstraints7.gridy = 5;
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.gridwidth = 5;
            gridBagConstraints6.fill = GridBagConstraints.HORIZONTAL;
            gridBagConstraints6.weightx = 1.0;
            gridBagConstraints6.insets = new Insets(5, 5, 5, 5);
            gridBagConstraints6.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints6.gridy = 4;
            jlblPreview = new JLabel();
            jlblPreview.setText(Translator
                    .localize("label.diagramappearance.preview"));
            jlblPreview.setPreferredSize(new Dimension(52, 50));
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 4;
            gridBagConstraints5.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints5.insets = new Insets(5, 5, 0, 0);
            gridBagConstraints5.gridy = 0;
            jlblSize = new JLabel();
            jlblSize.setText(Translator
                    .localize("label.diagramappearance.fontsize"));
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints4.insets = new Insets(5, 5, 0, 0);
            gridBagConstraints4.gridy = 0;
            jlblFamilies = new JLabel();
            jlblFamilies.setText(Translator
                    .localize("label.diagramappearance.fontlist"));
//            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
//            gridBagConstraints3.gridx = 2;
//            gridBagConstraints3.anchor = GridBagConstraints.NORTHWEST;
//            gridBagConstraints3.insets = new Insets(5, 5, 0, 0);
//            gridBagConstraints3.gridy = 3;
//            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
//            gridBagConstraints2.gridx = 0;
//            gridBagConstraints2.anchor = GridBagConstraints.NORTHWEST;
//            gridBagConstraints2.insets = new Insets(5, 5, 0, 0);
//            gridBagConstraints2.gridy = 3;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.gridy = 2;
            gridBagConstraints1.weightx = 0.0;
            gridBagConstraints1.weighty = 1.0;
            gridBagConstraints1.insets = new Insets(5, 0, 0, 5);
            gridBagConstraints1.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints1.gridwidth = 2;
            gridBagConstraints1.gridx = 4;
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.gridy = 2;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            gridBagConstraints.insets = new Insets(5, 5, 0, 5);
            gridBagConstraints.gridwidth = 4;
            gridBagConstraints.gridheight = 1;
            gridBagConstraints.anchor = GridBagConstraints.NORTHWEST;
            gridBagConstraints.gridx = 0;
            jContentPane = new JPanel();
            jContentPane.setLayout(new GridBagLayout());

            JScrollPane jscpFamilies = new JScrollPane();
            jscpFamilies.setViewportView(getJlstFamilies());
            JScrollPane jscpSizes = new JScrollPane();
            jscpSizes.setViewportView(getJlstSizes());
            jContentPane.add(jscpFamilies, gridBagConstraints);
            jContentPane.add(jscpSizes, gridBagConstraints1);
//            jContentPane.add(getJchbBold(), gridBagConstraints2);
//            jContentPane.add(getJchbItalic(), gridBagConstraints3);
            jContentPane.add(jlblFamilies, gridBagConstraints4);
            jContentPane.add(jlblSize, gridBagConstraints5);
            jContentPane.add(jlblPreview, gridBagConstraints6);
            jContentPane.add(getJbtnOk(), gridBagConstraints7);
            jContentPane.add(getJbtnCancel(), gridBagConstraints8);
        }
        return jContentPane;
    }

    /**
     * This method initializes jlstFamilies
     *
     * @return javax.swing.JList
     */
    private JList getJlstFamilies() {
        if (jlstFamilies == null) {
            jlstFamilies = new JList();
            jlstFamilies.setModel(new DefaultListModel());

            String[] fontNames = GraphicsEnvironment
                    .getLocalGraphicsEnvironment()
                    .getAvailableFontFamilyNames();
            for (String fontName : fontNames) {
                ((DefaultListModel) jlstFamilies.getModel())
                        .addElement(fontName);
            }
            jlstFamilies.setSelectedValue(resultName, true);

            jlstFamilies.getSelectionModel().addListSelectionListener(
                    new ListSelectionListener() {
                        public void valueChanged(ListSelectionEvent e) {
                            if (jlstFamilies.getSelectedValue() != null) {
                                resultName = (String) jlstFamilies
                                        .getSelectedValue();
                                updatePreview();
                            }
                        }
                    });
        }
        return jlstFamilies;
    }

    /**
     * This method initializes jlstSizes
     *
     * @return javax.swing.JList
     */
    private JList getJlstSizes() {
        if (jlstSizes == null) {
            jlstSizes = new JList(new Integer[] {Integer.valueOf(8),
                Integer.valueOf(9), Integer.valueOf(10), Integer.valueOf(11),
                Integer.valueOf(12), Integer.valueOf(14), Integer.valueOf(16),
                Integer.valueOf(18), Integer.valueOf(20), Integer.valueOf(22),
                Integer.valueOf(24), Integer.valueOf(26), Integer.valueOf(28),
                Integer.valueOf(36), Integer.valueOf(48), Integer.valueOf(72)
            });
            jlstSizes.setSelectedValue(resultSize, true);

            jlstSizes.getSelectionModel().addListSelectionListener(
                    new ListSelectionListener() {
                        public void valueChanged(ListSelectionEvent e) {
                            if (jlstSizes.getSelectedValue() != null) {
                                resultSize = (Integer) jlstSizes
                                        .getSelectedValue();
                                updatePreview();
                            }
                        }
                    });
        }
        return jlstSizes;
    }


    /**
     * Updates preview label.
     */
    private void updatePreview() {
        int style = 0;

        Font previewFont = new Font(resultName, style, resultSize);
        jlblPreview.setFont(previewFont);
    }

    /**
     * This method initializes jbtnOk
     *
     * @return javax.swing.JButton
     */
    private JButton getJbtnOk() {
        if (jbtnOk == null) {
            jbtnOk = new JButton();
            jbtnOk.setText(Translator.localize("button.ok"));

            jbtnOk.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    isOk = true;
                    dispose();
                    setVisible(false);
                }
            });
        }
        return jbtnOk;
    }

    /**
     * This method initializes jbtnCancel
     *
     * @return javax.swing.JButton
     */
    private JButton getJbtnCancel() {
        if (jbtnCancel == null) {
            jbtnCancel = new JButton();
            jbtnCancel.setText(Translator.localize("button.cancel"));

            jbtnCancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    isOk = false;
                    dispose();
                    setVisible(false);
                }
            });
        }
        return jbtnCancel;
    }

    /**
     * Returns true if dilaog result is OK.
     *
     * @return dialog result
     */
    public boolean isOk() {
        return isOk;
    }

    /**
     * Result.
     *
     * @return result
     */
    public String getResultName() {
        return resultName;
    }

    /**
     * Result.
     *
     * @return result
     */
    public int getResultSize() {
        return resultSize;
    }
}
