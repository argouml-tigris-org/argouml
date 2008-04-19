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

package org.argouml.uml.ui.behavior.use_cases;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 * @since Oct 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public class ActionAddExtendExtensionPoint
    extends AbstractActionAddModelElement2 {

    private static final ActionAddExtendExtensionPoint SINGLETON =
        new ActionAddExtendExtensionPoint();
    /**
     * Constructor for ActionAddExtendExtensionPoint.
     */
    protected ActionAddExtendExtensionPoint() {
        super();
    }


    @Override
    protected void doIt(Collection selected) {
        Model.getUseCasesHelper().setExtensionPoints(getTarget(), selected);
    }


    protected List getChoices() {
        List ret = new ArrayList();
        if (getTarget() != null) {
            Object extend = /*(MExtend)*/getTarget();
            Collection c = Model.getFacade().getExtensionPoints(
                    Model.getFacade().getBase(extend));
            ret.addAll(c);
        }
        return ret;
    }


    protected String getDialogTitle() {
        return Translator.localize(
                "dialog.title.add-extensionpoints");
    }


    protected List getSelected() {
        List ret = new ArrayList();
        ret.addAll(Model.getFacade().getExtensionPoints(getTarget()));
        return ret;
    }

    /**
     * @return Returns the SINGLETON.
     */
    public static ActionAddExtendExtensionPoint getInstance() {
        return SINGLETON;
    }

}
