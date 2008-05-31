// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.argouml.i18n.Translator;
import org.tigris.swidgets.Orientation;

/**
 * Abstract class for the properties panel of a Feature.
 *
 * @since Nov 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class PropPanelFeature extends PropPanelModelElement {

    private UMLFeatureOwnerScopeCheckBox ownerScopeCheckbox;

    private JPanel ownerScroll;

    private static UMLFeatureOwnerListModel ownerListModel;

    private JPanel visibilityPanel;

    /**
     * Construct a property panel for a Feature with the given name and icon.
     * 
     * @param name name of property which contains string to use for property
     *                panel name. This will be localized by the super
     *                constructor.
     * @param icon icon
     */
    protected PropPanelFeature(String name, ImageIcon icon) {
        super(name, icon);
    }
    
    /**
     * Constructor.
     * 
     * @param name name
     * @param icon icon
     * @param orientation orientation
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #PropPanelFeature(String, ImageIcon)} and
     *             setOrientation() after instantiation.
     */
    @Deprecated
    protected PropPanelFeature(String name, ImageIcon icon,
            Orientation orientation) {
        super(name, icon, orientation);
    }

    /**
     * Constructor for PropPanelFeature.
     *
     * @param name the name to be shown at the top of the panel
     * @param orientation the orientation of the panel
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link #PropPanelFeature(String, ImageIcon)} and
     *             setOrientation() after instantiation.
     */
    @Deprecated
    protected PropPanelFeature(String name, Orientation orientation) {
        super(name, orientation);
    }

    /**
     * Returns the ownerScroll.
     *
     * @return JScrollPane
     */
    public JPanel getOwnerScroll() {
        if (ownerScroll == null) {
            if (ownerListModel == null) {
                ownerListModel = new UMLFeatureOwnerListModel();
            }
            ownerScroll = getSingleRowScroll(ownerListModel);
        }
        return ownerScroll;
    }

    /**
     * Returns the ownerScopeCheckbox.
     *
     * @return UMLFeatureOwnerScopeCheckBox
     */
    public UMLFeatureOwnerScopeCheckBox getOwnerScopeCheckbox() {
        if (ownerScopeCheckbox == null) {
            ownerScopeCheckbox = new UMLFeatureOwnerScopeCheckBox();
        }
        return ownerScopeCheckbox;
    }

    /**
     * @return the panel for the visibility
     */
    protected JPanel getVisibilityPanel() {
        if (visibilityPanel == null) {
            visibilityPanel =
                new UMLModelElementVisibilityRadioButtonPanel(
                    Translator.localize("label.visibility"), true);
        }
        return visibilityPanel;
    }

}
