// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// File: WizAssocComposite.java
// Classes: WizAssocComposite
// Original Author: jrobbins@ics.uci.edu
// $Id$

// 12 Mar 2002: Jeremy Bennett (mail@jeremybennett.com). Code corrected and
// tidied up as part of fix to issue 619.

package org.argouml.uml.cognitive.critics;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.JPanel;
import org.apache.log4j.Category;

import org.argouml.cognitive.ui.WizStepChoice;
import org.argouml.kernel.Wizard;
import org.tigris.gef.util.VectorSet;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.data_types.MAggregationKind;

/**
 * <p>A non-modal wizard to assist the user in changing aggregation of an
 *   association.</p>
 *
 * <p>Earlier version always imposed composite aggregation. This version allows
 *   the user to choose.</p>
 *
 * <p><em>Note</em>. This only applies to binary associations. A separate
 *   wizard is needed for 3-way (or more) associations.</p>
 *
 * @see <a
 * href="http://argouml.tigris.org/documentation/snapshots/manual/argouml.html/#s2.ref.critics_multiple_agg">ArgoUML
 * User Manual: Two Aggregate ends (roles) in binary Association</a>
 */


public class WizAssocComposite extends Wizard {
    protected static Category cat =
	Category.getInstance(WizAssocComposite.class);
    

    /**
     * <p>The initial instructions on the Step 1 screen. May be set to a
     * different string through the {@link #setInstructions} method.</p>
     */

    private String _instructions =
        "Please select one of the following aggregation options:";


    /**
     * <p>Contains the {@link WizStepChoice} that is used to get the user's
     * desired options. Not created until we get to that step.</p>
     */

    private WizStepChoice _step1Choice = null;


    /**
     * <p>The {@link ru.novosoft.uml.foundation.core.MAssociation
     *   MAssociation}{@link WizStepChoice} that triggered the critic. Null
     *   until set when it is first needed.</p>
     */

    private MAssociation _triggerAsc = null;


    /**
     * <p>Constructor for the wizard. Currently does nothing.</p>
     *
     *  @return  Nothing, since this is a constructor
     */

    public WizAssocComposite() { }


    /**
     * <p>Returns the number of steps in this wizard.</p>
     *
     * @return  The number of steps (excluding the initial explanation) in this
     *          wizard (1).
     *
     * @see Wizard
     */

    public int getNumSteps() { return 1; }


    /**
     * <p>Tries to identify the 
     *   {@link ru.novosoft.uml.foundation.core.MAssociation MAssociation} that
     *   triggered the critic.</p> 
     *
     * <p>The first time it is called, it will initialise the trigger from the
     *   ToDoItem. If there, it is assumed to be the first trigger of the
     *   ToDoItem and to be an association. If found, the value is stored in
     *   the private field {@link #_triggerAsc}.</p>
     *
     * <p>On all subsequent calls, if a non-null value is found in {@link
     *   #_triggerAsc} that is returned.</p>
     *
     * @return  the {@link ru.novosoft.uml.foundation.core.MAssociation
     *          MAssociation} that triggered the critic, or <code>null</code>
     *          if there was none.
     */

    private MAssociation _getTriggerAsc() {

        // If we don't have it, find the trigger. If this fails it will keep
        // its default value of null

        if ((_triggerAsc == null) && (_item != null)) {
            VectorSet offs = _item.getOffenders();

            if (offs.size() >= 1) {
                _triggerAsc = (MAssociation) offs.elementAt(0);
            }
        }

        return _triggerAsc;
    }


    /**
     * <p>Returns a vector of options to be used in creating a {@link
     *   WizStepChoice} that will exercise the options.</p>
     *
     * <p>We provide five options, shared aggregation in each direction,
     *   composite aggregation in each direction and no aggregation at all.</p>
     *
     * <p>It is possible that a very malicious user could delete the triggering
     *   association just before we get to this point. For now we don't bother
     *   to trap this. It will raise an exception, and then everything will
     *   carry on happily.</p>
     *
     * @return  A {@link Vector} of the options or <code>null</code> if the
     *          association that triggered the critic is no longer there.
     */

    private Vector _buildOptions() {

        // The association that triggered the critic. Its just possible the
        // association is no longer there, in which case we return null

        MAssociation asc = _getTriggerAsc();

        if ( asc == null ) {
            return null;
        }

        // A vector in which to build the result

        Vector res = new Vector();

        // Get the ends from the association (we know there are two), and the
        // types associated with them.

        Iterator iter = asc.getConnections().iterator();

        MAssociationEnd ae0 = (MAssociationEnd) iter.next();
        MAssociationEnd ae1 = (MAssociationEnd) iter.next();

        MClassifier cls0 = ae0.getType();
        MClassifier cls1 = ae1.getType();

        // Get the names of the two ends. If there are none (i.e they are
        // currently anonymous), use the ArgoUML convention of "(anon)" for the
        // names

        String start = "(anon)";
        String end   = "(anon)";

        if ((cls0 != null)
	    && (cls0.getName() != null)
	    && (!(cls0.getName().equals(""))))
	{
            start = cls0.getName();
        }

        if ((cls1 != null)
	    && (cls1.getName() != null)
	    && (!(cls1.getName().equals(""))))
	{
            end = cls1.getName();
        }

        // Now create the five options

        res.addElement (start + " is a composite aggregation of " + end);
        res.addElement (start + " is a shared aggregation of " + end);

        res.addElement (end + " is a composite aggregation of " + start);
        res.addElement (end + " is a shared aggregation of " + start);

        res.addElement ("No aggregation");

        return res;
    }

