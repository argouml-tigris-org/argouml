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

package org.argouml.uml.diagram.static_structure.ui;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


import org.argouml.ui.AbstractGoRule;

import ru.novosoft.uml.foundation.core.MAssociation;
import ru.novosoft.uml.foundation.core.MAssociationEnd;
import ru.novosoft.uml.foundation.core.MClass;

public class GoClassToNavigableClass extends AbstractGoRule {

    public String getRuleName() {
        return "Class->Navigable Class";
    }

    public Collection getChildren(Object parent) {
        if (!(parent instanceof MClass))
            return null;
        
        List childClasses = new ArrayList();
        
        Collection ends = ((MClass) parent).getAssociationEnds();
        if (ends == null)
            return null;
        
        java.util.Iterator enum = ends.iterator();
        while (enum.hasNext()) {
            MAssociationEnd ae = (MAssociationEnd) enum.next();
            MAssociation asc = ae.getAssociation();
            Collection allEnds = asc.getConnections();
            
            MAssociationEnd otherEnd = null;
            Iterator endIt = allEnds.iterator();
            if (endIt.hasNext()) {
                otherEnd = (MAssociationEnd) endIt.next();
                if (ae != otherEnd && endIt.hasNext()) {
                    otherEnd = (MAssociationEnd) endIt.next();
                    if (ae != otherEnd)
                        otherEnd = null;
                }
            }
            
            if (otherEnd == null)
                continue;
            if (!otherEnd.isNavigable())
                continue;
            if (childClasses.contains(otherEnd.getType()))
                continue;
            childClasses.add(otherEnd.getType());
            // TODO: handle n-way Associations
        }
        return childClasses;
    }

}
