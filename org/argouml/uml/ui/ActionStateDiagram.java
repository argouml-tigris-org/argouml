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

package org.argouml.uml.ui;

import org.apache.log4j.Category;
import org.argouml.kernel.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.model.uml.behavioralelements.statemachines.StateMachinesFactory;
import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.uml.diagram.state.ui.*;
import org.argouml.i18n.Translator;

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.state_machines.*;

import java.awt.event.*;
import java.beans.*;

import javax.swing.JOptionPane;

/** Action to create a new state diagram.
 * @stereotype singleton
 */
public class ActionStateDiagram extends ActionAddDiagram {
    protected static Category cat = Category.getInstance(ActionStateDiagram.class);

    ////////////////////////////////////////////////////////////////
    // static variables
    
    public static ActionStateDiagram SINGLETON = new ActionStateDiagram(); 


    ////////////////////////////////////////////////////////////////
    // constructors

    private ActionStateDiagram() {
        super(Translator.localize("CoreMenu", "StateDiagram"));
    }
    
    protected ActionStateDiagram(String name) { super(name); }

    /**
     * Overriden since it should only be possible to add statediagrams and 
     * activitydiagrams to classifiers and behavioral features.
     * @see org.argouml.uml.ui.UMLAction#shouldBeEnabled()
     */
    public boolean shouldBeEnabled() {
    	ProjectBrowser pb = ProjectBrowser.TheInstance;
        if (pb != null) {
    	   Object target = pb.getDetailsTarget();
    	   if (target == null) {
    		target = pb.getTarget();
    	   }
    	   return target instanceof MBehavioralFeature || target instanceof MClassifier;
        } else
            return false;
    }
    
    /**
     * @see org.argouml.uml.ui.ActionAddDiagram#createDiagram(MNamespace, Object)
     */
    public ArgoDiagram createDiagram(MNamespace ns, Object target) {
        MStateMachine machine = StateMachinesFactory.getFactory().buildStateMachine((MModelElement)target);
        if (target instanceof MBehavioralFeature) {
            ns = ((MBehavioralFeature)target).getNamespace();
        }
        UMLStateDiagram d = new UMLStateDiagram(ns, machine);
        return d;
    }

    /**
     * @see org.argouml.uml.ui.ActionAddDiagram#isValidNamespace(MNamespace)
     */
    public boolean isValidNamespace(MNamespace ns) {
        if (ns instanceof MClassifier) return true;
        return false;
    }

} /* end class ActionStateDiagram */
