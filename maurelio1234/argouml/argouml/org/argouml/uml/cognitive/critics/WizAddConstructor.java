// $Id$
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
import java.util.Iterator;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ui.targetmanager.TargetManager;
import org.argouml.uml.profile.ProfileException;

/**
 * A wizard to add a constructor to a classifier.
 *
 * @author  d00mst (copied from WizAddOperation by mkl)
 * @since February 7, 2004, 12:35 AM
 */
public class WizAddConstructor extends UMLWizard {
    /**
     * Logger.
     */
    private static final Logger LOG =
        Logger.getLogger(WizAddConstructor.class);

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
	    TargetManager.getInstance().setTargets(savedTargets);
            break;
	}
    }

    /**
     * Finds the create stereotype for an object. It is assumed to be
     * available from the java profile.
     *
     * @param obj is the object the stereotype should be applicable to.
     * @return a suitable stereotype, or null.
     */
    private Object getCreateStereotype(Object obj) {
        Iterator iter = null;
	Project project = ProjectManager.getManager().getCurrentProject();
	Object profileModel = project.getProfileConfiguration()
		.getDefaultProfile().getModel();
	iter = Model.getFacade().getOwnedElements(profileModel).iterator();
            
        while (iter.hasNext()) {
            Object stereo = iter.next();
            if (!Model.getFacade().isAStereotype(stereo)
        	|| !"create".equals(Model.getFacade().getName(stereo))) {
                continue;
            }

            if (Model.getExtensionMechanismsHelper()
        	    .isValidStereoType(obj, stereo)) {
        	return Model.getModelManagementHelper()
        	    .getCorrespondingElement(stereo,
        				     Model.getFacade().getModel(obj));
            }
        }

        return null;
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

