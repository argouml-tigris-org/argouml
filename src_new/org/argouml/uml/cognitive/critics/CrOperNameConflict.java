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



// File: CrOperNameConflict.java
// Classes: CrOperNameConflict
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.cognitive.critics;

import java.util.*;
import javax.swing.*;

import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.data_types.*;

import org.argouml.cognitive.*;
import org.argouml.cognitive.critics.*;

public class CrOperNameConflict extends CrUML {

    public CrOperNameConflict() {
        setHeadline("Change Names or Signatures in <ocl>self</ocl>");
        addSupportedDecision(CrUML.decMETHODS);
        addSupportedDecision(CrUML.decNAMING);
        setKnowledgeTypes(Critic.KT_SYNTAX);
        addTrigger("behavioralFeature");
        addTrigger("feature_name");
    }

    public boolean predicate2(Object dm, Designer dsgr) {
        if (!(dm instanceof MClassifier)) return NO_PROBLEM;
        MClassifier cls = (MClassifier) dm;
        Collection str = cls.getFeatures();
        if (str == null) return NO_PROBLEM;
        Iterator enum = str.iterator();
        Vector operSeen = new Vector();
        // warn about inheritied name conflicts, different critic?
        while (enum.hasNext()) {
            MFeature f = (MFeature) enum.next();
            if (!(f instanceof MOperation))
                continue;
            MBehavioralFeature bf = (MBehavioralFeature) f;
            int size = operSeen.size();
            for (int i = 0; i < size; i++) {
                MBehavioralFeature otherBF = (MBehavioralFeature) operSeen.elementAt(i);
                if (signaturesMatch(bf, otherBF)) return PROBLEM_FOUND;
            }
            operSeen.addElement(bf);
        }
        return NO_PROBLEM;
    }


    public boolean signaturesMatch(MBehavioralFeature bf1, MBehavioralFeature bf2) {
        String name1 = bf1.getName();
        String name2 = bf2.getName();
        if (name1 == null || name2 == null) return false;
        if (!name1.equals(name2)) return false;
        List params1 = bf1.getParameters();
        List params2 = bf2.getParameters();
        int size1 = params1.size();
        int size2 = params2.size();
        if (size1 != size2) return false;
        for (int i = 0; i < size1; i++) {
            MParameter p1 = (MParameter) params1.get(i);
            MParameter p2 = (MParameter) params2.get(i);
            String p1Name = p1.getName();
            String p2Name = p2.getName();
            if (p1Name == null || p2Name == null) return false;
            if (!p1Name.equals(p2Name)) return false;
            MClassifier p1Type = p1.getType();
            MClassifier p2Type = p2.getType();
            if (p1Type == null || p2Type == null) return false;
            if (!p1Type.equals(p2Type)) return false;
        }

        return true;
    }

    public Icon getClarifier() {
        return ClOperationCompartment.TheInstance;
    }

} /* end class CrOperNameConflict.java */








