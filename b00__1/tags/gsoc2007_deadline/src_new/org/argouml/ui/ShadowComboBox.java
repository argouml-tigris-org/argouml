// $Id:ShadowComboBox.java 11516 2006-11-25 04:30:15Z tfmorris $
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

package org.argouml.ui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.argouml.i18n.Translator;
import org.argouml.uml.diagram.ui.FigNodeModelElement;

/**
 * A ComboBox that contains the set of possible Shadow Width values.
 *
 * @author Jeremy Jones
 */
public class ShadowComboBox extends JComboBox {

    private static ShadowFig[]  shadowFigs;

    /**
     * The constructor.
     *
     */
    public ShadowComboBox() {
        super();

        addItem(Translator.localize("label.stylepane.no-shadow"));
        addItem("1");
        addItem("2");
        addItem("3");
        addItem("4");
        addItem("5");
        addItem("6");
        addItem("7");
        addItem("8");

        setRenderer(new ShadowRenderer());
    }

    /**
     * Renders each combo box entry as a shadowed diagram figure with the
     * associated level of shadow.
     */
    private class ShadowRenderer
	extends JComponent
	implements ListCellRenderer {

        private ShadowFig  currentFig;

        /**
         * Constructor.
         */
        public ShadowRenderer() {
            super();
        }

        /*
         * @see javax.swing.ListCellRenderer#getListCellRendererComponent(
         *         javax.swing.JList, java.lang.Object, int, boolean, boolean)
         */
        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

            if (shadowFigs == null) {
                shadowFigs = new ShadowFig[ShadowComboBox.this.getItemCount()];

                for (int i = 0; i < shadowFigs.length; ++i) {
                    shadowFigs[i] = new ShadowFig();
                    shadowFigs[i].setShadowSize(i);
                    shadowFigs[i].setName(
                        (String) ShadowComboBox.this.getItemAt(i));
                }
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
            } else {
                setBackground(list.getBackground());
            }

            int figIndex = index;
            if (figIndex < 0) {
                for (int i = 0; i < shadowFigs.length; ++i) {
                    if (value == ShadowComboBox.this.getItemAt(i)) {
                        figIndex = i;
                    }
                }
            }

            if (figIndex >= 0) {
                currentFig = shadowFigs[figIndex];
                setPreferredSize(new Dimension(
                    currentFig.getWidth() + figIndex + 4,
                    currentFig.getHeight() + figIndex + 2));
            } else {
                currentFig = null;
            }

            return this;
        }

        /*
         * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
         */
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            if (currentFig != null) {
                currentFig.setLocation(2, 1);
                currentFig.paint(g);
            }
        }

        /**
         * The UID.
         */
        private static final long serialVersionUID = 5939340501470674464L;
    }

    /**
     * This Fig is never placed on a diagram. It is only used by the call
     * renderer so that pick list items look like diagram Figs.
     */
    private static class ShadowFig extends FigNodeModelElement {
        /**
         * Constructor.
         */
        public ShadowFig() {
            super();
            addFig(getBigPort());
            addFig(getNameFig());
        }
        
        public void setName(String text) {
            getNameFig().setText(text);
        }
        
        /**
         * TODO: Bob says - This is a really nasty horrible hack.
         * ShadowFig should not extend FigNodeModelElement. Instead
         * we require a base class FigNode with common behaviour of ALL
         * nodes in ArgoUML. ShadowFig should extend that and
         * FigNodeModelElement should extend that same base class adding
         * common functionality for FigNode that represent model element.
         * @see org.argouml.uml.diagram.ui.FigNodeModelElement#setShadowSize(int)
         */
        public void setShadowSize(int size) {
            super.setShadowSizeFriend(size);
        }
        

        /**
         * The UID.
         */
        private static final long serialVersionUID = 4999132551417131227L;


    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 3440806802523267746L;
}
