
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



// File: WizOperName.java
// Classes: WizOperName
// Original Author: Linus Tolke <linus@epact.se>
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JPanel;
import org.apache.log4j.Category;

import org.argouml.cognitive.ui.WizStepChoice;
import org.argouml.cognitive.ui.WizStepCue;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;

import ru.novosoft.uml.foundation.core.MModelElement;
import ru.novosoft.uml.foundation.core.MNamespace;
import ru.novosoft.uml.foundation.core.MOperation;
import ru.novosoft.uml.foundation.extension_mechanisms.MStereotype;
import ru.novosoft.uml.model_management.MModel;
import ru.novosoft.uml.model_management.MPackage;

/** A wizard to help the user change the name of an operation to a better name.
 * Same as WizMEName expect that it handles the special case where 
 * the operation instead should be made a constructor of the class.
 * This is helpful in languages where constructors have names that
 * do not agree with the convention for method names (i.e. Java).
 *
 * Path looks like this for the case when it is not supposed to be a 
 * constructor:
 * step0 -> step1
 *
 * Path looks like this for the case when it is supposed to be a constructor:
 * step0 -> step1 -> step2 (OK! in the case converted to constructor)
 *                -> step2 (same as step1 in the scenario above)
 */

public class WizOperName extends WizMEName {
    protected static Category cat = Category.getInstance(WizOperName.class);

    boolean _possibleConstructor = false;
    boolean _stereotypePathChosen;

    protected String _option0 = "This is really a constructor.";
    protected String _option1 = "This is not a constructor.";
    protected WizStepChoice _step1 = null;
    protected WizStepCue _step2 = null;

    protected MStereotype _oldStereotype;
    protected boolean _oldStereotypeIsSet = false;

    public WizOperName() { super(); }

    public int getNumSteps() {
	if (_possibleConstructor)
	    return 2;
	else
	    return 1;
    }

    private Vector getOptions() {
	Vector res = new Vector();
	res.addElement(_option0);
	res.addElement(_option1);
	return res;
    }

    /** 
     * Method to tell the Wizard what path it should work with.
     *
     * @param setToConstructor is true if we shall take the path where
     * the oper is converted to a constructor.
     */
    public void setPossibleConstructor(boolean b) { _possibleConstructor = b; }

    /** Create a new panel for the given step. */
    public JPanel makePanel(int newStep) {
	if (!_possibleConstructor)
	    return super.makePanel(newStep);

	switch (newStep) {
	case 0:
	    return super.makePanel(newStep);

	case 1:
	    if (_step1 == null) {
		_step1 = new WizStepChoice(this, _instructions, getOptions());
		_step1.setTarget(_item);
	    }
	    return _step1;

	case 2:
	    if (_stereotypePathChosen) {
		if (_step2 == null) {
		    _step2 = 
			new WizStepCue(this,
				       "The operator is now a constructor.");
		    _step2.setTarget(_item);
		}
		return _step2;
	    }
	    else
		return super.makePanel(1);
	}
	return null;
    }

    /** There is a possibility that the next step forward takes another path
     * in this wizard. To allow for this we must destroy the path already 
     * traveled by.
     * TODO:
     * I (Linus) would say that this is really a problem with the Wizard 
     * implementation since I believe it should be possible to explore a 
     * path in the wizard and then go back.
     */
    public void undoAction(int origStep) {
	super.undoAction(origStep);
	if (_step >= 1) {
	    _panels.remove(origStep);
	}
	if (origStep == 1) {
	    MOperation oper = (MOperation) getModelElement();

	    if (_oldStereotypeIsSet) {
		oper.setStereotype(_oldStereotype);
	    }
	}
    }

    /** Take action at the completion of a step. For example, when the
     *  given step is 0, do nothing; and when the given step is 1, do
     *  the first action.  Argo non-modal wizards should take action as
     *  they do along, as soon as possible, they should not wait until
     *  the final step. */
    public void doAction(int oldStep) {
	if (!_possibleConstructor) {
	    super.doAction(oldStep);
	    return;
	}

	switch (oldStep) {
	case 1:
	    int choice = -1;
	    if (_step1 != null) choice = _step1.getSelectedIndex();
	    
	    switch (choice) {
	    case -1:
		throw new Error("nothing selected, should not get here");

	    case 0:
		_stereotypePathChosen = true;
		MOperation oper = (MOperation) getModelElement();

		if (!_oldStereotypeIsSet) {
		    _oldStereotype = oper.getStereotype();
		    _oldStereotypeIsSet = true;
		}

                // We need to find the stereotype with the name
                // "create" and the base class BehavioralFeature in
                // the model. If there is none then we create one and
                // put it there.
		MModel m = oper.getModel();
                MStereotype theStereotype = null;
                for (Iterator iter = m.getOwnedElements().iterator();
                     iter.hasNext();) {
                    MModelElement candidate = (MModelElement) iter.next();
		    if (!(ModelFacade.isAStereotype(candidate)))
                        continue;
                    MStereotype ster = (MStereotype) candidate;
                    MNamespace ns = ster.getNamespace();
                    if (!("create".equals(ster.getName())))
                        continue;
                    if (!("BehavioralFeature".equals(ster.getBaseClass())))
                        continue;
                    theStereotype = ster;
                    break;
                }
                if (theStereotype == null) {
                    theStereotype =
			UmlFactory.getFactory().getExtensionMechanisms()
			.createStereotype();
		    theStereotype.setName("create");
		    // theStereotype.setStereotype(???);
		    theStereotype.setBaseClass("BehavioralFeature");
		    MNamespace targetNS = findNamespace(oper.getNamespace(),
							oper.getModel());
                    targetNS.addOwnedElement(theStereotype);
		}

		try {
		    oper.setStereotype(theStereotype);
		}
		catch (Exception pve) {
		    cat.error("could not set stereotype", pve);
		}
		return;

	    case 1:
		// Nothing to do.
		_stereotypePathChosen = false;
		return;
	    }
	    return;

	case 2:
	    if (!_stereotypePathChosen)
		super.doAction(1);
	    return;
	}
    }
    
    
    // TODO:
    // Move to MMUtil or some other common place and merge with 
    // UMLComboBoxEntry::findNamespace()
    private static MNamespace findNamespace(MNamespace phantomNS,
					    MModel targetModel)
    {
        MNamespace ns = null;
        MNamespace targetParentNS = null;
        if (phantomNS == null) {
            return targetModel;
        }
        MNamespace parentNS = phantomNS.getNamespace();
        if (parentNS == null) {
            return targetModel;
        }
        else {
            targetParentNS = findNamespace(parentNS, targetModel);
            //
            //   see if there is already an element with the same name
            //
            Collection ownedElements = targetParentNS.getOwnedElements();
            String phantomName = phantomNS.getName();
            String targetName;
            if (ownedElements != null) {
                MModelElement ownedElement;
                Iterator iter = ownedElements.iterator();
                while (iter.hasNext()) {
                    ownedElement = (MModelElement) iter.next();
                    targetName = ownedElement.getName();
                    if (targetName != null && phantomName.equals(targetName)) {
                        if (ModelFacade.isAPackage(ownedElement)) {
                            ns = (MPackage) ownedElement;
                            break;
                        }
                    }
                }
            }
            if (ns == null) {
                ns = targetParentNS.getFactory().createPackage();
                ns.setName(phantomName);
                targetParentNS.addOwnedElement(ns);
            }
        }
        return ns;
    }

} /* end class WizOperName */