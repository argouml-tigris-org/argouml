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

import ru.novosoft.uml.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.model_management.*;

/**
 *   This class implements a list model for the subpackages of a package.
 *   Used with a UMLList to display a list of attributes.  Since packages
 *   may be mixed with other model elements,
 *   this class implements a cache of packages that are kept synchronized
 *   with the owned elements.
 *
 *   @author Curt Arnold
 *   @see UMLModelElementListModel
 *   @see UMLList
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by ?,
 *             this class is part of the 'old'(pre 0.13.*) implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLGeneralizationsListModel extends UMLOwnedElementListModel  {

    /**
     *   Creates a new packages list model
     *   @param container the container (typically a PropPanelPackage)
     *                    that provides access to the target classifier.
     *   @param property  a string that specifies the name of an event that should force a refresh
     *                       of the list model.  A null value will cause all events to trigger a refresh.
     *   @param showNone  if true, an element labelled "none" will be shown where there are
     *                        no actual entries in the list.
     */    
    public UMLGeneralizationsListModel(UMLUserInterfaceContainer container,String property,boolean showNone) {
        super(container,property,showNone);
    }
    
    /**
     *   Called to determine if a particular feauture of the underlying collection
     *   should be in the cached list of model elements.
     *   @param obj object to be considered.
     *   @return true if object is appropriate for this list.
     */
    public boolean isProperClass(Object obj) {
        return obj instanceof MGeneralization;
    }

    public MModelElement createModelElement() {
        return UmlFactory.getFactory().getCore().createGeneralization();
    }
    
}


