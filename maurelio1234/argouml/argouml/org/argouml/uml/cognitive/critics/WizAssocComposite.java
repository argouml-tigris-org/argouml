// $Id$
// Copyright (c) 1996-2007 The Regents of the University of California. All
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

import java.util.Iterator;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ui.WizStepChoice;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * A non-modal wizard to assist the user in changing aggregation of an
 * association.<p>
 *
 * Earlier version always imposed composite aggregation. This version
 * allows the user to choose.<p>
 *
 * <em>Note</em>. This only applies to binary associations. A separate
 * wizard is needed for 3-way (or more) associations.<p>
 *
 * @see "ArgoUML User Manual: Two Aggregate ends (roles) in binary Association"
 * @author jrobbins@ics.uci.edu
 */
public class WizAssocComposite extends UMLWizard {
    /**
     * Logger.
     */
    private static final Logger LOG =
	Logger.getLogger(WizAssocComposite.class);

    /**
     * The initial instructions on the Step 1 screen. May be set to a
     * different string through {@link #setInstructions(String)}.<p>
     */
    private String instructions = 
        Translator.localize("critics.WizAssocComposite-ins");


    /**
     * Contains the {@link WizStepChoice} that is used to get the user's
     * desired options. Not created until we get to that step.<p>
     */
    private WizStepChoice step1Choice = null;


    /**
     * The Association {@link WizStepChoice} that triggered the
     * critic. Null until set when it is first needed.<p>
     */
    private Object triggerAssociation = null;


    /**
     * Constructor for the wizard. Currently does nothing.<p>
     */
    public WizAssocComposite() { }


    /**
     * Tries to identify the Association that triggered the critic.<p>
     *
     * The first time it is called, it will initialise the trigger
     * from the ToDoItem. If there, it is assumed to be the first
     * trigger of the ToDoItem and to be an association. If found, the
     * value is stored in the private field {@link #triggerAssociation}.<p>
     *
     * On all subsequent calls, if a non-null value is found in {@link
     * #triggerAssociation} that is returned.<p>
     *
     * @return  the Association that triggered the critic, or <code>null</code>
     *          if there was none.
     */
    private Object getTriggerAssociation() {

        // If we don't have it, find the trigger. If this fails it will keep
        // its default value of null

        if ((triggerAssociation == null) && (getToDoItem() != null)) {
            triggerAssociation = getModelElement();
        }

        return triggerAssociation;
    }


    /**
     * Returns a vector of options to be used in creating a {@link
     * WizStepChoice} that will exercise the options.<p>
     *
     * We provide five options, shared aggregation in each direction,
     * composite aggregation in each direction and no aggregation at
     * all.<p>
     *
     * It is possible that a very malicious user could delete the
     * triggering association just before we get to this point. For
     * now we don't bother to trap this. It will raise an exception,
     * and then everything will carry on happily.<p>
     *
     * @return  A {@link Vector} of the options or <code>null</code> if the
     *          association that triggered the critic is no longer there.
     */
    private Vector buildOptions() {

        // The association that triggered the critic. Its just possible the
        // association is no longer there, in which case we return null

        Object asc = getTriggerAssociation();

        if (asc == null) {
            return null;
        }

        // A vector in which to build the result

        Vector res = new Vector();

        // Get the ends from the association (we know there are two), and the
        // types associated with them.

        Iterator iter = Model.getFacade().getConnections(asc).iterator();

        Object ae0 = iter.next();
        Object ae1 = iter.next();

        Object cls0 = Model.getFacade().getType(ae0);
        Object cls1 = Model.getFacade().getType(ae1);

        // Get the names of the two ends. If there are none (i.e they are
        // currently anonymous), use the ArgoUML convention of "(anon)" for the
        // names

        String start = Translator.localize("misc.name.anon");
        String end   = Translator.localize("misc.name.anon");

        if ((cls0 != null)
                && (Model.getFacade().getName(cls0) != null)
                && (!(Model.getFacade().getName(cls0).equals("")))) {
            start = Model.getFacade().getName(cls0);
        }

        if ((cls1 != null)
                && (Model.getFacade().getName(cls1) != null)
                && (!(Model.getFacade().getName(cls1).equals("")))) {
            end = Model.getFacade().getName(cls1);
        }

        // Now create the five options

        res.addElement (start 
                + Translator.localize("critics.WizAssocComposite-option1") 
                + end);
        res.addElement (start 
                + Translator.localize("critics.WizAssocComposite-option2") 
                + end);

        res.addElement (end 
                + Translator.localize("critics.WizAssocComposite-option1") 
                + start);
        res.addElement (end 
                + Translator.localize("critics.WizAssocComposite-option2") 
                + start);

        res.addElement(Translator.localize(
                "critics.WizAssocComposite-option3"));

        return res;
    }

    /**
     * Set the initial instruction string for the choice. May be
     * called by the creator of the wizard to override the default.<p>
     *
     * @param s The new instructions.
     */
    public void setInstructions(String s) {
        instructions = s;
    }


