// Copyright (c) 1996-2002 The Regents of the University of California. All
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


package org.argouml.uml.ui;

import org.argouml.model.uml.UmlFactory;

import java.util.*;
import java.awt.*;

import javax.swing.*;

import ru.novosoft.uml.*;
import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;

/**
* An UMLAssociationEndListModel is used with {@link UMLList} to present a list
* of association ends that are connected with a classifier.
*
* @author Curt Arnold
* @see UMLModelElementListModel
* @see org.argouml.uml.ui.foundation.core.PropPanelClass
* @see org.argouml.uml.ui.foundation.core.PropPanelInterface
* @see UMLList
*/
public class UMLAssociationEndListModel extends UMLModelElementListModel  {

    final private static String _nullLabel = "null";
    
    /**
     *   Creates a new association end list model
     *   @param container the container (typically a PropPanelClass or PropPanelInterface)
     *                    that provides access to the target classifier.
     *   @param property  a string that specifies the name of an event that should force a refresh
     *                       of the list model.  A null value will cause all events to trigger a refresh.
     *   @param showNone  if true, an element labelled "none" will be shown where there are
     *                        no association ends connected to the classifier.
     */
    public UMLAssociationEndListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }
    
    private Collection getAssociationEnds() {
        Collection assocEnds = null;
        Object target = getTarget();
        if(target instanceof MAssociation) {
            assocEnds = ((MAssociation) target).getConnections();
        }
        return assocEnds;
    }

    /**
     * @see org.argouml.uml.ui.UMLModelElementListModel#recalcModelElementSize()
     */
    protected int recalcModelElementSize() {
        int size = 0;
        Collection assocEnds = getAssociationEnds();
        if(assocEnds != null) {
            size = assocEnds.size();
        }
        return size;
    }
    
    //  see superclass documentation
    protected MModelElement getModelElementAt(int index) {
        return elementAtUtil(getAssociationEnds(),index,MAssociationEnd.class);
    }
    
    //  see superclass documentation
    public boolean buildPopup(JPopupMenu popup,int index) {
        UMLUserInterfaceContainer container = getContainer();
        UMLListMenuItem open = new UMLListMenuItem(container.localize("Open"),this,"open",index);
        int size = getModelElementSize();
        if(size <= 0) {
            open.setEnabled(false);
        }
           
        return true;
    }

}


