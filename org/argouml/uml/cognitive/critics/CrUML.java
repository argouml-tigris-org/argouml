// $Id$
// Copyright (c) 1996-2005 The Regents of the University of California. All
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

// $Id$
package org.argouml.uml.cognitive.critics;

import org.apache.log4j.Logger;
import org.argouml.cognitive.Decision;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.critics.Critic;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.ocl.CriticOclEvaluator;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.tigris.gef.ocl.ExpansionException;

/**
 * "Abstract" Critic subclass that captures commonalities among all
 * critics in the UML domain.  This class also defines and registers
 * the categories of design decisions that the critics can
 * address.
 *
 * @see org.argouml.cognitive.Designer
 * @see org.argouml.cognitive.DecisionModel
 *
 * @author jrobbins
 */
public class CrUML extends Critic {
    private static final Logger LOG = Logger.getLogger(CrUML.class);

    /**
     * Decision type: INHERITANCE.
     */
    public static final Decision DEC_INHERITANCE =
	new Decision("misc.decision.inheritance", 1);

    /**
     * Decision type: CONTAINMENT.
     */
    public static final Decision DEC_CONTAINMENT =
	new Decision("misc.decision.containment", 1);

    /**
     * Decision type: PATTERNS.
     */
    public static final Decision DEC_PATTERNS =
	new Decision("misc.decision.design-patterns", 1); //??

    /**
     * Decision type: RELATIONSHIPS.
     */
    public static final Decision DEC_RELATIONSHIPS =
	new Decision("misc.decision.relationships", 1);

    /**
     * Decision type: STORAGE.
     */
    public static final Decision DEC_STORAGE =
	new Decision("misc.decision.storage", 1);

    /**
     * Decision type: BEHAVIOR.
     */
    public static final Decision DEC_BEHAVIOR =
	new Decision("misc.decision.behavior", 1);

    /**
     * Decision type: INSTANCIATION.
     */
    public static final Decision DEC_INSTANCIATION =
	new Decision("misc.decision.instantiation", 1);

    /**
     * Decision type: NAMING.
     */
    public static final Decision DEC_NAMING =
	new Decision("misc.decision.naming", 1);

    /**
     * Decision type: MODULARITY.
     */
    public static final Decision DEC_MODULARITY =
	new Decision("misc.decision.modularity", 1);

    /**
     * Decision type: CLASS_SELECTION.
     */
    public static final Decision DEC_CLASS_SELECTION =
	new Decision("misc.decision.class-selection", 1);

    /**
     * Decision type: EXPECTED_USAGE.
     */
    public static final Decision DEC_EXPECTED_USAGE =
	new Decision("misc.decision.expected-usage", 1);

    /**
     * Decision type: METHODS.
     */
    public static final Decision DEC_METHODS =
	new Decision("misc.decision.methods", 1); //??

    /**
     * Decision type: CODE_GEN.
     */
    public static final Decision DEC_CODE_GEN =
	new Decision("misc.decision.code-generation", 1); //??

    /**
     * Decision type: PLANNED_EXTENSIONS.
     */
    public static final Decision DEC_PLANNED_EXTENSIONS =
	new Decision("misc.decision.planned-extensions", 1);

    /**
     * Decision type: STEREOTYPES.
     */
    public static final Decision DEC_STEREOTYPES =
	new Decision("misc.decision.stereotypes", 1);

    /**
     * Decision type: STATE_MACHINES.
     */
    public static final Decision DEC_STATE_MACHINES =
	new Decision("misc.decision.mstate-machines", 1);

    /**
     * Static initializer for this class. Called when the class is
     * loaded (which is before any subclass instances are instanciated).
     */
    static {
	Designer d = Designer.theDesigner();
	d.startConsidering(DEC_CLASS_SELECTION);
        d.startConsidering(DEC_BEHAVIOR);
	d.startConsidering(DEC_NAMING);
	d.startConsidering(DEC_STORAGE);
	d.startConsidering(DEC_INHERITANCE);
	d.startConsidering(DEC_CONTAINMENT);
	d.startConsidering(DEC_PLANNED_EXTENSIONS);
	d.startConsidering(DEC_STATE_MACHINES);
	d.startConsidering(DEC_PATTERNS);
	d.startConsidering(DEC_RELATIONSHIPS);
	d.startConsidering(DEC_INSTANCIATION);
	d.startConsidering(DEC_MODULARITY);
	d.startConsidering(DEC_EXPECTED_USAGE);
	d.startConsidering(DEC_METHODS);
	d.startConsidering(DEC_CODE_GEN);
	d.startConsidering(DEC_STEREOTYPES);
    }


