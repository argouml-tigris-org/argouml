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

package org.argouml.uml.cognitive.critics;

import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.argouml.cognitive.Designer;
import org.argouml.cognitive.critics.Critic;
import org.argouml.model.ModelFacade;

/**
 * Well-formedness rule [2] for MClassifier. See page 29 of UML 1.1
 * Semantics. OMG document ad/97-08-04.
 *
 * @author jrobbins
 */

//TODO: split into one critic for inherited problems and
//one for pproblems directly in this class.
public class CrOppEndVsAttr extends CrUML {

    /**
     * The constructor.
     *
     */
    public CrOppEndVsAttr() {
        setHeadline("Rename Role or MAttribute");
        addSupportedDecision(CrUML.DEC_INHERITANCE);
        addSupportedDecision(CrUML.DEC_RELATIONSHIPS);
        addSupportedDecision(CrUML.DEC_NAMING);
        setKnowledgeTypes(Critic.KT_SYNTAX);
        addTrigger("associationEnd");
        addTrigger("structuralFeature");
    }

    /**
     * @see org.argouml.uml.cognitive.critics.CrUML#predicate2(
     * java.lang.Object, org.argouml.cognitive.Designer)
     */
    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(ModelFacade.isAClassifier(dm)))
            return NO_PROBLEM;
        Object cls = /*(MClassifier)*/ dm;
        Vector namesSeen = new Vector();
        Collection str = ModelFacade.getFeatures(cls);


        // warn about inheritied name conflicts, different critic?
        Iterator features = str.iterator();
        while (features.hasNext()) {
            Object o = features.next();

            if (!(ModelFacade.isAStructuralFeature(o)))
                continue;

            Object sf = /*(MStructuralFeature)*/ o;

            String sfName = ModelFacade.getName(sf);
            if ("".equals(sfName))
                continue;

            String nameStr = sfName;
            if (nameStr.length() == 0)
                continue;

            namesSeen.addElement(nameStr);

        }

        Collection assocEnds = ModelFacade.getAssociationEnds(cls);

        // warn about inheritied name conflicts, different critic?
        Iterator myEnds = assocEnds.iterator();
        while (myEnds.hasNext()) {
            Object myAe = /*(MAssociationEnd)*/ myEnds.next();
            Object asc = /*(MAssociation)*/ ModelFacade.getAssociation(myAe);
            Collection conn = ModelFacade.getConnections(asc);

            if (ModelFacade.isAAssociationRole(asc))
                conn = ModelFacade.getConnections(asc);
            if (conn == null)
                continue;

            Iterator ascEnds = conn.iterator();
            while (ascEnds.hasNext()) {
                Object ae = /*(MAssociationEnd)*/ ascEnds.next();
                if (ModelFacade.getType(ae) == cls)
                    continue;
                String aeName = ModelFacade.getName(ae);
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
