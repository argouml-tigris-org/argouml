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

package org.argouml.uml.ui.behavior.state_machines;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 * Provide a dialog which helps the user to select one event
 * out of an existing list,
 * which will be used as the trigger of the transition.
 *
 * @author MarkusK
 *
 */
class ActionAddSignalsToSignalEvent extends AbstractActionAddModelElement2 {
    /**
     * The one and only instance of this class.
     */
    public static final ActionAddSignalsToSignalEvent SINGLETON =
        new ActionAddSignalsToSignalEvent();

    /**
     * Constructor for ActionAddClassifierRoleBase.
     */
    protected ActionAddSignalsToSignalEvent() {
        super();
        setMultiSelect(false);
    }


    protected List getChoices() {
        List vec = new ArrayList();

        vec.addAll(Model.getModelManagementHelper().getAllModelElementsOfKind(
                Model.getFacade().getModel(getTarget()),
                Model.getMetaTypes().getSignal()));

        return vec;
    }


    protected List getSelected() {
        List vec = new ArrayList();
        Object signal = Model.getFacade().getSignal(getTarget());
        if (signal != null) {
            vec.add(signal);
        }
        return vec;
    }


    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-signal");
    }


    @Override
    protected void doIt(Collection selected) {
        Object event = getTarget();
        if (selected == null || selected.size() == 0) {
            Model.getCommonBehaviorHelper().setSignal(event, null);
        } else {
            Model.getCommonBehaviorHelper().setSignal(event,
                    selected.iterator().next());
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 6890869588365483936L;
}
