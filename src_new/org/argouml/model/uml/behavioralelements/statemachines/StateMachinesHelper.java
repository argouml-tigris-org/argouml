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

package org.argouml.model.uml.behavioralelements.statemachines;

import ru.novosoft.uml.behavior.state_machines.MStateVertex;
import ru.novosoft.uml.behavior.state_machines.MTransition;

/**
 * Helper class for UML BehavioralElements::StateMachines Package.
 *
 * Current implementation is a placeholder.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 * @stereotype singleton
 */
public class StateMachinesHelper {

    /** Don't allow instantiation.
     */
    private StateMachinesHelper() {
    }
    
     /** Singleton instance.
     */
    private static StateMachinesHelper SINGLETON =
                   new StateMachinesHelper();

    
    /** Singleton instance access method.
     */
    public static StateMachinesHelper getHelper() {
        return SINGLETON;
    }
    
    /**
     * Returns the source of the given transition. This operation is here to 
     * give a full implementation of all getSource and getDestination methods
     * on the uml helpers.
     * @param trans
     * @return MStateVertex
     */
    public MStateVertex getSource(MTransition trans) {
        return trans.getSource();
    }
    
    /**
     * Returns the destination of the given transition. This operation is here 
     * to give a full implementation of all getSource and getDestination methods
     * on the uml helpers.
     * @param trans
     * @return MStateVertex
     */
    public MStateVertex getDestination(MTransition trans) {
        return trans.getTarget();
    }
}

