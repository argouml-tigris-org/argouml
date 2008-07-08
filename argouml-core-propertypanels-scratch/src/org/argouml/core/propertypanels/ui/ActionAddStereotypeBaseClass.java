// $Id$
// Copyright (c) 2008 The Regents of the University of California. All
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

package org.argouml.core.propertypanels.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.argouml.i18n.Translator;
import org.argouml.model.Model;
import org.argouml.uml.ui.AbstractActionAddModelElement2;

/**
 * The Action to add a baseclass to the stereotype.
 *
 * @author Michiel
 */
public class ActionAddStereotypeBaseClass extends AbstractActionAddModelElement2 {

    private List<String> metaClasses;
    
    public ActionAddStereotypeBaseClass() {
        super();
        initMetaClasses();
    }
    
    /**
     * Initialize the meta-classes list. <p>
     * 
     * All this code is necessary to be independent of 
     * model repository implementation, 
     * i.e. to ensure that we have a 
     * sorted list of strings.
     */
    void initMetaClasses() {
        Collection<String> tmpMetaClasses = 
            Model.getCoreHelper().getAllMetatypeNames();
        if (tmpMetaClasses instanceof List) {
            metaClasses = (List<String>) tmpMetaClasses;
        } else {
            metaClasses = new LinkedList<String>(tmpMetaClasses);
        }
        try {
            Collections.sort(metaClasses);
        } catch (UnsupportedOperationException e) {
            // We got passed an unmodifiable List.  Copy it and sort the result
            metaClasses = new LinkedList<String>(tmpMetaClasses);
            Collections.sort(metaClasses);
        }
    }
    
    @Override
    protected List<String> getChoices() {
        return Collections.unmodifiableList(metaClasses);
    }

    @Override
    protected String getDialogTitle() {
        return Translator.localize("dialog.title.add-baseclasses");
    }

    @Override
    protected List<String> getSelected() {
        List<String> result = new ArrayList<String>();
        if (Model.getFacade().isAStereotype(getTarget())) {
            Collection<String> bases = 
                Model.getFacade().getBaseClasses(getTarget());
            result.addAll(bases);
        }
        return result;
    }

    @Override
    protected void doIt(Collection selected) {
        Object stereo = getTarget();
        Set<Object> oldSet = new HashSet<Object>(getSelected());
        Set toBeRemoved = new HashSet<Object>(oldSet);

        for (Object o : selected) {
            if (oldSet.contains(o)) {
                toBeRemoved.remove(o);
            } else {
                Model.getExtensionMechanismsHelper()
                        .addBaseClass(stereo, o);
            }
        }
        for (Object o : toBeRemoved) {
            Model.getExtensionMechanismsHelper().removeBaseClass(stereo, o);
        }
    }
    
}
