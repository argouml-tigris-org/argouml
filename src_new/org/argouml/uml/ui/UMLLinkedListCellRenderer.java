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

// $Id$
package org.argouml.uml.ui;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;


/**
 * Renderer for linked lists. Should underline the cell but this does not work
 * yet.
 * @author jaap.branderhorst@xs4all.nl	
 * @since Jan 2, 2003
 */
public class UMLLinkedListCellRenderer extends UMLListCellRenderer2 {

    /**
     * Constructor for UMLLinkedListCellRenderer.
     * @param showIcon
     */
    public UMLLinkedListCellRenderer(boolean showIcon) {
        super(showIcon);
    }

    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel)super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        /*        
        label.setBackground(list.getBackground());
        label.setForeground(list.getForeground());
        label.setBorder(BorderFactory.createEmptyBorder());
        if (isSelected) {
            Font font = label.getFont();
            Map textattributes = font.getAttributes();
            textattributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_EXTRABOLD);
            label.setFont(font.deriveFont(textattributes));            
        } 
        */
        return label;        
    }

}
