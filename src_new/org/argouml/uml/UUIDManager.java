// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.net.InetAddress;
import java.util.Collection;
import java.util.Iterator;
import java.net.UnknownHostException;
import java.rmi.server.UID;

import org.apache.log4j.Logger;
import org.argouml.application.security.ArgoSecurityManager;
import org.argouml.model.ModelFacade;

/**
 * @stereotype singleton
 */
public class UUIDManager {

    /**
     * The logger.
     */
    private static final Logger LOG = Logger.getLogger(UUIDManager.class);

    ////////////////////////////////////////////////////////////////
    // static variables
    
    private static final UUIDManager SINGLETON = new UUIDManager();

    private static InetAddress address = null; 

    static {
        try {
            address = InetAddress.getLocalHost(); 
        }
	catch (UnknownHostException e) {
            LOG.fatal("ERROR: unable to get localhost information.", e);
            LOG.fatal("On Unix systems this usually indicates that your "
		      + "/etc/hosts file is incorrectly setup.");
            LOG.fatal("Stopping execution of ArgoUML.");
            ArgoSecurityManager.getInstance().setAllowExit(true);
            System.exit(-1);
        }
    }
    
    ////////////////////////////////////////////////////////////////
    // constructors
    
    /**
     * Constructor for the UUIDManager. This is private to make sure that 
     * we are a proper singleton.
     *
     * @deprecated by Linus Tolke as of 0.15.4. Will be made private.
     * Use the UUIDManager singleton instead.
     */
    protected UUIDManager() { }
    
    /**
     * Return the UUIDManager.
     *
     * @return an UUIDManager
     */
    public static UUIDManager getInstance() {
	return SINGLETON;
    }

    ////////////////////////////////////////////////////////////////
    // public methods
    
    /**
     * @return the new uuid
     */
    public synchronized String getNewUUID() {
	UID uid = new UID();
	String s = "";
	if (address != null) {
	    byte[] b = address.getAddress();
	    for (int i = 0; i < b.length; i++)
		s += (new Byte(b[i])).longValue() + "-";
	}
	s += uid.toString();
//	LOG.debug("new UUID: " + s);
	return s;
    }

    /**
     * @param model is the model that we operate on.
     * @deprecated by Linus Tolke as of 0.15.4. I assume that the
     * person that wrote the "temporary method" info message meant
     * temporary method in the sense that it was implemented to
     * temporarily solve a problem in the code and that it would
     * eventually be removed when that problem was solved in some
     * other way. By deprecating it I am moving this knowledge to
     * compile time where it belongs, instead of having it in the log
     * file at run time.<p>
     *
     * If this assumption is wrong, then please explain what temporary
     * method means.
     */
    public synchronized void createModelUUIDS(Object model) {
        
        LOG.info("NOTE: The temporary method 'createModelUUIDs' "
		 + "has been called.");
        
        if (!ModelFacade.isANamespace(model)) {
            throw new IllegalArgumentException();
	}
        
        Collection ownedElements = ModelFacade.getOwnedElements(model);
	Iterator oeIterator = ownedElements.iterator();
        
        String uuid = ModelFacade.getUUID(model);
        if (uuid == null) {
	    ModelFacade.setUUID(model, getNewUUID());
	}

	while (oeIterator.hasNext()) {
            Object me = oeIterator.next();
            if (ModelFacade.isAModel(me)
                || ModelFacade.isAClassifier(me)
                || ModelFacade.isAFeature(me)
                || ModelFacade.isAStateVertex(me)
		|| ModelFacade.isAStateMachine(me)
                || ModelFacade.isATransition(me)
                || ModelFacade.isACollaboration(me)
		|| ModelFacade.isAMessage(me)
                || ModelFacade.isAAssociation(me)
                || ModelFacade.isAAssociationEnd(me)
                || ModelFacade.isAGeneralization(me)
                || ModelFacade.isADependency(me)
                || ModelFacade.isAStereotype(me)
		|| ModelFacade.isAUseCase(me)) {

                uuid = ModelFacade.getUUID(me);
                if (uuid == null) {
                    ModelFacade.setUUID(me, getNewUUID());
                }

            }
	    //recursive handling of namespaces, needed for Collaborations
	    if (ModelFacade.isANamespace(me)) {
		LOG.debug("Found another namespace: " + me);
		createModelUUIDS(me);
	    }
        }
    }
  

} /* end class UUIDManager */
