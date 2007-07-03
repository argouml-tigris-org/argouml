// $Id: UMLClassOperationListModel.java 12801 2007-06-09 15:49:57Z mvw $
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

package org.argouml.uml.ui.foundation.core;

import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;

/**
 *
 * @author jaap.branderhorst@xs4all.nl, alexb
 * @since Mar 22, 2003
 */
public class UMLClassOperationListModel
    extends UMLModelElementOrderedListModel2 {

    /**
     * Constructor for UMLClassifierFeatureListModel.
     */
    public UMLClassOperationListModel() {
        super("feature");
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        if (getTarget() != null) {
            List opsAndReceps =
                    Model.getFacade().getOperationsAndReceptions(getTarget());
            setAllElements(opsAndReceps);
        }
    }


    /*
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object element) {
        return (Model.getFacade().getOperationsAndReceptions(getTarget())
                .contains(element));
    }

    /*
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveDown(int)
     */
    protected void moveDown(int index1) {
        int index2 = index1 + 1;
        Object clss = getTarget();
        List c = Model.getFacade().getOperationsAndReceptions(clss);
        if (index1 < c.size() - 1) {
            Object op1 = c.get(index1);
            Object op2 = c.get(index2);
            List f = Model.getFacade().getFeatures(clss);
            index2 = f.indexOf(op2);
            Model.getCoreHelper().removeFeature(clss, op1);
            Model.getCoreHelper().addFeature(clss, index2, op1);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToBottom(int)
     */
    @Override
    protected void moveToBottom(int index) {
        Object clss = getTarget();
        List c = Model.getFacade().getOperationsAndReceptions(clss);
        if (index < c.size() - 1) {
            Object mem1 = c.get(index);
            Model.getCoreHelper().removeFeature(clss, mem1);
            Model.getCoreHelper().addFeature(clss, c.size() - 1, mem1);
        }
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveToTop(int)
     */
    @Override
    protected void moveToTop(int index) {
        Object clss = getTarget();
        List c = Model.getFacade().getOperationsAndReceptions(clss);
        if (index > 0) {
            Object mem1 = c.get(index);
            Model.getCoreHelper().removeFeature(clss, mem1);
            Model.getCoreHelper().addFeature(clss, 0, mem1);
        }
    }


}
