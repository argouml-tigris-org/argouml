// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.diagram.activity.ui;

import java.beans.PropertyChangeEvent;

import org.argouml.model.AssociationChangeEvent;
import org.argouml.model.AttributeChangeEvent;
import org.argouml.model.Model;
import org.argouml.notation.NotationProviderFactory2;
import org.tigris.gef.base.Selection;
import org.tigris.gef.graph.GraphModel;


/**
 * Class to display graphics for a UML CallState in a diagram.
 * The UML 1.3 standard does not contain a description of CallState
 * in the Notation Guide chapters. The later UML versions correct this omission.
 * So, for UML 1.3 it looks the same as an ActionState.
 * The only difference with an ActionState is
 * the extra Well-Formedness rule for a CallState.
 *
 * @author MVW
 */
public class FigCallState extends FigActionState {

    ////////////////////////////////////////////////////////////////
    // constructors

    /**
     * Main Constructor FigCallState (called from file loading)
     */
    public FigCallState() {
        super();
    }

    /**
     * Constructor FigCallState that hooks the Fig into
     * an existing UML model element
     * @param gm ignored!
     * @param node owner, i.e. the UML element
     */
    public FigCallState(GraphModel gm, Object node) {
        this();
        setOwner(node);
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigStateVertex#initNotationProviders(java.lang.Object)
     */
    protected void initNotationProviders(Object own) {
        super.initNotationProviders(own);
        if (Model.getFacade().isACallState(own)) {
            notationProvider =
                NotationProviderFactory2.getInstance().getNotationProvider(
                    NotationProviderFactory2.TYPE_CALLSTATE, own);
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#modelChanged(java.beans.PropertyChangeEvent)
     */
    protected void modelChanged(PropertyChangeEvent mee) {
        super.modelChanged(mee);
        if (mee instanceof AssociationChangeEvent
                || mee instanceof AttributeChangeEvent) {
            renderingChanged();
            updateListeners(getOwner(), getOwner());
            damage();
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#updateListeners(java.lang.Object)
     */
    protected void updateListeners(Object oldOwner, Object newOwner) {
        if (oldOwner != null) {
            removeAllElementListeners();
        }

        if (newOwner != null) {
            // register for events from all modelelements
            // that change the name and body text
            // i.e. when the CallAction is replaced:
            addElementListener(newOwner, new String[] {"entry", "name"});
            Object entryAction = Model.getFacade().getEntry(newOwner);
            if (Model.getFacade().isACallAction(entryAction)) {
                // and when the Operation is replaced:
                addElementListener(entryAction, "operation");
                Object operation = Model.getFacade().getOperation(entryAction);
                if (operation != null) {
                    // and when the owner is replaced (unlikely for operations),
                    // and when the operation changes name:
                    addElementListener(operation,
                            new String[] {"owner", "name"});
                    Object classifier = Model.getFacade().getOwner(operation);
                    // and when the class changes name:
                    addElementListener(classifier, "name");
                }
            }
        }
    }

    /**
     * @see org.argouml.uml.diagram.ui.FigNodeModelElement#placeString()
     */
    public String placeString() {
        return "new CallState";
    }

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        FigCallState figClone = (FigCallState) super.clone();
        return figClone;
    }

    /**
     * @see org.argouml.uml.diagram.state.ui.FigStateVertex#makeSelection()
     */
    public Selection makeSelection() {
        return new SelectionCallState(this);
    }

} /* end class FigCallState */
