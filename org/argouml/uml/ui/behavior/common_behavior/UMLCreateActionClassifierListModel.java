// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

package org.argouml.uml.ui.behavior.common_behavior;

import java.util.Iterator;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementListModel2;

public class UMLCreateActionClassifierListModel extends
        UMLModelElementListModel2 {

    /**
     * Constructor for ClassifierCreateActionListModel.
     */
    public UMLCreateActionClassifierListModel() {
        super("classifier");
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        removeAllElements();
        if (Model.getCommonBehaviorHelper().getInstantiation(getTarget()) == null) {
            Object classifier = getClassifierReceiver();
            if (classifier != null)
                Model.getCommonBehaviorHelper().setInstantiation(getTarget(),
                        classifier);
        }

        addElement(Model.getCommonBehaviorHelper()
                .getInstantiation(getTarget()));
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(java.lang.Object)
     */
    protected boolean isValidElement(Object elem) {
        return Model.getFacade().isAClassifier(elem)
                && Model.getCommonBehaviorHelper()
                        .getInstantiation(getTarget()) == elem;
    }

    private Object getClassifierReceiver() {

        Object me = Model.getCommonBehaviorHelper().getActionOwner(getTarget());

        if ((Model.getFacade().isAMessage(me))
                || (Model.getFacade().isAStimulus(me))) {
            Object receiver = Model.getFacade().getReceiver(me);
            if (Model.getFacade().isAInstance(receiver)) {
                Iterator classifier = Model.getFacade()
                        .getClassifiers(receiver).iterator();
                if (classifier.hasNext()) {
                    return classifier.next();
                }
            } else {
                if (Model.getFacade().isAClassifierRole(receiver)) {
                    Iterator classifier = Model.getFacade().getBases(receiver)
                            .iterator();
                    if (classifier.hasNext()) {
                        return classifier.next();
                    }
                }
            }
        }
        return null;
    }
}
