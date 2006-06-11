// $Id$
// Copyright (c) 2005-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * Create a new Signal.
 */
public class ActionNewSignal extends AbstractActionNewModelElement {

    /**
     * The constructor.
     */
    public ActionNewSignal() {
        super("button.new-signal");
        putValue(Action.NAME, Translator.localize("button.new-signal"));
        Icon icon = ResourceLoaderWrapper.lookupIcon("SignalSending");
        putValue(Action.SMALL_ICON, icon);
    }

    /**
     * Creates a new signal and in case of a SignalEvent as target also set the
     * Signal for this event.
     *
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object target = TargetManager.getInstance().getModelTarget();
        if (Model.getFacade().isASignal(target)
                || Model.getFacade().isASignalEvent(target)) {
            Object ns = Model.getFacade().getNamespace(target);
            if (ns != null) {
                Object newSig = Model.getCommonBehaviorFactory().createSignal();
                Model.getCoreHelper().addOwnedElement(ns, newSig);
                if (Model.getFacade().isASignalEvent(target)) {
                    Model.getCommonBehaviorHelper().setSignal(target, newSig);
                }
                TargetManager.getInstance().setTarget(newSig);
                super.actionPerformed(e);
            }
        }
    }
}
