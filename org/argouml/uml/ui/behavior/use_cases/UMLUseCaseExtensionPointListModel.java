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

package org.argouml.uml.ui.behavior.use_cases;

import java.util.ArrayList;
import java.util.List;

import org.argouml.model.Model;
import org.argouml.uml.ui.UMLModelElementOrderedListModel2;

/**
 * @since Oct 7, 2002
 * @author jaap.branderhorst@xs4all.nl
 */
public class UMLUseCaseExtensionPointListModel
    extends UMLModelElementOrderedListModel2 {

    /**
     * Constructor for UMLUseCaseExtensionPointListModel.
     */
    public UMLUseCaseExtensionPointListModel() {
        super("extensionPoint");
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#buildModelList()
     */
    protected void buildModelList() {
        setAllElements(Model.getFacade().getExtensionPoints(getTarget()));
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel2#isValidElement(Object)
     */
    protected boolean isValidElement(Object/*MBase*/ o) {
        return Model.getFacade().getExtensionPoints(getTarget()).contains(o);
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementOrderedListModel2#moveDown(int)
     */
    protected void moveDown(int index1) {
        int index2 = index1 + 1;
        Object usecase = getTarget();
        List c = new ArrayList(Model.getFacade().getExtensionPoints(usecase));
        Object mem1 = c.get(index1);
        Object mem2 = c.get(index2);
        List cc = new ArrayList(c);
        cc.remove(mem1);
        cc.remove(mem2);
        Model.getUseCasesHelper().setExtensionPoints(usecase, cc);
        c.set(index1, mem2);
        c.set(index2, mem1);
        Model.getUseCasesHelper().setExtensionPoints(usecase, c);
        buildModelList();
    }
}
