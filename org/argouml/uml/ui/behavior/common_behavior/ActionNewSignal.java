package org.argouml.uml.ui.behavior.common_behavior;

import java.awt.event.ActionEvent;

import javax.swing.Action;

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