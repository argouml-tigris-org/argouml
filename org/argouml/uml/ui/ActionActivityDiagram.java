// Copyright (c) 1996-01 The Regents of the University of California. All
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

import org.argouml.kernel.*;
import org.argouml.ui.*;
import org.argouml.uml.*;
import org.argouml.uml.diagram.activity.ui.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.behavior.use_cases.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.activity_graphs.*;
import java.awt.event.*;
import java.beans.*;
import javax.swing.JOptionPane;


public class ActionActivityDiagram extends UMLChangeAction {

    ////////////////////////////////////////////////////////////////
    // static variables
    
    public static ActionActivityDiagram SINGLETON = new ActionActivityDiagram(); 


    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionActivityDiagram() { super("ActivityDiagram"); }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Project p = pb.getProject();
	try {
	    Object me = pb.getDetailsTarget();
	    if (!((me instanceof MNamespace) && ((me instanceof MUseCase) || (me instanceof MClass))))  {
	    	JOptionPane.showMessageDialog(null, 
	    	"You need to have a class or use case as your target in order to\nspecify for what you want to define a behaviour for.",
	    	"Warning", JOptionPane.WARNING_MESSAGE);
	    	return;
	    };
	    MNamespace ns=(MNamespace)me;
	    String contextNameStr = ns.getName();
	    if (contextNameStr == null) contextNameStr = "untitled";
	    MActivityGraph am = new MActivityGraphImpl();
	    am.setUUID(UUIDManager.SINGLETON.getNewUUID());
	    am.setName(contextNameStr + "ActivityGraph");
	    MCompositeState cs = new MCompositeStateImpl();
	    cs.setName("activities_top");
	    //cs.setNamespace(ns);
	    am.setNamespace(ns);
	    am.setTop(cs);
	    ns.addBehavior(am);
	    UMLActivityDiagram d = new UMLActivityDiagram(ns, am);
	    p.addMember(d);
	    ProjectBrowser.TheInstance.getNavPane().addToHistory(d);
	    pb.setTarget(d);
	} catch (PropertyVetoException pve) {
	    System.out.println("PropertyVetoException in ActionActivityDiagram");
	}
	super.actionPerformed(ae);
    }
    public boolean shouldBeEnabled() {
    	return true;
//	ProjectBrowser pb = ProjectBrowser.TheInstance;
//	Project p = pb.getProject();
//	Object target = pb.getDetailsTarget();
//	return super.shouldBeEnabled() && p != null &&
//	    ((target instanceof MUseCase)||(target instanceof MClass)); // or MOperation
    }
} /* end class ActionActivityDiagram */
