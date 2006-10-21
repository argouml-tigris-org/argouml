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

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ui.WizStepChoice;
import org.argouml.cognitive.ui.WizStepCue;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * A wizard to help the user change the name of an operation to a better name.
 * Same as WizMEName except that it handles the special case where the operation
 * instead should be made a constructor of the class. This is helpful in
 * languages where constructors have names that do not agree with the convention
 * for method names (i.e. Java).<p>
 *
 * Path looks like this for the case when it is not supposed to be a
 * constructor:
 *
 * <pre>
 *
 *  step0 -&gt; step1
 *
 * </pre>
 *
 * Path looks like this for the case when it is supposed to be a constructor:
 *
 * <pre>
 *
 *  step0 -&gt; step1 -&gt; step2 (OK! in the case converted to constructor)
 *                 -&gt; step2 (same as step1 in the scenario above)
 *
 * </pre>
 */
public class WizOperName extends WizMEName {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(WizOperName.class);

    private boolean possibleConstructor;

    private boolean stereotypePathChosen;

    private String option0 = 
        Translator.localize("critics.WizOperName-options1");

    private String option1 = 
        Translator.localize("critics.WizOperName-options2"); 
        
    private WizStepChoice step1;

    private WizStepCue step2;

    private Object createStereotype;

    private boolean addedCreateStereotype;

    /**
     * @see org.argouml.cognitive.ui.Wizard#getNumSteps()
     */
    public int getNumSteps() {
        if (possibleConstructor) {
            return 2;
        }
        return 1;
    }

    private Vector getOptions() {
        Vector res = new Vector();
        res.addElement(option0);
        res.addElement(option1);
        return res;
    }

    /**
     * Method to tell the Wizard what path it should work with.
     *
     * @param b
     *            setToConstructor is true if we shall take the path where the
     *            oper is converted to a constructor.
     */
    public void setPossibleConstructor(boolean b) {
        possibleConstructor = b;
    }

    /**
     * @see org.argouml.cognitive.ui.Wizard#makePanel(int)
     *
     * Create a new panel for the given step.
     */
    public JPanel makePanel(int newStep) {
        if (!possibleConstructor) {
            return super.makePanel(newStep);
        }

        switch (newStep) {
        case 0:
            return super.makePanel(newStep);

        case 1:
            if (step1 == null) {
                step1 =
                    new WizStepChoice(this, getInstructions(), getOptions());
                step1.setTarget(getToDoItem());
            }
            return step1;

        case 2:
            if (stereotypePathChosen) {
                if (step2 == null) {
                    step2 =
                        new WizStepCue(this, Translator.localize(
                                "critics.WizOperName-stereotype"));
                    step2.setTarget(getToDoItem());
                }
                return step2;
            }
            return super.makePanel(1);

        default:
        }
        return null;
    }

    /**
     * There is a possibility that the next step forward takes another path in
     * this wizard. To allow for this we must destroy the path already traveled
     * by. TODO: I (Linus) would say that this is really a problem with the
     * Wizard implementation since I believe it should be possible to explore a
     * path in the wizard and then go back.
     *
     * @see org.argouml.cognitive.ui.Wizard#undoAction(int)
     */
    public void undoAction(int origStep) {
        super.undoAction(origStep);
        if (getStep() >= 1) {
            removePanel(origStep);
        }
        if (origStep == 1) {
            Object oper = getModelElement();

            if (addedCreateStereotype) {
                Model.getCoreHelper().removeStereotype(oper, createStereotype);
            }
        }
    }

    /**
     * Take action at the completion of a step. For example, when the given step
     * is 0, do nothing; and when the given step is 1, do the first action. Argo
     * non-modal wizards should take action as they do along, as soon as
     * possible, they should not wait until the final step.
     *
     * @see org.argouml.cognitive.ui.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
        if (!possibleConstructor) {
            super.doAction(oldStep);
            return;
        }

        switch (oldStep) {
        case 1:
            int choice = -1;
            if (step1 != null) {
                choice = step1.getSelectedIndex();
            }

            switch (choice) {
            case -1:
                throw new IllegalArgumentException(
                        "nothing selected, should not get here");

            case 0:
                stereotypePathChosen = true;
                Object oper = getModelElement();


                // We need to find the stereotype with the name
                // "create" and the base class BehavioralFeature in
                // the model. If there is none then we create one and
                // put it there.
                Object m = Model.getFacade().getModel(oper);
                Object theStereotype = null;
                for (Iterator iter =
                        Model.getFacade().getOwnedElements(m).iterator();
                                        iter.hasNext();) {
                    Object candidate = iter.next();
                    if (!(Model.getFacade().isAStereotype(candidate))) {
                        continue;
                    }
                    if (!("create".equals(
                            Model.getFacade().getName(candidate)))) {
                        continue;
                    }
                    Collection baseClasses = Model.getFacade().getBaseClasses(candidate);
                    Iterator iter2 = baseClasses != null ? baseClasses.iterator() : null;
                    if (iter2 == null || !("BehavioralFeature".equals(
                            iter2.next()))) {
                        continue;
                    }
                    theStereotype = candidate;
                    break;
                }
                if (theStereotype == null) {
                    theStereotype =
                        Model.getExtensionMechanismsFactory()
                        	.buildStereotype("create", m);
                    Model.getCoreHelper().setName(theStereotype, "create");
                    // theStereotype.setStereotype(???);
                    Model.getExtensionMechanismsHelper()
                            .addBaseClass(theStereotype, "BehavioralFeature");
                    Object targetNS =
                        findNamespace(Model.getFacade().getNamespace(oper),
                                      Model.getFacade().getModel(oper));
                    Model.getCoreHelper()
                        .addOwnedElement(targetNS, theStereotype);
                }

                try {
                    createStereotype = theStereotype;
                    Model.getCoreHelper().addStereotype(oper, theStereotype);
                    addedCreateStereotype = true;
                } catch (Exception pve) {
                    LOG.error("could not set stereotype", pve);
                }
                return;

            case 1:
                // Nothing to do.
                stereotypePathChosen = false;
                return;

            default:
            }
            return;

        case 2:
            if (!stereotypePathChosen) {
                super.doAction(1);
            }
            return;

        default:
        }
    }

    // TODO:
    // Move to MMUtil or some other common place and merge with
    // UMLComboBoxEntry::findNamespace()
    private static Object findNamespace(Object phantomNS, Object targetModel) {
        Object ns = null;
        Object targetParentNS = null;
        if (phantomNS == null) {
            return targetModel;
        }
        Object parentNS = Model.getFacade().getNamespace(phantomNS);
        if (parentNS == null) {
            return targetModel;
        }
        targetParentNS = findNamespace(parentNS, targetModel);
        //
        //   see if there is already an element with the same name
        //
        Collection ownedElements =
            Model.getFacade().getOwnedElements(targetParentNS);
        String phantomName = Model.getFacade().getName(phantomNS);
        String targetName;
        if (ownedElements != null) {
            Object ownedElement;
            Iterator iter = ownedElements.iterator();
            while (iter.hasNext()) {
                ownedElement = iter.next();
                targetName = Model.getFacade().getName(ownedElement);
                if (targetName != null && phantomName.equals(targetName)) {
                    if (Model.getFacade().isAPackage(ownedElement)) {
                        ns = ownedElement;
                        break;
                    }
                }
            }
        }
        if (ns == null) {
            ns = Model.getModelManagementFactory().createPackage();
            Model.getCoreHelper().setName(ns, phantomName);
            Model.getCoreHelper().addOwnedElement(targetParentNS, ns);
        }
        return ns;
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -4013730212763172160L;
} /* end class WizOperName */
