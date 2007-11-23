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

package org.argouml.uml.ui;

import java.util.Vector;

import javax.swing.Action;

import org.argouml.i18n.Translator;

/**
 * Abstract action that is the parent to all add actions that add the
 * modelelements via the UMLAddDialog.  This is the original form the API which
 * being preserved just long enough to migrate everyone to the List-based API.
 * 
 * @since Oct 2, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @deprecated for 0.25.4 by tfmorris. Use
 *             {@link AbstractActionAddModelElement2}.
 */
public abstract class AbstractActionAddModelElement extends
        AbstractActionAddModelElement2 {

    /**
     * Construct an action to add a model element to some list.
     * 
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link AbstractActionAddModelElement2#AbstractActionAddModelElement2()}.
     */
    @Deprecated
    protected AbstractActionAddModelElement() {
        super(Translator.localize("menu.popup.add-modelelement"), null);
        // Set the tooltip string:
        putValue(Action.SHORT_DESCRIPTION, 
                Translator.localize("menu.popup.add-modelelement"));
    }


    /**
     * Returns the choices the user has in the UMLAddDialog. The choices are
     * depicted on the left side of the UMLAddDialog (sorry Arabic users) and
     * can be moved via the buttons on the dialog to the right side. On the
     * right side are the selected modelelements.
     * 
     * @return Vector
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link AbstractActionAddModelElement2#getChoices()}.
     */
    @Deprecated
    protected abstract Vector getChoices();

    /**
     * The modelelements already selected BEFORE the dialog is shown.
     * @return Vector
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link AbstractActionAddModelElement2#getSelected()}.
     */
    @Deprecated
    protected abstract Vector getSelected();

    /**
     * The action that has to be done by ArgoUml after the user clicks ok in the
     * UMLAddDialog.
     * @param selected The choices the user has selected in the UMLAddDialog
     * @deprecated for 0.25.4 by tfmorris. Use
     *             {@link AbstractActionAddModelElement2#doIt(java.util.List)}.
     */
    @Deprecated
    protected abstract void doIt(Vector selected);


}
