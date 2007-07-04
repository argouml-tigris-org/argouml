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

import org.argouml.cognitive.Critic;
import org.argouml.cognitive.Designer;
import org.argouml.model.Model;
import org.argouml.uml.cognitive.UMLDecision;

/**
 * A critic to detect when a classifier might require
 * associations. It checks for inherited associations as well and
 * keeps silent if it finds any.  For usecases it checks the
 * extend/include relationships as well.
 * If the classifier has dependencies defined, then the critic 
 * remains silent (see issue 1129).
 */
public class CrNoAssociations extends CrUML {

    /**
     * Constructor.
     */
    public CrNoAssociations() {
        setupHeadAndDesc();
        addSupportedDecision(UMLDecision.RELATIONSHIPS);
        setKnowledgeTypes(Critic.KT_COMPLETENESS);
        addTrigger("associationEnd");
    }

    /**
     * Decide whether the given design material causes a problem.
     *
     * @param dm the object to criticize
     * the designer who decides the design process
     * @param dsgr the designer
     * @return <CODE>PROBLEM_FOUND</CODE> if there is a problem,
     *         otherwise <CODE>NO_PROBLEM</CODE>
     */
    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(Model.getFacade().isAClassifier(dm)))
            return NO_PROBLEM;
        if (!(Model.getFacade().isPrimaryObject(dm)))
            return NO_PROBLEM;

        // If the classifier does not have a name,
        // then no problem - the model is not finished anyhow.
        if ((Model.getFacade().getName(dm) == null)
	    || ("".equals(Model.getFacade().getName(dm)))) {
            return NO_PROBLEM;
	}

        // Abstract elements do not necessarily require associations
        if (Model.getFacade().isAGeneralizableElement(dm)
	    && Model.getFacade().isAbstract(dm)) {
            return NO_PROBLEM;
        }

        // Types can probably have associations, but we should not nag at them
        // not having any.
        // utility is a namespace collection - also not strictly required
        // to have associations.
        if (Model.getFacade().isType(dm))
            return NO_PROBLEM;
        if (Model.getFacade().isUtility(dm))
            return NO_PROBLEM;

        // See issue 1129: If the classifier has dependencies,
        // then mostly there is no problem. 
        if (Model.getFacade().getClientDependencies(dm).size() > 0)
            return NO_PROBLEM;
        if (Model.getFacade().getSupplierDependencies(dm).size() > 0)
            return NO_PROBLEM;
        
        // special cases for use cases
        // Extending use cases and use case that are being included are
        // not required to have associations.
        if (Model.getFacade().isAUseCase(dm)) {
            Object usecase = dm;
            Collection includes = Model.getFacade().getIncludes(usecase);
            if (includes != null && includes.size() >= 1) {
                return NO_PROBLEM;
            }
            Collection extend = Model.getFacade().getExtends(usecase);
            if (extend != null && extend.size() >= 1) {
                return NO_PROBLEM;
            }
        }
        
        

        //TODO: different critic or special message for classes
        //that inherit all ops but define none of their own.

        if (findAssociation(dm, 0))
            return NO_PROBLEM;
        return PROBLEM_FOUND;
    }

    /**
     * @param dm The classifier to examine.
     * @param depth Number of levels searched.
     * @return true if an association can be found in this classifier
     *		or in any of its generalizations.
     */
    private boolean findAssociation(Object dm, int depth) {
        if (Model.getFacade().getAssociationEnds(dm).iterator().hasNext())
            return true;

        if (depth > 50)
            return false;

        Iterator iter = Model.getFacade().getGeneralizations(dm).iterator();

        while (iter.hasNext()) {
            Object parent = Model.getFacade().getParent(iter.next());

            if (parent == dm)
                continue;

            if (Model.getFacade().isAClassifier(parent))
                if (findAssociation(parent, depth + 1))
                    return true;
        }

        if (Model.getFacade().isAUseCase(dm)) {
            // for use cases we need to check for extend/includes
            // actors cannot have them, so no need to check
            Iterator iter2 = Model.getFacade().getExtends(dm).iterator();
            while (iter2.hasNext()) {
                Object parent = Model.getFacade().getExtension(iter2.next());

                if (parent == dm)
                    continue;

                if (Model.getFacade().isAClassifier(parent))
                    if (findAssociation(parent, depth + 1))
                        return true;
            }

            Iterator iter3 = Model.getFacade().getIncludes(dm).iterator();
            while (iter3.hasNext()) {
                Object parent = Model.getFacade().getBase(iter3.next());

                if (parent == dm)
                    continue;

                if (Model.getFacade().isAClassifier(parent))
                    if (findAssociation(parent, depth + 1))
                        return true;
            }
        }
        return false;
    }

} /* end class CrNoAssociations */
