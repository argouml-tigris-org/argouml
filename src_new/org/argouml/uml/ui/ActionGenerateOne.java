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

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.model.ModelFacade;
import org.argouml.ui.ProjectBrowser;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.generator.ui.ClassGenerationDialog;
import org.tigris.gef.presentation.Fig;

/** Action to trigger creation of one class to source.
 *  @stereotype singleton */
public class ActionGenerateOne extends UMLAction {

    ////////////////////////////////////////////////////////////////
    // static variables

    public static ActionGenerateOne SINGLETON = new ActionGenerateOne();

    ////////////////////////////////////////////////////////////////
    // constructors

    protected ActionGenerateOne() {
        super("action.generate-selected-classes", NO_ICON);
    }

    ////////////////////////////////////////////////////////////////
    // main methods

    public void actionPerformed(ActionEvent ae) {
        ProjectBrowser pb = ProjectBrowser.getInstance();
        Vector classes = new Vector();
        Collection targets = TargetManager.getInstance().getTargets();
        Iterator it = targets.iterator();
        while (it.hasNext()) {
            Object target = it.next();
            if (target instanceof Fig) {
                target = ((Fig)target).getOwner();
            }
            if (ModelFacade.isAClass(target)
                || ModelFacade.isAInterface(target)) {
                classes.add(target);
            }
        }
        /*
        Editor ce = org.tigris.gef.base.Globals.curEditor();
        Vector sels = ce.getSelectionManager().getFigs();
        java.util.Enumeration enum = sels.elements();
        Vector classes = new Vector();
        while (enum.hasMoreElements()) {
        Fig f = (Fig)enum.nextElement();
        Object owner = f.getOwner();
        if (!(owner instanceof MClass) && !(owner instanceof MInterface))
        continue;
        MClassifier cls = (MClassifier)owner;
        String name = cls.getName();
        if (name == null || name.length() == 0)
        continue;
        classes.addElement(cls);
        }*/
        // There is no need to test if classes is empty because
        // the shouldBeEnabled mechanism blanks out the possibility to
        // choose this alternative in this case.
        ClassGenerationDialog cgd = new ClassGenerationDialog(classes);
        cgd.show();
    }

    public boolean shouldBeEnabled() {
        if (!super.shouldBeEnabled())
            return false;
        boolean foundOne = false;
        Collection targets = TargetManager.getInstance().getTargets();
        Iterator it = targets.iterator();
        while (it.hasNext()) {
            Object target = it.next();
            if (target instanceof Fig) {
                target = ((Fig)target).getOwner();
            }
            if (ModelFacade.isAClass(target)
                || ModelFacade.isAInterface(target)) {
                foundOne = true;
                break;
            }
        }
        return foundOne;
        /*
        Editor ce = org.tigris.gef.base.Globals.curEditor();
        if(ce != null) {
            Vector sels = ce.getSelectionManager().getFigs();
            java.util.Enumeration enum = sels.elements();
            while (enum.hasMoreElements()) {
        	Fig f = (Fig) enum.nextElement();
        	Object owner = f.getOwner();
        	if (!(owner instanceof MClass) && !(owner instanceof MInterface))
        	    continue;
        	MClassifier cls = (MClassifier) owner;
        	String name = cls.getName();
        	if (name == null || name.length() == 0) return false;
        	foundOne = true;
            }
        }
        return foundOne;
        */
    }
} /* end class ActionGenerateOne */
