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

package org.argouml.model.uml.behavioralelements.collaborations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.argouml.ui.ProjectBrowser;

import ru.novosoft.uml.behavior.collaborations.MClassifierRole;
import ru.novosoft.uml.foundation.core.MNamespace;

/**
 * Helper class for UML BehavioralElements::Collaborations Package.
 *
 * Current implementation is a placeholder.
 * 
 * @since ARGO0.11.2
 * @author Thierry Lach
 */
public class CollaborationsHelper {

    /** Don't allow instantiation.
     */
    private CollaborationsHelper() {
    }
    
     /** Singleton instance.
     */
    private static CollaborationsHelper SINGLETON =
                   new CollaborationsHelper();

    
    /** Singleton instance access method.
     */
    public static CollaborationsHelper getHelper() {
        return SINGLETON;
    }
    
    /**
	 * Returns all classifierroles found in the projectbrowser model
	 * @return Collection
	 */
	public Collection getAllClassifierRoles() {
		MNamespace model = ProjectBrowser.TheInstance.getProject().getModel();
		return getAllClassifierRoles(model);
	}
	
	/**
	 * Returns all classifierroles found in this namespace and in its children
	 * @return Collection
	 */
	public Collection getAllClassifierRoles(MNamespace ns) {
		Iterator it = ns.getOwnedElements().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof MNamespace) {
				list.addAll(getAllClassifierRoles((MNamespace)o));
			} 
			if (o instanceof MClassifierRole) {
				list.add(o);
			}
			
		}
		return list;
	}
}

