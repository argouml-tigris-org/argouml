/* $Id$
 *****************************************************************************
 * Copyright (c) 2009-2011 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Tom Morris
 *    Bob Tarling
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 1996-2008 The Regents of the University of California. All
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

import java.awt.Component;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.UIManager;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.InvalidElementException;
import org.argouml.model.Model;

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

    /**
     * True if the icon for the modelelement should be shown. The icon is, for
     * instance, a small class symbol for a class.
     */
    private boolean showIcon;

    /**
     * True if the containment path should be shown 
     * (to help the user disambiguate elements with the same name);
     */
    private boolean showPath;

    /**
     * Constructor for UMLListCellRenderer2.
     *
     * @param showTheIcon true if the list should show icons
     */
    public UMLListCellRenderer2(boolean showTheIcon) {
        this(showTheIcon, true);
    }
    
    /**
     * Constructor for UMLListCellRenderer2.
     *
     * @param showTheIcon true if the list should show icons
     * @param showThePath true if the list should show containment path
     */
    public UMLListCellRenderer2(boolean showTheIcon, boolean showThePath) {

        // only need to this from super()
        updateUI();
        setAlignmentX(LEFT_ALIGNMENT);

        showIcon = showTheIcon;
        showPath = showThePath;
    }

    /*
     * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList,
     *      java.lang.Object, int, boolean, boolean)
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus) {
        // Leave logging commented out by default for efficiency
//        LOG.debug("determine rendering for: " + value);
//        LOG.debug("show icon: " + showIcon);
        if (Model.getFacade().isAUMLElement(value)) {

//            LOG.debug("is a Base or Multiplicity");
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
                // hack to make sure that the right height is
                // applied when no icon is used.
                return super.getListCellRendererComponent(list, text, index,
                        isSelected, cellHasFocus);
            }

        } else if (value instanceof String) {
            return super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);
        } else if (value == null || value.equals("")) {
            JLabel label = new JLabel("<none>");
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
        if (value instanceof String) {
            return (String) value;
        }
        String name = null;
        if (Model.getFacade().isAParameter(value)) {
            Object type = Model.getFacade().getType(value);
            name = getName(value);
            String typeName = null;
            if (type != null) {
                typeName = Model.getFacade().getName(type);
            }
            if (typeName != null || "".equals(typeName)) {
                name = Translator.localize(
                        "misc.name.withType",
                        new Object[] {name, typeName});
            }
            return name;
        }
        if (Model.getFacade().isAUMLElement(value)) {
            try {
                name = getName(value);
                if (Model.getFacade().isAStereotype(value)) {
                    String baseString = "";
                    Iterator bases =
                            Model.getFacade().getBaseClasses(value).iterator();
                    if (bases.hasNext()) {
                        baseString = makeText(bases.next());
                        while (bases.hasNext()) {
                            baseString = Translator.localize(
                                    "misc.name.baseClassSeparator",
                                    new Object[] {baseString, 
                                                  makeText(bases.next())
                                    }
                            );
                        }
                    }
                    name = Translator.localize(
                            "misc.name.withBaseClasses",
                            new Object[] {name, baseString});
                } else if (showPath) {
                    List pathList =
                            Model.getModelManagementHelper().getPathList(value);
                    String path;
                    if (pathList.size() > 1) {
                        path = (String) pathList.get(0);
                        for (int i = 1; i < pathList.size() - 1; i++) {
                            String n = (String) pathList.get(i);
                            path = Translator.localize(
                                            "misc.name.pathSeparator",
                                            new Object[] {path, n});
                        }
                        name = Translator.localize(
                                        "misc.name.withPath",
                                        new Object[] {name, path});
                    }
                }
            } catch (InvalidElementException e) {
                name = Translator.localize("misc.name.deleted");
            }
        } else if (Model.getFacade().isAMultiplicity(value)) {
            name = Model.getFacade().getName(value);
        } else {
            name = makeTypeName(value);
        }
        return name;

    }

    private String getName(Object value) {
        String name = null;
        if (Model.getFacade().isANamedElement(value)) {
            name = Model.getFacade().getName(value);
        } else {
            // TODO: Bob says - if the model element is not named we could
            // generate a name. e.g. a generalization becomes "Class A -> Class B"
            name = null;
        }
        if (name == null || name.equals("")) {
            name = Translator.localize(
                            "misc.name.unnamed",
                            new Object[] {makeTypeName(value)});
        }
        return name;
    }

    private String makeTypeName(Object elem) {
        if (Model.getFacade().isAUMLElement(elem)) {
            return Model.getFacade().getUMLClassName(elem);
        }
        return null;
    }

}
