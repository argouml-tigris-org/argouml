// $Id:WizTooMany.java 11576 2006-12-10 17:02:26Z tfmorris $
// Copyright (c) 2003-2006 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ui.WizStepTextField;
import org.argouml.i18n.Translator;

/** 
 * A wizard which adjust the threshold for critics. <p>
 * 
 * TODO: Make the treshold setting adjustable without using the wizard, 
 * and make it persistent. 
 * Maybe by using the "Configure" button on the Browse Critics dialog.
 *
 * @see org.argouml.uml.cognitive.critics.AbstractCrTooMany
 * @author mkl
 *
 */
public class WizTooMany extends UMLWizard {

    private String instructions = 
        Translator.localize("critics.WizTooMany-ins");
        
    private WizStepTextField step1;

    /**
     *
     */
    public WizTooMany() {
        super();
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#getNumSteps()
     */
    public int getNumSteps() {
        return 1;
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#makePanel(int)
     */
    public JPanel makePanel(int newStep) {
        switch (newStep) {
        case 1:
            if (step1 == null) {
                ToDoItem item = (ToDoItem) getToDoItem();
                AbstractCrTooMany critic = (AbstractCrTooMany) item.getPoster();
                step1 = new WizStepTextField(this, instructions, "Threshold",
                        Integer.toString(critic.getThreshold()));
            }
            return step1;
        }
        return null;
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#canFinish()
     */
    public boolean canFinish() {
        if (!super.canFinish()) return false;
        if (getStep() == 0) return true;
        if (getStep() == 1 && step1 != null) {
            try {
                Integer.parseInt(step1.getText());
                return true;
            } catch (NumberFormatException ex) {
                // intentional: if there is nonsense in the field,
                // we return false
            }
        }
        return false;
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
        switch (oldStep) {
        case 1:
            String newThreshold;
            ToDoItem item = (ToDoItem) getToDoItem();
            AbstractCrTooMany critic = (AbstractCrTooMany) item.getPoster();
            if (step1 != null) {
                newThreshold = step1.getText();
                try {
                    critic.setThreshold(Integer.parseInt(newThreshold));
                } catch (NumberFormatException ex) {
                    // intentional: if there is nonsense in the field,
                    // we do not set the value
                }
            }
            break;
        }
    }
}
