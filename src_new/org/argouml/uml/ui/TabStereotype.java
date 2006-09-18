// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.ui;

import org.argouml.application.api.Configuration;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.DualListBox;
import org.argouml.uml.ui.foundation.core.UMLModelElementStereotypeListModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.swidgets.Horizontal;
import org.tigris.swidgets.Vertical;

/**
 * This the tab in the details pane for displaying the stereotypes applied to a
 * model element and allowing adding and removal of stereotypes to that list.<p>
 */
public class TabStereotype extends PropPanel {

    private static String orientation = Configuration.getString(Configuration
            .makeKey("layout", "tabstereotype"));
    
    private Object target;

    private UMLModelElementStereotypeListModel stereoListModel;
    
    private DualListBox dualListBox;

    /**
     * Construct new documentation tab
     */
    public TabStereotype() {
        super(Translator.localize("tab.stereotype"), (orientation
                .equals("West") || orientation.equals("East")) ? Vertical
                .getInstance() : Horizontal.getInstance());

        stereoListModel = new UMLModelElementStereotypeListModel();
        dualListBox = new DualListBox();
        //dualListBox.getSourceListModel().
        
        add(new ScrollList(stereoListModel));        
        add(dualListBox);

    }

    /**
     * Checks if the tab should be enabled. Returns true if the target
     * returned by getTarget is a modelelement or if that target shows up as Fig
     * on the active diagram and has a modelelement as owner.
     *
     * @return true if this tab should be enabled, otherwise false.
     */
    public boolean shouldBeEnabled() {
        Object target = getTarget();
        target = (target instanceof Fig) ? ((Fig) target).getOwner() : target;
        return Model.getFacade().isAModelElement(target);
    }

    /**
     * @see org.argouml.ui.TabTarget#setTarget(java.lang.Object)
     */
    public void setTarget(Object theTarget) {

        Object t = (theTarget instanceof Fig)
                    ? ((Fig) theTarget).getOwner() : theTarget;
        if (!(Model.getFacade().isAModelElement(t))) {
            target = null;
            return;
        }
        target = t;
        
        stereoListModel.setTarget(t);

        validate();
    }
}