    /**
     * The constructor for this class.
     */
    public CrUML() {
    }

    /**
     * Set the resources for this critic based on the class name.
     *
     * @param key is the class name.
     */
    public void setResource(String key) {
        // String head = Translator.localize("Cognitive", key + "_head");
        String head = Translator.localize("critics." + key + "-head");
        super.setHeadline(head);
        // String desc = Translator.localize("Cognitive", key + "_desc");
        String desc = Translator.localize("critics." + key + "-desc");
        super.setDescription(desc);
    }

    /**
     * Set up the locale specific text for the critic headline
     * (the one liner that appears in the to-do pane)
     * and the critic description (the detailed explanation that
     * appears in the to-do tab of the details pane).
     *
     * MVW: Maybe we can make it part of the constructor CrUML()?
     *
     * @deprecated by mvw, as of ArgoUML V0.17.5.
     * Since the parameter is ignored, replaced by {@link #setupHeadAndDesc()}.
     *
     * @see org.argouml.cognitive.critics.Critic#setHeadline(java.lang.String)
     */
    public final void setHeadline(String s) {
	//
	//   current implementation ignores the argument
	//     and triggers setResource()
	String className = getClass().getName();
	setResource(className.substring(className.lastIndexOf('.') + 1));
    }

    /**
     * Set up the locale specific text for the critic headline
     * (the one liner that appears in the to-do pane)
     * and the critic description (the detailed explanation that
     * appears in the to-do tab of the details pane).
     */
    public final void setupHeadAndDesc() {
        String className = getClass().getName();
        setResource(className.substring(className.lastIndexOf('.') + 1));
    }


    /**
     * @see org.argouml.cognitive.critics.Critic#predicate(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate(Object dm, Designer dsgr) {
	Project p = ProjectManager.getManager().getCurrentProject();
	if (p.isInTrash(dm)) {
	    return NO_PROBLEM;
	} else {
	    return predicate2(dm, dsgr);
	}
    }

    /**
     * This is the decision routine for the critic.
     *
     * @param dm is the UML entity (an NSUML object) that is being checked.
     * @param dsgr is for future development and can be ignored.
     *
     * @return boolean problem found
     */
    public boolean predicate2(Object dm, Designer dsgr) {
	return super.predicate(dm, dsgr);
    }

    ////////////////////////////////////////////////////////////////
    // display related methods
    private static final String OCL_START = "<ocl>";
    private static final String OCL_END = "</ocl>";

    /**
     * Expand text with ocl brackets in it.
     * No recursive expansion.
     *
     * @return the expanded text
     * @param res is the text to expand.
     * @param offs is the elements to replace
     */
    public String expand(String res, ListSet offs) {

        if (offs.size() == 0) {
	    return res;
	}

        Object off1 = offs.firstElement();

        StringBuffer beginning = new StringBuffer("");
        int matchPos = res.indexOf(OCL_START);

        // replace all occurances of OFFENDER with the name of the
        // first offender
        while (matchPos != -1) {
            int endExpr = res.indexOf(OCL_END, matchPos + 1);
            // check if there is no OCL_END; if so, the critic expression
            // is not correct and can not be expanded
            if (endExpr == -1) {
                break;
            }
            if (matchPos > 0) {
                beginning.append(res.substring(0, matchPos));
            }
            String expr = res.substring(matchPos + OCL_START.length(), endExpr);
            String evalStr = null;
            try {
                evalStr =
		    CriticOclEvaluator.getInstance().evalToString(off1, expr);
            } catch (ExpansionException e) {
                // Really ought to have a CriticException to throw here.
                LOG.error("Failed to evaluate critic expression", e);
            }
            if (expr.endsWith("") && evalStr.equals("")) {
                evalStr = "(anon)";
            }
            beginning.append(evalStr);
            res = res.substring(endExpr + OCL_END.length());
            matchPos = res.indexOf(OCL_START);
        }
        if (beginning.length() == 0) {
            // This is just to avoid creation of a new
            return res;		// string when not needed.
        } else {
            return beginning.append(res).toString();
        }
    }

    /**
     * create a new UMLToDoItem.
     *
     * @see org.argouml.uml.cognitive.UMLToDoItem
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	return new UMLToDoItem(this, dm, dsgr);
    }

} /* end class CrUML */
