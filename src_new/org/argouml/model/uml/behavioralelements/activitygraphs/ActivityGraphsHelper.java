// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

package org.argouml.model.uml.behavioralelements.activitygraphs;

import java.util.Collection;
import java.util.Iterator;

import org.argouml.model.ModelFacade;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesHelper;
import org.argouml.model.uml.modelmanagement.ModelManagementHelper;

import ru.novosoft.uml.behavior.activity_graphs.MObjectFlowState;
import ru.novosoft.uml.behavior.state_machines.MState;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MModelElement;

/**
 * Helper class for UML BehavioralElements::ActivityGraphs Package.
 *
 * Current implementation is a placeholder.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class ActivityGraphsHelper {

    /** Don't allow instantiation.
     */
    private ActivityGraphsHelper() {
    }
    
    /** Singleton instance.
     */
    private static ActivityGraphsHelper singleton =
                   new ActivityGraphsHelper();

    
    /** Singleton instance access method.
     * @return the singleton instance of the helper
     */
    public static ActivityGraphsHelper getHelper() {
        return singleton;
    }
    
    /**
     * Finds the Classifier to which a given ObjectFlowState 
     * refers by its given name. This function may be used for when the user 
     * types the name of a classifier in the diagram, in an ObjectFlowState.
     * 
     * @author MVW
     * @param ofs the given ObjectFlowState
     * @param s   the given String that represents 
     *            the name of the "type" Classifier
     * @return    the found classifier or null
     */
    public Object findClassifierByName(Object ofs, String s) {
        if (!(ofs instanceof MObjectFlowState)) 
            throw new IllegalArgumentException();
        Object cs = ModelFacade.getContainer(ofs); // the composite state
        Object sm = ModelFacade.getStateMachine(cs); // the statemachine
        Object ns = ModelFacade.getContext(sm); // the namespace
        if (!ModelFacade.isANamespace(ns)) 
            ns = ModelFacade.getNamespace(ns);
        if (ns != null) {
            Collection c = ModelManagementHelper.getHelper()
                .getAllModelElementsOfKind(ns, (Class) ModelFacade.CLASSIFIER);
            Iterator i = c.iterator();
            while (i.hasNext()) { 
                Object classifier = i.next();
                String cn = ((MModelElement) classifier).getName();
                if (cn.equals(s))
                    return classifier;
            }
        } else
            throw new IllegalArgumentException();
        return null;
    }
    
    /**
     * Find a state of a Classifier by its name.
     * This routine is used to make the connection between 
     * a ClassifierInState and its State.
     * 
     * @author mvw
     * @param c the Classifier. If this is not a Classifier, then 
     *          IllegalArgumentException is thrown.
     * @param s the string that represents the name of 
     *          the state we are looking for. If "" or null, then 
     *          null is returned straight away.
     * @return  the State (as Object) or null, if not found.
     */
    public Object findStateByName(Object c, String s) {
        if (!(c instanceof MClassifier))
            throw new IllegalArgumentException();
        if ((s == "") || (s == null)) return null;
        Collection allStatemachines = ModelFacade.getBehaviors(c);
        Iterator i = allStatemachines.iterator();
        while (i.hasNext()) {
            Object statemachine = i.next();
            Object top = StateMachinesHelper.getHelper().getTop(statemachine);
            Collection allStates = 
                StateMachinesHelper.getHelper().getAllSubStates(top);
            Iterator ii = allStates.iterator();
            while (ii.hasNext()) {
                Object state = ii.next();
                if (ModelFacade.isAState(state)) { 
                    String statename = ((MState) state).getName();
                    if (statename != null) 
                        if (statename.equals(s))
                            return state;
                }
            }
        }
        return null;
    }
}

