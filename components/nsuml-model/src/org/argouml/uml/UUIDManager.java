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

package org.argouml.uml;

import java.rmi.server.UID;
import java.net.InetAddress;
import java.util.*;

import org.apache.log4j.Category;

import ru.novosoft.uml.model_management.*;
import ru.novosoft.uml.foundation.core.*;
import ru.novosoft.uml.foundation.extension_mechanisms.*;
import ru.novosoft.uml.behavior.state_machines.*;
import ru.novosoft.uml.behavior.collaborations.*;
import ru.novosoft.uml.behavior.use_cases.*;

/** @stereotype singleton
 */
public class UUIDManager {
    protected static Category cat = Category.getInstance(UUIDManager.class);

    ////////////////////////////////////////////////////////////////
    // static variables
    
    public static UUIDManager SINGLETON = new UUIDManager();

    protected static InetAddress _address = null; 

    static {
        try {
            _address = InetAddress.getLocalHost(); 
        } catch (java.net.UnknownHostException e) {
            cat.fatal("ERROR: unable to get localhost information.", e);
            cat.fatal("On Unix systems this usually indicates that your /etc/hosts file is incorrectly setup.");
            cat.fatal("Stopping execution of ArgoUML.");
            System.exit(-1);
        }
    }
    
    ////////////////////////////////////////////////////////////////
    // constructors
    
    protected UUIDManager() { }
    
    ////////////////////////////////////////////////////////////////
    // public methods
    
    public synchronized String getNewUUID() {
		UID uid = new UID();
		String s = "";
		if (_address != null) {
			byte[] b = _address.getAddress();
			for (int i = 0; i < b.length; i++)
				s += (new Byte(b[i])).longValue() + "-";
		}
		s += uid.toString();
		cat.debug("new UUID: "+s);
		return s;
    }

    public synchronized void createModelUUIDS(MNamespace model) {
        
        cat.info("NOTE: The temporary method 'createModelUUIDs' has been called.");
        
        Collection ownedElements = model.getOwnedElements();
		Iterator oeIterator = ownedElements.iterator();
        
        String uuid = model.getUUID();
        if (uuid == null) model.setUUID(getNewUUID());

		while (oeIterator.hasNext()) {
            MModelElement me = (MModelElement) oeIterator.next();
            if (me instanceof MModel ||
                // me instanceof MNamespace ||
                me instanceof MClassifier ||
                me instanceof MFeature ||
                me instanceof MStateVertex ||
		me instanceof MStateMachine ||
                me instanceof MTransition ||
                me instanceof MCollaboration ||
		me instanceof MMessage ||
                me instanceof MAssociation ||
                me instanceof MAssociationEnd ||
                me instanceof MGeneralization ||
                me instanceof MDependency ||
                me instanceof MStereotype ||
		me instanceof MUseCase) {
                uuid = me.getUUID();
                if (uuid == null) {
                    me.setUUID(getNewUUID());
                }
            }
			//recursive handling of namespaces, needed for Collaborations
			if (me instanceof MNamespace) {
				cat.debug("Found another namespace: "+me);
				createModelUUIDS((MNamespace)me);
			}
        }
    }
  

} /* end class UUIDManager */

