/* $Id$
 *****************************************************************************
 * Copyright (c) 2009 Contributors - see below
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    tfmorris
 *****************************************************************************
 *
 * Some portions of this file was previously release using the BSD License:
 */

// Copyright (c) 2004-2007 The Regents of the University of California. All
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

package org.argouml.uml.cognitive.critics;

import java.util.Collection;

import javax.swing.JPanel;

import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;

/**
 * A wizard to add a constructor to a classifier.
 *
 * @author  d00mst (copied from WizAddOperation by mkl)
 * @since February 7, 2004, 12:35 AM
 */
public class WizAddConstructor extends UMLWizard {

    private WizStepTextField step1;
    private String label = Translator.localize("label.name");
    private String instructions = 
        Translator.localize("critics.WizAddConstructor-ins");

    /**
     * Creates a new instance of WizAddConstructor.
     */
    public WizAddConstructor() {
        super();
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
	Object oper;
	Collection savedTargets;

	switch (oldStep) {
	case 1:
	    String newName = getSuggestion();
	    if (step1 != null) {
	        newName = step1.getText();
	    }
	    Object me = getModelElement();
	    savedTargets = TargetManager.getInstance().getTargets();
	    Object returnType =
	        ProjectManager.getManager().getCurrentProject()
	        	.getDefaultReturnType();
	    oper =
	        Model.getCoreFactory().buildOperation2(me, returnType, newName);
	    Model.getCoreHelper()
	        .addStereotype(oper, getCreateStereotype(oper));
            ProjectManager.getManager().updateRoots();
	    TargetManager.getInstance().setTargets(savedTargets);
            break;
	}
    }

    /**
     * Finds the create stereotype for an object.
     * 
     * @param obj is the object the stereotype should be applicable to.
     * @return a suitable stereotype, or null.
     */
    private Object getCreateStereotype(Object obj) {
        return ProjectManager.getManager().getCurrentProject()
                .getProfileConfiguration().findStereotypeForObject("create",
                        obj);
    }

    /**
     * @param s set a new instruction string
     */
    public void setInstructions(String s) {
	instructions = s;
    }


    /**
     * Create a new panel for the given step.
     *
     * @param newStep The step.
     * @return The panel.
     */
    public JPanel makePanel(int newStep) {
        switch (newStep) {
	case 1:
	    if (step1 == null) {
		step1 =
		    new WizStepTextField(this, instructions,
		            		 label, offerSuggestion());
	    }
	    return step1;
        }
        return null;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4661562206721689576L;
}

