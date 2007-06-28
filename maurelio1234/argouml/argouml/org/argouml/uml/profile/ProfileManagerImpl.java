// $Id: eclipse-argo-codetemplates.xml 11347 2006-10-26 22:37:44Z linus $
// Copyright (c) 2007 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies. This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason. IN NO EVENT SHALL THE
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

package org.argouml.uml.profile;

import java.util.Vector;

/**
 * Default <code>ProfileManager</code> implementation
 *
 * @author Marcos Aurélio
 */
public class ProfileManagerImpl implements ProfileManager {

    private static ProfileManager instance = null;
    private Vector profiles = new Vector();
    
    private ProfileManagerImpl() {

    }
    
    /**
     * @return the unique instance for this class
     */
    public static ProfileManager getInstance() {
	if (instance == null) {
	    instance = new ProfileManagerImpl();
	}
	return instance;
    }
    
    /**
     * @return the list of profiles
     * @see org.argouml.uml.profile.ProfileManager#getRegisteredProfiles()
     */
    public Vector getRegisteredProfiles() {
	return profiles;
    }

    /**
     * @param p the profile
     * @see org.argouml.uml.profile.ProfileManager#registerProfile(org.argouml.uml.profile.Profile)
     */
    public void registerProfile(Profile p) {
	profiles.add(p);
    }

    /**
     * @param p the profile
     * @see org.argouml.uml.profile.ProfileManager#removeProfile(org.argouml.uml.profile.Profile)
     */
    public void removeProfile(Profile p) {
	profiles.remove(p);
    }

}
