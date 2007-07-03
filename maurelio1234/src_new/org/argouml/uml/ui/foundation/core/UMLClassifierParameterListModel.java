// $Id: UMLClassifierParameterListModel.java 12801 2007-06-09 15:49:57Z mvw $
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

import java.util.Collection;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;

/**
 * This is the model for the list of parameters for a classifier,
 * as e.g. present on the operation properties panel. <p>
 *
 * This is an ordered list, and hence it supports reordering functions.
 *
 * @author jaap.branderhorst@xs4all.nl
 * @since Jan 26, 2003
 */
public class UMLClassifierParameterListModel
    extends UMLModelElementOrderedListModel2 {

    /**
     * Constructor for UMLClassifierParameterListModel.
     * This is an ordered list (2nd parameter = true).
     */
    public UMLClassifierParameterListModel() {
        super("parameter");
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            setAllElements(Model.getFacade().getParameters(getTarget()));
        }
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return Model.getFacade().getParameters(getTarget()).contains(element);
    }


    /*
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveDown(int)
     */
    protected void moveDown(int index) {
        Object clss = getTarget();
        Collection c = Model.getFacade().getParameters(clss);
        if (c instanceof List && index < c.size() - 1) {
            Object mem = ((List) c).get(index);
            Model.getCoreHelper().removeParameter(clss, mem);
            Model.getCoreHelper().addParameter(clss, index + 1, mem);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToBottom(int)
     */
    @Override
    protected void moveToBottom(int index) {
        Object clss = getTarget();
        Collection c = Model.getFacade().getParameters(clss);
        if (c instanceof List && index < c.size() - 1) {
            Object mem = ((List) c).get(index);
            Model.getCoreHelper().removeParameter(clss, mem);
            Model.getCoreHelper().addParameter(clss, c.size() - 1, mem);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToTop(int)
     */
    @Override
    protected void moveToTop(int index) {
        Object clss = getTarget();
        Collection c = Model.getFacade().getParameters(clss);
        if (c instanceof List && index > 0) {
            Object mem = ((List) c).get(index);
            Model.getCoreHelper().removeParameter(clss, mem);
            Model.getCoreHelper().addParameter(clss, 0, mem);
        }
    }

}