    /**
     * <p>Set the initial instruction string for the choice. May be called by
     *   the creator of the wizard to override the default.</p>
     */

    public void setInstructions(String s) { _instructions = s; }


    /**
     * <p>Create a {@link JPanel} for the given step.</p>
     *
     * <p>We use a {@link WizStepChoice} to handle the choice selection for the
     *   user. We only create the panel once, saving it in a private field
     *   (<code>_step1Choice</code>) for subsequent use.</p>
     *
     * <p><em>Note</em>. If the association has been deleted, then we may not
     *   be able to create a vector of options. Under these circumstances we
     *   also return null.</p>
     *
     * @param  newStep  The index of the step for which a panel is needed.
     *
     * @return          The created {@link JPanel} or <code>null</code> if no
     *                  options were available.
     *
     * @see Wizard
     */

    public JPanel makePanel(int newStep) {

        switch (newStep) {

        case 1:

            // First step. Create the panel if not already done and options are
            // available. Otherwise it retains its default value of null.

            if (_step1Choice == null) {
                Vector opts = _buildOptions();

                if (opts != null) {
                    _step1Choice = new WizStepChoice(this, _instructions,
                                                     opts);
                    _step1Choice.setTarget(_item);
                }
            }

            return _step1Choice;
        }

        // Default (any other step) is to return nothing

        return null;
    }


    /**
     * <p>Take action at the completion of a step.</p>
     *
     * <p>The guideline for ArgoUML non-modal wizards is to act immediately,
     *   not wait for the finish. This method may also be invoked when finish
     *   is triggered for any steps whose panels didn't get created.</p>
     *
     * <p>The observation is that this seems to be trigged when there is any
     *   change on the panel (e.g choosing an option), not just when "next" is
     *   pressed. Coded accordingly</p>
     *
     * <p>We allow for the association that caused the problem having by now
     *   been deleted, and hence an exception may be raised. We catch this
     *   politely.</p>
     *
     * @param  oldStep  The index of the step just completed (0 for the first
     *                  information panel)
     *
     * @see Wizard
     */

    public void doAction(int oldStep) {

        switch (oldStep) {

        case 1:

            // Just completed the first step where we make our choices. First
            // see if we have a choice. We always should, so print a rude
            // message if we don't

            int choice = -1;

            if (_step1Choice != null) {
                choice = _step1Choice.getSelectedIndex();
            }

            if (choice == -1) {
                cat.warn("WizAssocComposite: nothing selected, " +
                                   "should not get here");
                return;
            }

            // It is quite possible that the cause of the problem has by now
            // been deleted, in which case we will throw an exception if we try
            // to change things. Catch this tidily.

            try {

                // Set the appropriate aggregation on each end

                Iterator iter = _getTriggerAsc().getConnections().iterator();

                MAssociationEnd ae0 = (MAssociationEnd) iter.next();
                MAssociationEnd ae1 = (MAssociationEnd) iter.next();

                switch (choice) {

                case 0:

                    // Start is a composite aggregation of end

                    ae0.setAggregation(MAggregationKind.COMPOSITE);
                    ae1.setAggregation(MAggregationKind.NONE);
                    break;

                case 1:

                    // Start is a shared aggregation of end

                    ae0.setAggregation(MAggregationKind.AGGREGATE);
                    ae1.setAggregation(MAggregationKind.NONE);
                    break;

                case 2:

                    // End is a composite aggregation of start

                    ae0.setAggregation(MAggregationKind.NONE);
                    ae1.setAggregation(MAggregationKind.COMPOSITE);
                    break;

                case 3:

                    // End is a shared aggregation of start

                    ae0.setAggregation(MAggregationKind.NONE);
                    ae1.setAggregation(MAggregationKind.AGGREGATE);
                    break;

                case 4:

                    // No aggregation

                    ae0.setAggregation(MAggregationKind.NONE);
                    ae1.setAggregation(MAggregationKind.NONE);
                    break;
                }
            }
            catch (Exception pve) {

                // Someone took our association away.

                cat.error("WizAssocComposite: could not set " +
                                   "aggregation.", pve); 
            }
        }
    }

    /**
     * <p>Determine if we have sufficient information to finish.</p>
     *
     * <p>We can't finish if our parent {@link Wizard} can't finish.</p>
     *
     * <p>We can finish if we're on step 0.</p>
     *
     * <p>We can finish if we're on step 1 and have made a choice.</p>
     *
     * @return  <code>true</code> if we can finish, otherwise
     *          <code>false</code>.
     *
     * @see Wizard
     */

    public boolean canFinish() {

        // Can't finish if our parent can't

        if (!super.canFinish()) {
            return false;
        }

        // Can finish if it's step 0

        if (_step == 0) {
            return true;
        }

        // Can finish if we're on step1 and have actually made a choice

        if ((_step == 1) && 
            (_step1Choice != null) &&
            (_step1Choice.getSelectedIndex() != -1)) {
            return true;
        }

        // Otherwise we can't finish

        return false;
    }


} /* end class WizAssocComposite */
