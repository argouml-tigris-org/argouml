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

// File: CrOppEndVsAttr.java
// Classes: CrOppEndVsAttr
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.ModelFacade;
import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClassifier;
import ru.novosoft.uml.foundation.core.MStructuralFeature;

/** Well-formedness rule [2] for MClassifier. See page 29 of UML 1.1
 *  Semantics. OMG document ad/97-08-04. */

//TODO: split into one critic for inherited problems and
//one for pproblems directly in this class.
public class CrOppEndVsAttr extends CrUML {

    public CrOppEndVsAttr() {
        setHeadline("Rename Role or MAttribute");
        addSupportedDecision(CrUML.decINHERITANCE);
        addSupportedDecision(CrUML.decRELATIONSHIPS);
        addSupportedDecision(CrUML.decNAMING);
        setKnowledgeTypes(Critic.KT_SYNTAX);
        addTrigger("associationEnd");
        addTrigger("structuralFeature");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(ModelFacade.isAClassifier(dm)))
            return NO_PROBLEM;
        MClassifier cls = (MClassifier) dm;
        Vector namesSeen = new Vector();
        Collection str = cls.getFeatures();

        Iterator enum = str.iterator();

        // warn about inheritied name conflicts, different critic?
        while (enum.hasNext()) {
            Object o = enum.next();

            if (!(ModelFacade.isAStructuralFeature(o)))
                continue;

            MStructuralFeature sf = (MStructuralFeature) o;

            String sfName = sf.getName();
            if ("".equals(sfName))
                continue;

            String nameStr = sfName;
            if (nameStr.length() == 0)
                continue;

            namesSeen.addElement(nameStr);

        }

        Collection assocEnds = cls.getAssociationEnds();

        enum = assocEnds.iterator();
        // warn about inheritied name conflicts, different critic?
        while (enum.hasNext()) {
            MAssociationEnd myAe = (MAssociationEnd) enum.next();
            MAssociation asc = (MAssociation) myAe.getAssociation();
            Collection conn = asc.getConnections();

            if (ModelFacade.isAAssociationRole(asc))
                conn = ModelFacade.getConnections(asc);
            if (conn == null)
                continue;

            Iterator enum2 = conn.iterator();
            while (enum2.hasNext()) {
                MAssociationEnd ae = (MAssociationEnd) enum2.next();
                if (ae.getType() == cls)
                    continue;
                String aeName = ae.getName();
                if ("".equals(aeName))
                    continue;
                String aeNameStr = aeName;
                if (aeNameStr == null || aeNameStr.length() == 0)
                    continue;

                if (namesSeen.contains(aeNameStr))
                    return PROBLEM_FOUND;
            }
        }
        return NO_PROBLEM;
    }
} /* end class CrOppEndVsAttr.java */