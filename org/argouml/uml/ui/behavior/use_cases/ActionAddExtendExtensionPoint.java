// Copyright (c) 1996-99 The Regents of the University of California. All
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
package org.argouml.uml.ui.behavior.use_cases;

import java.util.Vector;

import org.argouml.application.api.Argo;
import org.argouml.uml.ui.AbstractActionAddModelElement;
import ru.novosoft.uml.behavior.use_cases.MExtend;

/**
 * @since Oct 6, 2002
 * @author jaap.branderhorst@xs4all.nl
 * @stereotype singleton
 */
public class ActionAddExtendExtensionPoint extends AbstractActionAddModelElement {
 
    public final static ActionAddExtendExtensionPoint SINGLETON = new ActionAddExtendExtensionPoint();
    /**
     * Constructor for ActionAddExtendExtensionPoint.
     */
    protected ActionAddExtendExtensionPoint() {
        super();
    }
    
    /**
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#doIt(java.util.Vector)
     */
    protected void doIt(Vector selected) {
        ((MExtend)getTarget()).setExtensionPoints(selected);
    }

    /**
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getChoices()
     */
    protected Vector getChoices() {
        Vector ret = new Vector();
        if (getTarget() != null) {
            ret.addAll(((MExtend)getTarget()).getBase().getExtensionPoints());
        }
        return ret;
    }

    /**
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getDialogTitle()
     */
    protected String getDialogTitle() {
        return Argo.localize("UMLMenu", "dialog.title.add-extensionpoints");
    }

    /**
     * @see org.argouml.uml.ui.AbstractActionAddModelElement#getSelected()
     */
    protected Vector getSelected() {
        Vector ret = new Vector();
        ret.addAll(((MExtend)getTarget()).getExtensionPoints());
        return ret;
    }

}
