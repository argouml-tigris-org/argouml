// $Id$
// Copyright (c) 2003-2007 The Regents of the University of California. All
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

import java.util.Collection;

import javax.swing.Action;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * navigate to opposite association end.
 *
 * @author mkl
 *
 */
public class ActionNavigateOppositeAssocEnd extends AbstractActionNavigate {

    /**
     * The constructor.
     */
    public ActionNavigateOppositeAssocEnd() {
        super("button.go-opposite", true);
        putValue(Action.SMALL_ICON,
                ResourceLoaderWrapper.lookupIconResource("AssociationEnd"));
    }

    /*
     * @see org.argouml.uml.ui.AbstractActionNavigate#navigateTo(java.lang.Object)
     */
    protected Object navigateTo(Object source) {
        return Model.getFacade().getNextEnd(source);
    }

    /*
     * @see javax.swing.Action#isEnabled()
     */
    public boolean isEnabled() {
        Object o = TargetManager.getInstance().getTarget();
        if (o != null && Model.getFacade().isAAssociationEnd(o)) {
            Collection ascEnds =
                Model.getFacade().getConnections(
                        Model.getFacade().getAssociation(o));
            return !(ascEnds.size() > 2);
        }
        return false;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 7054600929513339932L;
}
