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
**/
public class ShadowComboBox extends JComboBox {

    private static ShadowFig[]  _shadowFigs = null;

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
    **/
    private class ShadowRenderer
	extends JComponent
	implements ListCellRenderer {

        protected ShadowFig  _currentFig = null;
        
        public ShadowRenderer() {
            super();
        }
        
        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {  
                
            if (_shadowFigs == null) {
                _shadowFigs = new ShadowFig[ShadowComboBox.this.getItemCount()];

                for (int i = 0; i < _shadowFigs.length; ++i) {
                    _shadowFigs[i] = new ShadowFig();
                    _shadowFigs[i].setShadowSize(i);                    
                    _shadowFigs[i].getNameFig().setText(
                        (String) ShadowComboBox.this.getItemAt(i));
                }
            }

            if (isSelected) {
                setBackground(list.getSelectionBackground());
            }
            else {
                setBackground(list.getBackground());
            }
            
            int figIndex = index;
            if (figIndex < 0) {
                for (int i = 0; i < _shadowFigs.length; ++i) {
                    if (value == ShadowComboBox.this.getItemAt(i)) {
                        figIndex = i;
                    }
                }
            }

            if (figIndex >= 0) {
                _currentFig = _shadowFigs[figIndex];
                setPreferredSize(new Dimension(
                    _currentFig.getWidth() + figIndex + 4,
                    _currentFig.getHeight() + figIndex + 2));
            }
            else {
                _currentFig = null;
            }

            return this;
        }
        
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
            if (_currentFig != null) {
                _currentFig.setLocation(2, 1);
                _currentFig.paint(g);
            }
        }
    }
    
    private class ShadowFig extends FigNodeModelElement {
        public ShadowFig() {
            super();
            addFig(getBigPort());
            addFig(getNameFig());
        }
    }
}
