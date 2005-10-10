package org.argouml.uml.ui.behavior.use_cases;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.ui.AbstractActionNewModelElement;

/**
 * <p>Invoked by the "Add use case" toolbar button to create a new use case
 *   property panel in the same namespace as the current use case.</p>
 *
 * <p>This code uses getFactory and adds the use case explicitly to the
 *   namespace. Extended to actually navigate to the new use case.</p>
 */
public class ActionNewUseCase extends AbstractActionNewModelElement {

    /**
     * The constructor.
     */
    public ActionNewUseCase() {
        super("button.new-usecase");
        putValue(Action.NAME, Translator.localize("button.new-usecase"));
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object target = TargetManager.getInstance().getModelTarget();
        if (Model.getFacade().isAUseCase(target)) {
            Object ns = Model.getFacade().getNamespace(target);
            if (ns != null) {
                Object useCase = Model.getUseCasesFactory()
                    .createUseCase();
                Model.getCoreHelper().addOwnedElement(ns, useCase);
                TargetManager.getInstance().setTarget(useCase);
                super.actionPerformed(e);
            }
        }
    }
}