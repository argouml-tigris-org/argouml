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

// $Id$
package org.argouml.uml.ui;

import org.argouml.model.ModelFacade;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;

/**
 * The default cell renderer for uml model elements. Used by UMLList2 and its
 * children.
 * 
 * This class must be efficient as it is called many 1000's of times.
 * 
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 2, 2003
 */
public class UMLListCellRenderer2 extends DefaultListCellRenderer {

    private static final Logger LOG = 
        Logger.getLogger(UMLListCellRenderer2.class);

    /**
     * True if the icon for the modelelement should be shown. The icon is, for
     * instance, a small class symbol for a class.
     */
    private boolean showIcon;

    /**
     * Constructor for UMLListCellRenderer2.
     *
     * @param showTheIcon true if the list should show icons
     */
    public UMLListCellRenderer2(boolean showTheIcon) {

        // only need to this from super()
        updateUI();
        setAlignmentX(LEFT_ALIGNMENT);

        showIcon = showTheIcon;
    }

    /**
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
     *      java.lang.Object, int, boolean, boolean)
     */
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        LOG.debug("determine rendering for: " + value);
        LOG.debug("show icon: " + showIcon);
        if (ModelFacade.isABase(value) || ModelFacade.isAMultiplicity(value)) {

            LOG.debug("is a MBase or MMultiplicity");
            String text = makeText(value);
            setText(text);

            if (showIcon) {

                // ----- setup similar to the super() implementation -----
                setComponentOrientation(list.getComponentOrientation());
                if (isSelected) {
                    setForeground(list.getSelectionForeground());
                    setBackground(list.getSelectionBackground());
                } else {
                    setForeground(list.getForeground());
                    setBackground(list.getBackground());
                }

                setEnabled(list.isEnabled());
                setFont(list.getFont());
                setBorder((cellHasFocus) ? UIManager
                        .getBorder("List.focusCellHighlightBorder")
                        : noFocusBorder);
                // --------------------------------------------------------
                setIcon(ResourceLoaderWrapper.getInstance()
                        .lookupIcon(value));
            } else {
                // hack to make sure that the right hight is
                // applied when no icon is used.
                return super.getListCellRendererComponent(list, text, index,
                        isSelected, cellHasFocus);
            }

        } else if (value instanceof String) {
            JLabel label = new JLabel(value.toString());
            return label;
        } else if (value == null || value.equals("")) {
            JLabel label = new JLabel(" ");
            label.setIcon(null);
            return label;
        }

        return this;
    }

    /**
     * Makes the text that must be placed on the label that is returned.
     * If there is no name for the given modelelement, then 
     * (anon xxx) is shown, with xxx the type name.
     * 
     * @param value the given modelelement
     * @return String the text to be shown 
     */
    public String makeText(Object value) {
        String name = null;
        if (ModelFacade.isAModelElement(value)) {
            Object/* MModelElement */elem = value;
            name = ModelFacade.getName(elem);
            if (name == null || name.equals("")) {
                name = "(anon " + makeTypeName(elem) + ")";
            }
        } else if (ModelFacade.isAMultiplicity(value)) {
            name = value.toString();
        } else {
            name = makeTypeName(value);
        }
        return name;

    }

    private String makeTypeName(Object elem) {
        if (org.argouml.model.ModelFacade.isABase(elem)) { 
            return ModelFacade.getUMLClassName(elem); 
        }
        return null;
    }

}