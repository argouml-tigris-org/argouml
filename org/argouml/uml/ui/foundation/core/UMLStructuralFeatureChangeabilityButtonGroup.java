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

// $header$
package org.argouml.uml.ui.foundation.core;

import java.awt.event.ActionEvent;
import javax.swing.JRadioButton;

import org.argouml.application.api.Argo;
import org.argouml.uml.ui.UMLButtonGroup;
import org.argouml.uml.ui.UMLUserInterfaceContainer;
import ru.novosoft.uml.foundation.core.MFeature;
import ru.novosoft.uml.foundation.core.MStructuralFeature;
import ru.novosoft.uml.foundation.data_types.MChangeableKind;

/**
 * @since Nov 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLStructuralFeatureChangeabilityButtonGroup
    extends UMLButtonGroup {
        
    private JRadioButton _changeableButton = 
        new JRadioButton(Argo.localize("UMLMenu", "checkbox.changeability.changeable"));
    private JRadioButton _frozenButton =
        new JRadioButton(Argo.localize("UMLMenu", "checkbox.changeability.frozen"));
    private JRadioButton _addOnlyButton =
        new JRadioButton(Argo.localize("UMLMenu", "checkbox.changeability.add-only"));

    /**
     * Constructor for UMLStructuralFeatureChangeabilityButtonGroup.
     * @param container
     */
    public UMLStructuralFeatureChangeabilityButtonGroup(UMLUserInterfaceContainer container) {
        super(container);
        getAddOnlyButton().addActionListener(this);
        getChangeableButton().addActionListener(this);
        getFrozenButton().addActionListener(this);
    }

    /**
     * @see org.argouml.uml.ui.UMLButtonGroup#buildModel()
     */
    public void buildModel() {
    }
    
    protected void setActions() {
    }

    /**
     * Returns the addOnlyButton.
     * @return JRadioButton
     */
    public JRadioButton getAddOnlyButton() {
        return _addOnlyButton;
    }

    /**
     * Returns the changeableButton.
     * @return JRadioButton
     */
    public JRadioButton getChangeableButton() {
        return _changeableButton;
    }

    /**
     * Returns the frozenButton.
     * @return JRadioButton
     */
    public JRadioButton getFrozenButton() {
        return _frozenButton;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        MStructuralFeature target = (MStructuralFeature)getTarget();
        if (target != null) {
            if (e.getSource() == getAddOnlyButton())
                target.setChangeability(MChangeableKind.ADD_ONLY);
            else
            if (e.getSource() == getChangeableButton())
                target.setChangeability(MChangeableKind.CHANGEABLE);
            if (e.getSource() == getFrozenButton())
                target.setChangeability(MChangeableKind.FROZEN);
        }
    }


}
