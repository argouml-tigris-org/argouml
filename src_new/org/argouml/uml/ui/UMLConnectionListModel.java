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

// 3 May 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to mark the
// project as needing saving if an association is added, deleted, changed or
// moved.

package org.argouml.uml.ui;

import ru.novosoft.uml.*;
import javax.swing.*;
import ru.novosoft.uml.foundation.core.*;
import java.util.*;
import java.awt.*;

import org.argouml.ui.*;
import org.argouml.kernel.*;


public class UMLConnectionListModel extends UMLModelElementListModel  {

    private final static String _nullLabel = "(null)";

    public UMLConnectionListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }

    protected int recalcModelElementSize() {
        int size = 0;
        Collection connections = getConnections();
        if(connections != null) {
            size = connections.size();
        }
        return size;
    }

    protected MModelElement getModelElementAt(int index) {
        MModelElement elem = null;
        Collection connections = getConnections();
        if(connections != null) {
            elem = elementAtUtil(connections,index,MAssociationEnd.class);
            if(elem != null) {
              elem = ((MAssociationEnd) elem).getAssociation();
            }
        }
        return elem;
    }


    private Collection getConnections() {
        Collection connections = null;
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            connections = classifier.getAssociationEnds();
        }
        return connections;
    }


    public void open(int index) {
        MModelElement assoc = getModelElementAt(index);
        if(assoc != null) {
            navigateTo(assoc);
        }
    }

    public void add(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MAssociationEnd newEnd = new MAssociationEndImpl();
            newEnd.setType(classifier);
            classifier.addAssociationEnd(newEnd);

            MAssociation newAssoc = new MAssociationImpl();
            newAssoc.setNamespace(((MClassifier) target).getNamespace());
            newEnd.setAssociation(newAssoc);
            newAssoc.addConnection(newEnd);
            MAssociationEnd otherEnd = new MAssociationEndImpl();
            newAssoc.addConnection(otherEnd);

            // Having added an association, mark as needing saving

            Project p = ProjectBrowser.TheInstance.getProject();
            p.setNeedsSave(true);

            fireIntervalAdded(this,index,index);
            navigateTo(newAssoc);
        }
    }

    public void delete(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            MAssociation assoc = (MAssociation) getModelElementAt(index);
            Collection ends = assoc.getConnections();
            if(ends != null) {
              Iterator iter = ends.iterator();
              while(iter.hasNext()) {
                MAssociationEnd end = (MAssociationEnd) iter.next();
                if(end.getType() == target) {
                  end.setType(null);
                  classifier.removeAssociationEnd(end);
                }
              }
            }

            // Having removed an association, mark as needing saving

            Project p = ProjectBrowser.TheInstance.getProject();
            p.setNeedsSave(true);

            fireIntervalRemoved(this,index,index);
        }
    }

    public void moveUp(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            classifier.setAssociationEnds(moveUpUtil(classifier.getAssociationEnds(),index));

            // Having moved an association, mark as needing saving

            Project p = ProjectBrowser.TheInstance.getProject();
            p.setNeedsSave(true);

            fireContentsChanged(this,index-1,index);
        }
    }

    public void moveDown(int index) {
        Object target = getTarget();
        if(target instanceof MClassifier) {
            MClassifier classifier = (MClassifier) target;
            classifier.setAssociationEnds(moveDownUtil(classifier.getAssociationEnds(),index));

            // Having moved an association, mark as needing saving

            Project p = ProjectBrowser.TheInstance.getProject();
            p.setNeedsSave(true);

            fireContentsChanged(this,index,index+1);
        }
    }

    public void roleAdded(final MElementEvent event) {
        super.roleAdded(event);
    }


}




