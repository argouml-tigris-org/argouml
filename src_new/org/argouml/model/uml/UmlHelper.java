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

package org.argouml.model.uml;

import java.util.Collection;
import java.util.Iterator;

import ru.novosoft.uml.MBase;
import ru.novosoft.uml.model_management.MModel;

/**
 * Helper class for UML metamodel.
 *
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class UmlHelper {

    /** Don't allow instantiation.
     */
    private UmlHelper() {
    }

    /** 
     *  Ensures that all of the elements in a model are registered
     *  to the UmlModelListener.  This is useful when the MModel is
     *  not created by the UmlFactory.
     */
    public static void addListenersToModel(MModel model) {
	addListenersToMBase(model);
    }

    /** 
     *  Internal recursive worker to add UmlModelListener.
     */
    protected static void addListenersToMBase(MBase mbase) {
        mbase.addMElementListener(UmlModelListener.getInstance());
	Collection elements = mbase.getModelElementContents();
	if (elements != null) {
	    Iterator iterator = elements.iterator();
	    while(iterator.hasNext()) {
	        Object o = iterator.next();
	        if (o instanceof MBase) {
	            addListenersToMBase((MBase)o);
	        }
	    }
	}
    }
}

