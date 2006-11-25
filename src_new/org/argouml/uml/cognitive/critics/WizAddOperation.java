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

package org.argouml.uml.cognitive.critics;

import javax.swing.JPanel;

import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.i18n.Translator;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;

/**
 * A wizard to add operations to a classifier.
 *
 * @author  mkl
 * @since Created on December 4, 2003, 1:55 PM
 */
public class WizAddOperation extends UMLWizard {

    private WizStepTextField step1 = null;
    private String label = Translator.localize("label.name");
    private String instructions;

    /**
     * Creates a new instance of WizAddOperation.
     */
    public WizAddOperation() {
        super();
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
        switch (oldStep) {
	case 1:
	    String newName = getSuggestion();
	    if (step1 != null) {
		newName = step1.getText();
	    }
	    Object me = getModelElement();
	    Object model =
	        ProjectManager.getManager()
	        	.getCurrentProject().getModel();
	    Object voidType =
	        ProjectManager.getManager()
	        	.getCurrentProject().findType("void");
	    Model.getCoreFactory().buildOperation(me, model,
	            voidType, newName);
        }
    }


    /**
     * @param s the new instructions
     */
    public void setInstructions(String s) { instructions = s; }

    /**
     * @param b
     */
    //public void setMustEdit(boolean b) { mustEdit = b; }

    /*
     * @see org.argouml.cognitive.ui.Wizard#makePanel(int)
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
}