    /**
     * Create a {@link JPanel} for the given step.<p>
     *
     * We use a {@link WizStepChoice} to handle the choice selection
     * for the user. We only create the panel once, saving it in a
     * private field (<code>_step1Choice</code>) for subsequent
     * use.<p>
     *
     * <em>Note</em>. If the association has been deleted, then we may
     * not be able to create a vector of options. Under these
     * circumstances we also return null.<p>
     *
     * @param  newStep  The index of the step for which a panel is needed.
     *
     * @return          The created {@link JPanel} or <code>null</code> if no
     *                  options were available.
     *
     * @see org.argouml.cognitive.critics.Wizard
     */
    public JPanel makePanel(int newStep) {

        switch (newStep) {

        case 1:

            // First step. Create the panel if not already done and options are
            // available. Otherwise it retains its default value of null.

            if (step1Choice == null) {
                Vector opts = buildOptions();

                if (opts != null) {
                    step1Choice =
                        new WizStepChoice(this, instructions, opts);
                    step1Choice.setTarget(getToDoItem());
                }
            }

            return step1Choice;

        default:
        }

        // Default (any other step) is to return nothing

        return null;
    }


    /**
     * Take action at the completion of a step.<p>
     *
     * The guideline for ArgoUML non-modal wizards is to act
     * immediately, not wait for the finish. This method may also be
     * invoked when finish is triggered for any steps whose panels
     * didn't get created.<p>
     *
     * The observation is that this seems to be trigged when there is
     * any change on the panel (e.g choosing an option), not just when
     * "next" is pressed. Coded accordingly<p>
     *
     * We allow for the association that caused the problem having by
     * now been deleted, and hence an exception may be raised. We
     * catch this politely.<p>
     *
     * @param  oldStep  The index of the step just completed (0 for the first
     *                  information panel)
     *
     * @see org.argouml.cognitive.critics.Wizard
     */
    public void doAction(int oldStep) {

        switch (oldStep) {

        case 1:

            // Just completed the first step where we make our choices. First
            // see if we have a choice. We always should, so print a rude
            // message if we don't

            int choice = -1;

            if (step1Choice != null) {
                choice = step1Choice.getSelectedIndex();
            }

            if (choice == -1) {
                LOG.warn("WizAssocComposite: nothing selected, "
			 + "should not get here");
                return;
            }

            // It is quite possible that the cause of the problem has by now
            // been deleted, in which case we will throw an exception if we try
            // to change things. Catch this tidily.

            try {

                // Set the appropriate aggregation on each end

                Iterator iter =
		    Model.getFacade().getConnections(getTriggerAssociation())
		    	.iterator();

                Object ae0 = iter.next();
                Object ae1 = iter.next();

                switch (choice) {

                case 0:

                    // Start is a composite aggregation of end

                    Model.getCoreHelper().setAggregation(
			    ae0,
			    Model.getAggregationKind().getComposite());
                    Model.getCoreHelper().setAggregation(
			    ae1,
			    Model.getAggregationKind().getNone());
                    break;

                case 1:

                    // Start is a shared aggregation of end

                    Model.getCoreHelper().setAggregation(
			    ae0,
			    Model.getAggregationKind().getAggregate());
                    Model.getCoreHelper().setAggregation(
			    ae1,
			    Model.getAggregationKind().getNone());
                    break;

                case 2:

                    // End is a composite aggregation of start

                    Model.getCoreHelper().setAggregation(
			    ae0,
			    Model.getAggregationKind().getNone());
                    Model.getCoreHelper().setAggregation(
			    ae1,
			    Model.getAggregationKind().getComposite());
                    break;

                case 3:

                    // End is a shared aggregation of start
                    Model.getCoreHelper().setAggregation(
			    ae0,
			    Model.getAggregationKind().getNone());
                    Model.getCoreHelper().setAggregation(
			    ae1,
			    Model.getAggregationKind().getAggregate());
                    break;

                case 4:

                    // No aggregation
                    Model.getCoreHelper().setAggregation(
			    ae0,
			    Model.getAggregationKind().getNone());
                    Model.getCoreHelper().setAggregation(
			    ae1,
			    Model.getAggregationKind().getNone());
                    break;

                default:
                }
            } catch (Exception pve) {

                // Someone took our association away.

                LOG.error("WizAssocComposite: could not set "
			  + "aggregation.", pve);
            }

        default:
        }
    }

    /**
     * Determine if we have sufficient information to finish.<p>
     *
     * We can't finish if our parent {@link org.argouml.cognitive.critics.Wizard}
     * can't finish.<p>
     *
     * We can finish if we're on step 0.<p>
     *
     * We can finish if we're on step 1 and have made a choice.<p>
     *
     * @return  <code>true</code> if we can finish, otherwise
     *          <code>false</code>.
     *
     * @see org.argouml.cognitive.critics.Wizard
     */

    public boolean canFinish() {

        // Can't finish if our parent can't

        if (!super.canFinish()) {
            return false;
        }

        // Can finish if it's step 0

        if (getStep() == 0) {
            return true;
        }

        // Can finish if we're on step1 and have actually made a choice

        if ((getStep() == 1)
	    && (step1Choice != null)
	    && (step1Choice.getSelectedIndex() != -1)) {
            return true;
        }

        // Otherwise we can't finish

        return false;
    }


} /* end class WizAssocComposite */
