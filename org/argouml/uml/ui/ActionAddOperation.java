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

import org.argouml.kernel.*;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.*;
import org.argouml.uml.diagram.ui.FigNodeModelElement;
import org.tigris.gef.presentation.FigNode;
import org.argouml.ui.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.behavior.common_behavior.MSignal;
import ru.novosoft.uml.foundation.core.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Vector;

/** Action to add an operation to a classifier.
 *  @stereotype singleton
 */
public class ActionAddOperation extends UMLChangeAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionAddOperation SINGLETON = new ActionAddOperation();

    


    ////////////////////////////////////////////////////////////////
    // constructors

    public ActionAddOperation() { super("button.add-operation"); }


    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
   
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Project p = ProjectManager.getManager().getCurrentProject();
	Object target = pb.getDetailsTarget();
	if (!(target instanceof MClassifier)) return;
	MClassifier cls = (MClassifier) target;
	MOperation oper = UmlFactory.getFactory().getCore().buildOperation(cls);
	pb.getNavigatorPane().addToHistory(oper);
	pb.setTarget(oper);
	 super.actionPerformed(ae);
	
    }

    public boolean shouldBeEnabled() {
	ProjectBrowser pb = ProjectBrowser.TheInstance;
	Object target = pb.getDetailsTarget();
	return super.shouldBeEnabled() && target instanceof MClassifier && !(target instanceof MSignal);
    }
} /* end class ActionAddOperation */
