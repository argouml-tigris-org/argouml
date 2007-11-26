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

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import org.apache.log4j.Logger;
import org.argouml.cognitive.ui.WizStepManyTextFields;
import org.argouml.i18n.Translator;
import org.argouml.model.Model;

/**
 * A non-modal wizard to help the user change the name of a ModelElement to a
 * better name.
 * 
 * @author jrobbins
 */
public class WizManyNames extends UMLWizard {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(WizManyNames.class);

    /**
     * The text that describes what to be done.
     */
    private String instructions = Translator
            .localize("critics.WizManyNames-ins");

    /**
     * A list of model elements.
     */
    private List mes;

    private WizStepManyTextFields step1;

    /**
     * The constructor.
     * 
     */
    public WizManyNames() {
    }

    /**
     * @param m
     *            the offenders
     * @deprecated for 0.25.4 by tfmorris. Use {@link #setModelElements(List)}.
     */
    @Deprecated
    public void setMEs(Vector m) {
        setModelElements(m);
    }

    /**
     * Set the list of offending ModelElements.
     * 
     * @param elements the list of offending ModelElements
     */
    public void setModelElements(List elements) {
        int mSize = elements.size();
        for (int i = 0; i < 3 && i < mSize; ++i) {
            if (!Model.getFacade().isAModelElement(elements.get(i))) {
                throw new IllegalArgumentException(
                        "The list should contain model elements in "
                                + "the first 3 positions");
            }
        }

        mes = elements;
    }
    
    /*
     * @see org.argouml.cognitive.ui.Wizard#makePanel(int)
     */
    public JPanel makePanel(int newStep) {
        switch (newStep) {
        case 1:
            if (step1 == null) {
                List<String> names = new ArrayList<String>();
                int size = mes.size();
                for (int i = 0; i < size; i++) {
                    Object me = mes.get(i);
                    names.add(Model.getFacade().getName(me));
                }
                step1 = new WizStepManyTextFields(this, instructions, names);
            }
            return step1;

        default:
        }
        return null;
    }

    /*
     * @see org.argouml.cognitive.ui.Wizard#doAction(int)
     */
    public void doAction(int oldStep) {
        LOG.debug("doAction " + oldStep);
        switch (oldStep) {
        case 1:
            List<String> newNames = null;
            if (step1 != null) {
                newNames = step1.getStringList();
            }
            try {
                int size = mes.size();
                for (int i = 0; i < size; i++) {
                    Object me = mes.get(i);
                    Model.getCoreHelper().setName(me, newNames.get(i));
                }
            } catch (Exception pve) {
                LOG.error("could not set name", pve);
            }
            break;

        default:
        }
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = -2827847568754795770L;
}
