// $Id$
// Copyright (c) 1996-2006 The Regents of the University of California. All
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

package org.argouml.uml.ui.foundation.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;

/**
 * The list model for the connections of the association. <p>
 *
 * The specialty of this list model, is that it need to be aware
 * of name changes in the ends connected to the association.
 * Most other listmodels only listen to
 * model changes in one UML element, but in this case, we also have to listen to
 * changes in some related elements. <p>
 *
 * Why is this case different: because it is possible to modify the
 * associationend names on the diagram, without changing the target - i.e.
 * the target remains the association throughout this modification.
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 4, 2003
 */
public class UMLAssociationConnectionListModel
    extends UMLModelElementOrderedListModel2 {

    private Collection others;

    /**
     * Constructor for UMLModelElementClientDependencyListModel.
     */
    public UMLAssociationConnectionListModel() {
        super("connection");
    }

     /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#addOtherModelEventListeners(java.lang.Object)
     */
    protected void addOtherModelEventListeners(Object newTarget) {
        super.addOtherModelEventListeners(newTarget);
        /* Make a copy of the modelelements: */
        others = new ArrayList(Model.getFacade().getConnections(newTarget));
        Iterator i = others.iterator();
        while (i.hasNext()) {
            Object end = i.next();
            Model.getPump().addModelEventListener(this, end, "name");
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#removeOtherModelEventListeners(java.lang.Object)
     */
    protected void removeOtherModelEventListeners(Object oldTarget) {
        super.removeOtherModelEventListeners(oldTarget);
        Iterator i = others.iterator();
        while (i.hasNext()) {
            Object end = i.next();
            Model.getPump().removeModelEventListener(this, end, "name");
        }
        others.clear();
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(Model.getFacade().getConnections(getTarget()));
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object o) {
        return Model.getFacade().isAAssociationEnd(o)
            && Model.getFacade().getConnections(getTarget()).contains(o);
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveDown(int)
     */
    protected void moveDown(int index) {
        Object assoc = getTarget();
        /* Since we are UML 1.4, this is surely a List! */
        List c = (List) Model.getFacade().getConnections(assoc);
        if (index < c.size() - 1) {
            Object mem = c.get(index);
            Model.getCoreHelper().removeConnection(assoc, mem);
            Model.getCoreHelper().addConnection(assoc, index + 1, mem);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToBottom(int)
     */
    @Override
    protected void moveToBottom(int index) {
        Object assoc = getTarget();
        /* Since we are UML 1.4, this is surely a List! */
        List c = (List) Model.getFacade().getConnections(assoc);
        if (index < c.size() - 1) {
            Object mem1 = c.get(index);
            Model.getCoreHelper().removeConnection(assoc, mem1);
            Model.getCoreHelper().addConnection(assoc, c.size() - 1, mem1);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToTop(int)
     */
    @Override
    protected void moveToTop(int index) {
        Object assoc = getTarget();
        /* Since we are UML 1.4, this is surely a List! */
        List c = (List) Model.getFacade().getConnections(assoc);
        if (index > 0) {
            Object mem1 = c.get(index);
            Model.getCoreHelper().removeConnection(assoc, mem1);
            Model.getCoreHelper().addConnection(assoc, 0, mem1);
        }
    }
}
