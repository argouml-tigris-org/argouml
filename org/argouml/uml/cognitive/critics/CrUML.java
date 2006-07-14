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

import org.apache.log4j.Logger;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.ListSet;
import org.argouml.cognitive.ToDoItem;
import org.argouml.cognitive.critics.Critic;
import org.argouml.i18n.Translator;
import org.argouml.kernel.Project;
import org.argouml.kernel.ProjectManager;
import org.argouml.model.Model;
import org.argouml.ocl.CriticOclEvaluator;
import org.argouml.uml.cognitive.UMLToDoItem;
import org.tigris.gef.ocl.ExpansionException;

/**
 * "Abstract" Critic subclass that captures commonalities among all
 * critics in the UML domain.  This class also defines and registers
 * the categories of design decisions that the critics can
 * address. IT also deals with particular UMLToDoItems.
 *
 * @see org.argouml.cognitive.Designer
 * @see org.argouml.cognitive.DecisionModel
 *
 * @author jrobbins
 */
public class CrUML extends Critic {
    /**
     * Logger.
     */
    private static final Logger LOG = Logger.getLogger(CrUML.class);

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
     * Loads the localized wizard's instruction 
     * 
     * @param suffix the suffix of the key
     * @return the instructions
     */
    protected String getLocalizedString(String suffix) {
        // TODO: The logic below could be replaced by getClass().getSimpleName()
        // (when Argo will support only java versions > 1.5)
        String className = getClass().getName();
        return Translator.localize("critics."
                + className.substring(className.lastIndexOf('.') + 1) + suffix);
    }
    
    /**
     * Loads the localized wizard's instruction 
     * 
     * @return the instructions
     */
    protected String getInstructions() {
        return this.getLocalizedString("-ins");
    }
    
    /**
     * Loads the localized wizard's default suggestion
     * 
     * @return the default suggestion
     */
    protected String getDefaultSuggestion() {
        // TODO: The logic below could be replaced by getClass().getSimpleName()
        // (when Argo will support only java versions > 1.5)
        String className = getClass().getName();
        return Translator.localize("critics."
                + className.substring(className.lastIndexOf('.') + 1) + "-sug");
    }

    /**
     * @see org.argouml.cognitive.critics.Critic#setHeadline(java.lang.String)
     *
     * Set up the locale specific text for the critic headline
     * (the one liner that appears in the to-do pane)
     * and the critic description (the detailed explanation that
     * appears in the to-do tab of the details pane).
     *
     * MVW: Maybe we can make it part of the constructor CrUML()?
     */
    public final void setHeadline(String s) {
        setupHeadAndDesc();
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
        if (p.isInTrash(dm)
                || (Model.getFacade().isAModelElement(dm)
                && Model.getUmlFactory().isRemoved(dm))) {
            return NO_PROBLEM;
        } else {
            return predicate2(dm, dsgr);
        }
    }

    /**
     * This is the decision routine for the critic.
     *
     * @param dm is the UML entity that is being checked.
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
     * @see org.argouml.cognitive.critics.Critic#toDoItem(Object, Designer)
     */
    public ToDoItem toDoItem(Object dm, Designer dsgr) {
	return new UMLToDoItem(this, dm, dsgr);
    }

    /**
     * The UID.
     */
    private static final long serialVersionUID = 1785043010468681602L;
} /* end class CrUML */
