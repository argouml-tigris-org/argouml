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

package org.argouml.uml.ui.foundation.core;

import java.awt.event.KeyEvent;
import javax.swing.JRadioButton;

import org.argouml.i18n.Translator;
import org.argouml.model.ModelFacade;
import org.argouml.uml.ui.UMLButtonGroup;
import org.argouml.uml.ui.UMLUserInterfaceContainer;

/**
 * Model for namespace visibility.
 *
 * @since Oct 12, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public abstract class UMLVisibilityButtonGroup extends UMLButtonGroup {
    
    private JRadioButton privateButton = 
        new JRadioButton(Translator.localize(
                "checkbox.visibility.private-lc"));
    private JRadioButton protectedButton =
        new JRadioButton(Translator.localize(
                "checkbox.visibility.protected-lc"));
    private JRadioButton publicButton =
        new JRadioButton(Translator.localize(
                "checkbox.visibility.public-lc"));
        
    /**
     * Constructor for UMLVisibilityButtonGroup.
     * @param container the container for the buttongroup
     */
    public UMLVisibilityButtonGroup(UMLUserInterfaceContainer container) {
        super(container);
        setActions();
        
        publicButton.setSelected(true);
        
        publicButton.setMnemonic(KeyEvent.VK_U);
        protectedButton.setMnemonic(KeyEvent.VK_O);
        privateButton.setMnemonic(KeyEvent.VK_I);
        
        
    }

    /**
     * @see org.argouml.uml.ui.UMLButtonGroup#buildModel()
     */
    public void buildModel() {
        if (ModelFacade.isPrivate(getTarget())) {
            privateButton.setSelected(true);
        } else {
	    if (ModelFacade.isProtected(getTarget())) {
		protectedButton.setSelected(true);
	    } else {
		publicButton.setSelected(true);
            }
        }
    }

    /**
     * Returns the privateButton.
     * @return JRadioButton
     */
    public JRadioButton getPrivateButton() {
        return privateButton;
    }

    /**
     * Returns the protectedButton.
     * @return JRadioButton
     */
    public JRadioButton getProtectedButton() {
        return protectedButton;
    }

    /**
     * Returns the publicButton.
     * @return JRadioButton
     */
    public JRadioButton getPublicButton() {
        return publicButton;
    }
    
    protected abstract void setActions();

}