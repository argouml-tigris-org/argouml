// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.state_machines;

import javax.swing.Icon;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetEvent;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.util.ConfigLoader;

/**
 * Property Panel for the collection of pseudostates (branch, fork, ...). It
 * dynamically sets its name to the pseudostate used.
 */
public class PropPanelPseudostate extends PropPanelStateVertex {

    /**
     * The serial version.
     */
    private static final long serialVersionUID = 5822284822242536007L;

    /**
     * Construct a new property panel for a PseudoState (branch, fork, etc).
     */
    public PropPanelPseudostate() {
        super("Pseudostate", null, ConfigLoader.getTabPropsOrientation());

        addField(Translator.localize("label.name"),
                getNameTextField());
        addField(Translator.localize("label.container"),
                getContainerScroll());

        addSeparator();

        addField(Translator.localize("label.incoming"),
                getIncomingScroll());
        addField(Translator.localize("label.outgoing"),
                getOutgoingScroll());

        TargetManager.getInstance().addTargetListener(this);
    }

    /**
     * This method is responsible for setting the title of the proppanel
     * according to the type of the pseudo state displayed in the property
     * panel. This is required as pseudostates share a common class and are
     * distinguished only by an attribute (pseudostatekind).
     */
    public void refreshTarget() {
        Object target = TargetManager.getInstance().getModelTarget();
        if (Model.getFacade().isAPseudostate(target)) {
            Object kind = Model.getFacade().getKind(target);
            if (Model.getFacade().equalsPseudostateKind(kind,
                Model.getPseudostateKind().getFork())) {
                getTitleLabel().setText(
                    Translator.localize("label.pseudostate.fork"));
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                Model.getPseudostateKind().getJoin())) {
                getTitleLabel().setText(
                    Translator.localize("label.pseudostate.join"));
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                Model.getPseudostateKind().getChoice())) {
                getTitleLabel().setText(
                    Translator.localize("label.pseudostate.choice"));
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                Model.getPseudostateKind().getDeepHistory())) {
                getTitleLabel().setText(
                    Translator.localize("label.pseudostate.deephistory"));
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                Model.getPseudostateKind().getShallowHistory())) {
                getTitleLabel().setText(
                    Translator.localize("label.pseudostate.shallowhistory"));
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                Model.getPseudostateKind().getInitial())) {
                getTitleLabel().setText(
                    Translator.localize("label.pseudostate.initial"));
            }
            if (Model.getFacade().equalsPseudostateKind(kind,
                Model.getPseudostateKind().getJunction())) {
                getTitleLabel().setText(
                    Translator.localize("label.pseudostate.junction"));
            }
            Icon icon =
                ResourceLoaderWrapper.getInstance().lookupIcon(target);
            if (icon != null) {
                getTitleLabel().setIcon(icon);
            }
        }

    }

    /*
     * @see org.argouml.uml.ui.PropPanel#targetAdded(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetAdded(TargetEvent e) {
        if (Model.getFacade().isAPseudostate(e.getNewTarget())) {
            refreshTarget();
            super.targetAdded(e);
        }
    }

    /*
     * @see org.argouml.uml.ui.PropPanel#targetRemoved(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetRemoved(TargetEvent e) {
        if (Model.getFacade().isAPseudostate(e.getNewTarget())) {
            refreshTarget();
            super.targetRemoved(e);
        }
    }

    /*
     * @see org.argouml.uml.ui.PropPanel#targetSet(org.argouml.ui.targetmanager.TargetEvent)
     */
    public void targetSet(TargetEvent e) {
        if (Model.getFacade().isAPseudostate(e.getNewTarget())) {
            refreshTarget();
            super.targetSet(e);
        }
    }

}
