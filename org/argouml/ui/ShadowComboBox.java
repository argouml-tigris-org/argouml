// $Id$
// Copyright (c) 1996-2003 The Regents of the University of California. All
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
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.argouml.application.api.Argo;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.diagram.static_structure.ui.FigComment;

/**
 * A ComboBox that contains the set of possible Shadow Width values.
 * 
 * @author Jeremy Jones
**/
public class ShadowComboBox extends JComboBox {

    private static final String BUNDLE = "Cognitive";

    public ShadowComboBox() {
        super();
        
        addItem(Argo.localize(BUNDLE, "stylepane.label.no-shadow"));
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
    protected class ShadowRenderer extends JComponent implements ListCellRenderer {        
        protected FigComment[]   _shadowFigs;
        protected FigComment     _currentFig;
        
        public ShadowRenderer() {
            _shadowFigs = null;
            _currentFig = null;
        }
        
        public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {  
                
            if (isSelected) {
                setBackground(list.getSelectionBackground());
            }
            else {
                setBackground(list.getBackground());
            }
            
            if (_shadowFigs == null) {
                FontMetrics fm = getFontMetrics(FigComment.LABEL_FONT);
                int textWidth = fm.stringWidth((String) ShadowComboBox.this.getItemAt(0));
                int textHeight = fm.getHeight();

                _shadowFigs = new FigComment[ShadowComboBox.this.getItemCount()];
                for (int i = 0; i < _shadowFigs.length; ++i) {
                    _shadowFigs[i] = new FigComment();
                    _shadowFigs[i].setSize(textWidth + 10, textHeight + 2);
                    _shadowFigs[i].setShadowSize(i);                    
                    _shadowFigs[i].setOwner(
                        UmlFactory.getFactory().getCore().createComment());
                    _shadowFigs[i].storeNote(
                        (String) ShadowComboBox.this.getItemAt(i));
                }
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
}
